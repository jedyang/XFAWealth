define(function(require) {
	var $ = require('jquery');
		require("swiper");
		require("echarts");
		require("layui");
	var angular = require("angular");
	
	$(document).on('click','.investor_eve_ico',function(){
		if($(this).hasClass('investor_eve_ico_show')){
			$(this).removeClass('investor_eve_ico_show');
			$('.investor_eve_ico_hide').remove();
			$('.investor_eve_ico_flag').show();
			sessionStorage.setItem("showassets","1")
		}else{
			$('.investor_eve_ico_flag').hide();
			$('.investor_eve_ico_flag').eq(0).closest('li').append('<p class="my-assets-word investor_eve_ico_hide"><span>***</span><img class="eve_ico investor_eve_ico investor_eve_ico_show" src="'+base_root+'/res/images/application/eye_hide.png"></p>');
			$('.investor_eve_ico_flag:gt(0)').closest('li').append('<p class="my-assets-word investor_eve_ico_hide"><span>***</span></p>');
			sessionStorage.setItem("showassets","0")
		}
	});
	
	var showassets = sessionStorage.getItem("showassets");
	if(showassets=="0"){
		$(".eve_ico").click();
	}
	
	function initSwiper(){
		var myswiper= new Swiper('.swiper-container', {
		    slidesPerView: 'auto',
		    preventClicks : false,
		    nextButton: '.swiper-button-next',
		    prevButton: '.swiper-button-prev',
		});
	}
	function windowHeight(){
		var windowHeight = $(window).height();
		windowHeight = windowHeight-70;
		if($('.wmes-content').height() < windowHeight){
				$('.wmes-content').css('min-height',windowHeight);
                $('.wmes-notop-content').css('min-height',windowHeight);
		};
	};
	
	for(i=0;i<$('.wmes-community-tab').length;i++){
		$('.wmes-community-tab').eq(i).next().css('display','block');
	};
	
	$('.wmes-community-topic-collect').on('click',function(){
		$(this).toggleClass('wmes-community-topic-collected');
	});
	
	$('.wmes-community-tab li').on('click',function(){
		$(this).addClass('wmes-community-tabac').siblings().removeClass('wmes-community-tabac');
		var tempclass = $(this).closest('ul').next().attr('class');
		$(this).closest('div').find("."+tempclass).css('display','none');
		$(this).closest('div').find("."+tempclass).eq($(this).index()).css('display','block');
		windowHeight();
	});
	
	var myChart1 = echarts.init(document.getElementById('mycharts'));
	function mychars(data){
		var portfolioNames = []; 
		var xAxis = []; 
		var series = []; 
		var minVal = 0;
		var maxVal = 0;
		if(typeof data != 'undefined' && data != null){
			$.each(data,function(i,n){
				portfolioNames.push(n.portfolioName);
				var seriesMap = {};
				seriesMap['name'] = n.portfolioName;
				seriesMap['type'] = 'line';
				if(n.holdCumperfs.length > 0){
					var data_ = []; 
					$.each(n.holdCumperfs,function(j,m){
						data_.push((m.cumulativeRate*100).toFixed(2));
						xAxis.push(m.valuationDate);
					});
					if(data_.min() < minVal){
						minVal = data_.min();
						minVal = Math.floor(Number(minVal)*1.2);
					}
					if(data_.max() > maxVal){
						maxVal = data_.max();
						maxVal = Math.ceil(Number(maxVal)*1.2);
					}
					seriesMap['data'] = data_;
				}
				series.push(seriesMap);
			});
		}
		var option = {
    			tooltip : {
        			trigger: 'axis'
    			},
    			legend: {
      				y:'bottom',
        			data:[]
    			},
    			calculable : true,
    			xAxis : [
        			{
            			type : 'category',
            			splitLine: {show: false},
            			data : []
        			}
    			],
    			yAxis : [
        			{
            			type : 'value',
            			min:minVal,
        				max:maxVal,
            			axisLabel : {
			                formatter: '{value} %'
			            },
			            splitNumber:10
        			}
    			],
    			series : []
			};
		option.legend['data'] = portfolioNames;
		option.xAxis[0].data = xAxis;
		option.series = series;
		myChart1.setOption(option, true);
	}
	$(window).resize(function(){
		myChart1.resize();
    });
	
	function initChar() {
		$(document).find(".myzone-strategies-charts").each(function(){
			var selfData = eval($(this).attr("data-value"));
			var opdata=[],selfcolor=[];
			if(selfData != null){
				for(var i = 0; i < selfData.length; i++) {
					var data={};
					data.value=selfData[i].value;
					data.name=langMutilForJs['allocation.'+selfData[i].name];
					opdata.push(data);
					if(selfData[i].name.indexOf('bond')==0){
						selfcolor.push("#a0d54e");
					}else if(selfData[i].name.indexOf('fund')==0){
						selfcolor.push("#fab00a");
					}else if(selfData[i].name.indexOf('stock')==0){
						selfcolor.push("#8f60c2");
					}else if(selfData[i].name.indexOf('insure')==0){
						selfcolor.push("#78a288");
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
			}else{
				$(this).empty();
			}
		});
	}
	
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
	
	/************************************************ Recommended ***************************************************/
	/**
	 * bind notice
	 */
	var investorHomeAngular = angular.module('investorHome', ['truncate']);
	investorHomeAngular.controller('investorNoticeCtrl', ['$scope', '$http', function($scope, $http) {
		var noticeUrl = base_root + '/front/investor/getInvestorNotice.do?d=' + new Date().getTime();
		$http({
            url : noticeUrl,
            method :'POST',
            params : {minSize : '4'}
       	}).success(function(re){
       		 ////console.log(re);
       		 $scope.sysNotices = re.sysNotices;
       		 $scope.fundNotices = re.fundNotices;
       	});
		var reportUrl = base_root + '/front/investor/getInvestorReports.do?d=' + new Date().getTime();
		$http({
			url : reportUrl,
			method :'POST',
			params : {minSize : '4'}
		}).success(function(re){
			////console.log(re);
			$scope.reports = re.reports;
		});
		$scope.previewNotice = function(title,releaseDateTime,releaseBy,content){
			initDialogNotice();
			$('#previewNoticeDialog').addClass('dispaly-active');
			$('#previewNoticeDialog').show();
			$('.notice-dialog-title').text(title);
			$('.notice-dialog-release-date-time').text(releaseDateTime);
			$('.notice-dialog-release-by').text(releaseBy);
			if(content != 'undefined'){
				$('.dialog-notice-content').html(decodeURI(content));
			}
		};
		/**
		 * init dialog notice
		 */
		function initDialogNotice(){
			$('.notice-dialog-title').text('');
			$('.notice-dialog-release-date-time').text('');
			$('.notice-dialog-release-by').text('');
			$('.dialog-notice-content').text('');
		}
		/**
		 * dialog close
		 */
		$(document).on('click','.wmes-close,.character-button-close',function(){
			$('#previewNoticeDialog').hide();
			$('#previewNoticeDialog').removeClass('dispaly-active');
			$('#selectUserDialog').hide();
			$('#selectUserDialog').removeClass('dispaly-active');
		});
		$(document).on('click','.myzone-notice-th li',function(){
			$(this).addClass('myzone-notice-thliac').siblings().removeClass('myzone-notice-thliac');
			$('.notice-list-cut').eq($(this).index()).addClass('notice-list-cutac').siblings().removeClass('notice-list-cutac');
		});
    }]);
	investorHomeAngular.controller('investorRecommendedCtrl', ['$scope', '$http', function($scope, $http) {
		var portfoliosUrl = base_root + '/front/investor/getRecommendedPortfolios.do?d=' + new Date().getTime();
		$http({
			url : portfoliosUrl,
			method :'POST',
			params : {}
		}).success(function(re){
			////console.log(re);
			$scope.arenas = re.arenas;
		});
		var strategiesUrl = base_root + '/front/investor/getRecommendedStrategies.do?d=' + new Date().getTime();
		$http({
			url : strategiesUrl,
			method :'POST',
			params : {}
		}).success(function(re){
			////console.log(re);
			$scope.strategies = re.strategies;
			setTimeout(function(){initChar()},500);
		});
		var fundUrl = base_root + '/front/investor/getRecommendedFund.do?d=' + new Date().getTime();
		$http({
			url : fundUrl,
			method :'POST',
			params : {}
		}).success(function(re){
			////console.log(re);
			$scope.funds = re.funds;
			setTimeout(function(){initSwiper();},500);
		});
		var newsUrl = base_root + '/front/investor/getRecommendedNews.do?d=' + new Date().getTime();
		$http({
			url : newsUrl,
			method :'POST',
			params : {minSize : '2'}
		}).success(function(re){
			////console.log(re);
			$scope.news = re.news;
		});
		var insightsUrl = base_root + '/front/investor/getRecommendedInsights.do?d=' + new Date().getTime();
		$http({
			url : insightsUrl,
			method :'POST',
			params : {minSize : '2'}
		}).success(function(re){
			////console.log(re);
			$scope.insights = re.insights;
		});
		setTimeout(function(){initSwiper();},500);
	}]);
	investorHomeAngular.controller('investorHotTopicCtrl', ['$scope', '$http', function($scope, $http) {
		var hotTopicsUrl = base_root + '/front/investor/getHotTopics.do?d=' + new Date().getTime();
		$http({
			url : hotTopicsUrl,
			method :'POST',
			params : {minSize : '2'}
		}).success(function(re){
			////console.log(re);
			$scope.hotTopics = re.hotTopics;
		});
	}]);
	/**
	 * My Assets
	 */
	investorHomeAngular.controller('investorMyAssetsCtrl', ['$scope', '$http', function($scope, $http) {
		var currencyCode = $("#currencyCode").val();
		$scope.displayColor=$("#displayColor").val();
		var periodType = 'YTD';
		var period = '';
		var MyPortfolioTotalAssetsUrl = base_root + '/front/investor/getMyPortfolioTotalAssets.do?d=' + new Date().getTime();
		$http({
			url : MyPortfolioTotalAssetsUrl,
			method :'POST',
			params : {
				currencyCode:currencyCode,
				periodType:periodType,
				period:period
			}
		}).success(function(re){
			$scope.allPortfolioAssets = re.allPortfolioAssets;
			$scope.currentTotalAsset = $scope.allPortfolioAssets.totalAsset;
			$scope.currentTotalCash = $scope.allPortfolioAssets.totalCash;
			//$scope.currentAccReturn = $scope.allPortfolioAssets.totalReturn;
			$scope.currentAccReturn = $scope.allPortfolioAssets.accReturn;
			if(re.allPortfolioAssets.portfolioAssets == null){
				$('.investor-nodata-zone').show();
				$('.myzone-myassets-content').hide();
			}else{
				$('.investor-nodata-zone').hide();
				$('.myzone-myassets-content').show();
			}
			var portfolioAssets = re.allPortfolioAssets.portfolioAssets;
			mychars(portfolioAssets);
			setTimeout(function(){
				$(document).find('.myzone-myassets-choose li').eq(0).addClass('myzone-myassets-chooseac');
			},500);
			setPortfolioAssets(portfolioAssets);
		});
		/**
		 * Assets display
		 */
	
		function setPortfolioAssets(portfolioAssets){
			$scope.portfolioAssets = {};
			if(typeof portfolioAssets != 'undefined' && portfolioAssets != null && portfolioAssets.length > 0){
				$.each(portfolioAssets,function(i,n){
					$scope.portfolioAssets[n.holdId] = n;
				});
			}
		}
		$scope.selectPortfolioAssets = function($event,holdId){
			$($event.target).siblings().removeClass('myzone-myassets-chooseac').end().addClass('myzone-myassets-chooseac');
			if(typeof holdId != 'undefined'){
				var portfolioAssets = [];
				$scope.currentTotalAsset = $scope.portfolioAssets[holdId].totalAsset;
				$scope.currentTotalCash = $scope.portfolioAssets[holdId].totalCash;
				$scope.currentAccReturn = $scope.portfolioAssets[holdId].totalReturn;
				portfolioAssets.push($scope.portfolioAssets[holdId]);
				mychars(portfolioAssets);
			}else{
				$scope.currentTotalAsset = $scope.allPortfolioAssets.totalAsset;
				$scope.currentTotalCash = $scope.allPortfolioAssets.totalCash;
				$scope.currentAccReturn =$scope.allPortfolioAssets.accReturn;// $scope.allPortfolioAssets.totalReturn;
				mychars($scope.allPortfolioAssets.portfolioAssets);
			}
		}
	}]);
	investorHomeAngular.controller('myfavouritesCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.portfolioCount = 0;
		$scope.watchListTypeCount = 0;
		$scope.strategyCount = 0;
		function getMyfavourites(){
			var myfavouritesUrl = base_root + '/front/investor/getMyfavourites.do?d=' + new Date().getTime();
			$http({
				url : myfavouritesUrl,
				method :'POST'
			}).success(function(re){
				$scope.portfolio = re.portfolio;
				$scope.portfolioCount = re.portfolioCount;
				$scope.watchListType = re.watchListType;
				$scope.watchListTypeCount = re.watchListTypeCount;
				$scope.strategy = re.strategy;
				$scope.strategyCount = re.strategyCount;
				$scope.news = re.news;
				$scope.topic = re.topic;
				setTimeout(function(){initChar()},500);
			});
		}
		getMyfavourites();
		/**
		 * Cansel Favorites 
		 */
		$(document).on('click','.myzone-myfavourites-collect,.wmes-community-topic-collect',function(){
			var self = this;
			layer.confirm(props['investor.zone.alert.cancel.collection'], {
				  title:props['global.info'],
				  btn: [props['global.confirm'],props['global.cancel']]
			},function(index){
				layer.close(index);
				var favoritesId = $(self).attr('data-favorites');
				var type = $(self).attr('data-type');
				var url = base_root + '/front/investor/canselFavorites.do?d=' + new Date().getTime();
				$.ajax({
					url:url,
					type:'post',
					data:{
						favoritesId:favoritesId
					},success:function(re){
						if(re.flag){
							var count = $(self).closest('li').find('.myzone-myfavourites-tips').text();
							if(count && Number(count) > 0){
								count = Number(count)-1;
								$(self).closest('li').find('.myzone-myfavourites-tips').text(count);
							}
							refrashFavorites(type);
						}else{
							layer.msg(props['global.failed'], {icon : 1});
						}
					},error:function(){
						layer.msg(props['global.failed'], {icon : 1});
					}
				});
			});
		});
		/**
		 * Refrash Favorites
		 */
		function refrashFavorites(type){
			var url = base_root + '/front/investor/getFavoritesByType.do?d=' + new Date().getTime();
			$.ajax({
				url:url,
				type:'post',
				data:{
					type:type
				},success:function(re){
					if(re.flag){
						if('portfolio' == type){
							$scope.portfolio = re.object;
						}else if('strategy' == type){
							$scope.strategy = re.object;
							$scope.$apply();
							setTimeout(function(){initChar()},500);
						}else if('news' == type){
							$scope.news = re.object;
						}else if('topic' == type){
							$scope.topic = re.object;
						}
					}else{
						if('portfolio' == type){
							$scope.portfolio = null;
						}else if('strategy' == type){
							$scope.strategy = null;
						}else if('news' == type){
							$scope.news = null;
						}else if('topic' == type){
							$scope.topic = null;
						}
					}
					$scope.$apply();
				}
			});
		}
	}]);
	Array.prototype.max = function(){ 
		return Math.max.apply({},this) 
	} 
	Array.prototype.min = function(){ 
		return Math.min.apply({},this) 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/****************************************************************************************************************/
});