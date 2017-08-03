/**
 * openAccountbasic.js basicinfo
 * @author tejay zhu
 */
define(function(require) {
	var $ = require('jquery');
	require('formValidLang');
	require("formValid");
	
	$(".onlyRead").on("click",function(){
		return false;
	})

	$("#btn_next").on("click",function(){
		if(!$("#declarForm").validationEngine('validate')) return;
		
		//alert(accountId);
		$.ajax({
			url:base_root+"/front/investor/accountDeclareSave.do",
			data:{"accountId":accountId,"r":Math.random()},
			dataType:"json",
			type:"post",
			success:function(data){
				if(data.result){
//					$.Tips({ content: data.msg});
//					alert(data.msg);
					window.location.href = base_root+"/front/investor/accountSubmit.do?accountId="+accountId;
				}else{
//					$.Tips({ content: data.msg});
					alert(data.msg);
				}
			},
			error:function(){
//				$.Tips({ content: "Save fail."});
				alert("Save fail.");
			}
		})
		//window.location.href = base_root + "/front/investor"
	});

	//取消
	$("#btn_cancle").on("click",function(){
		window.location.href=base_root + "/index.do";
	});
	
	for( var key in agreeFlag ){
		if( agreeFlag[key] == "1" ){
			$("#"+key).attr("checked",true);
		}
	}

	$(".login_checkbox label").click( function () { 
		var flag = $(this).attr( "flag" );
		if( flag == "declarationFlag" ){if ( agreeFlag.declarationFlag == "0" ) {agreeFlag.declarationFlag = "1"; } else {agreeFlag.declarationFlag = "0"; } }
		if( flag == "informationFlag" ){if ( agreeFlag.informationFlag == "0" ) {agreeFlag.informationFlag = "1"; } else {agreeFlag.informationFlag = "0"; } }
		if( flag == "aimFlag" ){if ( agreeFlag.aimFlag == "0" ) {agreeFlag.aimFlag = "1"; } else {agreeFlag.aimFlag = "0"; } }
		if( flag == "advisorFlag" ){if ( agreeFlag.advisorFlag == "0" ) {agreeFlag.advisorFlag = "1"; } else {agreeFlag.advisorFlag = "0"; } }
		if( flag == "qualifiedFlag" ){if ( agreeFlag.qualifiedFlag == "0" ) {agreeFlag.qualifiedFlag = "1"; } else {agreeFlag.qualifiedFlag = "0"; } }
		
		$.post( base_root + "/front/investor/accountDeclareSave.do?accountId="+accountId , agreeFlag, function(ret){
			agreeFlag.id = ret.data.id;
		} );
	});

	//声明跳转
	$("#CustomerSrc").on("click",function(){
	    $("#DeclarifaWrap").empty();
		$("#DeclarifaWrap").attr("src","./declareByCustomer.do?ifIfaCheck="+ifIfaCheck);
		$("#DeclarifaBack").css("display","block");
		$("#Declarifa").css("display","block");
	});

	$("#personalSrc").on("click",function(){
        $("#DeclarifaWrap").empty();
		$("#DeclarifaWrap").attr("src","./declareCollectionStatement.do?ifIfaCheck="+ifIfaCheck);
		$("#DeclarifaBack").css("display","block");
		$("#Declarifa").css("display","block");
	});

	$("#byadvisorSrc").on("click",function(){
        $("#DeclarifaWrap").empty();
		$("#DeclarifaWrap").attr("src","./declareByAdvisor.do?ifIfaCheck="+ifIfaCheck);
		$("#DeclarifaBack").css("display","block");
		$("#Declarifa").css("display","block");
	});

	$("#qualifiedSrc").on("click",function(){
        $("#DeclarifaWrap").empty();
		$("#DeclarifaWrap").attr("src","./declareQualifiedPerson.do?ifIfaCheck="+ifIfaCheck);
		$("#DeclarifaBack").css("display","block");
		$("#Declarifa").css("display","block");
	});
	function declareClose(){
	    document.getElementById("DeclarifaBack").style.display = 'none';  
	    document.getElementById("Declarifa").style.display = 'none';
	    $.post(  base_root + "/front/investor/accountDeclareSave.do?accountId="+accountId ,agreeFlag, function(ret){
	    	if(ret.data)
				agreeFlag.id = ret.data.id;
		});
	}
	
	$("#bycustomer").on("click", function(){
	    document.getElementById("declarationFlag").checked = true;
	    agreeFlag.declarationFlag = "1";
	    declareClose();
	});
	
	$("#bycustomerno").on("click",function(){
	    document.getElementById("declarationFlag").checked = false;
	    agreeFlag.declarationFlag = "0"
	    declareClose();
	});

//	function personal(){
	$("#personal").on("click",function(){
	    document.getElementById("informationFlag").checked = true;
	    agreeFlag.informationFlag = "1"
	    declareClose();
	});
//	function personalno(){
	$("#personalno").on("click",function(){
	    document.getElementById("informationFlag").checked = false;
	    agreeFlag.informationFlag = "0"
	    declareClose();
	});

//	function byadvisor(){
	$("#byadvisor").on("click",function(){
	    document.getElementById("advisorFlag").checked = true;
	    agreeFlag.advisorFlag = "1"
	    declareClose();
	});
//	function byadvisorSrclno(){
	$("#byadvisorSrclno").on("click",function(){
	    document.getElementById("advisorFlag").checked = false;
	    agreeFlag.advisorFlag = "0"
	    declareClose();
	});
	$("#iframeCloseLink").on("click",function(){
	    declareClose();
	});
	
//	function qualified(){
	$("#qualified").on("click",function(){
	    document.getElementById("qualifiedFlag").checked = true;
	    agreeFlag.qualifiedFlag = "1"
	    declareClose();
	});
	//function qualifiedno(){
	$("#qualifiedno").on("click",function(){
	    document.getElementById("qualifiedFlag").checked = false;
	    agreeFlag.qualifiedFlag = "0"
	    declareClose();
	});
	
	$("#declarForm").validationEngine({
	     promptPosition: "inline",
	     scroll:true,
	     isOverflown:false,
	     autoPositionUpdate:true,
	     validateNonVisibleFields:false,
	     focusFirstField:false,
	     showArrow:false
	 });	
});	