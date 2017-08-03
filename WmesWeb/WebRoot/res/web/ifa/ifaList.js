/**
 * fundifalist.js ifa列表页
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-27
 */

define(function(require) {
	var $ = require('jquery');
	require('pagination');
	require("swiper");
	require('layer');
	require("scrollbar");
	var Loading = require('loading');
	var angular = require('angular');


	//把按匹配度等固定在顶部
	// $.Tips({ content: "Please input old password."})
    function Fundsscrool(){

    	//表头滚动
			var funds_hei =  $(".funds_ifalist_sorting").height(),
				header_top = $(".wmes_top").height(),
				 wmes_window_top = $(window).scrollTop(),
				infor_top = $(".ifa_list").offset().top;
				//funds_top = infor_top - header_top;

			if( $('.wmes-nodata-tips').css('display') == "block" ){
				return false;
			}
			 if ( wmes_window_top > (infor_top  - header_top) ){
			 	//var new_funds_top = wmes_window_top - infor_top + header_top + funds_hei;
			 	$(".funds_ifalist_sorting").addClass("funds_fixed").css
			 		({
				 		'top':header_top-15,
				 		"width" : $(".ifa_list_ul").width()
				 	});
			 	$('.page_left').css({'top':funds_hei+50});
			 	$('.page_right').css({'top':funds_hei+50});
			 }else{
				 //$('.page_left').hide();
				 //$('.page_right').hide();
			 	$(".funds_ifalist_sorting").removeClass("funds_fixed").css
			 		({
				 		'top':0,
				 		"width" : 'auto'
				 	});

			 }
    };
	$(window).on('scroll',Fundsscrool);

	// 数据控制
	var mybase = angular.module('mySearch', []);
	var oLoading = new Loading($("#div_loading"));	
    mybase.controller('Searchctrl', ['$scope', '$http', function($scope, $http) {
    // 第一次进入页面，要判断，如果什么条件都没选，则在后面显示：Selected condition：Show All （筛选条件：显示全部）
    var select_condition_showall_en = 'Selected condition：Show All';
    var select_condition_showall_sc = '筛选条件：显示全部';
    var select_condition_showall_tc = '篩選條件：顯示全部';
    var select_condition_en = 'Selected condition';
    var select_condition_sc = '筛选条件';
    var select_condition_tc = '篩選條件';
    var select_condition_showall = '';
    var select_condition = '';
	var pageid = 1;//页码
	var sumpage ;//总页码

    if(lang=='en'){
    	select_condition_showall = select_condition_showall_en;
    	select_condition = select_condition_en;
    }
    else if(lang=='sc'){
    	select_condition_showall = select_condition_showall_sc;
    	select_condition = select_condition_sc;
    }
    else if(lang=='tc'){
    	select_condition_showall = select_condition_showall_tc;
    	select_condition = select_condition_tc;
    }
    $('.funds_selected_title').text(select_condition_showall);
    //第一次进页面，加载 所有IFA数据
    loadIFAList();

	if($('.fund_choice_active').index()==0){
		$('.fund_choice_active').addClass('fund_choice_active2');
	};
	//点击每个选项，在下面的已选方案中添加该选项
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
		$('.funds_selected_title').text(select_condition);
		if($(this).closest('ul').find('.fund_choice_active').index()==0){
			$(this).closest('ul').find('.fund_choice_active').eq(0).addClass('fund_choice_active2');
		}else{
			$(this).closest('ul').find('li').removeClass('fund_choice_active2');
		};
		// 解决重复请求的问题
		var self = this;
			listTime=setTimeout(function(){
				loadIFAList(self);
			}
			,100);
	});

	//关闭每个搜索条件项时
	$(".selection_criteria").on("click",".selection_delete",function(){
		var data_value = $(this).parent().attr("data-value");//data-value="8080804056a201d90156b01c3aeb0019"
		$(this).parent().remove();
		var dataName = $(this).parent().attr("data-name");
		var element = '.funds_choice[data-name="'+dataName+'"]';
		////console.log($(element).html());
		if(dataName=='IfafirmName'){
			var fundslenght = $(".funds_logo_choice li").length;
			for(var funds = 0 ; funds < fundslenght ;funds++){
				if( $(this).parent().attr("data-value") ==  $(".funds_logo_choice li").eq(funds).attr("data-value") ){
					$(".funds_logo_choice li").eq(funds).click();
				}
			}
		}
		else if(dataName=='BelongCountryName'){
			var cur_selected_element = 'li[data-value="'+data_value+'"]';
			$(cur_selected_element).removeClass('fund_choice_active');
			$('#belong_country').hide();
			$('#belong_country_choice li').eq(0).addClass('fund_choice_active').click();
			$('#hidBelongCountry').val('');//清除选项时，同时把隐藏域的值也置空
		}
		else {
			$(element).find('li').eq(0).addClass('fund_choice_active').click();
		}
		//判断条件是否为0，条件为0，则显示show all
		var selected_condition_length = $('.selection_criteria li').length;
		if(selected_condition_length==0){
			$('.funds_selected_title').text(select_condition_showall);
		}
		loadIFAList($(element).find('li').eq(0));
	});

	$("#yfunds_hidde_title").on("click",function(){
		var heightsum=0;
		for(var i=0;i<$('.fund_more_content div').length;i++){
			heightsum += $('.fund_more_content div').eq(i).height()+22;
		};
		if($(this).parent().hasClass("funds_more_wrap_show")){
			$(this).parent().removeClass("funds_more_wrap_show");
			 $(".fund_more_content").stop().animate({
		    	height: "0px"
			  }, 300 );
		}else{
			$(this).parent().addClass("funds_more_wrap_show");
			$(".fund_more_content").stop().animate({
		    	height: heightsum
			 }, 300 );
		}

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

	$(".chat_wrap").on("click",function(){
		var selfSelect = $(this).find(".chat_select");
		if( selfSelect.css('display') == "block" ){
			selfSelect.css('display','none');
		}else{
			selfSelect.css('display','block');
		}
	});

	//IFA所属公司，鼠标滑动到ABCD等字母上面时，比如飞卓控股有限公司-01，拼音为feizhuokongguyouxiangongsi-01，F要跟feizhuokongguyouxiangongsi挂购起来
	$("#ifafirm_choice li").on("mousemove",function(){

		$(this).siblings().removeClass("fund_choice_active").end().addClass("fund_choice_active");

		var this_letter	 = $(this).attr("data-letter"),
			funds_logo  = $("#ifafirm").children(),
			funds_logo_lenght = funds_logo.length;
		for (var k = 0 ; k< funds_logo_lenght; k++){
			if(funds_logo.eq(k).attr("data-letter")!=''&&funds_logo.eq(k).attr("data-letter")!=undefined){
				//获取首字母
				var first_letter = funds_logo.eq(k).attr("data-letter").substring(0,1).toUpperCase();
				if( this_letter == first_letter ){
					$('#ifafirm').show();
					$('belong_country').show();
	 				funds_logo.eq(k).show();
				}else{
					funds_logo.eq(k).hide();
				}
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
		loadIFAList(this);

	});

	//组装查询条件
	function loadIFAList(searchLi){
		////console.log($(searchLi).attr('data-name'));
		var dataName = $(searchLi).attr("data-name");
		var sizeFrom = $(searchLi).attr("data_from");
		var sizeTo = $(searchLi).attr("data_to");
		var dataValue = $(searchLi).attr("data-value");
		var houseId;
		if(!sizeFrom)sizeFrom="";//undefined则执行
		if(!sizeTo)sizeTo="";
		if(!dataValue)dataValue="";

		//如果当前点击的是IFA公司
		if("IfafirmName"==dataName){
			houseId = "";
			$(".fund_logo_active").each(function(index,element){
				houseId += $(this).attr("data-value");
				if(index != $(".fund_logo_active").length - 1){
					houseId += ","
				}


			})
			$('#hidIfaFirmIds').val(houseId);
		}
		//如果当前点击的是投资风格
		if("InvestmentType"==dataName){
			$('#hidInvestmentType').val(dataValue);
		}
		//如果当前点击的是服务国家地区
		if("ServiceRegion"==dataName){
			$('#hidServiceRegion').val(dataValue);
		}
		//如果当前点击的是服务语言
		if("ServiceLanguage"==dataName){
			$('#hidServiceLanguage').val(dataValue);
		}
		//如果当前点击的是投资领域
		if("Expertise"==dataName){
			$('#hidExpertise').val(dataValue);
		}
		//如果当前点击的是从业年限
		if("WorkingYears"==dataName){
			$('#hidWorkingYearsFrom').val(sizeFrom);
			$('#hidWorkingYearsTo').val(sizeTo);
		}
		//如果当前点击的是所属国籍
		if("BelongCountryName"==dataName){
			$('#hidBelongCountry').val(dataValue);
		}

		if("FundHouse"==dataName){
			houseId = "";
			//var houseLength = $(".fund_logo_active").length-1;
			$(".fund_logo_active").each(function(index,element){
				//var houseIndex = index;
				houseId += $(this).attr("data-value");
				//if(houseLength != houseIndex && houseLength != 0){
					houseId += ","
				//}
			})
			document.getElementById("fundHouseIds").value=houseId
		}
	 	Searchdata = $("#listForm").serialize();
	 	Initialization();
	}

	function Initialization(){

		// 初始化数值
	 	//var iPAGE = 1; //第N页数据
	 	//var is_finish = true;
	 	//var page_bol = true;
	 	pageid = 1;
		selection();
		$("#ifa_more").html(__clickmore__);
		getDataList(pageid);
	}

	function selection(){
		var _thisLenght =  $(".selection_criteria").children().length

		if( _thisLenght != 1  ){
			$(".funds_title_selection").css('display','inline-block');
		}else{
			$(".funds_title_selection").css('display','none');
		}
	}

	//执行清除方案点击操作，所有，如果什么条件都没选，则在后面显示：Selected condition：Show All （筛选条件：显示全部）
	$(".funds_title_selection").on("click",function(){
		$(".selection_criteria li").remove();
		$(".fund_logo_active").removeClass("fund_logo_active");
		$(".fund_choice_active").removeClass("fund_choice_active");
		$(".funds_all").addClass("fund_choice_active");
		$("#listForm").find("input").val("");
		//判断条件是否为0，条件为0，则显示show all
		var selected_condition_length = $('.selection_criteria li').length;
		if(selected_condition_length==0){
			$('.funds_selected_title').text(select_condition_showall);
		}
		Searchdata = $("#listForm").serialize();
		Initialization();
	});


	//执行搜索数据
	function getDataList(pageid) {

		var data =  "langCode="+lang+"&rows=9&page=" + pageid + "&" + Searchdata ;
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/ifa/info/getifalistJson.do?datestr="+ new Date().getTime(),
			data : data,
			beforeSend: function () {oLoading.show();},
            complete: function () {},
			success : function(json) {
				oLoading.hide();
				////console.log(json);
				var total = json.total;
				sumpage = Math.ceil(total / 9);

				if(total=='0'){
					//return;
				}
				$('#sp_meet_total').text(total);
				genTotalHtml(total);
				var list = json.rows;
				if(pageid == 1)$("#ul_ifa_list").empty();

				var li_html = '';
				$.each(list,function(i, n) {
					var ifaId = n.ifaId;
					var memberId = n.memberId;
					var ifaName = n.ifaName;
					var ifaBelongCountry = n.ifaBelongCountry;
					var ifaWorkingYears = n.ifaWorkingYears;
					if(ifaWorkingYears==null)ifaWorkingYears=0;
					var ifaFirmName = n.ifaFirm;
					var ifaPopularity = n.ifaPopularity;
					var ifaHeadImg = n.ifaHeadImg;
					if(ifaHeadImg=='null'||ifaHeadImg==null)ifaHeadImg=base_root+'/res/images/cat.png';
					var ifaPersonalCharacteristics = n.ifaPersonalCharacteristics;
					var personCharacteristics = ifaPersonalCharacteristics.split(',');
					var personCharacteristicsListHtml = '<div class="ifa_label">';
					for (i=0;i<personCharacteristics.length ;i++ )
					{
						if(personCharacteristics[i]!='')
							personCharacteristicsListHtml += '<p>'+personCharacteristics[i]+'</p>';
					}
					personCharacteristicsListHtml += '</div>';
					var spaceShowPortfolio = n.spaceShowPortfolio;////console.log(n);
					var ifaFirmLogoPath = n.ifaFirmLogoPath;//所属公司的LOGO图标

					//if(ifaFirmLogoPath==null||ifaFirmLogoPath==''||ifaFirmLogoPath==undefined)
					//	ifaFirmLogoPath =  '/res/images/ifa/ifafirm_empty_logo.png';//设置默认图标
					//组装匹配图像
					var matchDesc = '';
					var matchImg = '';
					var matchingDegree = n.matchingDegree;
					var isMatch = n.isMatch;
					if(isMatch==true){ //如果有匹配到
						if(matchingDegree=='1'){ //只命中一次，显示哭丧表情g
							matchImg = 'matching05.png';
							matchDesc = IFALIST_LIST_MATCHING_1;
						} else if(matchingDegree=='2'){
							matchImg = 'matching04.png';
							matchDesc = IFALIST_LIST_MATCHING_2;
						}
						else if(matchingDegree=='3'){
							matchImg = 'matching03.png';
							matchDesc = IFALIST_LIST_MATCHING_3;
						}
						else if(matchingDegree=='4'){
							matchImg = 'matching02.png';
							matchDesc = IFALIST_LIST_MATCHING_4;
						}
						else if(matchingDegree=='5'){
							matchImg = 'matching01.png';
							matchDesc = IFALIST_LIST_MATCHING_5;
						} else{
							matchImg = 'matching.png';
						}
					}else{
						matchDesc = IFALIST_LIST_MATCHING_0;
					} 
					//组装li的IFA数据
					var li = '<li memberid="'+memberId+'" class="ifa-fadein" style="cursor:pointer;">';
						li += '<div class="jumpintobox">';
						li += '<div class="jumpinto">';
						li += '<div class="ifa_list_ifo">';
						li += '<img class="ifa_touxiang" memberid="'+memberId+'" ifaid="'+ifaId+'" src="'+base_root+''+ifaHeadImg+'" alt="">';
						li += '<div class="ifa_list_information">';
						li += '<p class="ifa_list_name"><a class="ifa-a-href" href="' + base_root +'/front/community/space/ifaSpace.do?id='+memberId + '">'+ifaName+'</a></p>';
						li += '<p class="ifa_list_form">'+ifaBelongCountry+'</p>';
						li += '<p class="ifa_list_form">'+getExperiece(ifaWorkingYears)+'</p>';
						li += '</div>';
						li += '<div class="ifa_matching_wrap">';
						if(isMatch==true){
							li += '<img title="'+matchDesc+'" class="ifa_list_matching" src="'+base_root+'/res/images/ifa/'+matchImg+'" alt="">';
							li += '<p class="ifa_list_matchingWord">&nbsp;</p>';
						} else{
							li += '<img style="display:none;" class="ifa_list_matching" src="'+base_root+'/res/images/ifa/'+matchImg+'" alt="">';
							li += '<p class="ifa_list_matchingWord">'+matchDesc+'</p>';
						}
						li += '</div>';
						li += '</div>';
						li += '<div class="ifa_list_return"><p class="ifa_return_title">'+getOneYearReturn()+'</p><p class="ifa_list_pornum" style="color: #000000;">'+spaceShowPortfolio+'.00%</p></div>'
						li += personCharacteristicsListHtml;
						li += '<img class="ifa_firm_img" style="height:20px;" src="'+base_root+ifaFirmLogoPath + '" alt="" title="'+ifaFirmName+'">';
						// li += '<p class="ifa_list_firm">IFA Firm <span class="list_frim_word">'+ifaFirmName+'</span></p>';
						// li += '<div class="ifa_list_portfolio">';
						// li += '<span class="ifa_list_por">Portfolio</span>';
						// li += '<span class="ifa_list_pornum">'+spaceShowPortfolio+'%</span>';
						// li += '<span class="ifa_list_poracc">Acc. return</span>';
						// li += '</div>';
						li += '</div>';
						li += '<div class="ifa_list_social">';
						li += '<img class="ifa_list_people_ocp" src="'+base_root+'/res/images/ifa/popularity_ico.png" alt="">';
						li += '<p class="ifa_social_fans">'+ifaPopularity+'</p>';
						li += '<p class="ifa_social_line"></p>';
						li += '<p class="ifa_social_fans ifa_list_friends"><a memberId="'+memberId+'" ifaname="'+ifaName+'" href="javascript:void(0);"><i memberId="'+memberId+'"></i></a></p>';
						//li += '<p class="ifa_social_line"></p>';

						//li += '<p class="ifa_social_fans"><a href="#"><i></i>女生</a><p>';

						//li += '<img class="ifa_list_fanjia" memberId="'+memberId+'" ifaname="'+ifaName+'" src="/wmes/res/images/ifa/fansjia.png" alt="">';
						// li += '<img class="chat_ico_01" src="'+base_root+'/res/images/ifa/chat_ico_01.png" alt="">';
						li += '<img class="ifa_list_xie_bian" src="'+base_root+'/res/images/ifa/xie_bian.png" alt="">';
						li += '</div>';
						li += '</div>';
						li += '</li>';
						li_html+=li;

				});
				$('#ul_ifa_list').append(li_html);

				if(total=='0')$('.wmes-nodata-tips').show();
				else $('.wmes-nodata-tips').hide();
			}
		});
	}

	//返回一年投資回報率
	function getOneYearReturn(){
		return IFALIST_LIST_ONEYEARRETURN;
	}

	//返回工作年限
	function getExperiece(year){
	    return IFALIST_LIST_WORKEXPERIENCE + ' ' + year + ' ' + IFALIST_LIST_YEARS;
	}

	//返回搜索结果数量
	function genTotalHtml(number){
		var html = '';
		if(lang=='en')
			html =  'A total of <span class="funds_serach_digital ng-binding" ng-bind="datatotal" id="sp_meet_total">'+number+'</span> IFA to meet your requirements';
		else if(lang=='sc')
			html =  '共有  <span class="funds_serach_digital ng-binding" ng-bind="datatotal" id="sp_meet_total">'+number+'</span> 个投资顾问符合您的搜索条件';
		else if(lang=='tc')
			html =  '共有 <span class="funds_serach_digital ng-binding" ng-bind="datatotal" id="sp_meet_total">'+number+'</span> 個投資顧問符合您的搜索條件';
		$('#p_meet_total').html(html);
	}

	$("#ifa_more").on("click",function(){

		pageid +=1 ;

		if(pageid <= sumpage){
			getDataList(pageid);
			$(this).html(__clickmore__);
		}else{
			$(this).html(__nomoredata__);
		}


	});
	//添加好友
	$(document).on('click', '.ifa_list_friends a i', function () {
		//event.stopPropagation();
//		var ifaname = ($(this).attr('ifaname'));
		var memberId = $(this).attr('memberid');
		var loginMemberId = $('#hidloginMemberId').val();
		if(memberId!=''&&loginMemberId!=''&&memberId==loginMemberId){
			layer.alert(IFASPACE_LANG_CannotAddYourself, { title:IFASPACE_LANG_Alert, icon: 0  });
			return;
		} 
		if(memberId!=''&&loginMemberId==''){
			layer.alert(IFASPACE_LANG_LoginToAddFriends, { title:IFASPACE_LANG_Alert, icon: 0  });
			return;
		} 
		layer.confirm(IFASPACE_LANG_SureToAddFriends, { title:IFASPACE_LANG_Alert, icon: 3,btn:[IFASPACE_LANG_YES,IFASPACE_LANG_NO]  },function () {
			 $.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/info/addFriend.do?datestr="+ new Date().getTime(),
				data : {'toMemberId':memberId },
				 beforeSend: function () {},
				 complete: function () {
				 },
				success : function(json) {
					////console.log(json);
					json = JSON.parse(json);
					var result = json.result;
					var msg = json.msg;
					if(result==true||result=='true'){
						layer.msg(IFASPACE_LANG_WaitToAddFriends, { title:IFASPACE_LANG_Alert }, function () {  });
					}else if(result==false||result=='false'){
						layer.msg(msg, { icon: 1, time: 2000 }, function () {  });
					}
				}
			});
        	layer.closeAll();
        });

		//event.stopPropagation();
		//return false;
	});

	//跳转空间
	$("body").on('click', '.chat_ico_01', '', function () {
		layer.alert('功能开发中。。。。',{icon:1})
	});

	//跳转到个人空间
	$("body").on('click', '.jumpinto', '', function () {
		var memberId = $(this).closest('li').find('.ifa_touxiang').attr('memberid');
		window.location.href = base_root +'/front/community/space/ifaSpace.do?id='+memberId;
	});

	//按钮关键字
	$(".icon_search").on("click",function(){
		var ifaName = $("#ifaName").val();
		document.getElementById("keyword").value=ifaName;
		Searchdata = $("#listForm").serialize();
	 	Initialization();
	});

	//按回车键自动搜索
	$("#ifaName").keyup(function(event){
   	 if(event.keyCode == 13){
        	document.getElementById('searchKeyBtn').click();
        }
    });
	
	$(".funds_ifalist_sorting li").on("click",function(){
	    $(".funds_ifalist_sorting li").removeClass("funds_active");
	    $(this).addClass("funds_active");
	    
	    if($(this).find(".funds_arrow_top").hasClass("funds_top_active") ){
	    	$('.funds_arrow_down').removeClass("funds_down_active");
	    	$('.funds_arrow_top').removeClass("funds_top_active");
	    	$(this).find(".funds_arrow_down").addClass("funds_down_active");
	    	if($(this).index()==1){
	    		$("#hidSort").val('a.investLifeBegin desc');
	    	}else{
	    		$("#hidSort").val('a.popularityTotal desc');
	    	};
	    	Searchdata = $("#listForm").serialize();
	    	Initialization();
	    }else{
	    	$('.funds_arrow_down').removeClass("funds_down_active");
	    	$('.funds_arrow_top').removeClass("funds_top_active");
	    	$(this).find(".funds_arrow_top").addClass("funds_top_active");
	    	if($(this).index()==1){
	    		$("#hidSort").val('a.investLifeBegin asc');
	    	}else{
	    		$("#hidSort").val('a.popularityTotal asc');
	    	};
	    	Searchdata = $("#listForm").serialize();
	    	Initialization();
	    }
	});
//	//工作年限排序asc
//	$(".funds_arrow_top_year").on("click",function(){
//
//		$("#hidSort").val('a.investLifeBegin asc');
//		Searchdata = $("#listForm").serialize();
//	 	Initialization();
//	});
//
//	//工作年限排序desc
//	$(".funds_arrow_down_year").on("click",function(){
//
//		$("#hidSort").val('a.investLifeBegin desc');
//		Searchdata = $("#listForm").serialize();
//	 	Initialization();
//	});
//
//	//人气排序asc
//	$(".funds_arrow_top_popularity").on("click",function(){
//
//		$("#hidSort").val('a.popularityTotal asc');
//		Searchdata = $("#listForm").serialize();
//	 	Initialization();
//	});
//
//	//人气排序desc
//	$(".funds_arrow_down_popularity").on("click",function(){
//
//		$("#hidSort").val('a.popularityTotal desc');
//		Searchdata = $("#listForm").serialize();
//	 	Initialization();
//	});
//
//	//match me  asc
//	$(".funds_arrow_top_matching").on("click",function(){
//
//		$("#hidSort").val('a.popularityTotal asc');
//		Searchdata = $("#listForm").serialize();
//	 	Initialization();
//	});
//
//	//match me desc
//	$(".funds_arrow_down_matching").on("click",function(){
//
//		$("#hidSort").val('a.popularityTotal desc');
//		Searchdata = $("#listForm").serialize();
//	 	Initialization();
//	});

    }]);

    //更多筛选切换
	$("#yfunds_hidde_title").on("click",function(){
		if($(this).parent().attr("isopen")=="0"){
			$(this).parent().removeClass("funds_more_wrap_show");
			$(this).parent().attr('isopen','1');
			 $(".fund_more_content").stop().animate({
		    	height: "0px"
			  }, 300 );
	 		$(this).find("span").eq(1).css("display","inline-block");
	 		$(this).find("span").eq(0).css("display","none");
		}else{
			$(this).parent().addClass("funds_more_wrap_show");
			$(this).parent().attr('isopen','0');
			$(".fund_more_content").stop().animate({
		    	height: "100%"
			 }, 300 );
			$(this).find("span").eq(0).css("display","inline-block");
	 		$(this).find("span").eq(1).css("display","none");
		}
	});

	$(window).scroll(function () {
		////console.log($(window).scrollTop());
		if ($(window).scrollTop() == $(document).height() - $(window).height()) {


		}
		});
});