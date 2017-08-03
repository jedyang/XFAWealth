

define(function(require) {

	var $ = require('jquery');
		require("swiper");
		require("layui");
		
	 new Swiper('.pipeline-prospect .swiper-container', {
        slidesPerView: 'auto',
        grabCursor: true,
        preventClicks : false,
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
    });
    var SwiperBol = true;
	// document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);//监听touchmove 事件冒泡
	$("#pipelin-bell-ico").on("click",function(){
		$(this).parent().toggleClass("pipelin-bell-active");
	});
	$(".Small_cake_ico, #pipeline-search-close, #pipelin-bless").on("click",function(){
		$("#ifa-pipeline-search").toggleClass("ifa-space-active");
	});
	$("#pipelin-schedule, #pipelin-date-close").on("click",function(){
		$("#ifa-pipeline-date").toggleClass("ifa-space-active");
	});
	$(".pipelin-bell-tab li").on("click",function(){
		$(this).siblings().removeClass("bell-tab-active").end().addClass("bell-tab-active");
		$(".pipelin-bell-contents li").hide().eq($(this).index()).show();
	});

	$(".pipelin-bottom-more").on("click",function(){
		var selfIndex = $(this).parents(".pipelin-list").index();
		$(this).toggleClass("pipelin-more-show");
		if( $(this).parents(".pipelin-list-swiper").find(".pipelin-more-show").length > 0  ){
			$(this).parents(".pipelin-list-swiper").css("z-index",20 - selfIndex)
		}else{
			$(this).parents(".pipelin-list-swiper").css("z-index",0);
		}
	});

	$(".pipelin-date-title").on("click",function(){
		$(this).parent().toggleClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").toggleClass("pipelin-search-show");
	});

	$("#pipelin-search-close").on("click",function(){
		$(".pipelin-date-active").removeClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").removeClass("pipelin-search-show");
	});
	$(".noline_tab_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".pipeline-div").removeClass("pipeline-div-active").eq($(this).index()).addClass("pipeline-div-active");
		if(SwiperBol){
			 new Swiper('.pipeline-existing .swiper-container', {
		        slidesPerView: 'auto',
		        grabCursor: true,
		        nextButton: '.swiper-button-next',
		        prevButton: '.swiper-button-prev',
		    });
		    SwiperBol = false;
		}
		
	});
	$(".pipelin-contents-more").each(function(){
		var contentsNum = 0;
		$(this).find(".pipelin-list-eject").each(function(index){
			contentsNum++;
			$(this).css("left",228 * contentsNum);
		});
	});
	$(".pipelin-contents-more").on("click",function(){
		$(this).toggleClass("pipelin-contents-more-show");
		if($(this).hasClass("pipelin-contents-more-show")){
			var contentsNum = 0;
			$(this).find(".pipelin-list-eject").each(function(index){	
				contentsNum++;
			});
			$(this).css({
				"width":230 + 230 * contentsNum,
				"padding-right" : 230 * contentsNum + 11
			});
		}else{
			$(this).css({
				"width":230,
				"padding-right" : 10
			});	
		}
	});	
	
	$(".pipelin_xiala").on("click",function(){
		$(this).toggleClass("pipelin_xiala_active");
	});

	$(".plipelin-checkbox-ico").on("click",function(){
		$(this).toggleClass("plipelin-checkbox-ico-active");
		if($(this).hasClass("plipelin-checkbox-ico-active")){
			$(".pipeline-div .pipelin-list-contents").each(function(){
				if( $(this).find(".pipelin-list-heart").length <= 0){$(this).hide();}
			});
		}else{
			$(".pipeline-div .pipelin-list-contents").show();
		}
	});
	
	$("#pipelin-choice li").on('click',function(e){
		var selfIndex = $(this).index();
		if( selfIndex == 0){
			$(this).parents(".pipelin_xiala").toggleClass("pipelin_xiala_active").find("input").val("");
			$(".pipeline-div .pipelin-list-contents").show();
		}else{
			//临时写的切换数据
			$(this).parents(".pipelin_xiala").toggleClass("pipelin_xiala_active").find("input").val($(this).html());
			if( selfIndex == 1 ){
				$(".pipeline-div .pipelin-list-contents").each(function(index){
					if(index % 2 == 0){
						$(this).hide();
					}else{
						$(this).show();
					}			
				});
			}else if( selfIndex == 2 ){
				$(".pipeline-div .pipelin-list-contents").each(function(index){
					if(index % 2 != 0){
						$(this).hide();
					}else{
						$(this).show();
					}					
				});
			}
			
		}
		e.stopPropagation(); 
	});

	$(".pipelin-bell-contents li .pipelin-bell-mesWrap").on("click",function(){
		var seleNum = 	$(this).find(".pipelin-bell-num").html(),
			selfWrap = $(".pipeline-div-active .pipelin-list-contents");
			selfWrap.hide();
			if($(this).attr("data-type") == "kyc"){
				selfWrap = $(".pipeline-div-active .pipelin-kyc-img");
				for (var i = 0; i < seleNum; i++){
					var seflRom = Math.ceil(Math.random()*selfWrap.length-1);
					selfWrap.eq( seflRom ).parents(".pipelin-list-contents").show();
				}
			}else{
				for (var j = 0; j < seleNum; j++){
					selfWrap.eq( Math.ceil(Math.random()*selfWrap.length-1) ).show();
				}
			}
		
	});
	$(".wmes_choice li").on("click",function(){
		$(this).siblings().removeClass("wmes_choice_active").end().addClass("wmes_choice_active");
		if($(this).html() == "All"){
			$(".pipelin_choice-contents > li").show();
		}else{
			$(".pipelin_choice-contents > li").hide();
			$(".pipelin_choice-contents > li").eq(Math.ceil(Math.random()*9)).show();
			$(".pipelin_choice-contents > li").eq(Math.ceil(Math.random()*9)).show();
			$(".pipelin_choice-contents > li").eq(Math.ceil(Math.random()*9)).show();
		}
		
	});
	var disjson = {"person":[
	{"country":"China","name":"Daivl Senter","mobile":"+08613332978987","email":"ojzil1@ctner.org.cn"},
	{"country":"America","name":"Als Wenger","mobile":"+022134089787","email":"Wenger@ctner.org.cn"},
	{"country":"France","name":"Altetar Ta","mobile":"+02213408228987","email":"lovefamilty@ctner.org.cn"},
	{"country":"Britain","name":"Davil Owen","mobile":"+4413408978987","email":"owen@ctner.org.cn"},
	{"country":"Germany","name":"Senxcens Alex","mobile":"+32408978987","email":"alex@ctner.org.cn"},
	{"country":"Italy","name":"K.D.Lww","mobile":"+02313408978987","email":"K.D.Lww@ctner.org.cn"},
	{"country":"China","name":"Belilin","mobile":"+2313408978987","email":"Belilin@ctner.org.cn"},
	{"country":"America","name":"Motersaker","mobile":"+2313408978987","email":"Motersaker@ctner.org.cn"},
	{"country":"China","name":"Maxier SX","mobile":"+2313408978987","email":"Maxier@ctner.org.cn"},
	{"country":"France","name":"Ronaertior","mobile":"+2313408978987","email":"Ronaertior@ctner.org.cn"}],
	"success":true,"type":"1","total":"12"};
	
	 //加载6条数据
    function genInvestor(){
    	var list = disjson.person;
		var html = '';
		$(".home-investor-list").empty();
    	$.each(list,function(i, n) {
			var rand = parseInt(Math.random()*6,10);
			n = list[rand];
			if(i>5)return;
			var li = '<li class="home-investor-list-li">';
                li += '<p class="investor-list-name">'+n.name+'</p>';
                li += '<div class="investor-iof-wrap">';
                li += '<p class="investor-list-title">';
                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/ifa_phone_ico.png" alt="">';
                li += '<span class="investor-list-ifo">'+n.mobile+'</span>';
                li += '</p>';
                li += '<p class="investor-list-title">';
                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/ifa_email_ico.png" alt="">';
                li += '<span class="investor-list-ifo">'+n.email+'</span>';
                li += '</p>';
                li += '<div class="investor-chat-wrap">';
                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/investor_liaotian.png" alt="">';
                li += '<div class="investor-hide-chat">';
                li += '<img src="'+base_root+'/res/images/ifa/investor_wetchat.png" alt="">';
                li += '<img src="'+base_root+'/res/images/ifa/investor_phone.png" alt="">';
                li += '<img src="'+base_root+'/res/images/ifa/investor_line.png" alt="">';
                li += '</div>';
                li += '</div>';
                li += '</div>';  
                li += '<ul class="ifa-home-mask">';   
                li += '<li>Same Language</li>'; 
                li += '<li>Same Inv. Style</li>'; 
                li += '<li>'+ n.country +'</li>'; 
                li += '<li>Read My Insights</li>'; 
                li += '<li>No IFA yet</li>'; 
                li += '</ul>';   
                li += '</li>';
			html += li;
    	});
    	html = '<div type="portfolios"  class="page_left"></div>'+html;
    	html= html +'<div type="portfolios"  class="page_right" ></div>';
    	$('.home-investor-list').append(html).hide().fadeToggle(500);//.show('1000');
    }
	genInvestor();
	$("#ifa-pipeline-investors").on('click', '.page_left', '', function () {
    	genInvestor();
    	
    });
    
    $("#ifa-pipeline-investors").on('click', '.page_right', '', function () {
    	genInvestor();
    });

    $(".pipelin-date-bottom").on("click",function(){
		$("#ifa-pipeline-investors").toggleClass("ifa-space-active");
    });

    $("#pipelin-investors-close").on("click",function(){
    	$("#ifa-pipeline-investors").removeClass("ifa-space-active");
    });

    $("#ifa-pipeline-investors").on("click",".home-investor-list-li",function(){
    	$("#ifa-pipeline-investors").removeClass("ifa-space-active");
    });
    $(".pipelin-list-swiper").on("mouseenter",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").show();
    });
    $(".pipelin-list-swiper").on("mouseleave",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
    });

});