/**
 * @author 
 * email: 445752972@qq.com
 * @date: 2016-08-11
 */

define(function(require) {
	var $ = require('jquery');
	require('layer');
	
	$(".wmes-name-top").on("click",function(){
		$(".ifa-space-popup").show();
	});
	$(".space-mask-close").on("click",function(){
		$(".ifa-space-popup").hide();
	});
	// $(".ifa-his-logo").on("click",function(){
	// 	window.location.href="target.aspx"
	// });
	
	$("#ul_region_select>li").on("click",function(){
		var datavalue = $(this).attr('data-value');
		getDataList(datavalue);
	});
	
	
});