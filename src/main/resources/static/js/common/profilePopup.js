// profilePopup.js

(function() {
    // 프로필 팝업을 열기 위한 함수
    function openProfilePopup(likedUserNo, profilePopup, popupContent) {
        fetch(`/mypage/getProfileContent?likedUserNo=${likedUserNo}`)
            .then(response => response.json())
            .then(data => {
                if (data.status === "success") {
                    let selectedLikeCategories = data.likeCategories.filter(category => data.userSelectedCategories.includes(category.personalNo));
                    let selectedDislikeCategories = data.dislikeCategories.filter(category => data.userSelectedCategories.includes(category.personalNo));

                    let likeCategories = selectedLikeCategories.map(item => item.personalName).join(', ');
                    let dislikeCategories = selectedDislikeCategories.map(item => item.personalName).join(', ');
                    let hobbies = data.hobbies.map(item => item.hobbyName).join(', ');

                    // S3 URL이 포함된 이미지 URL 사용
                    let profileImageUrl = data.profilePictureUrl;

                    popupContent.innerHTML = `
                        <div style="display: flex;">
                            <div style="flex: 1;">
                                <img src="${profileImageUrl}" alt="프로필 사진" style="max-width: 250px;">
                            </div>
                            <div style="flex: 2; padding-left: 20px;">
                                <div style="display: flex; justify-content: flex-start;">
                                    <p style="width: 25%; text-align: right; font-weight: bold;">닉네임:</p>
                                    <p style="width: 75%; margin-left: 10px; text-align: left;">${data.nickname}</p>
                                </div>
                                <div style="display: flex; justify-content: flex-start;">
                                    <p style="width: 25%; text-align: right; font-weight: bold;">MBTI:</p>
                                    <p style="width: 75%; margin-left: 10px; text-align: left;">${data.mbti}</p>
                                </div>
                                <div style="display: flex; justify-content: flex-start;">
                                    <p style="width: 25%; text-align: right; font-weight: bold;">좋아하는 것:</p>
                                    <p style="width: 75%; margin-left: 10px; text-align: left;">${likeCategories}</p>
                                </div>
                                <div style="display: flex; justify-content: flex-start;">
                                    <p style="width: 25%; text-align: right; font-weight: bold;">싫어하는 것:</p>
                                    <p style="width: 75%; margin-left: 10px; text-align: left;">${dislikeCategories}</p>
                                </div>
                                <div style="display: flex; justify-content: flex-start;">
                                    <p style="width: 25%; text-align: right; font-weight: bold;">취미:</p>
                                    <p style="width: 75%; margin-left: 10px; text-align: left;">${hobbies}</p>
                                </div>
                                <div style="display: flex; justify-content: flex-start;">
                                    <p style="width: 25%; text-align: right; font-weight: bold;">좋아요 수:</p>
                                    <p style="width: 75%; margin-left: 10px; text-align: left;">${data.likeCount}</p>
                                </div>
                            </div>
                        </div>
                    `;
                } else {
                    popupContent.innerHTML = `<p>프로필 정보를 불러오는데 실패했습니다.</p>`;
                }
                profilePopup.style.display = 'block';
            })
            .catch(error => {
                console.error('Error fetching profile content:', error);
                popupContent.innerHTML = `<p>프로필 정보를 불러오는데 실패했습니다.</p>`;
                profilePopup.style.display = 'block';
            });
    }

    // 프로필 아이템 클릭 시 팝업 열기 기능 초기화
    function initializeProfileItemPopup() {
        const profileItems = document.querySelectorAll('.liked-prof-item');
        const profilePopup = document.getElementById('profile-popup');
        const popupContent = document.querySelector('.popup-content');

        profileItems.forEach(item => {
            item.addEventListener('click', function () {
                const likedUserNo = this.getAttribute('data-liked-user-no');
                openProfilePopup(likedUserNo, profilePopup, popupContent);
            });
        });
    }

    // 프로필 팝업 닫기 및 외부 클릭 시 팝업 닫기 기능 설정
    function initializeProfilePopupClose() {
        const profilePopup = document.getElementById('profile-popup');
        const closePopupButton = document.querySelector('.close-popup');

        if (profilePopup && closePopupButton) {
            closePopupButton.addEventListener('click', function () {
                profilePopup.style.display = 'none';
            });

            window.addEventListener('click', function (event) {
                if (event.target === profilePopup) {
                    profilePopup.style.display = 'none';
                }
            });
        }
    }

    // 전역으로 함수 노출
    window.ProfilePopupModule = {
        initializeProfileItemPopup: initializeProfileItemPopup,
        openProfilePopup: openProfilePopup,
        initializeProfilePopupClose: initializeProfilePopupClose
    };
})();
