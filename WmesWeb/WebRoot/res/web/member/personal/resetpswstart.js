define(function(require) {
    var $ = require('jquery');
    require('layui');
    $("#btn_next").on("click", function() {
        sendEmail();
    });

    // email验证
    var emailBol = false;
    $("#email").focus(function() {
        $(this).siblings(".Wrong_tips").html("").hide();
    });
    $("#email").on("blur", function() {
        // email正则
        var emailRegular = /([a-z0-9]*[-_\.]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\.][a-z]{2,3}([\.][a-z]{2})?/i;
        var _this = $(this);

        if ($("#email").val() != "") {
            if (!emailRegular.test($("#email").val())) {
                _this.siblings(".Wrong_tips").html(props['member.resetPassword.correct.email']).show();
                emailBol = false;
            }
            else {
                emailBol = true;
            }
        }
        else {
            _this.siblings(".Wrong_tips").html(props['member.resetPassword.email.empty']).show();
            emailBol = false;
        }
    });

    // 验证码
    var codeBol = false;
    $("#checkCode").focus(function() {
        $(this).siblings(".Wrong_tips").html("").hide();
    });
    $("#checkCode").on(
            "blur",
            function() {
                // loindid正则
                var _this = $(this);

                if ($("#checkCode").val() != "") {
                    codeBol = false;
                    $.ajax({ type : "GET", 
                        url : base_root + "/front/regist/checkValicode.do?r=" + Math.random() + "&valiCode=" + $("#checkCode").val(), 
                        async : false, 
                        dataType : "JSON",
                        success : function(response) {
                            if (response.result) {
                                _this.siblings(".Wrong_tips").html("<img src='" + base_root + "/res/images/Hook.png' alt=''>").show();
                                codeBol = true;
                            }
                            else {
                                _this.siblings(".Wrong_tips").html(props['member.resetPassword.verification.code.error']).show();
                                codeBol = false;
                            }
                        }, error : function(response) {
                        } })
                }
                else {
                    _this.siblings(".Wrong_tips").html(props['member.resetPassword.verification.code.empty']).show();
                    codeBol = false;
                }
            })

    // 发送邮件：临时密码？修改密码连接
    function sendEmail() {

        // 检查输入合法性
        var email = $("#email").val();
        if (!email) {
            layer.msg(props['member.resetPassword.please.input.email']);
            return;
        }
        if (!emailBol) {
            layer.msg(props['member.resetPassword.correct.email']);
            return;
        }

        var checkCode = $("#checkCode").val();
        if (!checkCode) {
            layer.msg(props['member.resetPassword.please.input.code']);
            return;
        }
        if (!codeBol) {
            layer.msg(props['member.resetPassword.verification.code.wrong']);
            return;
        }

        // 提交表单
        $.ajax({ type : "POST", url : _root_ + "/front/member/personal/sendtemppass.do",
        // iframe: true,
        dataType : "json", data : { "email" : email }, async : false, cache : false, success : function(response) {
            if (response.result) {
                // $.Tips({ content: "临时密码已发送至邮箱，请查收，并进入下一步。"});
                layer.msg(props['member.resetPassword.sent.to.mailbox']);
                setTimeout(function(){
                    window.location.href = _root_ + "/front/member/personal/goresetpsw.do?email=" + email + "&r=" + Math.random();
                },2000);
            }
            else {
                // alert(dataObj.msg);
                layer.msg(response.msg);
            }
        }, error : function(response) {
            // alert("error found while saving data.");
            layer.msg(props['member.resetPassword.error.sending.email']);
        } });
    }

});
