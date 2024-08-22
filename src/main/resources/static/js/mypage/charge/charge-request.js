const SHOP_ID = payment.shopID;
const CHANNEL_KEY = payment.channelKey;


let coinPriceBtnEle = document.getElementsByName('coin-price');
let resultCoinCnt = "";
let resultCoinPrice = "";
let userEmail = document.getElementById('mypage-user-email').value; // 임시
let userName = document.getElementById('mypage-user-name').value;
let userTelnum = document.getElementById('mypage-user-telnum').value;

for (const ele of Array.from(coinPriceBtnEle)) {
    ele.addEventListener("click", coinResponseTest);
}

async function coinResponseTest(event) {
    event.preventDefault(); // 링크의 기본 동작 막음

    resultCoinCnt = event.target.getAttribute('data-value');
    resultCoinPrice = event.target.getAttribute('data-value') * 100;

    try {
        const response = await fetch("/charge/payment-order", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                paymentUserEmail: userEmail,
                paymentAmount: resultCoinPrice,
                paymentCoin: resultCoinCnt,
            }),
        });

        const responseText = await response.text();
        console.log(typeof responseText);
        console.log(responseText);

        if (response.ok) {
            requestPayment(responseText);
        }else{
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${responseText}`);
        }

    } catch (error) {
        console.error('Error:', error);
    }
}


async function requestPayment(paymentNo) {

    console.log(resultCoinCnt);

    try {
        // 결제 요청 처리
        const paymentResponse = await PortOne.requestPayment({
            storeId: SHOP_ID,
            channelKey: CHANNEL_KEY,
            paymentId: paymentNo,
            orderName: "하트 코인 "+resultCoinCnt+ "개",
            totalAmount: resultCoinPrice,
            currency: "KRW",
            payMethod: "CARD",
            customer: {
                fullName: userName,
                phoneNumber: userTelnum,
                email: userEmail,
            },
        });

        if (paymentResponse.code != null) {
            // 오류 발생
            console.log(paymentResponse.message);
            return Swal.fire({
                    title: "결제 실패",
                    icon: "error"
                });
        }

        await fetch("/charge/complete", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                paymentNo: paymentNo
            }),
        }).then(response => {
            // 응답이 성공적인지 확인
            if (response.ok) {
                return response.json(); // JSON 형태로 응답 본문을 파싱
            } else {
                throw new Error('응답 실패');
            }
        }).then(data => {
            // 정상적인 응답을 받은 경우
            Swal.fire({
                title: "결제 완료",
                icon: "success"
            })
        }).catch(error => {
            // 오류가 발생한 경우
            alert("결제 실패, 문제가 발생했습니다.");
        });
    } catch (error) {
        console.error(error);
        alert("An error occurred : " + error);
    }
}


