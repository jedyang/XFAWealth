define(function(require) {

	var $ = require('jquery');
	  
		require("swiper");
		new Swiper('.pipeline-prospect .swiper-container', {
	        slidesPerView: 'auto',
	        grabCursor: true,
	        preventClicks : false,
	        nextButton: '.swiper-button-next',
	        prevButton: '.swiper-button-prev',
	    });
		$("#ifa-pipeline-investors").on("click",".home-investor-list-li",function(){
			var id=$(this).attr("invId");
			var name=$(this).find(".investor-list-name").text().trim();

		   /* 另一种方法
		    *  var element=parent.document.getElementById(elemId);
		    $(element).text(name);
		    $(element).attr("valuelist",id);*/
			
		    $('.'+className, parent.document).text(name);
		    $('.'+className, parent.document).attr("valuelist",id);
		    var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
	    });
})