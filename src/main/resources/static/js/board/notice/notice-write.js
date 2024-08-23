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
        const title = document.getElementById('title').value.trim();
        const content = document.getElementById('editor').innerHTML.trim();
        const noticePriority = document.getElementById('priority').checked ? 'Y' : 'N';

        // 유효성 검사: 제목과 내용을 확인
        if (!title) {
            Swal.fire('알림', '공지사항 제목 작성하세요.', 'error');
            return;
        }

        if (!content) {
            Swal.fire('알림', '공지사항 내용 작성하세요.', 'error');
            return;
        }

        // 등록할 내용을 서버로 전송하는 로직
        fetch('/notices/new', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                'title': title,
                'content': content,
                'noticePriority': noticePriority
            })
        })
        .then(response => {
            if (response.ok) {
                // 요청이 성공하면 공지사항 목록 페이지로 이동
                Swal.fire('알림', '글 작성 완료!', 'success');
                window.location.href = '/notices/list';
            } else {
                Swal.fire('알림', '글 작성 실패!', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire('알림', '에러 발생!', 'error');
        });
    });
});
