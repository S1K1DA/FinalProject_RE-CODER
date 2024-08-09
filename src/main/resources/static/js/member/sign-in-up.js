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
let emailFlag = false; // 이메일 유효성 검사 플래그
let nameFlag = false; // 이름 유효성 검사 플래그
let juminFlag = false; // 주민번호 유효성 검사 플래그
let phoneFlag = false; // 전화번호 유효성 검사 플래그

// 이름 유효성 검사
function validateName() {
    const patternName = /^[가-힣]+$/;
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

// 이메일 유효성 검사
function validateEmail() {
    const patternEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const email = document.getElementById('userEmail').value;
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

function checkGender() {
    const juminFront = document.getElementById('userBirthFront').value; // 주민등록번호 앞자리
    const juminBack = document.getElementById('userBirthBack').value; // 주민등록번호 뒷자리
    const textBirth = document.getElementById("textBirth");

    // 주민번호 앞자리와 뒷자리 유효성 검사
    if (!/^\d{6}$/.test(juminFront) || !/^[1-4]$/.test(juminBack)) {
        textBirth.innerHTML = "올바른 주민번호를 입력하세요.";
        textBirth.style.color = "red";
        juminFlag = false;
        return false;
    }
    textBirth.innerHTML = " ";
    juminFlag = true;
    return true;
}

// 유효성 검사 후 회원가입 버튼 동작
function registerClick() {
    validateName();
    validateEmail();
    validatePassword();
    checkGender();
    validatePhoneNumber();

    if (!nameFlag) {
        alert("올바른 이름을 입력하세요.");
        return;
    } else if (!emailFlag) {
        alert("올바른 이메일을 입력하세요.");
        return;
    } else if (!pwdFlag) {
        alert("비밀번호가 유효하지 않습니다.");
        return;
    } else if (!pwdFlag2) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    } else if (!juminFlag) {
        alert("올바른 주민번호를 입력하세요.");
        return;
    } else if (!phoneFlag) {
        alert("올바른 전화번호를 입력하세요.");
        return;
    } else {
        document.querySelector(".register-form").submit();
    }
}
