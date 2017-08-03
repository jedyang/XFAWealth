define(function(require) {
	
	var $ = require('jquery'),
	angular = require('angular');	
	require('layer');
	require('laydate');
	var mybase = angular.module('ifaTable', ['wmespage']);
	var searchData = null;
	
    mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
    	var iPAGE = 1; //第N页数据
		//初始化数据
		$scope.dataList = '';
		// 数字调用
		$scope.counter = Array;
		//固定每次拿10条数据
		var rows = 10; 
		$scope.status = 1;
		
    	function getDataList(){
    		var url = base_root + "/front/ifa/myInsight/listJson.do",
    			 data =  "rows="+ rows +"&page=" + iPAGE +"&"+searchData+"&status=" + $scope.status;
    			$http({
                  url: url,
                  method: 'POST',
                  data : data,
                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
             	 })
    			.success(function(response){
                        if(response.list.length > 0){
                          $scope.insightConf.totalItems = response.total;//设置总数
                    	  $(".dataListTr").show();
                    	  $(".no_list_tips").hide();
                      		 if( iPAGE === 1 ){
                      		 	 $scope.dataList = '';
                                 $scope.dataList =  response.list;
                              }else{
                                 $scope.dataList = $scope.dataList.concat(response.list);
                              }
                  		}else{
                 			 if( iPAGE === 1 ){
                 				 $(".dataListTr").hide();
                 				 $(".no_list_tips").show();
                  			 }
                  		}
		    			iPAGE++;
                });
    	}
        $scope.insightConf = {
            itemsPerPage:rows,
            onChange: function(){
                bindInsight($scope.insightConf.currentPage);
            }
        };
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
            }else{
                layer.msg("请选择日期")
            }
           
        });
    	var listTime;
    	$(".funds_choice li").on("click",function(){
    		clearTimeout(listTime)
    		if($(this).parent().hasClass("funds_logo_b")){
    			return;
    		}
            $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
            if($(this).hasClass("recommend-date-choice") && $(this).hasClass("fund_choice_active")){
                $(this).parents(".funds_choice").find(".recommend-other-wrap").addClass("recommend-other-block");
                return;
            }else{
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
    			$('.show-all').remove();
    			$(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
    		}
    		if($(this).data('name')=='IssuedDate'){
    			$('#fromDate').val('');
    			$('#hidFromDate').val('');
    			$('#toDate').val('');
    			$('#hidToDate').val('');
    		}
    		// show all
    		var flag = true; 
    		$('.funds_all').each(function(){
    			if(!$(this).hasClass('fund_choice_active')){
    				flag = false;
    			}
    		});
    		if(flag && $('.show-all').length == 0){
    			$('.selection_criteria').append('<li class="fund_choice_active show-all" style="margin:0 10px">Show All</li>');
    		}
    		
    		// 解决重复请求的问题
    		var self = this;
    		listTime=setTimeout(function(){
    			searchInsightList(self);
    		}
    		,100);
    	});
    	
    	//排序点击
    	$(".recommend-switch-tab li").on("click",function(){
    		$(".recommend-switch-tab li").removeClass("recommend-sort-active")
    		$(this).addClass("recommend-sort-active");
    		var currentSort = $(this).attr("currentSort");
    		var removeSort = $(this).attr("removeSort");
    		
    		if( $(this).find(".arrow_top").hasClass("top_active") ){
    			$('.arrow_down').removeClass("down_active");
    			$('.arrow_top').removeClass("top_active");
    			$(this).find(".arrow_down").addClass("down_active");
    			$("#"+removeSort).val("");
    			$("#"+currentSort).val("desc");
    		}else if( $(this).find(".arrow_down").hasClass("down_active") ){
    			$('.arrow_down').removeClass("down_active");
    			$('.arrow_top').removeClass("top_active");
    			$(this).find(".arrow_top").addClass("top_active");
    			$("#"+removeSort).val("");
    			$("#"+currentSort).val("asc");
    		}else{
    			$('.arrow_down').removeClass("down_active");
    			$('.arrow_top').removeClass("top_active");
    			$(this).find(".arrow_down").addClass("down_active");
    			$("#"+removeSort).val("");
    			$("#"+currentSort).val("asc");
    		}
    		
    		Initialization();
    	});
    	
    	$(".icon_search").on("click",function(){
			var fundName = $("#searchKeyWord").val();
			document.getElementById("keyWord").value=fundName
			searchData = $("#paramsForm").serialize();
		 	Initialization();
		})
		
		$("#searchKeyWord").keyup(function(event){
       	 if(event.keyCode == 13){
	        	document.getElementById('searchKeyBtn').click()
	        }
	    });		
    	
    	$(".selection_criteria").on("click",".selection_delete",function(){
    		var selectedCount = $(this).closest('ul').find('li').length;    		
    		if(1 == selectedCount){
    			$(this).closest('ul').append('<li class="fund_choice_active show-all" style="margin:0 10px">Show All</li>');
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
				prefCount++
			})
			if(prefCount==0)$("#per_all").addClass("per_active");
		});
    	
    	$("#funds_logo_choice li").on("mousemove",function(){
    		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
    		var this_letter= $(this).attr("data-letter");
    		if(this_letter != $('#geoAllocation').val()){
    			$('#geoAllocation').val(this_letter);
				Initialization();
			}
    		if(!$(this).hasClass('funds_all')){
    			$('.show-all').remove();
    		}
    		// show all
    		var flag = true; 
    		$('.funds_all').each(function(){
    			if(!$(this).hasClass('fund_choice_active')){
    				flag = false;
    			}
    		});
    		if(flag && $('.show-all').length == 0){
    			$('.selection_criteria').append('<li class="fund_choice_active show-all" style="margin:0 10px">Show All</li>');
    		}
    	});
    	
    	function searchInsightList(searchLi){
    		var dataName = $(searchLi).attr("data-name");
    		var dataValue = $(searchLi).attr("data-value");
    		
    		if(!dataValue)dataValue="";
    		
    		if("IssuedDate"==dataName){
    			$("#issuedDate").val(dataValue);
    		}
    		
    		if("RelateSector"==dataName){
    			$("#sector").val(dataValue);
    		}
		 	Initialization();
    	}
    	
    	function Initialization(){
    		// 初始化数值
    		iPAGE = 1; //第N页数据
    		searchData = $("#paramsForm").serialize();
    		getDataList();
            var _thisLenght =  $(".selection_criteria").children().length
                
                if( _thisLenght != 1  ){
                    $(".funds_title_selection").css('display','inline-block');
                }else{
                    $(".funds_title_selection").css('display','none');
                }
    	}
    	

        $(".funds_title_selection").on("click",function(){
            $(".selection_criteria li").remove();

            $(".fund_logo_active").removeClass("fund_logo_active");


            $(".fund_choice_active").removeClass("fund_choice_active");
            $(".funds_all").addClass("fund_choice_active");
            $("#listForm").find("input").val("");
            Searchdata = $("#listForm").serialize();
            Initialization();
        });
    	$(".ifa_list").on("click","#headdown",function(){
    		var insightId = $(this).attr("insightId");
    		if(!!insightId){
        		$.ajax({
        			url:base_root+"/front/ifa/myInsight/headStatusChange.do",
        			data:{"insightId":insightId,"headStatus":0},
        			type:"get",
        			dataType:"json",
        			success:function(data){
        				//layer.msg(data.msg, { icon: 0, time: 2000 });
        				Initialization();
        			}
        		})
        	}
    	});

    	//点赞、踩
    	$(".ifa_list").on("click","#recommend-news-like",function(){
    		var selt = this,
    			insightId = $(selt).data("insight"),
    			type = $(selt).data('type');
    		
    		if((window.sessionStorage.getItem(insightId+1) == 1 &&
    				type == 1) || (window.sessionStorage.getItem(insightId+0) == 1 &&
    	    				type == 0)){
				layer.msg("Do not repeat clicks!",{icon : 0,time : 2000});
				return false;
			}else if(!!insightId){
    			$.ajax({
    				url:base_root+"/front/ifa/myInsight/insightUpCounter.do",
    				data:{insightId:insightId,type:type},
    				type:"post",
    				success:function(result){
    					if(result.flag){
    						//Initialization();
    						var counter = '';
    						if(type == 1){
    							counter = result.insightInfo.upCounter;
    							window.sessionStorage.setItem(insightId+1,1);
    						}else{
    							counter = result.insightInfo.downCounter;
    							window.sessionStorage.setItem(insightId+0,1);
    						}
    						$(selt).find('.recommend-news-like-num').text(counter);
    					}else{
    						layer.msg('The system is busy!',{icon:2,time:2000});
    					}
    				}
    			})
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

    $(document).on("click",".recommend-news-title",function(){
       $(".ifa-article-space").show();
       $(".ifa-article-zhe").show();
    });
    $("#space-article-close").on("click",function(){
         $(".ifa-article-space").hide();
         $(".ifa-article-zhe").hide();
    });
});