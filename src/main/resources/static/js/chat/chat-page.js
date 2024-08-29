document.addEventListener('DOMContentLoaded', () => {
    const socket = new SockJS('/Chat-ws');
    const stompClient = Stomp.over(socket);

    const chatForm = document.getElementById('chatForm');
    const messageInput = document.getElementById('messageInput');
    const basicUserNo = document.getElementById('basicUserNo').value;
    const matchingNo = document.getElementById('matchingNo').value;
    const messageContainer = document.getElementById('messageContainer');

    stompClient.connect({}, () => {
        console.log("Connected to WebSocket");
        stompClient.subscribe(`/Chat-topic/messages/${matchingNo}`, (message) => {
            console.log("Received message: ", message.body);
            try {
                const receivedMessage = JSON.parse(message.body);
                const profile = profiles.find(profile => profile.basicUserNo === receivedMessage.basicUserNo);
                const fullPhotoPath = profile ? (profile.photoPath + profile.photoName) : '/default-profile.png';
                displayMessage(
                    receivedMessage.content,
                    receivedMessage.basicUserNo === parseInt(basicUserNo) ? 'self' : 'other',
                    profile ? profile.nickname : 'Unknown',
                    fullPhotoPath
                );
            } catch (error) {
                console.error("Error processing received message: ", error);
            }
        });
    }, (error) => {
        console.error("WebSocket connection error: ", error);
    });

    chatForm.addEventListener('submit', (event) => {
        event.preventDefault();
        const messageContent = messageInput.value.trim();

        if (messageContent) {
            const chatMessage = {
                matchingNo: matchingNo,
                basicUserNo: basicUserNo,
                sender: basicUserNo,
                content: messageContent,
                messageType: 'CHAT'
            };
            stompClient.send(`/Chat-app/message`, {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
    });

    function displayMessage(content, sender, nickname, fullPhotoPath) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('chat-list-item2');
        messageElement.textContent = content;

        if (sender === 'self') {
            const selfChatDiv = document.createElement('li');
            selfChatDiv.classList.add('self-chat');

            const selfContentDiv = document.createElement('div');
            selfContentDiv.classList.add('self');
            selfContentDiv.textContent = content;

            const nicknameSpan = document.createElement('span');
            nicknameSpan.textContent = nickname;
            nicknameSpan.classList.add('nik-name');

            const profileImage = document.createElement('img');
            profileImage.src = fullPhotoPath || '/default-profile.png';
            profileImage.alt = "self profile";
            profileImage.classList.add('profile');

            selfChatDiv.appendChild(selfContentDiv);
            selfChatDiv.appendChild(nicknameSpan);
            selfChatDiv.appendChild(profileImage);

            messageContainer.appendChild(selfChatDiv);
        } else {
            const otherChatDiv = document.createElement('li');
            otherChatDiv.classList.add('other-chat');

            const otherContentDiv = document.createElement('div');
            otherContentDiv.classList.add('other');
            otherContentDiv.textContent = content;

            const nicknameSpan = document.createElement('span');
            nicknameSpan.textContent = nickname;
            nicknameSpan.classList.add('nik-name');

            const profileImage = document.createElement('img');
            profileImage.src = fullPhotoPath || '/default-profile.png';
            profileImage.alt = "other profile";
            profileImage.classList.add('profile');

            otherChatDiv.appendChild(profileImage);
            otherChatDiv.appendChild(nicknameSpan);
            otherChatDiv.appendChild(otherContentDiv);

            messageContainer.appendChild(otherChatDiv);
        }

        messageContainer.scrollTop = messageContainer.scrollHeight;
    }
});
