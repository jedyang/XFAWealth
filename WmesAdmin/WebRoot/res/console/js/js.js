$(document).ready(function(){
	//tab
     $("#tab-search li").click(function(){
		 $(this).addClass("tab-li-crr").siblings().removeClass();
		$(".tab_wrap").children().css('display','none').eq($(this).index()).show();
	 });
      
   // 坐标栏
	 var wmes_wraper = $("#wmes_wraper").offset().top;
	 window.wemeMenuFixed = function(){
	    var wmes_window_top = $(window).scrollTop();
	    if (wmes_window_top >= wmes_wraper) {
	      $('#wmes_left_menu').css('top',0);
	    }else{
	      $('#wmes_left_menu').css('top',50 - $(window).scrollTop() );
	    }
	 };
	 $(window).on('scroll',wemeMenuFixed);
});

/*
 * 动态添加tab modify wwluo 160803
 */
var PREFIX_LI = 'tab-li-';
var PREFIX_DIV = 'tab-div-';
//var liArray = new Array();
//var indexCount = 0 ;
function showform(id,title,url) {
	if('' == title || null == title)
		title =  window.parent.OPEN_NEW_TAB;
	if('' == id || null == id)
	    id = new Date().getTime();
	// tab数量控制
	var tabCount = 6;
	var liCount = $( '#tab-search' ).find('li').size();
	if(tabCount <= liCount){
		 BootstrapDialog.show({
			title : window.parent.GLOBAL_PROMPT,
			cssClass : 'login-dialog',
			type:BootstrapDialog.TYPE_PRIMARY,
     	    draggable : true,
			message : $(
				     '<div id="warn">'
						    + '<p>' + window.parent.GLOBAL_CONTENT + '</p>'
				    +'</div>'),
			buttons : [{
				label : window.parent.GLOBAL_CLOSE,
				action : function(dialogItself) {
					dialogItself.close();
				}
			} ]
		});	 
	}else{
	    // 判断是否已存在该tab
		if($('#'+ PREFIX_LI + id).size() != 0){
			var existLi = $('#' + PREFIX_LI + id);
			existLi.trigger('click');
		}else{
			// 增加iframe Div 
			var iframe = $( '<div style="display:none;" id="'+ PREFIX_DIV + id + '"><iframe></iframe></div>' );
		    iframe.find( 'iframe' ).attr( 'width', '100%');
		    iframe.find( 'iframe' ).attr( 'name',id );
		    iframe.find( 'iframe' ).attr( 'frameborder','0' );
		    iframe.find( 'iframe' ).attr( 'src',url );
		    iframe.find( 'iframe' ).attr( 'scrolling','yes' );
		    $( '.tab_wrap' ).append(iframe);
//		    旧设置子tab页面高度方法
//		    var bHeight= $('iframe[name="'+id+'"]').context.body.offsetHeight;
//		    var outerHeight= $('iframe[name="'+id+'"]')[0].contentWindow.outerHeight;
//		    var height = Math.max(bHeight, outerHeight); 
//		    iframe.find( 'iframe' ).attr( 'height',height+100 );//modified by michael 20170322
//		    iframe.css('display','none');
//		    新设置子tab页面高度方法170613
		    var iframeHeight = $('iframe[name="'+id+'"]').context.body.scrollHeight; 
		    iframe.find( 'iframe' ).attr( 'height',iframeHeight);
		    iframe.css('display','none');
		    
		    // 增加tab Li
		    $( '#tab-search' ).append( '<li id="' + PREFIX_LI + id + '">'
		    		+ '<a href="#' + PREFIX_DIV + id + '">' + title + '<span class="ui-icon ui-icon-close" role="presentation"></span></a></li>' );
		    // 初始化tabs
		    var tabs = $( '.r-side-container' ).tabs();
		    // tab点击事件（焦点事件）
		    $( '#tab-search li' ).click(function(){
				$(this).addClass('tab-li-crr').siblings().removeClass();
				$( 'iframe' ).parent().css('display','none');
				$('.tab_wrap').css('display','none');
				if( 0 != $(this).index() ){
				   $('.tab_wrap').show();
				   //$(".tab_wrap").find('.form-horizontal').css('display','none').eq($(this).index()).show();
				   $(".tab_wrap").children().css('display','none').eq($(this).index()).show();
				}else {
				   $('.tab_wrap').show(); 
				}
			});
		    tabs.tabs( 'refresh' );
			// 定位当前tab
		    $( '#tab-search li:last' ).trigger('click');
		    
		    // 关闭tab
		    tabs.on( 'click', 'span.ui-icon-close', function() {
			      var currentLi = $( this ).closest( 'li' );
			      var iframeDiv = currentLi.find('a').attr('href');
			      currentLi.remove();
			      $( iframeDiv ).remove();
			      tabs.tabs( 'refresh' );
			      $( '#tab-search li:last' ).trigger('click');
		    });
		}
	}
}

