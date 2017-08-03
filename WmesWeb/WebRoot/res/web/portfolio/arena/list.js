/**
 * list.js 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	var $ = require('jquery');
	require('layer');
	require("scrollbar");
	require("ionrangeSlider");
	var Loading = require('loading');
	var angular = require('angular');

	var mybase = angular.module('ifaTable', ['truncate']);
	var searchData = null;
	$('.funds_search_wrap').perfectScrollbar();
	var oLoading = new Loading($(".portfolio_list"));	
	mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1; //第N页数据
		is_finish = true, //当前数据是否加载完成
			page_bol = true; //总页数控制
		//初始化数据
		$scope.dataList = '';
		// 数字调用
		$scope.counter = Array;
		$scope.defColor = $("#defColor").val();
		$scope.dateFormat=$("#dateFormat").val();
		$scope.dateTimeFormat=$("#dateFormat").val()+" HH:mm:ss";
		$scope.baseroot=base_root;
		//固定每次拿9条数据
		var rows = 9;
		// 正常拿数据	 
		function getDataList() {
			oLoading.show();
			var data = "";
			var element = null;
			if($(".funds_ifalist_sorting").find(".funds_down_active").length > 0) {
				var element = $(".funds_ifalist_sorting").find(".funds_down_active");

			} else if($(".funds_ifalist_sorting").find(".funds_top_active").length > 0) {
				var element = $(".funds_ifalist_sorting").find(".funds_top_active");
			}
			if(null != element && element != undefined) {
				var sort = $(element).attr("sort");
				var order = $(element).attr("order");
				data +="sort="+sort+"&order=" + order + "&";
			}
			var keyWord=$("#searchKeyWord").val();
			
			is_finish = false;
			data+="keyWord="+keyWord+"&";
			var url = base_root + "/front/portfolio/arena/listJson.do";
			data += "rows=" + rows + "&page=" + iPAGE + "&" + searchData + "&checkIsMyFollow=1";
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) {
					oLoading.hide();
					if(response.list.length > 0) {
						$(".clickmore").show();
						$(".dataListTr").show()
						$(".no_list_tips").hide();
						// 总页数
						var sumPage = Math.ceil(response.total / rows);
						if(iPAGE > sumPage) {
							return;
						}
						if(iPAGE === 1) {
							$scope.dataList = '';
							$scope.dataList = response.list;
						} else {
							$scope.dataList = $scope.dataList.concat(response.list);
						}

						if(iPAGE >= sumPage) {
							page_bol = false;
							$(".clickmore").hide();
						}
					} else {
						if(iPAGE === 1) {
							$scope.dataList = '';
							$(".dataListTr").hide()
							$(".no_list_tips").show();
						}
					}
					iPAGE++;
					$scope.datatotal = response.total;

					is_finish = true;
				});
		}
		getDataList();
		// 滚动条件
		//var scrollBol = false;

		// 滚动拿数据
		//  	setTimeout(function(){
		//  		$(window).on('scroll', GetScroll);
		//  	},1000);	
		$('.clickmore').on('click', function() {
			$(this).hide();
			getDataList();
		});

		/*function GetScroll(){
    		var docheight = $(window).scrollTop() + $(window).height(),
    			listheight = $('.strategies_list_wrap ').offset().top + $('.strategies_list_wrap ').height() + $(".wmes_top").height();
    		
    		if( docheight > listheight ){
    			scrollBol = true;
    			
    		}else{
    			scrollBol = false;
    		}

			if (scrollBol && is_finish && page_bol ){
				getDataList();
			}

    	}*/
		//收藏
		$(".portfolio_list").on("click", ".portfolio_fav_img_amend", function() {
			var _this = $(this);
			var relateid = _this.attr("relateid");
			var flag = _this.attr("optype");
			var opType = "";
			if(flag == "0") {
				opType = "Follow";
			} else {
				opType = "Delete";
			}
			$.ajax({
				url: base_root + "/front/portfolio/arena/setPortfolioFollow.do",
				data: {
					"relateid": relateid,
					"opType": opType
				},
				type: "get",
				dataType: "json",
				success: function(data) {
					if(data.result) {
						if(flag == "0") {
							layer.msg(langMutilForJs['favour.add']);
							_this.toggleClass('portfolio_fav_img_amend1active');
							//_this.attr("src",base_root+"/res/images/icon-herat-2.png");
							_this.attr("optype", "1");
						} else {
							layer.msg(langMutilForJs['favour.remove']);
							_this.toggleClass('portfolio_fav_img_amend1active');
							//_this.attr("src",base_root+"/res/images/fund/strategies_fav.png");
							_this.attr("optype", "0");
						}
					} else {
						layer.msg(data.msg);
					}
				}
			});
			return false;
		});
		$(document).on('mouseenter', ".portfolio_fav_img_amend", function(event) {
			$(this).addClass('portfolio_fav_img_amendhover');
			if($(this).hasClass('portfolio_fav_img_amend1active')){
				$(this).attr('title','点击取消收藏');
			}else{
				$(this).attr('title','点击收藏');
			}
		});
		$(document).on('mouseleave', ".portfolio_fav_img_amend", function(event) {
			$(this).removeClass('portfolio_fav_img_amendhover');
		});
		
		var listTime;
		$(".funds_choice li").on("click", function() {
			if($(this).hasClass('funds_all')) {
				$(this).addClass('fund_choice_active2');
			} else {
				$(this).closest('.funds_choice').find('.funds_all').removeClass('fund_choice_active2');
			};
			clearTimeout(listTime);
			if($(this).parent().hasClass("funds_logo_b")) {
				return;
			};
			var selection_criterialenght = $(".selection_criteria li").length;
			for(var i = 0; i < selection_criterialenght; i++) {
				if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name")) {
					$(".selection_criteria li").eq(i).remove();
				}
			}
			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
			if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
				
				//$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				
				if($(this).attr("data-value")=="pref"){
					$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '" data_from="' + $(this).attr("data_from") + '"data_to="' + $(this).attr("data_to") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				}else{
					$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				}
					
				
			}

			// 解决重复请求的问题
			var self = this;
			listTime = setTimeout(function() {
				Initialization();
			}, 100);
		});

		// 选定字母下的二级选项，允许多选
		$(".select_choice li").on("click", function() {
			if($(this).hasClass("fund_choice_active")) {
				var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght; i++) {
					if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value")) {
						$(".selection_criteria li").eq(i).remove();
					}
				}
				$(this).removeClass("fund_choice_active");
			} else {
				if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
					$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				}
				$(this).addClass("fund_choice_active");
			}
			$("#select_choice .funds_all").addClass("funds_aloac");
			Initialization();

		});

		//执行清除方案点击操作
		$(".funds_title_selection").on("click", function() {
			$(".selection_criteria li").remove();

			$(".fund_logo_active").removeClass("fund_logo_active");

			$(".fund_choice_active").removeClass("fund_choice_active");
			$(".funds_all").click();
			$('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active2');
			$('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active');
			$('#funds_logo li').removeClass('fund_logo_active1');
			$("#funds_logo").children().hide();
			$("#listForm").find("input").val("");
			Searchdata = $("#listForm").serialize();
			Initialization();
		});

		/**
		 * 搜索条件取消点击
		 */
		$(".selection_criteria").on("click", ".selection_delete", function() {
			$(this).parent().remove();
			var funds_all_lenght = $('.funds_all').length;
			for(var i = 0; i < funds_all_lenght; i++) {
				if($(this).parent().attr("data-name") == "regions") {
					var fundslenght = $("#funds_logo li").length;
					for(var funds = 0; funds < fundslenght; funds++) {
						if($(this).parent().attr("data-value") == $("#funds_logo li").eq(funds).attr("data-value")) {
							$("#funds_logo li").eq(funds).click();
						}
					};
					if($(this).parent().attr("data-name").indexOf("regions") == 0) {
						var count1 = 0;
						$(".selection_criteria li").each(function() {
							if($(this).attr("data-name").indexOf("regions") == 0) {
								count1++;
							}
						})
						if(count1 == 0) {
							$('.funds_all').eq(i).click();
						}
					};
					break;
				} else if($(this).parent().attr("data-name").indexOf("sector") == 0) {
					var count = 0;
					$(".selection_criteria li").each(function() {
						if($(this).attr("data-name").indexOf("sector") == 0) {
							count++;
						}
					})
					if(count == 0) {
						$('.funds_all').eq(1).click();
					}
				} else if($(this).parent().attr("data-name").indexOf("riskLevel") == 0) {
					$('.funds_all').eq(2).click();
				} else if($(this).parent().attr("data-value").indexOf("pref") == 0) {
					$('.funds_all').eq(3).click();
				}
			}

			var prefCount = 0;
			$(".selection_criteria").find("li").each(function() {
				$(this).attr("data-value") == "pref";
				prefCount++;
			})
			if(prefCount == 0) $("#per_all").addClass("per_active");
			Initialization();

			var dataValue = $(this).parent().attr("data-value");
			$('.funds_choice_sector li[data-value="' + dataValue + '"]').removeClass('fund_choice_active');
			$('.funds_choice_sector li[data-value="' + dataValue + '"]').css('color', '#000');
			if($('#funds_logo_choice li').eq(0).hasClass('fund_choice_active2')) {
				$('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active2');
				$('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active');
				$("#funds_logo").children().hide();
			};
		});

		$('#per_all').on('click', function() {
			$(this).addClass('per_active');
			$('#funds_per_content li').removeClass('fund_choice_active');
			var selection_criterialenght1 = $(".selection_criteria li").length;
			for(var i = 0; i < selection_criterialenght1; i++) {
				if($(".selection_criteria li").eq(i).attr("data-value") == 'pref') {
					$(".selection_criteria li").eq(i).addClass("thisremove");
				}
			}
			$(".thisremove").remove();
			selection();
		});
		/**
		 * 显示清除所有搜索条件按钮
		 * */
		function selection() {
			var _thisLenght = $(".selection_criteria").children().length

			if(_thisLenght != 1) {
				$(".funds_title_selection").css('display', 'inline-block');
			} else {
				$(".funds_title_selection").css('display', 'none');
			}
		};

		$("#funds_logo_choice li").on("mousemove", function() {

			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
			$(this).siblings().removeClass("fund_choice_active2").end().addClass("fund_choice_active2");

			var this_letter = $(this).attr("data-letter"),
				funds_logo = $("#funds_logo").children(),
				funds_logo_lenght = funds_logo.length;
			for(var k = 0; k < funds_logo_lenght; k++) {
				if(this_letter.indexOf(funds_logo.eq(k).attr("data-letter")) >= 0) {
					funds_logo.eq(k).show();
				} else {
					funds_logo.eq(k).hide();
				}
			}
			if($(this).hasClass("funds_all") && $(this).hasClass("funds_aloac")) {
				$(".funds_logo_choice li").removeClass("fund_logo_active");
				$(this).removeClass("funds_aloac");
				var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght; i++) {
					if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name")) {
						$(".selection_criteria li").eq(i).addClass("thisremove");
					}
				}
				$(".thisremove").remove();
				loadFundList(this);
			}
		});

		
		$(".funds_all_Sector").on("click", function() {
			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
			for(i = 1; i < $(this).closest('ul').find('li').length-1; i++) {
				$(".selection_criteria li[data-name='Sector" + i + "']").remove();
			}
		});
		
		/**
		 * 实例化操作
		 * 
		 * */
		function Initialization() {
			// 初始化数值
			iPAGE = 1; //第N页数据
			page_bol = true;
			$scope.dataList = '';
			$(".no_list_tips").hide();
			$(".clickmore").hide();
			selection();
			searchList();
			searchData = $("#paramsForm").serialize();
			getDataList();
		}
		
		$("#searchKeyBtn").on("click",function(){
			Initialization();
		})

		function searchList() {
			/*var dataName = $(searchLi).attr("data-name");
			var dataValue = $(searchLi).attr("data-value");*/

			var regions = "";
			var sectors = "";
			var riskLevel = "";
			var period = "";
			var dataFrom = "";
			var dataTo = "";
			var pref=[];
			
			$(".selection_criteria li").each(function() {
				
				
				var dataName = $(this).attr("data-name");
				var dataValue = $(this).attr("data-value");
				if(!dataValue) dataValue = "";
				if("regions" == dataName) {
					regions = dataValue;
				} else if("riskLevel" == dataName) {
					riskLevel = dataValue;
				} else if(dataName.indexOf("sector") == 0) {
					sectors += "," + dataValue;
				} else if("pref" == dataValue) {
					
					/*period = dataName;
					dataFrom = $(this).attr("data_from");
					dataTo = $(this).attr("data_to");*/
					var prefData={};
					
					prefData.period = dataName;
					prefData.dataFrom = $(this).attr("data_from");
					prefData.dataTo = $(this).attr("data_to");
					pref.push(prefData);
				}
			});
			$("#regions").val(regions);
			$("#riskLevel").val(riskLevel);
			$("#sectors").val(sectors);
			$("#pref").val(JSON.stringify(pref));
			
			searchData = $("#paramsForm").serialize();
			//Initialization();
		}
		
		$(".funds_ifalist_sorting li").on("click",function(){
	    	$(".funds_ifalist_sorting li").removeClass("funds_active");
	    	$(this).addClass("funds_active");
	    	if($(this).find(".funds_arrow_top").hasClass("funds_top_active") ){
	    		$('.funds_arrow_down').removeClass("funds_down_active");
	    		$('.funds_arrow_top').removeClass("funds_top_active");
	    		$(this).find(".funds_arrow_down").addClass("funds_down_active");
	    		Initialization();
	    	}else{
	    		$('.funds_arrow_down').removeClass("funds_down_active");
	    		$('.funds_arrow_top').removeClass("funds_top_active");
	    		$(this).find(".funds_arrow_top").addClass("funds_top_active");
	    		Initialization();
	    	}
	    });
