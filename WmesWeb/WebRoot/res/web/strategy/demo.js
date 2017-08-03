define(function(require) {
	var $ = require('jquery');
		    require("echarts");
		    require('slider');
			require('layer');
			require('jqueryRange');
			require('laydate');
    var angular = require('angular');
		    
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
	});
	$("#portfolioName-edit-img").on("click",function(){
    	//修改portfolio 名字
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
    	var period = '';
    	return {period:period};
    })();
    //折线图 1
	$(".builder-chart-wrap").width();
	function aggregate(){
		$('.builder-chart-wrap').show();
		$('.builder-chart-wrap-nodata-tips').hide();
		var option = {
		    tooltip: {
		        trigger: 'axis',
		        position: function (pt) {
		            return [pt[0], '10%'];
		        }
		    },
		    title: {
		        left: 'center',
		        text: ''
		    },
		    toolbox: {
		        feature: {
		            dataZoom: {
		                yAxisIndex: 'none'
		            },
		            restore: {},
		            saveAsImage: {}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: []
		    },
		    yAxis: {
		        type: 'value',
		        boundaryGap: [0, '100%']
		    },
		    dataZoom: [{
		        type: 'inside',
		        start: 0,
		        end: 100
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
		        }
		    }],
		    series: []
		    };		
		$("#builder-chart-one").width($(".proposal-step-wrap").width());
		var myChart = echarts.init(document.getElementById('builder-chart-one'));
  		myChart.setOption(option,true);
  		myChart.showLoading();
  		getIncomePercentageTotal(myChart);
    }
	//获取总收益数据
	function getIncomePercentageTotal(myChart){
		var xAxisData = [];
		var aggregate = {
				totalDataSeries:[],
				nameData:[],
				aipDataSeries:[]
		};
		var fundIds = getFundIds();
		var period = eChartsData.period,
			type = '';
		if('2Yr' == period ||'3Yr' == period){
			type = 'W';
		}else if('5Yr' == period){
			type = 'M';
		}
		var url = base_root+'/front/transaction/fundIncomePercentageTotal.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				fundIds:fundIds,
				proposalId:getUrlParam('id'), 
				type:type,
				period:period
			},
			success:function(result){
				if(result.flag){
					var incomePercentageTotal = result.incomePercentageTotal,
					    aipIncomePercentageTotal = result.aipIncomePercentageTotal;
					if(typeof(incomePercentageTotal)!='undefined'){
						var incomePercentage = [];
						var aipIncomePercentage = [];
						$.each(incomePercentageTotal,function(i,n){
							//折线图一
							incomePercentage[i] = n.incomePercentage;
							xAxisData[i] = n.marketDate;
							aggregate.nameData.push(prop['create.portfolio.step.three.incomePercentageTotal']);
							//aip
							if(aipIncomePercentageTotal[i]!=null){
								aipIncomePercentage[i] = aipIncomePercentageTotal[i].incomePercentage;
								aggregate.nameData.push(prop['create.portfolio.step.three.AIPIncomePercentageTotal']);
							}
						});
						aggregate.totalDataSeries = incomePercentage;
						aggregate.aipDataSeries = aipIncomePercentage;
					}
				}
				if(aggregate.totalDataSeries.length > 0 && aggregate.aipDataSeries.length == 0){
					myChart.hideLoading();
					myChart.setOption({    
						legend: {
							left:750,
							top:30,
					        data:aggregate.nameData
					    },    
						xAxis: {
					        type: 'category',
					        boundaryGap: false,
					        data: xAxisData
					    },
					    series: [{
				            name:prop['create.portfolio.step.three.incomePercentageTotal'],
				            type:'line',
				            smooth:true,
				            symbol: 'none',
				            sampling: 'average',
				            itemStyle: {
				                normal: {
				                    color: 'rgb(255, 70, 131)'
				                }
				            },
				            areaStyle: {
				                normal: {
				                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
				                        offset: 0,
				                        color: 'rgb(255, 158, 68)'
				                    }, {
				                        offset: 1,
				                        color: 'rgb(255, 70, 131)'
				                    }])
				                }
				            },
				            data: aggregate.totalDataSeries
				        }
				        ]
			        });
				}else if(aggregate.totalDataSeries.length > 0 && aggregate.aipDataSeries.length >0){
					myChart.hideLoading();
					myChart.setOption({    
						legend: {
							left:750,
							top:30,
					        data:aggregate.nameData
					    },    
						xAxis: {
					        type: 'category',
					        boundaryGap: false,
					        data: xAxisData
					    },
					    series: [{
				            name:prop['create.portfolio.step.three.incomePercentageTotal'],
				            type:'line',
				            smooth:true,
				            symbol: 'none',
				            sampling: 'average',
				            itemStyle: {
				                normal: {
				                    color: 'rgb(255, 70, 131)'
				                }
				            },
				            areaStyle: {
				                normal: {
				                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
				                        offset: 0,
				                        color: 'rgb(255, 158, 68)'
				                    }, {
				                        offset: 1,
				                        color: 'rgb(255, 70, 131)'
				                    }])
				                }
				            },
				            data: aggregate.totalDataSeries
				        },{
				            name:prop['create.portfolio.step.three.AIPIncomePercentageTotal'],
				            type:'line',
				            smooth:true,
				            symbol: 'none',
				            sampling: 'average',
				            itemStyle: {
				                normal: {
				                    color: 'rgba(25, 100, 150, 0.5)'
				                }
				            },
				            areaStyle: {
				                normal: {
				                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
				                        offset: 0,
				                        color: 'rgb(129, 227, 238)'
				                    }, {
				                        offset: 1,
				                        color: 'rgb(25, 183, 207)'
				                    }])
				                }
				            },
				            data: aggregate.aipDataSeries
				        }
				        ]
			        });
				}else{
					myChart.hideLoading();
					myChart.dispose();
					$('.builder-chart-wrap').hide();
					$('.builder-chart-wrap-nodata-tips').show();
				}
			},error:function(){
				myChart.hideLoading();
				myChart.dispose();
				$('.builder-chart-wrap').hide();
				$('.builder-chart-wrap-nodata-tips').show();
			}
		});
	}
	//折线图二
    function separateness(){
    	$('.builder-chart-wrap').show();
		$('.builder-chart-wrap-nodata-tips').hide();
		var option = {
		    tooltip: {
		        trigger: 'axis',
		        position: function (pt) {
		            return [pt[0], '10%'];
		        }
		    },
		    title: {
		        left: 'center',
		        text: ''
		    },
		    toolbox: {
		        feature: {
		            dataZoom: {
		                yAxisIndex: 'none'
		            },
		            restore: {},
		            saveAsImage: {}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: []
		    },
		    yAxis: {
		    	type : 'value',
	            axisLabel : {
	                formatter: '{value} %'
	            },
	            splitNumber:10
		    },
		    dataZoom: [{
		        type: 'inside',
		        start: 0,
		        end: 100
		    },{
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
		        }
		    }],
		    series: []
		};
		$("#builder-chart-one").width($(".proposal-step-wrap").width());
		var myChart = echarts.init(document.getElementById('builder-chart-one'));
			myChart.setOption(option,true);
			
		myChart.showLoading();
  		getIncomePercentage(myChart);
    }
    function getIncomePercentage(myChart){
    	var xAxisData = [];
		var separateness = {
				series:[]
		};
		var fundIds = getFundIds();
		var period = eChartsData.period,
			type = '';
		if('2Yr' == period ||'3Yr' == period){
			type = 'W';
		}else if('5Yr' == period){
			type = 'M';
		}
		var url = base_root+'/front/transaction/findFundMarketList.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				fundIds:fundIds,
				portfolioId:getUrlParam('portfolioId'),
				type:type,
				period:period
			},
			success:function(result){
				if(result.flag){
					var fundMarketDataVOs = result.fundMarketDataVOs,
						fundMarketName = [];
					$.each(fundMarketDataVOs,function(i,n){
						if(n.fundIncomePercentageVOs == ''){
							//layer.msg('sorry,'+n.fundName+' no data!', { icon: 0, time: 3000 });
						}else{
							//折线图二
							var separatenessSeries = {};
							var incomePercentage = [];
							separatenessSeries['name'] = n.fundName; 
							fundMarketName.push(n.fundName);
							separatenessSeries['type'] = 'line';
							if(n.fundIncomePercentageVOs.length !=0 ){
								$.each(n.fundIncomePercentageVOs,function(j,m){
									//折线图二
									incomePercentage[j] = m.incomePercentage;
									xAxisData[j] = m.marketDate;
								});
							}
							separatenessSeries['data'] = incomePercentage;
							separateness.series.push(separatenessSeries);
						}
					});
				}
				if(separateness.series.length>0){
					myChart.hideLoading();
					myChart.setOption({  
						xAxis: {
					        type: 'category',
					        boundaryGap: false,
					        data: xAxisData
					    },
					    series: separateness.series
			        });
				}else{
					myChart.hideLoading();
					myChart.dispose();
					$('.builder-chart-wrap').hide();
					$('.builder-chart-wrap-nodata-tips').show();
				}
			},error:function(){
				myChart.hideLoading();
				myChart.dispose();
				$('.builder-chart-wrap').hide();
				$('.builder-chart-wrap-nodata-tips').show();
			}
		});
    }
    //饼图
    function allocationMap(){
    	$('.allocation-chart').show();
    	$('.allocation-chart-title-1').show();
		$('.allocation-chart-title-2').show();
        $('.allocation-chart-nodata-tips').hide();
    	var optionOne = {
    		    title : {
    		        text: '',
    		        subtext: '',
    		        x:'center'
    		    },
    		    tooltip : {
    		        trigger: 'item',
    		        formatter: "{b} : {c}%"
    		    },
    		    legend: {
    		        orient: 'horizontal',
    		        top: '90%',
    		        left: '20%',
    		        data: []
    		    },
    		    series : [
    		        {
    		            name: 'weight',
    		            type: 'pie',
    		            radius : '60%',
    		            center: ['38%', '40%'],
    		            data:[]
    		        }
    		    ]
    		};
    	var optionTwo = {
    			title : {
    				text: '',
    				subtext: '',
    				x:'center'
    			},
    			tooltip : {
    				trigger: 'item',
					formatter: "{b} : {c}%"
    			},
    			legend: {
    				orient: 'horizontal',
    				top: '90%',
    				left: '18%',
    				data: []
    			},
    			series : [{
		            name: 'weight',
		            type: 'pie',
		            radius : '60%',
		            center: ['38%', '40%'],
		            data:[]
		        }]
    	};
		$("#allocation-chart-propose").width($(".builder-tab").width() * 0.45);
		$("#allocation-chart-change").width($(".builder-tab").width() * 0.45);
		var myChartOne = echarts.init(document.getElementById('allocation-chart-propose'));
		myChartOne.setOption(optionOne,true);
		var myChartTwo = echarts.init(document.getElementById('allocation-chart-change'));
		myChartTwo.setOption(optionTwo,true);

		myChartOne.showLoading();
		myChartTwo.showLoading();
		getPieData(myChartOne,myChartTwo);
    };
    //饼图数据获取
    function getPieData(myChartOne,myChartTwo){
    	$('.allocation-chart-title-1-emtpy').hide();
    	var fundIds = getFundIds();
		var allocationMap = {
    			legendData:[],
    			seriesData:[]
    	};
    	var allocationMapTwo = {
    			legendData:[],
    			seriesData:[]
    	};
		var url = base_root+'/front/transaction/getPieData.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{fundIds:fundIds},
			success:function(result){
				if(result.flag){
					var geoAllocations = result.geoAllocations,
						sectorTypes = result.sectorTypes;
    				//基金分布区域
					var key = '';
					if(geoAllocations != null){
						var total = 0;
						for(key in geoAllocations[0]){
							total = total + Number((geoAllocations[0])[key]);
						}
						for(key in geoAllocations[0]){
							allocationMap.legendData.push(key);
							var allocationMapSeries = {};
							allocationMapSeries['name'] = key;
							allocationMapSeries['value'] = parseFloat(Number((geoAllocations[0])[key])/total*100).toFixed(2);
							allocationMap.seriesData.push(allocationMapSeries);
						}
					}
					myChartOne.hideLoading();
					myChartOne.setOption({        
			        	legend: {
		    				orient: 'horizontal',
		    				top: '75%',
		    				left: '18%',
		    				data: allocationMap.legendData
		    			},
		    			series : [{
			        	   name: 'weight',
			        	   type: 'pie',
			        	   radius : '60%',
			        	   center: ['38%', '40%'],
			        	   label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%",
					                show:false    
				                }
				                
				            },
			        	   data:allocationMap.seriesData
			            }]
			        });
					//基金类别
					if(sectorTypes != null){
						var total = 0;
						for(key in sectorTypes[0]){
							total = total + Number((sectorTypes[0])[key]);
						}
						for(key in sectorTypes[0]){
							allocationMapTwo.legendData.push(key);
							var allocationMapSeries = {};
							allocationMapSeries['name'] = key;
							allocationMapSeries['value'] = parseFloat(Number((sectorTypes[0])[key])/total*100).toFixed(2);
							allocationMapTwo.seriesData.push(allocationMapSeries);
						}
					}
					myChartTwo.hideLoading(); 
					if(allocationMap.seriesData.length == 0 && allocationMapTwo.seriesData.length == 0){
						$('.allocation-chart-title-1').hide();
						$('.allocation-chart-title-2').hide();
						$('.allocation-chart').hide();
				        $('.allocation-chart-nodata-tips').show();
					}
					myChartTwo.setOption({        
			        	legend: {
		    				orient: 'horizontal',
		    				top: '75%',
		    				left: '18%',
		    				data: allocationMapTwo.legendData
		    			},
		    			series : [{
			        	   name: 'weight',
			        	   type: 'pie',
			        	   radius : '60%',
			        	   center: ['38%', '40%'],
			        	   label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%",
					                show:false    
				                }
				                
				            },
			        	   data:allocationMapTwo.seriesData
			            }]
			        });
				}
			},error:function(){
				myChartOne.hideLoading();  
		        myChartTwo.hideLoading();
		        $('.allocation-chart').hide();
		        $('.allocation-chart-nodata-tips').show();
			}
		});
    }
    //柱状图	
    function riskMap(){
		var	option = {
			    color: ['#3398DB'],
			    tooltip : {
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    xAxis : [
			        {
			            type : 'category',
			            data : [],
			            axisTick: {
			                alignWithLabel: true
			            }
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [
			        {
			            name:'',
			            type:'bar',
			            barWidth: '60%',
			            data:[]
			        }
			    ]
			};
		var myChart = echarts.init(document.getElementById('risk-analysis-chart'));
		myChart.setOption(option,true);
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
	//update chart 点击事件
	$('#btnUpdateChart').click(function(){
		refashData();
	});
	//重新获取数据 刷新视图
	function refashData(){
		//刷新 chart
		allocationMap();
		//separateness();
		//riskMap();
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
		if(fundIds == '{{items.fundId}}')return;
		return fundIds;
	}
	//refashData();
    /**********************************ECharts End**************************************/
    
	//风险计算
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
		if(typeof(riskLevels[0]) != 'undefined'){
			//最高风险
			$('#risk-level-max').text(riskLevels[0]);
		}
		var fundCount = $('.portfolio-table-data tbody tr').length;
		if(average!=0 && fundCount!=0){
			//加权平均风险
			$('#risk-level-average').text(Math.round(average));
		}
	};
   
    //弹出‘添加基金产品’页面   quick add
    $(".btnAddFund").on("click",quickAddFund);
    
    function quickAddFund(){
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
    
    //弹出‘添加基金产品’页面     该策略下的基金产品
	$("#btnStrategyFundList").on("click",function(){
		var strategyId = $('#hidStrategyId').val();
		var url=base_root+"/front/fund/info/getFundListForAllocation.do?view=true1&select=true&strategyId="+strategyId;
		openResWin(url,1200,600,"strategyFundSelector");
	});
	//弹出‘添加基金产品’页面    funds screener 页面
	$("#btnFundScreen").on("click",function(){
		var url=base_root+"/front/ifa/info/fundsscreener.do";
		openResWin(url,1200,600,"fundScreener");
	});
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
	
	//angular
	var mybase = angular.module('proposalTable', []);
	mybase.controller('proposalTableCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.tableActive = [];
		//table add fund
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
							layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added']);
						}else if($('tr[data-fund="'+n+'"]').length > 0){
							var fundName = $('tr[data-fund="'+n+'"]').find('.portfolio_table_names').text();
							layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added']);
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
						layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added']);
					}else if($('tr[data-fund="'+fundIds+'"]').length > 0){
						var fundName = $('tr[data-fund="'+fundIds+'"]').find('.portfolio_table_names').text();
						layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added']);
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
				//selectFund = $scope.selectFund.fundId;
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
							layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added']);
						}else if($('tr[data-fund="'+fundIds+'"]').length > 0){
							var fundName = $('tr[data-fund="'+fundIds+'"]').find('.portfolio_table_names').text();
							layer.msg('【'+fundName+'】'+prop['create.proposal.step.three.alert.added']);
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
				if($.inArray(n, existFund)<0){
					fundMaps.push(fundMap);
				}
				
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
         		// //console.log(result);
         		if(result.flag){
         			if($scope.fundInfoList){
         				$scope.fundInfoList = $scope.fundInfoList.concat(result.fundInfoList);
         			}else{
         				$scope.fundInfoList = result.fundInfoList;
         			}
         			
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
	     					//tableTotalAmount();
	     				},100);
	     			}
         		}else{
         			$('.portfolio-table-inf').hide();
					$('.no-funds-yet').show();
         		}
         	 });
		};
		
		$(document).on("click",".portfolio_top_add",function(){
			$scope.selectFund = $(this).closest('table').attr('id');
			quickAddFund();
		});
		$(document).on("click",".portfolio-del",function(){
			var self = $(this),
			_wrap = self.parents(".portfolio-table"),
			_length = self.parents(".portfolio-table").find("tr").length,
			_index = self.parents("tr").index();
			var _num = self.parents("table").find(".funds-single-slider").val().toString().split(',')[0];

			if( _length  == 1){
				//如果该基金没有候选基金
				layer.confirm(prop['create.proposal.step.three.alert.deleteFund'], {
					  title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
				}, function(index){
					  _wrap.remove();
					  tableTotalAmount();
					  tableTotalWeight();
					  if($('.portfolio-table-data').length == 0){
							$('.portfolio-table-inf').hide();
							$('.no-funds-yet').show();
					  }
					  refashData();
					  layer.close(index);
				});
			}else{
				if( _index == 0){
					layer.confirm(prop['create.proposal.step.three.alert.deleteFundAndCandidate'], {
						  title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
					}, function(index){
					  _wrap.remove();
					  refashData();
					  layer.close(index);
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
					});
				}else{
					var self = this;
					layer.confirm(prop['create.proposal.step.three.alert.deleteCandidate'], {
						  title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
					}, function(index){
						  $(self).parents("tr").remove();
						  refashData();
						  layer.close(index);
					});
				}
				tableTotalAmount();
				tableTotalWeight();
			}
			$('#ids').val('');
	 	});
		
		(function getProductdetails(){
			var toCurrencyCode = $('#portfolio-currency').attr('data-code');
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
				  				  toCurrency:toCurrencyCode,
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
					     					//tableTotalAmount();
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
		})();
	}]);
	//end
    
	// Array unique
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

    //fund weight setting
    function setFundWeight(){
    	layer.open({
            title: 'Funds Weight Setting',
            type: 1,
            skin: 'layui-layer-rim', //加上邊框
            area: ['770px', '380px'], //寬高
            content: $('#div_setrange').load(base_root+'/front/ifa/info/testslider.do')
        });
    }
    //Allocation Amount 重新计算
    $('#btnSetrange').click(function(){
    	var totalAmt = $('#txtTotalInvestmentAmt').val();
    	var toCurrency = $('#portfolio-currency').val();
    	if(typeof(totalAmt) != 'undefiend' && totalAmt != ''){
    		$('.slider_input').each(function (i, n) {
    			 var weight = ($(this).val().split('%'))[0];
    			 var amount = parseFloat(totalAmt*weight/100).toFixed(2);
    			 if(typeof(amount) != 'undefined'){
    	         	var html =
    	         		'<span class="conversion-flag">'
    	         	+ amount
    	         	+'</span>'
    	         	+'&nbsp;<span class="to-currency">'
    	         	+ toCurrency
    	         	+'</span>';
    	         }
    	         $('.allocation-amount').eq(i).empty().append(html);
        	});
    	}
	});

    //Cancel 按钮
    $('#btnCancel').click(function(){
    	$('.portfolio-table tbody tr:gt(0)').remove();
    });
    
    //跳转  step4
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
    			layer.msg("Each product has at least one optional product!");
    			return;
    		}
    		saveImage(true);
    		//saveProposal(true);
    	}else{
    		layer.msg("The product list cannot be empty!");
    	}
    });
    
    //跳转 step2
	$('#btnNextTwo').click(function(){
		var name = $('#invName').text();
		var id = $('#hidCrmProposalId').val();
		var url = base_root+'/front/crm/proposal/createProposalSetTwo.do?name='+name;
		if(id != '' && typeof(id) != 'undefiend'){
			url += '&id='+id;
		}
		window.location.href = url;
	});
    
	//SAVE
	$('#btnSave').click(function(){
		if($('.portfolio-table-data').length>0){
			saveImage(false);
		}else{
			layer.msg("The product list cannot be empty!");
		}
		//saveProposal(false);
	});
	
	/*function saveCreatePDF(jump){
		var crmProposalId = $('#hidCrmProposalId').val();
		var url = base_root+'/front/crm/proposal/proposalExportToPDF.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:
			{proposalId:crmProposalId}
			,beforeSend:function(){
				layer.msg(globalProp['global.saving']);
			},
			success:function(data){
				saveProposal(jump);
			}
		});
	}*/
	
	function saveImage(jump){
		//aggregate();
		var crmProposalId = $('#hidCrmProposalId').val();
		var aggregateChart = echarts.getInstanceByDom(document.getElementById('builder-chart-one')),
			allocationChart = echarts.getInstanceByDom(document.getElementById('allocation-chart-propose')),
			allocationChartTwo = echarts.getInstanceByDom(document.getElementById('allocation-chart-change'));
			//riskAnalysisChart = echarts.getInstanceByDom(document.getElementById('risk-analysis-chart'));
		var aggregatePng = aggregateChart.getDataURL({
			    type: 'png',
			    pixelRatio: 2,
			    backgroundColor: '',
			    excludeComponents: ['dataZoom']
			}),
			allocationPng = allocationChart.getDataURL({
			    type: 'png',
			    pixelRatio: 2,
			    backgroundColor: '',
			    excludeComponents: ['toolbox']
			}),
			allocationTwoPng = allocationChartTwo.getDataURL({
			    type: 'png',
			    pixelRatio: 2,
			    backgroundColor: '',
			    excludeComponents: ['toolbox']
			}),
			/*riskAnalysisPng = riskAnalysisChart.getDataURL({
			    type: 'png',
			    pixelRatio: 1,
			    backgroundColor: '',
			    excludeComponents: 'toolbox'
			});*/
		aggregatePng = (aggregatePng.split(','))[1];
		allocationPng = (allocationPng.split(','))[1];
		allocationTwoPng = (allocationTwoPng.split(','))[1];
		//riskAnalysisPng = (riskAnalysisPng.split(','))[1];
		var url = base_root+'/front/crm/proposal/saveEChartsImage.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:
			{
				proposalId:crmProposalId,
				allocationPng:allocationPng,
				allocationTwoPng:allocationTwoPng,
				//riskAnalysisPng:riskAnalysisPng,
				aggregatePng:aggregatePng
			},beforeSend:function(){
				//layer.msg(globalProp['global.saving']);
			},
			success:function(data){
				//saveCreatePDF(jump);
				saveProposal(jump);
			}
		});
	}
	
	function saveProposal(jump){
		var products = [];
		$('.products-list-wrap').find('.strategy_chart_tinput').each(function(i,n){
    		var product = {};
    		var fundWeight = '';
    		if($(this).val() && typeof($(this).val())!='undefiend'){
    			fundWeight = $(this).val().substring(0,$(this).val().length-1);
    		}
    		product['fundId'] = $(this).closest('table').attr('id');
    		product['fundWeight'] = parseFloat(fundWeight).toFixed(2);
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
		//proposal 信息
		var proposalName = $('#txtProposalName').val();
		var crmProposalId = $('#hidCrmProposalId').val();
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
				curStep:curStep,
				portfolioProduct:encodeURI(portfolioProduct)
			},beforeSend:function(){
				//layer.msg(globalProp['global.saving']);
			},
			success:function(data){
				result = data;
				if(result.flag){
					layer.msg(globalProp['global.success.save']);
					var url = base_root+'/front/crm/proposal/createProposalSetFour.do?dateStr=' 
					+ new Date().getTime()+'&id='+crmProposalId+'&memberId='+getUrlParam('memberId');
					if(jump){
						setTimeout(function(){
							window.location.href = url;
						},1000);
					}
				}else{
					layer.msg(globalProp['global.failed.save']);
				}
			}
		});
		return result;
	}
	
	// aip state
	$('#aipState p:gt(0)').on('click',function(){
		var state = $(this).data('state');
		if(state == 1){
			$('#aip-info').slideDown();
		}else if(state == 0){
			$('#aip-info').slideUp();
		}
		var proposalId = getUrlParam('id');
		//修改定投状态
	    $.post(base_root + '/front/crm/proposal/updateAipState.do?d='+new Date().getTime(),{proposalId:proposalId,state:state});
	});
	
	// aip-exec-cycle 周期选择事件
	$('#aipExecCycle p:gt(0)').on('click',function(){
		var day = $(this).data('day');
		var dayHTML = '';
		var date = new Date();
		var chargeDay = [];
		var dayCount;
		var month = '';
		var dayOfMonth = '';
		if('m'==day){
			//月
			for(var i=1;i<=31;i++){
				chargeDay[i-1] = i;
			}
			date.setMonth(date.getMonth()+1);
			date.setDate(1);
			$.each(chargeDay,function(i,n){
				dayHTML +='<option value="'+n+'">'+ n +'</option>';
			});
		}else if('b'==day){
			//双周
			chargeDay = [prop['create.portfolio.step.three.sun']
			,prop['create.portfolio.step.three.mon']
			,prop['create.portfolio.step.three.tue']
			,prop['create.portfolio.step.three.web']
			,prop['create.portfolio.step.three.thu']
			,prop['create.portfolio.step.three.fri']
			,prop['create.portfolio.step.three.sat']];
			// var getDay = $('#selChargeDay').val();
			dayCount = date.getDate()-date.getDay()+14;
			if(dayCount>30){
				dayCount = dayCount-30;
				date.setDate(dayCount);
				date.setMonth(date.getMonth()+3);
			}else{
				date.setDate(dayCount);
			}
			$.each(chargeDay,function(i,n){
				dayHTML +='<option value="'+i+'">'+ n +'</option>';
			});
		}else if('w'==day){
			//周
			chargeDay = [prop['create.portfolio.step.three.sun']
			,prop['create.portfolio.step.three.mon']
			,prop['create.portfolio.step.three.tue']
			,prop['create.portfolio.step.three.web']
			,prop['create.portfolio.step.three.thu']
			,prop['create.portfolio.step.three.fri']
			,prop['create.portfolio.step.three.sat']];
			dayCount = date.getDate()-date.getDay()+7;
			if(dayCount>30){
				dayCount = dayCount-30;
				date.setDate(dayCount);
				date.setMonth(date.getMonth()+3);
			}else{
				date.setDate(dayCount);
			}
			$.each(chargeDay,function(i,n){
				dayHTML +='<option value="'+i+'">'+ n +'</option>';
			});
		}
		$('#selChargeDay').empty().append(dayHTML);
		//默认扣款日
		if(date.getMonth()<10){month = '0'+(date.getMonth()+1);}else{month = (date.getMonth()+1);}
		if(date.getDate()<10){dayOfMonth = '0'+date.getDate();}else{dayOfMonth = date.getDate();}
		date = date.getFullYear()+'-'+month+'-'+dayOfMonth;
		$('#txtNextChargeDate').text(date);
	});
	
	
	if($('#hidAipTimeDistance').val() != null && 
			typeof($('#hidAipTimeDistance').val()) != 'undefined'){
		chargeDay();
	}
	if($('#hidAipEndDate').val() != null && 
			typeof($('#hidAipEndDate').val()) != 'undefined'){
		var aipEndDate = $('#hidAipEndDate').val();
		if(aipEndDate.indexOf(' ') > -1){
			aipEndDate = (aipEndDate.split(' '))[0];
			$('#txtAipEndDate').val(aipEndDate);
		}
	}
	//选择 ChargeDay 扣款日
	$('#selChargeDay').change(chargeDay);
	function chargeDay(){
		var date = new Date();
		var day = '';
		var differenceDay,
			dayCount,
			lastDay;
		$('#aipExecCycle p:gt(0)').each(function(){
			if($(this).hasClass("order-cycle-active")){
				day = $(this).data('day');
			}
		});
		var month = '';
		var dayOfMonth = '';
		if('w'==day){
			//周 计算
			if($('#selChargeDay').val() == prop['create.portfolio.step.three.sun']){
				differenceDay = date.getDay()-1;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.mon']){
				differenceDay = date.getDay()-2;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.tue']){
				differenceDay = date.getDay()-3;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.web']){
				differenceDay = date.getDay()-4;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.thu']){
				differenceDay = date.getDay()-5;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.fri']){
				differenceDay = date.getDay()-6;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.sat']){
				differenceDay = date.getDay()-7;
			}else{
				differenceDay = date.getDay()-parseInt($('#selChargeDay').val());
			}
			 	dayCount = date.getDate()-differenceDay+7;
				lastDay = getLastDay(date.getFullYear(),date.getMonth()+1);
			if(dayCount>lastDay){
				dayCount = dayCount-lastDay;
				date.setDate(dayCount);
				date.setMonth(date.getMonth()+3);
			}else{
				date.setDate(dayCount);
			}
			if(date.getMonth()<10){month = '0'+(date.getMonth()+1);}else{month = (date.getMonth()+1);}
			if(date.getDate()<10){dayOfMonth = '0'+date.getDate();}else{dayOfMonth = date.getDate();}
			date = date.getFullYear()+'-'+month+'-'+dayOfMonth;
		}else if('b'==day){
			//双周 计算
			if($('#selChargeDay').val() == prop['create.portfolio.step.three.sun']){
				differenceDay = date.getDay()-1;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.mon']){
				differenceDay = date.getDay()-2;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.tue']){
				differenceDay = date.getDay()-3;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.web']){
				differenceDay = date.getDay()-4;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.thu']){
				differenceDay = date.getDay()-5;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.fri']){
				differenceDay = date.getDay()-6;
			}else if($('#selChargeDay').val() == prop['create.portfolio.step.three.sat']){
				differenceDay = date.getDay()-7;
			}else{
				differenceDay = date.getDay()-parseInt($('#selChargeDay').val());
			}
				dayCount = date.getDate()-differenceDay+15;
			//获取当月天数
				lastDay = getLastDay(date.getFullYear(),date.getMonth()+1);
			if(dayCount>lastDay){
				dayCount = dayCount-lastDay;
				date.setDate(dayCount-1);
				date.setMonth(date.getMonth()+3);
			}else{
				date.setDate(dayCount);
			}
			if(date.getMonth()<10){month = '0'+(date.getMonth()+1);}else{month = (date.getMonth()+1);}
			if(date.getDate()<10){dayOfMonth = '0'+date.getDate();}else{dayOfMonth = date.getDate();}
			date = date.getFullYear()+'-'+month+'-'+dayOfMonth;
		}else if('m'==day){
			//月 计算
			if(parseInt($('#selChargeDay').val())==31){
				//获取下个月天数
				lastDay = getLastDay(date.getFullYear(),date.getMonth()+2);
				date.setMonth(date.getMonth()+2);
				if(date.getMonth()<10){month = '0'+date.getMonth();}
				if(date.getDate()<10){dayOfMonth = '0'+date.getDate();}
				date = date.getFullYear()+'-'+month+'-'+dayOfMonth;
			}else{
				date.setMonth(date.getMonth()+2);
				date.setDate(parseInt($('#selChargeDay').val()));
				if(date.getMonth()<10){month = '0'+date.getMonth();}else{month = date.getMonth();}
				if(date.getDate()<10){dayOfMonth = '0'+date.getDate();}else{dayOfMonth = date.getDate();}
				date = date.getFullYear()+'-'+month+'-'+dayOfMonth;
			}
		}
		if(day==''){
			date = '-';
		}
		$('#txtNextChargeDate').text(date);
	}
	
	//获取当月天数
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
    
    $(".aip-exec-cycle").on("click",function(){
    	$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
    });
    $(".order-setting-end").on("click",function(){
		$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
		$(".order-choice-list .order-choice-date").hide().eq($(this).index()-1).show();
	});
    
    $(".order-number-top").on("click",function(){
		$("#order-number-choice").val(Number($("#order-number-choice").val()) + 1);
	});
	$(".order-number-bottom").on("click",function(){
		if( Number($("#order-number-choice").val()) == 0)return;
		$("#order-number-choice").val(Number($("#order-number-choice").val()) - 1);
	});
	
	//定投、累计金额控制
	/*$('#order-money-choice').change(function(){
		var aipEndMoney = parseInt($('#order-money-choice').val());
		var aipAmount = parseInt($('#txtAipAmount').val());
	});
	$('#txtAipAmount').change(function(){
		var aipEndMoney = parseInt($('#order-money-choice').val());
		var aipAmount = parseInt($('#txtAipAmount').val());
	});*/

	$(".proposal-more-ico").on("click",function(){
		$(this).parents(".stepThree-rows").toggleClass("account-rows-hide");
	});
    //平均权重
    function weightSetting(){
    	//取得当前列表基金数量
		var fundCount = $('.slider_input').length;
		//总投资额
		var totalInvestmentAmt = $('#txtTotalInvestmentAmt').val();
		var html = '';
		var amount = '';
		var toCurrency = $('#portfolio-currency').val();
		//平均赋值权重、每只基金金额
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
		//把余数累加到最后一只基金上
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
    
    //货币选择
    $('.portfolio-currency').click(function(){
    	$('#currency-choice').toggle();
    	$('#currency-choice li').unbind('click');
    	$('#currency-choice li').click(function(){
    		$('#currency-choice').hide();
    		//待转换的货币
    		var fromCurrency = $('#portfolio-currency').attr('data-code');
    		//转换后的货币
    		var toCurrency = $(this).data('code');
    		//货币转换
    		conversion(fromCurrency,toCurrency);
    		//货币符号切换
    		$('.to-currency').text($(this).text());
    		$('#portfolio-currency').val($(this).text());
    		$('#portfolio-currency').attr('data-code',toCurrency);
    	});
    });
    $('.proposal_xiala').on('mouseleave',function(){
    	$('#currency-choice').hide();
    });
    //金额格式化
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
    
    //货币转换
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
								////console.log(typeof(n));
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
    
	//获取基础货币
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
	
	(function(){
		//refashData();
		currencyType();
	})();
	
	function rangesInit(ifInit){
    	var rangeInstanse = $('.portfolio-table').find('.funds-single-slider'),
    		backCount = $(".back-bar").length,
    		rangeCount = rangeInstanse.length - backCount;
    		weightNumber = 0;
    		$('.strategy_chart_tinput').each(function(i,n){
    			weightNumber += Number($(this).val());
    		});
    		var totalAmt = $('#txtTotalInvestmentAmt').val();
	    	if(totalAmt && typeof totalAmt != 'undefined'){
	    		totalAmt = totalAmt.replace(/,/gm,'');
	    	}
    		weightNumber = 100 - weightNumber;
    		totalAmt = totalAmt * (weightNumber / 100);
    	$('.strategy_chart_tinput').each(function(i,n){
    		if(!ifInit){
    			if($(this).closest('tr').find(".funds-single-slider").val() != ","){return true;}
	    		if(i == $('.strategy_chart_tinput').length-1){
	    			$(this).val(parseFloat(weightNumber/rangeCount+100%rangeCount/100).toFixed(0));
	        		$(this).closest('tr').find('.fund_amount').val(formatCurrency(parseFloat(totalAmt/rangeCount+totalAmt%rangeCount/100).toFixed(2)));
	    		}else{
	    			$(this).val(parseFloat(weightNumber/rangeCount).toFixed(0));
	        		$(this).closest('tr').find('.fund_amount').val(formatCurrency(parseFloat(totalAmt/rangeCount).toFixed(2)));
	    		}
    		}else{
    			totalAmt = $('#txtTotalInvestmentAmt').val();
    			var amount_val = $(this).parents("table").find(".fund_amount").val().replace(/,/gm,''),
    				amount_percentage = amount_val / totalAmt * 100;
    				amount_percentage = amount_percentage.toFixed(0);
    				$(this).val(amount_percentage);
    				var percentage_val = $(this).parents("table").find(".funds-single-slider");
    				tableTotalWeight();
					tableTotalAmount();
    				percentage_val.jRange('setValue', amount_percentage +',' + amount_percentage);
    		}
    	});
    	rangeInstanse.each(function(index){

     		//选择条 功能初始化
     		var self = $(this),
     			_start = 0,
     			_values = [];
     		if(!ifInit){
     			var _val = $(this).closest('tr').find('.strategy_chart_tinput').val();
     			self.val(_val+','+_val);
     		}
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
						_values = self.val().split(',');
						var tempValue = parseFloat(_values[0]).toFixed(0);
						var weight = 0;
						self.closest('td').find('.strategy_chart_tinput').val(tempValue);
						var amount = (tempValue*totalAmt/100).toFixed(2);
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
 	});	

 	$(document).on("click",".portfolio-table-exhibition",function(){
 		$(this).parents("table").toggleClass("portfolio-table-active");
 	});
 	function tableTotalWeight(){
 		var totalWeight = 0;
 		$('.portfolio-table-data').each(function(i,n){
 			var weight = $(n).find('.strategy_chart_tinput').val();
 			// weight = weight.substring(0,weight.length-1);
 			totalWeight = Number(totalWeight)+Number(weight);
 		});
 		totalWeight = parseFloat(totalWeight).toFixed(0);
 		$('#tableTotalWeight').text(totalWeight+'%');
 	}
 	function tableTotalAmount(){
 		var totalAmount = 0.00;
 		$('.portfolio-table-data').each(function(i,n){
 			var amount = $(n).find('.fund_amount').val();
 			amount = amount.replace(/,/gm,'');
 			totalAmount = Number(totalAmount)+Number(amount);
 		});
 		$('#tableTotalAmount').text(formatCurrency(totalAmount));
 	}	
 	function getUrlParam(name){  
 	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
 	    var r = window.location.search.substr(1).match(reg);  
 	    if (r!=null) return unescape(r[2]);  
 	    return null;  
 	};

 	$('.aip-exec-cycle-on').click(function(){
 		if($(this).hasClass('order-cycle-active')){
 			$('.order-setting-button').show();
 		}
 	});
 	$('.aip-exec-cycle-off').click(function(){
 		if($(this).hasClass('order-cycle-active')){
 			$('.order-setting-button').hide();
 		}
 	});
 	$(".order-setting-button").on("click",function(){
    	$(".proposal-mask-wrap").addClass("proposal-mask-show");
    	$(".proposal-mask-contents").addClass("proposal-mask-show");
    });

    $("#proposal-mask-close").on("click",function(){
    	$(".proposal-mask-wrap").removeClass("proposal-mask-show");
    	$(".proposal-mask-contents").removeClass("proposal-mask-show");
    });
    $(".proposal-mask-button-save").on("click",function(){
    	$(".proposal-mask-wrap").removeClass("proposal-mask-show");
    	$(".proposal-mask-contents").removeClass("proposal-mask-show");
    	
    	var aipExecCycle = '';
 		$('#aipExecCycle p:gt(0)').each(function(){
 			if($(this).hasClass('order-cycle-active')){
 				aipExecCycle = $(this).data('day');
 			}
 		});
 		var chargeDay = $('#selChargeDay').val();
 		var nextChargeDate = $('#txtNextChargeDate').text();
 		var aipAmount = $('#txtAipAmount').val();
 		aipAmount = Number(aipAmount.replace(/,/gm,''));
 		var aipEndDate = $('#txtAipEndDate').val();
 		var aipEndCount = $('#order-number-choice').val();
 		var aipEndTotalAmount = $('#order-money-choice').val();
 		aipEndTotalAmount = Number(aipEndTotalAmount.replace(/,/gm,''));
 		var data = {
			proposalId:getUrlParam('id'),
			aipState:'1',
			aipExecCycle:aipExecCycle,
			aipTimeDistance:chargeDay,
			aipAmount:aipAmount,
			aipEndDateStr:aipEndDate,
			aipEndCount:Number(aipEndCount),
			aipEndTotalAmount:aipEndTotalAmount
			};
 		var url = base_root+'/front/crm/proposal/saveProposaAip.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:data,
			success:function(result){
				if(result.flag){
					layer.msg(globalProp['global.success.save']);
					aggregate();
				}
			}
 		});
    });
    $(".proposal-mask-button-cancel").on("click",function(){
    	$(".proposal-mask-wrap").removeClass("proposal-mask-show");
    	$(".proposal-mask-contents").removeClass("proposal-mask-show");
    });
    $(document).on("keyup",".strategy_chart_tinput",function(){
    	//百分比输入检查
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
    	//输入控制
    	this.value = this.value.replace(/[^\.\d]/g,'');
    	this.value = Number(this.value).toFixed(2);

    	var _temp = 0;
    	$(".fund_amount").each(function(){
    		var _selfTemp = $(this).val().replace(/,/gm,'');
    		_temp += Number(_selfTemp);
    	});
    	$("#txtTotalInvestmentAmt").val(_temp.toFixed(2));
    	rangesInit(true);
    });
    $(document).on("blur",".strategy_chart_tinput",function(){
    	var self = $(this).siblings(".funds-slider-wrap").find('.funds-single-slider'),
    		selfVal = $(this).val();


    	self.jRange('setValue', $(this).val() +',' + $(this).val());
    	rangesInit(true);
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


    setTimeout(function(){
			if(!$('#txtNextChargeDate').text() || $('#txtNextChargeDate').text()=='NaN-NaN-NaN'){
				laydate({
				  istime: false,
				  elem: '#txtAipEndDate',
				  format: 'YYYY-MM-DD',
				  min: laydate.now()
				});
			}else{
				laydate({
				  istime: false,
				  elem: '#txtAipEndDate',
				  format: 'YYYY-MM-DD',
				  //min: $('#txtNextChargeDate').text()
				  min: laydate.now()
				});
			}
		},200);
});