/**
 * newsRightPage.js
 * @author mqzou
 * @date: 2017-03-07
 */
define(function(require) {
	var $ = require('jquery');
	////console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	/*var angular = require('angular');
	
	var mybase = angular.module('rightTable', []);
*/	
	/*mybase.controller('rightTableCtrl', ['$scope', '$http', function($scope, $http) {
		 
		 
		 
		 
		 
	 }]);*/
	
	getHotList();
	function getHotList(){
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/news/info/getHotNewsList.do",
			data:{},
			async:false,
			success:function(json){
			    var html="";
				$.each(json,function(i,n){
					html+='<div class="popular-grid">'
						+'<span class="popular-grid-num">'+(i+1)+'</span>'
						+'<div style="float:left;width:calc(100% - 52px)"><a class="title" href="newsContent.do?id='+n.id+'">'+n.title+'</a>'
						+'<p>'+n.lastUpdate+'</p></div>'
						+'</div>';
				});
				$('.hotList').append(html);
				////console.log(json);
			}
		})
	}
	getRecentList();
	function getRecentList(){
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/news/info/getRecentNewsList.do",
			data:{},
			async:false,
			success:function(json){
			    var html="";
				$.each(json,function(i,n){
					html+='<div class="popular-grid">';
						if(n.url!=null && n.url!=''){
							html+='<a class="popular-grid-img" href="newsContent.do?id='+n.id+'"><img src="'+n.url+'" alt="" /></a>'
						}
							
						html+='<div class="popular-grid-content">'
						+'<a class="title" href="newsContent.do?id='+n.id+'">'+n.title+'</a>'
						+'<p>'+n.lastUpdate+'</p>'
						+'</div>'
						+'</div>';
				});
				$('.recentList').append(html);
			}
		})
	}
	getRecommendList();
	function getRecommendList(){
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/news/info/getRecommendNewsList.do",
			data:{rows:5,page:1},
			async:false,
			success:function(json){
			    var html="";
				$.each(json,function(i,n){
					html+='<div class="side-bar-article">';
					if(n.url!=null && n.url!=''){
						html+='<a style="min-height: 200px;" href="newsContent.do?id='+n.id+'"><img style="min-height: 200px;"   src="'+n.url+'" alt="" /></a>'
					}
						
					html+='<div class="side-bar-article-title">'
						+'<a href="newsContent.do?id='+n.id+'">'+n.title+'</a>'
						+'</div>'
						+'</div>';
				});
				$('.recommendList').append(html);
			}
		})
	}
})