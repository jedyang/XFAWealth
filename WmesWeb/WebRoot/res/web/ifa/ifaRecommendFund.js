/**
 * fundsscreener.js 
 * @author tejay zhu
 * @date: 2016-08-16
 */


define(function(require) {
	
	var $ = require('jquery'),
		angular = require('angular');
		require("scrollbar");

		$('.funds_search_wrap').perfectScrollbar();
	    function Fundsscrool(){

	    	//表头滚动
				var funds_hei =  $(".funds_search_tab").height(),
					header_top = $(".wmes_top").height(), 
					 wmes_window_top = $(window).scrollTop(),
					infor_top = $(".funds_search_information").offset().top,
					_thisStyle;
					 
				if( $('.no_list_tips').css('display') == "block" ){
					return false;
				}
				 if ( wmes_window_top > (infor_top  - header_top) ){
				 	var new_funds_top = wmes_window_top - infor_top + header_top + funds_hei;
				 	$(".funds_search_tab").addClass("funds_fixed").css
				 		({
					 		'top':header_top,
					 		"width" : $(".funds_selected").width()
					 	});
						 _thisStyle =  "-webkit-transform : translateY(" + new_funds_top + "px);-moz-transform : translateY(" + new_funds_top + "px);-ms-transform : translateY(" + new_funds_top + "px);-o-transform : translateY(" + new_funds_top + "px);transform : translateY(" + new_funds_top + "px)";
						$('.funds_tables_th').attr("style",_thisStyle);

						//解决低版本浏览器tr无法滚动问题
						if( infor_top == $('.funds_tables_th').offset().top && new_funds_top > funds_hei + 2 ){
							// //console.log(infor_top + '---' + $('.funds_tables_th').offset().top)
							$('.funds_tables_th').addClass("funds_tables_caption")
						}
						$(".ps-scrollbar-x-rail").css("top",new_funds_top + $('.funds_tables_th').height());
				 }else{

				 	$(".funds_search_tab").removeClass("funds_fixed").css
				 		({
					 		'top':0,
					 		"width" : 'auto'
					 	});
						 _thisStyle =  "-webkit-transform : translateY(" + 0 + "px);-moz-transform : translateY(" + 0 + "px);-ms-transform : translateY(" + 0 + "px);-o-transform : translateY(" + 0 + "px);transform : translateY(" + 0 + "px)";
						$('.funds_tables_th').attr("style",_thisStyle);
						$(".ps-scrollbar-x-rail").css("top",60);

				 }
	    };
    	$(window).on('scroll',Fundsscrool);


	// 数据控制
	var mybase = angular.module('mySearch', []);
    mybase.controller('Searchctrl', ['$scope', '$http', function($scope, $http) {
	 		var url = base_root + "/front/fund/info/getFundListJson.do", //url
	 			iPAGE = 1, //第N页数据
	    		is_finish = true,//当前数据是否加载完成
	    		Searchdata = "",//搜索条件初始化
	    		Sortdata = "sort=issue_price&order=desc",//排序条件初始化 默认根据issue price（增长率降序）
	    		page_bol = true;//总页数控制
	    		
	    		//初始化数据
	    		$scope.dataList = '';

	    	// 数字调用
	    	$scope.counter = Array;
	    	
	    	//监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){

					//删除渲染效果
					$(".animated").one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				    		$(".animated").removeClass();
				    });

					//you can also do something 

				}
			}


	    	var rows = 10  //固定每次拿10条数据
	    	// 正常拿数据	 
	    	function getDataList(){
	    			// data =  "sort=risk_level&order=desc&langCode=en&rows="+ rows +"&page=" + iPAGE;
	    			var data =  "langCode=" + lang + "&rows="+ rows +"&page=" + iPAGE + "&" + Searchdata + "&" + Sortdata;

	    			//控制数据是否加载完成
	    			is_finish = false;
	    			$http({
                      url: url,
                      method: 'POST',
                      data : data,
                      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                 	 })
	    			.success(function(response){
		                 is_finish = true;
	                      if(response.list.length > 0){
	                      		$(".funds_search_information , .funds_search_wrap").show();
	                      		$(".no_list_tips").hide();
	                      		 if( iPAGE === 1 ){
	                      		 	 $scope.dataList = '';
	                                 $scope.dataList =  response.list;
	                              }else{
	                                 $scope.dataList = $scope.dataList.concat(response.list);
	                              }
	                      		// 总页数
	                      		var sumPage = Math.ceil(response.total / rows);
	                      		if(iPAGE >= sumPage){
	                      			page_bol = false;
	                      		}
                      		}else{
                      			page_bol = false;
                     			 if( iPAGE === 1 ){

                      			 	// 当第一页没有数据，显示提示信息
                      			 	$(".funds_search_information , .funds_search_wrap").hide()
                      			 	$(".no_list_tips").show();
                      			 }
                      		}
  		    			iPAGE++;
  		    			$scope.datatotal = response.total;
                    });
	    	}
	    	getDataList();
			
	    	// 滚动条件
	    	var scrollBol = false;

	    	// 滚动拿数据
	    	setTimeout(function(){
	    		$(window).on('scroll', GetScroll);
	    	},1000);	

	    	function Initialization(){
				// 初始化数值
			 	iPAGE = 1; //第N页数据
	    		is_finish = true;
	    		page_bol = true;
	    		selection();
	    		getDataList();
	    	}
	    	function GetScroll(){
	    		var docheight = $(window).scrollTop() + $(window).height(),
	    			listheight = $('.funds_selected_wrap ').offset().top + $('.funds_selected_wrap ').height() + $(".wmes_top").height();
	    		
	    		if( docheight > listheight ){
	    			scrollBol = true;
	    			
	    		}else{
	    			scrollBol = false;
	    		}

    			if (scrollBol && is_finish && page_bol ){
    				getDataList();
    			}

	    	}

	    	// 排序点击
	    	$(".funds_sort").on("click",function(){

	    		$(".funds_active_sort").removeClass("funds_active_sort");
	    		$(this).addClass("funds_active_sort");
	    		if( $(this).find(".arrow_top").hasClass("top_active") ){
	    			$('.arrow_down').removeClass("down_active");
	    			$('.arrow_top').removeClass("top_active");
	    			$(this).find(".arrow_down").addClass("down_active");
	    			dataSoat($(this).attr("data-type"),"desc");
	    		}else if( $(this).find(".arrow_down").hasClass("down_active") ){
	    			$('.arrow_down').removeClass("down_active");
	    			$('.arrow_top').removeClass("top_active");
	    			$(this).find(".arrow_top").addClass("top_active");
	    			dataSoat($(this).attr("data-type"),"asc");
	    		};
	    		
	    	});

	    	//数据排序
	    	function dataSoat(type,sort){
	    		Sortdata = "sort=" + type + "&order=" + sort;
	    		Initialization();
	    	}

			$("#funds_logo_choice li").on("mousemove",function(){

				$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");

				var this_letter		  = $(this).attr("data-letter"),
					funds_logo 		  = $("#funds_logo").children(),
					funds_logo_lenght = funds_logo.length;
				for (var k = 0 ; k< funds_logo_lenght; k++){
					if( this_letter == funds_logo.eq(k).attr("data-letter") ){
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
					loadFundList(this);
				}
			});
			var listTime;
			$(".funds_choice li").on("click",function(){
				clearTimeout(listTime)
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

				// 解决重复请求的问题
				var self = this;
					listTime=setTimeout(function(){
						loadFundList(self);
					}
					,100);
			});
	
			$(".funds_starts li").on("click",function(){
				var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght ;i++){
						if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
							$(".selection_criteria li").eq(i).remove();
						}
				}
				$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
				if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
					$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + $(this).html() + '<span class="selection_delete"></span></li>')
				}
				loadFundList(this);
			});
			$("#per_all").on("click",function(){
				$("#per_all").addClass("per_active");
				$('#listForm').find(".perfClean").val("");
				$("#funds_per_content").find(".funds_all").click();
			});

			$(".funds_logo_choice li").on("click",function(){
				if($(this).hasClass("fund_logo_active") ){
					var selection_criterialenght = $(".selection_criteria li").length;
					for(var i = 0; i < selection_criterialenght ;i++){
						if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value") ){
							$(".selection_criteria li").eq(i).remove();
						}
					}
					$(this).removeClass("fund_logo_active");
				}else{
					if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
						$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") +'">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
					}
					$(this).addClass("fund_logo_active");
				}
				$("#funds_logo_choice .funds_all").addClass("funds_aloac");
				loadFundList(this);

			});
			$("#funds_per_content .fund_xiala_active").find("li").on("click",function(){
				var self = $(this);
				if($(this).hasClass("funds_all") == false){
					$("#per_all").removeClass("per_active");
					// 修改：基金规模只能单选
					$('#listForm').find(".perfClean").val("");
					self.parents(".fund_xiala_active").siblings().find(".funds_all").click();
					clearTimeout(listTime);
					loadFundList(self);

					
				}
				
			})
	
			$('#funds_per_choice li').on("click",function(){
				if( $(this).hasClass("fund_xiala_active")){
					$(this).removeClass("fund_xiala_active");
					$("#funds_per_content").children().hide().eq( $(this).index() ).hide();
				}else{
					$(this).siblings().removeClass("fund_xiala_active").end().addClass("fund_xiala_active");
					$("#funds_per_content").children().hide().eq( $(this).index() ).show();
				}
				// loadFundList(this);
			});
			$(".funds_title_selection").on("click",function(){
				$(".selection_criteria li").remove();

				$(".fund_logo_active").removeClass("fund_logo_active");


				$(".fund_choice_active").removeClass("fund_choice_active");
				$(".funds_all").addClass("fund_choice_active");
				$("#listForm").find("input").val("");
				Searchdata = $("#listForm").serialize();
				Initialization();
			});
			function selection(){
				var _thisLenght =  $(".selection_criteria").children().length
				
				if( _thisLenght != 1  ){
					$(".funds_title_selection").css('display','inline-block');
				}else{
					$(".funds_title_selection").css('display','none');
				}
			}


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
						// loadFundList($('.funds_all').eq(i));
					}
				}
				
				var prefCount=0;
				$(".selection_criteria").find("li").each(function(){
					$(this).attr("data-value")=="pref";
					prefCount++
				})
				if(prefCount==0)$("#per_all").addClass("per_active");
			});
	

			$(".icon_search").on("click",function(){
				var fundName = $("#fundName").val();
				document.getElementById("keyword").value=fundName
				Searchdata = $("#listForm").serialize();
			 	Initialization();
			})
			$("#fundName").keyup(function(event){
	       	 if(event.keyCode == 13){
		        	document.getElementById('searchKeyBtn').click()
		        }
		    });		
	
			function loadFundList(searchLi){
				
				var dataName = $(searchLi).attr("data-name");
				var sizeFrom = $(searchLi).attr("data_from");
				var sizeTo = $(searchLi).attr("data_to");
				var dataValue = $(searchLi).attr("data-value");
											
				if(!sizeFrom)sizeFrom="";
				if(!sizeTo)sizeTo="";
				if(!dataValue)dataValue="";
				if("Fundsize"==dataName){
					document.getElementById("fundSizeFrom").value=sizeFrom;
					document.getElementById("fundSizeTo").value=sizeTo;
				}
				
				if("Sincelaunch"==dataName){
					document.getElementById("perfLaunchFrom").value=sizeFrom;
					document.getElementById("perfLaunchTo").value=sizeTo;
				}
				if("YTD"==dataName){
					document.getElementById("perfYTDFrom").value=sizeFrom;
					document.getElementById("perfYTDTo").value=sizeTo;
				}
				if("1Week"==dataName){
					document.getElementById("perfOneWeekFrom").value=sizeFrom;
					document.getElementById("perfOneWeekTo").value=sizeTo;
				}
				if("1Mon"==dataName){
					document.getElementById("perfOneMonthFrom").value=sizeFrom;
					document.getElementById("perfOneMonthTo").value=sizeTo;
				}
				if("3Mon"==dataName){
					document.getElementById("perfThreeMonthFrom").value=sizeFrom;
					document.getElementById("perfThreeMonthTo").value=sizeTo;
				}
				if("6Mon"==dataName){
					document.getElementById("perfSixMonthFrom").value=sizeFrom;
					document.getElementById("perfSixMonthTo").value=sizeTo;
				}
				if("1Yr"==dataName){
					document.getElementById("perfOneYearFrom").value=sizeFrom;
					document.getElementById("perfOneYearTo").value=sizeTo;
				}
				if("3Yr"==dataName){
					document.getElementById("perfThreeYearFrom").value=sizeFrom;
					document.getElementById("perfThreeYearTo").value=sizeTo;
				}
				if("5Yr"==dataName){
					document.getElementById("perfFiveYearFrom").value=sizeFrom;
					document.getElementById("perfFiveYearTo").value=sizeTo;
				}
				if("LipperCr"==dataName){
					document.getElementById("lipperCR").value=dataValue;
				}
				if("Fitch"==dataName){
					document.getElementById("fitch").value=dataValue;
				}
				if("Citywire"==dataName){
					document.getElementById("citywire").value=dataValue;
				}
				if("MinInitialInv"==dataName){
					document.getElementById("minInitialInv").value=dataValue;
				}
				if("MgtFee"==dataName){
					document.getElementById("mgtFee").value=dataValue;
				}
				if("FundHouse"==dataName){
					var houseId = "";
					//var houseLength = $(".fund_logo_active").length-1;
					$(".fund_logo_active").each(function(index,element){
						//var houseIndex = index;
						houseId += $(this).attr("data-value");
						//if(houseLength != houseIndex && houseLength != 0){
							houseId += ",";
						//}
					})
					document.getElementById("fundHouseIds").value=houseId;
				}
			 	Searchdata = $("#listForm").serialize();
			 	Initialization();
			}
    }]);

	



	// 货币转换
	var conversion = require("conversion");

	$(".conversion_select li").on("click",function(){

		new conversion($(this).attr("data-conversion"),$(this).html());
		$(this).parents(".funds_currency_xiala").find("input").val( $(this).html() );

	}) 
	$(".funds_currency_xiala").on("click",function(){

		if( $(this).find("ul").hasClass("funds_currency_active") ){
			$(this).find("ul").removeClass("funds_currency_active").hide();
		}else{
			$(this).find("ul").addClass("funds_currency_active").show();

		}
	});

	// 更多选择
	$("#yfunds_hidde_title").on("click",function(){
		if($(this).parent().hasClass("funds_more_wrap_show")){
			$(this).parent().removeClass("funds_more_wrap_show");
			 $(".fund_more_content").stop().animate({ 
		    	height: "0px"
			  }, 300 );
	 		$(this).find("span").eq(0).css("display","inline-block");
	 		$(this).find("span").eq(1).css("display","none");
		}else{
			$(this).parent().addClass("funds_more_wrap_show");
			$(".fund_more_content").stop().animate({ 
		    	height: "100%"
			 }, 300 );
			$(this).find("span").eq(1).css("display","inline-block");
	 		$(this).find("span").eq(0).css("display","none");
		}
	});


	// tab切换

	// $(".funds_search_tab li").on("click",function(){

	// 	$(this).siblings().removeClass("now").end().addClass("now");
	// 	$(".funds_search_Part").hide().eq($(this).index()).show();

	// });
	$(document).on("click",".funds_cart",function(){
		alert("该功能暂未开放");
	})
	// 收藏
	$(".white_background").on("click",".funds_heart",function(){

		var loginVal = $("#loginCode").val(),
			_this = $(this),
			status  = _this.attr("followFlag"),
			fundId = _this.attr("fundId");
		if(loginVal == 'true'){
	    	$.ajax({
	    		url:base_root+"/front/fund/info/setFoundCollection.do?r="+Math.random(),
	    		data:{opType:status,fundId:fundId},
	    		dataType:"json",
	    		type:"get",
	    		success:function(data){
	    			if(data.result){
	    				if(status == 'Delete'){
	    					$(".funds_heart" + fundId).removeClass("funds_heart_active");
	    					$(".funds_heart" + fundId).attr("followFlag",'Follow');
	    				}else{
							$(".funds_heart" + fundId).addClass("funds_heart_active");
	    					$(".funds_heart" + fundId).attr("followFlag",'Delete');
	    				}
	    			}
	    		},
	    		error:function(){
	    			alert("error!");
	    		}
	    	})
		}else{
			window.parent.location.href=base_root+"/front/viewLogin.do?r="+Math.random();
			return;
		}

		
	})
});