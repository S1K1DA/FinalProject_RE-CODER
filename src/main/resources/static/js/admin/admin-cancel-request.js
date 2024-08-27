

async function paymentResponse (type, paymentNo){
    let title = "";

    if(type === 'cancel'){
        title = "결제 취소 수락하시겠습니까?";
    }else if(type === 'denied'){
        title = "결제 취소 거부하시겠습니까?"
    }

    try {
        // 사용자에게 확인 메시지 표시
        const result = await Swal.fire({
            title: title,
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
            const response = await fetch("/admin/payment/"+type, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ paymentNo: paymentNo }),
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
            text: "결제 취소 중 오류가 발생했습니다.",
            icon: "error"
        });
    }
}