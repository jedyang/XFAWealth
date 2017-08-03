/**
 * openAccountbasic.js basicinfo
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-06
 */

define(function(require) {
	var $ = require('jquery'),
	angular = require('angular');
 	// tab切换
 	$(".OpenAccount_tab li").on("click",function(){
 		$(".OpenAccount_tab li a").removeClass("now").eq($(this).index()).addClass("now");
 		$(".OpenAccount_BasicInfo_div > div").hide().eq($(this).index()).show();
 	});
    $("#btn_next").on("click",function(){
            window.location.href = "/index.php?r=member/accountdoc";
    });
    $(".BasicInfo_title_ico").on("click",function(){
        $(this).parents(".BasicInfo_title").find(".BasicInfo_title_ico").hide().end().find(".BasicInfo_title_ico_hide").show();
        $(this).parents(".BasicInfo_show").stop().animate({ 
            height: "77px", 
          }, 300 );
    });
    $(".BasicInfo_title_ico_hide").on("click",function(){
        $(this).parents(".BasicInfo_title").find(".BasicInfo_title_ico").show().end().find(".BasicInfo_title_ico_hide").hide();
        $(this).parents(".BasicInfo_show").stop().animate({ 
            height: "100%", 
          }, 300 );
    });
  	var mybase = angular.module('mybase', []);
        mybase.controller('myCtrl', function($scope) {  
            // 初始化数据     
            $scope.dataList = '';

             $.ajax({
                  type:"POST",
                  url:WmesUrl + "/backend/index.php?r=user/get-user",
                  datatype:"JSON",
                  data:{inputData : '{"loginID":"chungabcbbb"}'},
                  success:function(response){
                      console.log(response);
                      //填充数据
                      $scope.dataList = response.data;
                      //更新视图 
                      $scope.$apply();
                  },
                  error:function(response){
                    console.log(response);
                  }
              });
              
      });
});