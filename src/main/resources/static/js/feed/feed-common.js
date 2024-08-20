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
                        feedNo: feedNo
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

});
