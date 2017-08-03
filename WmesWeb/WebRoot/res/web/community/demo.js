define(function(require) {
	var $ = require('jquery');
	require('layer');

	var editindex;
	layui.use('layedit', function(){
		  var layedit = layui.layedit;
		  layedit.set({
			  uploadImage: {
			    url: base_root + "/wmesUpload.do?moduleRelate=aaaaaa&uploadType=image" //接口url
			    ,type: 'post' //默认post
			  }
			});
			//注意：layedit.set 一定要放在 build 前面，否则配置全局接口将无效。
		  editindex = layedit.build('demo',{ height: 420 }); ////设置编辑器高度 建立编辑器
	});
	
	$("#btnsave").on('click',function(){
		var layedit = layui.layedit;
		var content = layedit.getContent(editindex);
		alert(content);
	});
});