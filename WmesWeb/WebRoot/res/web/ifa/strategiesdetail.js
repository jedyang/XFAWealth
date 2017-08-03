define(function(require) {
	var $ = require('jquery'),
		angular = require('angular');

		require("scrollbar");

		$('.funds_search_wrap').perfectScrollbar();
	// 数据控制
	var mybase = angular.module('myStrategies', []);
    mybase.controller('Strategies', ['$scope', '$http', function($scope, $http) {
    		var Sortdata = "sort=issue_price&order=desc&rows=1000";//排序条件初始化 默认根据issue price（增长率降序）
    		
    		//初始化数据
    		$scope.dataList = '';
	    	// 正常拿数据	 
	    	function getDataList(){
	    		var url = base_root + "/front/fund/info/getFundListJson.do?" + Sortdata;
	    			$http({
                      url: url,
                      method: 'POST',
                      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                 	 })
	    			.success(function(response){
		                // //console.log(JSON.stringify(response.list));
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

    $(".strategies_more").on("click",function(){
    	$(this).parents(".strategies_list_wrap").toggleClass("strategies_narrow");
    });

});