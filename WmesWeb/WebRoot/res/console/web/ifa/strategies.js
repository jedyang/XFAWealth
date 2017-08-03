define(function(require) {
    var $ = require('jquery');
    require("echarts");
    require('layer');
    require("scrollbar");
    var Loading = require('loading');
    var oLoading = new Loading($(".strategies_list"));    
    var searchData = null;
    var iPAGE = 1; //总页数控制
    var total=0;
    var perPage=9;
    initialization();
    
    var strategylistval = "";
    if(strategylistval=="show"){
        $(".client-more-screen-wrap").css({'height':'100%','margin-top':'20px'});
        $('.funds_list_selected').removeClass('ifa-more-ico-hidden');
        $(".wmes-menu-hide").toggleClass("wmes-menu-hideac");
    }else{
        $(".client-more-screen-wrap").css({'height':'0','margin-top':'0px'});
        $('.funds_list_selected').addClass('ifa-more-ico-hidden');
    };
    
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
    
    //点击每个选项，在下面的已选方案中添加该选项
    $(".funds_all_Sector").on("click", function() {
        $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
        for(i=1;i<21;i++){
            $(".selection_criteria li[data-name='Sector"+i+"']").remove();
        }
    });
    
    $('.funds_choice_amend li').on('click',function(){
        if($(this).attr('data-value')=='Sector_00'){
            $('.funds_choice_amend li').css('color','#000');
        }else{
            $(this).css('color','#4ba6de');
        }
        
    });
    

    //按字母显示二级选择项
    $("#letter_choice li").on("mousemove", function() {
        $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");

        var this_letter = $(this).attr("data-letter"),
            letter_search_choice = $("#letter_search_choice").children(),
            choice_lenght = letter_search_choice.length;
        for(var k = 0; k < choice_lenght; k++) {
            if(this_letter == letter_search_choice.eq(k).attr("data-letter")) {
                letter_search_choice.eq(k).show();
            } else {
                letter_search_choice.eq(k).hide();
            }
        }
        if($(this).hasClass("funds_all") && $(this).hasClass("funds_aloac")) {
            $(".select_choice li").removeClass("select_choice_active");
            $(this).removeClass("funds_aloac");
            var selection_criterialenght = $(".selection_criteria li").length;
            for(var i = 0; i < selection_criterialenght; i++) {
                if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name")) {
                    $(".selection_criteria li").eq(i).addClass("thisremove");
                } else if($(".selection_criteria li").eq(i).attr("data-name")) {

                }
            }
            $(".thisremove").remove();
            // initialization();
        }
    });
    //选定字母下的二级选项，允许多选
    $(".select_choice li").on("click", function() {
        if($(this).hasClass("select_choice_active")) {
            var selection_criterialenght = $(".selection_criteria li").length;
            for(var i = 0; i < selection_criterialenght; i++) {
                if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value")) {
                    $(".selection_criteria li").eq(i).remove();
                }
            }
            $(this).removeClass("select_choice_active");
        } else {
            if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
                $(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
            }
            $(this).addClass("select_choice_active");
        }
        $("#select_choice .funds_all").addClass("funds_aloac");

        // 解决重复请求的问题
        initialization();

    });
    
    
    //选定其他类型的条件
    var listTime;
    $(".funds_choice li").on("click", function() {
        //如当前是“所有”，则设为“背景”样式
        if($(this).hasClass('funds_all')) {
            $(this).addClass('fund_choice_active2');
        } else {
            $(this).closest('.funds_choice').find('.funds_all').removeClass('fund_choice_active2');
        };
        clearTimeout(listTime);
        if($(this).parent().hasClass("funds_logo_b")) {
            return;
        };
        
        //查找已选条件，把与当前相同的条件类型的已选项去掉
        var _this = $(this);
        var currDataName = $(this).attr("data-name");//条件类型
        $(".selection_criteria li").each(function(){
            if(_this.hasClass('funds_all')) {//选择“全部”时处理多选的情况
//                console.log($(this).attr("data-name"));
                if ( $(this).attr("data-name").indexOf(currDataName) == 0)
                    $(this).remove();//处理多选的条件
            }else if(currDataName == $(this).attr("data-name")) {
                $(this).remove();//处理单选的条件
            }
        })
        
        
        //添加当前选项到已选条件（“所有”选项除外，data-key=""）
//        if($(this).hasClass("fund_choice_active1")) {//再点一次已选项，则取消选择，移除已选条件
//            console.log("a");
//            var selection_criterialenght = $(".selection_criteria li").length;
//            for(var i = 0; i < selection_criterialenght; i++) {
//                if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value")) {
//                    $(".selection_criteria li").eq(i).remove();
//                }
//            }
//            $(this).removeClass("fund_choice_active1");
//            $(this).removeClass("fund_choice_active");
//        }else{//第一次点，设置选定，并添加到已选条件中
            $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
            if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
                $(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
            }
//            $(this).addClass("fund_choice_active1");
//        }
        
        // 解决重复请求的问题
        var self = this;
        listTime = setTimeout(function() {
            initialization();
        }, 100);
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
            $(".funds_logo_choice li").removeClass("fund_logo_active1");//去除已选样式
            $(this).removeClass("funds_aloac");
            var selection_criterialenght = $(".selection_criteria li").length;
            for(var i = 0; i < selection_criterialenght; i++) {
                if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name")) {
                    $(".selection_criteria li").eq(i).addClass("thisremove");
                }
            }
            $(".thisremove").remove();
            initialization();
        }
    });
    
    //图标类选项条件控制
    $(".funds_logo_choice li").on("click", function() {
        if($(this).hasClass("fund_logo_active1")) {//再点一次已选项，则取消选择
            var selection_criterialenght = $(".selection_criteria li").length;
            for(var i = 0; i < selection_criterialenght; i++) {
                if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value")) {
                    $(".selection_criteria li").eq(i).remove();
                }
            }
            $(this).removeClass("fund_logo_active1");
        } else {//第一次点选项，记录为已选
            if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
                $(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
            }
            $(this).addClass("fund_logo_active1");
        }
        $("#funds_logo_choice .funds_all").addClass("funds_aloac");
        $("#funds_logo_choice .funds_all").removeClass("fund_choice_active2");
        $("#funds_logo_choice .funds_all").removeClass("fund_choice_active");

        initialization();
    });

    //执行清除方案点击操作
    $(".funds_title_selection").on("click", function() {
        $(".selection_criteria li").remove();

        $(".fund_logo_active").removeClass("fund_logo_active");

        $(".fund_choice_active").removeClass("fund_choice_active");
        $(".funds_all").click();
        $('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active2');
        $('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active');
        $('#funds_logo li').removeClass('fund_logo_active1');
        $("#funds_logo").children().hide();
        $("#listForm").find("input").val("");
        initialization();
    });
    
    /**
     * 显示清除所有搜索条件按钮
     * */
    function selection() {
        var _thisLenght = $(".selection_criteria").children().length;
        if(_thisLenght != 1) {
            $(".funds_title_selection").css('display', 'inline-block');
        } else {
            $(".funds_title_selection").css('display', 'none');
        }
    };
    
    /**
     * 搜索条件取消点击
     */
    $(".selection_criteria").on("click", ".selection_delete", function() {
        $(this).parent().remove();
        var funds_all_lenght = $('.funds_all').length;
        for(var i = 0; i < funds_all_lenght; i++) {
            if($(this).parent().attr("data-name") == "regions") {
                var fundslenght = $("#funds_logo li").length;
                for(var funds = 0; funds < fundslenght; funds++) {
                    if($(this).parent().attr("data-value") == $("#funds_logo li").eq(funds).attr("data-value")) {
                        $("#funds_logo li").eq(funds).click();
                    }
                };
                if($(this).parent().attr("data-name").indexOf("regions") == 0) {
                    var count1 = 0;
                    $(".selection_criteria li").each(function() {
                        if($(this).attr("data-name").indexOf("regions") == 0) {
                            count1++;
                        }
                    })
                    if(count1 == 0) {
                        $('.funds_all').eq(i).click();
                    }
                };
                break;
            } else if($(this).parent().attr("data-name").indexOf("sector") == 0) {
                var count = 0;
                $(".selection_criteria li").each(function() {
                    if($(this).attr("data-name").indexOf("sector") == 0) {
                        count++;
                    }
                })
                if(count == 0) {
                    $('.funds_all').eq(1).click();
                }
            } else if($(this).parent().attr("data-name").indexOf("riskLevel") == 0) {
                $('.funds_all').eq(2).click();
            } else if($(this).parent().attr("data-value").indexOf("pref") == 0) {
                $('.funds_all').eq(3).click();
            }
        }

        var prefCount = 0;
        initialization();

        var dataValue = $(this).parent().attr("data-value");
        $('.funds_choice_amend li[data-value="' + dataValue + '"]').removeClass('fund_choice_active');
        $('.funds_choice_amend li[data-value="' + dataValue + '"]').css('color', '#000');
        if($('#funds_logo_choice li').eq(0).hasClass('fund_choice_active2')) {
            $('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active2');
            $('#funds_logo_choice li').eq(0).siblings().removeClass('fund_choice_active');
            $("#funds_logo").children().hide();
        };
    });
    
    //收藏
    $(".strategies_list").on("click", ".strategies_fav_img", function() {
        $(this).toggleClass('strategies_fav_imgac');
        var _this = $(this);
        var relateid = _this.attr("relateid");
        var flag = _this.attr("optype");
        var opType = "";
        if(flag == "0") {
            opType = "Follow";
        } else {
            opType = "Delete";
        }
        $.ajax({
            url: base_root + "/front/strategy/info/setStrategyFollow.do",
            data: {
                "relateid": relateid,
                "opType": opType
            },
            type: "get",
            dataType: "json",
            success: function(data) {
                if(data.result) {
                    if(flag == "0") {
                        layer.msg(langMutilForJs['favour.add']);
                        _this.attr("src", base_root + "/res/images/icon-herat-11.png");
                        _this.attr("optype", "1");
                    } else {
                        layer.msg(langMutilForJs['favour.remove']);
                        _this.attr("src", base_root + "/res/images/icon-herat-1.png");
                        _this.attr("optype", "0");
                    }
                } else {
                    layer.msg(data.msg);
                }
            }
        });
        return false;
    });
    $(document).on('mouseenter', ".strategies_fav_img", function(event) {
        $(this).addClass('strategies_fav_imghover');
        if($(this).hasClass('strategies_fav_imgac')){
            $(this).attr('title',langMutilForJs['favour.click.remove']);
        }else{
            $(this).attr('title',langMutilForJs['favour.click.add']);
        }
    });
    $(document).on('mouseleave', ".strategies_fav_img", function(event) {
        $(this).removeClass('strategies_fav_imghover');
    });

    function assetChart(){
        $(".strategies_list_ptos").each(function(){
            var selfData = eval($(this).attr("data-value"));
            var isLoaded=$(this).attr("isLoaded");
            if(isLoaded==1){
                return ;
            }
            var opdata=[],selfcolor=[];
            for(var i = 0; i < selfData.length; i++) {
                var name = langMutilForJs['allocation.'+selfData[i].name];
                if (!name) name = selfData[i].name;
                var data={};
                data.value = selfData[i].value;
                data.name = name+" "+selfData[0].value+"%";
                opdata.push(data);
                if(selfData[i].name.indexOf('bond')==0){
                    selfcolor.push("#9dd84e");
                }else if(selfData[i].name.indexOf('fund')==0){
                    selfcolor.push("#f6ac0a");
                }else if(selfData[i].name.indexOf('stock')==0){
                    selfcolor.push("#8c5ec2");
                }
            };
            var option = {
                legend: {
                    orient: 'vertical',
                    left: '60%',
                    top:'30%',
                    data: opdata
                },
                clickable:false,
                series: [
                    {
                        name:'account',
                        type:'pie',
                         center:    ['38%', '50%'],
                        // radius : [0,"80%"],
                        color :selfcolor,
                        label: {
                            normal: {
                                position: 'inner',
                                formatter : "{d}%"
                            }
                        },
                        itemStyle: {
                            normal:{
                                label:{
                                    textStyle:{
                                        fontSize:10
                                    }
                                }
                            },
                            emphasis:{
                                show:false
                            }
                        },
                   
                        data:opdata
                    }
                ]
            };

            var myChart = echarts.init($(this)[0]);
            myChart.setOption(option);
            $(this).attr("isLoaded",1);
        });
    }

    
    $('.funds_arrow_down').on("click",function(){
        $('.funds_arrow_down').removeClass("funds_down_active");
        $('.funds_arrow_top').removeClass("funds_top_active");
        $(this).parents("li").siblings().removeClass("funds_active").end().addClass("funds_active");
        $(this).addClass("funds_down_active");
    });
    $('.funds_arrow_top').on("click",function(){
        $('.funds_arrow_down').removeClass("funds_down_active");
        $('.funds_arrow_top').removeClass("funds_top_active");
        $(this).parents("li").siblings().removeClass("funds_active").end().addClass("funds_active");
        $(this).addClass("funds_top_active");
    });
    
    $('.funds_choice_wrap_hiddenclick').on('click',function(){
        var choiceHeight = $('.funds_choice_amend').css('height');
        $('.funds_choice_wrap_hiddenclick').toggleClass('funds_choice_wrap_showclick');
        if($(this).hasClass('funds_choice_wrap_showclick')){
            $(this).parents('.funds_choice_wrap_hidden').removeAttr('max-height');
            $(this).parents('.funds_choice_wrap_hidden').animate({'min-height':choiceHeight});
        }else{
            $(this).parents('.funds_choice_wrap_hidden').animate({'min-height':'34px'});;
        }
    });
    
    //加载更多
    $('#clickmore').on('click',function(){
        if ($("#clickmore").find("a").hasClass("nomoredata")) return;
        searchList();//设置查询条件
        searchData = $("#paramsForm").serialize();//把查询条件转换成参数
        iPAGE++;
        loadStrategyList(iPAGE);
    });
    
    //搜索按钮
    $('#searchKeyBtn').on('click',initialization);
    
    //$('#keyword').on('blur',initialization);
    //绑定回车动作 
    $('#keyword').keydown(function(event){ 
        if(event.which==13){ 
            initialization();
        } 
    }); 
    
    /**
     * 实例化操作
     * 
     * */
    function initialization() {
        selection();
        searchList();
        searchData = $("#paramsForm").serialize();
        iPAGE = 1; //第N页数据
        loadStrategyList(iPAGE);
    }
    
    function searchList(){
        var regions="";
        var sectors="";
        var riskLevel="";
        $(".selection_criteria li").each(function(){
            var dataName = $(this).attr("data-name");
            var dataValue = $(this).attr("data-value");
            if(!dataValue)dataValue="";
            if("regions"==dataName){
                regions=","+dataValue;
            }else if("riskLevel"==dataName){
                riskLevel=dataValue;
            }else if(dataName.indexOf("sector")==0){
                sectors+=","+dataValue;
            }
        });
        $("#regions").val(regions);
        $("#riskLevel").val(riskLevel);
        $("#sectors").val(sectors);
    }
    
    
    //异步查询策略 固定每次拿9条数据
    var listTime;
    function loadStrategyList(curPage) {
        clearTimeout(listTime);
        listTime=setTimeout(function(){
            oLoading.show();
//                console.log("curPage="+curPage);
            var page = curPage;
            if (page<1) page = 1;
//                console.log("page"+page);
            var ifaId = $("#ifaId").val();
            var data = "rows="+perPage+"&page=" + page +"&"+searchData;
            //var url = base_root + "/front/strategy/info/listJson.do";//前台
            var url = base_root+'/console/ifa/strategylistJson.do?datestr=' + new Date().getTime();//工作台
            $.ajax({
                type : 'POST',
                datatype : 'json',
                url : url,
                data : data,
                headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                success : function(json) {
                    var html = "";
                    var total = json.total;
                    var table = json.rows;
                    $.each(table, function(i, n) {
                        //ifa信息
                        var creatorUrl = "";
                        if (n.creator && n.creator.iconUrl)
                            creatorUrl = base_root+getUserHead(n.creator.iconUrl);//头像url转换
                        var ifaMemberId = "";
                        if (n.creator) ifaMemberId = n.creator.id;
                        var ifaName = "";
                        if (n.ifa && n.ifa.firstName && n.ifa.lastName) ifaName = n.ifa.firstName+" "+n.ifa.lastName;
                        else if (n.ifaMember) ifaName =  n.ifaMember.loginCode;
                      
                        //sector和geo
                        var sectorAndGeo = "";
                        if (n.sector && n.sector!='' && n.geoAllocation && n.geoAllocation!='') 
                            sectorAndGeo = n.sector + " | " + n.geoAllocation;
                        else sectorAndGeo = n.sector + " " + n.geoAllocation;
                        
                        html += "<li>"+
                        "    <a href='"+base_root+"/front/strategy/info/strategiesdetail.do?id="+n.id+"'>" +
                        	"<div class='strategies_list_ico'>"+
                        "        <p class='strategies_list_name'>"+n.strategyName;
//                        alert(n.strategyName+"-------"+n.isFollow)
                        if (n.isFollow=='1')
                            html += "<img class=\"strategies_fav_img\" optype=\"1\" relateid='"+n.id+"' src=\""+base_root+"/res/images/icon-herat-11.png\">";
                        else
                            html += "<img class=\"strategies_fav_img\" optype=\"0\" relateid='"+n.id+"' src=\""+base_root+"/res/images/icon-herat-1.png\">";
                        html += "</p>"+
                        "        <img class='strategies_eve_img' style='display: none;' src='"+base_root+"/res/images/fund/strategies_eve.png'>"+
                        "    </div>"+
                        "    <div class='strategies_word_wrap' style='height:28px;'>"+
                        "        <p class='strategies_list_goal' style=\"width:70%; white-space: nowrap;overflow: hidden;text-overflow: ellipsis;\" data-ellipsis=\"true\" data-ellipsis-max-line=\"3\" title='"+sectorAndGeo+"'>"+sectorAndGeo+"</p>"+
                        "       <span class=\"lump-equity-grade funds_leveal_"+n.riskLevel+"\" style=\"margin-right:30px\" title=\"Risk Level "+n.riskLevel+"\">"+n.riskLevel+"</span>"+
                        "    </div>"+
                        "    <p class='strategies_list_word'>"+n.reason+"</p>"+
                        "    <div class='strategies_list_ptos' data-value='"+n.allocationData+"'></div>"+
                        "    </a>"+
                        "    <div class='strategies_list_inf'>"+
                        "        <a href='"+base_root+"/front/community/space/ifaSpace.do?id="+n.ifa.id+"'>"+
                        "        <img class='strategies_inf_img' src='"+creatorUrl+"'>"+
                        "        <p class='strategies_list_ifaname'>"+ifaName+"</p>"+
                        "        </a>"+
                        "        <p class='strategies_time_word'>"+n.lastUpdate+"</p>"+
                        "        <img class='strategies_time_img' src='"+base_root+"/res/images/fund/strategies_time.png'>"+
                        "    </div>"+
                        "</li>\n";
                        
                    });
                    
                    if(page==1){
                        $('#strategies_list').empty();
                    }
                    $('#strategies_list').append(html);
                    $("#datatotal").empty().html(total);
                    
                    setTimeout(function() {
                        assetChart(); //初始化图表
                    }, 100)
                    
                    oLoading.hide();
                    if(perPage*page >= total){
                         $("#clickmore").find("a").text(tips["nomoredata"]);
                         $("#clickmore").find("a").addClass("nomoredata");
                    }else{
                         $("#clickmore").find("a").text(tips["clickmore"]);
                         $("#clickmore").find("a").removeClass("nomoredata");
                    }
                }
            });
        },100);
    }

    $('.mylist-new-bn').on('click',function(){
        window.location.href = base_root+"/front/strategy/info/addStart.do?dateStr=" + new Date().getTime();
    });
    
    function getUserHead(url){
        if (url && url.length>0)
            if (url.startWith("/u"))
                url = "/loadImgSrcByPath.do?filePath="+url;
        else
            url = "/res/images/common/portrait.png";
        return url;
    }
    
    String.prototype.startWith=function(str){     
        var reg=new RegExp("^"+str);     
        return reg.test(this);        
    }  

    String.prototype.endWith=function(str){     
        var reg=new RegExp(str+"$");     
        return reg.test(this);        
    }
});