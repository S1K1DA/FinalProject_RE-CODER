let currentQuestionIndex = 1;
const totalQuestions = 27;
const answers = { E: 0, I: 0, S: 0, N: 0, T: 0, F: 0, J: 0, P: 0 };
const selectedAnswers = []; // 각 질문에 대한 선택된 답변을 저장하는 배열

function selectAnswer(type, questionIndex) {
    answers[type]++;
    selectedAnswers[questionIndex] = type; // 선택된 답변을 저장
    document.getElementById(`question-${questionIndex}`).style.display = 'none';
    nextQuestion();
}

function nextQuestion() {
    currentQuestionIndex++;
    if (currentQuestionIndex <= totalQuestions) {
        document.getElementById(`question-${currentQuestionIndex}`).style.display = 'block';
    } else {
        showResults();
        document.getElementById('question-container').innerHTML = '<p>MBTI 검사가 끝났습니다!</p>';
    }
}

function prevQuestion() {
    if (currentQuestionIndex > 1) {
        const prevAnswer = selectedAnswers[currentQuestionIndex]; // 이전 질문에 대한 답변
        if (prevAnswer) {
            answers[prevAnswer]--; // 선택된 답변의 카운트를 감소
        }
        document.getElementById(`question-${currentQuestionIndex}`).style.display = 'none';
        currentQuestionIndex--;
        document.getElementById(`question-${currentQuestionIndex}`).style.display = 'block';
    }
}

function resetQuestion() {
    // 페이지를 새로고침하여 초기화
    location.reload();
}

function showResults() {
    const resultContainer = document.getElementById('result');
    const result = `
        ${answers.E > answers.I ? 'E' : 'I'}
        ${answers.S > answers.N ? 'S' : 'N'}
        ${answers.T > answers.F ? 'T' : 'F'}
        ${answers.J > answers.P ? 'J' : 'P'}
    `;
    resultContainer.innerHTML = `결과: ${result}`;
    resultContainer.style.display = 'block';
}

// 첫 질문 표시
document.getElementById(`question-${currentQuestionIndex}`).style.display = 'block';