//		$('.funds_arrow_down').on("click", function() {
//			$('.funds_arrow_down').removeClass("funds_down_active");
//			$('.funds_arrow_top').removeClass("funds_top_active");
//			$(this).parents("li").siblings().removeClass("funds_active").end().addClass("funds_active");
//			$(this).addClass("funds_down_active");
//			
//		});
//		$('.funds_arrow_top').on("click", function() {
//			$('.funds_arrow_down').removeClass("funds_down_active");
//			$('.funds_arrow_top').removeClass("funds_top_active");
//			$(this).parents("li").siblings().removeClass("funds_active").end().addClass("funds_active");
//			$(this).addClass("funds_top_active");
//			Initialization();
//		});
		
		/*	function singleSlider() {
		        $("#range_1").ionRangeSlider({
					min: -4,
					max: 1,
					from:-4,
					to: 1,
					type: 'double',// 设置类型
					step: 1,
					prefix: "min",// 设置数值前缀
					prefix1: "max",
					postfix: "%",// 设置数值后缀
					prettify: true,
					hasGrid: true
					});
		    }
		     singleSlider();*/

		$('.funds_choice_wrap_hiddenclick').on('click', function() {
			var choiceHeight = $('.funds_choice_sector').css('height');
			$('.funds_choice_wrap_hiddenclick').toggleClass('funds_choice_wrap_showclick');
			if($(this).hasClass('funds_choice_wrap_showclick')) {
				$(this).parents('.funds_choice_wrap_hidden').removeAttr('max-height');
				$(this).parents('.funds_choice_wrap_hidden').stop().animate({
					'min-height': choiceHeight
				});
			} else {
				$(this).parents('.funds_choice_wrap_hidden').stop().animate({
					'min-height': '27px'
				});;
			}

		});

		$("#funds_per_content .fund_xiala_active").find("li").on("click", function() {
			var self = $(this);
			if($(this).hasClass("funds_all") == false) {
				$("#per_all").removeClass("per_active");
				// 修改：基金规模只能单选
//				$('#listForm').find(".perfClean").val("");
//				self.parents(".fund_xiala_active").siblings().find(".funds_all").click();

			}

		})

		$('#funds_per_choice li').on("mouseenter", function() {
			if($(this).hasClass("fund_xiala_active")) {
				$(this).removeClass("fund_xiala_active");
				$("#funds_per_content").children().hide().eq($(this).index()).hide();
			} else {
				$(this).siblings().removeClass("fund_xiala_active").end().addClass("fund_xiala_active");
				$("#funds_per_content").children().hide().eq($(this).index()).show();
			}
			// loadFundList(this);
			$('.funds_choice_wrap1').on('mouseleave', function() {
				$('#funds_per_content').children().hide();
				$('.fund_xiala_active').removeClass('fund_xiala_active');
			});

		});

		$('.funds_choice_sector li').on('click', function() {
			if($(this).attr('data-value') == 'fund_sector_00') {
				$('.funds_choice_sector li').css('color', '#000');
			} else {
				$(this).css('color', '#4ba6de');
			}
		});

		$(".funds_all_Sector").on("click", function() {
			var temp = $('.funds_choice_sector li').length;
			for(i = 0; i < temp; i++) {
				$(".selection_criteria li[data-name='sector" + i + "']").remove();
			}

		});
		
		var portfolioval = sessionStorage.getItem("portfoliolist");
		if(portfolioval=="show"){
			$(".client-more-screen-wrap").css({'height':'100%','margin-top':'5px'});
			$(".wmes-menu-hide").toggleClass("wmes-menu-hideac");
			$('.funds_list_selected').removeClass('ifa-more-ico-hidden');
		}else{
			$(".client-more-screen-wrap").css({'height':'0','margin-top':'0px'});
			$('.funds_list_selected').addClass('ifa-more-ico-hidden');
		};
		
		$(".wmes-menu-hide").on("click",function(){
				$(this).toggleClass("wmes-menu-hideac");
				if( $(this).hasClass("wmes-menu-hideac")) {
					$(".client-more-screen-wrap").stop().animate({ 
				    		height: "100%"
					}, 300 );
					$(".client-more-screen-wrap").css({'margin-top':'5px'});
					$('.funds_list_selected').removeClass('ifa-more-ico-hidden');
					sessionStorage.setItem("portfoliolist", "show");
				}else{
					$(".client-more-screen-wrap").stop().animate({ 
				    		height: "0px",margin:'0px'
					}, 300 );
					$('.funds_list_selected').addClass('ifa-more-ico-hidden');
					sessionStorage.setItem("portfoliolist", "hide");
				}
			});
		
		$("#searchKeyWord").keyup(function(event){
      		if(event.keyCode == 13){
	        	$('#searchKeyBtn').click()
	    	}
		});	
	
		$(".funds_logo_choice li").on("click", function() {
			if($(this).hasClass("fund_logo_active1")) {
				var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght; i++) {
					if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value")) {
						$(".selection_criteria li").eq(i).remove();
					}
				}
				$(this).removeClass("fund_logo_active1");
			} else {
				if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
					$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				}
				$(this).addClass("fund_logo_active1");
			}
			$("#funds_logo_choice .funds_all").addClass("funds_aloac");
			$("#funds_logo_choice .funds_all").removeClass("fund_choice_active2");
			$("#funds_logo_choice .funds_all").removeClass("fund_choice_active");

			Initialization();
		});

	}]); //angular.js end

})