/**
 * openAccountbasic.js basicinfo
 * @author 赵晓聪
 * email: 445752972@qq.com
 * @date: 2016-06-06
 */

define(function(require) {
	var $ = require('jquery');
   
    $("#btn_next").on("click",function(){
              window.location.href = "/index.php?r=member/accountdeclar";
    });
    // $(".picker").each(function(){
    //        var uploader = WebUploader.create({
    //           auto: true,
    //           // swf文件路径
    //           swf: '/frontend/web/js/Uploader.swf',

    //           // 文件接收服务端。
    //           server: '/frontend/web/unload',

    //           // 选择文件的按钮。可选。
    //           pick: $(this),

    //           resize: false
    //       });     
    // });

});