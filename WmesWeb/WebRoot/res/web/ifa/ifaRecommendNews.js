/**
 * myList.js
 * 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	
	var $ = require('jquery');
	require("scrollbar");
	require('datepick');
	require('layer');

	$('.funds_search_wrap').perfectScrollbar();
	
	new JsDatePick({
	      useMode:2,
	      target:"recommend-date-to",
	      dateFormat:"%Y-%m-%d"
	    });
	     new JsDatePick({
	      useMode:2,
	      target:"recommend-date-from",
	      dateFormat:"%Y-%m-%d"
	    });
	     
	 	
	var angular = require('angular');	
	var mybase = angular.module('mySearch', []);
	var searchData = null;
	
    mybase.controller('Searchctrl', ['$scope', '$http', function($scope, $http) {
//  	var url = base_root + "/front/ifa/space/ifarecommendnewsJson.do",
			iPAGE = 1, // 第N页数据
		 is_finish = true,//当前数据是否加载完成
		 page_bol = true;//总页数控制
		
		// 初始化数据
		$scope.dataList = '';

	// 数字调用
	$scope.counter = Array;
	
	// 监听视图渲染是否结束
	$scope.checkLast = function($last){
		if($last){

			// 删除渲染效果
			$(".animated").one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
		    		$(".animated").removeClass();
		    });

			// you can also do something

		}
	}


	var rows = 10  // 固定每次拿10条数据
		// 正常拿数据
    	function getDataList(){
    		var url = base_root + "/front/ifa/space/ifarecommendnewsJson.do",
    			data =  "rows="+ rows +"&page=" + iPAGE +"&"+searchData;
    			$http({
                  url: url,
                  method: 'POST',
                  data : data,
                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
             	 })
    			.success(function(response){
                      if(response.list.length > 0){
                    	  $(".dataListTr").show()
                    	  $(".no_list_tips").hide();
                    		// 总页数
                    		var sumPage = Math.ceil(response.total / rows);
                    	  if(iPAGE > sumPage){
                    		  return;
                    	  }
                      		 if( iPAGE === 1 ){
                      		 	 $scope.dataList = '';
                                 $scope.dataList =  response.list;
                              }else{
                                 $scope.dataList = $scope.dataList.concat(response.list);
                              }
                          
	                      		if(iPAGE >= sumPage){
	                      			page_bol = false;
	                      		}
                  		}else{
                 			 if( iPAGE === 1 ){
                 				 $scope.dataList = '';
                 				 $(".dataListTr").hide()
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

    	function GetScroll(){
    		var docheight = $(window).scrollTop() + $(window).height(),
    			listheight = $('.funds_selected_wrap').offset().top + $('.funds_selected_wrap').height() + $(".wmes_top").height();
    		
    		if( docheight > listheight ){
    			scrollBol = true;
    			
    		}else{
    			scrollBol = false;
    		}

			if (scrollBol && is_finish && page_bol ){
				getDataList();
			}

    	}


	    // 按字母显示二级选择项
	    $("#letter_choice li").on("mousemove",function(){
	        $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
	
	        var this_letter = $(this).attr("data-letter"),
	            letter_search_choice = $("#letter_search_choice").children(),
	            choice_lenght = letter_search_choice.length;
	        for (var k = 0 ; k< choice_lenght; k++){
	            if( this_letter == letter_search_choice.eq(k).attr("data-letter") ){
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
	    // 选定字母下的二级选项，允许多选
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
	    
	    // 选定其他类型的条件
	    var listTime;
	    $(".funds_choice li").on("click",function(){
	        clearTimeout(listTime);
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
	
	        // 解决重复请求的问题
	        var self = this;
	        listTime=setTimeout(function(){
	        	searchList(self);
	        }
	        ,100);
	    });
	    
	    
	    $(".recommend-date-button").on("click",function(){

			var selection_criterialenght = $(".selection_criteria li").length;
			for(var i = 0; i < selection_criterialenght ;i++){
					if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
						$(".selection_criteria li").eq(i).remove();
					}
			}
			$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
			if($("#recommend-date-to" ).val() != "" && $("#recommend-date-from").val() != ""){
				$(".funds_title_selection").before('<li data-value="' + $("#recommend-date-to" ).val() + ' to ' + $("#recommend-date-from" ).val()  + '" data-name="' + $(this).attr("data-name") + '">' + $("#recommend-date-to" ).val() + ' to ' + $("#recommend-date-from" ).val()  + '<span class="selection_delete"></span></li>')
				$("#period").val($("#recommend-date-to" ).val() + ' to ' + $("#recommend-date-from" ).val());
			}else{

			}
			searchData = $("#paramsForm").serialize();
			Initialization();
		});
		
	   
		
		$(".icon_search").on("click",function(){
			var fundName = $("#searchKeyWord").val();
			document.getElementById("keyWord").value=fundName;
			searchData = $("#paramsForm").serialize();
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
    		
    		// alert(dataValue);
    		if("region"==dataName){
				var regions = "";
				// var houseLength = $(".select_choice_active").length-1;
				$(".select_choice_active").each(function(index,element){
					regions += $(this).attr("data-value") + ",";
				})
				document.getElementById("regions").value=regions;
			}
    		
    		$scope.dataList ='';
			Initialization();
    	}
    	
    	function Initialization(){
    		// 初始化数值
    		iPAGE = 1; 
    		is_finish = true;
    		page_bol = true;
    		searchData = $("#paramsForm").serialize();
    		getDataList();
    		
    		
    	}
    	
    	/**
		 * 访客列表
		 */ 
    	$(".recommend-news-list").on("click",".recommend-news-bottom-ico",function(){
    		itemId = $(this).attr("itemId");
    		getFriendList();
    	});
    	function getFriendList(){
    		$scope.friendList = '';
    		var url = base_root +"/front/ifa/space/newsVisitorList.do",
		 		data =  "itemId="+itemId;
    			// 控制数据是否加载完成
    			$http({
                  url: url,
                  method: 'POST',
                  data : data,
                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
             	 })
    			.success(function(response){
					if(response.length > 0){
						$scope.friendList =  response;
						$(".ifa-space-popup").show();
					}
					
                });
    	}
    	/**
		 * 置顶/取消置顶
		 */ 
    	$(".recommend-news-list").on("click",".recommend-ico-down",function(){
    		var itemId = $(this).parent().attr("itemId");
    		var overhead=$(this).attr("overhead");
    		setOverhead(itemId, overhead);
    	});
      	function setOverhead(itemId, type){
   		if(!!itemId && !!type){
  			$.ajax({
  				url:base_root+"/front/ifa/space/newsOverhead.do",
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
   	var itemId = null;
 	/**
	 * 删除推荐新闻
	 */
     $(".recommend-news-list").on("click",".recommend_del",function(){
     	itemId = $(this).parent().attr("itemId");
     	if(!!itemId){
     		layer.confirm('确定删除该新闻推荐？', {
     			btn: ['确定','取消'] // 按钮
     		}, function(){
     			$.ajax({
     				url:base_root+"/front/ifa/space/delRecommendNews.do",
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
     
    	
    	
    }]);// angular.js end
    
    function selection(){
        var _thisLenght =  $(".selection_criteria").children().length
        
        if( _thisLenght != 1  ){
            $(".funds_title_selection").css('display','inline-block');
        }else{
            $(".funds_title_selection").css('display','none');
        }
    }
    
    
    function substring(str,len){
        var rst = str;
        if(str.length>len){
            rst = str.substring(0,len) + '...';
        }
        return rst;
    }
    
    /**
	 * 访客列表关闭
	 * 
	 * @author scshi
	 */
    $(".space-mask-close").on("click",function(){
		$(".ifa-space-popup").hide();
	});
});