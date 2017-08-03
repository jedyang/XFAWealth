define(function(require) {
	var $ = require('jquery');
	require("echarts");

	$('.workbench_tab').on('click', function() {
		$('.workbench_tab').removeClass('workbench_tabac');
		$(this).addClass('workbench_tabac');
		$('.qiucon').removeClass('workbench_tabtop');
		$(this).parents('.qiucon').addClass('workbench_tabtop');
		var qiuindex = $(this).parents('.qiucon').index();
		/*if($(this).parents('.qiucon').css('top') !== '120px') {

		} else {
			$('.marginCon').addClass('workbench_tabzz');
			setTimeout(function() {
				$('.ifa_workbench_content').removeClass('ifa_workbench_contentac');
				$('.ifa_workbench_content').eq(qiuindex).addClass('ifa_workbench_contentac');
				$('.marginCon').removeClass('workbench_tabzz');
			}, 900);
		}*/
		$('.ifa_workbench_content').removeClass('ifa_workbench_contentac');
		$('.ifa_workbench_content').eq(qiuindex).addClass('ifa_workbench_contentac');
	});

	function echarts1() {
		var myChart = echarts.init(document.getElementById('echarts1'));
		var option = {
			tooltip: {
				trigger: 'axis',
				axisPointer: { // 坐标轴指示器，坐标轴触发有效
					type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			calculable: true,
			xAxis: [{
				type: 'category',
				position: 'top',
				axisLabel: {
					textStyle: {
						color: '#FFF'
					}
				},
				axisLine: {

				},
				data: ['2011', '2012', '2013', '2014', '2015']
			}],
			yAxis: {
				show: false
			},
			series: [{
				name: 'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [58, 90, 62, 70, 95]
			}]
		};
		myChart.setOption(option);
	}
	echarts1();

	function echarts11() {
		var myChart = echarts.init(document.getElementById('echarts11'));
		var option = {
			tooltip: {
				trigger: 'axis',
				axisPointer: { // 坐标轴指示器，坐标轴触发有效
					type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			calculable: true,
			xAxis: [{
				type: 'category',
				axisLabel: {
					textStyle: {
						color: '#FFF'
					}
				},
				data: ['31/7-6/8', '7/8-13/8', '14/8-20/8', '21/8-27/8', '28/8-3/9']
			}],
			yAxis: {
				show: false
			},
			series: [{
				name: 'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [9, 5, 7, 10, 21]
			}]
		};
		myChart.setOption(option);
	}
	echarts11();

	function echarts2() {
		var myChart = echarts.init(document.getElementById('echarts2'));
		var option = {
			tooltip: {
				
			},
			xAxis: [{
				type: 'category',
				data: ['Mining', 'Oil', 'Health', 'Bonds', 'Gold']
			}],
			yAxis: [{
				show: false
			}],
			series: [{
				name: ' ',
				top:'top',
				left:'left',
				type: 'bar',
				itemStyle: {
					normal: { // 系列级个性化，横向渐变填充
						color:'#00b0f0',
						borderRadius: 5,
						label: {
							show: true,
							position:'top',
							textStyle: {
								fontSize: '14',
								fontFamily: '微软雅黑',
								fontWeight: 'bold'
							}
						}
					}
				},
				data: [3590000, 3400000, 2980000, 2880000, 2750000]
			}]
		};
		myChart.setOption(option);
	}
	echarts2();
	
	function echarts3(){
		var myChart = echarts.init(document.getElementById('echarts3'));
				var option = {
				    series: [
				        {
				            type:'pie',
				            // center:	['30%', '50%'],
				            // radius : [0,"80%"],
				            radius : '90%',
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				       
				            data:[
				                {value:50, name:'Baring'},
				                {value:43, name:'BlackRock'},
				                {value:31, name:'BNP'},
				                {value:22, name:'AXA'},
				                {value:19, name:'Amundi'}
				            ]
				        }
				    ]
				};

	  			myChart.setOption(option,true);
			
		}
		echarts3();
		
		function ifaFirmHome7() {
		var myChart = echarts.init(document.getElementById('ifaFirmHome7'));
		var option = {
			tooltip: {
				
			},
			xAxis: [{
				type: 'category',
				data: ['JAFCO', 'Bee', 'YLG', 'GF', 'SMCL']
			}],
			yAxis: [{
				show: false
			}],
			series: [{
				name: ' ',
				top:'top',
				left:'left',
				type: 'bar',
				itemStyle: {
					normal: { // 系列级个性化，横向渐变填充
						color:'#00b0f0',
						borderRadius: 5,
						label: {
							show: true,
							position:'top',
							textStyle: {
								fontSize: '14',
								fontFamily: '微软雅黑',
								fontWeight: 'bold'
							}
						}
					}
				},
				data: [3590000, 3400000, 2980000, 2880000, 2750000]
			}]
		};
		myChart.setOption(option);
	}
	ifaFirmHome7();
	
});