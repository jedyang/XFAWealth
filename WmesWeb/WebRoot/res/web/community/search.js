/**
 * search.js 社区搜索页面
 * @author 李裕恒
 * email: 673577011@qq.com
 * @date: 2016-03-20
 */
define(function(require) {
	var $ = require('jquery');
	require('layer');
	require("scrollbar");
	require("ionrangeSlider");
	var Loading = require('loading');
	var angular = require('angular');
	var is_finish,page_bol;
	var mybase = angular.module('ifaTable', ['truncate']);
//	var searchData = null;
	$('.funds_search_wrap').perfectScrollbar();
	var oLoading = new Loading($(".wmes-community-content-left"));	
	mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1; //第N页数据
		var cmdFunction = 'getTopicListJson.do';//默认第一条的调用
		is_finish = true, //当前数据是否加载完成
			page_bol = true; //总页数控制
		//初始化数据
		$scope.dataList = '';
		$scope.userList = '';
		// 数字调用
		$scope.counter = Array;
		$scope.defColor = $("#defColor").val();
		$scope.dateFormat=$("#dateFormat").val();
		$scope.dateTimeFormat=$("#dateFormat").val()+" HH:mm:ss";
		$scope.baseroot=base_root;
		//搜索的关键字
		var keywords = getQueryString('keywords'); 
		$("#txtkeywords").val(keywords);
		$('#sp_keyword').text(keywords);
		//固定每次拿9条数据
		var rows = 5;
		var data = "rows=" + rows + "&page=" + iPAGE + "&keyWord=" + keywords;
		// 正常拿数据-我发表的与我关注的数据	 
		function getDataListForTab_1(data) {// alert(data);
			oLoading.show();
//			var element = null;
			
//			var keyWord=$("#txtkeywords").val();
			//data += '&keyWord=' + keyWord;
			is_finish = false;

			var url = base_root + "/front/community/info/"+cmdFunction;
			
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) { //console.log(response);
					oLoading.hide();
					if(response.rows.length > 0) {
						$(".clickmore").show();
						$(".dataListTr").show();
						$(".no_list_tips").hide();
						// 总页数
						var sumPage = Math.ceil(response.total / rows);
						if(iPAGE > sumPage) { 
							return;
						}
						if(iPAGE === 1) {
							$scope.dataList = '';
							$scope.dataList = response.rows;
						} else {
							$scope.dataList = $scope.dataList.concat(response.rows);
						} 
						if(iPAGE >= sumPage) {
							page_bol = false;
							$(".clickmore").hide();
						} 
					} else {
						if(iPAGE === 1) {
							$scope.dataList = '';
							$(".dataListTr").hide()
							$(".no_list_tips").show();
							
							page_bol = false;
							$(".clickmore").hide();
						}
					} 
					iPAGE++;
					$scope.datatotal = response.total;

					is_finish = true;
				});
		}
		
		function getDataListForTab_user(data) {// alert(data);
			oLoading.show();
//			var element = null;
			
//			var keyWord=$("#txtkeywords").val();
			//data += '&keyWord=' + keyWord;
			is_finish = false;

			var url = base_root + "/front/community/info/"+cmdFunction;
			
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) { //console.log(response);
					oLoading.hide();
					if(response.rows.length > 0) {
						$(".clickmore").show();
						$(".userListTr").show();
						$(".wmes-community-user-con").show();
						$(".dataListTr").hide();
						$(".no_list_tips").hide();
						// 总页数
						var sumPage = Math.ceil(response.total / rows);
						if(iPAGE > sumPage) { 
							return;
						}
						if(iPAGE === 1) {
							$scope.userList = '';
							$scope.userList = response.rows;
						} else {
							$scope.userList = $scope.userList.concat(response.rows);
						} 
						if(iPAGE >= sumPage) {
							page_bol = false;
							$(".clickmore").hide();
						} 
					} else {
						if(iPAGE === 1) {
							$scope.userList = '';
							$(".userListTr").hide()
							$(".no_list_tips").show();
							
							page_bol = false;
							$(".clickmore").hide();
						}
					} 
					iPAGE++;
					$scope.datatotal = response.total;

					is_finish = true;
				});
		}
		
		//我发表的与我关注的数据
		getDataListForTab_1(data);
		
		//点击更多时
		$('.clickmore').on('click', function() { 
			$(this).hide();
			var curdata = "rows=" + rows + "&page=" + iPAGE + "&keyWord=" + keywords;
			var curtype = $('.wmes-community-tabac').attr('type');
			if(curtype=='user')getDataListForTab_user(curdata);
			else getDataListForTab_1(curdata);
		});
		
		//点击各个tab
		$(document).on('click','.wmes-community-topic-tab li',function(){  
			$(this).addClass('wmes-community-tabac').siblings().removeClass('wmes-community-tabac');
			var type = $(this).attr('type');
			var keyWord=$("#txtkeywords").val();
			$(".userListTr").hide();
			if(type=='topic') {
				iPAGE = 1;
				cmdFunction = 'getTopicListJson.do';
				data = "keyWord="+keyWord+"&" + "rows=" + rows + "&page=" + iPAGE + "";
				$('.dataListTr').empty();
				getDataListForTab_1(data);
			}
			else if(type=='question') {
				iPAGE = 1;
				cmdFunction = 'getSearchQuestionListJson.do';
				data = "keyWord="+keyWord+"&" +"rows=" + rows + "&page=" + iPAGE + "&";
				$('.dataListTr').empty();
				getDataListForTab_1(data);
			}
			else if(type=='insight') {
				iPAGE = 1;
				cmdFunction = 'getSearchInsightListJson.do';
				data = "keyWord="+keyWord+"&" +"rows=" + rows + "&page=" + iPAGE + "&";
				$('.dataListTr').empty();
				getDataListForTab_1(data);
			}
			else if(type=='user') {
				iPAGE = 1;
				cmdFunction = 'getSearchUserListJson.do';
				data = "keyWord="+keyWord+"&" +"rows=" + rows + "&page=" + iPAGE + "&"; 
				$('.userListTr').empty();
				$('.userListTr').show();
				$('.dataListTr').hide();
				getDataListForTab_user(data);
			}
				
		});
		
		//跳转到发布页面
	    $('.wmes-community-searchbox-click').on('click',function(){
			window.location.href = base_root + '/front/community/info/publishTopic.do';
		});
	    
	    //搜索贴子
	    $('.wmes-community-searchbox-search').on('click',function(){
	    	
	    	$('.dataListTr').empty();
	    	var keyWord=$("#txtkeywords").val();
	    	window.location.href = base_root + '/front/community/info/search.do?keywords='+keyWord+'';
	    	
		});
	    
	    $(".wmes-community-content-right li").on('click',function(){
	    	$('.dataListTr').empty();
	    	var keyWord=$(this).text();
	    	window.location.href = base_root + '/front/community/info/search.do?keywords='+keyWord+'';
	    })
	    
	    //赞
	    $(document).on('click','.wmes-community-news-message-right-nice',function(){ 
	    	var selt = this;
	    	var type = $(selt).attr('type');
	    	var topicid = $(selt).attr('topicid');
	    	var behavior = $(selt).attr('behavior');
//	    	var iscancel = $(selt).attr('iscancel');
	    	
	    	$(selt).toggleClass("wmes-community-news-message-right-niceed");
	    	var isCancel="";
	    	if($(selt).hasClass("wmes-community-news-message-right-niceed")){
	    		$(selt).next().text(Number($(selt).next().text())+1);
	    	
	    	}else{
	    		$(selt).next().text(Number($(selt).next().text())-1);
	    		isCancel="1";
	    	}
	    	
	    	$.ajax({
				url:base_root+"/front/community/info/saveBehavior.do",
				data:{'id':topicid,'type':type,'behavior':behavior,'isCancel':isCancel},
				type:"post",
				success:function(object){ //console.log(object);
					if(object.result==true||object.result=='true'){ 
//						var counter = '';
					}
				}
			});
			
	    	var prevcai = $(selt).parent().prev().find('.wmes-community-news-message-right-cai');
	    	if( prevcai.hasClass("wmes-community-news-message-right-caied")){
	    		prevcai.toggleClass("wmes-community-news-message-right-caied");
	    		var number = prevcai.next('span').text(); 
	    		prevcai.next().text(number-1);
	    	}
		});
	    
	  //扔鸡蛋
	    $(document).on('click','.wmes-community-news-message-right-cai',function(){ 
	    	var selt = this;
	    	var type = $(selt).attr('type');
	    	var topicid = $(selt).attr('topicid');
	    	var behavior = $(selt).attr('behavior');
	    	
	    	$(selt).toggleClass("wmes-community-news-message-right-caied");
	    	var isCancel="";
	    	if($(selt).hasClass("wmes-community-news-message-right-caied")){
	    		$(selt).next().text(Number($(selt).next().text())+1);
	    	
	    	}else{
	    		$(selt).next().text(Number($(selt).next().text())-1);
	    		isCancel="1";
	    	}
	    	
	    	$.ajax({
				url:base_root+"/front/community/info/saveBehavior.do",
				data:{'id':topicid,'type':type,'behavior':behavior,'isCancel':isCancel},
				type:"post",
				success:function(object){ //console.log(object);
					if(object.result==true||object.result=='true'){ 
//						var counter = '';
					}
				}
			});
			
	    	var prevcai = $(selt).parent().next().find('.wmes-community-news-message-right-nice');
	    	if( prevcai.hasClass("wmes-community-news-message-right-niceed")){
	    		prevcai.toggleClass("wmes-community-news-message-right-niceed");
	    		var number = prevcai.next('span').text(); 
	    		prevcai.next().text(number-1);
	    	}
		});
	    
	    //关注或取消关注元素绑定方法
	    $(document).on("click",".wmes-space-add-attention",function(){ 
	    	var obj=$(this).find(".wmes-space-information-bottom-img1")[0];
	    	var memberId=$(obj).attr("memberId");
	    	var type=$(obj).attr("type");//alert(type);alert(memberId);
	    	if("add"==type){
	    		addFocus(memberId,obj);
	    	}else{
	    		  
	    	} cancelFocus(memberId,obj);
	    });
	    
	  //加关注
		function addFocus(id,obj){
			$.ajax({
				type:'post',
				datatype:'json',
				url:base_root+'/front/community/space/addFocus.do',
				data:{id:id},
				success:function(json){
					//console.log(json);
					$(obj).addClass("wmes-space-information-bottom-img0");
					$(obj).next().text("取消关注");
//					if($(obj).hasClass("wmes-space-information-bottom-img0")){
//						
//					}else{
//						$(obj).next().text("加关注");
//					}
				}
			})
		}
		
		//取消关注
		function cancelFocus(id,obj){
			$.ajax({
				type:'post',
				datatype:'json',
				url:base_root+'/front/community/space/cancelFocus.do',
				data:{id:id},
				success:function(json){
					//console.log(json);
					$(obj).toggleClass("wmes-space-information-bottom-img0");
					if($(obj).hasClass("wmes-space-information-bottom-img0")){
						$(obj).next().text("取消关注");
					}else{
						$(obj).next().text("加关注");
					}
				}
			})
		}
		
		//跳转页面
	    $(document).on('click','.headPortrait',function(){
			var hidloginMemberId = $('#hidloginMemberId').val();//
			var hidloginMemberType = $('#hidloginMemberType').val();
			var memberid = $(this).attr('memberid');//被查看人的ID
			var membertype = $(this).attr('membertype');//被查看人的类型
			if(hidloginMemberType=='1'){ //如果登录的是投资人
				if(hidloginMemberId==memberid){//如果当前登录的人跟所要查看的人是一样的，则跳转去这是investor的space链接
					window.location.href = base_root + '/front/investor/home.do';
				} else{
					//如果查看的是投资人
					if(membertype=='1')
						window.location.href = base_root + '/front/community/space/investorSpace.do?id='+memberid;
					else //如果查看的是IFA
						window.location.href = base_root + '/front/community/space/ifaSpace.do?id='+memberid;
				}	
			} else{//当前登录的是非投资人
				//如果查看的是投资人
				if(membertype=='1')
					window.location.href = base_root + '/front/community/space/investorSpace.do?id='+memberid;
				else //如果查看的是IFA
					window.location.href = base_root + '/front/community/space/ifaSpace.do?id='+memberid;
			}
		});
		
	}]); //angular.js end
	
	//angular 自定义过滤器截取字符
    mybase.filter('cut', function () {
	  return function (value, wordwise, max, tail) {
		//去除html标签，图片，换行，回车
		value = value.replace(/&nbsp;/g, "");  
		value = value.replace(/(\n)/g, "");  
		value = value.replace(/(\t)/g, "");  
		value = value.replace(/(\r)/g, "");  
		value = value.replace(/<\/?[^>]*>/g, "");  
		//value = value.replace(/\s*/g, "");   //匹配一个空白字符
	    if (!value) return '';
	    max = parseInt(max, 10);
	    if (!max) return value;
	    if (value.length <= max) return value;
	    value = value.substr(0, max);
	    if (wordwise) {
	      var lastspace = value.lastIndexOf(' ');
	      if (lastspace != -1) {
	        value = value.substr(0, lastspace);
	      }
	    }
	    return value + (tail || '…');
	  };
	});
    
  //angular 自定义过滤器截取字符
    mybase.filter('hightlight', function ($sce) {
    	  return function (value) {
    		 var keywords = getQueryString('keywords'); 
    		 var subStr=new RegExp(keywords,'ig');//创建正则表达式对象,不区分大小写,全局查找
    		 value=value.replace(subStr,'<span class="hightlight">'+keywords+'</span>');//把'is'替换为空字符串
    		 return $sce.trustAsHtml(value);
    	  };
    	});
	
	function windowHeight(){
		var windowHeight = $(window).height();
		windowHeight = windowHeight-70;
		if($('.wmes-content').height() < windowHeight){
				$('.wmes-content').css('min-height',windowHeight);
                $('.wmes-notop-content').css('min-height',windowHeight);
		};
	};
	
	$('.wmes-community-tab li').on('click',function(){
		$(this).addClass('wmes-community-tabac').siblings().removeClass('wmes-community-tabac');
		$('.wmes-community-news-con').removeClass('wmes-community-news-conac').eq($(this).index()).addClass('wmes-community-news-conac');
		windowHeight();
	});
	
	$('.wmes-community-topic-collect').on('click',function(){
		$(this).toggleClass('wmes-community-topic-collected');
	});
	
	$("#txtkeywords").keyup(function(event){
      	if(event.keyCode == 13){
	        $('.wmes-community-searchbox-search').click()
	    }
	});	
	
});