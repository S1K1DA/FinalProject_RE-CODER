

const signUpBtn = document.getElementById("signUp");
const signInBtn = document.getElementById("signIn");
const container = document.querySelector(".container-sign");

signUpBtn.addEventListener("click", () => {
  container.classList.add("right-panel-active");
});

signInBtn.addEventListener("click", () => {
  container.classList.remove("right-panel-active");
});

let pwdFlag = false; // 비밀번호 유효성 검사 플래그
let pwdFlag2 = false; // 비밀번호 확인 플래그
let nameFlag = false; // 이름 유효성 검사 플래그
let juminFlag = false; // 주민번호 유효성 검사 플래그
let phoneFlag = false; // 전화번호 유효성 검사 플래그
let nickFlag = false; // 닉네임 유효성 플래그
let nickChecked = false; // 닉네임 중복 확인 플래그
let emailVerified = false; // 이메일 인증 성공 플래그
let emailFlag = false; // 이메일 유효성 검사 플래그

// 닉네임 유효성 검사
function validateNick() {
    const patternNick = /^[가-힣]+$/;
    const nick = document.getElementById("userNickname").value;
    const textNick = document.getElementById("textNick");

    if (patternNick.test(nick)) {
        textNick.innerHTML = " ";
        nickFlag = true;
    } else {
        textNick.innerHTML = "올바른 닉네임을 입력하세요.";
        textNick.style.color = "red";
        nickFlag = false;
    }
}

// 닉네임 중복체크
function checkNicknameDuplicate() {
    const nickname = document.getElementById("userNickname").value;
    const textNick = document.getElementById("textNick");

    if (!nickFlag) return;

    $.ajax({
        type: "GET",
        url: "/member/check-nickname",
        data: { nickname: nickname },
        success: function(res) {
            if (res === "사용 가능한 닉네임입니다.") {
                alert("사용 가능한 닉네임입니다.");
                nickChecked = true;
                textNick.innerHTML = "사용 가능한 닉네임입니다.";
                textNick.style.color = "green";
            } else {
                alert("중복된 닉네임입니다.");
                nickChecked = false;
                textNick.innerHTML = "중복된 닉네임입니다.";
                textNick.style.color = "red";
            }
        },
        error: function(err) {
            console.error('Error:', err);
            textNick.innerHTML = "닉네임 확인 중 오류가 발생했습니다.";
            textNick.style.color = "red";
        }
    });
}

// 이메일 유효성 검사 함수
function validateEmail() {
    const patternEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const email = document.getElementById("userEmail").value;
    const textEmail = document.getElementById("textEmail");

    if (patternEmail.test(email)) {
        textEmail.innerHTML = " ";
        emailFlag = true;
    } else {
        textEmail.innerHTML = "올바른 이메일을 입력하세요.";
        textEmail.style.color = "red";
        emailFlag = false;
    }
}

// 이메일 중복 확인 함수
function checkEmailDuplicate() {
    if (!emailFlag) {
        alert("유효한 이메일 주소를 입력하세요.");
        return;
    }

    const email = document.getElementById("userEmail").value;
    const textEmail = document.getElementById("textEmail");

    $.ajax({
        type: "GET",
        url: "/member/check-email", // 이메일 중복 확인 API 엔드포인트
        data: { email: email },
        success: function(res) {
            if (res === "사용 가능한 이메일입니다.") {
                emailVerified = false; // 인증번호 발송을 시작하므로 인증 성공 초기화
                sendVerificationEmail(); // 이메일 인증번호 발송 함수 호출
            } else {
                textEmail.innerHTML = "중복된 이메일입니다.";
                textEmail.style.color = "red";
                emailFlag = false;
            }
        },
        error: function(err) {
            console.error('Error:', err);
            textEmail.innerHTML = "이메일 확인 중 오류가 발생했습니다.";
            textEmail.style.color = "red";
        }
    });
}

// 이메일 인증번호 발송 함수
function sendVerificationEmail() {
    const email = document.getElementById("userEmail").value;

    $.ajax({
        type: "POST",
        url: "/api/email/send", // 이메일 인증번호 발송 API 엔드포인트
        data: { email: email },
        success: function() {
            alert("인증번호가 발송되었습니다."); // 인증번호 발송 알림
            document.getElementById('codeContainer').style.display = 'flex'; // 인증 코드 입력 필드를 표시
        },
        error: function(err) {
            console.error('Error:', err);
            alert("이메일 발송에 실패했습니다."); // 에러 시 알림
        }
    });
}

