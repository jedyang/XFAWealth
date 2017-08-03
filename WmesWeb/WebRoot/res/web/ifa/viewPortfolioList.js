define(function(require) {
	var $ = require('jquery');
			require("echarts");
			var angular = require('angular');	
			var mybase = angular.module('ifaTable', ['truncate']);
			require("scrollbar");
			require('layer');
			var Loading = require('loading');
			var oLoading = new Loading($(".strategies_list"));	
			
	   mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
			var iPAGE = 1,
			 is_finish = true,// 当前数据是否加载完成
			 page_bol = true;// 总页数控制
			// 初始化数据
			$scope.dataList = '';
			$scope.dateFormat=$("#dateFormat").val();
			// 数字调用
			$scope.counter = Array;
			// 监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){

					/*setTimeout(function(){
						initChar();// 初始化图表
					},100)*/
					
				}
			}
			// 固定每次拿9条数据
			var rows = 9;
			// 正常拿数据
	    	function getDataList(){
	    		oLoading.show();
	    		$(".portfolio_list_ul").show();
	    		$(".no_list_tips").hide();
	    		is_finish=false;
	    		var keyWord=$("#fundName").val();
	    		////console.log(keyWord);
	    		var url = base_root + "/front/ifa/space/viewPofolioListJson.do?id="+id+"&keyWord="+keyWord,
	    			 data =  "rows="+ rows +"&page=" + iPAGE +"&checkIsMyFollow=1";
	    			$http({
	                  url: url,
	                  method: 'POST',
	                  data : data,
	                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
	             	 })
	    			.success(function(response){
	    				oLoading.hide();
	                      if(response.list.length > 0){
	                    	  $(".dataListTr").show()
	                    	  
	                    	  $(".clickmore").show();
	                    	// 总页数
	                  		var sumPage = Math.ceil(response.total / rows);
	                  	  if(iPAGE > sumPage){
	                  		  return;
	                  	  }
	                    		 if( iPAGE === 1 ){
	                    		 	 $scope.dataList = '';
	                               $scope.dataList =  response.list;
	                            }else{
	                               $scope.dataList = $scope.dataList.concat(response.list);
	                            }
	                        
		                      		if(iPAGE >= sumPage){
		                      			page_bol = false;
		                      			 $(".clickmore").hide();
		                      		}
	                		}else{
	               			 if( iPAGE === 1 ){
	               				 $scope.dataList = '';
	               				 $(".dataListTr").hide()
	               				 $(".no_list_tips").show();
	               				$(".clickmore").hide();
	                			 }
	                		}
			    			iPAGE++;
			    			$scope.datatotal = response.total;
			    			is_finish=true;
			    			$scope.defColor=$("#defColor").val();
			    			// //console.log("getDataList");
			    			
	                });
	    	}
	    	getDataList();
	    	$(".clickmore").on("click",function(){
	    		getDataList();
	    	})
	    	
	    	/*// 滚动条件
	    	var scrollBol = false;

	    	// 滚动拿数据
	    	setTimeout(function(){
	    		$(window).on('scroll', GetScroll);
	    	},1000);	

	    	function GetScroll(){
	    		var docheight = $(window).scrollTop() + $(window).height(),
	    			listheight = $('.strategies_list_wrap ').offset().top + $('.strategies_list_wrap ').height() + $(".wmes_top").height();
	    		
	    		if( docheight > listheight ){
	    			scrollBol = true;
	    			
	    		}else{
	    			scrollBol = false;
	    		}

				if (scrollBol && is_finish && page_bol ){
					getDataList();
				}

	    	}*/
	    	
	    	
	    	
	    	
	    	// 收藏
	    	$(".strategies_list").on("click",".portfolio_fav_img_amend",function (){
	    		var _this = $(this);
				var relateid  = _this.attr("relateid");
				var flag = _this.attr("optype");
				var opType = "";
				if(flag=="0"){
					opType = "Follow";
				}else{
					opType = "Delete";
				}
	    		$.ajax({
	    			url:base_root+"/front/portfolio/arena/setPortfolioFollow.do",
	    			data:{"relateid":relateid,"opType":opType},
	    			type:"get",
	    			dataType:"json",
	    			success:function(data){
	    				if(data.result){
	    					if(flag=="0"){
	    						layer.msg(langMutilForJs['favour.add']);
	        				//	_this.attr("src",base_root+"/res/images/icon-herat-11.png");
	        					_this.attr("optype","1");
	    					}else{
	    						layer.msg(langMutilForJs['favour.remove']);
	        				//	_this.attr("src",base_root+"/res/images/icon-herat-1.png");
	        					_this.attr("optype","0");
	    					}
	    					_this.toggleClass("portfolio_fav_img_amend1active");
	    				}else{
	    					layer.msg(data.msg);
	    				}
	    			}
	    		});
	    		return false;
	    	});
		   
			$(".icon_search").on("click",function(){
				//var fundName = $("#fundName").val();
				//document.getElementById("keyWord").value=fundName;
				//searchData = $("#paramsForm").serialize();
			 	Initialization();
			})
			
			$("#fundName").keyup(function(event){
		      	 if(event.keyCode == 13){
		        	document.getElementById('searchKeyBtn').click()
		        }
		    });		
			
			
	    	
	    	function Initialization(){
	    		// 初始化数值
	    		iPAGE = 1; // 第N页数据
	    		page_bol=true;
	    		//searchList();
	    		//searchData = $("#paramsForm").serialize();
	    		getDataList();
	    	}
	    	

	     	

	    }]);// angular.js end

});