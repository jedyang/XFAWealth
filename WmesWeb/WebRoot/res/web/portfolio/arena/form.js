/**
 * form.js 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	
	var $ = require('jquery');
	//var grid_selector = "#tableList";
	//var pageSize = 10;
    
    //定义textarea输入控制 --start
    var totalLen = 500;
    $("#investmentGoal").on("keydown keyup blur",function(){
    	var inputLen = $("#investmentGoal").val().length;
    	if (inputLen > totalLen){ 
    		$("#investmentGoal").val($("#investmentGoal").val().substring(0, totalLen)); 
    	}
    	inputLen = $("#investmentGoal").val().length;
    	$("#goalLen1").html(inputLen); 
	    $("#goalLen2").html(totalLen - inputLen); 
    });
    $("#suitability").on("keydown keyup blur",function(){
    	var inputLen = $("#suitability").val().length;
    	if (inputLen > totalLen){ 
    		$("#suitability").val($("#suitability").val().substring(0, totalLen)); 
    	}
    	inputLen = $("#suitability").val().length;
    	$("#suitLen1").html(inputLen); 
	    $("#suitLen2").html(totalLen - inputLen); 
    });
    //定义textarea输入控制 --end

    
    //定义用于添加基金产品 --start
    var _ids_ = $('#ids').val();
    $(".funds_keyword_xiala_search").on("click","li",function(){
    	$('.funds_keyword_input').val($(this).html());
    	
		if (_ids_.length>0)
			_ids_ += ","+$(this).attr("data-id");
		else
			_ids_ = $(this).attr("data-id");
		$('#ids').val(_ids_);
		loadFundList();
    });
    function loadFundList(){
		var url = base_root+"/front/fund/info/getFundListForSelect.do?id="+$('#ids').val();
		if ($('#view').val() && $('#view').val()=="true") url += "&view=true";
		//alert(_ids_)
  		$("#tableList").load(url, null,
  		 	function(text, status, xmlhttp){
//      		 	  initInnerHeight('mainFrame','mainFrame');
  			}
  		);
    }
    
    $("#btnAdd").on("click",function(){
    	var url=base_root+"/front/fund/info/fundSelector.do";
    	openResWin(url,500,600,"fundSelector");
    });
    //用于在弹出窗口刷新本页面
    $("#popupWinReturn").on("click",function(){
    	loadFundList();
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
	   //window.open(url,id,"height="+height+", width="+width+", top="+top+", left="+left+",location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	   //var winid = parseInt(Math.random()*10000000000)+"";
	   window.open(url,id,"height="+height+", width="+width+", top="+top+", left="+left+",location=no,menubar=no,toolbar=no,status=no,resizable=no,scrollbars=yes");   
	}
    //定义用于添加基金产品 -- end
	
	//保存
    $("#btnSave").on("click",function(){
    	//保存草稿
    	$("#ifPublish").val("false");
    	saveData();
    });
    $("#btnPub").on("click",function(){
    	//保存并发布
    	$("#ifPublish").val("true");
    	saveData();
    });
    function saveData(){	
	    //检查输入合法性
	    
	    var portfolioName = $('#portfolioName').val();
	    if (!portfolioName){$.Tips({ content: "Please input portfolio name."}); return;}

	    var investmentGoal = $('#investmentGoal').val(); 
	    if (!investmentGoal){$.Tips({ content: "Please input investment goal."}); return;}

	    var suitability = $('#suitability').val();
	    if (!suitability){$.Tips({ content: "Please input investor suitability."}); return;}

	    //如果checkbox不是选定状态，则清空值
	    if (!$("#permit_client").attr("checked") || $("#permit_client").attr("checked")!="checked"){
	    	$("#permit_clients").val("");
	    }
	    if (!$("#permit_prospect").attr("checked") || $("#permit_prospect").attr("checked")!="checked"){
	    	$("#permit_prospects").val("");
	    }
	    if (!$("#permit_buddy").attr("checked") || $("#permit_buddy").attr("checked")!="checked"){
	    	$("#permit_buddies").val("");
	    }
	    if (!$("#permit_colleague").attr("checked") || $("#permit_colleague").attr("checked")!="checked"){
	    	$("#permit_colleagues").val("");
	    }
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
	    
	    var data = $("#addForm").serialize();
	    //alert(data);
	    //提交表单
	    $.ajax({
	    	type:"POST",
	        url: base_root+"/front/portfolio/arena/savePortfolio.do?r="+Math.random(),
	        data: data,
	        success: function(response)
	        {   
	            var dataObj = JSON.parse(response);;
	            if(dataObj.result){
//	            	$.Tips({ content: "Password changed successfully."});
	            	alert("保存成功。");
	            }else{
	            	//$.Tips({ content: dataObj.msg});
	            	alert(dataObj.msg);
	            }
                window.location.href=base_root+"/front/portfolio/arena/myList.do?r="+Math.random();
	        },
	        error:function()
	        {
	            alert("error found while saving data.");
	        }
	    });
    }
    
    //定义附加设置选项控制 --start------------------------------------------------------------------------
	$("input:radio[name='permit']").on("click",function(){
	    var permitCheckResult=$("input:radio[name='permit']:checked").val();
	    if (permitCheckResult && permitCheckResult=='2') $("#permit_ext").show();
	    else $("#permit_ext").hide();
	    $("#permitSetting").val(permitCheckResult);
    });
    	
	$("input:radio[name='push']").on("click",function(){
	    var pushCheckResult=$("input:radio[name='push']:checked").val();
	    if (pushCheckResult && pushCheckResult=='2') $("#push_ext").show();
	    else $("#push_ext").hide();
	    $("#pushSetting").val(pushCheckResult);
	});
    
    $("#permitASetting").on("click",function(){
    	$("#permit2").click();
    	var url=base_root+"/front/strategy/info/userSelector.do";
    	openResWin(url,950,600,"permitASetting");
    });
    
    $("#pushASetting").on("click",function(){
    	$("#push2").click();
    	var url=base_root+"/front/strategy/info/userSelector.do";
    	openResWin(url,950,600,"pushASetting");
    });
    
    
    $("#permit2").on("click",function(){
    	$("#permit_ext").show();
    });
    
    $("#push2").on("click",function(){
    	$("#push_ext").show();
    });
    
    $("#permit_client").on("click",function(){
    	//alert($(this).is(':checked'));
    	if (!$(this).is(':checked')) $("#permit_clients").val("");
    	else $("#permit_clients").val("ALL");
    });
    $("#permit_prospect").on("click",function(){
    	if (!$(this).is(':checked')) $("#permit_prospects").val("");
    	else $("#permit_prospects").val("ALL");
    });
    $("#permit_buddy").on("click",function(){
    	if (!$(this).is(':checked')) $("#permit_buddies").val("");
    	else $("#permit_buddies").val("ALL");
    });
    $("#permit_colleague").on("click",function(){
    	if (!$(this).is(':checked')) $("#permit_colleagues").val("");
    	else $("#permit_colleagues").val("ALL");
    });
    $("#push_client").on("click",function(){
    	//alert($(this).is(':checked'));
    	if (!$(this).is(':checked')) $("#push_clients").val("");
    	else $("#push_clients").val("ALL");
    });
    $("#push_prospect").on("click",function(){
    	if (!$(this).is(':checked')) $("#push_prospects").val("");
    	else $("#push_prospects").val("ALL");
    });
    $("#push_buddy").on("click",function(){
    	if (!$(this).is(':checked')) $("#push_buddies").val("");
    	else $("#push_buddies").val("ALL");
    });
    $("#push_colleague").on("click",function(){
    	if (!$(this).is(':checked')) $("#push_colleagues").val("");
    	else $("#push_colleagues").val("ALL");
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
        		$(obj).val("1");

        		if (countVal(objVal)<cntVal){
        			$(obj).val("-1");
        			$(obj).attr("indeterminate",true);
        		}
        	}else{
        		$(obj).attr("checked",false);
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

    //定义附加设置选项控制 --end------------------------------------------------------------------------
    
    //页面load完后运行
	if ($("#permitSetting").val()=="2") $("#permit2").click();
	if ($("#pushSetting").val()=="2") $("#push2").click();
	loadFundList();
	$("#refreshSelection").click();
    
});