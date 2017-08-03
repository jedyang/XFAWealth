
define(function(require) {
	var $ = require('jquery');
	require('layui');
	require('formValidLang');
	require("formValid"); 	
	
    $("#btn_next").on("click",function(){
    	sendEmail();
    });
    
	// email验证
	var emailBol = true;
	$("#email").focus(function(){
	 	$(this).siblings(".Wrong_tips").html("").hide();
	});
	$("#email").on("blur",function(){
		var _this = $(this);

		if($("#email").val() != ""){
			emailBol = true;
		}else{
//			_this.siblings(".Wrong_tips").html("Email cannot be empty").show();
			layer.msg(langMutilForJs["member.resetPassword.email.missing"]);
			emailBol = false;
		}
	});

/*
	// temp password验证
	var tmpPassBol = false;
	$("#tmpPass").focus(function(){
	 	$(this).siblings(".Wrong_tips").html("").hide();
	});
	$("#tmpPass").on("blur",function(){
		var _this = $(this);

		if($("#tmpPass").val() != ""){
			tmpPassBol = true;
		}else{
			_this.siblings(".Wrong_tips").html("Temporary password cannot be empty").show();
			tmpPassBol = false;
		}
	});

	// new password验证
	var newPassBol = false;
	$("#newPass").focus(function(){
	 	$(this).siblings(".Wrong_tips").html("").hide();
	});
	$("#newPass").on("blur",function(){
		var _this = $(this);

		if($("#newPass").val() != ""){
			newPassBol = true;
		}else{
			_this.siblings(".Wrong_tips").html("New password cannot be empty").show();
			newPassBol = false;
		}
	});
	
	// confirm password验证
	var cfPassBol = false;
	$("#confirmpsw").focus(function(){
	 	$(this).siblings(".Wrong_tips").html("").hide();
	});
	$("#confirmpsw").on("blur",function(){
		var _this = $(this);

		if($("#confirmpsw").val() != ""){
			if ($("#newPass").val() != $("#confirmpsw").val()) {
				_this.siblings(".Wrong_tips").html("It is not same as new password").show();
				cfPassBol = false;
			}else cfPassBol = true;
		}else{
			_this.siblings(".Wrong_tips").html("Confirm password cannot be empty").show();
			cfPassBol = false;
		}
	});
	
	// 验证码
	$("#checkCode").focus(function(){
	 	$(this).siblings(".Wrong_tips").html("").hide();
	});
*/
	
    var codeBol = false;
	$("#checkCode").on("blur",function(){
		//loindid正则
		var _this = $(this);

        codeBol = false;
		if($("#checkCode").val() != ""){
			$.ajax({
				type:"GET",
				url:base_root+"/front/regist/checkValicode.do?r="+Math.random()+"&valiCode="+$("#checkCode").val(),
				async : false, //同步模式
				dataType: "JSON",
				success:function(response){
					if(response.result){
						_this.siblings(".Wrong_tips").html("<img src='"+base_root+"/res/images/Hook.png' alt=''>").show();
						codeBol = true;
					}else{
//						_this.siblings(".Wrong_tips").html("Verification code error").show();
						layer.msg(langMutilForJs["member.resetPassword.verCode.wrong"]);
					}
				},
				error:function(response){
				    layer.msg(langMutilForJs["member.resetPassword.verCode.error"]);
				}
			})
		}else{
//			_this.siblings(".Wrong_tips").html("Verification code cannot be empty").show();
			layer.msg(langMutilForJs["member.resetPassword.verCode.missing"]);
		}
	})
	
    //发送邮件：临时密码？修改密码连接
	function sendEmail(){
	    //检查输入合法性
	    if(!$('#resetpswForm').validationEngine('validate')) return;
	    
	    var email = $("#email").val(); 
//	    if (!email || !emailBol){layer.msg(langMutilForJs["member.resetPassword.email.missing"]); return;}
	    
	    var tmpPass = $("#tmpPass").val(); 
//	    if (!tmpPass || !tmpPassBol){layer.msg("Please input temporaty passowrd."); return;}
	    
	    var newPass = $("#newPass").val(); 
//	    if (!newPass || !newPassBol){layer.msg("Please input new password."); return;}
	    
	    var confirmpsw = $("#confirmpsw").val(); 
	    /*
	    if (!confirmpsw || !cfPassBol){
	    	if (newPass != confirmpsw) layer.msg("It is not same as new password.");
	    	else layer.msg("Please input confirm password."); 
	    	return;
	    }
	    if (newPass != confirmpsw) {layer.msg("It is not same as new password."); return;}
	    */
	    
        var checkCode = $("#checkCode").val(); 
        if (!checkCode){layer.msg(langMutilForJs["member.resetPassword.verCode.missing"]); return;}
        if (!codeBol){layer.msg(langMutilForJs["member.resetPassword.verCode.wrong"]); return;}
	    
	    //提交表单
	    $.ajax({
	    	type: "POST",  
	        url: _root_+"/front/member/personal/resetpsw.do",
	        async : false, //同步模式
//	        iframe: true,
	        dataType: "json",
	        data:{"email": email,"tmpPass": tmpPass,"newPass": newPass},
	        cache:false, 
	        success: function(response)
	        {
	        	var emailParm = encodeURI("email="+email);
	            if(response.result){
	            	layer.msg(response.msg);
//	            	layer.msg("Password saved.",{time:3000});
	                setTimeout(function(){
	                    window.location.href=_root_+"/front/member/personal/resetpswok.do?"+emailParm+"&r="+Math.random();
	                },2000);
	            }else{
	                layer.msg(response.msg);
//                    setTimeout(function(){
//                        window.location.href=_root_+"/front/member/personal/resetpswfail.do?"+emailParm+"&errMsg="+response.msg+"&r="+Math.random();
//                    },2000);
	            }
	        },
	        error:function (XMLHttpRequest, textStatus, errorThrown) {    
//                alert(textStatus);
                var msg= "textStatus";
                msg+="\n"+"XMLHttpRequest.status:"+XMLHttpRequest.status;
                msg+="\n"+"XMLHttpRequest.readyState:"+XMLHttpRequest.readyState;
                msg+="\n"+"XMLHttpRequest.responseText:"+XMLHttpRequest.responseText;
//                alert(msg);
//	            layer.alert(response);
//	            layer.alert(langMutilForJs["member.resetPassword.fail"]);
	        }
	    });
	}
});
