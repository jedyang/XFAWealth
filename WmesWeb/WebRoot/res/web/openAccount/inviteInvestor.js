/**
 * inviteInvestor.js 开户界面start
 * @author michael
 * @date: 2016-06-06
 */
define(function(require) {
	var $ = require('jquery');
 	//选定distributor
 	$(".OpenAccount_xfa_Dstributor").on("click","img",function(){
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(this).addClass("ifaActive");
 	});
 	$(".OpenAccount_xfa_Dstributor").on("click",".OpenAccount_xfa_Dstributor_item",function(){
 		//选定代理商
 		var distributorId = $(this).attr("data-id")+"";
// 		alert("distributorId:"+distributorId);
 		$("#distributorId").val(distributorId);
 	});
 	
    $("#btn_send").on("click",function(){
//    	var indId = $("#indId").val();
    	if (!indId){
    		$.Tips({ content: "Please select a investor."});
    		return false;
    	}
    	
    	var distributorId = $("#distributorId").val();
    	if (!distributorId || distributorId==""){
 			$.Tips({ content: "Please select a distributor."});
// 			alert("Please select a distributor.");
 			return;
 		}
    	
    	$.ajax({
		url:base_root+"/front/investor/sendToClient.do?r="+Math.random(),
		data:{"indId":indId,"distributorId":distributorId},
		dataType:"json",
		type:"post",
		success:function(data){
			if(data.result){
//				$.Tips({ content: "Sent successfully."});
				alert("Sent successfully.");
			}
			window.location.href = base_root+"/index.do";
		},
		error:function(){
			$.Tips({ content: "Sent failed."});
		}
	})
    });
  
 });