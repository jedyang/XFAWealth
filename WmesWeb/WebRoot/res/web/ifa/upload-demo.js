define(function(require) {
	'use strict';
	var $ = require('jquery');
	require('layer');
	require("jqueryForm");
	require("wmes_upload");
	var interfaceObj = require("interfaceCheckPwd");
	//require("ueditor_ueConfig");//配置文件
	//require("ueditor_ue");//配置文件

    //var ue = UE.getEditor('container');
	//$("#aaaaaaaa").click(function(){interfaceObj.showDialog();});
    $('#btn-save').click(function(){
    	$(".upload-eachimg").autoIMG();
    	var title = $('#txtTitle').val();
    	//获取上传图片信息，并转成HTML
    	var photoHtml = '<div class="photo-list"><ul>';
    	var imgsrc = '';
    	var imgList = $('.photo-list ul li');
    	$.each(imgList,function(i,n){
    		var src = $(this).find('img').attr('bigsrc');
    		var li_html = '<li><div class="img-box selected" ><img src="'+src+'" bigsrc="'+src+'"></div></li>';
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
    	//var content222 = UE.getEditor('container').getContent();
    	//保存主题信息
		  $.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/info/saveCornerInfo1.do?datestr="+ new Date().getTime(),
				data : {'title':title,'content':html,'imgpath':""},
				beforeSend: function () {},
				complete: function () {},
				success : function(json) {
					////console.log(json);
					var obj = $.parseJSON(json); 
					if(obj.result){
						layer.msg('Reply Message Successful!', {icon: 1});
						//点赞后页面的赞数+1效果
						var oldLikedCount = self.next('span').text();
						var newLikedCount = parseInt(oldLikedCount)+1;
						self.next('span').text(newLikedCount);
					}
				}
			});
    });
    
  
    //var jsonlist = JSON.parse('{"accountlist":"mqzou001|1,mic123|1,mqzou004|1","targetid":"txtTitle"}');
    //$("#btninterface").InitInterface({ jsonlist:jsonlist,'isopen':'1' });
    
        
    
    $(".upload-album").InitUploader({ uploadtype:'image',bgimagetype:3, btntext: "上传图片", multiple: false, water: true, thumbnail: true, filesize: "8000",modulerelate:"corner"});
    $(".upload-mulalbum").InitUploader({ uploadtype:'image',bgimagetype:3,btntext: "", multiple: true, water: true, thumbnail: true, filesize: "8000",modulerelate:"corner" });
    $(".upload-limitalbum").InitUploader({ uploadtype:'image',bgimagetype:4,btntext: "", multiple: true, water: true, thumbnail: true, filesize: "8000",modulerelate:"corner",count:3 });
    //upload other file ,docx doc....
    $(".upload-docalbum").InitUploader({btntext: "upload pdf", multiple: true, water: true, thumbnail: false, filesize: "8000",modulerelate:"corner",count:3 });
    //$(".upload-video").InitUploader({ uploadtype:"video",btntext: "上传视频",filesize: "1024000", sendurl: "../../tools/upload_ajax.ashx", swf: base_root + '/res/js/Uploader.swf', filetypes: "flv,mp3,mp4,avi" });
  //删除图片LI节点
    $("body").on('click','.class-delimg',function(){
    	var parentObj = $(this).parent().parent();
        var focusPhotoObj = parentObj.parent().siblings(".focus-photo");
        var smallImg = $(this).siblings(".img-box").children("img").attr("src");
        $(this).parent().remove(); //删除的LI节点

        if (focusPhotoObj.val() == smallImg) {
            focusPhotoObj.val("");
            var firtImgBox = parentObj.find("li .img-box").eq(0); 
            firtImgBox.addClass("selected");
            focusPhotoObj.val(firtImgBox.children("img").attr("src"));
        }
    	
    });
  
});	