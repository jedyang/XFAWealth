define(function(require){
	
	seajs.use(base_root + '/res/console/js/jquery-1.11.2.min.js');
	seajs.use(base_root + '/res/third/jqueryui/jquery-ui.js');
	seajs.use(base_root + '/res/console/js/js.js');
	
	var $ = require('jquery');
	require('layer');
	require('wmes_upload');
	
	var id = getUrlParam('id');
	var fundId = getUrlParam('fundId');
    //绑定查询按钮事件
    $("#btnSave").click(function () {  
    	var documentName = $('#txtDocumentName').val();
    	var filePath = $('#filePath').val();
    	if(!filePath){
    		layer.msg(docEmpty);
    		return ;
    	}
    	saveFeesItem(fundId,id,documentName,filePath);        	 
    });	
	
	$(".upload-album").InitUploader({ 
		btntext: uploadTitle,
		multiple: false, 
		filetypes: "doc,docx", //文件类型
		water: false, 
		thumbnail: false, 
		filesize: "8000",
		modulerelate:"fund_doc",
		//执行解析文件
		successCallBack:function(filePath,orifilename){
			/*
			$.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root+"/console/fund/info/docInput.do?datestr=" + new Date().getTime(),
				data : {"filePath":filePath},
				success : function(data) {
					if (data.result) {
						layer.msg("导入成功");
					} else {
						layer.msg(data.msg+" "+data.reason);
					}
				}
			});
			*/
			$('#filePath').val(filePath);
			$('#txtDocumentName').val(orifilename);
		}
	});
        
    $("#btnBackEn").click(function () {  
        	backToList();	
    });	
});
  
  	//保存数据
	function saveFeesItem(fundId,id,documentName,filePath){
		if(!fundId){
			layer.msg(fundEmpty);
			return ;
		}
		if(!documentName){
			layer.msg(docEmpty);
			return ;
		}
		if(!filePath){
			layer.msg(docEmpty);
			return ;
		}
		$.ajax({
			type : 'post',
			dataType : 'json',
			url : base_root + '/console/fund/info/saveDoc.do?datestr='+new Date().getTime(),
			data : {
				'fundId' : fundId,
				'id' : id,
				'documentName' : documentName,
				'filePath' : filePath
			},
			success : function(data) {
				var result = data.result;
				if(true==result) { 
					layer.msg(saveSuccess, {icon: 1}, function () { 
						setTimeout(function(){
							parent.document.getElementById("btnSearch").click();
			    			parent.document.getElementById("btnCloseIframe").click(); 
						},500)});          			
				} else { 
					layer.msg(saveFail, {icon: 0, time: 2000}, function () {   });
				}
			}
		});
	}
	
	function backToList(){
		var fundId = getUrlParam('fundId');
		window.location.href = '${base}/console/fund/info/fundDocList.do?fundId='+fundId;
	}
	
	//改变选择的文件
    function change(){  
        document.getElementById("upload_file_tmp").value=document.getElementById("upload_file").value;  
    }