// 인증 코드 검증 함수
function verifyCode() {
    const email = document.getElementById("userEmail").value;
    const code = document.getElementById("verificationCode").value;
    const textCode = document.getElementById("textCode");

    $.ajax({
        type: "POST",
        url: "/api/email/verify", // 인증 코드 검증 API 엔드포인트
        data: { email: email, code: code },
        success: function(res) {
            if (res === "인증 성공!") {
                textCode.innerHTML = "인증번호가 맞습니다.";
                textCode.style.color = "green";
                emailVerified = true; // 인증 성공 플래그 설정
            } else {
                textCode.innerHTML = "인증번호가 틀렸습니다.";
                textCode.style.color = "red";
                emailVerified = false;
            }
        },
        error: function(err) {
            console.error('Error:', err);
        }
    });
}



// 비밀번호 유효성 검사
function validatePassword() {
    const patternPwd = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=-])[a-zA-Z0-9!@#$%^&*()_+=-]{8,16}$/;
    const pwd = document.getElementById("userPassword").value;
    const pwd2 = document.getElementById("userPasswordConfirm").value;
    const textPwd = document.getElementById("textPwd");
    const textPwd2 = document.getElementById("textPwd2");

    if (patternPwd.test(pwd)) {
        textPwd.innerHTML = " ";
        pwdFlag = true;
    } else {
        textPwd.innerHTML = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.";
        textPwd.style.color = "red";
        pwdFlag = false;
    }

    if (pwdFlag && pwd === pwd2) {
        textPwd2.innerHTML = " ";
        pwdFlag2 = true;
    } else if (pwdFlag && pwd2 !== '') {
        textPwd2.innerHTML = "비밀번호가 일치하지 않습니다.";
        textPwd2.style.color = "red";
        pwdFlag2 = false;
    }
}

// 주민번호 유효성 검사 및 성별 설정
function checkGender() {
    const juminFront = document.getElementById('userBirthFront').value; // 주민등록번호 앞자리
    const juminBack = document.getElementById('userBirthBack').value; // 주민등록번호 뒷자리
    const textBirth = document.getElementById("textBirth");

    if (!/^\d{6}$/.test(juminFront)) {
        textBirth.innerHTML = "주민번호 앞자리를 정확히 입력하세요.";
        textBirth.style.color = "red";
        juminFlag = false;
        return false;
    } else if(!/^[1-4]$/.test(juminBack)) {
         textBirth.innerHTML = "주민번호 뒷자리를 정확히 입력하세요.";
         textBirth.style.color = "red";
         juminFlag = false;
         return false;
    } else {
        textBirth.innerHTML = " ";
        textBirth.style.color = "green";
        juminFlag = true;
    }
}


// 이름 유효성 검사
function validateName() {
    const patternName = /^[가-힣]+$/;  // 한글만 허용
    const name = document.getElementById("userName").value;
    const textName = document.getElementById("textName");

    if (patternName.test(name)) {
        textName.innerHTML = " ";
        nameFlag = true;
    } else {
        textName.innerHTML = "올바른 이름을 입력하세요.";
        textName.style.color = "red";
        nameFlag = false;
    }
}


// 전화번호 유효성 검사
function validatePhoneNumber() {
    const patternPhone = /^\d{10,11}$/;
    const phone = document.getElementById('userPhoneNumber').value;
    const textPhone = document.getElementById("textPhone");

    if (patternPhone.test(phone)) {
        textPhone.innerHTML = " ";
        phoneFlag = true;
    } else {
        textPhone.innerHTML = "올바른 전화번호를 입력하세요.";
        textPhone.style.color = "red";
        phoneFlag = false;
    }
}



