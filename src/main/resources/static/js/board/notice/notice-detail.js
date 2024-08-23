document.addEventListener('DOMContentLoaded', function() {
    const deleteBtn = document.querySelector('.delete-btn');

    deleteBtn.addEventListener('click', function() {
        const noticeId = this.getAttribute('data-id');

        if (confirm('정말로 삭제하시겠습니까?')) {
            fetch(`/notices/delete?id=${noticeId}`, {
                method: 'POST',  // POST 메서드 사용
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(response => response.text().then(text => ({ status: response.status, body: text })))
            .then(result => {
                if (result.status === 200) {
                    Swal.fire('알림', result.body || '공지사항이 삭제되었습니다.', 'success')
                        .then(() => window.location.href = '/notices/list');
                } else if (result.status === 403) {
                    Swal.fire('알림', result.body || '권한이 없습니다.', 'error');
                } else {
                    Swal.fire('알림', result.body || '공지사항 삭제에 실패했습니다.', 'error');
                }
            })
            .catch(error => {
                Swal.fire('알림', '서버 에러가 발생했습니다.', 'error');
                console.error('Error:', error);
            });
        }
    });
});
