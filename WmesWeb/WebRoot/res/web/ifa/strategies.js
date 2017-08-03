define(function(require) {
	var $ = require('jquery');
	//pagination = require('pagination');
	require('layer');
	require("scrollbar");

	//点击每个选项，在下面的已选方案中添加该选项
//	var listTime;
	$(".funds_choice li").on("click",function(){
//		clearTimeout(listTime)
		if($(this).parent().hasClass("funds_logo_b")){
			return;
		}
		var selection_criterialenght = $(".selection_criteria li").length;
		for(var i = 0; i < selection_criterialenght ;i++){
				if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
					$(".selection_criteria li").eq(i).remove();
				}
		}
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
		if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
			$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
		}
		Initialization();
		// 解决重复请求的问题
//		var self = this;
//			listTime=setTimeout(function(){
//				loadIFAList(self);
//			}
//			,100);
	});
	
	//执行清除方案点击操作
	$(".funds_title_selection").on("click",function(){
		$(".selection_criteria li").remove();

		$(".fund_logo_active").removeClass("fund_logo_active");

		$(".fund_choice_active").removeClass("fund_choice_active");
		$(".funds_all").addClass("fund_choice_active");
		//$("#listForm").find("input").val("");
		//Searchdata = $("#listForm").serialize();
		Initialization();
	});	
	
	/**搜索条件取消点击
	 * author scshi
	 * date 20160821
	 */
	$(".selection_criteria").on("click",".selection_delete",function(){
		$(this).parent().remove();
		var funds_all_lenght = $('.funds_all').length;
		for( var i = 0; i < funds_all_lenght ; i++){
			if($(this).parent().attr("data-name") == "FundHouse"){
				var fundslenght = $("#funds_logo li").length;
				for(var funds = 0 ; funds < fundslenght ;funds++){
					if( $(this).parent().attr("data-value") ==  $("#funds_logo li").eq(funds).attr("data-value") ){
						$("#funds_logo li").eq(funds).click();
					}
				}
				break;
			}else if( $(this).parent().attr("data-name") ==  $('.funds_all').eq(i).attr("data-name") ){
				$('.funds_all').eq(i).click();
			}
		}
		
		var prefCount=0;
		$(".selection_criteria").find("li").each(function(){
			$(this).attr("data-value")=="pref";
			prefCount++
		})
		if(prefCount==0)$("#per_all").addClass("per_active");
	});	

	/**
	 * 显示清除所有搜索条件按钮
	 * */
	function selection(){
		var _thisLenght =  $(".selection_criteria").children().length
		
		if( _thisLenght != 1  ){
			$(".funds_title_selection").css('display','inline-block');
		}else{
			$(".funds_title_selection").css('display','none');
		}
	}	
	/**
	 * 实例化操作
	 * 
	 * */
	function Initialization(){
		// 初始化数值
//	 	iPAGE = 1; //第N页数据
//		is_finish = true;
//		page_bol = true;
		selection();
	}
	
});	