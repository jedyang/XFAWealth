define(function(require) {
	var $ = require('jquery');
		require('layer');
		require("wmes_upload");
		
	$(document).ready(function(){
		//页面第一次加载时选第一个类别			
		$(".upload-album").InitUploader({ 
			btntext: langMutilForJs["global.uploadImage"], 
			multiple: true, 
			water: true, 
			thumbnail: true, 
			filesize: "3000",
			lang:lang,
			modulerelate:"fundCompanyLogo",
			filetypes: "jpg,png,jpeg,gif", //文件类型
			successCallBack:function(filePath,orifilename){
				$('#imgLogoUrl').val(filePath);
				//刷新预览
				$('#imgLogoUrl').attr('src',base_root+"/loadImgSrcByPath.do?filePath="+filePath);
				
			}
		});
	});

});