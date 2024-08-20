document.addEventListener('DOMContentLoaded', function() {
    const chatbotButton = document.getElementById('chatbot-button');
    const chatbotBox = document.getElementById('chatbot-box');
    const closeChatbot = document.getElementById('close-chatbot');
    const sendMessageButton = document.getElementById('send-message');
    const chatbotInput = document.getElementById('chatbot-input');

    chatbotButton.addEventListener('click', function() {
        chatbotBox.classList.toggle('hidden');
        if (!chatbotBox.classList.contains('hidden')) {
            connect();  // 웹소켓 연결
        } else {
            disconnect();  // 웹소켓 연결 해제
        }
    });

    closeChatbot.addEventListener('click', function() {
        chatbotBox.classList.add('hidden');
        disconnect(); // 웹소켓 연결 해제
    });

    sendMessageButton.addEventListener('click', function() {
        if (chatbotInput.value.trim() !== "") {
            sendMessage();
        }
    });

    chatbotInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && chatbotInput.value.trim() !== "") {
            sendMessage();
        }
    });
});
