define(function(require) {
	'use strict';
	var $ = require('jquery');
	require('layer');
	require("jqueryForm");
	require("wmes_upload");
	//require("ueditor_ueConfig");//配置文件
	//require("ueditor_ue");//配置文件
    //绑定二个上传控件
	$('#upload-video').hide();
	$(".upload-album").InitUploader({ uploadtype:'image',bgimagetype:3,btntext: "", multiple: true, water: true, thumbnail: true, filesize: "8000",modulerelate:"corner" });
	//$(".upload-video").InitUploader({ uploadtype:'video',bgimagetype:4, btntext: "上传图片", multiple: false, water: true, thumbnail: true, filesize: "8000",modulerelate:"corner"});
	//绑定单选radio事件
	$('input[name="uploadtype"]').on('click',selectuploadtype);
	//获取单选radio选中图片还是视频
	function selectuploadtype(){
		var uploadtype = $('input[name="uploadtype"]:checked').val();
		if(uploadtype=='1'){
			$('#upload-image').hide();
			$('#upload-video').show();
		} else{
			$('#upload-image').show();
			$('#upload-video').hide();
		}
	}
	//保存发表的圈子信息
    $('#btn-save').click(function(){
    	var title = $('#txtTitle').val();
    	if(title==''){ layer.msg('please input the message'); return; }
    	else {
    		//获取上传图片信息，并转成HTML
        	var photoHtml = '<div class="photo-list"><ul>';
//      	var imgsrc = '';
        	var imgList = $('.photo-list ul li');
        	$.each(imgList,function(i,n){
        		var src = $(this).find('img').attr('src');
        		var bigsrc = $(this).find('img').attr('bigsrc');
        		var li_html = '<li><div class="img-box selected" ><img src="'+src+'" bigsrc="'+bigsrc+'"></div></li>';
        		photoHtml += li_html;
        	});
        	photoHtml += '</ul></div><div style="clear:both;"></div>';
        	//获取上传视频
        	var videoHtml = '<div class="video-list"><ul>';
        	var videoList = $('.video-list ul li');
        	$.each(videoList,function(i,n){
        		var video = $(this).find('img').attr('bigsrc');
        		var li_html = '<li><div class="img-box selected" ><video src="'+video+'" controls="controls" width="320px" heigt="200px"></video></div></li>';
        		videoHtml += li_html;
        	});
        	videoHtml += '</ul></div><div style="clear:both;"></div>';
        	var html = photoHtml + videoHtml;
    		  $.ajax({
    				type : 'post',
    				datatype : 'json',
    				url : base_root + "/front/ifa/info/saveCornerInfo.do?datestr="+ new Date().getTime(),
    				data : {'title':title,'content':html,'imgpath':""},
    				beforeSend: function () {},
    				complete: function () {},
    				success : function(json) {
    					////console.log(json);
    					var obj = $.parseJSON(json); 
    					if(obj.result){
    						layer.msg(' Save Corner Info Successful!', {},function(){window.location.href=base_root + '/front/ifa/info/discover.do?type=Corners';});
    						//点赞后页面的赞数+1效果
    						var oldLikedCount = self.next('span').text();
    						var newLikedCount = parseInt(oldLikedCount)+1;
    						self.next('span').text(newLikedCount);
    					}
    				}
    			});
    	}
    	
    });
    
    $('#txtTitle').on('input',function(){
		var inpLen = $(this).val().length;
//		var id = $(this).attr('id');
		if(inpLen > 2000){
			$('#txtInvestmentGoal-inputed').text(2000);
			$('#txtInvestmentGoal-left').text(0);
			var value = $(this).val().substring(0, 2000);
			$(this).val(value);
		}else{
			$('#txtInvestmentGoal-inputed').text(inpLen);
			$('#txtInvestmentGoal-left').text(2000-inpLen);
		}
	});
  
});	