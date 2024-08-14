document.addEventListener('DOMContentLoaded', function () {
    const maxSentiSelections = 3;
    const maxHobbySelections = 5;

    // 성향 체크박스 처리
    const likeCheckboxes = document.querySelectorAll('input[name="likes"]');
    const dislikeCheckboxes = document.querySelectorAll('input[name="dislikes"]');

    likeCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            if (document.querySelectorAll('input[name="likes"]:checked').length > maxSentiSelections) {
                checkbox.checked = false;
                alert(`최대 ${maxSentiSelections}개까지 선택할 수 있습니다.`);
            }
        });
    });

    dislikeCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            if (document.querySelectorAll('input[name="dislikes"]:checked').length > maxSentiSelections) {
                checkbox.checked = false;
                alert(`최대 ${maxSentiSelections}개까지 선택할 수 있습니다.`);
            }
        });
    });

    // 취미 체크박스 처리
    const hobbyCheckboxes = document.querySelectorAll('input[name="hobbies"]');

    hobbyCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            if (document.querySelectorAll('input[name="hobbies"]:checked').length > maxHobbySelections) {
                checkbox.checked = false;
                alert(`취미는 최대 ${maxHobbySelections}개까지 선택할 수 있습니다.`);
            }
        });
    });

    // 좋아요 하트 아이콘 클릭 처리
    const heartIcons = document.querySelectorAll('.feed-heart');

    heartIcons.forEach(heart => {
        heart.addEventListener('click', function(event) {
            event.stopPropagation();  // 부모 요소의 클릭 이벤트 전파를 막음
            this.classList.toggle('liked');
        });
    });

    const heartIcons2 = document.querySelectorAll('.prof-heart');

    heartIcons2.forEach(heart => {
        heart.addEventListener('click', function(event) {
            event.stopPropagation();  // 부모 요소의 클릭 이벤트 전파를 막음
            this.classList.toggle('liked');
        });
    });

    // 팝업 관련 기능
    const openPopupButton = document.querySelector('.btn-edit-trigger');
    const popup = document.getElementById('passwordPopup');
    const closePopupButton = document.getElementById('closePopup');
    const submitPasswordButton = document.getElementById('submitPassword');
    const passwordInput = document.getElementById('passwordInput');

    // 요소가 존재하는지 확인한 후 이벤트 리스너 추가
    if (openPopupButton && popup && closePopupButton && submitPasswordButton && passwordInput) {
        openPopupButton.addEventListener('click', function () {
            popup.style.display = 'flex';
            passwordInput.focus();
        });

        closePopupButton.addEventListener('click', function () {
            popup.style.display = 'none';
        });

        submitPasswordButton.addEventListener('click', function () {
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
                          alert('비밀번호가 일치하지 않습니다.');
                          popup.style.display = 'none';
                      }
                  });
            }
        });

        passwordInput.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault(); // 기본 동작 막기 (폼 제출 등)
                submitPasswordButton.click();  // 확인 버튼 클릭
            }
        });
    }
});
