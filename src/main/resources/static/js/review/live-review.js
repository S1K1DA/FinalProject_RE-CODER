// 페이지 로드 시 서버에서 전달된 메시지를 SweetAlert2로 표시
window.onload = function() {
    var message = document.querySelector('.alert')?.textContent;
    if (message) {
        Swal.fire({
            title: '알림',
            text: message,
            icon: 'info',
            confirmButtonText: '확인'
        });
    }
};

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

// 신고하기 팝업 열기 - live-main.html 전용
function openLiveReviewReportPopup(button) {
    const reviewItem = button.closest('.live-review-item');
    const reviewNo = reviewItem.getAttribute('data-review-no');
    const reportedUserNo = reviewItem.getAttribute('data-reviewer-id');
    const reviewerNickname = reviewItem.getAttribute('data-reviewer-nickname');

    console.log("Review No:", reviewNo);
    console.log("Reported User No:", reportedUserNo);
    console.log("Reported User Name:", reviewerNickname);

    document.getElementById('reviewNo').value = reviewNo;
    document.getElementById('reportedUserNo').value = reportedUserNo;
    document.getElementById('reportTypeNo').value = reviewNo;
    document.getElementById('reportedUser').textContent = reviewerNickname;

    document.getElementById('reportOverlay').style.display = 'flex';
}

// 신고하기 팝업 닫기
document.getElementById('actionCloses').addEventListener('click', function() {
    document.getElementById('reportOverlay').style.display = 'none';
});

// 신고하기 폼 제출
document.getElementById('reportForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const formData = new FormData(this);

    try {
        const result = await Swal.fire({
            title: "신고하시겠습니까?",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "rgb(255 128 135)",
            cancelButtonColor: "rgb(150 150 150)",
            confirmButtonText: "예",
            cancelButtonText: "아니요"
        });

        if (result.isConfirmed) {
            const response = await fetch(this.action, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                await Swal.fire({
                    title: "신고 완료",
                    icon: "success"
                }).then(() => {
                    window.location.reload();
                });
            } else {
                console.error("서버 오류: " + response.status);
            }
        }
    } catch (error) {
        console.error(error);
        await Swal.fire({
            title: "오류 발생",
            text: "신고 처리 중 오류가 발생했습니다.",
            icon: "error"
        });
    }
});


    // feed page 더보기 드롭다운
    $(".more-drop-btn").on("click", function(e) {
        e.preventDefault();

        var $dropdown = $(this).closest('.dropdown-more').find('.more-dropdown-content');

        if ($dropdown.is(":visible")) {
            $dropdown.slideUp(300);
        } else {
            $dropdown.slideDown(300);
        }
    });