define(function(require) {
	var $ = require('jquery');
			require("echarts");
			require('jqueryRange');
			require('orderPlanCheckPwd');
	var angular = require("angular");require("layui");
	
	$(".client-more-ico").on("click",function(){
		$(this).closest(".order-plan-rows").toggleClass("client-more-active");
	});
	$(document).on('click', '.product-action-del', function(){
		var self = this;
		layer.confirm(props['global.alert.del'], {
			  title:props['global.info'],
			  btn: [props['global.confirm'], props['global.cancel']]
		},function(index){
			$(self).closest('tr').remove();
			layer.close(index);
			displayDataTips();
			orderTotalFee();
			orderTotalAmount();
			caluRiskLevel();
		});
	});
	function displayDataTips(){
		var productCount = $(document).find('.product-fund-info').length;
		if(productCount > 0){
			$('.order-table-subscription-purchase-tbody').find('.tr-wmes-nodata-tips').hide();
		}else{
			$('.order-table-subscription-purchase-tbody').find('.tr-wmes-nodata-tips').show();
		}
	}
	$(document).on('click', '.content_xiala,.icon_xiala', function(){
		$('.ul_xiala').hide();
		$(this).closest('.client_xiala').find('.ul_xiala').toggle();
	});	
	$(document).on('click', '.ul_xiala li', function(){
		$(this).closest('.client_xiala').find('.content_xiala').val($(this).text());
		if($(this).closest('ul').hasClass('account_ul_xiala')){
			if($(this).eq(0).attr('data-total-flag') == '0'||$(this).eq(0).attr('data-total-flag') == '1'){
				$(this).closest('.client_xiala').find('.content_xiala').attr('data-total-flag',$(this).eq(0).attr('data-total-flag'));
				var totlaFlagDisplay = $(this).eq(0).attr('data-total-flag-display');
				if($(this).closest('.product-account-div').find('.account-total-flag').length == 0){
					$(this).closest('.product-account-div').append('<p class="account-total-flag" data-total-flag="'+$(this).eq(0).attr('data-total-flag')+'" style="color: red;clear: both;text-align: -webkit-right;">'+totlaFlagDisplay+'</p>');
				}
			}else{
				$(this).closest('.client_xiala').find('.content_xiala').attr('data-total-flag','');
				$(this).closest('.product-account-div').find('.account-total-flag').remove();
			}
			$(this).closest('.client_xiala').find('.content_xiala').attr('data-account-id',$(this).attr('data-account-id'));
			$(this).closest('.client_xiala').find('.content_xiala').attr('data-authorized',$(this).attr('data-authorized'));
			$(this).closest('.client_xiala').find('.content_xiala').attr('data-rpq-level',$(this).attr('data-rpq-level'));
			orderTotalAmount();
			riskLevelTips();
		}else if($(this).closest('ul').hasClass('dividend_ul_xiala')){
			$(this).closest('.client_xiala').find('.content_xiala').attr('data-dividend',$(this).attr('data-dividend'));
		}
	});	
	function riskLevelTips(){
		$(document).find('.account_content_xiala').each(function(){
			var riskLevel = $(this).attr('data-risk-level');
			var rpqLevel = $(this).attr('data-rpq-level');
			if(riskLevel && rpqLevel && Number(riskLevel) > Number(rpqLevel)){
				$(this).closest('tr').find('.product-update-ico-rpq-level').show();
			}else{
				$(this).closest('tr').find('.product-update-ico-rpq-level').hide();
			}
		});
	}
	$(document).on('mouseleave', '.client_xiala', function(){
		$(this).find('.ul_xiala').hide();
	});	
	$(document).on('click','.plan-detail-more-wral-titleleft span', function(){
		$('#txtPortfolioName').removeAttr('readonly').css('border','1px solid #ccc');
	});	
	$(document).on('blur','#txtPortfolioName', function(){
		$('#txtPortfolioName').attr('readonly','readonly').css('border','0px');
	});	
	numeral(".order_plan_input");
	/**************************************************************************************************************/	
	/**
	 * order
	 */
	$('#btnOrder').click(function(){
		if(!validData()){
			return;
		}
		var memberType = $('#hidMemberType').val();
		var url = base_root + '/front/tradeStep/saveOrderPlan.do?d=' + new Date().getTime();
		var data = getOrderPlanData();
		$.ajax({
			type:'post',
			url:url,
			data:data,
			success:function(re){
				var planId = re.planId; 
				window.history.pushState(null, document.title, base_root + '/front/tradeStep/orderPlanSelectProduct.do?agreeRD=1&planId=' + planId);
				if(re.flag){
					layer.msg(props['global.success.save'], {icon:2,time:800});
					setTimeout(function(){
						if(1 == re.memberType){
							layer.confirm(props['order.plan.commission.alert.if.push.IFA.confirmation'], {
								title:props['global.info'],
								btn: [props['global.true'],props['global.false']],
								cancel:function(){
						        	window.location.href = window.location.href;
						        }
							},function(index){
								layer.close(index);
								sendOrderPlanCheck('1', planId);
							},function(index){
								layer.close(index);
								sendOrderPlanCheck('0', planId);
							});
						}else if(2 == re.memberType){
							getMySupervisor();
						}
					},1000);
				}else{
					window.location.href = window.location.href;
				}
			}
		});
	});
	/**
	 * get order data
	 */
	function getOrderPlanData(){
		var planId = getUrlParam('planId'),
			currencyCode = $('#portfolio-currency').attr('data-code'),
			portfolioName = $('#txtPortfolioName').val(),
			productData = '';
		var riskLevel = $('#risk-level-average').text();
		if(riskLevel){
			riskLevel = Number(riskLevel.trim());
			if(isNaN(riskLevel)){
				riskLevel = '';
			}
		}
		var products = [];
		$('.product-fund-info').each(function(){
			var productMap = {};
			var fundId = $(this).attr('id');
			var amount = $(this).find('.product_sub_amount').val();
			amount = Number(amount.replace(/,/g,''));
			var tranRate = $(this).find('.product_transaction_rate').val();
			var tranfee = $(this).find('.product_transaction_fee').val();
			if(tranfee){
				tranfee = Number(tranfee.replace(/,/g,''));
			}
			var weight = $(this).find('.strategy_chart_tinput').val();
			var accountId = $(this).find('.account_content_xiala').attr('data-account-id');
			var dividend = $(this).find('.dividend_content_xiala').attr('data-dividend');
			productMap['fundId'] = fundId, 
			productMap['amount'] = amount + '',
			productMap['tranRate'] = tranRate + '',
			productMap['tranFee'] = tranfee + '',
			productMap['weight'] = weight + '', 
			productMap['accountId'] = accountId, 
			productMap['dividend'] = dividend;
			products.push(productMap);
		});	
		var data = {
			planId:planId,
			currencyCode:currencyCode,
			riskLevel:riskLevel,
			portfolioName:encodeURI(portfolioName),
			productData:encodeURI(JSON.stringify(products))
		};
		return data;
	}
	/**
	 * valify
	 */
	function validData(){
		var flag = true;
		if($('.ifa-check-hold-account').length == 0){
			layer.msg(props['order.plan.rebalance.alert.empty.account'],{icon:3});
			flag = false;
			return flag;
		}
		var portfolioName = $('#txtPortfolioName').val();
		if(!portfolioName || portfolioName.length == 0){
			layer.msg(props['order.plan.rebalance.alert.empty.portfolioName'],{icon:3});
			flag = false;
			return flag;
		}
		var totalAmount = $('#investorAmount').val();
		if(!totalAmount || totalAmount.length == 0){
			layer.msg(props['order.plan.rebalance.alert.empty.totalInvAmount'],{icon:3});
			flag = false;
			return flag;
		}else{
			totalAmount = Number(totalAmount.replace(/,/g,''));
			if(totalAmount <= 0){
				layer.msg(props['order.plan.rebalance.alert.input.totalInvAmount.incorrect'],{icon:3});
				flag = false;
				return flag;
			}
		}
		var totalTransactionAmount = $('.total_transaction_amount').text();
		var cashAvailable = $('.cash_available').text();
		if(totalTransactionAmount && cashAvailable){
			totalTransactionAmount = Number(totalTransactionAmount.replace(/,/g,''));
			cashAvailable = Number(cashAvailable.replace(/,/g,''));
			if(totalTransactionAmount > cashAvailable){
				layer.msg(props['order.plan.rebalance.alert.totalTransactionAmount.limit'],{icon:3});
				flag = false;
				return flag;
			}
			if(cashAvailable == 0){
				layer.msg(props['order.plan.alert.available.cash.not.enough'],{icon:3});
				flag = false;
				return flag;
			}
		}
		var validWeightFlag = true;
		var totalWeight = 0;
		$('.strategy_chart_tinput').each(function(){
			var weight = Number($(this).val());
			if(weight == 0){
				validWeightFlag = false;
				return false;
			}
			totalWeight += weight;
		});
		if(!validWeightFlag){
			layer.msg(props['order.plan.rebalance.alert.weight.incorrect'],{icon:3});
			flag = false;
			return flag;
		}
		if(totalWeight != 100){
			layer.msg(props['order.plan.rebalance.alert.totalWeight.limit'],{icon:3});
			flag = false;
			return flag;
		}
		var validSubAmountFlag = true;
		$('.product_sub_amount').each(function(){
			var subAmount = $(this).val();
			subAmount = Number(subAmount.replace(/,/g,''));
			if(subAmount == 0){
				validSubAmountFlag = false;
				return false;
			}
			var minSubscribeAmount = $(this).closest('tr').find('.min_subscribe_amount').text();
			if(minSubscribeAmount){
				minSubscribeAmount = Number(minSubscribeAmount.replace(/,/g,''));
				if(subAmount < minSubscribeAmount){
					validSubAmountFlag = false;
					return false;
				}
			}
		});
		if(!validSubAmountFlag){
			layer.msg(props['order.plan.commission.alert.product.initial.amount.ctrl'],{icon:3});
			flag = false;
			return flag;
		}
		var validTransactionAmountFlag = true;
		$('.product_transaction_fee').each(function(){
			var transactionAmount = $(this).val();
			transactionAmount = Number(transactionAmount.replace(/,/g,''));
			var minAmountFee = $(this).closest('tr').find('.min_amount_fee').text();
			if(minAmountFee){
				minAmountFee = Number(minAmountFee.replace(/,/g,''));
				if(transactionAmount < minAmountFee){
					validTransactionAmountFlag = false;
					return false;
				}
			}
		});
		if(!validTransactionAmountFlag){
			layer.msg(props['order.plan.commission.alert.tran.fee.ctrl'],{icon:3});
			flag = false;
			return flag;
		}
		var authorizedYes = {};
		authorizedYes['flag'] = false;
		var authorizedNo = {};
		authorizedNo['flag'] = false;
		$('.product-fund-info').each(function(){
			var authorizedMap = {};
			var authorized = $(this).find('.account_content_xiala').attr('data-authorized');
			if(authorized == '1'){
				authorizedYes['flag'] = true;
			}else{
				authorizedNo['flag'] = true;
			}
		});	
		if(authorizedYes.flag && authorizedNo.flag){
			layer.msg(props['order.plan.commission.alert.trading.account.authorized.ctrl'],{icon:3});
			return;
		}
		return flag;
	}
	/**
	 * add
	 */
	var orderPlanAngular = angular.module('orderPlan', ['truncate']);
	orderPlanAngular.controller('orderPlanCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.planProducts = [];
		$(document).on('click', '#popupWinReturn', function(){
	    	var ids = $('#ids').val();
	    	var fundExists = '';
	    	$('.order-table-subscription-purchase').find('.product-fund-info').each(function(){
	    		var fundId = $(this).attr('id');
	    		var fundName = $(this).data('fundName');
	    		if(ids.indexOf(fundId) > -1){
	    			fundExists += '【' + fundName + '】';
	    			var temp = ids.split(fundId);
	    			if(temp[0] != '' && temp[0].charAt(temp[0].length-1) == ','){
	    				temp[0] = temp[0].substring(0, temp[0].length-1);
	    			}
	    			if(temp[1] != '' && temp[1].charAt(0) == ','){
	    				temp[1] = temp[1].substring(1, temp[1].length);
	    			}
	    			ids =  temp[0] + temp[1];
	    		}
	    	});
	    	if(fundExists != ''){
	    		layer.msg(fundExists + props['order.plan.rebalance.alert.alreadyExists'], {icon:3});
	    	}
	    	var currencyCode = $('#portfolio-currency').attr('data-code');
			var url = base_root + '/front/tradeStep/getProductVOByIds.do?d=' + new Date().getTime();
			$http({
				url : url,
				method :'POST',
				params : {
					currencyCode:currencyCode,
					ids:ids
				}
			}).success(function(re){
				$scope.memberType = re.memberType;
				if(re.flag && re.planProducts != null){
					var fundNames = '';
					$.each(re.planProducts,function(i,n){
						if(n.tradable!='Y' || n.status!='1'){
							fundNames += '【' + n.fundName + '】';
						}
						$scope.planProducts.push(n);
					});
					if(fundNames != ''){
						layer.msg(fundNames + props['order.plan.alert.product.can.not.be.traded'],{icon:3});
					}
					setTimeout(initJRange,500);
					initSelect();
				}
				setTimeout(function(){
					displayDataTips();
					orderTotalFee();
					orderTotalAmount();
					caluRiskLevel();
					riskLevelTips();
				}, 500);
			});
		});
		$(document).on('click','#btnReset',function(){
			var currencyCode = $('#portfolio-currency').attr('data-code');
			var planId = getUrlParam('planId');
			if(planId != null){
				$scope.$apply(function(){
					var url = base_root + '/front/tradeStep/getInitProduct.do?d=' + new Date().getTime();
					$http({
						url : url,
						method :'POST',
						params : {
							currencyCode:currencyCode,
							id:planId
						}
					}).success(function(re){
						if(re.flag && re.planProductVOs != null){
							$scope.planProducts = [];
							$('.freemarke-product-fund-info').remove();
							$.each(re.planProductVOs,function(i,n){
								$scope.planProducts.push(n);
							});
							setTimeout(initJRange,500);
							initSelect();
						}else{
							layer.msg(props['order.plan.alert.reset.fail'],{icon:3});
						}
						setTimeout(function(){
							displayDataTips();
							orderTotalFee();
							orderTotalAmount();
							caluRiskLevel();
							riskLevelTips();
						}, 500);
					});
				});
			}
		});
	}]);
	setTimeout(function(){
		$('#popupWinReturn').click();
	}, 500);
	
	/**
	 * Total
	 */
	function orderTotalAmount(){
		var totalAmount = 0;
		$('.product_sub_amount').each(function(i, n){
			var subAmount = $(n).val();
			if(subAmount){
				subAmount = subAmount.replace(/,/g,'');
			}
			totalAmount += Number(subAmount);
			var transactionAmount = Number(subAmount);
			var rate = $(n).closest('tr').find('.product_transaction_rate').val();
			var fee = 0;
			if(rate){
				fee = subAmount*Number(rate)/100;
				$(n).closest('tr').find('.product_transaction_fee').val(formatCurrency(fee));
			}
			var transactionFee = $(n).closest('tr').find('.product_transaction_fee').val();
			var accountTotalFlag = $(n).closest('tr').find('.content_xiala').attr('data-total-flag');
			if(transactionFee && accountTotalFlag == '0'){
				transactionFee = transactionFee.replace(/,/g,'');
				transactionAmount += Number(transactionFee);
			}
			$(n).closest('tr').find('.product_transaction_amount').text(formatCurrency(transactionAmount));
		});
		$('#investorAmount').val(formatCurrency(totalAmount));
		var totalTransactionAmount = 0;
		$('.product_transaction_amount').each(function(i,n){
			var transactionAmount = $(n).text();
			if(transactionAmount){
				transactionAmount = transactionAmount.trim().replace(/,/g,'');
			}
			totalTransactionAmount += Number(transactionAmount);
		});
		$('.total_transaction_amount').text(formatCurrency(totalTransactionAmount));
		orderTotalFee();
	}
	setTimeout(orderTotalAmount, 500);
	$(document).on('change','.product_sub_amount',function(){
		var self = this;
		var value = $(this).val();
		var curTotal = 0;
		$('.product_sub_amount').each(function(i,n){
			var val = $(n).val();
			if(val){
				val = Number(val.replace(/,/g,''));
				curTotal += val;
			}
		});
		var totalAmount = $(document).find('#investorAmount').val();
		totalAmount = Number(totalAmount.replace(/,/g,''));
		if(curTotal > totalAmount){
			layer.confirm(props['order.plan.tips.cur.total.amount.exceeds.total.investment.amount'], {
				title:props['global.info'],
				btn: [props['global.true'],props['global.false']],
			}, function(index){
				layer.close(index);
				$(self).val(formatCurrency(value));
				orderTotalAmount();
				$('.product_sub_amount').each(function(i,n){
					var val = $(n).val();
					if(val){
						val = Number(val.replace(/,/g,''));
					}
					var totalAmount = $(document).find('#investorAmount').val();
					totalAmount = Number(totalAmount.replace(/,/g,''));
					if(totalAmount){
						var weight = parseInt(val/totalAmount*100);
						$(n).closest('tr').find('.strategy_chart_tinput').val(weight);
						$(n).closest('tr').find('.funds-single-slider').jRange('setValue', weight + ',' + weight);
					}
				});
			}, function(index){
				layer.close(index);
				investorAmountChange($('#investorAmount'));
				return;
			});
		}else{
			$(this).val(formatCurrency(value));
			orderTotalAmount();
			
			$('.product_sub_amount').each(function(i,n){
				var val = $(n).val();
				if(val){
					val = Number(val.replace(/,/g,''));
				}
				var totalAmount = $(document).find('#investorAmount').val();
				totalAmount = Number(totalAmount.replace(/,/g,''));
				if(totalAmount){
					var weight = parseInt(val/totalAmount*100);
					$(n).closest('tr').find('.strategy_chart_tinput').val(weight);
					$(n).closest('tr').find('.funds-single-slider').jRange('setValue', weight + ',' + weight);
				}
			});
		}
	});
	$(document).on('change','#investorAmount',function(){
		investorAmountChange(this);
	});
	function investorAmountChange(self){
		var totalAmount = $(self).val();
		if(totalAmount){
			totalAmount = totalAmount.replace(/,/g,'');
		}
		totalAmount = Number(totalAmount);
		$('.product_sub_amount').each(function(i, n){
			var weight = $(this).closest('tr').find('.strategy_chart_tinput').val();
			weight = Number(weight)/100;
			var amount = totalAmount*weight;
			$(this).val(formatCurrency(amount));
			var rate = $(this).closest('tr').find('.product_transaction_rate').val();
			var fee = 0;
			if(rate){
				fee = amount*Number(rate)/100;
				$(this).closest('tr').find('.product_transaction_fee').val(formatCurrency(fee));
			}
			var transactionAmount = amount;
			var accountTotalFlag = $(this).closest('tr').find('.content_xiala').attr('data-total-flag');
			if(accountTotalFlag == '0'){
				transactionAmount += fee;
			}
			$(this).closest('tr').find('.product_transaction_amount').text(formatCurrency(transactionAmount));
		});
		$(self).val(formatCurrency(totalAmount));
		orderTotalFee();
		totalTransactionAmount();
	}
	function orderTotalFee(){
		var totalFee = 0;
		$('.product_transaction_fee').each(function(i, n){
			var fee = $(n).val();
			if(fee){
				fee = fee.replace(/,/g,'');
			}
			totalFee += Number(fee);
		});
		$('.total_transaction_fee').text(formatCurrency(totalFee));
	}
	setTimeout(orderTotalFee, 500);
	$(document).on('change','.product_transaction_fee',function(){
		$(this).val(formatCurrency($(this).val()));
		orderTotalFee();
	});
	function totalTransactionAmount(){
		var totalTransactionAmount = 0;
		$('.product_transaction_amount').each(function(i,n){
			var transactionAmount = $(n).text();
			if(transactionAmount){
				transactionAmount = transactionAmount.trim().replace(/,/g,'');
			}
			totalTransactionAmount += Number(transactionAmount);
		});
		$('.total_transaction_amount').text(formatCurrency(totalTransactionAmount));
	}
	$(document).on('change','.product_transaction_rate',function(){
		var rate = $(this).val();
		var subAmount = $(this).closest('tr').find('.product_sub_amount').val();
		if(subAmount){
			subAmount = subAmount.replace(/,/g,'');
			var fee = 0;
			if(Number(subAmount) > 0){
				fee = Number(rate)*Number(subAmount)/100;
				$(this).closest('tr').find('.product_transaction_fee').val(fee);
			}else{
				$(this).closest('tr').find('.product_transaction_fee').val('0.00');
			}
			var transactionAmount = Number(subAmount);
			var accountTotalFlag = $(this).closest('tr').find('.content_xiala').attr('data-total-flag');
			if(accountTotalFlag == '0'){
				transactionAmount += fee;
			}
			$(this).closest('tr').find('.product_transaction_amount').text(formatCurrency(transactionAmount));
		}else{
			$(this).closest('tr').find('.product_transaction_fee').val('0.00');
			$(this).closest('tr').find('.product_transaction_amount').text('0.00');
		}
		orderTotalFee();
		totalTransactionAmount();
	});
	
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
		sendOrderPlanCheck('', getUrlParam('planId'));
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
						var creatorId = re.creatorId;
						var isSupervisor = false;
						$.each(myTeamListVOs,function(i,n){
							if(creatorId != null && creatorId == n.memberId){
								isSupervisor = true;
								return false;
							}
							html += '<li class="regiter_xiala_li_supervisor" value="'+n.memberId+'">'+n.nickname+'</li>';
						});
						if(isSupervisor){
							sendOrderPlanCheck('', getUrlParam('planId'));
							return;
						}
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
						$('.fund-space-mask-wrap').css('height',$(document).height());
						$('.fund-space-mask-wrap,.fund_float_menu_addbox').show();
					}
				}else{
					layer.msg(props['order.plan.commission.dialog.select.supervisor.alert.no.found.supervisor'],{icon:3});
				}
			},
			error:function(){
				layer.msg(props['order.plan.commission.dialog.select.supervisor.alert.no.found.supervisor'],{icon:3});
			}
		});
	}
	/**
	 * OrderPlan Check
	 */
	function sendOrderPlanCheck(confirmation, planId){
		// supervisor
		var supervisor = $('.value_show').attr('data-member-id');
		var url = base_root + '/front/tradeStep/sendOrderPlanCheck.do?d='+new Date().getTime();
		$.ajax({
			type:'post',
			async:false,
			url:url,
			data:{
				planId:planId,	
				supervisor:supervisor,
				confirmation:confirmation
			},
			success:function(result){
				if(result.flag){
					if(result.memberType != null 
							&& result.memberType == 1){
					// investor
						if(result.confirmation != '1'){
							var map = {};
							map['planId'] = result.planId;
							$(this).InitCheckPwd(map);
						}else{
							setTimeout(function(){
								window.location.href = base_root + '/front/tradeStep/orderPlanComplete.do?planId='+ result.planId;
							},1000);
						}
					}else if(result.memberType != null && result.memberType == 2){
					// IFA
						setTimeout(function(){
							window.location.href = base_root + '/front/tradeStep/orderPlanComplete.do?planId='+ result.planId;
						},1000);
					}
				}
			}
		});
	}
	/**************************************************************************************************************/	
	/**
     * quick add 事件
     */
    $("#btnAddFund").on("click",function(){
    	quickAddFund();
    });
    function quickAddFund(){
    	$('#ids').val('');
    	var url = base_root+"/front/fund/info/fundSelectorForAllocation.do";
    	openResWin(url, 1015, 700, "fundSelector");
    }
	/**
	 * init JRange
	 */
	function initJRange(){
		$(document).find('.funds-single-slider').each(function(i, n){
			var self = $(this);
			self.jRange({
				from: 0, 
				to: 100,
				step: 1, 
				format: '%s%', 
				showLabels: false, 
				showScale: false,
				isRange: true, 
				ondragstart:function(){
					self.closest('.portfolio-slider').find('.strategy_chart_tinput')
					var totalValue = 0;
					$('.strategy_chart_tinput').each(function(){
						totalValue = totalValue + Number($(this).val());
					});
					var value = 100 - totalValue;
					var maxValue = (self.val().split(','))[1];
					maxValue = Number(maxValue) + value;
					self.jRange('setValue', (self.val().split(','))[0] + ',' + maxValue);
				}, 
				onstatechange:function(){
					var weight = (self.val().split(','))[0];
					self.closest('.portfolio-slider').find('.strategy_chart_tinput').val(weight);
					var totalAmount = $(document).find('#investorAmount').val();
					if(totalAmount){
						totalAmount = totalAmount.replace(/,/g,'');
					}
					var amount = Number(totalAmount)*Number(weight)/100;
					self.closest('tr').find('.product_sub_amount').val(formatCurrency(amount));
					var rate = self.closest('tr').find('.product_transaction_rate').val();
					var fee = 0;
					if(rate){
						fee = amount*Number(rate)/100;
					}
					self.closest('tr').find('.product_transaction_fee').val(formatCurrency(fee));
					var transactionAmount = amount;
					var accountTotalFlag = self.closest('tr').find('.content_xiala').attr('data-total-flag');
					if(accountTotalFlag == '0'){
						transactionAmount += fee;
					}
					self.closest('tr').find('.product_transaction_amount').text(formatCurrency(transactionAmount));
					orderTotalFee();
					totalTransactionAmount();
					caluRiskLevel();
				}, 
				ondragend:function(){
					self.val((self.val().split(','))[0] + ',' + (self.val().split(','))[0]);
				}
			});
		});
	}
	setTimeout(initJRange,500);
	/**
	 * init Select
	 */
	function initSelect(){
		setTimeout(function(){
			$(document).find('.ul_xiala').each(function(){
				var value = $(this).closest('.client_xiala').find('.content_xiala').val();
				var totalFlag = $(this).closest('.client_xiala').find('.content_xiala').attr('data-total-flag');
				if('' == value){
					$(this).find('li').eq(0).click();
				}
				if((totalFlag == '1' || totalFlag == '0' ) && $(this).closest('.product-account-div').find('.account-total-flag').length == 0){
					var totalFlagDisplay = $(this).closest('.client_xiala').find('.content_xiala').attr('data-total-flag-display');
					$(this).closest('.product-account-div').append('<p class="account-total-flag" data-total-flag="' + totalFlag + '" style="color: red;clear: both;text-align: -webkit-right;">'+totalFlagDisplay+'</p>');
				}
			});
		},500);
	}
	initSelect();
	/**
	 * 风险计算
	 */
	function caluRiskLevel(){
		var riskLevels = [];
		var average = 0;
		$('.product-risk-level').each(function(i,n){
			var riskLevel = $(this).text();
			riskLevel = parseInt(riskLevel);
			riskLevels[i] = riskLevel;
			var weight = $(this).closest('tr').find('.strategy_chart_tinput').val();
			weight =  parseFloat(weight/100);
			average = average + riskLevel*weight;
		});
		riskLevels.sort(function(a,b){
			return b-a;
		});
		if(riskLevels.length < 1){
			$('#risk-level-max').text('N/A');
			$('#risk-level-average').text('N/A');
			return;
		}
		if(riskLevels.length > 0 && typeof(riskLevels[0]) != 'undefined' && riskLevels[0] != 'NaN'){
			$('#risk-level-max').text(riskLevels[0]);
		}else{
			$('#risk-level-max').text('N/A');
		}
		var fundCount = $('.product-risk-level').length;
		if(typeof(average) != 'undefiend' && average!=0 && fundCount!=0){
			$('#risk-level-average').text(Math.floor(average));
		}else{
			$('#risk-level-average').text('N/A');
		}
	};
	setTimeout(caluRiskLevel,500);
	/******************************************************************* 货币转换 *******************************************/	
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
									args = parseFloat(Number(n)*rate).toFixed(0);
								}else{
									args = parseFloat(Number(n)*rate).toFixed(2);
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
	/******************************************************** ECharts 图表部分 ******************************************************/
	$('#btnAnalysis').click(function(){
		$('.analysis-chart-content').css('margin-top',$(window).scrollTop()+20);
		$('.client-portfolio-new-mask').show();
		refashData();
	});
	$('.wmes-close').click(function(){
		$('.client-portfolio-new-mask').hide();
	});
	$(".builder-tab li").on("click",function(){
		$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
		$(".builder-main-contents > div").hide().eq($(this).index()).show();
	});
	 var eChartsData = (function(){
	    	var period = '1Y';
	    	return {period:period};
	    })();
	    function getOption(nameData, xAxisData, minVal, maxVal){
			var option = {
				tooltip: {
			        trigger: 'axis',
			        formatter : function(a){
			        	var val = a[0].name + '<br/>';
			        	$.each(a, function(i,n){
			        		val += '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + n.color + '"></span>' + n.seriesName + ' : ' + n.value + ' %<br/>';
			        	})
			            return val;
			        },
			        position: function (pt) {
			            return [pt[0], '10%'];
			        }
			    },
			    yAxis: {
			    	type : 'value',
			    	min:minVal,
			    	max:maxVal,
		            axisLabel : {
		                formatter: '{value} %'
		            },
		            splitNumber:10
			    },
			    grid:{
	        		y2:90
	    		},
			    dataZoom: [{
			        type: 'inside',
			        start: 0,
			        end: 100,
			        bottom:35
			    }, {
			        start: 0,
			        end: 10,
			        handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
			        handleSize: '80%',
			        handleStyle: {
			            color: '#fff',
			            shadowBlur: 3,
			            shadowColor: 'rgba(0, 0, 0, 0.6)',
			            shadowOffsetX: 2,
			            shadowOffsetY: 2
			        }, 
			        bottom:35
			    }],
				legend: {
					data: nameData,
					align:'left',
					orient :'horizontal',
					left : 50,
					bottom : 5
				},
				xAxis: {
					type: 'category',
					boundaryGap: false,
					data: xAxisData
				},
				series: []
			};
			return option;
		}
	    function getSeries(option, name, color, serie){
			var series = {};
			series['name'] = name;
			series['type'] = 'line';
			series['smooth'] = true;
			series['sampling'] = 'average';
			series['itemStyle'] = {
				normal: {
					color: color
				}
			};
			series['data'] = serie;
			option['series'].push(series);
			return option;
		}
	    function getPieOption(legendData, seriesData){
	    	var option ={  
				tooltip : {
					trigger: 'item',
					formatter: "{b}"
				},
	        	legend: {
					orient: 'vertical',
					x: '28%',
					data: legendData,
					formatter: '{name}'
				},
				series : [{
	        	   name: 'weight',
	        	   type: 'pie',
	        	   radius : '60%',
	        	   center: ['18%', '33%'],
	        	   label: {
		                normal: {
		                    position: 'inner',
		                    formatter : "{d}%",
			                show:false    
		                }
		            },
	        	   data:seriesData
	            }]
	        };
			return option;
		}
	    /**
	     * 总收益图表
	     */
		$(".builder-chart-wrap").width();
		function aggregate(){
			$('.allocation-chart-title-0-emtpy').hide();
			$("#builder-chart-one").width($(".proposal-step-wrap").width());
			var myChart = echarts.init(document.getElementById('builder-chart-one'));
	  		myChart.showLoading();
	  		getIncomePercentageTotal(myChart);
	    }	
		function getIncomePercentageTotal(myChart){
			var xAxisData = [];
			var aggregate = {
				nameData:[],
				totalDataSeries:[],
				aipDataSeries:[]
			};
			var fundIds = getFundIds();
			var weights = getWeights();
			var period = eChartsData.period;
			var url = base_root+'/front/tradeChart/getFundsReturnData.do?dateStr=' + new Date().getTime();
			$.ajax({
				type:'post',
				url:url,
				data:{
					fundIds:fundIds,
					weights:weights,
					period:period
				},
				success:function(result){//console.log(result);
					if(result.flag && result.chartsData.returnRates != null && result.chartsData.returnRates.length > 0){
						xAxisData = result.chartsData.marketDates;
						aggregate.nameData.push(props['create.portfolio.step.three.incomePercentageTotal']);
						aggregate.totalDataSeries = result.chartsData.returnRates;
					}
					if(aggregate.nameData.length > 0){
						myChart.hideLoading();
						var minVal = aggregate.totalDataSeries.min();
						var maxVal = aggregate.totalDataSeries.max();
						if(!isNaN(minVal)){
							minVal = Math.floor(minVal*1.2);
						}
						if(!isNaN(maxVal)){
							maxVal = Math.ceil(maxVal*1.2);
						}
						var option = getOption(aggregate.nameData, xAxisData, minVal, maxVal);
						if(aggregate.totalDataSeries.length > 0){
							option = getSeries(option, props['create.portfolio.step.three.incomePercentageTotal'], 'rgba(255, 37, 37, 0.8)', aggregate.totalDataSeries);
						}
						if(aggregate.aipDataSeries.length > 0){
							option = getSeries(option, props['create.portfolio.step.three.AIPIncomePercentageTotal'], 'rgba(35, 180, 90, 0.8)', aggregate.aipDataSeries);
						}
						myChart.setOption(option);
					}else{
						myChart.hideLoading(); 
						$('.allocation-chart-title-0-emtpy').show();
					}
				},error:function(){
					myChart.hideLoading(); 
					$('.allocation-chart-title-0-emtpy').show();
				}
			});
		}
		/**
		 * 基金收益图表
		 */
	    function separateness(){
	    	$('.allocation-chart-title-0-emtpy').hide();
			$("#builder-chart-one").width($(".proposal-step-wrap").width());
			var myChart = echarts.init(document.getElementById('builder-chart-one'));
			myChart.showLoading();
	  		getIncomePercentage(myChart);
	    }
	    function getIncomePercentage(myChart){
	    	var xAxisData = [];
			var separateness = {
					nameData:[],
					series:[]
			};
			var fundIds = getFundIds();
			var period = eChartsData.period;
			var url = base_root+'/front/tradeChart/getSingleFundReturnData.do?dateStr=' + new Date().getTime();
			$.ajax({
				type:'post',
				url:url,
				data:{
					fundIds:fundIds,
					period:period
				},
				success:function(result){
					var minVal = 0;
					var maxVal = 0;
					if(result.flag){
						var fundMarketDataVOs = result.chartsDatas;
						$.each(fundMarketDataVOs,function(i,n){
							if(n.fundId != null && n.returnRates != null && n.returnRates.length > 0){
								var separatenessSeries = {};
								separatenessSeries['name'] = n.fundName; 
								separatenessSeries['type'] = 'line';
								separatenessSeries['smooth'] = true;
								separatenessSeries['sampling'] = 'average';
								separateness.nameData.push(n.fundName);
								xAxisData = n.marketDates;
								separatenessSeries['data'] = n.returnRates;
								separateness.series.push(separatenessSeries);
								if(n.returnRates.min() < minVal){
									minVal = n.returnRates.min();
									minVal = Math.floor(Number(minVal)*1.2);
								}
								if(n.returnRates.max() > maxVal){
									maxVal = n.returnRates.max();
									maxVal = Math.ceil(Number(maxVal)*1.2);
								}
							}
						});
					}
					if(separateness.series.length>0){
						var option = getOption(separateness.nameData, xAxisData, minVal, maxVal);
						option['series'] = separateness.series;
						myChart.hideLoading();
						myChart.setOption(option);
					}else{
						myChart.hideLoading(); 
						$('.allocation-chart-title-0-emtpy').show();
					}
				},error:function(){
					myChart.hideLoading(); 
					$('.allocation-chart-title-0-emtpy').show();
				}
			});
	    }
	    // pie
	    function allocationMap(){
			$("#allocation-chart-propose").width($(".builder-tab").width());
			$("#allocation-chart-propose").height('250px');
			$("#allocation-chart-change").width($(".builder-tab").width());
			$("#allocation-chart-change").height('250px');
			var myChartOne = echarts.init(document.getElementById('allocation-chart-propose'));
			var myChartTwo = echarts.init(document.getElementById('allocation-chart-change'));
			$('.allocation-chart-title-1').hide();
			$('.allocation-chart-title-2').hide();
			$('.allocation-chart-title-1-emtpy').hide();
			$('.allocation-chart-title-2-emtpy').hide();
			myChartOne.showLoading();
			myChartTwo.showLoading();
			getPieData(myChartOne,myChartTwo);
	    };
	    function getPieData(myChartOne,myChartTwo){
	    	var funds = getFundWeights();
			var allocationMap = {
	    			legendData:[],
	    			seriesData:[]
	    	};
	    	var allocationMapTwo = {
	    			legendData:[],
	    			seriesData:[]
	    	};
			var url = base_root+'/front/tradeChart/getPieData.do?dateStr=' + new Date().getTime();
			$.ajax({
				type:'post',
				url:url,
				data:{funds:encodeURI(JSON.stringify(funds))},
				success:function(result){
					if(result.flag){
						var geoAllocations = result.market,
							sectorTypes = result.sector;
						//基金分布区域
						if(geoAllocations != null){
							for(var key in geoAllocations){
								if(Number(geoAllocations[key]) <= 0){
									continue;
								}
								var formatterName = key +'('+parseFloat(Number(geoAllocations[key])).toFixed(2)+'%)';
								//allocationMap.legendData.push(formatterName);
								var allocationMapSeries = {};
								allocationMapSeries['name'] = formatterName;
								allocationMapSeries['value'] = parseFloat(Number(geoAllocations[key])).toFixed(2);
								allocationMap.seriesData.push(allocationMapSeries);
							}
						}
						allocationMap.seriesData = allocationMap.seriesData.sort(function(a, b){  
							if(Number(a.value)>Number(b.value)){
								return -1;
							}else{
								return 1;
							}
					    });
						$.each(allocationMap.seriesData,function(i, n){
							allocationMap.legendData.push(n.name);
						});
						if(allocationMap.seriesData.length > 0){
							$('.allocation-chart-title-1-emtpy').hide();
							$('.allocation-chart-title-1').show();
						}else{
							$('.allocation-chart-title-1-emtpy').show();
							$('.allocation-chart-title-1').hide();
						}
						myChartOne.hideLoading();
						var option = getPieOption(allocationMap.legendData, allocationMap.seriesData);
						myChartOne.setOption(option);
						//基金类别
						if(sectorTypes != null){
							for(var key in sectorTypes){
								if(Number(sectorTypes[key]) <= 0){
									continue;
								}
								var formatterName = key +'('+parseFloat(Number(sectorTypes[key])).toFixed(2)+'%)';
								//allocationMapTwo.legendData.push(formatterName);
								var allocationMapSeries = {};
								allocationMapSeries['name'] = formatterName;
								allocationMapSeries['value'] = parseFloat(Number(sectorTypes[key])).toFixed(2);
								allocationMapTwo.seriesData.push(allocationMapSeries);
							}
						}
						allocationMapTwo.seriesData.sort(function(a, b){  
							if(Number(a.value)>Number(b.value)){
								return -1;
							}else{
								return 1;
							}
					    });
						$.each(allocationMapTwo.seriesData,function(i, n){
							allocationMapTwo.legendData.push(n.name);
						});
						myChartTwo.hideLoading(); 
						if(allocationMapTwo.seriesData.length > 0){
							$('.allocation-chart-title-2-emtpy').hide();
							$('.allocation-chart-title-2').show();
						}else{
							$('.allocation-chart-title-2-emtpy').show();
							$('.allocation-chart-title-2').hide();
						}
						var option = getPieOption(allocationMapTwo.legendData, allocationMapTwo.seriesData);
						myChartTwo.setOption(option);
					}
				},error:function(){
					myChartOne.hideLoading();  
			        myChartTwo.hideLoading();
			        $('.allocation-chart-title-1-emtpy').show();
					$('.allocation-chart-title-2-emtpy').show();
				}
			});
	    }
	    //折线图切换
	    $("#builder-chart-aggregate").on("click",function(){
	    	$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
	    	aggregate();
	    });
	    $("#builder-chart-separateness").on("click",function(){
	    	$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
	    	separateness();
	    });
	    //‘时间段选择’点击事件
	    $('.builder-chart-date li').click(function(){
	    	$(this).addClass("now").siblings().removeClass();
	    	eChartsData.period = $(this).data('month');
	    	if('builder-chart-separateness' == $('.chart-tab.tab-active').attr('id')){
	    		separateness();
	    	}else if('builder-chart-aggregate' == $('.chart-tab.tab-active').attr('id')){
	    		aggregate();
	    	}
	    });
	    //获取fund集合
	    function getFundIds(){
	    	var fundIds = '';
	    	$(document).find('.product-fund-info').each(function(){
	    		var fundId = $(this).attr('id');
	    		fundIds += fundId + ',';
	    	});
	    	if('' != fundIds){
	    		fundIds = fundIds.substring(0, fundIds.length-1);
	    	}
	    	if(fundIds == '')return;
	    	return fundIds;
	    }
	    //获取weight集合
	    function getWeights(){
	    	var weights = '';
	    	$(document).find('.product-fund-info').each(function(){
	    		var weight = $(this).find('.strategy_chart_tinput').val();
	    		if(weight){
	    			weights += weight + ',';
	    		}else{
	    			weights += 0 + ',';
	    		}
	    	});
	    	if('' != weights){
	    		weights = weights.substring(0, weights.length-1);
	    	}
	    	if(weights == '')return;
	    	return weights;
	    }
	    //获取fund比例集合
	    function getFundWeights(){
	    	var funds = [];
	    	$(document).find('.product-fund-info').each(function(){
	    		var fund = {};
	    		var fundId = $(this).attr('id');
	    		var weight = $(this).find('.strategy_chart_tinput').val();
	    		fund['fundId'] = fundId;
	    		fund['weight'] = parseFloat(Number(weight)).toFixed(1);
	    		funds.push(fund);
	    	});
	    	return funds;
	    }
	    //update chart 点击事件
		$('#btnUpdateChart').click(function(){
			refashData();
		});
		//重新获取数据 刷新视图
		function refashData(){
			$(".builder-main-contents > div").hide().eq(0).show();
			$(".builder-tab li").eq(0).siblings().removeClass("tab-active").end().addClass("tab-active");
			$("#builder-chart-aggregate").siblings().removeClass("tab-active").end().addClass("tab-active");
			//刷新 chart
			allocationMap();
			aggregate();
		}
		//refashData();
	/********************************************************* 公共部分 *****************************************************/
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
	/**
	 * 新窗口弹窗
	 */
    function openResWin(url,width,height,id){ 
 	   var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽    
 	   var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高   
 	   if (width>myw) width = myw;
 	   if (height>myh) height = myh;
 	   var top = (myh-height)/2-(window.screen.height-myh)/2;
 	   if (top<0) top = 0;
 	   var left = (myw-width)/2-(window.screen.width-myw)/2;
 	   if (left<0) left = 0;
 	   window.open(url,id,"height="+height+", width="+width+", top="+top+", left="+left+",location=no,menubar=no,toolbar=no,status=no,resizable=no,scrollbars=yes");   
 	} 
    /**
     * 文本框只能输入数字，并屏蔽输入法和粘贴
     */  
    function numeral(self) {
       $(self).css("ime-mode", "disabled");     
       $(self).bind("keypress",function(e) {     
       var code = (e.keyCode ? e.keyCode : e.which);   
           if(($.browser&&!$.browser.msie)||(e.keyCode==0x8)){     
        	   return;     
           }     
           return code == 46 || (code >= 48 && code<= 57);     
       });     
       $(self).bind("blur", function() {     
           if (this.value.lastIndexOf(".") == (this.value.length - 1)) {     
               this.value = this.value.substr(0, this.value.length - 1);     
           } else if (isNaN(this.value.replace(/,/g,''))) {     
               this.value = "";     
           }     
       });     
       $(self).bind("paste", function() {     
           var s = this.value;     
           if (!/\D/.test(s));     
           return false;     
       });     
       $(self).bind("dragenter", function() {     
           return false;     
       });        
   }; 
   Array.prototype.max = function(){ 
	   return Math.max.apply({},this) 
   } 
   Array.prototype.min = function(){ 
	   return Math.min.apply({},this) 
   }
	/**************************************************************************************************************/
});