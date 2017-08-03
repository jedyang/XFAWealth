/**
 * myAssets.js 我的资产页面
 * @author 李裕恒
 * email: 673577011@qq.com
 * @date: 2016-03-23
 */
define(function(require) {
	var $ = require('jquery');
	require("swiper");
	require("echarts");

	

	$(document).on('click','.eve_ico',function(){
		if($(this).hasClass('investor_eve_ico_show')){
			$(this).removeClass('investor_eve_ico_show');
			$('.eve-hide').find('font').show();
			$('.eve-hide').find('span').remove();
			$(this).attr('src',base_root+'/res/images/application/eye_show.png');
			sessionStorage.setItem("showassets","1")
		}else{
			$(this).addClass('investor_eve_ico_show');
			$('.eve-hide').find('font').hide();
			$('.eve-hide').append('<span>***</span>');
			$(this).attr('src',base_root+'/res/images/application/eye_hide.png');
			sessionStorage.setItem("showassets","0")
		}
	});
	
	var showassets = sessionStorage.getItem("showassets");
	if(showassets=="0"){
		$(".eve_ico").click();
	}
	
	var myChart = null;
	var chartEl = document.getElementById('asset-cart');
	if(null != chartEl)
		myChart = echarts.init(chartEl);

	function mianChart(data, date) {
		var value = [];
		$.each(data,function(i,n){
			value.push(n.value);
		})
		var minVal = 0;
		var maxVal = 0;
		if(value.min() < minVal){
			minVal = value.min();
			minVal = Math.floor(Number(minVal)*1.2);
		}
		if(value.max() > maxVal){
			maxVal = value.max();
			maxVal = Math.ceil(Number(maxVal)*1.2);
		}
		var option = {
			tooltip: {
				trigger: 'axis',
				position: function(pt) {
					return [pt[0], '10%'];
				},
		        formatter: function (params) {
		            params = params[0];
		            return params.name+ ' : '+params.value+"%";
		        }
			},
			title: {
				left: 'center',
				text: '', //图表标题WMES
			},
			legend: {
				top: 'bottom',
				data: ['意向']
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
				data: date
			},
			yAxis: {
				type: 'value',
				boundaryGap: [0, '100%'],
				min:minVal,
				max:maxVal,
			    axisLabel: {
	                show: true,
	                interval: 'auto',
	                formatter: '{value} %'
        		}
			},
			series: [{
				name: '',
				type: 'line',
				smooth: true,
				symbol: 'none',
				itemStyle: {
					normal: {
						color: '#f2b9ae',
						lineStyle: {
							width: 3
						}
					}
				},
				data: data
			}]
		};

		myChart.setOption(option, true);

	}
	//mianChart();

	function twoChart(data, date) {
		$(".asset-cart").each(function() {
			var option = {
				color: ['#3398DB'],
				tooltip: {
					trigger: 'axis',
					axisPointer: { // 坐标轴指示器，坐标轴触发有效
						type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
					},
			        formatter: function (params) {
			            params = params[0];
			            return params.name+ ' : '+formatMoney(params.value,'')+" "+_currency;
			        } 
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
				grid: {
					left: '3%',
					right: '4%',
					bottom: '3%',
					containLabel: true
				},
				xAxis: [{
					type: 'category',
					data: date,
					axisTick: {
						alignWithLabel: true
					}
				}],
				yAxis: [{
					type: 'value',
					axisLabel: {
		                  show: true,
		                  interval: 'auto',
		                  formatter: '{value} '+_currency
		          		}
				}],
				series: [{
					name: '直接访问',
					type: 'bar',
					barWidth: '60%',
					data: data
				}]
			};
			myChart.setOption(option, true);
		});
	}

	function assetChart() {
		$(".my-assets-chart").each(function() {
			var option = {
				series: [{
					name: 'account',
					type: 'pie',
					color: ["#ff7726", "#8fc3ff"],
					label: {
						normal: {
							position: 'inner',
							formatter: "{d}%"
						}
					},

					data: [{
						value: totalCashValue,
						name: 'Cash'
					}, {
						value: totalMarketValue,
						name: 'Market Value'
					}]
				}]
			};

			var myChart2 = echarts.init($(this)[0]);
			myChart2.setOption(option, true);
		});
	}
	assetChart();

	$(".my-period-choose p").on("click", function() {
		$(this).siblings().removeClass("my-period-active").end().addClass("my-period-active");
		bindReport();
	});
	$(".my-period-list li").on("click",function(){
		$(this).siblings().removeClass("period-active").end().addClass("period-active");
		bindReport();
	});

	var lineData = [];
	var columnData = [];
	var date = [];
	bindReport();

	function bindReport() {
		var periodEl = $(".my-period-list li[class='period-active']");
		var period = periodEl.attr("period");
		$.ajax({
			type: "post",
			datatype: "json",
			url: base_root + "/front/investor/zone/getAssetCumperfsData.do",
			data: {
				period: period
			},
			success: function(json) {
				////console.log("json",json);
				var jsonData = JSON.parse(json);
				if(jsonData.length > 0) {
					$(".asset-cart").css("display", "block");
					$(".assetsTips").css("display", "none");
					lineData = [];
					columnData = [];
					date = [];
					$.each(jsonData, function(i, n) {
						lineData.push({
							value: (n.accReturn*100).toFixed(2),
							name: n.date
						});
						columnData.push({
							value: n.datePl,
							name: n.date
						});
						date.push(n.date);
					});
					if($(".my-period-active").index() == 1) {
						twoChart(columnData, date);
					} else {
						mianChart(lineData, date);
					}
				} else {
					$(".assetsTips").css("display", "block");
					$(".asset-cart").css("display", "none");
				}

				//mianChart(lineData,date);

			}
		})
	};

	function positionInformation() {
		var dataJson=$("#positionInformation").attr("data-value");
		if(dataJson==undefined){
			return;
		}
		////console.log("dataJson",dataJson);
		var chartJson=JSON.parse(dataJson);
		////console.log("chartJson",chartJson);
		var colorList = [];//["#8f60c2","#fab00a","#a0d54e","#78a288","#f7de5a"];
		var nameList=[];
		var data=[];
		$.each(chartJson,function(i,n){
			colorList.push(n.displayColor);
			var dataMdl={};
			dataMdl.value=n.marketValue;
			dataMdl.name=langMutilForJs['allocation.'+n.name]+" "+n.marketValueStr+" "+_currency;
			data.push(dataMdl);
			nameList.push(dataMdl.name);
		})
	/*	//console.log("colorList",colorList);
		//console.log("nameList",nameList);
		//console.log("data",data);*/
		
		var option = {
			tooltip: {
				show:false,
				trigger: 'item',
				formatter: "{a} <br/>{b} : {c} ({d}%)"
			},
			legend: {
				orient: 'vertical',
				left: '65%',
				top: '25%',
				data: nameList,
				textStyle:{
					color:'#000',
					fontSize:15
				},
				itemGap:16
			},
			series: [{
				type: 'pie',
				radius: '80%',
				center: ['40%', '50%'],
				data: data,
				itemStyle: {
					emphasis: {
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowColor: 'rgba(0, 0, 0, 0.5)'
					},
					normal: {
						label: {
							position: 'inner',
							formatter: "{d}%"
						}
					}
				},
				color:colorList
			}]
		};
		
		var myChart0 = echarts.init(document.getElementById('positionInformation'));
		myChart0.on('click', function (params) {
				//console.log(params);
				var name=params.name;
			  var str=name.split(" ")[0];
			  
		  	$(".funds_search_tab li[name='"+str+"']").click();
		});
		myChart0.setOption(option, true);
	}
	positionInformation();
	
	$(".zone-taccount-rows").on("mouseenter",function(){
        $(this).addClass("zone-taccount-active");
    });
    
    $(".zone-taccount-rows").on("mouseleave",function(){
        $(this).removeClass("zone-taccount-active");
    });
    
   /* $('.funds_search_tab li').on('click',function(){
    	$(this).addClass('now').siblings().removeClass('now');
    });*/
    
    $(".funds_search_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".conservative-position-rows").hide().eq($(this).index()).show();
	});
	
	$(document).on("click",".investor-xiala input",function(){
		$(this).siblings(".xiala-list").show();
	});
	$(document).on('click','.icon_xiala',function(){
		if($(this).parents('.investor-xiala').find('ul').css('display') == 'none'){
			$(this).siblings(".xiala-list").show();
		}else{
			$(this).siblings(".xiala-list").hide();
		};
	});
	$(document).on('mouseleave','.investor-xiala',function(){
		$(this).find('.xiala-list').hide();
	});
	$(document).on("click",".xiala-list li",function(){
		$('.investor-xiala input').attr('value',$(this).attr("data-value"));
		$(".xiala-list").hide();
		var currency=$(this).attr("data-value");
		window.location.href=base_root+'/front/investor/zone/myAssets.do?c='+currency
	});
	
	
	
	
	//百分比圆
	var rotationMultiplier = 3.6;
	$( "div[id$='bluecircle']" ).each(function() {

		var classList = $( this ).attr('class').split(/\s+/);

		for (var i = 0; i < classList.length; i++) {
		   if (classList[i].match("^p")) {
			var rotationPercentage = classList[i].substring(1, classList[i].length);
			var rotationDegrees = rotationMultiplier*rotationPercentage;
			$('.c100.p'+rotationPercentage+ ' .bar').css({
			  '-webkit-transform' : 'rotate(' + rotationDegrees + 'deg)',
			  '-moz-transform'    : 'rotate(' + rotationDegrees + 'deg)',
			  '-ms-transform'     : 'rotate(' + rotationDegrees + 'deg)',
			  '-o-transform'      : 'rotate(' + rotationDegrees + 'deg)',
			  'transform'         : 'rotate(' + rotationDegrees + 'deg)'
			});
		   }
		}
	});
	
	$(".portfolios_list li").on("click",function(){
		var id=$(this).attr("portfolioId");
		window.location.href=base_root+"/front/strategy/info/conservativePortfolio.do?id="+id;
	})
	Array.prototype.max = function(){ 
		return Math.max.apply({},this) 
	} 
	Array.prototype.min = function(){ 
		return Math.min.apply({},this) 
	}
});