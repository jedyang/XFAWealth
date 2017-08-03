
define(function(require) {
	//依赖
	var $ = require('jquery');
        //$(".ls-menu-discover").parents("li").addClass("li-menu-old");
		require("swiper");
		require('layer');
		//加载聊天工具
	 /*  var _chat=require('chat');
	   _chat.setType(1);*/
	   
	   
	  /* $(document).ready(function(){
		   $(".dialogue-message-ul").on("click",".discover-a-href",function(){
			   var memberId=$(this).parent().parent().attr("memberId");
			   var nickName=$(this).parent().parent().attr("nickName");
			   var iconUrl=$(this).parent().parent().find(".system-message-portrait").attr("src");
			   _chat.load(memberId,nickName,iconUrl);
		   })
	   })*/

	var swiperIndex = 0;

    switch (getQueryString('type')){
        case "Messages":
        swiperIndex = 0;
        //$(".ls-menu-messages").parents("li").addClass("li-menu-old");
        break;
        case "Corners":
        swiperIndex = 1;
        //$(".ls-menu-corners").parents("li").addClass("li-menu-old");
        break;
        case "News":
        swiperIndex = 2;
        //$(".ls-menu-news").parents("li").addClass("li-menu-old");
        break;
        case "Headline":
        swiperIndex = 3;
        //$(".ls-menu-headline").parents("li").addClass("li-menu-old");
        break;
        case "Live":
        swiperIndex = 4;
        //$(".ls-menu-live").parents("li").addClass("li-menu-old");
        break;
        case "Community":
        swiperIndex = 5;
        //$(".ls-menu-commnuity").parents("li").addClass("li-menu-old");
        break;
        default:
        swiperIndex = 0;
        //$(".ls-menu-messages").parents("li").addClass("li-menu-old");
        break;
    }
    // swiper
//    var swiper = new Swiper('.swiper-container', {
//        pagination: '.swiper-pagination',
//        initialSlide :swiperIndex,
//        paginationClickable: true,
//        preventClicks : false,
//        mousewheelControl : false,
//         onSlideChangeStart:function(swiper){
//            var nowHeight = $(".swiper-slide").eq($(".swiper-pagination-bullet-active").index()).find(".discover-wrap").height() + 40;
//            $(".swiper-container").height(nowHeight);
//        }
//    });
    function GetQueryString(name)
    {
         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
         var r = decodeURI(window.location.search).substr(1).match(reg);
         //原始地址没有参数，返回all
         if(r!=null)return  unescape(r[2]); return "";
    }
    if(GetQueryString('type') == "topic"){
        $(".swiper-pagination-bullet").eq(5).click();
    }else if(GetQueryString('type') == "news"){
        $(".swiper-pagination-bullet").eq(2).click();
    }    
    $(window).on('resize',function(){
       swiper.onResize();
    });
    //设置 swiper　高度
    $(".swiper-container").height($(".swiper-slide ").eq(0).height() + 40);
    // //点击重新获取 swiper 高度
    $(".swiper-pagination-bullet").on("click",function(){
        var nowHeight = $(".swiper-slide").eq($(".swiper-pagination-bullet-active").index()).find(".discover-wrap").height() + 40;
        $(".swiper-container").height(nowHeight);
    }); 

    $("#tab_news li").on("click",function(){
        $(this).siblings().removeClass("now").end().addClass("now");
        $(".discover-news-ul li").hide().eq($(this).index()).show();
    });
    $(".discover-live-tab li").on("click",function(){
        $(this).siblings().removeClass("now").end().addClass("now");
        $(".discover-live-rows").hide().eq($(this).index()).show();
    });
    
    
    // $("#dialogue-message-show, .space-mask-close").on("click",function(){
    //     $(".discover-space-popup").toggleClass("discover-space-active");
    // });

    $(".dialogue-message-ul li").on("click",function(){
        $(".discover-space-popup").toggleClass("discover-space-active");
        loaded();
    });
    $("#discover-chat-close").on("click",function(){
        $(".discover-space-popup").removeClass("discover-space-active");
    });

    $(".xfaWeekly").on("click",function(){
    	var weekly = layer.open({
    		type: 2,
    		content: 'http://en.xinfinance.com/html/Weekly/',
    		area: ['800px', '640px'],
    		offset:'rb',
    		maxmin: true
    	});
    	layer.full(weekly);
    });
    
    $(".xfaMarket").on("click",function(){
    	var market = layer.open({
    		type: 2,
    		content: 'http://en.xinfinance.com/plus/view.php?aid=254698',
    		area: ['800px', '640px'],
    		offset:'rb',
    		maxmin: true
    	});
    	layer.full(market);
    });
    
    $(".newsImg").on("click",function(){
    	var newsImg = layer.open({
    		type: 2,
    		content: 'http://en.xinfinance.com/html/Companies/2016/255257.shtml',
    		area: ['800px', '640px'],
    		offset:'rb',
    		maxmin: true
    	});
    	layer.full(newsImg);
    });
    
    $(".xfaWebcast").on("click",function(){
    	$('body,html').animate({scrollTop:0},100);
    	$(".swiper-pagination-bullet").eq(4).click();
    	$(".discover-live-tab li").eq(5).click();
    });
    
    $(".community-tab li").on("click",function(){
        $(this).siblings().removeClass("community-active").end().addClass("community-active");
    });

    $(".community-choose p").on("click",function(){
        $(this).siblings().removeClass("community-active").end().addClass("community-active");
    });


    
//  document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

    // var _chat = require("chat");
    // _chat.init();

    
});
