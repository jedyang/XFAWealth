
define(function(require) {
	var $ = require('jquery');
			require('layui');
			require('laydate');
			require('orderPlanCheckPwd');
	$("#order-portrait-show").on("click",function(){
		$(".order-plan-sapce").toggleClass("order-plan-block");
	});
	$(".order-setting-cycle").on("click",function(){
		$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
	});
	$(".order-setting-condition .order-setting-cycle").on("click",function(){
		$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
		$(".order-choice-list .order-choice-date").hide().eq($(this).index()-1).show();
	});
	$(".client-more-ico").on("click",function(){
		$(this).parents(".order-plan-rows").toggleClass("client-more-active");
	});
	$(".order-setting-xiala").on("click",function(){
		$(this).parents(".order-setting-table-rows").toggleClass("client-small-active");
	});
	$(".client_xiala").on("click",function(){
		$(this).toggleClass("client_xiala_active");
	});
	$(".client_xiala li").on("click",function(e){
		$(this).parents(".client_xiala").toggleClass("client_xiala_active").find("input").val($(this).html());
		e.stopPropagation(); 
		window.location.href=base_root+"/front/tradeCheck/transactionApproval.do?id="+id +"&currency="+$("#currency").val();
	});
	$(".order-number-top").on("click",function(){
		$("#order-number-choice").val(Number($("#order-number-choice").val()) + 1);
	});
	$(".order-number-bottom").on("click",function(){
		if( Number($("#order-number-choice").val()) == 0)return;
		$("#order-number-choice").val(Number($("#order-number-choice").val()) - 1);
	});
	
/**************************************************************************************************************/
	
	/**
	 * no data
	 */
	if($('.subscription-table tbody tr:gt(0)').length<1){
		$('.subscription-table-div').hide();
	}else{
		$('.subscription-table-div').show();
	}
	if($('.redemption-table tbody tr:gt(0)').length<1){
		$('.redemption-table-div').hide();
	}else{
		$('.redemption-table-div').show();
	}
	if($('.subscription-table tbody tr:gt(0)').length<1 && $('.redemption-table tbody tr:gt(0)').length<1){
		$('.rpq-risk-level-tips').hide();
		$('.product-nodata-tips').show();
	}else{
		$('.rpq-risk-level-tips').show();
		$('.product-nodata-tips').hide();
	}
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
	 * Cancel
	 */
	$('#btnCancel').click(function(){
		window.location.href = base_root + '/front/tradeRecord/orderPlanList.do';
	});
	
	/**
	 * Revoke
	 */
	$('.order-tables-action-revoke').click(function(){
		var accountId = $(this).data('accountId');
		var aeCode = $(this).data('aeCode');
		var historyId = $(this).closest('tr').attr('id');
		layer.confirm('Whether to withdraw the order?', {
 			  title:globalProp['global.info'],
			  btn: [globalProp['global.confirm'],
			        globalProp['global.cancel']]
		},function(index){
			layer.close(index);
			var initInterface = {};
			initInterface['historyId'] = historyId;
			initInterface['orderType'] = "0"; // 撤单
			$(this).InitCheckPwd(initInterface);
		});
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
	 * 获取URL参数
	 */
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
	/**************************************************************************************************************/
	
	
});