/*
 * tab页面关闭按钮   wwluo 160804
 */
function closeTab() {
	var id = window.name;
	var location = window.location.pathname;
	window.parent.$('#' + PREFIX_DIV + id).css('display','none');
	window.parent.$( '.r-side-container' ).tabs();
	var li = window.parent.$('#tab-search').find('li');
	var crrId = li.eq(li.size()-1).attr('id');
	if(crrId == PREFIX_LI + id)
		li.eq(li.size()-2).trigger('click');
	else
	li.eq(li.size()-1).trigger('click');
	window.parent.$('#' + PREFIX_LI + id).remove();//tabLi
	window.parent.$('#' + PREFIX_DIV + id).remove();//iframeDiv
}
/**
 * 根据frameName关闭页签
 * qgfeng 161122
 * @param frameName
 */
function closeTabByName(frameName){
	var TabFrame = window.frames[frameName];
	if(TabFrame!=null || TabFrame!=undefined){
		TabFrame.closeTab();
	}
}

/*
 * 切换工作台语言
 */
$(".langChange").on("click",function(){	
	$.get(base_root+"/console/site/changLang.do?langFlag="+$(this).attr("langCode")+"&datestr="+new Date().getTime(), function(data, status, Request){
		if(data != null){
			var url = window.location.href;
			url = url.replace("#", "");
			window.location.href=url;		
		}
	});
});

/*
 * 退出工作台
 */
$("#logout").on("click",function(){	
	if(confirm(logoutMsg)){
		window.location.href=base_root+"/console/logout.do";		
	}	
});

/*
 * 获取Url传递参数值
 */
function getUrlParam(name){  
    //构造一个含有目标参数的正则表达式对象  
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
    //匹配目标参数  
    var r = window.location.search.substr(1).match(reg);  
    //返回参数值  
    if (r!=null) return unescape(r[2]);  
    return null;  
}

