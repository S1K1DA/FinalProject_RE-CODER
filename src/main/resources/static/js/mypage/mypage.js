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
});
