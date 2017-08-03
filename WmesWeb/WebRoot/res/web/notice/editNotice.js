define(function(require) {
	var $ = require('jquery');
			require('laydate');
			require('wmes_upload');
			require('layui');
			
	  var editindex;
	  layui.use('layedit', function(){
		  var layedit = layui.layedit;
		  layedit.set({
			  uploadImage: {
			    url: base_root + "/layeditUpload.do?moduleId=notice",
			    type: 'post'
			  }
		  });
		  editindex = layedit.build('textContent', {
			  height: 280,
			  tool: [ 'strong','italic','underline','del' ,'|' ,'left','center' ,'right','link'  ,'unlink' ,'image' ]
		  });
	  });
	
	/**
	 * save notice
	 */
	$('#btnPublish').click(publishNotice);
	function getFormData(){
		var id = $('#hidId').val();
		var subject = $('#txtTitle').val();
		var layedit = layui.layedit;
		var content = layedit.getContent(editindex);
		var level = $('input[name="level"]:checked').val();
		var target = $('input[name="target"]:checked').val();
		var accessoryFiles = [];
		$('.removeNoticeDoc').each(function(){
			var accessoryFile = $(this).attr('accessoryfile');
			if(accessoryFile){
				accessoryFiles.push(accessoryFile);
			}
		});
		return {
			id : id,
			accessoryFileId : encodeURI(JSON.stringify(accessoryFiles)),
			subject : encodeURI(subject),
			content : encodeURI(content),
			level : level,
			target : target
		};
	}
	/**
	 * publish
	 */
	function publishNotice(){
		if(!verifyData()){
			return;
		}
		var data = getFormData();
		var url = base_root + '/console/notice/saveNotice.do?d=' + Math.random();
		$.ajax({
			type : 'post',
			url : url,
			data : data,
			success : function(re){
				if(re.flag){
					layer.msg(props['global.success.save'], {icon : 2});
					setTimeout(function(){
						window.location.href = base_root + '/console/notice/list.do?d=' + Math.random() ;
					},500);
				}else{
					layer.msg(props['global.failed.save'], {icon : 1});
				}
			},error : function(){
				layer.msg(props['global.failed.save'], {icon : 1});
			}
		});
	}
	/**
	 * preview notice
	 */
	$('#btnPreview').click(function(){
		initDialogNotice();
		$('#previewNoticeDialog').addClass('dispaly-active');
		$('#previewNoticeDialog').show();
		var title = $('#txtTitle').val();
		var releaseDateTime = $('#txtReleaseDate').val();
		/*if(!releaseDateTime){
			releaseDateTime = getDateStr();
		}*/
		var releaseBy = $('#txtSourceByName').val();
		var layedit = layui.layedit;
		layedit.sync(editindex);
		var content = $('#textContent').val();
		$('.notice-dialog-title').text(title);
		$('.notice-dialog-release-date-time').text(releaseDateTime);
		$('.notice-dialog-release-by').text(releaseBy);
		$('.dialog-notice-content').html(content);
	});
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
	 * init dialog notice
	 */
	function initDialogNotice(){
		$('.notice-dialog-title').text('');
		$('.notice-dialog-release-date-time').text('');
		$('.notice-dialog-release-by').text('');
		$('.dialog-notice-content').text('');
	}
	/**
	 * verify data
	 */
	function verifyData(){
		var flag = true;
		// title
		var title = $('#txtTitle').val();
		if(!title){
			$('.notice-empty-title-tip').show();
			flag = false;
		}else{
			$('.notice-empty-title-tip').hide();
		}
		// content
		var layedit = layui.layedit;
		var content = layedit.getText(editindex);
		if(!content || content.length == 0){
			$('.notice-empty-content-tip').show();	
			flag = false;
		}else{
			$('.notice-empty-content-tip').hide();
		}
		return flag;
	}
	$('#txtTitle').on('input',function(){
		var title = $('#txtTitle').val();
		if(!title){
			$('.notice-empty-title-tip').show();
		}else{
			$('.notice-empty-title-tip').hide();
		}
	});
	$(document).ready(function(){
		var count = $('.noticeDocName').length;
		if(count > 2){
			$('.notice_attachment_upload').css('height', count*45);
		}
		$(".upload-album").InitUploader({ 
			btntext: props['notice.attachment.upload'],
			multiple: true, 
			water: true, 
			thumbnail: true, 
			//filetypes: null,
			filesize: "20480",
			modulerelate:"notice",
			successCallBack:function(filePath, orifilename){
				uploadNoticeDoc(filePath, orifilename);
			}
		});
	});
	$(document).on('mouseover','.noticeDoc',function(){
		$(this).find('.removeNoticeDoc').show();
	});
	$(document).on('mouseout','.noticeDoc',function(){
		$(this).find('.removeNoticeDoc').hide();
	});
	
	function uploadNoticeDoc(filePath, orifilename){
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + '/console/notice/uploadNoticeDoc.do?datestr=' + new Date().getTime(),
			data : {
				id:getUrlParam('id'),
				filePath:encodeURI(filePath),
				orifilename:encodeURI(orifilename)
			},
			success : function(data) {
				if (data.flag) {
					layer.msg(props['notice.uploaded.successfully']);
					filePath = data.accessoryFile.filePath;
					var html = '<div class="noticeDoc"><div style="float: left;margin-right: 50px;width: 600px;" class="">'
							 + '<a class="noticeDocName" style="color: blue;" href="' + base_root + '/download.do?fileName=' + filePath + '">'
							 + orifilename
							 + '</a></div>'
							 + '<div accessoryfile="' + data.accessoryFile.id + '" style="display: none; vertical-align: middle; margin: 5px 0px;" class="removeNoticeDoc webuploader-pick webuploader-pick-remove-hover">'
							 + props['notice.delete.attachment']
							 + '</div></div>';
					var height = $('.notice_attachment_upload').height()+45;
					$('.notice_attachment_upload').css('height', height);
					$('.upload-box').before(html);
				} else {
					layer.msg(props['notice.uploaded.failed']);
				}	
			},error : function() {
				layer.msg(props['notice.uploaded.failed']);
			}
		});
	}
	$(document).on('click', '.removeNoticeDoc', function(){
		var self = this;
		var accessoryfile = $(self).attr('accessoryfile');
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + '/console/notice/removeNoticeDoc.do?datestr=' + new Date().getTime(),
			data : {
				accessoryfileId:accessoryfile
			},
			success : function(data) {
				if (data.flag) {
					layer.msg(props['notice.deleted.successfully']);
					$(self).closest('.noticeDoc').remove();
					var height = $('.notice_attachment_upload').height()-45;
					$('.notice_attachment_upload').css('height', height);
				} else {
					layer.msg(props['notice.deleted.failed']);
				}	
			},error : function() {
				layer.msg(props['notice.deleted.failed']);
			}
		});
	});
	/***************************************************************************************************/
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
});