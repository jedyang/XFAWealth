define(function(require) {
	var $ = require('jquery');

	$(".tab_list_tab > li").on("click",function(){
		$(this).siblings().removeClass("now");
		$(".client-detail-contents > div").hide().eq($(this).index()).show();
	});
});	