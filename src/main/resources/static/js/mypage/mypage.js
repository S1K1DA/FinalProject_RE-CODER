document.addEventListener('DOMContentLoaded', function () {
    const maxSentiSelections = 3;
    const maxHobbySelections = 5;
    let isNicknameValid = false; // 전역 변수로 상태 유지

    // 성향 체크박스 처리
    handleCheckboxSelection('input[name="likes"]', maxSentiSelections, `최대 ${maxSentiSelections}개까지 선택할 수 있습니다.`);
    handleCheckboxSelection('input[name="dislikes"]', maxSentiSelections, `최대 ${maxSentiSelections}개까지 선택할 수 있습니다.`);
    handleCheckboxSelection('input[name="hobbies"]', maxHobbySelections, `취미는 최대 ${maxHobbySelections}개까지 선택할 수 있습니다.`);

    // 좋아요 하트 아이콘 클릭 처리
    setupHeartIconToggle('.feed-heart', toggleFeedLike);
    setupHeartIconToggle('.prof-heart', toggleProfileLike);

    // 팝업 처리
    setupPopup();

    // 주소 나누기 및 결합 처리 (특정 페이지에서만 실행)
    if (document.getElementById('user-address-line1')) {
        splitAddressIntoTwoFields();
        setupAddressForm();
    }

    // 탈퇴 폼 처리
    setupDeleteForm();

    // MBTI 검사 열기
    const mbtiTestButton = document.getElementById('openMbtiTest');
    if (mbtiTestButton) {
        mbtiTestButton.addEventListener('click', openMbtiTest);
    }

    // 닉네임 중복 체크 초기화 (특정 페이지에서만 실행)
    if (document.getElementById('user-nickname')) {
        initializeNicknameCheck();
    }

    const feedPopup = document.getElementById('feed-popup');

    if (feedPopup) {
        initializeFeedItemPopup();
        setupPopupCloseHandlers();
    }

    // 프로필 사진 업로드 처리
    setupProfileImageUpload();
});


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

                    // 업로드할 파일을 서버에 전송
                    const formData = new FormData();
                    formData.append('file', file);

                    fetch('/mypage/profile-image-upload', {
                        method: 'POST',
                        body: formData
                    })
                    .then(response => response.text())
                    .then(data => {
                        // 서버에서 반환된 이미지 URL을 hidden 필드에 저장
                        document.querySelector('input[name="profilePicturePath"]').value = data;
                    })
                    .catch(error => console.error('Error:', error));
                }
            };
            fileInput.click();
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

// 피드 좋아요 토글 함수
function toggleFeedLike(element) {
    const feedNo = element.closest('.liked-feed-item').getAttribute('data-feed-no');
    const isLiked = element.classList.contains('liked');

    if (isLiked) {
        // 좋아요 해제
        sendLikeRequest('/mypage/unlikeFeed', { feedNo }, element, false);
    } else {
        // 좋아요 추가
        sendLikeRequest('/mypage/likeFeed', { feedNo }, element, true);
    }
}

// 프로필 좋아요 토글 함수
function toggleProfileLike(element) {
    const likedUserNo = element.closest('.liked-prof-item').getAttribute('data-liked-user-no');
    const isLiked = element.classList.contains('liked');

    if (isLiked) {
        // 프로필 좋아요 해제
        sendLikeRequest('/mypage/unlikeProfile', { likedUserNo }, element, false);
    } else {
        // 프로필 좋아요 추가
        sendLikeRequest('/mypage/likeProfile', { likedUserNo }, element, true);
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

// 팝업을 화면 중앙에 위치시키는 함수
function centerPopup(popup) {
    const rect = popup.getBoundingClientRect();
    popup.style.top = `${window.innerHeight / 2 - rect.height / 2}px`;
    popup.style.left = `${window.innerWidth / 2 - rect.width / 2}px`;
}

// 탈퇴 폼 처리 함수
function setupDeleteForm() {
    const deleteForm = document.querySelector('form[action="/mypage/delete"]');
    if (deleteForm) {
        deleteForm.addEventListener('submit', function(event) {
            event.preventDefault();
            handleDeleteFormSubmission(deleteForm);
        });
    }
}

// 탈퇴 폼 제출 처리 함수
function handleDeleteFormSubmission(deleteForm) {
    const password = document.getElementById('password').value;
    const passwordConfirm = document.getElementById('password-confirm').value;

    if (!password || !passwordConfirm) {
        Swal.fire({
            icon: 'warning',
            title: '경고',
            text: '비밀번호와 비밀번호 확인을 입력해주세요.',
        });
        return;
    }

    if (password !== passwordConfirm) {
        Swal.fire({
            icon: 'error',
            title: '오류',
            text: '비밀번호 확인이 일치하지 않습니다.',
        });
        return;
    }

    fetch('/mypage/validatePassword', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ password: password })
    }).then(response => response.json())
        .then(data => {
            if (data.valid) {
                deleteForm.submit();
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '오류',
                    text: '입력한 비밀번호가 현재 비밀번호와 일치하지 않습니다.',
                });
            }
        });
}

