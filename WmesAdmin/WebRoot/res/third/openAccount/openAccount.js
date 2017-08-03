/**
 * OpenAccount.js 开户界面start
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-06
 */
define(function(require) {
	var $ = require('jquery');

 	$("#ifaHelped").on("click",function(){
 		// console.log($("input[name='ifachioce']:checked").val()); 	
 		$(".OpenAccount_yourxfa").removeClass("OpenAccount_chioce");	
 		$(".Select_Dstributor").hide();
 		$(".OpenAccount_ifaWrap").show();
 	});

 	$("#ifaMyself").on("click",function(){
 		$(".OpenAccount_yourxfa").removeClass("OpenAccount_chioce");
 		$(".Select_Dstributor").show();
 		$(".OpenAccount_ifaWrap").hide();
 	});

 	$(".OpenAccount_yourxfa").on("click",function(){
 		$(".OpenAccount_yourxfa").removeClass("OpenAccount_chioce");
 		$(".select_dstributor").removeClass("OpenAccount_chioce");
 		$(this).addClass("OpenAccount_chioce");
 		$(".Select_Dstributor").show();
 	});

 	$(".select_dstributor").on("click",function(){
 		$(this).siblings().removeClass("OpenAccount_chioce").end().addClass("OpenAccount_chioce");
 	});

    $("#btn_next").on("click",function(){
            window.location.href = "/index.php?r=member/accountrpq";
    });
 });