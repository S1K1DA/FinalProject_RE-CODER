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

    // 피드 좋아요 하트 아이콘 클릭 처리
    const feedHeartIcons = document.querySelectorAll('.feed-heart');

    feedHeartIcons.forEach(heart => {
        heart.addEventListener('click', function(event) {
            event.stopPropagation();
            toggleFeedLike(this);
        });
    });

    // 프로필 좋아요 하트 아이콘 클릭 처리
    const profHeartIcons = document.querySelectorAll('.prof-heart');

    profHeartIcons.forEach(heart => {
        heart.addEventListener('click', function(event) {
            event.stopPropagation();
            toggleProfileLike(this);
        });
    });

    // 피드 좋아요 토글 함수
    function toggleFeedLike(element) {
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
            // 좋아요 추가
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

    // 프로필 좋아요 토글 함수
    function toggleProfileLike(element) {
        const likedUserNo = element.closest('.liked-prof-item').getAttribute('data-liked-user-no');
        const isLiked = element.classList.contains('liked');

        if (isLiked) {
            // 프로필 좋아요 해제
            fetch(`/mypage/unlikeProfile`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ likedUserNo: likedUserNo })
            }).then(response => {
                if (response.ok) {
                    element.classList.remove('liked');
                } else {
                    alert('프로필 좋아요 해제에 실패했습니다.');
                }
            });
        } else {
            // 프로필 좋아요 추가
            fetch(`/mypage/likeProfile`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ likedUserNo: likedUserNo })
            }).then(response => {
                if (response.ok) {
                    element.classList.add('liked');
                } else {
                    alert('프로필 좋아요 추가에 실패했습니다.');
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
