define(function(require) {
	var $ = require('jquery');
	require('layer');
	require("scrollbar");
	sessionStorage.removeItem("clientdetailtab");
	var angular = require('angular');
	//查询条件
	//点击每个选项，在下面的已选方案中添加该选项
	$(".funds_choice li").on("click",function(){
		if($(this).parent().hasClass("funds_logo_b")){
			return;
		}
		var selection_criterialenght = $(".selection_criteria li").length;
		for(var i = 0; i < selection_criterialenght ;i++){
			if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
				$(".selection_criteria li").eq(i).remove();
			}
		}
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
		if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
			$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>');
		}
		if($(this).data('name') == 'period' && $(this).hasClass('.funds_all')){
			$('#hidPeriod').val('');
			$('#hidPeriodType').val('');
		}else if($(this).data('name') == 'period'){
			$('#hidPeriod').val($(this).data('value'));
			$('#hidPeriodType').val($(this).data('type'));
		}if($(this).data('name') == 'status' && $(this).hasClass('.funds_all')){
			$('#hidStatus').val('');
		}else if($(this).data('name') == 'status'){
			$('#hidStatus').val($(this).data('value'));
		}
		$('#btnKeyWord').click();
	});
	//执行清除方案点击操作
	$(".funds_title_selection").on("click",function(){
		$(".selection_criteria li").remove();
		$(".fund_logo_active").removeClass("fund_logo_active");
		$(".fund_choice_active").removeClass("fund_choice_active");
		$(".funds_all").addClass("fund_choice_active");
	});	
	$(".selection_criteria").on("click",".selection_delete",function(){
		$(this).parent().remove();
		var funds_all_lenght = $('.funds_all').length;
		for( var i = 0; i < funds_all_lenght ; i++){
			if($(this).parent().attr("data-name") == "FundHouse"){
				var fundslenght = $("#funds_logo li").length;
				for(var funds = 0 ; funds < fundslenght ;funds++){
					if( $(this).parent().attr("data-value") ==  $("#funds_logo li").eq(funds).attr("data-value") ){
						$("#funds_logo li").eq(funds).click();
					}
				}
				break;
			}else if( $(this).parent().attr("data-name") ==  $('.funds_all').eq(i).attr("data-name") ){
				$('.funds_all').eq(i).click();
			}
		}
		var prefCount=0;
		$(".selection_criteria").find("li").each(function(){
			$(this).attr("data-value")=="pref";
			prefCount++;
		});
		if(prefCount==0)$("#per_all").addClass("per_active");
	});	
	/**
	 * 显示清除所有搜索条件按钮
	 * */
	function selection(){
		var _thisLenght =  $(".selection_criteria").children().length;
		if( _thisLenght != 1  ){
			$(".funds_title_selection").css('display','inline-block');
		}else{
			$(".funds_title_selection").css('display','none');
		}
	}	
	$("#yfunds_hidde_title").on("click",function(){
		if($(this).parent().hasClass("funds_more_wrap_show")){
			$(this).parent().removeClass("funds_more_wrap_show");
			 $(".fund_more_content").stop().animate({ 
		    	height: "0px"
			  }, 300 );
	 		$(this).find("span").eq(0).css("display","inline-block");
	 		$(this).find("span").eq(1).css("display","none");
		}else{
			$(this).parent().addClass("funds_more_wrap_show");
			$(".fund_more_content").stop().animate({ 
		    	height: "100%"
			 }, 300 );
			$(this).find("span").eq(1).css("display","inline-block");
	 		$(this).find("span").eq(0).css("display","none");
		}
	});
	 var mybase = angular.module('proposal', ['wmespage','truncate']);
	 mybase.controller('proposalCtrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1;
		var rows = 10; 
		$scope.dataList = '';
    	function getDataList(){
    		var keyWord = $('#txtKeyWord').val()
    		var period = $('#hidPeriod').val();
			var periodType = $('#hidPeriodType').val();
			var status = $('#hidStatus').val();
    		var data = {
            	  rows:rows,  
            	  page:iPAGE, 
            	  filterPeriod:period,
            	  filterPeriodType:periodType,
            	  filterKeyWord:keyWord,
            	  filterStatus:status
            };
    		var url = base_root + "/console/ifa/proposalJsonList.do?dateStr=" + new Date().getTime();
			$http({
              url: url,
              method: 'POST',
              headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
              params : data
         	 })
			.success(function(response){//console.log(response);
                if(response.total > 0){
        		  $scope.total = response.total;
        		  $scope.proposalConf.totalItems = response.total;
            	  $(".no_list_tips").hide();
                  $scope.dataList =  response.list;
          		}else{
          		  $scope.proposalConf.totalItems = 0;
          		  $scope.total = 0;
 				  $scope.dataList = '';
 				  $(".no_list_tips").show();
          		}
            });
    	}
    	$scope.proposalConf = {
            itemsPerPage:rows,
            onChange: function(){
            	iPAGE = $scope.proposalConf.currentPage;
            	getDataList();
            }
        };
    	$scope.searchKeyBtn = function(){
    		getDataList();
    	};
    	getDataList();
	 }]);
	 $(document).keypress(function(e) {  
       if(e.which == 13) {
    	  $('#btnKeyWord').click();
       }  
	 });
});	