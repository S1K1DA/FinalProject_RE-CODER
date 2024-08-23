document.addEventListener('DOMContentLoaded', (event) => {
    const toolbarButtons = document.querySelectorAll('.toolbar-btn');

    toolbarButtons.forEach(button => {
        button.addEventListener('click', () => {
            const command = button.getAttribute('data-command');
            document.execCommand(command, false, null);
        });
    });

    document.getElementById('fontSize').addEventListener('change', function() {
        const fontSize = this.value;
        document.execCommand('fontSize', false, fontSize);
    });

    document.querySelector('.submit-btn').addEventListener('click', () => {
        const noticeId = new URLSearchParams(window.location.search).get('id');  // URL에서 id 파라미터 가져오기
        const title = document.getElementById('title').value.trim();
        const content = document.getElementById('editor').innerHTML.trim();
        const noticePriority = document.getElementById('priority').checked ? 'Y' : 'N';

        // 유효성 검사: 제목과 내용을 확인
        if (!title) {
            Swal.fire('알림', '공지사항 제목을 작성하세요.', 'error');
            return;
        }

        if (!content) {
            Swal.fire('알림', '공지사항 내용을 작성하세요.', 'error');
            return;
        }

        // 수정할 내용을 서버로 전송하는 로직
        fetch(`/notices/edit`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                'id': noticeId,
                'title': title,
                'content': content,
                'noticePriority': noticePriority
            })
        })
        .then(response => {
            if (response.ok) {
                Swal.fire('알림', '공지사항이 수정되었습니다.', 'success')
                    .then(() => window.location.href = '/notices/list');
            } else {
                Swal.fire('알림', '공지사항 수정에 실패했습니다.', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire('알림', '서버 에러가 발생했습니다.', 'error');
        });
    });
});
