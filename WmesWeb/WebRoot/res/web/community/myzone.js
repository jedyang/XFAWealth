/**
 * myzone.js 我的区域页面
 * @author 李裕恒
 * email: 673577011@qq.com
 * @date: 2016-03-21
 */
define(function(require) {
	var $ = require('jquery');
//		require("swiper");
		require("echarts");
	
//	var myswiper= new Swiper('.swiper-container', {
//	    slidesPerView: 'auto',
//	    preventClicks : false,
//	    nextButton: '.swiper-button-next',
//	    prevButton: '.swiper-button-prev',
//	});
	
	
	function windowHeight(){
		var windowHeight = $(window).height();
		windowHeight = windowHeight-70;
		if($('.wmes-content').height() < windowHeight){
				$('.wmes-content').css('min-height',windowHeight);
                $('.wmes-notop-content').css('min-height',windowHeight);
		};
	};
	
	for(var i=0;i<$('.wmes-community-tab').length;i++){
		$('.wmes-community-tab').eq(i).next().css('display','block');
	};
	
	$('.wmes-community-topic-collect').on('click',function(){
		$(this).toggleClass('wmes-community-topic-collected');
	});
	
	$(document).on('click','.myzone-notice-th li',function(){
		$(this).addClass('myzone-notice-thliac').siblings().removeClass('myzone-notice-thliac');
		$('.notice-list-cut').eq($(this).index()).addClass('notice-list-cutac').siblings().removeClass('notice-list-cutac');
	});
	
	function mychars(){
		var option = {
    			tooltip : {
        			trigger: 'axis'
    			},
    			legend: {
      				y:'bottom',
        			data:['All Portfolio','“黄模建”的组合']
    			},
    			calculable : true,
    			xAxis : [
        			{
            			type : 'category',
            			splitLine: {show: false},
            			data : ['2017/02/15','2017/02/21','2017/02/27','2017/03/05','2017/03/11']
        			}
    			],
    			yAxis : [
        			{
            			type : 'value'
        			}
    			],
    			series : [
        			{
            			name:'All Portfolio',
            			type:'line',
            			data:[0, 0, 0, 900, 0,]
        			},
        			{
            			name:'“黄模建”的组合',
            			type:'line',
            			data:[0, 0, 0, 900, 0,]
        			}
    			]
			};
		var myChart1 = echarts.init(document.getElementById('mycharts'));
			myChart1.setOption(option, true);
	}
	mychars();
	
	function initChar() {
		$(".myzone-strategies-charts").each(function(){
			var selfData = eval($(this).attr("data-value"));
			var opdata=[],selfcolor=[];
			for(var i = 0; i < selfData.length; i++) {
				var data={};
				data.value=selfData[i].value;
				data.name=langMutilForJs['allocation.'+selfData[i].name]+" "+selfData[0].value;
				opdata.push(data);
				if(selfData[i].name.indexOf('bond')==0){
					selfcolor.push("#9dd84e");
				}else if(selfData[i].name.indexOf('fund')==0){
					selfcolor.push("#f6ac0a");
				}else if(selfData[i].name.indexOf('stock')==0){
					selfcolor.push("#8c5ec2");
				}
			};
			var option = {
				tooltip : {
        			trigger: 'item',
        			formatter: "{b} ({d}%)"
    			},
    			clickable:false,
				series: [
				    {
				    	name:'account',
				        type:'pie',
				            center:	['42%', '50%'],
				            color :selfcolor,
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				            itemStyle: {
				            	normal:{
				            		label:{
				            			textStyle:{
				            				fontSize:10
				            			}
				            		}
				            	},
				            	emphasis:{
				            		show:false
				            	}
				            },
				       		data:opdata
				    }
				]
			};
			var myChart2 = echarts.init($(this)[0]);
	  		myChart2.setOption(option);
		});
	}
	initChar();
	
	$('.ifa-more-ico').on('click',function(){
		$(this).toggleClass('ifa-more-icoactive');
		if($(this).hasClass('ifa-more-icoactive')==false){
			$(this).closest('.myzone-recommended-rows').css('height','auto');
		}else{
			$(this).closest('.myzone-recommended-rows').animate({'height':'59px'});
		}
	});
	
	$('.myzone-myassets-choose li').on('click',function(){
		$(this).addClass('myzone-myassets-chooseac').siblings().removeClass('myzone-myassets-chooseac');
	});
});