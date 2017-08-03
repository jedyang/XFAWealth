/**
 * fundifalist.js ifa列表页
 * @author 李裕恒
 * email: 673577011@qq.com
 * @date: 2016-03-13
 */

define(function(require) {
	var $ = require('jquery');
	require('layer');
	require("scrollbar");
	require("ionrangeSlider");
	var Loading = require('loading');
	var angular = require('angular');
	
	var mybase = angular.module('ifaTable', ['truncate']);
//	var searchData = null;
	$('.funds_search_wrap').perfectScrollbar();
	var oLoading = new Loading($(".wmes-community-content-left"));	
	mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1; //第N页数据
		var cmdFunction = 'getMyDynamicTopicListJson.do';
		var is_finish = true, //当前数据是否加载完成
			page_bol = true; //总页数控制
		//初始化数据
		$scope.dataList = '';
		// 数字调用
		$scope.counter = Array;
		$scope.defColor = $("#defColor").val();
		$scope.dateFormat=$("#dateFormat").val();
		$scope.dateTimeFormat=$("#dateFormat").val()+" HH:mm:ss";
		$scope.baseroot=base_root;
		//固定每次拿9条数据
		var rows = 5;
		var data ="";
		// 正常拿数据-我发表的与我关注的数据	 
		function getDataListForTab_1(data) {
			oLoading.show();
//			var element = null;
			
//			var keyWord=$("#txtkeywords").val();
			//data += '&keyWord=' + keyWord;
			is_finish = false;
			data+="rows=" + rows + "&page=" + iPAGE ;
			var url = base_root + "/front/community/info/"+cmdFunction;
			
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) { ////console.log(response);
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
		
		//我发表的与我关注的数据
		getDataListForTab_1(data);
		
		//点击更多时
		$('.clickmore').on('click', function() {
			$(this).hide();
			getDataListForTab_1(data);
		});
		
		
		//点击popularity各个tab
		$('.wmes-community-popularity-tabac li').on('click',function(){ 
			$(this).addClass('wmes-community-tabac').siblings().removeClass('wmes-community-tabac');
			var typeid = $(this).attr('typeid');
			if(typeid=='1'){
				$('.community-popularity-wrapcon').eq(0).show();
				$('.community-popularity-wrapcon').eq(1).hide();
				$('.community-popularity-wrapcon').eq(2).hide();
			}
			else if(typeid=='2'){
				$('.community-popularity-wrapcon').eq(0).hide();
				$('.community-popularity-wrapcon').eq(1).show();
				$('.community-popularity-wrapcon').eq(2).hide();
			}
			else if(typeid=='3'){
				$('.community-popularity-wrapcon').eq(0).hide();
				$('.community-popularity-wrapcon').eq(1).hide();
				$('.community-popularity-wrapcon').eq(2).show();
			}
		});
		
		//点击各个tab
		$(document).on('click','.wmes-community-topic-tab li',function(){  
			$(this).addClass('wmes-community-tabac').siblings().removeClass('wmes-community-tabac');
			var type = $(this).attr('type');
			type = type.toLowerCase();
			var typeid = $(this).attr('typeid');
			var keyWord=$("#txtkeywords").val();
			if(type=='dynamic') {
				iPAGE = 1;
				cmdFunction = 'getMyDynamicTopicListJson.do';
				data = "keyWord="+keyWord+"&";// + "rows=" + rows + "&page=" + iPAGE + "";
				$('.dataListTr').empty();
				getDataListForTab_1(data);
			}
			else if(type=='recommend') {
				iPAGE = 1;
				cmdFunction = 'getRecommandTopicListJson.do';
				data = "keyWord="+keyWord+"&";// +"rows=" + rows + "&page=" + iPAGE + "&";
				$('.dataListTr').empty();
				getDataListForTab_1(data);
			}
			else { //版块
				iPAGE = 1;
				cmdFunction = 'getSetionTypeTopicListJson.do';
				data = "keyWord="+keyWord+ "&secitionid="+typeid+"&";// +"rows=" + rows + "&page=" + iPAGE +"&";
				$('.dataListTr').empty();
				getDataListForTab_1(data);
			}
			windowHeight();
				
		});
		
		
		//跳转到发布页面
	    $('.wmes-community-searchbox-click').on('click',function(){
			window.location.href = base_root + '/front/community/info/publishTopic.do';
		});
	    
	    //搜索贴子
	    $('.wmes-community-searchbox-search').on('click',function(){
//	    	$('.dataListTr').empty();
//	    	var keyWord=$("#txtkeywords").val();
//	    	iPAGE = 1;
//	    	data = 'rows=' + rows + '&page='+iPAGE+'&keyWord=' + keyWord;
//	    	getDataListForTab_1(data);
	    	var keyWord=$("#txtkeywords").val();
	    	var params = 'keywords='+keyWord;//encodeURI('keywords='+keyWord); alert(params);
	    	window.location.href = base_root + '/front/community/info/search.do?' + params;
		});
	    
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
						//Initialization();
//						var counter = '';
//						if(type == 't'&&iscancel=='0'){ 
//							var oldcount = parseInt($(selt).next('span').text());
//							oldcount += 1;
//							$(selt).attr('iscancel','0');
//							$(selt).next('span').text(oldcount);
//							$(this).addClass('comment-topper-niceed');
//						}else{ 
//							var oldcount = parseInt($(selt).next('span').text());
//							oldcount = oldcount-1;
//							$(selt).attr('iscancel','1');
//							$(selt).next('span').text(oldcount);
//							$(this).toggleClass('wmes-community-news-message-right-nice');
//						}
						
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
						//Initialization();
						var counter = '';
//						if(type == 't'&&iscancel=='0'){ 
//							var oldcount = parseInt($(selt).next('span').text());
//							oldcount += 1;
//							$(selt).attr('iscancel','0');
//							$(selt).next('span').text(oldcount);
//							$(this).addClass('comment-topper-niceed');
//						}else{ 
//							var oldcount = parseInt($(selt).next('span').text());
//							oldcount = oldcount-1;
//							$(selt).attr('iscancel','1');
//							$(selt).next('span').text(oldcount);
//							$(this).toggleClass('wmes-community-news-message-right-nice');
//						}
						
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
	    
	    //跳转页面
	    $(document).on('click','.headPortrait',function(){
			var hidloginMemberId = $('#hidloginMemberId').val();//
			var hidloginMemberType = $('#hidloginMemberType').val();
			var memberid = $(this).attr('memberid');//被查看人的ID
			var membertype = $(this).attr('membertype');//被查看人的类型
			if(hidloginMemberType=='1'){ //如果登录的是投资人
				if(hidloginMemberId==memberid){//如果当前登录的人跟所要查看的人是一样的，则跳转去这是investor的space链接
					//window.location.href = base_root + '/front/investor/home.do';
					window.location.href = base_root + '/front/community/space/investorSpace.do?id='+memberid;
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
    mybase.filter('hightlight', function () {
	  return function (value, wordwise, max, tail) {
//		 var keyword = 'z';
		//去除html标签，图片，换行，回车
		value = value.replace(/&nbsp;/g, "");  
		value = value.replace(/(\n)/g, "");  
		value = value.replace(/(\t)/g, "");  
		value = value.replace(/(\r)/g, "");  
		value = value.replace(/<\/?[^>]*>/g, "");  
		//value = value.replace(/\s*/g, "");   //匹配一个空白字符
	   
	    return value + '<span class="highlight">zzzzzz</z>';
	  };
	});
	
    
    
	
	
	$('.wmes-community-news-message-right-caicon').on('click',function(){
		$(this).find('.wmes-community-news-message-right-cai').toggleClass('wmes-community-news-message-right-caied');
	});
	
	$('.wmes-community-searchbox-choose').on('click',function(){
		$('.wmes-community-searchbox-choose-choose').toggleClass('wmes-community-searchbox-choose-chooseac');
	});
	
	$(document).on('click','.wmes-community-searchbox-choose-chooseac li',function(){
		$('.wmes-community-searchbox-choose-txt').text($(this).text());
		$('.wmes-community-searchbox-choose-choose').removeClass('wmes-community-searchbox-choose-chooseac');
	});
	
	$(document).on("click",".register_xiala_long input",function(){
		$(this).siblings(".regiter_xiala_ul").show();
	});
	$(document).on('click','.register_xiala_ico',function(){
		if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none'){
			$(this).siblings(".regiter_xiala_ul").show();
		}else{
			$(this).siblings(".regiter_xiala_ul").hide();
		};
	});
	$(document).on('mouseleave','.register_xiala_long',function(){
		$(this).find('.regiter_xiala_ul').hide();
	});
	$(".regiter_xiala_ul").on("click","li",function(){
		$(this).parent().siblings('.value_hidden').val( $(this).attr("value") );
		$(this).parent().siblings('.value_show').val( $(this).text() );
		$(".regiter_xiala_ul").hide();
	});
	$("#txtkeywords").keyup(function(event){
      	if(event.keyCode == 13){
	        $('.wmes-community-searchbox-search').click()
	    }
	});	
});