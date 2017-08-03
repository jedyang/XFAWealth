define(function(require) {
	var $ = require('jquery');
	angular = require('angular');
	require('pagination');
	require('layer');
	require("scrollbar");
	require("echarts");
//点击每个选项，在下面的已选方案中添加该选项
//	var listTime;
	$(".funds_all_Sector").on("click", function() {
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
		for(var i=1;i<21;i++){
			$(".selection_criteria li[data-name='Sector"+i+"']").remove();
		}
	});
	
	var FILTER = (function(){
		var keyWord = '',
		 	distributorId = '',
		 	ifaFirmId = firmId,
		 	sort = 'createTime',
		 	page = 0,
		 	order = 'desc';
		return{
			keyWord:keyWord,
			distributorId:distributorId,
			ifaFirmId:ifaFirmId,
			sort:sort,
			page:page,
			order:order
		};
	})();
	
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
	
	
	function portfolio_star(){
		$(document).on('click', ".portfolio_fav_img_amend", function() {
			$(this).toggleClass('portfolio_fav_img_amend1active');
		});
	}
	portfolio_star();
	
	$(".funds_choice li").on("click", function() {
		//		clearTimeout(listTime)
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
			$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
		}
		Initialization();
		// 解决重复请求的问题
		//		var self = this;
		//			listTime=setTimeout(function(){
		//				loadIFAList(self);
		//			}
		//			,100);
		
		FILTER.distributorId = $(this).attr("data-value");
		////console.log(FILTER.distributorId);
		$('#btnKeyWord').click();
		
	});
	
	//执行清除方案点击操作
	$(".funds_title_selection").on("click", function() {
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
			prefCount++
		})
		if(prefCount == 0) $("#per_all").addClass("per_active");
		
		FILTER.distributorId = '';
		$('#btnKeyWord').click();
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

	$(".strategies_list_ptos").each(function() {
		var selfData = eval($(this).attr("data-value"));
		var StrData = [];
		for(var i = 0; i < selfData.length; i++) {
			var temp, selfcolor, selfpst, selfstartAngle;
			if(i == 0) {
				temp = 52;
				selfstartAngle = 210;
				selfpst = '50%';
			} else if(i == 1) {
				temp = (selfData[1].value / selfData[0].value) * 52;

				selfpst = 63 + (52 - temp);
				selfstartAngle = 190;
			} else if(i == 2) {
				temp = (selfData[2].value / selfData[1].value) * 52;
				selfstartAngle = 150;
				selfpst = 63 + (52 - temp);
			};
			if(selfData[i].name.indexOf('Stock') !== -1) {
				selfcolor = '#66cdff';
			} else if(selfData[i].name.indexOf('Fund') !== -1) {
				selfcolor = '#ffcd00';
			} else {
				selfcolor = '#0ed887';
			}
			StrData.push({
				name: selfData[i].name,
				type: 'pie',
				radius: temp,
				center: ['38%', selfpst],
				startAngle: selfstartAngle,
				tooltip: {
					show: false,
					showContent: false
				},
				clickable: false,
				legendHoverLink: false,
				markPoint: {
					symbolSize: 0
				},
				selectedMode: null,
				selectedOffset:0,
				markPoint:{
					effect: {show:false}
				},
				data: [{
					value: selfData[i].value,
					name: selfData[i].name
				}],
				itemStyle: {
					normal: {
						color: selfcolor,
						labelLine: {
							length:35
						}
					},
					emphasis: {
						label:{
							textStyle:{
								fontSize: 16
							}
						}
					}
				}
			})
		};
		var option = {
			series: StrData
		};
		var myChart = echarts.init($(this)[0]);
		myChart.setOption(option);
	});
	
	/**实例化操作 */
	function Initialization() {
// 初始化数值
//	 	iPAGE = 1; //第N页数据
//		is_finish = true;
//		page_bol = true;
		selection();
	}
	
	var mybase = angular.module('accountsWait', ['wmespage','truncate']);
	mybase.controller('accountsWaitCtrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1;
		var rows = 10; 
		$scope.dataList = '';
		function getDataList(){
			var keyWord = $('#fundName').val(),
			distributorId = FILTER.distributorId,
			ifaFirmId = FILTER.ifaFirmId;
	   		var data = {
	           	  rows:rows,  
	           	  page:iPAGE, 
	           	  sort:FILTER.sort,
	           	  order:FILTER.order,
					
	           	  keyWord:keyWord,
	           	  distributorId:distributorId,
	           	  ifaFirmId:ifaFirmId
	   		};
	   		var url = base_root + "/front/investor/accountsWaitforProcessJson.do?dateStr=" + new Date().getTime();
			$http({
				url: url,
				method: 'POST',
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
				params : data
			})
			.success(function(response){//console.log(response);
               if(response.total > 0){
	       		  $scope.total = response.total;
	       		  $scope.accountConf.totalItems = response.total;
	           	  $(".no_list_tips").hide();
	           	  $scope.dataList =  response.rows;
         		}else{
         		  $scope.accountConf.totalItems = 0;
         		  $scope.total = 0;
				  $scope.dataList = '';
				  $(".no_list_tips").show();
         		}
           });
	   	}
	   	$scope.accountConf = {
	       itemsPerPage:rows,
	       onChange: function(){
	    	   iPAGE = $scope.accountConf.currentPage;
	    	   getDataList();
	       }
	   	};
	   	$scope.searchKeyBtn = function(){
	   		getDataList();
	   	};
	   	getDataList();
	}]);
	
	$(document).keypress(function(e) {  
	   if(e.which == 13) {
		  $('#btnKeyWord').click();
	   }  
	});	
	
	/***
	function getAccountsWaitforProcess(){
		var keyWord = FILTER.keyWord,
			distributorId = FILTER.distributorId,
			ifaFirmId = FILTER.ifaFirmId;
		
		var url = base_root + '/front/investor/getAccountsWaitforProcess.do';
		$.ajax({
			type:'post',
			url:url,
			data:{
				page:FILTER.page+1,
				rows:10,
				sort:FILTER.sort,
				order:FILTER.order,
				
				keyWord:keyWord,
				distributorId:distributorId,
				ifaFirmId:ifaFirmId
			},
			success:function(result){
				////console.log(result);
				if(result.flag){
					$('.accountsWaiforProcess-table tbody tr:gt(0)').remove();
					var investorAccounts = result.investorAccounts;
					var total = investorAccounts.list.length;
					$('.funds_serach_digital').text(total);
					if(investorAccounts != null && total!=0){
						var html = '';
						$.each(investorAccounts.list,function(i,n){
							var clientName = n.nickName == null?'':n.nickName,
								accountType = n.accType == null?'':n.accType,
								distributorName = n.distributorName == null?'':n.distributorName,
								clientMobile = n.mobileNumber== null?'':n.mobileNumber,
								summitTime = n.createTime == null?'':n.createTime,
								time = n.time == null?'':n.time,
								ifaName = n.ifaName == null?'':n.ifaName,
								ifaIconUrl = n.ifaIconUrl == null?'':n.ifaIconUrl;
							if(accountType == 'I'){
								accountType = 'Individual';
							}else if(accountType == 'J'){
								accountType = 'Joint';
							}
							html += 
								'<tr>'
							+'<td class="funds_search_tdcenter">'
							+'<a>'
							+ clientName
							+'</a>'
							+'</td>'
							+'<td class="funds_search_tdcenter">'
                            + clientMobile
                            +'</td>'
							+'<td class="funds_tables_fnames">'
							+ accountType
                            +'</td>'
                            +'<td class="funds_search_tdcenter">'
                            + distributorName
                            +'</td>'
                            +'<td class="funds_search_tdcenter">'
                            + summitTime
                            + '<br>'
                            + time
                            +'</td>'
                            +'<td class="funds_search_tdcenter accounttable-img">'
                            +'<div style="margin-left: 50px;"><img style="width: 38px;height:38px;border-radius: 50%;" src="'+base_root+ifaIconUrl+'"/></div>'
                            +'<span>'
                            + ifaName
                            +'</span>'	
                            +'</td>'	
                            +'<td class="funds_search_tdcenter">'
                            +'<a href="'+base_root+'/front/distributor/accountApprove.do?accountId='+n.id+'">'
                            +'<img src="'+base_root+'/res/images/application/find.png">'
    						+'</a>'
                            +'</td>'
                            +'</tr>';
						});
						$('.accountsWaiforProcess-table tbody').append(html);
						$('#pagination').pagination(total, {
							callback : pageselectCallback,
							numDetail : '',
							items_per_page : 10,
							num_display_entries : 4,
							current_page : FILTER.page,
							num_edge_entries : 2
						});
						function pageselectCallback(page_id, jq) {
							FILTER.page = page_id;
							$('#btnKeyWord').click();
						}
					}else{
						$('#pagination').empty();
					}
					
				}
			}
		});
	}
	***/
	//$('#btnKeyWord').click();
	//submitTime
	$('.funds_arrow_down_submitTime').click(function(){
		$('.funds_arrow_top').removeClass('funds_top_active');
		$('.funds_arrow_down').removeClass('funds_down_active');
		$('.funds_active').removeClass('funds_active');
		$(this).parent().parent().addClass('funds_active');
		$(this).addClass('funds_down_active');
		FILTER.sort = 'createTime';
		FILTER.order = 'desc';
		$('#btnKeyWord').click();
	});
	$('.funds_arrow_top_submitTime').click(function(){
		$('.funds_arrow_top').removeClass('funds_top_active');
		$('.funds_arrow_down').removeClass('funds_down_active');
		$('.funds_active').removeClass('funds_active');
		$(this).parent().parent().addClass('funds_active');
		$(this).addClass('funds_top_active');
		FILTER.sort = 'createTime';
		FILTER.order = 'asc';
		$('#btnKeyWord').click();
	});
	
	// distributor
	$('.funds_arrow_down_matching').click(function(){
		$('.funds_arrow_top').removeClass('funds_top_active');
		$('.funds_arrow_down').removeClass('funds_down_active');
		$('.funds_active').removeClass('funds_active');
		$(this).parent().parent().addClass('funds_active');
		$(this).addClass('funds_down_active');
		FILTER.sort = 'distributor';
		FILTER.order = 'desc';
		$('#btnKeyWord').click();
	});
	$('.funds_arrow_top_matching').click(function(){
		$('.funds_arrow_top').removeClass('funds_top_active');
		$('.funds_arrow_down').removeClass('funds_down_active');
		$('.funds_active').removeClass('funds_active');
		$(this).parent().parent().addClass('funds_active');
		$(this).addClass('funds_top_active');
		FILTER.sort = 'distributor';
		FILTER.order = 'asc';
		$('#btnKeyWord').click();
	});
	//performance
	$('.funds_arrow_down_year').click(function(){
		$('.funds_arrow_top').removeClass('funds_top_active');
		$('.funds_arrow_down').removeClass('funds_down_active');
		$('.funds_active').removeClass('funds_active');
		$(this).parent().parent().addClass('funds_active');
		$(this).addClass('funds_down_active');
		FILTER.sort = 'ifa.ifafirm';
		FILTER.order = 'desc';
		$('#btnKeyWord').click();
	});
	$('.funds_arrow_top_year').click(function(){
		$('.funds_arrow_top').removeClass('funds_top_active');
		$('.funds_arrow_down').removeClass('funds_down_active');
		$('.funds_active').removeClass('funds_active');
		$(this).parent().parent().addClass('funds_active');
		$(this).addClass('funds_top_active');
		FILTER.sort = 'ifa.ifafirm';
		FILTER.order = 'asc';
		$('#btnKeyWord').click();
	});
	
	/**关键字查询
	$('#searchKeyBtn').click(function(){
		FILTER.keyWord = $('#fundName').val();
		$('#btnKeyWord').click();
	});
	**
	$('.page_left').click(function(){
		if(FILTER.page!=0){
			FILTER.page = FILTER.page-1;
		}
		$('#btnKeyWord').click();
	});
	$('.page_right').click(function(){
		FILTER.page = FILTER.page+1;
		$('#btnKeyWord').click();
	});
	*/
	//清空过滤
	$('.funds_title_selection').click(function(){
		FILTER.distributorId = '';
		$('#btnKeyWord').click();
	});
	
	/****初始化分页
	$('#pagination').pagination($('.funds_serach_digital').text(), {
		callback : pageselectCallback,
		numDetail : '',
		items_per_page : 10,
		num_display_entries : 4,
		current_page : FILTER.page,
		num_edge_entries : 2
	});
	function pageselectCallback(page_id, jq) {
		FILTER.page = page_id;
		$('#btnKeyWord').click();
	}
	****/
	$('.funds_all').click(function (){
		FILTER.distributorId = '';
		$('#btnKeyWord').click();
	});
});