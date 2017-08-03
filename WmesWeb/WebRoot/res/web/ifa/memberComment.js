define(function(require) {
	var $ = require('jquery');
	require("layui");

	//显示回复信息
	$('.comment-list-topper-replynone').on('click',function(){
		getReply(this);
	});
	//获取回复信息
	function getReply(selt){
		var discussId = $(selt).closest('li').attr('id');
		var state = $(selt).find('span').attr('data-state');
		if(state == 1){
			$(selt).find('span').attr('data-state','0');
			$(selt).find('span').text('展开回复');
			$('#'+discussId + ' >div:gt(1)').remove();
		}else if(state == 0){
			$(selt).find('span').attr('data-state','1');
			$(selt).find('span').text('收起回复');
			var url = base_root +'/front/discover/getWebDiscussReply.do?dateStr=' + new Date().getTime();
			$.ajax({
			type:'post',
			data:{discussId:discussId},
			url:url,
			success:function(result){
				if(result.flag){
					var webDiscussReplys = result.webDiscussReplys;
					if(webDiscussReplys != null){
						var html = '';
						$.each(webDiscussReplys,function(i,n){
							var iconUrl = n.iconUrl == null ?'':n.iconUrl,
								nickName = n.nickName == null ?'':n.nickName,
								beRepliedToName = n.beRepliedTo == null ?'':n.beRepliedTo,
								timeType = n.timeType == null ?'':n.timeType,
								timeNum = n.timeNum == null ?'':n.timeNum,
								replyContent = n.replyContent == null ?'':n.replyContent,
								ups = n.ups == null ?'':n.ups,
								replyCount = n.replyCount == null ?'':n.replyCount;
							var replyTime = '';
								if(timeType == 'D'){
									replyTime = timeNum + '天前';
								}else if(timeType == 'H'){
									replyTime = timeNum + '小时前';
								}
							html += 
							      '<div id="'+n.id+'" class="comment-list-rows1">'
								 +'<img class="comment-list-portrait" src="'+base_root+iconUrl+'">'
								 +'<div class="comment-list-topper">'
								 +'<div style="width:100%;height:18px;">'
								 +'<p class="comment-list-topper-name" style="float: left;">'
								 + nickName
								 +'<span class="comment-list-topper-time">回复</span><span class="comment-list-topper-time"><p class="comment-list-topper-name"  style="float: left;">'
								 + beRepliedToName
								 +'<span class="comment-list-topper-time">'
								 + replyTime
								 +'</span></p>'
								 +'</div>'
								 +'<p class="comment-list-topper-comment">'
								 + replyContent	
								 +'</p>'
						         +'<div data-reply-id="'+n.id+'" data-is-reply="1" class="comment-list-topper-nice" ><img src="'+base_root+'/res/images/ifa/nice.png" /><span>'
						         + ups
						         +'</span></div>'
						         +'<div data-reply="'+n.id+'" class="comment-list-topper-reply"><img src="'+base_root+'/res/images/ifa/reply.png" /><span>'
						         + replyCount
						         +'</span></div>'
							     +'</div>'
			                     +'</div>'
								 +'<div id="reply'+n.id+'" class="comment-list-rowstop">'
								 +'<div class="comment-topper-contents">'
								 +'<div class="member-comment-topper">'	
								 +'<p class="member-comment-title">会员回复</p>'
								 +'<p class="member-number-words"><span class="input-length">0</span>/500</p>'	
								 +'</div>'
								 +'<textarea class="member-comment-text"></textarea>'
								 +'<div class="member-comment-reply">'
							     +'<img class="comment-expression-ico" src="'+base_root+'/res/images/discover/expression_ico.png">'	
								 +'<a data-reply-id="'+n.id+'" class="comment-reply-word reply-word" href="javascript:;">Reply</a>'	
								 +'</div>'
							     +'</div>'
						         +'</div>';
						});
						$('#'+discussId + ' >div:gt(1)').remove();
						$('#'+discussId).children().eq(1).after(html);
						
						eventBind();
					}
				}
			}
		});
		}
	}
	//刷新清除缓存
	window.sessionStorage.clear();
	//'顶'操作
	$('.comment-list-topper-nice').on('click',function(){
		updateUps(this);
	});
	//更新‘顶’数据
	function updateUps(selt){
		var ups = $(selt).find('span').text();
		ups = parseInt(ups)+1;
		var discussId = '',
			discussReplyId = '';
		var isReply = $(selt).data('isReply');
		if(isReply == '1'){
			discussReplyId = $(selt).data('replyId');
			if(window.sessionStorage.getItem(discussReplyId) == 1){
				layer.msg("Do not repeat the point praise!",{icon : 0,time : 2000});
				return false;
			}else{
				window.sessionStorage.setItem(discussReplyId,1);
			}
		}else{
			discussId = $(selt).closest('li').attr('id');
			if(window.sessionStorage.getItem(discussId) == 1){
				layer.msg("Do not repeat the point praise!",{icon : 0,time : 2000});
				return false;
			}else{
				window.sessionStorage.setItem(discussId,1);
			}
		}
		var url = base_root +'/front/discover/webDiscussUps.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			data:{
				discussId:discussId,
				discussReplyId:discussReplyId
			},
			url:url,
			success:function(result){
				if(result.flag){
					$(selt).find('span').text(ups);
				}
			}
		});
	}
	
	$('.comment-list-topper-reply').on('click',function(){
		//$(this).parents('li').find('.comment-list-rows2').toggleClass('commentactive');
		$('#reply'+$(this).data('reply')).slideToggle();
	});
	$('.comment-list-topper-replytop').on('click',function(){
		$(this).parents('li').find('.comment-list-rowstop').toggleClass('commentactive1');
		$(this).parents('li').find('.comment-list-rowstop3').toggleClass('commentactive1');
	});
	//reply 操作
	$('.reply-word').on('click',function(){
		updateReply(this);
	});
	//更新回复数据
	function updateReply(selt){
		var discussId = $(selt).closest('li').attr('id'),
			discussReplyId = $(selt).data('replyId');
		var content = $(selt).parents('.comment-topper-contents').find('.member-comment-text').val();
		var url = base_root +'/front/discover/saveWebDiscussReply.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			data:{
				discussId:discussId,
				discussReplyId:discussReplyId,
				content:content
			},
			url:url,
			success:function(result){
				if(result.flag){
					$('#reply'+discussId).hide();
					$('#reply'+discussReplyId).hide();
					//清空回复
					$(selt).parents('.comment-topper-contents').find('.member-comment-text').val('');
					//插入最新回复
					var webDiscussReply = result.webDiscussReply;
					var replyTime = '';
						if(webDiscussReply.timeType == 'D'){
							replyTime = webDiscussReply.timeNum + '天前';
						}else if(webDiscussReply.timeType == 'H'){
							replyTime = webDiscussReply.timeNum + '小时前';
						}
					var html = 
					      '<div id="'+webDiscussReply.id+'" class="comment-list-rows1">'
						 +'<img class="comment-list-portrait" src="'+base_root+webDiscussReply.iconUrl+'">'
						 +'<div class="comment-list-topper">'
						 +'<div style="width:100%;height:18px;">'
						 +'<p class="comment-list-topper-name" style="float: left;">'
						 + webDiscussReply.nickName
						 +'<span class="comment-list-topper-time">回复</span><span class="comment-list-topper-time"><p class="comment-list-topper-name"  style="float: left;">'
						 + webDiscussReply.beRepliedTo
						 +'<span class="comment-list-topper-time">'
						 + replyTime
						 +'</span></p>'
						 +'</div>'
						 +'<p class="comment-list-topper-comment">'
						 + webDiscussReply.replyContent	
						 +'</p>'
				         +'<div data-reply-id="'+webDiscussReply.id+'" data-is-reply="1" class="comment-list-topper-nice"><img src="'+base_root+'/res/images/ifa/nice.png" /><span>'
				         + webDiscussReply.ups
				         +'</span></div>'
				         +'<div data-reply="'+webDiscussReply.id+'" class="comment-list-topper-reply"><img src="'+base_root+'/res/images/ifa/reply.png" /><span>'
				         + webDiscussReply.replyCount
				         +'</span></div>'
					     +'</div>'
	                     +'</div>'
						 +'<div id="reply'+webDiscussReply.id+'" class="comment-list-rowstop">'
						 +'<div class="comment-topper-contents">'
						 +'<div class="member-comment-topper">'	
						 +'<p class="member-comment-title">会员回复</p>'
						 +'<p class="member-number-words"><span class="input-length">0</span>/500</p>'	
						 +'</div>'
						 +'<textarea class="member-comment-text"></textarea>'
						 +'<div class="member-comment-reply">'
					     +'<img class="comment-expression-ico" src="'+base_root+'/res/images/discover/expression_ico.png">'	
						 +'<a data-reply-id="'+webDiscussReply.id+'" class="comment-reply-word reply-word" href="javascript:;">Reply</a>'	
						 +'</div>'
					     +'</div>'
				         +'</div>';
				     
				     //回复数累加
					 var discussTotal = '',
				 		 state = '';
					 if(typeof(discussReplyId) == 'undefined'){
						discussTotal = $('#'+discussId).children().eq(0).find('.comment-list-topper-reply').find('span').text();
						discussTotal = parseInt(discussTotal);
						discussTotal ++;
						$('#'+discussId).children().eq(0).find('.comment-list-topper-reply').find('span').text(discussTotal);
						
						$('#'+discussId).children().eq(0).find('.comment-list-topper-replynone').show();
						state = $('#'+discussId).children().eq(0).find('.comment-list-topper-replynone').find('span').attr('data-state');
						if(state == 1){
							$('#'+discussId).children().eq(0).after(html);
						}
					 }else if(typeof(discussReplyId) != 'undefined'){
						discussTotal = $('#'+discussReplyId).find('.comment-list-topper-reply').find('span').text();
						discussTotal = parseInt(discussTotal);
						discussTotal ++;
						$('#'+discussReplyId).find('.comment-list-topper-reply').find('span').text(discussTotal);
						
						state = $('#'+discussReplyId).find('.comment-list-topper-replynone').find('span').attr('data-state');
						if(state == 1){
							$('#'+discussId).children().eq(0).after(html);
						}
					 }
				     //重新绑定事件
				     eventBind();
				}
			}
		});
	}
	//评论 操作
	$('.discover-word').on('click',function(){
		//判断是否登录，在 memberComment.html 定义了 isLogin
		if(isLogin == '' && typeof(isLogin) != 'undefined'){
			layer.msg("Please login to comment!",{icon : 0,time : 2000});	
		}else{
			updateDiscover(this);
		}
	});
	//更新评论数据
	function updateDiscover(selt){
		var moduleType = $('#hidModuleType').val(),
			relateId = $('#hidRelateId').val();
		var content = $(selt).closest('.comment-topper-contents').find('textarea').val();
		var url = base_root +'/front/discover/saveWebDiscuss.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			data:{
				moduleType:moduleType,
				relateId:relateId,
				content:content
			},
			url:url,
			success:function(result){
				////console.log(result);
				if(result.flag){
					//清除文本框数据
					$(selt).closest('.comment-topper-contents').find('textarea').val('');
					var webDiscuss = result.webDiscuss;
					var html = assemble(webDiscuss);
					//插入记录
					$('.member-comment-list').prepend(html);
					//重新绑定事件
					eventBind();
					//重新绑定显示回复信息事件
					$('.comment-list-topper-replynone').unbind('click');
					$('.comment-list-topper-replynone').on('click',function(){
						getReply(this);
					});
					//总记录+1
					var discussTotal = $('.comment-contents-number').find('span').text();
					discussTotal = parseInt(discussTotal);
					discussTotal ++;
					$('.comment-contents-number').find('span').text(discussTotal);
				}
			}
		});
	}
	
	//组装评论HTML
	function assemble(webDiscuss){
		if(webDiscuss != null){
			var timeNum = webDiscuss.timeNum,
				timeType = webDiscuss.timeType,
				replyTime = '',
				openReply = '';
			if(timeType == 'D'){
				replyTime = timeNum + '天前';
			}else if(timeType == 'H'){
				replyTime = timeNum + '小时前';
			}
			if(webDiscuss.replyCount>0){
				openReply = 'block';
			}else{
				openReply = 'none';
			}
			var html =
				 '<li id="'+webDiscuss.id+'">'
				+'<div class="comment-list-rows">'
				+'<img class="comment-list-portrait" src="'+base_root+webDiscuss.iconUrl+'">'
				+'<div class="comment-list-topper">'
				+'<p class="comment-list-topper-name">'
				+ webDiscuss.nickName
				+'<span class="comment-list-topper-time" data-create-time="'+webDiscuss.createTime+'">'
				+ replyTime
				+'</span></p>'
				+'<p class="comment-list-topper-comment">'
				+ webDiscuss.content
				+'</p>'
				+'<div data-is-reply="0" class="comment-list-topper-nice"><img src="'+base_root+'/res/images/ifa/nice.png" /><span>'
				+ webDiscuss.ups 
				+'</span></div>'
				+'<div data-reply="'+webDiscuss.id+'" class="comment-list-topper-reply"><img src="'+base_root+'/res/images/ifa/reply.png" /><span>'
				+ webDiscuss.replyCount 
				+'</span></div>'
				+'<div class="comment-list-topper-replynone" style="display:'+openReply+'"><img src="'+base_root+'/res/images/ifa/replynone.png" /><span data-state="0" >展开回复</span></div>'
				+'</div>'
				+'</div>'
				+'<div id="reply'+webDiscuss.id+'" class="comment-list-rowstop">'
				+'<div class="comment-topper-contents">'
				+'<div class="member-comment-topper">'
				+'<p class="member-comment-title">会员回复</p>'
				+'<p class="member-number-words"><span class="input-length">0</span>/500</p>'
				+'</div>'
				+'<textarea class="member-comment-text"></textarea>'
				+'<div class="member-comment-reply">'
				+'<img class="comment-expression-ico" src="'+base_root+'/res/images/discover/expression_ico.png">'
				+'<a class="comment-reply-word reply-word" href="javascript:;">Reply</a>'
				+'</div>'
				+'</div>'
				+'</div>'
				+'</li>';
			return html;
		}
	}
	
	
	//文本长度控制
	$('.member-comment-text').on('input',function(){
		var length = $(this).val().length;
		if(length > 500){
			$(this).val($(this).val().substring(0,500));
			length = 500;
			layer.msg("Comments can not exceed 500 words!",{icon : 0,time : 2000});		
		}
		$(this).parent().children().eq(0).find('.input-length').text(length);
	});
	//事件绑定
	function eventBind(){
		//回复事件
		$('.comment-list-topper-reply').unbind('click');
		$('.comment-list-topper-reply').on('click',function(){
			$('#reply'+$(this).data('reply')).slideToggle();
		});
		
		//'顶'操作
		$('.comment-list-topper-nice').unbind('click');
		$('.comment-list-topper-nice').on('click',function(){
			updateUps(this);
		});
		
		//reply 操作
		$('.reply-word').unbind('click');
		$('.reply-word').on('click',function(){
			//判断是否登录，在 memberComment.html 定义了 isLogin
			if(isLogin == ''){
				layer.msg("Please login to comment!",{icon : 0,time : 2000});	
			}else{
				updateReply(this);
			}
		});
	}		
  
	/*$(window).scroll(function(){  
	    if ($(document).height() - $(this).scrollTop() - $(this).height()<0){
	    	//console.log($(document).height() - $(this).scrollTop() - $(this).height());
		    alert();
	    }  
	});*/
	
	$('.more-more').click(function(){
		var moduleType = $('#hidModuleType').val(),
			relateId = $('#hidRelateId').val();
		var rows = $('.member-comment-list li').length+10;
		var url = base_root +'/front/discover/getWebDiscuss.do?dateStr=' + new Date().getTime();
		$.ajax({
			type:'post',
			url:url,
			data:{
				rows:rows,
				moduleType:moduleType,
				relateId:relateId
			},
			success:function(result){
				var html = '';
				var webDiscusss = result.webDiscusss;
				$.each(webDiscusss.list,function(i,n){
					html += assemble(n);
				});
				//插入记录
				$('.member-comment-list').empty().prepend(html);
				//重新绑定事件
				eventBind();
				//重新绑定显示回复信息事件
				$('.comment-list-topper-replynone').unbind('click');
				$('.comment-list-topper-replynone').on('click',function(){
					getReply(this);
				});
				//总记录
				$('.comment-contents-number').find('span').text();
				$('.comment-contents-number').find('span').text(webDiscusss.list.length);
				if(webDiscusss.total <= rows){
					$('.more-more').remove();
				}
			}
		});
	});
});