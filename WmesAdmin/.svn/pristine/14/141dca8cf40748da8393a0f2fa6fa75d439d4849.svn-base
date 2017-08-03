// JavaScript Document
$(document).ready(function(){

	  $("#wmes_left_menu > li").on("click",function(){
	 		$(this).siblings().find(".left-sub-icon").removeClass('ico');
	 		$(this).siblings().find(".left-sub-list").slideUp('fast');
	 		$(this).find(".left-sub-icon").toggleClass('ico');
		   $(this).find(".left-sub-list").slideToggle('fast');
	 });
	 $(".left-sub-list li").on("click",function(){
	 	event.stopPropagation(); 
	 });
	
	
		// $(".left-sub-list li").click(function(){			
		// 	$('.left-sub-list li').removeClass();
		// 	$(this).addClass('left-li-bg-crr');
		// });

		// tab
		$(".wems_tab li").on("click",function(){
			$(this).siblings().removeClass("now").end().addClass("now");
			$(".wmes_tab_contens").children().removeClass("now").eq($(this).index()).addClass("now");
			$(".wmes_tab_pagination").children().removeClass("now").eq($(this).index()).addClass("now");
		});

		// 坐标栏
		 var wmes_left_nav = $("#wmes_wraper").offset().top;
	      window.wemeMenuFixed = function(){
	        var wmes_window_top = $(window).scrollTop();
	        if (wmes_window_top >= wmes_left_nav) {
	          $('#wmes_left_nav').css('top',0);
	        }else{
	          $('#wmes_left_nav').css('top',132 - $(window).scrollTop() );
	        }
	      };
      $(window).on('scroll',wemeMenuFixed);

});


function getUrlParam(name){  
    //构造一个含有目标参数的正则表达式对象  
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
    //匹配目标参数  
    var r = window.location.search.substr(1).match(reg);  
    //返回参数值  
    if (r!=null) return unescape(r[2]);  
    return null;  
}