// 닉네임 중복 체크 초기화
function initializeNicknameCheck() {
    const nicknameInput = document.getElementById('user-nickname');
    const checkNicknameBtn = document.getElementById('check-nickname-btn');
    const updateBtn = document.getElementById('update-btn');

    // 현재 저장된 닉네임
    const originalNickname = nicknameInput.value.trim();

    // 닉네임 입력 시 유효성 초기화
    nicknameInput.addEventListener('input', function () {
        isNicknameValid = false; // 닉네임이 변경될 때마다 중복 체크가 필요하므로 무효화
    });

    // 닉네임 중복 체크 버튼 클릭 처리
    checkNicknameBtn.addEventListener('click', function () {
        checkNicknameAvailability(nicknameInput.value.trim(), originalNickname);
    });

    // 폼 제출 시 중복 체크 유효성 검사
    updateBtn.addEventListener('click', function (event) {
        validateBeforeSubmit(event, nicknameInput.value.trim(), originalNickname);
    });
}

// 닉네임 중복 체크 처리 함수
function checkNicknameAvailability(nickname, originalNickname) {
    if (nickname === '') {
        Swal.fire({
            icon: 'warning',
            title: '경고',
            text: '닉네임을 입력해주세요.',
        });
        return;
    }

    // 닉네임이 변경되지 않았을 경우 유효성 검사를 하지 않음
    if (nickname === originalNickname) {
        isNicknameValid = true;
        Swal.fire({
            icon: 'info',
            title: '알림',
            text: '현재 사용 중인 닉네임입니다.',
        });
        return;
    }

    // 닉네임 중복 체크 요청
    fetch(`/mypage/checkNickname`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ nickname: nickname })
    }).then(response => response.json())
        .then(data => {
            if (data.isUnique) {
                Swal.fire({
                    icon: 'success',
                    title: '확인 완료',
                    text: '사용 가능한 닉네임입니다.',
                });
                isNicknameValid = true;
            } else {
                Swal.fire({
                    icon: 'error',
                    title: '중복 닉네임',
                    text: '이미 사용 중인 닉네임입니다.',
                });
                isNicknameValid = false;
            }
        });
}

// 닉네임 제출 전 유효성 검사
function validateBeforeSubmit(event, nickname, originalNickname) {
    if (nickname !== originalNickname && !isNicknameValid) {
        event.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: '중복 체크 필요',
            text: '닉네임 중복 체크를 진행해 주세요.',
        });
    }
}

// 주소 필드를 두 개로 나누기
function splitAddressIntoTwoFields() {
    const fullAddress = document.getElementById('user-full-address').value;

    if (fullAddress) {
        const addressParts = fullAddress.split(' ');
        const line1 = addressParts.slice(0, 3).join(' ');
        const line2 = addressParts.slice(3).join(' ');

        document.getElementById('user-address-line1').value = line1;
        document.getElementById('user-address-line2').value = line2;
    }
}

