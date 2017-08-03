    
define(function(require) {

    var $ = require('jquery');
    require("echarts");
    require("bootstrapDialog");
    var angular = require('angular');    
    var Loading = require('loading');
    var mybase = angular.module('ifaTable', []);
    sessionStorage.removeItem("clientdetailtab");
    
    var searchData = null;
    var oLoading = new Loading($(".client_list"));    
    mybase.controller('ifaTableCtrl', ['$scope', '$http', function($scope, $http) {
        var iPAGE = 1;
        // 初始化数据
        $scope.dataList = '';
        // 数字调用
        $scope.counter = Array;
        // 监听视图渲染是否结束
        $scope.checkLast = function($last){
            if($last){
                setTimeout(function(){
                    clientCharts();// 初始化图表
                },100)
            }
        }
        $scope.defColor=$("#defColor").val();
        $scope.currency=$("#currency").val();
        var bellType="";
  
        // 固定每次拿9条数据
        var rows = 12;
        // 正常拿数据
        function getDataList(){
            $(".client_list").show();
            $(".clickmore").hide();
            oLoading.show();
            var    data="";

            // 获取排序
            var element=null;
            if($(".client-list-tab").find(".down_active").length>0){
                element=$(".client-list-tab").find(".down_active");
                
            }else if($(".client-list-tab").find(".top_active").length>0){
                element=$(".client-list-tab").find(".top_active");
            }
            if(null!=element && element!=undefined){
                var sort=$(element).attr("sort");
                var order=$(element).attr("order");
                data="sort="+sort+"&order="+order+"&";
            }
            var url = base_root + "/console/ifafirm/client/ifaFirmClientListJson.do";
            data +=  "rows="+ rows +"&page=" + iPAGE +searchData+"&checkIsMyFollow=1";
            
            $http({
              url: url,
              method: 'POST',
              data : data,
              headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
              })
            .success(function(response){
                oLoading.hide();
                $scope.statistic=$(".client-statistic-choose").find("option:selected").text();
                $scope.statisticValue=$(".client-statistic-choose").val();
                ////console.log(response.list.length);
                if(response.list.length > 0){
                    $(".clickmore").show();
                    $(".dataListTr").show();
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
                        $(".clickmore").hide();
                      }
                }else{
                     if( iPAGE === 1 ){
                         $scope.dataList = '';
                     $(".dataListTr").hide()
                     $(".no_list_tips").show();
                         }
                     $(".clickmore").hide();
                }
                iPAGE++;
                $scope.datatotal = response.total;
              
            });
            
            // 加载货币
            currencyType();
        }
        Initialization();
        
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
                //设置选定项
                /*$.each(result.result,function(i,n){
                    if(n.code == $scope.toCurrency){
                        $('#currency').val(n.name);
                        return false;
                    }
                });*/
            });
        }
        
        $(".clickmore").on("click",function(){
            getDataList();
        })

        function clientCharts(){
            $(".client-list-chart").each(function(){
                var selfData = eval($(this).attr("data-value"));
                var isLoad=$(this).attr("isLoad");
                if(isLoad==1)
                    return;
                
                // 设置颜色
                for(var item in selfData){
                    if( selfData[item].name =="fund" ){
                        selfData[item]['itemStyle'] = {normal:{color:'#4891ff'},emphasis:{}}
                    }else if(selfData[item].name =="stock"){
                        selfData[item]['itemStyle'] = {normal:{color:'#5470df'},emphasis:{}}
                    }else if( selfData[item].name =="bond" ){
                        selfData[item]['itemStyle'] = {normal:{color:'#40c7f3'},emphasis:{}}
                    }
                }

                var option = {
                    series: [
                    {
                        type:'pie',
                        label: {
                            normal: {
                                position: 'inner',
                                show:false,
                                formatter : "{b}\n{d}%"
                            }
                        },
                        itemStyle :　{

                            emphasis : {
                                show:false
                            }
                        },
                        data:selfData
                    }
                    ]
                };
                var myChart = echarts.init($(this)[0]);
                    myChart.setOption(option,true);
                    $(this).attr("isLoad",1);
            });
        }

        $(".client-list-more-choice").on("click",function(){
            $(this).toggleClass("client-list-more-active");
            if( $(this).hasClass("client-list-more-active")) {
                $(".client-more-screen-wrap").stop().animate({ 
                        height: "100%"
                 }, 300 );
                $(this).html(langMutilForJs['client.list.hideScreen']+'<span class="client-more-screen-ico"></span>');
            }else{
                $(".client-more-screen-wrap").stop().animate({ 
                        height: "0px"
                 }, 300 );
                $(this).html(langMutilForJs['client.list.showScreen']+'<span class="client-more-screen-ico"></span>');
            }
        });

        $(".funds_choice li").on("click",function(){
// if($(this).parent().hasClass("funds_logo_b")){
// return;
// }
            
            $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
            if($(this).hasClass("recommend-date-choice") && $(this).hasClass("fund_choice_active")){
                $(this).parents(".funds_choice").find(".recommend-other-wrap").addClass("recommend-other-block");
                return;
            }else{
                $(this).parents(".funds_choice").find(".recommend-other-wrap").removeClass("recommend-other-block");
            }
            
            /*
             * var selection_criterialenght = $(".selection_criteria
             * li").length; for(var i = 0; i < selection_criterialenght ;i++){
             * if($(".selection_criteria li").eq(i).attr("data-name") ==
             * $(this).attr("data-name") ){ $(".selection_criteria
             * li").eq(i).remove(); } }
             * 
             * 
             * 
             * $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
             * if($(this).attr("data-key") != undefined &&
             * $(this).attr("data-key") != ""){
             * $(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' +
             * $(this).attr("data-key") + '<span class="selection_delete"></span></li>') }
             */
            
            // 选择客户名字母段，不需刷新数据
            if($(this).parent().hasClass("client_name_choice") && $(this).attr("data-key")!="All"){
                return;
            }
            Initialization();
        });

        $(".ifaFirmName").on("click",function(){
            Initialization();
        });
        
        $(".ifaName").on("click",function(){
            Initialization();
        });
        
        $(".icon_search").on("click",function(){
            Initialization();
        });
        
        $(".wmes-menu-hide").on("click",function(){
					$(this).toggleClass("wmes-menu-hideac");
					if( $(this).hasClass("wmes-menu-hideac")) {
						$(".client-more-screen-wrap").stop().animate({ 
							height: "100%"
						}, 300 );
						$(".client-more-screen-wrap").css({'margin-top':'20px'});
					}else{
						$(".client-more-screen-wrap").stop().animate({ 
							height: "0px",margin:'0px'
						}, 300 );
					}
				});
				
        $(".client_name_choice li").on("click",function(){
            var _val = $(this).attr("data-key");
            if(_val == "All"){

                $("#client-name-wrap li").css("display","none");
                $("#client-name-wrap li").removeClass("fund_choice_active");
            }else{

                $("#client-name-wrap li").each(function(){
                    if(_val.indexOf($(this).attr("data-letter"))>=0){
                        $(this).css("display","inline-block")
                    }else{
                        $(this).css("display","none");
                    }
                });
            }
        });
        
        function Initialization(){
            // 初始化数值
            iPAGE = 1; // 第N页数据
        // searchList();
            $(".no_list_tips").hide();
            $scope.dataList = '';
            searchData = getSearchData();
            getDataList();
        }
        
        function getSearchData(){
            var strCondition="";
            var element=$(".funds_choice").find(".fund_choice_active");
            $.each(element,function(i,n){
                var dataName=$(this).attr("data-name");
                if(dataName=="Total Return"){
                    strCondition+="&totalReturn="+encodeURI($(this).attr("data-key"));
                }else if(dataName=="AuM"){
                    strCondition+="&aum="+$(this).attr("data-key");
                }else if(dataName=="group"){
                    strCondition+="&groupId="+$(this).attr("data-key");
                }else if(dataName=="ClientName"){
                    strCondition+="&clientId="+$(this).attr("data-value");
                }
            });
            var period=$(".bell-tab-active").attr("key");
            strCondition+="&period="+period;
            strCondition+="&cur="+$scope.currency;
            var statistic=$(".client-statistic-choose").val();
            strCondition+="&statistic="+statistic;
            strCondition+="&type="+bellType;
            
            // 获取关键词
            var ifaFirm = $("#ifaFirm").val();
            if (ifaFirm) strCondition+="&ifaFirmName="+ifaFirm;
            var ifa = $("#ifa").val();
            if (ifa) strCondition+="&ifaName="+ifa;

            return strCondition;
        }
        
        $(".client-list-title-bell")
            .on("mouseenter",function(){
                $(this).addClass("client-bell-active");
            })
            .on("mouseleave",function(){
                $(this).removeClass("client-bell-active");
            });
            
        $(".pipelin-bell-tab li").on("click",function(){
            $(this).siblings().removeClass("bell-tab-active").end().addClass("bell-tab-active");
        // var period=$(this).attr("key");
            getRemind();
            // $(".pipelin-bell-contents
            // li").hide().eq($(this).index()).show();
        });
        
        getRemind();
        function getRemind(){
            var period=$(".pipelin-bell-tab").find(".bell-tab-active").attr("key");
            $.ajax({
                type:"post",
                datatype:"json",
                url:base_root+"/front/crm/client/getRemindData.do",
                data:{period:period},
                success:function(json){
                    $(".birthNum").text(json.birthday);
                    $(".appiontNum").text(json.appoint);
                    $(".portfolioNum").text(json.portfolioPre);
                    $(".kycNum").text(json.kycPre);
                    if(Number(json.birthday)>0 || Number(json.appoint)>0 || Number(json.portfolioPre)>0 || Number(json.kycPre)>0){
                        $(".client-list-title-bell").removeClass("client-list-active-bell").addClass("client-list-active-bell");
                        if(Number(json.birthday)==0)
                            $(".birthNum").hide();
                        else
                            $(".birthNum").show();
                        
                        if(Number(json.appoint)==0)
                            $(".appiontNum").hide();
                        else
                            $(".appiontNum").show();
                        
                        if(Number(json.portfolioPre)==0)
                            $(".portfolioNum").hide();
                        else
                            $(".portfolioNum").show();
                        
                        if(Number(json.kycPre)==0)
                            $(".kycNum").hide();
                        else
                            $(".kycNum").show();
                    }else{
                        $(".client-list-title-bell").removeClass("client-list-active-bell");
                    }
                }
            })
        }
        
        $(".client-cur-choose").on("change",function(){
            var cur=$(this).val();
            window.location.href=base_root+"/console/ifafirm/client/clients.do?cur="+cur+"&r="+Math.random();
        });
        $(".client-statistic-choose").on("change",function(){
            Initialization();
        });
        
        
//      $('.arrow_down').on("click",function(){
//          $('.arrow_down').removeClass("down_active");
//          $('.arrow_top').removeClass("top_active");
//          $(this).parents("li").siblings().removeClass("recommend-sort-active").end().addClass("recommend-sort-active");
//          $(this).addClass("down_active");
//          Initialization();
//      });
//      $('.arrow_top').on("click",function(){
//          $('.arrow_down').removeClass("down_active");
//          $('.arrow_top').removeClass("top_active");
//          $(this).parents("li").siblings().removeClass("recommend-sort-active").end().addClass("recommend-sort-active");
//          $(this).addClass("top_active");
//          Initialization();
//      });
        
        $(".recommend-switch-tab li").on("click",function(){
	    	$(".recommend-switch-tab li").removeClass("recommend-sort-active");
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
	    
        $(".pipelin-bell-mesWrap").on("click",function(){
            bellType=$(this).attr("data-type");
            Initialization();
        });
        
        $(document).on("click",".client-list-rows-title,.client-list-number,.client-aum-num",function(){
            var cusId=$(this).attr("cusId");
            var memberId= $(this).attr("cusMemberId");
            //window.open(base_root+"/front/crm/pipeline/clientDetail.do?customerId="+cusId+"&customerMemberId="+memberId);
            infoFormDialog(memberId);
        })
    }])
        
    //点击新增
    $('#btn_add_client').on('click',function(){
    	//window.location.href=base_root+"/console/ifafirm/investor/input.do?";
        infoFormDialog();
    });
    
    //弹出页面
    function infoFormDialog(id){
        var src = base_root+"/console/ifafirm/investor/input.do?r="+ Math.random();
        if (id) src += "&id="+id;
        BootstrapDialog.show({
            title : langMutilForJs['member.individual']+" "+langMutilForJs['member.detail'],
            cssClass : 'login-dialog',
            type : BootstrapDialog.TYPE_PRIMARY,
            size : BootstrapDialog.SIZE_WIDE,
            draggable : true,
            //message : $('<div><iframe scrolling="no" frameborder="0" name="Info1" onload="this.height=Info1.document.body.scrollHeight" width="100%" src="'+src+'"></iframe></div>').load(),
            message : $('<div></div>').load(src),
            buttons : [{
                label : langMutilForJs['global.close'],
                action : function(dialogItself) {
                    dialogItself.close();
                }
            }]
        });
    }
    
    //触发弹窗关闭
    $('#btnCloseIframe').click(function() {
        $(".close").click();            
    });
    //绑定查询按钮事件
    $("#btnSearch").click(function () {                
        $(".icon_search").click();
    });    
});