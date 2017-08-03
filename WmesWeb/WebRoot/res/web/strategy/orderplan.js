
define(function(require) {


	var $ = require('jquery');
			require('datepick');
			require("swiper");
		
	new Swiper('.swiper-container',{
		nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        onSlideChangeStart:function(swiper){
            // $("#zone-ifa").html( $(".zone-personal-swiper .swiper-slide-active").attr("data-name") );
        }
	});

	// var g_calendarObject = new JsDatePick({
 //      useMode:2,
 //      target:"order-choice",
 //      dateFormat:"%Y-%m-%d"
 //    });

	$("#order-portrait-show").on("click",function(){
		$(".order-plan-sapce").toggleClass("order-plan-block");
	});

	$(".order-setting-cycle").on("click",function(){
		$(this).siblings().removeClass("order-cycle-active").end().addClass("order-cycle-active");
		$(".order-choice-list .order-choice-date").hide().eq($(this).index()-1).show();
	});

	$(".client-more-ico").on("click",function(){
		$(this).parents(".order-plan-rows").toggleClass("client-more-active");
	});

	$(".order-setting-xiala").on("click",function(){
		$(this).parents(".order-setting-table-rows").toggleClass("client-small-active");
	});

	// 下拉
	$(".client_xiala").on("click",function(){
		$(this).toggleClass("client_xiala_active");
	});
	$(".client_xiala li").on("click",function(e){
		$(this).parents(".client_xiala").toggleClass("client_xiala_active").find("input").val($(this).html());
		e.stopPropagation(); 
		window.location.href=base_root+"/front/transaction/transactionApproval.do?id="+id +"&currency="+$("#currency").val();
	});

	$(".order-number-top").on("click",function(){
		$("#order-number-choice").val(Number($("#order-number-choice").val()) + 1);
	});
	$(".order-number-bottom").on("click",function(){
		if( Number($("#order-number-choice").val()) == 0)return;
		$("#order-number-choice").val(Number($("#order-number-choice").val()) - 1);
	});
	
	$(".order-button-reject").on("click",function(){
		var status="-1";
		submit(status);
	})
	
	$(".order-button-approved").on("click",function(){
		var status="1";
		submit(status);
	})
	
	function submit(status){
		var comment=$(".order-textarea-text").val();
		////console.log(comment);
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/transaction/tranApproval.do",
			data:{id:id,comments:comment,status:status},
			success:function(json){
				if(json.result){
					alert("成功！")
				}else
					alert("失败！")
			}
		})
	}
});