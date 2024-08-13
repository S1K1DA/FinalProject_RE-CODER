document.addEventListener('DOMContentLoaded', function () {
    const openPopupButton = document.querySelector('.btn-edit-trigger');
    const popup = document.getElementById('passwordPopup');
    const closePopupButton = document.getElementById('closePopup');
    const submitPasswordButton = document.getElementById('submitPassword');
    const passwordInput = document.getElementById('passwordInput');

    // 팝업 열기
    openPopupButton.addEventListener('click', function () {
        popup.style.display = 'flex';
        passwordInput.focus();
    });

    // 팝업 닫기
    closePopupButton.addEventListener('click', function () {
        popup.style.display = 'none';
    });

    // 비밀번호 제출
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

    // Enter 키를 눌렀을 때 비밀번호 제출
    passwordInput.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // 기본 동작 막기 (폼 제출 등)
            submitPasswordButton.click();  // 확인 버튼 클릭
        }
    });
});
