/**
 * wmesGeneral.js  new wmes general JS
 * @date: 2016-08-15
 */


define(function(require) {

    var $ = require('jquery');
    	require('iscroll');

    $(".ls-menu").each(function(){
        $(this).height($(this).children().length * 105);
    });
    
    var obj = document.getElementById("ls-menu-wrap");
    if(obj){
		var	myScroll = new IScroll('#ls-menu-wrap', {
			scrollbars: true,
			mouseWheel: true,
			interactiveScrollbars: true,
			shrinkScrollbars: 'scale',
			fadeScrollbars: true
		});
    }
    
	document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

    $(".ls-menu > li").on("mouseenter",function(){
        $(this).addClass("li-menu-active");
    });
    $(".ls-menu > li").on("mouseleave",function(){
        $(this).removeClass("li-menu-active");
    });
    $("#login-portrait-img").on("click",function(){
        $(this).parent().toggleClass("login-portrait-active");
    });
    $("#wmes-menu-show").on("click",function(){
        $(this).toggleClass("wmes-menu-close").toggleClass("wmes-menu-open");
        $("#ls-menu-more").toggleClass("ls-menu-hide");
    });

	$(".wmes_window").css("min-height",$(window).height() - $(".wmes_foot").height())
	$(".register_background_pic").css("min-height",$(window).height() - $(".wmes_foot").height() - $(".wmes_top").height());
	$(window).on('resize',function(){
	        $(".wmes_window").css("min-height",$(window).height() - $(".wmes_foot").height())
	        $(".register_background_pic").css("min-height",$(window).height() - $(".wmes_foot").height() - $(".wmes_top").height());
	});
    $(".login_background").css("height",$(window).height() - 80);
    
});