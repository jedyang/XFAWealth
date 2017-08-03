/**
 * fundcomparison.js 基金比较页面
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-21
 */

define(function(require) {
	var $ = require('jquery');
		require("echarts");
		require("layui");

		
		
   /***************************** 基金对比 ECharts 修改 ******************************/
	var eChartsData = (function(){
    	//var xAxisData = [];
    	//var nameData = [];
    	var separateness = {
    			series:[]
    	};
    	return {
    		separateness:separateness
    	};
    })();
	var echarGetTableData = (function(Chartinit){
		var xAxisData = [];
		var nameData = [];
    	var separateness = {
    			series:[]
    	};
    	var fundIds = _ids_;
    	var period = '1Yr';
    	var url = base_root+'/front/transaction/findFundMarketList.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			//async:false,
			url:url,
			data:
			{
				fundIds:fundIds,
				period:period
			},
			success:function(result){
				////console.log(result);
				if(result.flag){
					var fundMarketDataVOs = result.fundMarketDataVOs;
					var index = 0;
					$.each(fundMarketDataVOs,function(i,n){
						if(n.fundIncomePercentageVOs == ''){
							//layer.msg('no data!', { icon: 0, time: 3000 });
						}else{
							//折线图二
							var separatenessSeries = {};
							var incomePercentage = [];
							separatenessSeries['name'] = n.fundName; 
							separatenessSeries['type'] = 'line';
							if(n.fundIncomePercentageVOs.length !=0 ){
								$.each(n.fundIncomePercentageVOs,function(j,m){
									//折线图二
									incomePercentage[j] = m.incomePercentage;
									xAxisData[j] = m.marketDate;
								});
							}
							separatenessSeries['data'] = incomePercentage;
							separateness.series[index] = separatenessSeries;
							nameData[index] = n.fundName; 
							index++;
						}
					});
				}
				eChartsData.nameData = nameData;
				eChartsData.xAxisData = xAxisData;
				eChartsData.separateness = separateness;
				Chartinit.hideLoading();
				Chartinit.setOption({        
					legend: {
						data: eChartsData.nameData,
				        x: '10%',
				        y: '0%'
				    },
				    xAxis: {
				        type: 'category',
				        data: eChartsData.xAxisData
				    },
				    series: eChartsData.separateness.series
		        });
			}
		});
	});
	
	function optioninit(){
		var	option = {
		    legend: {
		        data: [],//eChartsData.nameData,
		        x:'10%',
		        y:'0%'
		    },
		    tooltip: {trigger: 'axis'},
		    color :["rgb(255, 70, 131)","#b2d6ed","yellow","green","blue"],
		    xAxis: {
		        type: 'category',
		        data: []//eChartsData.xAxisData
		    },
		    dataZoom: [{
			        type: 'inside',
			        start: 0,
			        end: 50
			    }, {
			        start: 0,
			        end: 50
			    }],
		    yAxis: {
			    type : 'value',
	            axisLabel : {
	                formatter: '{value} %'
	            },
	            splitNumber:10
		    },
		    series: []//eChartsData.separateness.series
		};
		////console.log(JSON.stringify(option));
		var Chartinit = echarts.init(document.getElementById('funds_comparison_chart'));
		  	Chartinit.setOption(option);
		  	Chartinit.showLoading();
		  	echarGetTableData(Chartinit);
	}
	optioninit();
		
  /***************************** 基金对比 ECharts 修改 end ******************************/	
	$(".funds_checkbox label").on("click",function(){		
		var _thisParent = $(this).parent(),
			 funds_checkbox = $(".funds_checkbox"),
			funds_checkbox_lenght = funds_checkbox.length;
		if(_thisParent.find('#Compare_Selectall').length>0){
			
			if(_thisParent.find('input').is(':checked')) {
				for(var i = 0; i < funds_checkbox_lenght;i++){
					$(".funds_checkbox input").eq(i)[0].checked=false;
					$(".comparison_col").hide();
				}
			}else{
				for(var j = 0; j < funds_checkbox_lenght;j++){
					$(".funds_checkbox input").eq(j)[0].checked=true;
					$(".comparison_col").show();
				}
			}
		}else{
			if(_thisParent.find('input').is(':checked')) {
				_thisParent.find('input')[0].checked=false;
				$("#" + _thisParent.attr("data-dom")).hide();
				$("#comparison_all_select input")[0].checked=false;
			}else{
				_thisParent.find('input')[0].checked=true;
				$("#" + _thisParent.attr("data-dom")).show();
				if($(".funds_checkbox input:checked").length == funds_checkbox_lenght - 1){
					$("#comparison_all_select input")[0].checked=true;
				}
			}
		}
	});


    $(".funds_comparison_name").on("click",".funds_comparison_name_deldete",function(){
		var url=_root_+"/front/fund/info/fundscomparison.do?id=";
		var delId = $(this).attr("data-id")+"";
		url= url+_ids_.substring(0,_ids_.indexOf(delId))+_ids_.substr(_ids_.indexOf(delId)+delId.length);

		location.href=url;
    });

    $(".funds_keyword_div").on("click",function(){  
    	if( $(this).find("ul").hasClass("funds_keyword_active") ){
    		$(this).find("ul").removeClass("funds_keyword_active").hide();
    		$(".funds_keyword_div").removeClass('li_ctrl');
    	}else{
    		$(this).find("ul").addClass("funds_keyword_active").show();
        	$(".funds_keyword_div").addClass('li_ctrl');
    	}
    });
    var houseId = '';
    $(".funds_keyword_xiala li").on("click",function(){
    	$(".funds_keyword_div").removeClass('li_ctrl');
    	$('.funds_keyword_div').find("input").val($(this).html());
    	houseId = $(this).attr("data-id");
    });

    $('.funds_keyword_div').on('mouseleave',function(){
    	$(this).find("ul").removeClass("funds_keyword_active").hide();
    	$(this).removeClass('li_ctrl');	
    });
    
    $(".funds_keyword_xiala_search").on("click","li",function(){
    	$('.funds_keyword_input').val($(this).html());
    	if($(".funds_comparison_name li").length < 5){

//    		$(".funds_comparison_name").append('<li>'+ $(this).html() +'<span class="funds_comparison_name_deldete"></span></li>');
    		var url=_root_+"/front/fund/info/fundscomparison.do?id=";
    		if (_ids_.length>0)
    			url= url+_ids_+","+$(this).attr("data-id");
    		else
    			url= url+$(this).attr("data-id");
    		//alert(url);
    		location.href=url;
    	}

    });
     /*$(".funds_keyword_search").on("click",function(){    	
     	if( $(this).find("ul").hasClass("funds_keyword_active") ){
     		$(this).find("ul").removeClass("funds_keyword_active").hide();
     	}else{
     		$(this).find("ul").addClass("funds_keyword_active").show();
     	}
     });*/
    $(".funds_keyword_input").on("focus",function(){
    	$(".funds_keyword_search").addClass('li_ctrl_search');
    	$(this).parent().find("ul").addClass("funds_keyword_active").show();
    	refreshSelList($(this));
    });
    $(".funds_keyword_input").on("blur",function(){
    	var _this = $(this);
    	setTimeout(function(){
			_this.parent().find("ul").removeClass("funds_keyword_active").hide();
		},200);
    	
    });
    $(".funds_keyword_input").on("keyup",function(){
    	refreshSelList($(this));
    });
    function refreshSelList(obj){
    	$(".funds_keyword_xiala_search").children().remove();
    	// $('.funds_keyword_input').val(obj.html());
        $.post(_root_+"/front/fund/info/getFundList.do",{'houseId':houseId,'keyword':$("#funds_keyword_input").val()}, function (values) {
    		//alert(values);
    		values = $.parseJSON( values ); //json字符串转对象
    		var len = values.length;
    		for (var i=0;i<len;i++){
        		$(".funds_keyword_xiala_search").append('<li title="'+ values[i].name +'" data-id="'+values[i].fundId+'">'+ values[i].name +'</li>');
            }
    	});	
    }
	$(".funds_fundname_collection span").on("click",function(){
		if($(this).hasClass("noLogin")){
			// var tips = "Please login first."; 
			// if ($("#login_error").val()) tips = $("#login_error").val();
			// $.Tips({ content: tips});
			window.location.href = base_root +"/front/viewLogin.do?r="+Math.random();
			return;
		}
		var status  = $(this).attr("followFlag");
		var fundId = $(this).attr("fundId");
		var productId = $(this).attr("productId");
		$.ajax({
    		url:base_root+"/front/fund/info/setFoundCollection.do?r="+Math.random(),
    		data:{'opType':status,'fundId':fundId,'productId':productId},
    		dataType:"json",
    		type:"get",
    		success:function(data){
    			if(data.result){
    				if(status == 'Delete'){
    					$(".funds_heart" + fundId).removeClass("funds_heart_active");
    					$(".funds_heart" + fundId).attr("followFlag",'Follow');
    					layer.msg(langMutilForJs['favour.remove']);
    				}else{
						$(".funds_heart" + fundId).addClass("funds_heart_active");
    					$(".funds_heart" + fundId).attr("followFlag",'Delete');
    					layer.msg(langMutilForJs['favour.add']);
    				}
    			}
    		},
    		error:function(){
    			//alert("error!");
    			layer.msg(langMutilForJs['error.illegalStateException']);
    		}
    	})
    	
	});
	var comparison_name_liwidth;
    for(var i=0;i<$('.funds_comparison_name li').length;i++){
    	comparison_name_liwidth = ($('.funds_comparison_name li').eq(i).find('.fundName_left').width()+28) +'px';
    	$('.funds_comparison_name li').eq(i).css('width',comparison_name_liwidth);
    };
	var text = '';
	$('.fundName_left').hover(function(){  
		if($(this).text().length>15){
			comparison_name_liwidth = $(this).width()+28;
			$(this).next().css('display','none');
			var fundName = $(this).data('fundName');
			text = $(this).text();
			$(this).text(fundName);
			var fundName_width = $(this).width()+10;
			$(this).parent().stop().animate({width:fundName_width});
		}
	},function(){
		if($(this).text().length>15){
			$(this).next().css('display','block');
			$(this).text(text);
			$(this).parent().stop().animate({width:comparison_name_liwidth});
		}
	});
	
	//表头滚动
	function taskListScrool(){
		var funds_hei =  $(".tasklist-header").height(),
			headerTop = $(".wmes_top").height(), 
			taskIcoWidth = $(".line-wrap").width(), 
			wmes_window_top = $(window).scrollTop(),
			infor_top = $(".line-wrap").offset().top;
		if ( wmes_window_top > (infor_top  - headerTop) ){
		 	var new_funds_top = wmes_window_top - infor_top - 100;
		 	$(".tasklist-header").addClass("funds_fixed");
		 	$('.tasklist-header').attr("style",'width:'+taskIcoWidth+'px');
			//解决低版本浏览器tr无法滚动问题
			if( infor_top == $('.tasklist-header').offset().top && new_funds_top > funds_hei + 2 ){
				$('.tasklist-header').addClass("funds_tables_caption");
			}
		}else{
		 	$(".tasklist-header").removeClass("funds_fixed");
			var	 _thisStyleY =  "-webkit-transform : translateY(" + 0 + "px);-moz-transform : translateY(" + 0 + "px);-ms-transform : translateY(" + 0 + "px);-o-transform : translateY(" + 0 + "px);transform : translateY(" + 0 + "px)";
				$('.tasklist-header').attr("style",_thisStyleY);
		}
    };
    $(window).on('scroll',taskListScrool);
    
    $('.span_mao').click(function(){
    	$(window).unbind('scroll',taskListScrool);
    	setTimeout(function(){
    		$(window).bind('scroll',taskListScrool);
    	},300);
    	if($('.tasklist-header').hasClass('funds_fixed')){
    		$('.tasklist-header').removeClass('funds_fixed');
    	}
    });
    
	if(getUrlParam('id') == null){
		layer.msg(langMutilForJs['fund.scomparison.alert.please.choose.one.fund'],{icon:3});
	}
	/**
	 * get url param
	 */
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
});
