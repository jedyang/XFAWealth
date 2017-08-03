define(function(require) {
	var $ = require('jquery');
	require('pagination');
	require('layer');
	require("scrollbar");
	require('laydate');
	var selector =  require('ifaSelectUser');
	selector.init();
	require("interfaceCheckPwd")
	var Loading = require('loading');
	var angular = require('angular');
	var mybase = angular.module('portfolioTable', ['truncate']);
	sessionStorage.removeItem("clientdetailtab");
	
	//数据过滤参数
	var FILTER = (function(){
		var period = '',
			fromDate = '',
			toDate = '',
			keyWord = '',
			totalReturn = '',
			riskLevel = '',
			status='',
			page = 0,
			sort = 'r.lastUpdate',
			order = 'desc';
		return {
			period:period,
			keyWord:keyWord,
			fromDate:fromDate,
			toDate:toDate,
			totalReturn:totalReturn,
			riskLevel:riskLevel,
			status:status,
			page:page,
			sort:sort,
			order:order
		};
	})();
	
	
	//验证登录
	if(_checkList!=undefined && _checkList!=""){
	$(this).InitInterface({"accountlist":_checkList,"accounttype":_accountType,"isopen":"1" });
	}
	
	
	var oLoading = new Loading($(".strategies_list"));	
	mybase.controller('portfolioCtrl', ['$scope', '$http', function($scope, $http) {
	
		var iPAGE = 1; //第N页数据
		//初始化数据
		$scope.dataList = '';
		$scope.currency=$("#currency").val();
		$scope.defColor=$("#displayColor").val();
		$scope.dateFormat=$("#dateFormat").val();
		$scope.currencyName=$("#currencyName").val();
		$scope.decimals=Number($("#decimals").val());
		
		// 监听视图渲染是否结束
    	$scope.checkLast = function($last){
			if($last){

				setTimeout(function(){
					
				},100)
				
			}
		}
    	
		// 正常拿数据	 
		function getDataList() {
			//$(".strategies_List").css("display","none");
			oLoading.show();
			$(".funds_search_information").show();
			var data="&period="+FILTER.period+"&fromDate="+FILTER.fromDate+"&toDate="+FILTER.toDate+"&riskLevel="+FILTER.riskLevel+
			         "&totalReturn="+FILTER.totalReturn+"&keyWord="+FILTER.keyWord+"&page="+Number(FILTER.page+1)+"&sort="+FILTER.sort+
			         "&order="+FILTER.order+"&status="+FILTER.status;
			$http({
				url: base_root+'/front/crm/proposal/getPortfolio.do?dateStr=' + new Date().getTime(),
				method: 'POST',
				data: data,
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
				},
			})
			.success(function(response) {
				
				oLoading.hide();
				var table = response.rows,
				total = response.total;
				$scope.dataList =table;
				$('#portfolioTotal').text(total);
				if(total!=0){
					$(".no_list_tips").hide();
					$("#pagination").show();
					$("#pagination").pagination(total, {
						callback : pageselectCallback,
						numDetail : '',
						items_per_page : 9,
						num_display_entries : 4,
						current_page : FILTER.page,
						num_edge_entries : 2
					});
					function pageselectCallback(page_id, jq) {
						FILTER.page = page_id;
						getDataList();
					}
				}else{
					$("#pagination").hide();
					$('.strategies_List tbody tr:gt(0)').empty();
					$('.no_list_tips').show();
				}
				
				////console.log(table);
			});
		}
		
		getDataList();
		
		
		$(".recommend-date-ok").on("click",function(){
	        if($("#fromDate" ).val() != "" && $("#toDate").val() != ""){
	        	var selection_criterialenght = $(".selection_criteria li").length;
	            for(var i = 0; i < selection_criterialenght ;i++){
	                    if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
	                        $(".selection_criteria li").eq(i).remove();
	                    }
	            }
	            $(".funds_title_selection").before('<li data-value="' + $("#fromDate").val() + ' to ' + $("#toDate").val()  + '" data-name="' + $(this).attr("data-name") + '">' + $("#fromDate").val() + ' to ' + $("#toDate").val()  + '<span class="selection_delete"></span></li>')
	            Initialization();
	            FILTER.fromDate = $('#fromDate').val();
				FILTER.toDate = $('#toDate').val();
				FILTER.period = "";
				initPortfolio();
	        }else{
	        	layer.msg(langMutilForJs['msg.selectDate'])
	        }

	    });
	    if($('.fund_choice_active').index()==0){
				$('.fund_choice_active').addClass('fund_choice_active2');
			}else{
				$('.fund_choice_active').removeClass('fund_choice_active2');
			};
		$(".funds_choice li").on("click",function(){
			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
	        if($(this).hasClass("recommend-date-choice") && $(this).hasClass("fund_choice_active")){
	            $(this).parents(".funds_choice").find(".recommend-other-wrap").addClass("recommend-other-block");
	            return;
	        }else if($(this).parents(".funds_choice").find(".recommend-other-wrap").length > 0){
	        	FILTER.fromDate = '';
				FILTER.toDate = '';
				FILTER.period = $(this).parents(".funds_choice").find(".fund_choice_active").data('value');
	            $(this).parents(".funds_choice").find(".recommend-other-wrap").removeClass("recommend-other-block");
	        }
			var selection_criterialenght = $(".selection_criteria li").length;
			for(var i = 0; i < selection_criterialenght ;i++){
					if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
						$(".selection_criteria li").eq(i).remove();
					}
			}
			
			if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
				$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>');
			};
			if($(this).index()==0){
				$(this).closest('ul').find('.funds_all').addClass('fund_choice_active2');
			}else{
				$(this).closest('ul').find('.funds_all').removeClass('fund_choice_active2');
			};
			Initialization();

			//过滤 period、riskLevel
			$('.selection_criteria >li').each(function(){
				if('riskLevel' == $(this).data('name')){
					FILTER.riskLevel = $(this).data('value');
				}else if('totalReturn' == $(this).data('name')){
					FILTER.totalReturn = $(this).data('value');
				}else if('status' == $(this).data('name')){
					FILTER.status = $(this).data('value');
				}
			});
			initPortfolio();
		});
		//搜索全部
		$('.funds_all').click(function(){
			if('period' == $(this).data('name')){
				$('#fromDate').val('');
				$('#toDate').val('');
				FILTER.fromDate = '';
				FILTER.toDate = '';
				FILTER.period = '';
				initPortfolio();
			}else if('riskLevel' == $(this).data('name')){
				FILTER.riskLevel = '';
				initPortfolio();
			}else if('totalReturn' == $(this).data('name')){
				FILTER.totalReturn = '';
				initPortfolio();
			}else if('status' == $(this).data('name')){
				FILTER.status = '';
				initPortfolio();
			}
			
		});
		//执行清除方案点击操作
		$(".funds_title_selection").on("click",function(){
			$(".selection_criteria li").remove();
			$(".fund_logo_active").removeClass("fund_logo_active");
			$(".fund_choice_active").removeClass("fund_choice_active");
			$(".funds_all").addClass("fund_choice_active");
			$(".funds_all").addClass("fund_choice_active2");
			$('#fromDate').val('');
			$('#toDate').val('');
			FILTER.fromDate = '';
			FILTER.toDate = '';
			FILTER.period = '';
			FILTER.riskLevel = '';
			FILTER.totalReturn = '';
			FILTER.status = '';
			initPortfolio();
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
		 */
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
		 */
		function Initialization(){
			selection();
		}
		
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
		function initPortfolio(){
			FILTER.page = 0;
			getDataList();
		}
		
		$('.arrow_top_total_returm').click(function(){
			filterOrder(this,'top_active');
			FILTER.sort = 'r.totalReturnRate';
			FILTER.order = 'asc';
			initPortfolio();
		});
		$('.arrow_down_total_returm').click(function(){
			filterOrder(this,'down_active');
			FILTER.sort = 'r.totalReturnRate';
			FILTER.order = 'desc';
			initPortfolio();
		});
		$('.arrow_top_last_modified').click(function(){
			filterOrder(this,'top_active');
			FILTER.sort = 'r.lastUpdate';
			FILTER.order = 'asc';
			initPortfolio();
		});
		$('.arrow_down_last_modified').click(function(){
			filterOrder(this,'down_active');
			FILTER.sort = 'r.lastUpdate';
			FILTER.order = 'desc';
			initPortfolio();
		});
		//字段排序样式切换
		function filterOrder(selt,orderClass){
			if($(selt).hasClass(orderClass)){
				$('.arrow_down').removeClass('down_active');
				$('.arrow_top').removeClass('top_active');
			}else{
				$('.arrow_down').removeClass('down_active');
				$('.arrow_top').removeClass('top_active');
				$(selt).addClass(orderClass);
			}
		}
		//关键字
		$('#searchKeyBtn').click(function(){
			FILTER.keyWord = $('#txtKeyWord').val();
			initPortfolio();
		});
		$("#txtKeyWord").keyup(function(event){
	      	 if(event.keyCode == 13){
	        	document.getElementById('searchKeyBtn').click()
	        }
	    });	
		
	}]);
	
	
	
	
	/**
	 * 绑定portfolio数据
	 */
	//bindPortfolio();
	/*function bindPortfolio(){
		$.ajax({
			type:'post',
			data:
				{
					period:FILTER.period,
					fromDate:FILTER.fromDate,
					toDate:FILTER.toDate,
					riskLevel:FILTER.riskLevel,
					totalReturn:FILTER.totalReturn,
					keyWord:FILTER.keyWord,
					page:FILTER.page+1,
					sort:FILTER.sort,
					order:FILTER.order
				},
			url: base_root+'/front/crm/proposal/getPortfolio.do?dateStr=' + new Date().getTime(),
			success:function(result){
				////console.log(result);
					var table = result.rows,
						total = result.total,
						html = '',
						returnRateHTML = '';
					$('#portfolioTotal').text(total);
					if(table != null){
						$.each(table,function(i,n){
							var portfolioName = n.portfolioName == null? '' : n.portfolioName,
								returnRate = n == null? '' : n.totalReturnRate,
								totalAssets = n == null? '' : n.totalAssets,
								defDisplayColor = n.creator == null? '' : n.creator.defDisplayColor;
							if('' != returnRate && returnRate != null){
								returnRate = parseFloat(returnRate)+'%';
							}
							if(defDisplayColor != null && defDisplayColor == '2'){
								returnRateHTML = '<span class="funds_search_positive">'+returnRate+'</span>';
							}else{
								returnRateHTML = '<span class="funds_search_negative">'+returnRate+'</span>';
							}
							//totalReturn = formatCurrency(totalReturn);
							var aipFlag = n.aipFlag == null? '' : n.aipFlag;
							if('0' == aipFlag){
								aipFlag = 'No';
							}else if('1' == aipFlag){
								aipFlag = 'Yes';
							}
							var nickName = n.clientName == null? '' : n.clientName,
								lastUpdate = n.lastUpdate == null? '' : n.lastUpdate,
								baseCurrency = n.baseCurrency == null? '' : n.baseCurrency;
							
							html += 
								'<tr>'
							+'<td class="tdleft funds_tables_fnames">'
							+'<a href="'+base_root+'/front/strategy/info/conservativePortfolio.do?id='+n.id+'" target="_self">'
							+ portfolioName
							+'</a>'
							+'</td>'
							+'<td class="funds_search_tdcenter">'
							+'<a href="'+base_root+'/front/crm/pipeline/clientDetail.do?customerId='+n.clientId+'" target="_self">'+ nickName
							+'</a></td>' 
							+'<td class="funds_search_tdcenter">'
							+'<div style="margin:0 auto;width:120px;height:90px;" id="g'+n.id+'"></div>'
							+'</td>'
							+'<td class="funds_search_tdcenter">';
							if(totalAssets != ''){
								html += totalAssets
								+'<span style="margin-left:10px;">'
								+ baseCurrency
								+'</apan>';
							}
							html +=
							'</td>'
							+'<td class="funds_search_tdcenter">'
							+ returnRateHTML
							+'</td>'
							+'<td class="funds_search_tdcenter">'
							+ aipFlag
							+'</td>'
							+'<td class="funds_search_tdcenter">'
							+ lastUpdate
							+'</td>' 
							+'</tr>';
						});
						
						$('.no_list_tips').hide();
						$('.strategies_List tbody tr:gt(0)').empty();
						$('.strategies_List tbody').append(html);
						//riskLevel 显示
						
						$.each(table,function(i,n){
							var riskLevel = n.riskLevel == null? '' : n.riskLevel;
							if('' != riskLevel){
								new JustGage({
						          id: "g"+n.id,
						          value: riskLevel,
						          min: 1,
						          max: 7,
						          title: "",
						          label: "Risk Level"
						        });
							}
						});
						
					}
					if(total!=0){
						$("#pagination").show();
						$("#pagination").pagination(total, {
							callback : pageselectCallback,
							numDetail : '',
							items_per_page : 9,
							num_display_entries : 4,
							current_page : FILTER.page,
							num_edge_entries : 2
						});
						function pageselectCallback(page_id, jq) {
							FILTER.page = page_id;
							bindPortfolio();
						}
					}else{
						$("#pagination").hide();
						$('.strategies_List tbody tr:gt(0)').empty();
						$('.no_list_tips').show();
					}
			}
		});
	}*/
	
	
	
	//Inv.Amount、Total Returm、Last Modified 排序点击事件
	/*$('.arrow_top_total_asset').click(function(){
		filterOrder(this,'top_active');
		FILTER.sort = 'r.totalAsset';
		FILTER.order = 'asc';
		bindPortfolio();
	});
	$('.arrow_down_total_asset').click(function(){
		filterOrder(this,'down_active');
		FILTER.sort = 'r.totalAsset';
		FILTER.order = 'desc';
		bindPortfolio();
	});*/
	
	//金额格式化
	function formatCurrency(num) { 
		if(!num)return '-';
	    num = num.toString().replace(/\$|\,/g,'');    
	    if(isNaN(num))    
	    num = "";    
	    var sign = (num == (num = Math.abs(num)));    
	    num = Math.floor(num*100+0.50000000001);    
	    var cents = num%100;    
	    num = Math.floor(num/100).toString();    
	    if(cents<10)    
	    cents = "0" + cents;    
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
	    num = num.substring(0,num.length-(4*i+3))+','+    
	    num.substring(num.length-(4*i+3));    
	    return (((sign)?'':'-') + num + '.' + cents);    
	} 
	
	//时间选择控件
	$('#fromDate').click(function(){
        var max = '';
        if(!$('#toDate').val()){
            max = laydate.now();
        }else{
            max = $('#toDate').val();
        }
        laydate({
               istime: false,
               max:max,
               elem: '#fromDate',
               format: 'YYYY-MM-DD',
               istoday: true,
               choose: function(datas){ 
               } 
        });
    });
    $('#toDate').click(function(){
        var min = '';
        if(!$('#fromDate').val()){
            min = laydate.now();
        }else{
            min = $('#fromDate').val();
        }
        laydate({
            istime: false,
            min:min,
            elem: '#toDate',
            format: 'YYYY-MM-DD'
        });
    });
    
    $("#txtKeyWord").on("click",function(){
    	selector.create(1,'client','memberId','txtKeyWord');
		$(".selectnamelistbox").show();
    });
    
    $(".wmes-menu-hide").on("click",function(){
		$(this).toggleClass("wmes-menu-hideac");
		if( $(this).hasClass("wmes-menu-hideac")) {
			$(".client-more-screen-wrap").stop().animate({ 
				height: "100%"
			}, 300 );
			$(".client-more-screen-wrap").css({'margin-top':'20px'});
			$('.funds_list_selected').removeClass('ifa-more-ico-hidden');
		}else{
			$(".client-more-screen-wrap").stop().animate({ 
				height: "0px",margin:'0px'
			}, 300 );
			$('.funds_list_selected').addClass('ifa-more-ico-hidden');
		}
	});
});	