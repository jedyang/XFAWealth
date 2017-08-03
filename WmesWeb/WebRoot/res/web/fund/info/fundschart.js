/**
 * fundcomparison.js 基金比较页面
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-21
 */

define(function(require) {
	var $ = require('jquery');
		require("echarts");
	var date = "2013/4/1,2013/4/2,2013/4/3,2013/4/8,2013/4/9,2013/4/10,2013/4/11,2013/4/12,2013/4/15,2013/4/16,2013/4/17,2013/4/18,2013/4/19,2013/4/22,2013/4/23,2013/4/24,2013/4/25,2013/4/26,2013/5/2,2013/5/3,2013/5/6,2013/5/7,2013/5/8,2013/5/9,2013/5/10,2013/5/13,2013/5/14,2013/5/15,2013/5/16,2013/5/17,2013/5/20,2013/5/21,2013/5/22,2013/5/23,2013/5/24,2013/5/27,2013/5/28,2013/5/29,2013/5/30,2013/5/31,2013/6/3,2013/6/4,2013/6/5,2013/6/6,2013/6/7,2013/6/18,2013/6/19,2013/6/20,2013/6/21,2013/6/24,2013/6/25,2013/6/26,2013/6/27,2013/6/28,2013/7/1,2013/7/2,2013/7/3,2013/7/4,2013/7/8,2013/7/9,2013/7/10,2013/7/11,2013/7/12,2013/7/15,2013/7/16,2013/7/17,2013/7/18,2013/7/19,2013/7/22,2013/7/23,2013/7/24,2013/7/25,2013/7/26,2013/7/29,2013/7/30,2013/7/31,2013/8/1,2013/8/2,2013/8/5,2013/8/6,2013/8/7,2013/8/8,2013/8/9,2013/8/12,2013/8/13,2013/8/14,2013/8/15,2013/8/16,2013/8/19,2013/8/20,2013/8/21,2013/8/22,2013/8/23,2013/8/26,2013/8/27,2013/8/28,2013/8/29,2013/8/30,2013/9/2,2013/9/3,2013/9/4,2013/9/5,2013/9/6,2013/9/9,2013/9/10,2013/9/11,2013/9/12,2013/9/13,2013/9/16,2013/9/17,2013/9/18,2013/9/23,2013/9/24,2013/9/25,2013/9/26,2013/9/27,2013/9/30,2013/10/8,2013/10/9,2013/10/10,2013/10/11,2013/10/14,2013/10/15,2013/10/16,2013/10/17,2013/10/18,2013/10/21,2013/10/22,2013/10/23,2013/10/24,2013/10/25,2013/10/28,2013/10/29,2013/10/30,2013/10/31,2013/11/1,2013/11/4,2013/11/5,2013/11/6,2013/11/7,2013/11/8,2013/11/11,2013/11/12,2013/11/13,2013/11/14,2013/11/15,2013/11/18,2013/11/19,2013/11/20,2013/11/21,2013/11/22,2013/11/25,2013/11/26,2013/11/27,2013/11/28,2013/11/29,2013/12/2,2013/12/3,2013/12/4,2013/12/5,2013/12/6,2013/12/9,2013/12/10,2013/12/11,2013/12/12,2013/12/13,2013/12/16,2013/12/17,2013/12/18,2013/12/19,2013/12/20,2013/12/23,2013/12/24,2013/12/25,2013/12/26,2013/12/27,2013/12/30,2013/12/31,2014/1/2,2014/1/3,2014/1/6";
	var data = '31.56,30.34,30.18,30.86,30.3,29.98,29.78,30.63,29.96,29.93,31.36,30.94,31.4,30.89,29.67,30.06,30.45,29.24,28.47,29.34,29.84,29.25,29.65,28.97,28.96,29.02,29.01,29.31,29.92,29.99,30.82,29.06,28.44,28.23,27.53,27.8,28.75,28.62,28.6,27.94,28.69,28.54,28.59,28.58,27.77,30.24,31.36,31.24,31.52,30.53,32.28,32.14,31.55,31.28,33.8,34.37,35.53,35.28,34.92,34.39,34.27,34.45,35.47,36.89,36.52,35.03,35.03,34.58,35.37,36.76,37.36,36.45,36.53,35.22,35.41,34.56,35.36,35.99,37.09,36.71,36.98,36.68,37.03,36.91,35.74,35.1,34.89,34.48,35.57,35.14,34.55,34.96,34.58,34.96,35.32,34.9,34.88,34.95,37.12,37.54,37.63,39.91,39.64,39.97,40.74,39.82,39.1,40.48,39.79,39.45,40,41.27,43.75,43.75,44.85,44.11,44.68,44.9,46.31,46.76,46.36,45.43,46.74,46.7,46.65,48.7,51.29,50.99,48.9,47.57,45.44,44.67,44.03,45.7,41.13,39.5,39.59,39.87,39.37,37.65,37.51,38.98,39.88,38.91,40.91,41.58,42.29,40.17,40.2,39.46,39.31,38.68,39.03,39.36,40.51,40.72,38.92,39.89,40.72,40.1,39.37,39.33,39.2,38.68,38.82,38.75,38.91,39.06,39.68,39.08,41.45,40.05,38.79,39.15,39.7,39.8,39.48,39.08,36.93,37.01,36.92';
	var fundschart = {
			//chart数据
			chartdata : [],
			//数据日期
			chartdate : [],
			MainChart : echarts.init(document.getElementById('funds_main_chart')),
			oneChart : echarts.init(document.getElementById('indicators_one')),
			twoChart : echarts.init(document.getElementById('indicators_two')),
			getChartdata : function(){
				// $.ajax({
				// 	type:"GET",
				// 	url:"/frontend/web/data.php",
				// 	success:function(response){
				// 		response = JSON.parse(response);
				// 		for(var j = 1; j < response.length; j++){
				// 			fundschart.chartdata.push(response[j][4]);
				// 			fundschart.chartdate.push(response[j][0]);						
				// 		}
				// 	},

				// });
				fundschart.chartdata = data.split(",");
				fundschart.chartdate = date.split(",");
				fundschart.MainChartinit();
				fundschart.indicatorsOneinit();
				fundschart.indicatorsTwoinit();
				// 图表联动
			    echarts.connect([fundschart.MainChart,fundschart.oneChart, fundschart.twoChart]);
			},
			// Indicator数据计算
			indicatorsAlgorithm : function(type,num){
				switch (type){
	 				case 'MA':
	 				return maAlgorithm(num);
	 				break;
	 				case "EMA":
	 				return emaAlgorithm(num);
	 				break;
	 				case "Bollinger":
	 				return bollingerAlgorithm(num);
	 				break;
	 				default:
	 				return ''

				}
				//MA
				function maAlgorithm(num){
					var Indicator_now = [];
					for(var j = 0; j < fundschart.chartdata.length; j++){
						if(j >= (num - 1) ){						
							var Indicator_new = 0;
							for(var k = j - (num - 1); k <= j; k++){
								Indicator_new += Number(fundschart.chartdata[k]);
							}
							var newtoFixed = Indicator_new/num;
							Indicator_now.push(newtoFixed.toFixed(2));
						}else{
							Indicator_now.push("-");
						}
						
					}
					return Indicator_now;
				};
				//EMA
				function emaAlgorithm(num){
					var Indicator_now = [],
						// 先计算EMA值
						Indicator_ema = 2 / (Number(num) + 1);
						 // Number( fundschart.chartdata[k] ) - Number( Indicator_now(Indicator_now.length-1) ) )
					for(var j = 0; j < fundschart.chartdata.length; j++){
						var Indicator_new = 0;
						if( j == 0){
							//第一天EMA没有对比值，就用他自身。
							Indicator_now.push(fundschart.chartdata[j]);
						}else{
							// 昨天的ema值
							Indicator_new = Indicator_now[Indicator_now.length-1];
							
							Indicator_new = ( Number(fundschart.chartdata[j]) - Number(Indicator_new)) * Number(Indicator_ema) + Number(Indicator_new);
							// //console.log(Indicator_new)
							Indicator_now.push(Indicator_new.toFixed(2));
							
						}
											
					}
					return Indicator_now;
				};
				//bollinger布林通道
				function bollingerAlgorithm(num){
					// 计算标准差

					// var Indicator_now = []
					// for(var j = 0; j < fundschart.chartdata.length; j++){
					// 	if(j > num){
					// 		var Indicator_new = 0,
					// 			Bollinger_arr = [];
					// 		for(var k = j - num; k < j; k++){
					// 			Indicator_new += Number(fundschart.chartdata[k]);
					// 			Bollinger_arr.push(fundschart.chartdata[k]);
					// 		}
					// 		var Bollinger_lenght = Bollinger_arr.length,
					// 			Bollinger_temp = new Array(Bollinger_lenght);

					// 		for (var i = 0; i < Bollinger_lenght ; i++){
					// 			var dev = parseFloat(Bollinger_arr[i]) - parseFloat(tempAvgl);
					// 		}
					// 		Indicator_now.push(Indicator_new/num);
					// 	}else{
					// 		Indicator_now.push("-");
					// 	}
						
					// }
					// return Indicator_now;
				};
			},
			// 技术数据计算
			technicalAlgorithm : function(type,num){
				switch (type){
	 				case 'MACD':
	 				return macdAlgorithm(num);
	 				break;
	 				case "RSI":
	 				return rsiAlgorithm(num);
	 				break;
	 				case "ROC":
	 				return rocAlgorithm(num);
	 				break;
	 				case "MTM":
	 				return mtmAlgorithm(num);
	 				break;
	 				default:
	 				return '';
				}

				function macdAlgorithm(num){
					// 计算公式 macd计算 = diff - dea
					var macd_one = fundschart.indicatorsAlgorithm('EMA',num[0]),
						macd_two = fundschart.indicatorsAlgorithm('EMA',num[1]);
					var macd_arr = [];
					var macd_standard = [];
					for(var i = 0 ;i < macd_one.length; i++){
						var macd_temp = macd_one[i]-macd_two[i];

						macd_standard.push(macd_temp.toFixed(2));
					}
					macd_arr.push(macd_standard)
					var macd_dea = [];
					for(var j = 0 ;j < macd_standard.length; j++){
						if( j==0){
							macd_dea.push(Number(macd_standard[j]));
						}else{
							var macd_dea_temp = 2 / ( Number(num[2]) + 1) * (Number(macd_standard[j])-Number(macd_dea[j-1])) + Number(macd_dea[j-1]);
							macd_dea.push( macd_dea_temp.toFixed(2));
						}						
					}
					macd_arr.push(macd_dea)
					var macd = [];
					for (var k = 0;k< macd_dea.length; k++){
						var macd_arr_temp = Number(macd_standard[k]) - Number(macd_dea[k])
						macd.push(macd_arr_temp.toFixed(2));
					}
					macd_arr.push(macd)
				  return macd_arr;
				}
				function rsiAlgorithm(num){
					// 计算公式 RSI（n）=N日内收盘涨幅的平均值/(N日内收盘涨幅均值+N日内收盘跌幅均值) ×100%
					var Algorithm_now = [];
					for(var j = 1; j < fundschart.chartdata.length; j++){

						if(j >= num ){
							// n日内涨幅值
							var Algorithm_positive = 0,
							// n日内跌幅值
							 	Algorithm_negative = 0,
							 	Algorithm_positive_sum = 0,
							 	Algorithm_negative_sum = 0,
							 	Algorithm_this = 0;
								
							for(var k = 0; k < num; k++){

								var Algorithm_number = Number(fundschart.chartdata[j-k]) - Number(fundschart.chartdata[j-k-1]);
								
								if( Algorithm_number >= 0){
									// N日内收盘涨幅总和
									Algorithm_positive += Number(Algorithm_number);
									Algorithm_positive_sum += 1;
								}else{
									// N日内收盘跌幅总和		
									Algorithm_negative += Number(Algorithm_number);	
									Algorithm_negative_sum +=1;
								}							

							}	
							// N日内收盘涨幅的平均值
							Algorithm_positive = Number(Algorithm_positive)  / Number(Algorithm_positive_sum);
							// //N日内收盘跌幅均值
							Algorithm_negative = Number(Algorithm_negative) / Number(Algorithm_negative_sum);

							// 计算 RSI（n）
							Algorithm_this = Number(Algorithm_positive) / (Number(Algorithm_positive) + Number(Algorithm_negative)) * 1;
							// if (Algorithm_this == "-") {//console.log("yes");};
							// //console.log(Algorithm_this )
							Algorithm_now.push(Number(Algorithm_this).toFixed(2))

						}else{
							Algorithm_now.push("-");
						}
						
					}
					// //console.log(Algorithm_now)
					return Algorithm_now;
				}
				function mtmAlgorithm(num){
					var Algorithm_now = [];
					for(var j = 0; j < fundschart.chartdata.length; j++){
						if(j >= num ){						
							var newtoFixed = Number(fundschart.chartdata[j]) - Number(fundschart.chartdata[j-num]);
							Algorithm_now.push(newtoFixed.toFixed(2));
						}else{
							Algorithm_now.push("-");
						}
						
					}
					return Algorithm_now;
				}
				function rocAlgorithm(num){
					// 计算方式 roc(n)
					// 	AX = 今天的收盘价—n 天前的收盘价
					// 	BX = n 天前的收盘价
					// 	roc = AX / BX	
					var Algorithm_now = [];
					for(var j = 0; j < fundschart.chartdata.length; j++){
						if(j >= num ){						
							var newtoFixed = Number(fundschart.chartdata[j]) - Number(fundschart.chartdata[j-num]);
								newtoFixed = newtoFixed / Number(fundschart.chartdata[j-num])
							Algorithm_now.push(newtoFixed.toFixed(2));
						}else{
							Algorithm_now.push("-");
						}
						
					}
					return Algorithm_now;
				}

			},
			//主图
			MainChartinit : function(){
				//Indicator初始值及参数
				var Maindate = [],
					MainLegend = ["Close"],
					MainName = $("#Indicator_select  option:selected").val(),
					Indicator_length = $(".Indicator_input input").length,
					Indicator_input = '';

				//初始收盘值
				Maindate.push({name:"Close",data:fundschart.chartdata,type:'line'});
				// 循环Indicator Input的值
				for (var i = 0;i < Indicator_length ; i++){
					Indicator_input = $(".Indicator_input input").eq(i).val();
					if(Indicator_input == "" || Indicator_input == "-"){
						continue;
					}	
					MainLegend.push(MainName + Indicator_input);
					Maindate.push({
						name:MainName + Indicator_input,
						data:fundschart.indicatorsAlgorithm(MainName,Indicator_input),
						type:'line'
					});
					
				}
				var option = {
				    legend: {
				        data:MainLegend,
				        x:'10%',
				        y:'5%',

				    },
				    color : ['rgb(255, 70, 131)','#b2d6ed','yellow','green','blue','black'],
				    tooltip: {trigger: 'axis'},
				    xAxis: {
				        type: 'category',
				        data: fundschart.chartdate
				    },
				    yAxis: {
				        type: 'value',
				        position : 'right',
				        splitNumber : 12,
				        min : '100%'
				        // boundaryGap: ['80%', '1800%']
				    },
				    dataZoom: [{
				        type: 'inside',
				        start: 0,
				        end: 35
				    }, {
				        start: 0,
				        end: 35
				    }],
				    series: Maindate
				};
		  		this.MainChart.setOption(option,true);
			},
			// 技术指标一
			indicatorsOneinit : function(){
				//Indicator初始值及参数
				var technicadate = [],
					technicarr = [],
					technicaName = $("#chartLayerI  option:selected").val(),
					technica_length = $(".chartLayerI input").length;
					if($("#chartLayerI  option:selected").val() == "MACD"){
						for (var i = 0;i < technica_length ; i++){
							if($(".chartLayerI input").eq(i).val() == ""){
								alert("MACD必须满足3个条件！");
								return ;
							}
							technicarr.push($(".chartLayerI input").eq(i).val())
						}
						var macd_data = fundschart.technicalAlgorithm(technicaName,technicarr),
							macd_name =['DIFF','DEA','MACD'];
						for (var j = 0; j < technica_length ; j++ ){
							technicadate.push({
								name:macd_name[j],
								data:macd_data[j],
								type:'line'
							});
						}
					}else{
						for (var k = 0;k < technica_length ; k++){
							var technica = $(".chartLayerI input").eq(k).val();
							if(technica == "" || technica == "-"){
								continue;
							}	
							technicadate.push({
								name:technicaName + technica,
								data:fundschart.technicalAlgorithm(technicaName,technica),
								type:'line'
							});
							
						}
					}
									
				var option = {
				    tooltip: {trigger: 'axis'},
				    xAxis: {
				        type: 'category',
				        data: fundschart.chartdate,
				        show: false
				    },
				    yAxis: {
				        type: 'value',
				        position : 'right',
				        // boundaryGap: [0, '30%']
				        splitNumber:4,
				        min : '100%'
				    },
				    dataZoom: [{
				        type: 'inside',
				        start: 0,
				        end: 35
				    }, {
				        start: 0,
				        end: 35,
				        show:false,
				    }],
				     grid:{
				    	y:'10%',
				    	height:'80%',
				    },
				    series: technicadate
				};
		  		this.oneChart.setOption(option,true);
			},
			// 技术指标二
			indicatorsTwoinit : function(){
				//Indicator初始值及参数
				var technicadate = [],
					technicarr = [],
					technicaName = $("#chartLayerII  option:selected").val(),
					technica_length = $(".chartLayerII input").length;
					if($("#chartLayerII  option:selected").val() == "MACD"){
						for (var i = 0;i < technica_length ; i++){
							if($(".chartLayerII input").eq(i).val() == ""){
								return ;
							}
							technicarr.push($(".chartLayerII input").eq(i).val())
						}
						var macd_data = fundschart.technicalAlgorithm(technicaName,technicarr),
							macd_name =['DIFF','DEA','MACD'];
						for (var j = 0; j < technica_length ; j++ ){
							technicadate.push({
								name:macd_name[j],
								data:macd_data[j],
								type:'line'
							});
						}
					}else{
						for (var k = 0;k < technica_length ; k++){
							var technica = $(".chartLayerII input").eq(k).val();
							if(technica == "" || technica == "-"){
								continue;
							}	
							technicadate.push({
								name:technicaName + technica,
								data:fundschart.technicalAlgorithm(technicaName,technica),
								type:'line'
							});
							
						}
					}
					


				var option = {
				    tooltip: {trigger: 'axis'},
				    xAxis: {
				        type: 'category',
				        data: fundschart.chartdate,
				        show: false
				    },
				    yAxis: {
				        type: 'value',
				        position : 'right',
				        // boundaryGap: [0, '30%']
				        splitNumber:4,
				        min : '100%'
				    },
				    dataZoom: [{
				        type: 'inside',
				        start: 0,
				        end: 35
				    }, {
				        start: 0,
				        end: 35,
				        show:false,
				    }],
				     grid:{
				    	y:'10%',
				    	height:'80%',
				    },
				    series: technicadate
				};
		  		this.twoChart.setOption(option,true);
			},
			chartEvents : function(){
				$(".funds_details_tab li").on("click",function(){
					$(this).siblings().removeClass("now").end().addClass("now");
				});
				$("#Indicator_draw").on("click",function(){
					fundschart.MainChartinit();
				});
				$("#chartLayer").on("click",function(){
					fundschart.indicatorsOneinit();
					fundschart.indicatorsTwoinit();
				});
				if($("#Indicator_select  option:selected").val() == "Bollinger"){
					$(".Indicator_input input:eq(2)").val("-").attr("disabled",true);
				}
				$("#Indicator_select").on("change",function(){
					if( $(this).val() == "Bollinger"){
						$(".Indicator_input input:eq(2)").val("-").attr("disabled",true);
					}else{
						$(".Indicator_input input:eq(2)").val("").attr("disabled",false);
					}
				});

			},
			init : function(){
				fundschart.chartEvents();
				fundschart.getChartdata();

			}
	};

	fundschart.init();

});