define(function(require) {
	//依赖
	var $ = require('jquery');
		require("echarts");
		require("swiper");
		require("fullcalendar");//日程控件
		require("fullcalendarlang");
		require("layui");
		require("bootstrapDialog");
		require('minicolors');
		require('laydate');
		require("interfaceCheckPwd")
		var Loading = require('loading');
		var angular = require('angular');
		//var _chat=require('chat');
		sessionStorage.removeItem("clientdetailtab");
		
		var myClient = angular.module('profitTable', ['truncate']);
		//验证登录
		if(_checkList!=undefined && _checkList!=""){
		$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
		}
		
		//var myClient = angular.module('ng').filter('profitTable', function (){});
	// var surplusHeight = $(".wmes-top").height() + 40;
	//new swiper
   var swiperIndex = 0; 
   if('schedule' == getUrlParam('type')){
	   swiperIndex = 1; 
   }
   var mySwiper = new Swiper('.ifa-home-wrap', {
        pagination: '.swiper-pagination',
        preventClicks : false,
        initialSlide : swiperIndex,
        paginationClickable: true,
        mousewheelControl : false,
        onSlideChangeStart:function(swiper){
		    var nowHeight = $(".ifa-home-contents-list").children().eq(swiper.activeIndex).find(".ifa-home-contents").height();
		    nowHeight=nowHeight>830?nowHeight:830;
        	$(".ifa-home-wrap").height(nowHeight);
		}

    });	
   
    function SetHeight(){
		var _height= 830;
		if(_height<=$(".swiper-slide-active .ifa-home-contents").height()){
			_height=$(".swiper-slide-active .ifa-home-contents").height();
		}
		//_height=_height>$(".swiper-slide-active .ifa-home-contents").height()?_height:$(".swiper-slide-active .ifa-home-contents").height();
		$(".ifa-home-wrap").height(_height+80);
	}

   	SetHeight();


	var HomeTopFive = {
		cartArr : [],
		topFiveIndex : 0,
		topFive : function(){
			$(".ifa-topfive-tab li").on("click",function(){
				$(this).siblings().removeClass("now").end().addClass("now");
				$(".ifa-topfive-content").removeClass("ifa-topfive-active").eq($(this).index()).addClass("ifa-topfive-active");
				HomeTopFive.topFiveIndex = $(this).index();
				HomeTopFive.topFiveCart();
			});
		},
		topFiveCart : function(){
			$(".ifa-home-cart").each(function(){
		        // 基于准备好的dom，初始化echarts实例
	   			 var myChart = echarts.init($(this)[0]);

		        // 指定图表的配置项和数据
		        var base = +new Date(2013, 9, 3);
				var oneDay = 24 * 3600 * 1000;
				var date = [];
					
				var data = [Math.random() * 300];
				var decima = [Math.random() * 300];
				for (var i = 1; i < 2000; i++) {
					var now = new Date(base += oneDay);
					date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'));
					data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
					decima.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
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
				    yAxis: [
					    {
					        type: 'value',
					        boundaryGap: [0, '100%']
					    },
					    {
					        type: 'value'
					    },

				    ],
				    toolbox: {
		        feature: {
		            dataZoom: {
		                yAxisIndex: 'none'
		            },
		            restore: {},
		            saveAsImage: {}
		        }
		    },
				    series: [
				        {
				            name:'chart',
				            type:'line',
				            smooth:true,
				            symbol: 'none',
				            sampling: 'average',
				            itemStyle: {
				                normal: {
				                    color: '#789fd6'
				                }
				            },
				            data: data
				        }
				    ]
				};
				HomeTopFive.cartArr.push(myChart);
		        // 使用刚指定的配置项和数据显示图表。
		        myChart.setOption(option);
			});

		},
		ifaInit : function(){
			this.topFive();
			this.topFiveCart();
			window.onresize = function () {
	        	for(var i = 0; i < HomeTopFive.cartArr.length; i++){
	        		HomeTopFive.cartArr[i].resize(); 
	        	}	
			}
		}
	}

	HomeTopFive.ifaInit();
	
	var HomeInvestors = {
		InvestorsListCon : function(){
			$(".mid-inverstor-left").on("click",".investor-list-img",function(){
				$(this).siblings(".investor-hide-chat").toggleClass("investor-show-chat");
			});
		},
		InvestorsInit : function(){
			this.InvestorsListCon();
		}
	}
	HomeInvestors.InvestorsInit();
	
	function swiperRows(){
	    $('.ifa-home-world-list').each(function(){
		    var swiper2 = new Swiper($(this), {
		        effect: 'coverflow',
		        grabCursor: true,
		        centeredSlides: true,
		        slidesPerView: 'auto',
		        initialSlide :0,
		        coverflow: {
		            rotate: 0,
		            stretch: 90,
		            depth: 100,
		            modifier: 1,
		            slideShadows : true
		        }
		    });
	    	$(".ifa-home-world-rows").on("click",function(){
		   		swiper2.slideTo($(this).index(), 300);
		   		HomeTopFive.topFiveCart();
		    });

	    });


    }
    swiperRows();
   
    $(".top5-bar-ul li").on("click",function(){
    	var self = $(this).parents(".ifa-topfive-content");
		$(this).siblings().removeClass("top5-bar-active").end().addClass("top5-bar-active");
		var WolrdRows  = self.find(".ifa-home-world-rows").eq(0).clone();
  		self.find(".ifa-home-world-rows").eq(0).remove();
  		self.find(".ifa-home-world").append(WolrdRows);
  		swiperRows();
	});
   
	//HomeTaskList.TaskListInit();
	
	/**************************************************************/
	
	
	//第二版页面
	$(".investor-chioce li p").hide();
	
	$(".investor-chioce li").on("click",function(e){
			// var number = $(this).attr('number'); 
			var isopen = $(this).attr('isopen'); 
			if(isopen=='0'){
				$(this).find('p').show('1000');$(this).attr('isopen','1');
				$(this).find('a').removeClass('investor-chioce-white').addClass('investor-chioce-button');
			}
			else if(isopen=='1'){
				$(this).find('p').hide('1000');$(this).attr('isopen','0');
				$(this).find('a').removeClass('investor-chioce-button').addClass('investor-chioce-button').addClass('investor-chioce-white');
			}else{
				$(this).find('a').toggleClass("investor-chioce-white");
			}
			e.stopPropagation(); 
			return false;
	});
	$(".mid-inverstor-left").on("mousemove",".home-investor-list-li",function(){
        $(this).find(".ifa-home-mask").stop(true).animate({ opacity: "0.9"}, 100,"linear");
	});         
    $(".mid-inverstor-left").on("mouseleave",".home-investor-list-li",function(){
    	$(this).find(".ifa-home-mask").stop(true).animate({ opacity: "0"}, 100,"linear");
    });
	var disjson = {"person":[
	{"country":"China","name":"Daivl Senter","mobile":"+08613332978987","email":"ojzil1@ctner.org.cn"},
	{"country":"America","name":"Als Wenger","mobile":"+022134089787","email":"Wenger@ctner.org.cn"},
	{"country":"France","name":"Altetar Ta","mobile":"+02213408228987","email":"lovefamilty@ctner.org.cn"},
	{"country":"Britain","name":"Davil Owen","mobile":"+4413408978987","email":"owen@ctner.org.cn"},
	{"country":"Germany","name":"Senxcens Alex","mobile":"+32408978987","email":"alex@ctner.org.cn"},
	{"country":"Italy","name":"K.D.Lww","mobile":"+02313408978987","email":"K.D.Lww@ctner.org.cn"},
	{"country":"China","name":"Belilin","mobile":"+2313408978987","email":"Belilin@ctner.org.cn"},
	{"country":"America","name":"Motersaker","mobile":"+2313408978987","email":"Motersaker@ctner.org.cn"},
	{"country":"China","name":"Maxier SX","mobile":"+2313408978987","email":"Maxier@ctner.org.cn"},
	{"country":"France","name":"Ronaertior","mobile":"+2313408978987","email":"Ronaertior@ctner.org.cn"}],
	"success":true,"type":"1","total":"12"};
	
	$(".investor-chioce li p").on("click",function(e){
		genInvestor();
		$(this).addClass('p-selected').siblings().removeClass('p-selected');
		e.stopPropagation(); 
	});
	
	$(".investor-chioce li p .investor-choice-x").on("click",function(e){ 
		genInvestor();
		$(this).parent('p').removeClass('p-selected');
		e.stopPropagation(); 
	});

	
	// $(".home-acc-wrap .page_left").on("click",function(){
	//   	var self = $(this).parents(".ifa-topfive-content"),
	//   		selfUl = self.find(".top5-bar-ul"),
	//   		selfIndex = selfUl.find(".top5-bar-active").index();
	//   	if(selfIndex == 0 ){
	//   		selfIndex = selfUl.children().length - 1;
	//   	}else{
	//   		selfIndex -=1
	//   	}
	//   	selfUl.children().removeClass("top5-bar-active").eq(selfIndex).addClass("top5-bar-active");
	//   	var noNum = selfUl.find(".top5-bar-active").attr("data-acc"),
	//   		noName = selfUl.find(".top5-bar-active").attr("data-name"),
	//   		noTitle = selfUl.find(".top5-bar-active").attr("data-title");
	//   		HomeTopFive.topFiveCart();
	//   		self
	//   		.find(".home-acc-btitle").html(selfIndex + 1)
	//   		.end()
	//   		.find(".home-new-percentage").html(noNum)
	//   		.end()
	//   		.find(".home-portfolio-name").html(noName)
	//   		.end()
	//   		.find(".home-portfolio-name").html(noTitle);
	//   		var WolrdRows  = self.find(".ifa-home-world-rows").eq(0).clone();
	//   		self.find(".ifa-home-world-rows").eq(0).remove();
	//   		self.find(".ifa-home-cart-world").append(WolrdRows);
	// });
	// $(".home-acc-wrap .page_right").on("click",function(){
	//   	var self = $(this).parents(".ifa-topfive-content"),
	//   		selfUl = self.find(".top5-bar-ul"),
	//   		selfIndex = selfUl.find(".top5-bar-active").index();
	//   	if(selfIndex >= selfUl.children().length - 1 ){
	//   		selfIndex = 0;
	//   	}else{
	//   		selfIndex += 1
	//   	}
	//   	selfUl.children().removeClass("top5-bar-active").eq(selfIndex).addClass("top5-bar-active");
	//   	var noNum = selfUl.find(".top5-bar-active").attr("data-acc"),
	//   		noName = selfUl.find(".top5-bar-active").attr("data-name"),
	//   		noTitle = selfUl.find(".top5-bar-active").attr("data-title");
	//   		HomeTopFive.topFiveCart();
	//   		self
	//   		.find(".home-acc-btitle").html(selfIndex + 1)
	//   		.end()
	//   		.find(".home-new-percentage").html(noNum)
	//   		.end()
	//   		.find(".home-portfolio-title").html(noName)
	//   		.end()
	//   		.find(".home-portfolio-name").html(noTitle);
	//   		var WolrdRows  = self.find(".ifa-home-world-rows").eq(0).clone();
	//   		self.find(".ifa-home-world-rows").eq(0).remove();
	//   		self.find(".ifa-home-cart-world").append(WolrdRows);

	// });
	//dom切换
	//打开页面时加载
	genInvestor();
	
	 //加载6条数据
    function genInvestor(){
    	var list = disjson.person;
		var html = '';
		$(".home-investor-list").empty();
    	$.each(list,function(i, n) {
			var rand = parseInt(Math.random()*6,10);
			n = list[rand];
			if(i>5)return;
			var li = '<li class="home-investor-list-li">';
                li += '<a href="'+ base_root +'/front/strategy/info/clientDetail.do"><p class="investor-list-name">'+n.name+'</p>';
                li += '<div class="investor-iof-wrap">';
                li += '<p class="investor-list-title">';
                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/ifa_phone_ico.png" alt="">';
                li += '<span class="investor-list-ifo">'+n.mobile+'</span>';
                li += '</p>';
                li += '<p class="investor-list-title">';
                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/ifa_email_ico.png" alt="">';
                li += '<span class="investor-list-ifo">'+n.email+'</span>';
                li += '</p>';
                li += '<div class="investor-chat-wrap">';
                li += '<img class="investor-list-img" src="'+base_root+'/res/images/ifa/investor_liaotian.png" alt="">';
                li += '<div class="investor-hide-chat">';
                li += '<img src="'+base_root+'/res/images/ifa/investor_wetchat.png" alt="">';
                li += '<img src="'+base_root+'/res/images/ifa/investor_phone.png" alt="">';
                li += '<img src="'+base_root+'/res/images/ifa/investor_line.png" alt="">';
                li += '</div>';
                li += '</div>';
                li += '</div>';  
                li += '<ul class="ifa-home-mask">';   
                li += '<li>Same Language</li>'; 
                li += '<li>Same Inv. Style</li>'; 
                li += '<li>'+ n.country +'</li>'; 
                li += '<li>Read My Insights</li>'; 
                li += '<li>No IFA yet</li>'; 
                li += '</ul>';   
                li += '</a></li>';
			html += li;
    	});
    	html = '<div type="portfolios"  class="page_left"></div>'+html;
    	html= html +'<div type="portfolios"  class="page_right" ></div>';
    	$('.home-investor-list').append(html).hide().fadeToggle(500);//.show('1000');
    }
	
  //点击左时
    $(".home-investor-list").on('click', '.page_left', '', function () {
    	genInvestor();
    	
    });
    
    $(".home-investor-list").on('click', '.page_right', '', function () {
    	genInvestor();
    });

	$("#fullcalendar-show").on("click",function(){
		$(".tasklist-date-choice p:eq(0)").click();
		$('#hidPage').val(1);
		$('#schedule_more').hide();
		$(this).toggleClass("take_list_contents");
		$(".line-wrap").toggleClass("tasklist-hide");
		$(".ifa-home-calendar").toggleClass("tasklist-show");
		//刷新数据
		bindTaskList();
		if($(".ifa-home-calendar").hasClass('tasklist-show')){
			$('.wmes-nodata-tips-tasklist').hide();
			$(".tasklist-date-choice").hide();
		}else{
			$(".tasklist-date-choice").show();
		}
		$('.fc-today-button').click();
		
		//触发点击
		$('.fc-day').click(function(){
			var startDate = $(this).attr('data-date');
			$('.ifa-home-calendar').fullCalendar( 'select', startDate, '', true );
		});

		SetHeight();
	});
	
/****************************************Task List *********************************************/
	//TaskList 事件源
	var TaskListEventData =(function(){
		//总事件
		var events = [];
		//TaskList
		var taskListEvents = [];
		//日程
		var scheduleEvents = [];
		return{
			events:events,
			taskListEvents:taskListEvents,
			scheduleEvents:scheduleEvents
		};
	})();
	var scheduleLoading = new Loading($(document).find('.schedule-task-list-loading'));
	scheduleLoading.show();
	var HomeTaskList = {
			lineMove : function(){
				$(".line-wrap-a").on("mousemove",function(){
		            var self = $(this);
		            self.find(".line-topline").stop(true).animate({ width: (self.width() - 26 ) + 'px'}, 100 ,function(){
		                self.find(".line-rightline").stop(true).animate({ height: (self.height() - 13 + 30) + 'px'}, 100 ,function(){
		                    self.find(".line-bottomline").stop(true).animate({ width: (self.width() - 13 ) + 'px'}, 100 );
		                });
		            });
		        });
		        $(".line-wrap-a").on("mouseleave",function(){
		            var self = $(this);
		            self.find(".line-bottomline").stop(true).animate({ width: "0%"}, 100 ,function(){
		                self.find(".line-rightline").stop(true).animate({ height: "0%"}, 100 ,function(){
		                    self.find(".line-topline").stop(true).animate({ width: "0%"}, 100 );
		                });
		            });
		        });
			},
			lineFun : function(){
				$(".tasklist-date-choice p").unbind('click');
				$(".tasklist-date-choice p").on("click",function(){
					if('startDate' == $(this).data('sort')){
						$('.tasklist-date-choice').attr('style','margin-left:-10%');
						//$('.tasklist-events-recurrent').show();
						$('.tasklist-events,.tasklist-metting,.tasklist-date-ico').show();
					}else if('createDate' == $(this).data('sort')){
						$('.tasklist-date-choice').attr('style','margin-left:0%');
						//$('.tasklist-events-recurrent').hide();
						$('.tasklist-events,.tasklist-metting,.tasklist-date-ico').hide();
					}
					$(this).siblings().removeClass("date-active").end().addClass("date-active");
//					var selt = this;
					initScheduleList();
					SetHeight();
				});
				$(".tasklist-metting-title").on("click",function(){
					$(this).toggleClass("tasklist-metting-show-choice");
				});
				$(".line-ball").on("click",function(){
					if('createDate' == $('.tasklist-date-choice .date-active').data('sort')){
						return;
					}
					if($(this).parents(".line-wrap-a").hasClass("line-wrap-show")){
						$(this).parents(".line-wrap-a").removeClass('line-wrap-show');
					}else{
						$(".line-wrap-a").removeClass('line-wrap-show');
						$(this).parents(".line-wrap-a").addClass("line-wrap-show");
						var ifImportant = $(this).parent().find('.line-word').attr('data-if-important');
						if(ifImportant == '1'){
							$(this).parent().find('#checkImportant').attr('checked','checked');
						}
					}
				});
			},
			TaskListInit : function(){
				this.lineMove();
				this.lineFun();
			}
		};
	//获取日程数据
	function bindSchedule(){
		var groupId = '',
			order = 'ASC',
			sort = '',
		 	eventType = '',
		 	ifImportant = '';
		$(".tasklist-date-choice p").each(function(){
			if($(this).hasClass('date-active')){
				sort = $(this).data('sort');
				return false;
			}
		});
		if($('#events-checkbox').prop('checked')){
			//eventType = '1';
		}else{
			eventType = '0';
		}
		if($('#important-events-checkbox').prop('checked')){
			ifImportant = '1';
		}else{
			ifImportant = '';
		}
		groupId = $('.tasklist-metting-choice').attr('data-group-id');
		var unclassified = '';
		if(typeof(groupId) == 'undefined'){
			groupId = '';
		}else if(groupId == 'unclassified'){
			unclassified = '1';
			groupId = '';
		}
		$.ajax({
			type:'post',
			data:
			{
				sort:sort,
				order:order,
				unclassified:unclassified,
				groupId:groupId,
				ifImportant:ifImportant,
				eventType:eventType
			},
			url: base_root+'/front/crm/schedule/getCRMSchedule.do?dateStr=' + new Date().getTime(),
			success:function(result){
				if(result.crmScheduleJson!=null){
					var events = eval('('+result.crmScheduleJson+')');
					TaskListEventData.scheduleEvents = [];
					//初始化日程数据源
					$.each(events,function(index,n){
						TaskListEventData.scheduleEvents.push({ 
							dataType: 'S',//代表日程数据 
					 		id: n.id, 
					 		color: n.color == null?'':n.color, 
					 		content: n.content == null?'':n.content, 
				            title: n.title == null?'':n.title,   
				            start: n.start == null?'':n.start, 
		            		create: n.createTime == null?'':n.createTime, 
				            repeatTo: n.repeatTo == null?'':n.repeatTo,
				            repeatFrom: n.repeatFrom == null?'':n.repeatFrom,
				            groupId: n.groupId == null?'':n.groupId,
				            groupName: n.groupName == null?'':n.groupName,
				            repeatDay: n.repeatDay == null?'':n.repeatDay,
				            end: n.end == null?'':n.end,
				            remindSetting: n.remindSetting == null?'':n.remindSetting,
		            		remindSettingName: n.remindSettingName == null?'':n.remindSettingName,
				            eventType: n.eventType == null?'':n.eventType, 
		            		customerId: n.customerId == null?'':n.customerId, 
		            		customerName: n.customerName == null?'':n.customerName,
            				ifImportant:n.ifImportant == null?'':n.ifImportant,
    						creatorName:n.creatorName == null?'':n.creatorName
				        });
					});
					//排序
					TaskListEventData.scheduleEvents= TaskListEventData.scheduleEvents.sort(function(a, b){
						var aDate = '';
						var bDate = '';
						$(".tasklist-date-choice p").each(function(){
							if($(this).hasClass('date-active') && 'startDate' == $(this).data('sort')){
								if(a.start.indexOf('T')){
									aDate = (a.start.split('T'))[0];
								}
								if(b.start.indexOf('T')){
									bDate = (b.start.split('T'))[0];
								}
							}else if($(this).hasClass('date-active') && 'createDate' == $(this).data('sort')){
								aDate = a.create;
								bDate = b.create;
							}
						});
						aDate = new Date(aDate);
						bDate = new Date(bDate);
						return aDate-bDate;	
					});
					// 初始化列表视图
					initScheduleList();
					// 初始化日程视图
					initFullcalendar();
					// 刷新视图
					refashView();
				}
			}
		});
	}
	
    $('#cheRecurrentFlag').change(function(){
    	if($('#cheRecurrentFlag')[0].checked){
    		$('.repeat_typecon').slideDown('fast');
    	}else{
    		$('.repeat_typecon').slideUp('fast');
    	}
    });
	
	//获取TaskList数据
	function bindTaskList(){
		var recurentFlag = '';
		var ifImportant = '';
		/*if($('#important-events-checkbox').prop('checked')){
			ifImportant = '1';
		}else{
			ifImportant = '';
		}*/
		var url = base_root+'/front/web/taskList/getTaskList.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			data:{recurentFlag:recurentFlag,ifImportant:ifImportant},
			url: url,
			success:function(result){
				////console.log(result);
				if(result.flag){
					TaskListEventData.taskListEvents = [];
					//初始化TaskList数据源
					$.each(result.webTaskLists,function(i,n){
						var title = n.title == null ? '' : n.title;
						var taskUrl = n.fullUrl == null ? '' : n.fullUrl;
						TaskListEventData.taskListEvents.push({ 
							dataType: 'T',//代表Task List数据 
					 		id: n.id, 
					 		start: n.createDate == null?'':n.createDate, 
			 				create: n.createDate == null?'':n.createDate, 
				            title: title,
				            taskUrl: taskUrl,
				            ifImportant: n.ifImportant,
				            
					 		color: n.color, 
					 		content: '', 
				            repeatTo: '',
				            repeatFrom: '',
				            groupId: '',
				            groupName: '',
				            repeatDay:'',
				            end: '',
				            remindSetting:'',
				            eventType:'' 
				        });
					});
					//排序
					TaskListEventData.taskListEvents= TaskListEventData.taskListEvents.sort(function(a, b){
						var aDate = '';
						var bDate = '';
						$(".tasklist-date-choice p").each(function(){
							if($(this).hasClass('date-active') && 'startDate' == $(this).data('sort')){
								if(a.start.indexOf('T')){
									aDate = (a.start.split('T'))[0];
								}
								if(b.start.indexOf('T')){
									bDate = (b.start.split('T'))[0];
								}
							}else if($(this).hasClass('date-active') && 'createDate' == $(this).data('sort')){
								aDate = a.create;
								bDate = b.create;
							}
						});
						aDate = new Date(aDate);
						bDate = new Date(bDate);
						return aDate-bDate;	
						//return bDate-aDate;	
					});
					// 日程数据获取
					bindSchedule();
				}
			}
		});
	}
	
	function limitData(page,rows){
		var total = '';
		if(typeof(page) == 'undefined' || page == ''){
			page = 1;
		}
		if(typeof(rows) == 'undefined' || rows == ''){
			rows = 10;
		}
		total = Number(page)*Number(rows);
		return total;
	}
	//组装任务列表视图数据
	function initScheduleList(){
		var htmlRight = '';
		var htmlLeft = '';
		$(".tasklist-date-choice p").each(function(){
			if($(this).hasClass('date-active') && 'createDate' == $(this).data('sort')){
				TaskListEventData.events = TaskListEventData.taskListEvents;
			}else if($(this).hasClass('date-active') && 'startDate' == $(this).data('sort')){
				TaskListEventData.events = TaskListEventData.scheduleEvents;
			}
		});
		if((typeof TaskListEventData.events == 'undefined' || TaskListEventData.events.length==0)
				&& !$('#fullcalendar-show').hasClass('take_list_date take_list_contents')){
			$('.wmes-nodata-tips-tasklist').show();
		}else{
			$('.wmes-nodata-tips-tasklist').hide();
		}
		var index_ = 0;
		$.each(TaskListEventData.events,function(index,n){
			var treeRight = '';
			var treeLeft = '';
			var dataType = n.dataType;
			var title = '';
			var start = '';
			var color = '';
			var taskUrl = '';
			var content = '';
			var ifImportant = n.ifImportant;
			if('S' == dataType){//日程
				title = n.title;
				start = n.start;
				var startStr = '';
				if(start.indexOf('T')){
					startStr = (start.split('T'))[0];
					startStr = startStr.replace(/-/g,'');
					/*var tempDate = (new Date().toISOString().split('T'))[0];
					tempDate = tempDate.replace(/-/g,'');
					// 过期日期判断
					if(Number(startStr)<Number(tempDate)){
						return;
					}*/
				}else{
					startStr = start.replace(/-/g,'');
					/*var tempDate = (new Date().toISOString().split('T'))[0];
					tempDate = tempDate.replace(/-/g,'');
					// 过期日期判断
					if(Number(startStr)<Number(tempDate)){
						return;
					}*/
				}
				color = n.color;
				content = n.content;
				$(".tasklist-date-choice p").each(function(){
					if($(this).hasClass('date-active') && 'createDate' == $(this).data('sort')){
						start = n.create;
					}else{
						if(start.indexOf('T')){
							start = (start.split('T'))[0];
						}
					}
				});
			}else if('T' == dataType){//task list
				taskUrl = n.taskUrl;
				title = n.title;
				start = n.start;
				color = n.color;
			}
			var subTitle = '';
			if(title.length>10){
				subTitle = title.substring(0,10)+'...';
			}else{
				subTitle = title;
			}
			var comm=
			   '<div class="line-topline"></div>'
			   +'<div class="line-rightline"></div>'
			   +'<div class="line-bottomline"></div>'
			   +'<div class="line-ball task-list-ball" data-schedule="'+n.id+'" style="background-color:'+color+'"></div>'
		       +'<div class="line-title-line"></div>'
		       +'<div class="line-title">'
		        +'<p class="line-date">'+start+'</p>'
		        +'<p class="line-l">|</p>'
		        +'<p class="line-word" title="'+title+'" data-if-important="'+ifImportant+'" data-task-url="'+ base_root + taskUrl +'" style="color:#49b0c7;">'+subTitle+'</p>'
		       +'</div>'
			+'<div class="line-for" style="color: #ab2e2e;">'
    		+'<p>'+content+'</p>'
    		+'</div>'
    		+'<ul class="line-add-label line-add-label-group"></ul>';
			if(0 == index%2){//R
				if (ifImportant == 1){
	    			treeRight += '<a id="line-wrap-right-2" style="margin-bottom: 60px;" class="line-wrap-a line-wrap-a-active" href="javascript:void(0);">';
		    	}else{
					treeRight += '<a id="line-wrap-right-2" style="margin-bottom: 60px;" class="line-wrap-a" href="javascript:void(0);">';
	    		}
				treeRight += comm;
			    treeRight += '</a>';
			    htmlRight += treeRight; 
			}else{//L
				if (ifImportant == 1){
	    			treeLeft +='<a id="line-wrap-left-1" class="line-wrap-a line-wrap-a-active" href="javascript:void(0);">';
		    	}else{
					treeLeft +='<a id="line-wrap-left-1" class="line-wrap-a" href="javascript:void(0);">';
	    		}
				treeLeft += comm;
			    treeLeft += '</a>';
			    htmlLeft +=	treeLeft;  
			}
			// 分页数据
			var page_ = $('#hidPage').val();
			var rows_ = $('#hidRows').val();
			var tatol_ = limitData(page_,rows_)-2;
			if(typeof(tatol_) != 'undefined' && index_ > tatol_){
				if($(document).find('.take_list_contents').length == 0){
					$('#schedule_more').show();
				}else{
					$('#schedule_more').hide();
				}
				return false;
			}else if(tatol_ >= TaskListEventData.events.length-2){
				$('#schedule_more').hide();
			}
			index_++;
		});
		if(htmlLeft == '' && htmlRight == ''){
			$('#schedule-view').empty();
			if($('.take_list_contents').length<1){
				$('.wmes-nodata-tips-tasklist').show();
			}
		}else{
			$('.wmes-nodata-tips-tasklist').hide();
			$('#schedule-view').empty().append('<div class="line-wrap" style="display:block;"><div class="line-wrap-right">'+htmlRight+'</div><div class="line-wrap-left">'+htmlLeft+'</div></div>');
		}
		scheduleLoading.hide();
		$('.line-word').unbind('click');
		$('.line-word').on('click',function(){
			if($('.tasklist-date-choice p:eq(0)').hasClass('date-active')){
				var eventId = $(this).closest('a').find('.line-ball').data('schedule');
				if(eventId && typeof eventId != 'undefiend'){
					showSchedule(eventId);
				}
			}else if($('.tasklist-date-choice p:eq(1)').hasClass('date-active') && $(this).data('taskUrl') != ''){
				window.open($(this).data('taskUrl'));
			}
		});
		bindGroup();
		HomeTaskList.TaskListInit();
		SetHeight();
	}
	function initDate(start,end){
		$('#txtStartAt').unbind('click');
		$('#txtStartAt').click(function(){startAt(start.replace('T',' '),'');});
		function startAt(min,max){
			if((typeof(max)=='undefined' || max == '') && !$('#txtEndAt').val()){
				max = '2099-12-30 18:00:00';
			}else if(typeof(max)=='undefined' || max == ''){
				max = $('#txtEndAt').val();
			}
			/*if(typeof(min)=='undefined' || min == ''){
				min = laydate.now()+' '+'09:00:00';
				$('#txtStartAt').val(min);
			}*/
			var istime = true;
			/*if(true == $('#cheRecurrentFlag')[0].checked){
				istime = false;
			}*/
			laydate({
				istime: istime,
				min:'0001-01-01 00:00:00',
				//max:max,
				istoday: false,
				isclear: false,
				elem: '#txtStartAt',
				format: 'YYYY-MM-DD hh:mm:ss',
				choose:function(){
					if($('#txtStartAt').val().length<1){
						$('.alert-schedule-start-at').show();
					}else{
						$('.alert-schedule-start-at').hide();
						if($('#txtStartAt').val().indexOf(' ')>-1){
							var value = endTimeAutoIncrease($('#txtStartAt').val());
							$('#txtEndAt').val(value);
						}
					}
				}
			});
		}
		$('#txtEndAt').unbind('click');
		$('#txtEndAt').click(function(){endAt('','');});
		function endAt(min,max){
			if((typeof(min)=='undefined' || min == '') && !$('#txtStartAt').val()){
				min = laydate.now();
			}else if(typeof(min)=='undefined' || min == ''){
				min = $('#txtStartAt').val();
			}
			if(typeof(max)=='undefined' || max == ''){
				max = '2099-12-30 00:00:00';
			}
			/*var istime = true;
			if(true == $('#cheRecurrentFlag')[0].checked){
				istime = false;
			}*/
			laydate({
				istime: true,
				min:min,
				max:'2099-12-30 00:00:00',
				isclear: false,
				istoday:false,
				elem: '#txtEndAt',
				format: 'YYYY-MM-DD hh:mm:hh',
				choose:function(){
					if($('#txtEndAt').val().length<1){
						$('.alert-schedule-end-at').show();
					}else{
						$('.alert-schedule-end-at').hide();
					}
				}
			});
		}
	}
	
	//组装日历视图数据
	function initFullcalendar() {
		var initLocale = '';
		if('tc'==lang){
			initLocale = 'zh-tw';
		}else if('sc'==lang){
			initLocale = 'zh-cn';
		}else{
			initLocale = '';
		}
//		var url =base_root+'/front/crm/schedule/detail.do?dateStr=' + new Date().getTime();
		$('.ifa-home-calendar').fullCalendar({
			header : {
				left : 'prev,next today',
				center : 'title',
				right : 'month,agendaWeek,agendaDay'
			},
			year: new Date().getFullYear(),   
        	month: new Date().getMonth(),   
       		day: new Date().getDate(),   
       		defaultView: 'month',
       		/*theme: true,
       		buttonIcons: 
			{
				prev: 'glyphicon glyphicon-chevron-left',
				next: 'glyphicon glyphicon-chevron-right'
			},*/
       		locale: initLocale,
			navLinks : true, 
			editable : true,
			eventLimit : true, 
			selectHelper:true,
			events : TaskListEventData.scheduleEvents,  //配置事件源
			//新增事件
			selectable: true,
			selectHelper: true,
			select: function(start, end) {
				$('.schedule-add-title').show();
				$('.schedule-edit-title').hide();
				clearForm('.register_table');
				$('.regiter_xiala_ul').hide();
				$('.repeat_typecon').hide();
				$('.add_schedule_delete').hide();
				$('.alert-schedule-title-name').hide();
				$('#hidScheduleId').val('');
				$('#txtCustomer').removeAttr('data-value');
				$('#txtGroup').removeAttr('data-value');
				$('#txtRemind').removeAttr('data-value');
				var week = new Date().getDay();
				$('#repeatDay-' + week).prop('checked','checked');
				$('#add_Schedule').show();
				$('#add_Schedule .space-mask').css('margin-top',$(window).scrollTop());
				if($('.fc-month-button').hasClass('fc-state-active')){
					// 时间控制
					$('#txtStartAt').val((start._d.toISOString().split('T'))[0]+' '+getTimeStr());
					$('#txtEndAt').val(endTimeAutoIncrease($('#txtStartAt').val()));
					//$('#txtEndAt').val((start._d.toISOString().split('T'))[0]+' '+getTimeStr('h','1'));
				}else{
					var startTime = (start._d.toISOString().split('.'))[0];
					var endTime = (end._d.toISOString().split('.'))[0];
					// 时间控制
					$('#txtStartAt').val((start._d.toISOString().split('T'))[0]+' '+(startTime.split('T'))[1]);
					$('#txtEndAt').val((start._d.toISOString().split('T'))[0]+' '+(endTime.split('T'))[1]);
				}
				$('.schedule-dialog-creator').hide();
				/*$('#selCustomer li:gt(0)').each(function(){
					$(this).click();
				});*/
				// 字数控制
				titleLength();
				contentLength();
				initDate(start._d.toISOString(),end._d.toISOString());
			},
			//编辑事件
			eventClick: function(calEvent, jsEvent, view) {
				$('.schedule-add-title').hide();
				$('.schedule-edit-title').show();
				clearForm('.register_table');
				$('.alert-schedule-title-name').hide();
				$('.regiter_xiala_ul').hide();
				$('#add_Schedule').show();
				$('#add_Schedule .space-mask').css('margin-top',$(window).scrollTop());
				if(calEvent.id != null && calEvent.id != ''){
					$('#add_schedule_delete').show();
				}
				$('#hidScheduleId').val(calEvent.id);
				$('#txtTitle').val(calEvent.title);
				if(calEvent.eventType=='1'){
					$("#cheRecurrentFlag").prop("checked",true);
					$('.repeat_typecon').show();
					if(calEvent.repeatFrom!=''){
						$('#txtStartAt').val(calEvent.repeatFrom.replace('T',' '));
					}
					if(calEvent.repeatTo!=''){
						$('#txtEndAt').val(calEvent.repeatTo.replace('T',' '));
					}
				}else{
					$("#cheRecurrentFlag").prop("checked",false);
					$('.repeat_typecon').hide();
					$('#txtStartAt').val((calEvent.start._d.toISOString().replace('T',' ').split('.'))[0]);
					if(calEvent.end!=null){
						$('#txtEndAt').val((calEvent.end._d.toISOString().replace('T',' ').split('.'))[0]);
					}else{
						$('#txtEndAt').val((calEvent.start._d.toISOString().replace('T',' ').split('.'))[0]);
					}
					var week = new Date().getDay();
					$('#repeatDay-' + week).prop('checked', true);
				}
				if(calEvent.ifImportant=='1'){
					$("#cheImportantFlag").prop("checked",true);
				}else{
					$("#cheImportantFlag").prop("checked",false);
				}
				$('#txtCustomer').val(calEvent.customerName);
				$('#txtCustomer').attr('data-value',calEvent.customerId);
				$('#txtGroup').val(calEvent.groupName);
				$('#txtGroup').attr('data-value',calEvent.groupId);
				$('#txtRemind').val(calEvent.remindSettingName);
				$('#txtRemind').attr('data-value',calEvent.remindSetting);
				$('#txtContent').val(calEvent.content);
				if(calEvent.repeatDay != ''){
					for ( var int = 0; int < calEvent.repeatDay.length; int++) {
						if(calEvent.repeatDay.charAt(int) == '1'){
							$("#repeatDay-"+int).prop("checked",true);
						}
					}
				}
				$('.schedule-dialog-creator').show();
				$('#schedule-creator-text').text(calEvent.creatorName);
				// 字数控制
				titleLength();
				contentLength();
				// 初始化时间
				var initStartTime = '';
				var initEndTime = '';
				if(calEvent.start != null){
					initStartTime = calEvent.start._d.toISOString();
				}
				if(calEvent.end != null){
					initEndTime = calEvent.end._d.toISOString();
				}
				initDate(initStartTime,initEndTime);
            },
             //事件拖曳停止
			 eventDrop: function(calEvent, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view){
             	//拖曳停止后，更新时间
            	eventDragUpdate(calEvent);
             }
		});
	}
	
	//保存与更新
	function saveAndUpdate(dialogItself){
		var id = $('#txtId').val();
		//取eventType值
       	var eventType = '';
       	if(true == $('#cheRecurrentFlag')[0].checked)
       		eventType = '1';
       	else eventType = '0';
       	//取repeat值
       	var repeatDay ='';
       	$.each($('input[name=repeatDay]'),function(index,n){
       		repeatDay += n.value;
       	});
       	if(eventType == '1' && '0000000' == repeatDay){
       		repeatDay = '1111111';
    	}else if(eventType == '0'){
       		repeatDay = '0000000';
       	}
       	var group = $('#selGroup').val();
       	var repeatFrom = $('#txtStartAt').val();
       	var repeatTo = $('#txtEndAt').val();
       	var title = $('#txtTitle').val();
       	var startAt = $('#txtStartAt').val();
       	var customerId = $('#selCustomer').val();
       	if(customerId == null){
       		customerId = $('#txtCustomer').data('value');//客户Id
       	}
       	var endAt = $('#txtEndAt').val();
    	//检验非空
       	if(!title){
       		layer.msg(prop['schedule.alert.empty.title'], {icon : 3});		
       		return false;	
       	}
       	if(!startAt){
       		layer.msg(prop['schedule.alert.empty.startTime'], {icon : 3});		
       		return false;	
       	}
       	if(startAt.indexOf(' ') > -1){
       		var startDate = (startAt.split(' '))[0];
       		var startTime = (startAt.split(' '))[1];
       	}
       	if(endAt.indexOf(' ') > -1){
       		var endDate = (endAt.split(' '))[0];
       		var endTime = (endAt.split(' '))[1];
       	}
       	var color = $('#txtColor').val();
       	var content = $('#txtContent').val();
       	//remind Setting
       	var remindSetting = $('#selRemindSetting').val();
       	/*if(remindSetting!=''&&remindSetting!=0){
       	 	$.each($('input[name=\'remindSetting\']'),function(){
       	 		if($(this).prop('checked')){
       	 			remindSetting += $(this).val();
       	 		}
           	});
       	}*/
    	var ifImportant = '';
       	if($('#cheImportantFlag')[0].checked){
       		ifImportant = '1';
       	}else{
       		ifImportant = '0';
       	}
       	var eventData = 
        {
        	start:startAt,end:endAt,//start,end必需
			id:id,eventType:eventType,title:title,
			
			strStartDate:startDate,strEndDate:endDate,
			strStartTime:startTime,strEndTime:endTime,
			repeatDay:repeatDay,groupId:group,
			strRepeatFrom:repeatFrom,strRepeatTo:repeatTo,
			
			color:color,content:content,
			customerId:customerId,
			ifImportant:ifImportant,
			
			remindSetting:remindSetting
		};
	    $.ajax({
			type:'post',
			//async:false,
			url:base_root+'/front/crm/schedule/updateSchedule.do?dateStr=' + new Date().getTime(),
			data:eventData,
			success:function(result)
			{
				if (result.flag) {
					layer.msg(globalProp['global.success.save'], {icon : 2});
					dialogItself.close();
				} else {
					layer.msg(globalProp['global.failed.save'], {icon : 1});
					dialogItself.close();
				}
			},
			error:function(){
				layer.msg(globalProp['global.failed.save'], {icon : 1});
				dialogItself.close();
			}
	    });
	    return eventData;
	}
	
