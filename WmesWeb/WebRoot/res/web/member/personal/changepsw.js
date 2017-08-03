
define(function(require) {

	var $ = require('jquery');
	require('layer');
    $("#saveButton").on("click",function(){
    	savePwd();
    });
    $("#cancelButton").on("click",function(){
    	location.href=_root_+"/front/member/personal/info.do";
    });
	function savePwd(){
	    //检查输入合法性
	    var id = $("#id").val(); 
	    var oldpsw = $('#oldpsw').val(); 
	    if (!oldpsw){layer.msg("Please input old password."); return;}

	    var password = $('#password').val();
	    if (!password){layer.msg("Please input new password."); return;}

	    var confirmpsw = $('#confirmpsw').val();
	    if (!confirmpsw){layer.msg("Please input confirm password."); return;}
	    if (password != confirmpsw) {layer.msg("It is not same as new password."); return;}

	    
	    //提交表单
	    $("#saveButton").attr({'disabled':true});
	    $.ajax({
	    	type:"POST",
	        url: _root_+"/front/member/personal/savepass.do?datestr="+new Date().getTime(),
	        data: $("#frm").serialize(),
	        success: function(response)
	        {   
	            $("#saveButton").attr({'disabled':false});
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
	            	//layer.msg("密码修改成功。");
	            	layer.msg("Password changed successfully.");
	                window.location.href=_root_+"/front/member/personal/info.do?id="+id+"&r="+Math.random();
	            }else{
	            	layer.msg(dataObj.msg);
	            }
	        },
	        error:function()
	        {
	            layer.msg("error found while saving data.");
	        }
	    });
	}
	
	// old password验证
	var oldPassBol = false;
	$("#oldpsw").focus(function(){
	 	$(this).siblings(".Wrong_tips").html("").hide();
	});
	$("#oldpsw").on("blur",function(){
		var _this = $(this);

		if($("#oldpsw").val() != ""){
			oldPassBol = true;
		}else{
			_this.siblings(".Wrong_tips").html("Old password cannot be empty").show();
			oldPassBol = false;
		}
	});
	
	// new password验证
	var newPassBol = false;
	$("#password").focus(function(){
	 	$(this).siblings(".Wrong_tips").html("").hide();
	});
	$("#password").on("blur",function(){
		var _this = $(this);

		if($("#password").val() != ""){
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
			if ($("#password").val() != $("#confirmpsw").val()) {
				_this.siblings(".Wrong_tips").html("It is not same as new password").show();
				cfPassBol = false;
			}else{
			 cfPassBol = true;
			}
		}else{
			_this.siblings(".Wrong_tips").html("Confirm password cannot be empty").show();
			cfPassBol = false;
		}
	});
	
	// 验证码
	$("#checkCode").focus(function(){
	 	$(this).siblings(".Wrong_tips").html("").hide();
	});
	var codeBol = true;
	$("#checkCode").on("blur",function(){
		//loindid正则
		var _this = $(this);

		if($("#checkCode").val() != "" && oldPassBol && newPassBol && cfPassBol && codeBol){
			codeBol = false;
			$.ajax({
				type:"GET",
				url:base_root+"/front/regist/checkValicode.do?r="+Math.random()+"&valiCode="+$("#checkCode").val(),
				dataType: "JSON",
				success:function(response){
					if(response.result){
						_this.siblings(".Wrong_tips").html("<img src='"+base_root+"/res/images/Hook.png' alt=''>").show();
						codeBol = true;
					}else{
						_this.siblings(".Wrong_tips").html("Verification code error").show();
						codeBol = false;
					}
				},
				error:function(response){}
			})
		}else{
			_this.siblings(".Wrong_tips").html("Verification code cannot be empty").show();
			codeBol = false;
		}
	})
});
