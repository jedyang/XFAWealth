
define(function(require) {
	'use strict';
	var $ = require('jquery');
		require('laydate');
		require('layui');
	var angular = require('angular');

	$(document).on('click','.recommend-ico1',function(){
		$('.RecommendNewscon').css('display','block');
	});
	
	$('.setting-ok-bn').on('click',function(){
		$('.RecommendNewscon').css('display','none');
	});
	
	$('.craetbnCancel').on('click',function(){
		$('.RecommendNewscon').css('display','none');
	});
	
	$('.RecommendNewscon input').change(function(e){
		if($("#letmespecify").prop("checked")==true){
			$('.pushToSettingmenu').css('visibility','visible');
		}else{
			$('.pushToSettingmenu').css('visibility','hidden');
		}
	});
	
	function selection(){
		var _thisLenght =  $(".selection_criteria").children().length
		
		if( _thisLenght != 1  ){
			$(".funds_title_selection").css('display','inline-block');
		}else{
			$(".funds_title_selection").css('display','none');
		}
	}	
	 // 下拉
    $(".funds_currency_xiala").on("click",function(){
    	$('.funds_currency_xiala ul').css('z-index','999');
    	$('.conversion_select').toggle();
    });
    
    $('.funds_currency_xiala').on('mouseleave',function(){
    	$('.conversion_select').hide();
    });
    
    var mybase = angular.module('transactionRecord', ['wmespage']);
	mybase.controller('transactionRecordCtrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1, //第N页数据
			rows = 10; 
		$scope.dataList = '';
		$scope.finishStatus = '-1,0,1,3';
    	function getDataList(){
    		var keyWord = '';
    		if($scope.keyWord != '' && $scope.keyWord != null && typeof($scope.keyWord) != 'undefiend'){
    			keyWord = encodeURI($scope.keyWord);
    		}
    		$scope.toCurrency = $(document).find('#toCurrency').attr('data-code');
    		var url = base_root + "/front/tradeRecord/listForJson.do?dateStr=" + new Date().getTime();
    			$http({
                  url: url,
                  method: 'POST',
                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                  params : {
                	  rows:rows,  
                	  page:iPAGE, 
                	  period:$scope.period,
                	  periodType:$scope.periodType,
                	  keyWord:keyWord,
                	  fromDate:$scope.fromDate,
                	  toDate:$scope.toDate,
                	  toCurrency:$scope.toCurrency,
                	  finishStatus:$scope.finishStatus
                  }
             	 })
    			.success(function(response){
                        if(response.total > 0){
                          $scope.orderplanConf.totalItems = response.total;//设置总数
                    	  $(".no_list_tips").hide();
              		 	  $scope.dataList = '';
                          $scope.dataList =  response.list;
                  		}else{
                  		  $scope.orderplanConf.totalItems = 0;
         				  $scope.dataList = '';
         				  $(".no_list_tips").show();
                  		}
                });
    	}
		getDataList();
		/**
		 * get aip task
		 */
		function getAipTask(){
			var keyWord = '';
			if($scope.keyWord != '' && $scope.keyWord != null && typeof($scope.keyWord) != 'undefiend'){
				keyWord = encodeURI($scope.keyWord);
			}
			$scope.toCurrency = $(document).find('#toCurrency').attr('data-code');
			var url = base_root + "/front/tradeRecord/getAipTask.do?dateStr=" + new Date().getTime();
			$http({
				url: url,
				method: 'POST',
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
				params : {
					rows:rows,  
					page:iPAGE, 
					period:$scope.period,
					periodType:$scope.periodType,
					keyWord:keyWord,
					fromDate:$scope.fromDate,
					toDate:$scope.toDate,
					toCurrency:$scope.toCurrency
				}
			})
			.success(function(response){
				if(response.total > 0){
					$scope.orderplanConf.totalItems = response.total;//设置总数
					$(".no_list_tips").hide();
					$scope.dataList = '';
					$scope.dataList =  response.list;
				}else{
					$scope.orderplanConf.totalItems = 0;
					$scope.dataList = '';
					$(".no_list_tips").show();
				}
			});
		}
		//绑定【页码】跳转事件
		$scope.orderplanConf = {
            itemsPerPage:rows,
            onChange: function(){
            	iPAGE = $scope.orderplanConf.currentPage;
            	if('aip' == $('.funds_viewList_tab .now').data('value')){
					getAipTask();
				}else{
					getDataList();
				}
            }
        };
		//tab 切换
		$(".funds_viewList_tab li").on("click",function(){
			$(this).siblings().removeClass("now").end().addClass("now");
			if('aip' == $(this).data('value')){
				$('.strategies_list').hide();
				$('.aip_task_list').show();
				iPAGE = 1;
				getAipTask();
			}else{
				$('.aip_task_list').hide();
				$('.strategies_list').show();
				$scope.finishStatus = $(this).data('value');
				iPAGE = 1;
				getDataList();
			}
		});
		//关键字搜索
		$scope.searchKeyBtn = function(){
			$scope.keyWord = $('#fundName').val();
			if('aip' == $('.funds_viewList_tab .now').data('value')){
				getAipTask();
			}else{
				getDataList();
			}
		};
		
		$('.funds_all').addClass('fund_choice_active2');
		//期间过滤
		$(".funds_choice li").on("click",function(){
			if($(this).parent().hasClass("funds_logo_b")){
				return;
			}
			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
	        if($(this).hasClass("recommend-date-choice") && $(this).hasClass("fund_choice_active")){
	            $(this).parents(".funds_choice").find(".recommend-other-wrap").addClass("recommend-other-block");
	            return;
	        }else if($(this).parents(".funds_choice").find(".recommend-other-wrap").length > 0){
	        	$scope.fromDate = "";
	        	$scope.toDate = "";
	            $(this).parents(".funds_choice").find(".recommend-other-wrap").removeClass("recommend-other-block");
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
			if('aip' == $('.funds_viewList_tab .now').data('value')){
				getAipTask();
			}else{
				getDataList();
			}
			if($(this).closest('ul').find('.fund_choice_active').index()==0){
				$(this).addClass('fund_choice_active2');
			}else{
				$(this).closest('ul').find('li').removeClass('fund_choice_active2');
			};
		});
		//执行清除方案点击操作
		$(".funds_title_selection").on("click",function(){
			$(".selection_criteria li").remove();
			$(".fund_logo_active").removeClass("fund_logo_active");
			$(".fund_choice_active").removeClass("fund_choice_active");
			$(".funds_all").addClass("fund_choice_active");
			$scope.period = '';
			$scope.periodType = '';
			if('aip' == $('.funds_viewList_tab .now').data('value')){
				getAipTask();
			}else{
				getDataList();
			}
		});	
		$(".selection_criteria").on("click",".selection_delete",function(){
			if($(this).parent().data('name')=='Period Date'){
				$scope.period = '';
				$scope.periodType = '';
			}
			if('aip' == $('.funds_viewList_tab .now').data('value')){
				getAipTask();
			}else{
				getDataList();
			}
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
				prefCount++;
			});
			if(prefCount==0)$("#per_all").addClass("per_active");
		});	
		//指定时间过滤
		$(".recommend-date-ok").on("click",function(){
			if($("#fromDate" ).val() != "" && $("#toDate").val() != ""){
                $(".funds_title_selection").before('<li data-value="' + $("#fromDate").val() + ' to ' + $("#toDate").val()  + '" data-name="' + $(this).attr("data-name") + '">' + $("#fromDate").val() + ' to ' + $("#toDate").val()  + '<span class="selection_delete"></span></li>')
                var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght ;i++){
						if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
							$(".selection_criteria li").eq(i).remove();
						}
				}
				$scope.fromDate = $('#fromDate').val();
				$scope.toDate = $('#toDate').val();
				$scope.period = '';
				$scope.periodType = '';
				if('aip' == $('.funds_viewList_tab .now').data('value')){
					getAipTask();
				}else{
					getDataList();
				}
            }else{
            }
			
		});
	    //货币转换
		function conversion(fromCurrency,toCurrency){
			var conversions = [];
			var toConversions = [];
			$('.conversion-flag').each(function(){
				if($(this).is('input')||$(this).is('textarea')||$(this).is('select')){
					var value = $(this).val();
					value = value.replace(/,/g,'');
					conversions.push(value);
				}else{
					var value = $(this).text();
					value = value.replace(/,/g,'');
					conversions.push(value);
				}
			});
			var url = base_root+'/front/fund/info/getExchangeRate.do?dateStr=' + new Date().getTime();
			$.ajax({
				type:'post',
				url:url,
				data:{fromCurrency:fromCurrency,toCurrency:toCurrency},
				success:function(result){
					if(result.flag){
						var rate = result.exchangeRate != null ? result.exchangeRate.rate : '';
						if(rate != ''){
							$.each(conversions,function(i,n){
								if(typeof(n) != 'undefined' && n != ''){
									var args = '';
									if('JPY' == toCurrency){
										args = parseFloat(n*rate).toFixed(0);
									}else{
										args = parseFloat(n*rate).toFixed(2);
									}
									toConversions.push(args);
								}else{
									toConversions.push('');
								}
							});
							$('.conversion-flag').each(function(i,n){
								if($(this).is('input')||$(this).is('textarea')||$(this).is('select')){
									$(this).val(toConversions[i]);	
								}else{
									$(this).text(toConversions[i]);
								}
							});
						}
					}
				}
			});
		}
	    
		//获取基础货币
		function currencyType(){
			var type = 'currency_type';
			var url = base_root+'/console/sys/param/paramList.do?dateStr=' + new Date().getTime();
			$http({
              url: url,
              method: 'POST',
              params : {
            	  type:type
              }
         	 })
			.success(function(result){
				$scope.currencys = result.result;
				$.each(result.result,function(i,n){
					if(n.code == $scope.toCurrency){
						$('#toCurrency').attr('data-code',n.code);
						$('#toCurrency').val(n.name);
						return false;
					}
				});
            });
		}
		currencyType();
		//货币选择
		$scope.conversionSelect = function(){
			$('.conversion_select').hide();
    		var fromCurrency = $('#toCurrency').attr('data-code');
    		var toCurrency = $(this)[0].currency.code;
    		conversion(fromCurrency,toCurrency);
    		$('.to-currency').text($(this)[0].currency.name);
			$('#toCurrency').attr('data-code',$(this)[0].currency.code);
			$('#toCurrency').val($(this)[0].currency.name);
			$scope.toCurrency = $(this)[0].currency.code;
			$('.conversion_select').toggle();
			//getDataList();
		}; 
		
		 /**
	     * delete Plan
	     */
	    $(document).on('click','.action-delete',function(){
	    	var self = this;
	    	layer.confirm(props['order.plan.list.alert.delete.plan'], {
	 			  title:props['global.info'],
				  btn: [props['global.confirm'],props['global.cancel']] //按钮
			},function(index){
			   layer.close(index);
			   deletePlan(self);
			});
	    });
	    function deletePlan(self){
	    	var id = $(self).closest('tr').attr('id');
	    	var url = base_root + '/front/tradeRecord/delPlanById.do?d=' + Math.random(); 
	     	$.ajax({
	    		type:'post',
	    		url:url,
	    		data:{id:id},
	    		success:function(re){
	    			if(re.flag){
	    				layer.msg(props['global.delete.success'],{icon:2});
	    			}else{
	    				layer.msg(props['global.delete.failed'],{icon:1});
	    			}
	    			getDataList();
	    		},
	    		error:function(){
	    			layer.msg(props['global.delete.failed'],{icon:1});
	    			getDataList();
	    		}
	    	});
	    }
	}]);

	mybase.filter('formatCurrency', function () {
    	return function (num,suffix) { 
    		if (!num) return '';  		
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
    	    var value = (((sign)?'':'-') + num + '.' + cents);
    	    if(suffix){value = value+suffix;}
    	    return value;
    	};
    });
	
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
	
	$("#fundName").keyup(function(event){
      	if(event.keyCode == 13){
	        document.getElementById('searchKeyBtn').click()
	    }
	});	
});