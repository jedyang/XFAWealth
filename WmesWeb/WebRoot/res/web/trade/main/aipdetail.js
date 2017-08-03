define(function(require) {
	var $ = require('jquery');
			require('slider');
			require('layui');
			require('jqueryRange');
			require("laydate");
	$(".order-setting-end").on("click",function(){
		$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
		$(".order-choice-list .order-choice-date").hide().eq($(this).index()-1).show();
	});
	$(".aip-exec-cycle").on("click",function(){
 		if(!$(this).hasClass('aip-exec-cycle-on')){
 			$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
 		}else{
 			$('.order-setting-button').click();
 		}
    });
    $('.wmes-close,.proposal-mask-button-cancel').on('click',function(){
    	$('.proposal-mask-wrap,.proposal-mask-contents').css('display','none');
    });
    $(".order-number-top").on("click",function(){
		$("#order-number-choice").val(Number($("#order-number-choice").val()) + 1);
		$('#txtAipEndDate').val('');
		$('#order-money-choice').val('');
	});
	$(".order-number-bottom").on("click",function(){
		if( Number($("#order-number-choice").val()) == 0)return;
		$("#order-number-choice").val(Number($("#order-number-choice").val()) - 1);
		$('#txtAipEndDate').val('');
		$('#order-money-choice').val('');
	});
	$(".client-more-ico").on("click",function(){
		$(this).closest(".order-plan-rows").toggleClass("client-more-active");
	});
	/*************************************************AIP*******************************************************/
    /**
	 * 定投开关
	 */
	$('#aipState').click(function(){
		if($(this).attr('data-state')==1){
		// off
			$(this).removeClass("aipstate_button_active");
			$(this).attr('data-state',0);
			$('#aipChange').hide();
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
		    $.post(base_root + '/front/transaction/updateAipState.do?d='+new Date().getTime(),{planId:planId,state:state});
		}
	});
    /**
     * AIP设置
     */
	$(".aip-setting-btn-changes").on("click",function(){
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
	$('#aipExecCycle p:gt(0)').on('click',function(){
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
		$('#txtNextChargeDate').text(nextChargeDate);
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
		$('#aipExecCycle p:gt(0)').each(function(){
			if($(this).hasClass('order-cycle-active')){
				validFlag = false;return false;
			}
		});
		if(validFlag){layer.msg(props['order.plan.alert.aip.exec.cycle.choose']);return;}  // 请选择定投周期
		if(!$('#txtAipAmount').val() || Number($('#txtAipAmount').val())==0){layer.msg(props['order.plan.alert.aip.amount.empty']);return;} // 定投金额不能为空或者0
		if(!$('#txtAipEndDate').val() && !$('#order-number-choice').val() && !$('#order-money-choice').val()){
			layer.msg(props['order.plan.alert.aip.stop.condition.empty']);return;  // 定投停止条件不能为空
		}
		//aip
    	var aipInitTime = $('#txtNextChargeDate').text();
    	var aipExecCycle = '';
		$('#aipExecCycle p:gt(0)').each(function(){
			if($(this).hasClass('order-cycle-active')){
				aipExecCycle = $(this).data('day');
			}
		});
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
    			}else{
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
		if(Number(aipAmount) < Number(tableTotalAmount)){
			layer.msg(props['order.plan.alert.aip.amount.control.Minimum']); //定投金额不能小于当前产品总金额
			$('#txtAipAmount').val(formatCurrency(tableTotalAmount));
		}else{
			$('#txtAipAmount').val(formatCurrency(aipAmount));
		}
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
		$('.table-product-data').each(function(i,n){
			var amount = $(this).find('.fund_amount').text();
			amount = amount.replace(/,/g,'');
			totalAmount = Number(totalAmount) + Number(amount);
		});
		return formatCurrency(totalAmount);
	}
	/**
	 * AIP 手动停止
	 */
	$('.aip-setting-btn-stop').click(function(){
		var aipState = $('.order-aip-state').attr('data-aip-state');
		if(typeof(aipState) != 'undefined' && aipState != '' 
			&& ('2' == aipState || '3' == aipState)){
			layer.msg(props['order.plan.aip.detail.alert.stop']);
		}else{
			layer.confirm(props['order.plan.aip.detail.stop.aip'], {
				  title:props['global.info'],
				  btn: [props['global.confirm'],props['global.cancel']]
			},function(index){
				updateOrderAip('3');
			  layer.close(index);
			});
		}
	});
	/**
	 * AIP 暂停
	 */
	$('.aip-setting-btn-suspend').click(function(){
		var aipState = $('.order-aip-state').attr('data-aip-state');
		if(typeof(aipState) != 'undefined' && aipState != '' 
			&& ('2' == aipState || '3' == aipState)){
			layer.msg(props['order.plan.aip.detail.alert.stop']);
		}else if(typeof(aipState) != 'undefined' && aipState != '' 
			&& '0' == aipState){
			layer.msg(props['order.plan.aip.detail.alert.suspension']);
		}else{
			layer.confirm(props['order.plan.aip.detail.suspension.aip'], {
				  title:props['global.info'],
				  btn: [props['global.confirm'],props['global.cancel']]
			},function(index){
				updateOrderAip('0');
			  layer.close(index);
			});
		}
	});
	/**
	 * AIP 状态修改（执行中）
	 */
	function updateOrderAip(state){
		var url = base_root + '/front/transaction/updateOrderAip.do?d=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				aipState:state,
				planId:getUrlParam('planId')
			},
			success:function(re){
				if(re.flag){
					layer.msg(props['global.successful.operation']);
					var aipState = re.aipState;
					var aipStateDisplay = re.aipStateDisplay;
					$('.order-aip-state').attr('data-aip-state',aipState);
					$('.order-aip-state').text(aipStateDisplay);
					if('3' == state){
						$('.aip-setting-btncon').remove();
					}
				}else{
					layer.msg(props['global.operation.failed']);
				}
			},
			error:function(){
				layer.msg(props['global.operation.failed']);
			}
		});
	}
	/****************************************************** AIP **************************************************/
	/**
	 * tab
	 */
	$(".funds_viewList_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$('.detail-product').toggle();
	});
	
	if($('.subscription-table tbody tr:gt(0)').length < 2){
		$('.rpq-risk-level-tips').hide();
	}else{
		$('.rpq-risk-level-tips').show();
	}
	/********************************************************************************************************/
	/**
	 * get url param
	 */
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
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
	/********************************************************************************************************/
});