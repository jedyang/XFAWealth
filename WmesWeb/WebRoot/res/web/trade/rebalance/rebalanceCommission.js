define(function(require) {
	var $ = require('jquery');
	var interfaceObj = require("interfaceCheckPwd");
	require('orderPlanCheckPwd');
	require('laydate');
	//验证登录
	if(_checkList!=undefined && _checkList!=""){
		$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
	}
	$(".client-more-ico").on("click",function(){
		$(this).closest(".rebalance-table-rows").toggleClass("client-more-active");
	});
	$('#analysis-chart-ico').click(function(){
		$(this).toggleClass('rotate');
		$('.client-Detailbox').slideToggle('fast');
	});
	$('#btnPrevious').click(function(){
		saveSubscription();
	});
	$(".proposal-more-ico").on("click",function(){
		$(this).closest(".order-plan-rows").toggleClass("client-more-active");
	});
	if($('.order-table-rebalance-redemption tbody tr').length < 3){
		$('.order-table-rebalance-redemption').hide();
		$('.order-setting-rebalance-redemption').hide();
	}
	if($('.order-table-subscription-purchase tbody tr').length < 3){
		$('.order-table-subscription-purchase').hide();
		$('.order-setting-subscription-purchase').hide();
	}
	if($('.order-table-rebalance-redemption tbody tr').length < 3
			&& $('.order-table-subscription-purchase tbody tr').length < 3){
		$('.no_list_tips').show();
	}else{
		$('.no_list_tips').hide();
	}
	$('.button-cancel').click(function(){
		$('.space-mask-wrap').hide();
		$(".visitedconlayer").hide();
	});
	/**
	 * submit
	 */
	$('#btnSubmit').click(function(){
		//getMySupervisor();
		var flag = dataValid();
		if(flag){
			getMySupervisor();
		}
	});
	/**
	 * data Valid
	 */
	function dataValid(){
		// 是否已开户，有账号
		if($('.client-table').find('.ifa-check-hold-account').length == 0){
			layer.msg(props['order.plan.rebalance.alert.empty.account']);
			return false;
		}
		// min tran Fee valid
		var mintTranFeeValid = false;
		$('.product-mini-tran-fee').each(function(){
			if($(this).text() && $(this).text() != '-'){
				var miniFee = $(this).text().replace(/,/g,'');
				var tranFee = $(this).closest('tr').find('.product-tran-fee').text().replace(/,/g,'');
				if(Number(tranFee) < Number(miniFee)){
					layer.msg(props['order.plan.rebalance.alert.mini.fee.incorrect']);
					mintTranFeeValid = true;
					return false;
				}
			}
		});
		if(mintTranFeeValid)return false;
		var accountTransactionInfo = {};
		$('.order-table-subscription-purchase-tbody tr:gt(0)').each(function(){
			var amount = $(this).find('.rebalance-amount-input').val();
			var minSubscribeAmount = $(this).find('.rebalance-min-subscribe-amount-text').text();
			// mini subscribe amount valid
			if(amount == '' || Number(amount) == 0){
				layer.msg(props['order.plan.commission.alert.product.amount.ctrl']);
				validFlag = true;return false;
			}
			if(minSubscribeAmount && Number(minSubscribeAmount) > Number(amount)){
				layer.msg(props['order.plan.commission.alert.product.amount.ctrl']);
				validFlag = true;return false;
			}
			var tranAmount = $(this).find('.product-transaction-amount').text();
			if(tranAmount){tranAmount = tranAmount.replace(/,/g,'').trim();}
			var investorId = '';
			if($(this).find('.order-table-select-account').length == 0){
				investorId = $(this).find('.select-option-account').data('investorId');
			}else{
				investorId = $(this).find('.order-table-select-account option:selected').data('investorId');
			}
			if(investorId){
				var accountTranAmount = '';
				if(typeof accountTransactionInfo[investorId] == 'undefined'){
					accountTranAmount = tranAmount;
				}else{
					accountTranAmount = accountTransactionInfo[investorId];
					accountTranAmount = Number(accountTranAmount) + Number(tranAmount);
				}
				accountTransactionInfo[investorId] = accountTranAmount;
			}
		});
		// account transaction valid
		var accountTransactionValid = false;
		if(accountTransactionInfo != '{}'){
			for(var key in accountTransactionInfo){
				var accountCashAvailable = $('#'+key).find('.cash-value').text();
				if(accountCashAvailable){accountCashAvailable = accountCashAvailable.replace(/,/g,'').trim();}
				var tranAmount = accountTransactionInfo[key];
				if(Number(tranAmount) > Number(accountCashAvailable)){
					var accountNo = $('#'+key).find('.transaction-account-no').text();
					var alertTip = props['order.plan.account.alert.account.transaction.valid'];
					alertTip = alertTip.replace('{0}','【'+accountNo+'】');
					alertTip = alertTip.replace('{ 0 }','【'+accountNo+'】');
					layer.msg(alertTip);
					accountTransactionValid = true;return false;
				}
			}
		}
		if(accountTransactionValid)return false;
		// investor account ifa valid
		var memberType = $('#hidMemberType').val();
		if('1' == memberType){
			var ifaIds = [];
			if($('.order-table-select-account').length > 0){
				$('.order-table-select-account').each(function(){
					var ifaId = $(this).find('option:selected').data('ifaId');
					ifaIds.push(ifaId);
				});
			}else{
				$('.select-option-account').each(function(){
					var ifaId = $(this).data('ifaId');
					ifaIds.push(ifaId);
				});
			}
			var targetCount = ifaIds.unique().length;
			if(targetCount > 1){
				layer.msg(props['order.plan.account.alert.same.ifa.account']);
				return false;
			}
		}
		return true;
	}
	/**
	 * submit data
	 */
	function submitData(confirmation){
		// supervisor
		var supervisor = $('.value_show').attr('data-member-id');
		// IFA
		var ifaId = '';
		if($('.order-table-select-account').length > 0){
			ifaId = $('.order-table-select-account').eq(0).find('option:selected').data('ifaId');
		}else{
			ifaId = $('.select-option-account').eq(0).data('ifaId');
		}
		var authorizedYes = {};
		authorizedYes['flag'] = false;
		var authorizedNo = {};
		authorizedNo['flag'] = false;
		$('.order-table-subscription-purchase-tbody tr:gt(0)').each(function(){
			var authorizedMap = {};
			var authorized = '';
			if($(this).find('.order-table-select-account').length == 0){
				authorized = $(this).find('.select-option-account').data('authorized');
			}else{
				authorized = $(this).find('.order-table-select-account option:selected').data('authorized');
			}
			if(authorized == '1'){
				authorizedYes['authorized'] = '1';
				authorizedYes['flag'] = true;
			}else{
				authorizedNo['authorized'] = '0';
				authorizedNo['flag'] = true;
			}
		});
		if(authorizedYes.flag && authorizedNo.flag){
			layer.msg(props['order.plan.commission.alert.trading.account.authorized.ctrl']);
			return;
		}
		var authorized = '0';
		if(authorizedYes.flag){
			authorized = '1';
		}
		var subscriptionDatas = [];
		$('.order-table-subscription-purchase-tbody tr:gt(0)').each(function(){
			var subscriptionData = {};
			var planProduct = $(this).data('planProduct');
			var investorId = '';
			if($(this).find('.order-table-select-account').length == 0){
				investorId = $(this).find('.select-option-account').data('investorId');
			}else{
				investorId = $(this).find('.order-table-select-account option:selected').data('investorId');
			}
			var dividend = $(this).find('.order-table-select-dividend').val();
			var tranRate = $(this).find('.table-product-tran-rate').val();
			var amount = $(this).find('.rebalance-amount-input').val();
			var minSubscribeAmount = $(this).find('.rebalance-min-subscribe-amount-text').text();
			var weightAdjust = $(this).find('.product-weight').text();
			if(amount == '' || Number(amount) == 0){
				layer.msg(props['order.plan.commission.alert.product.amount.ctrl']);
			}
			if(minSubscribeAmount && Number(minSubscribeAmount) > Number(amount)){
				layer.msg(props['order.plan.commission.alert.product.amount.ctrl']);
			}
			if(amount){
				amount = amount.replace(/,/g,'');
			}
			if(weightAdjust){
				weightAdjust = weightAdjust.trim();
			}
			subscriptionData['planProduct'] = planProduct;
			subscriptionData['investorId'] = investorId+'';
			subscriptionData['dividend'] = dividend;
			subscriptionData['tranRate'] = tranRate;
			subscriptionData['amount'] = amount;
			subscriptionData['weightAdjust'] = weightAdjust;
			subscriptionDatas.push(subscriptionData);
		});
		//return;
		var redemptionDatas = [];
		$('.order-table-rebalance-redemption tbody tr:gt(0)').each(function(){
			var redemptionData = {};
			var planProduct = $(this).data('planProduct');
			redemptionData['planProduct'] = planProduct;
			redemptionDatas.push(redemptionData);
		});
		var url = base_root + '/front/tradeRebalance/saveOrderPlanCommission.do?d='+new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				planId:getUrlParam('planId'),
				currencyCode:$('#portfolio-currency').attr('data-code'),
				supervisor:supervisor,
				ifaId:ifaId,
				authorized:authorized,
				confirmation:confirmation,
				subscriptionDatas:encodeURI(JSON.stringify(subscriptionDatas))
			},
			success:function(result){
				if(result.flag){
					if(result.memberType != null 
							&& result.memberType == 1){
					// investor
						if(result.confirmation == '1'){
							window.location.href = base_root + '/front/tradeRebalance/rebalanceComplete.do?planId='+ result.planId;
						}else if(result.confirmation != '1'){
							var map = {};
							map['planId'] = getUrlParam('planId');
							$(this).InitCheckPwd(map);
						}
					}else if(result.memberType != null && result.memberType == 2){
					// IFA
						window.location.href = base_root + '/front/tradeRebalance/rebalanceComplete.do?planId='+ result.planId;
					}
				}
			}
		});
	}
	
	function saveSubscription(){
		var subscriptionDatas = [];
		$('.order-table-subscription-purchase-tbody tr:gt(0)').each(function(){
			var subscriptionData = {};
			var planProduct = $(this).data('planProduct');
			var investorId = '';
			if($(this).find('.order-table-select-account').length == 0){
				investorId = $(this).find('.select-option-account').data('investorId');
			}else{
				investorId = $(this).find('.order-table-select-account option:selected').data('investorId');
			}
			var dividend = $(this).find('.order-table-select-dividend').val();
			var tranRate = $(this).find('.table-product-tran-rate').val();
			var amount = $(this).find('.rebalance-amount-input').val();
			var weightAdjust = $(this).find('.product-weight').text();
			if(amount){
				amount = amount.replace(/,/g,'');
			}
			if(weightAdjust){
				weightAdjust = weightAdjust.trim();
			}
			subscriptionData['planProduct'] = planProduct;
			subscriptionData['investorId'] = investorId+'';
			subscriptionData['dividend'] = dividend;
			subscriptionData['tranRate'] = tranRate;
			subscriptionData['amount'] = amount;
			subscriptionData['weightAdjust'] = weightAdjust;
			subscriptionDatas.push(subscriptionData);
		});
		var url = base_root + '/front/tradeStep/saveOrderPlanSubscription.do?d='+new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				planId:getUrlParam('planId'),
				currencyCode:$('#portfolio-currency').attr('data-code'),
				subscriptionDatas:encodeURI(JSON.stringify(subscriptionDatas))
			},
			success:function(result){
				window.location.href = base_root + '/front/tradeMain/orderPlanSendRedirect.do?planId='+ getUrlParam('planId')+'&ifEdit=0';
			},error:function(){
				layer.msg(props['global.operation.failed']);
			}
		});
	}
	
	/**
	 * 基金风险等级比较 
	 */
	$('.order-table-select-account').change(function(){
		var value = $(this).val();
		var rpqRiskLevel = '';
		var totalFlag = '';
		$(this).find('option').each(function(){
			if(value == $(this).val()){
				rpqRiskLevel = $(this).data('risk-level');
				totalFlag = $(this).data('total-flag');
				return false;
			}
		});
		var productRiskLevel = $(this).closest('tr').find('.product-risk-level').text();
		if(rpqRiskLevel != '' && productRiskLevel != '' && Number(productRiskLevel)>Number(rpqRiskLevel)){
			$(this).closest('tr').find('.product-update-ico-rpq-level').show();
		}else{
			$(this).closest('tr').find('.product-update-ico-rpq-level').hide();
		}
		$(this).closest('tr').find('.account-total-flag').text('');
		$(this).closest('tr').find('.account-total-flag').attr('data-total-flag',totalFlag);
		if('1' == totalFlag){
			$(this).closest('tr').find('.account-total-flag').text(props['order.plan.commission.account.total.flag.1']);
		}else if('0' == totalFlag){
			$(this).closest('tr').find('.account-total-flag').text(props['order.plan.commission.account.total.flag.0']);
		}
		// 金额汇总
		orderTotalFee();
	});
	(function(){
		$('.order-table-select-account').each(function(){
			var rpqRiskLevel = $(this).find('option:eq(0)').data('risk-level');
			var productRiskLevel = $(this).closest('tr').find('.product-risk-level').text();
			if(rpqRiskLevel != '' && productRiskLevel != '' && Number(productRiskLevel)>Number(rpqRiskLevel)){
				$(this).closest('tr').find('.product-update-ico-rpq-level').show();
			}else{
				$(this).closest('tr').find('.product-update-ico-rpq-level').hide();
			}
		});
	})();
	/**
	 * supervisor dialog 滚动
	 */
	function selectSupervisorLoadScroll(){
		$('.fund_float_menu_addbox').css('top',$(document).height()*0.15+$(window).scrollTop());
	}
	/**
	 * supervisor dialog close
	 */
	$('.select-supervisor-cancel').click(function(){
		$('.fund-space-mask-wrap,.fund_float_menu_addbox').hide();
	});
	/**
	 * supervisor dialog confirm
	 */
	$('.select-supervisor-confirm').click(function(){
		$('.fund-space-mask-wrap,.fund_float_menu_addbox').hide();
		submitData();
	});
	$('.value_show,.register_xiala_ico').click(function(){
		$('.regiter_xiala_ul_supervisor').show();
	});
	/**
	 * 获取My Supervisor数据
	 */
	function getMySupervisor(){
		var url = base_root + '/front/tradeStep/getMySupervisor.do?d=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				planId:getUrlParam('planId')
			},
			success:function(re){
				if(re.flag){
				var html = '';
				var myTeamListVOs = re.myTeamListVOs;
				if(myTeamListVOs != null && myTeamListVOs.length>0){
					$.each(myTeamListVOs,function(i,n){
						html += '<li class="regiter_xiala_li_supervisor" value="'+n.memberId+'">'+n.nickname+'</li>';
					});
					$('.regiter_xiala_ul_supervisor').empty().append(html);
					$('.regiter_xiala_li_supervisor').unbind('click');
					$('.regiter_xiala_li_supervisor').click(function(){
						$('.regiter_xiala_ul_supervisor').hide();
						var memberId = $(this).attr('value');
						var nickName = $(this).text();
						$('.value_show').val(nickName);
						$('.value_show').attr('data-member-id',memberId);
					});
					$('.regiter_xiala_li_supervisor').eq(0).click();
					$('.fund-space-mask-wrap,.fund_float_menu_addbox').show();
					$('.fund_float_menu_addbox').css('top',$(document).height()*0.15+$(window).scrollTop());
				    $(window).on('scroll',selectSupervisorLoadScroll);
				}
				}else if(1 == re.memberType){
				// investor
					layer.confirm(props['order.plan.commission.alert.if.push.IFA.confirmation'], {
			 			  title:props['global.info'],
						  btn: [props['global.true'],props['global.false']] //按钮
					},function(index){
						layer.close(index);
						check = '1';
						submitData('1');
					},function(index){
						layer.close(index);
						submitData('0');
					});
				}else{
					layer.msg(props['order.plan.commission.dialog.select.supervisor.alert.no.found.supervisor']);
				}
			},
			error:function(){
				layer.msg(props['order.plan.commission.dialog.select.supervisor.alert.no.found.supervisor']);
			}
		});
	}
	/**************************************************************************************************************/	
	/**
	 * 基础货币下拉选择
	 */
	$('.portfolio-currency').click(function(){
    	$('#currency-choice').toggle();
    	$('#currency-choice li').unbind('click');
    	$('#currency-choice li').click(function(){
    		$('#currency-choice').hide();
    		$('#portfolio-currency').val($(this).text());
    		$('#currency-choice').hide();
    		var fromCurrency = $('#portfolio-currency').attr('data-code');
    		var toCurrency = $(this).data('code');
    		conversion(fromCurrency,toCurrency);
    		$('.to-currency').text($(this).text());
    		$('#portfolio-currency').val($(this).text());
    		$('#portfolio-currency').attr('data-code',toCurrency);
    	});
    });
	$('.proposal_xiala').on('mouseleave',function(){
		$('#currency-choice').hide();
	});
	/**
	 * 基础货币获取
	 */
	function currencyType(){
		var type = 'currency_type';
		var url = base_root+'/console/sys/param/paramList.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{type:type},
			success:function(result){
				var currencys = result.result;
				if(typeof(currencys)!='undefined'&&currencys.length!=0){
					var liHtml = '';
					var defCode= $('#hidDefCurrency').val();
					var defCurrency = '';
					$.each(currencys,function(i,n){
						var name = n.name != null?n.name:'';
						var code = n.code != null?n.code:'';
						liHtml += '<li id="'+n.id+'" data-code="'+code+'">'
							+ name;
							+'</li>';
						if(defCode == code){
							defCurrency = name;
						}
					});
					$('#currency-choice').empty().append(liHtml);
					$('#portfolio-currency').val(defCurrency);
					$('#portfolio-currency').attr('data-code',defCode);
					$('.to-currency').each(function(){
						$(this).text(defCurrency);
					});
				}
			}
		});
	}
	/**
	 * 货币转换
	 */
	function conversion(fromCurrency,toCurrency){
		var conversions = [];
		var toConversions = [];
		$('.conversion-flag').each(function(){
			if($(this).is('input')||$(this).is('textarea')||$(this).is('select')){
				conversions.push($(this).val());
			}else{
				conversions.push($(this).text());
			}
		});
		var url = base_root+'/front/fund/info/getExchangeRate.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{fromCurrency:fromCurrency,toCurrency:toCurrency},
			success:function(result){
				if(result.flag){
					var rate = result.exchangeRate != null ? result.exchangeRate.rate : '';
					if(rate != ''){
						$.each(conversions,function(i,n){
							if(typeof(n) != 'undefined' && n != ''){
								if(n.indexOf(',') > -1){
									n = n.replace(/,/gm,'');
								}
								var args = '';
								if('JPY' == toCurrency){
									args = parseFloat(n*rate).toFixed(0);
								}else{
									args = parseFloat(n*rate).toFixed(2);
								}
								toConversions.push(args);
							}else{
								toConversions.push('');
							}
						});
						$('.conversion-flag').each(function(i,n){
							if($(this).is('input')||$(this).is('textarea')||$(this).is('select')){
								$(this).val(formatCurrency(toConversions[i]));	
							}else{
								$(this).text(formatCurrency(toConversions[i]));
							}
						});
					}
				}
			}
		});
	}
	currencyType(); // 获取基础货币
	/**
	 * 购买金额控制
	 */
	numCheck('.rebalance-amount-input');
	$(".rebalance-amount-input").on('change',function(){
		// 金额汇总
		orderTotalFee();
		// 重置 weight
		refashWeight();
	});
	/**
	 * 重置 weight
	 */ 
	function refashWeight(){
		var totalAmt = $('#txtTotalInvestmentAmt').val();
		totalAmt = Number(totalAmt.replace(/,/g,''));
		$('.order-table-subscription-purchase-tbody').find('.rebalance-amount-input').each(function(){
			var amount = $(this).val();
			if(amount){
				amount = Number(amount.replace(/,/g,''));
				var weight = Math.floor(amount/totalAmt*100);
				if(amount>0 && weight == 0){
					weight = 1;
				}
				$(this).closest('tr').find('.product-weight').text(weight);
			}
		});
		var totalWeight = 0;
		$('.order-table-subscription-purchase-tbody .product-weight').each(function(){
			var weight = $(this).text();
			if(weight){
				weight = weight.trim();
				totalWeight = Number(totalWeight) + Number(weight);
			}
		});
		if(Number(totalWeight) == 99){
			var weight = $('.order-table-subscription-purchase-tbody .product-weight').eq(0).text();
			if(weight){
				weight = Number(weight)+1;
				$('.order-table-subscription-purchase-tbody .product-weight').eq(0).text(weight);
				totalWeight = 100;
			}
		}
		$('.subscription-total-weight').text(totalWeight);
	}
	/**
	 * 交易费率控制
	 */
	$(document).find(".table-product-tran-rate").keypress(function(event) {  
        var keyCode = event.which;  
        if (keyCode == 46 || (keyCode >= 48 && keyCode <=57))  
            return true;  
        else  
            return false;  
    }).focus(function() {  
        this.style.imeMode='disabled';  
    });  
	/*$(".table-product-tran-rate").on('focus',function(){
		if(Number($(this).val())==0){$(this).val('');}
	});*/
	$('.table-product-tran-rate').on('input',function(){
		var rate = $(this).val();
		var tranType = $(this).data('tranType');
		var amount = 0.00;
		if('S' == tranType){
			var lastNav = $(this).closest('p').find('.hid-last-nav').val();
			var unit = $(this).closest('tr').find('.rebalance-unit-input').text();
			if(lastNav && unit){
				lastNav = Number(lastNav.replace(/,/g,''));
				unit = Number(unit.trim());
				amount = lastNav*unit;
			}
		}else{
			amount = $(this).closest('tr').find('.rebalance-amount-input').val();
	 		if(amount){
	 			amount = amount.replace(/,/gm,'');
	 		}
		}
 		var tranFee = parseFloat(amount*rate/100).toFixed(2);
 		$(this).closest('tr').find('.product-tran-fee').text(formatCurrency(tranFee));
 		// 金额汇总
		orderTotalFee();
	});
	$('.table-product-tran-rate').on('change',function(){
		var rate = $(this).val();
		var tranType = $(this).data('tranType');
		var amount = 0.00;
		if('S' == tranType){
			var lastNav = $(this).closest('p').find('.hid-last-nav').val();
			var unit = $(this).closest('tr').find('.rebalance-unit-input').text();
			if(lastNav && unit){
				lastNav = Number(lastNav.replace(/,/g,''));
				unit = Number(unit.trim());
				amount = lastNav*unit;
			}
		}else{
			amount = $(this).closest('tr').find('.rebalance-amount-input').val();
	 		if(amount){
	 			amount = amount.replace(/,/gm,'');
	 		}
		}
 		var tranFee = parseFloat(amount*rate/100).toFixed(2);
		var minFee = $(this).closest('tr').find('.product-mini-tran-fee').text();
 		minFee = minFee.replace(/,/g,'');
 		if(minFee && minFee != '-' && Number(minFee) > Number(tranFee)){
 			layer.msg(props['order.plan.commission.alert.tran.fee.ctrl']);
 			tranFee = minFee;
 			$(this).val(formatCurrency(Number(minFee)/Number(amount)*100));
 			$(this).closest('tr').find('.product-tran-fee').text(formatCurrency(tranFee));
 		}
 		// 金额汇总
		orderTotalFee();
	});
	/**
	 * 金额汇总
	 */
	function orderTotalFee(){
		var totalAmount = 0.00;
		var totalTranFee = 0.00;
		var totalTranAmount = 0.00;
		var totalWeight = 0;
		var totalUnit = 0;
		var totalRedeemTranFee = 0.00;
		var totalRedeemTranAmount = 0.00;
		var totalRedeemWeight = 0;
		$('.order-table-subscription-purchase-tbody .rebalance-amount-input').each(function(){
			var amount = $(this).val();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalAmount = Number(totalAmount) + Number(amount);
			}
		});
		$('.subscription-total-amount').text(formatCurrency(totalAmount));
		$('#txtTotalInvestmentAmt').val(formatCurrency(totalAmount));
		$('#txtAipAmount').val(formatCurrency(totalAmount));
		$('.order-table-redeem-purchase-tbody .rebalance-unit-input').each(function(){
			var unit = $(this).text();
			if(unit){
				unit = unit.trim().replace(/,/g,'');
				totalUnit = Number(totalUnit) + Number(unit);
			}
		});
		$('.redeem-total-unit').text(totalUnit);
		$('.total-transaction-unit-value').text(totalUnit);
		$('.order-table-subscription-purchase-tbody .product-tran-fee').each(function(){
			var amount = $(this).text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalTranFee = Number(totalTranFee) + Number(amount);
			}
		});
		$('.order-table-redeem-purchase-tbody .product-tran-fee').each(function(){
			var amount = $(this).text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalRedeemTranFee = Number(totalRedeemTranFee) + Number(amount);
			}
		});
		$('.order-table-subscription-purchase-tbody tr:gt(0)').each(function(){
			var amount = $(this).find('.rebalance-amount-input').val();
			var tranFee = $(this).find('.product-tran-fee').text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
			}
			if(tranFee){
				tranFee = tranFee.trim().replace(/,/g,'');
			}
			var trasnsactionAmount = 0.00;
			if($(this).find('.account-total-flag').attr('data-total-flag') == '0'){
				trasnsactionAmount = Number(amount) + Number(tranFee);
			}else{
				trasnsactionAmount = Number(amount);
			}
			$(this).find('.product-transaction-amount').text(formatCurrency(trasnsactionAmount));
		});
		$('.order-table-redeem-purchase-tbody tr:gt(0)').each(function(){
			var tranFee = $(this).find('.product-tran-fee').text();
			if(tranFee){
				tranFee = tranFee.trim().replace(/,/g,'');
			}
			var trasnsactionAmount = 0.00;
			if($(this).find('.account-total-flag').attr('data-total-flag') == '0'){
				trasnsactionAmount = Number(tranFee);
			}else{
				trasnsactionAmount = 0.00;
			}
			$(this).find('.product-transaction-amount').text(formatCurrency(trasnsactionAmount));
		});
		$('.subscription-total-transaction-fee').text(formatCurrency(totalTranFee));
		$('.redeem-total-transaction-fee').text(formatCurrency(totalRedeemTranFee));
		// total Fee
		totalTranFee = totalTranFee + totalRedeemTranFee;
		$('.total-transaction-fee-value').text(formatCurrency(totalTranFee));
		$('.order-table-subscription-purchase-tbody .product-transaction-amount').each(function(){
			var amount = $(this).text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalTranAmount = Number(totalTranAmount) + Number(amount);
			}
		});
		$('.subscription-total-transaction-amount').text(formatCurrency(totalTranAmount));
		$('.order-table-redeem-purchase-tbody .product-transaction-amount').each(function(){
			var amount = $(this).text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalRedeemTranAmount = Number(totalRedeemTranAmount) + Number(amount);
			}
		});
		$('.redeem-total-transaction-amount').text(formatCurrency(totalRedeemTranAmount));
		totalTranAmount = totalTranAmount + totalRedeemTranAmount;
		$('.total-transaction-amount-value').text(formatCurrency(totalTranAmount));
		$('.order-table-subscription-purchase-tbody .product-weight').each(function(){
			var weight = $(this).text();
			if(weight){
				weight = weight.trim();
				totalWeight = Number(totalWeight) + Number(weight);
			}
		});
		$('.subscription-total-weight').text(totalWeight);
		$('.order-table-redeem-purchase-tbody .product-weight').each(function(){
			var weight = $(this).text();
			if(weight){
				weight = weight.trim();
				totalRedeemWeight = Number(totalRedeemWeight) + Number(weight);
			}
		});
		$('.redeem-total-weight').text(totalRedeemWeight);
		var cashAvailable = $('.cash-available-value').text();
		if(cashAvailable && totalTranAmount){
			cashAvailable = Number(cashAvailable.replace(/,/g,''));
			if(Number(totalTranAmount) > cashAvailable){
				layer.msg(props['order.plan.commission.alert.total.transaction.amount.ctrl']);
				notAllowedSubmit();
			}else{
				allowedSubmit();
			}
		}else{
			notAllowedSubmit();
		}
	}
	/**
	 * submit on
	 */
	function allowedSubmit(){
		$('#btnSubmit').css('cursor','auto');
		$('#btnSubmit').removeAttr('title');
		$('#btnSubmit').unbind('click');
		$('#btnSubmit').click(function(){
			var flag = dataValid();
			if(flag){
				getMySupervisor();
			}
		});
	}
	/**
	 * submit off
	 */
	function notAllowedSubmit(){
		layer.msg(props['order.plan.commission.alert.total.transaction.amount.ctrl']);
		$('#btnSubmit').css('cursor','not-allowed');
		$('#btnSubmit').attr('title',props['order.plan.commission.alert.total.transaction.amount.ctrl']);
		$('#btnSubmit').unbind('click');
		$('#btnSubmit').click(function(){
			layer.msg(props['order.plan.commission.alert.total.transaction.amount.ctrl']);
		});
	}
	/**
	 * Amt控制
	 */
	$('#txtTotalInvestmentAmt').on('focus',function(){
		if($(this).val() == '-'){
			$(this).val('');
		}
	});
	numCheck('#txtTotalInvestmentAmt');
	function initOrderTotalFee(){
		var totalAmount = 0.00;
		var totalTranFee = 0.00;
		var totalTranAmount = 0.00;
		var totalWeight = 0;
		var totalUnit = 0;
		var totalRedeemTranFee = 0.00;
		var totalRedeemTranAmount = 0.00;
		var totalRedeemWeight = 0;
		$('.order-table-subscription-purchase-tbody .rebalance-amount-input').each(function(){
			var amount = $(this).val();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalAmount = Number(totalAmount) + Number(amount);
			}
		});
		$('.subscription-total-amount').text(formatCurrency(totalAmount));
		$('#txtTotalInvestmentAmt').val(formatCurrency(totalAmount));
		$('#txtAipAmount').val(formatCurrency(totalAmount));
		$('.order-table-redeem-purchase-tbody .rebalance-unit-input').each(function(){
			var unit = $(this).text();
			if(unit){
				unit = unit.trim().replace(/,/g,'');
				totalUnit = Number(totalUnit) + Number(unit);
			}
		});
		$('.redeem-total-unit').text(totalUnit);
		$('.total-transaction-unit-value').text(totalUnit);
		$('.order-table-subscription-purchase-tbody .product-tran-fee').each(function(){
			var amount = $(this).text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalTranFee = Number(totalTranFee) + Number(amount);
			}
		});
		$('.order-table-redeem-purchase-tbody .product-tran-fee').each(function(){
			var amount = $(this).text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalRedeemTranFee = Number(totalRedeemTranFee) + Number(amount);
			}
		});
		$('.order-table-subscription-purchase-tbody tr:gt(0)').each(function(){
			var amount = $(this).find('.rebalance-amount-input').val();
			var tranFee = $(this).find('.product-tran-fee').text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
			}
			if(tranFee){
				tranFee = tranFee.trim().replace(/,/g,'');
			}
			var trasnsactionAmount = 0.00;
			if($(this).find('.account-total-flag').attr('data-total-flag') == '0'){
				trasnsactionAmount = Number(amount) + Number(tranFee);
			}else{
				trasnsactionAmount = Number(amount);
			}
			$(this).find('.product-transaction-amount').text(formatCurrency(trasnsactionAmount));
		});
		$('.order-table-redeem-purchase-tbody tr:gt(0)').each(function(){
			var tranFee = $(this).find('.product-tran-fee').text();
			if(tranFee){
				tranFee = tranFee.trim().replace(/,/g,'');
			}
			var trasnsactionAmount = 0.00;
			if($(this).find('.account-total-flag').attr('data-total-flag') == '0'){
				trasnsactionAmount = Number(tranFee);
			}else{
				trasnsactionAmount = 0.00;
			}
			$(this).find('.product-transaction-amount').text(formatCurrency(trasnsactionAmount));
		});
		$('.subscription-total-transaction-fee').text(formatCurrency(totalTranFee));
		$('.redeem-total-transaction-fee').text(formatCurrency(totalRedeemTranFee));
		// total Fee
		totalTranFee = totalTranFee + totalRedeemTranFee;
		$('.total-transaction-fee-value').text(formatCurrency(totalTranFee));
		$('.order-table-subscription-purchase-tbody .product-transaction-amount').each(function(){
			var amount = $(this).text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalTranAmount = Number(totalTranAmount) + Number(amount);
			}
		});
		$('.subscription-total-transaction-amount').text(formatCurrency(totalTranAmount));
		$('.order-table-redeem-purchase-tbody .product-transaction-amount').each(function(){
			var amount = $(this).text();
			if(amount){
				amount = amount.trim().replace(/,/g,'');
				totalRedeemTranAmount = Number(totalRedeemTranAmount) + Number(amount);
			}
		});
		$('.redeem-total-transaction-amount').text(formatCurrency(totalRedeemTranAmount));
		totalTranAmount = totalTranAmount + totalRedeemTranAmount;
		$('.total-transaction-amount-value').text(formatCurrency(totalTranAmount));
		$('.order-table-subscription-purchase-tbody .product-weight').each(function(){
			var weight = $(this).text();
			if(weight){
				weight = weight.trim();
				totalWeight = Number(totalWeight) + Number(weight);
			}
		});
		$('.subscription-total-weight').text(totalWeight);
		$('.order-table-redeem-purchase-tbody .product-weight').each(function(){
			var weight = $(this).text();
			if(weight){
				weight = weight.trim();
				totalRedeemWeight = Number(totalRedeemWeight) + Number(weight);
			}
		});
		$('.redeem-total-weight').text(totalRedeemWeight);
		var cashAvailable = $('.cash-available-value').text();
		cashAvailable = cashAvailable.replace(/,/g,'');
		if(!isNaN(Number(cashAvailable)) && !isNaN(totalTranAmount)){
			if(Number(totalTranAmount) > cashAvailable){
				$('#btnSubmit').css('cursor','not-allowed');
				$('#btnSubmit').attr('title',props['order.plan.commission.alert.total.transaction.amount.ctrl']);
				$('#btnSubmit').unbind('click');
				$('#btnSubmit').click(function(){
					layer.msg(props['order.plan.commission.alert.total.transaction.amount.ctrl']);
				});
			}else{
				allowedSubmit();
			}
		}else{
			$('#btnSubmit').css('cursor','not-allowed');
			$('#btnSubmit').attr('title',props['order.plan.commission.alert.total.transaction.amount.ctrl']);
			$('#btnSubmit').unbind('click');
			$('#btnSubmit').click(function(){
				layer.msg(props['order.plan.commission.alert.total.transaction.amount.ctrl']);
			});
		}
	}
	setTimeout(initOrderTotalFee,100);
	$('#txtTotalInvestmentAmt').on('change',function(){
		totalAmtAdjust();
	});
	/**
	 * total Amt Adjust
	 */
	function totalAmtAdjust(){
		var totalAmt = $('#txtTotalInvestmentAmt').val();
		totalAmt = Number(totalAmt.replace(/,/g,''));
		$('.order-table-subscription-purchase-tbody').find('.product-weight').each(function(){
			var weight = Number($(this).text());
			amount = totalAmt*weight/100;
			$(this).closest('tr').find('.rebalance-amount-input').val(formatCurrency(amount));
		});
		orderTotalFee();
	}
    /*************************************************AIP*******************************************************/
    /**
	 * 定投开关
	 */
	$(document).on('click','#aipState',function(){
		if($(this).attr('data-state')==1){
		// off
			$(this).removeClass("aipstate_button_active");
			$(this).attr('data-state',0);
			$('#aipChange').hide();
			$('.order-plan-aip-detail').hide();
			inputCtrl(false);
		}else{
		// on
			$('.proposal-mask-wrap').show();
			$('.proposal-mask-contents').show();
			$('.proposal-mask-contents').css('top',$(window).height()*0.3+$(document).scrollTop());
			$(window).on('scroll',aipSettingLoadScroll);
		}
		var state = $(this).attr('data-state');
		if(state == 0){
			var planId = getUrlParam('planId');
			//修改定投状态
		    $.post(base_root + '/front/tradeStep/updateAipState.do?d='+new Date().getTime(),{planId:planId,state:state});
		}
	});
	numCheck('#txtAipAmount');
    /**
     * AIP设置
     */
	$("#aipChange").on("click",function(){
		$('.proposal-mask-contents').css('top',$(window).height()*0.3+$(document).scrollTop());
		$(window).on('scroll',aipSettingLoadScroll);
		$('.proposal-mask-wrap').show();
		$('.proposal-mask-contents').show();
	});
	function aipSettingLoadScroll(){
		$('.laydate_box').css('top',$(window).height()*0.3+$(document).scrollTop()+156.58);
		$('.proposal-mask-contents').css('top',$(window).height()*0.3+$(document).scrollTop());
	}
	$(".Small_cake_ico, #pipeline-search-close").on("click",function(){
		$("#order-plan-aip").removeClass("ifa-space-active");
	});
	$('.proposal-mask-button-cancel, #proposal-mask-close').click(function(){
		$('.proposal-mask-wrap').hide();
		$('.proposal-mask-contents').hide();
	});
	/**
	 * 扣款周期选择
	 */
	$('.li-aip-exec-cycle p:gt(0)').on('click',function(){
		var execCycle = $(this).data('day');
		var timeDistance = new Date().getDate();
		var chargeDay = [props['create.portfolio.step.three.mon']
		,props['create.portfolio.step.three.tue']
		,props['create.portfolio.step.three.wed']
		,props['create.portfolio.step.three.thu']
		,props['create.portfolio.step.three.fri']];
		var dayHTML = '' ;
		if('m' != execCycle){
			timeDistance = 2;
			$.each(chargeDay,function(i,n){
				dayHTML +='<option value="'+(i+2)+'">'+ n +'</option>';
			});
		}else{
			chargeDay = [];
			for (var int = 1; int < 32; int++) {
				chargeDay.push(int);
			}
			$.each(chargeDay,function(i,n){
				dayHTML +='<option value="'+n+'">'+ n +'</option>';
			});
		}
		$('#selChargeDay').empty().append(dayHTML);
		//默认扣款日
		var nextChargeDate = getNextCycleTime(new Date(),execCycle,timeDistance);
		$('.aip-detail-next-charge-date').text(nextChargeDate);
	});
	/**
	 * 扣款日选择
	 */
	$('#selChargeDay').change(chargeDay);
	function chargeDay(){
		var execCycle = $('#aipExecCycle').find('.order-cycle-active').data('day');
		var timeDistance = $('#selChargeDay').val();
		var nextChargeDate = getNextCycleTime(new Date(),execCycle,timeDistance);
		$('#txtNextChargeDate').text(nextChargeDate);
	}
    $(".aip-exec-cycle").on("click",function(){
    	$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
    });
    $(".order-setting-end").on("click",function(){
		$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
		$(".order-choice-list .order-choice-date").hide().eq($(this).index()-1).show();
	});
	$(".Small_cake_ico, #pipeline-search-close").on("click",function(){
		$("#order-plan-fund-switch").removeClass("ifa-space-active");
	}); 
	$('#btnSaveAip').click(saveAip);
	function saveAip(){
		var validFlag = true;
		var aipExecCycle = '';
		var aipExecCycleText = '';
		$('.li-aip-exec-cycle p:gt(0)').each(function(){
			if($(this).hasClass('order-cycle-active')){
				validFlag = false;
				aipExecCycle = $(this).data('day');
				aipExecCycleText = $(this).find('span').text();
				return false;
			}
		});
		if(validFlag){layer.msg(props['order.plan.alert.aip.exec.cycle.choose']);return;}  // 请选择定投周期
		if(!$('#txtAipAmount').val() || Number($('#txtAipAmount').val())==0){layer.msg(props['order.plan.alert.aip.amount.empty']);return;} // 定投金额不能为空或者0
		if(!$('#txtAipEndDate').val() && !$('#order-number-choice').val() && (!$('#order-money-choice').val() || $('#order-money-choice').val() == '-')){
			layer.msg(props['order.plan.alert.aip.stop.condition.empty']);return;  // 定投停止条件不能为空
		}
		//aip
    	var aipInitTime = $('#txtNextChargeDate').text();
    	var chargeDay = $('#selChargeDay').val();
    	var aipAmount = $('#txtAipAmount').val();
    	aipAmount = aipAmount.replace(/,/gm,'');
    	var aipEndType = $('.aip-end-type').find('.order-cycle-active').data('value');
    	var aipEndDate = $('#txtAipEndDate').val();
    	var aipEndCount = $('#order-number-choice').val();
    	var aipEndTotalAmount = $('#order-money-choice').val();
    	var url = base_root+'/front/transaction/saveAip.do?r=' + new Date().getTime();
    	var data = {
    			planId:getUrlParam('planId'),
    			aipExecCycle:aipExecCycle,
    			aipInitTime:aipInitTime,
    			chargeDay:chargeDay,
    			aipAmount:aipAmount,
    			aipEndType:aipEndType+'',
    			aipEndDate:aipEndDate,
    			aipEndCount:aipEndCount,
    			aipEndTotalAmount:aipEndTotalAmount
    		};
    	$.ajax({
    		type:'post',
    		url:url,
    		data:data,
    		success:function(result){
    			if(result.flag){
    				layer.msg(props['global.success.save']);
    				$('.proposal-mask-wrap').hide();
    				$('.proposal-mask-contents').hide();
    				$('#aipState').addClass("aipstate_button_active");
    				$('#aipState').attr('data-state',1);
    				$('#aipChange').show();
    				$('#txtTotalInvestmentAmt').val(formatCurrency(aipAmount));
    				totalAmtAdjust();
    				inputCtrl(true);
    				$('.order-plan-aip-detail').show();
    				$('.detail-aip-amount').text(formatCurrency(aipAmount));
    				$('.aip-detail-exec-cycle-text').text(aipExecCycleText);
    				$('.aip-detail-time-distance').text($('#selChargeDay').find("option:selected").text());
    				$('.aip-detail-next-charge-date').text(aipInitTime);
    				if('1' == aipEndType){
    					var expirationEndDate = props['order.plan.aip.detail.expiration.1'];
    					expirationEndDate = expirationEndDate.replace('{0}',aipEndDate);
    					$('.aip-detail-expiration-text').text(expirationEndDate);
    				}else if('2' == aipEndType){
    					var expirationEndCount = props['order.plan.aip.detail.expiration.2'];
    					expirationEndCount = expirationEndCount.replace('{0}',aipEndCount);
    					$('.aip-detail-expiration-text').text(expirationEndCount);
    				}else if('3' == aipEndType){
    					var expirationEndAmount = props['order.plan.aip.detail.expiration.3'];
    					var currencyName = $('#portfolio-currency').val();
    					expirationEndAmount = expirationEndAmount.replace('{0}',aipEndTotalAmount).replace('{1}',currencyName);
    					$('.aip-detail-expiration-text').text(expirationEndAmount);
    				}
    			}else{
    				inputCtrl(false);
    				layer.msg(props['global.failed.save']);
    				$('.proposal-mask-wrap').hide();
    				$('.proposal-mask-contents').hide();
    			}
    		}
    	});
	}
	$(".order-number-top").on("click",function(){
		$('#txtAipEndDate').val('');
		$('#order-money-choice').val('');
		$("#order-number-choice").val(Number($("#order-number-choice").val()) + 1);
		$('#txtAipEndDate').val('');
		$('#order-money-choice').val('');
	});
	$(".order-number-bottom").on("click",function(){
		$('#txtAipEndDate').val('');
		$('#order-money-choice').val('');
		if( Number($("#order-number-choice").val()) == 0)return;
		$("#order-number-choice").val(Number($("#order-number-choice").val()) - 1);
	});
	$('#order-number-choice').on('change',function(){
		$('#txtAipEndDate').val('');
		$('#order-money-choice').val('');
	});
	$('#order-money-choice').on('change',function(){
		$('#txtAipEndDate').val('');
		$('#order-number-choice').val('');
	});
	$('#txtAipEndDate').click(function(){
		var min = laydate.now();
		var nextChargeDate = $('#txtNextChargeDate').text();
		if(nextChargeDate && nextChargeDate != '-'){
			min = nextChargeDate;
		}
		laydate({
		   istime: false,
		   min:min,
		   start:min,
		   elem: '#txtAipEndDate',
		   format: 'YYYY-MM-DD',
		   istoday: false,
		   choose: function(datas){ 
			   $('#order-number-choice').val('');
			   $('#order-money-choice').val('');
	       } 
		});
	});
	if($('#txtAipAmount').val() == '-'){
		$('#txtAipAmount').val(getTableTotalAmount());
	}
	$('#txtAipAmount').on('blur',function(){
		var tableTotalAmount = getTableTotalAmount();
		tableTotalAmount = tableTotalAmount.replace(/,/g,'');
		var aipAmount = $('#txtAipAmount').val().replace(/,/g,'');
	});
	$('#order-money-choice').on('blur',function(){
		var cashAvailable = $('.cash-available-value').text().replace(/,/g,'');
		var aipTotalAmount = $('#order-money-choice').val().replace(/,/g,'');
		var aipAmount = $('#txtAipAmount').val().replace(/,/g,'');
		if(Number(aipTotalAmount) < Number(aipAmount)){
			layer.msg(props['order.plan.alert.aip.total.amount.control.Minimum']); //定投总金额不能小于每次定投金额
			$('#order-money-choice').val(formatCurrency(aipAmount));
		}else if(Number(aipTotalAmount) > Number(cashAvailable)){
			layer.msg(props['order.plan.alert.aip.total.amount.control.Maximum']); //定投总金额不能大于可用现金
			$('#order-money-choice').val(formatCurrency(cashAvailable));
		}else{
			$('#order-money-choice').val(formatCurrency(aipTotalAmount));
		}
	});
	/**
	 * 当前列表总金额
	 */
	function getTableTotalAmount(){
		var totalAmount = 0.00;
		$('.order-table-subscription-purchase-tbody').find('.rebalance-amount-input').each(function(i,n){
			var amount = $(this).val();
			if(amount){
				amount = Number(amount.replace(/,/g,''));
				totalAmount = Number(totalAmount) + amount;
			}
		});
		return formatCurrency(totalAmount);
	}
    /**
	 * 计算下个周期的某一天
	 */
	function getNextCycleTime(date,execCycle,timeDistance){
		var result = '',
		year_ = date.getFullYear(),
		month_ = date.getMonth()+1,
		day_ = date.getDay()+1,
		date_ = date.getDate();
		if('m' == execCycle){
			month_ = month_ + 1;
			date_ = timeDistance;
			if(Number(timeDistance)>getLastDay(year_,month_)){
				month_++;
			}
		}else{
			var dayNum = 7;
			if('b' == execCycle){
				dayNum = 14;
			}
			date_ = date_ - day_ +	dayNum + Number(timeDistance);
			if(Number(date_)>getLastDay(year_,month_)){
				date_ = date_ - getLastDay(year_,month_);
				month_++;
			}
		}
		if(month_.toString().length==1){month_ = '0'+month_;}
		if(date_.toString().length==1){date_ = '0'+date_;}
		result = year_ + '-' + month_ + '-' + date_;
		return result;
	}
	/**
	 * 获取当月天数
	 */
	function getLastDay(year,month){   
		var new_year = year;  
		var new_month = month++;  
		if(month>12){   
			 new_month -=12;    
			 new_year++;      
		}   
		var new_date = new Date(new_year,new_month,1);        
		return (new Date(new_date.getTime()-1000*60*60*24)).getDate();
	}
	/**
	 * input 控制
	 */
	function inputCtrl(flag){
		if(flag){
			$('.input-ctrl').addClass('input-ctrl-border');
			$('.input-ctrl').each(function(){
				if($(this).is('input')){
					$(this).attr('readonly','readonly');
				}else if($(this).is('select')){
					$(this).attr('disabled','disabled');
				}
			});
		}else{
			$('.input-ctrl').removeClass('input-ctrl-border');
			$('.input-ctrl').each(function(){
				if($(this).is('input')){
					$(this).removeAttr('readonly');
				}else if($(this).is('select')){
					$(this).removeAttr('disabled');
				}
			});
		}
	}
	/**************************************************************************************************************/
	/**
	 * 金额格式化
	 */
	function formatCurrency(num) {    
	    num = num.toString().replace(/\$|\,/g,'');    
	    if(isNaN(num))    
	    num = "";    
	    var sign = (num == (num = Math.abs(num)));    
	    num = Math.floor(num*100+0.50000000001);    
	    var cents = num%100;    
	    num = Math.floor(num/100).toString();    
	    if(cents<10)    
	    cents = "0" + cents;    
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
	    num = num.substring(0,num.length-(4*i+3))+','+    
	    num.substring(num.length-(4*i+3));    
	    return (((sign)?'':'-') + num + '.' + cents);    
	}
	/**
	 * 获取url参数
	 */
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
	/**
	 * 数字校验
	 */
	function numCheck(obj){
		$(document).find(obj).keypress(function(event) {  
			var keyCode = event.which;  
			if (keyCode == 46 || (keyCode >= 48 && keyCode <=57))  
				return true;  
			else  
				return false;  
		}).focus(function() {  
			this.style.imeMode='disabled';  
		});
	}
	/**
	 * 数据去重
	 */
	Array.prototype.unique = function(){
		var res = [];
		var json = {};
		for(var i = 0; i < this.length; i++){
			if(!json[this[i]]){
			    res.push(this[i]);
			    json[this[i]] = 1;
		    }
	    }
		return res;
	};
	/**************************************************************************************************************/
});