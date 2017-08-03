/**
 * login.js WMES登陆界面
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-05-18
 */

 define(function(require) {
	var $ = require('jquery');
			require('layer');
			require('jqueryForm');

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
		if(!$("#loginid").val().trim()){
			layer.msg(langMutilForLogin["member.login.loginCodeEmpty"]);
			return;
		}
		if(!$("#password").val()){
			layer.msg(langMutilForLogin["member.login.passwordEmpty"]);
			return;
		}
		if($("#CheckCode").length > 0 && !$("#CheckCode").val().trim()){
			layer.msg(langMutilForLogin["member.login.validateCodeEmpty"]);
			return;
		}
		var userName = $.trim($("#loginid").val());
	   	var tempPassword = $.trim($("#password").val());
	   	var userName64 = encode64(utf16to8(userName));
	   	var password64 = encode64(utf16to8(tempPassword));
	    $("#name").val(userName64);
	    $("#pwd").val(password64);
//      	$('#loginForm').submit();
/**	    异步登录20170324scshi*/
	    $('#loginForm').ajaxSubmit({
	    	url:base_root+"/front/login.do?r="+Math.random(),
	    	dataType:"json",
	    	success:function(data){
	    		if(data.result){
	    			var url = data.directTo;
	    			window.location.href=url;
	    		}else{
	    			var errorMsg = data.errorMsg;
	    			var lessCount = data.lessCount;
	    			$("#password").val(null);
	    			$("#CheckCode").val(null);
	    			$(".yanzhengma").click();//刷新验证码
	    			if(lessCount<=2){//添加验证码输入框
	    				$(".yzmcon").show();
	    				if($("#CheckCode").length==0)
	    					$(".yanzhengma").before('<input class="login_content_input_102 enter_click" type="text" value="" id="CheckCode" name="vercode" >')
	    			}
	    			layer.msg(errorMsg);
	    		}
	    	},
	    	error:function(){
	    		
	    	}
	    })
	    
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
		var msg = GetQueryString("errorMsg").replace(/\+/g," ");
		layer.msg(msg);
	}
	
	$(".langChange").on("click",function(){
        $.get(base_root+"/front/site/changLang.do?langFlag="+$(this).attr("langCode"), function(data, status, Request){
            if(data != null){
            	var href = window.location.href;
            	//去掉错误提示等的参数
            	var noParam = href.substring(0, href.indexOf('do')+2);
                window.location.href=noParam;
            }
        });
    });
	
	$(".disclosure-operation").on("click",function(){
		var instruction = '0';
		var rememberMe = $('#RememberMe').val();
		if($(this).hasClass('disclosure-operation-agree')){
			instruction = '1';
		}else if($(this).hasClass('disclosure-operation-reject')){
			instruction = '0';
		}
		$.ajax({
			type:'post',
			url:base_root + '/front/comfirmDisclaimer.do?d=' + new Date().getTime(),
			data:{
				instruction:instruction,
				rememberMe:rememberMe
			},
			success:function(re){
				////console.log(re);
				window.location.href = re.directTo;
			},
			error:function(){
				window.location.href = base_root + '/front/viewLogin.do';
			}
		});
	});

});