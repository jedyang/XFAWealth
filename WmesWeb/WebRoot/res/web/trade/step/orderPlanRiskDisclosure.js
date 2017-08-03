define(function(require) {
	var $ = require('jquery');
	
	/**
	 * agree risk disclosure
	 */
	$(document).on('click','.disclosure-operation-agree',function(){
		var params = '';
		if(!null == getUrlParam('products')){
			params = '?agreeRD=1&products=' + getUrlParam('products');
		}else{
			params = '?agreeRD=1';
		}
		if(!null == getUrlParam('currencyCode')){
			params += '&currencyCode=' + getUrlParam('currencyCode');
		}
		if(window.location.href.indexOf('?') > -1){
			params += '&' + (window.location.href.split('?'))[1];
		}
		window.location.href = base_root + '/front/tradeStep/orderPlanSelectProduct.do' + params; 
	});
	/**
	 * reject risk disclosure
	 */
	$(document).on('click','.disclosure-operation-reject',function(){
		if(window.history.length > 1){  
	        window.history.go( -1 );  
	    }else{  
	        window.location.href = base_root + '/front/tradeRecord/orderPlanList.do';  
	    } 
	});
	/**
	 * get url param
	 */
	function getUrlParam(name){  
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r!=null) return unescape(r[2]);  
	    return null;  
	}
});
