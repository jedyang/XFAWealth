/**
 * openAccountStart.js 开户界面start
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-06
 */
define(function(require) {
	var $ = require('jquery');
	require('layer');
	$(".OpenAccount_xfa_ifa").show();
	
 	$(".ifahelped").on("click",function(){
 		// //console.log($("input[name='ifachioce']:checked").val()); 
 		$(this).siblings().removeClass("ifaTypeActive").end().addClass("ifaTypeActive");
 		$(".OpenAccount_ifa").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor").hide();
 		$(".OpenAccount_xfa_ifa").show();
 		$("#investment").val("ifa");
 	});

 	$(".ifamyself").on("click",function(){

 		$(this).siblings().removeClass("ifaTypeActive").end().addClass("ifaTypeActive");
 		$(".OpenAccount_ifa").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor").show();
 		$(".OpenAccount_xfa_ifa").hide();
 		
 		getDistributors("");
 		$("#investment").val("self");
 	});
 	
 	//选定IFA
 	$(".OpenAccount_ifa").on("click",function(){
 		$(".OpenAccount_ifa").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(this).addClass("ifaActive");
 		//获取IFA相关代理商
 		var ifaId = $(this).attr("data-id")+"";
// 		alert("ifaId:"+ifaId);
 		$("#ifaId").val(ifaId);
 		getDistributors(ifaId);
    	
 		//显示代理商层
 		$(".OpenAccount_xfa_Dstributor").show();
 	});
 	$(".OpenAccount_xfa_Dstributor").on("click","img",function(){
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(this).addClass("ifaActive");
 	});
 	
 	//选定distributor
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
    
    function getDistributors(ifaId){
    	$.ajax({
    		url:base_root+"/front/investor/getIfaDistributors.do?r="+Math.random(),
    		data:{"ifaId":ifaId},
    		dataType:"json",
    		type:"get",
    		success:function(data){
    			if(data){
    				$(".OpenAccount_xfa_Dstributor_item").remove();
    				for (var i = 0;i< data.length;i++){
    					//alert(data[i].companyName);
    					$(".OpenAccount_xfa_Dstributor").append('<span class="OpenAccount_xfa_Dstributor_item" data-id="'+data[i].id+'" ><img style="width:222px;height:80px;" title="'+data[i].companyName+'" src="'+base_root+data[i].logofile+'" alt=""></span>');
    				}
    			}
    		},
    		error:function(){
    			
    		}
    	})
    }
    function saveAccount(act){
    	$("#investment").val("ifa");//目前只能通过ifa开户
    	var investment = $("#investment").val();
 		var ifaId = $("#ifaId").val();
 		var distributorId = $("#distributorId").val();
 		var accountId = $("#accountId").val();
 		if (!investment || investment==""){
// 			alert("Please select a investment mode.");
 			layer.alert("Please select a investment mode.");
 			return;
 		}
 		if (investment && investment=="ifa" && (!ifaId || ifaId=="")){
// 			alert("Please select a IFA.");
 			layer.alert("Please select a IFA.");
 			return;
 		}
 		if (!distributorId || distributorId==""){
// 			alert("Please select a distributor.");
 			layer.alert("Please select a distributor.");
 			return;
 		}
    	$.ajax({
    		url:base_root+"/front/investor/saveAccountStart.do?r="+Math.random(),
    		data:{"investment":investment,"ifaId":ifaId,"distributorId":distributorId,"accountId":accountId},
    		dataType:"json",
    		type:"post",
    		success:function(data){
    			if(data.result){
    				accountId = data.accountId;
    				if (data.accountId) $("#accountId").val(data.accountId);
    				if (act=="draft"){
    					layer.alert(data.msg);
    				}else {//next
//    					alert(accountId);
    					window.location.href = base_root+"/front/investor/accountRPQ.do?accountId="+accountId+"&r="+Math.random();
    				}
    			}else{
    				layer.alert(data.msg);
    			}
    		},
    		error:function(){
    			
    		}
    	})
    }
 });