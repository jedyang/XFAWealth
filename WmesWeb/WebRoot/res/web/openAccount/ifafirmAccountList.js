
define(function(require) {

	var $ = require('jquery');
			require("echarts");
			require("scrollbar");
			angular = require('angular');
			$('.funds_search_wrap').perfectScrollbar();

	// 数据控制
	var mybase = angular.module('mySearch', ['wmespage','truncate']);
    mybase.controller('Searchctrl', ['$scope', '$http', function($scope, $http) {
	 		var url = base_root + "/console/ifafirm/client/accountListJson.do", // url
	 			//iPAGE = 1, // 第N页数据
	    		is_finish = true,// 当前数据是否加载完成
	    		Searchdata = "",// 搜索条件初始化
	    		Sortdata = "",// 排序条件初始化 默认根据issue
															// price（增长率降序）
	    		page_bol = true;// 总页数控制
	    		
	    		// 初始化数据
	    		$scope.dataList = '';
	    		$scope.approvalCount=0;
	    		
		    	// 数字调用
		    	$scope.counter = Array;
		    	$scope.currencyName=$("#currencyName").val();
		    	$scope.decimal=$("#decimal").val();
	    	
	    	// 监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){
					// 删除渲染效果
					$(".animated").one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				    		$(".animated").removeClass();
				    });
					setTimeout(function(){
						
						if($scope.datatotal>0){
							$(".account-wrap .listContent").show();
						}
					},100)
				}
			}

	    	var rows = 10  // 固定每次拿10条数据
	    	// 正常拿数据
	    	function getDataList(iPAGE){
	    		var distributorId="";//$("#distributorId").val();
				var accountStatus="";
				var docDate="";
				var rpqDate="";
				var market="";
				var asset="";
				var keyword=$("#txtKeyWord").val();
				 
				$(".conservative-choice").find(".conservative-choice-active").each(function(){
					 if($(this).attr("data-name")=='account-status'){
						 accountStatus=$(this).attr("data-value");
					 }
					 else if($(this).attr("data-name")=='rpq'){
						 rpqDate=$(this).attr("data-value");
					 }
					 else if($(this).attr("data-name")=='doc'){
						 docDate=$(this).attr("data-value");
					 }
					 else if($(this).attr("data-name")=='market'){
						 market=$(this).attr("data-value");
					 }
					 else if($(this).attr("data-name")=='asset'){
						 asset=$(this).attr("data-value");
					 }else if($(this).attr("data-name")=="distributor"){
						 distributorId=$(this).attr("data-value");
					 }
				 })
				
				var currency=$("#currency").val();
	    		var   data ="rows="+ rows +"&page=" + iPAGE + "&distributorId="+distributorId+"&accountStatus="+accountStatus+"&docDate="+docDate+"&cur="+currency+"&rpqDate="+rpqDate
	    		+"&market="+market+"&keyword="+keyword+"&asset="+asset;
	    			// 控制数据是否加载完成
	    			is_finish = false;
	    			$http({
                      url: url,
                      method: 'POST',
                      data : data,
                      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                 	 })
	    			.success(function(response){
		                 is_finish = true;
	                      if(response.accountList.length > 0){
	                    	  $(".account-wrap .myProposalList ").show()
	                      		$(".no_list_tips").hide();
	                      	  $scope.dataList =  response.accountList;
	                      		var sumPage = Math.ceil(response.total / rows);
	                      		if(iPAGE >= sumPage){
	                      			page_bol = false;
	                      		}
                      		}else{
                      			page_bol = false;
                     			 if( iPAGE === 1 ){
                     				 $scope.dataList = '';
                     				 $scope.approvalCount=0;
                      			 	// 当第一页没有数据，显示提示信息
                      			 	$(".account-wrap .myProposalList ").hide()
                      			 	$(".no_list_tips").show();
                      			 }
                      		}
  		    			
  		    			$scope.datatotal = response.total;
  		    			$scope.currency=response.currency;
  		    			
  		    			$scope.account_page.totalItems = response.total;//设置总数
                    });
	    			
	    			
	    	}
	    	
	    	//绑定【页码】跳转事件
	    	$scope.account_page = {
	                itemsPerPage:rows,//每条多少数据
	                onChange: function(){
	                	getDataList($scope.account_page.currentPage);
	            	}
	            };

	    	     $(document).on("click",".account_page li",function(){
	    	    	 var page=$(this).attr("data-page");
	    	    	 getDataList(page);
	    	     })	
				
	    	getDataList(1);
    
	    	function Initialization(){
				// 初始化数值
	    		is_finish = true;
	    		page_bol = true;
	    		selection();
	    		getDataList(1);
	    	}
	    	
	    	$("#searchKeyBtn").on("click",function(){
	    		getDataList(1);
	    	})
	    	
	    	//点击每个选项，在下面的已选方案中添加该选项
			var listTime;
			$(".conservative-choice li").on("click",function(){
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
						Initialization();//loadIFAList(self);
					}
					,100);
			});
	    	
	    	
	    	/**搜索条件取消点击
			 * author scshi
			 * date 20160821
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
			
			//执行清除方案点击操作
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
				$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input[type='hidden']").val($(this).attr("data-conversion"));
				$(this).parents(".proposal_xiala").toggleClass("xiala-show").find("input[type='text']").val($(this).html());
				e.stopPropagation(); 
				$(this).closest(".proposal_xiala").toggleClass("xiala-show");
				$scope.currencyName=$(this).html();
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
	    	
    }]);
		
		
		
});