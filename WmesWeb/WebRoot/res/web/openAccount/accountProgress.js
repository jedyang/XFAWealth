/**
 * accountProgress.js 开户进度
 * @author mqzou
 * @date: 2016-09-22
 */
define(function(require) {
	var $ = require('jquery');
	require('layer');
	
	$(".progress-top-magnifier").click(function() {
		//弹出即全屏
		var index = layer.open({
			skin : 'layui-layer-lan',
			title : '查看pdf',
			type : 2,
			content : '/wmes/front/investor/dialogshowpdf.do',
			area : [ '320px', '195px' ],
			maxmin : true
		});
		layer.full(index);
	});
});