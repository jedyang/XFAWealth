define(function(require) {
	var $ = require('jquery');
			require('layui');
			require("fullcalendar");//日程控件
			require("fullcalendarlang");
			require('minicolors');
			require('laydate');
			
	$(".tab_list_tab li").on("click",function(){
		$('.fc-today-button').trigger('click');
		$('.fc-today-button').click();
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".client-detail-contents > div").hide().eq($(this).index()).show();
	});
	
	$("#pipelin-schedule").on("click",function(){
		$("#ifa-pipeline-date").toggleClass("ifa-space-active");
		getCustomers();
		$.when(bindSchedule()).done(initFullcalendar());
		$('.ifa-home-calendar').show();
		$('.fc-today-button').trigger('click');
	});
	$("#pipelin-date-close").on("click",function(){
		$("#ifa-pipeline-date").toggleClass("ifa-space-active");
	});
	
	$("#fullcalendar-show").on("click",function(){
		$(this).toggleClass("take_list_contents");
		$(this).toggleClass("take_list_date");
		$('#schedule-view').toggle();
		$('.ifa-home-calendar').toggle();
	});
/**************************************** schedule *********************************************/
	//TaskList 事件源
	var TaskListEventData =(function(){
		//总事件
		var events = [];
		return{
			events:events
		};
	})();
	var HomeTaskList = {
		lineMove : function(){
			$(".line-wrap-a").on("mousemove",function(){
	            var self = $(this);
	            self.find(".line-topline").stop(true).animate({ width: "100%"}, 100 ,function(){
	                self.find(".line-rightline").stop(true).animate({ height: "100%"}, 100 ,function(){
	                    self.find(".line-bottomline").stop(true).animate({ width: "100%"}, 100 );
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
					$('.tasklist-date-choice').attr('style','margin-left:0');
					$('.tasklist-events').show();
				}else if('createDate' == $(this).data('sort')){
					$('.tasklist-events').hide();
					$('.tasklist-date-choice').attr('style','margin-left:'+$('.tasklist-events').width()+'px');
				}
				$(this).siblings().removeClass("date-active").end().addClass("date-active");
				var selt = this;
				//排序
				TaskListEventData.events = TaskListEventData.events.sort(function(a, b){
					var aDate = '';
					var bDate = '';
					if('startDate' == $(selt).data('sort')){
						if(a.start.indexOf('T')){
							aDate = (a.start.split('T'))[0];
						}
						if(b.start.indexOf('T')){
							bDate = (b.start.split('T'))[0];
						}
						return false;
					}else if('createDate' == $(selt).data('sort')){
						aDate = a.create;
						bDate = b.create;
						return false;
					}
					aDate = new Date(aDate);
					bDate = new Date(bDate);
					return bDate-aDate;	
				});
				//initScheduleList();
			});
			$(".tasklist-metting-title").on("click",function(){
				$(this).siblings(".tasklist-metting-choice").toggleClass("tasklist-metting-show-choice");
			});
		},
		TaskListInit : function(){
			this.lineMove();
			this.lineFun();
		}
	};
	
	//获取日程数据
	function bindSchedule(){
		var clientType = '',
			order = 'DESC',
			sort = '';
		$(".tasklist-date-choice p").each(function(){
			if($(this).hasClass('date-active')){
				sort = $(this).data('sort');
				return false;
			}
		});
		/*$('.noline_tab_tab li').each(function(){
			if($(this).hasClass('now') && $(this).data('li') == 'P'){
				clientType = '0';
			}else if($(this).hasClass('now') && $(this).data('li') == 'E'){
				clientType = '1';
			}
		});*/
		var customerId = getUrlParam('customerId');
		var groupId = '';
		var eventType = '';
		if($('#events-checkbox').prop('checked')){
			eventType = '1';
		}
		groupId = $('.tasklist-metting-choice').attr('data-group-id');
		if(typeof(groupId) == 'undefined'){
			groupId = '';
		}
		$.ajax({
			type:'post',
			//async:false,
			data:
			{
				order:order,
				sort:sort,
				clientType:clientType,
				groupId:groupId,
				eventType:eventType,
				customerId:customerId
			},
			url: base_root+'/front/crm/schedule/getCRMSchedule.do?dateStr=' + new Date().getTime(),
			success:function(result){
				if(result.crmScheduleJson!=null){
					var events = eval('('+result.crmScheduleJson+')');
					TaskListEventData.events = [];
					//初始化日程数据源
					$.each(events,function(index,n){
						TaskListEventData.events.push({ 
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
            				creatorName:n.creatorName == null?'':n.creatorName,
        				    ifImportant: n.ifImportant
				        });
					});
					//排序
					TaskListEventData.events = TaskListEventData.events.sort(function(a, b){
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
								return false;
							}else if($(this).hasClass('date-active') && 'createDate' == $(this).data('sort')){
								aDate = a.create;
								bDate = b.create;
								return false;
							}
						});
						aDate = new Date(aDate);
						bDate = new Date(bDate);
						return bDate-aDate;	
					});
				}
				initFullcalendar();
				refashView();
			}
		});
	}
	
	//组装任务列表视图数据
	function initScheduleList(){
		var htmlRight = '';
		var htmlLeft = '';
		$.each(TaskListEventData.events,function(index,n){
			var treeRight = '';
			var treeLeft = '';
			var dataType = n.dataType;
			var title = '';
			var start = '';
			var color = '';
			var content = '';
			//var id = '';
			if('S' == dataType){//日程
				title = n.title;
				start = n.start;
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
				//id = n.id;
				title = n.title;
				start = n.start;
				color = '#000'; 
			}
			var subTitle = '';
			if(title.length>15){
				subTitle = title.substring(0,15)+'...';
			}else{
				subTitle = title;
			}
			var comm=
			   '<div class="line-topline"></div>'
			   +'<div class="line-rightline"></div>'
			   +'<div class="line-bottomline"></div>'
			   +'<div class="line-ball" style="background-color:'+color+'"></div>'
		       +'<div class="line-title-line"></div>'
		       +'<div class="line-title">'
		        +'<p class="line-date">'+start+'</p>'
		        +'<p class="line-l">|</p>'
		        +'<p class="line-word" title="'+title+'" style="color:#49b0c7;">'+subTitle+'</p>'
		       +'</div>'
			+'<div class="line-for" style="color: #ab2e2e;">'
    		+'<p>'+content+'</p>'
    		+'</div>';
			if(0 == index%2){//R
				treeRight +=
					'<a id="line-wrap-right-2"  class="line-wrap-a" style="margin-bottom:60px;" href="javascript:;">';
				treeRight += comm;
			    treeRight += '</a>';
			    htmlRight += treeRight; 
			}else{//L
				treeLeft +=
					'<a id="line-wrap-left-1"  class="line-wrap-a" href="javascript:;">';
				treeLeft += comm;
			    treeLeft += '</a>';
			    htmlLeft +=	treeLeft;  
			}
		});
		$('#schedule-view').empty().append('<div class="line-wrap" style="display:block;"><div class="line-wrap-right">'+htmlRight+'</div><div class="line-wrap-left">'+htmlLeft+'</div></div>');
		HomeTaskList.TaskListInit();
	}
	
	function initDate(start,end){
		$('#txtStartAt').unbind('click');
		var start_ = '';
		if(start_ == ''){start_ = '';}else{
			start_ = start._d.toISOString().replace('T',' ');
		}
		$('#txtStartAt').click(function(){startAt(start_,'');});
		function startAt(min,max){
			if((typeof(max)=='undefined' || max == '') && !$('#txtEndAt').val()){
				max = '2099-12-30 00:00:00';
			}else if(typeof(max)=='undefined' || max == ''){
				max = $('#txtEndAt').val();
			}
			var istime = true;
			/*if(true == $('#cheRecurrentFlag')[0].checked){
				istime = false;
			}*/
			laydate({
				istime: istime,
				istoday: false,
				min:'0001-01-01 00:00:00',
				//max:max,
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
			var istime = true;
			laydate({
				istime: istime,
				istoday: false,
				min:min,
				max:'2099-12-30 00:00:00',
				isclear: false,
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
		//语言切换
		var initLocale = '';
		if('tc'==lang){
			initLocale = 'zh-tw';
		}else if('sc'==lang){
			initLocale = 'zh-cn';
		}
		//弹窗url
		var url =base_root+'/front/crm/schedule/detail.do?dateStr=' + new Date().getTime();
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
       		locale: initLocale,
			navLinks : true, 
			editable : true,
			eventLimit : true, 
			events : TaskListEventData.events,  //配置事件源
			//新增事件
			selectable: true,
			selectHelper: true,
			select: function(start, end, allDay, jsEvent) {
				$('.schedule-add-title').show();
				$('.schedule-edit-title').hide();
				clearForm('.register_table');
				$('#hidScheduleId').val('');
				$('#txtCustomer').removeAttr('data-value');
				$('#txtGroup').removeAttr('data-value');
				$('#txtRemind').removeAttr('data-value');
				$('#add_schedule_delete').hide();
				$('.alert-schedule-title-name').hide();
				$('.regiter_xiala_ul').hide();
				var customerId = getUrlParam('customerId');
				if(typeof customerId != 'undefined' && customerId != '' && customerId != null){
					$('#selCustomer li').each(function(){
						if($(this).attr('value') == customerId){
							$(this).click();
							$(this).closest('div').find('span').hide();
						}
					});
					$('#txtCustomer').unbind('click');
				}else{
					$('#selCustomer li:gt(0)').each(function(){
						$(this).click();
					});
				}
				$('.repeat_typecon').hide();
				var week = new Date().getDay();
				$('#repeatDay-' + week).prop('checked', true);
				$('#add_Schedule').show();
				$('#add_Schedule .space-mask').css('margin-top',$(window).scrollTop());
				$('.schedule-dialog-creator').hide();
				if($('.fc-month-button').hasClass('fc-state-active')){
					// 时间控制
					$('#txtStartAt').val((start._d.toISOString().split('T'))[0]+' '+getTimeStr());
					$('#txtEndAt').val(endTimeAutoIncrease($('#txtStartAt').val()));
				}else{
					var startTime = (start._d.toISOString().split('.'))[0];
					var endTime = (end._d.toISOString().split('.'))[0];
					// 时间控制
					$('#txtStartAt').val((start._d.toISOString().split('T'))[0]+' '+(startTime.split('T'))[1]);
					$('#txtEndAt').val((start._d.toISOString().split('T'))[0]+' '+(endTime.split('T'))[1]);
				}
				// 字数控制
				titleLength();
				contentLength();
				initDate(start,end);
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
				$('.schedule-dialog-creator').show();
				$('#schedule-creator-text').text(calEvent.creatorName);
				if(calEvent.id != null && calEvent.id != ''){
					$('#add_schedule_delete').show();
				}
				$('#hidScheduleId').val(calEvent.id);
				$('#txtTitle').val(calEvent.title);
				if(calEvent.eventType=='1'){
					$("#cheRecurrentFlag").prop("checked",true);
					$('.repeat_typecon').show();
					$('#txtStartAt').val(calEvent.repeatFrom.replace('T',' '));
					$('#txtEndAt').val(calEvent.repeatTo.replace('T',' '));
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
				if(calEvent.ifImportant == '1'){
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
				titleLength();
				contentLength();
				initDate(calEvent.start,calEvent.end);
            },
			eventDrop: function(calEvent, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view){
            	eventDragUpdate(calEvent);
            }
		});
		refashView();
	}
	
	//保存与更新
	function saveAndUpdate(dialogItself){
		var id = $('#txtId').val();
		//取eventType值
       	var eventType = '';
       	if(true == $('#cheRecurrentFlag')[0].checked){
       		eventType = '1';
       	}else{
       		eventType = '0';
       	}
       	//取repeat值
       	var repeatDay ='';
       	$.each($('input[name=repeatDay]'),function(index,n){
       		repeatDay += n.value;
       	});
       	if(eventType == '1' && '0000000' == repeatDay){
       		repeatDay = '1111111';
       	}
       	var group = $('#selGroup').val();
       	var repeatFrom = $('#txtStartAt').val();
       	var repeatTo = $('#txtEndAt').val();
       	
       	var title = $('#txtTitle').val();
       	var startAt = $('#txtStartAt').val();
       	var endAt = $('#txtEndAt').val();
       	var customerId = $('#selCustomer').val();
       	if(customerId == null){
       		customerId = $('#txtCustomer').data('value');//客户Id
       	}
       	//检验非空
       	if(!title){
       		$('.alert-schedule-title-name').show();
       		return false;	
       	}
       	if(!startAt){
       		return false;	
       	}
       	if(!endAt){
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
       	if($('#txtStartAt').val() && $('#txtEndAt').val()){
			var startStr = $('#txtStartAt').val().replace(/:/,'').replace(/:/,'').replace(' ','');
			var endStr = $('#txtEndAt').val().replace(/:/,'').replace(/:/,'').replace(' ','');
			if(startStr > endStr){
				return false;
			}
		}
       	var remindSetting = $('#selRemindSetting').val();
       	var color = $('#txtColor').val();
       	var content = $('#txtContent').val();
       	var ifImportant = '';
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
			color:color,content:encodeURI(content),
			customerId:customerId,
			ifImportant:ifImportant,
			remindSetting:remindSetting
		};
	    $.ajax({
			type:'post',
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
			}
	    });
	    return eventData;
	}
	
	//删除
	function deleteSchedule(){
		var id = $('#hidScheduleId').val();
	    $.ajax({
			type:'post',
			url:base_root+'/front/crm/schedule/deleteSchedule.do?dateStr=' + new Date().getTime(),
			data:{id:id},
			success:function(result){
				if (result.flag) {
					layer.msg(globalProp['global.delete.success'], {icon : 2});
				} else {
					layer.msg(globalProp['global.delete.failed'], {icon : 1});
				}
				//刷新数据
				bindSchedule();
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

	//获取分组数据
	function bindGroup(){
		$.ajax({
			type:'post',
			url: base_root+'/front/crm/schedule/getScheduleGroup.do?dateStr=' + new Date().getTime(),
			success:function(result){
				if(null != result || typeof(result)!='undefined'){
					var obj = eval('('+result.crmScheduleGroupJson+')');
					var html = '<li id="all_group" class="metting-list-choice" style="margin-bottom:8px;" ><p><span style="display:inline-block;width:18px;height:18px;margin-right:10px;">&nbsp;</span> All group '
							 + '<p></li>';
					var groupHtml = '';
					var dialogHtml = '';
					$.each(obj,function(index,n){
						var groupName = n.groupName == null?'':n.groupName;
						var color = n.color == null?'':n.color;
						var subName = '';
						if(groupName.length>10){
							subName = groupName.substring(0,9)+'...';
						}else{
							subName = groupName;
						}
						// 1
						html += '<li id="'+n.id+'" groupId="'+n.id+'" title="'+groupName+'" data-color="'+color+'" class="metting-list-choice" style="margin-bottom:18px;">'
                            + '<span style="display:inline-block;width:18px;height:18px;margin-right:10px;background-color:'+color+';">&nbsp;</span>'
                            + subName
                            + '&nbsp;&nbsp;<a style="float: right;margin-right: 5px;" groupId='+n.id+' class="editGroup" id="editGroup" href="javascrip:void(0)"><img src="/wmes/res/images/strategy/strategy_edit_ico.png"></a>'
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
					html +='<li id="create_group" class="create_group metting-list-choice" style="margin-top:15px;" ><p><span style="display:inline-block;width:18px;height:18px;margin-right:10px;">&nbsp;</span><span style="color:black">Create group'
						 +'</span></p></li>';
					$('.tasklist-metting-choice').empty().append(html);
					// 2
					$('.line-add-label-group').each(function(){
						$(this).append(groupHtml);
					});
					$('.tasklist-list-click').click(function(){
						var groupId = $(this).attr('id');
						var groupName = $(this).find('p').text();
						var scheduleId = $(this).closest('a').find('.line-ball').data('schedule');
						layer.confirm(prop['schedule.alert.add.info.prefix']+groupName+prop['schedule.alert.add.info.suffix'], {
						  title:' ',btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
						}, function(index){
							var url = base_root + '/front/crm/schedule/updateScheduleGroup.do?d='+new Date().getTime();
							$.ajax({
								type:'post',
								url:url,
								data:{
									groupId:groupId,
									scheduleId:scheduleId
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
					});
					// 3
					dialogHtml +='<div id="create_group" class="create_group metting-list-choice" style="margin-top:15px;" ><p>'
							   +'<a class="schedule-group-list-button" href="javascript:;" style="text-align:center;background-color: #58adeb;color: white;padding: 2px 5px;border-radius: 3px;margin: 5px auto; display: block;width: 20%;">'
							   +globalProp["global.add"]+'</a></p></div>';
					$('#selGroup').empty().append(dialogHtml);
					$('.schedule-group-list-button').click(function(){
						$(this).closest('.regiter_xiala_ul').hide();
						groupDialog();
					});
					$('#selGroup >li').unbind('click');
					$('#selGroup >li').click(function(){
						$(this).closest('div').find('.value_show').val($(this).text());
						$(this).closest('div').find('.value_show').attr('data-value',$(this).attr('value'));
						$(this).parent().hide();
					});
				}
				//选择分组
				$('.tasklist-metting-choice li').unbind('click');
				$('.tasklist-metting-choice li').click(function(){
					$('.tasklist-metting-choice').hide();
					//创建分组
					if($(this).hasClass('create_group')){
						groupDialog();
					}else if('all_group' == $(this).attr('id')){
						$('.tasklist-metting-choice').attr('data-group-id','');
						$('#tasklist-title').text($(this).text());
						$('.tasklist-metting-color').attr('style','background-color:transparent');
						//刷新视图
						refashView();
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
						//刷新视图
						refashView();
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
	bindGroup();
	//group
	$('.tasklist-metting-title').click(function(){
		$('.tasklist-metting-choice').toggle();
	});
	//分组弹窗
	function groupDialog(groupId){
		$('.add_schedule_delete').hide();
		$('#txtGroupName').attr('data-group','');
		$('#txtGroupName').val('');
		$('#txtGroupColor').minicolors('value','');
		$('.mask-layer1').show();
		$('.mask-layer1').css('height',document.body.scrollHeight+'px');
		if(groupId && typeof(groupId) != 'undefined'){
			$('.tasklist-metting-choice >li').each(function(){
				if($(this).attr('id') == groupId){
					$('.add_schedule_delete').show();
					$('#txtGroupName').attr('data-group',groupId);
					$('#txtGroupName').val($(this).text());
					$('#txtGroupColor').minicolors('value',$(this).data('color'));
					return false;
				}
			});
		}
	}
	//选择分组
	$('.tasklist-metting-choice li').click(function(){
		$('.tasklist-metting-choice').hide();
		//创建分组
		if('create_group' == $(this).attr('id')){
			groupDialog();
		}else if('all_group' == $(this).attr('id')){
			$('.tasklist-metting-choice').attr('data-group-id','');
			$('#tasklist-title').text($(this).text());
			$('.tasklist-metting-color').attr('style','background-color:transparent');
			TaskListEventData.scheduleEvents = [];
			TaskListEventData.taskListEvents = [];
			bindSchedule();
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
			bindSchedule();
		}
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
	//编辑分组
	$('.tasklist-metting-choice li').find('#editGroup').click(function(){
		var groupId = $(this).attr('groupId');
		groupDialog(groupId);
	});
	
	//过滤定期日程
	$('#events-checkbox').change(function(){
		TaskListEventData.scheduleEvents = [];
		TaskListEventData.taskListEvents = [];
		bindSchedule();
	});
		
	$('#add_group_save').click(saveGroup);
	
	function saveGroup(){
		var groupId = $('#txtGroupName').attr('data-group');
		var groupName = $('#txtGroupName').val();
		if(!groupName){
			layer.msg(prop['schedule.alert.empty.groupName'], {icon : 3});
			return;
		}
		var color = $('#txtGroupColor').val();
    	if(!color){
    		layer.msg(prop['schedule.alert.empty.groupColor'], {icon : 3});
			return;
    	}
    	$.ajax({
			type:'post',
			data:
			{
				groupId:groupId,	
				groupName:encodeURI(groupName),
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
					layer.msg(globalProp['global.success.save'], {icon : 2});
					//刷新分组
					bindGroup();
					//刷新视图
					refashView();
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
							layer.msg(globalProp['global.success.save'], {icon : 2});
							//刷新分组
							bindGroup();
							//刷新视图
							refashView();
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
		var deltop = $(window).scrollTop();
		$(window).scrollTop(deltop);
		layer.confirm(prop['schedule.alert.delete.schedule'], {
			title:' ',btn: [globalProp['global.confirm'],globalProp['global.cancel']] //按钮
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
	}
	
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	} 
	
	$('.tab_list_tab').children().click(function(){
		if($(this).hasClass('now') && ($(this).text()=='Schedule'||$(this).hasClass('tab-schdule'))){
			$('.fc-today-button').trigger('click');
			//触发点击
			$('.fc-day').click(function(){
				var startDate = $(this).attr('data-date');
				$('.ifa-home-calendar').fullCalendar( 'select', startDate, '', true );
			});
		}
	});
	
	//动态输出内容绑定事件
	$("body").on("click",".pipelin-list-more li",function(){
		var dataName = $(this).attr("data-name");
		var customId = $(this).attr("data-value-customerId");
		if("setAppointment" == dataName ){
			$('.schedule-add-title').show();
			$('.schedule-edit-title').hide();
			clearForm('.register_table');
			$('#hidScheduleId').val('');
			$('#txtCustomer').removeAttr('data-value');
			$('#txtGroup').removeAttr('data-value');
			$('#txtRemind').removeAttr('data-value');
			$('.regiter_xiala_ul').hide();
			$('.alert-schedule-title-name').hide();
			$("#radEventType0").prop("checked",true);
			$('.repeat_typecon').hide();
			var week = new Date().getDay();
			$('#repeatDay-' + week).prop('checked', true);
			$('#add_Schedule').show();
			$('.schedule-dialog-creator').hide();
			$('#add_Schedule .space-mask').css('margin-top',$(window).scrollTop());
			$('.txtCustomer').val(dataName);
			$('#selCustomer li').each(function(){
				if(customId == $(this).attr('value')){
					$(this).click();
					$(this).closest('div').find('span').hide();
				}
			});
			// 字数控制
			titleLength();
			contentLength();
			// 时间控制
			$('#txtRepeatFrom').val(laydate.now());
			$('#txtStartAt').val(getDateStr(0)+' '+getTimeStr());
			$('#txtEndAt').val(endTimeAutoIncrease($('#txtStartAt').val()));
			//$('#txtEndAt').val(getDateStr(0)+' '+getTimeStr('h','1'));
			initDate('','');
		}
	});
	function getDateStr(type){
		var result = '';
		var date = new Date();
		var month = '';
		if((date.getMonth()+1)>9){
			month = (date.getMonth()+1);
		}else{
			month = '0'+(date.getMonth()+1);
		}
		var day = '';
		if(date.getDate()>9){
			day = date.getDate();
		}else{
			day = '0'+date.getDate();
		}
		var hour = '';
		if(date.getHours()>9){
			hour = date.getHours();
		}else{
			hour = '0'+date.getHours();
		}
		var minute = '';
		if(date.getMinutes()>9){
			minute = date.getMinutes();
		}else{
			minute = '0'+date.getMinutes();
		}
		var second = '';
		if(date.getSeconds()>9){
			second = date.getSeconds();
		}else{
			second = '0'+date.getSeconds();
		}
		var dateStr = date.getFullYear()+'-'+month+'-'+day; 
		var timeStr = hour+':'+minute+':'+second;
		
		if(type == '0'){
			result = dateStr;
		}else if(type == '1'){
			result = timeStr;
		}else{
			result = dateStr+' '+timeStr;
		}
		return result;
	}
	//表头滚动
	function taskListScrool(){
		if(!$('#schedule-view').is("div") || $('#schedule-view').is(":hidden")){
			return false;
		}else{
			var funds_hei =  $(".tasklist-header").height(),
			headerTop = $(".wmes_top").height(), 
			tabListTab = $(".tab_list_tab").height(), 
			clientTopRows = $(".client-top-rows").height(), 
			taskIcoWidth = $(".tasklist-date-ico").width()-50, 
			wmes_window_top = $(window).scrollTop(),
			infor_top = $(".line-wrap").offset().top;
			if ( wmes_window_top > (infor_top  - headerTop) ){
			 	var new_funds_top = wmes_window_top - infor_top - clientTopRows - tabListTab - 400;
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
		}
    };
    $(window).on('scroll',taskListScrool);
    
    $('#cheRecurrentFlag').change(function(){
    	if($('#cheRecurrentFlag')[0].checked){
    		$('.repeat_typecon').slideDown('fast');
    	}else{
    		$('.repeat_typecon').slideUp('fast');
    	}
    });
	$('.register_xiala_ico').click(function(){
		$(this).closest('div').find('.regiter_xiala_ul').toggle();
	});
	$('#txtTitle').on('input',titleLength);
	$('#txtContent').on('input',contentLength);
	// 字体长度控制
	function titleLength(){
		var inpLen = $('#txtTitle').val().length;
		$('#title-inputed').text(inpLen);
		$('#title-left').text(150-inpLen);
	}
	// 字体长度控制
	function contentLength(){
		var inpLen = $('#txtContent').val().length;
		$('#content-inputed').text(inpLen);
		$('#content-left').text(500-inpLen);
	}
	$('#add_schedule_cancel').click(function(){$('#add_Schedule').hide();$("#ifa-pipeline-date").removeClass("ifa-space-active");});
	$('#add_Schedule-close').click(function(){$('#add_Schedule').hide();$("#ifa-pipeline-date").removeClass("ifa-space-active");});
	// new save
	$('#add_schedule_save').click(function(){
		var id = $('#hidScheduleId').val();
//		if(typeof(id)=='undefined'){
//			var scheduleId = '';
//		}
		var title = $('#txtTitle').val();
       	var repeatFrom = '';
       	var repeatTo = '';
       	var startAt = $('#txtStartAt').val();
       	var startDate = '';
       	var startTime = '';
       	if(startAt && typeof(startAt)!='unedfined' && startAt.indexOf(' ')){
       		startDate = (startAt.split(' '))[0];
       		repeatFrom = (startAt.split(' '))[0];
       		startTime = (startAt.split(' '))[1];
       	}
       	var endAt = $('#txtEndAt').val();
       	var endDate = '';
       	var endTime = '';
       	if(endAt && typeof(endAt)!='unedfined' && endAt.indexOf(' ')){
       		endDate = (endAt.split(' '))[0];
       		repeatTo = (endAt.split(' '))[0];
       		endTime = (endAt.split(' '))[1];
       	}
       	var group = $('#txtGroup').attr('data-value');
       	var customerId = $('#txtCustomer').attr('data-value');
       	var remindSetting = $('#txtRemind').attr('data-value');
    	var ifImportant = '';
       	if($('#cheImportantFlag')[0].checked){
       		ifImportant = '1';
       	}else{
       		ifImportant = '0';
       	}
       	var content = $('#txtContent').val();
       	// 检验
		if(!title){layer.msg(prop['schedule.alert.empty.title'], {icon : 3});return;}
		var eventType = '0';
		if($('#cheRecurrentFlag')[0].checked){
			eventType = '1';
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
       	}else if(eventType == '0'){
       		repeatDay = '0000000';
       	}
		if(!startAt){layer.msg(layer.msg(prop['schedule.alert.empty.startTime'], {icon : 3}));return;}
		if(!endAt){layer.msg(layer.msg(prop['schedule.alert.empty.startTime'], {icon : 3}));return;}
       	var eventData = 
        {
        	start:startAt,end:endAt,//start,end必需
			id:id,eventType:eventType,title:encodeURI(title),
			strStartDate:startDate,strEndDate:endDate,
			strStartTime:startTime,strEndTime:endTime,
			repeatDay:repeatDay,groupId:group,
			strRepeatFrom:repeatFrom,strRepeatTo:repeatTo,
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
				bindSchedule();
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
		var id = getUrlParam('customerId');
		if(typeof id == 'undefined' || id == null){
			id = '';
		}
		var clientType = '';
		/*$('.noline_tab_tab li').each(function(){
			if($(this).hasClass('now') && $(this).data('li') == 'P'){
				clientType = '0';
			}else if($(this).hasClass('now') && $(this).data('li') == 'E'){
				clientType = '1';
			}
		});*/
		$.ajax({
			type:'post',
			data:{type:clientType,id:id},
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
					$('#selCustomer >li').unbind('click');
					$('#selCustomer >li').click(function(){
						$(this).closest('div').find('.value_show').val($(this).text());
						$(this).closest('div').find('.value_show').attr('data-value',$(this).attr('value'));
						$(this).parent().hide();
					});
					/*if(typeof id != 'undefined' && id != ''){
						$('#selCustomer li:gt(0)').each(function(){
							if(id == $(this).attr('value')){
								$(this).click();
								$(this).closest('div').find('span').hide();
							}
						});
					}*/
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
						//var nickName = n.nickName == null?'':n.nickName;
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
        $('.register_xiala_ico').show();
    }  
	
	$('#txtRemind,#txtGroup,#txtCustomer').click(function(){
		$(this).closest('.register_xiala_long').find('.regiter_xiala_ul').toggle();
	});
	
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
	
		bindSchedule();
	
 /**************************************** schedule end *********************************************/
});