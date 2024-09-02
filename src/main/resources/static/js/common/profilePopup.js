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

                    let profileImageUrl = data.profilePictureUrl;
                    let likeIconClass = data.isLiked ? 'prof-heart-icon liked' : 'prof-heart-icon';

                    popupContent.innerHTML = `
                        <div style="display: flex;">
                            <div style="flex: 1; position: relative;">
                                <img src="${profileImageUrl}" alt="프로필 사진" style="max-width: 250px; display: block;">
                                <a href="#" class="prof-heart ${data.isLiked ? 'liked' : ''}" style="position: absolute; top: 10px; right: 10px; cursor: pointer; width:30px; height:30px; display: flex; justify-content: center; align-items: center;">
                                    <i class="bi-heart ${likeIconClass}" style="font-size: 20px;"></i>
                                </a>
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

                    const likeIcon = popupContent.querySelector('.prof-heart-icon');
                    const heartElement = popupContent.querySelector('.prof-heart');

                    // 좋아요 아이콘 클릭 이벤트 추가
                    likeIcon.addEventListener('click', function(event) {
                        event.stopPropagation();
                        ProfileLikeModule.toggleProfileLike(heartElement, likedUserNo);
                    });

                    // 좋아요 상태에 따라 스타일 적용
                    if (data.isLiked) {
                        heartElement.classList.add('liked');
                    } else {
                        heartElement.classList.remove('liked');
                    }
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

                // 현재 페이지가 mypage/proflike인지 확인
                if (window.location.pathname.includes('/mypage/proflike')) {
                    const mainPageHeart = document.querySelector(`.liked-prof-item[data-liked-user-no="${likedUserNo}"] .prof-heart`);
                    if (mainPageHeart) {
                        if (!isLiked) {
                            mainPageHeart.classList.remove('liked'); // 좋아요 해제
                        } else {
                            mainPageHeart.classList.add('liked'); // 좋아요 설정
                        }
                    }
                }
            }
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
                popupContent.setAttribute('data-liked-user-no', likedUserNo); // likedUserNo를 저장
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
