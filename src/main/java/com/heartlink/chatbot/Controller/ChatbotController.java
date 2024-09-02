package com.heartlink.chatbot.Controller;

import com.heartlink.chatbot.model.dto.ChatbotDto;
import com.heartlink.chatbot.model.service.ChatbotService;
import com.heartlink.member.util.JwtUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

@Controller
public class ChatbotController {

    private static String apiUrl;
    private static String secretKey;

    private final JwtUtil jwtUtil;
    private final ChatbotService chatbotService;

    public ChatbotController(JwtUtil jwtUtil, ChatbotService chatbotService) {
        this.jwtUtil = jwtUtil;
        this.chatbotService = chatbotService;
    }

    static {
        try {
            Properties properties = new Properties();
            FileInputStream input = new FileInputStream("src/main/resources/apikeys.properties");
            properties.load(input);
            apiUrl = properties.getProperty("apiUrl");
            secretKey = properties.getProperty("secretKey");
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // SecurityContext에서 userId 가져오기
    private int getCurrentUserNo() {
        try {
            String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            return jwtUtil.getUserNumberFromToken(jwt);
        } catch (Exception e) {
            return 0;  // 로그인하지 않은 경우 0 반환
        }
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public String sendMessage(@Payload String chatMessage) throws IOException {
        JSONParser jsonParser = new JSONParser();
        JSONObject messageJson = null;
        boolean isPostback = false;
        int userId = getCurrentUserNo();

        try {
            messageJson = (JSONObject) jsonParser.parse(chatMessage);
            if (messageJson.containsKey("userId")) {
                userId = ((Long) messageJson.get("userId")).intValue(); // 메시지에서 userId 가져오기
            }
            isPostback = messageJson.containsKey("postback");
        } catch (Exception e) {
            System.out.println("Error parsing received message: " + e.getMessage());
        }

        String messageToSend = isPostback ? (String) messageJson.get("postback") : (String) messageJson.get("content");

        URL url = new URL(apiUrl);
        String message = getReqMessage(messageToSend, userId);  // userId 포함
        String encodeBase64String = makeSignature(message, secretKey);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json;UTF-8");
        con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(message.getBytes("UTF-8"));
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        String chatResponse = "";

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder jsonString = new StringBuilder();
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                jsonString.append(decodedString);
            }

            System.out.println("API Response: " + jsonString.toString());

            JSONParser jsonparser = new JSONParser();
            try {
                JSONObject json = (JSONObject) jsonparser.parse(jsonString.toString());

                // Extract scenarioName from the response and use it as the tag
                String scenarioName = "";
                if (json.containsKey("scenario")) {
                    JSONObject scenario = (JSONObject) json.get("scenario");
                    scenarioName = (String) scenario.get("name");
                }

                // Save inquiry with the extracted tag only if scenarioName is not null or empty
                if (scenarioName != null && !scenarioName.isEmpty()) {
                    saveChatbotInquiry(userId, scenarioName);
                }

                String version = (String) json.get("version");
                JSONArray bubblesArray = (JSONArray) json.get("bubbles");

                if (bubblesArray != null && !bubblesArray.isEmpty()) {
                    JSONObject bubbles = (JSONObject) bubblesArray.get(0);
                    String type = (String) bubbles.get("type");

                    if ("template".equals(type)) {
                        if ("v1".equals(version)) {
                            chatResponse = handleV1Form(bubbles);
                        } else if ("v2".equals(version)) {
                            chatResponse = handleV2Form(bubbles);
                        }
                    } else if ("text".equals(type)) {
                        JSONObject data = (JSONObject) bubbles.get("data");
                        chatResponse = data.get("description").toString();
                    }
                } else {
                    chatResponse = "Bubble Array의 값이 null입니다";
                }
            } catch (Exception e) {
                System.out.println("JSON 파싱에 오류가 발생했습니다. ");
                e.printStackTrace();
                chatResponse = "JSON 파싱에 오류가 발생했습니다.";
            }

            in.close();
        } else {
            chatResponse = con.getResponseMessage();
        }

        return chatResponse;
    }

    private void saveChatbotInquiry(int userId, String scenarioName) {
        ChatbotDto inquiryDto = new ChatbotDto();
        inquiryDto.setUserId(userId);
        inquiryDto.setChatbotInquiryTag(scenarioName);  // Extracted tag from the response

        chatbotService.saveChatbotInquiry(inquiryDto);
    }

    private String handleV1Form(JSONObject bubbles) {
        JSONObject data = (JSONObject) bubbles.get("data");
        JSONObject cover = (JSONObject) data.get("cover");
        JSONObject coverData = (JSONObject) cover.get("data");
        String chatResponse = coverData.get("description").toString();

        JSONArray contentTable = (JSONArray) data.get("contentTable");
        if (contentTable != null) {
            chatResponse += "\n" + contentTable.toJSONString();
        }

        return chatResponse;
    }

    private String handleV2Form(JSONObject bubbles) {
        JSONObject data = (JSONObject) bubbles.get("data");
        JSONObject cover = (JSONObject) data.get("cover");
        JSONObject coverData = (JSONObject) cover.get("data");
        String chatResponse = coverData.get("description").toString();

        JSONArray contentTable = (JSONArray) data.get("contentTable");
        if (contentTable != null) {
            chatResponse += "\n" + contentTable.toJSONString();
        }

        return chatResponse;
    }

    public static String makeSignature(String message, String secretKey) {
        String encodeBase64String = "";
        try {
            byte[] secrete_key_bytes = secretKey.getBytes("UTF-8");
            SecretKeySpec signingKey = new SecretKeySpec(secrete_key_bytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.encodeBase64String(rawHmac);
            return encodeBase64String;

        } catch (Exception e){
            System.out.println(e);
        }
        return encodeBase64String;
    }

    public String getReqMessage(String messageContent, int userId) {
        String requestBody = "";
        try {
            // JSON 객체 생성 및 메시지 구성
            JSONObject obj = new JSONObject();
            long timestamp = new Date().getTime();
            obj.put("version", "v2");
            obj.put("userId", userId);
            obj.put("timestamp", timestamp);

            JSONObject bubbles_obj = new JSONObject();
            JSONObject data_obj = new JSONObject();

            bubbles_obj.put("type", "text");
            data_obj.put("description", messageContent);

            bubbles_obj.put("data", data_obj);
            JSONArray bubbles_array = new JSONArray();
            bubbles_array.add(bubbles_obj);

            obj.put("bubbles", bubbles_array);
            obj.put("event", "send");

            requestBody = obj.toString();
        } catch (Exception e) {
            System.out.println("## Exception : " + e);
        }

        return requestBody;
    }
}
