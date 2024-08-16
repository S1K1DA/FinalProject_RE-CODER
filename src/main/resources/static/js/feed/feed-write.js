document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('.toolbar a').forEach(aEl => {
        aEl.addEventListener('click', function (e) {
            e.preventDefault();
            const command = aEl.dataset.command;

            if (command === 'h1' || command === 'h2' || command === 'h3' || command === 'p') {
                document.execCommand('formatBlock', false, command);
            } else {
                document.execCommand(command);
            }
        });
    });

    const form = document.querySelector('.write-main-box');
    const editor = document.querySelector('.editor');
    const feedContent = document.getElementById('hidden-feedContent');

    // 폼 제출 시, contenteditable 내용을 hidden input으로 설정
    form.addEventListener('submit', function(event) {
        // contenteditable 내용을 가져와서 hidden input에 설정
        feedContent.value = editor.innerHTML;
    });
});


