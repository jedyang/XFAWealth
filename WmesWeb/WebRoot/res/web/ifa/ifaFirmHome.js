define(function(require) {
	var $ = require('jquery');
	require("echarts");

	function ifaFirmHome1() {
		var myChart = echarts.init(document.getElementById('ifaFirmHome1'));
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
				name: 'New Clients',
				type: 'line',
				data: [98, 311, 102, 230, 400]
			}]
		};
		myChart.setOption(option);
	}
	ifaFirmHome1();

	function ifaFirmHome2() {
		var myChart = echarts.init(document.getElementById('ifaFirmHome2'));
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
				data: ['24/7-30/7','31/7-6/8', '7/8-13/8', '14/8-20/8', '21/8-27/8', '28/8-3/9']
			}],
			yAxis: {
				show: false
			},
			series: [{
				name: 'New Clients',
				type: 'line',
				data: [20, 19, 15, 17, 20, 31]
			}]
		};
		myChart.setOption(option);
	}
	ifaFirmHome2();
	
	function ifaFirmHome3() {
		var myChart = echarts.init(document.getElementById('ifaFirmHome3'));
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
				name: 'New Clients',
				type: 'line',
				data: [132020000, 148720000, 135220000, 148200000, 152220000]
			}]
		};
		myChart.setOption(option);
	}
	ifaFirmHome3();

	function ifaFirmHome4() {
		var myChart = echarts.init(document.getElementById('ifaFirmHome4'));
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
				data: ['24/7-30/7','31/7-6/8', '7/8-13/8', '14/8-20/8', '21/8-27/8', '28/8-3/9']
			}],
			yAxis: {
				show: false
			},
			series: [{
				name: 'New Clients',
				type: 'line',
				data: [3909000, 4701000, 3781000, 4631000, 5327000, 6073000]
			}]
		};
		myChart.setOption(option);
	}
	ifaFirmHome4();
	
	function ifaFirmHome5() {
		var myChart = echarts.init(document.getElementById('ifaFirmHome5'));
		var option = {
			tooltip: {
				
			},
			xAxis: [{
				type: 'category',
				data: ['Mining', 'Oil', 'Health', 'Bonds', 'Gold', 'Energy']
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
				data: [3590000, 3400000, 2980000, 2880000, 2750000, 2620000]
			}]
		};
		myChart.setOption(option);
	}
	ifaFirmHome5();
	
	function ifaFirmHome6() {
		var myChart = echarts.init(document.getElementById('ifaFirmHome6'));
		var option = {
			tooltip: {
				
			},
			xAxis: [{
				type: 'category',
				data: ['SC', 'NC', 'HK', 'JP', 'SG', 'KP']
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
				data: [4830000, 3980000, 3310000, 3010000, 2910000, 2810000]
			}]
		};
		myChart.setOption(option);
	}
	ifaFirmHome6();
	
	function windowHeight(){
		var windowHeight = $(window).height();
		windowHeight = windowHeight-100;
		if($('.wmes-content').height() < windowHeight){
				$('.wmes-content').css('min-height',windowHeight);
		};
	};
	
	$('.workbench_tab').on('click', function() {
            $('.workbench_tab').removeClass('workbench_tabac');
            $(this).addClass('workbench_tabac');
            $('.qiucon').removeClass('workbench_tabtop');
            $(this).parents('.qiucon').addClass('workbench_tabtop');
            var qiuindex = $(this).parents('.qiucon').index();
            $('.ifa_workbench_content').removeClass('ifa_workbench_contentac');
            $('.ifa_workbench_content').eq(qiuindex).addClass('ifa_workbench_contentac');
            $('.ifa_workbench_content').eq(qiuindex).css('borderColor',$(this).css('backgroundColor'));
            windowHeight();
        });
		
		
		$('.ifa_workbench_content').on('click',function(){
			$(this).find('.brief_report').css('display','block');
			$('.layer_distributor').css('display','block');
		});
		
		$('.layer_distributor').on('click',function(){
			$('.brief_report').css('display','none');
			$('.layer_distributor').css('display','none');
		});
});