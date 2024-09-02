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

    // 프로필 아이템 클릭 시 팝업 열기 초기화
    initializeProfileItemPopup();
});

// 매칭 수락 및 거절 관련 함수들 추가
function acceptMatch(matchingNo) {
    updateMatchingState(matchingNo, 'Y');
}

function rejectMatch(matchingNo) {
    updateMatchingState(matchingNo, 'N');
}

function updateMatchingState(matchingNo, state) {
    fetch('/mypage/updateMatchingState', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ matchingNo: matchingNo, state: state })
    })
    .then(response => response.text())
    .then(data => {
        if (data === '매칭 상태 업데이트 성공') {
            Swal.fire({
                title: 'Success!',
                text: '매칭 상태가 성공적으로 업데이트되었습니다.',
                icon: 'success',
                confirmButtonText: '확인'
            }).then(() => {
                location.reload();
            });
        } else {
            Swal.fire({
                title: 'Error!',
                text: '매칭 상태 업데이트에 실패했습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire({
            title: 'Error!',
            text: '매칭 상태 업데이트 중 오류가 발생했습니다.',
            icon: 'error',
            confirmButtonText: '확인'
        });
    });
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
        sendLikeRequest('/mypage/unlikeFeed', { feedNo }, element, false);
    } else {
        sendLikeRequest('/mypage/likeFeed', { feedNo }, element, true);
    }
}

// 프로필 좋아요 토글 함수
function toggleProfileLike(element) {
    const likedUserNo = element.closest('.liked-prof-item').getAttribute('data-liked-user-no');
    const isLiked = element.classList.contains('liked');

    if (isLiked) {
        sendLikeRequest('/mypage/unlikeProfile', { likedUserNo }, element, false);
    } else {
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

    const originalNickname = nicknameInput.value.trim();

    nicknameInput.addEventListener('input', function () {
        isNicknameValid = false;
    });

    checkNicknameBtn.addEventListener('click', function () {
        checkNicknameAvailability(nicknameInput.value.trim(), originalNickname);
    });

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

    if (nickname === originalNickname) {
        isNicknameValid = true;
        Swal.fire({
            icon: 'info',
            title: '알림',
            text: '현재 사용 중인 닉네임입니다.',
        });
        return;
    }

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
        popupName: 'postcodePopup',
        left: (window.screen.width / 2) - 250,
        top: (window.screen.height / 2) - 300,
        width: 500,
        height: 600,
    });
}

// 주소 검색 결과 처리 함수
function handleAddressSearch(data) {
    let fullRoadAddress = data.roadAddress;
    let extraAddress = '';

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
    const mbtiWindow = window.open('/matching/mbti', '_blank', 'width=600,height=800');

    window.receiveMbtiResult = function(result) {
        const sanitizedResult = result.replace(/\s+/g, '');
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
                const feedIndate = new Date(data.feedIndate);

                const formattedDate = feedIndate.getFullYear() + '.' +
                    String(feedIndate.getMonth() + 1).padStart(2, '0') + '.' +
                    String(feedIndate.getDate()).padStart(2, '0') + ' ' +
                    String(feedIndate.getHours()).padStart(2, '0') + ':' +
                    String(feedIndate.getMinutes()).padStart(2, '0');

                popupContent.innerHTML = `
                    <div class='feed-out-box'>
                        <div class='feed-head-box'>
                            <div class='feed-title-box'>
                                <p class='feed-title' >${data.feedTitle}</p>
                            </div>
                            <div class='feed-icon-box'>
                                <div class='feed-meta'>
                                    <p class='feed-nickname'>${data.author}</p>
                                    <p class='feed-like-cnt'>좋아요 수 : ${data.likeCount}</p>
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

function closeFeedPopup(feedPopup) {
    feedPopup.style.display = 'none';
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

                let address = data.consentLocationInfo === 'Y' ? data.address : '미공개';

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
                                <p style="width: 25%; text-align: right; font-weight: bold;">주소:</p>
                                <p style="width: 75%; margin-left: 10px; text-align: left;">${address}</p>
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


// 리뷰 삭제 확인을 SweetAlert2로 변경
function submitDeleteForm(element) {
    Swal.fire({
        title: '정말로 이 리뷰를 삭제하시겠습니까?',
        text: "이 작업은 되돌릴 수 없습니다!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '네, 삭제하겠습니다',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            var form = element.querySelector('form');
            if (form) {
                form.submit();
            }
        }
    });
}

// 프로필 팝업 닫기 및 외부 클릭 시 팝업 닫기 기능 설정
document.addEventListener('DOMContentLoaded', function () {
    const profilePopup = document.getElementById('profile-popup');
    const closePopupButton = document.querySelector('.close-popup');

    if (profilePopup && closePopupButton) {
        closePopupButton.addEventListener('click', function () {
            profilePopup.style.display = 'none';
        });

        window.addEventListener('click', function (event) {
            if (event.target == profilePopup) {
                profilePopup.style.display = 'none';
            }
        });
    }
});
