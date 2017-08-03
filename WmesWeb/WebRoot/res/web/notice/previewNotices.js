define(function(require) {
	var $ = require('jquery');
			require('layui');
	var angular = require('angular');	
	
	/**
	 * bind announcement
	 */
	var announcementAngular = angular.module('announcement', ['truncate','wmespage']);
	announcementAngular.controller('announcementCtrl', ['$scope', '$http', function($scope, $http) {
		var rows_ = 10;
		$scope.bindAnnouncement = function (type,page){
			var url = base_root + '/front/notice/getAnnouncement.do?d=' + new Date().getTime();
			$http({
	            url : url,
	            method :'POST',
	            params : {
	            	type : type,
	            	page : page,
	            	rows : rows_
	            }
	       	}).success(function(re){
	       		 if('fund' == type){
	       			$scope.fundAnnoConf.totalItems = re.total;
	       			$scope.fundNotices = re.rows;
	       		 }else if('system' == type){
	       			$scope.sysNoticeConf.totalItems = re.total;
	       			$scope.sysNotices = re.rows;
	       		 }
	       	});
		}
		$scope.fundAnnoConf = {
            itemsPerPage:rows_,
            onChange: function(){
            	var page = $scope.fundAnnoConf.currentPage;
            	$scope.bindAnnouncement('fund',page);
            }
        };
		$scope.sysNoticeConf = {
			itemsPerPage:rows_,
			onChange: function(){
				var page = $scope.sysNoticeConf.currentPage;
				$scope.bindAnnouncement('system',page);
			}
		};
		$scope.bindAnnouncement('fund',1);
		function initTab(){
			if('system' == getUrlParam('type')){
				$('.myzone-notice-th li').eq(1).addClass('myzone-notice-thliac').siblings().removeClass('myzone-notice-thliac');
				$('.notice-list-cut').eq(1).addClass('notice-list-cutac').siblings().removeClass('notice-list-cutac');
				$scope.bindAnnouncement('system',1);
			}else{
				$('.myzone-notice-th li').eq(0).addClass('myzone-notice-thliac').siblings().removeClass('myzone-notice-thliac');
				$('.notice-list-cut').eq(0).addClass('notice-list-cutac').siblings().removeClass('notice-list-cutac');
			}
		}
		initTab();
	}]);
	
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
		var title = $(this).attr('title');
		var releaseDateTime = $(this).data('releaseDate');
		var sourceName = $(this).data('sourceName');
		var content = $(this).data('content');
		$('.notice-dialog-title').text(title);
		$('.notice-dialog-release-date-time').text(releaseDateTime);
		$('.notice-dialog-release-by').text(sourceName);
		if(content != 'undefined'){
			//$('.dialog-notice-content').html(decodeURI(content));
			$('.dialog-notice-content').html(content);
		}
	});	
	$(document).on('click','.myzone-notice-th li',function(){
		$(this).addClass('myzone-notice-thliac').siblings().removeClass('myzone-notice-thliac');
		$('.notice-list-cut').eq($(this).index()).addClass('notice-list-cutac').siblings().removeClass('notice-list-cutac');
	});
	/**
	 * getUrlParam
	 */
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
});	
