const SHOP_ID = payment.shopID;
const CHANNEL_KEY = payment.channelKey;


let coinPriceBtnEle = document.getElementsByName('coin-price');
let resultCoinCnt = "";
let resultCoinPrice = "";
let userEmail = "john.doe@example.com"; // 임시

for (const ele of Array.from(coinPriceBtnEle)) {
    ele.addEventListener("click", coinResponseTest);
}

async function coinResponseTest(event) {
    event.preventDefault(); // 링크의 기본 동작 막음

    resultCoinCnt = "하트 코인 " + event.target.getAttribute('data-value') + "개";
    resultCoinPrice = event.target.getAttribute('data-value') * 100;

    try {
        const response = await fetch("/charge/payment-order", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                paymentUserEmail: userEmail,
                paymentAmount: resultCoinPrice,
                paymentProduct: resultCoinCnt,
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
            orderName: resultCoinCnt,
            totalAmount: resultCoinPrice,
            currency: "KRW",
            payMethod: "CARD",
            customer: {
                fullName: "johndoe",
                phoneNumber: "123-456-7890",
                email: userEmail,
            },
        });

        if (paymentResponse.code != null) {
            // 오류 발생
            return alert(paymentResponse.message);
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
            alert("결제 완료"); // 또는 응답 데이터에 따라 알림을 다르게 할 수 있습니다.
        }).catch(error => {
            // 오류가 발생한 경우
            alert("결제 실패, 문제가 발생했습니다.");
        });
    } catch (error) {
        console.error(error);
        alert("An error occurred : " + error);
    }
}
