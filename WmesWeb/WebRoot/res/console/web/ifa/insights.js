
define(function(require) {
	var $ = require('jquery');
	require('layer');	
	var Loading = require('loading');
	var oLoading = new Loading($(".portfolio_list"));	
	var searchData = null;
	var page=0;
	var total=0;
	var perPage=8;
    initialization();
  //点击每个选项，在下面的已选方案中添加该选项
	$(".funds_all_Sector").on("click", function() {
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
		for(var i=1;i<21;i++){
			$(".selection_criteria li[data-name='Sector"+i+"']").remove();
		}
		initialization();
	});
	
	$('.funds_choice_amend li').on('click',function(){
		if($(this).attr('data-value')=='Sector_00'){
			$('.funds_choice_amend li').css('color','#000');
		}else{
			$(this).css('color','#4ba6de');
		}
		initialization();
	});
	
	
	$("#funds_logo_choice li").on("mousemove",function(){
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
		var this_letter	= $(this).attr("data-letter"),
			funds_logo = $("#funds_logo").children(),
			funds_logo_lenght = funds_logo.length;
		for (var k = 0 ; k< funds_logo_lenght; k++){
			if( this_letter.indexOf(funds_logo.eq(k).attr("data-letter"))>=0 ){
 				funds_logo.eq(k).show();
			}else{
				funds_logo.eq(k).hide();
			}
		}
		if($(this).hasClass("funds_all") && $(this).hasClass("funds_aloac")){
			$(".funds_logo_choice li").removeClass("fund_logo_active");
			$(this).removeClass("funds_aloac");
			var selection_criterialenght = $(".selection_criteria li").length;
			for(var i = 0; i < selection_criterialenght ;i++){
				if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
					$(".selection_criteria li").eq(i).addClass("thisremove");
				}
			}
			$(".thisremove").remove();
		}
	});
	
	$(".funds_choice li").on("click", function() {
		if($(this).parent().hasClass("funds_logo_b")) {
			return;
		}
		var selection_criterialenght = $(".selection_criteria li").length;
		for(var i = 0; i < selection_criterialenght; i++) {
			if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name")) {
				$(".selection_criteria li").eq(i).remove();
			}
		}
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
		if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
			$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>');
		}
		initialization();
	});
	
	$(".funds_logo_choice li").on("click",function(){
		if($(this).hasClass("fund_logo_active1") ){
			var selection_criterialenght = $(".selection_criteria li").length;
			for(var i = 0; i < selection_criterialenght ;i++){
				if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value") ){
					$(".selection_criteria li").eq(i).remove();
				}
			}
			$(this).removeClass("fund_logo_active1");
		}else{
			if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
				$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") +'">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>');
			}
			$(this).addClass("fund_logo_active1");
		}
		$("#funds_logo_choice .funds_all").addClass("funds_aloac");
		initialization();
	});
			
	//执行清除方案点击操作
	$(".funds_title_selection").on("click", function() {
		$(".selection_criteria li").remove();
		$(".fund_logo_active").removeClass("fund_logo_active");
		$(".fund_choice_active").removeClass("fund_choice_active");
		$(".funds_all").addClass("fund_choice_active");
		$('.funds_choice_amend li').css('color','#000');
		initialization();
	});
	
	/**
	 * 显示清除所有搜索条件按钮
	 * */
	function selection() {
		var _thisLenght = $(".selection_criteria").children().length;
		if(_thisLenght != 1) {
			$(".funds_title_selection").css('display', 'inline-block');
		} else {
			$(".funds_title_selection").css('display', 'none');
		}
	};
	
	/**搜索条件取消点击
	 * author scshi
	 * date 20160821
	 */
	$(".selection_criteria").on("click", ".selection_delete", function() {
		$(this).parent().remove();
		var funds_all_lenght = $('.funds_all').length;
		for(var i = 0; i < funds_all_lenght; i++) {
			if($(this).parent().attr("data-name") == "FundHouse") {
				var fundslenght = $("#funds_logo li").length;
				for(var funds = 0; funds < fundslenght; funds++) {
					if($(this).parent().attr("data-value") == $("#funds_logo li").eq(funds).attr("data-value")) {
						$("#funds_logo li").eq(funds).click();
					}
				}
				break;
			} else if($(this).parent().attr("data-name") == $('.funds_all').eq(i).attr("data-name")) {
				$('.funds_all').eq(i).click();
			}
		}

		var prefCount = 0;
		$(".selection_criteria").find("li").each(function() {
			$(this).attr("data-value") == "pref";
			prefCount++;
		});
		if(prefCount == 0) $("#per_all").addClass("per_active");
		selection();
		
		var dataValue = $(this).parent().attr("data-value");
		$('.funds_choice_amend li[data-value="'+dataValue+'"]').removeClass('fund_choice_active');
		$('.funds_choice_amend li[data-value="'+dataValue+'"]').css('color','#000');
		if($('.funds_choice_amend li').hasClass('fund_choice_active') == 0){
			$('.funds_choice_amend').find('.funds_all_Sector').addClass('fund_choice_active');
		}
		initialization();
	});
	

	$('.funds_arrow_down').on("click",function(){
		$('.funds_arrow_down').removeClass("funds_down_active");
		$('.funds_arrow_top').removeClass("funds_top_active");
		$(this).parents("li").siblings().removeClass("funds_active").end().addClass("funds_active");
		$(this).addClass("funds_down_active");
	});
	$('.funds_arrow_top').on("click",function(){
		$('.funds_arrow_down').removeClass("funds_down_active");
		$('.funds_arrow_top').removeClass("funds_top_active");
		$(this).parents("li").siblings().removeClass("funds_active").end().addClass("funds_active");
		$(this).addClass("funds_top_active");
	});
	
	$('.funds_choice_wrap_hiddenclick').on('click',function(){
		var choiceHeight = $('.funds_choice_amend').css('height');
		$('.funds_choice_wrap_hiddenclick').toggleClass('funds_choice_wrap_showclick');
		if($(this).hasClass('funds_choice_wrap_showclick')){
			$(this).parents('.funds_choice_wrap_hidden').removeAttr('max-height');
			$(this).parents('.funds_choice_wrap_hidden').animate({'min-height':choiceHeight});
		}else{
			$(this).parents('.funds_choice_wrap_hidden').animate({'min-height':'34px'});;
		}
		
	});
	$('#clickmore').on('click',function(){
		searchList();
		searchData = $("#paramsForm").serialize();
		page=page+1;
		loadInsightsList();
	});
	$('#searchKeyBtn').on('click',initialization);
	//排序功能
	$('.recommend-switch-tab').find("li").on('click',function(){
		
	});
	
	/**
	 * 实例化操作
	 * 
	 * */
	function initialization(){
		searchList();
		searchData = $("#paramsForm").serialize();
		page=1;
		loadInsightsList();
	}
	
	function searchList(){
		var regions="";
		var sectors="";
		var period="";
		$(".selection_criteria li").each(function(){
			var dataName = $(this).attr("data-name");
			var dataValue = $(this).attr("data-value");
			if(!dataValue)dataValue="";
			if("period"==dataName){
				period = dataValue;
			}else if("regions"==dataName){
				regions+=","+dataValue;
			}else if(dataName.indexOf("sector")==0){
				sectors+=","+dataValue;
			}
		});
		$("#period").val(period);
		$("#geoAllocation").val(regions);
		$("#sector").val(sectors);
	}
	
	//异步查询观点
	var listTime;
	function loadInsightsList() {
		clearTimeout(listTime);
		listTime=setTimeout(function(){
			var data =  "rows="+perPage+"&page=" + page +"&"+searchData;
			oLoading.show();
			$.ajax({
				type : 'POST',
				datatype : 'json',
				url : base_root+'/console/ifa/insightList.do?datestr=' + new Date().getTime(),
				data : data,
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
				success : function(json) {
					var html = "";
					total = json.total;
					var table = json.rows;
					var thumbnail = base_root+"/res/images/discover/new_006.jpg";
					$.each(table, function(i, n) {
							if(n.thumbnail!=null){
								thumbnail = base_root+n.thumbnail;
							}
							html +=	"<li>"+
		                	"	<img class='recommend-news-img' src='"+thumbnail+"'>"+
		                	"	<div class='recommend-news-main'>"+
		                	"		<div class='recommend-news-contents'>"+
		                	"			<p class='recommend-news-title'>"+n.title+"</p>"+
		                	"			<p class='recommend-news-word'>"+n.content+"</p>"+
		                	"			<div class='recommend-news-contents-bottom'>"+
		                	"				<p class='recommend-news-bottom-left'>"+n.lastUpdate+"</p>"+
		                	"				<div class='recommend-news-bottom-right'>"+
		                	"					<div class='recommend-news-bottom-ico'>"+
		                	"						<img class='recommend-news-bottom-img' src='"+base_root+"/res/images/discover/eve_ico.png'>"+
		                	"						<p class='recommend-news-bottom-num'>"+n.views+"</p>"+
		                	"					</div>"+
		                	"					<div class='recommend-news-bottom-ico'>"+
		                	"						<img class='recommend-news-msg-ico' src='"+base_root+"/res/images/fund/msg_ico.png'>"+
		                	"						<p class='recommend-news-eve-num'>"+n.comments+"</p>"+
		                	"					</div>"+
		                	"					<div class='recommend-news-bottom-like'>"+
		                	"						<img class='recommend-news-like-ico' src='"+base_root+"/res/images/fund/fund_like_ico.png'>"+
		                	"						<p class='recommend-news-like-num'>"+n.upCounter+"</p>"+
		                	"					</div>"+
		                	"					<div class='recommend-news-bottom-like'>"+
		                	"						<img class='recommend-news-like-ico' src='"+base_root+"/res/images/fund/fund_step_ico.png'>"+
		                	"						<p class='recommend-news-like-num'>"+n.downCounter+"</p>"+
		                	"					</div>"+
		                	"				</div>"+
		                	"			</div>"+
		                	"		</div>"+
		                	"		<div class='recommend-news-list-right'>"+
			                "			<div class='recommend-news-date-contents'>"+
			                "				<p class='recommend-news-list-date'>"+tips["createTime"]+"</p>"+
			                "				<p class='recommend-news-date-time'>"+n.createTime+"</p>"+
			                "			</div>"+
			                "		</div>"+
		                	"	</div>"+
		                	"</li>";
					});
					if(page==1){
						$('#insightsList').empty();
					}
					$('#insightsList').append(html);
					oLoading.hide();
					$("#datatotal").empty().html(total);
					if(perPage*page >= total){
						 $("#clickmore").find("a").html(tips["nomoredata"]);
					}else{
						 $("#clickmore").find("a").html(tips["clickmore"]);
					}
				}
			});
		},100);
	}



	
});