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
        const editorContent = document.getElementById('editor').innerHTML;
        // 수정된 내용을 서버로 전송하는 로직을 여기에 작성합니다.
        console.log(editorContent);
    });
});
