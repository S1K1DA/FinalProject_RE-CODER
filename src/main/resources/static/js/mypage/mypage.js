document.addEventListener('DOMContentLoaded', function () {
    const maxSentiSelections = 3;
    const maxHobbySelections = 5;
    let isNicknameValid = false; // 전역 변수로 상태 유지

    // 성향 체크박스 처리
    handleCheckboxSelection('input[name="likes"]', maxSentiSelections, `최대 ${maxSentiSelections}개까지 선택할 수 있습니다.`);
    handleCheckboxSelection('input[name="dislikes"]', maxSentiSelections, `최대 ${maxSentiSelections}개까지 선택할 수 있습니다.`);
    handleCheckboxSelection('input[name="hobbies"]', maxHobbySelections, `취미는 최대 ${maxHobbySelections}개까지 선택할 수 있습니다.`);

    // 좋아요 하트 아이콘 클릭 처리 (feed 관련 기능 추가)
    setupHeartIconToggle('.feed-heart', toggleFeedLike);

    // 팝업 처리
    setupPopup();

    // 프로필 사진 업로드 처리
    setupProfileImageUpload();

    // 프로필 좋아요 기능 초기화
    ProfileLikeModule.initializeProfileLikeFeature();
    // 프로필 팝업 기능 초기화
    ProfilePopupModule.initializeProfileItemPopup();
    //프로필 팝업 닫기
    ProfilePopupModule.initializeProfilePopupClose();

    // 피드 아이템 클릭 시 팝업 열기 초기화
    initializeFeedItemPopup();

    // 피드 팝업 닫기 및 외부 클릭 시 닫기 설정
    initializeFeedPopupClose();
});

// 하트 아이콘 클릭 처리 함수 (feed 관련 기능 추가)
function setupHeartIconToggle(selector, toggleFunction) {
    const heartIcons = document.querySelectorAll(selector);

    heartIcons.forEach(heart => {
        heart.addEventListener('click', function(event) {
            event.stopPropagation();
            toggleFunction(this);
        });
    });
}

// 피드 좋아요 토글 함수
function toggleFeedLike(element) {
    const feedNo = element.closest('.liked-feed-item').getAttribute('data-feed-no');
    const isLiked = element.classList.contains('liked');

    if (isLiked) {
        sendLikeRequest('/mypage/unlikeFeed', { feedNo }, element, false);
    } else {
        sendLikeRequest('/mypage/likeFeed', { feedNo }, element, true);
    }
}

// 좋아요 요청을 보내는 함수
function sendLikeRequest(url, data, element, isLiking) {
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (response.ok) {
            if (isLiking) {
                element.classList.add('liked');
            } else {
                element.classList.remove('liked');
            }
        } else {
            Swal.fire({
                icon: 'error',
                title: '실패',
                text: isLiking ? '좋아요 추가에 실패했습니다.' : '좋아요 해제에 실패했습니다.',
            });
        }
    });
}

