/**
 * registration.js WMES 注册第一步 选择用户类型
 * 
 * @author 赵晓聪 email: 445752972@qq.com
 * @date: 2016-05-18
 */
define(function(require) {

    var $ = require('jquery');
    require('formValidLang');
    var cookies = require('cookies');
    require('layer');
    
    // 交互点击事件
    $(".register_big_ico").on(
            "click",
        function() {
            //屏蔽IFA Firm和Distributor
            if ($(this).attr("data-type")!="Distributor"){
                $(this).removeClass("register_unChoice").addClass(
                    "register_Choice").siblings().removeClass(
                    "register_Choice").addClass("register_unChoice");
                $(".register_conter_1").find(".Small_choice").removeClass(
                    "Small_choice").addClass("Small_unchoice");
            }
        });
    $(".Small_unchoice").on(
            "click",
        function(event) {
            //屏蔽IFA Firm和Distributor
            if ($(this).attr("data-type")!="Distributor" && $(this).attr("data-type")!="IFA Firm"){
                $(".register_conter_1").find(".Small_choice").removeClass(
                        "Small_choice").addClass("Small_unchoice");
                $(this).removeClass("Small_unchoice").addClass("Small_choice");
                event.stopPropagation();
            }
        });
            
    // next 操作
    $("#btn_next").on("click", function() {
        $("#member_type").val($(".Small_choice").attr("data-type"));
        $("#sub_type").val($(".register_Choice").attr("sub_type"))
        if ($("#member_type").val() == "") {
            // alert("请选择一个类型！")
            layer.msg(langMutilRegist["member.register.selectUserType"]);
            return;
        }
        else {
            cookies.set('registerType', $("#member_type").val());
            cookies.set('subType', $("#sub_type").val());
            window.location.href = base_root + "/front/regist/information.do?registerType="+$("#member_type").val();
        }

    });
});