/**
 * openAccountStart.js 开户界面start
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-06
 */
define(function(require) {
	var $ = require('jquery');

 	$(".ifahelped").on("click",function(){
 		// console.log($("input[name='ifachioce']:checked").val()); 
 		$(this).siblings().removeClass("ifaTypeActive").end().addClass("ifaTypeActive");
 		$(".OpenAccount_ifa").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor").hide();
 		$(".OpenAccount_xfa_ifa").show();

 	});

 	$(".ifamyself").on("click",function(){

 		$(this).siblings().removeClass("ifaTypeActive").end().addClass("ifaTypeActive");
 		$(".OpenAccount_ifa").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor").show();
 		$(".OpenAccount_xfa_ifa").hide();

 	});
 	$(".OpenAccount_ifa").on("click",function(){
 		$(".OpenAccount_ifa").removeClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(this).addClass("ifaActive");
 		$(".OpenAccount_xfa_Dstributor").show();
 	});
 	$(".OpenAccount_xfa_Dstributor img").on("click",function(){
 		$(".OpenAccount_xfa_Dstributor img").removeClass("ifaActive");
 		$(this).addClass("ifaActive");
 	});
    $("#btn_next").on("click",function(){
            window.location.href = "/index.php?r=member/accountrpq";
    });
 });