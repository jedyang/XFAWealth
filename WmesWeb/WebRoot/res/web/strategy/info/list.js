/**
 * list.js 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	var $ = require('jquery');
	require('layer');
	require("echarts");
	//	var grid_selector = "#tableList";
	require("scrollbar");
	var Loading = require('loading');
	var angular = require('angular');
	var mybase = angular.module('ifaTable', ['truncate']);
	var searchData = null;
	$('.funds_search_wrap').perfectScrollbar();
	var oLoading = new Loading($(".strategies_list"));	
	mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1; //总页数控制
		//初始化数据
		$scope.dataList = '';
		// 数字调用
		$scope.counter = Array;
		$scope.baseroot=base_root;
		$scope.dateFormat=$("#dateFormat").val();
		$scope.dateTimeFormat=$("#dateFormat").val()+" HH:mm:ss";
		// 监听视图渲染是否结束
		$scope.checkLast = function($last) {
				if($last) {

					setTimeout(function() {
						initChar(); //初始化图表
					}, 100)

				}
			}
			//固定每次拿9条数据
		var rows = 9;
		// 正常拿数据	 
		function getDataList() {
			oLoading.show();
			var url = base_root + "/front/strategy/info/listJson.do",
				data = "rows=" + rows + "&page=" + iPAGE + "&" + searchData + "&checkIsMyFollow=1&datestr="+ new Date().getTime();
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
						
						// 总页数
						var sumPage = Math.ceil(response.total / rows);
						if(iPAGE > sumPage) {
							$(".clickmore").hide();
							return;
						}
						$(".clickmore").show();
						$(".dataListTr").show()
						$(".no_list_tips").hide();
						if(iPAGE === 1) {
							$scope.dataList = '';
							$scope.dataList = response.list;
						} else {
							$scope.dataList = $scope.dataList.concat(response.list);
						}
						if(iPAGE >= sumPage) {
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

				});
		}
		getDataList();

		$(".clickmore").on("click", function() {
			$(this).hide();
			getDataList();
		})

		function initChar() {
			$(".strategies_list_ptos").each(function(){
				var selfData = eval($(this).attr("data-value"));
				var isLoaded=$(this).attr("isLoaded");
				if(isLoaded==1){
					return ;
				}
				var opdata=[],selfcolor=[];
				for(var i = 0; i < selfData.length; i++) {
					/*if(i == 0) {
						opdata = [{value:selfData[0].value, name:langMutilForJs['allocation.'+selfData[0].name]+" "+selfData[0].value+"%"}];
					}else if(i == 1){
						opdata = [
				                {value:selfData[0].value, name:langMutilForJs['allocation.'+selfData[0].name]+" "+selfData[0].value+"%"},
				                {value:selfData[1].value, name:langMutilForJs['allocation.'+selfData[1].name]+" "+selfData[1].value+"%"}];
					}else if(i == 2){
						opdata = [
				                {value:selfData[0].value, name:langMutilForJs['allocation.'+selfData[0].name]+" "+selfData[0].value+"%"},
				                {value:selfData[1].value, name:langMutilForJs['allocation.'+selfData[1].name]+" "+selfData[1].value+"%"},
				                {value:selfData[2].value, name:langMutilForJs['allocation.'+selfData[2].name]+" "+selfData[2].value+"%"}
						];
					};
					if(selfData[0].name.indexOf('bond') == 0) {
						if(selfData[1].name.indexOf('fund') == 0){
							selfcolor = ["#9dd84e","#f6ac0a","#8c5ec2"];
						}else{
							selfcolor = ["#9dd84e","#8c5ec2","#f6ac0a"];
						}
					} else if(selfData[0].name.indexOf('fund') == 0) {
						if(selfData[1].name.indexOf('bond') == 0){
							selfcolor = ["#f6ac0a","#9dd84e","#8c5ec2"];
						}else{
							selfcolor = ["#f6ac0a","#8c5ec2","#9dd84e"];
						}
					} else {
						if(selfData[1].name.indexOf('fund') == 0){
							selfcolor = ["#8c5ec2","#f6ac0a","#9dd84e"];
						}else{
							selfcolor = ["#8c5ec2","#9dd84e","#f6ac0a"];
						}
					};*/
					
					var data={};
					data.value=selfData[i].value;
					data.name=langMutilForJs['allocation.'+selfData[i].name];
					opdata.push(data);
					selfcolor.push(selfData[i].displayColor);
					/*if(selfData[i].name.indexOf('bond')==0){
						selfcolor.push("#9dd84e");
					}else if(selfData[i].name.indexOf('fund')==0){
						selfcolor.push("#f6ac0a");
					}else if(selfData[i].name.indexOf('stock')==0){
						selfcolor.push("#8c5ec2");
					}*/
				};
				var option = {
					legend: {
        				orient: 'vertical',
        				left: '60%',
        				top:'15%',
        				data: opdata
    				},
    				clickable:false,
				    series: [
				        {
				            name:'account',
				            type:'pie',
				             center:	['38%', '50%'],
				            // radius : [0,"80%"],
				            color :selfcolor,
				            label: {
				                normal: {
				                    position: 'inner',
				                    formatter : "{d}%"
				                }
				            },
				            itemStyle: {
				            	normal:{
				            		label:{
				            			textStyle:{
				            				fontSize:10
				            			}
				            		}
				            	},
				            	emphasis:{
				            		show:false
				            	}
				            },
				       
				            data:opdata
				        }
				    ]
				};

				var myChart = echarts.init($(this)[0]);
	  			myChart.setOption(option);
	  			$(this).attr("isLoaded",1);
			});
		}

		//收藏
		$(".strategies_list").on("click", ".strategies_fav_img", function() {
			$(this).toggleClass('strategies_fav_imgac');
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
				url: base_root + "/front/strategy/info/setStrategyFollow.do",
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
							_this.attr("src", base_root + "/res/images/icon-herat-11.png");
							_this.attr("optype", "1");
						} else {
							layer.msg(langMutilForJs['favour.remove']);
							_this.attr("src", base_root + "/res/images/icon-herat-1.png");
							_this.attr("optype", "0");
						}
					} else {
						layer.msg(data.msg);
					}
				}
			});
			return false;
		});
		$(document).on('mouseenter', ".strategies_fav_img", function(event) {
			$(this).addClass('strategies_fav_imghover');
			if($(this).hasClass('strategies_fav_imgac')){
				$(this).attr('title',langMutilForJs['favour.click.remove']);
			}else{
				$(this).attr('title',langMutilForJs['favour.click.add']);
			}
		});
		$(document).on('mouseleave', ".strategies_fav_img", function(event) {
			$(this).removeClass('strategies_fav_imghover');
		});
		//按字母显示二级选择项
		$("#letter_choice li").on("mousemove", function() {
			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");

			var this_letter = $(this).attr("data-letter"),
				letter_search_choice = $("#letter_search_choice").children(),
				choice_lenght = letter_search_choice.length;
			for(var k = 0; k < choice_lenght; k++) {
				if(this_letter == letter_search_choice.eq(k).attr("data-letter")) {
					letter_search_choice.eq(k).show();
				} else {
					letter_search_choice.eq(k).hide();
				}
			}
			if($(this).hasClass("funds_all") && $(this).hasClass("funds_aloac")) {
				$(".select_choice li").removeClass("select_choice_active");
				$(this).removeClass("funds_aloac");
				var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght; i++) {
					if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name")) {
						$(".selection_criteria li").eq(i).addClass("thisremove");
					} else if($(".selection_criteria li").eq(i).attr("data-name")) {

					}
				}
				$(".thisremove").remove();
				// initialization();
			}
		});
		//选定字母下的二级选项，允许多选
		$(".select_choice li").on("click", function() {
			if($(this).hasClass("select_choice_active")) {
				var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght; i++) {
					if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value")) {
						$(".selection_criteria li").eq(i).remove();
					}
				}
				$(this).removeClass("select_choice_active");
			} else {
				if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
					$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				}
				$(this).addClass("select_choice_active");
			}
			$("#select_choice .funds_all").addClass("funds_aloac");

			// 解决重复请求的问题
			initialization();

		});
		//选定其他类型的条件
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
				$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
			}

			// 解决重复请求的问题
			var self = this;
			listTime = setTimeout(function() {
				initialization();
			}, 100);
		});

		//选定tab
		$(".ifa_search_tab li").on("click", function() {
			$(".ifa_search_tab li").removeClass("now")

			$(this).addClass("now");
			var value = $(this).attr("data-value");
			$("#status").val(value);
			//alert($("#status").val());

			initialization();
		});

		function selection() {
			var _thisLenght = $(".selection_criteria").children().length

			if(_thisLenght != 1) {
				$(".funds_title_selection").css('display', 'inline-block');
			} else {
				$(".funds_title_selection").css('display', 'none');
			}
		};

		$(".icon_search").on("click", function() {
			var fundName = $("#searchKeyWord").val();
			document.getElementById("keyWord").value = fundName;
			searchData = $("#paramsForm").serialize();
			initialization();
		})

		$("#searchKeyWord").keyup(function(event) {
			if(event.keyCode == 13) {
				document.getElementById('searchKeyBtn').click()
			}
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
			initialization();

			var dataValue = $(this).parent().attr("data-value");
			$('.funds_choice_amend li[data-value="' + dataValue + '"]').removeClass('fund_choice_active');
			$('.funds_choice_amend li[data-value="' + dataValue + '"]').css('color', '#000');
			if($('#funds_logo_choice li').eq(0).hasClass('fund_choice_active2')) {
				$('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active2');
				$('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active');
				$("#funds_logo").children().hide();
			};
		});

		function searchList() {
			var regions = "";
			var sectors = "";
			var riskLevel = "";
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
				}
			});
			$("#regions").val(regions);
			$("#riskLevel").val(riskLevel);
			$("#sectors").val(sectors);

		}

		function initialization() {
			// 初始化数值
			iPAGE = 1; //第N页数据
			selection();
			searchList();
			$scope.dataList = '';
			$(".no_list_tips").hide();
			$(".clickmore").hide();
			searchData = $("#paramsForm").serialize();
			getDataList();
		}

		/**
		 * 置顶
		 * */
		$(".ifa_list").on("click", ".up", function() {
			var itemId = $(this).attr("itemId");
			setOverhead(itemId, "1");
		});
		/**
		 * 取消置顶
		 * */
		$(".ifa_list").on("click", ".down", function() {
			var itemId = $(this).attr("itemId");
			setOverhead(itemId, "0");
		});

		function setOverhead(itemId, type) {
			if(!!itemId && !!type) {
				$.ajax({
					url: base_root + "/front/strategy/info/setOverhead.do",
					data: {
						"itemId": itemId,
						"type": type
					},
					type: "post",
					dataType: "json",
					success: function(data) {
						if(data.result) {
							$scope.dataList = '';
							initialization();
						}
					}
				})
			}
		}
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
				initialization();
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

			initialization();
		});

	}]); //angular.js end
	function substring(str, len) {
		var rst = str;
		if(str.length > len) {
			rst = str.substring(0, len) + '...';
		}
		return rst;
	}

	/**
	 *访客列表关闭
	 *@author scshi 
	 * */
	$(".space-mask-close").on("click", function() {
		$(".ifa-space-popup").hide();
	});

	$('.funds_choice_wrap_hiddenclick').on('click', function() {
		var choiceHeight = $('.funds_choice_amend').css('height');
		$('.funds_choice_wrap_hiddenclick').toggleClass('funds_choice_wrap_showclick');
		if($(this).hasClass('funds_choice_wrap_showclick')) {
			$(this).parents('.funds_choice_wrap_hidden').removeAttr('max-height');
			$(this).parents('.funds_choice_wrap_hidden').stop().animate({
				'min-height': choiceHeight
			});
		} else {
			$(this).parents('.funds_choice_wrap_hidden').stop().animate({
				'min-height': '34px'
			});
		}

	});

	$('.funds_choice_amend li').on('click', function() {
		if($(this).attr('data-value') == 'Sector_00') {
			$('.funds_choice_amend li').css('color', '#000');
		} else {
			$(this).css('color', '#4ba6de');
		}

	});

	$(".funds_all_Sector").on("click", function() {
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
		for(i = 0; i < $(this).closest('ul').find('li').length-1; i++) {
			$(".selection_criteria li[data-name='sector" + i + "']").remove();
		}
	});
	
	var strategylistval = sessionStorage.getItem("strategylist");
	if(strategylistval=="show"){
		$(".client-more-screen-wrap").css({'height':'100%','margin-top':'5px'});
		$('.funds_list_selected').removeClass('ifa-more-ico-hidden');
		$(".wmes-menu-hide").toggleClass("wmes-menu-hideac");
	}else{
		$(".client-more-screen-wrap").css({'height':'0','margin-top':'0px'});
		$('.funds_list_selected').addClass('ifa-more-ico-hidden');
	};
	//alert('1');
	$(".wmes-menu-hide").on("click",function(){
		$(this).toggleClass("wmes-menu-hideac");
		if( $(this).hasClass("wmes-menu-hideac")) {
			$(".client-more-screen-wrap").stop().animate({ 
				height: "100%"
			}, 300 );
			$(".client-more-screen-wrap").css({'margin-top':'5px'});
			$('.funds_list_selected').removeClass('ifa-more-ico-hidden');
			sessionStorage.setItem("strategylist", "show");
		}else{
			$(".client-more-screen-wrap").stop().animate({ 
				height: "0px",margin:'0px'
			}, 300 );
			$('.funds_list_selected').addClass('ifa-more-ico-hidden');
			sessionStorage.setItem("strategylist", "hide");
		};
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
		initialization();
	});
});