
define(function(require) {
	//依赖
	var $ = require('jquery');
		require("echarts");
		require("swiper");
		
        var interfaceObj = require("interfaceCheckPwd");
		

		if(_checkList!=undefined && _checkList!=""){
			$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
		}

		 checkData();
		function checkData(){
		  var ifaCount=	$(".zone-personal-wrapper p").length;
		  var buddyCount=$(".zone-ifa-list .zone-ifa-ul li").length;
		  var spaceCount=$(".zone-ifa-list .zone-focus-ifa-ul li").length;
		  if(ifaCount==0){
			  $(".zone-personal-wrapper .ifaTips").css("display","block");
		  }
		  if(buddyCount==0){
			  $(".zone-ifa-list .buddyTips").css("display","block");
		  }
		  if(spaceCount==0){
			  $(".zone-ifa-list .spaceTips").css("display","block");
		  }
		 /* //console.log("ifaCount:"+ifaCount);
		  //console.log("buddyCount:"+buddyCount);*/
		}
		
		
        var myChart = null;
        var chartEl=document.getElementById('asset-cart');
        if(null!=chartEl)
        	myChart=echarts.init(chartEl);
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
		function assetChart(){
			$(".my-assets-chart").each(function(){
				var option = {		
				    series: [
				        {
				            name:'account',
				            type:'pie',
				            color :["#ff7726","#8fc3ff"],
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				       
				            data:[
				                {value:totalCashValue, name:'Cash'},
				                {value:totalMarketValue, name:'Market Value'}
				            ]
				        }
				    ]
				};

				var myChart2 = echarts.init($(this)[0]);
	  			myChart2.setOption(option,true);
			});
		}
		assetChart();
		
	$(".zone-taccount-rows").on("mouseenter",function(){
        $(this).addClass("zone-taccount-active");
    });
    $(".zone-taccount-rows").on("mouseleave",function(){
        $(this).removeClass("zone-taccount-active");
    });
	//点击圆形切换
    $(".ifa-more-ico").on("click",function(){
		$(this).parents(".ifa-zone-row").toggleClass("ifa-zone-row-hide");
	});

	$(".my-period-list li").on("click",function(){
		$(this).siblings().removeClass("period-active").end().addClass("period-active");
		bindReport();
	});

	$(".my-period-choose p").on("click",function(){
		$(this).siblings().removeClass("my-period-active").end().addClass("my-period-active");
		bindReport();
	});
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
	$(".eve_ico").on("click",function(){
        $(".eve_ico").toggleClass("eve_active");
        if($(this).hasClass("eve_active")){
            $(".eve-hide").html("***");
            $(".my-assets-cur").hide();
        }else{
            $(".eve-hide").each(function(){
                $(this).html($(this).attr("data-num"));
            });
            $(".my-assets-cur").show();
        }
    });
	
	new Swiper('.swiper-container',{
		nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        onSlideChangeStart:function(swiper){
            $("#zone-ifa").html( $(".zone-personal-swiper .swiper-slide-active").attr("data-name") );
        }
	});
	$("#zone-ifa").html( $(".zone-personal-swiper .swiper-slide-active").attr("data-name") );
	
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
	
	$(".portfolio-title").on('click',function(){
		var id=$(this).attr("portfolioId");
		window.location.href=base_root+"/front/portfolio/arena/detail.do?id="+id;
		return false;
	});
	
	$(".strategy-title").on('click',function(){
		var id=$(this).attr("strategyId");
		window.location.href=base_root+"/front/strategy/info/strategiesdetail.do?id="+id;
		return false;
	});
	
});