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
		
		var actionName = $(this).attr("actionName")
		var actionHref = base_root+"/front/investor/"+actionName+".do?accountId="+acccount;
		document.getElementById("listFrame").src=actionHref;
		
		var ifm= document.getElementById("listFrame");
		var subWeb = ifm.contentDocument;
		subWeb.getElementsByTagName('input').disabled=true;
	})
	

});