/**
 * sea.js配置项目
 *  
 */
seajs.config({
    "base": base_root+"/res/js",
    "paths": {
        "seajs": "seajs"
    },
    // 模块
    "alias": {
        "jquery"        :	base_root+"/res/js/library/jquery/jquery.js",
        "jquery_ui"        :	base_root+"/res/js/jquery-ui.js",
        "angular"       :	base_root+"/res/js/library/angular/angular.min.js",
        "datepick"      :   base_root+"/res/js/jsDatePick.jquery.min.1.3.js",
        "webuploader"   :	base_root+"/res/js/webuploader.min.js",
        "footclick"   :	base_root+"/res/js/footclick.js",
        "echarts"	    : 	base_root+"/res/js/echarts.min.js",
        "conversion"    : 	base_root+"/res/js/currconversion.js",
        "jqueryForm"    :	base_root+"/res/js/library/jquery/jquery.form.js",
        "formValid"     :	base_root+"/res/third/formValidator/js/jquery.validationEngine.js",
        "formValidLang" :   base_root+"/res/third/formValidator/languages/jquery.validationEngine-zh_"+ lang.toUpperCase() +".js",
        "scrollbar"     :   base_root+"/res/js/library/scroll/scrollbar.js",
        "range"         :   base_root+"/res/js/library/range/range.js",
        "swiper"        :   base_root+"/res/js/library/swiper/swiper.js",
        "pagination"    :   base_root+"/res/third/pagination/jquery.pagination.js",
        "layer"    		:   base_root+"/res/third/layer/layer.js",
        "zTree"    		:   base_root+"/res/js/library/zTree/js/jquery.ztree.all.min.js",
        "bootstrap":   base_root+"/res/js/library/bootstrap/bootstrap.min.js",
        "bootstrapDialog":   base_root+"/res/js/library/bootstrap/bootstrap-dialog.js",
        // um
        "umeditorConfig":   base_root+"/res/js/library/ueditor/umeditor.config.js",
        "ueditor"       :   base_root+"/res/js/library/ueditor/umeditor.min.js",
        // ue
        "ueditor_ueConfig":   base_root+"/res/js/library/ueditor_ue/ueditor.config.js",
        "ueditor_ue"      :   base_root+"/res/js/library/ueditor_ue/ueditor.all.min.js",

        "iscroll"       :   base_root+"/res/js/library/iscroll/iscroll.js",
        "slider"        :   base_root+"/res/third/slider/js/jssor.slider-21.1.5.min.js",
        "moment"        :   base_root+"/res/js/library/fullcalendar/moment.min.js",
        "fullcalendarlang" : base_root+"/res/js/library/fullcalendar/localeall.js",
        "fullcalendar"  :   base_root+"/res/js/library/fullcalendar/fullcalendar.min.js",
        "ellips"        :   base_root+"/res/js/library/ellipsis/ellipsis.js",
        "cookies"      :   base_root+"/res/js/library/cookies/cookies.js",
        "ionrangeSlider" : base_root+"/res/js/library/ionrangeSlider/ion.rangeSlider.js",
        "jqueryRange" : base_root+"/res/js/library/range/jquery.range-min.js",
        "wmes_upload"      :   base_root+"/res/js/library/wmes-upload/wmes-upload.js",
        "loading"      :   base_root+"/res/js/library/loading/loading.js",
        "interfaceCheckPwd"      :   base_root+"/res/web/interface/checkintervestpwd.js",
        "orderPlanCheckPwd"      :   base_root+"/res/web/trade/main/checkOrderPlanPwd.js",
        "minicolors"      :   base_root+"/res/js/library/minicolor/jquery.minicolors.min.js",
        "laydate"      :   base_root+"/res/js/library/laydate/laydate.js",
        "ifaSelectUser"      :   base_root+"/res/web/ifa/selectUser.js",
        "jqthumb"      :   base_root+"/res/third/jqthumb/jqthumb.js",
        "chat"      :   base_root+"/res/js/library/chat/chat.js",
        "im_wsdk"    :  "https://g.alicdn.com/aliww/h5.openim.sdk/1.0.6/scripts/wsdk.js",
        "foot"      :   base_root+"/res/js/foot.js",
        "emotion"   : base_root+"/res/js/library/emotion/emotion.js",
        "comments"   : base_root+"/res/web/strategy/memberComment.js",
        "layui":base_root+"/res/js/library/layui/layui.js",
    }
});
