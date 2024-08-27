document.getElementById('userFilter').addEventListener('change', function() {
    const selectedValue = this.value;
    const url = new URL(window.location.href);
    url.searchParams.set('filter', selectedValue);
    window.location.href = url.toString();
});

document.getElementById('searchForm').addEventListener('submit', function(event) {
    // Prevent the default form submission
    event.preventDefault();

    // Get form values
    const form = event.target;
    const category = form.querySelector('select[name="category"]').value;
    const search = form.querySelector('input[name="search"]').value;

    // Create URL with query parameters
    const url = new URL(window.location.href);
    url.searchParams.set('category', category);
    url.searchParams.set('search', search);

    // Redirect to the new URL
    window.location.href = url.toString();
});


async function changeUserState(button){
    const row = button.closest('tr');
    const selectElement = row.querySelector('#userState');
    const userNo = row.querySelector('#basicUserNo').getAttribute('data-user-no');
    const selectedValue = selectElement.options[selectElement.selectedIndex].value;

    console.log(selectedValue);
    console.log(userNo);

    try {

        const result = await Swal.fire({
            title: "해당 회원의 상태를\n변경하시겠습니까?",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "rgb(255 128 135)",
            cancelButtonColor: "rgb(150 150 150)",
            confirmButtonText: "예",
            cancelButtonText: "아니요"
        })

        // 사용자가 확인 버튼을 클릭했는지 확인
        if (result.isConfirmed) {
            // 결제 취소 요청 서버에 전송
            const response = await fetch("/admin/user/status/change", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    basicUserNo: userNo,
                    basicUserStatus: selectedValue,
                }),
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
            text: "상태 변경 중 오류가 발생했습니다.",
            icon: "error"
        });
    }
}