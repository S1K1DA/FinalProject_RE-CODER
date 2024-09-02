(function() {
    // 프로필 좋아요 요청 함수
    function sendLikeRequest(url, data, element, isLiking) {
        return fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                return true;
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '실패',
                    text: isLiking ? '좋아요 추가에 실패했습니다.' : '좋아요 해제에 실패했습니다.',
                });
                return false;
            }
        }).catch(error => {
            console.error("Error in sendLikeRequest:", error);
            return false;
        });
    }

    // 프로필 좋아요 토글 함수
    function toggleProfileLike(element, likedUserNo = null) {
        if (!likedUserNo) {
            likedUserNo = element.closest('.liked-prof-item')
                ? element.closest('.liked-prof-item').getAttribute('data-liked-user-no')
                : element.closest('.popup-content').getAttribute('data-liked-user-no');
        }

        const isLiked = element.classList.contains('liked');
        const url = isLiked ? '/mypage/unlikeProfile' : '/mypage/likeProfile';

        sendLikeRequest(url, { likedUserNo }, element, !isLiked).then(success => {
            if (success) {
                element.classList.toggle('liked', !isLiked); // 좋아요 상태를 토글
            }
        });
    }

    // 하트 아이콘 클릭 처리 함수
    function setupHeartIconToggle(selector, toggleFunction) {
        const heartIcons = document.querySelectorAll(selector);

        heartIcons.forEach(heart => {
            heart.addEventListener('click', function(event) {
                event.stopPropagation();
                toggleFunction(this);
            });
        });
    }

    // 프로필 좋아요 기능 초기화 함수
    function initializeProfileLikeFeature() {
        setupHeartIconToggle('.prof-heart', toggleProfileLike);
    }

    // 전역으로 함수 노출
    window.ProfileLikeModule = {
        initializeProfileLikeFeature: initializeProfileLikeFeature,
        toggleProfileLike: toggleProfileLike,
        sendLikeRequest: sendLikeRequest,
        setupHeartIconToggle: setupHeartIconToggle
    };
})();
