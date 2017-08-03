define(function(require) {
	//依赖
	var $ = require('jquery');
	//验证登录
	if(_checkList!=undefined && _checkList!=""){
	$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
	}
	
	
	 var myChart = echarts.init(document.getElementById('asset-cart'));
		function mianChart(data,date){
			var option = {
			    tooltip: {
			        trigger: 'axis',
			        position: function (pt) {
			            return [pt[0], '10%'];
			        }
			    },
			    title: {
			        left: 'center',
			        text: '',//图表标题WMES
			    },
			    legend: {
			        top: 'bottom',
			        data:['']
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
			        boundaryGap: [0, '100%']
			    },
			    series: [
			        {
			            name:'',
			            type:'line',
			            smooth:true,
			            symbol: 'none',
			            itemStyle: {
			                normal: {
			                    color: '#f2b9ae',
			                    lineStyle:{width : 3}
			                }
			            },
			            data: data
			        }
			    ]
			};

		    myChart.setOption(option,true);

		}
		//mianChart();

		function twoChart(data,date){
			$(".asset-cart").each(function(){
				var option = {
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
				            data : date,
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
				            name:'直接访问',
				            type:'bar',
				            barWidth: '60%',
				            data:data
				        }
				    ]
				};
				myChart.setOption(option,true);
			});
		}
		
	
	var lineData=[];
	  var columnData=[];
	  var date=[];
	bindReport();
	function bindReport(){
	  var periodEl=$(".my-period-list li[class='period-active']");
	  var period=periodEl.attr("period");
	  $.ajax({
		  type:"post",
		  datatype:"json",
		  url:base_root+"/front/investor/zone/getCumperfsData.do",
		  data:{period:period},
		  success:function(json){
			  var jsonData=JSON.parse(json);
			  if(jsonData.length>0){
				  $(".asset-cart").css("display","block");
				  $(".assetsTips").css("display","none");
				  lineData=[];
				   columnData=[];
				   date=[];
				  $.each(jsonData,function(i,n){
					  lineData.push({
			   				value : n.cumulativePl,
			   				name : n.valuationDate
			   			});
					  columnData.push({
						  value : n.dayPl,
			   				name : n.valuationDate
					  });
					  date.push(n.valuationDate);
				  });
				  if($(".my-period-active").index() == 1){
						twoChart(columnData,date);
					}else{
						mianChart(lineData,date);
					}
			  }else{
				  $(".assetsTips").css("display","block");
				  $(".asset-cart").css("display","none");
			  }
			   
			  //mianChart(lineData,date);
			  
		  }
	  })
	}
	
}