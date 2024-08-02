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
        // Handle the content, e.g., send it to the server or display it elsewhere
        console.log(editorContent);
    });
});
