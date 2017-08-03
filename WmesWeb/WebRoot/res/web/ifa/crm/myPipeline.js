

define(function(require) {

	var $ = require('jquery');
		require("swiper");
		require("layui");

	new Swiper('.pipeline-prospect .swiper-container', {
	    slidesPerView: 'auto',
	    preventClicks : false,
	    nextButton: '.swiper-button-next',
	    prevButton: '.swiper-button-prev',
	});
	
	var setGroupListLiContent = '';//设置分组数据的内容
		 
    function getCustomerGroupList(){
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/crm/pipeline/crmGroupListJson.do?datestr='+new Date().getTime(),
				data : {
					'page':1,'rows':100,'sort':'l.createTime','order':''
				},
				async : false,
				success : function(json) {
									
					$("#pipelin-choice").empty();
					$("#my-group-list").empty();
					var divContent = '<li class="pipelin-show-all">Show All</li>';
					var liContent = '';
					setGroupListLiContent = '';
					
					var list = json.rows;
					//alert(list);
								
					$.each(list, function (index, array) { //遍历json数据列	
						var id = array['id'] == null ? "" : array['id'];
						var groupName = array['groupName'] == null ? "" : array['groupName'];
											                                  
						divContent += '<li class="pipelin-choice-group" data-value="'+id+'">'+groupName+'</li>';
						liContent += '<li class="my-group-li">'
									+ '<input type="text" maxlength="18" readonly="" class="group-list-word" data-value="'+id+'" value="'+groupName+'">'
									+ '<div class="group-list-ico">'
									+ '<span class="group-list-edit"></span>'
									+ '<span class="group-list-close"></span>'
									+ '</div>'
									+ '</li>';
						
						setGroupListLiContent += '<li data-group-id="'+id+'">'+groupName+'</li>';
						
					});
					
					divContent += '<li class="pipelin-new-group">New Group</li>';
									
					$("#pipelin-choice").append(divContent);
					$("#my-group-list").append(liContent);
				}
			})
	    }
		    

	    //load CustomerGroup
	    getCustomerGroupList();
	    
		 
		 $(document).on("click",function(event){
				//點擊其他地方隐藏元素
				var _pipelin_xiala = $('.pipelin_xiala'); 

				if(!_pipelin_xiala.is(event.target) && _pipelin_xiala.has(event.target).length === 0){ 
					_pipelin_xiala.removeClass("pipelin_xiala_active");  
				 }

				 $(".pipelin-bottom-more").each(function(){
				 	var self = $(this);
				 	if(!self.is(event.target) && self.has(event.target).length === 0){ 
						self.removeClass("pipelin-more-show");  
						
					 }

					 if( self.parents(".pipelin-list-swiper").find(".pipelin-more-show").length <= 0){
					 	self.parents(".pipelin-list-swiper").removeClass("pipelin-swiper-active");
					 }
				 });	

			});

	

	var saleStageId = "";
	var clientStatus = "";
    var SwiperBol = true;
	// document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);//监听touchmove 事件冒泡
	$(".pipelin-bell")
	.on("mouseenter",function(){
		$(this).addClass("pipelin-bell-active");})
	.on("mouseleave",function(){
        $(this).removeClass("pipelin-bell-active");
    });
	
	$(".Small_cake_ico, #pipeline-search-close, #pipelin-bless").on("click",function(){
		$("#ifa-pipeline-search").toggleClass("ifa-space-active");
	});
	/*$("#pipelin-schedule, #pipelin-date-close").on("click",function(){
		$("#ifa-pipeline-date").toggleClass("ifa-space-active");
	});*/
	
	$(".pipelin-bell-tab li").on("click",function(){
		$(this).siblings().removeClass("bell-tab-active").end().addClass("bell-tab-active");
//		$(".pipelin-bell-contents li").hide().eq($(this).index()).show();
	});

	$(document).on("click",".pipelin-bottom-more",function(){
		$(this).parents('.pipelin-list').css('height','186px');
		$('.pipelin-list-eject').css('display','none');
    	$(this).closest('li').css({"width":220,"padding-right" : 12});
		var selfIndex = $(this).parents(".pipelin-list").index();
		$(this).parents(".pipelin-list-swiper").addClass("pipelin-swiper-active");
		$(this).toggleClass("pipelin-more-show");
		$(this).closest('.pipelin-list-contents').addClass('kejiande');
		if( $(this).parents(".pipelin-list-swiper").find(".pipelin-more-show").length > 0  ){
			$(this).parents(".pipelin-list-swiper").css("z-index",20 - selfIndex)
		}else{
			$(this).parents(".pipelin-list-swiper").css("z-index",0);
		}
	});



	

	$(".pipelin-date-title").on("click",function(){
		$('.tanchutitle').text($(this).text());
		$(window).scrollTop('0');
		$('.pipeline-search-choose-existing').removeClass('pipeline-search-choose-existingac');
		if($(this).closest('.pipelin-list').index()==2){
			$('.pipeline-search-choose').eq(1).addClass('pipeline-search-chooseac').siblings().removeClass('pipeline-search-chooseac');
			$('.pipeline_choose_list').eq($(this).closest('.pipelin-list').index()).addClass('pipeline_choose_listac').siblings().removeClass('pipeline_choose_listac');
		}else{
			$('.pipeline-search-choose').eq(0).addClass('pipeline-search-chooseac').siblings().removeClass('pipeline-search-chooseac');
			$('.pipeline_choose_list').eq($(this).closest('.pipelin-list').index()).addClass('pipeline_choose_listac').siblings().removeClass('pipeline_choose_listac');
		}
		
		$(this).parent().toggleClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").toggleClass("pipelin-search-show");
		
		var clientType = $("#hdClientType").val();
		if( clientType == "0" ){
			saleStageId = $(this).attr("saleStageId");
			bindClient(saleStageId);
		}else{
			clientStatus = $(this).attr("clientStatus");
			bindExistingClient(clientStatus);
		}
		 
		
	});
	
	$(".pipelin-date-title-existing").on("click",function(){
		$('.tanchutitle').text($(this).text());
		$(window).scrollTop('0');
		$('.pipeline-search-choose').removeClass('pipeline-search-chooseac');
		if($(this).closest('.pipelin-list').index()==0){
			$('.pipeline-search-choose-existing').eq(0).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else if($(this).closest('.pipelin-list').index()==1){
			$('.pipeline-search-choose-existing').eq(1).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else if($(this).closest('.pipelin-list').index()==2){
			$('.pipeline-search-choose-existing').eq(2).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else if($(this).closest('.pipelin-list').index()==3){
			$('.pipeline-search-choose-existing').eq(3).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else if($(this).closest('.pipelin-list').index()==4){
			$('.pipeline-search-choose-existing').eq(4).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		}else{
			$('.pipeline-search-choose-existing').eq(5).addClass('pipeline-search-choose-existingac').siblings().removeClass('pipeline-search-choose-existingac');
		};
		
		$('.pipeline_choose_list1').eq($(this).closest('.pipelin-list').index()).addClass('pipeline_choose_listac').siblings().removeClass('pipeline_choose_listac');
		
		
		
		
		
		$(this).parent().toggleClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").toggleClass("pipelin-search-show");
		
		var clientType = $("#hdClientType").val();
		if( clientType == "0" ){
			saleStageId = $(this).attr("saleStageId");
			bindClient(saleStageId);
		}else{
			clientStatus = $(this).attr("clientStatus");
			bindExistingClient(clientStatus);
		}
		 
		
	});
	
	$(document).on('click','.pipelin-list-bottom img',function(){
		$('.pipelin-list-contents').removeClass('kejiande');
		$(this).closest('li').css({"width":220,"padding-right" : 12});
		$(this).closest('.pipelin-list-contents').find('.mask-layer').css('display','block');
		$(this).closest('.pipelin-list-contents').find('.touming').css('display','block');
		$(this).closest('.pipelin-list-contents').find('.touming').animate({'opacity':'0.9'},500);
	});
	
	$('.mask-layer').on('click',function(){
		if($(this).find('.mask-layerbox').css('right') == '-134px'){
			$(this).css('display','none');
			$(this).closest('.pipelin-list-contents').find('.touming').css('display','none');
			$(this).closest('.pipelin-list-contents').find('.touming').css('opacity','0');
		}
		
	});
	
	$("#pipelin-search-close").on("click",function(){
		$(".pipelin-date-active").removeClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").removeClass("pipelin-search-show");
	});
	
	$(".pipeline-search-close").on("click",function(){
		$(".pipelin-date-active").removeClass("pipelin-date-active");
		$(".pipelin-search-mask, .pipelin-list-search").removeClass("pipelin-search-show");
	});
	
	$(".pipelin-bell-wrap").css("width","410");
	$(".noline_tab_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".pipeline-div").removeClass("pipeline-div-active").eq($(this).index()).addClass("pipeline-div-active");
		$(".pipelin-bell-contents li").hide().eq($(this).index()).show();
		if($(this).index() == 0){
			$(".pipelin-bell-wrap").css("width","410");
			$("#hdClientType").val("0");
			$('.pipelin-bell-existing').css('display','none');
		}else {
			$(".pipelin-bell-wrap").css("width","410");
			$("#hdClientType").val("1");
			$('.pipelin-bell-existing').css('display','block');
		}
		
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
//	$(".pipelin-contents-more").on("click",function(){
//		$(this).toggleClass("pipelin-contents-more-show");
//	});
	
	$(".pipelin-contents-more").each(function(){
		var contentsNum = 0;
		$(this).find(".pipelin-list-eject").each(function(index){
			contentsNum++;
			$(this).css("left",228 * contentsNum);
		});
	});
	$("body").on("click",".pipelin-contents-more",function(){
		$(this).toggleClass("pipelin-contents-more-show");
		$('.pipelin-list-eject').css('display','block');
		$('.pipelin-list-contents').removeClass('kejiande');
		if($(this).hasClass("pipelin-contents-more-show")){
			var contentsNum = 0;
			$(this).find(".pipelin-list-eject").each(function(index){	
				contentsNum++;
			});
			$(this).css({
				"width":220 + 220 * contentsNum,
				"padding-right" : 220 * contentsNum + 13
			});
		}else{
			$(this).css({
				"width":220,
				"padding-right" : 12
			});	
		}
	});	
	
	$("body").on("click",".j-exclamatory-ico",function(){
		$(this).parent().toggleClass("pipelin-topper-active");
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
	
	
	$("body").on('click',"#pipelin-choice li",function(e){

		var self = $(this);

		if( self.hasClass("pipelin-show-all") ){
			//show All
			self.parents(".pipelin_xiala").toggleClass("pipelin_xiala_active").find("input").val("");
			
			$(".pipelin-list-contents").show();
		}else if( self.hasClass("pipelin-new-group") ){

			return;
		}else{
			self.parents(".pipelin_xiala").toggleClass("pipelin_xiala_active").find("input").val( self.html() );
			
			$(".pipelin-list-contents").hide();

			$(".pipelin-list-contents").each(function(index){


				var _gropu = $(this).attr("data-group");

				if(!_gropu) _gropu = '';

				if ( _gropu.indexOf( self.html() ) >= 0 ) {

					$(this).show();
				}

			});
		}
		e.stopPropagation(); 
	});
	
	//第二版页面
	$(".investor-chioce li p").hide();

	$(".investor-chioce-list .investor-chioce-button").on("click",function(e){
			$(this).toggleClass("investor-chioce-active");
			if ($(this).hasClass("investor-chioce-active")) {
				$(this).siblings('p').stop(true).show('1000');
				$(this).removeClass('investor-chioce-white');
			}else{
				$(this).siblings('p').stop(true).hide('1000');
				$(this).addClass('investor-chioce-white');
			}
			
	});
	$(".investor-region-list").on("click",function(){
		$(this).toggleClass("investor-region-active");
		//var saleStageId=$(".pipelin-date-active p").attr("saleStageId");
    	genInvestor(saleStageId);
	})
	$(".mid-inverstor-left").on("mousemove",".home-investor-list-li",function(){
        $(this).find(".ifa-home-mask").stop(true).animate({ opacity: "0.9"}, 100,"linear");
        
        
	});         
    $(".mid-inverstor-left").on("mouseleave",".home-investor-list-li",function(){
    	$(this).find(".ifa-home-mask").stop(true).animate({ opacity: "0"}, 100,"linear");
    });
    $(document).on("click",".investor-j-chat",function(){
    	$(this).siblings(".investor-hide-chat").toggleClass("investor-show-chat");
    });
    $(".investor-noyet").on("click",function(){
    	$(this).toggleClass("investor-chioce-white");
    
    	//var saleStageId=$(".pipelin-date-active p").attr("saleStageId");
    	genInvestor(saleStageId);
    })

	$(".pipelin-bell-contents li .pipelin-bell-mesWrap").on("click",function(){
		var seleNum = 	$(this).find(".pipelin-bell-num").html(),
		selfWrap = $(".pipeline-div .pipelin-list-contents");
		selfWrap.hide();
		$(this).toggleClass("bell-mesWrap-active");
		if($(this).hasClass("bell-mesWrap-active")){
				$(".pipelin-bell-contents li .pipelin-bell-mesWrap").removeClass("bell-mesWrap-active");
				$(this).addClass("bell-mesWrap-active");
				if($(this).attr("data-type") == "kyc"){
					selfWrap = $(".pipeline-div-active .pipelin-kyc-img");
					for (var i = 0; i < seleNum; i++){
						var seflRomKyc = Math.ceil(Math.random()*selfWrap.length);
						selfWrap.eq( seflRomKyc ).parents(".pipelin-list-contents").show();
					}
				}else{
					for (var j = 0; j < seleNum; j++){
						var seflRom = Math.ceil(Math.random()*selfWrap.length);
						selfWrap.eq( seflRom ).show();
					}
				}
		}else{
			selfWrap.show();
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
		//var saleStageId=$(".pipelin-date-active p").attr("saleStageId");
		var clientType = $("#hdClientType").val();
		if(clientType == "0"){
			bindClient(saleStageId);
		}else if(clientType == "1"){
			bindExistingClient(clientStatus);
		}
		
	});
	
	var pageIndex=1;
	 //加载6条数据
    function genInvestor(saleStageId){
    	var langCode = "";
		var styleCode = "";
		var intrestCode = "";
		var region="";
		$(".investor-chioce li .investor-region-active").each(function(i, n) {
			var type = $(n).parent().attr("type");
			if ("language" == type) {
				langCode += $(n).attr("code")+",";
			} else if ("style" == type) {
				styleCode += $(n).attr("code")+",";
			} else if ("interest" == type) {
				intrestCode += $(n).attr("code")+",";
			}else if ("region" == type) {
				region += $(n).attr("code")+",";
			}
		});
		var noIfa = "";
		if (!$(".investor-noyet").hasClass("investor-chioce-white")) {
			noIfa = "1";
		}
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+'/front/crm/pipeline/getInverstorList.do?datestr='+new Date().getTime(),
    		data:{'clientType': '0',
    			'areaId':'',
    			'period':'',
    			'saleStageId':saleStageId,
    			'page':pageIndex,
    			'rows':6,
    			'sort':'',
    			'order':'',
    				'langCode' : langCode,
    				'invStyle' : styleCode,
    				'intrest' : intrestCode,
    				'region':region,
    				'noIfa' : noIfa
    			},
    		success:function(json){
    			
//				var divContent = "";
				var list = json.rows;
				var html = '';
				$.each(list, function (i, array) { //遍历json数据列	
					var name=array['loginCode'] == null ? "" : array['loginCode'];
					var mobile= array['mobileNumber'] == null ? "" : array['mobileNumber'];
					var email= array['email'] == null ? "" : array['email'];
					var sameLang=array['sameLang'] == null ? "" : array['sameLang'];
					var sameStyle=array['sameStyle'] == null ? "" : array['sameStyle'];
					var country=array['country'] == null ? "" : array['country'];
					var read=array['readInsight'] == null ? "" : array['readInsight'];
					var noIfa=array['noIfa'] == null ? "" : array['noIfa'];
					var memberId=array['memberId'] == null ? "" : array['memberId'];
					
					var li = '<li class="home-investor-list-li" memberId="'+memberId+'">';
	                li += '<p class="investor-list-name">'+name+'</p>';
	                li += '<div class="investor-iof-wrap">';
	                li += '<p class="investor-list-title">';
	                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/ifa_phone_ico.png" alt="">';
	                li += '<span class="investor-list-ifo">'+mobile+'</span>';
	                li += '</p>';
	                li += '<p class="investor-list-title">';
	                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/ifa_email_ico.png" alt="">';
	                li += '<span class="investor-list-ifo">'+email+'</span>';
	                li += '</p>';
	                li += '<div class="investor-chat-wrap">';
	                li += '<img class="investor-list-add" memberId="'+memberId+'" src="'+base_root+'/res/images/ifa/addf.png" alt="">'
	                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/investor_liaotian.png" alt="">';
	                li += '<div class="investor-hide-chat">';
	                li += '<img src="'+base_root+'/res/images/ifa/investor_wetchat.png" alt="">';
	                li += '<img src="'+base_root+'/res/images/ifa/investor_phone.png" alt="">';
	                li += '<img src="'+base_root+'/res/images/ifa/investor_line.png" alt="">';
	                li += '</div>';
	                li += '</div>';
	                li += '</div>';  
	                li += '<ul class="ifa-home-mask" memberId="'+memberId+'">'; 
	                if(sameLang=="1")
	                li += '<li>Same Language</li>'; 
	                if(sameStyle=="1")
	                li += '<li>Same Inv. Style</li>'; 
	                li += '<li>'+country+'</li>'; 
	                if(read=="1")
	                li += '<li>Read My Insights</li>'; 
	                if(noIfa=="1")
	                li += '<li>No IFA yet</li>'; 
	                li += '</ul>';   
	                li += '</li>';
				html += li;
				});
				if(pageIndex>1)
				html = '<div type="portfolios"  class="page_left"></div>'+html;
		    	html= html +'<div type="portfolios"  class="page_right" ></div>';
		    	$('.home-investor-list').empty().append(html).hide().fadeToggle(500);//.show('1000');
		    	$(".home-investors-sum").text(json.total);
    		}
    	})
    }
	//genInvestor(saleStageId);
	$("#ifa-pipeline-investors").on('click', '.page_left', '', function () {
		pageIndex--;
    	genInvestor(saleStageId);
    	
    });
    
    $("#ifa-pipeline-investors").on('click', '.page_right', '', function () {
    	pageIndex++;
    	genInvestor(saleStageId);
    });

    $(".pipelin-date-bottom").on("click",function(){
		$("#ifa-pipeline-investors").toggleClass("ifa-space-active");

		saleStageId=$(this).parent().parent().find(".pipelin-date-title").attr("saleStageId");//$(".pipelin-date-active p").attr("saleStageId");
		genInvestor(saleStageId);
    });

    $("#pipelin-investors-close").on("click",function(){
    	$("#ifa-pipeline-investors").removeClass("ifa-space-active");
    });

    $("#ifa-pipeline-investors").on("click",".investor-list-add",function(){
    	
        var memberId=$(this).attr("memberId");
        if(memberId!=""){
    		$.ajax({
    			type:"post",
        		datatype:"json",
        		url:base_root+'/front/crm/pipeline/addCustomer.do?datestr='+new Date().getTime(),
        		data:{memberId:memberId,salesStageId:saleStageId,clientType:'0'},
        		success:function(json){
        			if(json.result){
        				window.location.href=base_root+"/front/crm/pipeline/clientList.do";
        			}else{
        				layer.msg("Fail to add Customer");
        			}
        		}
    		})
    	}
    	
    });
    $(".order-sapce-wrapper").on("mouseenter",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").show();
    });
    $(".order-sapce-wrapper").on("mouseleave",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
    });
    $("#btnSearch").on("click",function(){
    	 //saleStageId=$(".pipelin-date-active p").attr("saleStageId");
    	bindClient(saleStageId);
	});
   
    $(".order-plan-sapce").on("click",".order-space-rows",function(){
    	var memberId=$(this).attr("memberId");
    	
    	if(memberId!=""){
    		$.ajax({
    			type:"post",
        		datatype:"json",
        		url:base_root+'/front/crm/pipeline/addCustomer.do?datestr='+new Date().getTime(),
        		data:{memberId:memberId,salesStageId:saleStageId,clientType:'0'},
        		success:function(json){
        			if(json.result){
        				window.location.href=base_root+"/front/crm/pipeline/clientList.do";
        			}else{
        				layer.msg("Fail to add Customer");
        			}
        		}
    		})
    	}
    });
    
    function bindClient(saleStageId){
		var keyword = $('#pipelinKeyWord').val();
		var char=$(".wmes_choice .wmes_choice_active").html();
		if(char=="All")
			char="";
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/clientListJsonForIfa.do?datestr='+new Date().getTime(),
			data : {
				'clientType': '0','areaId':'','period':'','saleStageId':saleStageId,'keyword':keyword,'page':1,'rows':100,'sort':'','order':'',
				'char':char
			},
			success : function(json) {
								
				$(".pipelin_choice-contents").empty();
				var divContent = "";
				var list = json.rows;
				//alert(list);
							
				$.each(list, function (index, array) { //遍历json数据列	
					var id = array['id'] == null ? "" : array['id'];
					 var member_id = array['member_id'] == null ? "" : array['member_id'];
					// var first_name = array['first_name'] == null ? "" : array['first_name'];
					// var last_name = array['last_name'] == null ? "" : array['last_name'];		
					var nick_name = array['nick_name'] == null ? "" : array['nick_name'];			
					var mobile_code = array['mobile_code'] == null ? "" : array['mobile_code'];
					var mobile_number = array['mobile_number'] == null ? "" : array['mobile_number'];
					var email = array['email'] == null ? "" : array['email'];
					var create_time = array['create_time'] == null ? "" : array['create_time'];
					// var born = array['born'] == null ? "" : array['born'];
					var sales_stage_id = array['sales_stage_id'] == null ? "" : array['sales_stage_id'];
					//alert("first_name:"+first_name);
					var is_important = array['is_important'] == null ? "" : array['is_important'];
					var heart = '';
					if(is_important == '1'){
						heart = '<img class="pipelin-list-heart" src="'+base_root+'/res/images/ifa/white_heart_ico.png">';
					}
					
					//suspand
					var loss = '';
					if(sales_stage_id == 'sales_suspend'){
						loss = '<div class="pipelin-contents-loss">'
							+ '<div class="pipelin-loss-mask"></div>'
							+ '<div class="pipelin-loss-word">LOSS</div>'
							+ '</div>';
					}
					var remark = array['remark'] == null ? "" : array['remark'];
					var remark_flag = '';
					if( '' != remark ){
						remark_flag = '<img class="pipelin-date-exclamatory" src="'+base_root+'/res/images/ifa/exclamatory_ico.png">';
					}
					
					//birthday_remind
					var birthday_remind = array['birthday_remind'] == null ? "0" : array['birthday_remind'];
					var birthday_flag = '';
					if( birthday_remind > "0" ){
						birthday_flag = '<img class="Small_cake_ico" src="'+base_root+'/res/images/ifa/Small_cake_ico.png">';
					}
					
					//appointment_remind
					var appointment_remind = array['appointment_remind'] == null ? "0" : array['appointment_remind'];
					var appointment_flag = '';
					if( appointment_remind > "0" ){
						appointment_flag = '<img class="pipelin-date-exclamatory" src="'+base_root+'/res/images/ifa/list_date_ico.png">';
					}
					
					var dataGroup = array['group_name'] == null ? "" : array['group_name'];					
					var dataGroupContent = '';
					if(dataGroup != ''){
						dataGroupContent = 'data-group="'+dataGroup+'"';
					}
					var liContent = setGroupListLiContent.replace('>'+dataGroup+'</li>',' class="set-group-active">'+dataGroup+'</li>');
//					alert('dataGroup:'+dataGroupContent);
					                                  
					divContent += '<li class="pipelin-list-contents  swiper-slide" '+dataGroupContent+'>'
								+ '<div class="pipelin-contents-top">'
								+ '<p class="pipelin-contents-name"><a href="'+base_root+'/front/crm/pipeline/clientDetail.do?customerId='+id+'&customerMemberId='+member_id+'">'+nick_name+ '</a>'+birthday_flag+'</p>'
								+ '<div class="pipelin-contents-topl">'
								+ remark_flag
								+ appointment_flag
								+ '<p class="pipelin-date-level">2</p>'
								+ '</div>'
								+ '</div>'
								+ '<p class="pipelin-contents-word"><img src="'+base_root+'/res/images/ifa/small_email_ico.png">'+email+'</p>'
								+ '<p class="pipelin-contents-word"><img src="'+base_root+'/res/images/ifa/small_phone_ico.png">'+mobile_code+' '+mobile_number+'</p>'
								+ '<div class="pipelin-list-bottom">'
								+ '<p class="pipelin-bottom-name">'+create_time+'</p>'
								+ '<div class="pipelin-bottom-more">'
								+ '<span></span>'
								+ '<span></span>'
								+ '<span></span>'
								+ '<ul class="pipelin-list-more">'
								+ '<li data-name="setImportant" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Important</li>'
								+ '<li data-name="setAppointment" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Set Appointment</li>'
								+ '<li data-name="setCharacter" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-list-character" >Character</li>'
								+ '<li data-name="setGreet" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Greet</li>'
								+ '<li data-name="delete" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Delete</li>'
								+ '<li data-name="communicationRecord" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Communication</li>';
					if(saleStageId == 'sales_close'){
						divContent += '<li data-name="openAccount" data-value-investorId="'+member_id+'">Open Account</li>';
					}

					divContent += '<li data-name="setGroup" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-set-group" >Set Group'
								+ '	<span class="pipelin-group-arrow"></span>'
			    				+ '	<ul class="set-group-list">'
			    				+ liContent								
			    				+ '	</ul>'
								+ '</li>'
								+ '</ul>'
								+ '</div>'
								+ '</div>'
								+ loss
								+ heart
								+ '</li>';	
				});
								
				$(".pipelin_choice-contents").append(divContent);
			}
		})
    }
    
    function bindExistingClient(clientStatus){
    	var period = $('#hdPeriod').val();
		var keyword = $('#pipelinKeyWord').val();
		var char=$(".wmes_choice .wmes_choice_active").html();
		if(char=="All")
			char="";
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/existingClientListJsonForIfa.do?datestr='+new Date().getTime(),
			data : {
				'clientType': '1','areaId':'','period':period,'clientStatus':clientStatus,'keyword':keyword,'page':1,'rows':100,'sort':'','order':'',
				'char':char				
			},
			success : function(json) {
								
				$(".pipelin_choice-contents").empty();
				var divContent = "";
				var list = json.rows;
				//alert(list);
							
				$.each(list, function (index, array) { //遍历json数据列	
					var id = array['id'] == null ? "" : array['id'];
					 var member_id = array['member_id'] == null ? "" : array['member_id'];
					// var first_name = array['first_name'] == null ? "" : array['first_name'];
					// var last_name = array['last_name'] == null ? "" : array['last_name'];		
					var nick_name = array['nick_name'] == null ? "" : array['nick_name'];			
					var mobile_code = array['mobile_code'] == null ? "" : array['mobile_code'];
					var mobile_number = array['mobile_number'] == null ? "" : array['mobile_number'];
					var email = array['email'] == null ? "" : array['email'];
					var create_time = array['create_time'] == null ? "" : array['create_time'];
					// var born = array['born'] == null ? "" : array['born'];
					var sales_stage_id = array['sales_stage_id'] == null ? "" : array['sales_stage_id'];
					//alert("first_name:"+first_name);
					var is_important = array['is_important'] == null ? "" : array['is_important'];
					var heart = '';
					if(is_important == '1'){
						heart = '<img class="pipelin-list-heart" src="'+base_root+'/res/images/ifa/white_heart_ico.png">';
					}
					
					//suspand
					var loss = '';
					if(sales_stage_id == 'sales_suspend'){
						loss = '<div class="pipelin-contents-loss">'
							+ '<div class="pipelin-loss-mask"></div>'
							+ '<div class="pipelin-loss-word">LOSS</div>'
							+ '</div>';
					}
					var remark = array['remark'] == null ? "" : array['remark'];
					var remark_flag = '';
					if( '' != remark ){
						remark_flag = '<img class="pipelin-date-exclamatory" src="'+base_root+'/res/images/ifa/exclamatory_ico.png">';
					}
					
					//birthday_remind
					var birthday_remind = array['birthday_remind'] == null ? "0" : array['birthday_remind'];
					var birthday_flag = '';
					if( birthday_remind > "0" ){
						birthday_flag = '<img class="Small_cake_ico" src="'+base_root+'/res/images/ifa/Small_cake_ico.png">';
					}
					
					//appointment_remind
					var appointment_remind = array['appointment_remind'] == null ? "0" : array['appointment_remind'];
					var appointment_flag = '';
					if( appointment_remind > "0" ){
						appointment_flag = '<img class="pipelin-date-exclamatory" src="'+base_root+'/res/images/ifa/list_date_ico.png">';
					}
					
					var dataGroup = array['group_name'] == null ? "" : array['group_name'];
					
					var dataGroupContent = '';
					if(dataGroup != ''){
						dataGroupContent = 'data-group="'+dataGroup+'"';
					}
					var liContent = setGroupListLiContent.replace('>'+dataGroup+'</li>',' class="set-group-active">'+dataGroup+'</li>');
//					                                  
					divContent += '<li class="pipelin-list-contents  swiper-slide" '+dataGroupContent+'>'
								+ '<div class="pipelin-contents-top">'
								+ '<p class="pipelin-contents-name"><a href="'+base_root+'/front/crm/pipeline/clientDetail.do?customerId='+id+'&customerMemberId='+member_id+'">'+nick_name+ '</a>'+birthday_flag+'</p>'
								+ '<div class="pipelin-contents-topl">'
								+ remark_flag
								+ appointment_flag
								+ '<p class="pipelin-date-level">2</p>'
								+ '</div>'
								+ '</div>'
								+ '<p class="pipelin-contents-word"><img src="'+base_root+'/res/images/ifa/small_email_ico.png">'+email+'</p>'
								+ '<p class="pipelin-contents-word"><img src="'+base_root+'/res/images/ifa/small_phone_ico.png">'+mobile_code+' '+mobile_number+'</p>'
								+ '<div class="pipelin-list-bottom">'
								+ '<p class="pipelin-bottom-name">'+create_time+'</p>'
								+ '<div class="pipelin-bottom-more">'
								+ '<span></span>'
								+ '<span></span>'
								+ '<span></span>'
								+ '<ul class="pipelin-list-more">'
								+ '<li data-name="setImportant" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Important</li>'
								+ '<li data-name="setAppointment" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Set Appointment</li>'
								+ '<li data-name="setCharacter" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-list-character" >Character</li>'
								+ '<li data-name="setGreet" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Greet</li>'
								+ '<li data-name="delete" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Delete</li>'
								+ '<li data-name="communicationRecord" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Communication</li>'
								+ '<li data-name="openAccount" data-value-investorId="'+member_id+'">Open Account</li>'
								+ '<li data-name="setGroup" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-set-group" >Set Group'
								+ '	<span class="pipelin-group-arrow"></span>'
			    				+ '	<ul class="set-group-list">'
			    				+ liContent										
			    				+ '	</ul>'
								+ '</li>'
								+ '</ul>'
								+ '</div>'
								+ '</div>'
								+ loss
								+ heart
								+ '</li>';	
				});
								
				$(".pipelin_choice-contents").append(divContent);
			}
		})
    }
  
    $("#ifa-pipeline-investors").on("click",".ifa-home-mask",function(){
    	$(".investment-wrap").removeClass("investment-hide");
    	var memberId=$(this).attr("memberId");
    	bindMemberInfo(memberId);
    });
    $(".investment-wrap").on("click",".investment-close-ico",function(){
    	$(".investment-wrap").addClass("investment-hide");
    });
    
    
    function bindMemberInfo(memberId){
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+"/front/crm/pipeline/getMemberInfo.do",
    		data:{memberId:memberId},
    		success:function(json){
//    			//console.log(json);
    			
    			var loginCode=json.loginCode;
    			var iconUrl=json.iconUrl;
    			var education=null!=json.education?education:"N/A";
    			var email=null!=json.email?json.email:"N/A";
    			var facebookCode=null!=json.facebookCode?json.facebookCode:"N/A";
    			
    			var introduction=null!=json.introduction?json.introduction:"N/A";
    			
    			var lastLoginDate=null!=json.lastLoginDate?json.lastLoginDate:"N/A";
    			var lineCode=null!=json.lineCode?json.lineCode:"N/A";
    			var mobileNumber=json.mobileNumber?json.mobileNumber:"N/A";
    			var nationality=null!=json.nationality?json.nationality:"N/A";
    			var occupation=null!=json.occupation?json.occupation:"";
    			var registrationDate=null!=json.registrationDate?json.registrationDate:"N/A";
    			var residence=null!=json.residence?json.residence:"N/A";
    			var webchatCode=null!=json.webchatCode?json.webchatCode:"N/A";
    			var gender=null!=json.gender?json.gender:"N/A";
    			
    			if(iconUrl==null || iconUrl==""){
    				if(gender=="F"){
    					iconUrl=base_root+"/res/images/head_f.png";
    				}else{
    					iconUrl=base_root+"/res/images/head_m.png";
    				}
    			}else{
    				iconUrl=base_root+iconUrl;
    			}
    			
    			var favoriteInvestmentField=json.favoriteInvestmentField;
    			var hobby=json.hobby;
    			var investmentStyle=json.investmentStyle;
    			
    			$(".information-topper-name").text(loginCode);
    			$(".registrationDate").text(registrationDate);
    			$(".loginDate").text(lastLoginDate);
    			$(".mobile").text(mobileNumber);
    			$(".email").text(email);
    			$(".wetchat").text(webchatCode);
    			$(".line").text(lineCode);
    			$(".facebook").text(facebookCode);
    			$(".residence").text(residence);
    			$(".nationality").text(nationality);
    			$(".education").text(education);
    			$(".occupation").text(occupation);
    			$(".introduction").text(introduction);
    			$(".information-plan-portrait").attr("src",iconUrl);
    			
    			var fieldHtml="";
    			var hobbyHtml="";
    			var styleHtml="";
    			$.each(favoriteInvestmentField,function(i,n){
    				fieldHtml+='<li class="information-describe-blue">'+n+'</li>';
    			});
    			$.each(hobby,function(i,n){
    				hobbyHtml+='<li class="information-describe-red">'+n+'</li>';
    			});
    			$.each(investmentStyle,function(i,n){
    				styleHtml+='<li class="information-describe-yellow">'+n+'</li>';
    			});
    			$(".style").empty().append(styleHtml);
    			$(".field").empty().append(fieldHtml);
    			$(".hobby").empty().append(hobbyHtml);
    		}
    	})
    }
	
		/*
	 * 获取Url传递参数值
	 */
	function getUrlParam(name){  
	    //构造一个含有目标参数的正则表达式对象  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    //匹配目标参数  
	    var r = window.location.search.substr(1).match(reg);  
	    //返回参数值  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
	
	$(".wmes_icon_search").on("click",function(){
		getClientDataList();
		getExistingClientDataList();
	});
	
	
	$(".pipelin-bell-tab li").on("click",function(){
		$(this).siblings().removeClass("bell-tab-active").end().addClass("bell-tab-active");
		//$(".pipelin-bell-contents li").hide().eq($(this).index()).show();
		//var dataName = $(this).attr("data-name");
		var dataValue = $(this).attr("data-value");
		$("#hdPeriod").val(dataValue);
		var clientType = $("#hdClientType").val();
		if("1" == clientType){
			getExistingClientRemind();
			getExistingClientDataList();
		}else{
			getClientRemind();
			getClientDataList();
		}
		
	});
	
	
	//第一次加载数据
	getClientDataList();
	getClientRemind();	
	getExistingClientDataList();
	getExistingClientRemind();
	

	

	
	function getClientDataList(){
		getClientList('sales_new');
		getClientList('sales_contact');
		getClientList('sales_proposal');
		getClientList('sales_negotiating');
		getClientList('sales_close');
	}
	
	function getClientRemind(){
		var period = $('#hdPeriod').val();
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/clientRemind.do?datestr='+new Date().getTime(),
			data : {
				'period':period,'clientType':'0'
			},
			success : function(json) {
				var birthday = json.birthday == null? 0 : json.birthday;
				var appointment = json.appointment == null? 0 :json.appointment;
				//alert("birthday:"+birthday+",appointment:"+appointment);
				$("#BirthdayRemindNum").text(birthday);
				$("#AppointmentRemindNum").text(appointment);
			}
		})
	}
	
	function getExistingClientRemind(){
		var period = $('#hdPeriod').val();
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/existingClientRemind.do?datestr='+new Date().getTime(),
			data : {
				'period':period,'clientType':'1'
			},
			success : function(json) {
				var birthday = json.birthday == null? 0 : json.birthday;
				var appointment = json.appointment == null? 0 :json.appointment;
				var portfolio = json.portfolio == null? 0 : json.portfolio;
				var kyc = json.kyc == null? 0 :json.kyc;
				//alert("birthday:"+birthday+",appointment:"+appointment);
				$("#BirthdayRemindNum_Existing").text(birthday);
				$("#AppointmentRemindNum_Existing").text(appointment);
				$("#PorfolioRemindNum_Existing").text(portfolio);
				$("#KYCRemindNum_Existing").text(kyc);
			}
		})
	}
	
 	//绑定Proposal数据
	function getClientList(saleStageId){
		var period = $('#hdPeriod').val();
		var keyword = $('#txtKeyword').val();
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/clientListJsonForIfa.do?datestr='+new Date().getTime(),
			data : {
				'clientType': '0','areaId':'','period':period,'saleStageId':saleStageId,'keyword':keyword,'page':1,'rows':100,'sort':'','order':''
			},
			success : function(json) {
								
				$("."+saleStageId).empty();
				var divContent = "";
				var list = json.rows;
				//alert(list);
							
				$.each(list, function (index, array) { //遍历json数据列	
					var id = array['id'] == null ? "" : array['id'];
					 var member_id = array['member_id'] == null ? "" : array['member_id'];
					// var first_name = array['first_name'] == null ? "" : array['first_name'];
					// var last_name = array['last_name'] == null ? "" : array['last_name'];		
					var nick_name = array['nick_name'] == null ? "" : array['nick_name'];			
					var mobile_code = array['mobile_code'] == null ? "" : array['mobile_code'];
					var mobile_number = array['mobile_number'] == null ? "" : array['mobile_number'];
					var email = array['email'] == null ? "" : array['email'];
					var create_time = array['create_time'] == null ? "" : array['create_time'];
					// var born = array['born'] == null ? "" : array['born'];
					var sales_stage_id = array['sales_stage_id'] == null ? "" : array['sales_stage_id'];
					//alert("first_name:"+first_name);
					var is_important = array['is_important'] == null ? "" : array['is_important'];
					var heart = '';
					if(is_important == '1'){
						heart = '<img class="pipelin-list-heart" src="'+base_root+'/res/images/ifa/white_heart_ico.png">';
					}
					
					//suspand
					var loss = '';
					if(sales_stage_id == 'sales_suspend'){
						loss = '<div class="pipelin-contents-loss">'
							+ '<div class="pipelin-loss-mask"></div>'
							+ '<div class="pipelin-loss-word">LOSS</div>'
							+ '</div>';
					}
					var remark = array['remark'] == null ? "" : array['remark'];
					var remark_flag = '';
					if( '' != remark ){
						remark_flag = '<img class="pipelin-date-exclamatory" src="'+base_root+'/res/images/ifa/exclamatory_ico.png">';
					}
					
					//birthday_remind
					var birthday_remind = array['birthday_remind'] == null ? "0" : array['birthday_remind'];
					var birthday_flag = '';
					if( birthday_remind > "0" ){
						birthday_flag = '<img class="Small_cake_ico" src="'+base_root+'/res/images/ifa/Small_cake_ico.png">';
					}
					
					//appointment_remind
					var appointment_remind = array['appointment_remind'] == null ? "0" : array['appointment_remind'];
					var appointment_flag = '';
					if( appointment_remind > "0" ){
						appointment_flag = '<img class="pipelin-date-exclamatory" src="'+base_root+'/res/images/ifa/list_date_ico.png">';
					}
					
					var dataGroup = array['group_name'] == null ? "" : array['group_name'];
					var dataGroupContent = '';
					if(dataGroup != ''){
						dataGroupContent = 'data-group="'+dataGroup+'"';
					}
					var liContent = setGroupListLiContent.replace('>'+dataGroup+'</li>',' class="set-group-active">'+dataGroup+'</li>');
//					
					                                  
					divContent += '<li class="pipelin-list-contents  swiper-slide" '+dataGroupContent+'>'
								+ '<div class="pipelin-contents-top">'
								+ '<p class="pipelin-contents-name"><a href="'+base_root+'/front/crm/pipeline/clientDetail.do?customerId='+id+'&customerMemberId='+member_id+'">'+nick_name+ '</a>'+birthday_flag+'</p>'
								+ '<div class="pipelin-contents-topl">'
								+ remark_flag
								+ appointment_flag
								+ '<p class="pipelin-date-level">2</p>'
								+ '</div>'
								+ '</div>'
								+ '<p class="pipelin-contents-word"><img src="'+base_root+'/res/images/ifa/small_email_ico.png">'+email+'</p>'
								+ '<p class="pipelin-contents-word"><img src="'+base_root+'/res/images/ifa/small_phone_ico.png">'+mobile_code+' '+mobile_number+'</p>'
								+ '<div class="pipelin-list-bottom">'
								+ '<p class="pipelin-bottom-name">'+create_time+'</p>'
								+ '<div class="pipelin-bottom-more">'
								+ '<span></span>'
								+ '<span></span>'
								+ '<span></span>'
								+ '<ul class="pipelin-list-more">'
								+ '<li data-name="setImportant" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Important</li>'
								+ '<li data-name="setAppointment" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Set Appointment</li>'
								+ '<li data-name="setCharacter" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-list-character" >Character</li>'
								+ '<li data-name="setGreet" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Greet</li>'
								+ '<li data-name="delete" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Delete</li>'
								+ '<li data-name="communicationRecord" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Communication</li>';
					if(saleStageId == 'sales_close'){
						divContent += '<li data-name="openAccount" data-value-investorId="'+member_id+'">Open Account</li>';
					}

					divContent += '<li data-name="setGroup" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-set-group" >Set Group'
								+ '	<span class="pipelin-group-arrow"></span>'
			    				+ '	<ul class="set-group-list">'
			    				+ liContent								
			    				+ '	</ul>'
								+ '</li>'
								+ '</ul>'
								+ '</div>'
								+ '</div>'
								+ loss
								+ heart
								+ '</li>';	
				});
								
				$("."+saleStageId).append(divContent);
				
			}
		})
	}
	

	
	function getExistingClientDataList(){
		getExistingClientList('customer-care');
		getExistingClientList('open-account');
		getExistingClientList('proposal');
		getExistingClientList('portfolio');
		getExistingClientList('not-yet-invest');
	}
	
 	//绑定Proposal数据
	function getExistingClientList(clientStatus){
		var period = $('#hdPeriod').val();
		var keyword = $('#txtKeyword').val();
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/existingClientListJsonForIfa.do?datestr='+new Date().getTime(),
			data : {
				'areaId':'','period':period,'clientStatus':clientStatus,'keyword':keyword,'page':1,'rows':100,'sort':'','order':''
			},
			success : function(json) {
								
				$("."+clientStatus).empty();
				var divContent = "";
				var list = json.rows;
				//alert(list);
							
				$.each(list, function (index, array) { //遍历json数据列	
					var id = array['id'] == null ? "" : array['id'];
					var member_id = array['member_id'] == null ? "" : array['member_id'];
					// var first_name = array['first_name'] == null ? "" : array['first_name'];
					// var last_name = array['last_name'] == null ? "" : array['last_name'];	
					var nick_name = array['nick_name'] == null ? "" : array['nick_name'];				
					var mobile_code = array['mobile_code'] == null ? "" : array['mobile_code'];
					var mobile_number = array['mobile_number'] == null ? "" : array['mobile_number'];
					var email = array['email'] == null ? "" : array['email'];
					var create_time = array['create_time'] == null ? "" : array['create_time'];
					// var born = array['born'] == null ? "" : array['born'];
					var sales_stage_id = array['sales_stage_id'] == null ? "" : array['sales_stage_id'];
					//alert("first_name:"+first_name);
					var is_important = array['is_important'] == null ? "" : array['is_important'];
					var heart = '';
					if(is_important == '1'){
						heart = '<img class="pipelin-list-heart" src="'+base_root+'/res/images/ifa/white_heart_ico.png">';
					}
					
					//suspand
					var loss = '';
					if(sales_stage_id == 'sales_suspend'){
						loss = '<div class="pipelin-contents-loss">'
							+ '<div class="pipelin-loss-mask"></div>'
							+ '<div class="pipelin-loss-word">LOSS</div>'
							+ '</div>';
					}
					var remark = array['remark'] == null ? "" : array['remark'];
					var remark_flag = '';
					if( '' != remark ){
						remark_flag = '<img class="pipelin-date-exclamatory" src="'+base_root+'/res/images/ifa/exclamatory_ico.png">';
					}
					
					//birthday_remind
					var birthday_remind = array['birthday_remind'] == null ? "0" : array['birthday_remind'];
					var birthday_flag = '';
					if( birthday_remind > "0" ){
						birthday_flag = '<img class="Small_cake_ico" src="'+base_root+'/res/images/ifa/Small_cake_ico.png">';
					}
					
					//appointment_remind
					var appointment_remind = array['appointment_remind'] == null ? "0" : array['appointment_remind'];
					var appointment_flag = '';
					if( appointment_remind > "0" ){
						appointment_flag = '<img class="pipelin-date-exclamatory" src="'+base_root+'/res/images/ifa/list_date_ico.png">';
					}
					
					
					var portfolio = getPortfolioData(member_id);
					var portfolio_more = '';
					if( '' != portfolio ){
						portfolio_more = 'pipelin-contents-more';
					}
					//alert( subtext);
					var liContent = setGroupListLiContent.replace('>'+dataGroup+'</li>',' class="set-group-active">'+dataGroup+'</li>');
//					                                 
					divContent += '<li class="pipelin-list-contents '+portfolio_more+' swiper-slide">'
								+ '<div class="pipelin-contents-top">'
								+ '<p class="pipelin-contents-name"><a href="'+base_root+'/front/crm/pipeline/clientDetail.do?customerId='+id+'&customerMemberId='+member_id+'">'+nick_name+ '</a>'+birthday_flag+'</p>'
								+ '<div class="pipelin-contents-topl">'
								+ remark_flag
								+ appointment_flag
								+ '<p class="pipelin-date-level">2</p>'
								+ '</div>'
								+ '</div>'
								+ '<p class="pipelin-contents-word"><img src="'+base_root+'/res/images/ifa/small_email_ico.png">'+email+'</p>'
								+ '<p class="pipelin-contents-word"><img src="'+base_root+'/res/images/ifa/small_phone_ico.png">'+mobile_code+' '+mobile_number+'</p>'
								+ '<div class="pipelin-list-bottom">'
								+ '<p class="pipelin-bottom-name">'+create_time+'</p>'
								+ '<div class="pipelin-bottom-more">'
								+ '<span></span>'
								+ '<span></span>'
								+ '<span></span>'
								+ '<ul class="pipelin-list-more">'
								+ '<li data-name="setImportant" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Important</li>'
								+ '<li data-name="setAppointment" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Set Appointment</li>'
								+ '<li data-name="setCharacter" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-list-character" >Character</li>'
								+ '<li data-name="setGreet" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Greet</li>'
								+ '<li data-name="delete" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Delete</li>'
								+ '<li data-name="communicationRecord" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" >Communication</li>'
								+ '<li data-name="openAccount" data-value-investorId="'+member_id+'">Open Account</li>'
								+ '<li data-name="setGroup" data-value-saleStageId="'+saleStageId+'" data-value-customerId="'+id+'" class="pipelin-set-group" >Set Group'
								+ '	<span class="pipelin-group-arrow"></span>'
			    				+ '	<ul class="set-group-list">'
			    				+ liContent									
			    				+ '	</ul>'
								+ '</li>'
								+ '</ul>'
								+ '</div>'
								+ '</div>'
								+ loss
								+ heart
								+ portfolio
								+ '</li>';	
				});
								
				$("."+clientStatus).append(divContent);
				$(".pipelin-contents-more").each(function(){
					var contentsNum = 0;
					$(this).find(".pipelin-list-eject").each(function(index){
						contentsNum++;
						$(this).css("left",228 * contentsNum);
					});
				});
			}
		})
		
		
	}
	
	
 	//绑定Portfolio数据
	function getPortfolioData(customerMemberId){
		//var customerMemberId = getUrlParam('customerMemberId');		
		var divContent = "";
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/portfolioListJson.do?datestr='+new Date().getTime(),
			data : {
				'customerMemberId': customerMemberId,'page':1,'rows':100,'sort':'l.createTime','order':'DESC'
			},
			async : false,
			success : function(json) {
								
				//var divContent = "";
				var list = json.rows;			
				$.each(list, function (index, array) { //遍历json数据列	
					//alert('customerMemberId:'+customerMemberId);					
					var portfolio_name = array[0]['portfolioName'] == null ? "" : array[0]['portfolioName'];
					var base_currency = array[0]['baseCurrency'] == null ? "" : array[0]['baseCurrency'];
					// var create_time = array[0]['createTime'] == null ? "" : array[0]['createTime'];
					var return_rate = array[1]['returnRate'] == null ? "" : array[1]['returnRate'];
					//var total_return = array[1]['totalReturn'] == null ? "" : array[1]['totalReturn'];
					var total_amount = array[1]['totalAmount'] == null ? "" : array[1]['totalAmount'];
					
					var return_rate_per;
					if(parseFloat(return_rate) != NaN ){
						// if(parseFloat(return_rate) < 0){
						// 	var toReview = "<span>To review</span>";
						// }
						return_rate_per = parseFloat(return_rate)*100.0 + "%";
					}
					

					var flag = "";
					if(parseFloat(total_amount) != NaN && parseFloat(total_amount) >= 0){
						flag = "";
					}else{
						flag = "-drop";
					}
									
                                    
					divContent += '<div class="pipelin-list-eject">'
                        		+ '<p class="pipelin-eject-name">'+portfolio_name+'</p>'
                        		+ '<p class="pipelin-eject-price'+flag+'">'+total_amount+'<span class="pipelin-eject-cur">'+base_currency+'</span></p>'
                        		+ '<p class="pipelin-eject-percentage'+flag+'">'+return_rate_per+'</p>'
                                + '</div>';
				});
						
			}
		})
