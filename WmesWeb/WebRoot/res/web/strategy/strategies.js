/**
 * @author tejay zhu
 * @date: 2016-08-23
 */

define(function(require) {
	
    var $ = require('jquery');
    require("datepicker");

    // var grid_selector = "#tableList";
    // var pageSize = 10;
	
   var fromDate  =  new JsDatePick({
      useMode:2,
      target:"fromDate",
      dateFormat:"%Y-%m-%d"
    });
   var toDate =  new JsDatePick({
      useMode:2,
      target:"toDate",
      dateFormat:"%Y-%m-%d"
    });

   fromDate.setOnSelectedDelegate(function(){
        var obj = fromDate.getSelectedDay();
        // //console.log("** 日期控件输出 a date was just selected and the date is : " + obj.year + "-" + obj.month + "-" + obj.day );
        var date = obj.year + "-" + obj.month + "-" + obj.day;
        var type = "fromDate";
        var prefix = "从";
        var selectedCondition = '<li data-value="'+date+'" data-name="'+type+'">'+ prefix + date +'<span class="selection_delete"></span></li>';
        $(".selection_criteria").append( selectedCondition );
        fromDate.populateFieldWithSelectedDate();
    });
 toDate.setOnSelectedDelegate(function(){
        var obj = toDate.getSelectedDay();
        // //console.log("** 日期控件输出 a date was just selected and the date is : " + obj.year + "-" + obj.month + "-" + obj.day );
        var date = obj.year + "-" + obj.month + "-" + obj.day;
        var type = "toDate";
        var prefix = "到";
        var selectedCondition = '<li data-value="'+date+'" data-name="'+type+'">'+ prefix + date +'<span class="selection_delete"></span></li>';        
        $(".selection_criteria").append( selectedCondition );
        toDate.populateFieldWithSelectedDate();
    });



    var urlParams = {};
    function getUrlParams() {
       var name,value; 
       var str=location.href; //取得整个地址栏
       var num=str.indexOf("?") 
       str=str.substr(num+1); //取得所有参数   stringvar.substr(start [, length ]

       var arr=str.split("&"); //各个参数放到数组里
       for(var i=0;i < arr.length;i++){ 
        num=arr[i].indexOf("="); 
        if(num>0){ 
         name=arr[i].substring(0,num);
         value=arr[i].substr(num+1);
         urlParams[name]=value;
         } 
        }
    }
    $(function(){
    //绑定查询按钮事件
        $(".icon_search").click(function () {
            var k = $("#txtKeyword").val();
            var selectedCondition = '<li data-value="'+k+'" data-name="keyword">'+ k +'<span class="selection_delete"></span></li>';        
            $(".selection_criteria").append( selectedCondition );            
        });
        //bindList(0);

        // 把参数中已选的过滤条件写入到 “已选方案”中   ---------------------------
        var theUrlPrm = {
            keyword : "",
            fromDate : "从",
            toDate : "到"
        }
        getUrlParams();
        var selectedCondition = "";
        for(var p in urlParams){
            if ( p in theUrlPrm ) {
                selectedCondition += '<li data-value="'+urlParams[p]+'" data-name="'+p+'">'+ theUrlPrm[p]+urlParams[p] +'<span class="selection_delete"></span></li>';
            }else{
                if ( $("[data-name='"+p+"'][data-value='"+urlParams[p]+"']").size() > 0 ) {
                    selectedCondition += '<li data-value="'+urlParams[p]+'" data-name="'+p+'">'+$("[data-name='"+p+"'][data-value='"+urlParams[p]+"']").text()+'<span class="selection_delete"></span></li>';
                }
            }
        }
        $(".selection_criteria").append( selectedCondition );
        // 把参数中已选的过滤条件写入到 “已选方案”中   ---------------------------

        // 绑定 搜索事件监听    -----------------
        var selection = $('.selection_criteria');//the element I want to monitor
        selection.bind('DOMNodeInserted', function(e) {
            requestSearch();
        });
        // 绑定 搜索事件监听    -----------------        

    });
    function requestSearch(){
        var params = [];
        params.push( "?r="+parseInt( Math.random()*10000 ) );
        $(".selection_criteria li").each(function(index, el) {
            params.push( [ "&", $(this).attr("data-name"), "=", $(this).attr('data-value') ].join('') )
        });
        console.info( "** requestSearch: "+params.join("") );
        location.href = base_root+'/front/strategy/info/publicInvestmentStrategy.do' + params.join("");
        return;    
    }

    //分页绑定Table数据
    function bindList(curPage){
    	location.href = base_root+'/front/strategy/info/publicInvestmentStrategy.do?keyword='+$("#txtKeyword").val();
    	return;
    	/*
    	$.ajax({
            type : 'post',
            datatype : 'json',
            url : base_root+'/front/strategy/listJson.do?datestr='+new Date().getTime(),
            data : {
                'keyword': $("#txtKeyword").val(),'page':curPage+1,'rows':pageSize,'sort':'','order':''
            },
            success : function(json) {
                var total = json.total;
                var table = json.rows;
                                
                $("#tableList tbody tr:gt(0)").remove();
				var tr = "";
				var list = json.rows;
				$.each(list, function (index, array) { //遍历json数据列                    
				    var id = array['id'] == null ? "" : array['id'];
				    
				    var strategyName = array['strategyName'] == null ? "" : array['strategyName'];
				     
				    var ispublic = array['isPublic'] == null ? "" : array['isPublic'];
				    if(ispublic == "1"){
				      ispublic = "[@lang_res  k='strategy.info.options.ispublic.1'/]";
				    }else if(ispublic == "0"){
				      ispublic = "[@lang_res  k='strategy.info.options.ispublic.0'/]";
				    }
				    
				    var len = 20;
				    var riskLevel = array['riskLeve'] == null ? "" : array['riskLeve'];
				    
				    var investmentGoal = array['investmentGoal'] == null ? "" : array['investmentGoal'];
				    investmentGoal = substring(investmentGoal,len);
				    
				    var investor = array['investorSuitability'] == null ? "" : array['investorSuitability'];
				    investor = substring(investor,len);
				    
				    var reason = array['reason'] == null ? "" : array['reason'];
				    reason = substring(reason,len);
				    
				    var status = array['status'] == null ? "" : array['status'];
				    if(status == "using"){
				      status = "[@lang_res  k='strategy.info.options.status.using'/]";
				    }else if(status == "unusing"){
				      status = "[@lang_res  k='strategy.info.options.status.unusing'/]";
				    }else if(status == "draft"){
				      status = "[@lang_res  k='strategy.info.options.status.draft'/]";
				    }
				    
				    var creator ="";
				    if(null != array['creator']) {
				      creator = array['creator']['nickName'] == null ? "" : array['creator']['nickName'];
				    }
				    
				    var createTime = array['createTime'] == null ? "" : array['createTime'];
				    
				    var infoUrl = base_root+'/front/strategy/input.do?datestr='+ new Date().getTime() + "&id=" + id;
				    
				    tr += "<tr>"
				        + "<td align='left'><a href='"+infoUrl+"' >" + strategyName + "</a></td>"
				        + "<td>" + ispublic + "</td>"
				        + "<td class='list-number'>" + riskLevel + "</td>"
				        + "<td align='left'>" + investmentGoal + "</td>"
				        + "<td>" + investor + "</td>"
				        + "<td>" + status + "</td>"
				        + "<td align='left'>" + creator + "</td>"
				        + "<td class='list-number'>" + createTime + "</td>"                      
				        + "</tr>";
				        
				        //alert(tr);
				});
				$("#tableList tbody").append(tr);
                
            }
        })
         //回调
        function pageselectCallback(page_id, jq) {
            bindList(page_id);
        }*/
    }
    
    function substring(str,len){
        var rst = str;
        if(str.length>len){
            rst = str.substring(0,len) + '...';
        }
        return rst;
    }

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
            $(".funds_logo_choice li").removeClass("fund_logo_active");
            $(this).removeClass("funds_aloac");
            var selection_criterialenght = $(".selection_criteria li").length;
            for(var i = 0; i < selection_criterialenght ;i++){
                if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
                    $(".selection_criteria li").eq(i).addClass("thisremove");
                }
            }
            $(".thisremove").remove();
            //bindList(0);
        }
    });
    //选定字母下的二级选项
    $(".funds_logo_choice li").on("click",function(){
		if($(this).hasClass("fund_logo_active") ){
			var selection_criterialenght = $(".selection_criteria li").length;
			for(var i = 0; i < selection_criterialenght ;i++){
				if($(".selection_criteria li").eq(i).attr("data-value") == $(this).attr("data-value") ){
					$(".selection_criteria li").eq(i).remove();
				}
			}
			$(this).removeClass("fund_logo_active");
		}else{
			if($(this).attr("data-key") != undefined && $(this).attr("data-key") != ""){
				$(".funds_title_selection").before('<li data-name="' + $(this).attr("data-name") + '" data-value="' + $(this).attr("data-value") + '"  data-key="' + $(this).attr("data-key") +'">' + $(this).attr("data-key") + '<span class="selection_delete"></span></li>')
			}
			$(this).addClass("fund_logo_active");
		}
		$("#funds_logo_choice .funds_all").addClass("funds_aloac");
		//bindList(0);

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
        // var self = this;
        // listTime=setTimeout(function(){
        //     //bindList(0);
        // }
        // ,100);
    });
    
    //选定排序
    $(".sort_choice li").on("click",function(){
        clearTimeout(listTime)
        var selection_criterialenght = $(".selection_criteria li").length;
        for(var i = 0; i < selection_criterialenght ;i++){
                if($(".selection_criteria li").eq(i).attr("data-name") == $(this).attr("data-name") ){
                    $(".selection_criteria li").eq(i).remove();
                }
        }
        $(this).siblings().removeClass("sort_choice_active").end().addClass("sort_choice_active");

    });
    
    function selection(){
        var _thisLenght =  $(".selection_criteria").children().length
        
        if( _thisLenght != 1  ){
            $(".funds_title_selection").css('display','inline-block');
        }else{
            $(".funds_title_selection").css('display','none');
        }
    }


    $(".selection_criteria").on("click",".selection_delete",function(){
        $(this).parent().remove();
        var funds_all_lenght = $('.funds_all').length;
        for( var i = 0; i < funds_all_lenght ; i++){
            if($(this).parent().attr("data-name") == "region"){
                var fundslenght = $("#letter_search_choice li").length;
                for(var funds = 0 ; funds < fundslenght ;funds++){
                    if( $(this).parent().attr("data-value") ==  $("#letter_search_choice li").eq(funds).attr("data-value") ){
                        $("#letter_search_choice li").eq(funds).click();
                    }
                }
                break;
            }else if( $(this).parent().attr("data-name") ==  $('.funds_all').eq(i).attr("data-name") ){
                $('.funds_all').eq(i).click();
                // loadFundList($('.funds_all').eq(i));
            }
        }
        
        var prefCount=0;
        $(".selection_criteria").find("li").each(function(){
            $(this).attr("data-value")=="pref";
            prefCount++
        })
        if(prefCount==0)$("#per_all").addClass("per_active");

        console.info( "** 点击删除查询过滤条件 " );
        requestSearch();
    });

    $(".strategies_fav_img").click( function () { 
        $.post( base_root+'/front/strategy/info/strategyFollow.do' , { id:  $(this).attr('strategyid') , type: "Follow" },
           function(data){
            console.info( "** JSON.stringify( data): "+JSON.stringify( data) );
           }
        );        
    });


});