/**
 * topicdetail.js 帖子详情
 * @author mqzou 2017-03-15
 */
define(function(require) {
	
	var $ = require('jquery');
	require('layui');
	 var _emotion=require("emotion");
	 var _comments=require("comments");
	 
	var id=getQueryString("id");
	var type=getQueryString("type");
	
	var title=$(".wmes-community-news-title").text();
	$(".wmes-community-news-title").html(emotionFilter(title))
	
	
	var page=1;
	var rows=10;
	getComment();
	function getComment(){
		$(".no-comment-tips").hide();
	    $.ajax({
	    	type:'post',
	    	datatype:'json',
	    	url:base_root+"/front/community/info/getCommentList.do",
	    	data:{page:page,rows:rows,id:id},
	    	success:function(json){
	    		if(page==1 && (json.list==undefined || json.list.length==0)){
	    			$(".no-comment-tips").show();
	    		}
	    		//console.log(json.list);
	    		var html=getCommentHtml(json.list);
	    		$(".member-comment-list").append(html);
	    		
	    		if(page<json.total/rows){
	    			$(".more-more").show();
	    		}else{
	    			$(".more-more").hide();
	    		};
	    		textlimit();
	    	}
	    		
	    }) 	
	}
	
	$(".more-more").on("click",function(){
		page++;
		getComment();
	})
	
	function getCommentHtml(list){
		var html="";
		$.each(list,function(i,n){
			
			var cssType="";
			var unlikeCss="";
			if(n.isLike==1){
				cssType="comment-topper-niceed";
			}else if(n.isUnlike==1){
				unlikeCss="comment-topper-caied";
			}
			var status=n.status;
			//var replyCount=null!=n.replylist?n.replylist.length:0;
			if(status==-1 || status=="-1"){
				html+='<li>'
			         +'<div class="comment-list-rows content-rows" id="'+n.id+'">'
			         +'<div class="comment-list-textbox">'
			         +'<img class="comment-list-portrait  headPortrait wmes-community-news-headPortrait "  membertype="'+n.memberType+'" memberid="'+n.memberId+'"  src="'+base_root+n.memberUrl+'">'
			         +'<div class="comment-list-topper">'
			         +'<p class="comment-list-topper-name">'+n.memberName+'<span class="comment-list-topper-time">'+n.dateTime+'</span></p>'
			         +'<div class="comment-list-topper-comment">'
			         + '<div style="background:#f2f2f2;color:#808080;padding:5px;display:inline-block;">'+langMutilForJs['community.reply.lock']+'</div>'
			         +'</div>'
				html+='</li>'
			}else{
				html+='<li>'
			         +'<div class="comment-list-rows content-rows" id="'+n.id+'">'
			         +'<div class="comment-list-textbox">'
			         +'<img class="comment-list-portrait  headPortrait wmes-community-news-headPortrait "  membertype="'+n.memberType+'" memberid="'+n.memberId+'"  src="'+base_root+n.memberUrl+'">'
			         +'<div class="comment-list-topper">'
			         +'<p class="comment-list-topper-name">'+n.memberName+'<span class="comment-list-topper-time">'+n.dateTime+'</span></p>'
			         +'<div class="comment-list-topper-comment">'
			         + _emotion.parseEmotion(n.content)
			         +'</div>'
			         +'<div class="wmes-community-news-qa-flex">'+langMutilForJs['community.space.showAllContent']+'</div>'
			         +'<div class="comment-list-topper-nice"><div class="comment-topper-nice '+cssType+'"></div><span>'+n.likeCount+'</span></div>'
			         +'<div class="comment-list-topper-cai"><div class="comment-topper-cai '+unlikeCss+'"></div><span>'+n.unlikeCount+'</span></div>'
			         +'<div class="comment-list-topper-reply"><img src="'+base_root+'/res/images/discover/wechat_ico.png" /><span>'+n.commentCount+'</span></div>'
			         if(n.childList!=undefined && n.childList.length>0){
			        	html+='<div class="comment-list-topper-replynone"><img src="'+base_root+'/res/images/ifa/replynone.png" /><span>'+langMutilForJs['comment.retractReply']+'</span></div>'
			        }
				html +='</div></div></div>';
				if(n.childList!=undefined && n.childList.length>0){
			    	   html+=appendChild(n,"",true);
			      };
				html+='</li>'
			}
			
		});
		textlimit();
		return html;
	}
	
	function appendChild(comment,html,isFirst){
		
		$.each(comment.childList,function(i,n){
			////console.log("n.isLike",n.isLike);

			var cssType="";
			var unlikeCss="";
			if(n.isLike==1){
				cssType="comment-topper-niceed";
			}else if(n.isUnlike==1){
				unlikeCss="comment-topper-caied";
			}
			var cssDisplay="";
			if(!isFirst){
				cssDisplay='commentactive';
			}
			
			//var replyCount=null!=n.replylist?n.replylist.length:0;
			
			html+='<div class="comment-list-rows1 content-rows '+cssDisplay+'" id="'+n.id+'" parentId="'+comment.id+'">'
	         +'<div class="comment-list-textbox">'
	         +'<img class="comment-list-portrait headPortrait wmes-community-news-headPortrait "  membertype="'+n.memberType+'" memberid="'+n.memberId+'" src="'+base_root+n.memberUrl+'">'
	         +'<div class="comment-list-topper">'
	         +' <div style="width:100%;height:18px;"> '
	         +'<p class="comment-list-topper-name" style="float: left;">'+n.memberName+'<span class="comment-list-topper-time">'+langMutilForJs['comment.reply']+'</span><span class="comment-list-topper-time"></span></p>'
	         +'<p class="comment-list-topper-name" style="float: left;">'+n.replyMemberName+'<span class="comment-list-topper-time">'+n.dateTime+'</span></p> '
	         +' </div> '
	        // +'<p class="comment-list-topper-name">'+n.memberName+'<span class="comment-list-topper-time">'+n.dateTime+'</span></p>'
	         +'<div class="comment-list-topper-comment">'
	         + _emotion.parseEmotion(n.content)
	         +'</div>'
	         +'<div class="wmes-community-news-qa-flex">'+langMutilForJs['community.space.showAllContent']+'</div>'
	         +'<div class="comment-list-topper-nice"><div class="comment-topper-nice '+cssType+'"></div><span>'+n.likeCount+'</span></div>'
	         +'<div class="comment-list-topper-cai"><div class="comment-topper-cai '+unlikeCss+'"></div><span>'+n.unlikeCount+'</span></div>'
	         +'<div class="comment-list-topper-reply"><img src="'+base_root+'/res/images/discover/wechat_ico.png" /><span>'+n.commentCount+'</span></div>'
	         /*if(replyCount>0){
		        	html+='<div class="comment-liscomment-reply-wordt-topper-replynone"><img src="'+base_root+'/res/images/ifa/replynone.png" /><span>'+langMutilForJs['comment.retractReply']+'</span></div>'
		        }*/
			html+='</div></div></div>'
			 if(isFirst && (comment.childList.length>1 || n.childList.length>0)){
	        	 html+='<div class="click-more showAll">'+langMutilForJs['comment.showAll']+'</div>'
	         }
			if(n.childList!=undefined && n.childList.length>0){
				html+=appendChild(n,"");
			}
			isFirst=false;
			textlimit();
		})
		return html;
	}	
	
	
	$(document).on('click','.showAll',function(){
		$(this).parent().find(".commentactive").toggleClass("commentactive");
		$(this).hide();
		textlimit();
	})
	
	getAnswerList();
	function getAnswerList(){
		$.ajax({
	    	type:'post',
	    	datatype:'json',
	    	url:base_root+"/front/community/info/getAnswerList.do",
	    	data:{page:page,rows:rows,id:id},
	    	success:function(json){
	    		var html=getCommentHtml(json);
	    		$(".answer-comment-list").append(html);
	    		////console.log(json);
	    	}
		});
	}
	
	
	 
    //点赞
    $(".wmes-community-news-message-right-nice").on("click",function(){

    	$(this).toggleClass("comment-topper-niceed");
    	var isCancel="";
    	if($(this).hasClass("comment-topper-niceed")){
    		$(this).next().text(Number($(this).next().text())+1);
    	
    	}else{
    		$(this).next().text(Number($(this).next().text())-1);
    		isCancel="1";
    	}
    	
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+'/front/community/info/saveBehavior.do',
    		data:{id:id,type:type,isCancel:isCancel,behavior:'like'},
    		success:function(json){
    			//console.log(json);
    		}
    		
    	});
    	if($(".wmes-community-news-message-right-cai").hasClass("comment-topper-caied")){
    		$(".wmes-community-news-message-right-cai").toggleClass("comment-topper-caied")
    		$(".wmes-community-news-message-right-cai").next().text(Number($(".wmes-community-news-message-right-cai").next().text())-1)
    	}
    })
    
    _comments.upClickFun=function(obj,status){
    	var id=$(obj).parents(".content-rows").attr("id");
    	var isCancel="";
    	if(!status==1){
    		isCancel="1";
    	
    	}
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+'/front/community/info/saveBehavior.do',
    		data:{id:id,type:"c",isCancel:isCancel,behavior:'like'},
    		success:function(json){
    			//console.log(json);
    		}
    		
    	})
    	var cai=$(obj).parents(".content-rows").find(".comment-topper-cai");
    	if(cai.hasClass("comment-topper-caied")){
    		cai.toggleClass("comment-topper-caied");
    		cai.next().text(Number(cai.next().text())-1)
    	}
    }
   
    
    //踩
    $(".wmes-community-news-message-right-cai").on("click",function(){
    	$(this).toggleClass("comment-topper-caied");
    	
    	var isCancel="";
    	if($(this).hasClass("comment-topper-caied")){
    		$(this).next().text(Number($(this).next().text())+1);
    	}else{
    		$(this).next().text(Number($(this).next().text())-1);
    		isCancel="1";
    	}
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+'/front/community/info/saveBehavior.do',
    		data:{id:id,type:type,isCancel:isCancel,behavior:'unlike'},
    		success:function(json){
    			//console.log(json);
    		}
    		
    	})
    	if($(".wmes-community-news-message-right-nice").hasClass("comment-topper-niceed")){
    		$(".wmes-community-news-message-right-nice").toggleClass("comment-topper-niceed")
    		$(".wmes-community-news-message-right-nice").next().text(Number($(".wmes-community-news-message-right-nice").next().text())-1)
    	}
    });
    
    _comments.downClickFun=function(obj,status){

    	var id=$(obj).parents(".content-rows").attr("id");
    	var isCancel="";
    	if(!status==1){
    		isCancel="1";
    	
    	}
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+'/front/community/info/saveBehavior.do',
    		data:{id:id,type:"c",isCancel:isCancel,behavior:'unlike'},
    		success:function(json){
    			////console.log(json);
    		}
    		
    	})
    	var nice=$(obj).parents(".content-rows").find(".comment-topper-nice");
    	if(nice.hasClass("comment-topper-niceed")){
    		nice.toggleClass("comment-topper-niceed");
    		nice.next().text(Number(nice.next().text())-1)
    	}
    }
    
    //收藏
    $(".wmes-community-topic-collect").on("click",function(){
    	$(this).toggleClass("news-collectionac");
    	var isCancel="";
    	if(!$(this).hasClass("news-collectionac")){
    		isCancel="1";
    	}
    	$.ajax({
    		type:"post",
    		datatype:"json",
    		url:base_root+'/front/community/info/saveBehavior.do',
    		data:{id:id,type:type,isCancel:isCancel,behavior:'favorite'},
    		success:function(json){
    			if(isCancel=="1"){
					layer.msg(langMutilForJs['favour.remove']);
				}else{
					layer.msg(langMutilForJs['favour.add']);
				}
    		}
    		
    	})
    })
    
    //评论 回复
    $(document).on('click','.comment-reply-word',function(){
    	var _this=this;
    	var target=$(this).attr("targetType");
    	var id=$(this).attr("targetId");
    	
    	if(id==undefined || id==""){
    		id=$(this).parents(".content-rows").attr("id");
    		target="c";
    	}
    	
    	var isAnswer='';
    	if(target=="a"){
    		isAnswer='1';
    		//target='q';
    	}
    	var content=$(this).parent().parent().find(".member-comment-text").val();
    	if(content==undefined || content==""){
    		layer.msg(langMutilForJs['topic.detail.comment.tips']);
    		return;
    	}
    	$.ajax({
    		type:'post',
    		datatype:'json',
    		url:base_root+'/front/community/info/saveComment.do',
    		data:{target:target,id:id,isAnswer:isAnswer,type:type,content:content},
    		success:function(json){
    			if(json.result){
    				var info=json.info;
    				if(info!=undefined){
    					if(info.replyId!=undefined && info.replyId!=''){
    						$(_this).parents(".comment-topper-contents").toggleClass("comment-list-rows3");
    						AddReply(json.info,_this)
    					}else{
    						if(target!='a'){//评论
    							addComment(json.info,".member-comment-list");
    						}else{//回答问题
    							addComment(json.info,".answer-comment-list");
    						}
    						$(".no-comment-tips").hide();
    						
    					}
    				}
					$(_this).parents(".comment-topper-contents").find(".member-comment-text").val("");
					$(_this).parents("comment-topper-contents").find(".member-number-words").text("0/500")
					_comments.refreshReply();
    			}
    			////console.log(json);
    		}
    		
    	})
    })
    
	function AddReply(comment,obj){
    	var cssType="";
		var unlikeCss="";
		if(obj.isLike==1){
			cssType="comment-topper-niceed";
		}else if(obj.isUnlike==1){
			unlikeCss="comment-topper-caied";
		}
		

		var html='<div class="comment-list-rows1 content-rows" id="'+comment.id+'"  parentId="'+comment.replyId+'">'
        +'<div class="comment-list-textbox">'
        +'<img class="comment-list-portrait  headPortrait wmes-community-news-headPortrait "  membertype="'+comment.memberType+'" memberid="'+comment.memberId+'" src="'+base_root+comment.memberUrl+'">'
        +'<div class="comment-list-topper">'
        +' <div style="width:100%;height:18px;"> '
        +'<p class="comment-list-topper-name" style="float: left;">'+comment.memberName+'<span class="comment-list-topper-time">'+langMutilForJs['comment.reply']+'</span><span class="comment-list-topper-time"></span></p>'
        +'<p class="comment-list-topper-name" style="float: left;">'+comment.replyMemberName+'<span class="comment-list-topper-time">'+comment.dateTime+'</span></p> '
        +' </div> '
        +'<div class="comment-list-topper-comment">'
        + _emotion.parseEmotion(comment.content)
        +'</div>'
        +'<div class="wmes-community-news-qa-flex">'+langMutilForJs['community.space.showAllContent']+'</div>'
        +'<div class="comment-list-topper-nice"><div class="comment-topper-nice '+cssType+'"></div><span>'+comment.likeCount+'</span></div>'
        +'<div class="comment-list-topper-cai"><div class="comment-topper-cai '+unlikeCss+'"></div><span>'+comment.unlikeCount+'</span></div>'
        +'<div class="comment-list-topper-reply"><img src="'+base_root+'/res/images/discover/wechat_ico.png" /><span>'+comment.commentCount+'</span></div>'
		html+='</div></div></div>'
		$(obj).parents(".content-rows").after($(html));
		textlimit();
	}
	
	function addComment(comment,obj){
		
		var cssType="";
		var unlikeCss="";
		if(obj.isLike==1){
			cssType="comment-topper-niceed";
		}else if(obj.isUnlike==1){
			unlikeCss="comment-topper-caied";
		}
		
    	var html='<li>'
	         +'<div class="comment-list-rows content-rows" id="'+comment.id+'">'
	         +'<div class="comment-list-textbox">'
	         +'<img class="comment-list-portrait  headPortrait wmes-community-news-headPortrait "  membertype="'+comment.memberType+'" memberid="'+comment.memberId+'" src="'+base_root+comment.memberUrl+'">'
	         +'<div class="comment-list-topper">'
	         +'<p class="comment-list-topper-name">'+comment.memberName+'<span class="comment-list-topper-time">'+comment.dateTime+'</span></p>'
	         +'<div class="comment-list-topper-comment">'
	         + _emotion.parseEmotion(comment.content)
	         +'</div>'
	         +'<div class="wmes-community-news-qa-flex">'+langMutilForJs['community.space.showAllContent']+'</div>'
	         +'<div class="comment-list-topper-nice"><div class="comment-topper-nice '+cssType+'"></div><span>'+comment.likeCount+'</span></div>'
	         +'<div class="comment-list-topper-cai"><div class="comment-topper-cai '+unlikeCss+'"></div><span>'+comment.unlikeCount+'</span></div>'
	         +'<div class="comment-list-topper-reply"><img src="'+base_root+'/res/images/discover/wechat_ico.png" /><span>'+comment.commentCount+'</span></div>'
	         +'</div></div></div>';
			textlimit();
			$(obj).prepend(html);
	}
    
    $(".answer-question,.wmes-community-topic-rely").on('click',function(){
    	$(".topic-gotoreply").hide();
    	if($(".answer-comment").find(".comment-topper-contents").length==0){
    		var html='<div class="comment-topper-contents">'
    		    +'<div class="member-comment-topper">'
    		    +'<p class="member-comment-title">'+langMutilForJs['comment.title']+'</p>'
    		    +'<p class="member-number-words">0/500</p>'
    		    +'</div>'
    		    +'<textarea class="member-comment-text"></textarea>'
    		    +'<div class="member-comment-reply">'
    		    +'<img class="comment-expression-ico" src="/wmes/res/images/discover/expression_ico.png">'
    		    +'<a class="comment-reply-word" targetType="a" targetId="'+id+'" href="javascript:;">'+langMutilForJs['comment.replybtn']+'</a>'
    		    +'</div>'
    		    +'<div class="comment-expression"></div>'
    		    +'</div>';
        	$(".answer-comment").append(html);
    	}
    	
    })
    
    $(".wmes-community-topic-zhuanfa,.character-close-ico,.character-button-close").on("click",function(){
		
		$("#recommendNews").toggleClass("ifa-space-popup");
	})
	
	$(".character-button-save").on('click',function(){
		var content=$(".recommend-text").val();
		var publishTo=$(".permit:checked").val();
		if(content!=undefined && content!=''){
			$.ajax({
				type:'post',
				datatype:'json',
				url:base_root+'/front/community/info/topicForward.do',
				data:{id:id,publishTo:publishTo,content:content},
				success:function(json){
					if(json.result){
						layer.msg(langMutilForJs['topic.transmit.success']);
						$("#recommendNews").toggleClass("ifa-space-popup");
					}
				}
			})
		}
		
	})
	
	//跳转页面
    $(document).on('click','.headPortrait',function(){
		var hidloginMemberId = $('#hidloginMemberId').val();//
		var hidloginMemberType = $('#hidloginMemberType').val();
		var memberid = $(this).attr('memberid');//被查看人的ID
		var membertype = $(this).attr('membertype');//被查看人的类型
		if(hidloginMemberType=='1'){ //如果登录的是投资人
			if(hidloginMemberId==memberid){//如果当前登录的人跟所要查看的人是一样的，则跳转去这是investor的space链接
				//window.location.href = base_root + '/front/investor/home.do';
				window.location.href = base_root + '/front/community/space/investorSpace.do?id='+memberid;
			} else{
				//如果查看的是投资人
				if(membertype=='1')
					window.location.href = base_root + '/front/community/space/investorSpace.do?id='+memberid;
				else //如果查看的是IFA
					window.location.href = base_root + '/front/community/space/ifaSpace.do?id='+memberid;
			}	
		} else{//当前登录的是非投资人
			//如果查看的是投资人
			if(membertype=='1')
				window.location.href = base_root + '/front/community/space/investorSpace.do?id='+memberid;
			else //如果查看的是IFA
				window.location.href = base_root + '/front/community/space/ifaSpace.do?id='+memberid;
		}
	});
	
	function textlimit(){
		for(var i=0;i<$('.comment-list-topper-comment').length;i++){
			if($('.comment-list-topper-comment').eq(i).height() > 47){
				$('.comment-list-topper-comment').eq(i).addClass('wmes-community-news-qaac');
				$('.comment-list-topper-comment').eq(i).next().css('display','block');
			};
		};
	};
	
	$(document).on('click','.wmes-community-news-qa-flex',function(){
		$(this).prev().toggleClass('wmes-community-news-qaac');
		if($(this).prev().hasClass('wmes-community-news-qaac')){
			$(this).text(langMutilForJs['community.space.showAllContent'])
		}else{
			$(this).text(langMutilForJs['comment.retract'])
		}
	});
	
	
	function emotionFilter(comtent){
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
			
			comtent = comtent
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

			return comtent;
	}
})