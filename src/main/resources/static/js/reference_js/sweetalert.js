
function yesOrNo(title, icon, confitmColor, cancelColor, confitmText, cancelText, resultTitle, resultIcon, url){
    Swal.fire({
        title: title,
        icon: icon,
        showCancelButton: true,
        confirmButtonColor: confitmColor,
        cancelButtonColor: cancelColor,
        confirmButtonText: confitmText,
        cancelButtonText: cancelText
    }).then((result) => {
        if (result.isConfirmed) {
            return Swal.fire({
                title: resultTitle,
                icon: resultIcon
            });
        } else {
            return Promise.reject();
        }
    }).then(() => {
        if(url !== ''){
            location.href = url;
        }
    });
}

