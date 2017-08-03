define(function(require) {
	'use strict';
	var $ = require('jquery');
			require('layui');
    $.paramsDefaults = $.paramsDefaults || {};
    $.paramsDefaults.property = {};
    $.fn.InitCheckPwd = function (b) { 
    	readDisclaimer();
        var fun = function (parentObj) {
            var p = $.extend({}, $.paramsDefaults.property, b || {});
            var orderType = p.orderType; // 撤单0
            var historyId = p.historyId;
            var planId = p.planId;
            params.orderType = orderType;
            params.historyId = historyId;
            params.planId = planId;
        };
        return $(this).each(function(){
            fun($(this));
        });
    };
    
    var params = (function(){
    	var password = '',
    	 	pinCode = '',
    	 	orderType = '',
    	 	historyId = '',
    		planId = '',
    		memberType = '',
    		accountData = [];
    	var loadingGif = '<div class="load-gif mask-layer1" style="position: absolute;top: -20%;width: 100%;left: 0;z-index: 9999;background: rgba(0,0,0,0.2);">'
						 +'<div class="create_groupbox" style="position: fixed;top: 50%;width: 100px;left: 45%;background: rgba(0,0,0,0);">'
					     +'<img style="width: 100px;" src="' + base_root + '/res/images/saving.gif">'
					     +'</div>'
					     +'</div>';
    	return {
    		password:password,
    		pinCode:pinCode,
    		orderType:orderType,
    		historyId:historyId,
    		memberType:memberType,
    		accountData:accountData,
    		loadingGif:loadingGif,
    		planId:planId
    	};
    })();
    
    function showDialog(orderType,historyId,planId){
    	var url = base_root + '/front/tradeOrder/getTransactionAccounts.do?d=' + new Date().getTime();
    	var content = '';
    	$.ajax({
    		type:'post',
    		url:url,
    		data:{
    			orderType:orderType,
    			historyId:historyId,
    			planId:planId
    		},
    		success:function(re){
    			if(re.flag){
    				params.memberType = re.memberType;
    				$.each(re.accounts,function(i,n){
    					content +=
        					'<tr data-distributor-id="'
    					   + n.distributorId
    					   +'"data-account-id="'
    					   + n.accountId
    					   +'" data-member-type="'
    					   + n.memberType
    					   +'" data-account-no="'
    					   + n.accountNo
    					   +'">'
    			    	   +'<td class="register_table_right" style="width: 100px;min-width: 100px;text-align: center;">'
    			    	   +'<img style="width:100px" class="rebalance-distributor-img" src="'
    			    	   + base_root
    			    	   + n.logoUrl
    			    	   +'">'
    			    	   +'</td>'
    			    	   +'<td class="register_table_right" style="width: 150px;min-width: 150px;text-align: center;line-height:35px;">'
    			    	   + n.accountNo
    			    	   +'</td>'
    			    	   +'<td class="register_table_right" style="width: 250px;min-width: 250px;text-align: left;">'
    			    	   +'<div class="input-password-verify" style="">'
    			    	   +'<input id="txtPassWord" type="password" style="height: 30px;border: #dcdadb 1px solid;border-radius: 3px;font-size: 14px;padding-left: 5px;color: #333;width: 215px;" name="" value=""/>'
    			    	   +'</div>'
    			    	   +'<div class="input-pin-verify" style="display:none">'
    			    	   +'<input id=""  maxlength="1" class="vefify-pin vefify-pin-1" readonly="readonly" style="width: 30px;margin-right:5px;float:left;padding-top:5px;line-height: 25px;border: #dcdadb 1px solid;border-radius: 3px;font-size: 16px;text-align:center;color: #a6a6a6;" name="" value="*"/>'
    			    	   +'<input id=""  maxlength="1" class="vefify-pin vefify-pin-2" readonly="readonly" style="width: 30px;margin-right:5px;float:left;padding-top:5px;line-height: 25px;border: #dcdadb 1px solid;border-radius: 3px;font-size: 16px;text-align:center;color: #a6a6a6;" name="" value="*"/>'
    			    	   +'<input id=""  maxlength="1" class="vefify-pin vefify-pin-3" readonly="readonly" style="width: 30px;margin-right:5px;float:left;padding-top:5px;line-height: 25px;border: #dcdadb 1px solid;border-radius: 3px;font-size: 16px;text-align:center;color: #a6a6a6;" name="" value="*"/>'
    			    	   +'<input id=""  maxlength="1" class="vefify-pin vefify-pin-4" readonly="readonly" style="width: 30px;margin-right:5px;float:left;padding-top:5px;line-height: 25px;border: #dcdadb 1px solid;border-radius: 3px;font-size: 16px;text-align:center;color: #a6a6a6;" name="" value="*"/>'
    			    	   +'<input id=""  maxlength="1" class="vefify-pin vefify-pin-5" readonly="readonly" style="width: 30px;margin-right:5px;float:left;padding-top:5px;line-height: 25px;border: #dcdadb 1px solid;border-radius: 3px;font-size: 16px;text-align:center;color: #a6a6a6;" name="" value="*"/>'
    			    	   +'<input id=""  maxlength="1" class="vefify-pin vefify-pin-6" readonly="readonly" style="width: 30px;margin-right:5px;float:left;padding-top:5px;line-height: 25px;border: #dcdadb 1px solid;border-radius: 3px;font-size: 16px;text-align:center;color: #a6a6a6;" name="" value="*"/>'
    			    	   +'</div>'
    			    	   +'</td>'
    			    	   +'<td class="register_table_right" style="width: 100px;min-width: 100px;text-align: center;">'
    			    	   +'<a class="btnTransactionVerify add_schedule_save" style="margin-top: 0px;margin-left:0px;width: 40px;min-width: 40px;text-align: center;" data-status="0">'+props['order.plan.transaction.btn.verify']+'</a>'
    			    	   +'</td>'
    			    	   +'</tr>';
    				});
    				var htmlTemplate = 
    		    		'<div class="transaction-account-varify" style="display:block;position: absolute;top: 0;left: 0;width: 100%;height:100%;z-index: 100;background: rgba(0,0,0,0.6);">'
    		    	   +'<div class="transaction-account-varify-core" style="border-radius: 10px;position: fixed;padding: 10px 30px;width:650px;height: 450px;top: 10%;left: 27%;background: #fff;rgb(62, 62, 62) 1px 1px 9px 4px">'
    		    	   +'<div class="wmes-close" id="verify_transaction-close"></div>'
    		    	   +'<p class="wmes-wrap-title tanchutitle schedule-add-title" style="margin-left:10px;text-align: left;line-height: 44px;margin-top:30px;">'+props['order.plan.transaction.title']+'</p>'
    		    	   +'<div style="height:220px;padding-top:20px;overflow-y:auto;overflow-x:hidden;">'
    		    	   +'<table width="100%" border="0" cellspacing="0" cellpadding="0" class="register_table table-transaction-account-verify" style="margin-top: 0px;">'
    		    	   +'<tbody>'
    		    	   + content
    		    	   +'</tbody>'
    		    	   +'</table>'
    		    	   +'</div>'
    		    	   +'<div style="margin: auto 50px;"><input id="cheAgreeDisclaimer" style="margin-right:8px" type="checkbox"><a id="riskDisclosure" style="line-height: 40px;color: #0976dc;cursor: pointer;">' + langMutilGlobal['global.risk.disclosure'] + '</a></div>'
    		    	   +'<div style="text-align: center;margin-bottom:10px;">'
    		    	   +'<a id="btnTransactionConfirm" class="add_schedule_save" style="cursor: not-allowed;float:none;float:none;margin:10px 0px 0px 10px;display:none;">'+props['global.confirm']+'</a>'
    		    	   +'<a id="btnTransactionCancel" class="add_schedule_cancel" style="float:none;float:none;margin:10px 0px 0px 10px;">'+props['global.cancel']+'</a>'
    		    	   +'</div>'
    		    	   +'</div>'
    		    	   +'</div>';
    				$(".table-transaction-account-verify tbody tr").remove();
    		    	$("body").append(htmlTemplate);
    		    	$('.btnTransactionVerify').click(function(){
    		    		transactionVerify(this);
    		    	});
    		    	$('#verify_transaction-close,#btnTransactionCancel').click(function(){
    		    		transactionCancel();
    		    	});
    		    	$('#btnTransactionConfirm').click(function(){
    		    		var flag = false;
    		    		if($('.btnTransactionVerify').length == 0){
    		    			flag = true;
    		    		}else{
    		    			$('.btnTransactionVerify').each(function(){
        		    			if('2' != $(this).attr('data-status')){
        		    				flag = true;
        		    				return false;
        		    			}
        		    		});
    		    		}
    		    		if(!flag){
    		    			transactionConfirm();
    		    		}
    		    	});
    			}else{
    				layer.msg(props['order.plan.alert.not.found.account'], {icon : 3});
    			}
    		}
    	});
    }
    /**
     * close
     */
    function transactionCancel(){
    	$('.transaction-account-varify').hide();
		window.location.href = window.location.href;
    }
    /**
     * confirm
     */
    function transactionConfirm(){
    	layer.confirm(
			props['order.plan.alert.comfirm.complete'], 
			{
				title:props['global.info'],
    		    btn: [props['global.confirm'], props['global.cancel']]
    		}, 
    		function(index){
    			layer.close(index);
    			$('.transaction-account-varify').hide();
    			if(typeof(params.orderType) != 'undefined' && params.orderType != ''
					&& params.orderType == '0'){ // 撤单
					deleteOrder();
				}else{ //  下单
					addOrder();
				}
    		}
    	);
    }
    /**
     * password、pin verify
     */
    function transactionVerify(self){
    	var status = $(self).attr('data-status');
    	var accountNo = $(self).closest('tr').data('accountNo');
    	if('0' == status){
    		var password = $(self).closest('tr').find('#txtPassWord').val();
    		if(!password){
    			layer.msg(props['order.plan.alert.password.empty'], {icon : 3});
    			return;
    		}
    	   	var password64 = encode64(utf16to8($.trim(password)));
    	   	var url = base_root + '/front/tradeOrder/saveLogin.do?d=' + new Date().getTime();
        	$.ajax({
        		type:'post',
        		url:url,
        		data:{
        			accountNo:accountNo,
        			password:password64
        		},
        		beforeSend:function(){
        			$('body').append(params.loadingGif);
    				$('.load-gif').css('height',$(document).height()*1.2);
    				$('.load-gif').show();
    			},
        		success:function(re){
        			$('.load-gif').hide();
        			if(re.flag){
        				var pingPos = re.pingPos;
        				params.password = password64;
        				params.pinCode = pingPos;
        				$(self).closest('tr').find('.input-password-verify').hide();
        				$(self).closest('tr').find('.input-pin-verify').show();
        				if(pingPos != null && pingPos.indexOf(',') > -1){
        					var pings = pingPos.split(',');
        					$.each(pings,function(i,n){
        						$(self).closest('tr').find('.vefify-pin-'+n).removeAttr('readonly');
        						$(self).closest('tr').find('.vefify-pin-'+n).attr('type','password');
        						$(self).closest('tr').find('.vefify-pin-'+n).val('');
        					});
        				}
        				$(self).attr('data-status','1');
        			}else{
        				layer.msg(props['order.plan.alert.password.verification.failed'], {icon : 1});
        			}
        		},
    			error:function(){
        			$('.load-gif').hide();
        			layer.msg(props['order.plan.alert.password.verification.failed'], {icon : 1});
        		}
        	});
    	}else if('1' == status){
    		var pinCode = '';
    		if(params.pinCode != '' && params.pinCode.indexOf(',') > -1){
    			var pings = params.pinCode.split(',');
    			var emptyValidFlag = true;
    			$.each(pings,function(i,n){
    				if(!$(self).closest('tr').find('.vefify-pin-'+n).val()){
    					emptyValidFlag = false;
    					return false;
    				}
    				pinCode += $(self).closest('tr').find('.vefify-pin-'+n).val() + ',';
    			});
    			if(!emptyValidFlag){
    				layer.msg(props['order.plan.alert.pin.empty'], {icon : 3});
        			return;
    			}
    		}else{
    			layer.msg(props['order.plan.alert.pin.verification.failed'], {icon : 1});
    		}
    		if(pinCode != '' && pinCode.charAt(pinCode.length - 1) == ','){
    			pinCode = pinCode.substring(0, pinCode.length - 1);
    		}
    		var pinCode64 = encode64(utf16to8($.trim(pinCode)));
    		var url = base_root + '/front/tradeOrder/savePin.do?d=' + new Date().getTime();
        	$.ajax({
        		type:'post',
        		url:url,
        		data:{
        			accountNo:accountNo,
        			pinCode:pinCode64
        		},
        		beforeSend:function(){
        			$('body').append(params.loadingGif);
    				$('.load-gif').css('height',$(document).height()*1.2);
    				$('.load-gif').show();
    			},
        		success:function(re){
        			$('.load-gif').hide();
        			if(re.flag){
        				layer.msg(props['order.plan.alert.pin.verification.success'], {icon : 2});
        				var map = {};
        				map['password'] = params.password;
        				map['accountNo'] = accountNo + '';
        				params.accountData.push(map);
        				$(self).closest('tr').find('.vefify-pin').attr('readonly','readonly');
        				$(self).unbind('click');
        				$(self).attr('data-status','2');
        				$(self).hide();
        				$(self).after('<img src="' + base_root + '/res/images/Hook.png">');
        				var flag_ = false;
        				$('.btnTransactionVerify').each(function(){
    		    			if('2' != $(this).attr('data-status')){
    		    				flag_ = true;
    		    				return false;
    		    			}
    		    		});
        				if(!flag_){
        					$('#btnTransactionConfirm').css('cursor','pointer');
        				}
    				}else{
    					layer.msg(props['order.plan.alert.pin.verification.failed'], {icon : 1});
    				}
        		},
        		error:function(){
        			$('.load-gif').hide();
        			layer.msg(props['order.plan.alert.pin.verification.failed'], {icon : 1});
        		}
			});
    	}
    }
    /**
     * 下单
     */
    function addOrder(accountNo){
    	var url = base_root + '/front/tradeOrder/addOrderPlan.do?d=' + new Date().getTime();
    	$.ajax({
    		type:'post',
    		url:url,
    		data:{
    			accountData:encodeURI(JSON.stringify(params.accountData)),
    			accountNo:accountNo,
    			planId:params.planId
    		},
    		beforeSend:function(){
    			$('body').append(params.loadingGif);
				$('.load-gif').css('height',$(document).height()*1.2);
				$('.load-gif').show();
			},
    		success:function(re){
    			$('.load-gif').hide();
    			if(re.flag){
    				layer.msg(props['order.plan.transaction.order.success'], {icon : 2});
    				window.parent.location.href = base_root + '/front/tradeMain/previewOrderPlan.do?id=' + params.planId;
    			}else{
    				layer.msg(props['order.plan.transaction.order.failed'], {icon : 1});
    			}
    		},
    		error:function(){
    			$('.load-gif').hide();
    			layer.msg(props['order.plan.transaction.order.failed'], {icon : 1});
    		}
    	});
    }
    /**
     * 撤单
     */
    function deleteOrder(accountNo){
    	var url = base_root + '/front/tradeOrder/deleteOrder.do?d=' + new Date().getTime();
    	$.ajax({
    		type:'post',
    		url:url,
    		data:{
    			password:params.password,
    			historyId:params.history
    		},
    		beforeSend:function(){
    			$('body').append(params.loadingGif);
				$('.load-gif').css('height',$(document).height()*1.2);
				$('.load-gif').show();
			},
    		success:function(re){
    			$('.load-gif').hide();
    			if(re.flag){
    				layer.msg(props['order.plan.transaction.revoke.success'], {icon : 2});
    				window.parent.location.href = base_root + '/front/tradeMain/previewOrderPlan.do?id=' + params.planId;
    			}else{
    				layer.msg(props['order.plan.transaction.revoke.failed'], {icon : 1});
    			}
    		},
			error:function(){
    			$('.load-gif').hide();
    			layer.msg(props['order.plan.transaction.revoke.failed'], {icon : 1});
    		}
    	});
    }
    /**
     * Disclaimer
     */
    function readDisclaimer(){
    	$('.footbox').remove();
		var footcontent = base_root+"/t/disclaimer/" + lang + ".html";
		var footcontenttitle = langMutilGlobal['global.dissclaimer'];
		if(typeof lang == 'undefined' || lang == ''){
			lang = 'en';
		}
		var footcon = '<div class="footbox">'
				+'<div class="footboxlay"></div>'
				+'<div class="space-mask-wrap" style="top:0px;">'
				+'<div class="ordercontent disclaimer-content">'
				+'<p>' + footcontenttitle + '</p>'
				+'<iframe src="'+footcontent+'" style="width:calc(100% - 70px);height:441px;margin:0 35px;"></iframe>'
				+'<ul><li class="disclaimer-agree" style="margin: auto 8px;display: inline-block;height: 30px;line-height: 30px;background: #2d80ce;border-radius: 3px;padding: 0 20px;color: #fff;cursor: pointer;">'+langMutilGlobal['global.agree']+'</li>'
				+'<li class="disclaimer-reject" style="margin: auto 8px;background:red;color: #fff;">'+langMutilGlobal['global.reject']+'</li></ul>'
				+'</div>'
				+'</div>'
				+'</div>';
		$(".wmes-copyright").before(footcon);	
		var foottop = parseInt(($(window).height() - 647)/2 + $(window).scrollTop());
		$('.ordercontent').css('top',foottop);
		$('.footbox').css('display','block');
    }
    $(document).on('click','.disclaimer-agree',function(){
		$('.footbox').css('display','none');
    	showDialog(params.orderType, params.historyId, params.planId);
	});
    $(document).on('click','.disclaimer-reject',function(){
    	$('.footbox').css('display','none');
		window.location.href = window.location.href;
    });
    $(document).on('change','#cheAgreeDisclaimer',function(){
    	if($(this).prop('checked')){
    		$('#btnTransactionConfirm').css('display','inline-block');
    	}else{
    		$('#btnTransactionConfirm').css('display','none');
    	}
    });
    
    /**
     * Risk Disclosure
     */
    function readRiskDisclosure(){
		var footcontent = base_root+"/t/riskDisclosure/" + lang + ".html";
		var footcontenttitle = langMutilGlobal['global.risk.disclosure'];
		if(typeof lang == 'undefined' || lang == ''){
			lang = 'en';
		}
		var footcon = '<div style="position: absolute;top: 0;left: 0;width: 100%;display: none;z-index: 999999;" class="footbox-second">'
				+'<div class="footboxlay"></div>'
				+'<div class="space-mask-wrap" style="top:0px;">'
				+'<div class="ordercontent risk-disclosure-content">'
				+'<p>'+footcontenttitle+'</p>'
				+'<iframe src="'+footcontent+'" style="width:calc(100% - 70px);height:441px;margin:0 35px;"></iframe>'
				+'<ul><li>'+langMutilGlobal['global.confirm']+'</li></ul>'
				+'</div>'
				+'</div>'
				+'</div>';
		$(".wmes-copyright").before(footcon);	
		var foottop = parseInt(($(window).height() - 647)/2 + $(window).scrollTop());
		$('.ordercontent').css('top',foottop);
		$('.footbox-second').css('display','block');
    }
    $(document).on('click','#riskDisclosure',function(){
    	readRiskDisclosure();
	});
    $(document).on('click','.risk-disclosure-content li',function(){
    	$('.footbox-second').css('display','none');
    });
    
    /*****************************************************************************************************/
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURIComponent(r[2]);
        return null;
    }
    function getParentQueryString(name) {
    	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    	var r = window.parent.location.search.substr(1).match(reg);
    	if (r != null) return decodeURIComponent(r[2]);
    	return null;
    }
});
