

define(function(require) {
	var $ = require('jquery');
	require("layui");
	
	$(".wmes-name-top").on("click",function(){
		$(".ifa-space-popup").show();
	});
	$(".space-mask-close").on("click",function(){
		$(".ifa-space-popup").hide();
	});
	//弹出框点击关闭
	$("#space-popup-close").on("click",function(){
		$(".ifa-space-popup").hide();
	});

	$(".ifa-more-ico").on("click",function(){
		$(this).parents(".ifa-space-hide-rows").toggleClass("screener_trending_narrow");
	});
	

	//点击推荐的Location或Sector下拉按钮
	$(".ifa-his-xiala").on("click",function(){
		if($(this).prev().height()!=35){
			var _selfParents = $(this).parents(".ifa-his-map");
			_selfParents.toggleClass("ifa-his-show");
			if(_selfParents.hasClass("ifa-his-show")){
			_selfParents.stop().animate({ 
	            height: _selfParents.find("ul").height() + "px"
	        }, 300 );
		}else{
			_selfParents.stop().animate({ 
	            height: "35px"
	        }, 300 );
		}
		}
		
	});

	//点击推荐的Location或Sector的每个筛选条件
	$(".ifa-his-map li").on("click",function(){
		$(this).siblings().removeClass("ifa-his-map-active").end().toggleClass("ifa-his-map-active");
		//组装条件
		var memberId = $('#hidMemberId').val();
		var elementGeoAllocation = $('#ul_geo_allocation li[class="ifa-his-map-active"]');
		var elementSector = $('#ul_sector li[class="ifa-his-map-active"]');
		var geoAllocationValue = $(elementGeoAllocation).attr('data-value');
		if(geoAllocationValue==undefined)geoAllocationValue='';
		var sectorValue = $(elementSector).attr('data-value');
		if(sectorValue==undefined)sectorValue='';
		
		////console.log($(elementGeoAllocation).attr('data-value'));
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/space/ifaSpaceFilterRecommendJson.do?datestr="+ new Date().getTime(),
			data : {'memberId':memberId,'regionCodeType':geoAllocationValue,'sectorCodeType':sectorValue},
			beforeSend: function () {},
            complete: function () {},
			success : function(json) {
				//输出策略数据
				var recommendedStrategiesCount = 0;
				var recommendedStrategies = json.recommendedStrategies;
				if(recommendedStrategies!=null&&recommendedStrategies.length>0){
					recommendedStrategiesCount = recommendedStrategies.length;
					var data = recommendedStrategies[0];
					if(data!=null&&data!=undefined){
						var latestStrategyName = data.strategyName;
						var latestStrategyId = data.id;
						var containProCount = data.containProCount;
						
						$('#p_lateststrategyproductcount').text(containProCount);
						$('#a_lateststrategybame').text(latestStrategyName);
						$('#a_lateststrategybame').attr('href',base_root + '/front/strategy/info/strategiesdetail.do?id=' + latestStrategyId);
						
						$('#a_lateststrategybame').show();
						$('.ifa-his-type-list li').eq(0).find('.ifa-type-content').empty().append('<p id="p_lateststrategyproductcount" class="ifa-type-parsent ifa-type-black">'+containProCount+'</p><p  class="ifa-type-date">基金</p> ');
						
					}
					
					$('#sp_strategyinfolistcount').text(recommendedStrategiesCount);
				}
				else{
					$('#sp_strategyinfolistcount').text(0);
					$('#a_lateststrategybame').hide();
					$('.ifa-his-type-list li').eq(0).find('.ifa-type-content').empty().append('<p class="ifa-type-date">'+IFASPACE_LANG_NoRecommendation+'</p>');
				}
				//输出组合数据
				var recommendedPortfolios = json.recommendedPortfolios;
				var recommendedPortfoliosCount = 0;
				if(recommendedPortfolios!=null&&recommendedPortfolios.length>0){
					recommendedPortfoliosCount = recommendedPortfolios.length;
					var data = recommendedPortfolios[0];
					if(data!=null&&data!=undefined){
						var id = data.id;
						var portfoliosName = data.portfoliosName;
						var returnRate = data.returnRate;
						var returnTotal = data.returnTotal;
						var riskLeve = data.riskLeve;
						var totalInvestAmount = data.totalInvestAmount;
						
						$('#a_latestPortfoliosName').text(portfoliosName).show();
						$('#a_latestPortfoliosName').attr('href',base_root + '/front/ifa/info/porfoliosdetail.do?id=' + id);
						$('#p_latestPortfoliosRate').text(returnRate);
						$('#sp_IfaSpacePortfoliosCount').text(recommendedPortfoliosCount);
						
						var h1 = '<div class="type-portfolios-left"><img class="ifa-type-portfolios" src="/wmes/res/images/ifa/Portfolios_img.png"></div>';
						var h2 = '<div class="type-portfolios-right"><p id="p_latestPortfoliosRate" class="ifa-type-parsent">'+returnRate+'%</p><p class="ifa-type-date">Cum 1 Mon</p></div>'
						$('.ifa-his-type-list li').eq(1).find('.ifa-type-content').empty().append(h1+h2);
					}
				}
				else{
					$('#sp_IfaSpacePortfoliosCount').text(0);
					$('#a_latestPortfoliosName').hide();
					$('.ifa-his-type-list li').eq(1).find('.ifa-type-content').empty().append('<p class="ifa-type-date">'+IFASPACE_LANG_NoRecommendation+'</p>');
				}
				//输出基金数据
				var fundList = json.fundList;
				var fundListCount = 0;
				if(fundList!=null&&fundList.length>0){
					fundListCount = fundList.length;
					var data = fundList[0];
					if(data!=null&&data!=undefined){
						var fundId = data.fundId;
						var fundName = data.fundName;
						var increaseRate = data.increaseRate;
						var periodName = data.periodName;
						
						$('#a_latestFundName').text(fundName).show();
						$('#a_latestFundName').attr('href',base_root + '/front/fund/info/fundsdetail.do?id=' + fundId);
						$('#p_latestFundincreaseRate').text(increaseRate);
						$('#sp_recommendedFundCount').text(fundListCount);
						
						var h1 = '<p id="p_latestFundincreaseRate" class="ifa-type-parsent">'+increaseRate+'%</p>';
						var h2 = '<p id="p_latestPeriodName" class="ifa-type-date">'+periodName+'</p>';
						$('.ifa-his-type-list li').eq(2).find('.ifa-type-content').empty().append(h1+h2);
					}
				}
				else{
					$('#sp_recommendedFundCount').text(0);
					$('#a_latestFundName').hide();
					$('.ifa-his-type-list li').eq(2).find('.ifa-type-content').empty().append('<p class="ifa-type-date">'+IFASPACE_LANG_NoRecommendation+'</p>');
				}
				//输出观点列表
				var liHtml = '';
				var insightList = json.insightList;
				var insightListCount = 0;
				if(insightList!=null&&insightList.length>0){
					insightListCount = insightList.length;
					$.each(insightList,function(i,n){
						var data =  n;
						if(data!=null&&data!=undefined){
							var comments = data.comments;
							var createTime = data.createTime;
							var insightId = data.insightId;
							var title = data.title;
							var views = data.views;
							if(comments==null||comments==undefined||comments=='')comments='0';
							if(views==null||views==undefined||views=='')views='0';
							
							var li = '<li>';
			                li += '<p class="insights-content"><span class="gray-box"></span> '+title+'</p>';
			                li += '<p class="insights-date">';
			                li += '<span class="insights-time">'+createTime+'</span>';
			                li += '<div class="insights-ico-wrap">';
			                li += '<p class="insights-ico"><img class="" src="'+base_root+'/res/images/ifa/view_ico.png"><span>'+views+'</span></p>';
			                li += '<p class="insights-ico"><img class="" src="'+base_root+'/res/images/ifa/comment_ico.png"><span>'+comments+'</span></p> '; 
			                li += '</div>';
			                li += '</p>';
			                li += '</li>';
			                liHtml += li;
							
						}
					});
					$('#id_not_insights').hide();
					$('#sp_IfaSpaceInsightListCount').text(insightListCount);
					$('#ul_insightsList').empty().append(liHtml);
					
				}
				else{
					$('#sp_IfaSpaceInsightListCount').text(0);
					//console.log($('#id_not_insights').length);
					if($('#id_not_insights').length<=0)
						$('.insights-title').eq(0).after('<p id="id_not_insights" style="color: #b5b4b4;">'+IFASPACE_LANG_NoRecommendation+'</p>');
					else{
						$('#id_not_insights').text(''+IFASPACE_LANG_NoRecommendation+'').show();
					}
					$('#ul_insightsList').empty();
				}
				//输出新闻列表
				var newsLiHtml = '';
				var newsList = json.newsList;
				var newsListCount = 0;
				if(newsList!=null&&newsList.length>0){
					newsListCount = newsList.length;
					$.each(newsList,function(i,n){
						var data =  n;
						if(data!=null&&data!=undefined){
							var createTime = data.createTime;
							var ifaRecommendedReason = data.ifaRecommendedReason;
							var newsId = data.newsId;
							var thumbnailImagePath = data.thumbnailImagePath;
							var title = data.title;
							var url = data.url;
							
							var li = '<li>';
				                li += '<p class="insights-content"><span class="gray-box"></span> <span>'+ifaRecommendedReason+'</span> </p>';
				                li += '<div class="news-word-wrap">';
				                li += '<a class="ifa-news-img" href="javascript:;">';
				                li += ' <img src="'+thumbnailImagePath+'">';
				                li += '</a>';
				                li += '<div class="news-wrap">';
				                li += '<p class="ifa-news-word"><a class="ifa-a-href" href="'+url+'" target="_self">'+title+'</a></p>';
				                li += '<p class="insights-time">'+createTime+'</p>';
				                li += '</div>';                             
				                li += '</div>';
				                li += '</li>';
				                newsLiHtml += li;
							
						}
					});
					$('#id_not_news').hide();
					$('#sp_IfaSpaceNewsListCount').text(insightListCount);
					$('#ul_newsList').empty().append(newsLiHtml);
				}
				else{
					$('#sp_IfaSpaceNewsListCount').text(0);;
					if($('#id_not_news').length<=0)
						$('.insights-title').eq(1).after('<p id="id_not_news" style="color: #b5b4b4;padding-bottom:60px;">'+IFASPACE_LANG_NoRecommendation+'</p>');
					else{
						$('#id_not_news').text(''+IFASPACE_LANG_NoRecommendation+'').show();
					}
					$('#ul_newsList').empty();
				}
			}
		});
	});
	
	//分享转载
	$(".class-share").on("click",function(){ 
		//获取点击来源
		var imgShareElementId = $('#box').attr('imgShareElementId');
		var newImgShareElementId = '#'+imgShareElementId;
		var imgShareElement = $(newImgShareElementId);
		var cornerId = imgShareElement.attr('cornerid');
		var cornerAuthorId = imgShareElement.attr('cornerAuthorId');
		var authorHeaderImg = $('.wmes-name-top').children('img').attr('src'); imgShareElement.attr('authorHeaderImg');
		//if(cornerId==''){ layer.msg('CornerId Error!', {icon: 0}); return;}
		//获取原有标题作为内容
		var self = $(this);
		var content = self.prev().text();
		var url = window.location.href;
		//取得原有主题的内容
		var authorName = $('.wmes-ifa-name').text();
		//页面层
		layer.open({
		  type: 1,
		  title:'Share Corner Info',
		  btn: ['Share', 'Close'],
		  yes:function(index){ 
			  var elelent_id = '#'+cornerId;
			  var title = $('#txtTitle').val();
			  //保存备注信息
			  $.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/ifa/info/shareInfo.do?datestr="+ new Date().getTime(),
					data : {'title':title,'content':content,'url':url},
					beforeSend: function () {},
	                complete: function () {},
					success : function(json) {
						var obj = $.parseJSON(json); 
						if(obj.result){
							layer.msg('Reprint Corner Info Successful!', { },function(){ layer.closeAll();});
							//var element_img_remark_id = '#img_remark_'+watchingid;
							//$(element_img_remark_id).attr('remark-value',newRemark);
							//layer.closeAll();
						}
					}
				});
		  },
		  skin: 'layui-layer-rim', //加上边框
		  area: ['700px', '400px'], //宽高
		  content: '<div><input class="login_content_input" type="text" id="txtTitle" style="margin-bottom: 5px;margin-left: 5px;margin-top: 10px;width:95%; color:#000000;" placeholder="please input something "></div>'
			+'<div style="height:1px;line-height:1px;background-color:#ecf6fc;margin-bottom:10px;">&nbsp;</div>'
			+ '<div class="conner-top-wrap">'
	   		+ '<img class="corners-top-portrait" style="margin-left:10px;" src="'+authorHeaderImg+'">'
	   		+ '<div class="conner-top-infor">'
	   		+ '<p class="conner-top-title" style="font-size:15px;margin-bottom:10px;">'+content+'</p>'
	   		+ '<div class="conner-top-ico">'
	   		+ '<div class="conner-ico-wrap" style="margin-right:0px;">'
	   		+ '<p class="conner-ico-title" style="text-indent:2em;">'+authorName+'</p>'
	   		+ '</div>'
	   		+ '</div>'
	   		+ '</div>'
	   		+ '</div>'
		//<input class="login_content_input" type="text" id="txtTitle" style="margin-bottom: 5px;margin-left: 5px;margin-top: 10px;width:95%; color:#000000;" placeholder="please input something "><textarea readonly id="'+cornerId+'" style=" height: 150px;width: 95%;margin: 5px 5px 5px 5px;border:#dcdadb 1px solid;">'+content+'</textarea>
		});
	});

	$('#addFriend').click(function(){
		var ifaname = $('#ifaName').text();
		var memberId = $('#hidMemberId').val();
		var loginMemberId = $('#hidloginMemberId').val();
		if(memberId!=''&&loginMemberId!=''&&memberId==loginMemberId){
			layer.alert(IFASPACE_LANG_CannotAddYourself, { title:IFASPACE_LANG_Alert, icon: 0,btn: ['OK']  });
			return;
		}
		if(memberId!=''&&(loginMemberId==''||loginMemberId==undefined)){
			layer.alert(IFASPACE_LANG_LoginToAddFriends, { title:IFASPACE_LANG_Alert, icon: 0 ,btn: ['OK'] });
			return;
		}
		layer.confirm(IFASPACE_LANG_SureToAddFriends, { title:IFASPACE_LANG_Alert, icon: 3 ,btn: ['OK', 'Cancel'] },function () { 
			 $.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/info/addFriend.do?datestr="+ new Date().getTime(),
				data : {'toMemberId':memberId },
				 beforeSend: function () {},
				 complete: function () {
				 },
				success : function(json) {
					json = JSON.parse(json);
					var result = json.result;
					if(result==true||result=='true'){
						layer.msg(IFASPACE_LANG_WaitToAddFriends, { title:IFASPACE_LANG_Alert  }, function () {  });
					}else if(result==false||result=='false'){
						layer.msg("你已经添加该好友或已发过了申请", { icon: 1, time: 2000 }, function () {  });
					}
				}
			});
        	layer.closeAll();
        });
	});	
	
	//下拉
	$(".ifa-xiala").on("click",function(){
		if( $(this).find("ul").hasClass("ifa-xiala_active") ){
			$(this).find("ul").removeClass("ifa-xiala_active").hide();
		}else{
			$(this).find("ul").addClass("ifa-xiala_active").show();
		}
	});
	$('.ifa-xiala').on('mouseleave',function(){
		$(this).find("ul").removeClass("ifa-xiala_active").hide();
	});
	//点击下拉筛选类型
	$("body").on('click', '.ifa-xiala ul li', '', function () {
		var typeId = $(this).attr('data-value');
		var dataKey = $(this).attr('data-key');
		if(typeId!=''){ 
			$('#txt-xiala-selected').val(dataKey);
			//同级的隐藏
			var cur_element_id = '#watching-rows-'+typeId;
			$(cur_element_id).show().siblings().hide();
			//执行转换
			var ifaDefCurrency = $('#txt-xiala-selected').attr('ifaDefCurrency');
			var ifaAum = $('#txt-xiala-selected').attr('ifaAum');
			//if(ifaDefCurrency==typeId)return;
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/space/exchangeCurrency.do?datestr="+ new Date().getTime(),
				data : {'ifaAum':ifaAum,'ifaDefCurrency':ifaDefCurrency,'toCurrency':typeId},
				 beforeSend: function () {},
				 complete: function () {
				 },
				success : function(json) {//console.log(json);
					//json = JSON.parse(json);
					var newaum = json.newaum;
					$('#p_exchange_aum').find('.price_positive').text(newaum);
				}
			});
		} else{ 
			$('.watching-rows').show();
		}
		
	});
	$('textarea').each(function () {
	  
	  this.setAttribute('style', 'height:' + (this.scrollHeight) + 'px;overflow-y:hidden;');
	
	}).on('input', function () {
	  
	  this.style.height = 'auto';	  
	  this.style.height = (this.scrollHeight) + 'px';
	
	});

	$(document).on("click",".communication-rows-del",function(){

		$(this).parents(".communication-contents-rows").remove();
	
	});

	//如果当前登录的IFA刚好查看自己的空间，则显示编辑按钮，进行编辑个人最心hightlight
	$(".ifa-thehigt-edit").on("click",function(){
		var _selfParents = $(this).parents(".zone-edit");
    	_selfParents.toggleClass("ifa-thehigt-edit-wrap");

    	if( _selfParents.hasClass("ifa-thehigt-edit-wrap") ){
    		//编辑按钮
    		_selfParents.find(".ifa-thehight-word").removeAttr("readonly");
    		
    	}else{
    		
    		_selfParents.find(".ifa-thehight-word").attr("readonly","");
    		//保存操作
    		saveHightlight();
    	}
	});
	
	//保存Hightlight数据
	function saveHightlight(){
		var hightLightMemo = $('#txtHightLight').val();
		var memberId = getQueryString('id');
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/space/updateHightLight.do?datestr="+ new Date().getTime(),
			data : {'hightlight':hightLightMemo,'id':memberId },
			 beforeSend: function () {},
			 complete: function () {},
			success : function(json) {
				var result = json.result;
//				if(result==true||result=='true'){
//					layer.msg("添加好友申请发送成功，请等待通过", { icon: 1, time: 2000 }, function () {  });
//				}else if(result==false||result=='false'){
//					layer.msg("你已经添加该好友或已发过了申请", { icon: 1, time: 2000 }, function () {  });
//				}
			}
		});
		
	}
	
	//弹框
	  $(document).on("click",".recommend-news-title",function(){ 
	  	var insightId = $(this).attr("insightid");
	  	//
	  	$.ajax({
	  		url:base_root+"/front/investor/myInsight/insightView.do",
	  		data:{insightId:insightId},
	  		type:"get",
	  		success:function(result){ 
	  			if("" != result.id || null != result.id){
	  				$(".ifa-article-space .ifa-article-wrap").html('');
	  				var html = '<div class="wmes-close" id="space-article-close"></div>'
	  		            + '<p class="ifa-article-title">' + result.title + '</p>'
	  		            + '<div class="ifa-article-pub-wrap">'
	                    + '<p class="ifa-article-name-wrap"><img class="ifa-article-portrait-img" src="'+ base_root +'/res/images/ifa/linshi_ifa_04.png">' + result.author.nickName + '</p>'
	                    + '<p class="ifa-article-time">' + result.pubDate + '</p>'
	                    + '<p class="ifa-article-click"><img class="recommend-news-bottom-img" src="'+base_root+'/res/images/discover/eve_ico.png">' + (result.click==null?'0':result.click) + '</p>'
	                    + '</div>'
	  		            + '<p class="ifa-article-word">' + result.content +'</p>'
	  		            + '<div class="article-retopst-wrap">'
	                    + '<div class="atricle-retopst-like-wrap">'
	                    + '<img insightid="'+insightId+'" class="recommend-news-like-ico class_article_like" src="'+ base_root +'/res/images/fund/fund_like_ico.png">'
	                    + '<span id="span_id_likd_'+insightId+'" class="recommend-news-like-num">'+(result.upCounter==null?'0':result.upCounter) +'<span></div>'
	                    + '<div class="atricle-retopst-like-wrap">'
	                    + '<img insightid="'+insightId+'" class="recommend-news-like-ico class_article_notlike" src="'+ base_root +'/res/images/fund/fund_step_ico.png">'
	                    + '<span id="span_id_down_'+insightId+'" class="recommend-news-like-num">'+ (result.downCounter==null?'0':result.downCounter) +'<span></div>'
	                    + '<div class="atricle-retopst-cen"><a class="atricle-retopst-close" href="javascript:;">'+GLOBAL_CLOSEWINDOW+'</a></div>'
	  		            + '</div>';
	  				$(".ifa-article-space .ifa-article-wrap").append(html);
	  				$(".ifa-article-space").show();
	  			    $(".ifa-article-zhe").show();
	  			    $('.ifa-article-space').css({'top':$(document).scrollTop()});
	  			}else{
	  				layer.msg('The system is busy!',{icon:2,time:2000});
	  			}
	  		}
	  	});
	  });
	  
	  //设置AUM是否显示
	  $("#aum_setting_eye").on("click",function(){ 
	  		$(this).toggleClass('setting_eyeac');
	  		var isShowAum = $(this).attr('field')=='1'?'0':'1';
	  		if(isShowAum!=''){
	  			$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/ifa/space/updateIfaIsShowAum.do?datestr="+ new Date().getTime(),
					data : {'isShowAum':isShowAum },
					beforeSend: function () {},
					complete: function () {},
					success : function(json) {
						var result = json.result;
						if(result==true||result=='true'){
							$("#aum_setting_eye").attr('field',isShowAum);
						}
					}
				});
	  		}
      });
	  
	  //设置Total Return Rate是否显示
	  $("#topporflio_setting_eye").on("click",function(){ 
	  		$(this).toggleClass('setting_eyeac');
	  		var showperformance = $(this).attr('field')=='1'?'0':'1';
	  		if(showperformance!=''){
	  			$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/ifa/space/updateIfaIsShowFerformance.do?datestr="+ new Date().getTime(),
					data : {'isShowperFormance':showperformance },
					beforeSend: function () {},
					complete: function () {},
					success : function(json) {
						var result = json.result;
						if(result==true||result=='true'){
							$("#topporflio_setting_eye").attr('field',showperformance);
						}
					}
				});
	  		}
      });
    
	  $(document).on("click","#space-article-close",function(){
	       $(".ifa-article-space").hide();
	       $(".ifa-article-zhe").hide();
	  });
	  $(document).on("click",".atricle-retopst-close",function(){
	       $(".ifa-article-space").hide();
	       $(".ifa-article-zhe").hide();
	  });
	  
	  //点赞
	  $(document).on("click",".class_article_like",function(){
		  var insightid= $(this).attr('insightid');
		  upordowninsight(insightid,'1');
	  });
	  
	 //踩
	  $(document).on("click",".class_article_notlike",function(){
		  var insightid= $(this).attr('insightid');
		  upordowninsight(insightid,'0');
	  });
	  
	  function upordowninsight(insightid,type){
		  if((window.sessionStorage.getItem(insightid+1) == 1 &&
  				type == '1') || (window.sessionStorage.getItem(insightid+0) == 1 &&
  	    				type == '0')){
				layer.msg("Do not repeat clicks!",{icon : 0,time : 2000});
				return false;
			}
		  
		  $.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/investor/myInsight/insightUpCounter.do?datestr="+ new Date().getTime(),
				data : {'insightId':insightid,'type':type },
				beforeSend: function () {},
				complete: function () {},
				success : function(json) {
					//console.log(json);
					var result = json.flag;
					if(result==true||result=='true'){
						if(type=='1'){
							var span_like_id = '#span_id_likd_'+insightid;
							$(span_like_id).html(json.insightInfo.upCounter);
							window.sessionStorage.setItem(insightid+1,1);
						}else{
							var span_id_down = '#span_id_down_'+insightid;
							$(span_id_down).html(json.insightInfo.downCounter);
							window.sessionStorage.setItem(insightid+0,1);
						}
					}
				}
			});
	  }
	  
});