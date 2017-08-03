/**
 * myList.js 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	
	var $ = require('jquery');
	require('layer');
	require("swiper");
//	var grid_selector = "#tableList";
	var Loading = require('loading');
	var oLoading = new Loading($('.tabcutcon'));
	var iLoading = new Loading($('.tabcutcon1'));
	
	var angular = require('angular');	
	var mybase = angular.module('ifaTable', ['truncate']);
	//var searchData = null;
    mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
    	//--- 分页列表数据显示定义  开始 ---
		var searchData = "";//搜索条件初始化
		//初始化数据
		$scope.dataList = '';
		// 数字调用
		$scope.counter = Array;
		
		$scope.isPublic = 1,
		$scope.status = '';
		$scope.dateTimeFormatStr=$("#dateTimeFormat").val();
		$scope.dateFormatStr=$("#dateFormat").val();
		//固定每次拿10条数据
		var rows = 10;
		var currPage = 1;
		
		// 正常拿数据	 
    	function getDataList(num){
    		oLoading.show();
    		iLoading.show();
    		var keyWord = $('#fundName').val();
    		var iPAGE = Number(num);
    		//var url = base_root + "/front/strategy/info/myListJson.do",
    		var url = base_root +"/front/strategy/info/getMyStrategys.do?dateStr="+new Date().getTime(),
			data =  "rows="+ rows +"&page=" + iPAGE +"&"+searchData+"&keyWord=" + keyWord+"&isPublic=" + $scope.isPublic+"&status=" + $scope.status;
			//控制数据是否加载完成
			is_finish = false;
			$http({
				url: url,
				method: 'POST',
				data : data,
				headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
         	 })
			.success(function(response){
                is_finish = true;
                if(response.list.length > 0){
          		 	 $scope.dataList = '';
                     $scope.dataList =  response.list;
                     $(".no_list_tips").hide();
              	}else{//无返回数据时
              		$scope.dataList = '';
     				$(".dataListTr").hide();
     				$(".no_list_tips").show();
              	}
    			// 总记录数
    			$scope.datatotal = response.total;
    			// 总页数
    			$scope.pages = Math.ceil(response.total/rows);
    			//if ($scope.pages<1) $scope.pages=1;
    			var sumPage = $scope.pages;
    			// 当前页
        		if (iPAGE<1) iPAGE=1;
        		if (iPAGE>sumPage) iPAGE=sumPage;
        		//currPage = iPAGE;
    			//分页显示
    			var ListPage = [];
    			//页码内容
    			if ( sumPage <= 5){
    				for(var i = 0 ; i < sumPage ;i++){
//    					////console.log(sumPage)
    					ListPage.push({'PageShow':i+1});
    				}
    			}else if( iPAGE < sumPage - 2){
    				for(var k = iPAGE ; k < iPAGE + 3 ; k++ ){
//    					////console.log(iPAGE + ' --'+ iPAGE + 3)
    					ListPage.push({'PageShow':k});
    				}
    				ListPage.push({'PageShow':"..."});
    				ListPage.push({'PageShow':sumPage});
    			}else{
    				ListPage.push({'PageShow':1});
    				ListPage.push({'PageShow':"..."});	
    				for(var j = sumPage - 2 ; j <= sumPage ; j++ ){
//    					////console.log(sumPage)
    					ListPage.push({'PageShow':j});
    				}
    			}
    			/* 分页数据 */
			    $scope.page = {
			    	'nowPage': iPAGE,
			    	'totalPage': sumPage,
			    	'ListPage' : ListPage
			    };
			    oLoading.hide();
			    iLoading.hide();
            });
    	}
    	//绑定【页码】跳转事件
    	$(document).on("click",".wmes_pagint_num",function(){
			if($(this).attr("data-page") != "..." && $(this).attr("data-page") != $('.wmes_pagint_now').attr("data-page")){
				getDataList($(this).attr("data-page"));
			}
		});
    	//绑定左右拖动事件
    	/*
        var swiper = new Swiper('.strategies_list_wrap', {
            //pagination: '.swiper-pagination',
            //initialSlide :swiperIndex,
            preventClicks : true,
            paginationClickable: true,
            mousewheelControl : false,
            onSlideChangeStart:function(swiper){
            	getDataList(2);
    		}
        });	*/
    	/*var _OldX = 0;//初始化鼠标位置
    	$(".strategies_list_wrap").on({
    		mousedown: function(e){
    			_OldX = e.pageX;
//    			$(this).css("cursor","pointer");
                ////console.log("down:"+e.pageX);
    			e.stopPropagation();
            },
            mousemove: function(e){
    			e.stopPropagation();
            },
            mouseup: function(e){ 
//    			$(this).css("cursor","");
            	if (e.pageX<_OldX){
            		getDataList(currPage+1);
//            		////console.log("left");
            	}else if (e.pageX>_OldX){
            		getDataList(currPage-1);
//            		////console.log("right");
            	}
            	e.stopPropagation();
//            	////console.log("up:"+e.pageX);
            }
	    });*/
        //初始化，获取第一页数据
    	getDataList(1);
    	//--- 分页列表数据显示定义  结束 ---
    	
    	/**
    	 * Publish / Draft 切换
    	 * 
    	 */
    	$('.application-information-tab li').on('click',function(){
    		$(this).toggleClass('tab-active');
    		$(this).siblings().toggleClass('tab-active');
    		$('.applications-tabcontrol').toggleClass('tabactive');
    		$('.applications-tabcontrol1').toggleClass('tabactive');
    		$('.tabcut').toggleClass('ifa-more-ico-hidden');
    		if($(this).data('active')=='publish'){
    			$scope.isPublic = 1,
    			$scope.status = '';
    			getDataList(1);
    		}else if($(this).data('active')=='draft'){
    			$scope.isPublic = '',
    			$scope.status = 0;
    			getDataList(1);
    		}
    	});


	    //按字母显示二级选择项
	    $("#letter_choice li").on("mousemove",function(){
	        $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
	
	        var this_letter = $(this).attr("data-letter"),
	            letter_search_choice = $("#letter_search_choice").children(),
	            choice_lenght = letter_search_choice.length;
	        for (var k = 0 ; k< choice_lenght; k++){
	        	if( this_letter.indexOf(funds_logo.eq(k).attr("data-letter"))>=0 ){
	                letter_search_choice.eq(k).show();
	            }else{
	                letter_search_choice.eq(k).hide();
	            }
	        }
	        if($(this).hasClass("funds_all") && $(this).hasClass("funds_aloac")){
	            $(".select_choice li").removeClass("select_choice_active");
	            $(this).removeClass("funds_aloac");
	            var selection_criterialenght = $(".selection_criteria li").length;
	            for(var i = 0; i < selection_criterialenght ;i++){
	                if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
	                    $(".selection_criteria li").eq(i).addClass("thisremove");
	                }
	            }
	            $(".thisremove").remove();
	            searchList(this);
	        }
	    });
	    //选定字母下的二级选项，允许多选
	    $(".select_choice li").on("click",function(){
			if($(this).hasClass("select_choice_active") ){
				var selection_criterialenght = $(".selection_criteria li").length;
				for(var i = 0; i < selection_criterialenght ;i++){
					if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value") ){
						$(".selection_criteria li").eq(i).remove();
					}
				}
				$(this).removeClass("select_choice_active");
			}else{
				if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
					$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") +'">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
				}
				$(this).addClass("select_choice_active");
			}
			$("#select_choice .funds_all").addClass("funds_aloac");
			
			// 解决重复请求的问题
	        searchList(this);
	
		});
	    
	    //选定其他类型的条件
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
	        	searchList(self);
	        }
	        ,100);
	    });
	    
	    //选定tab
	    $(".ifa_search_tab li").on("click",function(){
	    	$(".ifa_search_tab li").removeClass("now")
	    	
	    	$(this).addClass("now");
	        var value = $(this).attr("data-value");
	        $("#status").val(value);
	        //alert($("#status").val());
	        
	        Initialization();
	    });
	    
	    //排序点击
		$(".funds_ifalist_sorting li").on("click",function(){
			$(".funds_ifalist_sorting li").removeClass("funds_active")
			$(this).addClass("funds_active");
			var currentSort = $(this).attr("currentSort");

			//清空所有排序选择
			$(".funds_ifalist_sorting li").each(function(){
				var removeSort = $(this).attr("currentSort");
				$("#"+removeSort).val("");
			});
			
			if( $(this).find(".funds_arrow_top").hasClass("funds_top_active") ){
				$('.funds_arrow_down').removeClass("funds_down_active");
				$('.funds_arrow_top').removeClass("funds_top_active");
				$(this).find(".funds_arrow_down").addClass("funds_down_active");
				$("#"+currentSort).val("desc");
			}else if( $(this).find(".funds_arrow_down").hasClass("funds_down_active") ){
				$('.funds_arrow_down').removeClass("funds_down_active");
				$('.funds_arrow_top').removeClass("funds_top_active");
				$(this).find(".funds_arrow_top").addClass("funds_top_active");
				$("#"+currentSort).val("asc");
			}else{
				$('.funds_arrow_down').removeClass("funds_down_active");
				$('.funds_arrow_top').removeClass("funds_top_active");
				$(this).find(".funds_arrow_down").addClass("funds_down_active");
				$("#"+currentSort).val("asc");
			}
			
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
	    /*
		$("#inputFromDate").on("onblur",function(){
			alert($(this).val());
			if (!$(this).val()) return;
			$("#fromDate").val($(this).val());
			
			searchData = $("#paramsForm").serialize();
		 	Initialization();
		})*/
		
		$(".icon_search").on("click",function(){
			//var fundName = $("#searchKeyBtn").val();
			//document.getElementById("keyWord").value=fundName;
			//searchData = $("#paramsForm").serialize();
		 	Initialization();
		})
		
		$("#searchKeyWord").keyup(function(event){
	      	 if(event.keyCode == 13){
	        	document.getElementById('searchKeyBtn').click()
	        }
	    });		
		
	    $(".selection_criteria").on("click",".selection_delete",function(){
	        $(this).parent().remove();
	        var funds_all_lenght = $('.funds_all').length;
	        for( var i = 0; i < funds_all_lenght ; i++){
	            if($(this).parent().attr("data-name") == "region"){
	                var len = $("#letter_search_choice li").length;
	                for(var j = 0 ; j < len ;j++){
	                    if( $(this).parent().attr("data-value") ==  $("#letter_search_choice li").eq(j).attr("data-value") ){
	                        $("#letter_search_choice li").eq(j).click();
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
	    
    	function searchList(searchLi){
    		var dataName = $(searchLi).attr("data-name");
    		var dataValue = $(searchLi).attr("data-value");
    		
    		if(!dataValue)dataValue="";
    		
    		if("period"==dataName){
    			$("#period").val(dataValue);
    		}
    		
    		if("sector"==dataName){
    			$("#sectors").val(dataValue);
    		}
    		
    		//alert(dataValue);
    		if("region"==dataName){
				var regions = "";
				//var houseLength = $(".select_choice_active").length-1;
				$(".select_choice_active").each(function(index,element){
					regions += $(this).attr("data-value") + ",";
				})
				document.getElementById("regions").value=regions;
			}
    		
		 	Initialization();
    	}
    	
    	function Initialization(){
    		// 初始化数值
    		var iPAGE = 1; //第N页数据
    		searchData = $("#paramsForm").serialize();
    		getDataList(1);
    	}
    	
    	var itemId = null;
    	/**
         * 删除投资策略
         * */
        $(".myProposalList").on("click",".delete",function(){
        	itemId = $(this).attr("itemId");
        	if(!!itemId){
        		layer.confirm(langMutilForJs['global.confirm.delete'], {
        			btn: [langMutilForJs['global.confirm'],langMutilForJs['global.cancel']], //按钮
        			title:''
        		}, function(){
        			$.ajax({
        				url:base_root+"/front/strategy/info/delStrategy.do",
        				data:{"itemId":itemId},
        				type:"post",
        				dataType:"json",
        				success:function(data){
        					if(data.result){
        						$scope.dataList ='';
        						Initialization();
        					}
        				}
        			})
        			layer.closeAll();
        		}, function(){
        			
        		});
        	}
        });
    	/**
 	    * 置顶
 	    * */ 
     	$(".myProposalList").on("click",".up",function(){
     		var itemId = $(this).attr("itemId");
     		setOverhead(itemId, "1");
     	});
    	/**
  	    * 取消置顶
  	    * */ 
      	$(".myProposalList").on("click",".down",function(){
      		var itemId = $(this).attr("itemId");
      		setOverhead(itemId, "0");
      	});
     	function setOverhead(itemId, type){
     		if(!!itemId && !!type){
    			$.ajax({
    				url:base_root+"/front/strategy/info/setOverhead.do",
    				data:{"itemId":itemId,"type":type},
    				type:"post",
    				dataType:"json",
    				success:function(data){
    					if(data.result){
    						$scope.dataList ='';
    						Initialization();
    					}
    				}
    			})
        	}
     	}
     	
    	/**
	    * 访客列表
	    * */ 
    	$(".myProposalList").on("click",".visited-click",function(){
    		var count = $(this).find('.visitCount').text();
    		if(Number(count)>0){
    			itemId = $(this).data("itemId");
        		getFriendList();
    		}
    	});
    	function getFriendList(){
    		$scope.friendList = '';
    		$scope.base_root=base_root;
    		var rows = 12,
    			moduleType = 'strategy';
    		var url = base_root +"/front/strategy/info/getMyVisitors.do?dateStr="+new Date().getTime();
    	 		//data =  "insightId="+ itemId;
    			//控制数据是否加载完成
    			$http({
                  url: url,
                  method: 'POST',
                  params : {
                	  page:$scope.friendListPage,
                	  rows:rows,
                	  relateId:itemId,
                	  moduleType:moduleType
                  },
                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
             	 }).success(function(response){
             		$scope.friendList =  response.list;
             		var page = response.page;
             		$scope.friendListPage = page;
    				$('.visitedconlayer').show();
    	    		$('.visitedcon').show();
    	    		$('.visitedconlayer').css('height',document.body.scrollHeight+'px');
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
        * 投资组合详情
        */
        $(".myProposalList").on("click",".view",function(){
        	itemId = $(this).attr("itemId");
        	window.location.href= base_root+"/front/strategy/info/addStart.do?id="+itemId;
        });
    	
    }]);//angular.js end
    
    function substring(str,len){
        var rst = str;
        if(str.length>len){
            rst = str.substring(0,len) + '...';
        }
        return rst;
    }
    
    /**
     *访客列表关闭
     *@author scshi 
     * */
    $(".space-mask-close").on("click",function(){
		$(".ifa-space-popup").hide();
	});
    
    /*********************************************/
	$('.visited').on('click',function(){
		alert();
		$('.visitedconlayer').show();
		$('.visitedcon').show();
	});
	$('.visitedconlayer').on('click',function(){
		$('.visitedconlayer').hide();
		$('.visitedcon').hide();
	});
	$('.applications-information-choose li').on('click',function(){
		$(this).toggleClass('applications-information-chooseac');
		$(this).siblings().toggleClass('applications-information-chooseac');
	});
	$('.mylist-new-bn').on('click',function(){
		window.location.href = base_root+"/front/strategy/info/addStart.do?dateStr=" + new Date().getTime();
	});
    
    $("#fundName").keyup(function(event){
      	if(event.keyCode == 13){
	        document.getElementById('searchKeyBtn').click()
	    }
	});	
    
    
    
    
});