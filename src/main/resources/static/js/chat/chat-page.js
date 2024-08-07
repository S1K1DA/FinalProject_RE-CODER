document.addEventListener('DOMContentLoaded', () => {
    const messageContainer = document.getElementById('messageContainer');
    const chatForm = document.getElementById('chatForm');
    const messageInput = document.getElementById('messageInput');

    chatForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const message = messageInput.value;
        if (message) {
            displayMessage(message, 'self');
            messageInput.value = '';
        }
    });

    function displayMessage(message, sender) {
        const messageElement = document.createElement('li');
        messageElement.textContent = message;

        if (sender === 'self') {
            const selfChatDiv = document.createElement('div');
            selfChatDiv.classList.add('self-chat');

            const profileImage = document.createElement('img');
            profileImage.src = "/image/team_member/team_member_eunsik.jpg"; // 프로필 이미지 경로
            profileImage.alt = "self profile";
            profileImage.classList.add('profile');

            const nicknameSpan = document.createElement('span');
            nicknameSpan.textContent = "멋쟁이식이";
            nicknameSpan.classList.add('nik-name');

            selfChatDiv.appendChild(messageElement);
            selfChatDiv.appendChild(nicknameSpan);
            selfChatDiv.appendChild(profileImage);

            messageContainer.appendChild(selfChatDiv);
        } else {
            messageElement.classList.add('other');
            messageContainer.appendChild(messageElement);
        }

        messageContainer.scrollTop = messageContainer.scrollHeight;
    }
});