//		alert( divContent);
		return divContent;
	}

	
	//动态输出内容绑定事件
	$("body").on("click",".pipelin-list-more li",function(){
		var dataName = $(this).attr("data-name");
		//var saleStageId = $(this).attr("data-value-saleStageId");
		var customerId = $(this).attr("data-value-customerId");
		//alert("dataName:"+dataName);
		if("setImportant" == dataName ){
			updateClient(dataName,customerId);
		}else if ( "delete" == dataName ){
			layer.confirm('Are you sure you want to delete?', {
				  btn: ['Yes','No'] //按钮
				}, function(){
					layer.closeAll('dialog');
					updateClient(dataName,customerId);
				}, function(){
				  
				});
			
		}else if( "setRemark" == dataName ){
//			layer.msg("setRemark");
			var remark = $(this).attr("data-value-remark");

			 layer.open({
				  type: 1,
				  area: ['500px', '240px'],
				  shadeClose: true, //点击遮罩关闭
				  content: '\<\div style="padding:20px;"><textarea id="txtRemark" style="width:100%;"  rows="6">'+remark+'</textarea>\<\/div>',
				  btn: ['保存'],
				  yes: function(index,layero){
					  var remark = $("#txtRemark").val();
//					  layer.msg("save remark:"+remark);
					  updateClient(dataName,customerId,remark);
					  layer.close(index);
				  }
				  });
			 
		}else if ( "openAccount" == dataName ){
			var investorId = $(this).attr("data-value-investorId"); 
			var url = base_root + "/front/investor/accountStart.do?indId="+investorId;
			window.open(url);
		}
	});
	
	function updateClient(updateType,customerId,remark){
		//alert("customerId:"+customerId+",saleStageId:"+saleStageId);	
		var clientType = $("#hdClientType").val();
		
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/updateDetail.do?datestr='+new Date().getTime(),
			data : {
				'updateType':updateType,'customerId':customerId,'remark':remark
			},
			success : function(json) {
				if( json.result == true ){
					//getClientList(saleStageId);	
					if( "0" == clientType ){
						getClientDataList();
						bindClient(saleStageId);
					}else{
						getExistingClientDataList();
						bindExistingClient(clientStatus);
					}		
					
				}							
			}
		})		
	}
	
	
    $(document).on("click",".ifa-home-mask",function(){
    	$("#MemberInformation").removeClass("investment-hide");
    });
    $(document).on("click",".investment-close-ico",function(){
    	$("#MemberInformation").addClass("investment-hide");
    });
    
    // add wwluo Character Setting
    $(document).on("click",".pipelin-list-character",function(){
    	$('#character-choose-list').empty();
    	$('#hobbytypes-choose-list').empty();
    	$('#character-selected-option').empty();
    	$('#hobby-selected-option').empty();
    	//$("#CharacterSetting").removeClass("investment-hide");
    	$("#CharacterSetting").show();
    	var customerId = $(this).data('valueCustomerid');
    	var url = base_root + '/front/crm/pipeline/characterSetting.do?dateStr=' + new Date().getTime();
    	$.ajax({
    		type:'post',
    		data:{customerId:customerId},
    		url:url,
    		success:function(result){
    			////console.log(result);
    			if(result.flag){
    				var customer = result.customer,
    					characters = result.clientCharacters,
    					hobbyTypes = result.hobbyTypes;
    				//all
    				if(characters != null){
    					var cahrachersLi = '';
    					$.each(characters,function(i,n){
    						cahrachersLi +=
    							'<li data-code="'+n.itemCode+'" data-value="'+n.name +'">'
    							+ n.name 
    							+'<span class="character-list-close"></span></li>';
    					});
    					$('#character-choose-list').append(cahrachersLi);
    				}
    				if(hobbyTypes != null){
    					var hobbyTypesLi = '';
    					$.each(hobbyTypes,function(i,n){
    						hobbyTypesLi +=
    							'<li data-code="'+n.itemCode+'" data-value="'+n.name +'">'
    							+ n.name 
    							+'<span class="character-list-close"></span></li>';
    					});
    					$('#hobbytypes-choose-list').append(hobbyTypesLi);
    				}
    				//select
    				var characterCode = customer.character,
    					character = customer.characterName,
    					hobbyCode = customer.hobby,
    					hobby = customer.hobbyName,
    					selected = '';
    				if(character != null && character.indexOf(',')>-1){
    					character = character.split(',');
    					selected = '';
    					$.each(character,function(i,n){
    						selected +=
	    						'<li data-code="'+(characterCode.split(','))[i]+'" data-value="'+n+'">'
	    						+ n
	    						+'<span class="character-list-close"></span></li>';
    					});
    					$('#character-selected-option').append(selected);
    				}else if(character != null && character.indexOf(',')<0){
    					$('#character-selected-option').empty().append(
    							'<li data-code="'+characterCode+'" data-value="'+character+'">'
	    						+ character
	    						+'<span class="character-list-close"></span></li>');
    				}
    				if(hobby != null && hobby.indexOf(',')>-1){
    					hobby = hobby.split(',');
    					selected = '';
    					$.each(hobby,function(i,n){
    						selected +=
    							'<li data-code="'+(hobbyCode.split(','))[i]+'" data-value="'+n+'">'
    							+ n
    							+'<span class="character-list-close"></span></li>';
    					});
    					$('#hobby-selected-option').append(selected);
    				}else if(hobby != null && hobby.indexOf(',')<0){
    					$('#hobby-selected-option').empty().append(
    							'<li data-code="'+hobbyCode+'" data-value="'+hobby+'">'
	    						+ hobby
	    						+'<span class="character-list-close"></span></li>');
    				}
    				$('#txtSpecial').val(customer.special);
    				$('#txtDislike').val(customer.dislike);
    				$('.character-button-save').attr('data-customer-id',customer.id);
    			}
    		}
    	});
    });
    //add wwluo Save Character Setting
    $(document).on("click",".character-button-save",function(){ 
    	var customerId = $(this).data('customerId'),
	    	special = $('#txtSpecial').val(),
			dislike = $('#txtDislike').val(),
    		character = '',
    		hobby = '';
    	//character
    	$('#character-selected-option >li').each(function(){
    		var code = $(this).data('code');
    		if(typeof(code) == 'undefined'){
    			code = '{'+$(this).data('value')+'}';
    		}
    		character += code + ',';
    	});
    	if(character != ''){
    		character = character.substring(0,character.length-1);
    	}
    	//hobby
    	$('#hobby-selected-option >li').each(function(){
    		var code = $(this).data('code');
    		if(typeof(code) == 'undefined' || code.indexOf('{')>-1){
    			code = '{'+$(this).data('value')+'}';
    		}
    		hobby += code + ',';
    	});
    	if(character != ''){
    		hobby = hobby.substring(0,hobby.length-1);
    	}
    	var url = base_root + '/front/crm/pipeline/saveCharacterSetting.do?dateStr=' + new Date().getTime();
    	$.ajax({
    		type:'post',
    		url:url,
    		data:{
    			customerId:customerId,
    			special:special,
    			dislike:dislike,
    			character:character,
    			hobby:hobby
    		},
    		success:function(result){
    			if(result.flag){
    				layer.msg("Save Success!",{icon : 1,time : 2000});	
    				$("#CharacterSetting").addClass("investment-hide");
    			}
    		}
    	});
    	
    });
    // add end
    
    $(document).on("click",".character-close-ico, .character-button-close",function(){
    	$("#CharacterSetting").hide();
    	//$("#CharacterSetting").addClass("investment-hide");
    });

    $(".character-choose-list").on("click","li",function(){
    	$(this).parents(".character-setting-rows").find(".character-setting-list").append($(this));
    });
    $(".character-setting-list").on("click",".character-list-close",function(){
    	$(this).parents(".character-setting-rows").find(".character-choose-list").append($(this).parent());
    });

    $(".character-setting-add").on("blur",function(){
    	if($(this).val().replace(/(^\s*)|(\s*$)/g, "") !=''){
    		var StrHtml = '<li data-value='+ $(this).val() +'>'+ $(this).val() +' <span class="character-list-close"></span></li>'
        	$(this).parents(".character-setting-rows").find(".character-setting-list").append(StrHtml);
        	$(this).val("");
    	}
    });

	//添加或编辑分组
    $("body").on("click",".my-group-list .group-list-edit",function(){

		var groupId = $(this).parents(".my-group-li").find("input").attr("data-value"),
		groupName = $(this).parents(".my-group-li").find("input").val(),
    		_bol = true,
    		self = $(this);

    	if(self.parents(".my-group-li").hasClass("my-group-edit")){
    	
	    	$("#my-group-list .group-list-word").each(function(){

	    		if( $(this).val() == groupName && !$(this).parents(".my-group-li").hasClass("my-group-edit")){
	    			
	    			layer.msg("已经存在该组名");
	    			
	    			_bol = false;
	    		};
	    	});

	    	if( groupName == ""){

	    		layer.msg("组名不能为空");
	    			
	    		_bol = false;
	    	}
	    	
    	}
    	if(_bol){


			
	    	self.parents(".my-group-li").toggleClass("my-group-edit");

	    	if(self.parents(".my-group-li").hasClass("my-group-edit") ){

	    		self.parents(".my-group-li").find(".group-list-word").removeAttr("readonly");

	    		self.attr("data-value",self.parents(".my-group-li").find("input").val());
	    		
//	    		layer.msg("111");

	    	}else{
//	    		layer.msg("222");
    			saveCustomerGroup(groupId,groupName);
    			
	    		$(".pipelin-choice-group").each(function(){
	    			
	    			if($(this).html() == self.attr("data-value")){
	    				$(this).html( groupName );
	    				$(".set-group-list li").each(function(){
	    					if($(this).html() == self.attr("data-value")){
	    						$(this).html( groupName );
	    					}
	    				});

	    				$(".pipelin-list-contents").each(function(){

	    					var _gropu = $(this).attr("data-group");

							if(!_gropu) _gropu = '';

							if ( _gropu.indexOf( self.attr("data-value") ) >= 0 ) {


		    					var ArrTemp = _gropu.split(",");

		    					var ArrIndex = ArrTemp.indexOf( self.attr("data-value") );

					    		ArrTemp.splice(ArrIndex, 1 , groupName);

					    		ArrTemp = ArrTemp.join(",");

					    		$(this).attr("data-group",ArrTemp);
	    					}
	    					
	    				});
	    				_bol = false;
	    			}
	    		});
	    		if(_bol){
	    			$(".pipelin-new-group").before('<li class="pipelin-choice-group">'+ groupName +'</li>');
	    			$(".set-group-list").each(function(){
	    				$(this).append('<li>'+ groupName +'</li>')
	    			});
	    		}

	    		self.parents(".my-group-li").find(".group-list-word").attr("readonly","readonly");
	    		self.removeAttr("data-value");
	    	}
    	}
    	
    });
    
	var	myScroll2 = new IScroll('#group-list-wrapper', {
        scrollbars: true, 
        interactiveScrollbars: true, 
        shrinkScrollbars: 'scale', 
        fadeScrollbars: true, 
        click: true,
        scrollX: false,
         scrollY: true, 
        preventDefault: true

    });
    
    //delete group
    $(".my-group-list").on("click",".group-list-close",function(){
    	var groupId = $(this).parents(".my-group-li").find("input").attr("data-value");
    	var self = $(this).parents(".my-group-li").find("input").val();
    	
    	layer.confirm('Are you sure you want to delete?', {
			  btn: ['Yes','No'] //按钮
			}, function(){
				layer.closeAll('dialog');
				
				deleteCustomerGroup(groupId);
		    	
		    	$(this).parents(".my-group-li").remove();

		    	$(".pipelin-choice-group").each(function(){

					if($(this).html() == self){
						$(this).remove();
					};

				});
		    	myScroll2.refresh();
			}, function(){
			  
			});
    	
    	
    });
    $(".group-title-close").on("click",function(){

    	$(".my-group-wrapper").hide();

    })

		document.addEventListener('touchmove', function (e) { e.preventDefault(); 

     e.stopPropagation(); }, false);

	$("body").on("click",".pipelin-new-group",function(){

		$(".my-group-wrapper").show();

	});


    $(".group-list-button").on("click",function(){

    	var _Dom = $(".my-group-list")
    		
    	,	StrLenght = _Dom.children().length;

    	if( _Dom.find(".my-group-edit").length > 0){

    		layer.msg("请先保存信息");
		    return;
	    }
    	var StrName = ""
    	,	StrTemp = [];

    	$("#my-group-list .group-list-word").each(function(){  		
    		StrTemp.push($(this).val());
    	});

    	for (var i = 0 ;i < StrLenght + 1; i++){
 
    		var Strsum = Number(i) + 1;

    		StrName = "客户分组" + Strsum;
    		if ( StrTemp.indexOf(StrName) < 0){
    			break;
    		}
    	}

    	var StrHtml = '<li class="my-group-li my-group-edit">';

    		StrHtml+= '<input type="text" maxlength="18" class="group-list-word" value="'+ StrName +'">';
    		
    		StrHtml+= '<div class="group-list-ico"><span class="group-list-edit"></span><span class="group-list-close"></span></div></li>'
    		
    		_Dom.append(StrHtml);

    		var _input = _Dom.children().eq( StrLenght - 1 ).find("input")

    		,	_val = _input.val();

    			_input.val("").focus().val(_val); 

    	var StrHeight = StrLenght * (Number($(".my-group-li").height()) + 5);

    		if( _Dom.height() > $(".group-list-wrapper").height()){

    			myScroll2.scrollTo(0, -StrHeight , 0,true);

    		};

    		myScroll2.refresh()
    		
    });

    $(document).on("click",".set-group-list li",function(){

    	var self = $(this)

    	,   ArrTemp = []

    	,   StrGroup = self.parents(".pipelin-list-contents").attr("data-group");

    	self.siblings().removeClass("set-group-active").end().addClass("set-group-active");
//    	self.toggleClass("set-group-active");

    	if( self.hasClass("set-group-active") ){
//	    	if(StrGroup){
//	    		//单个分组
////	    		ArrTemp = StrGroup.split(",");
//	    		ArrTemp.push( self.html() );
//	    		ArrTemp = ArrTemp.join(",");
//	    	}else{
//	    		ArrTemp.push( self.html() );
//	    		ArrTemp = ArrTemp.join(",");
//	    	};
	    	
	    	ArrTemp.push( self.html() );
    		ArrTemp = ArrTemp.join(",");
    	}else{
    		if(StrGroup){
	    		ArrTemp = StrGroup.split(",");
	    		var ArrIndex = ArrTemp.indexOf( self.html() );
	    		ArrTemp.splice(ArrIndex, 1);
	    		ArrTemp = ArrTemp.join(",");
	    	};
    	}
    	

    	var customerId = self.parents("li .pipelin-set-group").eq(0).attr("data-value-customerid");
    	var groupId = self.attr("data-group-id");
//    	layer.msg(customerId+','+groupId);
    	saveCustomerGroupRelationship(groupId,customerId);
    	self.parents(".pipelin-list-contents").attr("data-group",ArrTemp);

    });
	

    
    function saveCustomerGroup(groupId,groupName){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/saveCrmGroup.do?datestr='+new Date().getTime(),
			data : {
				'groupId':groupId,'groupName':groupName
			},
			success : function(json) {
				if(json.result == true){
//					layer.msg("ok");
					getCustomerGroupList();
				}							
			}
		})
    }
    
    function deleteCustomerGroup(groupId){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/deleteCrmGroup.do?datestr='+new Date().getTime(),
			data : {
				'groupId':groupId
			},
			success : function(json) {
				if(json.result == true){
//					layer.msg("ok");
					getCustomerGroupList();
				}							
			}
		})
    }
    
    function saveCustomerGroupRelationship(groupId,customerId){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/crm/pipeline/saveCrmGroupRelationship.do?datestr='+new Date().getTime(),
			data : {
				'groupId':groupId,'customerId':customerId
			},
			success : function(json) {
				if(json.result == true){
					layer.msg("ok");
				}							
			}
		})
    }
    
    $('body').on("click",".information-more-ico",function(){
    	$(this).parents(".information-plan-rows").toggleClass("information-plan-hide");
    });  
    
    $(document).on('click','.pipeline_page',function(){
    	event.stopPropagation();
    	$('.pipelin-list-contents').removeClass('kejiande');
    	$(this).closest('li').css({"width":220,"padding-right" : 12});
    	$(this).closest('li').find('.mask-layer').css('display','block');
    	$(this).closest('li').find('.mask-layer .mask-layerbox').stop().animate({'right':0},500);
    });
    
    $('.mask-layerleft').on('click',function(){
    	$(this).closest('.mask-layerbox').animate({'right':'-134px'},500,function(){
    		$(this).closest('.mask-layer').css('display','none')
    	});
    });
    
    $('.mask-layerright li').on('click',function(){
    	$(this).addClass('groupingac').siblings().removeClass('groupingac');
    });
    
    $("body").on("click",".addclienttype",function(){
    	$(".my-group-wrapper").show();
	});
	
	$('.pipelinenew-Client_Type li').on('click',function(){
		$(this).addClass('pipelinenew-Client_Typeac').siblings().removeClass('pipelinenew-Client_Typeac');
		$(".pipeline-div .pipelin-list-contents").show();
		if($(this).index() == 2){
			$(".pipeline-div .pipelin-list-contents").each(function(){
				if( $(this).find(".pipelin-list-important").length <= 0){$(this).hide();}
			});
		}else if($(this).index() == 1){
			$(".pipeline-div .pipelin-list-contents").each(function(){
				if( $(this).find(".pipelin-list-ordinary").length <= 0){$(this).hide();}
			});
		}else{
			$(".pipeline-div .pipelin-list-contents").show();
		}
	});
	
	$(".pipelin-list-swiper").on("mouseenter",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").show();
    });
    $(".pipelin-list-swiper").on("mouseleave",function(){
        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
    });
    
    
    
});