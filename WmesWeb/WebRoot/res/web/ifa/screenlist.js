
define(function(require) {
	'use strict';
	var $ = require('jquery'),
		angular = require('angular');
		require("scrollbar");
		require('layer');
		var Loading = require('loading');
		$('.funds_search_wrap').perfectScrollbar();
		$(".funds_search_tab").width($(".funds_serach_sum").width());
		function Fundsscrool(){

	    	//表头滚动
				var funds_hei =  $(".funds_search_tab").height(),
					 wmes_window_top = $(window).scrollTop(),
					infor_top = $(".funds_search_information").offset().top,
					funds_top = infor_top ;
					 
				if( $('.no_list_tips').css('display') == "block" ){
					return false;
				}
				 if ( wmes_window_top > infor_top ){
				 	var new_funds_top = wmes_window_top - infor_top  + funds_hei;
				 	$(".funds_search_tab").addClass("funds_fixed").css("top",0);
						var	 _thisStyle =  "-webkit-transform : translateY(" + new_funds_top + "px);-moz-transform : translateY(" + new_funds_top + "px);-ms-transform : translateY(" + new_funds_top + "px);-o-transform : translateY(" + new_funds_top + "px);transform : translateY(" + new_funds_top + "px)";
						$('.funds_tables_th').attr("style",_thisStyle);

						//解决低版本浏览器tr无法滚动问题
						if( infor_top == $('.funds_tables_th').offset().top && new_funds_top > funds_hei + 2 ){
							$('.funds_tables_th').addClass("funds_tables_caption")
						}
						// $(".ps-scrollbar-x-rail").css("top",new_funds_top + $('.funds_tables_th').height());
				 }else{

				 	$(".funds_search_tab").removeClass("funds_fixed");
					var	 _thisStyle =  "-webkit-transform : translateY(" + 0 + "px);-moz-transform : translateY(" + 0 + "px);-ms-transform : translateY(" + 0 + "px);-o-transform : translateY(" + 0 + "px);transform : translateY(" + 0 + "px)";
						$('.funds_tables_th').attr("style",_thisStyle);
						// $(".ps-scrollbar-x-rail").css("top",60);

				 }
	    };
	    $(window).on('scroll',Fundsscrool);
	   	$(window).on('resize',function(){
	   		$(".funds_search_tab").width($(".funds_serach_sum").width())
	   	});
		var leftFlag = true;
//		var leftContent = "";
//		var rightContent = "";
		//getQueryCriteria();
		
		//筛选条件
		function getQueryCriteria(){	
			var criteriaId = getUrlParam('criteriaId');
			$.ajax({
				type : 'get',
				datatype : 'json',
				url : base_root+'/front/ifa/info/queryCriteria.do?datestr='+new Date().getTime(),
				data : {	'criteriaId':criteriaId			
				},
				async : false,
				success : function(json) {									
					//var divContent = "";
//					var list = json.dataType;
//					//console.log(json.criteria);
					var jsonObj = eval("("+json.criteria+")");
					$.each(jsonObj.criteria, function (idx, item) {
//		                //console.log(item.lable + "  " + item.text);
						if( "currency,geoAllocation,fundType,sectorType,fundSize,inceptionDate,minInitialInv,mgtFee,".indexOf(item.code) > -1 ){
							getCriteriaNameList(item.title,item.value,"paramConfig");
						}else if( "distributor".indexOf(item.code) > -1 ){
							getCriteriaNameList(item.title,item.value,"distributor");
						}else if( "fundHouseIds".indexOf(item.code) > -1 ){
							getCriteriaNameList(item.title,item.value,"fundHouse");
						}else if( "sinceLaunch,trailingYTD,trailing1Mon,trailing3Mon,trailing6Mon,trailing1Year,trailing3Year,trailing5Year,".indexOf(item.code) > -1 ){
//							//console.log("code:"+item.code);
							var cvalue = item.value.replace(",","%&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;")+"%";
							getFilterData(item.title,cvalue);
						}else{
							//riskLevel
//							//console.log("code:"+item.code);
							getFilterData(item.title,item.value); 
						}
		                
		                $("#"+item.code).val(item.value);
		            });

//		            $(".funds_facts_information_left").append(leftContent);
//		            $(".funds_facts_information_right").append(rightContent);
				}
			})
		}
		getQueryCriteria();
		
		//筛选条件
		function getCriteriaNameList(title,codeList,criteriaType){	
			$.ajax({
				type : 'get',
				datatype : 'json',
				url : base_root+'/front/ifa/info/criteriaNameList.do?datestr='+new Date().getTime(),
				data : {	'codeList':codeList	,'criteriaType':criteriaType		
				},
				async : false,
				success : function(json) {									
					//var divContent = "";
					var nameList = json.nameList;
//					//console.log(json.nameList);
					getFilterData(title,nameList); 
				}
			})
		}
		

		/*
		 * 获取Url传递参数值
		 */
		function getUrlParam(name){  
		    //构造一个含有目标参数的正则表达式对象  
		    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
		    //匹配目标参数  
		    var r = window.location.search.substr(1).match(reg);  
		    //返回参数值  
		    if (r!=null) return unescape(r[2]);  
		    return null;  
		}
		
		
		//生成单个条件内容
		function getFilterData(key,value){			
			if(null != value && "" != value ){

				var content = '<div><p class="funds_information_title">'+key+'</p>'
                            + '<p class="funds_information_word" title="'+ value + '">'+getString(value)+'</p></div>';
				if(leftFlag == true){
//					leftContent += content; 
					$(".funds_facts_information_left").append(content);
				}else{
//					rightContent += content;
					$(".funds_facts_information_right").append(content);
				}
				leftFlag = !leftFlag;
			}
		}
		
		//截取字符串函数
		function getString(str){

			var _str = str.replace(/&nbsp;/ig,"?"),

				_length = _str.length;

			if (_length > 50){

				_str = _str.substring(0,50) + '...';
			}	
			_str = _str.replace(/\?/ig,"&nbsp;");

			
			return _str;
		}

		
	// 数据控制
	var mybase = angular.module('mySearch', ['truncate']);
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
	    	var rows = 9;  //固定每次拿9条数据
	    	
	    	var oLoading = new Loading($(".funds-views-lump"));	
	    	// 正常拿数据	 
	    	function getDataList(){
	    		var url = base_root + "/front/ifa/info/getFundListJson.do",
	    			// data =  "sort=risk_level&order=desc&langCode=en&rows="+ rows +"&page=" + iPAGE;
	    		Searchdata = $("#listForm").serialize(),	 
	    		data =  "langCode=" + lang + "&rows="+ rows +"&page=" + iPAGE + "&" + Searchdata + "&" + Sortdata;

	    		$("#ifa_more").hide();
					oLoading.show();
	    			//控制数据是否加载完成
	    			$http({
                      url: url,
                      method: 'POST',
                      data : data,
                      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                 	 })
	    			.success(function(response){
	    				 oLoading.hide();
		                 
		                 var total = response.total;
	    				  sumpage = Math.ceil(total / rows);
	    				  
	                      if(response.list.length > 0){
	                      		$("#ifa_more").show();
	                      		 if( iPAGE === 1 ){
	                      			$("#comparison-num").html("0");
	                                 $scope.dataList =  response.list;
	                              }else{
	                                 $scope.dataList = $scope.dataList.concat(response.list);
	                              }
	                      		// 总页数
	                      		var sumPage = Math.ceil(response.total / rows);
	                      		exchange();
                      		}else{
                     			 if( iPAGE === 1 ){
                      			 	// 当第一页没有数据，显示提示信息
                      			 	$(".no_list_tips").show();
                      			 }
                      		}
  		    			iPAGE++;
  		    			checkPage();
  		    			$scope.datatotal = response.total;
                    });
	    	}
	    	getDataList();
			

	    	function Initialization(){
				// 初始化数值
			 	iPAGE = 1; //第N页数据
	    		$scope.dataList = '';
	    		$(".no_list_tips").hide();
	    		checkPage();
	    		selection();
	    		getDataList();
	    		$("#ifa_more").hide();
	    	}
	    	function checkPage(){
	    		if(iPAGE <= sumpage){
	    			$("#ifa_more").show();
	    			$("#ifa_more").html(langMutilForJs["fund.info.list.clickForMore"]);
	    		}else{
	    			$("#ifa_more").hide();
	    			$("#ifa_more").html(langMutilForJs["fund.info.list.noMore"]); //"没有更多的数据了"
	    		}
	    		
	    	}
	    	$("#ifa_more").on("click",function(){
	    		if(iPAGE <= sumpage){
	    			getDataList();
	    		}
	    	});

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
    
	// 货币转换
	var conversion = require("conversion");

	$(".conversion_select li").on("click",function(){

		new conversion($(this).attr("data-conversion"),$(this).html());
		$(this).parents(".funds_currency_xiala").find("input").val( $(this).html() );

	}) 
	
	function exchange(){
		var selectedCurrency = $('#hdSelectedCurrency').val();
		if(selectedCurrency != ''){
			$('.conversion_select li').each(function(){
				var currency = $(this).attr('data-conversion');
				if(selectedCurrency == currency){
					$(this).click();
					$('.funds_currency_xiala').find("ul").removeClass("funds_currency_active").hide();
					return false;
				}
			});
		}
		
	}
	
	$(".funds_currency_xiala").on("click",function(){

		if( $(this).find("ul").hasClass("funds_currency_active") ){
			$(this).find("ul").removeClass("funds_currency_active").hide();
		}else{
			$(this).find("ul").addClass("funds_currency_active").show();

		}
	});
    
	$(".funds_view_switch_ico").on("click",function(){
		$(".funds-views-lump").show(); $(".funds_switch_tab").show(); $(".funds_viewList_tab").hide(); $(".funds-views-list").hide();

		$(".funds_comparison_check:checked").each(function(){
			$(this).click();
		});
	});
	$(".funds_view_list_ico").on("click",function(){
		$(".funds-views-lump").hide();$(".funds_switch_tab").hide();$(".funds_viewList_tab").show();$(".funds-views-list").show();
		
		$(".lump_comparison_check:checked").each(function(){
			$(this).click();
		});
	});
	

	$("#editCriteria,#editCriteria2").on("click",function(){
		var criteriaId = getUrlParam("criteriaId");
		window.location.href = base_root+'/front/ifa/info/fundsscreener.do?&criteriaId='+criteriaId;
	});
	
	$("#newCriteria").on("click",function(){
		window.location.href = base_root+'/front/ifa/info/fundsscreener.do';
	});
	
	
	
	$(".views-lump-ul li").on("click",".funds-lump-title",function(){
		window.location.href = base_root + "/front/fund/info/fundsdetail.do?id=A87FF679A2F3E71D9181A67B7542122C";
	});
	
	$("#saveCriteria,#saveCriteria2").on("click",function(){
		 layer.open({
			  area: ['500px', '240px'],
			  content: '\<\div class="criteria-wrap"><p class="criteria-wrap-title">Save Criteria</p>Description<br /><textarea id="txtDescription" style="width:100%;"  rows="5"></textarea>\<\/div>',
			  btn: ['Save'],
			  yes: function(index,layero){
				  var description = $("#txtDescription").val();
				  if('' != description){
					  saveCriteria(description);
					  layer.close(index);
				  }else{
					  layer.msg("Description can not be empty");
				  }				  
			  }
			  });
	});
	

	function saveCriteria(description,criteriaId){
		if(null == criteriaId || "" == criteriaId){
			criteriaId = getUrlParam("criteriaId");
		}
		
//		var description = $("txtDescription").val();
//		//console.log(description);
		
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/ifa/info/saveQueryCriteria.do?datestr='+new Date().getTime(),
			data : {'criteriaId':criteriaId	,'type':'0','description':description
			},
			async : true,
			success : function(json) {	
//				//console.log(json);
				getMyQueryCriteriaList();
				layer.msg("Operation is successful");
			}
		})
	}
	
	function deleteCriteria(criteriaId){
						
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root+'/front/ifa/info/deleteQueryCriteria.do?datestr='+new Date().getTime(),
			data : {'criteriaId':criteriaId
			},
			async : true,
			success : function(json) {	
//				//console.log(json);
				getMyQueryCriteriaList();
				layer.msg("Operation is successful");
			}
		})
	}
	
	
	function getMyQueryCriteriaList(){				
		$.ajax({
			type : 'get',
			datatype : 'json',
			url : base_root+'/front/ifa/info/queryCriteriaList.do?datestr='+new Date().getTime(),
			data : {
			},
			async : true,
			success : function(json) {	
				if(json.flag == true){
					var list = json.rows;
					$("#srenner-criteria-list").empty();
					var liContent = '';
					$.each(list, function (index, array) { //遍历json数据列

						liContent += '<li class="srenner-criteria-rows" data-value="'+array.id+'">'
                                   + '<input data-url="'+base_root+'/front/ifa/info/fundslist.do?criteriaId='+array.id+'"' 
                                   + 'class="srenner-criteria-input" type="" name="" value="' + array.description + '" readonly="" maxlength="18">'
                                   + '<span class="screener-list-edit"></span><span class="screener-list-close"></span></li>';

					});
					if(liContent == ''){
						liContent = '<li>no criteria</li>';
					}
					$("#srenner-criteria-list").append(liContent);
				}			
			}
		})
	}

	getMyQueryCriteriaList();
	
	$(".funds-views-list").on("click",".funds_comparison_check",function(){
		if( $(".funds_comparison_check:checked").length > 5){
			layer.msg(langMutilForJs['fund.info.list.comparisonNum']);
			$(this).removeAttr("checked");
		}else{
			var Strhref = "";
			$(".funds_comparison_check:checked").each(function(){
				Strhref += $(this).val() + ',';
			});
			var Strhref = base_root + "/front/fund/info/fundscomparison.do?id=" + Strhref.substring(0,Strhref.length-1);
			$("#comparison-num").html(  $(".funds_comparison_check:checked").length);
			$("#comparison").attr("href",Strhref);
		}
		
	});

	$(".views-lump-ul").on("click",".lump_comparison_check",function(){

		if( $(".lump_comparison_check:checked").length > 5){
			layer.msg(langMutilForJs['fund.info.list.comparisonNum']);
			$(this).removeAttr("checked");
		}else{

			if($(this).is(':checked')){
				$(this).parents("li").addClass("funds_lump_active");
			}else{
				$(this).parents("li").removeClass("funds_lump_active");
			}
			var Strhref = "";
			$(".lump_comparison_check:checked").each(function(){
				Strhref += $(this).val() + ',';
			});
			var Strhref = base_root + "/front/fund/info/fundscomparison.do?id=" + Strhref.substring(0,Strhref.length-1);
			$("#comparison-num").html(  $(".lump_comparison_check:checked").length);
			$("#comparison").attr("href",Strhref);
		}

	});

	$(".funds-screnner-rows").on("click",".screener-list-edit",function(){
		$(this).parents(".srenner-criteria-rows").toggleClass("screener-group-edit");

		if($(this).parents(".srenner-criteria-rows").hasClass("screener-group-edit") ){
			$(this).parents(".srenner-criteria-rows").find(".srenner-criteria-input").removeAttr("readonly");
		}else{
			$(this).parents(".srenner-criteria-rows").find(".srenner-criteria-input").attr("readonly","readonly");
			
			var criteriaId = $(this).parents(".srenner-criteria-rows").attr("data-value"),
			criteriaName = $(this).parents(".srenner-criteria-rows").find("input").val();
//			//console.log(criteriaName+' -- '+ criteriaId);
			saveCriteria(criteriaName,criteriaId);
		}
	});
	
	$(".funds-screnner-rows").on("click",".srenner-criteria-input",function(){
		if(!$(this).parents(".srenner-criteria-rows").hasClass("screener-group-edit") ){
			 window.location.href= $(this).attr("data-url");
		}
	});
	$(".funds-screnner-rows").on("click",".screener-list-close",function(){

		var criteriaId = $(this).parents(".srenner-criteria-rows").attr("data-value");
    	layer.confirm('Are you sure you want to delete?', {
				title:'',
				btn: ['Yes','No'] //按钮
			}, function(){
				layer.closeAll('dialog');
				deleteCriteria(criteriaId);
				$(this).parents(".srenner-criteria-rows").remove();

		    			    	
			}, function(){
			  
			});
	});

	$("#j-criteria").on("click",function(){
		$(this).parent().toggleClass("srenner-criteria-show");
	});

	$(document).on("click",function(event){
		//點擊其他地方隐藏元素
		var _criteria = $("#j-criteria").parent();

		if(!_criteria.is(event.target)  && _criteria.has(event.target).length === 0){ 

			_criteria.removeClass("srenner-criteria-show");  

		}

	});
	
	// 收藏
	$("body").on("click",".funds_heart",function(){

		var loginVal = $("#loginCode").val(),
			_this = $(this),
			status  = _this.attr("followFlag"),
			fundId = _this.attr("fundId"),
			productId = _this.attr("productId");
		if(loginVal == 'true'){
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
	    			alert("error!");
	    			lay.msg('error',{icon:2});
	    		}
	    	})
		}else{
			window.parent.location.href=base_root+"/front/viewLogin.do?r="+Math.random();
			return;
		}		
	})
	
    //加入购物车
    $(".addCart").on("click",function(){
        if($(this).hasClass('funds_cart2_active')){
            $(this).removeClass("funds_cart2_active");
        }else{
            $(this).addClass("funds_cart2_active");
        }
    });
    
    $(document).on('click','.views-lump-ul li',function(){
		$(this).find('.lump_comparison_check').click();
	});
});