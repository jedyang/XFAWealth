define(function(require) {
	var $ = require('jquery');
			require('laydate');
			require('layui');
	var angular = require('angular');
	/**
	 * filter time
	 */
	$(document).on('click','#startTime',startTime);
	function startTime(){
		laydate({
			istime: true,
			istoday: true,
			isclear: true,
			elem: '#startTime',
			format: 'YYYY-MM-DD hh:mm:ss',
			choose:function(){}
		});
	}
	$(document).on('click','#endTime',endTime);
	function endTime(){
		laydate({
			istime: true,
			istoday: true,
			isclear: true,
			elem: '#endTime',
			format: 'YYYY-MM-DD hh:mm:ss',
			choose:function(){}
		});
	}
	var orderPlanAngular = angular.module('notice', ['truncate','wmespage']);
	orderPlanAngular.controller('noticeCtrl', ['$scope', '$http', function($scope, $http) {
		var rows_ = 10;
		$scope.bindNoticeData = function (){
			var page_ = $('#hidPage').val();
			var order_ = $('#hidOrder').val();
			var sort_ = $('#hidSort').val();
			var title = $('#txtTitle').val();
			var level = $('#selLevel').val();
			var target = $('#selTarget').val();
			var releaseName = $('#txtReleaseName').val();
			var startTime = $('#startTime').val();
			var endTime = $('#endTime').val();
			var keyWord = $('#txtKeyWord').val();
			var data = {
				order : order_,
				sort : sort_,
				page : page_,
				rows : rows_,
				subject : title,
				releaseName : releaseName,
				target : target,
				level : level,
				startTime : startTime,endTime : endTime,
				keyWord:keyWord,
			};
			var url = base_root + '/console/notice/noticeJson.do?d=' + new Date().getTime();
       		$.ajax({
				type:'post',
				url:url,
				data:data,
				success:function(re){
		       		var total = re.total;
					$scope.notices = re.rows;
					$('.table-rows-total').text(total);
					if(0 == Number(total) || $scope.notices.length == 0){
						$('.notice-nodata-tips').show();
						$('#hidPage').val(1);
						$scope.noticeConf.totalItems = 0;
					}else{
						$('.notice-nodata-tips').hide();
						$scope.noticeConf.totalItems = total;//设置总数
					}
					$scope.$apply();
				}
	       	});
		};
		$scope.noticeConf = {
            itemsPerPage:rows_,
            onChange: function(){
            	$('#hidPage').val($scope.noticeConf.currentPage);
            	$scope.bindNoticeData();
            }
        };
		$scope.bindNoticeData();
		/**
		 * order
		 */
		$('.notice_order').click(function(){
			noticeOrder(this);
		});
		function noticeOrder(self){
			if($(self).find('.arrow_top_release_date').hasClass('top_active')){
				$('.notice_order span').removeClass('down_active').removeClass('top_active');
				$(self).find('.arrow_down_release_date').addClass('down_active');
				// releaseDate DESC
				$('#hidSort').val('n.releaseDate');
				$('#hidOrder').val('DESC');
			}else{
				$('.notice_order span').removeClass('down_active').removeClass('top_active');
				$(self).find('.arrow_top_release_date').addClass('top_active');
				// releaseDate ASC
				$('#hidSort').val('n.releaseDate');
				$('#hidOrder').val('ASC');hidOrder
			}
			$scope.bindNoticeData();
		}
		$(document).on('change', '#txtTitle,#txtReleaseName,#selLevel,#selTarget', function(){
			$scope.bindNoticeData();
		});
		/**
		 * delete
		 */
		$(document).on('click','.notice-actions-delete',function(){noticeDelete(this)});
		function noticeDelete(self){
			layer.confirm(props['notice.list.alert.action.delete'],{title:props['global.info'],btn:[props['global.confirm'],props['global.cancel']]},
			function(index){
				var noticeId = $(self).closest('tr').attr('id');
				var url = base_root + '/console/notice/dateleNotice.do?d=' + new Date().getTime();
				$.ajax({
					type:'post',
					url:url,
					data:{
						id:noticeId
					},success:function(re){
						if(re.flag){
							layer.msg(props['global.delete.success'], {icon : 2});
						}else{
							layer.msg(props['global.delete.failed'], {icon : 1});
						}
						$scope.bindNoticeData();
					},error:function(){
						layer.msg(props['global.delete.failed'], {icon : 1});
						$scope.bindNoticeData();
					}
				});
			});
		}
		
		$('.notice-filter-select-btn').click(function(){
			var startTime = $('#startTime').val();
			var endTime = $('#endTime').val();
			if(startTime && endTime){
				startTime = new Date(startTime).getTime();
				endTime = new Date(endTime).getTime();
				if(startTime > endTime){
					layer.msg(props['schedule.alert.startTime.control'],{icon:3});
				}else{
					$scope.bindNoticeData();
				}
			}
		});
		
		$('#btnKeyWordSearch').click(function(){
			$scope.bindNoticeData();
		});
		$(document).keypress(function(e) {  
	       if(e.which == 13) {  
	    	   $scope.bindNoticeData();  
	       }  
	    }); 
	}]);
	/**
	 * change status
	 */
	function changeStatus(id,status){
		var url = base_root + '/front/notice/updateNoticeStatus.do?d=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				id:id,
				status:status
			},success:function(re){
				if(re.flag){
					layer.msg(props['global.successful.operation'], {icon : 2});
					bindNoticeData();
				}else{
					layer.msg(props['global.operation.failed'], {icon : 1});
					bindNoticeData();
				}
			},error:function(){
				layer.msg(props['global.operation.failed'], {icon : 1});
				bindNoticeData();
			}
		});
	}
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
		$('#previewNoticeDialog').addClass('dispaly-active');
		$('#previewNoticeDialog').show();
		var title = $(this).text();
		var releaseDateTime = $(this).closest('tr').find('.notice-table-release-date').text();
		var releaseBy = $(this).attr('sourceName');
		var content = $(this).closest('tr').find('.notice-table-content').val();
		$('.notice-dialog-title').text(title);
		$('.notice-dialog-release-date-time').text(releaseDateTime);
		$('.notice-dialog-release-by').text(releaseBy);
		if(content != 'undefined'){
			//$('.dialog-notice-content').html(escape2Html(decodeURI(content)));
			$('.dialog-notice-content').html(escape2Html(content));
		}
	});
	
	/***************************************************************************************************/
	/**
	 * endTime Auto Increase
	 */
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
				if(year%4==0 && day=='28'){
					day = '1';
					month = Number(month)+1;
				}else if(year%4!=0 && day=='29'){
					day = '1';
					month = Number(month)+1;
				}else{
					day = Number(day)+1;
				}
			}
			if(month.toString().length == 1){month = '0'+month;}
			if(day.toString().length == 1){day = '0'+day;}
			if(hour.toString().length == 1){hour = '0'+hour;}
			if(minute.toString().length == 1){minute = '0'+minute;}
			if(second.toString().length == 1){second = '0'+second;}
			endTime = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;
		}
		return endTime;
	}
	/**
	 * checkEndTime
	 * @param obj0
	 * @param obj1
	 * @returns
	 */
	function checkEndTime(obj0,obj1){ 
	    var start = obj0.replace(/-/g, '').replace(/:/g,'').replace(' ','');  
	    var end = obj1.replace(/-/g, '').replace(/:/g,'').replace(' ','');  
	    if(Number(end) > Number(start))return false;  
	    else return true;  
	}  
	function escape2Html(str) {
	  var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
	  return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
	}
});