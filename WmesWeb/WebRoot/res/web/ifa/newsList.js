/**
 * newsList.js
 * @author mqzou
 * @date: 2017-03-07
 */
define(function(require) {
	var $ = require('jquery');
/*	angular = require('angular');
	
	//console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

	var mybase = angular.module('newsTable', []);
	
	 mybase.controller('newsTableCtrl', ['$scope', '$http', function($scope, $http) {
		 
	 }]);*/
	 var id=getQueryString("id");
	 var code=getQueryString("code");
	 
	 var pageIndex=0;
	 var pageSize=10;
	 var total=0;
	 getDataList();
	 function getDataList(){
		 pageIndex=pageIndex+1;
		 $.ajax({
			 type:"post",
			 datatype:"json",
			 url:base_root+"/front/news/info/newsListJson.do",
			 data:{id:id,page:pageIndex,rows:pageSize,code:code},
			 async:false,
			 success:function(json){
				 total=json.total/pageSize;
				 if(total>pageIndex){
					 $("#ifa_more").show();
				 }else{
					 $("#ifa_more").hide();
				 }
				 var html="";
				 $.each(json.list,function(i,n){
					 html+='<div class="article">'
							+'<div class="article-left">'
							
							if(n.url!=undefined && n.url!=""){
								html+='<a href="newsContent.do?id='+n.id+'"><img src="'+n.url+'"></a>';
							}
							
					 html+='</div>'
							+'<div class="article-right">'
							+'<div class="article-title">'
							+'<p>'+n.lastUpdate+'</p>'
							+'<a class="title" href="newsContent.do?id='+n.id+'">'+n.title+'</a>'
							+'</div>'
							+'<div class="article-text">'
							+'<p>'+n.description+'</p>'
							//+'<a href="newsContent.do?id='+n.id+'"><img src="'+base_root+'/res/css/news/images/more.png" alt="" /></a>'
							+'<div class="clearfix"></div>'
							+'</div>'
							+'</div>'
							+'<div class="clearfix"></div>'
							+'</div>';
				 })
				 $(".newsList").append(html);
				// //console.log(json);
			 }
		 })
	 }
	 
	 $("#ifa_more").on("click",function(){
		 getDataList();
	 })
 /*// 数据控制
	var mybase = angular.module('newsTable', []);
	 mybase.controller('newsTableCtrl', ['$scope', '$http', function($scope, $http) {
	 		var url = base_root + "/front/news/info/newsListJson.do", // url
	 			iPAGE = 1, // 第N页数据
	    		is_finish = true,// 当前数据是否加载完成
	    		Searchdata = "",// 搜索条件初始化
															// price（增长率降序）
	    		page_bol = true;// 总页数控制
	    		
	    		// 初始化数据
	    		$scope.dataList = '';

	    	// 数字调用
	    	$scope.counter = Array;
	    	
	    	// 监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){

					// 删除渲染效果
					$(".animated").one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				    		$(".animated").removeClass();
				    });

					// you can also do something

				}
			}


	    	var rows = 10  // 固定每次拿10条数据
	    	// 正常拿数据
	    	function getDataList(){
	    			var data =   "rows="+ rows +"&page=" + iPAGE + "&" + Searchdata ;

	    			// 控制数据是否加载完成
	    			is_finish = false;
	    			$http({
                   url: url,
                   method: 'POST',
                   data : data,
                   headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
              	 }).success(function(response){
              		is_finish = true;
                    if(response.list.length > 0){
                    		$(".no_list_tips").hide();
                    		 if( iPAGE === 1 ){
                    		 	 $scope.dataList = '';
                               $scope.dataList =  response.list;
                            }else{
                               $scope.dataList = $scope.dataList.concat(response.list);
                            }
                    		// 总页数
                    		var sumPage = Math.ceil(response.total / rows);
                    		if(iPAGE >= sumPage){
                    			page_bol = false;
                    		}
                		}else{
                			page_bol = false;
               			 if( iPAGE === 1 ){
                			 	$(".no_list_tips").show();
                			 }
                		}
	    			iPAGE++;
	    			$scope.datatotal = response.total;
	    			})
	    			}
	    	
	    	getDataList();
	 }])*/
	 
	 $(".sb-icon-search").on("click",function(){
		 var key=$(".sb-search-input").val();
		 window.location.href=base_root+"/front/news/info/searchNews.do?key="+key;
	 })
	 

	$("#searchKeyWord").keydown(function(e) {
		if (e.keyCode == 13) {
			search()
		}
	});
	$("#searchKeyBtn").on("click", function() {
		search()
	})
	function search() {

		var key = $("#searchKeyWord").val();
		window.location.href = base_root + "/front/news/info/searchNews.do?key=" + key;
	};
	
	var mainBodayHeight = parseInt($('.header').height() + 30 + $('.main-body').height() - $(window).height());
	$(window).on('scroll',function(){
		var rightHeight = parseInt($('.header').height() + 30 + $('.new-content-right').height() - $(window).height());
		if($(window).scrollTop() >= rightHeight){
			if($(window).scrollTop() >= mainBodayHeight){
				
			}else{
				var tempHeight = $(window).scrollTop() - rightHeight;
				$('.new-content-right').css('margin-top',tempHeight);
			}
		}else{
			$('.new-content-right').css('margin-top','0');
		}
	});
	
})