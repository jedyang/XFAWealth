define(function(require) {
	var $ = require('jquery');
	require('range');
	/**
	 * 初始化回显数据
	 */
	 $(window).load(function() {
		var $form = $("#preference_form");
		var defCurrency = $form.find("#defCurrency").val();
		var langCode = $form.find("#langCode").val();
		var defDisplayColor = $form.find("#defDisplayColor").val();
		var dateFormat = $form.find("#dateFormat").val();
		var privacyContact = $form.find("#privacyContact").val();
		
		$("#preference_form select[name='defCurrency']").find("option[value='"+defCurrency+"']").attr("selected",true);
		$("#preference_form select[name='langCode']").find("option[value='"+langCode+"']").attr("selected",true); 
		$("#preference_form select[name='dateFormat']").find("option[value='"+dateFormat+"']").attr("selected",true);
		$("#preference_form input[name='defDisplayColor'][value='"+defDisplayColor+"']").attr("checked","checked");
		$("#preference_form input[name='privacyContact'][value='"+privacyContact+"']").attr("checked","checked");
		
		var nowFmt = $("#preference_form select[name='dateFormat']").find("option[value='"+dateFormat+"']").val();
		if(nowFmt!=null && nowFmt!=""){
			var fmt = dateFormatUtil(new Date(),nowFmt);
			$("#preference_form #dateChage").text(fmt);
			var langFmt = changeToEn(nowFmt);
			$("#preference_form #dateChage").text(langFmt);
			
		}
		//滑动
		$('.slider-input').jRange({
			from: 0, 
	        to: 100, 
	        step: 1, 
	        scale: [0, 25, 50, 75, 100], 
	        format: '%s', 
	        width: 300, 
	        showLabels: true, 
	        isRange: true 
		});
		
	 });
	
	/**
	 * 提交保存
	 */
	$("#savePreference").on("click",function(){
		var $form = $("#preference_form");
		var defCurrency = $form.find("select[name='defCurrency']").val();
		var langCode = $form.find("select[name='langCode']").val();
		var defDisplayColor = $form.find("input[name='defDisplayColor']:checked").val();
		var dateFormat = $form.find("select[name='dateFormat']").val(); 
		var privacyContact = $form.find("input[name='privacyContact']:checked").val();
		$.ajax({
			type:"POST",
			url:base_root+"/front/member/personal/savePreference.do",
			dataType: "JSON",
			data:{
				"defCurrency":defCurrency,
				"langCode":langCode,
				"defDisplayColor":defDisplayColor,
				"dateFormat":dateFormat,
				"privacyContact":privacyContact
			},
			success:function(response){
				if(response.result){
					
				}
			},
			error:function(response){}
		});
    });
	/**
	 * 选择切换日期格式展示
	 */
	$("#preference_form select[name='dateFormat']").change(function(){
		var nowFmt = $(this).val();
		var langFmt = changeToEn(nowFmt);
		$("#preference_form #dateChage").text(langFmt);
	});  
	
	/**
	 * MM格式的日期转成英文
	 */
	function changeToEn(format){
		var index = format.indexOf("MM");
		var value = dateFormatUtil(new Date(),format);
		if(index == -1){
			return value;
		}else{
			var months=["Jan","Feb","Mar","Apr","May","June","july","Aug","Sept","Oct","Nov","dec"];
			var preStr = value.substring(0,index);
			var endStr = value.substring(index+2,value.length);
			var monNum = value.substring(index,index+2);
			var langMon = months[Number(monNum)-1];
			return preStr+langMon+endStr;
		}
	}
	
	/**
	 * 日期格式转换方法
	 *  dateFormat(new Date(),"yyyy-MM-dd") ==> 2006-07-02  
	 */
	function dateFormatUtil(date,format)   
	{ //author: meizz   
	  var o = {   
	    "M+" : date.getMonth()+1,                 //月份   
	    "d+" : date.getDate(),                    //日   
	    "h+" : date.getHours(),                   //小时   
	    "m+" : date.getMinutes(),                 //分   
	    "s+" : date.getSeconds(),                 //秒   
	    "q+" : Math.floor((date.getMonth()+3)/3), //季度   
	    "S"  : date.getMilliseconds()             //毫秒   
	  };   
	  if(/(y+)/.test(format))   
	    format=format.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
	  for(var k in o)   
	    if(new RegExp("("+ k +")").test(format))   
	  format = format.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	  return format;   
	}  
	
});