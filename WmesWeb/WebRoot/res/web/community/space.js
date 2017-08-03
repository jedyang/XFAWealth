/**
 * space.js 空间页面
 * @author 李裕恒
 * email: 673577011@qq.com
 * @date: 2016-03-16
 */

define(function(require) {
	var $ = require('jquery');
	require('layui');
	var angular = require('angular');
	var mybase = angular.module('ifaSpace', ['truncate']);
	var _emotion=require("emotion");
	
	var memberId=getQueryString("id");
	
	for(var i=0;i<$('.wmes-community-tab').length;i++){
		$('.wmes-community-tab').eq(i).next().css('display','block');
	};
	
	
	mybase.controller('ifaSpaceCtrl', ['$scope', '$http', function($scope, $http) {
		$scope.dynamicList='';
		$scope.topicList='';
		$scope.questionList='';
		$scope.favouriteList='';
		$scope.topicPage=1;
		$scope.questionPage=1;
		$scope.favouritePage=1;
		$scope.dynamicPage=1;
		$scope.dateTimeFmt=_dateFormt;
		$scope.base_root=base_root;
		var rows=10;
		getTopicList();
		function getTopicList(){
			var url = base_root + "/front/community/space/getTopicList.do",
			data = "rows=" + rows + "&page=" + $scope.topicPage +"&id="+memberId;
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) {
					if($scope.topicList!=''){
						$scope.topicList=$scope.topicList.concat(response.list);
					}else{
						$scope.topicList=response.list;
					}
					if(response.list.length==0 && $scope.topicPage==1){
						$('.topic-tips').show();
					}
					
					var totalPage=Math.ceil(response.total/rows);
					if($scope.topicPage<totalPage){
						$('.moretopic').show();
					}else{
						$('.moretopic').hide();
					}
					
				}).then(function(data){
					if(data.data.page==1){
						getQuestionList();
					}
				})
			}
		$('.moretopic').on('click',function(){
			 $scope.topicPage= $scope.topicPage+1;
			 getTopicList();
		})
		
		
		function getQuestionList(){
			var url = base_root + "/front/community/space/getIfaQuestionList.do",
			data = "rows=" + rows + "&page=" + $scope.questionPage+"&id="+memberId;
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) {
					/*$scope.questionList=response.list;
					//console.log("question",response);*/
					
					if($scope.questionList!=''){
						$scope.questionList=$scope.questionList.concat(response.list);
					}else{
						$scope.questionList=response.list;
					}
					if(response.list.length==0 && $scope.topicPage==1){
						$('.quetion-tips').show();
					}
					
					var totalPage=Math.ceil(response.total/rows);
					if($scope.topicPage<totalPage){
						$('.morequestion').show();
					}else{
						$('.morequestion').hide();
					}
					
				}).then(function(data){
					if(data.data.page==1){
					getFavorite();
					}
				})
		}
		$('.morequestion').on('click',function(){
			 $scope.questionPage= $scope.questionPage+1;
			 getQuestionList();
		})
		
		function getFavorite(){
			var url = base_root + "/front/community/space/getFavoriteList.do",
			data = "rows=" + rows + "&page=" + $scope.favouritePage+"&id="+memberId;
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) {
					/*$scope.favouriteList=response.list;
					//console.log(response);*/
					
					if(response.list.length==0 && $scope.favouritePage==1){
						$('.favourite-tips').show();
					}
					
					if($scope.favouriteList!=''){
						$scope.favouriteList=$scope.favouriteList.concat(response.list);
					}else{
						$scope.favouriteList=response.list;
					}
					
					
					var totalPage=Math.ceil(response.total/rows);
					if($scope.favouritePage<totalPage){
						$('.morefavourite').show();
					}else{
						$('.morefavourite').hide();
					}
					
					
				}).then(function(data){
					if(data.data.page==1 && _myself=='1'){
						getDynamic();
					}
				})
		}
		$('.morefavourite').on('click',function(){
			 $scope.favouritePage= $scope.favouritePage+1;
			 getFavorite();
		})
		
		function getDynamic(){
			var url = base_root + "/front/community/info/getMyDynamicTopicListJson.do",
			data = "rows=" + rows + "&page=" + $scope.dynamicPage;
			$http({
					url: url,
					method: 'POST',
					data: data,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					},
				})
				.success(function(response) {
					if($scope.dynamicList!=''){
						$scope.dynamicList=$scope.dynamicList.concat(response.rows);
					}else{
						$scope.dynamicList=response.rows;
					}
					if(response.list.length==0 && $scope.dynamicPage==1){
						$('.dynamic-tips').show();
					}
					
					
					var totalPage=Math.ceil(response.total/rows);
					if($scope.dynamicPage<totalPage){
						$('.moredynamic').show();
					}else{
						$('.moredynamic').hide();
					}
					//console.log(response);
				})
		}
		
		$('.moredynamic').on('click',function(){
			 $scope.dynamicPage= $scope.dynamicPage+1;
			 getDynamic();
		})
		
		
		
		
	}]);
	
	
	
	
	$('.wmes-community-tab li').on('click',function(){
		$(this).addClass('wmes-community-tabac').siblings().removeClass('wmes-community-tabac');
		var tempclass = $(this).closest('ul').next().attr('class');
		$(this).closest('div').find("."+tempclass).css('display','none');
		$(this).closest('div').find("."+tempclass).eq($(this).index()).css('display','block');
		for(i=0;i<$('.wmes-community-news-qa').length;i++){
			if($('.wmes-community-news-qa').eq(i).height() > 90){
				$('.wmes-community-news-qa').eq(i).addClass('wmes-community-news-qaac');
				$('.wmes-community-news-qa').eq(i).next().css('display','block');
			};
		}
	});
	
	$(document).on('click','.wmes-community-news-qa-flex',function(){
		$(this).prev().toggleClass('wmes-community-news-qaac');
	});
	
	$('.wmes-community-topic-collect').on('click',function(){
		$(this).toggleClass('wmes-community-topic-collected');
	});
	
	$(document).on('click','.wmes-space-screen-title li',function(){
		$(this).addClass('wmes-space-screen-titleliac').siblings().removeClass('wmes-space-screen-titleliac');
		$('.wmes-space-screen-content').addClass('wmes-space-screen-contentac');
	});
	
	$(document).on('click','.wmes-space-screen-content li',function(){
		$(this).addClass('wmes-space-screen-contentliac').siblings().removeClass('wmes-space-screen-contentliac');
	});
	
	$('.wmes-space-screencon').on('mouseleave',function(){
		$('.wmes-space-screen-content').removeClass('wmes-space-screen-contentac');
		$('.wmes-space-screen-title li').removeClass('wmes-space-screen-titleliac');
	});
	
	$(document).on('click',".topicTitle",function(){
		var id=$(this).attr("id");
		window.location.href=base_root+'/front/community/info/topicDetail.do?id='+id+'&type=t';
	});
	
	$(document).on('click','.lookover-all-reply',function(){
		$(this).parent().find(".answer-list").show();
		
	});
	
	//踩
	$(document).on('click','.wmes-community-news-message-right-cai',function(){
		var id;
		$(this).toggleClass("comment-topper-caied");
		var type=$(this).parents(".wmes-community-news-message").attr("type");
		var isCancel="";
		if(!$(this).hasClass("comment-topper-caied")){
			isCancel="1";
			$(this).next().text(Number($(this).next().text())-1);
		}else{
			$(this).next().text(Number($(this).next().text())+1);
		}
		
		if("a"==type){//回复
			id = $(this).parents(".wmes-community-news-message").attr("commentId");
			$.ajax({
	    		type:"post",
	    		datatype:"json",
	    		url:base_root+'/front/community/info/saveBehavior.do',
	    		data:{id:id,type:"c",isCancel:isCancel,behavior:'unlike'},
	    		success:function(json){
	    			//console.log(json);
	    		}
	    		
	    	})
			
		}else if("q"==type){//问题
			id = $(this).parents(".wmes-community-news-message").attr("questionId");
			$.ajax({
	    		type:"post",
	    		datatype:"json",
	    		url:base_root+'/front/community/info/saveBehavior.do',
	    		data:{id:id,type:"q",isCancel:isCancel,behavior:'unlike'},
	    		success:function(json){
	    			//console.log(json);
	    		}
	    		
	    	})
		}
		var nice=$(this).parents(".wmes-community-news-message").find(".wmes-community-news-message-right-nice");
		if(nice.hasClass("comment-topper-niceed")){
			nice.toggleClass("comment-topper-niceed");
			nice.next().text(Number(nice.next().text())-1)
		}
	})
	
	//点赞
	$(document).on('click','.wmes-community-news-message-right-nice',function(){
		$(this).toggleClass("comment-topper-niceed");
		var type=$(this).parents(".wmes-community-news-message").attr("type");
		var isCancel="";
		if(!$(this).hasClass("comment-topper-niceed")){
			isCancel="1";
			$(this).next().text(Number($(this).next().text())-1);
		}else{
			$(this).next().text(Number($(this).next().text())+1);
		}
		if("a"==type){//回复
			var id=$(this).parents(".wmes-community-news-message").attr("commentId");
			$.ajax({
	    		type:"post",
	    		datatype:"json",
	    		url:base_root+'/front/community/info/saveBehavior.do',
	    		data:{id:id,type:"c",isCancel:isCancel,behavior:'like'},
	    		success:function(json){
	    			//console.log(json);
	    		}
	    		
	    	})
			
		}else if("q"==type){//问题
			var id=$(this).parents(".wmes-community-news-message").attr("questionId");
			 
			$.ajax({
	    		type:"post",
	    		datatype:"json",
	    		url:base_root+'/front/community/info/saveBehavior.do',
	    		data:{id:id,type:"q",isCancel:isCancel,behavior:'like'},
	    		success:function(json){
	    			//console.log(json);
	    		}
	    		
	    	})
		}
		
		var cai=$(this).parents(".wmes-community-news-message").find(".wmes-community-news-message-right-cai");
		if(cai.hasClass("comment-topper-caied")){
			cai.toggleClass("comment-topper-caied");
			cai.next().text(Number(cai.next().text())-1)
		}
	})
	
	/*$(document).on('click',".addFocus",function(){
		addFocus(memberId,this);
	});
	$(".cancelFocus").on('click',function(){
		cancelFocus(memberId,this);
	})*/
	
	var lay_index;
	$(".wmes-space-information-bottom li").on('click',function(){ 
		var obj;
		if($(this).find(".addFocus").length>0){
			obj=$(this).find(".addFocus")[0];
			lay_index=layer.confirm(COMMUNITY_SPACE_SURETOADDFOCUS, { title:IFASPACE_LANG_Alert, icon: 3 ,btn: [COMMUNITY_SPACE_YES, COMMUNITY_SPACE_NO] },function () { 
				addFocus(memberId,obj);
			});
		}else if($(this).find(".cancelFocus").length>0){
			obj=$(this).find(".cancelFocus")[0];
			lay_index=layer.confirm(COMMUNITY_SPACE_SURETOCANCELFOCUS, { title:IFASPACE_LANG_Alert, icon: 3 ,btn: [COMMUNITY_SPACE_YES, COMMUNITY_SPACE_NO] },function () { 
				cancelFocus(memberId,obj);
			});
		}else if($(this).find(".askbtn").length>0){
			$("#askQuestion").show();
			popHeight();
		}  
	})
	
	$(".character-button-close,.character-close-ico").on('click',function(){
		$("#askQuestion").hide();
	});
	
	$('.wmes-close').on('click',function(){
		$(this).closest('.ifa-space-popup').hide();
	});
	//加关注
	function addFocus(id,obj){
		$.ajax({
			type:'post',
			datatype:'json',
			url:base_root+'/front/community/space/addFocus.do',
			data:{id:id},
			success:function(json){
				//console.log(json);
				$(obj).toggleClass("wmes-space-information-bottom-img0");
				$(obj).toggleClass("addFocus");
				$(obj).toggleClass("cancelFocus");
				if($(obj).hasClass("wmes-space-information-bottom-img0")){
					$(obj).next().text(langMutilForJs['community.space.cancelFocus']);
				}else{
					$(obj).next().text(langMutilForJs['community.space.addFocus']);
				}
				layer.close(lay_index);
			}
		})
	}
	
	//取消关注
	function cancelFocus(id,obj){
		$.ajax({
			type:'post',
			datatype:'json',
			url:base_root+'/front/community/space/cancelFocus.do',
			data:{id:id},
			success:function(json){
				//console.log(json);
				$(obj).toggleClass("wmes-space-information-bottom-img0");
				$(obj).toggleClass("addFocus");
				$(obj).toggleClass("cancelFocus");
				if($(obj).hasClass("wmes-space-information-bottom-img0")){
					$(obj).next().text(langMutilForJs['community.space.cancelFocus']);
				}else{
					$(obj).next().text(langMutilForJs['community.space.addFocus']);
				}
				layer.close(lay_index);
			}
		})
	}
	
      $(document).on('click','.comment-expression-ico',function(){
		var ele=$(this).closest('.comment-topper-contents').find('.comment-expression');
		ele.toggleClass('comment-expressionac');
		if(ele.hasClass('comment-expressionac') ){
			if(ele.find("#sinaEmotion").length==0){
				_emotion.init(ele);
			}
			
		}else{
			_emotion.destroy();
		}
		
		
	});
      _emotion.selectCallBack=function(alt){
  		//console.log("alt",alt);
  		$(".comment-expression-ico").click();
  		var textArea=$(".member-comment-text");
  		var text=textArea.val();
  		if(text==undefined)
  			text="";
  		textArea.val(text+alt)
  		
  	}
      
     $(".sendQuestion").on("click",function(){
    	var content=$(".member-comment-text").val();
    	if(content!=undefined && content!=''){
    		$.ajax({
    			type:'post',
    			datatype:'json',
    			url:base_root+"/front/community/space/sendQuestion.do",
    			data:{id:memberId,content:content},
    			success:function(json){
    				if(json.result){
    					layer.msg(langMutilForJs['community.space.question.success'],{icon:2});
    					$("#askQuestion").hide();
    				}
    				//console.log(json);
    			}
    		})
    	}
    })
      
    $(".wmes-space-add-attention").on("click",function(){
    	var memberId=$(this).attr("memberId");
    	var type=$(this).attr("type");
    	var obj=$(this).find(".wmes-space-information-bottom-img1")[0];
    	if("add"==type){
    		addFocus(memberId,obj)
    	}else{
    		  
    	} cancelFocus(memberId,obj)
    });
      
      $(document).on('click','.wmes-community-topic-collect',function(){
    	  var id=$(this).attr("id");
    	  $(this).toggleClass("news-collectionac");
      	var isCancel="";
      	if(!$(this).hasClass("news-collectionac")){
      		isCancel="1";
      	}
      	$.ajax({
      		type:"post",
      		datatype:"json",
      		url:base_root+'/front/community/info/saveBehavior.do',
      		data:{id:id,type:'t',isCancel:isCancel,behavior:'favorite'},
      		success:function(json){
      			////console.log(json);
      		}
      		
      	})
      })
      
    //设置AUM是否显示
	  $("#aum_setting_eye").on("click",function(){ 
	  		$(this).toggleClass('setting_eyeac');
	  		var isShowAum = $(this).attr('field')=='1'?'0':'1';
	  		if(isShowAum!=''){
	  			$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/ifa/space/updateIfaIsShowAum.do?datestr="+ new Date().getTime(),
					data : {'isShowAum':isShowAum },
					beforeSend: function () {},
					complete: function () {},
					success : function(json) {
						var result = json.result;
						if(result==true||result=='true'){
							$("#aum_setting_eye").attr('field',isShowAum);
						}
					}
				});
	  		}
      });
	  
	  //设置Total Return Rate是否显示
	  $("#topporflio_setting_eye").on("click",function(){  
	  		$(this).toggleClass('setting_eyeac');
	  		var showperformance = $(this).attr('field')=='1'?'0':'1';
	  		if(showperformance!=''){
	  			$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/ifa/space/updateIfaIsShowFerformance.do?datestr="+ new Date().getTime(),
					data : {'isShowperFormance':showperformance },
					beforeSend: function () {},
					complete: function () {},
					success : function(json) {
						var result = json.result;
						if(result==true||result=='true'){
							$("#topporflio_setting_eye").attr('field',showperformance);
						}
					}
				});
	  		}
      });
	  
	  $('#addFriend').click(function(){
//			var ifaname = $('#ifaName').text();
			var memberId = $('#ifaName').attr('memberid');
			var loginMemberId = $('#hidloginMemberId').val();
			if(memberId!=''&&loginMemberId!=''&&memberId==loginMemberId){
				layer.alert(IFASPACE_LANG_CannotAddYourself, { title:IFASPACE_LANG_Alert, icon: 0,btn: [COMMUNITY_SPACE_YES]  });
				return;
			}
			if(memberId!=''&&(loginMemberId==''||loginMemberId==undefined)){
				layer.alert(IFASPACE_LANG_LoginToAddFriends, { title:IFASPACE_LANG_Alert, icon: 0 ,btn: [COMMUNITY_SPACE_YES] });
				return;
			}
			layer.confirm(IFASPACE_LANG_SureToAddFriends, { title:IFASPACE_LANG_Alert, icon: 3 ,btn: [COMMUNITY_SPACE_YES, COMMUNITY_SPACE_NO] },function () { 
				 $.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/ifa/info/addFriend.do?datestr="+ new Date().getTime(),
					data : {'toMemberId':memberId },
					 beforeSend: function () {},
					 complete: function () {
					 },
					success : function(json) {
						json = JSON.parse(json);
						var result = json.result;
						if(result==true||result=='true'){
							layer.msg(IFASPACE_LANG_WaitToAddFriends, { title:IFASPACE_LANG_Alert,icon:2  }, function () {  });
						}else if(result==false||result=='false'){
							layer.msg(langMutilForJs['ifaspace.info.haveAddFriends'], { icon: 1, time: 2000 }, function () {  });
						}
					}
				});
	        	layer.closeAll();
	        });
		});	
	
	$('.wmes-space-information').on('click',function(){
		$('.ifacv').show();
	});
	
	//调整弹出窗居中位置
	function popHeight(){
		var tcheight;
		tcheight = parseInt($('.investment-plan-wrap').height())+parseInt($('.investment-plan-wrap').css('padding-top'))+parseInt($('.investment-plan-wrap').css('padding-bottom'))
		var jz;
		jz = parseInt(($(window).height() - tcheight)/2);
		$('.investment-plan-wrap').css('margin-top',jz);
	}
	
	
	//跳转页面
    $(document).on('click','.headPortrait',function(){
		var hidloginMemberId = _curMemberId;//
		var hidloginMemberType = _memberType;
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
    
  //如果当前登录的IFA刚好查看自己的空间，则显示编辑按钮，进行编辑个人最心hightlight
	$(".ifa-thehigt-edit").on("click",function(){
		var _selfParents = $(this).parents(".zone-edit");
    	_selfParents.toggleClass("ifa-thehigt-edit-wrap");

    	if( _selfParents.hasClass("ifa-thehigt-edit-wrap") ){
    		//编辑按钮
    		_selfParents.find(".ifa-thehight-word").removeAttr("readonly");
    		
    	}else{
    		_selfParents.find(".ifa-thehight-word").attr("readonly","");
    		//保存操作
    		saveHightlight();
    	}
	});
	
	//保存Hightlight数据
	function saveHightlight(){
		var hightLightMemo = $('#txtHightLight').val();
		var memberId = getQueryString('id');
		$.ajax({
			type : 'post',
			datatype : 'json',
			url : base_root + "/front/community/space/updateHighlight.do?datestr="+ new Date().getTime(),
			data : {'highlight':hightLightMemo,'id':memberId },
			 beforeSend: function () {},
			 complete: function () {},
			success : function(json) {
//				var result = json.result;
//				if(result==true||result=='true'){
//					layer.msg("添加好友申请发送成功，请等待通过", { icon: 1, time: 2000 }, function () {  });
//				}else if(result==false||result=='false'){
//					layer.msg("你已经添加该好友或已发过了申请", { icon: 1, time: 2000 }, function () {  });
//				}
			}
		});
		
	}
	
});