/**
 * form.js 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	
	var $ = require('jquery');
	require('layer');
//	var grid_selector = "#tableList";
//	var pageSize = 10;
	var selector =  require('ifaSelectUser');
	selector.init();
	
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
	 var isNew="0";
	    if(getQueryString("new")=="1" ){
			isNew="1";
		}
	//保存
    $("#btnSave").on("click",function(){
    	//保存草稿
    	$("#ifPublish").val("false");
    	saveData();
    });
    $("#btnNext").on("click",function(){
    	//保存并发布
    	$("#ifPublish").val("true");
    	saveData();
    });
    function saveData(){
	    //检查输入合法性

	    //如果checkbox不是选定状态，则清空值
    	//if($("#permit2").attr("checked")|| $("#permit2").attr("checked")=="checked"){
    	if($("#permit2").is(':checked')){
    		 //if (!$("#check_client").is(':checked') || !$("#check_client").attr("checked") || $("#check_client").attr("checked")!="checked"){
    		if (!$("#check_client").is(':checked') ){
    	    	$("#permit_clients").val("");
    	    }
    	    if (!$("#check_prospect").is(':checked')){
    	    	$("#permit_prospects").val("");
    	    }
    	    if (!$("#check_colleague").is(':checked')){
    	    	$("#permit_colleagues").val("");
    	    }
    	    if (!$("#check_buddy").is(':checked')){
     	    	$("#permit_buddies").val("");
     	    }
    	    
    	}else{
    		$("#permit_clients").val("");
    		$("#permit_prospects").val("");
    		$("#permit_buddies").val("");
    		$("#permit_colleagues").val("");
    	}
    	/*if($("#push2").attr("checked")|| $("#push2").attr("checked")=="checked"){
    		 if (!$("#push_client").attr("checked") || $("#push_client").attr("checked")!="checked"){
     	    	$("#push_clients").val("");
     	    }
     	    if (!$("#push_prospect").attr("checked") || $("#push_prospect").attr("checked")!="checked"){
     	    	$("#push_prospects").val("");
     	    }
     	    if (!$("#push_buddy").attr("checked") || $("#push_buddy").attr("checked")!="checked"){
     	    	$("#push_buddies").val("");
     	    }
     	    if (!$("#push_colleague").attr("checked") || $("#push_colleague").attr("checked")!="checked"){
     	    	$("#push_colleagues").val("");
     	    }
    	}else{
    		$("#push_clients").val("");
    		$("#push_prospects").val("");
    		$("#push_buddies").val("");
    		$("#push_colleagues").val("");
    	}*/
	    
    	if($("#permit_clients").val().length>0)
    		$("#permit_client").val(-1);
    	if($("#permit_prospects").val().length>0)
    		$("#permit_prospect").val(-1);
    	if($("#permit_buddies").val().length>0)
    		$("#permit_buddy").val(-1);
    	if($("#permit_colleagues").val().length>0)
    		$("#permit_colleague").val(-1);
    	
	    
	    var data = $("#addForm").serialize();
	    //layer.msg(data);
	    //提交表单
	    $.ajax({
	    	type:"POST",
	        url: base_root+"/front/strategy/info/saveRelease.do?r="+Math.random(),
	        data: data,
	        success: function(response)
	        {   
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
//	            	layer.msg("Password changed successfully.");
	            	var id = $("#id").val();
	            	layer.msg(dataObj.msg);
	            	if ($("#ifPublish").val()=="true")
	            		window.location.href=base_root+"/front/strategy/info/myList.do?r="+Math.random();
	            	else 
	            		window.location.href=base_root+"/front/strategy/info/addRelease.do?id="+id+"&r="+Math.random();
	            }else{
	            	layer.msg(dataObj.msg);
	            }
	        },
	        error:function()
	        {
	            layer.msg("error found while saving data.");
	        }
	    });    	
    }
    
    //定义附加设置选项控制 --start------------------------------------------------------------------------
	$("input:radio[name='permit']").on("click",function(){
	    var permitCheckResult=$("input:radio[name='permit']:checked").val();
	    if (permitCheckResult && permitCheckResult=='3') {
	    	$("#permit_ext").show();
	    }
	    	
	    else $("#permit_ext").hide();
	    $("#permitSetting").val(permitCheckResult);
	    $("#permit").val($(this).val());
    });
    	
	$("input:radio[name='push']").on("click",function(){
	    var pushCheckResult=$("input:radio[name='push']:checked").val();
	    if (pushCheckResult && pushCheckResult=='2'){
	    	$("#push_ext").show();
	    }
	    else $("#push_ext").hide();
	    $("#pushSetting").val(pushCheckResult);
	});
    
    $(".j-permitASetting").on("click",function(){
    	//$("#permit2").click();
    	/*var url = base_root+"/front/strategy/info/userSelector.do";
    	openResWin(url,950,600,"permitASetting");*/
    	var type1=$(this).attr("type1");
    	var type=$(this).attr("type");
    	selector.create(0,type,"permit_"+type1,type+"Names");
		$(".selectnamelistbox").show();
    	
    });
    
    $(".nameInput").on("input",function(){
//  	console.log($(this).val());
    });
    
    $("#pushASetting").on("click",function(){
    	var url=base_root+"/front/strategy/info/userSelector.do";
    	openResWin(url,950,600,"pushASetting");
    });
    
    
  /*  $("#permit2").on("click",function(){
    	$("#permit_ext").show();
    });*/
    
    $("#push2").on("click",function(){
    	$("#push_ext").show();
    });

    $("#permit_ext .setting").on("click",function(){
        $(this).parents("li").toggleClass("setting_active");
        var type=$(this).attr("permitType");
        if($(this).parents("li").hasClass("setting_active")){
        	$("#permit_"+type).val("1");
        }else{
        	$("#permit_"+type).val("0");
        }
    });
   
    $(".setting").on("click",function(){
    	var obj = {};
    	var hiddenObj = {};
    	if ($(this).attr("name")=="permit_client"){
    		obj = $("#permit_client");
    		hiddenObj = $("#permit_clients");
    	}else if ($(this).attr("name")=="permit_prospect"){
    		obj = $("#permit_prospect");
    		hiddenObj = $("#permit_prospects");
    	}else if ($(this).attr("name")=="permit_buddy"){
    		obj = $("#permit_buddy");
    		hiddenObj = $("#permit_buddies");
    	}else if ($(this).attr("name")=="permit_colleague"){
    		obj = $("#permit_colleague");
    		hiddenObj = $("#permit_colleagues");
    	}
    	/*else if ($(this).attr("name")=="push_client"){
    		obj = $("#push_client");
    		hiddenObj = $("#push_clients");
    	}else if ($(this).attr("name")=="push_prospect"){
    		obj = $("#push_prospect");
    		hiddenObj = $("#push_prospects");
    	}else if ($(this).attr("name")=="push_buddy"){
    		obj = $("#push_buddy");
    		hiddenObj = $("#push_buddies");
    	}else if ($(this).attr("name")=="push_colleague"){
    		obj = $("#push_colleague");
    		hiddenObj = $("#push_colleagues");
    	}*/
    		
    	if (!$(this).is(':checked')){
    		obj.val("0");
    		hiddenObj.val("");
    	}else{
    		obj.val("1");
    		hiddenObj.val("ALL");
    	}
    });
    
    //用于在配置用户权限后，刷新本 页面的选择按钮状态
    $("#refreshSelection").on("click",function(){
    	//1.获取总人员数
        var cnt_clients = $("#cnt_clients").val();
        var cnt_prospects = $("#cnt_prospects").val();
        var cnt_buddies = $("#cnt_buddies").val();
        var cnt_colleagues = $("#cnt_colleagues").val();
        
        //2.设置查看权限
    	var clients = $("#permit_clients").val();
    	refreshCheckStatus("permit_client",clients,cnt_clients);
    	//var checkbox = document.getElementById("some-checkbox");
    	//checkbox.indeterminate = true;
    	
        var prospects = $("#permit_prospects").val();
    	refreshCheckStatus("permit_prospect",prospects,cnt_prospects);
    	
        var buddies = $("#permit_buddies").val();
    	refreshCheckStatus("permit_buddy",buddies,cnt_buddies);
    	
        var colleagues = $("#permit_colleagues").val();
    	refreshCheckStatus("permit_colleague",colleagues,cnt_colleagues);
    	
        //3.设置推送权限
    	/*var clients2 = $("#push_clients").val();
    	refreshCheckStatus("push_client",clients2,cnt_clients);
    	
        var prospects2 = $("#push_prospects").val();
    	refreshCheckStatus("push_prospect",prospects2,cnt_prospects);
    	
        var buddies2 = $("#push_buddies").val();
    	refreshCheckStatus("push_buddy",buddies2,cnt_buddies);
    	
        var colleagues2 = $("#push_colleagues").val();
    	refreshCheckStatus("push_colleague",colleagues2,cnt_colleagues);*/
    });

    function refreshCheckStatus(objName, objVal, cntVal){
    	try{
        	/*var obj = eval(objName);
        	if (objVal && objVal.length > 1) {
        		$(obj).attr("checked",true);
        		$(obj).val("1");

        		if (countVal(objVal)<cntVal){
        			$(obj).val("-1");
        			$(obj).attr("indeterminate",true);
        		}
        	}else{
        		$(obj).attr("checked",false);
        		$(obj).val("0");
        	}*/
    	}catch(e){
    		layer.msg(e.message); 
    	}
    }
    
    function countVal(str){
    	if (!str) return 0;
    	str = str.substring(str.indexOf(",")+1);//去掉首字符为逗号
    	str = str.replace("ALL,");
    	var strArr = str.split(",");
        return strArr.length;
    }

    //定义附加设置选项控制 --end------------------------------------------------------------------------
    
    //页面load完后运行
	//if ($("#permitSetting").val()=="2") $("#permit2").click();
	//if ($("#pushSetting").val()=="2") $("#push2").click();
	$("#refreshSelection").click();
    
	$(".craetbnPrevious").on("click",function(){
		var newHtml="";
		if(isNew=="1"){
			newHtml="&new=1"
		}
		var url=base_root+"/front/strategy/info/addAllocation.do?id="+$("#id").val()+newHtml;
		window.location.href=url;
	})
});