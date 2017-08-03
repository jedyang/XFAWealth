/**
 * Accountapplication.js 开户审批
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-24
 */

define(function(require) {
	var $ = require('jquery');

	$(".OpenAccount_tab li").on("click",function(){

		$(".OpenAccount_tab li").find("a").removeClass("now");
		$(this).find("a").addClass("now");
		$('.OpenAccount_Approval_content > div').hide().eq($(this).index() ).show();
	})
	
    $("#btn_reject").on("click",function(){
    	if (!$("#msg").val() || $("#msg").val()==""){
    		alert("Please input comment.");
    	}
    	$("#checkStatus").val("0");//不同意
    });
	
    $("#btn_approve").on("click",function(){
    	$("#checkStatus").val("1");//同意
    });
    
    function saveApproval(){
    	var accountId = $("#accountId").val();
    	var checkStatus = $("#checkStatus").val();
    	var msg = $("#msg").val();
    	
    	$.ajax({
    		url:base_root+"/front/investor/saveApproval.do?r="+Math.random(),
    		data:{"checkStatus":checkStatus,"msg":msg,"accountId":accountId},
    		dataType:"json",
    		type:"post",
    		success:function(data){
    			if(data.result){
					$.Tips({ content: data.msg});
					window.location.href = base_root+"/front/investor/approvalList.do?r="+Math.random();
    			}
    		},
    		error:function(){
    			$.Tips({ content: "提交失败"});
    		}
    	})
    }
});