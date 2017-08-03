
define(function(require) {
	'use strict';
	var $ = require('jquery');
		require('laydate');
		require('layui');
		require("scrollbar");
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
	})
	function selection(){
		var _thisLenght =  $(".selection_criteria").children().length
		
		if( _thisLenght != 1  ){
			$(".funds_title_selection").css('display','inline-block');
		}else{
			$(".funds_title_selection").css('display','none');
		}
	}	
    /**
	 * 基础货币下拉选择
	 */
	$('.portfolio-currency,.icon_xiala').click(function(){
		$('.funds_currency_xiala ul').css('z-index','999');
    	$('#currency-choice').toggle();
    	$('#currency-choice li').unbind('click');
    	$('#currency-choice li').click(function(){
    		$('#currency-choice').hide();
    		$('#portfolio-currency').val($(this).text());
    		$('#currency-choice').hide();
    		var fromCurrency = $('#portfolio-currency').attr('data-code');
    		var toCurrency = $(this).data('code');
    		conversion(fromCurrency, toCurrency);
    		$('.to-currency').text($(this).text());
    		$('#portfolio-currency').val($(this).text());
    		$('#portfolio-currency').attr('data-code', toCurrency);
    	});
    });
	$('.proposal_xiala').on('mouseleave',function(){
		$('#currency-choice').hide();
	});
	/**
	 * 基础货币获取
	 */
	function currencyType(){
		var type = 'currency_type';
		var url = base_root+'/console/sys/param/paramList.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{type:type},
			success:function(result){
				var currencys = result.result;
				if(typeof(currencys)!='undefined'&&currencys.length!=0){
					var liHtml = '';
					var defCode = $('#hidDefCurrency').val();
					var defCurrency = '';
					$.each(currencys,function(i,n){
						var name = n.name != null?n.name:'';
						var code = n.code != null?n.code:'';
						liHtml += '<li id="'+n.id+'" data-code="'+code+'">'
							+ name;
							+'</li>';
						if(defCode == code){
							defCurrency = name;
						}
					});
					$('#currency-choice').empty().append(liHtml);
					$('#portfolio-currency').val(defCurrency);
					$('#portfolio-currency').attr('data-code',defCode);
					$('.to-currency').each(function(){
						$(this).text(defCurrency);
					});
				}
			}
		});
	}
	/**
	 * 货币转换
	 */
	function conversion(fromCurrency,toCurrency){
		var conversions = [];
		var toConversions = [];
		$('.conversion-flag').each(function(){
			if($(this).is('input')||$(this).is('textarea')||$(this).is('select')){
				conversions.push($(this).val());
			}else{
				conversions.push($(this).text());
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
								if(n.indexOf(',') > -1){
									n = n.replace(/,/gm,'');
								}
								var args = '';
								if('JPY' == toCurrency){
									args = parseFloat(Number(n)*rate).toFixed(0);
								}else{
									args = parseFloat(Number(n)*rate).toFixed(2);
								}
								toConversions.push(args);
							}else{
								toConversions.push('');
							}
						});
						$('.conversion-flag').each(function(i,n){
							if($(this).is('input')||$(this).is('textarea')||$(this).is('select')){
								$(this).val(formatCurrency(toConversions[i]));	
							}else{
								$(this).text(formatCurrency(toConversions[i]));
							}
						});
					}
				}
			}
		});
	}
	currencyType(); // 获取基础货币
    var mybase = angular.module('mySearch', ['wmespage','truncate']);
	mybase.controller('Searchctrl', ['$scope', '$http', function($scope, $http) {
		var iPAGE = 1; //第N页数据
		var rows = 10; 
		$scope.dataList = '';
		$scope.finishStatus = '';
    	function getDataList(){
    		var keyWord = '';
    		if($scope.keyWord != '' && $scope.keyWord != null && typeof($scope.keyWord) != 'undefiend'){
    			keyWord = encodeURI($scope.keyWord);
    		}
    		var data = {
            	  rows:rows,  
            	  page:iPAGE, 
            	  currencyCode:$scope.toCurrency,
            	  filterPeriod:$scope.period,
            	  filterPeriodType:$scope.periodType,
            	  filterKeyWord:keyWord,
            	  filterStartTime:$scope.fromDate,
            	  filterEndTime:$scope.toDate,
            	  filterStatus:$scope.finishStatus,
            	  filterIfaFirmId:$scope.ifaFirmId
            };
    		//console.log(data);
    		var url = base_root + "/console/ifafirm/client/transRecordsJson.do?dateStr=" + new Date().getTime();
			$http({
              url: url,
              method: 'POST',
              headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
              params : data
         	 })
			.success(function(response){//console.log(response);
                if(response.total > 0){
        		  $scope.total = response.total;
        		  $scope.transactionConf.totalItems = response.total;
            	  $(".no_list_tips").hide();
      		 	  $scope.dataList = '';
                  $scope.dataList =  response.list;
                  $.each(response.list,function(i,n){
                	  if(n.toCurrency != null){
                		  $scope.toCurrency = n.toCurrency;
                		  return false;
                	  }
                  });
          		}else{
          		  $scope.transactionConf.totalItems = 0;
          		  $scope.total = 0;
 				  $scope.dataList = '';
 				  $(".no_list_tips").show();
          		}
                setTimeout(function(){
    		    	$('.funds_search_wrap').perfectScrollbar();
    		    },500);
    		    // 加载货币
    		    currencyType();
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
		//关键字搜索
		$scope.searchKeyBtn = function(){
			$scope.keyWord = $('#txtKeyWord').val();
			getDataList();
		};
		//期间过滤
		$(".funds_choice li").on("click",function(){
		    $(this).siblings().removeClass("fund_choice_active2").end();
		    if($(this).index()==0){
                $(this).addClass('fund_choice_active2');
            }
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
				if($(this).data('name') == 'Status'){
					$scope.finishStatus = $(this).data('value');
				}else if($(this).data('name') == 'Period Date'){
					$scope.period = $(this).data('value');
					$scope.periodType = $(this).data('type');
				}else if($(this).data('name') == 'FundHouse'){
					$scope.ifaFirmId = $(this).data('value');
				}
				$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
			}
			if($(this).hasClass('status_all')){
				$scope.finishStatus = '';
			}else if($(this).hasClass('period_all')){
				$scope.period = '';
				$scope.periodType = '';
				$scope.fromDate = "";
	        	$scope.toDate = "";
			}else if($(this).hasClass('ifafirm_all')){
				$scope.ifaFirmId = '';
				$('.selection_criteria li').each(function(){
					if($(this).data('name') == 'FundHouse'){
						$(this).remove();
					}
				});
			}
			getDataList();
		});
		//执行清除方案点击操作
		$(".funds_title_selection").on("click",function(){
			$(".selection_criteria li").remove();
			$(".fund_logo_active").removeClass("fund_logo_active");
			$(".fund_choice_active").removeClass("fund_choice_active");
			$(".funds_all").addClass("fund_choice_active");
			$scope.period = '';
			$scope.periodType = '';
			getDataList();
		});	
		$(document).on("click",".selection_delete",function(){
			if($(this).parent().data('name')=='Period Date'){
				$scope.period = '';
				$scope.periodType = '';
			}else if($(this).parent().data('name')=='Status'){
				$scope.finishStatus = '';
			}else if($(this).parent().data('name')=='FundHouse'){
				$scope.ifaFirmId = '';
			}
			$(this).closest('li').remove();
			getDataList();
		});	
		//指定时间过滤
		$(".recommend-date-ok").on("click",function(){
	        if($("#fromDate" ).val() != "" && $("#toDate").val() != ""){
	        	var selection_criterialenght = $(".selection_criteria li").length;
	            for(var i = 0; i < selection_criterialenght ;i++){
	                    if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
	                        $(".selection_criteria li").eq(i).remove();
	                    }
	            }
	            $(".funds_title_selection").before('<li data-value="' + $("#fromDate").val() + ' '+props['criteria.period.to']+' ' + $("#toDate").val()  + '" data-name="' + $(this).attr("data-name") + '">' + $("#fromDate").val() + ' '+props['criteria.period.to']+' ' + $("#toDate").val()  + '<span class="selection_delete"></span></li>')
	            $scope.fromDate = $('#fromDate').val();
				$scope.toDate = $('#toDate').val();
				$scope.period = '';
				$scope.periodType = '';
				getDataList();
	        }
	    });
		$(document).keypress(function(e) {  
	       if(e.which == 13) {
	    	   getDataList();
	       }  
	    });
	}]);
	
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
           choose: function(datas){} 
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
    function formatCurrency(num) {    
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
});