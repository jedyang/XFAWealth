/**
 * openAccountbasic.js basicinfo
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-06
 */

define(function(require) {
	var $ = require('jquery'),
	angular = require('angular');
	require("jqueryForm");
	require('formValidLang');
	require("formValid");
	
 	// tab切换
 	$(".OpenAccount_tab li").on("click",function(){
 		//表单切换时提交保存当前tab
 		$(".OpenAccount_tab li a").each(function(){
 			var liClass = $(this).attr("class")
 			if("now"==liClass){//切换tab时，保存数据
 				var formName = $(this).attr("formName");
 				var ajaxActionName = $(this).attr("actionName");
 				$("#"+formName).ajaxSubmit({
 					url: base_root+"/front/investor/"+ajaxActionName+".do?datestr="+new Date().getTime()+"&accountId="+$("#accountId").val(),
 					success: function(data, status)
 					{   
 			            if(data.result){
 			            	if(!!data.iContactId)$("#iContactId").val(data.iContactId);
 			            	if(!!data.addr_rid)$("#iAddr_r").val(data.addr_rid);
 			            	if(!!data.addr_pid)$("#iAddr_p").val(data.addr_pid);
 			            	if(!!data.addr_cid)$("#iAddr_c").val(data.addr_cid);
 			            	if(!!data.jointContactId)$("#jointContactId").val(data.jointContactId);
 			            	if(!!data.jAddr_rid)$("#jointAddr_r").val(data.jAddr_rid);
 			            	if(!!data.jAddr_pid)$("#jointAddr_p").val(data.jAddr_pid);
 			            	if(!!data.jAddr_cid)$("#jointAddr_c").val(data.jAddr_cid);
 			            	if(!!data.bankId)$("#ibank_id").val(data.bankId);
 			            }else{
 			            	$('#contact-form-error').show().fadeOut(10000);
 			            }
 					},
 					error:function()
 					{
 						$('#contact-form-error').show().fadeOut(10000);
 					}
 				});
 			}
 		})
 		$(".OpenAccount_tab li a").eq($(this).index()).removeClass("openAccount_tab_error");
 		$(".OpenAccount_tab li a").removeClass("now").eq($(this).index()).addClass("now");
 		$(".OpenAccount_BasicInfo_div > div").hide().eq($(this).index()).show();
 	});
 	
 	//地址信息复制
 	$("#reAddrAsMain").on("click",function(){
 		//alert()
 		if($(this).prop('checked')){
 			$("#j_resAddr_room").val( $("#resAddr_room").val() );
 			$("#j_resAddr_floor").val( $("#resAddr_floor").val() );
 			$("#j_resAddr_building").val( $("#resAddr_building").val() );
 			$("#j_resAddr_court").val( $("#resAddr_court").val() );
 			$("#j_resAddr_road").val( $("#resAddr_road").val() );
 			$("#j_resAddr_district").val( $("#resAddr_district").val() );
 			$("#j_resAddr_province").val( $("#resAddr_province").val() );
 			$("#j_resAddr_country").val( $("#resAddr_country").val() );
 			$("#j_resAddr_country_name").val( $("#resAddr_country_name").val() );
 			$("#j_resAddr_postCode").val( $("#resAddr_postCode").val() );
 		}else{
 			$("input[id^='j_resAddr_']").each(function(){
 				$(this).val("");
 			})
 		}
 	});
 	
 	$("#perAddrAsMain").on("click",function(){
 		//alert()
 		if($(this).prop('checked')){
 			$("#j_perAddr_room").val( $("#perAddr_room").val() );
 			$("#j_perAddr_floor").val( $("#perAddr_floor").val() );
 			$("#j_perAddr_building").val( $("#perAddr_building").val() );
 			$("#j_perAddr_court").val( $("#perAddr_court").val() );
 			$("#j_perAddr_road").val( $("#perAddr_road").val() );
 			$("#j_perAddr_district").val( $("#perAddr_district").val() );
 			$("#j_perAddr_province").val( $("#perAddr_province").val() );
 			$("#j_perAddr_country").val( $("#perAddr_country").val() );
 			$("#j_perAddr_country_name").val( $("#perAddr_country_name").val() );
 			$("#j_perAddr_postCode").val( $("#perAddr_postCode").val() );
 		}else{
 			$("input[id^='j_perAddr_']").each(function(){
 				$(this).val("");
 			})
 		}
 	});
 	
 	$("#corAddrAsMain").on("click",function(){
 		//alert()
 		if($(this).prop('checked')){
 			$("#j_corAddr_room").val( $("#corAddr_room").val() );
 			$("#j_corAddr_floor").val( $("#corAddr_floor").val() );
 			$("#j_corAddr_building").val( $("#corAddr_building").val() );
 			$("#j_corAddr_court").val( $("#corAddr_court").val() );
 			$("#j_corAddr_road").val( $("#corAddr_road").val() );
 			$("#j_corAddr_district").val( $("#corAddr_district").val() );
 			$("#j_corAddr_province").val( $("#corAddr_province").val() );
 			$("#j_corAddr_country").val( $("#corAddr_country").val() );
 			$("#j_corAddr_country_name").val( $("#corAddr_country_name").val() );
 			$("#j_corAddr_postCode").val( $("#corAddr_postCode").val() );
 		}else{
 			$("input[id^='j_corAddr_']").each(function(){
 				$(this).val("");
 			})
 		}
 	});

 	//investor点击下一步，保存数据，进入文档检查
    $("#btn_draft").on("click",function(){
    	saveAccountBasic(false);
    });

 	//investor点击下一步，保存数据，进入文档检查
    $(".next_btn").on("click",function(){
    	saveAccountBasic(true);
    });

    //investor点击下一步，保存数据，进入文档检查
    function saveAccountBasic(next){
    	var accFlag = true;
    	var mainFlag = true;
    	var jointFlag =  true;
    	var bankFlag = true;
    	
    	var acctype = $("input[name='iAccount.accType']:checked").val();
//    	//console.log("acctype=="+acctype)
    	if("false"==ifIfaCheck  &&　!$("#acc_detail").validationEngine('validate')){
    		$(".OpenAccount_tab li a").eq(0).attr("class","openAccount_tab_error");
    		accFlag = false;
    	}
    	
    	if("false"==ifIfaCheck && !$("#main_contact_detail").validationEngine('validate')){
    		$(".OpenAccount_tab li a").eq(1).attr("class","openAccount_tab_error");
    		mainFlag = false;
    	}
    	
    	if("false"==ifIfaCheck && acctype=="J" && !$("#joint_contact_detail").validationEngine('validate')){
    		$(".OpenAccount_tab li a").eq(2).attr("class","openAccount_tab_error");
    		jointFlag = false;
    	}
    	
    	
    	if("false"==ifIfaCheck && !$("#bank_account_detial").validationEngine('validate')){
    		$(".OpenAccount_tab li a").eq(3).attr("class","openAccount_tab_error");
    		bankFlag = false
    	}
    	
    	
    	//if(accFlag && mainFlag && jointFlag && bankFlag){
    	if(mainFlag && jointFlag && bankFlag){
    		var succ = true;
    		var accountId = $("#accountId").val();
    		//账号信息保存
//    		$("#acc_detail").form("submit", function(){
    		//$("#acc_detail").ajaxSubmit(
    			var params = $("#acc_detail").serialize();
				params = decodeURIComponent(params,true);
    			$.ajax(
				{
					async: true,
					url: base_root+"/front/investor/accountDetailSave.do?datestr="+new Date().getTime(),
					method: 'post',             
			        data: params,
					success: function(data, status)
					{   
			            if(data.result){
			            	
			            }else{
			            	$('#contact-form-error').show().fadeOut(10000);
			            	succ = false;
			            }
					},
					error:function()
					{
						$('#contact-form-error').show().fadeOut(10000);
						succ = false;
					}
				});
//    		});
    		
    		//主要联系人保存
//    		$("#main_contact_detail").form("submit", function(){
        		//$("#main_contact_detail").ajaxSubmit(
    			var params = $("#main_contact_detail").serialize();
				params = decodeURIComponent(params,true);
    			$.ajax(
				{
					async: true,
					url: base_root+"/front/investor/mainContactSave.do?datestr="+new Date().getTime()+"&accountId="+accountId,
					method: 'post',             
			        data: params,
					success: function(data, status)
					{   
			            if(data.result){
			            	
			            }else{
			            	$('#contact-form-error').show().fadeOut(10000);
			            	succ = false;
			            }
					},
					error:function()
					{
						$('#contact-form-error').show().fadeOut(10000);
						succ = false;
					}
				});
//    		});
    		
    		//关联联系人保存
    		if(acctype=="J"){
//    			$("#joint_contact_detail").form("submit", function(){
            		//$("#joint_contact_detail").ajaxSubmit(
        			var params = $("#joint_contact_detail").serialize();
    				params = decodeURIComponent(params,true);
        			$.ajax(
					{
						async: true,
						url: base_root+"/front/investor/jointContactSave.do?datestr="+new Date().getTime()+"&accountId="+accountId,
						method: 'post',             
				        data: params,
						success: function(data, status)
						{   
				            if(data.result){
				            	
				            }else{
				            	$('#contact-form-error').show().fadeOut(10000);
				            	succ = false;
				            }
						},
						error:function()
						{
							$('#contact-form-error').show().fadeOut(10000);
							succ = false;
						}
					});
//    			});
    		}
    		
    		//银行信息保存
//			$("#bank_account_detial").form("submit", function(){
        		//$("#bank_account_detial").ajaxSubmit(
    			var params = $("#bank_account_detial").serialize();
				params = decodeURIComponent(params,true);
    			$.ajax(
				{
					async: true,
					url: base_root+"/front/investor/accountBankSave.do?datestr="+new Date().getTime()+"&accountId="+accountId,
					method: 'post',             
			        data: params,
					success: function(data, status)
					{   
				       // var dataObj=eval("("+data+")");
			            if(data.result){
			            	//if (next) window.location.href = base_root+"/front/investor/accountDoc.do?accountId="+accountId+"&r="+Math.random();
			        		if (next && succ) window.location.href = base_root+"/front/investor/accountDoc.do?accountId="+accountId+"&r="+Math.random();
			            }else{
			            	$('#contact-form-error').show().fadeOut(10000);
			            	succ = false;
			            }
					},
					error:function()
					{
						$('#contact-form-error').show().fadeOut(10000);
						succ = false;
					}
				});
//			});
    		//alert(succ);
    	}else{//验证不通过，滚动到头部
    		$('body,html').animate({scrollTop:0},1000);
    	}
    }
    
    $(".BasicInfo_title_ico").on("click",function(){
        $(this).parents(".BasicInfo_title").find(".BasicInfo_title_ico").hide().end().find(".BasicInfo_title_ico_hide").show();
        $(this).parents(".BasicInfo_show").stop().animate({ 
            height: "77px", 
          }, 300 );
    });
    
    $(".BasicInfo_title_ico_hide").on("click",function(){
        $(this).parents(".BasicInfo_title").find(".BasicInfo_title_ico").show().end().find(".BasicInfo_title_ico_hide").hide();
        $(this).parents(".BasicInfo_show").stop().animate({ 
            height: "100%", 
          }, 300 );
    });
    
    $(".accTypeRadio").on("click",function(){
    	if("J"==$(this).val()) $("#jAppTab").show();
    	else $("#jAppTab").hide();
    })
    
    $(".purposeRadio").on("click",function(){
    	if("Others"==$(this).val()) {
    		$("#otherTextInput").attr("readonly",false);
    	}
    	else {
    		$("#otherTextInput").val("");
    		$("#otherTextInput").attr("readonly",true);
    	 	$('#purposeError').html("");//清空提示
    	}
    })
    
    $(".aimFlagRadio").on("click", function(){
    	if("0"==$(this).val()){
    		$("#aimNameText").val("");
    		$("#aimNameText").attr("readonly",true);
    	}else{
    		$("#aimNameText").attr("readonly",false);
    	}
    })
    
    $(".closeFlagRadio").on("click", function(){
    	if("1"==$(this).val()){
    		$("#closeFlagDesc").attr("readonly",false);
    	}else{
    		$("#closeFlagDesc").val("");
    		$("#closeFlagDesc").attr("readonly",true);
    	}
    })
    
    $(".BasicInfo_xiala input").on("focus",function(){
		$(this).siblings(".regiter_xiala_ul").show();
	});
    
	$(".BasicInfo_xiala").on("blur","input",function(){
		var _this = $(this);
			// mandatoryVal = _this.siblings(".regiter_xiala_ul").children().eq(0).attr("value");
			//if(!$(this).val())$(this).val(_this.val(mandatoryVal));					
		setTimeout(function(){
			_this.siblings(".regiter_xiala_ul").hide();
		},200);
	});
	
	$(".regiter_xiala_ul").on("click","li",function(){
		$(this).parent().siblings('.value_show').validationEngine('hide');//解决初次选择下拉值，验证提示不隐藏问题
		$(this).parent().siblings('.value_hidden').val( $(this).attr("value") );
		$(this).parent().siblings('.value_show').val( $(this).text() );
	});
	
  	var mybase = angular.module('mybase', []);
        mybase.controller('myCtrl', function($scope) {  
            // 初始化数据     
            $scope.dataList = '';
             $.ajax({
                  type:"POST",
                  url:WmesUrl + "/backend/index.php?r=user/get-user",
                  datatype:"JSON",
                  data:{inputData : '{"loginID":"chungabcbbb"}'},
                  success:function(response){
                      //填充数据
                      $scope.dataList = response.data;
                      //更新视图 
                      $scope.$apply();
                  },
                  error:function(response){

                  }
              });
              
      });
        
/**********************************地区，学历等基本信息加载**************************************************/
     // 请求接口下拉数据（国家信息
 		$.ajax({
	 			type:"POST",
	 			url:base_root+"/front/regist/countryListSearch.do?r="+Math.random(),
	 			datatype:"JSON",
	 			data:{},
	 			success:function(response){
	 				var data = eval("("+response.countryJson+")");
					for(var data_item in data){
						$(".country_xiala").append('<li code="' + data[data_item].itemCode
		                        + '" value="' + data[data_item].id
		                        + '">' + data[data_item].name
		                        + '</li>');
	 				}
	 			},
	 			error:function(response){

	 			}
 		});
		
 		//请求接口下拉数据（学历情况
 		$.ajax({
 			type:"POST",
 			url: base_root + "/front/investor/loadParamConfigJson.do?r="+Math.random(),
 			datatype:"JSON",
 			data:{category : "education"},
 			success:function(response){
 				var paramCofigList = response.paramConfigJson;
 				//数据渲染
 					for(var data_item in paramCofigList){
	 					$(".edu_xiala").append('<li value="'+ paramCofigList[data_item].itemCode
	 						+'" code="'+paramCofigList[data_item].itemCode+'">' + paramCofigList[data_item].name.replace(/(^\s*)|(\s*$)/g, "") + '</li>');
	 				}
 			},
 			error:function(response){

 			}
		});  
 		
 		//请求接口下拉数据（职业分类
 		$.ajax({
 			type:"POST",
 			url: base_root + "/front/investor/loadParamConfigJson.do?r="+Math.random(),
 			datatype:"JSON",
 			data:{category : "occupation"},
 			success:function(response){
 				var paramCofigList = response.paramConfigJson;
 				//数据渲染
 					for(var data_item in paramCofigList){
	 					$(".occupation_xiala").append('<li value="'+ paramCofigList[data_item].itemCode
	 						+'" code="'+paramCofigList[data_item].itemCode+'">' + paramCofigList[data_item].name + '</li>');
	 				}
 			},
 			error:function(response){

 			}
		});
/***************************************************************************************************/        
 		$("#acc_detail").validationEngine({
 	        promptPosition: "inline",
 	        scroll:false,
 	        isOverflown:false,
 	        autoPositionUpdate:true,
 	        validateNonVisibleFields:true,
 	        focusFirstField:false,
 	        showArrow:false
 	    });
 		
 		$("#main_contact_detail").validationEngine({
 	        promptPosition: "inline",
 	        scroll:false,
 	        isOverflown:false,
 	        autoPositionUpdate:true,
 	        validateNonVisibleFields:true,
 	        focusFirstField:false,
 	        showArrow:false
 	        
 	    }); 
 		
 		$("#joint_contact_detail").validationEngine({
 	        promptPosition: "inline",
 	        scroll:false,
 	        isOverflown:false,
 	        autoPositionUpdate:true,
 	        validateNonVisibleFields:true,
 	        focusFirstField:false,
 	        showArrow:false
 	    }); 
 		
 		$("#bank_account_detial").validationEngine({
 	        promptPosition: "inline",
 	        scroll:false,
 	        isOverflown:false,
 	        autoPositionUpdate:true,
 	        validateNonVisibleFields:true,
 	        focusFirstField:false,
 	        showArrow:false
 	    }); 
 		
});