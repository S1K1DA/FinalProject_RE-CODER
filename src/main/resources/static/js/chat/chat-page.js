document.addEventListener('DOMContentLoaded', () => {
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    const chatForm = document.getElementById('chatForm');
    const messageInput = document.getElementById('messageInput');
    const basicUserNo = document.getElementById('basicUserNo').value;
    const matchingNo = document.getElementById('matchingNo').value;
    const messageContainer = document.getElementById('messageContainer');

    // 페이지 로드 시 채팅창을 맨 아래로 스크롤
    scrollToBottom();

    // WebSocket 연결 설정 및 구독 설정
    stompClient.connect({}, () => {
        console.log("Connected to WebSocket");

        // 채팅 메시지 수신 처리
        stompClient.subscribe(`/topic/messages/${matchingNo}`, (message) => {
            console.log("Received message: ", message.body);
            try {
                const receivedMessage = JSON.parse(message.body);
                const profile = profiles.find(profile => profile.basicUserNo === receivedMessage.basicUserNo);
                const fullPhotoPath = profile ? (profile.photoPath) : '/image/mypage/icon_users.png';
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

        // lastMessage 업데이트 수신 처리
        stompClient.subscribe("/topic/lastMessage", (updatedChatsMessage) => {
            console.log("Received updated chats: ", updatedChatsMessage.body);
            const updatedChats = JSON.parse(updatedChatsMessage.body);
            updateChatListUI(updatedChats);
        });

    }, (error) => {
        console.error("WebSocket connection error: ", error);
    });

    // 메시지 전송 처리
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
            stompClient.send(`/app/message`, {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
    });

    // 수신된 메시지를 채팅 창에 표시
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
            profileImage.src = fullPhotoPath || '/image/mypage/icon_users.png';
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
            profileImage.src = fullPhotoPath || '/image/mypage/icon_users.png';
            profileImage.alt = "other profile";
            profileImage.classList.add('profile');

            otherChatDiv.appendChild(profileImage);
            otherChatDiv.appendChild(nicknameSpan);
            otherChatDiv.appendChild(otherContentDiv);

            messageContainer.appendChild(otherChatDiv);
        }

        // 새 메시지가 도착했을 때 스크롤을 맨 아래로 이동
        scrollToBottom();
    }

    // 채팅 목록 UI 업데이트 (lastMessage 업데이트 반영)
    function updateChatListUI(updatedChats) {
        const chatListItems = document.querySelectorAll('.chat-list-item');
        updatedChats.forEach(updatedChat => {
            chatListItems.forEach(item => {
                const matchingNo = item.getAttribute('data-matchingNo');
                if (parseInt(matchingNo) === updatedChat.matchingNo) {
                    const lastMessageSpan = item.querySelector('.last-message');

                    // lastMessage 길이 체크 후 업데이트
                    let displayMessage = updatedChat.lastMessage;
                    if (displayMessage.length > 6) {
                        displayMessage = displayMessage.substring(0, 6) + '...';
                    }

                    lastMessageSpan.textContent = displayMessage;
                }
            });
        });
    }

    // 채팅창을 맨 아래로 스크롤하는 함수
    function scrollToBottom() {
        messageContainer.scrollTop = messageContainer.scrollHeight;
    }
});

// 신고하기 팝업 열기
function openChatReportPopup(button) {
    const chatItem = button.closest('.chat-list-item');
    const matchingNo = chatItem.getAttribute('data-matchingNo');  // 해당 채팅의 매칭 넘버
    const reportedUserNo = chatItem.getAttribute('data-basicUserNo'); // 신고 대상 사용자 번호
    const reportedUserNickname = chatItem.querySelector('.nik-name').textContent;  // 신고 대상 사용자 닉네임

    console.log("Matching No:", matchingNo);
    console.log("Reported User No:", reportedUserNo);
    console.log("Reported User Nickname:", reportedUserNickname);

    // 팝업에 신고 대상 정보 설정
    document.getElementById('matchingNo1').value = matchingNo;
    document.getElementById('reportedUserNo').value = reportedUserNo;
    document.getElementById('reportTypeNo').value = matchingNo;
    document.getElementById('reportedUser').textContent = reportedUserNickname;

    // 신고 팝업 열기
    document.getElementById('reportOverlay').style.display = 'flex';
}

// 신고 팝업 닫기
document.getElementById('actionCloses').addEventListener('click', function() {
    document.getElementById('reportOverlay').style.display = 'none';
});

// 신고하기 폼 제출
document.getElementById('reportForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const formData = new FormData(this);



    try {
        const result = await Swal.fire({
            title: "신고하시겠습니까?",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "rgb(255 128 135)",
            cancelButtonColor: "rgb(150 150 150)",
            confirmButtonText: "예",
            cancelButtonText: "아니요"
        });

        if (result.isConfirmed) {
            const response = await fetch(this.action, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                await Swal.fire({
                    title: "신고 완료",
                    icon: "success"
                }).then(() => {
                    window.location.reload();
                });
            } else {
                console.error("서버 오류: " + response.status);
            }
        }
    } catch (error) {
        console.error(error);
        await Swal.fire({
            title: "오류 발생",
            text: "신고 처리 중 오류가 발생했습니다.",
            icon: "error"
        });
    }
});

