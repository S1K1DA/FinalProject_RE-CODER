document.addEventListener('DOMContentLoaded', function () {
    const maxSelections = 3;

    const likeCheckboxes = document.querySelectorAll('input[name="likes"]');
    const dislikeCheckboxes = document.querySelectorAll('input[name="dislikes"]');

    likeCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            if (document.querySelectorAll('input[name="likes"]:checked').length > maxSelections) {
                checkbox.checked = false;
                alert(`최대 ${maxSelections}개까지 선택할 수 있습니다.`);
            }
        });
    });

    dislikeCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            if (document.querySelectorAll('input[name="dislikes"]:checked').length > maxSelections) {
                checkbox.checked = false;
                alert(`최대 ${maxSelections}개까지 선택할 수 있습니다.`);
            }
        });
    });
});
