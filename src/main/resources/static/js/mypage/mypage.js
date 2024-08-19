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
            event.stopPropagation();
            toggleLike(this);
        });
    });

    // 좋아요 토글 함수
    function toggleLike(element) {
        const feedNo = element.closest('.liked-feed-item').getAttribute('data-feed-no');
        const isLiked = element.classList.contains('liked');

        if (isLiked) {
            // 좋아요 해제
            fetch(`/mypage/unlikeFeed`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ feedNo: feedNo })
            }).then(response => {
                if (response.ok) {
                    element.classList.remove('liked');
                } else {
                    alert('좋아요 해제에 실패했습니다.');
                }
            });
        } else {
            // 좋아요 추가 (이미 좋아요 되어있으므로 이 부분은 필요 없을 수 있습니다)
            fetch(`/mypage/likeFeed`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ feedNo: feedNo })
            }).then(response => {
                if (response.ok) {
                    element.classList.add('liked');
                } else {
                    alert('좋아요 추가에 실패했습니다.');
                }
            });
        }
    }

    // 팝업 관련 기능
    const openPopupButton = document.querySelector('.btn-edit-trigger');
    const popup = document.getElementById('passwordPopup');
    const closePopupButton = document.getElementById('closePopup');
    const submitPasswordButton = document.getElementById('submitPassword');
    const passwordInput = document.getElementById('passwordInput');

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
            } else {
                alert('비밀번호를 입력해주세요.');
            }
        });

        passwordInput.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                submitPasswordButton.click();
            }
        });
    }

    // 탈퇴 폼 처리
    const deleteForm = document.querySelector('form[action="/mypage/delete"]');
    if (deleteForm) {
        deleteForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const password = document.getElementById('password').value;
            const passwordConfirm = document.getElementById('password-confirm').value;

            if (!password || !passwordConfirm) {
                alert('비밀번호와 비밀번호 확인을 입력해주세요.');
                return;
            }

            if (password !== passwordConfirm) {
                alert('비밀번호 확인이 일치하지 않습니다.');
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
                      alert('입력한 비밀번호가 현재 비밀번호와 일치하지 않습니다.');
                  }
              });
        });
    }
});
