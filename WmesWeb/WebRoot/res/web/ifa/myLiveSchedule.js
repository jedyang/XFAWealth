
define(function(require) {

	var $ = require('jquery');

			require('laydate');
	$(".live-schedule-tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".live-schedule-rows").hide().eq($(this).index()).show();
	});

  // 下拉
   $(".create-schedule-xiala").on("click",function(){
       $(this).toggleClass("xiala-show");
   });
   
   //点击下拉
   $(".create-schedule-xiala li").on("click",function(e){ 
       $(this).parents(".create-schedule-xiala").toggleClass("xiala-show").find("input").val($(this).html());
       e.stopPropagation(); 
   });   
   
    $(".create-schedule-xiala").on("mouseleave",function(){
   		if($(this).hasClass('xiala-show')){
   			 $(this).toggleClass("xiala-show");
   		}
       
    });

    $(".create-schedule-radio-rows").on("click",function(){

    	var _str = $('input[name="Figure"]:checked').val();

    	if( _str == "specify"){
    		$(".create-schedule-specify-wrapper").show();
    	}else{
    		$(".create-schedule-specify-wrapper").hide();
    	}
    });

  setTimeout(function(){
		if(!$('#broadcast').text() || $('#broadcast').text()=='NaN-NaN-NaN'){
			laydate({
			  istime: false,
			  elem: '#broadcast',
			  format: 'YYYY-MM-DD',
			  min: laydate.now()
			});
		}else{
			laydate({
			  istime: false,
			  elem: '#broadcast',
			  format: 'YYYY-MM-DD',
			  //min: $('#txtNextChargeDate').text()
			  min: laydate.now()
			});
		}
	},200);
		

	$(".live-schedule-new").on("click",function(){
		$(".live-schedule-mask").show();
		$(".create-schedule-wrap").show();
	});

	$("#create-schedule-close, .create-schedule-button-cancel").on("click",function(){
		$(".live-schedule-mask").hide();
		$(".create-schedule-wrap").hide();
	});
});
