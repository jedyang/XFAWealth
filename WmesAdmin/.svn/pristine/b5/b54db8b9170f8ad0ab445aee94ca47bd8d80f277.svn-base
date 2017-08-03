define(function(require) {

	var $ = require('jquery');
		require('layer');
		require("wmes_upload");

	//截取路径头
	var _src = base_root+'/loadImgSrcByPath.do?filePath=';
	
	//页面第一次加载时选第一个类别			
	$("#logo").InitUploader({ 
		btntext: langMutilForJs["global.uploadImage"], 
		multiple: true, 
		water: true, 
		thumbnail: true, 
		filesize: "1000",
		modulerelate:"company",
		successCallBack:function(filePath,orifilename){
			$('#imgLogoUrl').attr("value",filePath);
			//刷新预览
			$('#imgLogoUrl').attr('src',_src+filePath);
			//console.log(orifilename);
		}
	});
	
	//页面第一次加载时选第一个类别			
	$("#background").InitUploader({ 
		btntext: langMutilForJs["global.uploadImage"], 
		multiple: true, 
		water: true, 
		thumbnail: true, 
		filesize: "4000",
		modulerelate:"company",
		successCallBack:function(filePath,orifilename){
			$('#imgBackgroundUrl').attr("value",filePath);
			//刷新预览
			$('#imgBackgroundUrl').attr('src',_src+filePath);
			//console.log(orifilename);
		}
	});

	
    $('#btnBack').on('click',back);
    function back() {
        window.location.href = base_root+'/console/company/list.do?';
    }

    //保存信息
    $('#btnSave').on('click',function(){
        //console.log("lg="+$('#imgLogoUrl').attr("value"));
        //console.log("bg="+$('#imgBackgroundUrl').attr("value"));
        //非空判断
        if(null==$('#txtCode').val()||""==$('#txtCode').val()){
            layer.msg(langMutilForJs["global.companyCodeTips"]);
            return ;
        }
        if(null==id||""==id){
            //新增时检查公司编码唯一性
            if (!checkCodeUnique()) return;
        }
        if((null==$('#txtNameSc').val()||""==$('#txtNameSc').val())){
            layer.msg(langMutilForJs["global.companyNameScTips"]);
            return ;
        }
        if((null==$('#txtNameTc').val()||""==$('#txtNameTc').val())){
            layer.msg(langMutilForJs["global.companyNameTcTips"]);
            return ;
        }
        if((null==$('#txtNameEn').val()||""==$('#txtNameEn').val())){
            layer.msg(langMutilForJs["global.companyNameEnTips"]);
            return ;
        }
        if(null==$('#imgLogoUrl').attr("value")||""==$('#imgLogoUrl').attr("value")){
            layer.msg(langMutilForJs["global.companyUploadLogoTips"]);
            return ;
        }
        if(null==$('#imgBackgroundUrl').attr("value")||""==$('#imgBackgroundUrl').attr("value")){
            layer.msg(langMutilForJs["global.companyUploadBackgroundTips"]);
            return ;
        }
        
        save();
    });
    
    //检查公司编码唯一性
    function checkCodeUnique(){
        var code = $('#txtCode').val();
        if (code==null || code==""){
        	layer.msg(langMutilForJs["global.companyCodeTips"]);
        	return false;
        }
        var result = false;
        $.ajax({
            type: "post",
            url : base_root+"/console/company/checkCodeUnique.do?",
            data: {'code':code},
            async: false,
            dataType: "json",
            error: function (XMLHttpRequest, textStatus, errorThrown) {},
            success: function (data) {
                result = data.result;
                if(!result)
                	layer.msg(langMutilForJs["global.companyCodeExistTips"]);
            }
        });
        return result;
    }
    
    function save() {
        //获取地址栏参数
        var id = getQueryString('id');
        var code = $('#txtCode').val() == null ? "" : $('#txtCode').val();
        var webUrl = $('#txtWebUrl').val() == null ? "" : $('#txtWebUrl').val();
        var logoUrl = $('#imgLogoUrl').attr("value") == null ? "" : $('#imgLogoUrl').attr("value");
        var backgroundUrl = $('#imgBackgroundUrl').attr("value") == null ? "" : $('#imgBackgroundUrl').attr("value");
        var cssUrl = $('#txtCssUrl').val() == null ? "" : $('#txtCssUrl').val();
        var loginUrl = $('#txtLoginUrl').val() == null ? "" : $('#txtLoginUrl').val();
        //SC
        var nameSc = $('#txtNameSc').val() == null ? "" : $('#txtNameSc').val();
        var sysNameSc = $('#txtSysNameSc').val() == null ? "" : $('#txtSysNameSc').val();
        var copyrightSc = $('#txtCopyrightSc').val() == null ? "" : $('#txtCopyrightSc').val();
        //TC
        var nameTc = $('#txtNameTc').val() == null ? "" : $('#txtNameTc').val();
        var sysNameTc = $('#txtSysNameTc').val() == null ? "" : $('#txtSysNameTc').val();
        var copyrightTc = $('#txtCopyrightTc').val() == null ? "" : $('#txtCopyrightTc').val();
        //EN
        var nameEn = $('#txtNameEn').val() == null ? "" : $('#txtNameEn').val();
        var sysNameEn = $('#txtSysNameEn').val() == null ? "" : $('#txtSysNameEn').val();
        var copyrightEn = $('#txtCopyrightEn').val() == null ? "" : $('#txtCopyrightEn').val();
        
        var postData = { 
            'id' : id,
            'code' : code,
            'webUrl' : webUrl,
            'logoUrl' : logoUrl,
            'backgroundUrl' : backgroundUrl,
            'cssUrl' : cssUrl,
            'loginUrl' : loginUrl,
            'nameSc' : nameSc,
            'sysNameSc' : sysNameSc,
            'copyrightSc' : copyrightSc,
            'nameTc' : nameTc,
            'sysNameTc' : sysNameTc,
            'copyrightTc' : copyrightTc,
            'nameEn' : nameEn,
            'sysNameEn' : sysNameEn,
            'copyrightEn' : copyrightEn
        }; 
        $.ajax({
            type: "post",
            url : base_root+"/console/company/save.do?",
            data: postData,
            async: false,
            dataType: "json",
            //beforeSend: function () { layer.msg("正在保存数据中......", { time: 50000 });},
            //complete: function () {},
            error: function (XMLHttpRequest, textStatus, errorThrown) {},
            success: function (data, textStatus) {
                var result = data.result;
                if(result==true) { 
                       layer.msg(langMutilForJs["global.success.save"], {icon: 1, time: 2000}, function () { 
                           parent.document.getElementById("btnSearch").click();
                           parent.document.getElementById("btnCloseIframe").click(); 
                       });                      
                } else  layer.msg(langMutilForJs["global.failed.save"], {icon: 0, time: 2000});
            }
        });
    }	
});