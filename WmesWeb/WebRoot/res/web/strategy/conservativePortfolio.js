
define(function(require) {

	var $ = require('jquery');
			require("echarts");
			require("layui");
			require('laydate');

			var interfaceObj = require("interfaceCheckPwd");

			//验证登录
			if(_checkList!=undefined && _checkList!=""){
				$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
				}
			
		// function assetChart(){
		// 	$(".conservative-asset-chart").each(function(){
		// 		var option = {
				    
		// 		    legend: {
		// 		    	orient : 'vertical',
		// 		        x: 'right',
		// 		        itemWidth:'15',
		// 		        itemHeight:'15',
		// 		        y:'40px',
		// 		        data:['Total Cash','Total Market Value']
		// 		    },
		// 		    series: [
		// 		        {
		// 		            name:'account',
		// 		            type:'pie',
		// 		            center:	['25%', '50%'],
		// 		            radius : [0,"80%"],
		// 		            color :["#c3ef56","#8fc3ff"],
		// 		            label: {
		// 		                normal: {
		// 		                    position: 'inner',
		// 		                    formatter : "{d}%"
		// 		                }
		// 		            },
				       
		// 		            data:[
		// 		                {value:totalCashValue, name:'Total Cash'},
		// 		                {value:totalMarketValue, name:'Total Market Value'}
		// 		            ]
		// 		        }
		// 		    ]
		// 		};

		// 		var myChart = echarts.init($(this)[0]);
	 //  			myChart.setOption(option,true);
		// 	});
		// }
		// assetChart();
		$(".j-more-ico").on("click",function(){
			$(this).parents(".account-rows").toggleClass("account-rows-hide");
		});
		$(".o-more-ico").on("click",function(){
			$(this).parents(".conservative-position-rows").toggleClass("account-rows-hide");
		});
		
		var mainChart =echarts.init(document.getElementById('conservative-main-chart'));
		function assetChartmain(data,date){
			var minVal=0;
			var maxVal=0;
			$.each(data,function(i,n){
				if(minVal>parseFloat(n.value)){
					minVal=parseFloat(n.value);
				}
				if(maxVal<parseFloat(n.value)){
					maxVal=parseFloat(n.value);
				}
			})
			if(minVal<0){
				minVal=Math.floor(minVal*1.2);
			}else{
				minVal=0;
			}
		    maxVal=Math.ceil(maxVal*1.2);
			var option = {
				    tooltip: {
				        trigger: 'axis',
				        position: function (pt) {
				            return [pt[0], '10%'];
				        },
				        formatter: function (params) {
				            params = params[0];
				            //var date = new Date(params.name);
				            return params.name+ ' : '+params.value+' %';
				        }
				    },
				    title: {
				        left: 'center',
				        text: '',//图表标题WMES
				    },
				    legend: {
				        top: 'bottom',
				        data:['意向']
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

				
			mainChart.setOption(option,true);
		}
		
		$(window).resize(function(){
			mainChart.resize();

	    });
		//assetChartmain();
		$('.conservative-time-list li').on("click",function(){
			$(this).siblings().removeClass("list-active").end().addClass("list-active");
			
			if($(this).attr("data-type") == "Other"){
				$("#account-date-to").val("");
				$("#account-date-from").val("");
				$(".conservative-date-wrap").addClass("conservative-date-wrap-block");
			}else{
				bindReport();
				$(".conservative-date-wrap").removeClass("conservative-date-wrap-block");
			}
		});	
		
		$(".account-data-search").on("click",function(){
			var beginDate=$("#account-date-to").val();
			var endDate=$("#account-date-from").val();
			if(beginDate==undefined || beginDate==""){
				layer.msg(langMutilForJs['msg.select.startDate']);
				return ;
			}
			if(endDate==undefined || endDate==""){
				layer.msg(langMutilForJs['msg.select.endDate']);
				return ;
			}
			$('.conservative-time-list').find(".otherPeriod").attr("period",beginDate+" to "+endDate);
			////console.log(beginDate+" to "+endDate);
			bindReport();
			
		})
		
		$(".funds-type-choose li").on("click",function(){
			$(this).siblings().removeClass("funds-active").end().addClass("funds-active");
			bindReport();
			/*if($(this).index() == 0){
				assetChartmain();
			}else if($(this).index() == 1){
				zhuChartmain();
			}*/
			
		});
		
		//百分比圆
		var rotationMultiplier = 3.6;
		$( "div[id$='circle']" ).each(function() {

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

		// 下拉
		$(".proposal_xiala").on("click",function(){
			$(this).toggleClass("xiala-show");
		});
		$(".proposal_xiala").on("mouseleave",function(){
			$(this).removeClass("xiala-show");
		});
		$(".proposal_xiala li").on("click",function(e){
			$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").val($(this).html());
			$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").attr("code",$(this).attr("code"));
			e.stopPropagation(); 
			 var currency=$("#currencyName").attr("code");
			 window.location.href=base_root+"/front/strategy/info/conservativePortfolio.do?id="+id+"&currency="+currency;
		});

		// tab
		$(".builder-tab li").on("click",function(){
			$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
			 $(".products-list-wrap > div").hide().eq($(this).index()).show();
		});
		
		var lineData=[];
		  var date=[];
		  var data=[];
		bindReport();
		function bindReport(){
		  var periodEl=$(".conservative-time-list li[class*='list-active']");
		  var currency=$("#currencyName").attr("code");
		  var period=periodEl.attr("period");
		  $.ajax({
			  type:"post",
			  datatype:"json",
			  url:base_root+"/front/investor/zone/getCumperfsData.do",
			  data:{period:period,currency:currency,portfolioId:id},
			  success:function(json){
				  var jsonData=JSON.parse(json);
				  if(jsonData.length>0){
					  $(".chartTips").css("display","none");
					  $(".conservative-main-chart").css("display","block");
					  lineData=[];
					   date=[];
					   data=[];
					  $.each(jsonData,function(i,n){
						  lineData.push({
				   				value : (n.cumulativeRate*100).toFixed(2),
				   				name : n.valuationDate
				   			});
						  data.push({
				   				value : n.dayPl,
				   				name : n.valuationDate
				   			});
						 
						  date.push(n.valuationDate);
					  });
					  
					  if($(".funds-active").index() == 1){
						     zhuChartmain(data,date)
							}else if($(".funds-active").index() == 0){
								assetChartmain(lineData,date);
							} 
				  }else{
					  $(".chartTips").css("display","block");
					  $(".conservative-main-chart").css("display","none");
					 
				  }
				   
				  
				  
			  }
		  })
		}
		
		
		function zhuChartmain(data,date){
			$(".conservative-main-chart").each(function(){
				var option = {
				    color: ['#3398DB'],
				    tooltip : {
				        trigger: 'axis',
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
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
				           // name:date,
				            type:'bar',
				            barWidth: '60%',
				            data:data
				        }
				    ]
				};
				mainChart.setOption(option,true);
  			});
		}
		
		SetColor();
		function SetColor(){
			
			$(".displayValue").each(function(){
				var value=parseFloat($(this).attr("displayValue"));
				if(display_color=="0"){
					if(value<0){
						$(this).addClass("portfolio-green");
					}else{
						$(this).addClass("portfolio-red");
					}
				}else{
					if(value<0){
						$(this).addClass("portfolio-red");
					}else{
						$(this).addClass("portfolio-green");
					}
				}
			})
		}

		$('#account-date-from').click(function(){
	        var max = '';
	        if(!$('#account-date-to').val()){
	            max = laydate.now();
	        }else{
	            max = $('#account-date-to').val();
	        }
	        laydate({
	               istime: false,
	               max:max,
	               elem: '#account-date-from',
	               format: 'YYYY-MM-DD',
	               istoday: true,
	               choose: function(datas){ 
	               } 
	        });
	    });
	    $('#account-date-to').click(function(){
	        var min = '';
	        if(!$('#account-date-from').val()){
	            min = laydate.now();
	        }else{
	            min = $('#account-date-from').val();
	        }
	        laydate({
	            istime: false,
	            min:min,
	            elem: '#account-date-to',
	            format: 'YYYY-MM-DD'
	        });
	    });

	    function positionChart(){
			$("#positionInformation").each(function(){
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

				var myChart = echarts.init($(this)[0]);
	  			myChart.setOption(option,true);
	  			myChart.on('click', function (params) {
	  				var name=params.name;
	  			  var str=name.split(" ")[0];
				  	$("#conservative-rows-choose li[name='"+str+"']").click();
				});
			});

			$(".position-half-chart").each(function(){
				var selfData = eval($(this).attr("data-value"));

				var selfName = [];
				for (item in selfData){
					selfName.push(selfData[item]['name']);
				}

				var option = {
				    color:["#fc4f51","#c84244","#89c5e9","#fbc50f","#72b981","#fd4000"],
				    legend: {
				    	orient : 'vertical',
				        x: 'right',
				        itemWidth:'15',
				        itemHeight:'15',
				        y:'40px',
				        data:selfName,
				    },
		    		tooltip: {
						trigger: 'item',
						formatter: "{b} : {c} ({d}%)"
					},
				    series: [
				        {
				            type:'pie',
				            center:	['35%', '50%'],
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				       
				            data:selfData
				        }
				    ]
				};

				var myChart = echarts.init($(this)[0]);
					myChart.setOption(option,true);
			});

		}
		positionChart();

		$("#conservative-rows-choose li").on("click",function(){
			$(this).siblings().removeClass("now").end().addClass("now");
			$(".conservative-position-rows").hide().eq($(this).index()).show();
		});
});