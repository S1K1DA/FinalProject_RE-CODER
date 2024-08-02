const signUpBtn = document.getElementById("signUp");
const signInBtn = document.getElementById("signIn");
const container = document.querySelector(".container-sign");

signUpBtn.addEventListener("click", () => {
  container.classList.add("right-panel-active");
});
signInBtn.addEventListener("click", () => {
  container.classList.remove("right-panel-active");
});

function checkGender() {
            const juminPart = document.getElementById('userJuminPart').value;
            if (juminPart.length !== 1 || !/[1-4]/.test(juminPart)) {
                alert('유효한 주민번호 뒷자리 한 자리를 입력하세요 (1-4)');
                return false;
            }
            return true;
        }