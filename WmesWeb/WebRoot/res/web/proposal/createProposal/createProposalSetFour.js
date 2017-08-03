define(function(require) {
	//依赖
	var $ = require('jquery');
	require('layui');
	require("umeditorConfig");//配置文件
	require("ueditor");//配置文件
	require("wmes_upload");
	
	/**
	 * 可视化编辑器
	 */
    var ue = UM.getEditor('container',{
    	toolbar: ['source | undo redo | bold italic underline strikethrough | superscript subscript | forecolor backcolor | removeformat |',
    	            'insertorderedlist insertunorderedlist | selectall cleardoc paragraph | fontfamily fontsize' ,
    	            '| justifyleft justifycenter justifyright justifyjustify ']
		,lang:lang
		,imageUrl:base_root + "/upload.do?r="+Math.random()+"&moduleId=4"//图片上传提交地址
		,imagePath:base_root
		,imageFieldName:"upfile"
    });
    ue.ready(function(){
        $("#edui1").width("100%");
        $("#edui1_iframeholder").width("100%");
        $("#container").height("350px");
        var bodyContent = $("#preContent").html().trim();
        ue.setContent(bodyContent);
    });
    
	// 下拉
	$(".proposal_xiala").on("click",function(){
		$(this).toggleClass("xiala-show");
	});
	$(".proposal_xiala li").on("click",function(e){
		var proposalId = $('#proposalId').val();
		$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("#reportLanguage").val($(this).html());
		$('#reportLanguage').attr('data-value',$(this).data('value'));
		$('#preview').attr('href',base_root+'/front/crm/proposal/proposalPreview.do?isPrint=1&id=' + proposalId+'&language='+$(this).data('value'));
		e.stopPropagation(); 
	});
	
	//导出PDF
	$('#exportToPDF').click(function(){
		var proposalId = $('#proposalId').val();
		var language = $('#reportLanguage').attr('data-value');
		var iframe = $("<iframe id='dowmPdf'>");
		iframe.attr("style","display:none");
		iframe.attr("method","post");
		iframe.attr( 'frameborder','0' );
		iframe.attr("src",base_root + '/front/crm/proposal/downPdf.do?proposalId=' + proposalId+'&language='+language);
		$("body").append(iframe);
		//$("#dowmPdf").remove();
	});
	
	//附件上传
	//var proposalId = $('#proposalId').val();
	var upload = $(".upload-album").InitUploader({ 
		multiple: true, 
		thumbnail: false, 
		//uploadtype: 'uploadSec', 
		modulerelate: 'create_proposal_pdf', 
		sendurl: base_root + "/wmesUpload.do?r="+Math.random(), 
		filesize: "8000"
		/*uploadSuccess:function(file, data){
		}*/
	});
	$('#addAttachment').click(function(){
		$('.webuploader-element-invisible').click();
	});
	
	//邮件发送
	$('#btnSendMail').click(function(){
		var clientId = getUrlParam('memberId'),
			receivers = '',
			subject = $('#txtSubject').val(),
			message = ue.getContent();
		message = escape2Html(message);
		receivers = $('#registerMail').text() + ',' + $('#txtExtraEmail').val() + ',' + $('#txtCC').val();
		var url = base_root + '/front/crm/proposal/sendForConfirmation.do?d='+new Date().getTime();
		$.ajax({
			url:url,
			type:'post',
			data:{
				proposalId:getUrlParam('id'),
				clientId:clientId,
				receivers:receivers,
				subject:subject,
				message:encodeURI(message)
			},
			beforeSend:function(){
				$('.load-gif').css('height',$(document).height()*1.2);
				$('.load-gif').show();
				//$('.create_groupbox').css('top',$(document).height()*0.40+$(window).scrollTop());
			    //$(window).on('scroll',loadScroll);
			},
			success:function(result){
				$('.load-gif').hide();
				if(result.flag){
					layer.msg(props['create.proposal.step.four.alert.send.success'],{icon:2});
					setTimeout(function(){
						window.location.href = base_root + '/front/crm/proposal/previewProposal.do?proposalId='+getUrlParam('id');
					},1000);
				}
			}
		});
	});
	
	function loadScroll(){
		$('.create_groupbox').css('top',$(document).height()*0.40+$(window).scrollTop());
	}
	
	function sendSms(){
		//发送提醒
		var smsUrl = base_root + '/front/crm/proposal/smsProposalSummit.do?d='+ new Date().getTime();
		$.ajax({
			url:smsUrl,
			type:'post',
			data:{
				proposalId:getUrlParam('id')
			},
			success:function(result){
				if(result.flag){
					setTimeout(function(){
						window.location.href = base_root + '/front/crm/proposal/previewProposal.do?proposalId='+getUrlParam('id');
					},1000);
				}
			}
		});
	}
	$('#btnPrevious').click(function(){
		var url = base_root + '/front/crm/proposal/createProposalSetThree.do?memberId='+getUrlParam('memberId')+'&id='+getUrlParam('id');
		if(getUrlParam('edit') == '1'){
			url = urlUpdateParams(url, 'edit', '1');
		}
		window.location.href = url;
	});
	
	function getUrlParam(name){  
 	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
 	    var r = window.location.search.substr(1).match(reg);  
 	    if (r!=null) return unescape(r[2]);  
 	    return null;  
 	};
	
	function saveCreatePDF(){
		var proposalId = getUrlParam('id');
		var url = base_root+'/front/crm/proposal/proposalExportToPDF.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:
			{proposalId:proposalId},
			success:function(data){
				layer.msg(globalProp['global.success'],{icon:2});
			}
		});
	}
	
	
	(function preStoredPdf(){
		var proposalId = getUrlParam('id');
		var url = base_root+'/front/crm/proposal/preStoredPdf.do?d=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:
			{proposalId:proposalId},
			success:function(re){}
		});
	})();
	
	/**
     * URL add param
     */
 	 function urlUpdateParams(url, name, value) {
         var r = url;
         if (r != null && r != 'undefined' && r != "") {
             value = encodeURIComponent(value);
             var reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
             var tmp = name + "=" + value;
             if (url.match(reg) != null) {
                 r = url.replace(eval(reg), tmp);
             }
             else {
                 if (url.match("[\?]")) {
                     r = url + "&" + tmp;
                 } else {
                     r = url + "?" + tmp;
                 }
             }
         }
         return r;
     }
 	function escape2Html(str) {
	  var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
	  return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
	}
	
	
});