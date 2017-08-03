
define(function(require) {

	var $ = require('jquery');
	var interfaceObj = require("interfaceCheckPwd");
	
	var baseCurrency=getQueryString("cur");
	if(baseCurrency==undefined || baseCurrency==""){
		$("#in_use").attr("checked","true"); 
		$("#InApproval").attr("checked","true"); 
	}
	
	if(_checkList!=undefined && _checkList!=""){
		$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
	}
	
	$(".proposal-more-ico").on("click",function(){
		$(this).parents(".account-rows").toggleClass("account-rows-hide");
	});
	
	$(".proposal_xiala").on("click",function(){
		$(this).toggleClass("xiala-show");
	});
	$(".proposal_xiala").on("mouseleave",function(){
		$(this).removeClass("xiala-show");
	});
	$(".proposal_xiala li").on("click",function(e){
		$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").val($(this).html());
		$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").attr("code",$(this).attr("value"));
		e.stopPropagation(); 
		refreshform();
		
	});
	
	 $("#in_use").on("change", function () {
		 refreshform();
	 })
	 $("#InApproval").on("change", function () {
		 refreshform();
	 })
	 $("#Cancellation").on("change", function () {
		 refreshform();
	 })
	 
	 function refreshform(){
		 var in_use="";
		 var inApproval="";
		 var cancellation="";
		 if($("#in_use").is(':checked')){
			 in_use="1";
		 }else{
			 in_use="";
		 }
			
		 if($("#InApproval").is(':checked')){
			 inApproval="1";
		 }else{
			 inApproval=""; 
		 }
			
		 if($("#Cancellation").is(':checked')){
			 cancellation="1";
		 }else{
			 cancellation="";
		 }
			
		 var currency=$("#currency").attr("code");
		 window.location.href=base_root+"/front/investor/myAccountList.do?in_use="+in_use+"&inApproval="+inApproval+"&cancellation="+cancellation+"&cur="+currency;
	 }
	
});