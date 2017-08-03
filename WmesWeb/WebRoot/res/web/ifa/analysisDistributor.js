define(function(require) {
	var $ = require('jquery');
		require("echarts");
		require('jqueryRange');
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
				data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug']
			}],
			yAxis: {
				type: 'value'
			},
			series: [{
				name:'Sales Status',
				type: 'line',
				stack: '搜索引擎',
				data: [35.065, 35.780, 40.203, 44.669, 34.361, 28.634, 28.634, 28.073]
			}]
		};
		myChart.setOption(option);
	}
	salesleft();
	
		$('.application-information-tab li').on('click',function(){
			$(this).addClass('tab-active').siblings().removeClass('tab-active');
		});
		
	$(function() {
		$('.single-slider').jRange({
			from: 0, //滑动范围的最小值，数字，如0 
			to: 100, //滑动范围的最大值，数字，如100 
			step: 1, //步长值，每次滑动大小 
			scale: ['0%', '100%'], //滑动条下方的尺度标签，数组类型，如[0,50,100] 
			format: '%s%', //数值格式 
			width: 300, //滑动条宽度 
			showLabels: false, // 是否显示滑动条下方的尺寸标签 
			showScale: false //是否显示滑块上方的数值标签 
		});
		
		$(".single-slider").on('change', function() {
			var aa = $(".single-slider").val();
			$('.range_result').html(aa);
			var bb = $('.zongshu').text();
			$('.range_jieguo').html(((bb/100)*aa).toFixed(3));
		});
		
	});
});