//	function loadScroll(){
//		$('.create_groupbox').css('top',$(document).height()*0.40+$(window).scrollTop());
//	}
	
	function getTimeStr(a,b,c){
		var hStr = new Date().getHours();
		var mStr = new Date().getMinutes();
		var sStr = new Date().getSeconds();
		if(a=='h'){
			if(c=='-')hStr = hStr-Number(b);
			else hStr = hStr+Number(b);
		}else if(a=='m'){
			if(c=='-')mStr = mStr-Number(b);
			else mStr = mStr+Number(b);
		}else if(a=='s'){
			if(c=='-')sStr = mStr-Number(b);
			else sStr = mStr+Number(b);
		}
		if(hStr.toString().length==1)hStr = '0'+hStr;
		if(mStr.toString().length==1)mStr = '0'+mStr;
		if(sStr.toString().length==1)sStr = '0'+sStr;
		return hStr+':'+mStr+':'+sStr;
	}
	
	//删除
	function deleteSchedule(){
		var id = $('#hidScheduleId').val();
	    $.ajax({
			type:'post',
			//async:false,
			url:base_root+'/front/crm/schedule/deleteSchedule.do?dateStr=' + new Date().getTime(),
			data:{id:id},
			success:function(result){ 
				if (result.flag) {
					layer.msg(globalProp['global.delete.success'], {icon : 2});
				} else {
					layer.msg(globalProp['global.delete.failed'], {icon : 1});
				}
				//刷新数据
				bindTaskList();
			}
		});
		return id;
	}

	//拖曳后更新日程时间
	function eventDragUpdate(calEvent){
		var dragStop_StartDate = null != calEvent.start ? calEvent.start._d.toISOString():'';
       	var dragStop_EndDate = null != calEvent.end ? calEvent.end._d.toISOString():'';
       	if(calEvent.start._i != dragStop_StartDate || calEvent.start._i != dragStop_StartDate){
       		 var startDate;
       		 var endDate;
       		 var startTime;
       		 var endTime;
       		 if(dragStop_StartDate.indexOf('T') > -1){
       		 	var dateAndTime = dragStop_StartDate.split('T');
       		 	startDate = dateAndTime[0];
       		 	startTime = dateAndTime[1];
       		 }else{
       		 	startDate = dragStop_StartDate;
       		 }
       		 if(dragStop_EndDate.indexOf('T') > -1){
       		 	//var dateAndTime = dragStop_EndDate.split('T');
       		 	endDate = dateAndTime[0];
       		 	endTime = dateAndTime[1];
       		 }else{
       		 	endDate = dragStop_EndDate;
       		 }
       		 var eventData = 
        		 {
        		 	start:dragStop_StartDate,end:dragStop_EndDate ,//必需
        		 	strStartDate:startDate,strStartDate:startDate,
        		 	strEndDate:endDate,strEndDate:endDate,
        		 	strStartTime:startTime,strEndTime:endTime,
        		 	id:calEvent._id,color:calEvent.color,
        		 	content:calEvent.content,color:calEvent.color,
        		 	repeatDay:calEvent.repeatDay,
        		 	groupId:calEvent.groupId,
        			strRepeatFrom:calEvent.repeatFrom,strRepeatTo:calEvent.repeatTo,
        		 	title:calEvent.title,eventType:calEvent.eventType
        		 };
       		 $.ajax({
				type:'post',
				url: base_root+'/front/crm/schedule/updateSchedule.do?dateStr=' + new Date().getTime(),
				data:eventData,
				success:function(result)
				{
					if (result.flag) {
						layer.msg(globalProp['global.success.save'], {icon : 2});
					} else {
						layer.msg(globalProp['global.failed.save'], {icon : 1});
					}
				}
		     });
       	}
	}
	//语言切换
	if('tc'==lang){
		var initLocale = 'zh-tw';
		$('.ifa-home-calendar').fullCalendar('option', 'locale', initLocale);
	}else if('sc'==lang){
		initLocale = 'zh-cn';;
		$('.ifa-home-calendar').fullCalendar('option', 'locale',initLocale);
	}
	//获取分组数据
	function bindGroup(){
		$.ajax({
			type:'post',
			//async:false,
			url: base_root+'/front/crm/schedule/getScheduleGroup.do?dateStr=' + new Date().getTime(),
			success:function(result){
				if(null != result || typeof(result)!='undefined'){
					var obj = eval('('+result.crmScheduleGroupJson+')');
					var html = '<li id="all_group" class="metting-list-choice" data-title="All Groups" title="'+ifaProp['ifa.home.taskList.choosegroup']+'" style="margin-bottom: 5px;height: 15px;" >'
							+'<p><a class="" href="javascript:;"><span class="metting-groups-title">'+ifaProp['ifa.home.taskList.choosegroup']+'</span></a>'
							+'<p></li>';
					var groupHtml = '<li style="margin-top:10px;border-bottom: 1px solid #e1e1e1;" title="Is it marked as important?" class="tasklist-list-choice"><input id="checkImportant" class="checkImportant" type="checkbox" value="" style="margin-right:5px">'+prop['schedule.important']+'</li>';
					var dialogHtml = '';
					$.each(obj,function(index,n){
						var groupName = n.groupName == null?'':n.groupName;
						var color = n.color == null?'':n.color;
						var subName = '';
						if(groupName.length>15){
							subName = groupName.substring(0,15)+'...';
						}else{
							subName = groupName;
						}
						// 1
						html += '<li id="'+n.id+'" groupId="'+n.id+'" title="'+groupName+'" data-color="'+color+'" class="metting-list-choice" style="margin-bottom:18px;">'
                            + '<span style="display:inline-block;width:18px;height:18px;margin-right:10px;background-color:'+color+';">&nbsp;</span>'
                            + subName
                            + '&nbsp;&nbsp;<a style="float: right;margin-right: 5px;" groupId='+n.id+' class="editGroup" id="editGroup" href="javascrip:void(0)"><img src="'+ base_root +'/res/images/strategy/strategy_edit_ico.png"></a>'
                            + '</li>';
						// 2
						groupHtml += '<li id="'+n.id+'" title="'+groupName+'" data-color="'+color+'" class="tasklist-list-choice tasklist-list-click">'
								  + '<span class="tasklist-list-circular" style="background-color:'+color+'"></span>'
								  + '<p class="task-list-word">'
								  + subName
								  +'</p>'
								  + '</li>';
						// 3
//						var nickName = n.nickName == null?'':n.nickName;
						dialogHtml +='<li data-color="'+color+'" title="'+groupName+'" value="'+n.id+'">'
								   + '<span style="display:inline-block;width:18px;height:18px;margin-right:10px;background-color:'+color+';">&nbsp;</span>'
								   + groupName
								   +'</li>';
					});
					// 1
					html +='<li id="create_group" class="create_group metting-list-choice" style="margin-top:20px;border-top: 1px solid #e1e1e1;padding-left:0px;height: 40px;" >'
						 +'<p><a class="group-list-button group-list-button-unclassified" href="javascript:;" style="TEXT-ALIGN: -webkit-center;">'+prop["schedule.group.unclassified"]+'</a>'
						 +'<a class="group-list-button group-list-button-click" href="javascript:;" style="TEXT-ALIGN: -webkit-center;">'+globalProp['global.new']+'</a>'
						 +'</p></li>';
					$('.tasklist-metting-choice').empty().append(html);
					// 2
					$('.line-add-label-group').each(function(){
						$(this).empty().append(groupHtml);
					});
					$('.tasklist-list-click').click(function(){
						// 点击ball选择分组 
						selectGroup(this);
					});
					$('.checkImportant').on('change',function(){
						// 重要事件标记
						markeredAsImportant(this);
					});
					// 3
					dialogHtml +='<div id="create_group" class="create_group metting-list-choice" style="margin-top:15px;" ><p><a class="group-list-button group-list-button-click" href="javascript:;" style="TEXT-ALIGN: -webkit-center;">'+globalProp['global.new']+'</a></p></div>';
					$('#selGroup').empty().append(dialogHtml);
					$('.group-list-button-unclassified').click(function(){
						$('.tasklist-metting-choice').attr('data-group-id','unclassified');
						$('#tasklist-title').text(prop["schedule.group.unclassified"]);
						$('.tasklist-metting-color').attr('style','background-color:transparent');
						bindTaskList();
					});
					$('.group-list-button-click').click(function(){
						$(this).closest('.regiter_xiala_ul').hide();
						groupDialog();
					});
					$('.regiter_xiala_ul >li').unbind('click');
					$('.regiter_xiala_ul >li').click(function(){
						$(this).closest('div').find('.value_show').val($(this).text());
						$(this).closest('div').find('.value_show').attr('data-value',$(this).attr('value'));
						$(this).parent().hide();
					});
				}
				//选择分组
				$(document).on("mouseleave",".tasklist-metting",function(){
					$(".tasklist-metting-choice").hide();
				});
				$('.tasklist-metting-choice li').unbind('click');
				$('.tasklist-metting-choice li').click(function(){
					$('.tasklist-metting-choice').hide();
					//创建分组
					if($(this).hasClass('create_group')){
						//groupDialog();
					}else if('all_group' == $(this).attr('id')){
						$('.tasklist-metting-choice').attr('data-group-id','');
						$('#tasklist-title').text($(this).data('title'));
						$('.tasklist-metting-color').attr('style','background-color:transparent');
						bindTaskList();
					}else{
						var groupId = $(this).attr('groupId');
						$('.tasklist-metting-choice').attr('data-group-id',groupId);
						$('#tasklist-title').text($(this).text());
						var color = $(this).data('color');
						if(typeof(color)=='undefined'){
							color = 'transparent';
						}
						$('.tasklist-metting-color').attr('style','background-color:'+color);
						TaskListEventData.scheduleEvents = [];
						TaskListEventData.taskListEvents = [];
						bindTaskList();
					}
				});
				//编辑分组
				$('.editGroup').click(function(){
					var groupId = $(this).attr('groupId');
					groupDialog(groupId);
				});
			}
		});
	}
	// 点击ball选择分组 
	function selectGroup(self){
		var groupId = $(self).attr('id');
		var groupName = $(self).find('p').text();
		var scheduleId = $(self).closest('a').find('.line-ball').data('schedule');
		var type = '';
		if($('.tasklist-date-choice p:eq(0)').hasClass('date-active')){
			type = 'S';
		}else if($('.tasklist-date-choice p:eq(1)').hasClass('date-active')){
			type = 'T';
		}
		layer.confirm(prop['schedule.alert.add.info.prefix']+groupName+prop['schedule.alert.add.info.suffix'], {
		  title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
		}, function(index){
			var url = base_root + '/front/crm/schedule/updateScheduleGroup.do?d='+new Date().getTime();
			$.ajax({
				type:'post',
				url:url,
				data:{
					groupId:groupId,
					type:type,
					id:scheduleId
				},
				success:function(result){
					if(result.flag){
						var color = result.group == null ? '' : result.group.color;
						$('.line-ball').each(function(){
							if($(this).data('schedule')==scheduleId){
								$(this).css('background-color',color);
							};
						});
						layer.msg(globalProp['global.success.save'], {icon : 2});
						layer.close(index);
					}
				}
			});
		});
	}
	// 重要事件标记
	function markeredAsImportant(self){
		var ifImportant = '';
		var type;
		if($(self).is(':checked')){
			ifImportant = '1';
			$(this).parents(".line-wrap-a").addClass("line-wrap-a-active");
		}else{
			ifImportant = '0';
			$(this).parents(".line-wrap-a").removeClass("line-wrap-a-active");
		}
		if($('.tasklist-date-choice p:eq(0)').hasClass('date-active')){
			type = 'S';
		}else if($('.tasklist-date-choice p:eq(1)').hasClass('date-active')){
			type = 'T';
		}
		var eventId = $(self).closest('a').find('.line-ball').data('schedule');
		var url = base_root + '/front/crm/schedule/updateImportant.do?d='+new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				type:type,
				ifImportant:ifImportant,
				id:eventId
			},
			success:function(result){if(result.flag){
				bindTaskList();
			}}
		});
	}
	//group
	$('.tasklist-metting-title').click(function(){
		$('.tasklist-metting-choice').toggle();
	});
	//过滤定期日程,重要事件
	$('#events-checkbox,#important-events-checkbox').change(function(){
		bindTaskList();
	});
	$('#wmes-group-close').click(function(){
		$('.mask-layer1').hide();
	});
	$('#add_group_cancel').click(function(){
		$('.mask-layer1').hide();
	});
	$('#txtGroupColor').minicolors({
		control: $(this).attr('data-control') || 'hue',
		defaultValue: $(this).attr('data-defaultValue') || '',
		inline: $(this).attr('data-inline') === 'true',
		letterCase: $(this).attr('data-letterCase') || 'lowercase',
		opacity: $(this).attr('data-opacity'),
		position: $(this).attr('data-position') || 'bottom left',
		change: function(hex, opacity) {
			if( !hex ) return;
			if( opacity ) hex += ', ' + opacity;
			try {
				////console.log(hex);
			} catch(e) {}
		},
		theme: 'bootstrap'
	});
	$('.minicolors-swatch').height('25px');
	
	//分组弹窗
	function groupDialog(groupId){
		$('.alert-group-name').hide();
		$('.alert-group-color').hide();
		$('.add_schedule_delete').hide();
		$('#txtGroupName').attr('data-group','');
		$('#txtGroupName').val('');
		$('#txtGroupColor').minicolors('value','');
		
		$('.mask-layer1').show();
		$('.mask-layer1').css('height',document.body.scrollHeight+'px');
		$('.create_groupbox').css('top',($(window).height() - $('.create_groupbox').height())/2);
//		$(window).on('scroll',scheduleGroupScroll);
		if(groupId && typeof(groupId) != 'undefined'){
			$('.tasklist-metting-choice >li').each(function(){
				if($(this).attr('id') == groupId){
					$('.add_schedule_delete').show();
					$('#txtGroupName').attr('data-group',groupId);
					$('#txtGroupName').val($(this).attr('title'));
					$('#txtGroupColor').minicolors('value',$(this).data('color'));
					return false;
				}
			});
		}
	}
	$('#add_group_save').click(saveGroup);
	