// 피드 팝업 열기 함수
function openFeedPopup(feedNo, feedPopup, popupContent) {
    fetch(`/mypage/getFeedContent?feedNo=${feedNo}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === "success") {
                const formattedDate = new Date(data.feedIndate).toLocaleString('ko-KR', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit',
                    hour: '2-digit',
                    minute: '2-digit'
                });

                popupContent.innerHTML = `
                    <div class='feed-out-box'>
                        <div class='feed-head-box'>
                            <div class='feed-title-box'>
                                <p class='feed-title'>${data.feedTitle}</p>
                            </div>
                            <div class='feed-icon-box'>
                                <div class='feed-meta'>
                                    <p class='feed-nickname'>${data.author}</p>
                                    <p class='feed-like-cnt'>좋아요 수: ${data.likeCount}</p>
                                </div>
                            </div>
                            <div class='feed-remark'>
                                <div class='feed-meta'>
                                    <p class='feed-tag'>${data.feedTag}</p>
                                    <p class='feed-indate'>${formattedDate}</p>
                                </div>
                            </div>
                        </div>
                        <hr>
                        <div class='feed-content-box'>
                            <span class='feed-content'>${decodeHtml(data.feedContent)}</span>
                        </div>
                    </div>
                `;
            } else {
                popupContent.innerHTML = `<p>${data.message}</p>`;
            }
            feedPopup.style.display = 'block';
        })
        .catch(error => {
            console.error('Error fetching feed content:', error);
            popupContent.innerHTML = `<p>Error loading feed content</p>`;
            feedPopup.style.display = 'block';
        });
}

// 피드 아이템 클릭 시 팝업 열기 초기화
function initializeFeedItemPopup() {
    const feedItems = document.querySelectorAll('.liked-feed-item');
    const feedPopup = document.getElementById('feed-popup');
    const popupContent = document.getElementById('popup-feed-content');

    feedItems.forEach(item => {
        item.addEventListener('click', function() {
            const feedNo = this.getAttribute('data-feed-no');
            openFeedPopup(feedNo, feedPopup, popupContent);
        });
    });
}

// 피드 팝업 닫기 및 외부 클릭 시 닫기 설정
function initializeFeedPopupClose() {
    const feedPopup = document.getElementById('feed-popup');
    const closePopupButton = document.querySelector('.close-popup');

    if (feedPopup && closePopupButton) {
        closePopupButton.addEventListener('click', function() {
            feedPopup.style.display = 'none';
        });

        window.addEventListener('click', function(event) {
            if (event.target === feedPopup) {
                feedPopup.style.display = 'none';
            }
        });
    }
}

// 체크박스 선택 처리 함수
function handleCheckboxSelection(selector, maxSelections, warningText) {
    const checkboxes = document.querySelectorAll(selector);

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            if (document.querySelectorAll(`${selector}:checked`).length > maxSelections) {
                checkbox.checked = false;
                Swal.fire({
                    icon: 'warning',
                    title: '선택 초과',
                    text: warningText,
                });
            }
        });
    });
}

// 팝업 설정 함수
function setupPopup() {
    const openPopupButton = document.querySelector('.btn-edit-trigger');
    const popup = document.getElementById('passwordPopup');
    const closePopupButton = document.getElementById('closePopup');
    const submitPasswordButton = document.getElementById('submitPassword');
    const passwordInput = document.getElementById('passwordInput');

    if (openPopupButton && popup && closePopupButton && submitPasswordButton && passwordInput) {
        openPopupButton.addEventListener('click', function () {
            popup.style.display = 'flex';
            passwordInput.focus();
            centerPopup(popup);
        });

        closePopupButton.addEventListener('click', function () {
            popup.style.display = 'none';
        });

        submitPasswordButton.addEventListener('click', function () {
            handlePasswordSubmission(popup, passwordInput);
        });

        passwordInput.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                submitPasswordButton.click();
            }
        });
    }

    window.addEventListener('resize', function() {
        if (popup.style.display === 'flex') {
            centerPopup(popup);
        }
    });
}

// 팝업을 화면 중앙에 위치시키는 함수
function centerPopup(popup) {
    const rect = popup.getBoundingClientRect();
    popup.style.top = `${window.innerHeight / 2 - rect.height / 2}px`;
    popup.style.left = `${window.innerWidth / 2 - rect.width / 2}px`;
}

// 비밀번호 제출 처리 함수
function handlePasswordSubmission(popup, passwordInput) {
    const password = passwordInput.value;
    if (password) {
        fetch('/mypage/validatePassword', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ password: password })
        }).then(response => response.json())
            .then(data => {
                if (data.valid) {
                    window.location.href = '/mypage/infoedit';
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: '오류',
                        text: '비밀번호가 일치하지 않습니다.',
                    });
                    passwordInput.value = ''; // 비밀번호 틀렸을 시 비워줍니다.
                    popup.style.display = 'none';
                }
            });
    } else {
        Swal.fire({
            icon: 'warning',
            title: '경고',
            text: '비밀번호를 입력해주세요.',
        });
    }
}

// 프로필 사진 업로드 처리 함수
function setupProfileImageUpload() {
    const newFileButton = document.querySelector('.btn-new-file');
    if (newFileButton) {
        newFileButton.addEventListener('click', function () {
            const fileInput = document.createElement('input');
            fileInput.type = 'file';
            fileInput.accept = 'image/*';
            fileInput.onchange = function (event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        document.querySelector('.profile-picture img').src = e.target.result;
                    };
                    reader.readAsDataURL(file);

                    const formData = new FormData();
                    formData.append('file', file);

                    fetch('/mypage/profile-image-upload', {
                        method: 'POST',
                        body: formData
                    })
                    .then(response => response.text())
                    .then(data => {
                        document.querySelector('input[name="profilePicturePath"]').value = data;
                    })
                    .catch(error => console.error('Error:', error));
                }
            };
            fileInput.click();
        });
    }
}

// HTML 디코딩 함수 (필요시 사용)
function decodeHtml(html) {
    const txt = document.createElement("textarea");
    txt.innerHTML = html;
    return txt.value;
}
