define(function(require) {
	var $ = require('jquery');

		require("scrollbar");
		 require("echarts");
		 require('layui');
		 require('laydate');
		 var selector =  require('ifaSelectUser');
			selector.init();

	    $('#account-date-to').click(function(){
	        var max = '';
	        if(!$('#toDate').val()){
	            max = laydate.now();
	        }else{
	            max = $('#toDate').val();
	        }
	        laydate({
	               istime: false,
	               max:max,
	               elem: '#account-date-to',
	               format: 'YYYY-MM-DD',
	               istoday: true,
	        });
	    });
	    $('#account-date-from').click(function(){
	        var min = '';
	        if(!$('#fromDate').val()){
	            min = laydate.now();
	        }else{
	            min = $('#fromDate').val();
	        }
	        laydate({
	            istime: false,
	            min:min,
	            elem: '#account-date-from',
	            format: 'YYYY-MM-DD'
	        });
	    });

		$('.funds_search_wrap').perfectScrollbar();
		
		var data = [], dataname = [];
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

    $(".strategies_more").on("click",function(){
    	$(this).parents(".strategies_list_wrap").toggleClass("strategies_narrow");
    });
    

    sectorchartInit();
    function sectorchartInit(){
    	data=[];dataname=[];
    	 $.each(sectorList,function(i,n){
    	    	data.push({
    				value : n.value,
    				name : n.name
    			});
    			dataname.push(n.name);
    	    })
    	   
    	    
    	    var option = {
				title: {
					text: langMutilForJs['portfolio.detail.allocation.sector'],
					x: 90,
					y: 25
				},
			    legend: {
			        
					left: '10%',
					top: '75%',
			        data:dataname
			    },
			    series: [
			        {
			            type:'pie',
			            center:	['35%', '42%'],
			            radius : '42%',
			            color :["#0080a3","#0097c1","#00abd9","#81c1e1","#b3d5e9"],
			            label: {
			                normal: {
			                    position: 'inner',
			                    formatter : "{d}%"  
			                }
			                
			            },
			            data:data
			        }
			    ]
			};
			var Chartinit = echarts.init(document.getElementById('Allocation_Overview_left'));
			Chartinit.setOption(option);
    }
    
  geographicalchartInit();
    function geographicalchartInit(){
    	data=[];dataname=[];
   	 $.each(geoList,function(i,n){
   	    	data.push({
   				value : n.value,
   				name : n.name
   			});
   			dataname.push(n.name);
   	    });
   	var option = {
			title: {
				text: langMutilForJs['portfolio.detail.allocation.region'],
				x: 90,
				y: 25
			},
		    legend: {
		        
				left: '10%',
				top: '75%',
		        data:dataname
		    },
		    series: [
		        {
		            type:'pie',
		            center:	['35%', '42%'],
		            radius : '42%',
		            color :['#bd8704', '#dda106', '#fbb508', '#fbc981', '#fbd9b3'],
		            label: {
		                normal: {
		                    position: 'inner',
		                    formatter : "{d}%"
		                }
		                
		            },
		            data:data
		        }
		    ]
		};
			var Chartinit = echarts.init(document.getElementById('Allocation_Overview_center'));
			Chartinit.setOption(option);
   }
    
     typechartInit();
    function typechartInit(){
    	data=[];dataname=[];
   	 $.each(typeList,function(i,n){
   	    	data.push({
   				value : n.value,
   				name : n.name
   			});
   			dataname.push(n.name);
   	    });
   	var option = {
			title: {
				text: langMutilForJs['portfolio.detail.allocation.fundsType'],
				x: 90,
				y: 25
			},
		    legend: {
		        
				left: '10%',
				top: '75%',
		        data:dataname
		    },
		    series: [
		        {
		            type:'pie',
		            center:	['35%', '42%'],
		            radius : '42%',
		            color :['#6dcb1f', '#e70068', '#dc9a00', '#00a4da'],
		            label: {
		                normal: {
		                    position: 'inner',
		                    formatter : "{d}%"
		                }
		                
		            },
		            data:data
		        }
		    ]
		};
			var Chartinit = echarts.init(document.getElementById('Allocation_Overview_right'));
			Chartinit.setOption(option);
   }
    
    var myChart = echarts.init(document.getElementById('porfolios-chart'));
	function mianChart(){
	        var base = +new Date(2016,7, 3);
			var oneDay = 24 * 3600 * 1000;
			var date = [];
			var data=[];
			$.each(cumperfList,function(i,n){
				data.push({
	   				value : (n.returnRate*100).toFixed(2),
	   				name : n.marketDate
	   			});
				date.push(n.marketDate);
			});
			
	    	    
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
		            name:'Performance chart',
		            type:'line',
		            smooth:true,
		            symbol: 'none',
		            itemStyle: {
		                normal: {
		                    color: '#12a1ec',
		                    lineStyle:{width : 3}
		                }
		            },
		            data: data
		        }
		    ]
		};

	    myChart.setOption(option,true);

	}
	
	$(window).resize(function(){
		myChart.resize();

    });
