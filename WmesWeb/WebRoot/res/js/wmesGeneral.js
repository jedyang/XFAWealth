define(function(require) {

    var $ = require('jquery');
		require('footclick');
    	require('iscroll');

    function GetUrlRelativePath(){
      //回显菜单
　　　var url = document.location.toString()
        , arrUrl = url.split("//")
        , start = arrUrl[1].indexOf("/")
        , relUrl = arrUrl[1].substring(start);

　　　　if(relUrl.indexOf("?") != -1){
　　　　　　relUrl = relUrl.split("?")[0];
　　　　}

        $(".j-ls-menu").each(function(){
            var _href = $(this).children("a").attr("href");
            if(_href == relUrl){
                $(this).addClass("li-menu-old");
            }
        });
        $(".ls-small-menu li").each(function(){
            var _href = $(this).children("a").attr("href");
            if(_href == relUrl){
                $(this).addClass("li-menu-old");
                $(this).parents(".j-ls-menu").addClass("li-menu-old");
            }
        });
　　}

    setTimeout(GetUrlRelativePath,100);

    $(".ls-menu").each(function(){
        $(this).height($(this).children().length * 85 + 40);
    });
    
    var obj = document.getElementById("ls-menu-wrap");
    if(!!obj){	
	var	myScroll = new IScroll('#ls-menu-wrap', {
        scrollbars: true, 
        interactiveScrollbars: true, 
        shrinkScrollbars: 'scale', 
        fadeScrollbars: true, 
        click: true });
         }
//	document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

    $(document)
    .on("mouseover",".ls-menu > li",function(){
        $(this).addClass("li-menu-active");})
    .on("click",".ls-menu > li",function(){
        $(this).siblings().removeClass("li-menu-active").end().addClass("li-menu-active");})
    .on("mouseleave",".ls-menu > li",function(){
        $(this).removeClass("li-menu-active");})

    $("#login-portrait-img").on("click",function(){
        $(this).parent().toggleClass("login-portrait-active");
    });

    $(".ls-port-ico").on("click",function(){
        $(this).parents(".ls-menu-port").toggleClass("ls-port-active");

    });
    setTimeout(function(){
    $(".small-menu-wrap").each(function(){
        var _lenght = $(this).find("li").length;
        if(_lenght == 1){
            $(this).width(118);
        }else if(_lenght == 2){
            $(this).width(236);
        }
    });
},500)
    $(".login_background_pic").css("height",$(window).height()-145);

    $(".ls-menu-narrow").on("click",function(){
        $(this).parents(".wmes-wraper").toggleClass("ls-menu-none");

        if( $(this).parents(".wmes-wraper").hasClass("ls-menu-none")){
            $(".left-s-menu").parents(".wmes-wraper").removeClass("li-menu-mouse");
            $(".wmes-logo").addClass("animated zoomIn");
            setTimeout(function(){
               $(".wmes-logo").removeClass("animated zoomIn"); 
           },2000);
        }
        $(window).resize();
    });

    $(".wmes-logo").mousemove(function() {
        $(".left-s-menu").parents(".wmes-wraper").addClass("li-menu-mouse"); 
    });


    $(".left-s-menu").mouseleave(function(){
        $(".left-s-menu").parents(".wmes-wraper").removeClass("li-menu-mouse");
    });

    $(".wmes-off").on("click",function(){
        alert("开发中,敬请期待!");
    });
	
	function windowHeight(){
		var windowHeight = $(window).height();
		windowHeight = windowHeight-70;
		if($('.wmes-content').height() < windowHeight){
				$('.wmes-content').css('min-height',windowHeight);
                $('.wmes-notop-content').css('min-height',windowHeight);
		};
	};
	setTimeout(function(){
			windowHeight();
	},200);
	
	
	function loginHeight(){
		var loginHeight = $(window).height();
		loginHeight = loginHeight-80;
		if($('.login_background').height() < loginHeight){
				$('.login_background').css('min-height',loginHeight);
		};
	}
	loginHeight();
	
	//foot位置固定
	function windowHeight(){
		var windowHeight = $(window).height();
		windowHeight = windowHeight-90;
		if($('.wmes-content').height() < windowHeight){
				$('.wmes-content').css('min-height',windowHeight);
            	$('.wmes-notop-content').css('min-height',windowHeight);
		};
	};
	
	if(window.history.length==1){
		$('.wmes-ruturn').css('display','none');
	};
	
	/*$(document).on('click','.funds_heart',function(){
		$('.wmes-myfavourites-groupcon').css('display','block');
	});*/
	
	$(document).on('click','.wmes-myfavourites-groupcon .wmes-close',function(){
		$(this).closest('.wmes-myfavourites-groupcon').css('display','none');
	});
	
	$(document).on('click','.wmes-myfavourites-group li span',function(){
		$(this).prev().css('border','1px solid #dcdadb');
		$(this).prev().removeAttr('readonly');
	});
	
    //获取聊天数
    function Getchat(num){
        var _num = num;

        if(_num > 0){
            var _html = '<div class="wmes-chat-number">'+ _num +'</div>';
            $(".ls-menu-discover").parents("li").append(_html);
            $(".ls-menu-messages").parentsUntil("li").append(_html);
        } 
    }

   
    
    /**************************获取未读消息数*********************************/
    getMsgCount();
    function getMsgCount(){
    	$.ajax({
    		type:'post',
    		datatype:'json',
    		url:base_root+'/front/ifa/info/getAllUnreadCount.do',
    		data:{},
    		success:function(json){
    			var count=json.count;
    			if(count > 0){
    	            var _html = '<div class="wmes-chat-number">'+ count +'</div>';
    	            $(".ls-menu-discover").parents("li").append(_html);
    	            $(".ls-menu-messages").parentsUntil("li").append(_html);
    	        } 
    			//console.log("msgCount",json);
    		}
    	})
    }
   
    /**************************获取未读消息数*********************************/
   
});