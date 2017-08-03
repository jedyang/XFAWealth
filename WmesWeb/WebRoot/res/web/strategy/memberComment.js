define(function(require) {
	var $ = require('jquery');
	var _emotion=require("emotion");
	
	var comment={};
	
	comment.upClickFun=function(){
		
	}
	comment.downClickFun=function(){
		
	}
	
	var _replyTarget;
	
	
	$(document).on('click','.comment-list-topper-replynone',function(){
		/*$(this).closest('li').find('.comment-list-rows1').toggleClass('commentactive');
		$(this).closest('li').find('.comment-list-rowstop3').toggleClass('commentactive1');*/
		if($(this).find('span').text() == langMutilForJs['comment.retractReply']){
			$(this).find('span').text(langMutilForJs['comment.spreadReply']);
			$(this).closest('li').find('.comment-list-rows1').addClass('commentactive');
			$(this).closest('li').find('.comment-list-rowstop3').addClass('commentactive1');
		}else{
			$(this).find('span').text(langMutilForJs['comment.retractReply']);
			$(this).closest('li').find('.comment-list-rows1').removeClass('commentactive');
			$(this).closest('li').find('.comment-list-rowstop3').removeClass('commentactive1');
		}
		$(this).closest('li').find(".showAll").hide();
	});
	
	$(document).on('click','.comment-list-topper-nice',function(){
		var status=1;
		if($(this).find('.comment-topper-nice').hasClass('comment-topper-niceed')){
			var temp = $(this).find('span').text();
			temp = parseInt(temp)-1;
			$(this).find('span').text(temp);
			$(this).find('.comment-topper-nice').removeClass('comment-topper-niceed');
			status=0;
		}else{
			var temp = $(this).find('span').text();
			temp = parseInt(temp)+1;
			$(this).find('span').text(temp);
			$(this).find('.comment-topper-nice').addClass('comment-topper-niceed');
		}
		comment.upClickFun(this,status);
	});
	
	$(document).on('click','.comment-list-topper-cai',function(){
		var status=1;
		if($(this).find('.comment-topper-cai').hasClass('comment-topper-caied')){
			var temp = $(this).find('span').text();
			temp = parseInt(temp)-1;
			$(this).find('span').text(temp);
			$(this).find('.comment-topper-cai').removeClass('comment-topper-caied');
			status=0;
		}else{
			var temp = $(this).find('span').text();
			temp = parseInt(temp)+1;
			$(this).find('span').text(temp);
			$(this).find('.comment-topper-cai').addClass('comment-topper-caied');
		}
		comment.downClickFun(this,status);
	});
	
	$(document).on('click','.comment-list-topper-reply',function(){
		//$(this).parents('li').find('.comment-list-rows2').toggleClass('commentactive');
		
		var replyRows=$(this).parents(".content-rows").find(".comment-topper-contents")
		if(replyRows.length==0){
			var html='<div class="comment-topper-contents">'
				    +'<div class="member-comment-topper">'
				    +'<p class="member-comment-title">'+langMutilForJs['comment.title']+'</p>'
				    +'<p class="member-number-words">0/500</p>'
				    +'</div>'
				    +'<textarea class="member-comment-text"></textarea>'
				    +'<div class="member-comment-reply">'
				    +'<img class="comment-expression-ico" src="'+base_root+'/res/images/discover/expression_ico.png">'
				    +'<a class="comment-reply-word" href="javascript:;">'+langMutilForJs['comment.replybtn']+'</a>'
				    +'</div>'
				    +'<div class="comment-expression"></div>'
				    +'</div>';
			$(this).parents(".content-rows").append(html);
				    
		}else{
			$(replyRows).toggleClass("comment-list-rows3");
		}
		_replyTarget=this;
		
	})
	
	comment.refreshReply=function(){
		if(_replyTarget!=undefined){
			
			var count= $(_replyTarget).find("span").text();
			$(_replyTarget).find("span").text(Number(count)+1);
			
			var parentId=$(_replyTarget).parents(".content-rows").attr("parentId");
			if(parentId!=undefined && parentId!=""){
				addReplyCount(parentId);
			}
		}
	} 
	
	function addReplyCount(parentId){
		if(parentId!=undefined && parentId!=""){
			
		   var ele=	$("#"+parentId);
		   //console.log("ele",ele);
			//console.log("parentId",parentId);
		   if(ele!=undefined){
			   var count= ele.find(".comment-list-topper-reply").find("span").text();
			   ele.find(".comment-list-topper-reply").find("span").text(Number(count)+1);
			   parentId=ele.attr("parentId");
			   if(parentId!=undefined && parentId!=""){
				   addReplyCount(parentId);
				   
			   }
		   }
			
		}
	}
	
	$('.comment-list-topper-replytop').on('click',function(){
		/*$(this).parents('li').find('.comment-list-rowstop').toggleClass('commentactive1');
		$(this).parents('li').find('.comment-list-rowstop3').toggleClass('commentactive1');*/
		var replyRows=$(this).parents(".content-rows").find(".comment-topper-contents")
		if(replyRows.length==0){
			var html='<div class="comment-topper-contents">'
				    +'<div class="member-comment-topper">'
				    +'<p class="member-comment-title">'+langMutilForJs['comment.title']+'</p>'
				    +'<p class="member-number-words">0/500</p>'
				    +'</div>'
				    +'<textarea class="member-comment-text"></textarea>'
				    +'<div class="member-comment-reply">'
				    +'<img class="comment-expression-ico" src="'+base_root+'/res/images/discover/expression_ico.png">'
				    +'<a class="comment-reply-word" href="javascript:;">'+langMutilForJs['comment.replybtn']+'</a>'
				    +'</div>'
				    +'</div>';
			$(this).parents(".content-rows").append(html);
				    
		}else{
			$(replyRows).toggleClass("comment-list-rows3");
		}
		////console.log(replyRows);
	});
	$('#comment-reply-word').on('click',function(){
		$(this).parents('li').find('.comment-list-rows3').toggleClass('commentactive1');
		var content = $(this).parents('.comment-topper-contents').find('.member-comment-text').val();
		$(this).parents('li').find('.comment-list-rows3').find('.comment-list-topper-comment').text(content);
		$(this).parents('.comment-list-rows2').css('display','none');
	});
	
	$(document).on("input",".member-comment-text",function(){
		var maxLength=500;
		var length=$(this).val().length;
		if(length>maxLength){
			$(this).val($(this).val().substring(0,maxLength));
			$(this).parent().find(".member-number-words").text(maxLength+"/"+maxLength)
		}else{
			$(this).parent().find(".member-number-words").text(length+"/"+maxLength)
		}
	});
	
	var _curCommentBox;
	
	$(document).on('click','.comment-expression-ico',function(){
		
		var ele=$(this).closest('.comment-topper-contents').find('.comment-expression');
		ele.toggleClass('comment-expressionac');
		if(ele.hasClass('comment-expressionac') ){
			if(ele.find("#sinaEmotion").length==0){
				_emotion.init(ele);
			}
			
		}else{
			
			//ele.parents(".comment-topper-contents").find("#sinaEmotion").remove();
			_emotion.destroy();
		}
		_curCommentBox=ele;
		
	});
	
	_emotion.selectCallBack=function(alt){
		//console.log("alt",alt);
		$(_curCommentBox).parents(".comment-topper-contents").find(".comment-expression-ico").click();
		var textArea=$(_curCommentBox).parents(".comment-topper-contents").find(".member-comment-text");
		var text=textArea.val();
		if(text==undefined)
			text="";
		textArea.val(text+alt)
		
	}
	return comment;
})