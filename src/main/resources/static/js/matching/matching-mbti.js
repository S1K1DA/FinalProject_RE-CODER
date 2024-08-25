document.addEventListener("DOMContentLoaded", function() {
    // 사용자 MBTI 가져오기
    const userMbti = document.getElementById("user-mbti").textContent.trim().toUpperCase();

    // MBTI에 따른 천생연분 데이터
    const mbtiSoulmates = {
        ISTJ: { soulmate: "ESFP", reason: "ISTJ는 신뢰할 수 있는 든든한 바위 같은 성격이에요. 반면 ESFP는 파티의 주인공처럼 사람들에게 활력을 주는 성격이죠. ISTJ는 ESFP에게 안정감을 주고, ESFP는 ISTJ의 일상에 활기를 불어넣어 서로의 삶을 풍성하게 만들어줘요." },
        ISFJ: { soulmate: "ESTP", reason: "ISFJ는 따뜻하고 배려심이 많은 성격이고, ESTP는 모험을 사랑하는 자유로운 영혼이에요. ESTP는 ISFJ에게 새로운 세계를 보여주고, ISFJ는 ESTP에게 따뜻한 안식처를 제공해주며 서로에게 새로운 경험과 안정감을 줄 수 있어요." },
        INFJ: { soulmate: "ENFP", reason: "INFJ는 깊은 생각을 좋아하는 성향이고, ENFP는 상상력과 열정이 넘치는 사람이에요. ENFP는 INFJ에게 다채로운 아이디어와 열정을 주고, INFJ는 ENFP에게 삶의 의미와 깊이를 더해주며 둘 사이에 깊은 연결이 형성돼요." },
        INTJ: { soulmate: "ENFP", reason: "INTJ는 계획적이고 목표지향적인 성향을 가지고 있어요. 반면 ENFP는 창의적이고 자유로운 영혼이죠. ENFP는 INTJ의 단단한 계획에 유연성을 더해주고, INTJ는 ENFP에게 인생의 방향성을 제공해 서로를 보완해요." },
        ISTP: { soulmate: "ESFJ", reason: "ISTP는 독립적이고 문제 해결에 뛰어나요. ESFJ는 사람들을 챙기고 돌보는 것을 좋아하는 따뜻한 성격이죠. ESFJ는 ISTP에게 따뜻한 인간관계를 만들어주고, ISTP는 ESFJ에게 현실적인 문제 해결 능력을 제공해 서로에게 안정감을 줘요." },
        ISFP: { soulmate: "ESTJ", reason: "ISFP는 감성적이고 예술적인 성향을 가지고 있어요. ESTJ는 조직적이고 목표지향적인 성격이죠. ESTJ는 ISFP에게 구조와 안정감을 주고, ISFP는 ESTJ에게 창의적이고 유연한 사고를 불어넣어 서로에게서 큰 도움을 받을 수 있어요." },
        INFP: { soulmate: "ENFJ", reason: "INFP는 이상적이고 감정적으로 깊은 사람이고, ENFJ는 사람들을 돕는 것을 좋아하는 따뜻한 성격이에요. ENFJ는 INFP에게 감정적 지지를 제공하고, INFP는 ENFJ에게 진심 어린 공감을 나눠 서로의 감정적 연결을 깊게 만들어요." },
        INTP: { soulmate: "ENTJ", reason: "INTP는 분석적이고 논리적인 사고를 즐기고, ENTJ는 강한 리더십과 추진력을 가진 사람이에요. ENTJ는 INTP에게 실천력을 불어넣고, INTP는 ENTJ에게 새로운 시각과 아이디어를 제공해 함께 큰 그림을 그려 나갈 수 있어요." },
        ESTP: { soulmate: "ISFJ", reason: "ESTP는 즉흥적이고 활기찬 성격이고, ISFJ는 헌신적이고 따뜻한 사람이에요. ISFJ는 ESTP에게 안정감을 주고, ESTP는 ISFJ의 삶에 재미와 활력을 더해주어 서로의 세계를 넓혀줄 수 있어요." },
        ESFP: { soulmate: "ISTJ", reason: "ESFP는 외향적이고 사람들과 어울리는 것을 좋아하는 성격이고, ISTJ는 체계적이고 신뢰할 수 있는 사람이에요. ISTJ는 ESFP에게 안정적인 기반을 제공하고, ESFP는 ISTJ에게 즐거움과 활기를 더해주며 서로에게서 큰 힘을 얻어요." },
        ENFP: { soulmate: "INFJ", reason: "ENFP는 창의적이고 열정적인 성격을 가지고 있어요. INFJ는 깊이 있는 통찰력과 의미를 찾는 성향을 가지고 있죠. INFJ는 ENFP에게 삶의 깊이를 더해주고, ENFP는 INFJ에게 다채로운 아이디어와 새로운 시각을 제공해 둘 사이에 특별한 연결이 형성돼요." },
        ENTP: { soulmate: "INFJ", reason: "ENTP는 혁신적이고 논쟁을 즐기며, INFJ는 깊은 통찰력을 가진 사람이에요. INFJ는 ENTP에게 감정적 안정감을 제공하고, ENTP는 INFJ에게 새로운 관점과 도전을 제시해 서로의 지적 호기심을 자극해요." },
        ESTJ: { soulmate: "ISFP", reason: "ESTJ는 조직적이고 목표지향적인 성격이고, ISFP는 예술적이고 감정에 충실한 사람이에요. ISFP는 ESTJ에게 유연성과 창의성을 더해주고, ESTJ는 ISFP에게 구조와 방향성을 제공해 서로의 장점을 극대화해요." },
        ESFJ: { soulmate: "ISTP", reason: "ESFJ는 사교적이고 배려심이 많은 성격이고, ISTP는 독립적이고 논리적인 사고를 즐기는 사람이에요. ISTP는 ESFJ에게 현실적인 문제 해결 능력을 제공하고, ESFJ는 ISTP에게 따뜻한 인간관계를 만들어 주어 서로를 더욱 강하게 만들어줘요." },
        ENFJ: { soulmate: "INFP", reason: "ENFJ는 외향적이고 타인을 돕는 것을 좋아하는 따뜻한 성격이고, INFP는 이상적이고 감정적으로 깊은 사람이에요. INFP는 ENFJ에게 깊은 공감 능력을 제공하고, ENFJ는 INFP에게 감정적 지지를 제공해 서로의 감정적 유대감을 강화해요." },
        ENTJ: { soulmate: "INTP", reason: "ENTJ는 강한 리더십과 추진력을 가진 성격이고, INTP는 논리적이고 분석적인 사고를 즐기는 사람이에요. INTP는 ENTJ에게 새로운 아이디어와 분석을 제공하고, ENTJ는 INTP에게 실천력을 불어넣어 함께 목표를 달성해 나갈 수 있어요." }
    };

    // 천생연분 MBTI와 이유 가져오기
    const soulmateData = mbtiSoulmates[userMbti];

    if (soulmateData) {
        document.getElementById("soulmate-mbti").textContent = soulmateData.soulmate;
        document.getElementById("soulmate-reason").textContent = soulmateData.reason;
    } else {
        document.getElementById("soulmate-mbti").textContent = "정보를 찾을 수 없습니다.";
        document.getElementById("soulmate-reason").textContent = "";
    }

    // 천생연분 매칭 버튼 클릭 이벤트 핸들러
    document.getElementById("love-matching").addEventListener("click", function() {
        console.log("천생연분 매칭 버튼 클릭됨");

        fetch("/matching/soulmates")
            .then(response => {
                if (!response.ok) {
                    throw new Error("네트워크 응답이 올바르지 않습니다: " + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                console.log("매칭 데이터 수신됨:", data);
                updateMatchingList(data);
            })
            .catch(error => console.error("천생연분 매칭 데이터 가져오기 오류:", error));
    });

    // 리스트를 업데이트하는 함수
    function updateMatchingList(data) {
        const matchingList = document.getElementById("matching-list");
        matchingList.innerHTML = ""; // 기존 리스트 초기화

        data.forEach(user => {
            const listItem = document.createElement("li");
            listItem.classList.add("matching-list-item");

            listItem.innerHTML = `
                <div class="list-profile-photo">
                    <img src="${user.profilePhoto || '/images/default-profile.png'}" alt="Profile Photo">
                </div>
                <div class="list-profile-info">
                    <p class="list-profile-nickname">닉네임: ${user.nickname}</p>
                    <p class="list-profile-mbti">
                        <img src="/image/icons/mbti_icon.png" alt="MBTI Icon" class="mbti-icon">
                        MBTI: ${user.mbti}
                    </p>
                </div>
                <button class="match-button">매칭 신청</button>
            `;

            matchingList.appendChild(listItem);
        });
    }
});
