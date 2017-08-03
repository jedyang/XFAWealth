/**
 * registration.js WMES 注册第三步 个人公司信息
 * 
 * @author 赵晓聪 email: 445752972@qq.com
 * @date: 2016-05-18
 */
define(function(require) {
    var $ = require('jquery');
//    var cookies = require('cookies');
    require('formValidLang');
    require("jqueryForm");
    require("formValid");
    require("layui");
//    //console.log("registerType="+cookies.get('registerType'));
//    //console.log("subType="+cookies.get('subType'));
    var baseParam = {
        submitBol : false,
        // information
        baseData : "",
        registerType : registerType /* cookies.get('registerType') */,
        baseInfoDom : "",
        usertype : memberType,
        subType : subMemberType /* cookies.get('subType')getCookie("subType") */,

        inputCtrl : function() {
            $(".register_xiala_long input").on("focus", function() {
                $(this).siblings(".regiter_xiala_ul").show();
            });
            $('.register_xiala_ico').on('click',function(){
                if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none') {
                    $(this).siblings(".regiter_xiala_ul").show();
                }else{
                    $(this).siblings(".regiter_xiala_ul").hide();
                };
            });
            $(".regiter_xiala_ul").on("click","li",function() {
                $(this).parent().siblings('.value_hidden').val($(this).attr("value"));
                $(this).parent().siblings('.value_show').val($(this).text());
                $(this).closest('.regiter_xiala_ul').hide();
            });
            
            $('.register_xiala_long').on('mouseleave',function(){
            	$(this).find('.regiter_xiala_ul').hide();
//          	alert($(this).find('input').eq(0).val());
          	$(this).find('input').blur();
            });
            
            
            $(".register_xiala_long").on("blur", "input", function() {
                var _this = $(this);
                // mandatoryVal =
                // _this.siblings(".regiter_xiala_ul").children().eq(0).attr("value");
                // _this.val(mandatoryVal);
                var temp=0;
                // 如果输入跟可选不符，则选择第一个可选项
                if(_this.val()==""){
                	
                }else{
                	setTimeout(function() {
//                  _this.siblings(".regiter_xiala_ul").hide();
                	for(i=0;i<_this.closest('.register_xiala_long').find('.regiter_xiala_ul li').length;i++){
                		if(_this.closest('.register_xiala_long').find('.value_show').val()==_this.closest('.register_xiala_long').find('.regiter_xiala_ul li').eq(i).text()){
                			temp=1;
                		};
                	};
                	if(temp==0){
                		_this.closest('.register_xiala_long').find(".value_show").val(_this.closest('.register_xiala_long').find('.regiter_xiala_ul li').eq(0).text());
                		_this.closest('.register_xiala_long').find(".value_hidden").val(_this.closest('.register_xiala_long').find('.regiter_xiala_ul li').eq(0).val());
                	}
                }, 200);
                }
                
            });
            
        	},

        // investor数据与控制
        InvestorFromCheck : function() {
            // 请求接口下拉数据（国家信息
            var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//            $("#invest_country").append( pleaseSelect);
//            $("#invest_nation").append( pleaseSelect);
//            $("#invest_residence").append( pleaseSelect);
            $.ajax({
                type : "POST",
//                url : base_root + "/front/regist/countryList.do?r="+ Math.random(),
                url : base_root + "/front/regist/countryListSearch.do?r="+ Math.random(),
                datatype : "JSON",
                data : {
                    inputData : '{"keyword":""}'
                },
                success : function(response) {
                    var data = eval("(" + response.countryJson + ")");
                    if(data){
	                    for ( var data_item in data) {
	                        var li = '<li code="' + data[data_item].itemCode
                            + '" value="' + data[data_item].id
                            + '">' + data[data_item].name
                            + '</li>';
	                        $("#invest_country").append(li);
	                        $("#invest_nation").append(li);
	                        $("#invest_residence").append(li);
	                    }
                    }
                }, error : function(response) {

                }
            });

            // 请求接口下拉数据（学历情况
            $.ajax({
                type : "POST",
                url : base_root + "/front/investor/loadParamConfigJson.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {
                    category : "education"
                },
                success : function(response) {
                    var paramCofigList = response.paramConfigJson
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $("#ivestEdu").append( pleaseSelect);
                    // 数据渲染
                    for ( var data_item in paramCofigList) {
                        $("#ivestEdu").append(
                                '<li value="'
                                        + paramCofigList[data_item].itemCode
                                        + '" code="'
                                        + paramCofigList[data_item].itemCode
                                        + '">'
                                        + paramCofigList[data_item].name
                                                .replace(/(^\s*)|(\s*$)/g, "")
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });

            // 请求接口下拉数据（证件类型
            $.ajax({
                type : "POST",
                url : base_root + "/front/investor/loadParamConfigJson.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {
                    category : "cert_type"
                },
                success : function(response) {
                    var paramCofigList = response.paramConfigJson
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $(".cardType").append( pleaseSelect);
                    // 数据渲染
                    for ( var data_item in paramCofigList) {
                        $(".cardType").append(
                                '<li value="'
                                        + paramCofigList[data_item].itemCode
                                        + '" code="'
                                        + paramCofigList[data_item].itemCode
                                        + '">'
                                        + paramCofigList[data_item].name
                                                .replace(/(^\s*)|(\s*$)/g, "")
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });

            // 请求接口下拉数据（就业情况
            $.ajax({
                type : "POST",
                url : base_root + "/front/investor/loadParamConfigJson.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {
                    category : "employment"
                },
                success : function(response) {
                    var paramCofigList = response.paramConfigJson
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $("#ivestEmploy").append( pleaseSelect);
                    // 数据渲染
                    for ( var data_item in paramCofigList) {
                        $("#ivestEmploy").append(
                                '<li value="'
                                        + paramCofigList[data_item].itemCode
                                        + '" code="'
                                        + paramCofigList[data_item].itemCode
                                        + '">'
                                        + paramCofigList[data_item].name
                                                .replace(/(^\s*)|(\s*$)/g, "")
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });

            // 请求接口下拉数据（职业信息
            $.ajax({
                type : "POST",
                url : base_root + "/front/investor/loadParamConfigJson.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {
                    category : "occupation"
                },
                success : function(response) {
                    var paramCofigList = response.paramConfigJson
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $("#invest_occupation").append( pleaseSelect);
                    // 数据渲染
                    for ( var data_item in paramCofigList) {
                        $("#invest_occupation").append(
                                '<li value="'
                                        + paramCofigList[data_item].itemCode
                                        + '" code="'
                                        + paramCofigList[data_item].itemCode
                                        + '">' + paramCofigList[data_item].name
                                        + '</li>');

                        $("#incorporation_place").append(
                                '<li value="'
                                        + paramCofigList[data_item].itemCode
                                        + '" code="'
                                        + paramCofigList[data_item].itemCode
                                        + '">' + paramCofigList[data_item].name
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });

        },

        // Company数据与控制
        CompanyFromCheck : function() {
            $(".InputOther").on("change", function() {
                $(".RadioOtherVal").val($(this).val());
            });
            // 请求接口下拉数据（国家信息
            $.ajax({
                type : "POST",
                url : base_root + "/backend/index.php?r=sys-config/get-config",
                datatype : "JSON",
                data : {
                    inputData : '{"category":"country"}'
                },
                success : function(response) {
                    // 数据渲染
                    for ( var data_item in response.data) {
                        $("#comCoun").append(
                                '<option value="'
                                        + response.data[data_item].value + '">'
                                        + response.data[data_item].name
                                        + '</option>');
                        $("#incorporation_place").append(
                                '<option value="'
                                        + response.data[data_item].value + '">'
                                        + response.data[data_item].name
                                        + '</option>');
                    }
                }, error : function(response) {

                }
            });
            $("#company_name").on(
                "blur",
                function() {
                    var _this = $(this);

                    if (_this.val() != "") {
                        _this.siblings(".Wrong_tips").html("").hide();
                    }
                    else {
                        _this.siblings(".Wrong_tips").html(
                                "Company Name cannot be blank").show();
                        baseParam.submitBol = false;
                    }
            });
        },

        // IFA USER数据
        IfaUserFromCheck : function() {
            // 请求接口下拉数据（国家信息
            $.ajax({
                type : "POST",
                url : base_root + "/front/regist/countryListSearch.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {
                    inputData : '{"category":"country"}'
                },
                success : function(response) {
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $(".countryList").append( pleaseSelect);
                    // 数据渲染
                    var data = eval("(" + response.countryJson + ")")
                    for ( var data_item in data) {
                        $(".countryList").append(
                                '<li code="' + data[data_item].itemCode
                                        + '" value="' + data[data_item].id
                                        + '">' + data[data_item].name
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });
            // 请求接口下拉数据（证件类型
            $.ajax({
                type : "POST",
                url : base_root + "/front/investor/loadParamConfigJson.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {
                    category : "cert_type"
                },
                success : function(response) {
                    var paramCofigList = response.paramConfigJson
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $(".cardType").append( pleaseSelect);
                    // 数据渲染
                    for ( var data_item in paramCofigList) {
                        $(".cardType").append(
                                '<li value="'
                                        + paramCofigList[data_item].itemCode
                                        + '" code="'
                                        + paramCofigList[data_item].itemCode
                                        + '">'
                                        + paramCofigList[data_item].name
                                                .replace(/(^\s*)|(\s*$)/g, "")
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });

            // 请求接口下拉数据（学历情况
            $.ajax({
                type : "POST",
                url : base_root + "/front/investor/loadParamConfigJson.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {
                    category : "education"
                },
                success : function(response) {
                    var paramCofigList = response.paramConfigJson
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $(".levEdu").append( pleaseSelect);
                    // 数据渲染
                    for ( var data_item in paramCofigList) {
                        $(".levEdu").append(
                                '<li value="'
                                        + paramCofigList[data_item].itemCode
                                        + '" code="'
                                        + paramCofigList[data_item].itemCode
                                        + '">'
                                        + paramCofigList[data_item].name
                                                .replace(/(^\s*)|(\s*$)/g, "")
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });

            // 请求接口下拉数据（公司信息
            $.ajax({
                type : "POST",
                url : base_root + "/front/regist/firmList.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {},
                success : function(response) {
                    var firmList = response.firmList;
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $(".ifa_com").append( pleaseSelect);
                    // 数据渲染
                    for ( var data_item in firmList) {
                        $(".ifa_com").append(
                                '<li value="'
                                        + firmList[data_item].id
                                        + '" code="'
                                        + firmList[data_item].id
                                        + '">'
                                        + firmList[data_item].companyName
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });
            // 请求接口下拉数据（职位信息
            $.ajax({
                type : "POST",
                url : base_root + "/front/investor/loadParamConfigJson.do?r="
                        + Math.random(),
                datatype : "JSON",
                data : {
                    category : "position"
                },
                success : function(response) {
                    var paramCofigList = response.paramConfigJson
                    var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
//                    $(".IFAPosition").append( pleaseSelect);
                    // 数据渲染
                    for ( var data_item in paramCofigList) {
                        $("#IFAPosition").append(
                                '<li value="'
                                        + paramCofigList[data_item].itemCode
                                        + '" code="'
                                        + paramCofigList[data_item].itemCode
                                        + '">'
                                        + paramCofigList[data_item].name
                                                .replace(/(^\s*)|(\s*$)/g, "")
                                        + '</li>');
                    }
                }, error : function(response) {

                }
            });

        },

        ControllerView : function() {
          //  //console.log(baseParam.registerType);

            switch (baseParam.registerType) {
                case "Individual":
                    baseParam.InvestorFromCheck();
                    $("#InvestorFrom").show();
                    baseParam.baseInfoDom = 'InvestorFrom';
                    break;
//                case "Corporate":// 暂无
//                    $("#CorporateFrom").show();
//                    baseParam.usertype = 2;
//                    baseParam.CompanyFromCheck();
//                    baseParam.baseInfoDom = 'CorporateFrom';
//                    break;
//                case "FI":// 暂无
//                    $("#CorporateFrom").show();
//                    baseParam.usertype = 3;
//                    baseParam.CompanyFromCheck();
//                    baseParam.baseInfoDom = 'CorporateFrom';
//                    break;
                case "IFA User":
                    $("#IfaUserFrom").show();
                    baseParam.IfaUserFromCheck();
                    baseParam.baseInfoDom = 'IfaUserFrom';
                    break;
                // case "IFA Firm":
                // $("#CorporateFrom").show();
                // CompanyFromCheck();
                // baseInfoDom = 'CorporateFrom';
                // usertype = 5;
                // break;
                // case "Distributor":
                // $("#CorporateFrom").show();
                // CompanyFromCheck();
                // baseInfoDom = 'CorporateFrom';
                // usertype = 6;
                // break;
                default:
                    layer.msg(langMutilRegist["member.register.selectUserType"]);
                    window.location.href = base_root + "/front/regist/regist.do";
                    break;
            }
        },

        SubmitBase : function() {
            $("#btn_next").on(
                    "click",
                    function() {
                        if (!baseParam.submitBol) {
                            if (baseParam.baseInfoDom == 'InvestorFrom') {// 独立投资人提交
                                if ($('#InvestorFrom').validationEngine('validate')) {
                                    // 基本信息护照/身份证号码唯一验证
//                                    //console.log("certType="+ $("#ivestCertType").val());
//                                    //console.log("idCard="+ $("#idCard").val());
                                    $.ajax({
                                            type : "GET",
                                            url : base_root
                                                    + "/front/regist/certNoUnique.do?r="
                                                    + Math.random()
                                                    + "&idCard="
                                                    + $("#idCard").val()
                                                    + "&certType="
                                                    + $("#ivestCertType").val()
                                                    + "&tableName=MemberIndividual",
                                            dataType : "JSON",
                                            success : function(response) {
                                                if (response.result) {
                                                    baseParam.submitBol = true;
                                                    submitFrom();
                                                }
                                                else {
                                                    $('#certNoError').validationEngine(
                                                                    'showPrompt',
                                                                    langMutilRegist["member.register.idIsExist"],
                                                                    'error');
                                                }
                                            }, error : function() {

                                            }
                                        })
                                }
                            }
                            /*else if (baseParam.baseInfoDom == 'CorporateFrom') {// 暂无
                                baseParam.submitBol = true;
                                $("#company_name").blur();
                                if ($("#register_no").val() == "") {
                                    $("#register_no").siblings(
                                            ".Wrong_tips").html(
                                            "Number cannot be blank")
                                            .show();
                                    baseParam.submitBol = false;
                                }
                                else {
                                    var DataTextbase = JSON.stringify({
                                        "userType" : usertype,
                                        "keyName" : "register_no",
                                        "keyValue" : $("#register_no").val()
                                    });
                                    $.ajax({
                                            type : "POST",
                                            url : base_root
                                                    + "/front/regist/baseInfoSave.do",
                                            data : {
                                                inputData : DataTextbase
                                            },
                                            success : function(
                                                    response) {
                                                if (response.data) {
                                                    $("#register_no")
                                                            .siblings(
                                                                    ".Wrong_tips")
                                                            .html(
                                                                    "")
                                                            .hide();
                                                    submitFrom();
                                                }
                                                else {
                                                    $("#register_no")
                                                            .siblings(
                                                                    ".Wrong_tips")
                                                            .html(
                                                                    "Number already exists")
                                                            .show();
                                                    baseParam.submitBol = false;
                                                }
                                            },
                                            error : function(
                                                    response) {

                                            }
                                        });
                                }

                            }*/
                            else if (baseParam.baseInfoDom == 'IfaUserFrom') {// ifa投资人

                                if ($('#IfaUserFrom').validationEngine(
                                        'validate')) {
                                    $.ajax({
                                        type : "GET",
                                        url : base_root
                                                + "/front/regist/certNoUnique.do?r="
                                                + Math.random()
                                                + "&idCard="
                                                + $("#ifaIdCard").val()
                                                + "&certType="
                                                + $("#ifa_certType").val()
                                                + "&tableName=MemberIfa",
                                        dataType : "JSON",
                                        success : function(
                                                response) {
                                            if (response.result) {
                                                baseParam.submitBol = true;
                                                submitFrom();
                                            }
                                            else {
                                                $('#ifaCertNoError').validationEngine(
                                                                'showPrompt',
                                                                langMutilRegist["member.register.idIsExist"],
                                                                'error');
                                            }
                                        }, error : function() {

                                        }
                                    })
                                }
                            }
                            else {
                                return;
                            }
                        }

                        function submitFrom() {
                            if (baseParam.submitBol) {
                                // 获取上一页用户信息 base表信息

                                // 用户表信息

                                var params = $("#" + baseParam.baseInfoDom).serialize();
                                params = params.replace(/\+/g," ");
                                params = decodeURIComponent(params,true);
                                
                                $.ajax({
                                        type : "POST",
                                        url : base_root + "/front/regist/baseInfoSave.do",
                                        datatype : "JSON",
                                        data : {
                                            inputData : params
                                        },
                                        success : function(response) {
                                            if (response.result) {
                                                window.location.href = base_root
                                                        + "/front/regist/complete.do?memberId="
                                                        + response.memberId
                                                        + "&r="
                                                        + Math.random();
                                            }
                                            else {
                                                layer.msg("Save failed");
                                            }
                                        },
                                        error : function(response) {

                                        }

                                    });
                            }
                        }

                        // 
                    });
        },

        init : function() {
            this.inputCtrl();
            // this.getInformation();
            this.ControllerView();
            this.SubmitBase();
        }
    };
    baseParam.init();
    
    function loadCountry(listObj, keyword){
        var pleaseSelect = "<li code='' value='' ><-- "+langMutilRegist["member.register.pleaseselect"]+" --></li>";
        $(listObj).empty();
//        $(listObj).append( pleaseSelect);
        $.ajax({
            type : "POST",
//            url : base_root + "/front/regist/countryList.do?r="+ Math.random(),
            url : base_root + "/front/regist/countryListSearch.do?r="+ Math.random(),
            datatype : "JSON",
            data : { "keyword":keyword },
            success : function(response) {
                var data = eval("(" + response.countryJson + ")");
                if(data){
                    for ( var data_item in data) {
                        var li = '<li code="' + data[data_item].itemCode
                        + '" value="' + data[data_item].id
                        + '">' + data[data_item].name
                        + '</li>';
                        $(listObj).append(li);
                    }
                }
            }, error : function(response) {

            }
        });
    }
    
    /** 验证 */
    $("#InvestorFrom").validationEngine(
            {
                promptPosition : "inline", scroll : false, isOverflown : true,
                overflownDIV : "container", autoPositionUpdate : true,
                focusFirstField : false, showArrow : false
            });

    $("#IfaUserFrom").validationEngine(
            {
                promptPosition : "inline", scroll : false, isOverflown : true,
                overflownDIV : "container", autoPositionUpdate : true,
                focusFirstField : false, showArrow : false
            });

    $('#investorLastName').on(
            'change',
            function() {

                if ($('#investorLastName').val() == ""
                        || $('#investorFirstName').val() == "") {
                    $('#btn_next').css('background', '#999999');
                    $('#btn_next').css('pointer-events', 'none');
                    $('#btn_next').css('cursor', 'default');
                }
                else {
                    $('#btn_next').css('background', '#2d80ce');
                    $('#btn_next').css('pointer-events', 'visiblepainted ');
                    $('#btn_next').css('cursor', 'pointer');
                }
                ;

            });

    $('#investorFirstName').on(
            'change',
            function() {
                if ($('#investorLastName').val() == ""
                        || $('#investorFirstName').val() == "") {
                    $('#btn_next').css('background', '#999999');
                    $('#btn_next').css('pointer-events', 'none');
                    $('#btn_next').css('cursor', 'default');
                }
                else {
                    $('#btn_next').css('background', '#2d80ce');
                    $('#btn_next').css('pointer-events', 'visiblepainted ');
                    $('#btn_next').css('cursor', 'pointer');
                }
                ;
            });

    $('#IFAlastName').on('change', function() {
        if ($('#IFAlastName').val() == "" || $('#IFAfirstName').val() == "") {
            $('#btn_next').css('background', '#999999');
            $('#btn_next').css('pointer-events', 'none');
            $('#btn_next').css('cursor', 'default');

        }
        else {
            $('#btn_next').css('background', '#2d80ce');
            $('#btn_next').css('pointer-events', 'visiblepainted ');
            $('#btn_next').css('cursor', 'pointer');
        }
        ;
    });

    $('#IFAfirstName').on('change', function() {
        if ($('#IFAlastName').val() == "" || $('#IFAfirstName').val() == "") {
            $('#btn_next').css('background', '#999999');
            $('#btn_next').css('pointer-events', 'none');
            $('#btn_next').css('cursor', 'default');

        }
        else {
            $('#btn_next').css('background', '#2d80ce');
            $('#btn_next').css('pointer-events', 'visiblepainted ');
            $('#btn_next').css('cursor', 'pointer');
        }
        ;
    });

    if ($('#IFAlastName').val() == "" || $('#IFAfirstName').val() == "") {
        $('#btn_next').css('background', '#999999');
        $('#btn_next').css('pointer-events', 'none');
        $('#btn_next').css('cursor', 'default');
    }
    else {
        $('#btn_next').css('background', '#2d80ce');
        $('#btn_next').css('pointer-events', 'visiblepainted ');
        $('#btn_next').css('cursor', 'pointer');
    }

    $('.showall').on('click', function() {
        $('.register_div').css('height', '1331px');
        $(this).css('display', 'none');
    });
    
    $('.btn_previous').on('click',function(){
//	    self.location=document.referrer;
	    self.location=base_root+"/front/regist/information.do";
	});
    

    //根据输入动态获取国家清单
//    $("#countryName").on("keyup",function(){
//        var key = $(this).val();
//        loadCountry($("#invest_country"),key.toUpperCase());
//    });
    
    //根据输入动态获取国家清单
    $("#nationName").on("keyup",function(){
        var key = $(this).val();
        loadCountry($("#invest_nation"),key.toUpperCase());
    });
    
    //根据输入动态获取国家清单
    $("#ifaNationName").on("keyup",function(){
        var key = $(this).val();
        loadCountry($("#IFAnationality"),key.toUpperCase());
    });
});