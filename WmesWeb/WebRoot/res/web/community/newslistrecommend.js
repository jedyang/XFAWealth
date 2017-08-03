//newslistrecommend.js

define(function(require) {
	var $ = require('jquery');
	var angular = require('angular');
	var mybase = angular.module('ifaTable', ['truncate']);
	require("layui");
	
	var memberId=getQueryString("id");
	
	mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.dataList='';
		var iPAGE=1;
		var rows=10;
		
		$scope.datatotal=0;
		
		getDataList();
		function getDataList(){
			
			var url = base_root + "/front/community/space/ifaRecommendNews.do",
			data = "rows=" + rows + "&page=" + iPAGE +"&id="+memberId;
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
		

		 //点赞
	    $(document).on("click",".wmes-community-news-message-right-nice",function(){
	    	var id=$(this).attr("topicId");
	    	$(this).toggleClass("comment-topper-niceed");
	    	var isCancel="";
	    	if($(this).hasClass("comment-topper-niceed")){
	    		$(this).next().text(Number($(this).next().text())+1);
	    	
	    	}else{
	    		$(this).next().text(Number($(this).next().text())-1);
	    		isCancel="1";
	    	}
	    	
	    	$.ajax({
	    		type:"post",
	    		datatype:"json",
	    		url:base_root+'/front/community/info/saveBehavior.do',
	    		data:{id:id,type:"t",isCancel:isCancel,behavior:'like'},
	    		success:function(json){
	    			//console.log(json);
	    		}
	    		
	    	});
	    	var cai=$(this).parent().parent().find(".wmes-community-news-message-right-cai");
	    	if(cai.hasClass("comment-topper-caied")){
	    		cai.toggleClass("comment-topper-caied")
	    		cai.next().text(Number(cai.next().text())-1)
	    	}
	    })
	    
	     //踩
	    $(document).on("click",".wmes-community-news-message-right-cai",function(){
	    	$(this).toggleClass("comment-topper-caied");
	    	var id=$(this).attr("topicId");
	    	var isCancel="";
	    	if($(this).hasClass("comment-topper-caied")){
	    		$(this).next().text(Number($(this).next().text())+1);
	    	}else{
	    		$(this).next().text(Number($(this).next().text())-1);
	    		isCancel="1";
	    	}
	    	$.ajax({
	    		type:"post",
	    		datatype:"json",
	    		url:base_root+'/front/community/info/saveBehavior.do',
	    		data:{id:id,type:"t",isCancel:isCancel,behavior:'unlike'},
	    		success:function(json){
	    			//console.log(json);
	    		}
	    		
	    	})
	    	var nice=$(this).parent().parent().find(".wmes-community-news-message-right-nice");
	    	
	    	if(nice.hasClass("comment-topper-niceed")){
	    		nice.toggleClass("comment-topper-niceed")
	    		nice.next().text(Number(nice.next().text())-1)
	    	}
	    });
		
		
	}])
	
	
})