define(function(require) {
	// 依赖
	var $ = require('jquery');
	require("echarts");
	require("swiper");
	sessionStorage.removeItem("clientdetailtab");
	
	var HomeInvestors = {
		InvestorsListCon : function() {
			$(".mid-inverstor-left").on("click", ".investor-list-img", function() {
				$(this).siblings(".investor-hide-chat").toggleClass("investor-show-chat");
			});
		},
		InvestorsInit : function() {
			this.InvestorsListCon();
		}
	}
	HomeInvestors.InvestorsInit();

	$(".investor-chioce li p").hide();

	$(".investor-chioce li").on("click", function(e) {
		// var number = $(this).attr('number');
		var isopen = $(this).attr('isopen');
		if (isopen == '0') {
			$(this).find('p').stop(true).show('1000');
			$(this).attr('isopen', '1');
			$(this).find('a').removeClass('investor-chioce-white').addClass('investor-chioce-button');
		} else if (isopen == '1') {
			$(this).find('p').stop(true).hide('1000');
			$(this).attr('isopen', '0');
			$(this).find('a').removeClass('investor-chioce-button').addClass('investor-chioce-button').addClass('investor-chioce-white');
		} else {
			$(this).find('a').toggleClass("investor-chioce-white");
			genInvestor();
		}
		e.stopPropagation();
		return false;
	});
	$(".mid-inverstor-left").on("mousemove", ".home-investor-list-li", function() {
		$(this).find(".ifa-home-mask").stop(true).animate({
			opacity : "0.9"
		}, 100, "linear");
	});
	$(".mid-inverstor-left").on("mouseleave", ".home-investor-list-li", function() {
		$(this).find(".ifa-home-mask").stop(true).animate({
			opacity : "0"
		}, 100, "linear");
	});
	

	$(".investor-chioce li p").on("click", function(e) {

		$(this).addClass('p-selected').siblings().removeClass('p-selected');
		e.stopPropagation();
		genInvestor();
	});

	$(".investor-chioce li p .investor-choice-x").on("click", function(e) {

		$(this).parent('p').removeClass('p-selected');
		e.stopPropagation();
		genInvestor();
	});

	bindInvestor(investorList);

	function bindInvestor(list) {
		// var list = disjson.person;
		// var list=investorList;
		var html = '';
		$(".home-investor-list").empty();
		$(".home-investors-sum").text(list.length);
		$.each(list, function(i, n) {
			if (list.length > 5) {
				var rand = parseInt(Math.random() * 6, 10);
				n = list[rand];
			}

			if (i > 5)
				return;
			var li = '<li class="home-investor-list-li">';
			li += '<p class="investor-list-name">' + n.loginCode + '</p>';
			li += '<div class="investor-iof-wrap">';
			li += '<p class="investor-list-title">';
			li += '<img class="investor-list-img" src="' + base_root + '/res/images/ifa/ifa_phone_ico.png" alt="">';
			li += '<span class="investor-list-ifo">' + n.mobileNumber + '</span>';
			li += '</p>';
			li += '<p class="investor-list-title">';
			li += '<img class="investor-list-img" src="' + base_root + '/res/images/ifa/ifa_email_ico.png" alt="">';
			li += '<span class="investor-list-ifo">' + n.email + '</span>';
			li += '</p>';
			li += '<div class="investor-chat-wrap">';
			li += '<img class="investor-list-img" src="' + base_root + '/res/images/ifa/investor_liaotian.png" alt="">';
			li += '<div class="investor-hide-chat">';
			li += '<img src="' + base_root + '/res/images/ifa/investor_wetchat.png" alt="">';
			li += '<img src="' + base_root + '/res/images/ifa/investor_phone.png" alt="">';
			li += '<img src="' + base_root + '/res/images/ifa/investor_line.png" alt="">';
			li += '</div>';
			li += '</div>';
			li += '</div>';
			li += '<ul class="ifa-home-mask">';

			li += '<a href="' + base_root + '/front/strategy/info/clientDetail.do">';
			if (n.sameLang != undefined && n.sameLang == "1")
				li += '<li>Same Language</li>';
			if (n.sameStyle != undefined && n.sameStyle == "1")
				li += '<li>Same Inv. Style</li>';
			if (n.readInsight != undefined && n.readInsight == "1")
				li += '<li>Read My Insights</li>';
			if (n.noIfa != undefined && n.noIfa == "1")
				li += '<li>No IFA yet</li>';
			li += '</a></ul>';
			li += '</li>';
			html += li;
		});
		html = '<div type="portfolios"  class="page_left"></div>' + html;
		html = html + '<div type="portfolios"  class="page_right" ></div>';
		$('.home-investor-list').append(html).hide().fadeToggle(500);// .show('1000');
	}

	// 加载6条数据
	function genInvestor() {
		var langCode = "";
		var styleCode = "";
		var intrestCode = "";
		$(".investor-chioce li .p-selected").each(function(i, n) {
			var num = $(n).parent().attr("number");
			if ("1" == num) {
				langCode = $(n).attr("code");
			} else if ("2" == num) {
				styleCode = $(n).attr("code");
			} else if ("3" == num) {
				intrestCode = $(n).attr("code");
			}
		});
		var noIfa = "";
		if (!$("#btnNoIfa").hasClass("investor-chioce-white")) {
			noIfa = "1";
		}

		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + '/front/ifa/space/findInvestorList.do',
			data : {
				langCode : langCode,
				invStyle : styleCode,
				intrest : intrestCode,
				noIfa : noIfa
			},
			success : function(json) {
				var list = json;
				bindInvestor(list);
			}
		})

	}

	// 点击左时
	$(".home-investor-list").on('click', '.page_left', '', function() {
		genInvestor();

	});

	$(".home-investor-list").on('click', '.page_right', '', function() {
		genInvestor();
	});
	
	
	

})