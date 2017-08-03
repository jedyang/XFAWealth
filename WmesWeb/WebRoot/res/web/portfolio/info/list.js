/**
 * list.js 
 * @author michael
 * @date: 2016-08-18
 */

define(function(require) {
	
	var $ = require('jquery');
	require('layer');
//	var grid_selector = "#tableList";
	
	var angular = require('angular');	
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
		// 正常拿数据	 
    	function getDataList(){
    		var url = base_root + "/front/portfolio/info/myListJson.do",
    			data =  "rows="+ rows +"&page=" + iPAGE +"&"+searchData;
    			//控制数据是否加载完成
    			var is_finish = false;
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
                      		 if( iPAGE === 1 ){
                      		 	 $scope.dataList = '';
                                 $scope.dataList =  response.list;
                              }else{
                                 $scope.dataList = $scope.dataList.concat(response.list);
                              }
                  		}else{
                 			 if( iPAGE === 1 ){
                 				 $(".dataListTr").hide()
                 				 $(".no_list_tips").show();
                  			 }
                  		}
		    			iPAGE++;
		    			$scope.datatotal = response.total;
                });
    	}
    	getDataList();



	    //按字母显示二级选择项
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
			var removeSort = $(this).attr("removeSort");
			
			if( $(this).find(".funds_arrow_top").hasClass("funds_top_active") ){
				$('.funds_arrow_down').removeClass("funds_down_active");
				$('.funds_arrow_top').removeClass("funds_top_active");
				$(this).find(".funds_arrow_down").addClass("funds_down_active");
				$("#"+removeSort).val("");
				$("#"+currentSort).val("desc");
			}else if( $(this).find(".funds_arrow_down").hasClass("funds_down_active") ){
				$('.funds_arrow_down').removeClass("funds_down_active");
				$('.funds_arrow_top').removeClass("funds_top_active");
				$(this).find(".funds_arrow_top").addClass("funds_top_active");
				$("#"+removeSort).val("");
				$("#"+currentSort).val("asc");
			}else{
				$('.funds_arrow_down').removeClass("funds_down_active");
				$('.funds_arrow_top').removeClass("funds_top_active");
				$(this).find(".funds_arrow_down").addClass("funds_down_active");
				$("#"+removeSort).val("");
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
    		iPAGE = 1;
    		searchData = $("#paramsForm").serialize();
    		getDataList();
    	}
    	
    	var itemId = null;
    	/**
         * 删除投资策略
         * */
        $(".ifa_list").on("click",".delete",function(){
        	itemId = $(this).attr("itemId");
        	if(!!itemId){
        		layer.confirm('确定删除该投资策略？', {
        			btn: ['确定','取消'] //按钮
        		}, function(){
        			$.ajax({
        				url:base_root+"/front/portfolio/info/delPortfolio.do",
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
	    * 访客列表
	    * */ 
    	$(".ifa_list").on("click",".visitorsBtn",function(){
    		itemId = $(this).attr("itemId");
    		getFriendList();
    	});
    	function getFriendList(){
    		$scope.friendList = '';
    		var url = base_root +"/front/ifa/myInsight/viewList.do",
		 		data =  "insightId="+ itemId;
    			$http({
                  url: url,
                  method: 'POST',
                  data : data,
                  headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
             	 })
    			.success(function(response){
    				is_finish = true;
					if(response.length > 0){
						$scope.friendList =  response;
					}
					$(".ifa-space-popup").show();
                });
    	}
    	
    	
    }]);//angular.js end
    
	/**
	    * 置顶
	    * */ 
  	$(".ifa_list").on("click",".up",function(){
  		var itemId = $(this).attr("itemId");
  		setOverhead(itemId, "1");
  	});
 	/**
	    * 取消置顶
	    * */ 
   	$(".ifa_list").on("click",".down",function(){
   		var itemId = $(this).attr("itemId");
   		setOverhead(itemId, "0");
   	});
  	function setOverhead(itemId, type){
  		if(!!itemId && !!type){
 			$.ajax({
 				url:base_root+"/front/portfolio/info/setOverhead.do",
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
});