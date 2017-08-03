define(function(require) {
    var $ = require('jquery');
    require('layer');    
    var searchData = null;
    var page=0;
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
        for(var i=1;i<21;i++){
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
    
    
    $("#per_all").on("click",function(){
        $("#per_all").addClass("per_active");
        $("#funds_per_content").find(".funds_all").click();
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
        $(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");
        if($(this).attr("data-key") != undefined && $(this).attr("data-key") != "") {
            //处理issuedDate  和  dataFrom、dataTo
            if ($(this).attr("data-value") == "perf"){
//                console.log($(this).attr("data-value"));
                $(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '" data_from="' + $(this).attr("data_from") + '" ' + ' data_to="' + $(this).attr("data_to")+ '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
            }else
                $(".funds_title_selection").before('<li data-value="' + $(this).attr("data-value") + '" data-name="' + $(this).attr("data-name") + '">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
        }

        // 解决重复请求的问题
        //var self = this;
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
        if($(this).hasClass("fund_logo_active1")) {
            var selection_criterialenght = $(".selection_criteria li").length;
            for(var i = 0; i < selection_criterialenght; i++) {
                if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value")) {
                    $(".selection_criteria li").eq(i).remove();
                }
            }
            $(this).removeClass("fund_logo_active1");
        } else {
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
            } else if($(this).parent().attr("data-value").indexOf("perf") == 0) {
                $('.funds_all').eq(3).click();
            }
        }

        //var prefCount = 0;
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
        
        
    $("#funds_per_content .fund_xiala_active").find("li").on("click",function(){
        var self = $(this);
        if($(this).hasClass("funds_all") == false){
            $("#per_all").removeClass("per_active");
            // 修改：基金规模只能单选
            $('#listForm').find(".perfClean").val("");
            self.parents(".fund_xiala_active").siblings().find(".funds_all").click();
        }
    });
    

    $('#funds_per_choice li').on("mouseenter",function(){
        if( $(this).hasClass("fund_xiala_active")){
            $(this).removeClass("fund_xiala_active");
            $("#funds_per_content").children().hide().eq( $(this).index() ).hide();
        }else{
            $(this).siblings().removeClass("fund_xiala_active").end().addClass("fund_xiala_active");
            $("#funds_per_content").children().hide().eq( $(this).index() ).show();
        }
    });
    
    //加载更多
    $('#clickmore').on('click',function(){
        searchList();
        searchData = $("#paramsForm").serialize();
        page=page+1;
        loadPorfolisList();
    });
    $('#searchKeyBtn').on('click',initialization);
    /**
     * 实例化操作
     * 
     * */
    function initialization(){
        searchList();
        searchData = $("#paramsForm").serialize();
        page=1;
        loadPorfolisList();
    }
    
    function searchList(){
        var regions="";
        var sectors="";
        var riskLevel="";
        var issuedDate = "";
        var perf_from = "";
        var perf_to = "";
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
            }else if(dataValue.indexOf("perf") == 0){
//                console.log(dataName);
                issuedDate=dataName;
                perf_from = $(this).attr("data_from");
//                console.log(perf_from);
                perf_to = $(this).attr("data_to");
            }
        });
        $("#riskLevel").val(riskLevel);
        $("#geoAllocation").val(regions);
        $("#sector").val(sectors);
        $("#issuedDate").val(issuedDate);
        $("#dataFrom").val(perf_from);
        $("#dataTo").val(perf_to);
    }
    
    //异步查询组合 固定每次拿8条数据
    var listTime;
    function loadPorfolisList() {
        clearTimeout(listTime);
        listTime=setTimeout(function(){
            var data = "rows="+perPage+"&page=" + page +"&"+searchData;
            $.ajax({
                type : 'POST',
                datatype : 'json',
                url : base_root+'/console/ifa/pofolioJsonList.do?datestr=' + new Date().getTime(),
                data : data,
                 headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                success : function(json) {
                    var html = "";
                    total = json.total;
                    var table = json.rows;
                    var riskName = "";
                    $.each(table, function(i, n) {
                        riskName = $("#risk_"+n.riskLevel).attr("data-key");
                        if(riskName==undefined || riskName==null){
                            riskName="unknown";
                        }
                        //图表
                        var chartUrl = n.chartUrl;
                        if (chartUrl && chartUrl.length>0)
                            chartUrl = base_root+"/loadImgSrcByPath.do?filePath="+chartUrl;
                        else
                            chartUrl = base_root+"/res/images/application/portfolio_nodata.png";
                        
                        //ifa信息
                        var creatorUrl = "";
                        if (n.creator && n.creator.iconUrl)
                            creatorUrl = base_root+getUserHead(n.creator.iconUrl);//头像url转换
                        var ifaMemberId = "";
                        if (n.ifaMember) ifaMemberId = n.ifaMember.id;
                        var ifaName = "";
//                        console.log( n.ifaMember.loginCode);
                        if (n.ifa && n.ifa.firstName && n.ifa.lastName) ifaName = n.ifa.firstName+" "+n.ifa.lastName;
                        else if (n.ifaMember) ifaName =  n.ifaMember.loginCode;
                            
                        //sector和geo
                        var sectorAndGeo = "";
                        if (n.sector && n.sector!='' && n.geoAllocation && n.geoAllocation!='') 
                            sectorAndGeo = n.sector + " | " + n.geoAllocation;
                        else sectorAndGeo = n.sector + " " + n.geoAllocation;
                        
                        html += "<li>"+
                        //"<a href='"+base_root+"/front/ifa/info/porfoliosdetail.do?id="+n.id+"' target='_self'>"+
                        '<a href="'+base_root+'/front/portfolio/arena/detail.do?id='+n.id+'">'+
                        "    <div class='portfolio_list_contents portfolio_list_contents_amend'>"+
                        "        <p class='portfolio_name portfolio_name_amend' title='"+n.portfolioName+"'>"+n.portfolioName+"</p>";
                        if (n.isFollow && n.isFollow=='1')
                            html += "            <div class='portfolio_fav_img_amend portfolio_fav_img_amend1active'  optype='1' relateid='"+n.id+"' src='"+base_root+"/res/images/fund/icon-herat-11.png'></div>";
                        else
                            html += "            <div class='portfolio_fav_img_amend' optype='0' relateid='"+n.id+"' src='"+base_root+"/res/images/fund/icon-herat-1.png'></div>";
                        html += "            <div class='strategies_word_wrap'>"+
                        "                <p class='strategies_list_goal' style='width:70%; white-space: nowrap;overflow: hidden;text-overflow: ellipsis;' data-ellipsis='true' data-ellipsis-max-line='3' title='"+sectorAndGeo+"'>"+sectorAndGeo+"</p>"+
                        '               <span class="lump-equity-grade funds_leveal_'+n.riskLevel+'" style="margin-right:30px" title="Risk Level '+n.riskLevel+'">'+riskName+'</span>'+
                        "            </div>"+
                        "            <p style='margin:10px 0;font-weight:bold;min-height:16px;'><span class='portfolio_percent' style='color:#5dc94f;margin-right:8px;'>"+n.monReturn3+"% </span> "+langMutilForJs['fund.info.screener.performance.3MonthReturn']+"</p>"+
                        "            <div class='portfolio_total portfolio_total_amend'>"+
                        "                <div class='portfolio_index_wrap_amend'>"+
                        "                    <img src='"+chartUrl+"' style='height:100%;width:100%' />"+
                        "                </div>"+
                        "                <div class='portfolio_return portfolio_return_amend'>"+
                        "                    <p class='portfolio_percent_amend'>"+n.totalReturn+"%</p>"+
                        "                    <p class='portfolio_return_word'>"+langMutilForJs['list.search.return']+"</p>"+
                        "                </div>"+
                        "            </div>"+
                        "    </div>"+
                        "    </a>"+
                        "    <div class='strategies_list_inf'>"+
                        "        <a href='"+base_root+"'/front/community/space/ifaSpace.do?id="+ifaMemberId+"' >"+
                        "        <img class='strategies_inf_img' src='"+creatorUrl+"'>"+
                        "        <p class='strategies_list_ifaname strategies_list_ifaname_amend'>"+ifaName+"</p>"+
                        "        </a>"+
                        "        <p class='strategies_time_word strategies_time_word_amend'>"+n.lastUpdate+"</p>"+
                        "        <img class='strategies_time_img' src='"+base_root+"/res/images/fund/strategies_time.png'>"+
                        "    </div>"+
                        "</li>";
                        
                    });
                    if(page==1){
                        $('#porfoliosList').empty();
                    }
                    $('#porfoliosList').append(html);
                    $("#datatotal").empty().html(total);
                    if(perPage*page >= total){
                         $("#clickmore").find("a").html(tips["nomoredata"]);
                    }else{
                         $("#clickmore").find("a").html(tips["clickmore"]);
                    }
                }
            });
            function pageselectCallback(page_id, jq) {
                loadStrategyList(page_id);
            }
        },100);
    }
    
    $('.mylist-new-bn').on('click',function(){
        window.location.href = base_root+"/front/portfolio/arena/createPortfolioOne.do?dateStr=" + new Date().getTime();
    });
    
    function getUserHead(url){
        if (url && url.length>0)
            if (url.startWith("/u"))
                url = "/loadImgSrcByPath.do?filePath="+url;
        else
            url = "/res/images/common/portrait.png";
        return url;
    }
    
    //收藏
    $(".portfolio_list").on("click", ".portfolio_fav_img_amend", function() {
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
            url: base_root + "/front/portfolio/arena/setPortfolioFollow.do",
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
                        _this.toggleClass('portfolio_fav_img_amend1active');
                        //_this.attr("src",base_root+"/res/images/icon-herat-2.png");
                        _this.attr("optype", "1");
                    } else {
                        layer.msg(langMutilForJs['favour.remove']);
                        _this.toggleClass('portfolio_fav_img_amend1active');
                        //_this.attr("src",base_root+"/res/images/fund/strategies_fav.png");
                        _this.attr("optype", "0");
                    }
                } else {
                    layer.msg(data.msg);
                }
            }
        });
        return false;
    });
    $(document).on('mouseenter', ".portfolio_fav_img_amend", function(event) {
        $(this).addClass('portfolio_fav_img_amendhover');
        if($(this).hasClass('portfolio_fav_img_amend1active')){
            $(this).attr('title','点击取消收藏');
        }else{
            $(this).attr('title','点击收藏');
        }
    });
    $(document).on('mouseleave', ".portfolio_fav_img_amend", function(event) {
        $(this).removeClass('portfolio_fav_img_amendhover');
    });
    
    String.prototype.startWith=function(str){     
        var reg=new RegExp("^"+str);     
        return reg.test(this);        
    }  

    String.prototype.endWith=function(str){     
        var reg=new RegExp(str+"$");     
        return reg.test(this);        
    }
});    