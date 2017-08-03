 define(function(require) {
	
	var $ = require('jquery'),
	angular = require('angular');	
	require('layer');
	require('laydate');
	
	var mybase = angular.module('ifaTable', ['truncate']);
	var searchData = null;
	var memberId = $('#memberId').val();
	
    mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
    	var iPAGE = 1; //第N页数据
		//初始化数据
		$scope.dataList = '';
		// 数字调用
		$scope.counter = Array;
		//固定每次拿10条数据
		var rows = 10; 
		$scope.status = 1;
		// 正常拿数据	 
		
    	function getDataList(){
    		var url = base_root + "/front/investor/myInsight/listJson.do",
    			 data =  "rows=" + rows 
    			 	+ "&page=" + iPAGE 
    			 	+ "&" + searchData
    			 	+ "&status=" + $scope.status
    			 	+ "&memberId=" + memberId;
    			$http({
                  url: url,
                  method: 'POST',
                  data : data,
                  params : {
                	  allocation : encodeURI($("#allocation").val()),
                	  sector : encodeURI($("#sector").val())
                  },
                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
             	 })
    			.success(function(response){ //console.log(response.rows);
    			if(response.rows.length > 0){
              	  $(".dataListTr").show();
              	  $(".no_list_tips").hide();
                		 if( iPAGE === 1 ){
                		 	 $scope.dataList = '';
                           $scope.dataList =  response.rows;
                        }else{
                      	 $scope.dataList = '';
                      	 $scope.dataList =  response.rows;
                           //$scope.dataList = $scope.dataList.concat(response.list);
                        }
            		}else{
           			 if( iPAGE === 1 ){
           				 $(".dataListTr").hide();
           				 $(".no_list_tips").show();
            			 }
            		}
	    			// 总记录数
	    			$scope.datatotal = response.total;
	    			// 总页数
	    			$scope.pages = Math.ceil(response.total/rows);
	    			var sumPage = $scope.pages;
	    			// 当前页
	    			if (iPAGE<1) iPAGE=1;
	    			if (iPAGE>sumPage) iPAGE=sumPage;
	    			//分页显示
	    			var ListPage = [];
	    			//页码内容
	    			if ( sumPage <= 5){
	    				for(var i = 0 ; i < sumPage ;i++){
	    					ListPage.push({'PageShow':i+1});
	    				}
	    			}else if( iPAGE < sumPage - 2){
	    				for(var k = iPAGE ; k < iPAGE + 3 ; k++ ){
	    					ListPage.push({'PageShow':k});
	    				}
	    				ListPage.push({'PageShow':"..."});
	    				ListPage.push({'PageShow':sumPage});
	    			}else{
	    				ListPage.push({'PageShow':1});
	    				ListPage.push({'PageShow':"..."});	
	    				for(var j = sumPage - 2 ; j <= sumPage ; j++ ){
	    					ListPage.push({'PageShow':j});
	    				}
	    			}
	    		    $scope.page = {
	    		    	'nowPage': iPAGE,
	    		    	'totalPage': sumPage,
	    		    	'ListPage' : ListPage
	    		    };
                });
    	}
    	
    	//绑定【页码】跳转事件
		$(document).on("click",".wmes_pagint_num",function(){
			if($(this).attr("data-page") != "..."){
				iPAGE = $(this).attr("data-page");
				getDataList();
			}
		});

		Initialization();

        $(".recommend-date-ok").on("click",function(){
  
        var selection_criterialenght = $(".selection_criteria li").length;
        for(var i = 0; i < selection_criterialenght ;i++){
                if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
                    $(".selection_criteria li").eq(i).remove();
                }
        }
        if($("#fromDate" ).val() != "" && $("#toDate").val() != ""){
            $(".funds_title_selection").before('<li data-value="' + $("#fromDate").val() + ' to ' + $("#toDate").val()  + '" data-name="' + $(this).attr("data-name") + '">' + $("#fromDate").val() + ' to ' + $("#toDate").val()  + '<span class="selection_delete"></span></li>')
            
        }
        Initialization();
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
    		if($(this).closest('.funds_choice').find('.funds_all').hasClass('fund_choice_active')){
    			$(this).closest('.funds_choice').find('.funds_all').addClass('fund_choice_active2');
    		}else{
    			$(this).closest('.funds_choice').find('.funds_all').removeClass('fund_choice_active2');
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
//    		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
//    		var this_letter= $(this).attr("data-letter");
//    		if(this_letter != $('#geoAllocation').val()){
//    			$('#geoAllocation').val(this_letter);
//				Initialization();
//			}
//    		
//    		if(this_letter != $('#geoAllocation').val()){
//    			$('#geoAllocation').val(this_letter);
//				Initialization();
//			}
//    		
//    		if(!$(this).hasClass('funds_all')){
//    			$('.show-all').remove();
//    		}
//    		// show all
//    		var flag = true; 
//    		$('.funds_all').each(function(){
//    			if(!$(this).hasClass('fund_choice_active')){
//    				flag = false;
//    			}
//    		});
//    		if(flag && $('.show-all').length == 0){
//    			$('.selection_criteria').append('<li class="fund_choice_active show-all" style="margin:0 10px">Show All</li>');
//    		}
    		
    		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
			$(this).siblings().removeClass("fund_choice_active2").end().addClass("fund_choice_active2");
    		
			var this_letter = $(this).attr("data-letter"),
			funds_logo = $("#funds_logo").children(),
			funds_logo_lenght = funds_logo.length;
			for (var k = 0 ; k< funds_logo_lenght; k++){
				var strTemp = funds_logo.eq(k).attr("data-letter");
				var str = strTemp.charAt(0);
				if( this_letter.indexOf(str) > -1 ){
	 				funds_logo.eq(k).show();
				}else{
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
    	
    	function searchInsightList(searchLi){
    		var dataName = $(searchLi).attr("data-name"); 
    		var dataValue = $(searchLi).attr("data-value");
    		
    		if(!dataValue)dataValue="";
    		
    		if("IssuedDate"==dataName){
    			$("#issuedDate").val(dataValue);
    		}
    		
    		if(dataName.indexOf('Sector') > -1){
//    			var sector = $("#sector").val();
//    			if(sector != ''){
//    				sector = $.parseJSON(sector);
//    				sector.push(dataValue);
//    			}else{
//    				sector = [];
//    				sector.push(dataValue);
//    			}
    			sector = [];
				sector.push(dataValue);
    			$("#sector").val(JSON.stringify(sector));
    		}
    		if(dataName.indexOf('geoAllocation') > -1){
    			
    			var allocation = [];
				allocation.push(dataValue);
    			$("#allocation").val(JSON.stringify(allocation));
    		}
    		
//    		if("RelateSector"==dataName){
//    			$("#sector").val(dataValue);
//    		}
    		
    		if("geoAllocation"==dataName){
    			$("#geoAllocation").val(dataValue);
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
            $("#allocation").val('');
			$("#sector").val('');
            Searchdata = $("#listForm").serialize();
            
            
            Initialization();
            $('.funds_choice li').removeClass('fund_choice_active2');
            $('.fund_choice_active').addClass('fund_choice_active2');
        });
    	$(".ifa_list").on("click","#headdown",function(){
    		var insightId = $(this).attr("insightId");
    		if(!!insightId){
        		$.ajax({
        			url:base_root+"/front/investor/myInsight/headStatusChange.do",
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
    	$(document).on("click",".recommend-news-like-ico",function(){
    		var selt = this,
    			insightId = $(selt).data("insight"),
    			type = $(selt).data('type');
    		
    		if((window.sessionStorage.getItem(insightId+1) == 1 &&
    				type == 1) || (window.sessionStorage.getItem(insightId+0) == 1 &&
    	    				type == 0)){
				layer.msg(INSIGHT_UPORDOWN_DOUBLECLICK_ALERT,{icon : 0,time : 2000});
				return false;
			}else if(!!insightId){
    			$.ajax({
    				url:base_root+"/front/investor/myInsight/insightUpCounter.do",
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
    						//console.log($(selt));
    						$(selt).next().text(counter);
    					}else{
    						//layer.msg('The system is busy!',{icon:2,time:2000});
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

   
  //弹框
  $(document).on("click",".recommend-news-title",function(){
  	//var insightId = $(this).prev(".insightId").val();
	var selt = this,
	  insightId = $(selt).data("insight");
  	$.ajax({
  		url:base_root+"/front/investor/myInsight/insightView.do",
  		data:{insightId:insightId},
  		type:"get",
  		success:function(result){
  			if("" != result.id || null != result.id){
  				$(".ifa-article-space .ifa-article-wrap").html('');
  				var html = '<div class="wmes-close" id="space-article-close"></div>'
  		            + '<p class="ifa-article-title">' + result.title + '</p>'
  		            + '<div class="ifa-article-pub-wrap">'
                    + '<p class="ifa-article-name-wrap"><img class="ifa-article-portrait-img" src="'+ base_root +'/res/images/ifa/linshi_ifa_04.png"> ' + result.author.nickName + '</p>'
                    + '<p class="ifa-article-time">' + result.pubDate + '</p>'
                    + '<p class="ifa-article-click"><img class="recommend-news-bottom-img" src="'+ base_root +'/res/images/discover/eve_ico.png"> ' + result.click + '</p>'
                    + '</div>'
  		            + '<p class="ifa-article-word">' + result.content +'</p>'
  		            + '<div class="article-retopst-wrap">'
                    + '<div class="atricle-retopst-like-wrap">'
                    + '<img class="recommend-news-like-ico" data-type="1" data-insight="'+insightId+'" src="'+ base_root +'/res/images/fund/fund_like_ico.png">'
                    + '<span class="recommend-news-like-num">' + result.upCounter + '</span></div>'
                    + '<div class="atricle-retopst-like-wrap">'
                    + '<img class="recommend-news-like-ico" data-type="0" data-insight="'+insightId+'" src="'+ base_root +'/res/images/fund/fund_step_ico.png">'
                    + '<span class="recommend-news-like-num">' + result.downCounter + '</span></div>'
                    + '<div class="atricle-retopst-cen"><a class="atricle-retopst-close" href="javascript:;">Close</a></div>'
  		            + '</div>';
  				$(".ifa-article-space .ifa-article-wrap").append(html);
  				$('.ifa-article-space').css('margin-top',$(document).scrollTop());
  				$(".ifa-article-space").show();
  			    $(".ifa-article-zhe").show();
  			}else{
  				layer.msg('The system is busy!',{icon:2,time:2000});
  			}
  		}
  	})
  });

  $(document).on("click","#space-article-close",function(){
       $(".ifa-article-space").hide();
       $(".ifa-article-zhe").hide();
  });
  $(document).on("click",".atricle-retopst-close",function(){
       $(".ifa-article-space").hide();
       $(".ifa-article-zhe").hide();
  });
  
  $('.funds_choice_wrap_hiddenclick').on('click', function() {
			var choiceHeight = $(this).closest('.funds_choice_wrap_hidden').find('.funds_choice').css('height');
			$('.funds_choice_wrap_hiddenclick').toggleClass('funds_choice_wrap_showclick');
			if($(this).hasClass('funds_choice_wrap_showclick')) {
				$(this).closest('.funds_choice_wrap_hidden').removeAttr('max-height');
				$(this).closest('.funds_choice_wrap_hidden').stop().animate({
					'min-height': choiceHeight
				});
			} else {
				$(this).closest('.funds_choice_wrap_hidden').stop().animate({
					'min-height': '27px'
				});;
			}

		});
		
	$(".wmes-menu-hide").on("click",function(){
		$(this).toggleClass("wmes-menu-hideac");
		if( $(this).hasClass("wmes-menu-hideac")) {
			$(".client-more-screen-wrap").stop().animate({ 
				height: "100%"
			}, 300 );
			$(".client-more-screen-wrap").css({'margin-top':'20px'});
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
	
	//angular 自定义过滤器截取字符
    mybase.filter('cut', function () {
	  return function (value, wordwise, max, tail) {
		//去除html标签，图片，换行，回车
		value = value.replace(/&nbsp;/g, "");  
		value = value.replace(/(\n)/g, "");  
		value = value.replace(/(\t)/g, "");  
		value = value.replace(/(\r)/g, "");  
		value = value.replace(/<\/?[^>]*>/g, "");  
		value = value.replace(/\s*/g, "");   
	    if (!value) return '';
	    max = parseInt(max, 10);
	    if (!max) return value;
	    if (value.length <= max) return value;
	    value = value.substr(0, max);
	    if (wordwise) {
	      var lastspace = value.lastIndexOf(' ');
	      if (lastspace != -1) {
	        value = value.substr(0, lastspace);
	      }
	    }
	    return value + (tail || '…');
	  };
	});
});