//JavaScript Document
var i=0;
function loadjscssfile(filename,filetype){
    if(filetype == "js"){
        var fileref = document.createElement('script');
        fileref.setAttribute("type","text/javascript");
        fileref.setAttribute("src",filename);
    }else if(filetype == "css"){
        var fileref = document.createElement('link');
        fileref.setAttribute("rel","stylesheet");
        fileref.setAttribute("type","text/css");
        fileref.setAttribute("href",filename);
    }/**/
    if(typeof fileref != "undefined"){
        document.getElementsByTagName("head")[0].appendChild(fileref);
        fileref.onload=fileref.onreadystatechange=function(){  
        	if(!this.readyState||this.readyState=='loaded'||this.readyState=='complete'){  
        		i++;
        		//这里标志着css文件和js文件加载完毕了
        		/*if(i==dataLength){
        			$("#menuList").treetable({ expandable: true });
        			
    				$("#menuList tbody").on("mousedown", "tr", function() {
    						$(".selected").not(this).removeClass("selected");
    						$(this).toggleClass("selected");
    				});
        		}*/
        	}  
        	fileref.onload=fileref.onreadystatechange=null;
        };
   }
}
//是否IE浏览器 mqzou 2016-08-04
function browserIsIe() {
	if (!!window.ActiveXObject || "ActiveXObject" in window)
		return true;
	else
		return false;
}
//下载图片  mqzou 2016-08-04
function DownLoadReportIMG(imgPathURL, filename) {

	//如果隐藏IFRAME不存在，则添加  
	if (!document.getElementById("IframeReportImg")) {
		var html = '<iframe style="display:none;" id="IframeReportImg" name="IframeReportImg" onload="DoSaveAsIMG();" width="0" height="0" src="about:blank"></iframe>';
		$(html).appendTo("body");
	}

	if (document.all.IframeReportImg.src != imgPathURL) {
		//加载图片  
		document.all.IframeReportImg.src = imgPathURL;
	} else {
		//图片直接另存为  
		DoSaveAsIMG(filename);
	}
}
// 保存图片 mqzou 2016-08-04
function DoSaveAsIMG(filename) {
	if (document.all.IframeReportImg.src != "about:blank") {
		//document.getElementById("IframeReportImg").document.execCommand("SaveAs");
		$(document.getElementById("IframeReportImg")).prop('contentWindow').document.execCommand("saveAs", filename);
	}

}
// 自动查询功能
function setAutoSearch(element, changeMethod, params) {
	var elements = $(element);
	if (elements != undefined && elements != null) {
		$.each(elements, function(i, el) {
			if ($(el).is('input')) {
				if ($(el).prop('type') == 'text') {
					if ($(el).hasClass('Wdate') || $(el).hasClass('form-control-laydate')) {
						$(el).bind('change', function() {
							changeMethod(params);
						});
						$(el).bind('blur', function() {
							$(el).change();
						})
					} else {
						$(el).bind('input', function() {
							changeMethod(params);
						});
					}

				} else {
					$(el).bind('change', function() {
						changeMethod(params);
					});
				}
			} else if ($(el).is('select')) {
				$(el).bind('change', function() {
					changeMethod(params);
				});
			}
		});
	}
}

	function initInnerHeight(name1,name2){
	  var iframeid1=parent.parent.document.getElementById(name1); //iframe id
	  var iframeid2=parent.document.getElementById(name2); //iframe id
	 	  if (document.getElementById){
		  	  if (iframeid2 && !window.opera){
		  
				    if (iframeid2.contentDocument && iframeid2.contentDocument.body.offsetHeight){
				         iframeid2.height = iframeid2.contentDocument.body.offsetHeight;
				    }else if(iframeid2.Document && iframeid2.Document.body.scrollHeight){
				    	   iframeid2.height = iframeid2.Document.body.scrollHeight;
				    }
			   }
			   if (iframeid1 && !window.opera){
				    
				    if (iframeid1.contentDocument && iframeid1.contentDocument.body.offsetHeight){
				        iframeid1.height = iframeid1.contentDocument.body.offsetHeight;
				    }  
				    else if(iframeid1.Document && iframeid1.Document.body.scrollHeight){
				        iframeid1.height = iframeid1.Document.body.scrollHeight;
	 			    }
			   }
			   
	 	  }
	}
	//金额格式化
	function formatMoney(num,suffix){
		if (!num) return '0.00'+suffix;  		
		num = num.toString().replace(/\$|\,/g,'');    
	    if(isNaN(num))    
	    num = "";    
	    var sign = (num == (num = Math.abs(num)));    
	    num = Math.floor(num*100+0.50000000001);    
	    var cents = num%100;    
	    num = Math.floor(num/100).toString();    
	    if(cents<10)    
	    cents = "0" + cents;    
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
	    num = num.substring(0,num.length-(4*i+3))+','+    
	    num.substring(num.length-(4*i+3)); 
	    var value = (((sign)?'':'-') + num + '.' + cents);
	    if(suffix){value = value+suffix;}
	    return value;
	}
