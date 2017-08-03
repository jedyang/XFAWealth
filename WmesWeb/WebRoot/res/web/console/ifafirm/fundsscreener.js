/**
 * fundsscreener.js 
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-14
 */


define(function(require) {
	
	var $ = require('jquery'),
		angular = require('angular');
		require("scrollbar");
		require('layui');
		$(".lump_comparison_check").prop("checked",false);
		var Loading = require('loading');
		// 货币转换
		var conversion = require("conversion");
	 
		$('.funds_search_wrap').perfectScrollbar();
		$(".funds_search_tab").width($(".funds_serach_sum").width());
	    function Fundsscrool(){

	    	//表头滚动
				var funds_hei =  $(".funds_search_tab").height(),
					header_top = $(".wmes_top").height(), 
					 wmes_window_top = $(window).scrollTop(),
					infor_top = $(".funds_search_information").offset().top;
					//funds_top = infor_top - header_top;
					 
				if( $('.no_list_tips').css('display') == "block" ){
					return false;
				}
				 if ( wmes_window_top > (infor_top  - header_top) ){
				 	var new_funds_top = wmes_window_top - infor_top + header_top + funds_hei;
				 	$(".funds_search_tab").addClass("funds_fixed").css
				 		({
					 		'top':header_top,
					 		"width" : '90%'
					 	});
						var	 _thisStyle =  "-webkit-transform : translateY(" + new_funds_top + "px);-moz-transform : translateY(" + new_funds_top + "px);-ms-transform : translateY(" + new_funds_top + "px);-o-transform : translateY(" + new_funds_top + "px);transform : translateY(" + new_funds_top + "px)";
						$('.funds_tables_th').attr("style",_thisStyle);

						//解决低版本浏览器tr无法滚动问题
						if( infor_top == $('.funds_tables_th').offset().top && new_funds_top > funds_hei + 2 ){
							// //console.log(infor_top + '---' + $('.funds_tables_th').offset().top)
							$('.funds_tables_th').addClass("funds_tables_caption")
						};
						
						for(var i=0;i<$('.account-checkbox').length;i++){
							if($('.account-checkbox').eq(i).offset().top - $(window).scrollTop()<=108){
								$('.account-checkbox').eq(i).css('z-index','-1');
							}else{
								$('.account-checkbox').eq(i).css('z-index','0');
							};
					
						};
						
							
						//$(".ps-scrollbar-x-rail").css("top",new_funds_top + $('.funds_tables_th').height());
				 }else{
					$('.account-checkbox').eq(0).css('z-index','0');
				 	$(".funds_search_tab").removeClass("funds_fixed").css
				 		({
					 		'top':0,
					 		"width" : 'auto'
					 	});
					var	 _thisStyleY =  "-webkit-transform : translateY(" + 0 + "px);-moz-transform : translateY(" + 0 + "px);-ms-transform : translateY(" + 0 + "px);-o-transform : translateY(" + 0 + "px);transform : translateY(" + 0 + "px)";
						$('.funds_tables_th').attr("style",_thisStyleY);
						//$(".ps-scrollbar-x-rail").css("top",60);

				 }
	    };
		$(window).on('scroll',Fundsscrool);
	   		$(window).on('resize',function(){
	   		$(".funds_search_tab").width($(".funds_serach_sum").width())
	   	});
	// 数据控制
	var mybase = angular.module('mySearch', ['wmespage','truncate']);
    mybase.controller('Searchctrl', ['$scope', '$http', function($scope, $http) {
//	 		var url = base_root + "/front/fund/info/getFundListJson.do", //url
	 		var	iPAGE = 1, //第N页数据
	    		Searchdata = "",//搜索条件初始化
	    		Sortdata = "sort=issue_price&order=desc";//排序条件初始化 默认根据issue price（增长率降序）

	 		//格式化日期
	 		$scope.dateFormat = date_format;
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
				}
			}

	    	var sumpage = 0;
	    	var rows = 8;  //固定每次拿9条数据

	    	var oLoading = new Loading($(".funds-views-lump"));	
	    	var iLoading = new Loading($(".funds-views-list"));	
	    	// 正常拿数据	 
	    	function getDataList(){Searchdata = $("#listForm").serialize();
	    		var url = base_root + "/front/ifafirm/fund/getFundListJson.do",
	    			// data =  "sort=risk_level&order=desc&langCode=en&rows="+ rows +"&page=" + iPAGE;
	    			 data =  "langCode=" + lang + "&rows="+ rows +"&page=" + iPAGE + "&" + Searchdata + "&" + Sortdata;
					//oLoading.show();
					//iLoading.show();
	    			//控制数据是否加载完成
	    			$http({
                      url: url,
                      method: 'POST',
                      data : data,
                      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                 	 })
	    			.success(function(response){console.log(response);
	    				 oLoading.hide();
	    				 iLoading.hide();
		                 
		                 var total = response.total;
		                 $scope.datatotal = total;
		                 $scope.transactionConf.totalItems = total;
	    				  sumpage = Math.ceil(total / rows);
	    				  
	                      if(response.list.length > 0){
	                      		 if( iPAGE === 1 ){
	                      			$("#comparison-num").html("0");
	                      			$(".fund_float_menu_num").html("0");
	                                 $scope.dataList =  response.list;
	                              }else{
	                                 //$scope.dataList = $scope.dataList.concat(response.list);
	                            	  $scope.dataList =  response.list;
	                              }
	                      		// 总页数
//	                      		var sumPage = Math.ceil(response.total / rows);
	                      		exchange();
	                      		//工作台默认显示列表形式
	                      		if("IFA_FIRM"==sessionRole || "distributor"==sessionRole){
	                      			$(".funds_view_list_ico").click();
	                      		}
	                      		
                      		}else{
                     			 if( iPAGE === 1 ){
                     				$("#comparison-num").html("0");
                     				$(".fund_float_menu_num").html("0");
                     				$scope.dataList = '';
                      			 	// 当第一页没有数据，显示提示信息
                      			 	$(".no_list_tips").show();
                      			 }
                      		}
  		    			iPAGE++;
  		    			//$("#ifa_more").show();
  		    			//checkPage();
                    });
	    	}
	    	
	    	$scope.transactionConf = {
	                itemsPerPage:rows,
	                onChange: function(){
	                	iPAGE = $scope.transactionConf.currentPage;
	                	getDataList();
	                }
	            };
	    	
	    	getDataList();
			

	    	function Initialization(){
				// 初始化数值
			 	iPAGE = 1; //第N页数据
	    		$scope.dataList = '';
	    		$(".no_list_tips").hide();
	    		//$("#ifa_more").hide();
	    		checkPage();
	    		selection();
	    		getDataList();
	    	}
	    	function checkPage(){
//	    		if(iPAGE <= sumpage){
//	    			$("#ifa_more").html(langMutilForJs["fund.info.list.clickForMore"]);
//	    		}else{
//	    			$("#ifa_more").html(langMutilForJs["fund.info.list.noMore"]); //"没有更多的数据了"
//	    		}
	    		
	    	}
//	    	$("#ifa_more").on("click",function(){
//	    		if(iPAGE <= sumpage){
//	    			$(this).hide();
//	    			getDataList();
//	    		}
//	    	});

	    	// 排序点击
	    	$(".funds_sort").on("click",function(){

	    		$(".funds_active_sort").removeClass("funds_active_sort");
	    		$(this).addClass("funds_active_sort");
	    		if( $(this).find(".arrow_top").hasClass("top_active") ){
	    			$('.arrow_down').removeClass("down_active");
	    			$('.arrow_top').removeClass("top_active");
	    			$(this).find(".arrow_down").addClass("down_active");
	    			dataSoat($(this).attr("data-type"),"desc");
	    		}else 
	    		{
	    			$('.arrow_down').removeClass("down_active");
	    			$('.arrow_top').removeClass("top_active");
	    			$(this).find(".arrow_top").addClass("top_active");
	    			dataSoat($(this).attr("data-type"),"asc");
	    		}
	    		
	    	})

	    	//数据排序
	    	function dataSoat(type,sort){
	    		Sortdata = "sort=" + type + "&order=" + sort;
	    		Initialization();
	    	}

			$("#funds_logo_choice li").on("mousemove",function(){

				$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
				$(this).siblings().removeClass("fund_choice_active2").end().addClass("fund_choice_active2");

				var this_letter		  = $(this).attr("data-letter"),
					funds_logo 		  = $("#funds_logo").children(),
					funds_logo_lenght = funds_logo.length;
				for (var k = 0 ; k< funds_logo_lenght; k++){
					var letter = funds_logo.eq(k).attr("data-letter");
					letter = letter.substr(0,1).toUpperCase();
					if(letter != '' && this_letter.indexOf(letter) > -1 ){
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
				$(this).siblings().removeClass("fund_choice_active2");
				if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
					$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				};
				if($(this).closest('ul').find('.funds_all').hasClass('fund_choice_active')){
					$(this).closest('ul').find('.funds_all').addClass('fund_choice_active2');
				}else{
					$(this).closest('ul').find('.funds_all').removeClass('fund_choice_active2');
				}

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
				$(this).siblings().removeClass("fund_choice_active2").end().addClass("fund_choice_active2");
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

			$("#funds_logo li").on("click",function(){
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
						$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") +'">' + langMutilForJs["fund.info.fundHouse"] +'：' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
					}
					$(this).addClass("fund_logo_active");
				}
				$("#funds_logo_choice .funds_all").addClass("funds_aloac");
				loadFundList(this);

			});
			
			//点击代理商
			$(".distributors_logo_choice li").on("click",function(){
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
						$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") +'">' + langMutilForJs["member.distributor"] +'：' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
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
	
			$('#funds_per_choice li').on("mouseenter",function(){
				if( $(this).hasClass("fund_xiala_active")){
					$(this).removeClass("fund_xiala_active");
					$("#funds_per_content").children().hide().eq( $(this).index() ).hide();
				}else{
					$(this).siblings().removeClass("fund_xiala_active").end().addClass("fund_xiala_active");
					$("#funds_per_content").children().hide().eq( $(this).index() ).show();
				};
				$(this).closest('.funds_choice_wrap').on('mouseleave', function() {
					$('#funds_per_content').children().hide();
					$('.fund_xiala_active').removeClass('fund_xiala_active');
				});
				// loadFundList(this);
			});
			
			$(".funds_title_selection").on("click",function(){
				$(".selection_criteria li").remove();

				$(".fund_logo_active").removeClass("fund_logo_active");

				$(".fund_choice_active").removeClass("fund_choice_active");
				$(".funds_all").click();
				$('#funds_logo_choice li').eq(0).addClass('fund_choice_active2').siblings().removeClass('fund_choice_active2');
				$('#funds_logo li').removeClass('fund_logo_active1');
				$("#funds_per_content").children().hide();
				$('#funds_per_choice li').removeClass('fund_xiala_active');
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
													////console.log(dataName)
				if(!sizeFrom)sizeFrom="";
				if(!sizeTo)sizeTo="";
				if(!dataValue)dataValue="";
				if("Fundsize"==dataName){
					document.getElementById("fundSizeFrom").value=sizeFrom
					document.getElementById("fundSizeTo").value=sizeTo
				}
				
				if("Sincelaunch"==dataName){
					document.getElementById("perfLaunchFrom").value=sizeFrom
					document.getElementById("perfLaunchTo").value=sizeTo
				}
				if("YTD"==dataName){
					document.getElementById("perfYTDFrom").value=sizeFrom
					document.getElementById("perfYTDTo").value=sizeTo
				}
				if("1Week"==dataName){
					document.getElementById("perfOneWeekFrom").value=sizeFrom
					document.getElementById("perfOneWeekTo").value=sizeTo
				}
				if("1Mon"==dataName){
					document.getElementById("perfOneMonthFrom").value=sizeFrom
					document.getElementById("perfOneMonthTo").value=sizeTo
				}
				if("3Mon"==dataName){
					document.getElementById("perfThreeMonthFrom").value=sizeFrom
					document.getElementById("perfThreeMonthTo").value=sizeTo
				}
				if("6Mon"==dataName){
					document.getElementById("perfSixMonthFrom").value=sizeFrom
					document.getElementById("perfSixMonthTo").value=sizeTo
				}
				if("1Yr"==dataName){
					document.getElementById("perfOneYearFrom").value=sizeFrom
					document.getElementById("perfOneYearTo").value=sizeTo
				}
				if("3Yr"==dataName){
					document.getElementById("perfThreeYearFrom").value=sizeFrom
					document.getElementById("perfThreeYearTo").value=sizeTo
				}
				if("5Yr"==dataName){
					document.getElementById("perfFiveYearFrom").value=sizeFrom
					document.getElementById("perfFiveYearTo").value=sizeTo
				}
				if("LipperCr"==dataName){
					document.getElementById("lipperCR").value=dataValue
				}
				if("Fitch"==dataName){
					document.getElementById("fitch").value=dataValue
				}
				if("Citywire"==dataName){
					document.getElementById("citywire").value=dataValue
				}
				if("MinInitialInv"==dataName){
					document.getElementById("minInitialInv").value=dataValue
				}
				if("fundDistributor"==dataName){ 
					document.getElementById("distributorId").value=dataValue
				}
				if("MgtFee"==dataName){
					document.getElementById("mgtFee").value=dataValue
				}
				if("FundHouse"==dataName){
					var houseId = "";
					//var houseLength = $(".fund_logo_active").length-1;
					$(".fund_logo_active").each(function(index,element){
						//var houseIndex = index;
						houseId += $(this).attr("data-value");
						//if(houseLength != houseIndex && houseLength != 0){
							houseId += ","
						//}
					})
					document.getElementById("fundHouseIds").value=houseId
				}
			 	Searchdata = $("#listForm").serialize();
			 	Initialization();
			}
			
			
		     
    }]);

	$(".conversion_select li").on("click",function(){

		$('#hdSelectedCurrency').val($(this).attr("data-conversion"));
		new conversion($(this).attr("data-conversion"),$(this).html());
		$(this).parents(".funds_currency_xiala").find("input").attr("curCode",$(this).attr("data-conversion") );//用于点击加载更多时，获取转换货币编码
		$(this).parents(".funds_currency_xiala").find("input").val( $(this).html() );
	});
	
	function exchange(){
		//var selectedCurrency = $('#hdSelectedCurrency').val(); //scshi 170419
		var selectedCurCode = $('#hdSelectedCurrency').attr("curcode");
		//if(selectedCurrency != ''){
			$('.conversion_select li').each(function(){
				var currency = $(this).attr('data-conversion');
				//if(selectedCurrency == currency){//scshi 170419 判断错误
				if(selectedCurCode == currency){
					$(this).click();
					$('.funds_currency_xiala').find("ul").removeClass("funds_currency_active").hide();
					return false;
				}
			});
		//}
		
	}
	
	$(".funds_currency_xiala").on("click",function(){
		if( $(this).find("ul").hasClass("funds_currency_active") ){
			$(this).find("ul").removeClass("funds_currency_active").hide();
		}else{
			$(this).find("ul").addClass("funds_currency_active").show();

		}
	});
	$(".funds_currency_xiala").on("mouseleave",function(){
		$(this).find("ul").removeClass("funds_currency_active").hide();
	});
	
	$(".funds_view_switch_ico").on("click",function(){
		$(".funds-views-lump").show();
		$(".funds_switch_tab").show();
		$(".funds_viewList_tab").hide();
		$(".funds-views-list").hide();
	});
	$(".funds_view_list_ico").on("click",function(){
		$(".funds-views-lump").hide();
		$(".funds_switch_tab").hide();
		$(".funds_viewList_tab").show();
		$(".funds-views-list").show();
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
	// ******************收藏***************************************************************/
	$(".white_background").on("click",".funds_heart",function(event){
		event.stopPropagation();
//		var loginVal = $("#loginCode").val(),
			_this = $(this),
			status  = _this.attr("followFlag"),
			fundId = _this.attr("fundId"),
			productId = _this.attr("productId");
		if(status!="Delete"){
			$('.wmes-myfavourites-groupcon').attr("productId",productId);
			$('.wmes-myfavourites-groupcon').attr("fundId",fundId);
            $('.wmes-myfavourites-groupcon').css('display','block');
          }else{
              saveCollection(status,productId,fundId,'','');
          }
		/*if(loginVal == 'true'){
	    	$.ajax({
	    		url:base_root+"/front/fund/info/setFoundCollection.do?r="+Math.random(),
	    		data:{'opType':status,'fundId':fundId,'productId':productId},
	    		dataType:"json",
	    		type:"get",
	    		success:function(data){
	    			if(data.result){
	    				if(status == 'Delete'){
	    					$(".funds_heart" + fundId).removeClass("funds_heart_active");
	    					$(".funds_heart" + fundId).attr("followFlag",'Follow');
	    					layer.msg(langMutilForJs['favour.remove']);
	    				}else{
							$(".funds_heart" + fundId).addClass("funds_heart_active");
	    					$(".funds_heart" + fundId).attr("followFlag",'Delete');
	    					layer.msg(langMutilForJs['favour.add']);
	    				}
	    			}
	    		},
	    		error:function(){
	    			//alert("error!");
	    			layer.msg('error');
	    		}
	    	})
		}else{
			window.parent.location.href=base_root+"/front/viewLogin.do?r="+Math.random();
			return;
		}*/
	})
	
	getTypeList();
    function getTypeList(){
      $.ajax({
				type:'post',
				datatype:'json',
				url:base_root+'/front/web/watchlist/getWatchTypeList.do',
				data:{},
				success:function(json){
					var html="";
					html+='<div class="wmes-wrap-title favourite-group" >'+langMutilForJs['fund.favourite.type.tilte']+'</div>';
			        html+='<ul style="height:200px;">';
					if(json!=undefined && json.length>0){
						$.each(json,function(i,n){
							html+='<li>'
							+'<input type="radio" id="'+n.id+'" name="fenzu" /><input type="text" value="'+n.name+'"  /><span></span><b class="del-type"></b>'
							+'</li>'
						})
					}else{
						html+='<li>'
							+'<input type="radio" name="fenzu" checked/><input type="text" value="'+langMutilForJs['fund.favourite.type.new']+'"  /><span></span><b class="del-type"></b>'
							+'</li>'
					}
					 html+='</ul >'
						 +'<div class="wmes-myfavourites-operate">'
						 +'<b class="add-type">+</b>'
						 +'<a href="javascript:void(0);" class="save-collection">'+langMutilForJs['global.submit']+'</a>'
						 +'<a href="javascript:void(0);" class="cancel-fav">'+langMutilForJs['global.cancel']+'</a>'
						 +'</div>'
						 +'<div class="wmes-close"></div>';
					 
					 $(".wmes-myfavourites-group").append(html);
				}
			})
    }
    
   $('.wmes-myfavourites-groupcon').on("click",".save-collection",function(){
       var ele= $('.wmes-myfavourites-groupcon').find("input[type='radio']:checked");
       if(ele.length==0){
         layer.msg(langMutilForJs['fund.favourite.type.tips']);
         return;
       }
       
       var typeId=ele.attr("id");
       var typeName=ele.next().val();
       if(typeId==undefined){
          typeId="";
       }
     var productId=$('.wmes-myfavourites-groupcon').attr("productId");
     var fundId= $('.wmes-myfavourites-groupcon').attr("fundId");
       saveCollection("Follow",productId,fundId,typeId,typeName)
       /* //console.log(ele);
        //console.log("id",id);
        //console.log("name",name);*/
   })
	
   $(".wmes-myfavourites-group").on('click','.cancel-fav',function(){
	   $(".wmes-myfavourites-groupcon").hide();
   })
   function saveCollection(status,productId,fundId,typeId,typeName){
       //console.log("typeName",typeName);
        var loginVal = $("#loginCode").val();
        if(loginVal && loginVal.length>1){
            $.ajax({
                url:base_root+"/front/fund/info/setFoundCollection.do?r="+Math.random(),
                //data:{opType:status,fundId:fundId},
                data:{opType:status,productId:productId,typeId:typeId,typeName:encodeURI(typeName)},
                dataType:"json",
                type:"get",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8", 
                success:function(data){
                    if(data.result){
                        if(status == 'Delete'){
                            $(".funds_heart" + fundId).removeClass("funds_heart_active");
                            $(".funds_heart" + fundId).attr("followFlag",'Follow');
                            layer.msg(langMutilForJs['favour.remove']);
                        }else{
                            $(".funds_heart" + fundId).addClass("funds_heart_active");
                            $(".funds_heart" + fundId).attr("followFlag",'Delete');
                            layer.msg(langMutilForJs['favour.add']);
                        }
                    }
                    var type=data.type;
                    if(type!=undefined){
                    	$('.wmes-myfavourites-groupcon').find("input[type=radio]:checked").attr("id",type.id);
                    }
                    $('.wmes-myfavourites-groupcon').css('display','none');
                },
                error:function(){
                    //alert("error!");
                    layer.alert("[@lang_res k='error.errorMessage'/]");
                }
            })
        }else{
            //alert('Please login first.');
            layer.alert("[@lang_res k='error.noLogin'/]");
            //window.parent.location.href=base_root+"/front/viewLogin.do?r="+Math.random();
            return;
        }  
    }
   $('.wmes-myfavourites-groupcon').on("click",".add-type",function(){
	 
	   var html='<li>'
			+'<input type="radio" name="fenzu" ><input type="text" value="'+langMutilForJs['fund.favourite.type.new']+'"  /><span></span><b class="del-type"></b>'
			+'</li>'
		$(".wmes-myfavourites-group ul").append(html);	
   })
   
   $('.wmes-myfavourites-groupcon').on("click",".del-type",function(){
	   var id=$(this).parent().find("input[type=radio]").attr("id");
	   var _this=this;
	   if(id!=undefined && id!=""){
		   layer.confirm(langMutilForJs['fund.favourite.type.deleteTips'], {
			   btn: [langMutilForJs['global.true'],langMutilForJs['global.false']] //按钮
			 }, function(){
				 $.ajax({
					   type:'post',
					   datatype:'json',
					   url:base_root+'/front/web/watchlist/delWatchType.do',
					   data:{typeId:id},
					   success:function(json){
						   if(json.result){
							   
							   $(_this).parent().remove();
							  
						   }
					   }
				   })
			 })
		   
	   }else{
		   $(_this).parent().remove();
	   }
   })
		   
// ******************收藏***************************************************************/
	
	//加入购物车
    $(".addCart").on("click",function(){
        if($(this).hasClass('funds_cart2_active')){
            $(this).removeClass("funds_cart2_active");
        }else{
            $(this).addClass("funds_cart2_active");
        }
    });
	
	//当基金列表作为选择器时?select=true，读取checkbox选定的基金，并返回基金id到父窗口
	//checkbox.name=funds_comparison，父窗口基金编码对象=ids
	$("#cart").on("click",function(){
        var id = "";
        $("input:checkbox[name='funds_comparison']:checked").each(function() {
            if (id.length>0) id += ",";
                id += $(this).attr("value");
        });

        if (!id){
            //alert("Please select a fund.");
            layer.alert(langMutilForJs['fund.selector.pleaseSelect']);
            return;
        }
        if (!window.opener){
            //alert("Getting opener unsuccessfully.\nThe result can not be return:\n"+id);
            layer.alert(langMutilForJs['fund.selector.openerError']+id);
            return;
        }
        
        //获取父窗口的id对象，添加新id值
        var parent_parm_ids = window.opener.document.getElementById("ids");
        if (!parent_parm_ids){
            //alert("Getting opener object unsuccessfully.\nThe result can not be return:\n"+id);
            layer.alert(langMutilForJs['fund.selector.openerObjError']+id);
            return;
        }
        var _ids_ = "";
        try{
            _ids_ = parent_parm_ids.value;
        }catch(e){
            _ids_ = "";
        }
        if (_ids_.length>0)
            _ids_ += ","+id;
        else _ids_ = id;
        //alert(_ids_);
        parent_parm_ids.value=_ids_;
        
        //alert(parent_parm_ids.value);
        //调用父窗口的基金列表刷新方法
        window.opener.document.getElementById("popupWinReturn").click();
        window.close();
    });
	
	$(".funds-views-list").on("click",".funds_comparison_check",function(){
		if( $(".funds_comparison_check:checked").length > 5){
			layer.msg(langMutilForJs['fund.info.list.comparisonNum']);
			//$(this).removeAttr("checked");
		}
			
			if($(this).is(':checked')){
				$("input[name='funds_comparison'][value='"+$(this).val()+"']").attr("checked",true);
				$("div>input[name='funds_comparison'][value='"+$(this).val()+"']").parents("li").addClass("funds_lump_active");
			}else{
				$("input[name='funds_comparison'][value='"+$(this).val()+"']").attr("checked",false);
				$("div>input[name='funds_comparison'][value='"+$(this).val()+"']").parents("li").removeClass("funds_lump_active");
			}
			
			var Strhref = "";
			var i=0;
			$(".funds_comparison_check:checked").each(function(){
				Strhref += $(this).val() + ',';
				i++;
				if(i>=5) return false;
			});
			Strhref = base_root + "/front/fund/info/fundscomparison.do?id=" + Strhref.substring(0,Strhref.length-1);
			$("#comparison-num").html(  $(".funds_comparison_check:checked").length);
			$(".fund_float_menu_num").html($(".funds_comparison_check:checked").length);
			$("#comparison").attr("href",Strhref);
			$("#jump_compare").attr("href",Strhref);
		
		
	});
	
	$(".wmes-close,.fund_float_attributes-buttoncancel").on("click",function(){
		$(".fund-space-mask-wrap").removeClass("fund-space-mask-wrapac");
	});
	$('.fund_float_menucon_purchasebox').on('click',function(){
		var purchaseboxtop = $(window).scrollTop();
		$('.fund_float_menu_menu_addbox').css('top','calc('+purchaseboxtop+'px + 10%)');
		$('.fund-space-mask-wrap').addClass('fund-space-mask-wrapac');
	});
	
	$(".views-lump-ul").on("click",".lump_comparison_check",function(){

		if( $(".lump_comparison_check:checked").length > 5){
			layer.msg(langMutilForJs['fund.info.list.comparisonNum']);
			//$(this).removeAttr("checked");
			return false;
		}

			if($(this).is(':checked')){
				$(this).parents("li").addClass("funds_lump_active");
				$("input[name='funds_comparison'][value='"+$(this).val()+"']").attr("checked",true);
			}else{
				$(this).parents("li").removeClass("funds_lump_active");
				$("input[name='funds_comparison'][value='"+$(this).val()+"']").attr("checked",false);
			}
			var Strhref = "";
			var i=0;
			$(".lump_comparison_check:checked").each(function(){				
				Strhref += $(this).val() + ',';
				i++;
				if(i>=5) return false;
			});
			Strhref = base_root + "/front/fund/info/fundscomparison.do?id=" + Strhref.substring(0,Strhref.length-1);
			$("#comparison-num").html(  $(".lump_comparison_check:checked").length);
			$(".fund_float_menu_num").html($(".lump_comparison_check:checked").length);
			$("#comparison").attr("href",Strhref);
			$("#jump_compare").attr("href",Strhref);
	});
	
	
	$(document).on('click','.views-lump-ul li',function(){
		$(this).find('.lump_comparison_check').click();
	});
	
	$(document).on('click','.views-lump-ul li a',function(e){
		e.stopPropagation();
	});
	
	var fundval = sessionStorage.getItem("fundlist");
	if(fundval=="show"){
		$(".client-more-screen-wrap").css({'height':'100%','margin-top':'20px'});
		$(".wmes-menu-hide").toggleClass("wmes-menu-hideac");
		$('.funds_selected').removeClass('ifa-more-ico-hidden');
	}else{
		$(".client-more-screen-wrap").css({'height':'0','margin-top':'0px'});
		$('.funds_selected').addClass('ifa-more-ico-hidden');
	};
	
	$(".wmes-menu-hide").on("click",function(){
		$(this).toggleClass("wmes-menu-hideac");
		if( $(this).hasClass("wmes-menu-hideac")) {
			$(".client-more-screen-wrap").stop().animate({ 
				height: "100%"
			}, 300 );
			$(".client-more-screen-wrap").css({'margin-top':'20px'});
			$('.funds_selected').removeClass('ifa-more-ico-hidden');
			sessionStorage.setItem("fundlist", "show");
		}else{
			$(".client-more-screen-wrap").stop().animate({ 
				height: "0px",margin:'0px'
			}, 300 );
			$('.funds_selected').addClass('ifa-more-ico-hidden');
			sessionStorage.setItem("fundlist", "hide");
		}
	});
	
	$('.fund_float_menu').on('mouseenter',function(){
		
		var individualId = $('#individualId').val();
		if(individualId.length == 0){
			//判断是否为投资者，不是则隐藏购买图标
			$('.fund_float_menucon_purchasebox').css('display','none');
		}
		
		$('.fund_float_menubox').addClass('fund_float_menuboxac');
		if($(this).hasClass('fund_float_menuac')){
			$('.fund_float_menucon').css('display','block');			
		}else{
			$('.fund_float_menucon').css('display','none');			
		};
	});
	
	$('.fund_float_menubox').on('mouseleave',function(){
		$('.fund_float_menubox').removeClass('fund_float_menuboxac');
	});
	
	var fund_float_menuboxheight = $(window).height()-180;
	$('.fund_float_menubox').css('top',fund_float_menuboxheight+'px');
	$('.fund_float_menubox').show();
	
	$(document).on("click",".register_xiala_long input",function(){
		$(this).siblings(".regiter_xiala_ul").show();
	});
	$(document).on('click','.register_xiala_ico',function(){
		if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none'){
			$(this).siblings(".regiter_xiala_ul").show();
		}else{
			$(this).siblings(".regiter_xiala_ul").hide();
		};
	});
	$(document).on('mouseleave','.register_xiala_long',function(){
		$(this).find('.regiter_xiala_ul').hide();
	});
	$(".regiter_xiala_ul").on("click","li",function(){
		$(this).parent().siblings('.value_hidden').val( $(this).attr("value") );
		$(this).parent().siblings('.value_show').val( $(this).html() );
		$(".regiter_xiala_ul").hide();
	});
	
	$(".fund_float_menu_menu_addbox").on("click",".fund_float_attributes-button",function(){

//		if($(".funds-views-lump").css("display") == "none"){
//			layer.msg("list");
//		}else{
//			layer.msg("lump");
//		}
			
		if( $(".lump_comparison_check:checked").length > 0){
//			layer.msg("selected:"+$(".lump_comparison_check:checked").length);
			var holdId = $(".value_hidden").val();
			var Strhref = "";
			var productIds = "";
			$(".lump_comparison_check:checked").each(function(){
				Strhref += $(this).val() + ',';
				//productIds += $(this).attr("productId") + ',';
				productIds += $(this).val() + ',';
			});
			productIds = productIds.substring(0,productIds.length-1);
			if(holdId.length==0 || productIds.length==0){
				layer.msg(langMutilForJs['fund.info.list.purchas.noPortfolio']);
			}else{
				$("#products").val(productIds);
				if(holdId == "newPortfolio") holdId="";
				$("#holdId").val(holdId);
				
//				layer.msg("productIds:"+productIds);
//				layer.msg("holdId:"+holdId);
				document.getElementById("addFundToPortfolio").submit();
			}

		}else{
			
		}

	});

});