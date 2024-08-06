document.addEventListener('DOMContentLoaded', function() {
    const chatbotButton = document.getElementById('chatbot-button');
    const chatbotBox = document.getElementById('chatbot-box');
    const closeChatbot = document.getElementById('close-chatbot');

    // Show chatbot box when the button is clicked
    chatbotButton.addEventListener('click', function() {
        chatbotBox.classList.toggle('hidden');
    });

    // Hide chatbot box when close button is clicked
    closeChatbot.addEventListener('click', function() {
        chatbotBox.classList.add('hidden');
    });
});
