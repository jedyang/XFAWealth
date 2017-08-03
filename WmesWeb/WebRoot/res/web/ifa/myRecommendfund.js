
define(function(require) {
	'use strict';
	var $ = require('jquery');//,
		angular = require('angular');
		require("scrollbar");
		require('layui');
		require('laydate');
		 var selector =  require('ifaSelectUser');
			selector.init();
		$('.funds_search_wrap').perfectScrollbar();

		setTimeout(function(){
		var toDate={
			  istime: false,
			  elem: '#recommend-date-to',
			  format:"YYYY-MM-DD",
			  choose: function(datas){ //选择日期完毕的回调
				  fromDate.max=datas;
				   //$("#bornDate").val(formatDate(datas,$("#dateFormat").val()));  //alert('得到：'+datas);
			  }
			}
		
	 var fromDate=	{
			  istime: false,
			  elem: '#recommend-date-from',
			  format:"YYYY-MM-DD",
			  choose: function(datas){ //选择日期完毕的回调
				  toDate.min=datas;
				   //$("#bornDate").val(formatDate(datas,$("#dateFormat").val()));  //alert('得到：'+datas);
			  }
			}
		laydate(fromDate);
		laydate(toDate);
		},200);
		
	/*new JsDatePick({
      useMode:2,
      target:"recommend-date-to",
      dateFormat:"%Y-%m-%d"
    });
     new JsDatePick({
      useMode:2,
      target:"recommend-date-from",
      dateFormat:"%Y-%m-%d"
    });*/
	function Fundsscrool(){

	    	// 表头滚动
				var funds_hei =  $(".funds_search_tab").height(),
					header_top = $(".wmes_top").height(), 
					 wmes_window_top = $(window).scrollTop(),
					infor_top = $(".funds_search_information").offset().top;
					//funds_top = infor_top - header_top;
					 
				if( $('.no_list_tips').css('display') == "block" ){
					return false;
				}
				 if ( wmes_window_top > (infor_top  - header_top) ){
				 	var new_funds_top = wmes_window_top - infor_top + header_top + funds_hei;
				 	$(".funds_search_tab").addClass("funds_fixed").css
				 		({
					 		'top':header_top,
					 		"width" : $(".funds_selected").width()
					 	});
						var	 _thisStyle =  "-webkit-transform : translateY(" + new_funds_top + "px);-moz-transform : translateY(" + new_funds_top + "px);-ms-transform : translateY(" + new_funds_top + "px);-o-transform : translateY(" + new_funds_top + "px);transform : translateY(" + new_funds_top + "px)";
						$('.funds_tables_th').attr("style",_thisStyle);

						// 解决低版本浏览器tr无法滚动问题
						if( infor_top == $('.funds_tables_th').offset().top && new_funds_top > funds_hei + 2 ){
							// //console.log(infor_top + '---' +
							// $('.funds_tables_th').offset().top)
							$('.funds_tables_th').addClass("funds_tables_caption")
						}
						$(".ps-scrollbar-x-rail").css("top",new_funds_top + $('.funds_tables_th').height());
				 }else{

				 	$(".funds_search_tab").removeClass("funds_fixed").css
				 		({
					 		'top':0,
					 		"width" : 'auto'
					 	});
					var	 _thisStyle1 =  "-webkit-transform : translateY(" + 0 + "px);-moz-transform : translateY(" + 0 + "px);-ms-transform : translateY(" + 0 + "px);-o-transform : translateY(" + 0 + "px);transform : translateY(" + 0 + "px)";
						$('.funds_tables_th').attr("style",_thisStyle1);
						$(".ps-scrollbar-x-rail").css("top",60);

				 }
	    };
  	$(window).on('scroll',Fundsscrool);

	// 数据控制
	var mybase = angular.module('mySearch', ['truncate']);
     mybase.controller('Searchctrl', ['$scope', '$http', function($scope, $http) {
	 		var url = base_root + "/front/ifa/info/getifarecommendfundlistJson.do", // url
	 			iPAGE = 1, // 第N页数据
	    		is_finish = true,// 当前数据是否加载完成
	    		Searchdata = "",// 搜索条件初始化
	    		Sortdata = "sort=issue_price&order=desc",// 排序条件初始化 默认根据issue
															// price（增长率降序）
	    		page_bol = true;// 总页数控制
	    		
	    		// 初始化数据
	    		$scope.dataList = '';

	    		var dateSearch=false;
	    		
	    	// 数字调用
	    	$scope.counter = Array;
	    	
	    	// 监听视图渲染是否结束
	    	$scope.checkLast = function($last){
				if($last){

					// 删除渲染效果
					$(".animated").one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				    		$(".animated").removeClass();
				    });

					

				}
			}

	    	var rows = 10  // 固定每次拿10条数据
	    	// 正常拿数据
	    	function getDataList(){
	    		 var period="";
  		     var sector="";
  		     var region="";
  		     $(".selection_criteria").find("li").each(function(){
  		    	 var type=$(this).attr("data-name");
  		    	 if("Recommend Date"==type){
  		    		 if($(this).attr("data-value")=="Other"){
  		    			period=$("#recommend-date-from").val()+"to"+$("#recommend-date-to").val();
  		    		 }else{
  		    			 period=$(this).attr("data-value");
  		    		 }
  		    		
  		    	 }else if("regions"==type){
  		    		 region= $(this).attr("data-value");
  		    	 }else if("sector"==type){
  		    		 sector= $(this).attr("data-value");
  		    	 }
  		     });
  		     
  		      if(dateSearch){
  		    	period=$("#recommend-date-from").val()+"to"+$("#recommend-date-to").val();
  		      }
	    		    
	    		var   data =  "langCode=" + lang + "&rows="+ rows +"&page=" + iPAGE + "&" + Searchdata + "&" + Sortdata+"&period="+period+"&sector="+sector+"&region="+region;
	    			// 控制数据是否加载完成
	    			is_finish = false;
	    			$http({
                    url: url,
                    method: 'POST',
                    data : data,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
               	 })
	    			.success(function(response){
	    				//console.log(response);
		                 is_finish = true;
	                      if(response.list.length > 0){
	                      		$(".funds_search_information , .funds_search_wrap").show();
	                      		$(".no_list_tips").hide();
	                      		 if( iPAGE === 1 ){
	                      		 	 $scope.dataList = '';
	                                 $scope.dataList =  response.list;
	                              }else{
	                                 $scope.dataList = $scope.dataList.concat(response.list);
	                              }
	                      		// 总页数
	                      		var sumPage = Math.ceil(response.total / rows);
	                      		if(iPAGE >= sumPage){
	                      			page_bol = false;
	                      		}
                    		}else{
                    			page_bol = false;
                   			 if( iPAGE === 1 ){

                    			 	// 当第一页没有数据，显示提示信息
                    			 	$(".funds_search_information ").hide()
                    			 	$(".no_list_tips").show();
                    			 }
                    		}
		    			iPAGE++;
		    			$scope.datatotal = response.total;
		    			
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
			 	iPAGE = 1; //第N页数据
	    		is_finish = true;
	    		page_bol = true;
	    		selection();
	    		getDataList();
	    		console.log("Initialization");
	    	}
	    	function GetScroll(){
	    		var docheight = $(window).scrollTop() + $(window).height(),
	    			listheight = $('.funds_search_wrap ').offset().top + $('.funds_search_wrap ').height() + $(".wmes_top").height();
	    		
	    		if( docheight > listheight ){
	    			scrollBol = true;
	    			
	    		}else{
	    			scrollBol = false;
	    		}

  			if (scrollBol && is_finish && page_bol ){
  				getDataList();
  			}

	    	}
			
	$(".recommend-date-button").on("click",function(){
		dateSearch=true;
		var selection_criterialenght = $(".selection_criteria li").length;
        for(var i = 0; i < selection_criterialenght ;i++){
                if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
                    $(".selection_criteria li").eq(i).remove();
                }
        }
        $(".funds_title_selection").before('<li data-value="' + $("#recommend-date-from").val() + ' to ' + $("#recommend-date-to").val() 
        		+ '" data-name="' + $(this).attr("data-name") + '">' + $("#recommend-date-from").val() + ' to ' + $("#recommend-date-to").val() 
        		+ '<span class="selection_delete"></span></li>')
		Initialization();
	});
	
	$(".funds_choice li").on("click",function(){
		if($(this).attr("data-name")=="Recommend Date"){
			dateSearch=false;
		}
		if($(this).attr('data-value')=='Other'){
			$('.recommend-date-choice').css('display','inline-block');
			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
			return;
		}else{
			$('.recommend-date-choice').css('display','none');
			$("#recommend-date-from").val("");
			$("#recommend-date-to").val("");
		};
		
		if($(this).parent().hasClass("funds_logo_b")){
			return;
		}
		if($(this).hasClass("recommend-date-choice")){
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
	//	console.log(" li click")
		if($(this).index()==0){
			$(this).closest('ul').find('.funds_all').addClass('fund_choice_active2');
		}else{
			$(this).closest('ul').find('.funds_all').removeClass('fund_choice_active2');
		};
		Initialization();
	});
	
	$("#funds_logo_choice li").on("mousemove",function(){
		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");

		var this_letter= $(this).attr("data-letter"),
			funds_logo= $("#funds_logo").children(),
			funds_logo_lenght = funds_logo.length;
		for (var k = 0 ; k< funds_logo_lenght; k++){
			if( this_letter.indexOf(funds_logo.eq(k).attr("data-letter"))>=0 ){
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
		}
		//Initialization();
	});
	
	
	
	
	$(".strategies_List").on("click",".btnUp",function(){
			var overhead="";
			if($(this).attr("overhead")=="1"){
				overhead="0";
			}else{
				overhead="1";
			}
			var id=$(this).attr("recommendid");
			$.ajax({
				type:"post",
				datatype:"json",
				url:base_root+"/front/ifa/info/recommendFundOverhead.do",
				data:{overhead:overhead,id:id},
				success:function(json){
					Initialization();
				}
			})
		});
	// 执行清除方案点击操作
	$(".funds_title_selection").on("click",function(){
		$(".selection_criteria li").remove();

		$(".fund_logo_active").removeClass("fund_logo_active");

		$(".fund_choice_active").removeClass("fund_choice_active");
		$(".funds_all").addClass("fund_choice_active");
		// $("#listForm").find("input").val("");
		// Searchdata = $("#listForm").serialize();
		Initialization();
	});	
	
	/**
	 * 搜索条件取消点击 author scshi date 20160821
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
		
		Initialization();
	});	
	
	/*$(".recommend-switch-tab  li").on("click",function(){
    	$(".recommend-switch-tab  li").removeClass("recommend-sort-active");
    	$(this).addClass("recommend-sort-active");
    	if($(this).find(".arrow_top").hasClass("top_active") ){
    		$('.arrow_down').removeClass("down_active");
    		$('.arrow_top').removeClass("top_active");
    		$(this).find(".arrow_down").addClass("down_active");
    		Initialization();
    	}else{
    		$('.arrow_down').removeClass("down_active");
    		$('.arrow_top').removeClass("top_active");
    		$(this).find(".arrow_top").addClass("top_active");
    		Initialization();
    	}
    });
*/
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

	$(".strategies_List").on('click','.btnDel',function(){
		var id=$(this).attr("recommendId");
		layer.confirm(langMutilForJs['global.alert.del'], function(){
			$.ajax({
				type:'post',
				datatype:'json',
				url:base_root+'/front/ifa/info/deleteRecommend.do',
				data:{id:id},
				success:function(json){
					if(json.result){
						layer.msg(langMutilForJs['global.success']);
						Initialization();
					}else{
						layer.msg(langMutilForJs['global.failed'])
					}
				}
			})
		})
		
	})
	
	//保存推荐
	$("#btnRecommend").on("click",function(){
		$("#btnRecommend").attr("disable",true);
		var fundName=$(".funds_keyword_input").val();
		var fundId=$(".funds_keyword_input").attr("fundId");
		if(fundName==undefined || fundName=="" || fundId==undefined || fundId==''){
			layer.msg(langMutilForJs['fund.recommend.selectfund']);
			return ;
		}
		var reason=$("#reason").val();
		if(reason==undefined || reason==''){
			layer.msg(langMutilForJs['news.recommend.enterTips']);
			return ;
		}
		
		var scopeFlag=$('input:radio[name="choose2"]:checked').val();
		if(scopeFlag=="3"){
			if (!$("#pushClient").is(":checked")){
    	    	$("#push_clients").val("");
    	    }
    	    if (!$("#pushProspect").is(":checked") ){
    	    	$("#push_prospects").val("");
    	    }
    	    if (!$("#pushBuddy").is(":checked")){
    	    	$("#push_buddys").val("");
    	    }
    	    if (!$("#pushTeam").is(":checked")){
    	    	$("#push_colleagues").val("");
    	    }
    	}else{
    		$("#push_clients").val("");
    		$("#push_prospects").val("");
    		$("#push_buddys").val("");
    		$("#push_colleagues").val("");
    	}
		if($("#push_clients").val().length>0)
    		$("#push_client").val(-1);
    	if($("#push_prospects").val().length>0)
    		$("#push_prospect").val(-1);
    	if($("#push_buddys").val().length>0)
    		$("#push_buddy").val(-1);
    	if($("#push_colleagues").val().length>0)
    		$("#push_colleague").val(-1);
    	
    	$.ajax({
    		type:'post',
    		datatype:'json',
    		url:base_root+'/front/ifa/info/checkFundRecommend.do',
    		data:{id:fundId},
    		success:function(json){
    			if(!json.hasRecommend){
    				$.ajax({
    		    		type:'post',
    		    		datatype:'json',
    		    		url:base_root+'/front/ifa/info/updateWebRecommended.do',
    		    		data:{scopeFlag:scopeFlag,id:fundId,pushClients:$("#push_clients").val(),pushClient:$("#push_client").val(),pushProspects:$("#push_prospects").val(),
    		    			pushProspect:$("#push_prospect").val(),pushBuddies:$("#push_buddys").val(),pushBuddy:$("#push_buddy").val(),pushColleagues:$("#push_colleagues").val(),
    		    			pushColleague:$("#push_colleague").val(),content:reason},
    		    		success:function(json){
    		    			if(json.result){
    		    				layer.msg(langMutilForJs[json.code]);
    		    				$(".recommendfunlistmasklayer").hide();
    		    				Initialization();
    		    			}else{
    		    				layer.msg(langMutilForJs[json.code]);
    		    			}
    		    			////console.log(json);
    		    		}
    		    	})
    			}else{
    				layer.msg("您已经推荐了该基金")
    			}
    		},
    		complete:function(){
    			$("#btnRecommend").attr("disable",false);
    		}
    	})
    	
    	
		
	})
	
	
	
     }])
     $(".mylist-new-bn").on('click',function(){
 		$(".recommendfunlistmasklayer").show();
 	})
 	
 	$(".wmes-close,#btnCancelRe").on('click',function(){
 		$(".recommendfunlistmasklayer").hide();
 	})
     var houseId = '';
     $(".funds_keyword_xiala li").on("click",function(){
     	$(".funds_keyword_div").removeClass('li_ctrl');
     	$('.funds_keyword_div').find("input").val($(this).html());
     	houseId = $(this).attr("data-id");
     	$('.funds_keyword_input').val("");
     });

     $('.funds_keyword_div').on('mouseleave',function(){
     	$(this).find("ul").removeClass("funds_keyword_active").hide();
     	$(this).removeClass('li_ctrl');	
     });
     
 	 $(".funds_keyword_div").on("click",function(){  
     	if( $(this).find("ul").hasClass("funds_keyword_active") ){
     		$(this).find("ul").removeClass("funds_keyword_active").hide();
     		$(".funds_keyword_div").removeClass('li_ctrl');
     	}else{
     		$(this).find("ul").addClass("funds_keyword_active").show();
         	$(".funds_keyword_div").addClass('li_ctrl');
     	}
     });
 	$(".funds_keyword_input").on("focus",function(){
     	$(".funds_keyword_search").addClass('li_ctrl_search');
     	$(this).parent().find("ul").addClass("funds_keyword_active").show();
     	refreshSelList($(this));
     });
     $(".funds_keyword_input").on("blur",function(){
     	var _this = $(this);
     	setTimeout(function(){
 			_this.parent().find("ul").removeClass("funds_keyword_active").hide();
 		},200);
     	
     });
     $(".funds_keyword_input").on("keyup",function(){
     	refreshSelList($(this));
     });
     function refreshSelList(obj){
     	$(".funds_keyword_xiala_search").children().remove();
         $.post(base_root+"/front/fund/info/getFundList.do",{'houseId':houseId,'keyword':$("#funds_keyword_input").val()}, function (values) {
     		values = $.parseJSON( values ); //json字符串转对象
     		var len = values.length;
     		for (var i=0;i<len;i++){
         		$(".funds_keyword_xiala_search").append('<li title="'+ values[i].name +'" data-id="'+values[i].fundId+'">'+ values[i].name +'</li>');
             }
     	});	
     }
     
     $(".funds_keyword_xiala_search").on("click","li",function(){
     	$('.funds_keyword_input').val($(this).html());
     	var id=$(this).attr("data-id");
     	$('.funds_keyword_input').attr("fundId",id);
     	$(".fundlist_littletitle").text("");
			$(".isin").text("");
			$(".lastnav").text("");
			$(".risklevel").text("");
			$(".currency").text("");
     	$.ajax({
     		type:'post',
     		datatype:'json',
     		url:base_root+'/front/ifa/info/getFundSimpleInfo.do',
     		data:{id:id},
     		success:function(json){
     			$(".fundlist_littletitle").text(json.fundName);
     			$(".isin").text(json.isin);
     			$(".lastnav").text(json.lastNAV);
     			$(".risklevel").text(json.riskLevel);
     			$(".currency").text(json.fundCurrency);
     			////console.log(json);
     		}
     	})
     	

     });
     
     $("#reason").on("input",function(){
    	 var length=$(this).val().length;
		var huiche = $(this).val().split('\n').length-1;
		var maxLength=Number($(this).attr("maxLength"));
		if(length>maxLength){
			$(this).parent().find(".inputed").text(maxLength);
			
			$(this).parent().find(".left").text(0);
			$(this).val(obj.val().subString(0,maxLength));
		}else{
			$(this).parent().find(".inputed").text(length+huiche);
			$(this).parent().find(".left").text(maxLength-length-huiche);
		};
		if($(this).parent().find(".left").text()=='-1'){
			$(this).parent().find(".left").text(0);
			$(this).parent().find(".inputed").text(length+huiche-1);
		}
		if($(this).parent().find(".left").text()==0){
			$(this).parent().find(".left").css('color','red');
		}else{
			$(this).parent().find(".left").css('color','#ccc');
		}
     })
     

  // 所属地区，鼠标滑动到ABCD等字母上面时
	$("#funds_logo_choice li").on("mousemove",function(){

		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");

		var this_letter	 = $(this).attr("data-letter"),
			funds_logo  = $("#regionList").children(),
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
			loadIFAList(this);
		}
	});
	
	$(".btnUpdate").on("click",function(){
		
	})

	$('.mylist-new-bn').on('click',function(){
		$('.recommendfunlistmasklayer').show();
	});
	
	$('.wmes-close').on('click',function(){
		$(this).closest('.recommendfunlistmasklayer').css('display','none');
	});
	
	$('.ifa_keyserach_wrap input').change(function(e) {
			if($(this).parents('.view_permissions_setting').find(".letmespecify").prop("checked") == true) {
				$(this).parents('.view_permissions_setting').next().css('display', 'block');
			} else {
				$(this).parents('.view_permissions_setting').next().css('display', 'none');
			}
		});
	
	$('.pushToSettingCheckbox input').on('click',function(){
			if($(this).is(':checked')){
				$(this).closest('.pushToSettingCheckbox').siblings().css('display','block');
			}else{
				$(this).closest('.pushToSettingCheckbox').siblings().css('display','none');
			}
		});
	
	$(".j-permitASetting").on("click",function(){
    	var type=$(this).attr("type");
    	selector.create(0,type,"push_"+type+'s',type+"Names");
		$(".selectnamelistbox").show();
    	
    });
	
	
});