    function imageUploader(file, el) {
    	var formData = new FormData();
    	formData.append('file', file);

    	$.ajax({
    		data : formData,
    		type : "POST",
            // url은 자신의 이미지 업로드 처리 컨트롤러 경로로 설정해주세요.
    		url : '/post/image-upload',
    		contentType : false,
    		processData : false,
    		enctype : 'multipart/form-data',
    		success : function(data) {
    			$(el).summernote('insertImage', "${pageContext.request.contextPath}/assets/images/upload/"+data, function($image) {
    				$image.css('width', "100%");
    			});
                // 값이 잘 넘어오는지 콘솔 확인 해보셔도됩니다.
    			console.log(data);
    		}
    	});
    };
