define(function(require){
	var $ = require('jquery');
	
	//引用
	var selector =  require('ifaSelectUser');
	    
	//初始化选择用户页面
	selector.init();
	
	//点击事件
	$("#btnTest").click(function(){
		var userType = $("#userType").val();//选用户类型		
		selector.create(0,userType,'memberIdsResult','memberNamesResult');   //0 多选
		$(".selectnamelistbox").css("display","block");
	});
	
	$("#btnTest2").click(function(){
		var userType = $("#userType").val();//选用户类型
		selector.create(1,userType,'memberIdsResult2','memberNamesResult2');	//1 单选

		$(".selectnamelistbox").css("display","block");
	});

});