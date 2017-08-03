/**
 * fundsscreener.js 
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-14
 */

define(function(require) {
	var $ = require('jquery'),
		angular = require('angular');

		require("scrollbar");

		$('.funds_search_wrap').perfectScrollbar();
// 数据控制
	var mybase = angular.module('myFav', []);
    mybase.controller('Favctrl', ['$scope', '$http', function($scope, $http) {
    		var Sortdata = "sort=issue_price&order=desc&rows=1000";//排序条件初始化 默认根据issue price（增长率降序）
    		
    		//初始化数据
    		$scope.dataList = '';

	    	// 数字调用
	    	$scope.counter = Array;
	    	
	    	//监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){

					//删除渲染效果
					$(".flipInX").one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				    		$(".flipInX").removeClass();
				    });

					//you can also do something 

				}
			}

	    	// 正常拿数据	 
	    	function getDataList(){
	    		var url = base_root + "/front/fund/info/collectionListJson.do?" + Sortdata;
	    			$http({
                      url: url,
                      method: 'GET',
                      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                 	 })
	    			.success(function(response){

                        $scope.dataList =  response.list;
              
                    });
	    	}
	    	getDataList();

	    	// 排序点击
	    	$(".funds_sort").on("click",function(){
	    		if( $(this).find(".arrow_top").hasClass("top_active") ){
	    			$('.arrow_down').removeClass("down_active");
	    			$('.arrow_top').removeClass("top_active");
	    			$(this).find(".arrow_down").addClass("down_active");
	    			dataSoat($(this).attr("data-type"),"desc");
	    		}else if( $(this).find(".arrow_down").hasClass("down_active") ){
	    			$('.arrow_down').removeClass("down_active");
	    			$('.arrow_top').removeClass("top_active");
	    			$(this).find(".arrow_top").addClass("top_active");
	    			dataSoat($(this).attr("data-type"),"asc");
	    		}
	    		
	    	})

	    	//数据排序
	    	function dataSoat(type,sort){
	    		// $scope.dataList = '';
	    		Sortdata = "sort=" + type + "&order=" + sort;
	    		getDataList();
	    	}



			
	

  }]);
	$(".funds_search_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".funds_search_Part").hide().eq($(this).index()).show();
	});
	
	$(".white_background").on("click",".funds_heart",function(){
		var status  = $(this).attr("followFlag"),
		    fundId = $(this).attr("fundId"),
		    baseUrl = $("#site_base").val(),
			removeIndex = $(this).parents("tr").index();
		
		
    	$.ajax({
    		url:baseUrl+"/front/fund/info/setFoundCollection.do?r="+Math.random(),
    		data:{opType:status,fundId:fundId},
    		dataType:"json",
    		type:"get",
    		success:function(data){
    			var tabLenght = $(".funds_search_Part").length;
				for(var i = 0; i < tabLenght ;i++){
					$(".funds_search_Part").eq(i).find("tr").eq(removeIndex).addClass("animated bounceOut readyRemove");
				}
				$(".funds_search_information").find("tr").eq(removeIndex).addClass("animated bounceOut readyRemove");
				setTimeout(function(){
					$(".readyRemove").remove();
				},750);
    		},
    		error:function(){
    			
    		}
    	})
	})

});
