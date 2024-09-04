$(document).ready(function () {
    let isLoading = false;
    let hasMoreData = true;
    let currentPage = 2;

    // 스크롤 이벤트 처리
    $(window).on('scroll', function () {
        if (!isLoading && hasMoreData && $(window).scrollTop() + $(window).height() >= $(document).height() - 350) {
            loadMoreData();
        }
    });

    async function loadMoreData() {
        if (isLoading || !hasMoreData) return;
        isLoading = true;

        const filter = $('input[name="filter"]:checked').val();

        try {
            const response = await fetch(`/feed/reload?filter=${filter}&page=${currentPage}`);
            const result = await response.json();

            if (!Array.isArray(result.data)) {
                console.error('배열이 예상되었지만 받은 데이터:', result.data);
                hasMoreData = false;
                return;
            }

            const feedList = result.data;
            const thisUserNo = result.thisUserNo;
            hasMoreData = result.hasMoreData;

            feedList.forEach(feed => {
                const feedItem = $('<div class="feed-ele"></div>');
                const likedUser = Array.isArray(feed.likedUser) ? feed.likedUser : [];

                let innerResultHtml = `
                <div class="feed-out-box">
                    <div class="feed-head-box">
                        <div class="feed-title-box">
                            <p class="feed-title">${feed.feedTitle}</p>
                            <input type="hidden" id="feedNo" value="${feed.feedNo}">
                            <div class="feed-icons" ${thisUserNo !== 0 ? '' : 'style="display: none;"'}>
                                ${likedUser.includes(thisUserNo) ? `
                                    <a class="badge ms-auto feed-heart-cancel" id="like-heart" onclick="feedLikeCancelBtn(this)">
                                        <i class="bi-heart feed-heart-icon"></i>
                                    </a>` : `
                                    <a class="badge ms-auto feed-heart" id="like-heart" onclick="feedLikeBtn(this)">
                                        <i class="bi-heart feed-heart-icon"></i>
                                    </a>`}
                                <div class="dropdown-more">
                                    <a class="more-drop-btn more-icon">
                                        <img class="more-icon-image" src="/image/icon_more.png" alt="More"/>
                                    </a>
                                    <div class="more-dropdown-content">
                                        <input type="hidden" value="${feed.feedNo}">
                                        <div class="more-ele" id="reportBtn">신고하기</div>
                                        ${thisUserNo === feed.authorUserNo ? `
                                            <div class="more-ele" id="deleteFeedBtn">삭제</div>
                                            <div class="more-ele" id="editFeedBtn">수정</div>` : ''}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="feed-icon-box">
                            <p class="feed-nickname">${feed.basicUserNickname}</p>
                            <p class="feed-like-cnt bi-heart">
                                <span>${feed.likeCount}</span>
                            </p>
                            <input type="hidden" id="userNo" value="${feed.authorUserNo}">
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
                <div class="reply-list-submit-box">
                    <div class="feed-reply-box">
                        <input type="hidden" value="${feed.feedNo}">
            `;

                feed.comments.forEach(comment => {
                    innerResultHtml += `
                    <div class="feed-reply-main">
                        <div class="feed-reply-ele">
                            <div class="reply-profile-box">
                                <input type="hidden" value="${comment.commentNo}">
                                <p class="reply-nickname">${comment.commentUserNickname}</p>
                                <p class="reply-indate">${comment.commentIndate}</p>
                                ${comment.commentUserNo === thisUserNo && thisUserNo !== 0 ? `
                                    <a class="bi-x reply-delete" onclick="deleteReply(this)"></a>` : ''}
                            </div>
                            <div class="reply-content-box">
                                <span class="reply-content">${comment.commentContent}</span>
                            </div>
                        </div>
                    </div>
                `;
                });

                innerResultHtml += `
                    </div>
                    <div class="answer-reply-box">
                        <div class="answer-text-box">
                            ${thisUserNo !== 0 ? `
                                <textarea class="answer-text" placeholder="댓글을 입력해주세요."></textarea>
                                <button class="answer-submit" id="comment-submit-btn">등록</button>` : ''}
                        </div>
                    </div>
                </div>
            `;

                feedItem.html(innerResultHtml);
                $('#feed-main-box').append(feedItem);
            });

            currentPage += 1;
        } catch (error) {
            console.error('데이터를 가져오는 중 오류 발생:', error);
        } finally {
            isLoading = false;
        }
    }

    // 드롭다운 토글 이벤트
    $(document).on('click', '.more-drop-btn', function (e) {
        e.preventDefault();
        $(this).siblings('.more-dropdown-content').slideToggle(300);
    });

    $(document).on('click', function (event) {
        const $target = $(event.target);

        if (!$target.closest('.dropdown-more').length) {
            $(".more-dropdown-content").slideUp(300);
        }
    });

    // 댓글 엔터키 이벤트
    $(document).on('keydown', '.answer-text', function (event) {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault(); // 새 줄로 넘어가는 기본 동작 방지

            // 현재 textarea와 가장 가까운 등록 버튼을 찾아 클릭
            const $submitButton = $(this).closest('.feed-ele').find('.answer-submit');
            $submitButton.click(); // 등록 버튼 클릭
        }
    });

    // 댓글 등록 버튼 클릭 이벤트
    $(document).on('click', '.answer-submit', function () {
        const $feedBox = $(this).closest('.feed-ele');
        const feedNo = $feedBox.find('input[type="hidden"]').val();
        const commentContent = $feedBox.find('.answer-text').val().trim();
        const userNo = $feedBox.find('.this-user-no').val().trim();

        if (commentContent === '') {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

        fetch('/feed/addcomment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                feedNo: feedNo,
                commentContent: commentContent,
                commentUserNo: userNo
            })
        })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    throw new Error('댓글 등록 실패');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('댓글 등록 중 오류가 발생했습니다.');
            });
    });

    // 댓글 삭제 버튼 클릭 이벤트
    window.deleteReply = function (element) {
        Swal.fire({
            title: '정말로 이 댓글을 삭제하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '삭제',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                const $replyElement = $(element).closest('.feed-reply-ele');
                const commentNo = $replyElement.find('input[type="hidden"]').val();

                fetch('/feed/deletecomment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ commentNo: commentNo })
                })
                    .then(response => {
                        if (response.ok) {
                            $replyElement.remove();
                            Swal.fire({
                                icon: 'success',
                                title: '삭제 완료',
                                text: '댓글이 성공적으로 삭제되었습니다.'
                            });
                        } else {
                            throw new Error('댓글 삭제 실패');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        Swal.fire({
                            icon: 'error',
                            title: '오류',
                            text: '댓글 삭제 중 오류가 발생했습니다.'
                        });
                    });
            }
        });
    };

    // 피드 삭제 버튼 클릭 이벤트
    window.deleteFeed = function (element) {
        Swal.fire({
            title: '정말로 이 글을 삭제하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '삭제',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                const $feedElement = $(element).closest('.feed-ele');
                const feedNo = $feedElement.find('input[type="hidden"]').val();

                fetch('/feed/delete', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ feedNo: feedNo })
                })
                    .then(response => {
                        if (response.ok) {
                            $feedElement.remove();
                            Swal.fire({
                                icon: 'success',
                                title: '삭제 완료',
                                text: '피드가 성공적으로 삭제되었습니다.'
                            });
                        } else {
                            throw new Error('피드 삭제 실패');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        Swal.fire({
                            icon: 'error',
                            title: '오류',
                            text: '피드 삭제 중 오류가 발생했습니다.'
                        });
                    });
            }
        });
    };

    // 피드 좋아요 버튼 클릭 이벤트
    window.feedLikeBtn = function (element) {
        const $feedElement = $(element).closest('.feed-ele');
        const feedNo = $feedElement.find('input[type="hidden"]').val();

        fetch(`/feed/like?feedNo=${feedNo}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: '좋아요 성공!',
                    }).then(() => {
                        window.location.reload();
                    });
                } else {
                    throw new Error('좋아요 실패');
                }
            })
    };

    // 피드 좋아요 취소 버튼 클릭 이벤트
    window.feedLikeCancelBtn = function (element) {
        const $feedElement = $(element).closest('.feed-ele');
        const feedNo = $feedElement.find('input[type="hidden"]').val();

        fetch(`/feed/like-cancel?feedNo=${feedNo}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    Swal.fire({
                        icon: 'success',
                        title: '좋아요 취소',
                    }).then(() => {
                        window.location.reload();
                    });
                } else {
                    throw new Error('좋아요 취소 실패');
                }
            })
    };

    // 신고하기 버튼 클릭 이벤트
    $(document).on('click', '.more-ele#reportBtn', function () {
        const $feedElement = $(this).closest('.feed-ele');
        const feedNo = $feedElement.find('input[type="hidden"][id="feedNo"]').val();
        const reportedUserNo = $feedElement.find('#userNo').val();
        const reportedUser = $feedElement.find('.feed-nickname').text();

        $('#feedNo').val(feedNo);
        $('#reportedUserNo').val(reportedUserNo);
        $('#reportedUser').text(reportedUser);

        $('#reportOverlay').css('display', 'flex');
    });

    // 신고하기 창 닫기 버튼 클릭 이벤트
    $('#actionCloses').on('click', function () {
        $('#reportOverlay').css('display', 'none');
    });

    // 신고하기 폼 제출 이벤트
    $('#reportForm').on('submit', async function (event) {
        event.preventDefault();
        const formData = new FormData(this);

        try {
            const result = await Swal.fire({
                title: "신고하시겠습니까?",
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "rgb(255 128 135)",
                cancelButtonColor: "rgb(150 150 150)",
                confirmButtonText: "예",
                cancelButtonText: "아니요"
            });

            if (result.isConfirmed) {
                const response = await fetch(this.action, {
                    method: 'POST',
                    body: formData
                });

                if (response.ok) {
                    await Swal.fire({
                        title: "신고 완료",
                        icon: "success"
                    }).then(() => {
                        window.location.reload();
                    });
                } else {
                    throw new Error('신고 실패');
                }
            }
        } catch (error) {
            console.error(error);
            await Swal.fire({
                title: "오류 발생",
                text: "신고 처리 중 오류가 발생했습니다.",
                icon: "error"
            });
        }
    });

    // 필터 버튼 클릭 이벤트
    $(document).on('click', 'input[name="filter"]', function () {
        const filterValue = $(this).val();
        const url = new URL(window.location.href);
        url.searchParams.set('filter', filterValue);
        window.location.href = url.toString();
    });

    // 정렬 변경 이벤트
    $('#feed-array').on('change', function () {
        const selectedValue = $(this).val();
        const url = new URL(window.location.href);
        url.searchParams.set('feedarray', selectedValue);
        window.location.href = url.toString();
    });

});
