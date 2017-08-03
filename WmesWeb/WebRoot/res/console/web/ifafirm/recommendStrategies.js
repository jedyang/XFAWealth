define(function(require) {
	'use strict';
	var $ = require('jquery');
	require('layer');		
	var Loading = require('loading');
	var oLoading = new Loading($('.tabcutcon'));
	var iLoading = new Loading($('.tabcutcon1'));
	
	var angular = require('angular');	
	var mybase = angular.module('ifaTable', ['truncate']);
	//var searchData = null;
    mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
    	//--- 分页列表数据显示定义  开始 ---
		var searchData = "";//搜索条件初始化
		//初始化数据
		$scope.dataList = '';
		// 数字调用
		$scope.counter = Array;
		
		$scope.isPublic = 1,
		$scope.status = '';
		$scope.dateTimeFormatStr=$("#dateTimeFormat").val();
		$scope.dateFormatStr=$("#dateFormat").val();
		//固定每次拿10条数据
		var rows = 10;
		var currPage = 1;
		
		
		// 正常拿数据	 
    	function getDataList(num){
    		oLoading.show();
    		iLoading.show();
    		var keyWord = $('#keyword').val();
    		var period=$(".funds_choice").find(".fund_choice_active").attr("data-value");
    		if(period=="other"){
    			period=$("#fromDate").val()+"to"+$("#toDate").val();
    		}
    		var iPAGE = Number(num);
    		//var url = base_root + "/front/strategy/info/myListJson.do",
    		var url = base_root +"/console/ifa/ifaRecommendStrategyJson.do?dateStr="+new Date().getTime(),
			data =  "rows="+ rows +"&page=" + iPAGE +"&"+searchData+"&keyword=" + keyWord+"&period="+period,
			//控制数据是否加载完成
			is_finish = false;
			$http({
				url: url,
				method: 'POST',
				data : data,
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
         	 })
			.success(function(response){
                is_finish = true;
                if(response.list.length > 0){
          		 	 $scope.dataList = '';
                     $scope.dataList =  response.list;
                     $(".no_list_tips").hide();
              	}else{//无返回数据时
              		$scope.dataList = '';
     				$(".dataListTr").hide();
     				$(".no_list_tips").show();
              	}
    			// 总记录数
    			$scope.datatotal = response.total;
    			// 总页数
    			$scope.pages = Math.ceil(response.total/rows);
    			//if ($scope.pages<1) $scope.pages=1;
    			var sumPage = $scope.pages;
    			// 当前页
        		if (iPAGE<1) iPAGE=1;
        		if (iPAGE>sumPage) iPAGE=sumPage;
        		//currPage = iPAGE;
    			//分页显示
    			var ListPage = [];
    			//页码内容
    			if ( sumPage <= 5){
    				for(var i = 0 ; i < sumPage ;i++){
//    					////console.log(sumPage)
    					ListPage.push({'PageShow':i+1});
    				}
    			}else if( iPAGE < sumPage - 2){
    				for(var k = iPAGE ; k < iPAGE + 3 ; k++ ){
//    					////console.log(iPAGE + ' --'+ iPAGE + 3)
    					ListPage.push({'PageShow':k});
    				}
    				ListPage.push({'PageShow':"..."});
    				ListPage.push({'PageShow':sumPage});
    			}else{
    				ListPage.push({'PageShow':1});
    				ListPage.push({'PageShow':"..."});	
    				for(var j = sumPage - 2 ; j <= sumPage ; j++ ){
//    					////console.log(sumPage)
    					ListPage.push({'PageShow':j});
    				}
    			}
    			/* 分页数据 */
			    $scope.page = {
			    	'nowPage': iPAGE,
			    	'totalPage': sumPage,
			    	'ListPage' : ListPage
			    };
			    oLoading.hide();
			    iLoading.hide();
            });
    	}
    	//绑定【页码】跳转事件
    	$(document).on("click",".wmes_pagint_num",function(){
			if($(this).attr("data-page") != "..." && $(this).attr("data-page") != $('.wmes_pagint_now').attr("data-page")){
				getDataList($(this).attr("data-page"));
			}
		});
    	
    	//初始化，获取第一页数据
    	getDataList(1);
    	
    	
    	$("#searchkey").on('click',function(){
    		getDataList(1);
    	})
    	
    	$(".funds_choice li").on("click",function(){
	    	if($(this).parent().hasClass("funds_logo_b")){
				return;
			}
			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
	        if($(this).hasClass("recommend-date-choice") && $(this).hasClass("fund_choice_active")){
	            $(this).parents(".funds_choice").find(".recommend-other-wrap").addClass("recommend-other-block");
	            return;
	        }else if($(this).parents(".funds_choice").find(".recommend-other-wrap").length > 0){
	            $(this).parents(".funds_choice").find(".recommend-other-wrap").removeClass("recommend-other-block");
	        }
			
			if($(this).index()==0){
				$(this).closest('ul').find('.funds_all').addClass('fund_choice_active2');
			}else{
				$(this).closest('ul').find('.funds_all').removeClass('fund_choice_active2');
			};
			getDataList(1);
       })
       
       $(".recommend-date-ok").on('click',function(){
    	   getDataList(1);
       })
    
    
    }])
    
    
    
	
})