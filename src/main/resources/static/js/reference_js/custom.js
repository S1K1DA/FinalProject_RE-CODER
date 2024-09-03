
(function ($) {

    "use strict";

    // MENU
    $('.navbar-collapse a').on('click',function(){
        $(".navbar-collapse").collapse('hide');
    });

    // CUSTOM LINK
    $('.smoothscroll').click(function(){
        var el = $(this).attr('href');
        var elWrapped = $(el);
        var header_height = $('.navbar').height();

        scrollToDiv(elWrapped,header_height);
        return false;

        function scrollToDiv(element,navheight){
            var offset = element.offset();
            var offsetTop = offset.top;
            var totalScroll = offsetTop-0;

            $('body,html').animate({
                scrollTop: totalScroll
            }, 300);
        }
    });

    $('.owl-carousel').owlCarousel({
        center: true,
        loop: true,
        margin: 30,
        autoplay: true,
        // autoplay: false,
        responsiveClass: true,
        responsive:{
            0:{
                items: 2,
            },
            767:{
                items: 3,
            },
            1200:{
                items: 4,
            }
        }
    });

    // nav의 알림창 드롭다운
    $(".nav-bell-icon").on("click", function(e) {
        e.preventDefault(); // 기본 동작 방지

        var $dropdown = $("#bell-dropdown");
        var $bellIcon = $(".bell-icon");

        if ($dropdown.hasClass("show")) {
            $dropdown.removeClass("show").addClass("hide");
            $bellIcon.removeClass("active");
            setTimeout(function() {
                $dropdown.removeClass("hide").hide();
            }, 300); // 애니메이션 시간과 동일하게 설정
        } else {
            $dropdown.show().removeClass("hide").addClass("show");
            $bellIcon.addClass("active");
        }
    });

    $(document).on("click", function(e) {
        if (!$(e.target).closest('.dropdown-bell').length) {
            var $dropdown = $("#bell-dropdown");
            var $bellIcon = $(".bell-icon");
            if ($dropdown.hasClass("show")) {
                $dropdown.removeClass("show").addClass("hide");
                $bellIcon.removeClass("active");
                setTimeout(function() {
                    $dropdown.removeClass("hide").hide();
                }, 300); // 애니메이션 시간과 동일하게 설정
            }
        }
    });

    $(document).on("click", function(e) {
        if (!$(e.target).closest('.dropdown-more').length) {
            $(".more-dropdown-content").slideUp(300);
        }
    });

})(window.jQuery);


