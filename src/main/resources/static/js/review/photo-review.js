// 이미지 업로더 기능
function imageUploader(file, el) {
    var formData = new FormData();
    formData.append('file', file);

    $.ajax({
        data: formData,
        type: "POST",
        url: '/post/image-upload',
        contentType: false,
        processData: false,
        enctype: 'multipart/form-data',
        success: function(data) {
            // 업로드된 이미지의 URL을 이용하여 이미지 삽입
            $(el).summernote('insertImage', data, function($image) {
                $image.css({
                    'width': '100%',
                    'max-width': '600px'
                });
            });
            console.log("Uploaded image URL: " + data);
        },
        error: function() {
            console.log("이미지 업로드 실패");
        }
    });
}

// 페이지 로드 시 서버에서 전달된 메시지를 확인하고 알림창을 표시
window.onload = function() {
    var message = document.querySelector('.alert')?.textContent;
    if (message) {
        alert(message);
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
            // 클릭된 div 안에 있는 form을 찾습니다
            var form = element.querySelector('form');
            if (form) {
                form.submit();
            }
        }
    });
}

// 신고하기 팝업 열기
function openReportPopup(button) {
    // 리뷰 정보 가져오기
    const reviewNoElement = document.getElementById('reviewNo');
    const reportedUserNoElement = document.getElementById('reportedUserNo');
    const reportTypeNoElement = document.getElementById('reportTypeNo');

    // 오류 방지: 요소가 존재하는지 확인
    if (!reviewNoElement || !reportedUserNoElement || !reportTypeNoElement) {
        console.error("ReviewNo, ReportedUserNo or ReportTypeNo element not found!");
        return;
    }

    const reviewNo = reviewNoElement.value;
    const reportedUserNo = reportedUserNoElement.value;
    const reportTypeNo = reportTypeNoElement.value;
    const reportedUser = document.querySelector('.profile-info-ct strong').textContent;

    // 로그로 확인
    console.log("Review No:", reviewNo);
    console.log("Reported User No:", reportedUserNo);
    console.log("Report Type No:", reportTypeNo);
    console.log("Reported User Name:", reportedUser);

    // 신고하기 창의 입력값 설정
    document.getElementById('reviewNo').value = reviewNo;
    document.getElementById('reportedUserNo').value = reportedUserNo;
    document.getElementById('reportTypeNo').value = reportTypeNo; // 추가
    document.getElementById('reportedUser').textContent = reportedUser;

    // 신고하기 창 표시
    document.getElementById('reportOverlay').style.display = 'flex';
}



// 신고하기 팝업 닫기
document.getElementById('actionCloses').addEventListener('click', function() {
    document.getElementById('reportOverlay').style.display = 'none';
});

// 신고하기 폼 제출
document.getElementById('reportForm').addEventListener('submit', async function(event) {
    event.preventDefault();  // 기본 폼 제출 동작을 막음

    // 폼 데이터 수집
    const formData = new FormData(this);

    // 콘솔에 폼 데이터 확인하기
    for (var pair of formData.entries()) {
        console.log(pair[0]+ ': ' + pair[1]);
    }

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

            // 응답 상태 확인
            console.log(response.status);

            if (response.ok) {
                await Swal.fire({
                    title: "신고 완료",
                    icon: "success"
                }).then(() => {
                    window.location.reload();
                });
            } else {
                // 서버에서 응답이 200이 아닌 경우 오류 처리
                console.log("서버 응답 상태 코드: " + response.status);
                const errorText = await response.text();
                console.error("서버 오류 메시지: " + errorText);
                return Promise.reject();
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


