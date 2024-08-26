document.getElementById('reportActionBtn').addEventListener('click', function() {
    const reportedUserNo = this.getAttribute('data-reported-user-no');
    const reportNo = this.getAttribute('data-report-no');

    document.getElementById('reportedUserNo').textContent = reportedUserNo;
    document.getElementById('reportedUserNoInput').value = reportedUserNo;
    document.getElementById('reportNum').value = reportNo;

    document.getElementById('reportOverlay').style.display = 'flex';
});

document.getElementById('actionCloses').addEventListener('click', function() {
    document.getElementById('reportOverlay').style.display = 'none';
});


function dateCalc() {
    const daysInput = document.getElementById('punishDate').value;
    const resultDateElement = document.getElementById('resultDate');

    // 입력값을 숫자로 변환
    const days = parseInt(daysInput, 10);

    if (isNaN(days) || days < 0) {
        resultDateElement.innerText = '올바른 숫자를 입력하세요.';
        return;
    }

    // 오늘 날짜를 가져옴
    const today = new Date();

    // 입력된 일 수만큼 날짜를 계산
    const futureDate = new Date(today);
    futureDate.setDate(today.getDate() + days);

    // 날짜를 YYYY-MM-DD 형식으로 포맷
    const year = futureDate.getFullYear();
    const month = String(futureDate.getMonth() + 1).padStart(2, '0');
    const day = String(futureDate.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    formattedDate
    // 결과를 화면에 표시
    resultDateElement.textContent = formattedDate;

    // Hidden input에 punishmentResult 값을 설정
    document.getElementById('punishmentResultInput').value = formattedDate;
};

document.getElementById('reportActionBox').addEventListener('submit', async function(event) {
    event.preventDefault(); // 기본 제출 동작을 막습니다

    // 폼 데이터 가져오기
    const formData = new FormData(this);


    try {
        // 사용자에게 확인 메시지 표시
        const result = await Swal.fire({
            title: "처리하시겠습니까?",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "rgb(255 128 135)",
            cancelButtonColor: "rgb(150 150 150)",
            confirmButtonText: "예",
            cancelButtonText: "아니요"
        });


        // 사용자가 확인 버튼을 클릭했는지 확인
        if (result.isConfirmed) {
            // 결제 취소 요청 서버에 전송
            // fetch를 사용하여 데이터를 비동기적으로 제출합니다
            const response = await fetch(this.action, {
                method: 'POST',
                body: formData
            });

            console.log(response.text());
            console.log(response.status);

            // 취소 완료 메시지 표시
            if (response.ok) {
                await Swal.fire({
                    title: "처리 완료",
                    icon: "success"
                }).then(() => {
                    window.location.reload();
                });

            }else{
                return Promise.reject();
            }
        }
    } catch (error) {
        // 에러가 발생한 경우 에러 메시지 표시
        console.error(error);
        await Swal.fire({
            title: "오류 발생",
            text: "처리 중 오류가 발생했습니다.",
            icon: "error"
        });
    }

});