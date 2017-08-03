define(function(require) {
	var $ = require('jquery');
		require("echarts");

	function salesleft() {
		var myChart = echarts.init(document.getElementById('salesleft'));
		//var temp1 = $('#Allocation_Overview_left').attr('data-value');
		//var temp1 = JSON.parse(temp1);
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
				data: ['7/8~13/8', '14/8~20/8', '21/8~27/8', '28/8~3/9']
			}],
			yAxis: {
				type: 'value',
				min:'3'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [3.781, 4.631, 5.327, 6.073]
			}]
		};
		myChart.setOption(option);
	}
	salesleft();
	
	function salesright() {
		var myChart = echarts.init(document.getElementById('salesright'));
		//var temp1 = $('#Allocation_Overview_left').attr('data-value');
		//var temp1 = JSON.parse(temp1);
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
				data: ['08/2012', '08/2013', '08/2014', '08/2015', '08/2016']
			}],
			yAxis: {
				type: 'value',
				min:'3'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [13.781, 16.631, 20.327, 25.700,28.073]
			}]
		};
		myChart.setOption(option);
	}
	salesright();
	
	function teamleft() {
		var myChart = echarts.init(document.getElementById('teamleft'));
		//var temp1 = $('#Allocation_Overview_left').attr('data-value');
		//var temp1 = JSON.parse(temp1);
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
				data: ['7/8~13/8', '14/8~20/8', '21/8~27/8', '28/8~3/9']
			}],
			yAxis: {
				type: 'value',
				min:'3'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [3.781, 4.631, 5.327, 6.073]
			}]
		};
		myChart.setOption(option);
	};
	
	function teamright() {
		var myChart = echarts.init(document.getElementById('teamright'));
		//var temp1 = $('#Allocation_Overview_left').attr('data-value');
		//var temp1 = JSON.parse(temp1);
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
				data: ['08/2012', '08/2013', '08/2014', '08/2015', '08/2016']
			}],
			yAxis: {
				type: 'value',
				min:'3'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [13.781, 16.631, 20.327, 25.700,28.073]
			}]
		};
		myChart.setOption(option);
	};
	
	function productleft() {
		var myChart = echarts.init(document.getElementById('productleft'));
		//var temp1 = $('#Allocation_Overview_left').attr('data-value');
		//var temp1 = JSON.parse(temp1);
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
				data: ['7/8~13/8', '14/8~20/8', '21/8~27/8', '28/8~3/9']
			}],
			yAxis: {
				type: 'value',
				min:'3'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [3.781, 4.631, 5.327, 6.073]
			}]
		};
		myChart.setOption(option);
	};
	
	function productright() {
		var myChart = echarts.init(document.getElementById('productright'));
		//var temp1 = $('#Allocation_Overview_left').attr('data-value');
		//var temp1 = JSON.parse(temp1);
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
				data: ['08/2012', '08/2013', '08/2014', '08/2015', '08/2016']
			}],
			yAxis: {
				type: 'value',
				min:'3'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [13.781, 16.631, 20.327, 25.700,28.073]
			}]
		};
		myChart.setOption(option);
	};
	
	function clientleft() {
		var myChart = echarts.init(document.getElementById('clientleft'));
		//var temp1 = $('#Allocation_Overview_left').attr('data-value');
		//var temp1 = JSON.parse(temp1);
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
				data: ['7/8~13/8', '14/8~20/8', '21/8~27/8', '28/8~3/9']
			}],
			yAxis: {
				type: 'value',
				min:'3'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [3.781, 4.631, 5.327, 6.073]
			}]
		};
		myChart.setOption(option);
	};
	
	function clientright() {
		var myChart = echarts.init(document.getElementById('clientright'));
		//var temp1 = $('#Allocation_Overview_left').attr('data-value');
		//var temp1 = JSON.parse(temp1);
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
				data: ['08/2012', '08/2013', '08/2014', '08/2015', '08/2016']
			}],
			yAxis: {
				type: 'value',
				min:'3'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [13.781, 16.631, 20.327, 25.700,28.073]
			}]
		};
		myChart.setOption(option);
	};
	
	function assetChart(){
		var myChart = echarts.init(document.getElementById('sales_structure_analysispic'));
				var option = {
				    series: [
				        {
				            type:'pie',
				            // center:	['30%', '50%'],
				            // radius : [0,"80%"],
				            color :["#ff7726","#8fc3ff","#8fc3ff","#8fc3ff","#8fc3ff"],
				            radius : '60%',
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				       
				            data:[
				                {value:80, name:'Cash'},
				                {value:30, name:'Market Value'},
				                {value:30, name:'Market Value1'},
				                {value:30, name:'Market Value2'},
				                {value:30, name:'Market Value3'}
				            ]
				        }
				    ]
				};

	  			myChart.setOption(option,true);
			
	}
	assetChart();
	
	function assetChart1(){
		var myChart = echarts.init(document.getElementById('team_structure_analysispic'));
				var option = {
				    series: [
				        {
				            type:'pie',
				            // center:	['30%', '50%'],
				            // radius : [0,"80%"],
				            color :["#ff7726","#8fc3ff","#8fc3ff","#8fc3ff","#8fc3ff"],
				            radius : '60%',
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				       
				            data:[
				                {value:80, name:'Cash'},
				                {value:30, name:'Market Value'},
				                {value:30, name:'Market Value1'},
				                {value:30, name:'Market Value2'},
				                {value:30, name:'Market Value3'}
				            ]
				        }
				    ]
				};

	  			myChart.setOption(option,true);
			
	}
	assetChart();
	
	function assetChart2(){
		var myChart = echarts.init(document.getElementById('product_structure_analysispic'));
				var option = {
				    series: [
				        {
				            type:'pie',
				            // center:	['30%', '50%'],
				            // radius : [0,"80%"],
				            color :["#ff7726","#8fc3ff","#8fc3ff","#8fc3ff","#8fc3ff"],
				            radius : '60%',
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				       
				            data:[
				                {value:80, name:'Cash'},
				                {value:30, name:'Market Value'},
				                {value:30, name:'Market Value1'},
				                {value:30, name:'Market Value2'},
				                {value:30, name:'Market Value3'}
				            ]
				        }
				    ]
				};

	  			myChart.setOption(option,true);
			
	};
	
	function assetChart3(){
		var myChart = echarts.init(document.getElementById('client_structure_analysispic'));
				var option = {
				    series: [
				        {
				            type:'pie',
				            // center:	['30%', '50%'],
				            // radius : [0,"80%"],
				            color :["#ff7726","#8fc3ff","#8fc3ff","#8fc3ff","#8fc3ff"],
				            radius : '60%',
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				       
				            data:[
				                {value:80, name:'Cash'},
				                {value:30, name:'Market Value'},
				                {value:30, name:'Market Value1'},
				                {value:30, name:'Market Value2'},
				                {value:30, name:'Market Value3'}
				            ]
				        }
				    ]
				};

	  			myChart.setOption(option,true);
			
	};
		$('.application-information-tab li').on('click',function(){
			$(this).addClass('tab-active').siblings().removeClass('tab-active');
			$('.analysis_tabcontent').eq($(this).index()).addClass('analysis_tabcontentac').siblings().removeClass('analysis_tabcontentac');
			if($(this).index()==1){
				teamleft();
				teamright();
				assetChart1();
			}else if($(this).index()==2){
				productleft()
				productright();
				assetChart2();
			}else if($(this).index()==3){
				clientleft();
				clientright();
				assetChart3();
			}else if($(this).index()==0){
				salesleft();
				salesright();
				assetChart();
			}
		});
		
		$('.analysis_left_ul li').on('click',function(){
			$(this).addClass('analysis_left_titleac').siblings().removeClass('analysis_left_titleac');
		});
});