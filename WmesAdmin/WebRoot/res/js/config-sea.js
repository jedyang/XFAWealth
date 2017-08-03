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
        "jquery"        :   base_root+"/res/js/library/jquery/jquery.js",
        "angular"       :   base_root+"/res/js/library/angular/angular.min.js",
        "datepick"      :   base_root+"/res/js/jsDatePick.jquery.min.1.3.js",
        "webuploader"   :   base_root+"/res/js/webuploader.min.js",
        "echarts"       :   base_root+"/res/js/echarts.min.js",
        "conversion"    :   base_root+"/res/js/currconversion.js",
        "jqueryForm"    :   base_root+"/res/js/library/jquery/jquery.form.js",
        "formValid"     :   base_root+"/res/third/formValidator/js/jquery.validationEngine.js",
        "formValidLang" :   base_root+"/res/third/formValidator/languages/jquery.validationEngine-zh_CN.js",
        "scrollbar"     :   base_root+"/res/js/library/scroll/scrollbar.js",
        "range"         :   base_root+"/res/js/library/range/range.js",
        "swiper"        :   base_root+"/res/js/library/swiper/swiper.js",
        "pagination"    :   base_root+"/res/third/pagination/jquery.pagination.js",
        "layer"         :   base_root+"/res/third/layer/layer.js",
        // um
        "umeditorConfig":   base_root+"/res/js/library/ueditor/umeditor.config.js",
        "ueditor"       :   base_root+"/res/js/library/ueditor/umeditor.min.js",
        // ue
        "ueditor_ueConfig":   base_root+"/res/js/library/ueditor_ue/ueditor.config.js",
        "ueditor_ue"      :   base_root+"/res/js/library/ueditor_ue/ueditor.all.min.js",

        "iscroll"       :   base_root+"/res/js/library/iscroll/iscroll.js",
        "slider"        :   base_root+"/res/third/slider/js/jssor.slider-21.1.5.min.js",
        "moment"        :   base_root+"/res/js/library/fullcalendar/moment.min.js",
        "fullcalendar"  :   base_root+"/res/js/library/fullcalendar/fullcalendar.min.js",
        "ellips"        :   base_root+"/res/js/library/ellipsis/ellipsis.js",
        "cookies"      :   base_root+"/res/js/library/cookies/cookies.js",
        "wmes_upload"      :   base_root+"/res/js/library/wmes-upload/wmes-upload.js",
    }
});