// 유효성 검사 후 회원가입 버튼 눌리게
function registerClick(event) {
    event.preventDefault();  // 폼 제출 중단

    const patternNick = /^[가-힣]+$/;
    const patternEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const patternPwd = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=-])[a-zA-Z0-9!@#$%^&*()_+=-]{8,16}$/;
    const patternPhone = /^\d{10,11}$/;
    const space = /\s/;

    const nick = document.getElementById("userNickname").value;
    const email = document.getElementById("userEmail").value;
    const pwd = document.getElementById("userPassword").value;
    const pwd2 = document.getElementById("userPasswordConfirm").value;
    const name = document.getElementById("userName").value;
    const juminFront = document.getElementById('userBirthFront').value; // 주민등록번호 앞자리
    const juminBack = document.getElementById('userBirthBack').value; // 주민등록번호 뒷자리
    const phone = document.getElementById("userPhoneNumber").value;

    if (space.test(nick) || space.test(email) || space.test(pwd) || space.test(pwd2)) {
        Swal.fire('알림', '띄어쓰기는 사용할 수 없습니다.', 'error');
        return;
    } else if (!nick) {
        Swal.fire('알림', '닉네임을 입력해주세요.', 'error');
        return;
    } else if (!patternNick.test(nick)) {
        Swal.fire('알림', '올바른 닉네임을 입력해주세요.', 'error');
        return;
    } else if (!nickChecked) {
        Swal.fire('알림', '닉네임 중복 확인을 해주세요.', 'error');
        return;
    } else if (!email) {
        Swal.fire('알림', '이메일을 입력해주세요.', 'error');
        return;
    } else if (!patternEmail.test(email)) {
        Swal.fire('알림', '올바른 이메일 주소를 입력해주세요.', 'error');
        return;
    } else if (!emailFlag) {
        Swal.fire('알림', '이메일 주소가 중복되었습니다. 다시 입력해주세요.', 'error');
        return;
    } else if (!pwd) {
        Swal.fire('알림', '비밀번호를 입력해주세요.', 'error');
        return;
    } else if (!patternPwd.test(pwd)) {
        Swal.fire('알림', '비밀번호는 8-16자 사이의 문자, 숫자, 특수문자를 포함해야 합니다.', 'error');
        return;
    } else if (!pwd2) {
        Swal.fire('알림', '비밀번호 확인을 입력해주세요.', 'error');
        return;
    } else if (pwd !== pwd2) {
        Swal.fire('알림', '비밀번호가 일치하지 않습니다.', 'error');
        return;
    } else if (!name) {
        Swal.fire('알림', '이름을 입력해주세요.', 'error');
        return;
    } else if (!patternNick.test(name)) {
        Swal.fire('알림', '올바른 이름을 입력해주세요.', 'error');
        return;
    } else if (!juminFront || !juminBack) {
        Swal.fire('알림', '주민번호를 입력해주세요.', 'error');
        return;
    } else if (!/^\d{6}$/.test(juminFront)) {
        Swal.fire('알림', '올바른 주민 앞자리 입력해주세요.', 'error');
        return;
    } else if(!/^[1-4]$/.test(juminBack)) {
        Swal.fire('알림', '올바른 주민 뒷자리 입력해주세요.', 'error');
        return;
    } else if (!phone) {
        Swal.fire('알림', '전화번호를 입력해주세요.', 'error');
        return;
    } else if (!patternPhone.test(phone)) {
        Swal.fire('알림', '올바른 전화번호를 입력해주세요.', 'error');
        return;
    } else if (!emailVerified) {
        Swal.fire('알림', '이메일 인증이 완료되지 않았습니다.', 'error');
        return;
    } else if (!nickChecked) {
        Swal.fire('알림', '닉네임 중복 확인을 해주세요.', 'error');
        return;
    }
    // 모든 유효성 검사를 통과하면 폼 제출
    document.querySelector(".register-form").submit();
}

// 주소 가져오기
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 사용자가 선택한 주소를 처리하는 로직
            let fullRoadAddress = data.roadAddress; // 도로명 주소
            let extraAddress = ''; // 참고 항목

            // 참고 항목이 있는 경우 추가
            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '' && data.apartment === 'Y') {
                extraAddress += (extraAddress !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            if (extraAddress !== '') {
                extraAddress = ' (' + extraAddress + ')';
            }

            // 최종 주소 합치기
            let finalAddress = fullRoadAddress + extraAddress;

            // 도로명 주소를 input 필드에 설정
            document.getElementById('userAddress').value = finalAddress;
        }
    }).open();
}


// 로그인
document.querySelector('.login-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const email = formData.get('email');
    const password = formData.get('password');

    fetch('/member/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            email: email,
            password: password
        })
    })
    .then(response => response.text()) // 서버에서 텍스트로 응답을 받음
    .then(result => {
        if (result === 'success') {
            alert('로그인 성공!');
            window.location.href = '/'; // 로그인 성공 후 메인 페이지로 이동
        } else {
            alert('로그인 실패. 이메일 또는 비밀번호가 잘못되었습니다.');
            window.location.href = '/member/sign'
        }
    })
    .catch(error => console.error('Error:', error));
});