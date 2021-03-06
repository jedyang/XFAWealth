/**
 * login.js WMES登陆界面
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-05-18
 */

 define(function(require) {
	var $ = require('jquery');
			require('layer');
			
	$(".enter_click").bind('keydown',function(event){
      if(event.keyCode==13){
         $("#btn_login").click();
      } 
    });
	
	$('#RememberMe').change(function(){
		if($('#RememberMe').prop('checked')){
			$('#RememberMe').val('1');
		}else{
			$('#RememberMe').val('0');
		}
	});
	
	$("#btn_login").on("click",function(){
		var userName = $.trim($("#loginid").val());
	   	var tempPassword = $.trim($("#password").val());
	   	var userName64 = encode64(utf16to8(userName));
	   	var password64 = encode64(utf16to8(tempPassword));
	    $("#name").val(userName64);
	    $("#pwd").val(password64);
      	$('#loginForm').submit();				
	});
	
	function GetQueryString(name)
    {
         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
         var r = decodeURI(window.location.search).substr(1).match(reg);
         //原始地址没有参数，返回all
         if(r!=null)return  unescape(r[2]); return "";
         
    }
	
	if(!!GetQueryString("errorMsg")){
		//$.Tips({ content: GetQueryString("errorMsg") });
		layer.msg(GetQueryString("errorMsg"));
	}

});