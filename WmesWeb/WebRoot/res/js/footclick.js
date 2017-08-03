define(function(require, exports, module) {
	var $ = require('jquery');

	$(document).on('click','.wmes-copyright li',function(){
		$('.footbox').remove();
		var footcon;
		var footcontent;
		if(typeof lang == 'undefined' || lang == ''){
			lang = 'en';
		}
		switch($(this).index()){
			case 0:
				footcontent = base_root+"/t/disclaimer/" + lang + ".html";
				footcontenttitle = langMutilGlobal['global.dissclaimer'];
			break;
			case 1:
				footcontent = base_root+"/t/privacy/" + lang + ".html";
				footcontenttitle = langMutilGlobal['global.privacy'];
			break;
			case 2:
				footcontent = base_root+"/t/riskDisclosure/" + lang + ".html";
				footcontenttitle = langMutilGlobal['global.risk.disclosure'];
			break;
		};
		
		footcon = '<div class="footbox">'
				+'<div class="footboxlay"></div>'
				+'<div class="space-mask-wrap" style="top:0px;">'
				+'<div class="footcontent">'
				+'<p>'+footcontenttitle+'</p>'
				+'<iframe src="'+footcontent+'" style="width:calc(100% - 50px);height:441px;background:#edf7fe;padding:30px 25px;box-sizing:content-box;-webkit-box-sizing:content-box;"></iframe>'
				+'<ul><li>'+langMutilGlobal['global.confirm']+'</li></ul>'
				+'</div>'
				+'</div>'
				+'</div>';
		
		$(".wmes-copyright").before(footcon);	
		
		var foottop = parseInt(($(window).height() - 647)/2 + $(window).scrollTop());
		$('.footcontent').css('top',foottop);
		
		$('.footbox').css('display','block');
	});
	
	$(document).on('click','.footcontent li',function(){
		$('.footbox').css('display','none');
	});

});