//	mianChart();

	
	loadFundList();
	function loadFundList() {
		var url = base_root + "/front/fund/info/getFundListForSelect.do?id=" +_fundIds;
		url += "&view=true";
		var type=getQueryString("type");
		if(type=="read"){
			url+= "&fav=n";
		}
		$("#tableList").load(url, null, function(text, status, xmlhttp) {
			// initInnerHeight('mainFrame','mainFrame');
			
			////console.log("fundList",fundList);
			$(".funds_search_information tr").each(function(index,r){
				
				if(index==0){
					$(this).prepend("<th class='w2'>"+langMutilForJs["portfolio.hold.detail.productList.weight"]+" </th>");
				}else{
				 var weight=fundList[index-1].weight;
				 var html="<td class='w'>"+weight*100+"%</td>";
				 $(this).prepend(html);
					//var id=$(this).attr("fundId");
					
				}
				/*//console.log("index",index);
				//console.log(r);*/
			})
			
			
		});
		
		

	}
	
	
	
	$('.conservative-time-list li').on("click",function(){
		$(this).siblings().removeClass("list-active").end().addClass("list-active");
		if($(this).attr("data-type") == "Other"){
			$(".conservative-date-wrap").addClass("conservative-date-wrap-block");
		}else{
			$(".conservative-date-wrap").removeClass("conservative-date-wrap-block");
			bindMainChart();	
		}
		
	});	
	$(".recommend-date-ok").on("click",function(){
		bindMainChart();
		/*if($(".funds-active").index() == 1){
			zhuChartmain();
		}else if($(".funds-active").index() == 0){
			assetChartmain();
		}*/
	});
	
	bindMainChart();
	function bindMainChart(){
		var period="";
		var fromDate="";
		var toDate="";
	   var activeTime=$('.conservative-time-list').find(".list-active");
	   if($(activeTime).attr("data-type") == "Other"){
		   fromDate=$("#account-date-to").val();
		   toDate=$("#account-date-from").val();
	   }else{
		   period=activeTime.attr("period");
	   }
	   $.ajax({
		  type:"post",
		  datatype:"json",
		  url:base_root+"/front/portfolio/arena/getPortfolioCumperf.do",
		  data:{id:_id_,period:period,fromDate:fromDate,toDate:toDate},
		  success:function(json){
			  var date = json.marketDates;
			  var data = json.returnRates;
				if(data.length==0){
					$(".chart_noTips").show();
					$("#porfolios-chart").hide();
					return;
				}
				$(".chart_noTips").hide();
				$("#porfolios-chart").show();
				var minVal=data.min();
				var maxVal=data.max();
				if(minVal<0){
					minVal=Math.floor(minVal*1.3);
				}else{
					minVal=0;
				}
				maxVal=Math.ceil(maxVal*1.3);
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
			            name:'Performance',
			            type:'line',
			            smooth:true,
			            symbol: 'none',
			            itemStyle: {
			                normal: {
			                    color: '#12a1ec',
			                    lineStyle:{width : 3}
			                }
			            },
			           
			            data: data
			        }
			    ]
			};

		    myChart.setOption(option,true);
		  }
	   });
	  /* //console.log("period:"+period);
	   //console.log("fromDate:"+fromDate);
	   //console.log("toDate:"+toDate);*/
	}
	$(".ifa-more-ico").on("click",function(){
		$(this).toggleClass("ifa-more-icoactive");
		$(this).parents('#strategies_list').children().eq(1).toggleClass('ifa-more-ico-hidden');
		$(this).parents('#strategies_list').children().eq(2).toggleClass('ifa-more-ico-hidden');
		$(this).parents('.strategies_inf_word').children().eq(1).toggleClass('ifa-more-ico-hidden');
		$(this).parents('.strategies_inf_word').children().eq(2).toggleClass('ifa-more-ico-hidden');
		$(this).parents('.strategies_inf_word').find('.wmes-nodata-tips').toggleClass('ifa-more-ico-hidden');
		$(this).parents('.strategies_inf_word').children().eq(3).toggleClass('ifa-more-ico-hidden');
		$(this).parents('.Allocation_Overview_con').children().eq(0).toggleClass('ifa-more-ico-hidden');
		$(this).parents('.Allocation_Overview_con').children().eq(1).toggleClass('ifa-more-ico-hidden');
		$(this).parents('.Allocation_Overview_con').children().eq(2).toggleClass('ifa-more-ico-hidden');

	});
	
	$(".portfolio_fav_img_amend1").on("click",function(){
		var status="";
		var isFollow=$(this).attr("isFollow");
		var _this=$(this);
		if(isFollow=="1"){
			status="Delete";
		}else{
			status="Follow";
		}
		$.ajax({
			url:base_root+"/front/portfolio/arena/setPortfolioFollow.do",
			data:{"relateid":_id_,"opType":status},
			type:"get",
			dataType:"json",
			success:function(json){
				if(json.result){
					if(isFollow=="1")
						isFollow="0";
					else
						isFollow="1";
					
					_this.attr("isFollow",isFollow);
					_this.toggleClass("portfolio_fav_img_amend1active");
					
					if(isFollow=="0"){
						layer.msg(langMutilForJs['favour.remove']);
					}else{
						layer.msg(langMutilForJs['favour.add']);
					}
					
				}
			}
		});
	});
	
	$(document).on('click','.amend-type',function(){
		$('.view_permissions_settingcon').css('display','block');
	});
	$("input:radio[name='permit']").on("click",function(){
	    var permitCheckResult=$("input:radio[name='permit']:checked").val();
	    if (permitCheckResult && permitCheckResult=='3') {
	    	$("#permit_ext").show();
	    }else{
	    	$("#permit_ext").hide();
	    } 
	    $("#permitSetting").val(permitCheckResult);
	    $("#permit").val($(this).val());
    });
	$("#permit_ext .setting").on("click",function(){
        $(this).parents("li").toggleClass("setting_active");
        var type=$(this).attr("permitType");
        if($(this).parents("li").hasClass("setting_active")){
        	$("#permit_"+type).val("1");
        }else{
        	$("#permit_"+type).val("0");
        }
    });	
	$('#btnSave').click(function(){
		var portfolioId = getUrlParam('id');
		//1:Only Me，2:Public，3:Let me specify
    	var scopeFlag = '',
    		//1全部，0否，-1部分
    		clientFlag = '',
    		prospectFlag = '',
    		buddyFlag = '',
    		colleagueFlag = '',
    		clientsDetail = '',
    		prospectsDetail = '',
    		buddiesDetail = '',
    		colleaguesDetail = '';
    	$('.view_permissions_setting input').each(function(i,n){
    		if($(n).prop('checked')){
        		scopeFlag = $(this).val();
        	}
    	});
    	if(scopeFlag == '3'){
    		clientFlag = '0',
    		prospectFlag = '0',
    		buddyFlag = '0',
    		colleagueFlag = '0';
    		if($('#permit_client').prop('checked')){
    			clientFlag = '1';
    			if(typeof $('#permit_clients').val() != 'undefined' && $('#permit_clients').val()!=''){
    				clientFlag = '-1';
    				clientsDetail = $('#permit_clients').val();
    			}
    		}
    		if($('#permit_prospect').prop('checked')){
    			prospectFlag = '1';
    			if(typeof $('#permit_prospects').val() != 'undefined' && $('#permit_prospects').val()!=''){
    				prospectFlag = '-1';
    				prospectsDetail = $('#permit_prospects').val();
    			}
    		}
    		if($('#permit_buddy').prop('checked')){
    			buddyFlag = '1';
    			if(typeof $('#permit_buddies').val() != 'undefined' && $('#permit_buddies').val()!=''){
    				buddyFlag = '-1';
    				buddiesDetail = $('#permit_buddies').val();
    			}
    		}
    		if($('#permit_colleague').prop('checked')){
    			colleagueFlag = '1';
    			if(typeof $('#permit_colleagues').val() != 'undefined' && $('#permit_colleagues').val()!=''){
    				colleagueFlag = '-1';
    				colleaguesDetail = $('#permit_colleagues').val();
    			}
    		}
    	}
    	var data = {
			step:4,
			portfolioId:portfolioId,
			scopeFlag : scopeFlag,
			clientFlag : clientFlag,
			prospectFlag : prospectFlag,
			buddyFlag : buddyFlag,
			colleagueFlag : colleagueFlag,
			prospectsDetail : prospectsDetail,
			clientsDetail : clientsDetail,
			buddiesDetail : buddiesDetail,
			colleaguesDetail : colleaguesDetail
    	};
		var url = base_root+'/front/portfolio/arena/savePortfolioArena.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:data,
			success:function(result){
				setTimeout(function(){
					window.location.href = base_root + '/front/portfolio/arena/detail.do?id=' + portfolioId;
				},1000); 
			}
		});
	
	});
	$(".setting-close").on('click',function(){
		$(".view_permissions_settingcon").hide();
	})
	
	$("#btnCancle").on('click',function(){
		$(".view_permissions_settingcon").hide();
	})
	function getUrlParam(name){  
 	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
 	    var r = window.location.search.substr(1).match(reg);  
 	    if (r!=null) return unescape(r[2]);  
 	    return null;  
 	}
	///prospect existing client buddy colleague
	$(".j-permitASetting").on("click",function(){
    	var type = $(this).data("userType");
    	if('existing' == type){
    		selector.create(0, type, 'permit_clients', 'clients_names', selectClientCallback);
    	}else if('prospect' == type){
    		selector.create(0, type, 'permit_prospects', 'prospect_names', selectProspectCallback);
    	}else if('buddy' == type){
    		selector.create(0, type, 'permit_buddies', 'buddy_names', selectBuddyClientCallback);
    	}else if('colleague' == type){
    		selector.create(0, type, 'permit_colleagues', 'colleague_names', selectColleagueCallback);
    	}
		$(".selectnamelistbox").show();
    });
	function selectClientCallback(){
		var clients_names = $('#clients_names').val();
		$('#permit_ext ul>li').each(function(){
			if($(this).data('id') == 'permit_clients'){
				var html = getSelectNameHtml(clients_names);
				$(this).find('.pushToSettingName').remove();
				$(this).find('.pushToSettingCheckbox').after(html);
			}
		});
	}
	function selectProspectCallback(){
		var clients_names = $('#clients_names').val();
		var prospects_names = $('#prospect_names').val();
		$('#permit_ext ul>li').each(function(){
			if($(this).data('id') == 'permit_prospects'){
				var html = getSelectNameHtml(prospects_names);
				$(this).find('.pushToSettingName').remove();
				$(this).find('.pushToSettingCheckbox').after(html);
			}
		});
	}
	function selectBuddyClientCallback(){
		var buddies_names = $('#buddy_names').val();
		var colleagues_names = $('#colleague_names').val();
		$('#permit_ext ul>li').each(function(){
			if($(this).data('id') == 'permit_buddies'){
				var html = getSelectNameHtml(buddies_names);
				$(this).find('.pushToSettingName').remove();
				$(this).find('.pushToSettingCheckbox').after(html);
			}
		});
	}
	function selectColleagueCallback(){
		var colleagues_names = $('#colleague_names').val();
		$('#permit_ext ul>li').each(function(){
			if($(this).data('id') == 'permit_colleagues'){
				var html = getSelectNameHtml(colleagues_names);
				$(this).find('.pushToSettingName').remove();
				$(this).find('.pushToSettingCheckbox').after(html);
			}
		});
	}
	function getSelectNameHtml(obj){
		var html = '';
		var names = [];
		if(obj.indexOf(',') > -1){
			names = obj.split(',');
		}else if(obj != ''){
			names.push(obj);
		}
		if(names.length > 0){
			$.each(names, function(i, n){
				if(i > 2){
					html += '<p class="pushToSettingName" style="margin-right:30px">...</p>';
					return false;
				}else if(i == names.length-1){
					html += '<p class="pushToSettingName" title="' + n + '" style="margin-right:30px">' + n + '</p>';
				}else{
					html += '<p class="pushToSettingName" title="' + n + '" style="margin-right:30px">' + n + '<span> ,</span></p>'
				}
			});
		}else{
			html = '<p class="pushToSettingName select-all" style="margin-right:30px">' + props["global.all"] + '</p>';
		}
		return html;
	}
	Array.prototype.max = function(){ 
		return Math.max.apply({},this) 
	} 
	Array.prototype.min = function(){ 
		return Math.min.apply({},this) 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});