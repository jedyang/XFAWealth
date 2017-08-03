
define(function(require) {
	'use strict';
	var $ = require('jquery');
		require('datepick');
	//可视化编辑器
	require("umeditorConfig");//配置文件
	require("ueditor");//配置文件


	 new JsDatePick({
      useMode:2,
      target:"insight-date",
      dateFormat:"%Y-%m-%d"
    });
 //    g_calendarObject.setOnSelectedDelegate(function(){

	//     var obj = g_calendarObject.getSelectedDay();
	//     g_calendarObject.populateFieldWithSelectedDate();
	// });
    var ue = UM.getEditor('container');
    ue.ready(function(){
        $("#edui1").width("100%");
        $("#edui1_iframeholder").width("100%");
    });


});