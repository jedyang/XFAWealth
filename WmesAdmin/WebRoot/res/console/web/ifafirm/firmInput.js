define(function(require) {
	var $ = require('jquery');
		require('layer');
		require("wmes_upload");
		
	$(document).ready(function(){
		//页面第一次加载时选第一个类别			
		$(".upload-album").InitUploader({ 
			btntext: "上传图片", 
			multiple: true, 
			water: true, 
			thumbnail: true, 
			filesize: "8000",
			modulerelate:"portrait",
			successCallBack:function(filePath,orifilename){
				$('#imgFile').val(filePath);
				//刷新预览
				$('#imgFile').attr('src',base_root+"/loadImgSrcByPath.do?filePath="+filePath);
				//console.log(orifilename);
			}
		});
	});
	/*
	function initUpload(){
	  $(".upload-album").InitUploader({ 
			btntext: "上传图片", 
			multiple: true, 
			water: true, 
			thumbnail: true, 
			filesize: "8000",
			modulerelate:"firmlogo" 
	  });
	}
  	  
	$(".upload-album").on("click",function(){
	
	});*/	
});