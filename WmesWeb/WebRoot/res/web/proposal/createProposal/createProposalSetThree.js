define(function(require) {
	var $ = require('jquery');
		    require("echarts");
		    require('slider');
			require('layui');
			require('jqueryRange');
			require('laydate');
    var angular = require('angular');
	$(".step-portrait-choose li").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show").find(".step-portrait-name").html($(this).html());
	});
	$(".step-portrait-name, .stepOne-constrains-xiala").on("click",function(){
		$(this).parents(".step-portrait-name-wrap").toggleClass("step-portrait-name-show");
	});
	$(".builder-tab li").on("click",function(){
		$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
		$(".builder-main-contents > div").hide().eq($(this).index()).show();
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
    /**********************************ECharts Start**************************************/
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
    function getPieOption(legendData, seriesData, color){
    	var option ={  
			tooltip : {
				trigger: 'item',
				formatter: "{b}"
			},
        	legend: {
				orient: 'vertical',
				x: '30%',
				data: legendData,
				formatter: '{name}'
			},
			color:color,
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
		var url = base_root+'/front/tradeChart/getFundsReturnData.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				fundIds:fundIds,
				weights:weights,
				period:period
			},
			success:function(result){
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
				tooltip:{position : function(p) {
            		return [p[0] - 50, p[1] + 10];
        		}},
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
				if(separateness.series.length > 0){
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
		$("#allocation-chart-propose").height('300px');
		$("#allocation-chart-change").width($(".builder-tab").width());
		$("#allocation-chart-change").height('300px');
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
    			color:['#f15c80','#8085e9','#f7a35c','#90ed7d','#434348','#7cb5ec'],
    			seriesData:[]
    	};
    	var allocationMapTwo = {
    			legendData:[],
    			color:['#90ed7d','#7cb5ec','#434348','#f15c80','#f7a35c','#8085e9'],
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
					var option = getPieOption(allocationMap.legendData, allocationMap.seriesData, allocationMap.color);
					myChartOne.setOption(option);
					//基金类别
					if(sectorTypes != null){
						for(var key in sectorTypes){
							if(Number(sectorTypes[key]) <= 0){
								continue;
							}
							var formatterName = key +'('+parseFloat(Number(sectorTypes[key])).toFixed(2)+'%)';
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
					var option = getPieOption(allocationMapTwo.legendData, allocationMapTwo.seriesData, allocationMapTwo.color);
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
    // 折线图切换
    $("#builder-chart-aggregate").on("click",function(){
    	$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
    	aggregate();
    });
    $("#builder-chart-separateness").on("click",function(){
    	$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
    	separateness();
    });
    // 时间段选择 事件
    $('.builder-chart-date li').click(function(){
    	$(this).addClass("now").siblings().removeClass();
    	eChartsData.period = $(this).data('month');
    	if('builder-chart-separateness' == $('.chart-tab.tab-active').attr('id')){
    		separateness();
    	}else if('builder-chart-aggregate' == $('.chart-tab.tab-active').attr('id')){
    		aggregate();
    	}
    });
	//update chart 点击事件
	$('#btnUpdateChart').click(function(){
		refashData();
	});
	//重新获取数据 刷新视图
	function refashData(){
		allocationMap();
		aggregate();
		$("#builder-chart-aggregate").siblings().removeClass("tab-active").end().addClass("tab-active");
	}
	refashData();
	function getFundIds(){		
		var fundIds = '';
		$('.portfolio-table-data').each(function(){
			var fundId = $(this).attr('id');
			fundIds += fundId + ',';
		});
		if('' != fundIds){
			fundIds = fundIds.substring(0, fundIds.length-1);
		}
		if(typeof fundIds == 'undefined' || fundIds == '{{items.fundId}}' )return '';
		return fundIds;
	}
	function getWeights(){		
		var weights = '';
		$('.portfolio-table-data').each(function(){
			var weight = $(this).find('.strategy_chart_tinput').val();
			weights += weight + ',';
		});
		if('' != weights){
			weights = weights.substring(0, weights.length-1);
		}
		if(typeof weights == 'undefined' || weights == '{{items.productWeight}}')return '';
		return weights;
	}
	//获取fund比例集合
    function getFundWeights(){
    	var funds = [];
    	$(document).find('.portfolio-table-data').each(function(){
    		var fund = {};
    		var fundId = $(this).attr('id');
    		if(fundId == '{{items.fundId}}')return;
    		var weight = $(this).find('tr .strategy_chart_tinput').val();
    		fund['fundId'] = fundId;
    		fund['weight'] = parseFloat(weight).toFixed(1);
    		funds.push(fund);
    	});
    	return funds;
    }
    /**********************************ECharts End**************************************/
	/**
	 * 风险计算
	 */
	function caluRiskLevel(){
		var riskLevels = [];
		var average = 0;
		$('.portfolio-table-data').each(function(i,n){
			var tr_ = $(this).find('tbody tr:eq(0)');
			var riskLevel = tr_.find('.td-riskLevel').text();
			if(typeof(riskLevel) != undefined){
				riskLevel = parseInt(riskLevel);
				riskLevels[i] = riskLevel;
				var weight = tr_.find('.strategy_chart_tinput').val();
				if(typeof(weight)!='undefined' && weight.indexOf('%') > -1){
					weight = (weight.split('%'))[0];
				}
				weight =  parseFloat(weight/100);
				average = average + riskLevel*weight;
			}
		});
		riskLevels.sort(function(a,b){
			return b-a;
		});
		if(riskLevels.length > 0 && typeof(riskLevels[0]) != 'undefined' && riskLevels[0] != 'NaN'){
			$('#risk-level-max').text(riskLevels[0]);
		}else{
			$('#risk-level-max').text('N/A');
		}
		var fundCount = $('.portfolio-table-data tbody tr').length;
		if(typeof(average) != 'undefiend' && average!=0 && fundCount!=0){
			$('#risk-level-average').text(Math.floor(average));
		}else{
			$('#risk-level-average').text('N/A');
		}
	};
    /**
     * quick add
     */
    $(".btnAddFund").on("click",quickAddFund);
    function quickAddFund(){
    	$('#ids').val('');
    	var geoAllocation = $('#hidGeoAllocation').val();
    	var sector = $('#hidSector').val();
    	var types = $('#hidFundType').val();
    	var url = '';
    	if(typeof(types)== 'undefined'){
    		types = '';
    	}
    	if(typeof(geoAllocation)!= 'undefined' && geoAllocation != ''){
			url = base_root+"/front/fund/info/fundSelectorForAllocation.do?types="+types+"&regions="+geoAllocation+"&sectors="+sector;
		}else if(typeof(sector)!= 'undefined' && sector != ''){
			url = base_root+"/front/fund/info/fundSelectorForAllocation.do?types="+types+"&sectors="+sector;
		}else{
			url = base_root+"/front/fund/info/fundSelectorForAllocation.do?types="+types;
		}
    	openResWin(url,935,650,"fundSelector");
    }
	/**
	 * angular start
	 */
	var mybase = angular.module('proposalTable', ['truncate']);
	mybase.controller('proposalTableCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.tableActive = [];
		/**
		 * table add product
		 */
		$scope.popupWinReturn = function(){
			var strategyId = $('#hidStrategyId').val(),
				fundIds = $('#ids').val(),
				toCurrencyCode = $('#portfolio-currency').attr('data-code'),
				proposalId = $('#hidCrmProposalId').val(),
				url = base_root+'/front/portfolio/arena/getFunds.do?dateStr=' + new Date().getTime();
			var existFund = [];
			$('.portfolio-table-data').each(function(){
				var fund = $(this).attr('id');
				if(typeof(fund)!='undefined'&&fund!=null && fund!=''){
					existFund.push(fund);
				}
				if($(this).find('tr:gt(0)').length>0){
					$.each($(this).find('tr:gt(0)'),function(){
						existFund.push($(this).data('fund'));
					});
				}
			});
			if(typeof(fundIds) != 'undefined' && fundIds.indexOf(',')>-1){
				var fund = fundIds.split(',');
				fundIds = '';
				$.each(fund,function(i,n){
					if($.inArray(n, existFund)!=-1){
						if($('#'+n).length > 0 && $('#'+n).is('table')){
							var fundName = $('#'+n).find('tr:eq(0)').find('.portfolio_table_names').text();
							layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added'],{icon:3});
						}else if($('tr[data-fund="'+n+'"]').length > 0){
							var fundName = $('tr[data-fund="'+n+'"]').find('.portfolio_table_names').text();
							layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added'],{icon:3});
						}
					}else{
						fundIds += n+',';
					}
				});
				if(fundIds!=''){fundIds = fundIds.substring(0, fundIds.length-1);}
			}else{
				if($.inArray(fundIds, existFund)!=-1){
					if($('#'+fundIds).length > 0 && $('#'+fundIds).is('table')){
						var fundName = $('#'+fundIds).find('tr:eq(0)').find('.portfolio_table_names').text();
						layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added'],{icon:3});
					}else if($('tr[data-fund="'+fundIds+'"]').length > 0){
						var fundName = $('tr[data-fund="'+fundIds+'"]').find('.portfolio_table_names').text();
						layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added'],{icon:3});
					}
					return;
				}
			}
			var fundMaps = [];
			var tableFundIds = '';
			$scope.tableActive = [];
			$('.portfolio-table-data').each(function(){
				var fund = $(this).attr('id');
				if(typeof(fund) != 'undefiend' && fund != ''){
					tableFundIds += fund + ',';
				}
				if($(this).hasClass('portfolio-table-active')){
					$scope.tableActive.push(fund);
				}
			});
			if(tableFundIds != ''){
				tableFundIds = tableFundIds.substring(0,tableFundIds.length-1); 
			}
			var selectFund = ''; 
			if(typeof($scope.selectFund) == 'undefined' || $scope.selectFund == ''){
				if(tableFundIds != ''){
					tableFundIds += ',' + fundIds ;
				}else{
					tableFundIds = fundIds;
				}
			}else{
				selectFund = $scope.selectFund;
				if(fundIds.indexOf(',') > -1){
					var temp = fundIds.split(',');
					fundIds = '';
					$.each(temp,function(i,n){
						if(selectFund != n){
							fundIds += n + ',';
						}
					});
					fundIds = fundIds.substring(0,fundIds.length-1);
				}else{
					if(selectFund == fundIds){
						if($('#'+fundIds).length > 0 && $('#'+fundIds).is('table')){
							var fundName = $('#'+fundIds).find('tr:eq(0)').find('.portfolio_table_names').text();
							layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added'],{icon:3});
						}else if($('tr[data-fund="'+fundIds+'"]').length > 0){
							var fundName = $('tr[data-fund="'+fundIds+'"]').find('.portfolio_table_names').text();
							layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added'],{icon:3});
						}
						return;
					}
				}
			}
			tableFundIds = tableFundIds.split(',');
			tableFundIds = tableFundIds.unique();
			$.each(tableFundIds,function(i,n){
				var fundMap = {};
				fundMap['fund'] = n;
				var fundTable = $('#'+n);
				var weight = fundTable.find('.strategy_chart_tinput').val();
				if(typeof(weight) != 'undefined' && weight != ''){
					fundMap['weight'] = parseFloat(Number(weight)/100).toFixed(2);
				}
				if(fundTable.is('table')){
					var spareFund = [];
					if(n == selectFund){
						if(fundIds.indexOf(',')>-1){
							$.each(fundIds.split(','),function(x,y){
								spareFund.push(y);
							});
						}else{
							spareFund.push(fundIds);
						}
						$scope.selectFund = '';
					}
					fundTable.find('tbody tr:gt(0)').each(function(j,m){
						if(typeof($(this).data('fund'))!='undefined'){
							spareFund.push($(this).data('fund'));
						}
					});
					fundMap['spareFunds'] = spareFund;
				}
				fundMaps.push(fundMap);
			});
			$http({
              url: url,
              method: 'POST',
              headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
              params : {
            	  fundIds:encodeURI(JSON.stringify(fundMaps)),
  				  toCurrency:toCurrencyCode,
  				  proposalId:proposalId,
  				  strategyId:strategyId
              }
         	 }).success(function(result){
         		if(result.flag){
         			$scope.fundInfoList = result.fundInfoList;
         			if($scope.fundInfoList == null){
         				$('.portfolio-table-inf').hide();
						$('.no-funds-yet').show();
         			}else{
         				$('.portfolio-table-inf').show();
						$('.no-funds-yet').hide();
	     				setTimeout(function(){
	     					if($scope.tableActive.length > 0){
	         					$.each($scope.tableActive,function(i,n){
	             					$('#'+n).addClass('portfolio-table-active');
	             				});
	     					}
	     					rangesInit();
	     					refashData();
	     					tableTotalWeight();
	     				},100);
	     			}
         		}else{
         			$('.portfolio-table-inf').hide();
					$('.no-funds-yet').show();
         		}
         	 });
		};
		/**
		 * 備選基金添加
		 */
		$(document).on("click",".portfolio_top_add",function(){
			$scope.selectFund = $(this).closest('table').attr('id');
			quickAddFund();
		});
		/**
		 * delete
		 */
		$(document).on("click",".portfolio-del",function(){
			var self = $(this),
			_wrap = self.parents(".portfolio-table"),
			_length = self.parents(".portfolio-table").find("tr").length,
			_index = self.parents("tr").index();
			var _num = self.parents("table").find(".funds-single-slider").val().toString().split(',')[0],
					_tnum = self.parents(".ranges-init").attr("data-temp");
					_tnum = Number(_tnum) + Number(_num);
			if( _length  == 1){
				//如果该基金没有候选基金
				layer.confirm(prop['create.proposal.step.three.alert.deleteFund'], {
					  title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
				}, function(index){
					  self.parents(".ranges-init").attr("data-temp",_tnum);
					  _wrap.remove();
					  tableTotalAmount();
					  tableTotalWeight();
					  if($('.portfolio-table-data').length == 0){
							$('.portfolio-table-inf').hide();
							$('.no-funds-yet').show();
					  }
					  refashData();
					  layer.close(index);
					  tableTotalAmount();
					  tableTotalWeight();
					  if($('.portfolio-table-data').length == 0){
						$('.portfolio-table-inf').hide();
						$('.no-funds-yet').show();
				      }
					  caluRiskLevel();
				});
			}else{
				if( _index == 0){
					layer.confirm(prop['create.proposal.step.three.alert.deleteFundAndCandidate'], {
						  title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
					}, function(index){
					  self.parents(".ranges-init").attr("data-temp",_tnum);
					  _wrap.remove();
					  refashData();
					  layer.close(index);
					  tableTotalAmount();
					  tableTotalWeight();
					  if($('.portfolio-table-data').length == 0){
						$('.portfolio-table-inf').hide();
						$('.no-funds-yet').show();
				      }
					  caluRiskLevel();
					});
				}else if(_index != 0 && _length == 2){
					var self = this;
					layer.confirm(prop['create.proposal.step.three.alert.deleteCandidate'], {
						  title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
					}, function(index){
						  $(self).closest('table').find('.display_ico').hide();
						  $(self).parents("tr").remove();
						  refashData();
						  layer.close(index);
						  tableTotalAmount();
						  tableTotalWeight();
						  if($('.portfolio-table-data').length == 0){
							$('.portfolio-table-inf').hide();
							$('.no-funds-yet').show();
					      }
						  caluRiskLevel();
					});
				}else{
					var self = this;
					layer.confirm(prop['create.proposal.step.three.alert.deleteCandidate'], {
						  title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
					}, function(index){
						  $(self).parents("tr").remove();
						  refashData();
						  layer.close(index);
						  tableTotalAmount();
						  tableTotalWeight();
						  if($('.portfolio-table-data').length == 0){
							$('.portfolio-table-inf').hide();
							$('.no-funds-yet').show();
					      }
						  caluRiskLevel();
					});
				}
			}
			$('#ids').val('');
	 	});
		/**
		 * 初始化數據
		 */
		function getProductdetails(){
			var currencyCode = $('#hidDefCurrency').val();
			var url = base_root + '/front/crm/proposal/getPortfolioProductDetails.do?d=' + new Date().getTime();
			$.ajax({
				type:'post',
				url:url,
				data:{id:getUrlParam('id')},
				success:function(re){
					if(re.flag){
						$http({
				              url: base_root+'/front/portfolio/arena/getFunds.do?dateStr=' + new Date().getTime(),
				              method: 'POST',
				              headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
				              params : {
				            	  fundIds:encodeURI(JSON.stringify(re.fundIds)),
				            	  toCurrency:currencyCode,
				  				  proposalId:getUrlParam('id')
				              }
			         	 }).success(function(result){
			         		if(result.flag){
			         			$scope.fundInfoList = result.fundInfoList;
			         			if($scope.fundInfoList == null){
			         				$('.portfolio-table-inf').hide();
									$('.no-funds-yet').show();
			         			}else{
			         				$('.portfolio-table-inf').show();
									$('.no-funds-yet').hide();
				     				setTimeout(function(){
				     					if($scope.tableActive.length > 0){
				         					$.each($scope.tableActive,function(i,n){
				             					$('#'+n).addClass('portfolio-table-active');
				             				});
				     					}
				     					rangesInit();
				     					refashData();
				     					tableTotalWeight();
				     				},100);
				     			}
			         		}else{
			         			$('.portfolio-table-inf').hide();
								$('.no-funds-yet').show();
			         		}
			         	 });
					}
				}
			});
		};
		getProductdetails();
		$('#btnReset').click(function(){
			getProductdetails();
	 	});
	}]);
	/**
     * Cancel
     */
    $('#btnCancel').click(function(){
    	//$('.portfolio-table tbody tr:gt(0)').remove();
    });
    /**
     * next step4
     */
    $('#btnNextFour').click(function(){
    	var crmProposalId = $('#hidCrmProposalId').val();
    	var fundCount = $('.portfolio-table-data').length;
    	if(fundCount > 0 && typeof(fundCount) != 'undefined'){
    		var flag = false;
    		$('.portfolio-table-data').each(function(){
    			if($(this).find('tr').length <= 1){
    				flag = true;
    				return false;
    			}
    		});
    		if(flag){
    			layer.msg(props['create.proposal.step.three.alert.optional.product.list.empty'],{icon:3});
    			return;
    		}
    		var totalWeight = $('#tableTotalWeight').text();
    		if(totalWeight){
    			totalWeight = totalWeight.replace('%','').trim();
    			if(Number(totalWeight) != 100){
    				layer.msg(props['create.proposal.step.three.alert.total.weight.ctrl'],{icon:3});
        			return;
    			}
    		}else{
    			layer.msg(props['create.proposal.step.three.alert.total.weight.ctrl'],{icon:3});
    			return;
    		}
    		saveImage(true);
    	}else{
    		layer.msg(props['create.proposal.step.three.alert.product.list.empty'],{icon:3});
    	}
    });
    /**
     * next step2
     */
	$('#btnNextTwo').click(function(){
		var name = $('#invName').text();
		var id = $('#hidCrmProposalId').val();
		var url = base_root+'/front/crm/proposal/createProposalSetTwo.do?name='+name;
		if(id != '' && typeof(id) != 'undefiend'){
			url += '&id='+id;
		}
		if(getUrlParam('edit') == '1'){
			url = urlUpdateParams(url, 'edit', '1');
		}
		window.location.href = url;
	});
	/**
	 * save
	 */
	$('#btnSave').click(function(){
		if($('.portfolio-table-data').length>0){
			saveImage(false);
		}else{
			layer.msg(props['create.proposal.step.three.alert.product.list.empty'],{icon:3});
		}
	});
	/**
	 * save image
	 */
	function saveImage(jump){
		$('.load-gif').css('height',$(document).height()*1.2);
		$('.load-gif').show();
		var crmProposalId = $('#hidCrmProposalId').val();
		var aggregateChart = echarts.getInstanceByDom(document.getElementById('builder-chart-one')),
			allocationChart = echarts.getInstanceByDom(document.getElementById('allocation-chart-propose')),
			allocationChartTwo = echarts.getInstanceByDom(document.getElementById('allocation-chart-change'));
		var aggregatePng = '';
		var separatenesPng = '';
		var allocationPng = '';
		var allocationTwoPng = '';
		if(typeof allocationChart != 'undefined' && allocationChart._chartsViews.length > 0){
			allocationPng = allocationChart.getDataURL({
			    type: 'png',
			    pixelRatio: 2,
			    backgroundColor: '',
			    excludeComponents: ['toolbox']
			});
			allocationPng = (allocationPng.split(','))[1];
		}
		if(typeof allocationChartTwo != 'undefined' && allocationChartTwo._chartsViews.length > 0){
			allocationTwoPng = allocationChartTwo.getDataURL({
			    type: 'png',
			    pixelRatio: 2,
			    backgroundColor: '',
			    excludeComponents: ['toolbox']
			});
			allocationTwoPng = (allocationTwoPng.split(','))[1];
		}
		if($('#builder-chart-aggregate').hasClass('tab-active')){
			if(typeof aggregateChart != 'undefined' && aggregateChart._chartsViews.length > 0){
				aggregatePng = aggregateChart.getDataURL({
				    type: 'png',
				    pixelRatio: 2,
				    backgroundColor: '',
				    excludeComponents: ['dataZoom']
				});
				aggregatePng = (aggregatePng.split(','))[1];
			}
			separateness();
			setTimeout(function(){
				aggregateChart = echarts.getInstanceByDom(document.getElementById('builder-chart-one'));
				if(typeof aggregateChart != 'undefined' && aggregateChart._chartsViews.length > 0){
					separatenesPng = aggregateChart.getDataURL({
					    type: 'png',
					    pixelRatio: 2,
					    backgroundColor: '',
					    excludeComponents: ['dataZoom']
					});
					separatenesPng = (separatenesPng.split(','))[1];
				}
				aggregate();
				saveEChartsImage(crmProposalId,allocationPng,allocationTwoPng,separatenesPng,aggregatePng,jump)
			},2500);
		}else if($('#builder-chart-separateness').hasClass('tab-active')){
			if(typeof aggregateChart != 'undefined' && aggregateChart._chartsViews.length > 0){
				separatenesPng = aggregateChart.getDataURL({
				    type: 'png',
				    pixelRatio: 2,
				    backgroundColor: '',
				    excludeComponents: ['dataZoom']
				});
				separatenesPng = (separatenesPng.split(','))[1];
			}
			aggregate();
			setTimeout(function(){
			aggregateChart = echarts.getInstanceByDom(document.getElementById('builder-chart-one'));
			if(typeof aggregateChart != 'undefined' && aggregateChart._chartsViews.length > 0){
				aggregatePng = aggregateChart.getDataURL({
				    type: 'png',
				    pixelRatio: 2,
				    backgroundColor: '',
				    excludeComponents: ['dataZoom']
				});
				aggregatePng = (aggregatePng.split(','))[1];
			}
			separateness();
			saveEChartsImage(crmProposalId,allocationPng,allocationTwoPng,separatenesPng,aggregatePng,jump)
			},2500);
		}
	}
	function saveEChartsImage(crmProposalId,allocationPng,allocationTwoPng,separatenesPng,aggregatePng,jump){
		var url = base_root+'/front/crm/proposal/saveEChartsImage.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:
			{
				proposalId:crmProposalId,
				allocationPng:allocationPng,
				allocationTwoPng:allocationTwoPng,
				separatenesPng:separatenesPng,
				aggregatePng:aggregatePng
			},
			success:function(data){
				saveProposal(jump);
			},
			error:function(){
				layer.msg(globalProp['global.failed.save'],{icon:1});
			}
		});
	}
	/**
	 * save proposal
	 */
	function saveProposal(jump){
		var products = [];
		$('.products-list-wrap').find('.strategy_chart_tinput').each(function(i,n){
    		var product = {};
    		var fundWeight = $(this).val();
    		product['fundId'] = $(this).closest('table').attr('id');
    		product['fundWeight'] = fundWeight;
    		var detail = [];
    		var details = $(this).closest('table').find('tbody tr:gt(0)');
    		if(details.length > 0){
	    		$.each(details,function(j,m){
	    			detail.push($(m).data('fund'));
	    		});
    		}
    		product['detail'] = detail;
    		products.push(product);
    	});
		//proposal info
		var proposalName = $('#txtProposalName').val();
		var crmProposalId = $('#hidCrmProposalId').val();
		var riskLevel = $('#risk-level-average').text();
		if(riskLevel){
			riskLevel = Number(riskLevel.trim());
			if(isNaN(riskLevel)){
				riskLevel = '';
			}
		}
		var curStep = $('#hidCurStep').val();
		var portfolioProduct = JSON.stringify(products);		
		var url = base_root+'/front/crm/proposal/saveProposal.do?dateStr=' + new Date().getTime();
		var result = '';
		$.ajax({
			type:'post',
			async:false,
			url:url,
			data:
			{
				proposalId:crmProposalId,
				proposalName:proposalName,
				riskLevel:riskLevel,
				curStep:curStep,
				portfolioProduct:encodeURI(portfolioProduct)
			},
			success:function(data){
				$('.load-gif').hide();
				result = data;
				if(result.flag){
					layer.msg(globalProp['global.success.save'], {icon:2});
					var url = base_root+'/front/crm/proposal/createProposalSetFour.do?dateStr=' 
					+ new Date().getTime()+'&id='+crmProposalId+'&memberId='+getUrlParam('memberId');
					if(jump){
						setTimeout(function(){
							if(getUrlParam('edit') == '1'){
								url = urlUpdateParams(url, 'edit', '1');
							}
							window.location.href = url;
						},1000);
					}
				}else{
					layer.msg(globalProp['global.failed.save'], {icon:1});
				}
			},
			error: function(){
				$('.load-gif').hide();
			}
		});
		return result;
	}
	/**
	 * 收縮欄
	 */
	$(".proposal-more-ico").on("click",function(){
		$(this).parents(".stepThree-rows").toggleClass("account-rows-hide");
	});
	$(".order-setting-button").on("click",function(){
    	$(".proposal-mask-wrap").addClass("proposal-mask-show");
    	$(".proposal-mask-contents").addClass("proposal-mask-show");
    });
    $("#proposal-mask-close").on("click",function(){
    	$(".proposal-mask-wrap").removeClass("proposal-mask-show");
    	$(".proposal-mask-contents").removeClass("proposal-mask-show");
    });
    /**
     * 平均权重
     */
    function weightSetting(){
		var fundCount = $('.slider_input').length;
		var totalInvestmentAmt = $('#txtTotalInvestmentAmt').val();
		var html = '';
		var amount = '';
		var toCurrency = $('#portfolio-currency').val();
		$('.slider_input').each(function (i, n) {
            $('.slider_input').eq(i).val(parseInt(100/fundCount) + '%');
            amount = parseInt(totalInvestmentAmt/fundCount);
            if(typeof(amount) != 'undefined' && amount!=0){
            	html =
            		'<span class="conversion-flag">'
            	+ amount
            	+'</span>'
            	+'&nbsp;<span class="to-currency">'
            	+ toCurrency
            	+'</span>';
            }
            $('.allocation-amount').eq(i).empty().append(html);
        });
		if(parseInt(100%fundCount)!=0){
			$('.slider_input').eq(fundCount-1).val((parseInt(100/fundCount)+(100%fundCount)) + '%');
			amount = parseInt(totalInvestmentAmt/fundCount)+(totalInvestmentAmt%fundCount);
            if(typeof(amount) != 'undefined' && amount!=0){
            	html =
            		'<span class="conversion-flag">'
            	+ amount
            	+'</span>'
            	+'&nbsp;<span class="to-currency">'
            	+ toCurrency
            	+'</span>';
            }
            $('.allocation-amount').eq(fundCount-1).empty().append(html);
		}
    }
    /**
     * 货币选择
     */
    $('.portfolio-currency').click(function(){
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
    $('.proposal_xiala').on('mouseleave',function(){
    	$('#currency-choice').hide();
    });
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
	currencyType();
	/**
	 * Ranges Init
	 */
	function rangesInit(){
    	var rangeInstanse = $('.portfolio-table').find('.funds-single-slider');
    	$('.strategy_chart_tinput').each(function(i,n){
			var totalAmt = $('#txtTotalInvestmentAmt').val();
			if(totalAmt){
				totalAmt = totalAmt.replace(/,/g,'');
			}
			$('.strategy_chart_tinput').each(function(){
				var rate = $(this).val();
				var value = totalAmt*rate/100;
				$(this).closest('tr').find(".fund_amount").val(formatCurrency(value));
			});
    	});
    	rangeInstanse.each(function(index){
     		var self = $(this),
     			_start = 0,
     			_values = [];
 			var _val = $(this).closest('tr').find('.strategy_chart_tinput').val();
 			self.val(_val+','+_val);
    	 	var _test = self.jRange({
    				from: 0, 
    				to: 100,
    				step: 1, 
    				format: '%s%', 
    				showLabels: false, 
    				showScale: false,
    				isRange:true
    				, ondragstart :function(){
    					_values = self.val().split(',');
    					var totalValue = 0;
						$('.portfolio-table').find('.funds-single-slider').each(function(i,n){
							if(self.data('fund') != $(n).data('fund')){
								totalValue = totalValue + Number(($(n).val().split(','))[0]);
							}
			    		});
						totalValue = 100 - totalValue;
						self.jRange('setValue',_values[0]+','+totalValue);
					}, onstatechange : function(){
						totalAmt = $('#txtTotalInvestmentAmt').val();
						if(totalAmt){
							totalAmt = totalAmt.replace(/,/g,'');
						}
						_values = self.val().split(',');
						var tempValue = _values[0];
						var weight = 0;
						self.closest('td').find('.strategy_chart_tinput').val(tempValue);
						var currencyCode = $('#hidDefCurrency').val();
						var amount = '';
						if('JPY' == currencyCode){
							amount = parseFloat(Number(tempValue)*totalAmt/100).toFixed(0);
						}else{
							amount = parseFloat(Number(tempValue)*totalAmt/100).toFixed(2);
						}
						$(self).closest('tr').find('.fund_amount').val(formatCurrency(amount));
						tableTotalWeight();
						tableTotalAmount();
					}, ondragend : function(){
						_values = self.val().split(',');
						self.jRange('setValue',_values[0]+','+_values[0]);
						caluRiskLevel();
						_values = [];
					}
    		});
     	});
    	caluRiskLevel();
    	tableTotalAmount();
    }
	$(document).on("keyup",".strategy_chart_tinput",function(){
    	this.value=this.value.replace(/[^\.\d]/g,'');
    	this.value=this.value.replace('.','');

    	var self = $(this).siblings(".funds-slider-wrap").find('.funds-single-slider');
    	var totalValue = 0;
    	var rangeInstanse = $('.portfolio-table').find('.funds-single-slider');
		rangeInstanse.each(function(i,n){
			if(self.data('fund') != $(n).data('fund')){
				totalValue = totalValue + Number(($(n).val().split(','))[0]);
			}
		});
		totalValue = 100 - totalValue;
    	if(this.value > totalValue){
    		this.value = totalValue;
    	}
    });
    $(document).on("blur",".fund_amount",function(){
    	this.value = this.value.replace(/[^\.\d]/g,'');
    	this.value = Number(this.value).toFixed(2);
    	var _temp = 0;
    	$(".fund_amount").each(function(){
    		var _selfTemp = $(this).val().replace(/,/gm,'');
    		_temp += Number(_selfTemp);
    	});
    	$("#txtTotalInvestmentAmt").val(_temp.toFixed(2));
    	rangesInit();
    });
    $(document).on("blur",".strategy_chart_tinput",function(){
    	var self = $(this).siblings(".funds-slider-wrap").find('.funds-single-slider'),
    		selfVal = $(this).val();
    	self.jRange('setValue', $(this).val() +',' + $(this).val());
    	rangesInit();
		tableTotalWeight();
		tableTotalAmount();
    }); 
    $("#txtTotalInvestmentAmt").on("blur",function(){
    	this.value = this.value.replace(/[^\.\d]/g,'');
    	this.value = Number(this.value).toFixed(2);
    	var _tempVal = this.value ;
    	$(".strategy_chart_tinput").each(function(){
    		var _val = $(this).val(),
    		 selfval = (_val / 100 ) * _tempVal;
    		$(this).parents("table").find(".fund_amount").val(selfval.toFixed(2));
    	});
    });
    $(document).on("click",".rang_lock",function(){
    	var self = $(this).parents("table");
    	self.toggleClass("portfolio-slider-lock");
    	if(self.hasClass("portfolio-slider-lock")){
    		self.find(".funds-single-slider").jRange('disable');
    		self.find(".strategy_chart_tinput").attr("readonly","").end().find(".fund_amount").attr("readonly","");
    	}else{
    		self.find(".funds-single-slider").jRange('enable');
    		self.find(".strategy_chart_tinput").removeAttr("readonly").end().find(".fund_amount").removeAttr("readonly");
    	}
    });
    /**
     * 基金切換
     */
 	$(".products-list-wrap").on("click",".portfolio_top_ico",function(){
 		var tableFund = $(this).parents(".portfolio-table");
 		//表格置顶
 		var _par   = $(this).parents("tr"),
 			_first = $(this).parents(".portfolio-table").find("tr").eq(0);
		_first.find("td").each(function(index){
			if( !$(this).hasClass("portfolio-slider") && !$(this).hasClass("portfolio-amount")){
				var _temp = $(this).html(),
				_index = index;
				$(this).html(_par.children().eq(_index).html());
				_par.children().eq(_index).html(_temp);
			}
		});
		_first.find('.img_top_add').show();
		_first.find('.portfolio-table-exhibition').show();
		_par.find('.img_top_add').hide();
		_par.find('.portfolio-table-exhibition').hide();
		var spareFund = _par.attr('data-fund');
		_par.attr('data-fund',tableFund.attr('id'));
		tableFund.attr('id',spareFund);
		caluRiskLevel();
		allocationMap();
		separateness();
		aggregate();
 	});	
 	$(document).on("click",".portfolio-table-exhibition",function(){
 		$(this).parents("table").toggleClass("portfolio-table-active");
 	});
 	/**
 	 * Total Weight
 	 */
 	function tableTotalWeight(){
 		var totalWeight = 0.00;
 		$('.portfolio-table-data').each(function(i,n){
 			var weight = $(n).find('.strategy_chart_tinput').val();
 			totalWeight = Number(totalWeight)+Number(weight);
 		});
 		totalWeight = parseInt(totalWeight);
 		if(Number(totalWeight)>=100){
 			allocationMap();
 			aggregate();
 		}
 		$('#tableTotalWeight').text(totalWeight+'%');
 	}
 	/**
 	 * Total Amount
 	 */
 	function tableTotalAmount(){
 		var totalAmount = 0.00;
 		$('.portfolio-table-data').each(function(i,n){
 			var amount = $(n).find('.fund_amount').val();
 			amount = amount.replace(/,/gm,'');
 			totalAmount = Number(totalAmount)+Number(amount);
 		});
 		$('#tableTotalAmount').text(formatCurrency(totalAmount));
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
			aggregate();
		}else{
		// on
			$('.proposal-mask-wrap').show();
			$('.proposal-mask-contents').show();
			$('.proposal-mask-contents').css('top',$(window).height()*0.3+$(document).scrollTop());
			$(window).on('scroll',aipSettingLoadScroll);
		}
		var state = $(this).attr('data-state');
		if(state == 0){
			var proposalId = getUrlParam('id');
			//修改定投状态
		    $.post(base_root + '/front/crm/proposal/updateAipState.do?d='+new Date().getTime(),{proposalId:proposalId,state:state});
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
		if(validFlag){layer.msg(props['order.plan.alert.aip.exec.cycle.choose'], {icon:3});return;}  // 请选择定投周期
		if(!$('#txtAipAmount').val() || Number($('#txtAipAmount').val())==0){layer.msg(props['order.plan.alert.aip.amount.empty'], {icon:3});return;} // 定投金额不能为空或者0
		if(!$('#txtAipEndDate').val() && !$('#order-number-choice').val() && (!$('#order-money-choice').val() || $('#order-money-choice').val() == '-')){
			layer.msg(props['order.plan.alert.aip.stop.condition.empty'], {icon:3});return;  // 定投停止条件不能为空
		}
		//aip
    	var aipInitTime = $('.aip-detail-next-charge-date').text();
    	var chargeDay = $('#selChargeDay').val();
    	var aipAmount = $('#txtAipAmount').val();
    	aipAmount = aipAmount.replace(/,/g,'');
    	var aipEndType = $('.aip-end-type').find('.order-cycle-active').data('value');
    	var aipEndDate = $('#txtAipEndDate').val();
    	var aipEndCount = $('#order-number-choice').val();
    	var aipEndTotalAmount = $('#order-money-choice').val();
    	if('-' == aipEndTotalAmount){
    		aipEndTotalAmount = '';
    	}
    	var currencyCode = $('#portfolio-currency').attr('data-code');
    	var riskLevel = $('#risk-level-average').text();
		if(riskLevel){
			riskLevel = Number(riskLevel.trim());
			if(isNaN(riskLevel)){
				riskLevel = '';
			}
		}
    	var url = base_root+'/front/crm/proposal/saveProposaAip.do?r=' + new Date().getTime();
    	var data = {
    			proposalId:getUrlParam('id'),
    			riskLevel:riskLevel,
    			currencyCode:currencyCode,
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
    				layer.msg(props['global.success.save'], {icon:2});
    				$('.proposal-mask-wrap').hide();
    				$('.proposal-mask-contents').hide();
    				$('#aipState').addClass("aipstate_button_active");
    				$('#aipState').attr('data-state',1);
    				$('#aipChange').show();
    				$('#txtTotalInvestmentAmt').val(formatCurrency(aipAmount));
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
    				aggregate();
    			}else{
    				inputCtrl(false);
    				layer.msg(props['global.failed.save'], {icon:1});
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
		/*if(Number(aipAmount) < Number(tableTotalAmount)){
			layer.msg(props['order.plan.alert.aip.amount.control.Minimum']); //定投金额不能小于当前产品总金额
			$('#txtAipAmount').val(formatCurrency(tableTotalAmount));
		}else{
			$('#txtAipAmount').val(formatCurrency(aipAmount));
		}*/
	});
	/**
	 * init Repayment Date
	 */
	if(!$('.aip-detail-next-charge-date').text()){
		var execCycle = $('.aip-exec-cycle .order-cycle-active').data('day');
		var timeDistance = $('#selChargeDay').val();
		var date = getNextCycleTime(new Date(),execCycle,timeDistance);
		$('.aip-detail-next-charge-date').text(date);
	}
	$('#order-money-choice').on('blur',function(){
		var cashAvailable = $('.cash-available-value').text().replace(/,/g,'');
		var aipTotalAmount = $('#order-money-choice').val().replace(/,/g,'');
		var aipAmount = $('#txtAipAmount').val().replace(/,/g,'');
		if(Number(aipTotalAmount) < Number(aipAmount)){
			layer.msg(props['order.plan.alert.aip.total.amount.control.Minimum'], {icon:3}); //定投总金额不能小于每次定投金额
			$('#order-money-choice').val(formatCurrency(aipAmount));
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
			$('.input-ctrl').each(function(){
				if($(this).is('input')){
					$(this).attr('readonly','readonly');
				}else if($(this).is('select')){
					$(this).attr('disabled','disabled');
				}
			});
		}else{
			$('.input-ctrl').each(function(){
				if($(this).is('input')){
					$(this).removeAttr('readonly');
				}else if($(this).is('select')){
					$(this).removeAttr('disabled');
				}
			});
		}
	}
      /********************************************Commom**************************************************/
      /**
       * 新頁面彈窗
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
       * Array unique
       * @returns {Array}
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
  	  * get Url Param
  	  */
  	 function getUrlParam(name){  
 	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
 	    var r = window.location.search.substr(1).match(reg);  
 	    if (r!=null) return unescape(r[2]);  
 	    return null;  
 	 };
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
     * URL add param
     */
 	 function urlUpdateParams(url, name, value) {
         var r = url;
         if (r != null && r != 'undefined' && r != "") {
             value = encodeURIComponent(value);
             var reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
             var tmp = name + "=" + value;
             if (url.match(reg) != null) {
                 r = url.replace(eval(reg), tmp);
             }else{
                 if (url.match("[\?]")) {
                     r = url + "&" + tmp;
                 } else {
                     r = url + "?" + tmp;
                 }
             }
         }
         return r;
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
 	Array.prototype.max = function(){ 
		return Math.max.apply({},this) 
	} 
	Array.prototype.min = function(){ 
		return Math.min.apply({},this) 
	}
});