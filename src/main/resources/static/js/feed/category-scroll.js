// 필터 버튼 클릭 시 필터 적용
document.querySelectorAll('input[name="filter"]').forEach(ele => {
    ele.addEventListener("click", applyFilter);
});

function applyFilter(event) {
    const filterValue = event.target.value;
    const url = new URL(window.location.href);
    url.searchParams.set('filter', filterValue);
    window.location.href = url.toString();
}


// 정렬
document.getElementById('feed-array').addEventListener('change', function() {
    const selectedValue = this.value;
    const url = new URL(window.location.href);
    url.searchParams.set('feed-array', selectedValue);
    window.location.href = url.toString();
});



let isLoading = false; // 데이터 로딩 상태
let hasMoreData = true; // 더 많은 데이터가 있는지 여부
let currentPage = 2; // 현재 페이지 번호

// 스크롤 이벤트 리스너
window.addEventListener('scroll', () => {
    if (!isLoading && hasMoreData && window.innerHeight + window.scrollY >= document.documentElement.offsetHeight - 100) {
        loadMoreData();
    }
});

async function loadMoreData() {
    if (isLoading || !hasMoreData) return;  // 로딩 중또는 불러올 데이터가 없을시
    isLoading = true;

    const filter = document.querySelector('input[name="filter"]:checked')?.value;

    try {
        const response = await fetch(`/feed/reload?filter=${filter}&page=${currentPage}`);
        const result = await response.json();


        if (!Array.isArray(result.data)) {
            console.error('Expected an array but received:', result.data);
            hasMoreData = false;
            return;
        }

        const feedList = result.data;
        hasMoreData = result.hasMoreData;

        feedList.forEach(feed => {
            const feedItem = document.createElement('div');
            feedItem.className = 'feed-ele';
            let innerResultHtml = `
                <div class="feed-out-box">
                <div class="feed-head-box">
                <div class="feed-title-box">
                <p class="feed-title">${feed.feedTitle}</p>
            <div class="feed-icons">
                <a href="#" class="badge ms-auto feed-heart" onclick="feedLikeBtn(this)">
                    <i class="bi-heart feed-heart-icon"></i>
                </a>
                <div class="dropdown-more">
                    <a class="more-drop-btn more-icon">
                        <img class="more-icon-image" src="/image/icon_more.png" alt="More"/>
                    </a>
                    <div class="more-dropdown-content">
                        <input type="hidden" value="${feed.feedNo}">
                        <div class="more-ele">신고하기</div>
                        <div class="more-ele" onclick="deleteFeed(this)">삭제</div>
                        <div class="more-ele" th:attr="onclick='location.href=\\'feed/modify?feedNo='+${feed.feedNo}+'\\''">수정</div>
                    </div>
                </div>
            </div>
        </div>
            <div class="feed-icon-box">
                <p class="feed-nickname">${feed.basicUserNickname}</p>
                <p class="feed-like-cnt">${feed.likeCount}</p>
            </div>
            <div class="feed-remark">
                <p class="feed-indate">${feed.feedTag !== 'none' ? feed.feedTag : ''}</p>
                <p class="feed-indate">${feed.feedIndate}</p>
            </div>
        </div>
            <div class="feed-content-box">
                <span class="feed-content">${feed.feedContent}</span>
            </div>
        </div>
            <div class="reply-list-submit-box" id="reply-list-submit-box">
                <div class="feed-reply-box">
                    <input type="hidden" value="${feed.feedNo}">
                        <div class="feed-reply-main">`

                            feed.comments.forEach(comment => {
                                innerResultHtml += `
                                <div class="feed-reply-ele">
                                    <div class="reply-profile-box">
                                        <input type="hidden" value="${comment.commentNo}">
                                        <p class="reply-nickname">${comment.commentUserNickname}</p>
                                        <p class="reply-indate">${comment.commentIndate}</p>
                                        <a class="bi-x reply-delete"></a>
                                    </div>
                                    <div class="reply-content-box">
                                        <span class="reply-content">${comment.commentContent}</span>
                                    </div>
                                </div>`
                            });
                            innerResultHtml += `
                        </div>
                </div>
                <div class="answer-reply-box">
                    <div class="answer-text-box">
                        <textarea class="answer-text"></textarea>
                        <button class="answer-submit" id="comment-submit-btn">등록</button>
                    </div>
                </div>
            </div>`;


        feedItem.innerHTML = innerResultHtml;
        document.getElementById('feed-main-box').appendChild(feedItem);
        });

        currentPage += 1; // 다음 페이지를 위한 시작 값 업데이트
        } catch (error) {
            console.error('Error during fetch:', error);
        } finally {
            isLoading = false;
        }
}
