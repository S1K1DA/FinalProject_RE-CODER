document.addEventListener('DOMContentLoaded', () => {
    const chatListContainer = document.querySelector('.chat-list');

    // 채팅 중인 사용자 목록을 가져오는 함수
    function fetchActiveChats() {
        fetch('/chat/activeChats')
            .then(response => response.json())
            .then(data => {
                data.forEach(chat => {
                    const listItem = document.createElement('li');
                    listItem.classList.add('chat-list-item');

                    // 프로필 이미지
                    const profileImage = document.createElement('img');
                    profileImage.src = chat.photoPath;  // 서버에서 가져온 프로필 이미지 경로
                    profileImage.alt = "profile";
                    profileImage.classList.add('profile');

                    // 채팅 정보
                    const chatInfo = document.createElement('div');
                    chatInfo.classList.add('chat-info');

                    const nicknameSpan = document.createElement('span');
                    nicknameSpan.classList.add('nik-name');
                    nicknameSpan.textContent = chat.nickname;  // 서버에서 가져온 닉네임

                    const lastMessageSpan = document.createElement('span');
                    lastMessageSpan.classList.add('last-message');
                    lastMessageSpan.textContent = chat.lastMessage;  // 서버에서 가져온 마지막 메시지

                    chatInfo.appendChild(nicknameSpan);
                    chatInfo.appendChild(lastMessageSpan);

                    listItem.appendChild(profileImage);
                    listItem.appendChild(chatInfo);

                    chatListContainer.appendChild(listItem);
                });
            })
            .catch(error => console.error('Error fetching active chats:', error));
    }

    // 페이지 로드 시 채팅 목록 불러오기
    fetchActiveChats();
});
