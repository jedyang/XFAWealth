/**
 * loading状态
 * 	功能： loading状态，显示状态下，
 * 	调用方式：
 * 	var Loading = require('loading');
 * 	var oLoading = new Loading(dom);//实例化 传的参数是放loading的位置
 *  oLoading.show();				//显示
 *  oLoading.hide();				//隐藏
 */
define(function(require) {
	var $ = require('jquery');

	function Loading(dom) {

		//没有的话就放在body
		if(!dom) dom = $(document.body);

		if (dom.find(".spinner").length > 0 ) {
			return;
		}
		var html = 	'<div class="spinner"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div>'
					
		var oLoading = document.createElement('div');
		oLoading.innerHTML = html;
		oLoading.style.display = 'none';
		dom.append( oLoading );
		this.el = oLoading;

	}

	Loading.prototype = {

		/**
		 * 显示
		 */
		show: function() {
			var self = this;
			self.el.style.display = 'block';
		},

		/**
		 * 隐藏
		 */
		hide: function() {			
			var self = this;
			self.el.style.display = 'none';
		},

		constructor: Loading
	};



	return Loading;
});