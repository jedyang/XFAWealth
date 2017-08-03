/**
 * newsContent.js
 * @author mqzou
 * @date: 2017-03-08
 */
define(function(require) {
	var $ = require('jquery');
	 require('layer');
	 var _emotion=require("emotion");
	 var _comments=require("comments");
	
	var infoId=getQueryString("id");
	
	$(document).on("click",'.comment-reply-word',function(){
		
		var _this=this;
		var comment=$(this).parent().parent().find(".member-comment-text").val();
		if(comment==undefined || comment==""){
			layer.msg(langMutilForJs['topic.detail.comment.tips']);
			return;
		}
		var id=$(this).parents(".content-rows").attr("id");
		$.ajax({
			type:'post',
			datatype:'json',
			url:base_root+"/front/news/info/addNewsComment.do",
			data:{infoId:infoId,comment:comment,id:id},
			success:function(json){
				
				if(json.result){
					//layer.msg("评论成功");
					
					////console.log(json.info);
					//var n=json.info;
					if(id!=undefined && id!=''){
						$(_this).parents(".comment-topper-contents").toggleClass("comment-list-rows3");
						AddReply(json.info,_this)
					}else{
						addComment(json.info,_this);
					}
					textlimit();
					$(_this).parents(".comment-topper-contents").find(".member-comment-text").val("");
					$(_this).parents("comment-topper-contents").find(".member-number-words").text("0/500")
					_comments.refreshReply();
				}else{
					//layer.msg("评论失败");
				}
			}
		});
	});
	
	function AddReply(news,obj){
	//	//console.log(news);
		//var parentId= $(obj).parents(".content-rows").attr("id");
		var html='<div class="comment-list-rows1 content-rows" id="'+news.id+'" parentId="'+news.replyId+'">'
        +'<div class="comment-list-textbox">'
        +'<img class="comment-list-portrait" src="'+base_root+news.memberUrl+'">'
        +'<div class="comment-list-topper">'
        +' <div style="width:100%;height:18px;"> '
        +'<p class="comment-list-topper-name" style="float: left;">'+news.memberName+'<span class="comment-list-topper-time">'+langMutilForJs['comment.reply']+'</span><span class="comment-list-topper-time"></span></p>'
        +'<p class="comment-list-topper-name" style="float: left;">'+news.replyMemberName+'<span class="comment-list-topper-time">'+news.opTime+'</span></p> '
        +' </div> '
        +'<div class="comment-list-topper-comment">'
        + _emotion.parseEmotion(news.comment)
        +'</div>'
        +'<div class="wmes-community-news-qa-flex">'+langMutilForJs['community.space.showAllContent']+'</div>'
        +'<div class="comment-list-topper-nice"><div class="comment-topper-nice""></div><span>'+news.good+'</span></div>'
        +'<div class="comment-list-topper-cai"><div class="comment-topper-cai""></div><span>'+news.bad+'</span></div>'
        +'<div class="comment-list-topper-reply"><img src="'+base_root+'/res/images/discover/wechat_ico.png" /><span>0</span></div>'
       
		//html+='</div></div></div><div class="click-more">查看全部回复</div>'
		$(obj).parents(".content-rows").after($(html));
	}
	
	function addComment(news,obj){
		var html ='<li>'
	         +'<div class="comment-list-rows content-rows" id="'+news.id+'">'
	         +'<div class="comment-list-textbox">'
	         +'<img class="comment-list-portrait" src="'+base_root+news.memberUrl+'">'
	         +'<div class="comment-list-topper">'
	         +'<p class="comment-list-topper-name">'+news.memberName+'<span class="comment-list-topper-time">'+news.opTime+'</span></p>'
	         +'<div class="comment-list-topper-comment">'
	         + _emotion.parseEmotion(news.comment)
	         +'</div>'
	         +'<div class="wmes-community-news-qa-flex">'+langMutilForJs['community.space.showAllContent']+'</div>'
	         +'<div class="comment-list-topper-nice"><div class="comment-topper-nice""></div><span>'+news.good+'</span></div>'
	         +'<div class="comment-list-topper-cai"><div class="comment-topper-cai""></div><span>'+news.bad+'</span></div>'
	         +'<div class="comment-list-topper-reply"><img src="'+base_root+'/res/images/discover/wechat_ico.png" /><span>0</span></div>'
	        
		html +='</div></div></div>';
		html+='</li>'
			$(".member-comment-list").prepend(html);
	}
	
	var page=1;
	var rows=10;
	getComment();
	function getComment(){
	    $.ajax({
	    	type:'post',
	    	datatype:'json',
	    	url:base_root+"/front/news/info/newsCommentJson.do",
	    	data:{page:page,rows:rows,infoId:infoId},
	    	success:function(json){
	    		var html="";
	    		////console.log(json.list);
	    		$.each(json.list,function(i,n){
	    			
	    			var cssType="";
	    			if(n.isLike=="1"){
	    				cssType="comment-topper-niceed";
	    			}
	    			var uncssType="";
	    			if(n.isUnlike=="1"){
	    				uncssType="comment-topper-caied";
	    			}
	    			
	    			var replyCount=n.replyCount;
	    			html+='<li>'
				         +'<div class="comment-list-rows content-rows" id="'+n.id+'">'
				         +'<div class="comment-list-textbox">'
				         +'<img class="comment-list-portrait" src="'+base_root+n.memberUrl+'">'
				         +'<div class="comment-list-topper">'
				         +'<p class="comment-list-topper-name">'+n.memberName+'<span class="comment-list-topper-time">'+n.opTime+'</span></p>'
				         +'<div class="comment-list-topper-comment">'
				         + _emotion.parseEmotion(n.comment)
				         +'</div>'
				         +'<div class="wmes-community-news-qa-flex">'+langMutilForJs['community.space.showAllContent']+'</div>'
				         +'<div class="comment-list-topper-nice"><div class="comment-topper-nice '+cssType+'"></div><span>'+n.good+'</span></div>'
				         +'<div class="comment-list-topper-cai"><div class="comment-topper-cai '+uncssType+'"></div><span>'+n.bad+'</span></div>'
				         +'<div class="comment-list-topper-reply"><img src="'+base_root+'/res/images/discover/wechat_ico.png" /><span>'+replyCount+'</span></div>'
				        if(replyCount>0){
				        	html+='<div class="comment-list-topper-replynone"><img src="'+base_root+'/res/images/ifa/replynone.png" /><span>'+langMutilForJs['comment.retractReply']+'</span></div>'
				        }
	    			html +='</div></div></div>';
				       if(replyCount>0){
				    	   html+=appendChild(n,"",true);
				       }  
	    			html+='</li>'
	    		});
	    		$(".member-comment-list").append(html);
	    		textlimit();
	    		if(page<json.total/rows){
	    			$(".more-more").show();
	    		}else{
	    			$(".more-more").hide();
	    		}
	    	}
	    		
	    }) 	
	}
	
	$(".more-more").on("click",function(){
		page++;
		getComment();
	})
	
	function appendChild(news,html,isFirst){
		
		$.each(news.replylist,function(i,n){
			////console.log("n.isLike",n.isLike);
			var cssType="";
			if(n.isLike=="1"){
				cssType="comment-topper-niceed";
			}
			var cssDisplay="";
			if(!isFirst){
				cssDisplay='commentactive';
			}
			var uncssType="";
			if(n.isUnlike=="1"){
				uncssType="comment-topper-caied";
			}
			
			var replyCount=n.replyCount;
			
			html+='<div class="comment-list-rows1 content-rows '+cssDisplay+'" id="'+n.id+'" parentId="'+news.id+'">'
	         +'<div class="comment-list-textbox">'
	         +'<img class="comment-list-portrait" src="'+base_root+n.memberUrl+'">'
	         +'<div class="comment-list-topper">'
	         +' <div style="width:100%;height:18px;"> '
	         +'<p class="comment-list-topper-name" style="float: left;">'+n.memberName+'<span class="comment-list-topper-time">'+langMutilForJs['comment.reply']+'</span><span class="comment-list-topper-time"></span></p>'
	         +'<p class="comment-list-topper-name" style="float: left;">'+n.replyMemberName+'<span class="comment-list-topper-time">'+n.opTime+'</span></p> '
	         +' </div> '
	        // +'<p class="comment-list-topper-name">'+n.memberName+'<span class="comment-list-topper-time">'+n.opTime+'</span></p>'
	         +'<div class="comment-list-topper-comment">'
	         + _emotion.parseEmotion(n.comment)
	         +'</div>'
	         +'<div class="wmes-community-news-qa-flex">'+langMutilForJs['community.space.showAllContent']+'</div>'
	         +'<div class="comment-list-topper-nice"><div class="comment-topper-nice '+cssType+'"></div><span>'+n.good+'</span></div>'
	         +'<div class="comment-list-topper-cai"><div class="comment-topper-cai '+uncssType+'"></div><span>'+n.bad+'</span></div>'
	         +'<div class="comment-list-topper-reply"><img src="'+base_root+'/res/images/discover/wechat_ico.png" /><span>'+n.replyCount+'</span></div>'
	         /*if(replyCount>0){
		        	html+='<div class="comment-list-topper-replynone"><img src="'+base_root+'/res/images/ifa/replynone.png" /><span>'+langMutilForJs['comment.retractReply']+'</span></div>'
		        }*/
	         html+='</div></div></div>';
	         if(isFirst && (news.replylist.length>1 || replyCount>0)){
	        	 html+='<div class="click-more showAll">'+langMutilForJs['comment.showAll']+'</div>'
	         }
			if(replyCount>0){
				html+=appendChild(n,"");
			}
			isFirst=false;
		})
		return html;
	}	
	
	$(document).on('click','.showAll',function(){
		$(this).parent().find(".commentactive").toggleClass("commentactive");
		$(this).hide();
	})
	

	$(".14pxclick").on("click", function() {
		$(".last-article").css("font-size", "14px");
	});
	$(".16pxclick").on("click", function() {
		$(".last-article").css("font-size", "16px");
	});
	$(".18pxclick").on("click", function() {
		$(".last-article").css("font-size", "18px");
	});

	$(".step-on-ico").on("click", function() {
		
		$(this).toggleClass("step-on-ico-active");
		if($(this).hasClass("step-on-ico-active")){
			upOrDown("1");
		}else{
			cancelNewsUpOrDown(infoId);
		}
		
		if($(".step-down-ico").hasClass("step-down-ico-active")){
			$(".step-down-ico").toggleClass("step-down-ico-active");
		}
	})
	
	$(".step-down-ico").on("click", function() {
		
		$(this).toggleClass("step-down-ico-active");
		if($(this).hasClass("step-down-ico-active")){
			upOrDown("0");
		}else{
			cancelNewsUpOrDown(infoId);
		}
		if($(".step-on-ico").hasClass("step-on-ico-active")){
			$(".step-on-ico").toggleClass("step-on-ico-active");
		}
		
	})
	
	function cancelNewsUpOrDown(id){
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/news/info/cancelNewsUpOrDown.do",
			data:{infoId:id},
			success:function(json){
				/*if(json.result){
					layer.msg("cancelNewsUpOrDown  success");
				}else{
					layer.msg("cancelNewsUpOrDown  faile");
				}*/
			}
		})
	}
	
	//点赞或踩的操作
	function upOrDown(type,commentId){
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/news/info/newsUpOrDown.do",
			data:{infoId:infoId,type:type,commentId:commentId},
			success:function(json){
				/*if(json.result){
					layer.msg("type:"+type+"  success");
				}else{
					layer.msg("type:"+type+"  faile");
				}*/
			}
		})
	}
	
	_comments.upClickFun=function(obj,status){
		var id=$(obj).parents(".content-rows").attr("id");
		if(status=="1"){//如果是点赞
			//点赞或踩的操作
			upOrDown("1",id);
			var cai=$(obj).parents(".content-rows").find(".comment-topper-cai");
	    	if(cai.hasClass("comment-topper-caied")){
	    		cai.toggleClass("comment-topper-caied");
	    		cai.next().text(Number(cai.next().text())-1)
	    	}
		}else{
			cancelNewsUpOrDown(id);
		}
		////console.log("id",id);
		////console.log("ele",obj);
	}
	
	_comments.downClickFun=function(obj,status){
		var id=$(obj).parents(".content-rows").attr("id");
		if(status=="1"){//如果是踩
			//点赞或踩的操作
			upOrDown("0",id);
			var nice=$(obj).parents(".content-rows").find(".comment-topper-nice");
	    	if(nice.hasClass("comment-topper-niceed")){
	    		nice.toggleClass("comment-topper-niceed");
	    		nice.next().text(Number(nice.next().text())-1)
	    	}
		}else{
			cancelNewsUpOrDown(id);
		}
		////console.log("id",id);
		////console.log("ele",obj);
	}
	
	$(".news-collection").on("click",function(){
		$(this).toggleClass("news-collectionac");
		var status="0";
		if($(this).hasClass("news-collectionac")){
			status="1";
		}
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/news/info/saveNewsFavorite.do",
			data:{status:status,infoId:infoId},
			success:function(json){
				if(json.result){
					if(status=="1"){
						layer.msg(langMutilForJs['favour.add']);
					}else{
						layer.msg(langMutilForJs['favour.remove']);
					}
					////console.log("collection success");
				}else{
					////console.log("collection faile");
				}
			}
		})
	})
	
	$(".post-recommend,.character-button-close,.character-close-ico").on("click",function(){
		
		$("#recommendNews").toggleClass("ifa-space-popup");
	})
	
	/*$(".character-close-ico").on('click',function(){
		$("#recommendNews").toggleClass("ifa-space-popup");
	})*/

	$(".character-button-save").on('click',function(){
		//var sectionType=$("#sectionType").val();
		var title=$("#newsTitle").text();
		var content=$("#recommendText").val();
		var sourceType="news";
		var visitor=$(".permit[checked='checked']").val();
		var sectionId=$(".sectionId").val();
		if(content==undefined || content==""){
			layer.msg(langMutilForJs['news.recommend.enterTips']);
			return ;
		}
		$.ajax({
			type:'post',
			datatype:'json',
			url:base_root+"/front/community/info/saveTopicFromShare.do",
			data:{title:title,content:content,sourceType:sourceType,sourceId:infoId,sectionId:sectionId,visitor:visitor},
			success:function(json){
				if(json.result){
					layer.msg(langMutilForJs['news.recommend.success'])
					$("#recommendNews").toggleClass("ifa-space-popup");
				}else{
					layer.msg(langMutilForJs['news.recommend.fail'])
				}
			}
		})
		
		
	});
	
	$("#searchKeyWord").keydown(function(e) {
		if (e.keyCode == 13) {
			search()
		}
	});
	$("#searchKeyBtn").on("click", function() {
		search()
	})
	function search() {

		var key = $("#searchKeyWord").val();
		window.location.href = base_root + "/front/news/info/searchNews.do?key=" + key;
	}
	
	var rightheight = $(window).height();
	var contentheight;
	setTimeout(function(){
		contentheight = $('.wmes-content').height();
	},200);
	
	$(document).on('scroll',function(){
		var tempnum = parseInt($('.popular-news').height()) + parseInt($('.header-bottom').height()) + parseInt($('.header-bottom').css('padding-top')) + parseInt($('.header-bottom').css('padding-bottom')) + parseInt(20);
		tempnum = parseInt(tempnum + rightheight);
		if($(window).scrollTop() >= tempnum){
			if($(window).scrollTop() >= parseInt(contentheight - rightheight - 40)){
				
			}else{
				$('.new-content-right').css('margin-top',$(window).scrollTop() - parseInt(tempnum));
			};
		}else{
			$('.new-content-right').css('margin-top','0');
		};
	});
	
	function textlimit(){
		for(i=0;i<$('.comment-list-topper-comment').length;i++){
			if($('.comment-list-topper-comment').eq(i).height() > 47){
				$('.comment-list-topper-comment').eq(i).addClass('wmes-community-news-qaac');
				$('.comment-list-topper-comment').eq(i).next().css('display','block');
			};
		};
	}
	
	
	$(document).on('click','.wmes-community-news-qa-flex',function(){
		$(this).prev().toggleClass('wmes-community-news-qaac');
		if($(this).prev().hasClass('wmes-community-news-qaac')){
			$(this).text(langMutilForJs['community.space.showAllContent'])
		}else{
			$(this).text(langMutilForJs['comment.retract'])
		}
	});
	
	 $(document).on("click",".register_xiala_long input",function(){
			$(this).siblings(".regiter_xiala_ul").show();
		});
		$(document).on('click','.register_xiala_ico',function(){
			if($(this).parents('.register_xiala_long').find('ul').css('display') == 'none'){
				$(this).siblings(".regiter_xiala_ul").show();
			}else{
				$(this).siblings(".regiter_xiala_ul").hide();
			};
		});
		$(document).on('mouseleave','.register_xiala_long',function(){
			$(this).find('.regiter_xiala_ul').hide();
		});
		
	    $(".regiter_xiala_ul").on("click","li",function(){
			$(this).parent().siblings('.value_hidden').val( $(this).attr("id") );
			$(this).parent().siblings('.value_show').val( $(this).text() );
			$(".regiter_xiala_ul").hide();
		});
	
})