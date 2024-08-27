document.addEventListener('DOMContentLoaded', function () {
    // 댓글 등록 버튼에 이벤트 리스너 추가
    document.querySelectorAll('.answer-submit').forEach(button => {
        button.addEventListener('click', function () {
            // 현재 피드 번호와 댓글 입력 필드를 가져오기
            const feedBox = button.closest('.feed-ele');
            const feedNo = feedBox.querySelector('input[type="hidden"]').value;
            const commentContent = feedBox.querySelector('.answer-text').value.trim();

            const userNo = 2;

            if (commentContent === '') {
                alert('댓글 내용을 입력해주세요.');
                return;
            }

            // AJAX 요청
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
                        return window.location.reload();

                    } else {
                        throw new Error('댓글 등록 실패');
                    }
                }).catch(error => {
                console.error('Error:', error);
                alert('댓글 등록 중 오류가 발생했습니다.');
            });

        });
    });

    window.deleteReply = function (element) {
        Swal.fire({
            title: '정말로 이 댓글을 삭제하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '삭제',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                const replyElement = element.closest('.feed-reply-main');
                const commentNo = replyElement.querySelector('input[type="hidden"]').value;

                fetch('/feed/deletecomment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        commentNo: commentNo
                    }),
                })
                    .then(response => {
                        if (response.ok) {
                            replyElement.remove();
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

    window.deleteFeed = function (element) {
        Swal.fire({
            title: '정말로 이 글을 삭제하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '삭제',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                // 피드 요소를 찾고, 그 안에서 피드 번호를 찾습니다.
                const feedElement = element.closest('.feed-ele');
                const feedNo = feedElement.querySelector('input[type="hidden"]').value;

                fetch('/feed/delete', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        feedNo: feedNo
                    }),
                })
                .then(response => {
                    if (response.ok) {
                        feedElement.remove();
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

    window.feedLikeBtn = function (element){
        const feedElement = element.closest('.feed-ele');
        const likeHeart = document.getElementById('like-heart');
        const feedNo = feedElement.querySelector('input[type="hidden"]').value;

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
                });
                likeHeart.style.backgroundColor = '#d9294f';
                likeHeart.style.color = '#fff';

            } else {
                throw new Error('좋아요 실패');
            }
        })
    }


    document.querySelectorAll('.more-ele#reportBtn').forEach(function(button) {
        button.addEventListener('click', function() {
            // 클릭된 버튼의 부모 요소를 찾음
            const feedElement = this.closest('.feed-ele');

            // 피드 정보 가져오기
            const feedNo = feedElement.querySelector('input[type="hidden"][id="feedNo"]').value;
            const reportedUserNo = feedElement.querySelector('#userNo').value;
            const reportedUser = feedElement.querySelector('.feed-nickname').textContent;

            // 신고하기 창의 입력값 설정
            document.getElementById('feedNo').value = feedNo;
            document.getElementById('reportedUserNo').value = reportedUserNo;
            document.getElementById('reportedUser').textContent = reportedUser;

            // 신고하기 창 표시
            document.getElementById('reportOverlay').style.display = 'flex';
        });
    });

    document.getElementById('actionCloses').addEventListener('click', function() {
        document.getElementById('reportOverlay').style.display = 'none';
    });


    document.getElementById('reportForm').addEventListener('submit', async function(event) {
        event.preventDefault(); // 기본 제출 동작을 막습니다

        // 폼 데이터 가져오기
        const formData = new FormData(this);


        try {
            // 사용자에게 확인 메시지 표시
            const result = await Swal.fire({
                title: "신고하시겠습니까?",
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "rgb(255 128 135)",
                cancelButtonColor: "rgb(150 150 150)",
                confirmButtonText: "예",
                cancelButtonText: "아니요"
            });


            // 사용자가 확인 버튼을 클릭했는지 확인
            if (result.isConfirmed) {
                // 결제 취소 요청 서버에 전송
                // fetch를 사용하여 데이터를 비동기적으로 제출합니다
                const response = await fetch(this.action, {
                    method: 'POST',
                    body: formData
                });
                
                console.log(response.text());
                console.log(response.status);

                // 취소 완료 메시지 표시
                if (response.ok) {
                    await Swal.fire({
                        title: "신고 완료",
                        icon: "success"
                    }).then(() => {
                        window.location.reload();
                    });

                }else{
                    return Promise.reject();
                }
            }
        } catch (error) {
            // 에러가 발생한 경우 에러 메시지 표시
            console.error(error);
            await Swal.fire({
                title: "오류 발생",
                text: "신고 처리 중 오류가 발생했습니다.",
                icon: "error"
            });
        }

    });


});
