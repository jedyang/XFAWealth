define(function(require) {
	var $ = require('jquery');
			require('layui');
	var angular = require('angular');	
	
	/**
	 * bind E-mail Records
	 */
	var emailRecordsAngular = angular.module('emailRecords', ['truncate','wmespage']);
	emailRecordsAngular.controller('emailRecordsCtrl', ['$scope', '$http', function($scope, $http) {
		var rows_ = 10;
		$scope.bindEmailRecords = function (page){
			var url = base_root + '/front/crm/proposal/getEmailRecords.do?d=' + new Date().getTime();
			$http({
	            url : url,
	            method :'POST',
	            params : {
	            	proposalId : getUrlParam('id'),
	            	page : page,
	            	rows : rows_
	            }
	       	}).success(function(re){
	       		if(re.flag && re.records != null){
	       			$scope.emailRecordsConf.totalItems = re.records.length;
	       			$scope.emailRecords = re.records;
	       		}
	       	});
		}
		$scope.emailRecordsConf = {
            itemsPerPage:rows_,
            onChange: function(){
            	var page = $scope.emailRecordsConf.currentPage;
            	$scope.bindEmailRecords(page);
            }
        };
		$scope.bindEmailRecords(1);
	}]);
	
	/**************************************Notice Start***********************************************/
	/**
	 * init dialog notice
	 */
	function initDialogNotice(){
		$('.email-dialog-title').val('');
		$('.email-record-to-email').val('');
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
	$(document).on('click','.email-preview',function(){
		initDialogNotice();
		$('#previewNoticeDialog').find('.selectSector').css('box-shadow',null);
		$('#previewNoticeDialog').find('.selectSector').css('background',null);
		$('#previewNoticeDialog').addClass('dispaly-active');
		$('#previewNoticeDialog').show();
		var title = $(this).text();
		if(typeof title != 'undefined'){
			title = title.trim();
		}
		var toEmail = $(this).data('toEmail');
		var content = $(this).data('content');
		$('.email-dialog-title').val(title);
		$('.email-record-to-email').val(toEmail);
		if(content != 'undefined'){
			$('.dialog-notice-content').html(decodeURI(content));
		}
	});	
	/**************************************Notice End*************************************************/
	
	
	
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
