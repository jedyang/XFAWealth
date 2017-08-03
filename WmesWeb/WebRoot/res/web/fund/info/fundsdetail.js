/**
 * fundsscreener.js 
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-15
 */

define(function(require) {
	var $ = require('jquery');
		require("echarts");
		require("layui");
		var industrialDistributionText = langMutilForJs['fund.info.comparison.IndustrialDistribution'];
		var regionalDistributionText = langMutilForJs['fund.info.comparison.RegionalDistribution'];
		var FundsDetails = {
			// fund price 数据图
			parceMainChart : function(period){
				var type = '';
				var url = base_root+'/front/fund/info/findFundMarketList.do?dateStr=' + new Date().getTime();
//				if('2Yr' == period ||'3Yr' == period){
//					type = 'W';
//				}else if('5Yr' == period){
//					type = 'M';
//				}
				$.post(url,{'fundIds':_fundId_,'chart':'price','period':period,'type':type}, function (result) {
					console.log(result)
						var fundMarketDataVOs = result.fundMarketDataVOs,
							index = 0,
							xAxisData = [],
							separateness = {
				    			series:[]
				    		}; //console.log(fundMarketDataVOs);
				    		var mivnav = 0;
				    		
				    		var separatenessSeries = {},
							incomePercentage = [];
						separatenessSeries['name'] = ''; 
						separatenessSeries['type'] = 'line';
						
						$.each(fundMarketDataVOs,function(i,n){
							
							
							
							incomePercentage[i] = n.nav;
							xAxisData[i] = n.marketDate;
//							if(n.fundIncomePercentageVOs.length !=0 ){
//								$.each(n.fundIncomePercentageVOs,function(j,m){
//									//折线图二
//									incomePercentage[j] = m.nav;
//									xAxisData[j] = m.marketDate;
//									
									if(n.nav!=''){
										if(mivnav==0)mivnav = parseInt(n.nav);
										else{
											if(parseInt(n.nav)<mivnav)mivnav = parseInt(n.nav);
										}
									}
//								});
//							}
							
							//index++;
						});
						separatenessSeries['data'] = incomePercentage;
						separateness.series[index] = separatenessSeries;
						//mivnav = mivnav.toFixed(0);
							var	option = {
							    color :["#5ab1ef","rgb(255, 70, 131)","yellow"],
							    tooltip: {trigger: 'axis'},
							    grid :{y : 30},
							    xAxis: {
							        type: 'category',
							        data: xAxisData
							    },
							    yAxis: {
							        type: 'value',
							        position : 'left',
							        splitNumber : 4,
							        min : mivnav,
							        //max : 1800,
							        axisLabel : {
						                formatter: '{value}'//.00
						            }
							    },
							    series:separateness.series
							};

							$("#funds_details_price_chart").width($(".funds_details_tab").width());
							var myChart = echarts.init(document.getElementById('funds_details_price_chart'));
							myChart.setOption(option);
					});	
			},
			// fund price industrial饼图
			industrialChart : function(){
//			var	industrialoptionData = [];
			var	industrialoption = {
				title : {
			        text: industrialDistributionText,
			        x:'center'
			    },
				tooltip : {
					trigger: 'item',
					formatter: "{b}"
				},
				color:['#f15c80','#8085e9','#f7a35c','#90ed7d','#434348','#7cb5ec'],
				series : [{
		        	name: 'weight',
		            type: 'pie',
		            radius : '50%',
					center: ['30%', '44%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },label: {
		                normal: {
		                    position: 'inner',
		                    formatter : "{d}%",
			                show:false    
		                }
		            },
		        }]
			 };
			$("#funds_facts_industrial_chart").width($(".funds_details_tab").width());
			var industrialChart = echarts.init(document.getElementById('funds_facts_industrial_chart'));
			industrialChart.setOption(industrialoption);
				$.post(_root_+"/front/fund/info/getIndustrialChartData.do",{'id':_fundId_,'chart':'sector'}, function (values) {
					values = $.parseJSON( values ); //json字符串转对象
					
					var namearray=[];
					$.each(values,function(i,n){
						var value = Number(n.value);
						if(value == 0){
							return true;
						} 
						var temp = n.name;
						namearray.push(temp);
					});
					if(values == null ||typeof(values) == 'undefiend'){
						$("#funds_facts_industrial_chart").hide();
					}else{
						industrialChart.setOption({
							legend: {
						        orient : 'vertical',
						        x : '50%',
						        y : 'center',
						        data:namearray
						    },
							series:{
								radius : '50%',
								center: ['30%', '44%'],
								data : values
							}
						});
					}
				});	
			},
			//fund price regional 饼图
			regionalChart : function(){
//			var	regionaloptionData = [];
			var	regionaloption = {				
				    title : {
				        text: regionalDistributionText,
				        x:'center'
				    },
				    tooltip : {
						trigger: 'item',
						formatter: "{b}"
					},
					color:['#90ed7d','#7cb5ec','#434348','#f15c80','#f7a35c','#8085e9'],
				    series : [
				        {
				        	name: 'weight',
				            type: 'pie',
				            radius : '50%',
							center: ['30%', '44%'],
				            itemStyle: {
				                emphasis: {
				                    shadowBlur: 10,
				                    shadowOffsetX: 0,
				                    shadowColor: 'rgba(0, 0, 0, 0.5)'
				                }
				            },label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%",
					                show:false    
				                }
				            },
				        }
				    ]
				};
				$("#funds_facts_regional_chart").width($(".funds_details_tab").width());
				var regionalChartinit = echarts.init(document.getElementById('funds_facts_regional_chart'));
				regionalChartinit.setOption(regionaloption);
				$.post(_root_+"/front/fund/info/getIndustrialChartData.do",{'id':_fundId_,'chart':'market'}, function (values) {
					values = $.parseJSON( values ); //json字符串转对象
					var namearray=[];
					$.each(values,function(i,n){
						var value = Number(n.value);
						if(value == 0){
							return true;
						}
						var temp = n.name;
						namearray.push(temp);
					});
					if(values == null ||typeof(values) == 'undefiend'){
						$("#funds_facts_regional_chart").hide();
					}else{
						regionalChartinit.setOption({
						    legend: {
						        orient : 'vertical',
						        x : '50%',
						        y : 'center',
						        data:namearray
						    },
							series:{
								radius : '50%',
								center: ['30%', '44%'],
								data : values
							}
						});
					}
				});	
			},
			// 公用事件
			maininit : function(){
				$(".funds_search_tab li").on("click",function(){
					$(this).siblings().removeClass("now").end().addClass("now");
					$(".funds_search_Part").hide().eq($(this).index()).show();
				});
				$(".funds_details_tab li").on("click",function(){
					$(this).siblings().removeClass("now").end().addClass("now");
					$(".funds_details_part").children().hide().eq($(this).index()).show();
				});

				$(".funds_details_price div").on("click",function(){
					
					$(this).siblings().removeClass("funds_price_active").end().addClass('funds_price_active');
					FundsDetails.parceMainChart($(this).attr("data-value"));
				});
				$(".funds_details_icon_wrap span").on("click",function(){
						var loginVal = $("#loginCode").val();
						var	_this = $(this);
						var	status  = _this.attr("followFlag");
						var	fundId = _this.attr("fundId");
						var	productId = _this.attr("productId");
						if(loginVal == 'true'){
					    	$.ajax({
					    		url:base_root+"/front/fund/info/setFoundCollection.do?r="+Math.random(),
					    		data:{'opType':status,'fundId':fundId,'productId':productId},
					    		dataType:"json",
					    		type:"get",
					    		success:function(data){
					    			if(data.result){
					    				if(status == 'Delete'){
					    					_this.removeClass("funds_heart_active");
					    					_this.attr("followFlag",'Follow');
					    					layer.msg(langMutilForJs['favour.remove']);
					    				}else{
											_this.addClass("funds_heart_active");
					    					_this.attr("followFlag",'Delete');
					    					layer.msg(langMutilForJs['favour.add']);
					    				}
					    			}
					    		},
					    		error:function(){
					    			layer.msg("error");
					    		}
					    	})
						}else{
							window.parent.location.href=base_root+"/front/viewLogin.do?r="+Math.random();
							return;
						}

				});
			},
			init : function(){
				this.parceMainChart( $(".funds_details_price .funds_price_active").attr("data-value"));
				this.industrialChart();
				this.regionalChart();
				this.maininit();
			}
		
		};

		FundsDetails.init();
		
	    function getBrowsingFundList(){
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/fund/info/bestFundListJson.do',
				data : {
					'dataType':'browsing'
				},
				async : true,
				success : function(json) {
									
					var divContent = '';
					var displayColor = $('#hdDisplayColor').val();
					var list = json.rows;
//					$(".funds_table_ul").siblings().remove();
					$.each(list, function (index, array) { // 遍历json数据列
						if(index>3) return false;
						
						var fundId = array['fundId'] == null ? "" : array['fundId'];
						var fundName = '';
						var currency = '';
						if(lang == 'sc'){
							fundName = array['fundInfoSc'] == null ? "" : array['fundInfoSc']['fundName'];
							currency = array['fundInfoSc'] == null ? "" : array['fundInfoSc']['fundCurrencyCode'];
						}else if(lang == 'tc'){
							fundName = array['fundInfoTc'] == null ? "" : array['fundInfoTc']['fundName'];
							currency = array['fundInfoTc'] == null ? "" : array['fundInfoTc']['fundCurrencyCode'];
						}else{
							fundName = array['fundInfoEn'] == null ? "" : array['fundInfoEn']['fundName'];
							currency = array['fundInfoEn'] == null ? "" : array['fundInfoEn']['fundCurrencyCode'];
						}
						
						var lastNav = array['fundInfo'] == null ? "" : array['fundInfo']['lastNav'];
						var fundReturnOneMonth = array['fundReturnOneMonth'] == null ? "" : array['fundReturnOneMonth'];
						var fundReturnThreeMonth = array['fundReturnThreeMonth'] == null ? "" : array['fundReturnThreeMonth'];
						if(lastNav != ''){
							lastNav = parseFloat(lastNav).toFixed(2);
						}
												
						fundReturnOneMonth = genColorNumber(fundReturnOneMonth,displayColor,'p');
						fundReturnThreeMonth = genColorNumber(fundReturnThreeMonth,displayColor,'p');
						
						divContent += '<div class="funds_table_div">'
	                				+ '	<p><a href="'+base_root+'/front/fund/info/fundsdetail.do?id='+fundId+'" >'+fundName+'</a></p>'
	                				+ '	<ul>'
	                				+ '		<li class="funds_tul_name"><span class="funds_tableul_name">'+lastNav+' '+currency+'</span></li>'
	                				+ '		<li class="funds_tul_num">'+fundReturnOneMonth+'</li>'
	                				+ '		<li class="funds_tul_num">'+fundReturnThreeMonth+'</li>'
	                				+ '	</ul>'
	                				+ '</div>';
						
						
					});									
					$(".browsing_fund").append(divContent);					
				}
			})
	    }
	    
	    function getBestFundList(){
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+'/front/fund/info/bestFundListJson.do',
				data : {
					'dataType':'best'
				},
				async : true,
				success : function(json) {
									
					var divContent = '';
					var displayColor = $('#hdDisplayColor').val();
					var list = json.rows;
//					var fundName;
//					$(".funds_table_ul").siblings().remove();
					$.each(list, function (index, array) { // 遍历json数据列
//						console.log(array);
						if(index>7) return false;
						
						var fundId = array['fundId'] == null ? "" : array['fundId'];
						var fundName = '';
						if(lang == 'sc'){
							fundName = array['fundInfoSc']['fundName'] == null ? "" : array['fundInfoSc']['fundName'];
						}else if(lang == 'tc'){
							fundName = array['fundInfoTc']['fundName'] == null ? "" : array['fundInfoTc']['fundName'];
						}else{
							fundName = array['fundInfoEn']['fundName'] == null ? "" : array['fundInfoEn']['fundName'];
						}
						
						var lastNav = array['fundInfo']['lastNav'] == null ? "" : array['fundInfo']['lastNav'];
						var fundReturnOneMonth = array['fundReturnOneMonth'] == null ? "" : array['fundReturnOneMonth'];
						var fundReturnThreeMonth = array['fundReturnThreeMonth'] == null ? "" : array['fundReturnThreeMonth'];
						if(lastNav != ''){
							lastNav = parseFloat(lastNav).toFixed(2);
						}
												
						fundReturnOneMonth = genColorNumber(fundReturnOneMonth,displayColor,'p');
						fundReturnThreeMonth = genColorNumber(fundReturnThreeMonth,displayColor,'p');
						
						divContent += '<ul class="funds_ctul">'
	                				+ '	<li class="funds_tul_name"><a href="'+base_root+'/front/fund/info/fundsdetail.do?id='+fundId+'" >'+fundName+'</a></li>'
	                				+ '	<li class="funds_tul_num">'+fundReturnOneMonth+'</li>'
	                				+ '	<li class="funds_tul_num">'+fundReturnThreeMonth+'</li>'
	                				+ '</ul>';
						
						
					});									
					$(".best_fund").append(divContent);					
				}
			})
	    }
	    
	    
	    function genColorNumber(n,c,f){
	    	var fontClass = 'price_zero';
	    	if(n==null || n==''){
	    		return 'N/A';
	    	}    		
	    	
	    	n = parseFloat(n);
    		if(n>0){
    			if(c=='1'){
    				fontClass = 'price_positive';
    	    	}else{
    	    		fontClass = 'price_negative';
    	    	}
    			
    		}else if(n<0){
    			if(c=='1'){
    				fontClass = 'price_negative';
    	    	}else{
    				fontClass = 'price_positive';
    	    	}
    		}
	    	
	    	
	    	if(f=='p'){
	    		n = n*100;
	    		n = n.toFixed(2) + '%';
	    	}else{
	    		n = n.toFixed(2);
	    	}
	    	
	    	return '<font class="'+fontClass+'">'+n+'</font>';
	    }
		    
	    // load CustomerGroup
	    getBrowsingFundList();
	    getBestFundList();
		
	    //获取通知详细信息
		$('.funds_title_announcemts a').on('click',function(){
			$('.fund-space-mask-wrap').addClass('fund-space-mask-wrapac');
			var aid = $(this).attr('aid'); 
			if(aid!=undefined&&aid!=''){
				$.ajax({
		    		type:"post",
		    		datatype:"json",
		    		url:base_root+'/front/fund/info/findFundAnnoDetail.do',
		    		data:{id:aid },
		    		success:function(json){ 
		    			if(json!=null){
		    				var content = json.annoContent;
		    				var annodate = json.annoDate;
		    				var title = json.annoName;
		    				$('.notice-dialog-title').text(title);
		    				$('.notice-dialog-release-date-time').text(annodate);
		    				$('.dialog-notice-content').text(content);
		    			}
		    		}
		    		
		    	})
			}
		});
		
		$('.wmes-close').on('click',function(){
			$(this).closest('.fund-space-mask-wrap').removeClass('fund-space-mask-wrapac');
		});
		
		$('.character-button-close').on('click',function(){
			$(this).closest('.fund-space-mask-wrap').removeClass('fund-space-mask-wrapac');
		});
});


