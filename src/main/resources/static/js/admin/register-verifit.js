let pwdFlag = false; // 비밀번호 유효성 검사 플래그
let pwdFlag2 = false; // 비밀번호 확인 플래그
let emailFlag = false; // 이메일 유효성 검사 플래그

function validateEmail() {
    const patternEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const email = document.getElementById("adminEmail").value;
    const textEmail = document.getElementById("email-input");

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
async function checkEmailDuplicate() {
    if (!emailFlag) {
        Swal.fire('알림', '유효한 이메일 입력하세요.', 'error');
        return;
    }

    const email = document.getElementById("adminEmail").value;
    const textEmail = document.getElementById("email-input");


    try {
        const response = await fetch("/admin/register/verifit?adminUserEmail=" + email , {
            method: "GET",
            headers: {
                'Content-Type': 'application/json'
            }
        });

        const responseText = await response.text();

        console.log(responseText);

        if (responseText === "사용 가능한 이메일입니다.") {
            textEmail.innerHTML = "사용 가능한 이메일입니다.";
            textEmail.style.color = "blue";
            emailFlag = true;
        } else {
            textEmail.innerHTML = "중복된 이메일입니다.";
            textEmail.style.color = "red";
            emailFlag = false;
        }

    }catch (error){
        console.error('Error:', error);
        textEmail.innerHTML = "이메일 확인 중 오류가 발생했습니다.";
        textEmail.style.color = "red";
    }

}

function validatePassword() {
    const patternPwd = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=-])[a-zA-Z0-9!@#$%^&*()_+=-]{8,16}$/;
    const pwd = document.getElementById("adminPasswd").value;
    const pwd2 = document.getElementById("adminPasswdConfirm").value;
    const textPwd = document.getElementById("password-input");
    const textPwd2 = document.getElementById("password-comfirm");

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

function registerClick(event) {
    event.preventDefault();  // 폼 제출 중단

    const patternEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const patternPwd = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=-])[a-zA-Z0-9!@#$%^&*()_+=-]{8,16}$/;

    const email = document.getElementById("adminEmail").value;
    const pwd = document.getElementById("adminPasswd").value;
    const pwd2 = document.getElementById("adminPasswdConfirm").value;

    if (!email) {
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
    }
    // 모든 유효성 검사를 통과하면 폼 제출
    document.querySelector(".register-form").submit();
}

// 로그인
document.querySelector('.login-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const email = formData.get('adminUserEmail');
    const password = formData.get('adminUserPwd');

    try {
        const response = await fetch('/admin/login/authentication', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                adminUserEmail: email,
                adminUserPwd: password
            })
        });

        const errorText = await response.text();

        if (response.ok) {
            Swal.fire({
                title: '알림',
                text: '로그인 완료!',
                icon: 'success',
                confirmButtonText: '확인'
            }).then(() => {
                window.location.href = '/admin/main'; // 로그인 성공 후 메인 페이지로 이동
            });
        } else{
            Swal.fire({
                title: '알림',
                text: errorText,
                icon: 'error',
                confirmButtonText: '확인'
            }).then(() => {
                window.location.href = '/admin/login';
            });
        }

    } catch (error) {
        console.error('Error:', error);
        Swal.fire('오류', '로그인 중 오류가 발생했습니다.', 'error');
    }
});
