
define(function(require) {
	//依赖
	var $ = require('jquery');
	require("layui");
	require("swiper");
	//require("jqthumb");
	
	
//	 $('.img-box>img').jqthumb({
//		  classname      : 'jqthumb',
//          width          : 112,
//          height         : 100,
//          showoncomplete : true
//     });
	$(".comment-show-button").on("click",function(){
		$(this).parents(".conner-contens-rows").toggleClass("conner-comment-show");
	});
	
	//回复主题
	$(".connet-add-button").on("click",function(){
		var comment = $(this).prev('.connet-add-text').val();//评论信息
		var cornerId = $(this).attr('cornerid');
		if(comment==''){ layer.msg('Please Input The Reply Message!', {icon: 0}); return;}
		var self = $(this);
		//保存评论信息
		  $.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/info/replyCornerInfo.do?datestr="+ new Date().getTime(),
				data : {'cornerId':cornerId,'content':comment},
				beforeSend: function () {},
				complete: function () {},
				success : function(json) {
					var obj = $.parseJSON(json); 
					if(obj.result){
						//layer.msg('Reply corner successful!'); 发表成功不再显示提示信息
						$('#sinaEmotion').remove();//发表后把图标框删除
						//发表成功后，添加到conner-comment-list后面
						var nickName = $('#hidCurLoginNickName').val();
						var li_html = '<li class="corner-fadein">';
         					li_html += '<p class="comment-list-name">'+nickName+':</p>';
         					li_html +='<p class="comment-list-word">'+parseEmotion(comment)+'</p>';
         					li_html += '</li>';
						self.parent('.conner-add-comment').next('.conner-comment-contents').find('.conner-comment-list').append(li_html);
						self.prev('.connet-add-text').val("");//评论输入框置空
						//评论成功后收到评论框
						var conner_rows_comment = self.parents('.conner-add-comment').parents('.conner-contens-rows').eq(0);
						//console.log(conner_rows_comment.attr('class'));
						if(conner_rows_comment.hasClass('conner-comment-show')==true){
							conner_rows_comment.toggleClass("conner-comment-show");
						}
					}
				}
			});
	});
	
	function removeClass(){
		$animate.removeClass();
	}
	
	//点赞主题
	$(".img-liked").on("click",function(){ 
		var cornerId = $(this).attr('cornerid');
		if(cornerId==''){ layer.msg('CornerId Error!', {icon: 0}); return;}
		var self = $(this);
		//保存评论信息
		  $.ajax({
				type : 'post',
				datatype : 'json',
				url : base_root + "/front/ifa/info/likedCornerInfo.do?datestr="+ new Date().getTime(),
				data : {'cornerId':cornerId},
				beforeSend: function () {},
				complete: function () {},
				success : function(json) {
					var obj = $.parseJSON(json); 
					if(obj.result){
						//layer.msg('Liked the info successful!'); 点赞成功不再显示提示信息
						//点赞后页面的赞数+1效果
						var oldLikedCount = self.next('span').text();
						var newLikedCount = parseInt(oldLikedCount)+1;
						self.next('span').text(newLikedCount);
						var likeelement = '#conner-comment-like-'+cornerId+'';
						$(likeelement).append('<p class="conner-like-name">'+obj.nickname+',</p>').show();
					}
				}
			});
	});
	
	//分享主题
	$(".img-reprint").on("click",function(){ 
		//先下拉选择分享到那
		var imgShareElementId = $(this).attr('id');
		var cornerid = $(this).attr('cornerid');
		var cornerShareElement = '#conner-share-'+cornerid;
		
		var ps = $(this).position();  
        $(cornerShareElement).css("position", "absolute");  
        $(cornerShareElement).css("left", ps.left+10 ); //距离左边距  
        $(cornerShareElement).css("top", ps.top+20 ); //距离上边距  
        $(cornerShareElement).attr('imgShareElementId',imgShareElementId);
        $(cornerShareElement).toggleClass("box-show");
	});
	
	//关闭分享
	$(".bshare-close").on("click",function(){ 
		$(this).parent().toggleClass("box-show");
	});

	//转载主题
	$(".goog-menuitem").on("click",function(){ 
		$("#box").toggleClass("box-show");
		//获取点击来源
		var imgShareElementId = $('#box').attr('imgShareElementId');
		var newImgShareElementId = '#'+imgShareElementId;
		var imgShareElement = $(newImgShareElementId);
		var cornerId = imgShareElement.attr('cornerid');
		var cornerAuthorId = imgShareElement.attr('cornerAuthorId');
		var authorHeaderImg = imgShareElement.attr('authorHeaderImg');
		if(cornerId==''){ layer.msg('CornerId Error!', {icon: 0}); return;}
		//获取原有标题作为内容
		var self = imgShareElement;
		var content = self.parents('.conner-contens-rows').find('.conner-rows-title').text();
		//取得原有主题的内容
		var oldCornerContent = self.parents('.conner-contens-rows').find('.conner-rows-word').html();
		//页面层
		layer.open({
		  type: 1,
		  title:'Share Corner Info',
		  btn: ['Share', 'Close'],
		  yes:function(index){ 
			  //var elelent_id = '#'+cornerId;
			  var title = $('#txtTitle').val();
			  //保存备注信息
			  $.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/ifa/info/reprintCornerInfo.do?datestr="+ new Date().getTime(),
					data : {'cornerId':cornerId,'title':title,'content':content,'cornerAuthorId':cornerAuthorId},
					beforeSend: function () {},
	                complete: function () {},
					success : function(json) {
						var obj = $.parseJSON(json); 
						if(obj.result){
							layer.msg('Reprint Corner Info Successful!', {icon: 1},function(){ layer.closeAll();});
						}
					}
				});
		  },
		  skin: 'layui-layer-rim', //加上边框
		  area: ['700px', '400px'], //宽高
		  content: '<div><input class="login_content_input" type="text" id="txtTitle" style="margin-bottom: 5px;margin-left: 5px;margin-top: 10px;width:95%; color:#000000;z-index:1;" placeholder="please input something "></div>'
			+'<div style="height:1px;line-height:1px;background-color:#ecf6fc;margin-bottom:10px;">&nbsp;</div>'
			+ '<div class="conner-top-wrap">'
	   		+ '<img class="corners-top-portrait" style="margin-left:10px;" src="/wmes'+authorHeaderImg+'">'
	   		+ '<div class="conner-top-infor">'
	   		+ '<p class="conner-top-title" style="font-size:15px;margin-bottom:10px;">'+content+'</p>'
	   		+ '<div class="conner-top-ico">'
	   		+ '<div class="conner-ico-wrap" style="margin-right:0px;">'
	   		+ '<p class="conner-ico-title" style="text-indent:2em;">'+oldCornerContent+'</p>'
	   		+ '</div>'
	   		+ '</div>'
	   		+ '</div>'
	   		+ '</div>'
		//<input class="login_content_input" type="text" id="txtTitle" style="margin-bottom: 5px;margin-left: 5px;margin-top: 10px;width:95%; color:#000000;" placeholder="please input something "><textarea readonly id="'+cornerId+'" style=" height: 150px;width: 95%;margin: 5px 5px 5px 5px;border:#dcdadb 1px solid;">'+content+'</textarea>
		});
	});
	
	//
	$(".img-faces").on("click",function(){ 
		
		var sinaEmotion = $('#sinaEmotion');
		var show = sinaEmotion.attr('show');
		
		if(show =='1') { //点击时，如果发现存在，则隐藏
			$('#sinaEmotion').remove();
			event.stopPropagation();
		} 
		else {
			$('#sinaEmotion').remove();
			//加载图标圈
			var faceHtml = ' <div show="1" id="sinaEmotion" >';
			faceHtml += '<div class="right"></div>';
			faceHtml += '<ul class="categories">';
			faceHtml += '<li class="item">';
			faceHtml += '<a  class="category current">WemsIonc</a>';
			faceHtml += '</li>';
			faceHtml += '</ul>';
			faceHtml += '<ul class="faces">';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/pcmoren_huaixiao_thumb.png" alt="[FACE1001]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/pcmoren_tian_thumb.png" alt="[FACE1002]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/pcmoren_wu_thumb.png" alt="[FACE1003]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/huanglianwx_thumb.gif" alt="[FACE1004]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/tootha_thumb.gif" alt="[FACE1005]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/laugh.gif" alt="[FACE1006]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/tza_thumb.gif" alt="[FACE1007]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/kl_thumb.gif" alt="[FACE1008]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/wabi_thumb.gif" alt="[FACE1009]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cj_thumb.gif" alt="[FACE1010]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/shamea_thumb.gif" alt="[FACE1011]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/zy_thumb.gif" alt="[FACE1012]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/bz_thumb.gif" alt="[FACE1013]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/bs2_thumb.gif" alt="[FACE1014]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/lovea_thumb.gif" alt="[FACE1015]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sada_thumb.gif" alt="[FACE1016]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/heia_thumb.gif" alt="[FACE1017]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/qq_thumb.gif" alt="[FACE1018]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sb_thumb.gif" alt="[FACE1019]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/mb_thumb.gif" alt="[FACE1020]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/landeln_thumb.gif" alt="[FACE1021]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/yhh_thumb.gif" alt="[FACE1022]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/zhh_thumb.gif" alt="[FACE1023]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/x_thumb.gif" alt="[FACE1024]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cry.gif" alt="[FACE1025]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/wq_thumb.gif" alt="[FACE1026]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/t_thumb.gif" alt="[FACE1027]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/haqianv2_thumb.gif" alt="[FACE1028]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/bba_thumb.gif" alt="[FACE1029]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/angrya_thumb.gif" alt="[FACE1030]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/yw_thumb.gif" alt="[FACE1031]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cza_thumb.gif" alt="[FACE1032]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/88_thumb.gif" alt="[FACE1033]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sk_thumb.gif" alt="[FACE1034]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sweata_thumb.gif" alt="[FACE1035]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/kunv2_thumb.gif" alt="[FACE1036]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/huangliansj_thumb.gif" alt="[FACE1037]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/money_thumb.gif" alt="[FACE1038]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sw_thumb.gif" alt="[FACE1039]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cool_thumb.gif" alt="[FACE1040]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/huanglianse_thumb.gif" alt="[FACE1041]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/hatea_thumb.gif" alt="[FACE1042]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/gza_thumb.gif" alt="[FACE1043]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/dizzya_thumb.gif" alt="[FACE1044]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/bs_thumb.gif" alt="[FACE1045]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/crazya_thumb.gif" alt="[FACE1046]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/h_thumb.gif" alt="[FACE1047]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/yx_thumb.gif" alt="[FACE1048]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/numav2_thumb.gif" alt="[FACE1049]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/hufen_thumb.gif" alt="[FACE1050]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/hearta_thumb.gif" alt="[FACE1051]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/unheart.gif" alt="[FACE1052]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/pig.gif" alt="[FACE1053]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/panda_thumb.gif" alt="[FACE1054]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/rabbit_thumb.gif" alt="[FACE1055]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/ok_thumb.gif" alt="[FACE1056]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/ye_thumb.gif" alt="[FACE1057]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/good_thumb.gif" alt="[FACE1058]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/buyao_org.gif" alt="[FACE1059]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/z2_thumb.gif" alt="[FACE1060]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/come_thumb.gif" alt="[FACE1061]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/sad_thumb.gif" alt="[FACE1062]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/shenshou_thumb.gif" alt="[FACE1063]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/horse2_thumb.gif" alt="[FACE1064]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/j_thumb.gif" alt="[FACE1065]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/fuyun_thumb.gif" alt="[FACE1066]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/geiliv2_thumb.gif" alt="[FACE1067]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/wg_thumb.gif" alt="[FACE1068]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/vw_thumb.gif" alt="[FACE1069]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/huatongv2_thumb.gif" alt="[FACE1070]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/lazhuv2_thumb.gif" alt="[FACE1071]"></a></li>';
			faceHtml += '<li class="item"><a  class="face"><img class="sina-emotion" src="' + base_root + '/res/images/cornericon/cakev2_thumb.gif" alt="[FACE1072]"></a></li>';
			faceHtml += '</ul></div>';
			//获取父元素
			var parentElement = $(this).parent();
			$(parentElement).append(faceHtml);
			//获取该按钮的位置
			var pos = $(this).position();
			var pos_x = pos.left+20;
			var pos_y = pos.top+10;
			$('#sinaEmotion').css({'left' : pos_x, 'top' : pos_y});
			$('#sinaEmotion').show();
			event.stopPropagation();
		}
	});
	
	//显示图片扩大
	$("body").on("click",".img-box",function(){ 
		//页面层
		var html = '<div class="swiper-container">';
		html += '<div class="swiper-wrapper">';
		var img_list = $(this).parents('.photo-list').find('ul');
		//console.log(img_list.html());
		$(img_list.html()).find('.img-box').each(function(i){ 
			var big_src = $(this).find('img').attr('bigsrc');
			//console.log(big_src);
			var li_html = '<div class="swiper-slide">';
				li_html += '<img data-src="'+big_src+'" class="swiper-lazy">';
				li_html += '<div class="swiper-lazy-preloader swiper-lazy-preloader-white"></div>';
				li_html += '</div>';
				html += li_html;
		});
		html += '</div>';
		html += '<div class="swiper-pagination swiper-pagination-white"></div>';
		html += '<div class="swiper-button-next swiper-button-white"></div>';
		html += '<div class="swiper-button-prev swiper-button-white"></div>';
		html += '</div>';
		//console.log(html);
		layer.open({
		  type: 1,
		  title:'',
		  skin: '', //加上边框
		  area: ['80%', '80%'], //宽高
		  content: html,
		  success:function(){ 
			  var swiper = new Swiper('.swiper-container', {
			        nextButton: '.swiper-button-next',
			        prevButton: '.swiper-button-prev',
			        pagination: '.swiper-pagination',
			        paginationClickable: true,
			        // Disable preloading of all images
			        preloadImages: false,
			        // Enable lazy loading
			        lazyLoading: true
			    });
		  }
		});
	});
	//点击图标时
	$("body").on("click",".sina-emotion",function(){ 
		//alert(parseEmotion('[白眼]我们还能说什么呢？中国足球就是烂滩子了。。。。。我们还能说什么呢？中国足球就是烂滩子了。。。。。我们还能说什么呢？中国足球就是烂滩子了。。。。。我们还能说什么呢？中国足球就是烂滩子了。。。。。我们还能说什么呢？中国足球就是烂滩子了。。。。。[嘻嘻]'));
		//$('#sinaEmotion').hide();
		//获取输入框
		var iconText = $(this).prop('alt');
		var oldValue = $('#sinaEmotion').prev().prev().prev().val();
		var newValue = oldValue + iconText;
		$('#sinaEmotion').prev().prev().prev().val(newValue);
		//$('.connet-add-text').insertText($(this).children('img').prop('alt'));
		event.preventDefault();
	});
	
	function parseEmotion(html){
		var emotionsMap;
		emotionsMap = {};
		emotionsMap['[FACE1001]'] = base_root + '/res/images/cornericon/pcmoren_huaixiao_thumb.png';
		emotionsMap['[FACE1002]'] = base_root + '/res/images/cornericon//pcmoren_tian_thumb.png';
		emotionsMap['[FACE1003]'] = base_root + '/res/images/cornericon//pcmoren_wu_thumb.png';
		emotionsMap['[FACE1004]'] = base_root + '/res/images/cornericon/huanglianwx_thumb.gif';
		emotionsMap['[FACE1005]'] = base_root + '/res/images/cornericon/tootha_thumb.gif';
		emotionsMap['[FACE1006]'] = base_root + '/res/images/cornericon/laugh.gif';
		emotionsMap['[FACE1007]'] = base_root + '/res/images/cornericon/tza_thumb.gif';
		emotionsMap['[FACE1008]'] = base_root + '/res/images/cornericon/kl_thumb.gif';
		emotionsMap['[FACE1009]'] = base_root + '/res/images/cornericon/wabi_thumb.gif';
		emotionsMap['[FACE1010]'] = base_root + '/res/images/cornericon/cj_thumb.gif';
		emotionsMap['[FACE1011]'] = base_root + '/res/images/cornericon/shamea_thumb.gif';
		emotionsMap['[FACE1012]'] = base_root + '/res/images/cornericon/zy_thumb.gif';
		emotionsMap['[FACE1013]'] = base_root + '/res/images/cornericon/bz_thumb.gif';
		emotionsMap['[FACE1014]'] = base_root + '/res/images/cornericon/bs2_thumb.gif';
		emotionsMap['[FACE1015]'] = base_root + '/res/images/cornericon/lovea_thumb.gif';
		emotionsMap['[FACE1016]'] = base_root + '/res/images/cornericon/sada_thumb.gif';
		emotionsMap['[FACE1017]'] = base_root + '/res/images/cornericon/heia_thumb.gif';
		emotionsMap['[FACE1018]'] = base_root + '/res/images/cornericon/qq_thumb.gif';
		emotionsMap['[FACE1019]'] = base_root + '/res/images/cornericon/sb_thumb.gif';
		emotionsMap['[FACE1020]'] = base_root + '/res/images/cornericon/mb_thumb.gif';
		emotionsMap['[FACE1021]'] = base_root + '/res/images/cornericon/landeln_thumb.gif';
		emotionsMap['[FACE1022]'] = base_root + '/res/images/cornericon/yhh_thumb.gif';
		emotionsMap['[FACE1023]'] = base_root + '/res/images/cornericon/zhh_thumb.gif';
		emotionsMap['[FACE1024]'] = base_root + '/res/images/cornericon/x_thumb.gif';
		emotionsMap['[FACE1025]'] = base_root + '/res/images/cornericon/cry.gif';
		emotionsMap['[FACE1026]'] = base_root + '/res/images/cornericon/wq_thumb.gif';
		emotionsMap['[FACE1027]'] = base_root + '/res/images/cornericon/t_thumb.gif';
		emotionsMap['[FACE1028]'] = base_root + '/res/images/cornericon/haqianv2_thumb.gif';
		emotionsMap['[FACE1029]'] = base_root + '/res/images/cornericon/bba_thumb.gif';
		emotionsMap['[FACE1030]'] = base_root + '/res/images/cornericon/angrya_thumb.gif';
		emotionsMap['[FACE1031]'] = base_root + '/res/images/cornericon/yw_thumb.gif';
		emotionsMap['[FACE1032]'] = base_root + '/res/images/cornericon/cza_thumb.gif';
		emotionsMap['[FACE1033]'] = base_root + '/res/images/cornericon/88_thumb.gif';
		emotionsMap['[FACE1034]'] = base_root + '/res/images/cornericon/sk_thumb.gif';
		emotionsMap['[FACE1035]'] = base_root + '/res/images/cornericon/sweata_thumb.gif';
		emotionsMap['[FACE1036]'] = base_root + '/res/images/cornericon/kunv2_thumb.gif';
		emotionsMap['[FACE1037]'] = base_root + '/res/images/cornericon/huangliansj_thumb.gif';
		emotionsMap['[FACE1038]'] = base_root + '/res/images/cornericon/money_thumb.gif';
		emotionsMap['[FACE1039]'] = base_root + '/res/images/cornericon/sw_thumb.gif';
		emotionsMap['[FACE1040]'] = base_root + '/res/images/cornericon/cool_thumb.gif';
		emotionsMap['[FACE1041]'] = base_root + '/res/images/cornericon/huanglianse_thumb.gif';
		emotionsMap['[FACE1042]'] = base_root + '/res/images/cornericon/hatea_thumb.gif';
		emotionsMap['[FACE1043]'] = base_root + '/res/images/cornericon/gza_thumb.gif';
		emotionsMap['[FACE1044]'] = base_root + '/res/images/cornericon/dizzya_thumb.gif';
		emotionsMap['[FACE1045]'] = base_root + '/res/images/cornericon/bs_thumb.gif';
		emotionsMap['[FACE1046]'] = base_root + '/res/images/cornericon/crazya_thumb.gif';
		emotionsMap['[FACE1047]'] = base_root + '/res/images/cornericon/h_thumb.gif';
		emotionsMap['[FACE1048]'] = base_root + '/res/images/cornericon/yx_thumb.gif';
		emotionsMap['[FACE1049]'] = base_root + '/res/images/cornericon/numav2_thumb.gif';
		emotionsMap['[FACE1050]'] = base_root + '/res/images/cornericon/hufen_thumb.gif';
		emotionsMap['[FACE1051]'] = base_root + '/res/images/cornericon/hearta_thumb.gif';
		emotionsMap['[FACE1052]'] = base_root + '/res/images/cornericon/unheart.gif';
		emotionsMap['[FACE1053]'] = base_root + '/res/images/cornericon/pig.gif';
		emotionsMap['[FACE1054]'] = base_root + '/res/images/cornericon/panda_thumb.gif';
		emotionsMap['[FACE1055]'] = base_root + '/res/images/cornericon/rabbit_thumb.gif';
		emotionsMap['[FACE1056]'] = base_root + '/res/images/cornericon/ok_thumb.gif';
		emotionsMap['[FACE1057]'] = base_root + '/res/images/cornericon/ye_thumb.gif';
		emotionsMap['[FACE1058]'] = base_root + '/res/images/cornericon/good_thumb.gif';//res/images/cornericon/'/ext/normal/d8/good_thumb.gif';
		emotionsMap['[FACE1059]'] = base_root + '/res/images/cornericon/buyao_org.gif';
		emotionsMap['[FACE1060]'] = base_root + '/res/images/cornericon/z2_thumb.gif';
		emotionsMap['[FACE1061]'] = base_root + '/res/images/cornericon/come_thumb.gif';
		emotionsMap['[FACE1062]'] = base_root + '/res/images/cornericon/sad_thumb.gif';
		emotionsMap['[FACE1063]'] = base_root + '/res/images/cornericon/shenshou_thumb.gif';
		emotionsMap['[FACE1064]'] = base_root + '/res/images/cornericon/horse2_thumb.gif';
		emotionsMap['[FACE1065]'] = base_root + '/res/images/cornericon/j_thumb.gif';
		emotionsMap['[FACE1066]'] = base_root + '/res/images/cornericon/fuyun_thumb.gif';
		emotionsMap['[FACE1067]'] = base_root + '/res/images/cornericon/geiliv2_thumb.gif';
		emotionsMap['[FACE1068]'] = base_root + '/res/images/cornericon/wg_thumb.gif';
		emotionsMap['[FACE1069]'] = base_root + '/res/images/cornericon/vw_thumb.gif';
		emotionsMap['[FACE1070]'] = base_root + '/res/images/cornericon/huatongv2_thumb.gif';
		emotionsMap['[FACE1071]'] = base_root + '/res/images/cornericon/lazhuv2_thumb.gif';
		emotionsMap['[FACE1072]'] = base_root + '/res/images/cornericon/cakev2_thumb.gif';
		
		html = html
				.replace(/<.*?>/g, function($1) {
					$1 = $1.replace('[', '[');
					$1 = $1.replace(']', ']');
					return $1;
				})
				.replace(
						/\[[^\[\]]*?\]/g,
						function($1) {
							var url = emotionsMap[$1];
							if (url) {
								return '<img class="wmes-emotion" src="'
										+ url
										+ '" alt="'
										+ $1 + '" />';
							}
							return $1;
						});

		return html;
	}
	
//	//$('#a-bshare-wmes').css({'background':'url('+base_root+'/res/images/discover/weixin.gif) no-repeat','display':'inline-block'});
//	var cornerTitle = $(".conner-rows-title");  
    bShare.entries = [];
    $.each(cornerTitle,function(i,n){ 
    	var summaryContent = $(n).text();
    	////console.log(summaryContent);
    	bShare.addEntry({  
            title: 'XFA Wealth 分享',  
            url: window.location.href,//"http://fb.gmcc.net/facebook.html",  
            summary: summaryContent
            //pic: "http://avatar.csdn.net/B/9/3/1_pukuimin1226.jpg"  
        });  
    });
//    ////console.log(bShare.entries)
    
	
});