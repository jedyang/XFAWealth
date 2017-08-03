/**
 * openAccountStart.js 开户界面start
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-06
 */
define(function(require) {
	var $ = require('jquery');
	require('layer');
	
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
    $("#btn_next").on("click",function(){
    	saveAccount("next");
    });
    //保存草稿 
    $("#btn_draft").on("click",function(){
    	saveAccount("draft");
    });
    
    function saveAccount(act){

 		var distributorId = $("#distributorId").val();
 		var accountId = $("#accountId").val();
 		var indId = $("#indId").val();

 		//邀请开户必须传入被邀请人（investor）的ID
 		if (!indId || indId==""){
// 			$.Tips({ content: "Missing client infomation."});
 			layer.msg("Missing client infomation.");
 			return;
 		}
 		
 		//选定基金代理商
 		if (!distributorId || distributorId==""){
// 			$.Tips({ content: "Please select a distributor."});
// 			alert("Please select a distributor.");
 			layer.msg("Please select a distributor.");
 			return;
 		}

    	$.ajax({
    		url:base_root+"/front/investor/saveSelectionByIFA.do?r="+Math.random(),
    		data:{"indId":indId,"distributorId":distributorId,"accountId":accountId},
    		dataType:"json",
    		type:"post",
    		success:function(data){
    			if(data.result){
    				accountId = data.accountId;
    				if (data.accountId) $("#accountId").val(data.accountId);
    				if (act=="draft"){
//    					$.Tips({ content: data.msg});
    					layer.msg(data.msg);
    				}else {//next
//    					alert(accountId);
    					window.location.href = base_root+"/front/investor/accountRPQ.do?accountId="+accountId+"&r="+Math.random();
    				}
    			}else{
//    				$.Tips({ content: data.msg});
    				layer.msg(data.msg);
    			}
    		},
    		error:function(){
    			
    		}
    	})
    }
 });