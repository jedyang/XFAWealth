define(function(require) {
	var $ = require('jquery');
	require('layer');
	require("wmes_upload");
	$("#select_file").on("click",function(){
		$("#excel_input input[type='file'").trigger("click");
	});
	//上传控件初始化
	$("#excel_input").InitUploader({ 
		btntext: "",
		multiple: false, 
		filetypes: "xls,xlsx", //文件类型
		water: true, 
		thumbnail: true, 
		filesize: "8000",
		modulerelate:"corner",
		//执行解析文件
		successCallBack:function(filePath,orifilename){
			$("#excel_name").val(orifilename);
			$("#file_path").val(filePath);
		}
	});
	//保存解析
	$("#excel_save").on("click",function(){
		var filePath = $("#file_path").val();
		if(filePath == ""){
			layer.msg("请选择excel文件");
			return ;
		}
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+"/console/member/individual/excelInput.do?datestr=" + new Date().getTime(),
			data : {"filePath":filePath},
			success : function(data) {
				if (data.result) {
					layer.msg("导入成功");
				} else {
					var errorMsg = "导入失败：excel行数"+data.errorRow+",";
					if(data.errorType=="0"){
						errorMsg += loginCodeEmptyTip;
					}else if(data.errorType=="1"){
						errorMsg += existTip ;
					}else if(data.errorType=="2"){
						errorMsg += mobileNumEmptyTip;
					}else if(data.errorType=="3"){
						errorMsg += emailEmptyTip;
					}
					layer.msg(errorMsg);
				}
			}
		});
		
		
	});


});	