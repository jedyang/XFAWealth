/**
 * ifa管理推荐新闻页面
 * @author mqzou
 * @date: 2017-03-21
 */

define(function(require) {
	var $ = require('jquery');
	var angular = require('angular');
	var mybase = angular.module('ifaTable', ['truncate']);
	require("layui");
	
	mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.dataList='';
		var iPAGE=1;
		var rows=10;
		
		$scope.datatotal=0;
		
		getDataList();
		function getDataList(){
			var keyWord=$("#searchKeyWord").val();
			var url = base_root + "/front/community/space/getIfaRecommendNews.do",
			data = "rows=" + rows + "&page=" + iPAGE+"&keyWord="+keyWord;
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) {
					if(response.list.length > 0){
						$scope.datatotal=response.total;
					// $scope.insightConf.totalItems = response.total;//设置总数
					if( iPAGE === 1 ){
             		 	 $scope.dataList = '';
                         $scope.dataList =  response.list;
                     }else{
                        $scope.dataList = $scope.dataList.concat(response.list);
                     }
					
					var totalPage=Math.ceil(response.total/rows);
					if(iPAGE<totalPage){
						$('.clickmore').show();
					}else{
						$('.clickmore').hide();
					}
					
					
	         		}else{
	        			 if( iPAGE === 1 ){
	        				 $(".dataListTr").hide();
	        				 $(".no_list_tips").show();
	         			 }
	         		}
					
				})
		}
		$('.clickmore').on('click',function(){
			iPAGE++;
			getDataList();
		})
		
		$("#searchKeyBtn").on('click',function(){
			iPAGE=1;
			getDataList();
		})
	
		$(document).on('click','.delete',function(){
			var id=$(this).attr("topicId");
			$.ajax({
				type:'post',
				datatype:'json',
				url:base_root+'/front/community/space/delIfaRecommendNews.do',
				data:{id:id},
				success:function(json){
				    if(json.result){
				    	layer.msg("已删除该新闻推荐");
				    }else{
				    	layer.msg("删除新闻推荐失败");
				    }
				}
			})
		})
		
	}])
	
	
})