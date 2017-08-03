define(function(require) {
	var $ = require('jquery');
			require("echarts");
			require("swiper");
			require("layui");
	$("#portfolio-two").on("click",function(){
		$(this).toggleClass("portfolio-state-active");
		$("#portfolio-show").hide();
		$("#portfolio-hide").show();
	});
	$(".step-portrait-name").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show");
		
	});
	
	function createProposalCharts(){
		$(".create-proposal-charts").each(function(){
			var selfData = eval($(this).attr("data-value"));
			//设置颜色
            for(var item in selfData){
                if( selfData[item].name =="fund" ){
                	selfData[item].name = props['allocation.fund'];
                    selfData[item]['itemStyle'] = {normal:{color:'#fab00a'}}
                }else if(selfData[item].name =="stock"){
                	selfData[item].name = props['allocation.stock'];
                    selfData[item]['itemStyle'] = {normal:{color:'#8f60c2'}}
                }else if( selfData[item].name =="bond" ){
                	selfData[item].name = props['allocation.bond'];
                    selfData[item]['itemStyle'] = {normal:{color:'#a0d54e'}}
                }else if( selfData[item].name =="insure" ){
                	selfData[item].name = props['allocation.insure'];
                    selfData[item]['itemStyle'] = {normal:{color:'#78a288'}}	
                }
            }
			var option = {
			    series: [
			        {
			            type:'pie',
			            label: {
			                normal: {
			                    position: 'inner',
			                    formatter : "{b}\n{d}%"
			                }
			            },
			            data:selfData
			        }
			    ]
			};
			var myChart = echarts.init($(this)[0]);
				myChart.setOption(option,true);
		});
	}
	
	function swiper(){
		new Swiper('.proposal-charts-wrap', {
	        slidesPerView: 'auto',
	        grabCursor: true,
	        preventClicks : false,
	        nextButton: '.swiper-button-next',
	        prevButton: '.swiper-button-prev'
	    });
		$(".proposal-charts-wrap").on("mouseenter",function(){
	        $(this).find(".swiper-button-next, .swiper-button-prev").show();
	    });
	    $(".proposal-charts-wrap").on("mouseleave",function(){
	        $(this).find(".swiper-button-next, .swiper-button-prev").hide();
	    });
	    //点击事件
	    $(".proposal-chart-rows").on("click",function(){
	    	$('#portfolio-one').addClass('portfolio-state-active');
	    	var background = $(this).closest('.proposal-choose-rows').find('.proposal-choose-title').css('background');
	    	//$('#portfolio-one').css('background',background);
	    	var strategyId = $(this).data('strategyId');
	    	if($(this).hasClass("proposal-chart-active")){
	    		$('.proposal-chart-rows').attr('style','border:\'\'');
	    		$(this).removeClass("proposal-chart-active");
	    		$('#hidStrategyId').val('');
	    		//$('#portfolio-one').removeClass('portfolio-state-active');
	    		//$('#portfolio-one').attr('style','border:\'\'');
	    		$('#btnNextTwo').css('background','#94999e').css('cursor','no-drop');
	    		$('#btnNextTwo').unbind('click');
	    	}else{
	    		$(".proposal-chart-rows").removeClass("proposal-chart-active");
		    	$(this).addClass("proposal-chart-active");
		    	$('.proposal-chart-rows').attr('style','border:\'\'');
		    	background = (background.split(' none'))[0];
		    	$('.proposal-chart-active').attr('style','border:2px solid '+background);
		    	$('#hidStrategyId').val(strategyId);
		    	$('#btnNextTwo').css('background','#2d80ce').css('cursor','pointer');
		    	//跳转 step2
		    	$('#btnNextTwo').unbind('click');
		    	$('#btnNextTwo').click(function(){
		    		nextTwo();
		    	});
	    	}
	    });
	    var strategyId = $('#hidStrategyId').val();
	    if(typeof(strategyId) != 'undefined' && strategyId != ''){
	    	$('.proposal-chart-rows').each(function(){
		    	var id = $(this).data('strategyId');
		    	if(strategyId == id){
		    		$(this).addClass("proposal-chart-active");
		    	}
		    });
	    }
	    
	}
    
	/**
	 * 绑定‘我的投资策略’数据
	 */
	function bindMyStrategy(){
		$.ajax({
			type:'post',
			data:{},
			url: base_root+'/front/portfolio/arena/getMyStrategy.do?dateStr=' + new Date().getTime(),
			success:
				function(result){
					////console.log(result);
					if(result.flag){
						var data = result.strategyInfoVOs;
						if(typeof(data)!='undefined' && data!=null){
							var strategy_1 = '';
							var strategy_2 = '';
							var strategy_3 = '';
							var strategy_4 = '';
							var strategy_5 = '';
							$.each(data,function(i,n){
								var strategyName = n.strategyName == null? '':n.strategyName;
								var riskLevel = n.riskLevel == null? '':n.riskLevel;
								var strategyAllocations = n.strategyAllocations;
								if(strategyAllocations != null){
									var dataValues = [];
									var itemWeightFund = 0;
									var itemWeightStock = 0;
									var itemWeightBond = 0;
									$.each(strategyAllocations,function(j,m){
										////console.log(strategyAllocations);
										var itemWeight = m.itemWeight != null ? m.itemWeight : '';
										var type = m.itemWeight != null ? m.type : '';
										if('fund' == type){
											itemWeightFund = itemWeightFund + itemWeight;
										}else if('stock' == type){
											itemWeightStock = itemWeightStock + itemWeight;
										}else if('bond' == type){
											itemWeightBond = itemWeightBond + itemWeight;
										}
									});
									var dataValue = {};
									var index = 0;
									if(itemWeightFund != 0 && typeof(itemWeightFund) != 'undefined'){
										dataValue = {};
										dataValue['name'] = 'fund';
										dataValue['value'] = itemWeightFund;
										dataValues[index] = dataValue;
										index++;
									}
									if(itemWeightStock != 0 && typeof(itemWeightStock) != 'undefined'){
										dataValue = {};
										dataValue['name'] = 'stock';
										dataValue['value'] = itemWeightStock;
										dataValues[index] = dataValue;
										index++;
									}
									if(itemWeightBond != 0 && typeof(itemWeightBond) != 'undefined'){
										dataValue = {};
										dataValue['name'] = 'bond';
										dataValue['value'] = itemWeightBond;
										dataValues[index] = dataValue;
										index++;
									}
									dataValues = JSON.stringify(dataValues);
									dataValues = dataValues.replace(new RegExp('\"', 'g'), '\'');
									if(dataValues !='[]' &&'' != riskLevel){
										var temp =
											'<div data-strategy-id="'+n.id+'" class="proposal-chart-rows swiper-slide">'
				                            +'<div class="create-proposal-charts" '
				                            //+ defaultColor
				                            +'data-value="'
				                            + dataValues
				                            +'"></div>'
				                            +'<p class="proposal-charts-title" data-name="'+strategyName+'">';
											//if(strategyName.length>14){
											//	temp += strategyName.substring(0, 14) + '...';
											//}else{
												temp += strategyName;
											//}
											temp +=
											 '</p>'
				                            +'</div>';
										switch (parseInt(riskLevel)) {
											case 1:strategy_1 += temp;
												break;
											case 2:strategy_2 += temp;
												break;
											case 3:strategy_3 += temp;
												break;
											case 4:strategy_4 += temp;
												break;
											case 5:strategy_5 += temp;
												break;
											default:
												break;
										}
									}
								}
							});
							var noStrategy = '<div style="margin-top:16%;margin-left:calc(50% - 64px);">'
                             	+'<img alt="" src="'+base_root+'/res/images/proposal/proposalnodata.png">'
								+'<p style="text-align:center;color: #aad1e8;line-height:31px;position:absolute;top:3px;left:0;width:100%;">'+props["global.nodata.tips"]+'</p>'
								+'</div>';
							if(strategy_1 == ''){
								strategy_1 = noStrategy;
								$('#strategy-1').closest('.proposal-charts-wrap').removeClass('proposal-charts-wrap');
							}
							if(strategy_2 == ''){
								strategy_2 = noStrategy;
								$('#strategy-2').closest('.proposal-charts-wrap').removeClass('proposal-charts-wrap');
							}
							if(strategy_3 == ''){
								strategy_3 = noStrategy;
								$('#strategy-3').closest('.proposal-charts-wrap').removeClass('proposal-charts-wrap');
							}
							if(strategy_4 == ''){
								strategy_4 = noStrategy;
								$('#strategy-4').closest('.proposal-charts-wrap').removeClass('proposal-charts-wrap');
							}
							if(strategy_5 == ''){
								strategy_5 = noStrategy;
								$('#strategy-5').closest('.proposal-charts-wrap').removeClass('proposal-charts-wrap');
							}
							$('#strategy-1').empty().append(strategy_1);
							$('#strategy-2').empty().append(strategy_2);
							$('#strategy-3').empty().append(strategy_3);
							$('#strategy-4').empty().append(strategy_4);
							$('#strategy-5').empty().append(strategy_5);
							var text = '';
							$('.proposal-charts-title').hover(function(){  
								if($(this).text().length>14){
									var fundName = $(this).data('name');
									text = $(this).text();
									$(this).text(fundName);
								}
							},function(){
								if($(this).text().length>14){
									$(this).text(text);
								}
							});
							//ECharts启动
							createProposalCharts();
							//滑动效果
							swiper();
							$('.proposal-charts-title').click(function(event){
								var strategyId = $(this).closest('div').data('strategyId');
								window.open(base_root + '/front/strategy/info/strategiesdetail.do?id='+strategyId);
								event.stopPropagation();
							});
						}
					}
				}
			});
	}
	function nextTwo(){
		var strategyId = $('#hidStrategyId').val();
		if($('#portfolio-one').hasClass('portfolio-state-active') && strategyId==''){
			layer.msg(porps['create.portfolio.step.one.alert.select.strategy'], {icon:3});
		}else{
			var url = '';
			if(typeof(strategyId) != '' && strategyId != ''){
				url = base_root+'/front/portfolio/arena/createPortfolioTwo.do?strategyId='+strategyId+'&portfolioId='+urlRequest('portfolioId');
				if(getUrlParam('strategyId') != null && getUrlParam('strategyId') != strategyId){
					url = urlUpdateParams(url,'newStrategy','1');
				}else if(getUrlParam('strategyId') == null){
					url = urlUpdateParams(url,'newStrategy','1');
				}
			}else{
				url = base_root+'/front/portfolio/arena/createPortfolioTwo.do?portfolioId='+urlRequest('portfolioId');
			}
			window.location.href = url;
		}
	}
	
	
	function getUrlParam(name){ 
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
	
	//回退判断
	(function(){
		//回退时，url有 strategyId 参数
		var strategyId = getUrlParam('strategyId');
		if(typeof(strategyId) != 'undefined' && strategyId != null && strategyId != ''){
			$('#hidStrategyId').val(strategyId);
			$('#btnNextTwo').css('background','#2d80ce').css('cursor','pointer');
			//跳转 step2
			$('#btnNextTwo').unbind('click');
			$('#btnNextTwo').click(function(){
				nextTwo();
			});
		}
		bindMyStrategy();
	})();
	
	 function urlRequest(m) {
        var sValue = location.search.match(new RegExp("[\?\&]" + m + "=([^\&]*)(\&?)", "i"));
        return sValue ? sValue[1] : sValue;
	 }
	 
	 $('#create-new-portfolio').click(function(){
		 window.location.href = base_root + '/front/portfolio/arena/createPortfolioTwo.do?portfolioId='+getUrlParam('portfolioId')+'&d='+new Date().getTime();
	 });
	 function urlUpdateParams(url, name, value) {
        var r = url;
        if (r != null && r != 'undefined' && r != "") {
            value = encodeURIComponent(value);
            var reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
            var tmp = name + "=" + value;
            if (url.match(reg) != null) {
                r = url.replace(eval(reg), tmp);
            }
            else {
                if (url.match("[\?]")) {
                    r = url + "&" + tmp;
                } else {
                    r = url + "?" + tmp;
                }
            }
        }
        return r;
     }
});