/**
 * @author qgfeng
 * 填写提交问卷流程
 */
define(function(require) {
	var $ = require('jquery');
			require('layui');
	// 提交问卷事件
	$("#rpq_fill_next").on("click",function(){
		var ul_num = document.getElementById("rpq_list").getElementsByTagName("ul").length;
		var totalScore = 0;
		var answerList = [];
		$(".rpqContent input[type='radio']").each(function(){
			if($(this).prop('checked')){
				$(this).closest('.wenti').find('.alert_complete_quest').hide();
				var answer = {};
				answer.questId = $(this).attr("name");
				answer.answerId = $(this).attr("id");
				answer.answerValue = $(this).val();
				answerList.push(answer);	
				totalScore += parseInt($(this).val());
			}else if($(".rpqContent input[name='"+$(this).attr("name")+"']:checked").length == 0){
				$(this).closest('.wenti').find('.alert_complete_quest').show();
			}
		});
		if(answerList.length<ul_num){
			//layer.msg(props['kyc.rpq.paper.alert.complete.quest']);
			return ;
		}
		var answerJson = JSON.stringify(answerList);
		var pageId = $("#form_fillrpq").find("input[name='pageId']").val();
		var examId = $("#form_fillrpq").find("input[name='examId']").val();
		$.ajax({
			method: 'POST',
			datatype:"JSON",
			url:base_root+"/front/rpq/saveAnswer.do?d=" + new Date().getTime(),
			data:{
				"answerJson":answerJson,
				"pageId":pageId,
				"examId":examId,
				"totalScore":totalScore
			},
			success:function(response){
				if(response.result == true){
					var myform = $("#form_fillrpq");
					myform.attr('action', base_root+"/front/rpq/confirmRpqPaper.do?d=" + new Date().getTime());  
					myform.attr('method', 'post');
					myform.find("#rpq_score").val(totalScore);
					myform.submit();
				}else{
					layer.msg(props['global.failed.save']);
				}
			},
			error:function(response){}
		});
	});
	//确认答题问卷结果
	$("#rpq_confirm_next").on("click",function(){
		if($('.rpq-confirm-rows-risk-level input[name="rpq_change_level"]:checked').val() == '1'
			&& typeof $('.rpq-confirm-rows-user-risk-level input[name="rpq_level"]:checked').val() == 'undefined'){
			layer.msg(props['kyc.rpq.paper.alert.please.risk.orientation']);
			return;
		}rpq_change_level
		var userLevel = $(".rpq-orientation-rows input[type='radio']:checked");
		var userLevelId = userLevel.attr("id");
		var myform = $("#form_fillrpq");
		myform.attr('action', base_root+"/front/rpq/completeRpqPaper.do");  
		myform.attr('method', 'post');  
		myform.find("#userLevelId").val(userLevelId);
		myform.submit();
	});
	//回退
	$("#rpq_confirm_previous").on("click",function(){
		history.go(-1);
	});
	//取消
	$("#rpq_fill_cancel").on("click",function(){
		var pIndex = $(this).attr("pIndex");
		if(pIndex=="1"){
			history.go(-1);
		}else{
			history.go(-2);
		}
		var examId = $("#form_fillrpq").find("input[name='examId']").val();
		$.ajax({
			method: 'POST',
			datatype:"JSON",
			url:base_root+"/front/rpq/cancelFill.do",
			data:{
				"examId":examId
			},
			success:function(response){
			},
			error:function(response){}
		});
	});
	//自定义投资风险等级
	/*$(".rpq-orientation-rows .less_level").on("click",function(){
		var val=$('input:radio[name="rpq_change_level"]:checked').val();
		if(val==null){
			layer.msg("请同意自主选择风险取向");
            return false;
        }
	});*/
	$("#rpq_change_level").on("click",function(){
		$('input:radio[name="rpq_level"]').each(function() {
			$(this).attr("disabled",false);
		});
	});
	//下载成PDF文件
	$("#rpq_downpdf").on("click",function(){
		var pageId = $("#rpq_form_complete input[name='pageId']").val();
		var totalScore = $("#rpq_form_complete input[name='totalScore']").val();
		var myform = $("#rpq_form_complete");
		myform.attr('action', base_root+"/front/rpq/generatePDF.do");  
		myform.attr('method', 'post');  
		myform.submit();
	});
	//打印问卷
	$("#rpq_print").on("click",function(){
		var headstr = "<html><head><title></title></head><body>";  
		var footstr = "</body>";  
		var printData = document.getElementById("showRpqResult").innerHTML; //获得 div 里的所有 html 数据
		var oldstr = document.body.innerHTML;  
		document.body.innerHTML = headstr+printData+footstr;  
		window.print();  
		document.body.innerHTML = oldstr;  
		location.reload();
	});
	//客户确认点击显示风险级别选择切换
	$('input[name=rpq_change_level').on('click',function(){
		var rpq_agree = $('input[name=rpq_change_level]:checked').val();
		if('0' == rpq_agree){
			$('.rpq-orientation-rows').hide();
		} else if('1' == rpq_agree){
			$('.rpq-orientation-rows').show();
		}
	});
	$(".rpqContent input[type='radio']").click(function(){
		$(this).closest('.wenti').find('.alert_complete_quest').hide();
	});
	if($('.rpq-orientation-rows').find('.rpq-confirm-radio').length < 1){
		$('.rpq-confirm-radio-my-select').hide();
	}	
});