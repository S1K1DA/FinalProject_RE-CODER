var stompClient = null;

function setConnected(connected) {
    $("#send").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#msg").html("");
}

function connect() {
    if (stompClient !== null && stompClient.connected) {
        return;  // 이미 연결되어 있는 경우 연결 시도하지 않음
    }

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', function(message) {
            showMessage(message.body);
        });

        // 연결 후 웰컴 메시지 요청
        let userId = parseInt(document.getElementById('user-id').value); // userId를 int로 변환
        let welcomeMessage = { type: "welcome", content: "open", userId: userId };
        stompClient.send("/app/sendMessage", {}, JSON.stringify(welcomeMessage));

        // 여기에서 고정 메뉴를 초기화합니다.
        displayPersistentMenu();
    });
}



function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
        setConnected(false);
        console.log("Disconnected");
    }
    clearMessages(); // 메시지 박스 비우기
}

function sendMessage() {
    let userId = parseInt(document.getElementById('user-id').value); // userId를 int로 변환
    let message = $("#chatbot-input").val();
    let messagePayload = { content: message, userId: userId };
    showUserMessage(message); // 사용자 메시지 표시
    stompClient.send("/app/sendMessage", {}, JSON.stringify(messagePayload));
    $("#chatbot-input").val(''); // 입력 창 비우기
}

function showUserMessage(message) {
    const chatbotBody = document.querySelector('.chatbot-body');
    let userMessageElement = document.createElement('div');
    userMessageElement.className = 'chatbot-message chatbot-message-right';
    userMessageElement.textContent = message;
    chatbotBody.appendChild(userMessageElement);
    chatbotBody.scrollTop = chatbotBody.scrollHeight; // 스크롤을 아래로 자동 이동
}

function showMessage(message) {
    const chatbotBody = document.querySelector('.chatbot-body');

    const jsonStartIndex = message.indexOf('[[');
    if (jsonStartIndex !== -1) {
        const textMessage = message.substring(0, jsonStartIndex).trim();
        const jsonDataString = message.substring(jsonStartIndex);

        let botMessageElement = document.createElement('div');
        botMessageElement.className = 'chatbot-message chatbot-message-left';
        botMessageElement.textContent = textMessage;
        chatbotBody.appendChild(botMessageElement);

        try {
            const jsonData = JSON.parse(jsonDataString);
            if (jsonData && jsonData.length > 0) {
                jsonData.forEach(function(row) {
                    row.forEach(function(item) {
                        let buttonTitle = item.data.title;
                        let actionType = item.data.data.action.type;
                        let actionData = item.data.data.action.data;

                        let buttonContainer = document.createElement('div');

                        if (actionType === 'postback') {
                            let postback = actionData.postback;
                            buttonContainer.innerHTML = `<button class='option-button' onclick='handlePostback("${postback}")'>${buttonTitle}</button>`;
                        } else if (actionType === 'link') {
                            let url = actionData.url;
                            buttonContainer.innerHTML = `<button class='option-button' onclick='window.open("${url}", "_blank")'>${buttonTitle}</button>`;
                        }

                        chatbotBody.appendChild(buttonContainer);
                    });
                });
            }

        } catch (e) {
            console.log("JSON 데이터를 파싱하는데 문제가 발생했습니다 : ", e);
        }
    } else {
        let botMessageElement = document.createElement('div');
        botMessageElement.className = 'chatbot-message chatbot-message-left';
        botMessageElement.textContent = message;
        chatbotBody.appendChild(botMessageElement);
    }

    chatbotBody.scrollTop = chatbotBody.scrollHeight;
}

function displayPersistentMenu() {
    const persistentMenuContainer = document.getElementById('persistent-menu');

    // 홈으로 돌아가기 버튼을 텍스트로 표시
    persistentMenuContainer.innerHTML = `<button class='home-button' onclick='handlePostback("home_action")'>홈으로 돌아가기</button>`;
    persistentMenuContainer.classList.remove('hidden');
}

function handlePostback(postback) {
    console.log("Postback received: " + postback);

    let userId = parseInt(document.getElementById('user-id').value); // userId를 int로 변환

    if (postback === "home_action") {
        clearMessages();
        const postbackMessage = JSON.stringify({ postback: "open", userId: userId });
        stompClient.send("/app/sendMessage", {}, postbackMessage); // 웰컴 메시지를 요청
    } else {
        const postbackMessage = JSON.stringify({ postback: postback, userId: userId });
        stompClient.send("/app/sendMessage", {}, postbackMessage);
    }
}



function clearMessages() {
    const chatbotBody = document.querySelector('.chatbot-body');
    chatbotBody.innerHTML = ''; // 메시지 모두 지우기
}
