/**
 * registration.js WMES 注册第二步 填写用户信息
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-05-20
 */

define(function(require) {
	var $ = require('jquery');
 			require('formValidLang');
 			require("jqueryForm");
 			require("formValid");
 			require("layui");

	$(".login_checkbox").on("click",function(event){
		if( $(this).hasClass("login_box_active") ){
			$(this).removeClass("login_box_active");
		}else{
			$(this).addClass("login_box_active");
		}
		 event.stopPropagation(); 
		$("#Agreement").click();

			
		if($('#Agreement').prop("checked") == false || $('#loginCode').val() == "" || $('#password').val() == "" || $('#passwordok').val() == "" || $('#email').val() == "" || $('#regPhoneKey').val() == "" || $('#CheckCode').val() == ""){
			$('#btn_next').css('background','#999999');
			$('#btn_next').css('pointer-events','none');
			$('#btn_next').css('cursor','default');
		}else{
			$('#btn_next').css('background','#2d80ce');
			$('#btn_next').css('pointer-events','visiblepainted ');
			$('#btn_next').css('cursor','pointer');
		}
	});
		
	function BasicUser(){//个人
		/**next*/
		$("#btn_next").on("click",function(){
			if($('#register_msg').validationEngine('validate')){
				//验证账号唯一
				var DataText = $("#loginCode").val();
				$.ajax({
					type:"POST",
					url:base_root+"/front/regist/checkUnique.do?r="+Math.random(),
					data:{keyValue : DataText,keyName:"loginCode"},
					success:function(response){
						if(response.result){
							//$('#codeError').validationEngine('showPrompt','账号可用','pass'); 
							//邮箱唯一验证
							var emailVal =$("#email").val();
							$.ajax({
								type:"POST",
								url:base_root+"/front/regist/checkUnique.do?r="+Math.random(),
								data:{keyValue : emailVal,keyName:"email"},
								success:function(response){
									if(response.result){
										//$('#emailError').validationEngine('showPrompt','邮箱可用','pass');
										//手机号验证
										var mobileValue =$("#regPhoneKey").val();
										var mobileCode=$("#regPhoneCode").val();
										$.ajax({
											type:"POST",
											url:base_root+"/front/regist/checkCodeMobileUnique.do?r="+Math.random(),
											data:{'mobileCode': mobileCode,'mobileNumber': mobileValue},
											success:function(response){
												if(response.result){
													//$('#mobileError').validationEngine('showPrompt','手机号可用','pass');
													//验证码验证
													$.ajax({
														type:"GET",
														url:base_root+"/front/regist/checkValicode.do?r="+Math.random()+"&valiCode="+$("#CheckCode").val(),
														dataType: "JSON",
														success:function(response){
															if(response.result){
																//表单提交
																var params = $("#register_msg").serialize();
																params = decodeURIComponent(params,true);
																$('#register_msg').ajaxSubmit({
																	url:base_root+"/front/regist/informationSave.do?r="+new Date().getTime(),
															  	    data:{Userparams : params},
															  	    dataType: "json",
															  		success:function(data, status)
																	{   
															           if(data.result){
															        	   window.location.href = base_root+"/front/regist/baseInfo.do";
															           }
																	},
															   	    error:function(){
															   	    	
																  	}
														      	});  
															}else{
																$('#validCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.validcodeerror"]+'','error');
																//重新生成验证码
																timeFunction();
															}
														},
														error:function(response){
															$('#validCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.validcodeerror"]+'','error');
															//重新生成验证码
															timeFunction();
														}
													})
												}else{
													$('#mobileError').validationEngine('showPrompt',''+langMutilRegist["member.register.mobileerrorisexist"]+'','error');//手机号已存在
												}
											},
											error:function(response){
												$('#mobileError').validationEngine('showPrompt',''+langMutilRegist["member.register.mobileerror"]+'','error');//手机号验证失败
											}
										});
									}else{
										$('#emailError').validationEngine('showPrompt',''+langMutilRegist["member.register.emailerrorisexist"]+'','error'); //邮箱已存在
									}
								},
								error:function(response){
									$('#emailError').validationEngine('showPrompt',''+langMutilRegist["member.register.emailerror"]+'','error');//邮箱唯一验证失败
								}
							});
						}else{
							$('#codeError').validationEngine('showPrompt',''+langMutilRegist["member.register.codeerrorisexist"]+'','error');//账号已存在
						}
					},
					error:function(response){
						$('#codeError').validationEngine('showPrompt',''+langMutilRegist["member.register.ecodeerror"]+'','error');//账号唯一验证失败
					}
				});	
				return;
			}		
		});
	}

	function BasicFirm(){//公司
		$("#btn_next").on("click",function(){
			if($('#distributor_msg').validationEngine('validate')){
				//验证码验证
				$.ajax({
					type:"GET",
					url:base_root+"/front/regist/checkValicode.do?r="+Math.random()+"&valiCode="+$("#FirmCheckCode").val(),
					dataType: "JSON",
					success:function(response){
						if(response.result){
							var params = $("#distributor_msg").serialize();
							params = decodeURIComponent(params,true);
							//表单提交
							$.ajax({
							 	type:'POST',
							 	data:{inputData : params /*,subdivided:usertype,memberType:cookies.get('subType')*/},
							 	url:base_root+"/front/regist/baseInfoSave.do",
							 	success:function(response){
							 		//到最后一步
							 		if(response.result){
										window.location.href= base_root+"/front/regist/complete.do?memberId="+response.memberId+"&r="+Math.random();
									}else{
										layer.alert("login has failed");
									}
							 	},
							 	error:function(response){
							 		// //console.log(response);
							 	}
							});
						}else{
							$('#vCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.validcodeerror"]+'','error');
						}
					},
					error:function(response){
						$('#vCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.validcodeerror"]+'','error');
					}
				})				
			}
		})
			

		}

		if(registerType == 'Individual' || registerType == 'IFA User'){
			$("#register_msg").show();
			$(".basicliucheng").show();
			BasicUser();
		}else if( registerType == 'IFA Firm' || registerType == 'Distributor' ){
			$("#distributor_msg").show();
			$(".Firmliucheng").show();
			BasicFirm();
		}
		
		/**验证*/
		$("#register_msg").validationEngine({
	        promptPosition: "inline",
	        scroll:false,
	        isOverflown:true,
	        overflownDIV:"container",
	        autoPositionUpdate:true,
	        focusFirstField:false,
	        showArrow:false
	    }); 
		
		$("#distributor_msg").validationEngine({
	        promptPosition: "inline",
	        scroll:false,
	        isOverflown:true,
	        overflownDIV:"container",
	        autoPositionUpdate:true,
	        focusFirstField:false,
	        showArrow:false
	    });
		
		
		$('.register_background_pic input').on('change',function(){
			if($('#Agreement').prop("checked") == false || $('#loginCode').val() == "" || $('#password').val() == "" || $('#passwordok').val() == "" || $('#email').val() == "" || $('#regPhoneKey').val() == "" || $('#CheckCode').val() == ""){
				$('#btn_next').css('background','#999999');
				$('#btn_next').css('pointer-events','none');
				$('#btn_next').css('cursor','default');
			}else{
				$('#btn_next').css('background','#2d80ce');
				$('#btn_next').css('pointer-events','visiblepainted ');
				$('#btn_next').css('cursor','pointer');
			}
		});
		if($('#Agreement').prop("checked") == false || $('#loginCode').val() == "" || $('#password').val() == "" || $('#passwordok').val() == "" || $('#email').val() == "" || $('#regPhoneKey').val() == "" || $('#CheckCode').val() == ""){
			$('#btn_next').css('background','#999999');
			$('#btn_next').css('pointer-events','none');
			$('#btn_next').css('cursor','default');
		}else{
			$('#btn_next').css('background','#2d80ce');
			$('#btn_next').css('pointer-events','visiblepainted ');
			$('#btn_next').css('cursor','pointer');
		}
		
		$('.showProtocol').on('click',function(){
			$(".registrationcon").css("display","block");
		});
		
		$('.btn_close').on('click',function(){
			$(".registrationcon").css("display","none");
		});
		//账号唯一性验证
		$('#loginCode').on('blur',function(){
			var DataText =$("#loginCode").val();
			if(""!=DataText&&null!=DataText){
				$.ajax({
					type:"POST",
					url:base_root+"/front/regist/checkUnique.do?r="+Math.random(),
					data:{keyValue : DataText,keyName:"loginCode"},
					success:function(response){
						if(response.result){
							$('#codeError').next('.formError').css('cssText','display:none !important;');
						}else{
							$('#codeError').validationEngine('showPrompt',''+langMutilRegist["member.register.codeerrorisexist"]+'','error');//账号已存在
						}
					},
					error:function(response){
						$('#codeError').validationEngine('showPrompt',''+langMutilRegist["member.register.ecodeerror"]+'','error');//账号唯一验证失败
					}
				});
			}
		});
		//email唯一性验证
		$('#email').on('blur',function(){
			var emailVal =$("#email").val();
			if(""!=emailVal&&null!=emailVal){
				$.ajax({
					type:"POST",
					url:base_root+"/front/regist/checkUnique.do?r="+Math.random(),
					data:{keyValue : emailVal,keyName:"email"},
					success:function(response){
						if(response.result){
							$('#emailError').next('.formError').css('cssText','display:none !important;');
						}else{
							$('#emailError').validationEngine('showPrompt',''+langMutilRegist["member.register.emailerrorisexist"]+'','error'); //邮箱已存在
						}
					},
					error:function(response){
						$('#emailError').validationEngine('showPrompt',''+langMutilRegist["member.register.emailerror"]+'','error');//邮箱唯一验证失败
					}
				});
			}
		});
		//区号非空验证
		$('#regPhoneCode').on('blur',function(){
			phoneCheck();
		});
		//手机号码非空验证
		$('#regPhoneKey').on('blur',function(){
			phoneCheck();
		});
		//手机（区号+号码）非空、唯一验证
		function phoneCheck(){
			//console.log("yes")
			var mobileCode = $('#regPhoneCode').val();
			var mobileNumber = $('#regPhoneKey').val();
			if((""==mobileCode || null==mobileCode )||( ""==mobileNumber || null==mobileNumber)){
				$('#mobileError').validationEngine('showPrompt',''+langMutilRegist["member.register.required"]+'','error');
			} else {
				var checkNum=/^[1-9]\d{6,10}$/;
				if(!checkNum.test(mobileNumber)){
					$('#mobileError').validationEngine('showPrompt',''+langMutilRegist["member.validation.mobile.error"]+'','error');//手机号格式不正确
				}else{
					$('#mobileError').next('.formError').css('cssText','display:none !important;');
					$.ajax({
						type:"POST",
						url:base_root+"/front/regist/checkCodeMobileUnique.do?r="+Math.random(),
						data:{'mobileCode': mobileCode,'mobileNumber': mobileNumber},
						success:function(response){
							if(response.result){
								$('#mobileError').next('.formError').css('cssText','display:none !important;');
							}else{
								$('#mobileError').validationEngine('showPrompt',''+langMutilRegist["member.register.mobileerrorisexist"]+'','error');//手机号已存在
							}
						},
						error:function(response){
							$('#mobileError').validationEngine('showPrompt',''+langMutilRegist["member.register.mobileerror"]+'','error');//手机号验证失败
						}
					});
				}
				
				
			}
		}
		//邀请码验证
		$('#inviteCode').on('change',function(){
			var inviteCode = $('#inviteCode').val();
			if("" != inviteCode && null != inviteCode){
				//正则输入判断（8位含字母、数字）
				var reg = /^[0-9a-zA-Z]{8}$/;
				if(!reg.test(inviteCode)){
					$('#inviteCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.invitecodeerror"]+'','error');
				}
				var email = $('#email').val();
				//邀请码不为空时必需填写email
				if("" == email || null == email){
					layer.msg(''+langMutilRegist["member.register.invitecodeemail"]+'');
				}
				$.ajax({
				 	type:'POST',
				 	dataType:'json',
				 	data:{'email': email,'inviteCode':inviteCode},
				 	url:base_root+"/front/regist/checkinviteCode.do",
				 	success:function(response){
				 		if(response.result){
				 		}else{
							//$.Tips({ content: "no invite code" });
							$('#inviteCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.invitecodeerror"]+'','error');
						}
				 	},
				 	error:function(response){
				 		$('#inviteCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.invitecodeerror"]+'','error');
				 	}
				 });
			}
		});
		
		//重新生成验证码
		function timeFunction() {
            document.getElementById("changeCode").src = document.getElementById("changeCode").src+'?';
        }
		
	    window.onload = function () {
	    	//5分钟自动刷新验证码
            setInterval(timeFunction, 5*60*1000);
	    }
	    
	    //输入验证码后立刻验证
	    $('#CheckCode').on('change',function(){
		    //验证码验证
			$.ajax({
				type:"GET",
				url:base_root+"/front/regist/checkValicode.do?r="+Math.random()+"&valiCode="+$("#CheckCode").val(),
				dataType: "JSON",
				success:function(response){
					if(response.result){
	
					}else{
						$('#validCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.validcodeerror"]+'','error');
					}
				},
				error:function(response){
					$('#validCodeError').validationEngine('showPrompt',''+langMutilRegist["member.register.validcodeerror"]+'','error');
				}
			});
	    });
	    
	    $('.btn_previous').on('click',function(){
	    	self.location=base_root+"/front/regist/regist.do";
	    });
	    
	    $('.changeyzm').on('click',function(){
	    	$(this).prev().click();
	    });
 });