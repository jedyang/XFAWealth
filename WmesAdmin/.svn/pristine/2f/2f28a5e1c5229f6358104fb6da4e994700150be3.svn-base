/**
 * Currency.js  通用类JS
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-05-20
 */
     var WmesUrl = "http://192.168.138.97:9000";
    // var WmesUrl = "http://192.168.138.165:81/YII2";
    // Cookie
    function setCookie(name, value, options, time){
            var text = name + '=' + value;

            options = options || {};


            var exp = new Date();

            exp.setTime(exp.getTime() +  24*60*60*1000);
            text += '; expires=' + exp.toGMTString();

            if (options.domain) {
                text += '; domain=' + options.domain;
            }

            if (options.path) {
                text += '; path=' + options.path;
            }

            if (options.secure) {
                text += '; secure';
            }
            document.cookie = text;
            return text;
    }
    //獲取cookie(初始時候)
    function getCookie(name, code) {
        var result = '',
            reg = new RegExp('(^|; )' + name + '=([^;]+)(;|$)'),
            text = document.cookie,
            match = [];

        match = text.match(reg);

        if (match) {
            result = code ? decodeURIComponent(match[2]) : match[2];
        }

        return result;
    }
    //清除cookie(退出)
    function removeCookie(name){
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval=getCookie(name);
        if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString()+";domain=;path=/";
    }
    

define(function(require) {

    var $ = require('jquery');

    
        $(".wmes_window").css("min-height",$(window).height() - $(".wmes_foot").height())
        $(".register_background_pic").css("min-height",$(window).height() - $(".wmes_foot").height() - $(".wmes_top").height());
        $(window).on('resize',function(){
            $(".wmes_window").css("min-height",$(window).height() - $(".wmes_foot").height())
            $(".register_background_pic").css("min-height",$(window).height() - $(".wmes_foot").height() - $(".wmes_top").height());
        });
    // ip
    $("#wmes_menu_show").on("click",function(){
        if($(this).hasClass("wmes_menu_active")){
            $(this).removeClass("wmes_menu_active");
            $(".wmes_menu").hide();
        }else{
            $(this).addClass("wmes_menu_active");
            $(".wmes_menu").show();
        }
    });
    
    $(".showMore").on("click",function(){
    	if($(this).parent().hasClass("showActive")){
    		$(this).parents(".wmes_ifa_touxiang").find(".wmes_touxiangkuang").hide();
    		$(this).parent().removeClass("showActive");
    	}else{
    		$(this).parents(".wmes_ifa_touxiang").find(".wmes_touxiangkuang").show();
    		$(this).parent().addClass("showActive");
    	}
    	
    });
    $("#yuanyan_wrap").on("click",function(){
        if($(this).parent().hasClass("yuyanshow")){
            $(this).parents(".wmes_yuyan").find(".wmes_yuyankuang").hide();
            $(this).parent().removeClass("yuyanshow");
        }else{
            $(this).parents(".wmes_yuyan").find(".wmes_yuyankuang").show();
            $(this).parent().addClass("yuyanshow");
        }
        
    })
    $(".langChange").on("click",function(){
    	$.get(base_root+"/front/site/changLang.do?langFlag="+$(this).attr("langCode"), function(data, status, Request){
    		if(data != null){
    			window.location.href=window.location.href;
    		}
    	});
    	
    })

    $.extend({
        Tips: function (options) {
            options = $.extend({
                title: "",
                content: "",
            }, options);
            var mask = '<div id="artwl_mask"></div>';
            var boxcontain = '<div id="artwl_boxcontain">\
                                  <div id="artwl_showbox">\
                                      <div id="artwl_message">\
                                          正在加载，请稍后...<br />\
                                      </div>\
                                      <div id="artwl_closeW"><span id="artwl_close">Close</span></div>\
                                        <span class="ns-close"></span>\
                                  </div>\
                              </div>';
            if ($("#artwl_mask").length == 0) {
                $("body").append(mask + boxcontain);
            }
            else{
                $("#artwl_boxcontain").remove();
                $("body").append(boxcontain);
            }

            if (options.content != "") {
                $("#artwl_message").html(options.content);
            }


            var height = $("#artwl_boxcontain").height();
            var width = $("#artwl_boxcontain").width();
            $("#artwl_mask").show();

            $("#artwl_boxcontain").css("top", "30%").css("left", ($(window).width() - width) / 2).show();
            $(window).on("resize",function(){
                $("#artwl_boxcontain").css("left", ($(window).width() - width) / 2);
            })
            $(".ns-close ,#artwl_close").click(function () {
                $("#artwl_mask").hide();
                $("#artwl_boxcontain").hide();
            });
        },
        artwl_close: function (options) {
            options = $.extend({
                callback: null
            }, options);
            $("#artwl_mask").hide();
            $("#artwl_boxcontain").hide();
            if (options.callback != null) {
                options.callback();
            }
        }
    });
});