
define(function(require) {

	var $ = require('jquery');
			require("echarts");
			require("scrollbar");
			var angular = require('angular');
			$('.funds_search_wrap').perfectScrollbar();
			var Loading = require('loading');
			var oLoading = new Loading($(".account_list"));
			var selector =  require('ifaSelectUser');
			selector.init();
	require("interfaceCheckPwd")
	// 验证登录
	if(_checkList!=undefined && _checkList!=""){
		$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
	}
			
	// 数据控制
	var mybase = angular.module('mySearch', []);
    mybase.controller('Searchctrl', ['$scope', '$http', function($scope, $http) {
	 		var url = base_root + "/console/investorClient/ifaFirmClientJson.do", // url
	 			iPAGE = 1, // complete,第N页数据
	 			iPAGE2= 1, // uncomplete
	    		is_finish = true,// 当前数据是否加载完成
//	    		Searchdata = "",// 搜索条件初始化
//	    		Sortdata = "",// 排序条件初始化 默认根据issue
															// price（增长率降序）
	    		page_bol = true;// 总页数控制
	    		
	    		// 初始化数据
	    		$scope.dataList = '';
	    		$scope.approvalCount=0;

	    	// 数字调用
	    	$scope.counter = Array;
	    	
	    	// 监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){
					// setTimeout(function(){
					// initChar();//初始化图表
					// },100)
					$(".listContent").show();
				}
			}

	    	var rows = 10  // 固定每次拿10条数据
	    	// 正常拿数据
	    	function getDataList(){
	    		oLoading.show();
	    		var distributorId="";
				 var period="";
				 $(".conservative-choice li").each(function(){
					 if( $(this).hasClass('conservative-choice-active')){
						 if($(this).attr("data-name")=='distributor' ){
							 distributorId=$(this).attr("data-value");
						 }else if($(this).attr("data-name")=='period'){
							 period=$(this).attr("data-value");
						 } 
					 }
					 
				 });
				 if(distributorId==undefined){
					 distributorId="";
				 }
				 if(period==undefined){
					 period="";
				 }
				 
				 var in_use="";
				 var inApproval="";
				 var cancellation="";
				 if($("#Active").is(':checked')){
					 in_use="1";
				 }else{
					 in_use="";
				 }
					
				 if($("#Processing").is(':checked')){
					 inApproval="1";
				 }else{
					 inApproval=""; 
				 }
					
				 if($("#Cancel").is(':checked')){
					 cancellation="1";
				 }else{
					 cancellation="";
				 }
					
				 var currency=$("#currency").val();
				 var keyWord=$("#txtKeyWord").val();
				// var url="/wmes/front/ifa/space/accountList.do?;
	    			
				 var el=$(".listCompleted .client-list-tab").find(".down_active");
				 if(el==undefined || el.length==0){
					 el=$(".listCompleted .client-list-tab").find(".top_active");
				 }
				 var sort=$(el).attr("sort");
				 var order=$(el).attr("order");
	    		    
	    		var   data ="rows="+ rows +"&page=" + iPAGE + "&in_use="+in_use+"&inApproval="+inApproval+"&cancellation="+cancellation+"&cur="+currency+"&period="+period
	    		+"&distributorId="+distributorId+"&keyWord="+keyWord;
	    		data+="&sort="+sort+"&order="+order;
	    		
    			// 控制数据是否加载完成
    			is_finish = false;
    			$http({
					url: url,
					method: 'POST',
					data : data,
					headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
             	 })
    			.success(function(response){
    				// //console.log("re=="+response.accountList);
    				oLoading.hide();
	                is_finish = true;
                    if(response.accountList.length > 0){
                  		$(".funds_search_information , .funds_search_wrap").show();
                  		$(".tips1").hide();
                  		if( iPAGE === 1 ){
                  			$scope.dataList = '';
                  			$scope.dataList =  response.accountList;
// //console.log("list=="+$scope.dataList);
                  			$scope.approvalCount=0;
                        }else{
                            $scope.dataList = $scope.dataList.concat(response.accountList);
                        }
                  		// 总页数
                  		var sumPage = Math.ceil(response.total / rows);
                  		if(iPAGE >= sumPage){
                  			page_bol = false;
                  		}
              		}else{
// //console.log("list is null");
// //console.log(iPAGE);
              			page_bol = false;
             			 if( iPAGE === 1 ){
             				 $scope.dataList = '';
             				 $scope.approvalCount=0;
              			 	// 当第一页没有数据，显示提示信息
              			 	$(".funds_search_information , .funds_search_wrap").hide()
              			 	$(".tips1").show();
              			 }
              		}
	    			iPAGE++;
	    			$scope.datatotal = response.total;
	    			$scope.currency=response.currency;
	    			$scope.approvalCount=$scope.approvalCount+response.approvalCount;
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
			 	iPAGE = 1; // 第N页数据
			 	iPAGE2 = 1; // 第N页数据
	    		is_finish = true;
	    		page_bol = true;
	    		$scope.dataList = '';
	    		selection();
// getDataList();
//				var status=$(".now").attr("status");
//				if(status=="1"){
					getDataList();// 完成审批的账户：3、4
//				}else{
//					getUnList();
//				}	    		
	    	}
	    	function GetScroll(){
	    		var docheight = $(window).scrollTop() + $(window).height(),
	    			listheight = $('.listContent').offset().top + $('.listContent').height() + $(".wmes_top").height();
	    		
	    		if( docheight > listheight ){
	    			scrollBol = true;
	    			
	    		}else{
	    			scrollBol = false;
	    		}

    			if (scrollBol && is_finish && page_bol ){
//    				var status=$(".now").attr("status");
//    				if(status=="1"){
    					getDataList();// 完成审批的账户：3、4
//    				}else{
//    					getUnList();
//    				}
    			}
	    	}
	    	
	    	$("#searchKeyBtn").on("click",function(){
//	    		var status=$(".now").attr("status");
//				if(status=="1"){
					iPAGE=1;
					getDataList();// 完成审批的账户：3、4
//				}else{
//					iPAGE2=1;
//					getUnList();
//				}
	    	});
	    	
	    	// 获取非正式的帐号列表
	    	function getUnList(){
	    		var distributorId="";
				 var period="";
				 $(".conservative-choice li").each(function(){
					 if( $(this).hasClass('conservative-choice-active')){
						 if($(this).attr("data-name")=='distributor' ){
							 distributorId=$(this).attr("data-value");
						 }else if($(this).attr("data-name")=='period'){
							 period=$(this).attr("data-value");
						 } 
					 }
				 });
				 if(distributorId==undefined){
					 distributorId="";
				 }
				 if(period==undefined){
					 period="";
				 }
	    		 var currency=$("#currency").val();
				 var keyWord=$("#txtKeyWord").val();
				 var el=$(".listUnCompleted .client-list-tab").find(".down_active");
				 if(el==undefined || el.length==0){
					 el=$(".listUnCompleted .client-list-tab").find(".top_active");
				 }
				 var sort=$(el).attr("sort");
				 var order=$(el).attr("order");
	    		    
	    		var data ="rows="+ rows +"&page=" + iPAGE2 +"&cur="+currency+"&period="+period
	    				+"&distributorId="+distributorId+"&keyWord="+keyWord;
	    		data+="&sort="+sort+"&order="+order;
	    		
    			// 控制数据是否加载完成
    			is_finish = false;
    			$http({
                 url: base_root + "/console/investorClient/ifaFirmUnComplateclientJson.do",
                 method: 'POST',
                 data : data,
                 headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            	 })
    			.success(function(response){
    				oLoading.hide();
    				is_finish = true;
                  	if(response.accountList.length > 0){
                  		$(".funds_search_information , .funds_search_wrap").show();
                  		$(".tips2").hide();
                  		 if( iPAGE2 === 1 ){
                  		 	 $scope.dataList1 = '';
                             $scope.dataList1 =  response.accountList;
                          }else{
                             $scope.dataList1 = $scope.dataList1.concat(response.accountList);
                          }
                  		// 总页数
                  		var sumPage = Math.ceil(response.total / rows);
                  		if(iPAGE2 >= sumPage){
                  			page_bol = false;
                  		}
             		}else{
             			page_bol = false;
            			 if( iPAGE2 === 1 ){
            				 $scope.dataList1 = '';
             			 	// 当第一页没有数据，显示提示信息
             			 	$(".funds_search_information , .funds_search_wrap").hide()
             			 	$(".tips2").show();
             			 }
             		}
                  	iPAGE2++;
	    			$scope.datatotal1 = response.total;
               });
	    	}
//	    	getUnList();

	    	function initChar(){
	    		$(".client-chart").each(function(){
	    			var hasLoad=$(this).attr("hasLoad");
	    			if(hasLoad=="1")
	    				return ;
	    			var selfData = eval($(this).attr("data-value"));
	    			var cashData={};
	    			var marketData={};
	    			$.each(selfData,function(i,n){
	    				if(n.name=="Market Value"){
	    					marketData=n;
	    				}else if(n.name="Cash"){
	    					cashData=n;
	    				}
	    			})
					var option = {
					    
					    legend: {
					    	orient : 'vertical',
					        x: 'right',
					        itemWidth:'15',
					        itemHeight:'15',
					        y:'60px',
					        data:['Cash','Market Value']
					    },
					    series: [
					        {
					            name:'account',
					            type:'pie',
					            center:	['30%', '50%'],
					            radius : [0,"80%"],
					            color :["#c3ef56","#8fc3ff"],
					            label: {
					                normal: {
					                    position: 'inner',
					                    formatter : "{d}%"
					                }
					            },
					       
					            data:[
					                {value:cashData.value, name:'Cash'},
					                {value:marketData.value, name:'Market Value'}
					            ]
					        }
					    ]
					};

					var myChart = echarts.init($(this)[0]);
		  			myChart.setOption(option,true);
		  			$(this).attr("hasLoad","1");
				});
	    	}
	    	
	    	
	    	// 点击每个选项，在下面的已选方案中添加该选项
			var listTime;
			$(".conservative-choice li").on("click",function(){
				if($(this).index()==0){
			 		$(this).addClass('conservative-choice-active2');
			 	}else{
			 		$(this).closest('ul').find('li').removeClass('conservative-choice-active2');
			 	};
				
				clearTimeout(listTime)
				if($(this).parent().hasClass("funds_logo_b")){
					return;
				}
				

				var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght ;i++){
						if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") || $(this).text()=='All' ){
							$(".selection_criteria li").eq(i).remove();
						}
				}
				$(this).siblings().removeClass("conservative-choice-active").end().addClass("conservative-choice-active");
				if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
					$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				}

				
				// 解决重复请求的问题
				// var self = this;
				listTime=setTimeout(function(){
					Initialization();// loadIFAList(self);
				}
				,100);

			});
	    	
	    	$('.conservative_all').addClass('conservative-choice-active2');
	    	/**
			 * 搜索条件取消点击 author scshi date 20160821
			 */
			$(".selection_criteria").on("click",".selection_delete",function(){
				$(this).parent().remove();
				var funds_all_lenght = $('.conservative_all').length;
				for( var i = 0; i < funds_all_lenght ; i++){
					if($(this).parent().attr("data-name") == "FundHouse"){
						var fundslenght = $("#funds_logo li").length;
						for(var funds = 0 ; funds < fundslenght ;funds++){
							if( $(this).parent().attr("data-value") ==  $("#funds_logo li").eq(funds).attr("data-value") ){
								$("#funds_logo li").eq(funds).click();
							}
						}
						break;
					}else if( $(this).parent().attr("data-name") ==  $('.conservative_all').eq(i).attr("data-name") ){
						$('.conservative_all').eq(i).click();
					}
				}
				
				var prefCount=0;
				$(".selection_criteria").find("li").each(function(){
					$(this).attr("data-value")=="pref";
					prefCount++
				})
				if(prefCount==0)$("#per_all").addClass("per_active");
				
			});	
			
			// 执行清除方案点击操作
			$(".funds_title_selection").on("click",function(){
				$(".selection_criteria li").remove();

				$(".fund_logo_active").removeClass("fund_logo_active");
				$(".conservative-choice-active").removeClass("conservative-choice-active");
				$(".conservative_all").addClass("conservative-choice-active");
				$("#listForm").find("input").val("");
				Initialization();
			});
			$('.conservative-choice-button').on('click',function(){
				Initialization();
			})
			
			function selection(){
				var _thisLenght =  $(".selection_criteria").children().length;
				
				if( _thisLenght != 1  ){
					$(".funds_title_selection").css('display','inline-block');
				}else{
					$(".funds_title_selection").css('display','none');
				}
			}
			
			// 下拉
			$(".proposal_xiala").on("click",function(){
				$(this).toggleClass("xiala-show");
			});
			$(".proposal_xiala li").on("click",function(e){
				$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input").val($(this).html());
				e.stopPropagation(); 
				Initialization();
			});
			$("#Active").on("change", function () {
				Initialization();
			 })
			 $("#Processing").on("change", function () {
				 Initialization();
			 })
			 $("#Cancel").on("change", function () {
				 Initialization();
			 })
			 
			 $('.arrow_down').on("click",function(){
					$('.arrow_down').removeClass("down_active");
					$('.arrow_top').removeClass("top_active");
					$(this).parents("li").siblings().removeClass("recommend-sort-active").end().addClass("recommend-sort-active");
					$(this).addClass("down_active");
					Initialization();
				});
				$('.arrow_top').on("click",function(){
					$('.arrow_down').removeClass("down_active");
					$('.arrow_top').removeClass("top_active");
					$(this).parents("li").siblings().removeClass("recommend-sort-active").end().addClass("recommend-sort-active");
					$(this).addClass("top_active");
					Initialization();
				});
				
	    	
    }]);
		
  // var isLoaded=false;
	$(".strategy_client_tab li").on("click",function(){
		$(this).siblings().removeClass("now").end().addClass("now");
		$(".account-list-wrapper-rows").hide().eq($(this).index()).show();
		/*
		 * if(!isLoad){ getUnList(); isLoaded=true; }
		 */
			
	});
	
	$("#txtKeyWord").on("click",function(){
		selector.create(1,'client','memberId','txtKeyWord');
		$(".selectnamelistbox").show();
	})

		
});