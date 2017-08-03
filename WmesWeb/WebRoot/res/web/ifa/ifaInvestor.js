
define(function(require) {
    var $ = require('jquery');
        require("echarts");
        require("layui");
        require("swiper");
        require('iscroll');
        var angular = require('angular');
        require("interfaceCheckPwd");
    	var Loading = require('loading');
         var cartArr = [];
        $(".cart-temp").each(function(){
            // 基于准备好的dom，初始化echarts实例
             var myChart = echarts.init($(this)[0]);

            // 指定图表的配置项和数据
            var base = +new Date(2014, 9, 3);
            var oneDay = 24 * 3600 * 1000;
            var date = [];
            
            var data = [Math.random() * 300];
            
            for (var i = 1; i < 200; i++) {
                var now = new Date(base += oneDay);
                date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'));
                data.push(Math.round( Math.random() * 20) );
            }

            var option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt) {
                        return [pt[0], '10%'];
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
                dataZoom: [{
                    type: 'inside',
                    start: 0,
                    end: 10
                }, {
                    start: 0,
                    end: 10,
                    handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                    handleSize: '80%',
                    handleStyle: {
                        color: '#fff',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0, 0, 0, 0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    }
                }],
                series: [
                    {
                        name:'模拟数据',
                        type:'line',
                        smooth:true,
                        symbol: 'none',
                        sampling: 'average',
                        itemStyle: {
                            normal: {
                                color: '#ff9896'
                            }
                        },
                        areaStyle: {
                            normal: {
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0,
                                    color: 'rgb(255, 158, 68)'
                                }, {
                                    offset: 1,
                                    color: 'rgb(255, 70, 131)'
                                }])
                            }
                        },
                        data: data
                    }
                ]
            };
            cartArr.push(myChart);
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        });
        function wolrdChart(){

            var base = +new Date(2016, 3, 29);
            var oneDay = 24 * 3600 * 1000;
            var date = [];
            
            var data = [Math.random() * 3700];
            
            for (var i = 1; i < 150; i++) {
                var now = new Date(base += oneDay);
                date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'));
                data.push(Math.round( Math.random() * 3700) );
            }

            var option = {
                tooltip: {
                    trigger: 'axis',
                    position: function (pt) {
                        return [pt[0], '10%'];
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
                        name:'模拟数据',
                        type:'line',
                        smooth:true,
                        symbol: 'none',
                        sampling: 'average',
                        itemStyle: {
                            normal: {
                                color: '#005cf8'
                            }
                        },
                        areaStyle: {
                            normal: {
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0,
                                    color: '#dfecfe'
                                }, {
                                    offset: 1,
                                    color: '#dfecfe'
                                }])
                            }
                        },
                        data: data
                    }
                ]
            };

            var myChart = echarts.init(document.getElementById('mian-chart'));
            cartArr.push(myChart);
            myChart.setOption(option);
        }
        wolrdChart();
        window.onresize = function () {
            $("#investor-zone-chart").width($(".investor-zone-wrap").width() - $(".investor-zone-left").width() - 25);
            for(var i = 0; i < cartArr.length; i++){
                cartArr[i].resize(); 
            }   
        }
        var swiper1 = new Swiper('.ifa-hide-swiper', {slidesPerView: 'auto', grabCursor: true, preventClicks : false, nextButton: '.swiper-button-next', prevButton: '.swiper-button-prev', });
        $(".ifa-hide-swiper").on("mouseenter",function(){
            $(this).find(".swiper-button-next, .swiper-button-prev").show();
        });
        $(".ifa-hide-swiper").on("mouseleave",function(){
            $(this).find(".swiper-button-next, .swiper-button-prev").hide();
        });

        $(".wmes-name-top").on("click",function(){
            $(".ifa-space-popup").show();
        });
        $(".space-mask-close").on("click",function(){
            $(".ifa-space-popup").hide();
        });
        
        var StrategiesJson = {"person":[
        {"name":"Funds Strategy","fundscount":"9","updatedate":"2016-07-25 16:12:22","Funds":"57","Bonds":"30","Stock":"45"},
        {"name":"Long-term Energy Strategy","fundscount":"12","updatedate":"2016-07-25 09:12:13","Funds":"38","Bonds":"32","Stock":"30"},
        {"name":"Long-term Conservative Strategy","fundscount":"6","updatedate":"2016-07-24 22:10:00","Bonds":"52","Stock":"48"},
        {"name":"High And New Technology Investment Strategy","fundscount":"15","updatedate":"2016-07-24 12:00:00","Funds":"42","Stock":"58"},
        {"name":"Bond Strategy","fundscount":"13","updatedate":"2016-07-25 06:09:00","Funds":"25","Bonds":"35","Stock":"40"},
        {"name":"Mining Low Equity Funds Strategy","fundscount":"15","updatedate":"2016-07-23 08:00:00","Funds":"48","Bonds":"22","Stock":"30"},
        {"name":"Low Risk And High Return Investment Strategy","fundscount":"16","updatedate":"2016-07-23 09:12:00","Funds":"25","Bonds":"45","Stock":"30"},
        {"name":"Mining Fidelity Funds Strategy","fundscount":"10","updatedate":"2016-07-22 19:00:00","Funds":"29","Bonds":"71"},
        {"name":"XinHe stock fund strategy","fundscount":"15","updatedate":"2016-07-22 17:00:00","Funds":"23","Bonds":"22","Stock":"55"}],
        "success":true,"type":"1","total":"12"};

        var PortfoliosJson = {"person":[
        {"name":"Mining Low Equity Funds Portfolio","rate":"18.15%"},
        {"name":"Long-term Energy Portfolio","rate":"12.22%"},
        {"name":"Long-term Conservative Portfolio","rate":"11.31%"},
        {"name":"High And New Technology Investment Portfolio","rate":"17.44%"},
        {"name":"Bond Portfolio","rate":"8.12%"},
        {"name":"Low Risk And High Return Investment Portfolio","rate":"9.94%"},
        {"name":"Mining Fidelity Funds Portfolio","rate":"8.12%"},]
        ,"success":true,"type":"1","total":"12"};

        var fundsJson = {"person":[
        {"name":"Allianz Inc and Growth Cl AM Dis H2-AUD ","rate":"19.54%"},
        {"name":"Blackrock Global High Yield Bd A8 AUD-H ","rate":"12.14%"},
        {"name":"Blackrock US Small and Midcap Opp A2 AUD-H ","rate":"16.72%"},
        {"name":"Legg Mason WA Global HY A AUD H (mdis) plus ","rate":"14.35%"},
        {"name":"BNPP A European Multi-Asset Inc MD RH CNH ","rate":"12.12%"},
        {"name":"Allianz Flexi Asia Bond Cl AM Dis H2-EUR","rate":"12.69%"},
        {"name":"Parvest Bond Euro HY Classic Cap EUR","rate":"14.27%"},
        {"name":"Schroder ISF Euro Eq Alp A Acc EUR","rate":"13.48%"},
        {"name":"Threadneedle European Select Cl 1 Net Acc EUR ","rate":"11.89%"}],
        "success":true,"type":"1","total":"12"};
        
        //进入页面时加载
        genStrategy(); genPortfolios(); genFunds();
        //点击地球时，加载全部
        $('#region-service-all').on('click',function(){
            $(this).siblings().removeClass('region-service-now');
            genStrategy(); genPortfolios(); genFunds();genHeight(); });
        
        //点击china时
        $('#egion-service-china').on('click',function(){
            //自己的样式加深
            $(this).addClass('region-service-now').siblings().removeClass('region-service-now');
            genStrategy(); genPortfolios(); genFunds();genHeight(); });
        
      //点击eu时
        $('#egion-service-eu').on('click',function(){
            $(this).addClass('region-service-now').siblings().removeClass('region-service-now');
            genStrategy(); genPortfolios(); genFunds();genHeight(); });
        
        //策略绘图
        function initStrategy(){
            $(".trending-strategies-chart").each(function(){
                var selfData = eval($(this).attr("data-value"));

                /*//设置颜色
                for(item in selfData){
                	
                    if( selfData[item].name =="Fund" ){
                        selfData[item]['itemStyle'] = {normal:{color:'#4891ff'}}
                    }else if(selfData[item].name =="Stock"){
                        selfData[item]['itemStyle'] = {normal:{color:'#5470df'}}
                    }else if( selfData[item].name =="Bonds" ){
                        selfData[item]['itemStyle'] = {normal:{color:'#40c7f3'}}
                    }
                }*/

                    var option = {
                        series: [
                            {
                                type:'pie',
                                center: ['50%', '50%'],
                                clockWise:false,                          
                                label: {
                                    normal: {
                                        position: 'inner',
                                        formatter : "{b}\n{d}%"
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
        //加载5条策略
        function genStrategy(){
            var list = StrategiesJson.person;
            var html = '';

            $("#ul_strategies_list").hide()
            $("#ul_strategies_list").empty();
            $.each(list,function(i, n) {
                var rand = parseInt(Math.random()*7,10);
                var n = list[rand];

                //组装图表data
                var sData = [];
                if(n.Funds){
                    sData.push('{name:"Fund",value:"'+ n.Funds +'"}');
                }

                if(n.Bonds){
                    sData.push('{name:"Bonds",value:"'+ n.Bonds +'"}');
                }

                if(n.Stock){
                    sData.push('{name:"Stock",value:"'+ n.Stock +'"}');
                }
                if(i>3)return;
                var li = '<li class="swiper-slide">';
                    li += '<a href="'+ base_root +'/front/ifa/info/strategiesdetail.do"><p class="trending-title">'+n.name+'</p>';
                    li += '<div class="trending-strategies-list"><p class="trending-strategies-word">Fiance,General</p><p class="trending-strategies-word">China</p></div>';
                    li += '<div class="trending-describe"><p class="trending-describe-word">Aggressive</p></div>'
                    li += "<div class='trending-strategies-chart' data-value='[" + sData + "]' ></div>";
                    li += '</a></li>';
                html += li;
            });
    
            $('#ul_strategies_list').append(html);
            $('#ul_strategies_list').fadeIn("slow");
            initStrategy();
        }
      //加载5条组合
        function genPortfolios(){
            var list = PortfoliosJson.person;
            var html = '';
            $("#ul_portfolios_list").hide();
            $("#ul_portfolios_list").empty();
            $.each(list,function(i, n) {
                var rand = parseInt(Math.random()*7,10);
                var li;
                n = list[rand];
                if(i>3)return;
                if( i % 2 == 0){
                li = '<li class="swiper-slide">';
                    li += '<a href="'+ base_root +'/front/ifa/info/strategiesdetail.do"><p class="trending-title">'+n.name+'</p>';
                    li += '<div class="trending-strategies-list"><p class="trending-strategies-word">Fiance,General</p><p class="trending-strategies-word">China</p></div>';
                    li += '<div class="trending-describe"><p class="trending-describe-word">'+ (rand+1) +' Risk</p></div>'
                    li += '<p class="trending-funds"><span class="trednding-big-red">-'+n.rate+'</span></p>';
                    li += '<p class="trending-date">1 MTH Return</p>';
                    li += '</a></li>';
                }else{
                li = '<li class="swiper-slide">';
                    li += '<a href="'+ base_root +'/front/ifa/info/strategiesdetail.do"><p class="trending-title">'+n.name+'</p>';
                    li += '<div class="trending-strategies-list"><p class="trending-strategies-word">Fiance,General</p><p class="trending-strategies-word">China</p></div>';
                    li += '<div class="trending-describe"><p class="trending-describe-word">'+ (rand+1) +' Risk</p></div>'
                    li += '<p class="trending-funds"><span class="trednding-big-green">'+n.rate+'</span></p>';
                    li += '<p class="trending-date">1 MTH Return</p>';
                    li += '</a></li>';
                }
                
                html += li;
            });

            $('#ul_portfolios_list').append(html);
            $("#ul_portfolios_list").fadeIn("slow");
        }
        
        //加载5条基金
        function genFunds(){
            var list = fundsJson.person;
            var html = '';

            $("#ul_funds_list").hide();
            $("#ul_funds_list").empty();
            $.each(list,function(i, n) {
                var rand = parseInt(Math.random()*7,10);
                n = list[rand];
                var li;
                if(i>3)return;
                if( i % 2 == 0){
                li = '<li class="swiper-slide">';
                    li += '<a href="'+ base_root +'/front/ifa/info/strategiesdetail.do"><p class="trending-title">'+n.name+'</p>';
                    li += '<div class="trending-strategies-list"><p class="trending-strategies-word">Fiance,General</p><p class="trending-strategies-word">China</p></div>';
                    li += '<div class="trending-describe"><p class="trending-describe-word">'+ (rand+1) +' Risk</p></div>'
                    li += '<p class="trending-funds"><span class="trednding-big-green">'+n.rate+'</span></p>';
                    li += '<p class="trending-date">1 MTH Return</p>';
                    li += '</a></li>';
                }else{
                li = '<li class="swiper-slide">';
                    li += '<a href="'+ base_root +'/front/ifa/info/strategiesdetail.do"><p class="trending-title">'+n.name+'</p>';
                    li += '<div class="trending-strategies-list"><p class="trending-strategies-word">Fiance,General</p><p class="trending-strategies-word">China</p></div>';
                    li += '<div class="trending-describe"><p class="trending-describe-word">'+ (rand+1) +' Risk</p></div>'
                    li += '<p class="trending-funds"><span class="trednding-big-red">-'+n.rate+'</span></p>';
                    li += '<p class="trending-date">1 MTH Return</p>';
                    li += '</a></li>';
                }
                html += li;
            });
            
            $('#ul_funds_list').append(html);
            $("#ul_funds_list").fadeIn("slow");
        }
        
      //点击左时
        $("body").on('click', '.page_left', '', function () {
            var type = $(this).attr('type');
            if(type=='strategies'){
                genStrategy();
            } else if(type=='portfolios'){
                genPortfolios();
            } else if(type=='funds'){
                genFunds();
            }
            
        });
        
        $("body").on('click', '.page_right', '', function () {
            var type = $(this).attr('type');
            if(type=='strategies'){
                genStrategy();
            } else if(type=='portfolios'){
                genPortfolios();
            } else if(type=='funds'){
                genFunds();
            }
        });
        
        //点击能源等类别
        $('.ul-ifa-his-type li').on('click',function(){
            $(this).addClass('now').siblings().removeClass('now');
            //alert($(this).attr('data-key'))
            genStrategy();
            genPortfolios();
            genFunds();
            //genHeight();
        });
        
        //8大指数
//      var zsArray = ['SHI','SSE','SZSE','DJIA','NASDAQ','NIKKEI225','DAX','CAC'];
        var totalZSJson = {"person":
        [{"name": "SHI","todaynum":"22949.43", "todayrate":"+129.13", "todayratepercent":"+0.54%", "今开":"22949.43", "昨收":"22190.00", "最高":"22390.00", "最低":"21178.00", "成交量":"1256.00", "成交额":"2190.00", "市值":"34124.00"}, {"name":"SSE","todaynum":"3085.57","todayrate":"+17.41","todayratepercent":"+0.57%","今开":"3085.57","昨收":"3033.42","最高":"3099.03","最低":"3005.34","成交量":"128.00","成交额":"789.00","市值":"1291.00"},{"name":"SZSE","todaynum":"10779.83","todayrate":"+100.08","todayratepercent":"+0.94%","今开":"10779.83","昨收":"10549.43","最高":"10798.00","最低":"10679.43","成交量":"22190.00","成交额":"12190.00","市值":"34789.00"},{"name":"DJIA","todaynum":"18448.21","todayrate":"-33.07","todayratepercent":"-0.59%","今开":"18448.43","昨收":"18248.45","最高":"18499.45","最低":"18341.23","成交量":"23112.00","成交额":"1009.00","市值":"43223.00"},{"name":"NASDAQ","todaynum":"22949.43","todayrate":"+129.13","todayratepercent":"+0.54%","今开":"22949.43","昨收":"22190.00","最高":"22190.00","最低":"22190.00","成交量":"22190.00","成交额":"22190.00","市值":"22190.00"},{"name":"NIKKEI225","todaynum":"22949.43","todayrate":"+129.13","todayratepercent":"+0.54%","今开":"22949.43","昨收":"22190.00","最高":"22190.00","最低":"22190.00","成交量":"22190.00","成交额":"22190.00","市值":"22190.00"},{"name":"DAX","todaynum":"22949.43","todayrate":"+129.13","todayratepercent":"+0.54%","今开":"22949.43","昨收":"22190.00","最高":"22190.00","最低":"22190.00","成交量":"22190.00","成交额":"22190.00","市值":"22190.00"},{"name":"CAC","todaynum":"22949.43","todayrate":"+129.13","todayratepercent":"+0.54%","今开":"22949.43","昨收":"22190.00","最高":"22190.00","最低":"22190.00","成交量":"22190.00","成交额":"22190.00","市值":"22190.00"},],"success":true,"type":"1","total":"12"}; 
        function genZSPopu(type){
            var json ;
            var curnumber=0;
            if(type==0){json =totalZSJson.person[0];curnumber=0;}
            else if(type==1){json =totalZSJson.person[1];curnumber=1;}
            else if(type==2){json =totalZSJson.person[2];curnumber=2;}
            else if(type==3){json =totalZSJson.person[3];curnumber=3;}
            else if(type==4){json =totalZSJson.person[4];curnumber=4;}
            else if(type==5){json =totalZSJson.person[5];curnumber=5;}
            else if(type==6){json =totalZSJson.person[6];curnumber=6;}
            else if(type==7){json =totalZSJson.person[7];curnumber=7;}
            //获取数据
            $('#p_cur_zs_num').attr('number',curnumber);
            $('#p_cur_zs_num').text(json.name);
            $('#data-1').text(json.todaynum);
            $('#data-2').text(json.todayrate+'('+json.todayratepercent+')');
            $('#data-3').text(json.今开);
            $('#data-4').text(json.昨收);
            $('#data-5').text(json.最高);
            $('#data-6').text(json.最低);
            $('#data-7').text(json.成交量);
            $('#data-8').text(json.成交额);
            $('#data-9').text(json.市值);
            wolrdChart();
        }
        
        $('#btn_trurnleft').click(function(){
            //获取当前的显示NUMBER
            var number =  $('#p_cur_zs_num').attr('number');
            var prevnumber
            if(parseInt(number)>0){
                prevnumber = parseInt(number)-1;
            } else{
                prevnumber=7;
            }
            genZSPopu(prevnumber);
        });
        
        $('#btn_trurnright').click(function(){
            //获取当前的显示NUMBER
            var number =  $('#p_cur_zs_num').attr('number');
            var prevnumber
            if(parseInt(number)<7){
                prevnumber = parseInt(number)+1;
            } else{
                prevnumber=0;
            }
            genZSPopu(prevnumber);
        });
        
        //点击各个指数弹出
        $("body").on('click', '.investor-top-li', '', function () {
            $(".ifa-space-popup").show();
            var number = $(this).attr('number');
            genZSPopu(parseInt(number));
        });
        
        //点击圆形切换
        $(".ifa-more-ico").on("click",function(){
            $(this).parents(".ifa-trending-row").toggleClass("screener_trending_narrow");
        });

        $(".ifa-trending-row").on("mouseenter",function(){
            $(this).find(".page_left, .page_right").show();
        });
        $(".ifa-trending-row").on("mouseleave",function(){
            $(this).find(".page_left, .page_right").hide();
        });
        
        var swiper2 = new Swiper('.ifa-trending-content', {
            slidesPerView: 'auto',
            preventClicks : false,
            mousewheelControl : false
        });

        new Swiper('.following-images-wrap', {
            slidesPerView: 'auto',
            preventClicks : false,
            mousewheelControl : false,
            nextButton: '.following-right',
            prevButton: '.following-left',
        });

        new Swiper('.ifa-trending-discussing ', {
            slidesPerView: 'auto',
            preventClicks : false,
            mousewheelControl : false,
            nextButton: '.discussing-right',
            prevButton: '.discussing-left',
        });

   

   /* function setDate(){
    	
    	
    	
        var base = +new Date(2016, 1, 01);
            var oneDay = 24 * 3600 * 1000;
            var date = [];
            
            var data = [Math.random() * 30];
            
            for (var i = 1; i < 30; i++) {
                var now = new Date(base += oneDay);
                date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'));
                data.push(Math.round(Math.random() * 3000 ));
            }
            return [data,date];
    }
    var info = [];
    for (var j = 0 ; j < 3;j++){
        info.push(setDate())
    }*/
    
    var mybase = angular.module('investorHome', ['wmespage','truncate']);
	mybase.controller('homeCtrl', ['$scope', '$http', function($scope, $http) {
		var oLoading = new Loading($(".investor-zone-wrap"));	
		$scope.portfolioList='';
		
		getPortfolioList();
		function getPortfolioList(){
			oLoading.show();
			var currency=$("#txtCurrency").val();
			var url = base_root + "/front/investor/queryMyPortfolioList.do?dateStr=" + new Date().getTime();
			$http({
              url: url,
              method: 'POST',
              headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
              params : {
            	  currency:currency
              }
         	 })
			.success(function(response){
				
				oLoading.hide();
				$scope.portfolioList=response.portfolioList;
				if($scope.portfolioList!=undefined && $scope.portfolioList.length>0){
					$(".investor-zone-wrap").find(".investor-zone-left").show();
					$(".investor-zone-wrap").find(".investor-zone-right").show();
				}else{
					$(".investor-nodata-zone").show();
				}
				setTimeout(function(){
					separateness();
				},500)
				
			});
			
		}
		
		  
		function separateness(){ 
			var ChartData = [],
		    ChartDate,
		    ChartName = [];
	    	$(".investor-portfolio-wrap").find(".investor-portfolio-rows").each(function(i){
	    		var dataList=$(this).attr("dataList");
	    		////console.log(eval("("+dataList+")") );
	    		var data=eval("("+dataList+")");
	    		var arr =[];
	    		$.each(data,function(i,n){
	    			arr.push(n);
	    		});
	    		
	    		////console.log(arr);
	    		if($(this).hasClass("investor-portfolio-active")){
	    			 ChartData.push({
	                     name: $(".investor-zone-choose").eq(i).html().trim(),
	                     type: 'line',
	                     data: arr,
	                     itemStyle:{normal :{lineStyle:{width : 4}}}
	                 });
	    		}else{
	    			 ChartData.push({
	                     name: $(".investor-zone-choose").eq(i).html().trim(),
	                     type: 'line',
	                     data:arr
	                 });
	    		}
	    		 ChartName.push($(this).find(".investor-zone-choose").html().trim());
	    		
	    	});
	    	ChartDate=_dateList.split(',');
	        
	    /*	//console.log(ChartData);
	    	//console.log(ChartName);
	    	//console.log(ChartDate);*/
	    	
	        var option = {
	            tooltip: {
	                trigger: 'axis',
	                position: function (pt) {
	                    return [pt[0], '10%'];
	                }
	            },
	            grid: {
	                top: 30,
	                bottom:100,
	                // x:"5%",
	                // x2:"5%",
	                // width:"90%"
	            },
	            color :["#3399cc","#ff9999","#99cc99"],
	            title: {
	                left: 'center',
	                text: '',//图表标题WMES
	            },
	            xAxis: {
	                type: 'category',
	                boundaryGap: false,
	                data: ChartDate
	            },
	            yAxis: {
	                type: 'value',
	                boundaryGap: [0, '100%']
	            },
	            legend: {
	                orient : 'horizontal',
	                x: 'left',
	                y:'bottom',
	                data:ChartName
	            },
	            series: ChartData
	        };
	        $("#investor-zone-chart").width($(".investor-zone-wrap").width() - $(".investor-zone-left").width() - 25);
	        if(document.getElementById('investor-zone-chart')){
	        	var myChart = echarts.init(document.getElementById('investor-zone-chart'));
	            cartArr.push(myChart);
	            myChart.setOption(option,true);
	        }
	        
	    }
		
		
		 $('.investor-portfolio-wrap').on("click",'.investor-zone-choose',function(){
		        $(this).parents(".investor-portfolio-rows").siblings().removeClass("investor-portfolio-active").end().addClass("investor-portfolio-active");
		        var content= $('.investor-portfolio-content');
		        var tLabel = $(this);
		        var tContent = tLabel.siblings(".investor-portfolio-content");

		        content.stop(true).slideUp(200);
		        tContent.stop(true).slideDown(200);
		      
		        separateness($(this).parents(".investor-portfolio-rows").index());
		        if(tContent.is(":visible")) {
		            return;
		        }

		    });
		// 下拉
		   $(".investor-xiala").on("click",function(){
		       $(this).toggleClass("xiala-show");
		     
		   });
		   $('.investor-xiala').on('mouseleave',function(){
		   		$(this).removeClass("xiala-show");
		   });
		   $(".xiala-list li").on("click",function(){
			   $(this).parent().hide();
			  $("#txtCurrency").val($(this).text());
			  getPortfolioList();
			  
			  /* var currency=$("#txtCurrency").val();
		       var url=window.location.href;
		       if(url.indexOf("?", 0)>0){
		    	   url=url.substring(0,url.indexOf("?", 0));
		       }
		       window.location.href=url+"?currency="+currency;*/
		   })
		   
		
	}]);
    
    
  
   
    
    $(".inv-top-performance p").on("click",function(){
        $(this).siblings().removeClass("inv-active").end().addClass("inv-active");
        $(".inv-performance-wrap > div").hide().eq($(this).index()).show();
    });
        
    
 
    var liveScroll = new IScroll('#investor-live-wrap');

    $(" .investor-portfolio-wrap ").on("click",".inv_eve_ico",function(){
        $(".investor-portfolio-wrap .inv_eve_ico ").toggleClass("inv_eve_active");
        if($(this).hasClass("inv_eve_active")){
            $(".inv-num-hide").html("***");
            $(".my-assets-cur").hide();
        }else{
            $(".inv-num-hide").each(function(){
                $(this).html($(this).attr("data-num"));
            });
            $(".my-assets-cur").show();
        }
    });
    
   

    $(".ifa-his-xiala").on("click",function(){

        var _selfParents = $(this).parents(".ifa-his-map");

        _selfParents.toggleClass("ifa-his-show");

        if(_selfParents.hasClass("ifa-his-show")){
            _selfParents.stop().animate({ 
                height: _selfParents.find("ul").height() + "px", 
            }, 300 );
        }else{
            _selfParents.stop().animate({ 
                height: "35px", 
            }, 300 );
        }

    });

   $(".ifa-his-map li").on("click",function(){
            $(this).siblings().removeClass("ifa-his-map-active").end().toggleClass("ifa-his-map-active");

    });
   
   /******************************************新版投资人首页***************************************/
   
   //点击下拉筛选数据
   $("#investor-period-xiala ul li").on("click",function(e){ 
       $(this).parents("#investor-period-xiala").toggleClass("xiala-show").find("input").val($(this).html());
       var datavalue = $(this).attr('data-value');
       if($('#p-portfolio').hasClass('inv-active')) getTopPerformancePortfolio(datavalue);
       else if($('#p-funds').hasClass('inv-active'))getTopPerformanceFunds(datavalue);
       //$(".inv-per-con-date").html($(this).html());        
       e.stopPropagation(); 
   });   
   
   $(".investor-xiala").on("mouseleave",function(){
        //$(this).toggleClass("xiala-show");
    });
   function getTopPerformancePortfolio(periodCode){ 
	   $.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/investor/queryTopPerformancePortfolioList.do?datestr="+ new Date().getTime(),
			data : {'periodCode':periodCode},
			beforeSend: function () {},
           complete: function () {},
			success : function(json) {
				//console.log(json);
				//[{"portfolioId":null,"portfolioName":"农业教育投资组合","increasePercent":0.89,"increaseImagePath":null,"period":"近1月","titleNum":0},{"portfolioId":null,"portfolioName":"亚太稳健型基金投资组合","increasePercent":0.67,"increaseImagePath":null,"period":"近1月","titleNum":2},{"portfolioId":null,"portfolioName":"组合17","increasePercent":0.37,"increaseImagePath":null,"period":"近1月","titleNum":4}]

				
				var content_html = '';
				var other_html = '<ul class="inv-performance-list">';
				$.each(json,function(i, n) {
					var portfolioId = n.portfolioId;
					var portfolioName = n.portfolioName;
					var increasePercent = n.increasePercent;
					var increasePercentString = n.increasePercentStr;
//					var increaseImagePath = n.increaseImagePath;
//					var period = n.period;
//					var titleNum = n.titleNum;
					var riskLevel = n.riskLevel;
					var period = n.period;
					var top_html = '';
					
					if(i==0){ //输出第一个
//						top_html += '<div class="inv-performance-main">';
//						top_html += '<div class="inv-per-num">1</div>';
//						top_html += '<div class="inv-per-contents">';
//						top_html += '<p class="inv-per-title"><a class="ifa-a-href" href="/wmes/front/ifa/info/porfoliosdetail.do">'+portfolioName+'</a></p>';
//						top_html += '<div class="inv-per-main">';
//						top_html += '<img class="inv-per-img" src="/wmes/res/images/ifa/inv_index_img.png">';
//						top_html += '<p class="inv-per-con-title">'+genColorFloadNum(increasePercent,increasePercentString)+'</p>';
//						top_html += '<p class="inv-per-con-word"><span class="inv-per-con-date">'+period+'</span> Return</p>';
//						top_html += '</div>';
//						top_html += '</div>';
//						top_html += '</div>';
						top_html += '<div class="inv-performance-main">';
						top_html += '<div class="inv-per-num">1</div>';
						top_html += '<div class="inv-per-contents">';
						top_html += '<p class="inv-per-title"><a class="ifa-a-href" href="'+base_root+'/front/portfolio/arena/detail.do?id='+portfolioId+'">'+portfolioName+'</a></p>';
						top_html += '<div class="inv-per-main">';
						top_html += '<span class="lump-equity-grade funds_leveal_'+riskLevel+'" style="margin-right:30px" >'+riskLevel+'</span>';
						top_html += '<p class="inv-per-con-title"><font class="price_positive">'+genColorFloadNum(increasePercent,increasePercentString)+'</font></p>';
						top_html += '<p class="inv-per-con-word"><span class="inv-per-con-date">'+period+'</span> Return</p>';
						top_html += '</div>';
						top_html += '</div>';
						top_html += '</div>';
						content_html+= top_html;
					}
					//输出第2到第5只
					if(i>0){
						var li_html = '';
//							li_html += '<div class="inv-per-num">'+(i+1)+'</div>';
//							li_html += '<div class="inv-per-contents">';
//							li_html += '<p class="inv-per-title"><a class="ifa-a-href" href="/wmes/front/ifa/info/porfoliosdetail.do">'+portfolioName+'</a></p>';
//							li_html += '<div class="inv-per-main">';
//							li_html += '<img class="inv-per-img" src="/wmes/res/images/ifa/inv_index_img.png">';
//							li_html += '<p class="inv-per-con-title">'+increasePercent+'%</p>';
//							li_html += '<p class="inv-per-con-word"><span class="inv-per-con-date">'+period+'</span> Return</p>';
//							li_html += '</div>';
//							li_html += '</div>';
//							li_html += '</li>';
						li_html += '<li>';
						li_html += '<div class="inv-per-num">'+(i+1)+'</div>';
						li_html += '<div class="inv-per-contents">';
						li_html += '<p class="inv-per-title"><a class="ifa-a-href" href="'+base_root+'/front/portfolio/arena/detail.do?id='+portfolioId+'">'+portfolioName+'</a></p>';
						li_html += '<div class="inv-per-main">';
						li_html += '<span class="lump-equity-grade funds_leveal_'+riskLevel+'" style="margin-right:30px" title="Risk Level '+riskLevel+'">'+riskLevel+'</span>';
						li_html += '<p class="inv-per-con-title"><font class="price_zero">'+genColorFloadNum(increasePercent,increasePercentString)+'</font></p>';
						li_html += '<p class="inv-per-con-word"><span class="inv-per-con-date">'+period+'</span> Return</p>';
						li_html += '</div>';
						li_html += '</div>';
						li_html += '</li>';
						other_html+= li_html;
					}
				});
				other_html+= "</ul>";
				content_html += other_html;
				$('#div-topperformance-portfolio').empty().append(content_html);

			}
		});
   }
   
   function genColorFloadNum(increasePercent,increasePercentString){
		var fmtnumber = (parseFloat(increasePercent));
		var rcolor="color:#24b31d!important;";
		var fcolor="color:red!important;";
		if(MEMBER_DEFDISPLAYCOLOR=='1'){
			rcolor="color:#24b31d!important;";
			fcolor="color:red!important;";
		} else if(MEMBER_DEFDISPLAYCOLOR=='2'){
			rcolor="color:red!important;";
			fcolor="color:#24b31d!important;";
		}
		if(fmtnumber>0){
			return '<span style="'+rcolor+'">'+increasePercentString+'</span>';
		} else if(fmtnumber==0){
			return '<span style="">'+increasePercentString+'</span>';
		} else if(fmtnumber<0){
			return '<span style="'+fcolor+'">'+increasePercentString+'</span>';
		} else return '<span style="">'+increasePercentString+'</span>';
	}
   
   function getTopPerformanceFunds(periodCode){ 
	   $.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/investor/queryTopPerformanceFundsList.do?datestr="+ new Date().getTime(),
			data : {'periodCode':periodCode},
			beforeSend: function () {},
           complete: function () {},
			success : function(json) {
				//console.log(json);
				//[{"fundId":null,"fundName":"安本环球 - 亚太股票基金 (美元) A2","increase":11.76,"increaseImagePath":null,"period":"近3月","titleNum":0},{"fundId":null,"fundName":"富兰克林高息基金 (美元) A (每月派息)","increase":6.63,"increaseImagePath":null,"period":"近3月","titleNum":2},{"fundId":null,"fundName":"法国巴黎L1基金俄罗斯股票基金经典资本类别(欧元)","increase":5.93,"increaseImagePath":null,"period":"近3月","titleNum":4}]

				
				var content_html = '';
				var other_html = '<ul class="inv-performance-list">';
				$.each(json,function(i, n) {
					var fundId = n.fundId;
					var fundName = n.fundName;
					var increase = n.increase;
					var increasePercentString = n.increasePercentStr;
//					var increaseImagePath = n.increaseImagePath;
					var period = n.period;
//					var titleNum = n.titleNum;
					var riskLevel = n.riskLevel;
					var top_html = '';
					
					if(i==0){ //输出第一个
//						top_html += '<div class="inv-performance-main">';
//						top_html += '<div class="inv-per-num">1</div>';
//						top_html += '<div class="inv-per-contents">';
//						top_html += '<p class="inv-per-title"><a class="ifa-a-href" href="/wmes/front/ifa/info/porfoliosdetail.do">'+fundName+'</a></p>';
//						top_html += '<div class="inv-per-main">';
//						top_html += '<img class="inv-per-img" src="/wmes/res/images/ifa/inv_index_img.png">';
//						top_html += '<p class="inv-per-con-title">'+increase+'%</p>';
//						top_html += '<p class="inv-per-con-word"><span class="inv-per-con-date">'+period+'</span> Return</p>';
//						top_html += '</div>';
//						top_html += '</div>';
//						top_html += '</div>';
						top_html += '<div class="inv-performance-main">';
						top_html += '<div class="inv-per-num">1</div>';
						top_html += '<div class="inv-per-contents">';
						top_html += '<p class="inv-per-title"><a class="ifa-a-href" href="'+base_root+'/front/fund/info/fundsdetail.do?id='+fundId+'">'+fundName+'</a></p>';
						top_html += '<div class="inv-per-main">';
						top_html += '<span class="lump-equity-grade funds_leveal_'+riskLevel+'" style="margin-right:30px" title="Risk Level '+riskLevel+'">'+riskLevel+'</span>';
								
						top_html += '<p class="inv-per-con-title"><font class="price_positive">'+genColorFloadNum(increase,increasePercentString)+'</font></p>';
						top_html += '<p class="inv-per-con-word"><span class="inv-per-con-date">'+period+'</span> Return</p>';
						top_html += '</div>';
						top_html += '</div>';
						top_html += '</div>';
						content_html+= top_html;
					}
					//输出第2到第5只
					if(i>0){
						var li_html = '';
//							li_html += '<div class="inv-per-num">'+(i+1)+'</div>';
//							li_html += '<div class="inv-per-contents">';
//							li_html += '<p class="inv-per-title"><a class="ifa-a-href" href="/wmes/front/ifa/info/porfoliosdetail.do">'+fundName+'</a></p>';
//							li_html += '<div class="inv-per-main">';
//							li_html += '<img class="inv-per-img" src="/wmes/res/images/ifa/inv_index_img.png">';
//							li_html += '<p class="inv-per-con-title">'+increase+'%</p>';
//							li_html += '<p class="inv-per-con-word"><span class="inv-per-con-date">'+period+'</span> Return</p>';
//							li_html += '</div>';
//							li_html += '</div>';
//							li_html += '</li>';
						li_html += '<li>';
						li_html += '<div class="inv-per-num">'+(i+1)+'</div>';
						li_html += '<div class="inv-per-contents"><p class="inv-per-title"><a class="ifa-a-href" href="/wmes/front/fund/info/fundsdetail.do?id='+fundId+'">'+fundName+'</a></p>';
						li_html += '<div class="inv-per-main"><img class="inv-per-img" src="'+base_root+'/res/images/ifa/inv_index_img.png"><p class="inv-per-con-title">'+genColorFloadNum(increase,increasePercentString)+'</p>';
						li_html += '<p class="inv-per-con-word"><span class="inv-per-con-date">'+period+'</span> Return</p></div></div></li>';
						other_html+= li_html;
					}
				});
				other_html+= "</ul>";
				content_html += other_html;
				$('#div-topperformance-funds').empty().append(content_html);

			}
		});
   }

   onopenpage();
   function onopenpage(){
	   $.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/investor/whenOpenInvestorPage.do?datestr="+ new Date().getTime(),
			data : {},
			beforeSend: function () {},
          complete: function () {},
			success : function(json) {
				//console.log(json);
				var accountlist = '';
				$.each(json.list,function(i,n){ 
					var accountNo = n.accountNo;
					accountlist += accountNo+','
				});

				//var jsonlist = JSON.parse('{"accountlist":"'+accountlist+'","accounttype":"1"}');
				//$(this).InitInterface({"accountlist":"mqzou001,mic123,mqzou004","accounttype":"1","isopen":"1" });
			}
	   });
   }
 //验证登录
   if(_checkList!=undefined && _checkList!=""){
   $(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
   }


	   //var jsonlist = JSON.parse('{"accountlist":"mqzou001|1,mic123|1,mqzou004|1","targetid":"hidloginresult"}');
	   //$("#btninterface").InitInterface({"accountlist":"mqzou001,mic123,mqzou004","accounttype":"1" });

});