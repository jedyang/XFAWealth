
define(function(require) {
	'use strict';
	var $ = require('jquery');
	require('laydate');
	require('layer');
	require("wmes_upload");
	
	$(document).ready(function(){
		//页面第一次加载时选第一个类别			
	    initUpload();
	});
		
	setTimeout(function(){
	    console.log(date_format);
        laydate({
              istime: false,
              elem: '#incorporationDate',
              max: laydate.now(-1),
              format: date_format, //保存在会话中的用户日期格式（head_v2.html）
              choose: function(dates){ //选择日期完毕的回调
                   $("#incorporationDate").val(formatDate(dates,date_format));  
                   //alert('得到：'+datas);
              }
            });
    },200);
	
	$("#saveFirmInfo").on("click",function(){
		
		var id = $('#hidIfaFirmId').val();
		var entitytype = $('#entityType').val();
		var registerNo = $('#registerNo').val();
		var isFinancial = $('#isFinancial').val();
		var giin = $('#giin').val();
		var naturePurpose = $('#naturePurpose').val();//
		var incorporationDate = $('#incorporationDate').val();//
		var incorporationPlace = $('#incorporationPlace').val();
		var registeredAddress = $('#registeredAddress').val();
		var mailingAddress = $('#mailingAddress').val();
		var country = $('#country').val();
		var website = $('#website').val();//
		var companyNameSc = $('#companyNameSc').val();
		var companyNameTc = $('#companyNameTc').val();//
		var companyNameEn = $('#companyNameEn').val();//
		var superCheckType = $('#superCheckType').val();//

		//获取上传图片信息，并转成HTML
//		var photoHtml = '<div class="photo-list"><ul>';
        var imgsrc = '';
        var imgList = $('.photo-list ul li');
        $.each(imgList,function(i,n){
            imgsrc = $(this).find('input').attr('value');
            var arr = imgsrc.split("|");
//            var src = base_root + "/loadImgSrcByPath.do?filePath=" + arr[3];
//            var li_html = '<li><div class="img-box selected" ><img src="'+src+'" bigsrc="'+src+'"></div></li>';
//            $(".photo-list ul").append(li_html);
            
            return false;//break;
        });
//        photoHtml += '</ul></div><div style="clear:both;"></div>';
        
		var postData = {
			'id' : id,
			'entitytype' : entitytype,
			'registerNo' : registerNo,
			'isFinancial' : isFinancial,
			'giin' : giin,
			'incorporationPlace' : incorporationPlace,
			'registeredAddress' : registeredAddress,
			'mailingAddress' : mailingAddress,
			'country' : country,
			'naturePurpose' : naturePurpose,
			'incorporationDate' : incorporationDate,
			'website' : website,
			'superCheckType' : superCheckType,
			'companyNameSc' : companyNameSc,
			'companyNameTc' : companyNameTc,
			'companyNameEn' : companyNameEn,
			'imgList': imgsrc
		};
					
		
		$.ajax({
			type : "post",
			url : base_root +"/console/ifafirm/info/saveFirmInfo.do?datestr="
					+ new Date().getTime(),
			data : postData,
			async : false,
			dataType : "json",
			success : function(data) {
				var result = data.result;
				if (result == true) {
					layer.msg(data.msg, {
						icon : 1,
						time : 2000
					});
//						$('#btnSave').attr('disabled', 'disabled');
				} else
					layer.msg(data.msg, {
						icon : 0,
						time : 2000
					});
			}
		});
	});

	/*
	 * 获取Url传递参数值
	 */
	function getUrlParam(name){  
	    //构造一个含有目标参数的正则表达式对象  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    //匹配目标参数  
	    var r = window.location.search.substr(1).match(reg);  
	    //返回参数值  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}

    function initUpload(){
	  $(".upload-album").InitUploader({ 
		btntext: "上传图片", multiple: false, water: true, thumbnail: true, filesize: "6000",filetypes: "jpg,png,gif,jpeg", modulerelate:"portrait" });
	}
  
    $(".upload-album").on("click",function(){

    });

});