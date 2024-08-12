function imageUploader(file, el) {
    var formData = new FormData();
    formData.append('file', file);

    $.ajax({
        data : formData,
        type : "POST",
        url : '/post/image-upload',
        contentType : false,
        processData : false,
        enctype : 'multipart/form-data',
        success : function(data) {
            // 업로드된 이미지의 URL을 이용하여 이미지 삽입
            $(el).summernote('insertImage', data, function($image) {
                $image.css({
                    'width': '100%',
                    'max-width': '600px'
                });
            });
            console.log("Uploaded image URL: " + data);
        },
        error: function() {
            console.log("이미지 업로드 실패");
        }
    });
};
