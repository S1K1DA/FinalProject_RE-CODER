document.getElementById('reportActionBtn').addEventListener('click', function() {
    document.getElementById('reportOverlay').style.display = 'flex';
});

document.getElementById('actionCloses').addEventListener('click', function() {
    document.getElementById('reportOverlay').style.display = 'none';
});

document.getElementById('punishDate').addEventListener('change', function() {
    const selectedDate = new Date(this.value);
    const today = new Date();

    // 설정한 날짜를 무시하기 위해 시간을 00:00:00으로 설정
    selectedDate.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);

    // 두 날짜 사이의 차이를 밀리초 단위로 계산
    const timeDifference = selectedDate - today;

    // 밀리초를 일(day) 단위로 변환
    const dayDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));

    // 결과를 표시
    document.querySelector('.punish-date-result').innerText = `총 ${dayDifference}일`;
});
