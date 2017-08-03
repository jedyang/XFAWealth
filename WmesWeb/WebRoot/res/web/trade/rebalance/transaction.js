define(function(require) {
	var $ = require('jquery');
		    require("echarts");
		    require('slider');
			require('layui');
			require('laydate');
	//姓名切换
	$(".step-portrait-choose li").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show").find(".step-portrait-name").html($(this).html());
	});
	$(".step-portrait-name, .stepOne-constrains-xiala").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show");
	});
	$(".builder-tab li").on("click",function(){
		$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
		$(".builder-main-contents > div").hide().eq($(this).index()).show();
		allocationMap();
		separateness();
		aggregate();
	});
	$("#portfolioName-edit-img").on("click",function(){
    	$("#txtProposalName").toggleClass("portfolio-edit-name-active");
    	if($('#txtProposalName').hasClass('portfolio-edit-name-active')){
    		$('#txtProposalName').removeAttr('readonly');
		}else{
    		$('#txtProposalName').attr('readonly','readonly');
    	}
    });
    $(".portfolio-edit-name").on("blur",function(){
    	$(".portfolio-edit-name").removeClass("portfolio-edit-name-active");
    	$(".portfolio-edit-name").attr("readonly","readonly");
    });
    if($('.transformation-wrap tbody').length<3){
		$('.rebalance-product-total').hide();
		$('.product-tips-rpq-risk-level').hide();
		$('.rebalance-product-nodata-tips').show();
	}else{
		$('.rebalance-product-total').show();
		$('.product-tips-rpq-risk-level').show();
		$('.rebalance-product-nodata-tips').hide();
	}
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
	    	var fundIds = '';
	    	var weights = '';
	    	var funds = '';
	    	return {
	    		period:period,
	    		fundIds:fundIds,
	    		weights:weights,
	    		funds:funds
	    	};
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
				aipDataSeries:[],
				holdDataSeries:[]
			};
			var fundIds = getFundIds();
			var weights = getWeights();
			var period = eChartsData.period;
			var url = base_root+'/front/tradeChart/getHoldProductReturnData.do?dateStr=' + new Date().getTime();
			$.ajax({
				type:'post',
				url:url,
				data:{
					holdId:getUrlParam('id'),
					fundIds:fundIds,
					weights:weights,
					period:period
				},
				success:function(result){//console.log(result);
					if(result.flag && result.chartsData.returnRates != null && result.chartsData.returnRates.length > 0){
						xAxisData = result.chartsData.marketDates;
						aggregate.nameData.push(props['create.portfolio.step.three.incomePercentageTotal']);
						aggregate.nameData.push(props['order.plan.portfolio.hold.cumperf']);
						aggregate.totalDataSeries = result.chartsData.returnRates;
						aggregate.holdDataSeries = result.holdChartsData.returnRates;
					}
					if(aggregate.nameData.length > 0){
						myChart.hideLoading();
						var minVal = aggregate.totalDataSeries.min();
						var maxVal = aggregate.totalDataSeries.max();
						var holdMinVal = aggregate.holdDataSeries.min();
						var holdMaxVal = aggregate.holdDataSeries.max();
						if(minVal > holdMinVal){
							minVal = holdMinVal;
						}
						if(maxVal < holdMaxVal){
							maxVal = holdMaxVal;
						}
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
						if(aggregate.holdDataSeries.length > 0){
							option = getSeries(option, props['order.plan.portfolio.hold.cumperf'], 'rgba(246, 137, 21, 0.8)', aggregate.holdDataSeries);
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
	    	$(document).find('.transformation-wrap tbody:gt(0)').each(function(){
	    		var fundId = $(this).find('tr:eq(0)').attr('id');
	    		if(typeof fundId !='undefined')
	    		fundIds += fundId + ',';
	    	});
	    	if('' != fundIds){
	    		fundIds = fundIds.substring(0, fundIds.length-1);
	    	}
	    	if(fundIds == '{{items.fundId}}')return;
	    	return fundIds;
	    }
	    //获取fund比例集合
	    function getFundWeights(){
	    	var funds = [];
	    	$(document).find('.transformation-wrap tbody:gt(0)').each(function(){
	    		var fund = {};
	    		var fundId = $(this).find('tr:eq(0)').attr('id');
	    		if(typeof fundId == 'undefined')return true;
	    		var weight = $(this).find('tr .product-new-weight').text();
	    		if(!isNaN(weight)){weight = 0.0;}
	    		fund['fundId'] = fundId;
	    		fund['weight'] = parseFloat(weight).toFixed(1);
	    		funds.push(fund);
	    	});
	    	return funds;
	    }
	    
	    
	    
	   //获取weight集合
	    function getWeights(){
	    	var weights = '';
	    	$(document).find('.product-new-weight').each(function(){
	    		var weight = $(this).text();
	    		if(weight && weight.indexOf('%')){
	    			weight = (weight.split('%'))[0];
	    			weights += weight + ',';
	    		}
	    	});
	    	if('' != weights){
	    		weights = weights.substring(0, weights.length-1);
	    	}
	    	if(weights == '')return;
	    	return weights;
	    }
	    /*//获取fund集合
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
	    }*/
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
    /**********************************ECharts End**************************************/ 
	
	
	//analysis-chart 图表栏 缩展切换
	$('#analysis-chart-ico').click(function(){
		$(this).toggleClass('rotate');
		$('#analysis-chart').slideToggle('fast');
	});
	//portfolio-Detail 组合详情栏 缩展切换
	$('#portfolio-Detail-ico').click(function(){
		$(this).toggleClass('rotate');
		$('#portfolio-Detail').slideToggle('fast');
	});
	//automatic-setting 定投设置栏 缩展切换
	$('#automatic-setting-ico').click(function(){
		$(this).toggleClass('rotate');
		$('#automatic-setting').slideToggle('fast');
	});
    $('#aip-date-range').click(function(){
    	$('#aip-date-range-div').show();
    	$('#aip-end-count-div').hide();
    	$('#aip-total-amount-div').hide();
    });
    $('#aip-end-count').click(function(){
    	$('#aip-end-count-div').show();
    	$('#aip-date-range-div').hide();
    	$('#aip-total-amount-div').hide();
    });
    $('#aip-total-amount').click(function(){
    	$('#aip-total-amount-div').show();
    	$('#aip-end-count-div').hide();
    	$('#aip-date-range-div').hide();
    });
    //列表当前行持仓数据
    var positionsData =(function(){
    	var weight = '';
    	var holdingUnit = '';
    	var availableUnit = '';
    	var referenceCost = 0;
    	var nav = '';
    	var referenceCostTotal = 0;
    	return {
    		weight:weight,
    		holdingUnit:holdingUnit,
    		availableUnit:availableUnit,
    		nav:nav,
    		referenceCostTotal:referenceCostTotal,
    		referenceCost:referenceCost
    	};
    })(); 
    
    initWeight();
    (function(){
		var currencyCode = $('#portfolio-currency').attr('data-code');
    	var url = base_root+'/front/tradeRebalance/getPortfolioHoldProducts.do?dateStr=' + new Date().getTime();
    	$.ajax({
			type:'post',
			async:false,
			data:{currencyCode:currencyCode,planId:getUrlParam('planId')},
			url: url,
			success:
				function(result){
					if(result.flag){
						var planProductVOs = result.planProductVOs;
						if(planProductVOs != null && planProductVOs.length>0){
							$.each(planProductVOs,function(i,n){
								var original = n.original==null?'':n.original;
								var fundName = n.fundName==null?'':n.fundName;
								var fundInfoId = n.fundInfoId==null?'':n.fundInfoId;
								var riskLevel = n.riskLevel==null?'':n.riskLevel;
								var fundType = n.fundType==null?'':n.fundType;
								var fundCurrencyName = n.fundCurrencyName==null?'':n.fundCurrencyName;
								var lastNav = n.lastNav==null?'':n.lastNav;
								var currencyName = n.currencyName==null?'':n.currencyName;
								var weightAdjust = n.weightAdjust==null?'':n.weightAdjust;
								var tranType = n.tranType ==null?'':n.tranType ;
								var weight = n.weight ==null?'':n.weight ;
								var amount = n.amount==null?'':n.amount;
								var unit = n.unit==null?'':n.unit;
								var switchFlag = n.switchFlag==null?'':n.switchFlag;
								var fromProductId = n.fromProductId==null?'':n.fromProductId;
								var fromFundInfoId = n.fromFundInfoId==null?'':n.fromFundInfoId;
								var unitRedeem = n.unitRedeem ==null?'':n.unitRedeem;
								if('1' == switchFlag && 'B' == tranType){
								// 转换
									var flag = false;
									$('.transformation-wrap tbody').each(function(){
										if(fromFundInfoId == $(this).children().eq(0).attr('id')){
											var html = '';
											html =
											'<tr id="'+fromFundInfoId+'" class="product-tr-transformation action-original">'
											+'<td class="portfolio_tables_fnames">'
											+'<img class="product-update-ico" src="'+base_root+'/res/images/client/product_update_ico.png">'
											+'<a href="javascript:;">'+parseFloat(unitRedeem).toFixed(2)+'</a>'
											+'<div class="portfolio_tables_nameWord">'
											+'<p class="portfolio_tables_risk">'
											+'<span class="product-risk-level">'+riskLevel+'</span> '
											+ props['order.plan.rebalance.table.risk']
											+'</p>'
											+'<p class="portfolio_tables_risk" style="width:50%">'+fundType+'</p>'
											+'</div></td><td class="portfolio-tdcenter">N/A</td>'
											+'<td class="portfolio-tdcenter">'
											+'<span class="product-last-nav conversion-flag">'+formatCurrency(lastNav)+'</span>'
											+'<span class="to-currency">'+currencyName+'</span></td>'
											+'<td class="portfolio-tdcenter">N/A</td>'
											+'<td class="portfolio-tdcenter product-available-unit" colspan="2">'
											+'<a class="product-redeem-button" href="javascirpt:;">'+props['order.plan.rebalance.switch.unitRedeem']+' '
											+'<span class="product-unit-redeem">'+parseFloat(unitRedeem).toFixed(2)+'</span>'
											+'</a><p class="product-redeem-price">'
											+'<span class="product-last-nav conversion-flag">'
											+ formatCurrency(amount)+'</span><span style="margin-left:3px" class="to-currency">'
											+ currencyName+'</span></p></td><td class="portfolio-tdcenter portfolio-table-blue product-new-weight">'
											+ weight+'%</td><td class="portfolio-tdcenter">'
											+'<img class="product-del-ico action-delete" src="'+base_root+'/res/images/client/product_del_ico.png">'
											+'</td>'
											+'</tr>';
											$(this).addClass('product-tbody-wrap');
											$(this).find('.product-actions').hide();
											$(this).find('.product-actions').closest('td').append('<img class="product-edit-ico" src="'+base_root+'/res/images/client/product_edit_ico.png">');
											$(this).append(html);
											return false;
										}
									});
									$('.product-edit-ico').unbind('click');
									$('.product-edit-ico').click('click',function(){
										fundConversion(this);
									});
								}else if(fromProductId == ''){
								// 新增
									var html = 
									'<tbody class="product-tbody-wrap action-original">'
									+'<tr id="'+n.fundInfoId+'">'
									+'<td class="portfolio_tables_fnames">'
									+'<a href="javascript:;">'+fundName+'</a>'
									+'<div class="portfolio_tables_nameWord">'
									+'<p class="portfolio_tables_risk" style="width:30%">'
									+'<span class="product-risk-level">'+riskLevel+'</span>'
									+ props['order.plan.rebalance.table.risk']
									+'</p>'
									+'<p class="portfolio_tables_risk" style="width:70%">'
									+ fundType+'</p>'
									+'</div></td><td class="portfolio-tdcenter">N/A</td>'
									+'<td class="portfolio-tdcenter">'
									+'<span class="product-last-nav conversion-flag">'+formatCurrency(lastNav)+'</span>'
									+'<span class="to-currency"> '+currencyName+'</span>'
									+'</td><td class="portfolio-tdcenter">N/A</td>'
									+'<td class="portfolio-tdcenter">N/A</td>'
									+'<td class="portfolio-tdcenter">'
									+'<input class="product-table-input product-tbody-valid-add list-add-amount-flag" type="text" value="'+formatCurrency(amount)+'">'
									+'<p class="product-table-cur" style="margin-left:1px">'
									+'<span class="to-currency">'+currencyName+'</span>'
									+'</p></td><td class="portfolio-tdcenter portfolio-table-blue product-new-weight">'+ weightAdjust
									+'%</td><td class="portfolio-tdcenter">'
									+'<div class="product-actions">'
									+'<img class="product-del-ico action-delete" src="'+base_root+'/res/images/client/product_del_ico.png">'
									+'</div></td></tr></tbody>';
									$('.tbody-total').before(html);
								}else if('B' == tranType){
									var html = '';
									html = 
									'<tr class="product-original">'
		                            +'<td class="portfolio-tdcenter" colspan="3"></td>'
		                            +'<td class="portfolio-tdcenter" colspan="2" style="text-align: -webkit-right;">'
		                            +'<img class="product-cart-ico" src="'+base_root+'/res/images/client/product_cart_ico.png">'
		                            +'<span style="margin-left:20px">'
		                            + props['order.plan.rebalance.AIP.currentStatus.amount']
		                            +'</span></td><td class="portfolio-tdcenter">'
		                            +'<input class="product-table-input list-add-amount-flag" type="text" value="'+formatCurrency(amount)+'">'
		                            +'<p class="product-table-cur">'+currencyName+'</p></td>'
		                            +'<td class="portfolio-tdcenter portfolio-table-blue">'
		                            +'</td><td class="portfolio-tdcenter"><div class="product-actions">'
		                            +'<img class="product-off-ico action-recovery" src="'+base_root+'/res/images/client/product_off_ico.png">'
		                            +'</div></td></tr>'
		                            +'</tbody>';
									$('.transformation-wrap tbody').each(function(){
										if(fundInfoId == $(this).children().eq(0).attr('id')){
											$(this).addClass('product-tbody-wrap');
											$(this).find('.product-actions').hide();
											$(this).append(html);
											return false;
										}
									});
								}else if('1' != switchFlag && 'S' == tranType){
									var html = '';
									html = 
									'<tr class="action-original">'
									+'<td class="portfolio-tdcenter" colspan="3"></td>'
									+'<td class="portfolio-tdcenter">'
									+'<img class="product-cart-ico product-reduce-ico" src="'+base_root+'/res/images/client/product_reduce_ico.png">'
									+'<p class="product-table-word">'
									+ props['order.plan.aip.detail.table.th.unit']
									+'</p></td><td class="portfolio-tdcenter">'
									+'<input class="product-table-input" type="text" value="'+unit+'"></td>'
									+'<td class="portfolio-tdcenter"><p class="product-redeem-price">'
									+'<span class="product-redeem-amount conversion-flag">'+formatCurrency(lastNav*unit)+'</span>' 
									+'<span class="to-currency"> '+currencyName+'</span></p></td>'
									+'<td class="portfolio-tdcenter portfolio-table-blue"></td>'
									+'<td class="portfolio-tdcenter"><div class="product-actions">'
									+'<img class="product-off-ico action-recovery" src="'+base_root+'/res/images/client/product_off_ico.png">'
									+'</div></td></tr>';
									$('.transformation-wrap tbody').each(function(){
										if(fundInfoId == $(this).children().eq(0).attr('id')){
											$(this).addClass('product-tbody-wrap');
											$(this).find('.product-actions').hide();
											$(this).append(html);
											return false;
										}
									});
								}
							});
						}
					}
				}
		});
		reWeight();
		//refashData();
})();
//table
function ProductTable(fundInfos){
	var html = '';
	//持仓对象
	var product = '';
	var toCurrency = $('#portfolio-currency').val();
	$.each(fundInfos,function(i,n){
		var fundInfo = null;
		//多语言
		if('en' == lang){
			fundInfo = n.fundInfoEn;
		}else if('tc' == lang){
			fundInfo = n.fundInfoTc;
		}else{
			fundInfo = n.fundInfoSc;
		}
		if(null != fundInfo){
			var fundName = fundInfo.fundName == null?'':fundInfo.fundName;
			var riskLevel = '';
			var lastNav = '';
			var fundId = '';
			if(null != n.fundInfo){
				riskLevel = n.fundInfo.riskLevel == null?'':n.fundInfo.riskLevel;
				lastNav = n.fundInfo.lastNav == null?'':n.fundInfo.lastNav;
				fundId = n.fundInfo.id == null?'':n.fundInfo.id;
			}
			var fundType = fundInfo.fundType == null?'':fundInfo.fundType;
			var fundCurrencyCode = fundInfo.fundCurrencyCode == null ? '' : fundInfo.fundCurrencyCode;
			product = n.portfolioHoldProduct;
			var productId = '';
			var holdingUnit = '';
			var referenceCost = '';
			var availableUnit = '';
			if(null != product){
				productId = product.id;
				holdingUnit = product.holdingUnit == null ? '' : product.holdingUnit;
				referenceCost = product.referenceCost == null ? '' : product.referenceCost;
				availableUnit = product.availableUnit == null ? '' : product.availableUnit;
			}else{
				//新增时，product为null
				holdingUnit = 'N/A';
				referenceCost = '<input id="txtreduction" type="text" data-original="0" data-value-type="Amount" value="" style="width:80px;color:blue;margin-left:5px;"/>';
				availableUnit = 'N/A';
			}
			//总成本计算
			var actualCost = '';
			if(null != product && holdingUnit!='' && lastNav!=''){
				actualCost = parseFloat(holdingUnit*lastNav).toFixed(2);
				positionsData.referenceCostTotal = eval(parseFloat(positionsData.referenceCostTotal+holdingUnit*lastNav).toFixed(2));
			}
			var totalMarketValue = $('#hidTotalMarketValue').val();
			var allocationRate = '';
			if(referenceCost != '' && null != product){
				allocationRate = parseFloat(referenceCost/totalMarketValue*100).toFixed(2);
			}
			//基金所属公司Id
			var fundHouseId = n.fundHouseId == null? '' : n.fundHouseId;
	    	if(product == null){
	    		html +=
					'<tr data-weight="'+allocationRate+'" id="'+fundId+'" data-fundId="'+fundId+'" data-fundHouseId="'+fundHouseId+'">';
	    	}else{
	    		html +=
					'<tr data-amount="'+referenceCost+'" data-unit="'+holdingUnit+'" data-weight="'+allocationRate+'" id="'+fundId+'" data-fundId="'+fundId+'" data-fundHouseId="'+fundHouseId+'">';
	    	}
	    	html +=
            '<td width="20%" class="portfolio_tables_fnames portfolio_td"><a href="javascript:;">'
            + fundName
            +'</a>'
            +'<div class="portfolio_tables_nameWord">'
	    	+'<p class="portfolio_tables_risk">'
	    	+ riskLevel
	    	+' risk</p>'
	    	+'<p class="portfolio_tables_risk">'
	    	+ fundType
	    	+'</p>'
            +'</div></td>'
            +'<td width="5%" class="portfolio-tdcenter">'
            + fundCurrencyCode
            +'</td>'
            +'<td width="10%" class="portfolio-tdcenter">';
            if(lastNav != ''){
            	html +=
            		'<span id="span-lastNav" class="conversion-flag">'
        		+ formatCurrency(lastNav)
        		+'</span>'
            	+'&nbsp;<span class="to-currency">'
            	+ toCurrency
            	+'</span>';
            }
			html +=
            '</td>'
			+'<td width="10%" class="portfolio-tdcenter" id="tdHoldingUnit-'+fundId+'">'
			+ holdingUnit
			+'</td>'
            +'<td width="15%" class="portfolio-tdcenter col-md-2" id="availableUnit-'+fundId+'">'
            + availableUnit
            +'</td>';
            if(product == null){
        		html +=
            	'<td width="10%" class="portfolio-tdcenter col-md-2" id="tdReferenceCost-'+fundId+'">';
        		if(referenceCost != ''){
                	html +=
                		'<span id="span-lastNav" class="conversion-flag">'
            		+ formatCurrency(referenceCost)
            		+'</span>'
                	+'&nbsp;<span class="to-currency">'
                	+ toCurrency
                	+'</span>';
                }
            }else{
            	html +=
            	'<td width="10%" class="portfolio-tdcenter col-md-2" data-reference-cost="'+referenceCost+'" id="tdReferenceCost-'+fundId+'">';
            	if(referenceCost != ''){
                	html +=
                		'<span id="span-lastNav" class="conversion-flag">'
            		+ formatCurrency(referenceCost)
            		+'</span>'
                	+'&nbsp;<span class="to-currency">'
                	+ toCurrency
                	+'</span>';
                }
            }
			if(product != null){
				html += '<br>'
                + '(' + allocationRate + '%)';
			}
			html +=
             '</td>'
            +'<td width="10% class="portfolio-tdcenter td-newWeight" data-actual-cost="'+actualCost+'" data-averageCost="'+lastNav+'" data-td="newWeight" id="td-allocationRate-'+fundId+'" style="color:blue">'
            +'</td>'
            +'<td width="20% class="portfolio-tdcenter">';
			if(product == null){
				html +=
					'<a href="javascript:void(0);"><span class="glyphicon glyphicon-remove icon-remove" style="color:red">&nbsp;</span></a>';
			}else{
				html +=
					 '<div data-nav="'+lastNav+'" data-availableUnit="'+availableUnit+'" data-holdingUnit="'+holdingUnit+'" data-referenceCost="'+actualCost+'" id="td-actions-'+fundId+'">'
					+'<a href="javascript:void(0);"><span class="glyphicon glyphicon-plus icon-overweight" style="color:green">&nbsp;</span></a>'           
	                +'<a href="javascript:void(0);"><span class="glyphicon glyphicon-minus icon-reduction" style="color:red">&nbsp;</span></a>'              
	                +'<a href="javascript:void(0);"><span data-productId="'+productId+'" class="glyphicon glyphicon-refresh icon-conversion" style="color:gray">&nbsp;</span></a>'
	                +'</div>';	
			}
			html += '</td></tr>';
		}
	});	
	return html;
}
function loadScroll(){
	$('.space-mask-wrap').css('top',$(window).height()*0.1+$(document).scrollTop());
}
//转换
function fundConversion(selt){
	//获取可转入的基金信息
	getFundIn();
	$('#outFundOption').text('');
	$('#outFundOption').attr('value','');
	$('#available-value').text('');
	$('#unit-hold-value').text('');
	var windowHeight1 = $(window).height()*0.1+$(document).scrollTop();
	$('.space-mask-wrap').css('top',windowHeight1);
	$(window).on('scroll',loadScroll);
	//$("#order-plan-fund-switch").toggleClass("ifa-space-active");
	var fundId = $(selt).closest('tr').attr('id'),
		fundName = $(selt).closest('tr').children().eq(0).find('.product-fund-name').text(),
		holdingUnit = $(selt).closest('tr').find('.product-hold-unit').text(),
		availableUnit = $(selt).closest('tr').find('.product-available-unit').text();
	$('#outFundOption').text(fundName);
	$('#outFundOption').attr('value',fundId);
	var unitRedeems = $('#'+fundId).closest('tbody').find('.product-unit-redeem');
	$.each(unitRedeems,function(i,n){
		var unitRedeem = $(n).text();
		availableUnit = Number(availableUnit) - Number(unitRedeem);
	});
	$('#available-value').text(availableUnit);
	$('#available-value').attr('data-value',availableUnit);
	$('#unit-hold-value').text(holdingUnit);
}
//取消
function cansel(){
	     var fundId = $(this).parent().attr('data-fundId');
	 var weight = $(this).parent().attr('data-weight');
	 //原权重
	 if(typeof(weight)!='undefined' && weight!=''){
		 $('#td-allocationRate-'+fundId).text(weight);
	 }
	 $('#td-actions-'+fundId).show();
	 $(this).closest('tr').remove();
	 computeWeight();
}
//移除
function remove(){
	$(this).closest('tr').remove();
}
//新增
$(document).on('click','#popupWinReturn',function addfund(){
	var fundIds = $('#ids').val();
	var toCurrencyCode = $('#portfolio-currency').attr('data-code');
	var fundMaps = [];
	if(fundIds.indexOf(',') > -1){
		var tableFundIds = fundIds.split(',').unique();
		$.each(tableFundIds,function(i,n){
			var alTr = $('.portfolio-table').find('#'+n);
			if(alTr.length > 0){
				layer.msg('【'+alTr.find('.product-fund-name').text()+'】'+props['order.plan.rebalance.alert.alreadyExists']);
			}else{
				var fundMap = {};
    			fundMap['fund'] = n;
    			fundMaps.push(fundMap);
			}
		});
	}else{
		var alTr = $('.portfolio-table').find('#'+fundIds);
		if(alTr.length > 0){
			layer.msg('【'+alTr.find('.product-fund-name').text()+'】'+props['order.plan.rebalance.alert.alreadyExists']);
		}else{
			var fundMap = {};
    		fundMap['fund'] = fundIds;
    		fundMaps.push(fundMap);
		}
	}
	var url = base_root+'/front/portfolio/arena/getFunds.do?dateStr=' + new Date().getTime();
	$.ajax({
		type:'post',
		data:{
			fundIds:encodeURI(JSON.stringify(fundMaps)),
		    toCurrency:toCurrencyCode
		},
		url: url,
		success:
			function(result){
				if(result.flag){
					//后面添加的基金的id集合
					fundIds = result.fundIds == null? '':result.fundIds;
					$('#ids').val(fundIds);
					var fundInfoList = result.fundInfoList == null? '':result.fundInfoList;
					//table
					var html = ProductTable(fundInfoList);
					//添加列表数据
					$('#portfolio-table tbody').append(html);
					//refashData();
					computeWeight();
					addProduct(fundInfoList);
				}
			}
	});
});
//新增基金修改权重
function addFundWeight(){
	var value = $(this).val();
	var totalMarketValue = $('#hidTotalMarketValue').val();
	var allocationRate = '';
	if(null != value && typeof(value) != 'undefined'){
		allocationRate = parseFloat(value/totalMarketValue*100).toFixed(2);
	}
	$(this).closest('tr').children().each(function(){
		if('newWeight' == $(this).data('td')){
			if(allocationRate == '' || value == 0){
				$(this).text('');
			}else{
				$(this).text(allocationRate);
			}
		}
	});
}
//table 转换时，add fund
$(document).on('click','#btnAddConversionFund',function(){
	var inFunds = $.parseJSON($('#inFunds').val());
	var fundIds = '';
	var outFundId = '';
	$.each(inFunds,function(i,n){
		outFundId = n.outFundId;
		if(null != n.inFundId && '' != n.inFundId){
			fundIds += n.inFundId + ',';
		}
	});
	if('' != fundIds){
		fundIds = fundIds.substring(0,fundIds.length-1);
	}
	var url = base_root+'/front/portfolio/arena/getFunds.do?dateStr=' + new Date().getTime();
	$.ajax({
		type:'post',
		data:{fundIds:fundIds},
		url: url,
		success:
			function(result){
				if(result.flag){
					var fundInfoList = result.fundInfoList == null? '':result.fundInfoList;
					var html = '';
			    	//持仓对象
					// var product = '';
					$.each(fundInfoList,function(i,n){
						var fundInfo = null;
						//多语言
						if('en' == lang){
							fundInfo = n.fundInfoEn;
						}else if('tc' == lang){
							fundInfo = n.fundInfoTc;
						}else{
							fundInfo = n.fundInfoSc;
						}
						if(null != fundInfo){
							var fundName = fundInfo.fundName == null?'':fundInfo.fundName;
							var riskLevel = '';
							var minSubscribeAmount = '';
							var lastNav = '';
							var fundId = '';
							if(null != n.fundInfo){
								riskLevel = n.fundInfo.riskLevel == null?'':n.fundInfo.riskLevel;
								minSubscribeAmount = n.fundInfo.minSubscribeAmount == null?'':n.fundInfo.minSubscribeAmount;
								lastNav = n.fundInfo.lastNav == null?'':n.fundInfo.lastNav;
								fundId = n.fundInfo.id == null?'':n.fundInfo.id;
							}
							var fundCurrencyCode = fundInfo.fundCurrencyCode == null ? '' : fundInfo.fundCurrencyCode;
							var fundHouseId = n.fundHouseId == null? '' : n.fundHouseId;
							$('#portfolio-table tbody tr').each(function(){
								if(fundId == $(this).attr('id')){
									$(this).remove();
								}
							});
							html +=
							'<tr id="'+fundId+'" data-fundHouseId="'+fundHouseId+'" data-outfundid="'+outFundId+'">'
			                +'<td class="portfolio_tables_fnames portfolio_td" style="padding-left: 50px;"><a style="color:#9f9f9f;float: right;" href="javascript:;">'
			                + fundName
			                +'</a>'
			                +'</td>'
			                +'<td style="color:#9f9f9f" class="portfolio-tdcenter">'
			                + riskLevel
			                +'</td>'
			                +'<td style="color:#9f9f9f" class="portfolio-tdcenter">'
			                + fundCurrencyCode
			                +'</td>'
			                +'<td style="color:#9f9f9f" class="portfolio-tdcenter">'
			                + minSubscribeAmount;
							if('' != minSubscribeAmount){
								html += '&nbsp;'+ fundCurrencyCode;
							}
							html +=
			                '</td>'
			                +'<td id="td-lastNav-'+fundId+'" data-average-cost="'+lastNav+'" style="color:#9f9f9f" class="portfolio-tdcenter">'
			                + lastNav;
							if('' != lastNav){
								html += '&nbsp;'+ fundCurrencyCode;
							}
							html +=
			                '</td>'
							+'<td id="unit-redeem-'+fundId+'" colspan="3" class="portfolio-tdcenter">'
				            +'</td>'	
			                +'<td style="color:blue" class="portfolio-tdcenter td-newWeight" data-td="newWeight" id="td-allocationRate-'+fundId+'" style="color:blue">'
			                +'</td>'
			                +'<td class="portfolio-tdcenter">'
							+'<a href="javascript:void(0);"><span class="glyphicon glyphicon-remove icon-remove" style="color:red">&nbsp;</span></a>';
							html += '</td></tr>';
						}
					});
					//添加列表数据
					$('#'+outFundId).after(html);
					$(document).on('click','.icon-remove',remove);//移除
					$.each(inFunds,function(i,n){
			    		if(null != n.inFundId && '' != n.inFundId){
			    			var averageCost = $('#td-lastNav-'+n.inFundId).attr('data-averageCost');
			    			var referenceCost = parseFloat(n.unitRedeem*averageCost).toFixed(2);
			    			var totalMarketValue = $('#hidTotalMarketValue').val();
			    			var newWeight = parseFloat(n.unitRedeem*averageCost/totalMarketValue*100).toFixed(2);
			    			$('#td-allocationRate-'+n.inFundId).empty().append(newWeight);
			    			var weight = $('#'+outFundId).data('weight');
							var unit = $('#'+outFundId).data('unit');
							var amount = $('#'+outFundId).data('amount');
			    			var tdtext = '<div class="unit_redeem conversion_unit_redeem" data-outfund-amount="'+amount+'" data-outfund-unit="'+unit+'" data-outfund-weight="'+weight+'" id="conversionUnitRedeem" data-unit-redeem="'+n.unitRedeem+'" data-reference-cost="'+referenceCost+'" data-new-weight="'+newWeight+'" >'+props['order.plan.rebalance.switch.unitRedeem']+' '+n.unitRedeem+'</div><br><div style="color:#9f9f9f">'+referenceCost+' USD</div>';
			    			$('#unit-redeem-'+n.inFundId).empty().append(tdtext);
			    		}
			    	});
				}
			}
	});	
});
//new Weight 重新赋值
function computeWeight(fundId,newReferenceCost,differenceCost){
	var referenceCostTotal = positionsData.referenceCostTotal;
	if(typeof(differenceCost)!='undefined'){
		referenceCostTotal = referenceCostTotal+differenceCost;
	}
	$('.td-newWeight').each(function(){
		var id = $(this).closest('tr').attr('id');
		if($('#td-actions-'+id).is(':hidden')){
			var cost = $(this).data('differenceCost');
			if(typeof(cost) != 'undefined'){
				referenceCostTotal = referenceCostTotal + cost;
			}
		}
	});
	$('.td-newWeight').each(function(){
		var actualCost = $(this).data('actualCost');
		var tempCost = $(this).data('tempCost');
		var id = $(this).closest('tr').attr('id');
		var weight = '';
		if(fundId == $(this).closest('tr').attr('id') && typeof(actualCost)=='undefined'){
			weight = parseFloat(newReferenceCost/referenceCostTotal*100).toFixed(2);
			$(this).attr('data-temp-cost',newReferenceCost);
			$(this).text(weight+'%');
	        $('#td-actions-'+id).attr('data-weight',weight);
		}else if(fundId == $(this).closest('tr').attr('id') && typeof(actualCost)!='undefined'){
			$(this).attr('data-difference-cost',differenceCost);
		}else if(typeof(tempCost)!='undefined' && typeof(actualCost)=='undefined'){
			weight = parseFloat(tempCost/referenceCostTotal*100).toFixed(2);
			$(this).text(weight+'%');
	        $('#td-actions-'+id).attr('data-weight',weight);
		}else if(!$('#td-actions-'+id).is(':hidden')){
			weight = parseFloat(actualCost/referenceCostTotal*100).toFixed(2);
			$(this).text(weight+'%');
        	$('#td-actions-'+id).attr('data-weight',weight);
		}
	});
	computeTotalWeight();
};
//计算总权重
function computeTotalWeight(){
	var totalWeight = 0;
    $('.td-newWeight').each(function(){
    	var weight = parseFloat($(this).text());
    	totalWeight = parseFloat(totalWeight+weight);
	});
};
//next
$('#btnNext').click(function(){
	fundMixerNext(this);
});
function fundMixerNext(selt){
	if($('.transformation-wrap').find('.action-original').length<1){
		layer.msg(prop['order.plan.rebalance.no.product.adjusted']);
		return;
	}
	if(!addAmountValid()){
		return;
	}
	var productInput = false;
	$('.product-table-input').each(function(){
		var value = $(this).val();
		if(value){
			value = value.replace(/,/g,'');
			value = Number(value);
			if(isNaN(value) || value <= 0){
				productInput = true;
				layer.msg(props['order.plan.alert.input.incorrect']);
				$(this).focus();
				return false;
			}
		}else{
			productInput = true;
			layer.msg(props['order.plan.alert.input.incorrect']);
			$(this).focus();
			return false;
		}
	});
	if(productInput){
		return;
	}
	var totalWeight = $('.product-total-weight').text();
	totalWeight = totalWeight.replace('%','').replace(/,/g,'');
	if(Number(totalWeight)<100){
		layer.msg(prop['order.plan.rebalance.alert.totalWeight.limit']);
		return;
	}
	var empty = false;
	$('.product-tbody-valid-add').each(function(){
		if(!$(this).val() || Number($(this).val())==0){
			layer.msg(prop['order.plan.rebalance.alert.new.product.empty']);
			empty = true;
			return false;
		}
	});
	if(empty){return;}
	var orderPlans = [];
	$('.portfolio-table tbody:gt(0)').each(function(i,n){
		var orderPlan = {};
		var outFundId = $(n).children().eq(0).attr('id');
		var fundId = '';
		var unit = '';
		var amount = '';
		if($(n).children().length > 1){
			var switchTotal = 0;
			var switchGroup = getGroupNo();
			$.each($(n).children(':gt(0)'),function(j,m){
				if($(n).hasClass('tbody-total')){
					return;
				}
				fundId = $(m).attr('id');
				if(typeof(fundId) != 'undefined'){
					if(j==1){return;}
					//转换
					orderPlan['unit'] = unit;
					orderPlan['amount'] = amount;
					orderPlan['fundId'] = fundId;
					$.each($(n).children(':gt(0)'),function(x,y){
						var redeemUnit = $(y).find('.product-unit-redeem').text();
						if(redeemUnit != ''){
							amount = $(y).find('.product-redeem-price span:eq(0)').text();
							amount = amount.replace(/,/gm,'');
							switchTotal = switchTotal + Number(redeemUnit);
							var orderPlan = {};
							orderPlan['fundId'] = $(y).attr('id');
							orderPlan['tranType'] = 'B';
							orderPlan['amount'] = amount;
							orderPlan['amountAdjust'] = amount;
							orderPlan['weight'] = 0;
							var weightAdjust = $(y).find('.product-new-weight').text();
							weightAdjust = Number(weightAdjust.replace('%',''));
							orderPlan['weightAdjust'] = weightAdjust;
							orderPlan['switchFlag'] = '1';
							orderPlan['switchGroup'] = switchGroup;
							orderPlan['original'] = '0';
							orderPlan['fromProductId'] = outFundId;
							orderPlans.push(orderPlan);
						}
					});
				}else{
					fundId = outFundId;
					var inputVal = $(n).children().eq(1).find('.product-table-input').val();
					if(typeof(inputVal) != 'undefined'){
						inputVal = Number(inputVal.trim().replace(/,/gm,''));
						var redeemAmount = $(n).children().eq(1).find('.product-redeem-amount').text();
						redeemAmount = redeemAmount.replace(/,/gm,'');
						if(redeemAmount != ''){
						//减
							unit = inputVal;
							orderPlan['tranType'] = 'S';
							var holdUnit = $(n).children().eq(0).find('.product-hold-unit').text();
							orderPlan['unitAdjust'] = Number(holdUnit)-Number(unit);
						}else{
						//增
							amount = inputVal;
							orderPlan['tranType'] = 'B';
							
							var holdingAmount = $(n).children().eq(0).find('.product-hold-amount').text();
							holdingAmount = holdingAmount.replace(/,/gm,'');
							orderPlan['amountAdjust'] = Number(holdingAmount)+amount;
						}
						orderPlan['switchFlag'] = '0';
						orderPlan['original'] = '0';
						orderPlan['fromProductId'] = outFundId;
						
						var weight = $(n).children().eq(0).find('.product-old-weight').text();
						weight = Number(weight.replace('%',''));
						orderPlan['weight'] = weight;
						
						var weightAdjust = $(n).children().eq(0).find('.product-new-weight').text();
						weightAdjust = Number(weightAdjust.replace('%',''));
						orderPlan['weightAdjust'] = weightAdjust;
					}
					orderPlan['unit'] = unit;
					orderPlan['amount'] = amount;
					orderPlan['fundId'] = fundId;
					if(typeof(fundId)!='undefined' && fundId != ''){
						orderPlans.push(orderPlan);
					}
				}
			});
			if(switchTotal != 0 && $(n).children(':eq(1)').hasClass('product-tr-transformation')){
				var orderPlan = {};
				orderPlan['fundId'] = $(n).children(':eq(0)').attr('id');
				orderPlan['tranType'] = 'S';
				orderPlan['unit'] = switchTotal;
				orderPlan['unitAdjust'] = switchTotal;
				var weight = $(n).children(':eq(0)').find('.product-old-weight').text();
				weight = Number(weight.replace('%',''));
				orderPlan['weight'] = weight;
				var weightAdjust = $(n).children(':eq(0)').find('.product-new-weight').text();
				weightAdjust = Number(weightAdjust.replace('%',''));
				orderPlan['weightAdjust'] = weightAdjust/100;
				orderPlan['switchFlag'] = '1';
				orderPlan['switchGroup'] = switchGroup;///
				orderPlan['original'] = '0';
				orderPlan['fromProductId'] = $(n).children(':eq(0)').attr('id');
				orderPlans.push(orderPlan);
			}
		}else{
			if(!$(n).hasClass('action-original')){
				return;
			}
			fundId = outFundId;
			var lastNav = $(n).children().eq(0).find('.product-table-input').val();
			var holdingAmount = $(n).children().eq(0).find('.product-hold-amount').text();
			holdingAmount = holdingAmount.replace(/,/gm,'');
			orderPlan['amount'] = holdingAmount;
			orderPlan['original'] = '1';
			// 新增
			if(typeof lastNav != 'undefined'){
				if(!lastNav){layer.msg(props['order.plan.rebalance.alert.new.product.empty']);return;}
				orderPlan['amount'] = lastNav.replace(/,/gm,'');
				orderPlan['amountAdjust'] = lastNav.replace(/,/gm,'');
				orderPlan['original'] = '0';
				orderPlan['tranType'] = 'B';
			}
			var weight = $(n).children().eq(0).find('.product-old-weight').text();
			weight = Number(weight.replace('%',''));
			orderPlan['weight'] = weight;
			var weightAdjust = $(n).children().eq(0).find('.product-new-weight').text();
			weightAdjust = Number(weightAdjust.replace('%',''));
			orderPlan['weightAdjust'] = weightAdjust;
			var holdUnit = $(n).children().eq(0).find('.product-hold-unit').text();
			orderPlan['unit'] = holdUnit;
			orderPlan['fundId'] = fundId;
			if(typeof(fundId)!='undefined' && fundId != ''){
				orderPlans.push(orderPlan);
			}
		}
	});
	var data = {
		planId:getUrlParam('planId'),
		portfolioName:$('#txtProposalName').val(),
		currencyCode:$('#portfolio-currency').attr('data-code'),
		orderPlanData:encodeURI(JSON.stringify(orderPlans))
	};
		var url = base_root+'/front/tradeRebalance/saveOrderPlanData.do?holdId='+ getUrlParam('id');
		$.ajax({
			type:'post',
			url:url,
			data:data,
			success:function(re){
				if(re.flag){
					var planId = re.planId;
					$.post(base_root + '/front/tradeStep/updateAipState.do?d='+new Date().getTime(),{planId:planId,state:0},function(){
						window.location.href = base_root+'/front/tradeRebalance/rebalanceCommission.do?planId=' + planId;
		    		});
				}else{
					layer.msg(props['global.operation.failed']);
				}
			},
			error:function(){
				layer.msg(props['global.operation.failed']);
			}
		});
    } 
	//获取可转入的基金信息
	function getFundIn(){
		var fundId = $('#outFundOption').attr('value');
		var currencyCode = $('#portfolio-currency').attr('data-code');
 		var url = base_root+'/front/tradeRebalance/getFundIn.do';
 		$.ajax({
 	          url: url,
 	          type: 'POST',
 	          data:{
 	        	  fundId:fundId,
 	        	  toCurrency:currencyCode
 	          },
 	          success:function(result){
 	        	  if(result.flag){
 	  				var fundVOs = result.fundInfoDataVOs;
 	  				var optionHTML = '';
 	  				var tableHtml = '';
 	  				//判断是否有可转换的基金
 	  				if(fundVOs.length <= 1){
 						layer.msg(props['order.plan.rebalance.switch.empty.product']);
 						$('#txtUnitRedeem').val('');
 						$("#order-plan-fund-switch").removeClass("ifa-space-active");
 	  				}else{
 	  					$("#order-plan-fund-switch").addClass("ifa-space-active");
 	  					$.each(fundVOs,function(i,n){
 	  					//排除自身
 	  					if(n.fundId != fundId){
 								var fundInfo = null;
 								//多语言
 								if('en' == lang){
 									fundInfo = n.fundInfoEn;
 								}else if('tc' == lang){
 									fundInfo = n.fundInfoTc;
 								}else{
 									fundInfo = n.fundInfoSc;
 								}
 								var fundName = '';
 								//基金类型
 								var fundType = '';
 		    					if(fundInfo != null){
 		    						fundName = fundInfo.fundName == null?'':fundInfo.fundName;
 		    						fundType = fundInfo.fundType == null?'':fundInfo.fundType;
 		    					}
 		    					//最新净值
 		    					var lastNav = n.fundInfo == null? '' : n.fundInfo.lastNav;
 		    					var riskLevel = n.fundInfo == null? '' : n.fundInfo.riskLevel;
 		    					optionHTML +=
 			    					'<option id="'+n.fundId+'" data-toCurrency-name="'+n.toCurrencyName+'" data-risk-level="'+riskLevel+'" data-fundCurrencyCode="'+n.fundCurrency+'" data-fundType="'+fundType+'" data-lastNav="'+lastNav+'">'
 			    					+ fundName
 			    					+'</option>';
 			    				//是否是重新转换	
 								if('undefined' != typeof($('#inFunds').val()) && '' != $('#inFunds').val()){
 									var inFunds = eval($('#inFunds').val());
 									$.each(inFunds,function(x,y){
 										if(y.outFundId == $('#outFundOption').attr('value')){
 											$.each(inFunds,function(i,m){
 												if(m.inFundId == n.fundId){
 													var outFundName = $('#outFundOption').text();
 													var outFundlastNav = $('.transformation-wrap').find('#'+y.outFundId).find('.product-last-nav').text();
 													outFundlastNav = outFundlastNav.replace(/,/g,'');
 													var refAmount = outFundlastNav*m.unitRedeem;
 													var refUnit = parseFloat(refAmount/m.lastNav).toFixed(2);
 													var toCurrencyName = $('#portfolio-currency').val();
 													tableHtml += 
 														'<tr data-toCurrencyName="'
 										   				  +m.toCurrencyName+'" data-lastNav="'
 										   				  +m.lastNav+'" data-currency="'
 										   				  +m.currency+'" data-fundType="'
 										   				  +m.fundType+'" data-riskLevel="'
 										   				  +m.riskLevel+'" data-fundId="'
 										   				  +m.fundId+'" data-unitRedeem="'
 										   				  +m.unitRedeem+'" data-inFundId="'
 										   				  +m.inFundId+'">'
 								                          +'<td class="al-out-product">'
 								                          + outFundName
 								                          +'</td>'
 								                          +'<td><span class="conversion-flag">'
 									                      + formatCurrency(outFundlastNav)
 									                      +'</span><span style="margin-left:5px;">'
 									                      + toCurrencyName
 									                      +'</span></td>'
 								                          +'<td class="switch-unit-redeem">'
 								                          + m.unitRedeem
 								                          +'</td>'
 								                          +'<td><span class="conversion-flag">'
 									                      + formatCurrency(refAmount)
 									                      +'</span><span style="margin-left:5px;">'
 									                      + toCurrencyName
 									                      +'</span></td>'
 								                          +'<td>'
 								                          + fundName
 								                          +'</td>'
 								                          +'<td>'
 									                      + refUnit
 									                      +'</td>'
 								                          +'<td>'
 								                          + '<a class="removeFund" href="javascript:void(0);"><img class="switch-ico" src="'+base_root+'/res/images/fund/delete_ico.png"></a>'
 								                          +'</td>'
 								                          +'</tr>';
 												}
 											});
 										}
 									});
 								}
 							}
 	  				});
 	  				$('.switch_funds_tables_tbody tr:gt(1)').remove();
 	  				if(tableHtml != ''){
 	  					$('.switch_table_tbody_tip').hide();
 	  				}
 					$('.switch_funds_tables_tbody').append(tableHtml);
 	                $(document).on('click','.removeFund',function(){
 	                	if($(this).closest('tbody').find('tr').length == 3){
 	                		$('.switch_table_tbody_tip').show();
 	                	}
 	                	$(this).closest('tr').remove();
 	                	$('#btnSave').show();
 	            	});
 	  				$('#fund-in-sel').empty().append(optionHTML);
 	    			var fundCurrencyCode = $('#fund-in-sel').find('option:selected').attr('data-fundCurrencyCode');
 	    			var fundType = $('#fund-in-sel').find('option:selected').attr('data-fundType');
 	    			var lastNav = $('#fund-in-sel').find('option:selected').attr('data-lastNav');
 	    			var toCurrencyName = $('#fund-in-sel').find('option:selected').attr('data-toCurrency-name');
 	    			toCurrencyName = toCurrencyName == 'null'?'':toCurrencyName;
 	    			$('#txtCurrency').text(fundCurrencyCode);
 	    			$('#txtFundType').text(fundType);
 	    			$('#txtLatestNAV').text(lastNav);
 	    			$('.product-in-fund-toCurrency').text(toCurrencyName);
 	  				}
 	  			}else{
 	  				layer.msg(props['order.plan.rebalance.switch.empty.product']);
 	  				$('#txtUnitRedeem').val('');
 	  				$("#order-plan-fund-switch").removeClass("ifa-space-active");
 	  			}
 	        	$('#fund-in-sel').change(function() {
 	    			var fundCurrencyCode = $('#fund-in-sel').find('option:selected').attr('data-fundCurrencyCode');
 	    			var fundType = $('#fund-in-sel').find('option:selected').attr('data-fundType');
 	    			var lastNav = $('#fund-in-sel').find('option:selected').attr('data-lastNav');
 	    			var toCurrencyName = $('#fund-in-sel').find('option:selected').attr('data-toCurrency-name');
 	    			toCurrencyName = toCurrencyName == 'null'?'':toCurrencyName;
 	    			$('#txtCurrency').text(fundCurrencyCode);
 	    			$('#txtFundType').text(fundType);
 	    			$('#txtLatestNAV').text(lastNav);
 	    			$('.product-in-fund-toCurrency').text(toCurrencyName);
 	    		});
 	          },error:function(){
 	        	  layer.msg(props['order.plan.rebalance.switch.empty.product']);
 	        	  $('#txtUnitRedeem').val('');
 	      		  $("#order-plan-fund-switch").removeClass("ifa-space-active");
 	          }	
 	        });
 			};
 		//关闭转换窗
 		$('#btnCansel').click(function(){
 			$('#txtUnitRedeem').val('');
 			$("#order-plan-fund-switch").removeClass("ifa-space-active");
 		});
 		//按比例转换份额
 		$('.switch-written-ul >li').click(function(){
 			var quit = $(this).attr('value'),
 				availableUnit = parseInt($('#available-value').attr('data-value'));
 			var fundId = $('#fund-in-sel').find('option:selected').attr('id');
 			$('.switch-unit-redeem').each(function(){
 				if(fundId != $(this).closest('tr').attr('data-infundid')){
 					availableUnit = availableUnit - Number($(this).text());
 				};
 			});
 			if(quit==1){
 				$('#available-value').text(0);
 				$('#txtUnitRedeem').val(availableUnit);
 				$('#txtAvailableUnit').val(0);
 			}else{
 				//$('#available-value').attr('data-value',parseInt(availableUnit-availableUnit/quit));
 				$('#available-value').text(parseInt(availableUnit-availableUnit/quit));
 				$('#txtUnitRedeem').val(parseInt(availableUnit/quit));
 			}
 		});
 		//转换操作
 		$(document).on('click','#btnSwitch',function(){
 			var inFundId = $('#fund-in-sel').find('option:selected').attr('id'),
 				inFundName = $('#fund-in-sel').find('option:selected').text(),
 				riskLevel = $('#fund-in-sel').find('option:selected').attr('data-risk-level'),
 				fundType = $('#txtFundType').text(),
 				currency = $('#txtCurrency').text(),
 				lastNav = $('#txtLatestNAV').text(),
 				toCurrencyName = $('#fund-in-sel').find('option:selected').attr('data-toCurrency-name'),
 				fundId = $('#outFundOption').attr('value'),
 				fundName = $('#outFundOption').text(),
 				unitRedeem = $('#txtUnitRedeem').val();
 			var availableUnit = 0;
 			var fundFlag = true;
 			$('.switch-unit-redeem').each(function(){
 				availableUnit = availableUnit + Number($(this).text());
 				if(inFundId == $(this).closest('tr').attr('data-infundid')){
 					fundFlag = false;
 				};
 			});
 			if($('#available-value').attr('data-value') == availableUnit && fundFlag){
 				layer.msg(props['order.plan.rebalance.alert.no.holding.available']);
 			}else if(!(!!unitRedeem)){
 				layer.msg(props['order.plan.rebalance.alert.switch.enter.unitRedeem']);
 			}else{
 			var flag = true;
 			//判断table是否存在该基金 
 			$('#fundSwitchTable tbody tr:gt(1)').each(function(){
 				if(inFundId == $(this).attr('data-inFundId') && 
 					unitRedeem == $(this).attr('data-unitRedeem')){
 					flag = false;
 					layer.msg('【'+inFundName+'】 ' + props['order.plan.rebalance.alert.alreadyExists']);
 					return false;
 				}else if(inFundId == $(this).attr('data-inFundId') && 
 					unitRedeem != $(this).attr('data-unitRedeem')){
 					$(this).remove();
 					return false;
 				}
 			});
 			var outFundlastNav = $('.transformation-wrap').find('#'+fundId).find('.product-last-nav').text();
 			outFundlastNav = outFundlastNav.replace(/,/g,'');
 			var refAmount = outFundlastNav*unitRedeem;
 			var refUnit = parseFloat(refAmount/lastNav).toFixed(2);
 			//if($('.al-out-product').length>0){fundName = '';}
 			if(flag){
 				var tableHtml = '';
 	   			tableHtml += '<tr data-toCurrencyName="'
 		   				  +toCurrencyName+'" data-lastNav="'
 		   				  +lastNav+'" data-currency="'
 		   				  +currency+'" data-fundType="'
 		   				  +fundType+'" data-riskLevel="'
 		   				  +riskLevel+'" data-fundId="'
 		   				  +fundId+'" data-unitRedeem="'
 		   				  +unitRedeem+'" data-inFundId="'
 		   				  +inFundId+'">'
 	                      +'<td class="al-out-product">'
 	                      + fundName
 	                      +'</td>'
 	                      +'<td><span class="conversion-flag">'
 	                      + formatCurrency(outFundlastNav)
 	                      +'</span><span style="margin-left:5px;">'
 	                      + toCurrencyName
 	                      +'</span></td>'
 	                      +'<td class="switch-unit-redeem">'
 	                      + unitRedeem
 	                      +'</td>'
 	                      +'<td><span class="conversion-flag">'
 	                      + formatCurrency(refAmount)
 	                      +'</span><span style="margin-left:5px;">'
 	                      + toCurrencyName
 	                      +'</span></td>'
 	                      +'<td>'
 	                      + inFundName
 	                      +'</td>'
 	                      +'<td>'
 	                      + refUnit
 	                      +'</td>'
 	                      +'<td>'
 	                      +'<a class="removeFund" href="javascript:void(0);"><img class="switch-ico" src="'+base_root+'/res/images/fund/delete_ico.png"></a>'
 	                      +'</td>'
 	                      +'</tr>';
 	   			if(tableHtml){
 	   				$('.switch_table_tbody_tip').hide();
 	   				$('#btnSave').show();
 	   			}
 	            $('#fundSwitchTable tbody').append(tableHtml);
 	            $('.removeFund').on('click',function(){
 	            	if($(this).closest('tbody').find('tr').length == 3){
 	            		$('.switch_table_tbody_tip').show();
 	            	}
 	            	$(this).closest('tr').remove();
 	            	var switchUnit = $(this).closest('tr').find('.switch-unit-redeem').text();
 	            	var available = $('#available-value').text();
 	            	available = Number(available) + Number(switchUnit);
 	            	$('#available-value').text(available);
 	            	$('#btnSave').show();
 	            });
 			}
 			}
 			$('#txtUnitRedeem').val('');
 		});
 		$(document).on('click','#btnSave',function(){
 			var inFunds = [];
 			$('#fundSwitchTable tbody tr:gt(0)').each(function(i){
 				var inFund = {};
 				inFund['outFundId'] = $(this).attr('data-fundId');
 				inFund['inFundId'] = $(this).attr('data-inFundId');
 				inFund['unitRedeem'] = $(this).attr('data-unitRedeem');
 				
 				inFund['toCurrencyName'] = $(this).attr('data-toCurrencyName');
 				inFund['lastNav'] = $(this).attr('data-lastNav');
 				inFund['currency'] = $(this).attr('data-currency');
 				inFund['fundType'] = $(this).attr('data-fundType');
 				inFund['riskLevel'] = $(this).attr('data-riskLevel');
 				inFund['name'] = $(this).children().eq(4).text();
 				inFunds[i] = inFund;
 			});
 			// JSON.stringify(inFunds)
 			if(''!=$('#inFunds').val() && 'undefined'!=typeof($('#inFunds').val())){
 				var oldInFunds = eval($('#inFunds').val());
 				var addFund = [];
 				$.each(oldInFunds,function(i,n){
 					$.each(inFunds,function(j,m){
 						if(n.inFundId != m.inFundId){
 							addFund.push(n);
 						}
 					});
 				});
 				$.each(oldInFunds,function(i,n){
 					if($.inArray( n, addFund ) == -1){
 						inFunds.push(n);
 					}
 				});
 				$('#inFunds').val(JSON.stringify(inFunds));	
 			}else{
 				$('#inFunds').val(JSON.stringify(inFunds));
 			}
 			actionConversion(inFunds);
 			$("#order-plan-fund-switch").removeClass("ifa-space-active");
 			$("#txtUnitRedeem").val('');
 		});
 		
 		/**********************************************************/
 		//增持
 		$('.action-add').click(function(){
 			actionAdd(this);
 		});
 		function actionAdd(selt){
 			var currencyName = $('#portfolio-currency').val();
 			var html = '';
 			html = 
 			'<tr class="action-original">'
 			+'<td class="portfolio-tdcenter" colspan="3"></td>'
 			+'<td class="portfolio-tdcenter" colspan="2" style="text-align: -webkit-right;">'
 			+'<img class="product-cart-ico" src="'+base_root+'/res/images/client/product_cart_ico.png"><span style="margin-left:20px">'+props['order.plan.create.portfolio.table.amount']+'</span>'
 			+'</td>'
 			+'<td class="portfolio-tdcenter">' 
 			+'<input class="product-table-input list-add-amount-flag" type="text" value="">'	
 			+'<p class="product-table-cur">'
 			+ currencyName
 			+'</p>'	
 			+'</td>' 
 			+'<td class="portfolio-tdcenter portfolio-table-blue"></td>'
 			+'<td class="portfolio-tdcenter">'
 			+'<div class="product-actions"><img class="product-off-ico action-recovery" src="'+base_root+'/res/images/client/product_off_ico.png"></div>'
 			+'</td>'
 			+'</tr>';
 			$(selt).closest('.product-actions').hide();
 			$(selt).closest('tbody').addClass('product-tbody-wrap');
 			$(selt).closest('tr').after(html);
 			$('.action-recovery').unbind('click');
 			$('.action-recovery').bind('click',function(){actionRecovery(this);});
 			//计算
 			$('.product-table-input').unbind('blur');
 			$('.product-table-input').on('blur',function(){
 				var selt = this;
 				var value = $(selt).val().trim();
 				value = value.replace(/,/gm,'');
 				$(selt).val(formatCurrency(value));
 				var currentNav = $(selt).closest('tbody').find('.product-last-nav').text();
 				currentNav = currentNav.replace(/,/gm,'');
 				//权重
 				reWeight();
 				caluRiskLevel();
 			});
 		}
 		$('.list-add-amount-flag').on('blur',function(){
 			var selt = this;
 			var value = $(selt).val().trim();
 			value = value.replace(/,/gm,'');
 			$(selt).val(formatCurrency(value));
 			var currentNav = $(selt).closest('tbody').find('.product-last-nav').text();
 			currentNav = currentNav.replace(/,/gm,'');
 			//权重
 			reWeight();
 			caluRiskLevel();
 		});
 		//转换
 		$('.action-conversion').click(function(){
 			$('#btnSave').hide();
 			fundConversion(this);
 		});
 		function actionConversion(inFunds){
 			var currencyName = $('#portfolio-currency').val();
 			var html = '';
 			$.each(inFunds,function(i,n){
 				var outFund = n.outFundId;
 				var riskLevel = n.riskLevel==null?'':n.riskLevel;
 				var lastNav = n.lastNav==null?'':n.lastNav;
 				var currency = n.currency==null?'':n.currency;
 				var unitRedeem = n.unitRedeem==null?'':n.unitRedeem;
 				var name = n.name==null?'':n.name;
 				var fundType = n.fundType==null?'':n.fundType;
 				var inFundId = n.inFundId==null?'':n.inFundId;
 				var outFundLav = $('#'+outFund).find('.product-last-nav').text();
 				outFundLav = Number(outFundLav.replace(/,/gm,''));
 				var amountRedeem = '';
 				if(unitRedeem!=''){
 					amountRedeem = Number(unitRedeem)*outFundLav;
 				}
 				html = 
 					'<tr id="'+inFundId+'" class="product-tr-transformation action-original">'
 			        +'<td class="portfolio_tables_fnames">'
 			        +'<img class="product-update-ico" src="'+base_root+'/res/images/client/product_update_ico.png">'
 			        +'<a href="javascript:;">'
 			        + name
 			        +'</a>'
 			        +'<div class="portfolio_tables_nameWord">'
 			        +'<p class="portfolio_tables_risk"><span class="product-risk-level">'
 			        + riskLevel
 			        +'</span>'
 			        + props['order.plan.rebalance.table.risk']
 			        +'</p>'
 			        +'<p class="portfolio_tables_risk" style="width:50%">'
 			        + fundType
 			        +'</p>'
 			        +'</div>'
 			        +'</td>'
 			        +'<td class="portfolio-tdcenter">'
 			        + currency
 			        +'</td>'
 			        +'<td class="portfolio-tdcenter"><span class="product-last-nav conversion-flag">'
 			        + formatCurrency(lastNav)
 			        +'</span><span class="to-currency">'
 			        + currencyName
 			        +'</span></td>'
 			        +'<td class="portfolio-tdcenter">N/A'
 			        +'</td>'
 			        +'<td class="portfolio-tdcenter product-available-unit" colspan="2">'
 			        +'<a class="product-redeem-button" href="javascirpt:;">'+props['order.plan.rebalance.switch.unitRedeem']+' <span class="product-unit-redeem">'
 			        + unitRedeem
 			        +'</span></a>'
 			        +'<p class="product-redeem-price"><span class="product-last-nav conversion-flag">'
 			        + formatCurrency(amountRedeem) 
 			        +'</span><span style="margin-left:3px" class="to-currency">'
 			        + currencyName
 			        +'</span>'
 			        +'</p>'
 			        +'</td>'
 			        +'<td class="portfolio-tdcenter portfolio-table-blue product-new-weight"></td>'
 			        +'<td class="portfolio-tdcenter">'
 			        +'<img class="product-del-ico action-delete" src="'+base_root+'/res/images/client/product_del_ico.png">'
 			        +'</td>'
 			        +'</tr>';
 				$('#'+outFund).find('.product-actions').hide();
 				if($('#'+outFund).find('.product-edit-ico').length == 0){
 					$('#'+outFund).find('.product-actions').after('<img class="product-edit-ico" src="'+base_root+'/res/images/client/product_edit_ico.png">');
 				}
 				$('#'+outFund).closest('tbody').addClass('product-tbody-wrap');
 				$('#'+outFund).closest('tbody').children(':gt(0)').each(function(){
 					if($(this).attr('id') == inFundId){
 						$(this).remove();
 					}
 				});
 				$('#'+outFund).closest('tr').after(html);
 				$('.action-delete').unbind('click');
 				$('.action-delete').bind('click',function(){actionDelete(this);});
 				
 				$('.product-edit-ico').unbind('click');
 				$('.product-edit-ico').click('click',function(){
 					$('#btnSave').hide();
 					fundConversion(this);
 				});
 			});
 			//权重
 			reWeight();
 			caluRiskLevel();
 		}
 		//减持
 		$('.action-reduction').click(function(){
 			actionReduction(this);
 		});
 		function actionReduction(selt){
 			var currencyName = $('#portfolio-currency').val();
 			var html = '';
 			html = 
 			'<tr class="action-original">'
 	    	+'<td class="portfolio-tdcenter" colspan="3"></td>'
 	    	+'<td class="portfolio-tdcenter">'
 	    	+'<img class="product-cart-ico product-reduce-ico" src="'+base_root+'/res/images/client/product_reduce_ico.png">'
 	    	+'<p class="product-table-word">'+props['order.plan.aip.detail.table.th.unit']+'</p>'
 	    	+'</td>'
 	    	+'<td class="portfolio-tdcenter">'
 	    	+'<input class="product-table-input input-action-reduction" type="text" value="">'
 	    	+'</td>'
 	    	+'<td class="portfolio-tdcenter">'
 	    	+'<p class="product-redeem-price"><span class="product-redeem-amount conversion-flag">0</span> <span class="to-currency">'
 	    	+ currencyName
 	    	+'</span></p>'
 	    	+'</td>'
 	    	+'<td class="portfolio-tdcenter portfolio-table-blue"></td>'
 	    	+'<td class="portfolio-tdcenter">'
 	    	+'<div class="product-actions"><img class="product-off-ico action-recovery" src="'+base_root+'/res/images/client/product_off_ico.png"></div>'
 	    	+'</td>'
 	    	+'</tr>';
 			$(selt).closest('.product-actions').hide();
 			$(selt).closest('tbody').addClass('product-tbody-wrap');
 			$(selt).closest('tr').after(html);
 			$('.action-recovery').unbind('click');
 			$('.action-recovery').bind('click',function(){actionRecovery(this);});
 			//计算
 			$('.product-table-input').unbind('blur');
 			$('.input-action-reduction').on('blur',function(){
 				var availableUnit = $(this).closest('tbody').find('.product-available-unit').text();
 				var value = $(this).val().trim();
 				if(Number(value) > Number(availableUnit)){
 					layer.msg(props['order.plan.rebalance.alert.available.unit.ctrl']+' '+availableUnit);
 					$(this).val(availableUnit.trim());
 					value = availableUnit;
 				}
 				var lastNav = $(this).closest('tbody').find('.product-last-nav').text();
 				lastNav = lastNav.replace(/,/gm,'');
 				var amount = Number(value*lastNav);
 				$(this).closest('tr').find('.product-redeem-amount').text(formatCurrency(amount));
 				//权重
 				reWeight();
 				caluRiskLevel();
 			});
 			$(document).find('.input-action-reduction').keypress(function(event) { 
 				var keyCode = event.which; 
 				if($('.product-table-input').val().length >= 10 && keyCode != 8){
 					return false;
 				}
 		        if (keyCode == 8 || keyCode == 46 || (keyCode >= 48 && keyCode <=57))  
 		            return true;  
 		        else  
 		            return false;  
 		    }).focus(function() {  
 		        this.style.imeMode='disabled';  
 		    }); 
 			$(document).find('.input-action-reduction').on('input',function(){
 				if(isNaN($(this).val())){
 					$(this).val('');
 				}
 			});
 		}
 		//计算
 		$('.product-table-input').on('blur',function(){
 			var availableUnit = $(this).closest('tbody').find('.product-available-unit').text();
 			if(!availableUnit){return;}
 			var value = $(this).val().trim();
 			if(Number(value) > Number(availableUnit)){
 				layer.msg(props['order.plan.rebalance.alert.available.unit.ctrl']+' '+availableUnit);
 				$(this).val(availableUnit.trim());
 				value = availableUnit;
 			}
 			var lastNav = $(this).closest('tbody').find('.product-last-nav').text();
 			lastNav = lastNav.replace(/,/gm,'');
 			var amount = parseFloat(value*lastNav).toFixed(2);
 			$(this).closest('tr').find('.product-redeem-amount').text(formatCurrency(amount));
 			//权重
 			reWeight();
 			caluRiskLevel();
 		});
 		//复原
 		$('.action-recovery').click(function(){
 			 actionRecovery(this);
 		});
 		function actionRecovery(selt){
 			$(selt).closest('tbody').removeClass('product-tbody-wrap');
 			$(selt).closest('tbody').children().eq(0).find('.product-actions').show();
 			$(selt).closest('tr').remove();
 			reWeight();
 			caluRiskLevel();
 		}
 		//新增
 		function addProduct(selt){
 			var html = '';
 			$.each(selt,function(i,n){
 				var fundName = n.fundName==null?'':n.fundName;
 				var riskLevel = n.fundInfo==null?'':n.fundInfo.riskLevel;
 				var fundType = n.fundType==null?'':n.fundType;
 				var fundCurrency = n.fundCurrency==null?'':n.fundCurrency;
 				var lastNav = n.lastNav==null?'':n.lastNav;
 				var toCurrencyName = n.toCurrencyName==null?'':n.toCurrencyName;
 				html +=
 					'<tbody class="product-tbody-wrap action-original">'
 			        +'<tr id="'+n.fundId+'">'
 					+'<td class="portfolio_tables_fnames">'
 			      	+'<a href="javascript:;">'
 			      	+ fundName
 			      	+'</a>'    	
 			    	+'<div class="portfolio_tables_nameWord">'       
 			        +'<p class="portfolio_tables_risk" style="width:30%"><span class="product-risk-level">'
 			        + riskLevel
 			        +'</span>'
 			        + props['order.plan.rebalance.table.risk']
 			        +'</p>'            
 			        +'<p class="portfolio_tables_risk" style="width:70%">'
 			        + fundType
 			        +'</p>'            
 			        +'</div>'        
 			        +'</td>'    
 			        +'<td class="portfolio-tdcenter">N/A</td>'    
 			        +'<td class="portfolio-tdcenter"><span class="product-last-nav conversion-flag">'
 			        + formatCurrency(lastNav)
 			        +'</span><span class="to-currency"> '
 			        + toCurrencyName
 			        +'</span></td>'     
 			        +'<td class="portfolio-tdcenter">N/A</td>'     
 			        +'<td class="portfolio-tdcenter">N/A</td>'    
 			        +'<td class="portfolio-tdcenter">'    
 			        +'<input class="product-table-input product-tbody-valid-add list-add-amount-flag" type="text" value="">'	
 					+'<p class="product-table-cur" style="margin-left:1px"><span class="to-currency">'
 			        + toCurrencyName
 			        +'</span></p>'	 	
 			        +'</td>'    
 			        +'<td class="portfolio-tdcenter portfolio-table-blue product-new-weight"></td>'    
 			        +'<td class="portfolio-tdcenter">'    
 			        +'<div class="product-actions">'         
 			        +'<img class="product-del-ico action-delete" src="'+base_root+'/res/images/client/product_del_ico.png">'        
 			    	+'</td>'    
 			    	+'</tr>'   
 			    	+'</tbody>'; 
 			});
 			$('.tbody-total').before(html);
 			//refashData();
 			//删除
 			$('.action-delete').click(function(){
 				$(this).closest('tbody').remove();
 				//权重
 				reWeight();
 				caluRiskLevel();
 			});
 			//计算
 			$('.product-table-input').on('blur',function(){
 				$(this).val(formatCurrency($(this).val().trim()));
 				//权重
 				reWeight();
 				caluRiskLevel();
 			});
 		}
 		//删除
 		$('.action-delete').click(function(){
 			actionDelete(this);
 		});
 		function actionDelete(selt){
 			if($(selt).closest('tbody').children().length == 2){
 				$(selt).closest('tbody').removeClass('product-tbody-wrap');
 				$(selt).closest('tbody').children().eq(0).find('.product-actions').show();
 				$(selt).closest('tbody').children().eq(0).find('.product-edit-ico').remove();
 			}
 			$(selt).closest('tbody').remove();
 			reWeight();
 			caluRiskLevel();
 		}
 		$('#btnReset').click(function(){
 			$('.action-delete').trigger('click');
 			$('.action-recovery').trigger('click');
 		});
 		
 		/*****************************************************************************************************************/
 		/**
 		 * quick add
 		 */
 	    $("#btnAddFund").on("click",quickAddFund);
 	    function quickAddFund(){
 	    	var cashAvailable = $('#txtCashAvailable').val();
 	    	cashAvailable = cashAvailable.replace(/,/g,'');
 	    	if(!cashAvailable || cashAvailable=='-' || Number(cashAvailable)==0){
 	    		layer.msg('Insufficient cash available to add product');
 	    		return;
 	    	}
 	    	$('#ids').val('');
 	    	var geoAllocation = $('#hidGeoAllocation').val();
 	    	var sector = $('#hidSector').val();
 	    	var url = '';
 			if(typeof(geoAllocation)!= 'undefined' && geoAllocation != ''){
 				url = base_root+"/front/fund/info/fundSelectorForAllocation.do?regions="+geoAllocation+"&sectors="+sector;
 			}else if(typeof(sector)!= 'undefined' && sector != ''){
 				url = base_root+"/front/fund/info/fundSelectorForAllocation.do?sectors="+sector;
 			}else{
 				url = base_root+"/front/fund/info/fundSelectorForAllocation.do";
 			}
 	    	openResWin(url,915,600,"fundSelector");
 	    }
 	    /**
 	     * 货币下拉鼠标事件
 	     */
 	    $('.proposal_xiala').on('mouseleave',function(){
 	    	$('#currency-choice').hide();
 	    });
 	    /**
 		  * 货币选择
 		  */
 	    $('#portfolio-currency').click(function(){
 	    	$('#currency-choice').toggle();
 	    	$('#currency-choice li').unbind('click');
 	    	$('#currency-choice li').click(function(){
 	    		$('#currency-choice').hide();
 	    		var fromCurrency = $('#portfolio-currency').attr('data-code');
 	    		var toCurrency = $(this).data('code');
 	    		conversion(fromCurrency,toCurrency);
 	    		$('.to-currency').text($(this).text());
 	    		$('#portfolio-currency').val($(this).text());
 	    		$('#portfolio-currency').attr('data-code',toCurrency);
 	    	});
 	    });
 	    /**
 		  * 获取基础货币
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
 		currencyType();
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
 				var weight = $(this).closest('tr').find('.product-new-weight').text();
 				weight = weight.replace('%','');
 				weight =  parseFloat(weight/100);
 				average = average + riskLevel*weight;
 			});
 			riskLevels.sort(function(a,b){
 				return b-a;
 			});
 			if(typeof(riskLevels[0]) != 'undefined'){
 				//最高风险
 				$('#risk-level-max').text(riskLevels[0]);
 			}
 			var fundCount = $('.product-risk-level').length;
 			if(isNaN(Number(average))){
 				$('#risk-level-average').text('N/A');
 			}else if(average!=0 && fundCount!=0){
 				//加权平均风险
 				$('#risk-level-average').text(Math.ceil(average));
 			}
 		};
 		setTimeout(function(){caluRiskLevel();},500);
 		/**
 		 * 初始权重赋值
 		 */
 		function initWeight(){
 			var totalAmount = 0;
 			$('.portfolio-table').find('.product-last-nav').each(function(i,n){
 				var lastNav = $(n).text();
 				lastNav = lastNav.replace(/,/gm,'');
 				var unit = $(n).closest('tr').find('.product-hold-unit').text();
 				totalAmount = totalAmount + parseFloat(lastNav).toFixed(2)*parseFloat(unit).toFixed(2);
 			});
 			$('.portfolio-table').find('.product-last-nav').each(function(i,n){
 				var lastNav = $(n).text();
 				lastNav = lastNav.replace(/,/gm,'');
 				var unit = $(n).closest('tr').find('.product-hold-unit').text();
 				var amount = parseFloat(lastNav).toFixed(2)*parseFloat(unit).toFixed(2);
 				var weight = parseFloat(amount/totalAmount*100).toFixed(2);
 				$(n).closest('tr').find('.product-old-weight').text(weight+'%');
 			});
 		}
 		$('.product-table-input').on('change',reWeight);
 		/**
 		 * 动态权重计算
 		 */
 		function reWeight(){
 			var totalAmount = 0;
 			$('.portfolio-table tr').each(function(i,n){
 				var lastNav = $(n).find('.product-last-nav').text();
 				var inputVal = '';
 				if(lastNav != ''){
 					lastNav = lastNav.replace(/,/gm,'');
 					var unit = $(n).find('.product-hold-unit').text();
 					unit = Number(unit);
 					totalAmount = totalAmount + parseFloat(lastNav).toFixed(2)*unit;
 					//新增
 					inputVal = $(n).closest('tr').find('.product-table-input').val();
 					if(typeof(inputVal)!='undefined'){
 						totalAmount = totalAmount + Number(inputVal.trim().replace(/,/gm,''));
 					}
 				}else{
 					inputVal = $(n).find('.product-table-input').val();
 					if(typeof(inputVal) != 'undefined'){
 						inputVal = parseFloat(inputVal.trim().replace(/,/gm,'')).toFixed(2);
 						var amount = $(n).find('.product-redeem-amount').text();
 						if(amount != ''){
 						//减
 							lastNav = $(n).closest('tbody').children().eq(0).find('.product-last-nav').text();
 							lastNav = parseFloat(lastNav.replace(/,/gm,'')).toFixed(2);
 							totalAmount = totalAmount - (lastNav*inputVal);
 						}else{
 						//增
 							totalAmount = Number(totalAmount)+Number(inputVal);
 						}
 					}
 				}
 			});
 			//重新分配
 			$('.portfolio-table').find('.product-last-nav').each(function(i,n){
 				var trs = $(n).closest('tbody').children();
 				var actionAmount = 0;
 				if(trs.length > 1){
 					$.each(trs,function(j,m){
 						var lastNav = $(m).find('.product-last-nav').text();
 						if(lastNav != ''){
 							//转换
 							var trId = $(n).closest('tr').attr('id');
 							var currId = $(m).attr('id');
 							var tempUnit = $(n).closest('tr').find('.product-unit-redeem').text();//过滤原tr
 							var redeemUnit = $(m).find('.product-unit-redeem').text();
 							if(tempUnit != '' && redeemUnit != '' && currId == trId){
 								var conversionLastNav = trs.eq(0).find('.product-last-nav').text();
 								conversionLastNav = conversionLastNav.replace(/,/gm,'');
 								actionAmount = Number(conversionLastNav)*Number(redeemUnit);
 							}else if(tempUnit == '' && redeemUnit != ''){
 								var conversionLastNav = trs.eq(0).find('.product-last-nav').text();
 								conversionLastNav = conversionLastNav.replace(/,/gm,'');
 								actionAmount = actionAmount - Number(conversionLastNav)*Number(redeemUnit);
 							}
 						}else{
 							var inputVal = $(m).find('.product-table-input').val().trim();
 							if(typeof(inputVal) != 'undefined'){
 								inputVal = parseFloat(inputVal.replace(/,/gm,'')).toFixed(2);
 								var amount = $(m).find('.product-redeem-amount').text();
 								if(amount != ''){
 								//减
 									lastNav = $(m).closest('tbody').children().eq(0).find('.product-last-nav').text();
 									lastNav = parseFloat(lastNav.replace(/,/gm,'')).toFixed(2);
 									actionAmount = actionAmount - lastNav*inputVal;
 								}else{
 								//增
 									actionAmount = parseFloat(actionAmount+inputVal).toFixed(2);
 								}
 							}
 						}
 					});
 				}else{
 					//新增
 					inputVal = $(n).closest('tr').find('.product-table-input').val();
 					if(typeof(inputVal)!='undefined'){
 						actionAmount = Number(inputVal.trim().replace(/,/gm,''));
 					}
 				}
 				var lastNav = $(n).text();
 				lastNav = lastNav.replace(/,/gm,'');
 				var unit = $(n).closest('tr').find('.product-hold-unit').text();
 				if(unit == ''){
 					unit = 0;
 				}
 				unit = Number(unit);
 				var amount = 0;
 				if(typeof(actionAmount)!='undefined'){
 					amount = parseFloat(lastNav).toFixed(2)*unit+Number(actionAmount);
 				}
 				var weight = parseFloat(amount/totalAmount*100).toFixed(2);
 				var inputVal_ = $(n).closest('tr').find('.product-table-input').val();
 				var unitRedeem = $(n).closest('tr').find('.product-unit-redeem').text();
 				var holdAmount = $(n).closest('tr').find('.product-hold-amount').text();
 				if(weight == 0 && (inputVal_||unitRedeem||holdAmount)){
 					if(typeof inputVal_ != 'undefined'){
 						inputVal_ = inputVal_.replace(/,/g,'');
 					}
 					if(typeof holdAmount != 'undefined'){
 						holdAmount = holdAmount.replace(/,/g,'');
 					}
 				}
 				if(weight < 0){
 					weight = 0;
 				}
 				if(weight > 100){
 					weight = 100;
 				}
 				$(n).closest('tr').find('.product-new-weight').text(weight+'%');
 				//total weight
 				var totalWeight = 0;
 				$('.portfolio-table').find('.product-new-weight').each(function(i,n){
 					var weight = $(n).text();
 					weight = weight.replace('%','');
 					totalWeight = Number(totalWeight)+Number(weight);
 					if(Number(totalWeight) > 100){totalWeight = 100;}
 					if(Number(totalWeight) == 99 && i == $('.portfolio-table').find('.product-new-weight').length-1){
 						var weight = $('.product-tbody-wrap').eq(0).find('.product-new-weight').eq(0).text();
 						if(weight){
 							weight = weight.replace('%','');
 							weight = Number(weight) + 1;
 							$('.product-tbody-wrap').eq(0).find('.product-new-weight').text(weight+'%');
 						}
 						totalWeight=100;
 					}
 					/*if(Number(totalWeight) == 100){
 						allocationMap();
 						aggregate();
 					}*/
 					$('.product-total-weight').text(parseFloat(totalWeight).toFixed(2)+'%');
 				});
 			});
 		}
 		/**
 		 * 跳转定时
 		 */
 		function jumpTimer(url){
 			var second = $('#jump-second').text();
 			if(Number(second)>0){
 				setTimeout(function(){
 					$('#jump-second').text(Number(second)-1);
 					jumpTimer(url);
 				},1000);
 			}else{
 				window.location.href = base_root + url;
 			}
 		}
 		/**
 		 * valid jump
 		 */
	(function(){
		var hrefUrl = window.location.href;
		if(hrefUrl.indexOf('rebalanceMixer') > -1){
			var id = getUrlParam('id');
			if(!id){
				$('.rebalance-jump').show();
				var jumpUrl = '/front/tradeRecord/orderPlanList.do';
				jumpTimer(jumpUrl);
			}
			var url = base_root + '/front/tradeRebalance/valiHoldProduct.do?d=' + new Date().getTime();
			$.ajax({
				type:'post',
				url:url,
				data:{
					id:id
				},
				success:function(re){
					if(re.flag){
						var ifHold = re.ifHold; // 0:空仓，1：有持仓
						var ifPlan = re.ifPlan; // 0:所有交易订单已提交，1：尚有未提交订单
						if(ifHold == '0'){
						// 无持仓,跳转重新购买页面
							$('#jump-content').text(prop['order.plan.alert.hold.no.product']);
							$('.rebalance-jump').show();
							$('.rebalance-jump-wrap').css('top',$(document).height()*0.1+$(window).scrollTop());
							$(window).on('scroll',loadScroll);
							var jumpUrl = '/front/tradeRebalance/rebalance.do?d=' + new Date().getTime() + '&id=' + id;
							$('#rebalance-jump-url').attr('href',base_root + jumpUrl);
							//jumpTimer(jumpUrl);
						}else if(ifPlan == '1'){
						// 当前持仓有未提交的交易计划
							var planId = re.planId;
							var status = re.status;
							var ifFirst = re.ifFirst; // 0:投资过，1：未投资过。（区分草稿状态的订单来源）
							var jumpUrl = '';
							$('#jump-content').text(prop['order.plan.alert.plan.no.complete']);
							$('.rebalance-jump').show();
							$('.rebalance-jump-wrap').css('top',$(document).height()*0.1+$(window).scrollTop());
							$(window).on('scroll',loadScroll);
							if(status == '1' || status == '2' || status == '3'){ // 1：审批中，2：审批不通过，3：审批通过
								jumpUrl = '/front/tradeMain/previewOrderPlan.do?d=' + new Date().getTime() + '&id=' + planId;
								$('#rebalance-jump-url').attr('href',base_root + jumpUrl);
							}else{
								jumpUrl = '/front/tradeRebalance/rebalance.do?d=' + new Date().getTime() + '&id='+id;
								$('#rebalance-jump-url').attr('href',base_root + jumpUrl);
							}
							//jumpTimer(jumpUrl);
						}else{
						// 进入rebalance页面 
							window.location.href = base_root + '/front/tradeRebalance/rebalance.do?id='+id;
						}
					}else{
						$('.rebalance-jump').show();
						$('.rebalance-jump-wrap').css('top',$(document).height()*0.1+$(window).scrollTop());
						$(window).on('scroll',loadScroll);
						var second = $('#jump-second').text();
						var jumpUrl = '/front/tradeRecord/orderPlanList.do';
						$('#rebalance-jump-url').attr('href',base_root + jumpUrl);
						//jumpTimer(jumpUrl);
					}
				}
			});
		}
	})();
	/**
     * div
     */
    function loadScroll(){
    	$('.rebalance-jump-wrap').css('top',$(document).height()*0.1+$(window).scrollTop());
	}
	/**
	 * 金额输入控制
	 */
	function addAmountValid(){
		var txtCashAvailable = $('#txtCashAvailable').val();
		txtCashAvailable = Number(txtCashAvailable.replace(/,/g,''));
		var totalAmount = 0.00;
		$('.list-add-amount-flag').each(function(){
			var amount = $(this).val();
			amount = amount.replace(/,/g,'');
			totalAmount = totalAmount + Number(amount);
		});
		if(totalAmount>txtCashAvailable){
			layer.msg(prop['order.plan.rebalance.alert.product.totalInvAmount.limit']);
			return false;
		}else{
			return true;
		}
	}
	$('#txtUnitRedeem').on('blur',function(){
		var availableValue = $.trim($('#available-value').text());
		if($(this).val() && Number($(this).val()) > Number(availableValue)){
			$(this).val(availableValue);
		}
	});   
	/**
	 * cancel
	 */
	$('#btnCancel').click(function(){
		if(window.history.length > 1){  
	        window.history.go( -1 );  
	    }else{  
	        window.location.href = base_root + '/front/tradeRecord/orderPlanList.do';  
	    } 
	});
	
	$(document).find('.product-table-input').keypress(function(event) { 
		var keyCode = event.which; 
		if($('.product-table-input').val().length >= 10 && keyCode != 8){
			return false;
		}
        if (keyCode == 8 || keyCode == 46 || (keyCode >= 48 && keyCode <=57))  {
        	reWeight();
        	return true;  
        }
        else return false;  
    }).focus(function() {  
        this.style.imeMode='disabled';  
    }); 
	$(document).find('.product-table-input').on('input',function(){
		if(isNaN($(this).val())){
			$(this).val('');
		}
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
			//var planId = getUrlParam('planId');
			var planId = planId;
			//修改定投状态
		    $.post(base_root + '/front/tradeStep/updateAipState.do?d='+new Date().getTime(),{planId:planId,state:state});
		}
	});
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
    	var url = base_root+'/front/tradeStep/saveAip.do?r=' + new Date().getTime();
    	var data = {
    			holdId:getUrlParam('id'),
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
    				if('1' == result.refresh){
    					var url = window.location.href;
    					if(getUrlParam('currencyCode') == null){
    						var currencyCode = $('#portfolio-currency').attr('data-code');
    						url = urlUpdateParams(url, 'currencyCode', currencyCode);
    					}
    					window.location.href = urlUpdateParams(url, 'planId', result.planId);
    				}
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
	Array.prototype.inFundUnique = function(){
		this.sort(); //先排序
		var res = [this[0]];
		for(var i = 1; i < this.length; i++){
		  if(this[i].inFundId !== res[res.length - 1].inFundId){
			  res.push(this[i]);
		  }
		}
		return res;
	};
	/*****************************************************************************************************************/
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
	 * 生成转换分组编号
	 */
	function getGroupNo(){
		var date = new Date().toISOString();
		var dateStr = (date.split('.'))[0];
		dateStr = dateStr.replace('T','').replace(/-/g,'').replace(/:/g,'').substring(2,dateStr.length-1)+Math.floor(Math.random() * 10)+Math.floor(Math.random() * 10)+Math.floor(Math.random() * 10);
		return dateStr;
	}
	/**
	 * 数组去重
	 */
	Array.prototype.unique = function(){
		this.sort(); //先排序
		var res = [this[0]];
		for(var i = 1; i < this.length; i++){
		  if(this[i] !== res[res.length - 1]){
			  res.push(this[i]);
		  }
		}
		return res;
	};
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
	 Array.prototype.max = function(){ 
	   return Math.max.apply({},this) 
     } 
     Array.prototype.min = function(){ 
	   return Math.min.apply({},this) 
     }
	/*****************************************************************************************************************/
});