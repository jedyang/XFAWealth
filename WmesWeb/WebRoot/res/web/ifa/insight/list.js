define(function(require) {
	var $ = require('jquery'),
	angular = require('angular');	
	require('laydate');
	require('layer');
	
	var mybase = angular.module('ifaTable', []);
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
		// 正常拿数据	 
    	function getDataList(){
    		var url = base_root + "/front/ifa/myInsight/listJson.do",
    			data =  "rows="+ rows +"&page=" + iPAGE +"&"+searchData+"&status=" + $scope.status;
    			$http({
                  url: url,
                  method: 'POST',
                  data : data,
                  params : {
                	  allocation : encodeURI($("#allocation").val()),
                	  sector : encodeURI($("#sector").val())
                  }
    			,headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
             	 })
    			.success(function(response){//console.log(response.rows);
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
		
		function Initialization(){
			// 初始化数值
			iPAGE = 1; //第N页数据
			searchData = $("#paramsForm").serialize();
			getDataList();
		}
		
		Initialization();
		$('#fromDate').click(function(){
			var max = laydate.now();
			if($('#toDate').val() != ''){
				max = $('#toDate').val();
			}
			laydate({
				istime: false,
				//min:min,
				max:max,
				istoday:false,
				elem: '#fromDate',
				format: 'YYYY-MM-DD'
			});
		});
		$('#toDate').click(function(){
			var min = '';
			if($('#fromDate').val() != ''){
				min = $('#fromDate').val();
			}
			laydate({
				istime: false,
				min:min,
				max:laydate.now(),
				istoday:false,
				elem: '#toDate',
				format: 'YYYY-MM-DD',
				choose:function(){
					if(!!$('#fromDate').val()){
						$('#hidFromDate').val($('#fromDate').val());
						$('#hidToDate').val($('#toDate').val());
						Initialization();
					}
				}
			});
		});
		
		$('.funds_title_selection').click(function(){
			$('.selection_criteria li').remove();
			$('.funds_title_selection').hide();
			
			$('.funds_choice_issued_date li').each(function(){
				$(this).css('color', '#000');
			});
			$('.select-geo-allo li').each(function(){
				$(this).css('color', '#000');
			});
			$('.funds_choice_sector li').each(function(){
				$(this).css('color', '#000');
			});
			$('.funds_choice_issued_date').find('.funds_all').click();
			$('.funds_logo_b').find('.funds_all').click();
			$('.funds_choice_sector').find('.funds_all').click();
			$("#issuedDate").val('');
			$("#allocation").val('');
			$("#sector").val('');
			Initialization();
		});
		
		/**
    	 * Publish / Draft 切换
    	 */
    	$('.application-information-tab li').on('click',function(){
    		$(this).toggleClass('tab-active');
    		$(this).siblings().toggleClass('tab-active');
    		$('.applications-tabcontrol').toggleClass('tabactive');
    		$('.applications-tabcontrol1').toggleClass('tabactive');
    		$('.tabcut').toggleClass('ifa-more-ico-hidden');
    		if($(this).data('active')=='publish'){
    			$scope.status = 1;
    			Initialization();
    		}else if($(this).data('active')=='draft'){
    			$scope.status = 0;
    			Initialization();
    		}
    	});

    	/**点击每个选项，在下面的已选方案中添加该选项
    	 * author scshi
    	 * date 20160821
    	 */
    	var listTime;
    	$(".funds_choice li").on("click",function(){
    		if($(this).data('name') != 'IssuedDate' && !$(this).hasClass('funds_all')){
    			if($(this).attr('style') != 'color: rgb(75, 166, 222);'){
    				return;
    			}
    		}else if($(this).data('name') == 'IssuedDate' && $(this).hasClass('fund_choice_active') && !$(this).hasClass('funds_all')){
    			$(this).removeClass('fund_choice_active');
    			var name_ = $(this).data('value');
    			$('.selection_criteria li').each(function(){
    			    if($(this).data('value') == name_){
        				$(this).remove();
        			}
				});
    			$("#issuedDate").val('');
    			if($('.selection_criteria li').length==0){
    				$('.funds_title_selection').hide();
    			}
    			Initialization();
    			return;
    		}
    		clearTimeout(listTime);
    		if($(this).hasClass('funds_all')){
    			var name_ = $(this).data('name');
    			$('.selection_criteria li').each(function(){
    			    if($(this).data('name').substring(0,$(this).data('name').length-1) == name_){
        				$(this).remove();
        			}
				});
    			if($('.selection_criteria li').length==0){
    				$('.funds_title_selection').hide();
    			}
    		}
    		if($(this).parent().hasClass("funds_logo_b")){
    			return;
    		}
    		if($(this).hasClass('issued-date-other')){
    			$(this).parent().find('.funds_all').removeClass('fund_choice_active');
    			$('.issued-date-other-time').toggle();
    			$(this).siblings().removeClass("fund_choice_active").end().toggleClass("fund_choice_active");
    			if($('.issued-date-other-time').is(':hidden')){
    				$(this).parent().find('.funds_all').addClass('fund_choice_active2');
    			}else{
    				$(this).parent().find('.funds_all').removeClass('fund_choice_active2');
    			}
    			$('.selection_criteria li').each(function(){
    				if($(this).data('name') == 'IssuedDate'){
    					$(this).remove();return;
    				}
    			});
    			if($('.selection_criteria li').length==0){
    				$('.funds_title_selection').hide();
    			}
    			return;
    		}
    		$(this).parent().find('.issued-date-other-time').hide();
    		if(!$(this).hasClass('funds_all')){
    			$(this).parent().find('.funds_all').removeClass('fund_choice_active2');
    		}else if($(this).hasClass('funds_all')){
    			$(this).addClass('fund_choice_active2');
    			var name_ = $(this).data('name');
    			$('.selection_criteria li').each(function(){
    				if($(this).data('name') == 'IssuedDate' && $(this).data('name') == name_){
    					$(this).remove();
    					$("#issuedDate").val('');
        			}else if($(this).data('name') == 'region' && $(this).data('name').indexOf(name_) > -1){
        				$(this).remove();
        				$("#allocation").val('');
        			}else if($(this).data('name') == 'sector' && $(this).data('name').indexOf(name_) > -1){
        				$(this).remove();
        				$("#sector").val('');
        			}
				});
    			if($('.selection_criteria li').length==0){
    				$('.funds_title_selection').hide();
    			}
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
    			var value = $(this).attr("data-key");
    			if(value.length>20){
    				value = value.substring(0,20);
    			}
    			$(".funds_title_selection").before('<li title="'+$(this).attr("data-key")+'" style="width:180px;float: left;" data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + value + '<span class="selection_delete"></span></li>')
    		}
    		if($(".selection_criteria li").length > 0){
    			$(".funds_title_selection").show();
    		}
    		if($(this).data('name')=='IssuedDate'){
    			$('#fromDate').val('');
    			$('#hidFromDate').val('');
    			$('#toDate').val('');
    			$('#hidToDate').val('');
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
    		$(".recommend-switch-tab li").removeClass("recommend-sort-active");
    		$(this).addClass("recommend-sort-active");
    		var currentSort = $(this).attr("currentSort");
    		var removeSort = $(this).attr("removeSort");
    		if($(this).find(".arrow_top").hasClass("top_active") ){
    			$('.arrow_down').removeClass("down_active");
    			$('.arrow_top').removeClass("top_active");
    			$(this).find(".arrow_down").addClass("down_active");
    			$("#"+removeSort).val("");
    			$("#"+currentSort).val("desc");
    		}else if($(this).find(".arrow_down").hasClass("down_active") ){
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
    			$("#"+currentSort).val("desc");
    		}
    		Initialization();
    	});
    	$(".icon_search").on("click",function(){
			var fundName = $("#searchKeyWord").val();
			document.getElementById("keyWord").value=fundName;
			searchData = $("#paramsForm").serialize();
		 	Initialization();
		});
		$("#searchKeyWord").keyup(function(event){
       	 if(event.keyCode == 13){
	        	document.getElementById('searchKeyBtn').click();
	        }
	    });		
		$("#searchKeyWord").on('input',function(){
			document.getElementById('searchKeyBtn').click();
		});		
    	$(".selection_criteria").on("click",".selection_delete",function(){
    		var selectValue = $(this).closest('li').data('name');
    		var value_ = $(this).closest('li').data('value');
    		if('IssuedDate' == selectValue){
    			$('.funds_choice_issued_date li').each(function(){
    				if($(this).data('value') == value_){
    					$(this).css('color','#000');
    				}
    			});
    		}else if(selectValue.substring(0,6) == 'sector'){
    			$('.funds_choice_sector li').each(function(){
    				if($(this).data('name') == selectValue){
    					$(this).css('color','#000');
    				}
    			});
    			var selectFlag = false;
    			$('.funds_choice_sector li').each(function(){
    				if($(this).attr('style') == 'color: rgb(75, 166, 222);'){
    					selectFlag = true;
    					return false;
    				}
    			});
    			if(!selectFlag){
    				$('.funds_choice_sector').find('.funds_all').click();
    			}
    			var sector = $("#sector").val();
    			if(sector != ''){
    				sector = $.parseJSON(sector);
    				sector.splice($.inArray(value_,sector),1); 
    				$("#sector").val(JSON.stringify(sector));
    			}
    		}else if(selectValue.substring(0,7) == 'regions'){
    			$('.select-geo-allo li').each(function(){
    				if($(this).data('name') == selectValue){
    					$(this).css('color','#000');
    				}
    			});
    			var selectFlag = false;
    			$('.select-geo-allo li').each(function(){
    				if($(this).attr('style') == 'color: rgb(75, 166, 222);'){
    					selectFlag = true;
    					return false;
    				}
    			});
    			if(!selectFlag){
    				$('.funds_logo_b').find('.funds_all').click();
    			}
    			var allocation = $("#allocation").val();
    			if(allocation != ''){
    				allocation = $.parseJSON(allocation);
    				allocation.splice($.inArray(value_,allocation),1); 
    				$("#allocation").val(JSON.stringify(allocation));
    			}
    		}
    		var selectedCount = $(this).closest('ul').find('li').length;    		
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
			if($('.selection_criteria li').length==0){
				$('.funds_title_selection').hide();
			}
			Initialization();
		});
    	
    	function searchInsightList(searchLi){
    		var dataName = $(searchLi).attr("data-name");
    		var dataValue = $(searchLi).attr("data-value");
    		if(dataValue == 'Sector_00'){
    			return;
    		}
    		if(!dataValue)dataValue="";
    		if("IssuedDate"==dataName){
    			$("#issuedDate").val(dataValue);
    		}else if(dataName.indexOf('regions') > -1){
    			var allocation = $("#allocation").val();
    			if(allocation != ''){
    				allocation = $.parseJSON(allocation);
    				allocation.push(dataValue);
    			}else{
    				allocation = [];
    				allocation.push(dataValue);
    			}
    			$("#allocation").val(JSON.stringify(allocation));
    		}else if(dataName.indexOf('sector') > -1){
    			var sector = $("#sector").val();
    			if(sector != ''){
    				sector = $.parseJSON(sector);
    				sector.push(dataValue);
    			}else{
    				sector = [];
    				sector.push(dataValue);
    			}
    			$("#sector").val(JSON.stringify(sector));
    		}
		 	Initialization();
    	}
    	
    	var insightId = null;
    	$(".ifa_list").on("click",".visitorsBtn",function(){
    		if($(this).find('.recommend-news-eve-num').text() == '0'){
    			layer.msg('No data display');
    		}else{
    			insightId = $(this).attr("insightId");
        		getFriendList();
    		}
    	});
    	/**
	    * 观点访客列表
	    * @author
	    * */ 
    	function getFriendList(){
    		$scope.friendList = '';
    		var rows = 12;
    		var url = base_root +"/front/ifa/myInsight/viewList.do";
    			$http({
                  url: url,
                  method: 'POST',
                  params : {
                	  page:$scope.friendListPage,
                	  rows:rows,
                	  insightId:insightId
                  },
                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
             	 })
    			.success(function(response){
					$scope.friendList =  response.list;
					var page = response.page;
             		$scope.friendListPage = page;
					$('.visitedconlayer').show();
					var windowHeight1 = $(window).height()*0.35+$(document).scrollTop();
					$('.visitedcon').css('top',windowHeight1);
					$('.visitedcon').show();
					if(response.total>12*page){
    	    			$('.visitedright').show();
    	    		}
                });
    	}
    	$('.visitedright').on('click',function(){
    		$scope.friendListPage = $scope.friendListPage+1;
    		getFriendList();
    		$('.visitedleft').show();
    		$('.visitedright').hide();
    	});
    	$('.visitedleft').on('click',function(){
    		$scope.friendListPage = $scope.friendListPage-1;
    		getFriendList();
    		$('.visitedright').show();
    		$('.visitedleft').hide();
    	});
    	
        /**
         * 观点删除
         * @author scshi
         * @date 20160823
         * */
        $(".ifa_list").on("click",".delete",function(){
        	insightId = $(this).attr("insightId");
        	if(!!insightId){
        		layer.confirm('Sure delete the item？', {
        			btn: ['confirm','consel'] //按钮
        		}, function(){
        			$.ajax({
        				url:base_root+"/front/ifa/myInsight/delInsight.do",
        				data:{"insightId":insightId},
        				type:"get",
        				dataType:"json",
        				success:function(data){
        					if(data.result){
        						$scope.dataList ='';
        						Initialization();
        					}
        				}
        			});
        			layer.closeAll();
        		}, function(){
        			//关闭方法回调
        		});
        	}
        });
        
        //置顶操作
        $(".ifa_list").on("click","#headup",function (){
        	var insightId = $(this).attr("insightId");
        	if(!!insightId){
        		$.ajax({
        			url:base_root+"/front/ifa/myInsight/headStatusChange.do",
        			data:{"insightId":insightId,"headStatus":1},
        			type:"get",
        			dataType:"json",
        			success:function(data){
        				Initialization();
        			}
        		});
        	}
        	
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
        				Initialization();
        			}
        		});
        	}
    	});
    	
    	//刷新清除缓存
    	window.sessionStorage.clear();
    	//点赞、踩
    	$(".ifa_list").on("click","#recommend-news-like",function(){
    		var selt = this,
    			insightId = $(selt).data("insight"),
    			type = $(selt).data('type');
    		if((window.sessionStorage.getItem(insightId+1) == 1 &&
    				type == 1) || (window.sessionStorage.getItem(insightId+0) == 1 &&
    	    				type == 0)){
				layer.msg("Do not repeat clicks!");
				return false;
			}else if(!!insightId){
    			$.ajax({
    				url:base_root+"/front/ifa/myInsight/insightUpCounter.do",
    				data:{insightId:insightId,type:type},
    				type:"post",
    				success:function(result){
    					if(result.flag){
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
    						layer.msg('The system is busy!');
    					}
    				}
    			})
    		}
    	});
    	
    }]);//angular.js end
    
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
    
    /**
     *访客列表关闭
     *@author scshi 
     * */
    $('.visitedconlayer').on('click',function(){
		$('.visitedconlayer').hide();
		$('.visitedcon').hide();
	});
    
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
			loadFundList(this);
		}
	});
	$('.funds_choice_wrap_hiddenclick').on('click', function() {
		var choiceHeight = $(this).closest('.funds_choice_wrap_hidden').find('ul').css('height');
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
			$(this).parent('ul').find('li').css('color', '#000');
		}else{
			if($(this).attr('style') == 'color: rgb(75, 166, 222);'){
				$(this).css('color', '#000');
				var name_ = $(this).data('name');
				var value_ = $(this).data('value');
				$('.selection_criteria li').each(function(){
					if(name_ == $(this).data('name')){
						$(this).remove();
					}
				});
				if(name_.indexOf('regions') > -1){
	    			var allocation = $("#allocation").val();
	    			if(allocation != ''){
	    				allocation = $.parseJSON(allocation);
	    				allocation.splice($.inArray(value_,allocation),1); 
	    				$("#allocation").val(JSON.stringify(allocation));
	    			}
	    		}else if(name_.indexOf('sector') > -1){
	    			var sector = $("#sector").val();
	    			if(sector != ''){
	    				sector = $.parseJSON(sector);
	    				sector.splice($.inArray(value_,sector),1); 
	    				$("#sector").val(JSON.stringify(sector));
	    			}
	    		}
				if($('.selection_criteria li').length==0){
    				$('.funds_title_selection').hide();
    			}
				Initialization();
			}else{
				$(this).css('color', '#4ba6de');
			}
		}
	});
	$('.select-geo-allo-all').click(function(){
		$('.select-geo-allo').find('li').css('color', '#000');
	});

	
	
	//弹框
	  $(document).on("click",".recommend-news-title",function(){ 
	  	var insightId = $(this).attr("insightid");
	  	//
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
	                    + '<p class="ifa-article-name-wrap"><img class="ifa-article-portrait-img" src="'+ base_root +'/res/images/ifa/linshi_ifa_04.png">' + result.author.nickName + '</p>'
	                    + '<p class="ifa-article-time">' + result.pubDate + '</p>'
	                    + '<p class="ifa-article-click"><img class="recommend-news-bottom-img" src="'+base_root+'/res/images/discover/eve_ico.png">' + (result.click==null?'0':result.click) + '</p>'
	                    + '</div>'
	  		            + '<p class="ifa-article-word">' + result.content +'</p>'
	  		            + '<div class="article-retopst-wrap">'
	                    + '<div class="atricle-retopst-like-wrap">'
	                    + '<img insightid="'+insightId+'" class="recommend-news-like-ico class_article_like" src="'+ base_root +'/res/images/fund/fund_like_ico.png">'
	                    + '<span id="span_id_likd_'+insightId+'" class="recommend-news-like-num">'+(result.upCounter==null?'0':result.upCounter) +'<span></div>'
	                    + '<div class="atricle-retopst-like-wrap">'
	                    + '<img insightid="'+insightId+'" class="recommend-news-like-ico class_article_notlike" src="'+ base_root +'/res/images/fund/fund_step_ico.png">'
	                    + '<span id="span_id_down_'+insightId+'" class="recommend-news-like-num">'+ (result.downCounter==null?'0':result.downCounter) +'<span></div>'
	                    + '<div class="atricle-retopst-cen"><a class="atricle-retopst-close" href="javascript:;">'+GLOBAL_CLOSEWINDOW+'</a></div>'
	  		            + '</div>';
	  				$(".ifa-article-space .ifa-article-wrap").append(html);
	  				$(".ifa-article-space").show();
	  			    $(".ifa-article-zhe").show();
	  			    $('.ifa-article-space').css({'top':$(document).scrollTop()});
	  			}else{
	  				layer.msg('The system is busy!',{icon:2,time:2000});
	  			}
	  		}
	  	});
	  });
	
	  $(document).on("click","#space-article-close",function(){
	       $(".ifa-article-space").hide();
	       $(".ifa-article-zhe").hide();
	  });
	  $(document).on("click",".atricle-retopst-close",function(){
	       $(".ifa-article-space").hide();
	       $(".ifa-article-zhe").hide();
	  });
	
	
	
	
	
});