//	function scheduleGroupScroll(){
//		$('.create_groupbox').css('top',$(document).height()*0.05+$(window).scrollTop());
//	}
	
	function saveGroup(){
		var groupId = $('#txtGroupName').attr('data-group').trim();
		var groupName = $('#txtGroupName').val().trim();
		if(!groupName){
			//layer.msg(prop['schedule.alert.empty.groupName']);
			return;
		}
		var color = $('#txtGroupColor').val();
    	if(!color){
    		$('.alert-group-color').show();
    		//layer.msg(prop['schedule.alert.empty.groupColor']);
			return;
    	}
    	$.ajax({
			type:'post',
			data:
			{
				groupId:groupId,
				groupName:groupName,
				color:color
			},
			url: base_root+'/front/crm/schedule/createGroup.do?dateStr=' + new Date().getTime(),
			success:function(result){
				if((null != result || typeof(result)!='undefined') &&
						result.flag){
					var group = result.group;
					$('#txtGroup').attr('data-value',group.id);
					$('#txtGroup').val(group.groupName);
					$('.mask-layer1').hide();
					//刷新分组
					bindGroup();
					//刷新数据
					bindTaskList();
					layer.msg(globalProp['global.success.save'], {icon : 2});
        			$('.tasklist-metting-choice').toggle();
				} else {
					layer.msg(globalProp['global.failed.save'], {icon : 1});
				}
			}
		});
	}
	
	$('#add_group_delete').click(delGroup);
	function delGroup(){
		var groupId = $('#txtGroupName').attr('data-group');
		layer.confirm(prop['schedule.alert.delete.group'], {
			title:' ',btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
			}, function(index){
				$.ajax({
					type:'post',
					data:
					{
						groupId:groupId
					},
					url: base_root+'/front/crm/schedule/deleteGroup.do?dateStr=' + new Date().getTime(),
					success:function(result){
						if((null != result || typeof(result)!='undefined') &&
								result.flag){
							//刷新分组
							bindGroup();
							//刷新数据
							bindTaskList();
							layer.msg(globalProp['global.success.save'], {icon : 2});
		        			$('.tasklist-metting-choice').toggle();
						} else {
							layer.msg(globalProp['global.failed.save'], {icon : 1});
						}
						$('.tasklist-metting-choice li:eq(0)').click();
						$('.mask-layer1').hide();
						layer.close(index);
					}
				});
		});
	}
	
	$('#add_schedule_delete').click(delSchedule);
	function delSchedule(){
		layer.confirm(prop['schedule.alert.delete.schedule'], {
			title:globalProp['global.info'],btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
		}, function(index){
			deleteSchedule();
			$('#add_Schedule').hide();
			layer.close(index);
		});
	}
	
	//刷新视图
	function refashView(){
		//schdule
		$('.ifa-home-calendar').fullCalendar('removeEvents');//移除
		$('.ifa-home-calendar').fullCalendar( 'addEventSource', TaskListEventData.events );
		/*$.each(TaskListEventData.events,function(index,n){
			$('.ifa-home-calendar').fullCalendar('renderEvent', n, true);
		});*/
	}
	//表头滚动
	function taskListScrool(){
		var funds_hei =  $(".tasklist-header").height(),
			headerTop = $(".wmes_top").height(), 
			taskIcoWidth = $(".ifa-home-title").width()-20, 
			wmes_window_top = $(window).scrollTop(),
			infor_top = $(".line-wrap").offset().top;
		if ( wmes_window_top > (infor_top  - headerTop) ){
		 	var new_funds_top = wmes_window_top - infor_top - 20;
		 	$(".tasklist-header").addClass("funds_fixed");
			var	_thisStyle =  "-webkit-transform : translateY(" + new_funds_top + "px);-moz-transform : translateY(" + new_funds_top + "px);-ms-transform : translateY(" + new_funds_top + "px);-o-transform : translateY(" + new_funds_top + "px);transform : translateY(" + new_funds_top + "px);width:"+taskIcoWidth+"px;";
			$('.tasklist-header').attr("style",_thisStyle);
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
	
	$('#radEventType1').change(function(){
		if($('#radEventType1')[0].checked){
			$('.repeat_typecon').slideToggle('fast');
		}
	});
	$('#radEventType0').change(function(){
		if($('#radEventType0')[0].checked){
			$('.repeat_typecon').slideUp('fast');
		}
	});
	$('#txtRemind,#txtGroup,#txtCustomer,.register_xiala_ico').click(function(){
		$(this).closest('div').find('.regiter_xiala_ul').toggle();
	});
	$('.regiter_xiala_ul >li').click(function(){
		$(this).closest('div').find('.value_show').val($(this).text());
		$(this).parent().hide();
	});
	$('#txtTitle').on('input',titleLength);
	$('#txtContent').on('input',contentLength);
	// 字体长度控制
	function titleLength(){
		var inpLen = $('#txtTitle').val().length;
		if(inpLen>50){
			layer.msg(prop['schedule.create.schedule.control.title'], {icon : 3});
			$('#title-inputed').text(50);
			$('#title-left').text(0);
		}else{
			$('#title-inputed').text(inpLen);
			$('#title-left').text(50-inpLen);
		}
	}
	// 字体长度控制
	function contentLength(){
		var inpLen = $('#txtContent').val().length;
		if(inpLen>500){
			layer.msg(prop['schedule.create.schedule.control.contant'], {icon : 3});
			$('#content-inputed').text(500);
			$('#content-left').text(0);
		}else{
			$('#content-inputed').text(inpLen);
			$('#content-left').text(500-inpLen);
		}
	}
	$('#add_schedule_cancel').click(function(){$('#add_Schedule').hide();});
	$('#add_Schedule-close').click(function(){$('#add_Schedule').hide();});
	// new save
	$('#add_schedule_save').click(function(){
		var id = $('#hidScheduleId').val();
		if(typeof(id)=='undefined'){
			var scheduleId = '';
		}
		var title = $('#txtTitle').val();
       	var repeatFrom = $('#txtStartAt').val();
       	var repeatTo = $('#txtEndAt').val();
       	var startAt = $('#txtStartAt').val();
       	var startDate = '';
       	var startTime = '';
       	if(startAt && typeof(startAt)!='unedfined' && startAt.indexOf(' ')){
       		startDate = (startAt.split(' '))[0];
       		startTime = (startAt.split(' '))[1];
       	}
       	var endAt = $('#txtEndAt').val();
       	var endDate = '';
       	var endTime = '';
       	if(endAt && typeof(endAt)!='unedfined' && endAt.indexOf(' ')){
       		endDate = (endAt.split(' '))[0];
       		endTime = (endAt.split(' '))[1];
       	}
       	var group = $('#txtGroup').attr('data-value');
       	var customerId = $('#txtCustomer').attr('data-value');
       	var remindSetting = $('#txtRemind').attr('data-value');
       	var content = $('#txtContent').val();
       	// 检验
		if(!title){
			$('.alert-schedule-title-name').show();
			return;
		}
		var eventType = '0';
		if($('#cheRecurrentFlag')[0].checked){
			eventType = '1';
			if(!repeatFrom){
				return;
			}
			if(!repeatTo){
				return;
			}
		}
       	var repeatDay ='';
       	$.each($('input[name=repeatDay]'),function(index,n){
       		if(n.checked){
       			repeatDay += '1';
       		}else{
       			repeatDay += '0';
       		}
       	});
       	if(eventType == '1' && '0000000' == repeatDay){
       		repeatDay = '1111111';
       	}
		if(!startAt){
			return;
		}
		if(!endAt){
			return;
		}
		var ifImportant = '0';
       	if($('#cheImportantFlag')[0].checked){
       		ifImportant = '1';
       	}else{
       		ifImportant = '0';
       	}
       	var eventData = 
        {
        	start:startAt,end:endAt,//start,end必需
			id:id,eventType:eventType,title:encodeURI(title),
			strStartDate:startDate,strEndDate:endDate,
			strStartTime:startTime,strEndTime:endTime,
			repeatDay:repeatDay,groupId:group,
			strRepeatFrom:repeatFrom,strRepeatTo:repeatTo,
			//color:color,
			content:encodeURI(content),
			customerId:customerId,
			ifImportant:ifImportant,
			remindSetting:remindSetting
		};
	    $.ajax({
			type:'post',
			url:base_root+'/front/crm/schedule/updateSchedule.do?dateStr=' + new Date().getTime(),
			data:eventData,
			beforeSend:function(){
				$('.schedule-save-load').show();
			},
			success:function(result)
			{
				$('.schedule-save-load').hide();
				if (result.flag) {
					layer.msg(globalProp['global.success.save'], {icon : 2});
				} else {
					layer.msg(globalProp['global.failed.save'], {icon : 1});
				}
				//刷新数据
				bindTaskList();
				$('#add_Schedule').hide();
			},error:function(){
				setTimeout(function(){
					$('.schedule-save-load').hide();
					layer.msg(globalProp['global.failed.save'], {icon : 1});
				},500);
			}
	    });
	});
	// 获取客户
	function getCustomers(){
		$.ajax({
			type:'post',
			url: base_root+'/front/crm/schedule/getCustomers.do?dateStr=' + new Date().getTime(),
			success:function(result){
				if(null != result || typeof(result)!='undefined'){
					var obj = eval('('+result.customersJson+')');
					var html = '';
					$.each(obj,function(index,n){
						var nickName = n.nickName == null?'':n.nickName;
						html += '<li value='+n.id+'>'+nickName+'</li>';
					});
					$('#selCustomer').empty().append(html);
					$('.regiter_xiala_ul >li').unbind('click');
					$('.regiter_xiala_ul >li').click(function(){
						$(this).closest('div').find('.value_show').val($(this).text());
						$(this).closest('div').find('.value_show').attr('data-value',$(this).attr('value'));
						$(this).parent().hide();
					});
				}
			}
		});
	}
	getCustomers();
	// 获取提醒设置
	function getRemind(){
		$.ajax({
			type:'post',
			url: base_root+'/front/crm/schedule/getRemind.do?dateStr=' + new Date().getTime(),
			success:function(result){
				var html = '';
				if(result.flag){
					$.each(result.remindData,function(i,n){
//						var nickName = n.nickName == null?'':n.nickName;
						html += '<li id="'+n.id+'" title="'+n.name+'" value='+n.value+'>'+n.name+'</li>';
					});
					$('#selRemind').empty().append(html);
					$('#selRemind >li').unbind('click');
					$('#selRemind >li').click(function(){
						$(this).closest('div').find('.value_show').val($(this).text());
						$(this).closest('div').find('.value_show').attr('data-value',$(this).attr('value'));
						$(this).parent().hide();
					});
				}
			}
		});
	}
	getRemind();
	//objE为form表单  
	function clearForm(objE){
        $(objE).find(':input').each(  
            function(){  
	            switch(this.type){  
	                case 'passsword':  
	                case 'select-multiple':  
	                case 'select-one':  
	                case 'text':  
	                case 'textarea':  
	                    $(this).val('');  
	                    break;  
	                case 'checkbox':  
	                case 'radio':  
	                    this.checked = false;  
	            }  
            }     
        );  
    }  
	$('.register_xiala_ico,#txtCustomer,#txtGroup,#txtRemind').click(function(){
		var select =  $(this).closest('.register_xiala_long').find('.regiter_xiala_ul');
		if(select.is(":visible")){
			$('.regiter_xiala_ul').hide();
			select.show();
		}else{
			$('.regiter_xiala_ul').hide();
			select.hide();
		}
	});
	$('#tasklist-title,.group-img').click(function(){
		$('.tasklist-metting-title').find('img').toggleClass('rotate');
	});
	
	$('#txtGroupName').on('blur',function(){
		if($(this).val().length<1){
			$('.alert-group-name').show();
		}else{
			$('.alert-group-name').hide();
		}
	});
	$('#txtGroupColor').on('blur',function(){
		if($(this).val().length<1){
			$('.alert-group-color').show();
		}else{
			$('.alert-group-color').hide();
		}
	});
	$('#txtTitle').on('blur',function(){
		if($(this).val().length<1){
			$('.alert-schedule-title-name').show();
		}else{
			$('.alert-schedule-title-name').hide();
		}
	});
	$('#txtRepeatFrom').on('blur',function(){
		if($(this).val().length<1){
			$('.alert-schedule-repeat-from').show();
		}else{
			$('.alert-schedule-repeat-from').hide();
		}
	});
	$('#txtRepeatTo').on('blur',function(){
		if($(this).val().length<1){
			$('.alert-schedule-repeat-to').show();
		}else{
			$('.alert-schedule-repeat-to').hide();
		}
	});
	$('#txtStartAt').on('blur',function(){
		if($(this).val().length<1){
			$('.alert-schedule-start-at').show();
		}else{
			$('.alert-schedule-start-at').hide();
		}
	});
	$('#txtEndAt').on('blur',function(){
		if($(this).val().length<1){
			$('.alert-schedule-end-at').show();
		}else{
			$('.alert-schedule-end-at').hide();
		}
	});
	function endTimeAutoIncrease(startTime){
		var endTime = '';
		if(startTime.indexOf(' ') > -1){
			var date = (startTime.split(' '))[0],
				time = (startTime.split(' '))[1],
				year = (date.split('-'))[0],
				month = (date.split('-'))[1],
				day = (date.split('-'))[2],
				hour = (time.split(':'))[0],
				minute = (time.split(':'))[1],
				second = (time.split(':'))[2];
			if(hour == '23'){
				hour = 00;
				var bigMonth = ['01','03','05','07','08','10','12'];
				var smallMonth = ['04','06','09','11'];
				if($.inArray(month, bigMonth)>-1){
					if(day=='31'){
						day = '1';
						if(month == '12'){
							month = '1';
							year = Number(year)+1;
						}else{
							month = Number(month)+1;
						}
					}else{
						day = Number(day)+1;
					}
				}else if($.inArray(month, smallMonth)>-1){
					if(day=='30'){
						day = 01;
						month = Number(month)+1;
					}else{
						day = Number(day)+1;
					}
				}else{
					if((year%4==0 && day=='28') || (year%4!=0 && day=='29')){
						day = '1';
						month = Number(month)+1;
					}else{
						day = Number(day)+1;
					}
				}
			}else{
				hour = Number(hour)+1;
			}
			if(month.toString().length==1){month = '0'+month;}
			if(day.toString().length==1){day = '0'+day;}
			if(hour.toString().length==1){hour = '0'+hour;}
			if(minute.toString().length==1){minute = '0'+minute;}
			if(second.toString().length==1){second = '0'+second;}
			endTime = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
		}
		return endTime;
	}
	/**
	 * schedule more
	 */
	$('#schedule_more').click(function(){
		var page = $('#hidPage').val();
		if(page){
			page = Number(page);
			page = page+1;
		}else{
			page = 1;
		}
		$('#hidPage').val(page);
		initScheduleList();
	});
	bindTaskList();
	function showSchedule(eventId){
		var url = base_root+'/front/crm/schedule/getScheduleInfo.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			data:{id:eventId},
			//async:false,
			url: url,
			success:function(result){
				if(result.flag){
					var calEvent = result.schedule;
					$('.schedule-add-title').hide();
					$('.schedule-edit-title').show();
					clearForm('.register_table');
					$('.regiter_xiala_ul').hide();
					$('.alert-schedule-title-name').hide();
					$('#add_Schedule').show();
					$('.schedule-dialog-creator').show();
					$('#schedule-creator-text').text(calEvent.creatorName);
					$('#add_Schedule .space-mask').css('margin-top',$(window).scrollTop());
					if(calEvent.id != null && calEvent.id != ''){
						$('#add_schedule_delete').show();
					}
					$('#hidScheduleId').val(calEvent.id);
					$('#txtTitle').val(calEvent.title);
					if(calEvent.eventType=='1'){
						$("#cheRecurrentFlag").prop("checked",true);
						$('.repeat_typecon').show();
					}else{
						$("#cheRecurrentFlag").prop("checked",false);
						$('.repeat_typecon').hide();
						var week = new Date().getDay();
						$('#repeatDay-' + week).prop('checked', true);
					}
					if(calEvent.ifImportant=='1'){
						$("#cheImportantFlag").prop("checked",true);
					}else{
						$("#cheImportantFlag").prop("checked",false);
					}
					if(calEvent.repeatFrom!=''){
						$('#txtRepeatFrom').val(calEvent.repeatFrom);
					}
					if(calEvent.repeatTo!=''){
						$('#txtRepeatTo').val(calEvent.repeatTo);
					}
					$('#txtStartAt').val((calEvent.start.replace('T',' ').split('.'))[0]);
					if(calEvent.end!=null){
						$('#txtEndAt').val((calEvent.end.replace('T',' ').split('.'))[0]);
					}else{
						$('#txtEndAt').val((calEvent.start.replace('T',' ').split('.'))[0]);
					}
					$('#txtCustomer').val(calEvent.customerName);
					$('#txtCustomer').attr('data-value',calEvent.customerId);
					$('#txtGroup').val(calEvent.groupName);
					$('#txtGroup').attr('data-value',calEvent.groupId);
					$('#txtRemind').val(calEvent.remindSettingName);
					$('#txtRemind').attr('data-value',calEvent.remindSetting);
					$('#txtContent').val(calEvent.content);
					if(calEvent.repeatDay != ''){
						for ( var int = 0; int < calEvent.repeatDay.length; int++) {
							if(calEvent.repeatDay.charAt(int) == '1'){
								$("#repeatDay-"+int).prop("checked",true);
							}
						}
					}
					// 字数控制
					titleLength();
					contentLength();
					// 初始化时间
					initDate(calEvent.start,calEvent.end);
				}
			}
		});
	}
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
	if('schedule' == getUrlParam('type') && getUrlParam('id')){
		var id = getUrlParam('id');
		showSchedule(id);
	}
 /****************************************Task List end*********************************************/

	
	/**************************************Clients***********************************************/
	
	myClient.controller('profitTableCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.profitList='';
		$scope.lossList='';
		$scope.bestList='';
		$scope.worstList='';
		// 数字调用
		$scope.counter = Array;
		
		$scope.bestStatistic='';
		$scope.worstStatistic='';
		// 监听视图渲染是否结束
    	$scope.checkLast = function($last){
			if($last){

				setTimeout(function(){
					clientCharts();//初始化图表
				},100)
				
			}
		}
   		$scope.defColor = $("#defColor").val();
   	    $scope.currency = $("#currency").val();
   	    $scope.currencyName=$("#currencyName").val();
        $scope.decimals=Number($("#decimals").val()); 
		$(".clientsdetail-tab li").on("click",function(){
			$(this).siblings().removeClass("now").end().addClass("now");
			$(".clientdetail-contents-wrap").removeClass("clientdetail-contents-active").eq($(this).index()).addClass("clientdetail-contents-active");
		});
	  
	 //获取盈利客户
	 function bindProfit(){
		 var listLoading = new Loading($(".profit-list"));	
		 listLoading.show();
		 var url = base_root + "/front/ifa/info/getClients.do?dateStr=" + new Date().getTime();
		$http({
          url: url,
          method: 'POST',
          data : "type=profit",
          headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
     	 })
		.success(function(response){
			listLoading.hide();
		    $scope.profitList =  response.list;
		   if(response.list.length>0){
			   $(".profileTips").hide();
		   }else{
			   $(".profileTips").show();
		   }
		    clientCharts();
		    bindLoss();
		});
		
	    }
	
	 function bindLoss(){
		 var listLoading = new Loading($(".loss-list"));	
		 listLoading.show();
		 var url = base_root + "/front/ifa/info/getClients.do?dateStr=" + new Date().getTime();
			$http({
	          url: url,
	          method: 'POST',
	          data : "type=loss",
	          headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
	     	 })
			.success(function(response){
				listLoading.hide();
			    $scope.lossList =  response.list;
			    if(response.list.length>0){
					   $(".lossTips").hide();
				   }else{
					   $(".lossTips").show();
				   }
			    clientCharts();
			});
	 }
	 
	 bindTopBest();
	 var bestListLoading ;
	 function bindTopBest(){
		  $(".bestTop5").hide();
		 var statistic=$(".best-choose").find(".topfive-date-active").attr("data-key");
		 var statisticText="";//$(".best-choose").find(".topfive-date-active").text();
		 if(statistic=="1M"){
			 statisticText=langMutilForJs['ifa.home.top5.1Mre']
		 }else if(statistic=="3M"){
			 statisticText=langMutilForJs['ifa.home.top5.3Mre']
		 }else if(statistic=="YTD"){
			 statisticText=langMutilForJs['ifa.home.top5.ytdre']
		 }else if(statistic=="1Y"){
			 statisticText=langMutilForJs['ifa.home.top5.1yre']
		 }else{
			 statisticText=langMutilForJs['ifa.home.top5.totalre']
		 }
		 $scope.bestStatistic=statisticText;
		 var url = base_root + "/front/ifa/info/getTop5Best.do?dateStr=" + new Date().getTime();
		 if(bestListLoading==undefined)
		   bestListLoading  = new Loading($(".ifa-topfive-best"));	
		 bestListLoading.show();
		 $scope.bestList = "";
			$http({
	          url: url,
	          method: 'POST',
	          data : "type=best&statistic="+statistic,
	          headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
	     	 })
			.success(function(response){
				bestListLoading.hide();
			    $scope.bestList =  response.list;
			    if(response.list.length>0){
					   $(".bestTop5").hide();
				   }else{
					   $(".bestTop5").show();
				   }
			    clientCharts();
			    bindTopWorst();
			});
	 }
	 
	
	 var worstListLoading;	
	 function bindTopWorst(){
		 $(".worstTop5").hide();
		 if(worstListLoading==undefined)
		    worstListLoading = new Loading($(".ifa-topfive-worst"));	
		 var statistic=$(".worst-choose").find(".topfive-date-active").attr("data-key");
		 var statisticText=""//$(".worst-choose").find(".topfive-date-active").text();
		 if(statistic=="1M"){
			 statisticText=langMutilForJs['ifa.home.top5.1Mre']
		 }else if(statistic=="3M"){
			 statisticText=langMutilForJs['ifa.home.top5.3Mre']
		 }else if(statistic=="YTD"){
			 statisticText=langMutilForJs['ifa.home.top5.ytdre']
		 }else if(statistic=="1Y"){
			 statisticText=langMutilForJs['ifa.home.top5.1yre']
		 }else{
			 statisticText=langMutilForJs['ifa.home.top5.totalre']
		 }
		 $scope.worstStatistic=statisticText;
		 var url = base_root + "/front/ifa/info/getTop5Best.do?dateStr=" + new Date().getTime();
		 worstListLoading.show();
		 $scope.worstList = "";
			$http({
	          url: url,
	          method: 'POST',
	          data : "type=worst&statistic="+statistic,
	          headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
	     	 })
			.success(function(response){
				worstListLoading.hide();
				 
			    $scope.worstList =  response.list;
			   
			    if(response.list.length>0){
					   $(".worstTop5").hide();
				   }else{
					   $(".worstTop5").show();
				   }
			    clientCharts();
			    bindProfit();
			});
	 }
	 
	 
	 function clientCharts(){
			$(".clientdetail-mainer-chart").each(function(){
				var selfData = eval($(this).attr('data-value'));
				//设置颜色
	            for(var item in selfData){
	            	selfData[item]['itemStyle'] = {normal:{color:selfData[item].displayColor},emphasis:{}}
	              /*  if( selfData[item].name =="fund" ){
	                    selfData[item]['itemStyle'] = {normal:{color:selfData[item].name},emphasis:{}}
	                }else if(selfData[item].name =="stock"){
	                    selfData[item]['itemStyle'] = {normal:{color:'#5470df'},emphasis:{}}
	                }else if( selfData[item].name =="bond" ){
	                    selfData[item]['itemStyle'] = {normal:{color:'#40c7f3'},emphasis:{}}
	                }*/
	            }
	           if(selfData.length==0){
	        	   var data={};
	        	   data.name="";
	        	   data.value=1;
	        	   selfData.push(data);
	        	   selfData[0]['itemStyle'] = {normal:{color:"#696969"},emphasis:{}}
	           }
	            

				var option = {
				    series: [
				        {
				            type:'pie',
				            label: {
				                normal: {
				                    position: 'inner',
				                    show:false,
				                    formatter : "{b}\n{d}%"
				                }
				            },
				            itemStyle :{

				                emphasis : {
				                	show:false
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
	 setTimeout(function(){
	 	$.each($('.clientdetail-bottom-left .clientdetail-mainer-number span,.clientdetail-bottom-left .clientdetail-bottom-aum span'),function(){
//	 		var leng = $('.clientdetail-bottom-left .clientdetail-mainer-number span,.clientdetail-bottom-left .clientdetail-bottom-aum span').text().length;
	 		if($(this).width()>=115){
	 			$(this).css('cssText','font-size:15px!important')
	 		}
	 	});
	 },400);
	 
	 
	 $(".worst-choose span").on("click",function(){
		 $(this).siblings().removeClass("topfive-date-active").end().addClass("topfive-date-active");
		 bindTopWorst();
	 })
	 $(".best-choose span").on("click",function(){
		 $(this).siblings().removeClass("topfive-date-active").end().addClass("topfive-date-active");
		 bindTopBest();
	 })
	
//	var scrollTimer;   
//	$("#callboard").hover(function() {   
//		clearInterval(scrollTimer);   
//	},function(){
//		scrollTimer = setInterval(function() {
//			scrollNews($("#callboard"));
//		}, 2000);   
//	}).trigger("mouseleave");   
//
//	function scrollNews(obj) {   
//		var $self = obj.find("ul");   
//		var lineHeight = $self.find("li:first").height();   
//		$self.animate({   
//			"marginTop": -lineHeight + "px"   
//		}, 600, function(){
//			$self.css({
//				marginTop: 0
//			}).find("li:first").appendTo($self);   
//		})   
//	}
	$(document).on('click','.notice-title b',function(){
		var noticewidth = parseInt(- ($('.notice-con').width() + 1));
		$('.notice-con').animate({
			right:noticewidth+'px'
		},1000,function(){
			$('.noticeac').css('display','block');
		});
	});
	$(document).on('click','.noticeac',function(){
		$('.noticeac').css('display','none');
		$('.notice-con').animate({
			right:0
		},1000);
	});
	$('.notice-con').animate({bottom:'0'},3000);
	/* $(".ifa-topfive-wrap").on("click",".topfive-rows-chat",function(){
		 var memberId=$(this).attr("userId");
		 var nickName=$(this).attr("nickName");
		_chat.load(memberId,nickName); 
	 });*/
	 
	  }]);
	 /**************************************Clients End***********************************************/
	
	/**************************************Notice Start***********************************************/
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
	/**
	 * preview notice
	 */
	$(document).on('click','.notice-preview',function(){
		initDialogNotice();
		$('#previewNoticeDialog').find('.selectSector').css('box-shadow',null);
		$('#previewNoticeDialog').find('.selectSector').css('background',null);
		$('#previewNoticeDialog').addClass('dispaly-active');
		$('#previewNoticeDialog').show();
		var title = $(this).text();
		var releaseDateTime = $(this).data('releaseDate');
		var releaseBy = $(this).data('releaseBy');
		//var content = $(this).data('content');
		var content = $(this).closest('li').find('.hidContent').text();
		$('.notice-dialog-title').text(title);
		$('.notice-dialog-release-date-time').text(releaseDateTime);
		$('.notice-dialog-release-by').text(releaseBy);
		if(content != 'undefined'){
			//$('.dialog-notice-content').html(decodeURI(content));
			$('.dialog-notice-content').html(content);
		}
	});	
	/**************************************Notice End*************************************************/
});