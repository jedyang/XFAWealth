/**
 *  ifaRecommendNewsSetting.js
 * @author mqzou
 * @date: 2016-10-08
 */

define(function(require) {
	var $ = require('jquery');
	 

	$("input:radio[name='push']").on("click",function(){
	    var pushCheckResult=$("input:radio[name='push']:checked").val();
	    if (pushCheckResult && pushCheckResult=='2'){
	    	 $("#tips").css("display","");
			 $("#specifyType").css("display","");
	    }else {
	    	$("#tips").css("display","none");
			 $("#specifyType").css("display","none");
	    }
	    $("#pushSetting").val(pushCheckResult);
	});
	 $("#push2").on("click",function(){
		 $("#tips").css("display","");
		 $("#specifyType").css("display","");
	    });
	 
	
		
	  
		
	
	$("#btnOk").on("click",function(){
		var checkedValue= $("input[name='push']:checked").val(); 
		var reason=$("#txtReason").val();
		var push_clients=$("#push_clients").val();
		var push_prospects=$("#push_prospects").val();
		var push_buddies=$("#push_buddies").val();
		var push_colleagues=$("#push_colleagues").val();
		var push_client=$("#push_client").val();
		var push_prospect=$("#push_prospect").val();
		var push_buddy=$("#push_buddy").val();
		var push_colleague=$("#push_colleague").val();
		
		$.ajax({
			type:"post",
			dataType:"json",
			url:base_root+"/front/ifa/space/saveNewsRecommendSetting.do",
			data:{id:_id,newsId:_newsId,pushto:checkedValue,reason:reason,
				push_client:push_client,push_prospect:push_prospect,
				push_buddy:push_buddy,push_colleague:push_colleague,push_clients:push_clients,
				push_prospects:push_prospects,push_buddies:push_buddies,push_colleagues:push_colleagues},
			success:function(json){
				if(json.result){
					alert("成功");
				}else{
					alert("失败");
				}
			}
			
		})
		
	});
	
	 $("#tips").on("click",function(){
	    	$("#push2").click();
	    	var url=base_root+"/front/strategy/info/userSelector.do";
	    	openResWin(url,950,600,"pushASetting");
	    });
	 
	 function openResWin(url,width,height,id){ 
		   var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽    
		   var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高   
		   if (width>myw) width = myw;
		   if (height>myh) height = myh;
		   
		   var top = (myh-height)/2-(window.screen.height-myh)/2;
		   if (top<0) top = 0;
		   
		   var left = (myw-width)/2-(window.screen.width-myw)/2;
		   if (left<0) left = 0;
		   window.open(url,id,"height="+height+", width="+width+", top="+top+", left="+left+",location=no,menubar=no,toolbar=no,status=no,resizable=no,scrollbars=yes");   
		}
	 
	 $("#btnCancel").on("click",function(){
		 window.href=base_root+"/front/ifa/space/ifarecommendnews.do";
	 });
	 
	//用于在配置用户权限后，刷新本 页面的选择按钮状态
	    $("#refreshSelection").on("click",function(){
	    	//1.获取总人员数
	        var cnt_clients = $("#cnt_clients").val();
	        var cnt_prospects = $("#cnt_prospects").val();
	        var cnt_buddies = $("#cnt_buddies").val();
	        var cnt_colleagues = $("#cnt_colleagues").val();

	    	var clients2 = $("#push_clients").val();
	    	refreshCheckStatus("push_client",clients2,cnt_clients);
	    	
	        var prospects2 = $("#push_prospects").val();
	    	refreshCheckStatus("push_prospect",prospects2,cnt_prospects);
	    	
	        var buddies2 = $("#push_buddies").val();
	    	refreshCheckStatus("push_buddy",buddies2,cnt_buddies);
	    	
	        var colleagues2 = $("#push_colleagues").val();
	    	refreshCheckStatus("push_colleague",colleagues2,cnt_colleagues);
	    });

	    function refreshCheckStatus(objName, objVal, cntVal){
	    	try{
	        	var obj = eval(objName);
	        	if (objVal && objVal.length > 1) {
	        		$(obj).attr("checked",true);
	        		$(obj).prop("checked", true);
	        		$(obj).val("1");

	        		if (countVal(objVal)<cntVal){
	        			$(obj).val("-1");
	        			$(obj).attr("indeterminate",true);
	        		}
	        	}else{
	        		$(obj).attr("checked",false);
	        		$(obj).prop("checked", false);
	        		$(obj).val("0");
	        	}
	    	}catch(e){
	    		alert(e.message); 
	    	}
	    }
	    function countVal(str){
	    	if (!str) return 0;
	    	str = str.substring(str.indexOf(",")+1);//去掉首字符为逗号
	    	str = str.replace("ALL,");
	    	var strArr = str.split(",");
	        return strArr.length;
	    }
	    
	    if ($("#pushSetting").val()=="2") $("#push2").click();
	    $("#refreshSelection").click();
	   
})