// 주소 처리 폼 설정 함수
function setupAddressForm() {
    document.querySelector('form').addEventListener('submit', function() {
        const line1 = document.getElementById('user-address-line1').value;
        const line2 = document.getElementById('user-address-line2').value;
        document.getElementById('user-full-address').value = line1 + ' ' + line2;
    });
}

// 주소 검색 함수
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            handleAddressSearch(data);
        }
    }).open({
        popupName: 'postcodePopup', // 팝업 이름을 지정해 줄 수 있습니다.
        left: (window.screen.width / 2) - 250, // 창을 화면 중앙에 위치시키기 위해 계산
        top: (window.screen.height / 2) - 300,
        width: 500, // 팝업 창 너비
        height: 600, // 팝업 창 높이
    });
}

// 주소 검색 결과 처리 함수
function handleAddressSearch(data) {
    let fullRoadAddress = data.roadAddress; // 도로명 주소
    let extraAddress = ''; // 참고 항목

    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
        extraAddress += data.bname;
    }
    if (data.buildingName !== '' && data.apartment === 'Y') {
        extraAddress += (extraAddress !== '' ? ', ' + data.buildingName : data.buildingName);
    }
    if (extraAddress !== '') {
        extraAddress = ' (' + extraAddress + ')';
    }

    let finalAddress = fullRoadAddress + extraAddress;

    const addressParts = finalAddress.split(' ');
    const line1 = addressParts.slice(0, 3).join(' ');
    const line2 = addressParts.slice(3).join(' ');

    document.getElementById('user-address-line1').value = line1;
    document.getElementById('user-address-line2').value = line2;

    document.getElementById('user-full-address').value = finalAddress;

    var geocoder = new kakao.maps.services.Geocoder();

    var callback = function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            const longitude = document.getElementById('addr-longitude');
            const latitude = document.getElementById('addr-latitude');

            longitude.value = result[0].y;
            latitude.value = result[0].x;
        }
    };

    geocoder.addressSearch(finalAddress, callback);
}

// MBTI 검사 열기
function openMbtiTest() {
    const mbtiWindow = window.open('/matching/mbti-test', '_blank', 'width=600,height=800');

    window.receiveMbtiResult = function(result) {
        const sanitizedResult = result.replace(/\s+/g, ''); // 값에서 공백을 제거
        document.getElementById('user-mbti').value = sanitizedResult;
    };
}

// 피드 아이템 클릭 시 팝업 열기 기능 초기화
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

// 피드 팝업 열기
function openFeedPopup(feedNo, feedPopup, popupContent) {
    fetch(`/mypage/getFeedContent?feedNo=${feedNo}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === "success") {
                // 피드 내용을 동적으로 생성
                popupContent.innerHTML = `
                    <div class='feed-out-box'>
                        <div class='feed-head-box'>
                            <div class='feed-title-box'>
                                <p class='feed-title' style='font-size:24px;'>${data.feedTitle}</p>
                            </div>
                            <div class='feed-icon-box'>
                                <div class='feed-meta'>
                                    <p class='feed-nickname'>${data.author}</p>
                                    <p class='feed-like-cnt'>${data.likeCount} Likes</p>
                                </div>
                            </div>
                            <div class='feed-remark'>
                                <div class='feed-meta'>
                                    <p class='feed-tag'>${data.feedTag}</p>
                                    <p class='feed-indate'>${data.feedIndate}</p>
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
                // 에러 메시지를 표시
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


function decodeHtml(html) {
    const txt = document.createElement("textarea");
    txt.innerHTML = html;
    return txt.value;
}


// 팝업 닫기 및 외부 클릭 시 팝업 닫기 기능 설정
function setupPopupCloseHandlers() {
    const feedPopup = document.getElementById('feed-popup');
    const closePopupButton = document.querySelector('.close-popup');

    closePopupButton.addEventListener('click', function() {
        closeFeedPopup(feedPopup);
    });

    window.addEventListener('click', function(event) {
        if (event.target == feedPopup) {
            closeFeedPopup(feedPopup);
        }
    });
}

// 피드 팝업 닫기
function closeFeedPopup(feedPopup) {
    feedPopup.style.display = 'none';
}
