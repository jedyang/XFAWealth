
define(function(require) {

	var $ = require('jquery');

	$(".tab_list_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".client-detail-contents > div").hide().eq($(this).index()).show();
	});
	
	var Url_type = getQueryString('type');

	if (Url_type == "prospect"){

		$("#detail-transaction").hide();
		$("#detail-analysis").hide();
		$("#detail-account").hide();
		$("#detail-portfolio").hide();
		$("#detail-schedule").click();
		$(".detail-schedule").addClass("now");
		$(".client-detail-contents").eq($("#detail-schedule").index()).show();
	}else{
		$("#detail-portfolio").addClass("now");
		$(".client-detail-contents").eq($("#detail-portfolio").index()).show();
	}
})