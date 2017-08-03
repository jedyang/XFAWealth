
define(function(require) {

	var $ = require('jquery');
			require("echarts");
			require("layui");
				
		$(".proposal-more-ico").on("click",function(){
			$(this).parents(".account-rows").toggleClass("account-rows-hide");
		});
		
		function assetChartmain(data,date){
			var mainChart =echarts.init(document.getElementById('funds-main-chart'));
			var option = {
				    tooltip: {
				        trigger: 'axis',
				        position: function (pt) {
				            return [pt[0], '10%'];
				        }
				    },
				    title: {
				        left: 'center',
				        text: '',// 图表标题WMES
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
				mainChart.setOption(option,true);
				}
			
			
		
		// assetChartmain();
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
				layer.msg("请选择开始日期");
				return ;
			}
			if(endDate==undefined || endDate==""){
				layer.msg("请选择结束日期");
				return ;
			}
			$('.conservative-time-list').find(".otherPeriod").attr("period",beginDate+" to "+endDate);
			////console.log(beginDate+" to "+endDate);
			bindReport();
			
		});
		
		
		
		// 百分比圆
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
			e.stopPropagation(); 
			 var currency=$("#currencyName").val();
			 window.location.href=base_root+"/front/strategy/info/fundDetailIncome.do?id="+id+"&currency="+currency;

		});

		// tab
		$(".builder-tab li").on("click",function(){
			$(this).siblings().removeClass("tab-active").end().addClass("tab-active");
		});
		
		var mainData=[];
		var dayData=[];
		var date=[];
		$(".funds-type-choose li").on("click",function(){
			$(this).siblings().removeClass("funds-active").end().addClass("funds-active");
			// assetChartmain();
			/* if($(".funds-active").index() == 1){
				  assetChartmain(dayData,date);
				}else{
					assetChartmain(mainData,date);
				}*/
			bindReport();
		});
		bindReport();

		function bindReport(){
			 var periodEl=$(".conservative-time-list li[class='list-active']");
			  var period=periodEl.attr("period");
			  var currency=$("#currencyName").val();
			$.ajax({
				type:"post",
				datatype:"json",
				url:base_root+"/front/strategy/info/getFundIncomReport.do?",
				data:{id:id,period:period,currency:currency},
				success:function(json){
					var jsonData=JSON.parse(json);
					mainData=[];
					dayData=[];
					   date=[];
					   if(jsonData.length>0){
						   $.each(jsonData,function(i,n){
								  mainData.push({
						   				value : n.totalPl,
						   				name : n.valuationDate
						   			});
								  dayData.push({
						   				value : n.dayPl,
						   				name : n.valuationDate
						   			});
								 
								  date.push(n.valuationDate);
							  });
							  if($(".funds-active").index() == 1){
								  assetChartmain(dayData,date);
								}else{
									assetChartmain(mainData,date);
								}
							  $(".funds-chart-wrap").show();
							   $(".wmes-nodata-tips").hide();
					   }else{
						   $(".funds-chart-wrap").hide();
						   $(".wmes-nodata-tips").show();
					   }
					  
				}
			});
		
     }
		
		SetColor();
		function SetColor(){
			
			$(".displayValue").each(function(){
				var value=parseFloat($(this).attr("displayValue"));
				if(display_color=="0"){
					if(value<0){
						$(this).addClass("funds_search_positive");
					}else{
						$(this).addClass("funds_search_negative");
					}
				}else{
					if(value<0){
						$(this).addClass("funds_search_negative");
					}else{
						$(this).addClass("funds_search_positive");
					}
				}
			})
		}
});