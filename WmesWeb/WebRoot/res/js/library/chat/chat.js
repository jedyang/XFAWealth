/**
 * chat.js 聊天组件
 * 
 * @author 赵晓聪
 */

define(function(require) {

	// 依赖
	var $ = require('jquery');
	require('iscroll');

	window.WSDK = require('im_wsdk');// 聊天工具
	var im_sdk = new WSDK();

	var _foot=require("foot");
	
	var _emotion=require("emotion");
	
	
	var recentList = []; // 最近联系人集合
	var chat = {};

	var curIconUrl = "";
	var otherIconUrl = "";
	var userModel = {};
	
	var userIds=[];
	
	
	var _isOut=false;//聊天是否被踢
	//监听当前页面是否为浏览器的当前标签
	var _currentTag=true;
	var hiddenProperty = 'hidden' in document ? 'hidden' :    
	    'webkitHidden' in document ? 'webkitHidden' :    
	    'mozHidden' in document ? 'mozHidden' :  null;
	    var visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
	    var onVisibilityChange = function(){
	        if (!document[hiddenProperty]) {    
	        	_currentTag=true;
	        	if(_isOut){
	        		login(userModel);
	        	}
	            // $(document).attr("title","切换了");
	        }else{
	        	_currentTag=false;
	            // $(document).attr("title","未切换");
	        }
	    }
	    document.addEventListener(visibilityChangeEvent, onVisibilityChange);
	    
	

	// 用于区分父容器（1:message,2:mybuddy）
	var _type = 1;// 默认为mybuddy

	function getHeadUrl(url, gender) {
		if (gender == undefined || gender == '')
			gender = 'M';
		if (url != undefined && url != "") {
			if (url.indexOf("/res") == 0) {
				url = url;
			} else if (url.indexOf("/u") == 0) {
				url = "/loadImgSrcByPath.do?filePath=" + url;
			} else {
				if ("F".indexOf(gender.toUpperCase()) == 0) {
					url = "/res/images/common/portrait_f.png";
				} else if ("M".startsWith(gender.toUpperCase())) {
					url = "/res/images/common/portrait_m.png";
				} else {
					url = "/res/images/common/portrait.png";
				}
			}
		} else {
			if ("F".indexOf(gender.toUpperCase()) == 0) {
				url = "/res/images/common/portrait_f.png";
			} else if ("M".indexOf(gender.toUpperCase())) {
				url = "/res/images/common/portrait_m.png";
			} else {
				url = "/res/images/common/portrait.png";
			}
		}
		return base_root + url;
	}

	// var Json = {"user": [{"name":"我的客户1", "online":2, "total":3, "uesrList":
	// [{"portrait":"/res/images/discover/Leo.png", "name":"Leo", "online":true
	// }, {"portrait":"/res/images/discover/live01.png", "name":"Anjolie",
	// "online":true }, {"portrait":"/res/images/discover/Jennifer.png",
	// "name":"Suki", "online":false } ] }, {"name":"我的客户2", "online":2,
	// "total":2, "uesrList": [{"portrait":"/res/images/discover/live08.png",
	// "name":"Lam", "online":true },
	// {"portrait":"/res/images/discover/live02.png", "name":"Chung",
	// "online":true } ] }, ]} var chat = {};
	chat.strscroll = "";
	chat.conscroll = "";
	chat.addHtml = function() {
		var _this = this;
		$.ajax({
			type : "post",
			datatype : "json",
			url : base_root + "/front/web/webchat/getContacts.do",
			data : {},
			success : function(json) {
				if (json != undefined) {
					var _html = '<div class="discover-space-popup" style="display:none;">' + '<div class="discover-space-mask"></div>' + '<div class="space-mask-wrap">' + '<div class="discover-chat-mask">' + '<div class="discover-chat-left">' + '<div class="discover-chat-tree">';

					$.each(json, function(i, n) {
						var list = n.memberList;
						_html += '<div class="chat-tree-rows"> <p class="chat-tree-rows-title">' + langMutilForJs['chat.type.' + n.type] + '<span class="tree-total-number">  (' + list.length + ')</span> ' + '<img class="tree-opentree-ico" src="' + base_root + '/res/images/discover/opentree_ico.png"> </p>';

						if (list != undefined) {
							_html += '<ul class="chat-tree-list"> ';
							$.each(list, function(i, member) {
								if($.inArray(member.userId,userIds)<0 && null!=member.userId)
									userIds.push(member.userId);
								_html += ' <li class="chat-list-rows"> <img class="chat-list-portrait " src="' + getHeadUrl(member.iconUrl, member.gender) + '"><div class="chat-list-right">' + ' <p class="chat-list-name " memberId="' + member.userId + '">' + member.nickName + '</p> ' + '</div> </li>';
							});
							_html += '</ul>';
						}
						_html += '</div>'

					});
					
					console.log("userIds",userIds);

					_html += '</div></div>';
					// 右边的聊天信息
					_html += '<div class="discover-chat-right"> <div class="discover-chat-wrap"><span class="contact-name">  </span>'
						+ '<div class="discover-chat-ico"> <span id="discover-chat-close" class="wmes-close" ></span>' 
						+ ' </div> </div> <div class="discover-chat-contents">' 
						+ ' <div class="discover-chat-contents-srcoll">'
//						+ '<div class="chat-document-sendcon"><img class="chat-document-sendimg" /><div class="chat-document-sendtext"><p>aaa.png</p><p>发送成功</p></div><div class="chat-document-senddownload"></div><div class="chat-document-senddocheck"></div></div>' 
						+'<div class="discover-chat-msg-wrap">  '
						+ '</div><div class="chat-expression-con"></div></div> <div class="discover-chat-add"> <div class="chat-add-ico-wrap">'
						
					// +'<img class="chat-expression-ico" src="'+ base_root
					// +'/res/images/discover/a_ico.png"> '
					+ '<img class="chat-expression-ico show-expression " src="' + base_root + '/res/images/discover/expression_ico.png">' 
					+ '<input type="file" id="J_fileInput" /> <img class="chat-expression-ico file-upload" src="' + base_root + '/res/images/discover/addimg_ico.png">' 
					+ ' </div> <textarea class="discover-chat-text"></textarea> ' + '<a class="discover-chat-button" href="javascript:;">Send</a> </div> ' 
					+ '</div> <span class="chat-hide-btn"></span>'
					+'<audio id="chatAudio"><source src="'+base_root+'/res/js/library/chat/14.mp3" type="audio/mpeg"></audio>'
					+ '</div></div></div>';
					//_html+=' <input type="file" id="J_fileInput" />';
					$('body').append(_html);
					_this.iscroll();
					_this.cScroll();

					_emotion.init($(".chat-expression-con"));
					// getRecentContact();
					$('#J_fileInput').css({'width':'17px','height':'16px','position':'absolute','left':'42px','opacity':'0','overflow':'hidden','cursor':'pointer'});
					
				
				}

			}
		});
	}
	
	
	

	chat.iscroll = function() {
		strscroll = new IScroll('.discover-chat-left', {
			scrollbars : true,
			interactiveScrollbars : true,
			shrinkScrollbars : 'scale',
			fadeScrollbars : true,
			mouseWheel : true,
			click : true
		});
//		strscroll.on('scrollEnd',function(){
//			console.log('1');
//		});
	}
	chat.cScroll = function() {
		conscroll = new IScroll('.discover-chat-contents-srcoll', {
			scrollbars : true,
			interactiveScrollbars : true,
			shrinkScrollbars : 'scale',
			fadeScrollbars : true,
			mouseWheel : true,
			click : true
		});
	}

	chat.show = function() {
		$(".discover-chat-button").css("background-color","#999999");
		$(".discover-space-popup").show();
		strscroll.refresh();
		getUserStatus();//获取用户状态
		
	}
	chat.load = function(id, nickName, iconUrl) {
		$(".contact-name").attr("memberId", id);
		$(".contact-name").text(nickName);
		$(".discover-space-popup").show();
		otherIconUrl = iconUrl;
		getHistory(id, '');
		strscroll.refresh();
		conscroll.refresh();
		conscroll.scrollTo(0, -$(".discover-chat-contents-srcoll").height(), 0);
		getUserStatus();//获取用户状态
		/*
		 * $.ajax({ type:'post', datatype:'json',
		 * url:base_root+'/front/web/webchat/getRecently.do', data:{ids:id},
		 * success:function(json){ if(json!=undefined && json.length>0){ var
		 * data=json[0]; otherIconUrl=getHeadUrl(data.iconUrl,data.gender);
		 * 
		 * //console.log("otherIconUrl",otherIconUrl); } } })
		 */

	}
	chat.hide = function() {
		$(".discover-space-popup").hide();
	}

	chat.sdk = im_sdk;
	chat.setType = function(type) {
		_type = type;
	}
	
	chat.init = function() {// type--用于区分父容器（1:message,2:mybuddy）

		// _type=type
		$.ajax({
			type : "post",
			datatype : "json",
			url : base_root + "/front/web/webchat/loginOnChat.do",
			data : {},
			success : function(json) {
				if (json.loginUser != undefined) {
					var imUserId = json.loginUser.imUserId;
					var imPwd = json.loginUser.imUserPwd;
					var iconUrl = json.loginUser.imIconUrl;
					curIconUrl = getHeadUrl(iconUrl, json.loginUser.gender);
					userModel.appKey = '23573928';
					userModel.userId = imUserId;
					userModel.pwd = imPwd;
					login(userModel);
				}
			}
		})

		this.addHtml();
		var _chat = this;
		$(document).on("click", ".chat-tree-rows-title", function() {
			$(this).parents(".chat-tree-rows").toggleClass("chat-tree-hide");
			strscroll.refresh();
		}).on("click", ".chat-hide-btn", function() {

			$(this).parents(".discover-chat-mask").toggleClass("discover-chat-left-hide");

		}).on("click", "#discover-chat-close", function() {
			chat.hide();
		}).on('click', '.chat-list-name', function() {// 切换聊天对象
			var memberId = $(this).attr("memberId");
			var nickName = $(this).text();
			var url = $(this).parent().parent().find(".chat-list-portrait").attr("src");
			$(".contact-name").attr("memberId", memberId);
			$(".contact-name").text(nickName);
			otherIconUrl = url;
			getHistory(memberId, '');
			$(".discover-chat-button").css("background-color","#069dd5");
			

		}).on('click', '.discover-chat-button', function() {// 发送消息
			var textarea = $(this).parent().find(".discover-chat-text")
			var content = textarea.val();
			if (content == "" || content == undefined) {
				layer.msg("不能发送空消息");
				return;
			}
			var userId = $(this).parents(".discover-chat-right").find(".contact-name").attr("memberid");
			$.ajax({
				type : "post",
				datatype : "json",
				url : base_root + "/front/web/webchat/sendMessages.do",
				data : {
					content : content,
					toUserId : userId
				},
				success : function(json) {
					if (json.result) {
						sendMsg(userId, content,0);//发送文本消息
						content=_emotion.parseEmotion(content);
						var html = ' <div class="discover-own-msg"> ' + '<img class="discover-own-portrait" src="' + curIconUrl + '" /> ' + '<p class="discover-own-contens">' + content + '</p> ' + '</div>';
						$('.discover-chat-msg-wrap').append(html);

						conscroll.scrollTo(0, -$(".discover-chat-msg-wrap").height(), 0);
						conscroll.refresh();
						textarea.val('');
					}
				}
			})
			/*
			 * console.log(userId); console.log(content);
			 */
		}).on('click', '.chat-record-more-wrap', function() {
			var userId = $(this).parents(".discover-chat-right").find(".contact-name").attr("memberid");
			var nextKey = $(this).attr("next-key");
			getHistory(userId, nextKey);
			$(this).remove();
		}).on("click", '.show-expression', function() {
			//_chat.loadFace($('.chat-expression-con'));
			$('.chat-expression-con').toggleClass('chat-expression-conac');
			//console.log($('.chat-expression-con'));
			
		}).on("keydown",function(){
			if (event.keyCode == "13") {//keyCode=13是回车键
				if($(".discover-chat-text").is(":focus")){
					$(".discover-chat-button").click();
					return false;
				}
              
            }
		}).on("click",".file-upload",function(){
			//console.log("upload");
			//var  fileInput = document.getElementById('J_fileInput');
			im_sdk.Plugin.Uploader.upload({
		        base64Str: 'base64',
		        ext: 'png||jpg',
		        type: 1,
		        success: function(data){
		        	console.log("upload success",data);
		        }
			});
		}).on("change","#J_fileInput",function(){
			var userId = $(".contact-name").attr("memberid");
			if(userId==undefined || userId=="")
				return;
			var  fileInput = document.getElementById('J_fileInput');
			//console.log(fileInput);
			var filePath=fileInput.value;
			var strValue=filePath.split('\\');
			var fileName="";
			if(strValue!=undefined){
				fileName=strValue[strValue.length-1];
			}
			//console.log("fileName"+fileName);
			im_sdk.Plugin.Image.upload({
	            target: fileInput,
	            success: function(data){
	            	
	            	
	            	var url=data.data.url;
	            	var msgMdl={};
	            	msgMdl.url=url;
	            	msgMdl.fileName=fileName;
	            	sendMsg(userId, JSON.stringify(msgMdl),1);//发送图片消息
	                /*
	                {
	                    code: 1000,
	                    resultCode: 'SUCCESS',
	                    data: {
	                        url: '',
	                        base64Str: ''
	                    }
	                }
	                */
	                console.log('上传成功', data);
	            },
	            error: function(error){
	                console.log('上传失败', error);
	            }
	        });
		})
		

	}
	
	
	
	_emotion.selectCallBack=function(alt){
		var text=$(".discover-chat-text").val()+alt;
		$('.chat-expression-con').removeClass('chat-expression-conac');
		$(".discover-chat-text").val(text);
		//$(".discover-chat-text").innerHTML=text;
	}
	
	var move=false;//移动标记 
	var iX,iY;//鼠标离控件左上角的相对位置 
	$(document).on('mousedown','.discover-chat-wrap',function(e){
		iX=e.clientX - $('.space-mask-wrap').offset().left;
		iY=e.clientY - $('.space-mask-wrap').offset().top;
		move=true;
	});
	$(document).on('mousemove',function(e){
		if(move){ 
			var e = e || window.event;
			$(".space-mask-wrap").css("top",(e.clientY - iY));
			$(".space-mask-wrap").css("left",(e.clientX - iX));
		};
	}).mouseup(function(){ 
		move=false;
	});

	/*
	 * chat.getChatCount = function(){ im_sdk.Base.getUnreadMsgCount({ count:
	 * 50, success: function(data){
	 * //console.log("getUnreadMsgCount111111111111111",data); var
	 * list=data.data; var count=0; $.each(list,function(i,n){
	 * count+=n.msgCount; }); $.ajax({ type:'post', datatype:'json',
	 * url:base_root+'/front/ifa/info/getAllUnreadCount.do', data:{},
	 * success:function(json){ if(json!=undefined){ count+=json.count;
	 * Getchat(count); im_sdk.Base.destroy(); } } })
	 *  }, error: function(error){ console.log('getUnreadMsgCount
	 * fail1111111111111', error); } }); }
	 */

	/**
	 * ******************************************** IM SDK
	 * 方法*********************************************************
	 */

	// 登录
	function login(userModel) {
		im_sdk.Base.destroy();
		im_sdk.Base.login({
			uid : userModel.userId,
			appkey : userModel.appKey,
			credential : userModel.pwd,
			timeout : 5000,
			success : function(data) {
				console.log('login success', data);
				// 登录成功之后的操作
				getUnreadMsgCount();
				getRecentContact();
				startListenAllSingleMsg();
				// chat.getChatCount();
			},
			error : function(error) {
				// {code: 1002, resultText: 'TIMEOUT'}
				console.log('login fail', error);
			}
		});
	}

	// 登出
	function logout() {
		im_sdk.Base.logout();
	}

	// 本地销毁WSDK及退出登录
	function destroy() {
		im_sdk.Base.destroy();
		im_sdk = null;
	}

	// 未读消息
	function getUnreadMsgCount() {
		var recentTribe = [];
		im_sdk.Base.getUnreadMsgCount({
			count : 10,
			success : function(data) {
				console.log('get unread msg count success', data);
				var list = data.data;
				var str = '';
				var totalCount = 0;
				list.forEach(function(item) {
					if (item.contact.substring(0, 8) === 'chntribe') {// chntribe表示群组消息
						recentTribe.push(item);
					} else {
						var id = im_sdk.Base.getNick(item.contact);
						if (_type == 2) {// mybuddy 页面

							var a = $('.my-buddy-contens img[memberid=' + id + ']');
							if (a != undefined) {
								a.parent().find(".my-ifa-msg").show();
								a.parent().find(".my-ifa-msg").text(item.msgCount);
							}
						} else if (_type == 1) {// message页面
							var a = $('.dialogue-message-ul li[memberid=' + id + ']');
							if (a != undefined) {
								a.find(".message_num").text(item.msgCount);
								a.find(".message_num").show();

							}
						}

						totalCount += item.msgCount;
						
					}

				});
				_foot.refreshChatCount(totalCount);
				recentTribe.length && console.log('最近给我发消息的群', recentTribe);
			},
			error : function(error) {
				/*
				 * if(error.code='1001'){ setTimeout(function(){
				 * getUnreadMsgCount(); },1000)
				 *  }
				 */
				console.log('get unread msg count fail', error);
			}
		});
	}

	// 最近联系人
	function getRecentContact() {
		im_sdk.Base.getRecentContact({
			count : 10,
			success : function(data) {
				//console.log('get recent contact success', data);
				data = data.data;
				var list = data.cnts, type = '';

				list.forEach(function(item) {

					var id = im_sdk.Base.getNick(item.uid);
					recentList.push(id);

				});
				$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + '/front/web/webchat/getRecently.do',
					data : {
						ids : recentList.toString()
					},
					success : function(json) {
						getUnreadMsgCount();
						$(".recent-list").remove();
						if (json != undefined) {
							var html = "";
							html += '<div class="chat-tree-rows recent-list"> <p class="chat-tree-rows-title">Recently<span class="tree-total-number">  (' + list.length + ')</span> <img class="tree-opentree-ico" src="/wmes/res/images/discover/opentree_ico.png"> </p>' + '<ul class="chat-tree-list">  '
							$.each(json, function(i, n) {
								html += '<li class="chat-list-rows"> <img class="chat-list-portrait" src="' + getHeadUrl(n.iconUrl, n.gender) + '"> ' + '<div class="chat-list-right"> <p class="chat-list-name" memberid="' + n.userId + '">' + n.nickName + '</p> </div> </li>';
							});
							html += '</ul></div>';
							$(".discover-chat-tree").prepend(html);
							
							html="";
							
							if (_type == 2) {
								
							} else if (_type == 1) {
								$.each(json, function(i, n) {
									html += '<li memberid="' + n.userId + '" nickname="' + n.nickName + '">' + '<img class="system-message-portrait" src="' 
									+ getHeadUrl(n.iconUrl, n.gender) + '">' + '<div class="message_num" style="display:none">0</div>' 
									+ '<p class="system-message-title"><a class="discover-a-href" href="javascript:;">' + n.nickName + '</a></p>' 
									
									list.forEach(function(item) {
										var id= im_sdk.Base.getNick(item.uid);
										if(id==n.userId){
											var date = formatDateFun(item.time, "yyyy-MM-dd HH:mm:ss");
											html+='<p class="system-message-brief">'+item.msg[0][1]+'</p>' 
											+ '<p class="system-message-time">'+date+'</p>' ;
											
										}
									});
									html+= '</li>';
								});
								$(".dialogue-message-ul").prepend(html);
								var nowHeight = $(".swiper-slide").eq($(".swiper-pagination-bullet-active").index()).find(".discover-wrap").height() + 40;
								$(".discover-wrapper").height(nowHeight);
							}

						}
					}
				})

			},
			error : function(error) {
				/*
				 * if(error.code='1001'){ setTimeout(function(){
				 * getRecentContact(); },1000);
				 *  }
				 */
				console.log('get recent contact fail', error);
			}
		});
	}

	/*function getUserStatus(){
		sdk.Chat.getUserStatus({
	        uids: userIds,
	        hasPrefix: true,
	        success: function(data){
	        	console.log("getUserStatus:",data);
	        	
	        }
		});
	}*/
	
	
	function formatDateFun(date, format) {
		if (format == undefined || format == "")
			format = "yyyy-MM-dd HH:mm:ss";
		// var d = new Date(date);
		var d = new Date(date);
		var zeroize = function(value, length) {
			if (!length)
				length = 2;
			value = String(value);
			for ( var i = 0, zeros = ''; i < (length - value.length); i++) {
				zeros += '0';
			}
			return zeros + value;
		};

		return format.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function($0) {
			switch ($0) {
			case 'd':
				return d.getDate();
			case 'dd':
				return zeroize(d.getDate());
			case 'ddd':
				return [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat' ][d.getDay()];
			case 'dddd':
				return [ 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday' ][d.getDay()];
			case 'M':
				return d.getMonth() + 1;
			case 'MM':
				return zeroize(d.getMonth() + 1);
			case 'MMM':
				return [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ][d.getMonth()];
			case 'MMMM':
				return [ 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December' ][d.getMonth()];
			case 'yy':
				return String(d.getFullYear()).substr(2);
			case 'yyyy':
				return d.getFullYear();
			case 'h':
				return d.getHours() % 12 || 12;
			case 'hh':
				return zeroize(d.getHours() % 12 || 12);
			case 'H':
				return d.getHours();
			case 'HH':
				return zeroize(d.getHours());
			case 'm':
				return d.getMinutes();
			case 'mm':
				return zeroize(d.getMinutes());
			case 's':
				return d.getSeconds();
			case 'ss':
				return zeroize(d.getSeconds());
			case 'l':
				return zeroize(d.getMilliseconds(), 3);
			case 'L':
				var m = d.getMilliseconds();
				if (m > 99)
					m = Math.round(m / 10);
				return zeroize(m);
			case 'tt':
				return d.getHours() < 12 ? 'am' : 'pm';
			case 'TT':
				return d.getHours() < 12 ? 'AM' : 'PM';
			case 'Z':
				return d.toUTCString().match(/[A-Z]+$/);
				// Return quoted strings with the surrounding quotes removed
			default:
				return $0.substr(1, $0.length - 2);
			}
		});
	}
	// 监听所有消息，包括成功和失败的消息: START_RECEIVE_ALL_MSG
	// 所有成功的消息： MSG_RECEIVED
	// 只监听成功的单聊消息：CHAT.MSG_RECEIVED
	// 只监听成功的群聊消息：TRIBE.MSG_RECEIVED

	// 启动接收当前登录用户的所有单聊消息 ，不能再建立与某个人的单聊，否则会失效
	function startListenAllSingleMsg() {
		im_sdk.Base.stopListenAllMsg();// 停止接收所有消息
		im_sdk.Event.on('CHAT.MSG_RECEIVED', function(data) {
			//console.log('获取所有的单聊消息', data);
			$('#chatAudio')[0].play();
			var msgList = data.data.msgs;
			var html = "";
			$.each(msgList, function(i, n) {
				var userId = im_sdk.Base.getNick(n.from);
				if ($(".contact-name").attr("memberid") == userId) {
					var msg=n.msg;
					msg=_emotion.parseEmotion(msg)
					html = ' <div class="discover-others-msg"> ' + '<img class="discover-others-portrait" src="' + otherIconUrl + '" /> ' + ' <p class="discover-others-contens">' + msg + '</p> ' + ' </div> ';
					$(".discover-chat-msg-wrap").append(html);
					conscroll.scrollTo(0, -$(".discover-chat-msg-wrap").height(), 0);
					conscroll.refresh();
					setReadTag(userId);
				} else {
					var a = $('.my-buddy-contens img[memberid=' + userId + ']');
					if (a != undefined) {
						a.parent().find(".my-ifa-msg").show();
						var number = a.parent().find(".my-ifa-msg").text();
						if (number == undefined || number == '')
							number = '0';
						a.parent().find(".my-ifa-msg").text(Number(number) + 1);
					}
					// console.log(a);
				}

			});
			
			getUnreadMsgCount();
		});

		im_sdk.Event.on('KICK_OFF', function(data) {
			console.log('啊，我被踢了', data);
			_isOut=true;
			if(_currentTag){
				console.log('当前页面被迫下线，重新登录');
				setTimeout(function(){
					login(userModel);
				},10000)
				 
			}
			
		})
		im_sdk.Base.startListenAllMsg();
	}

	// 启动接收当前登录用户所有的聊天消息
	function startListenAllMsg() {
		im_sdk.Event.on('MSG_RECEIVED', function(data) {
			console.log('MSG_RECEIVED', data);
		});
		im_sdk.Base.startListenAllMsg();
	}

	// 停止接收当前登录用户所有的聊天消息
	function stopListenAllMsg() {
		im_sdk.Event.on('MSG_RECEIVED', function(data) {
			console.log('MSG_RECEIVED_Stop', data);
		});
		im_sdk.Base.stopListenAllMsg();
	}

	// 启动接收与某个用户的聊天消息
	function startListenAllMsgByUid(toUserId) {
		im_sdk.Event.on('CHAT.MSG_RECEIVED', function(data) {
			console.log("CHAT.MSG_RECEIVED", data);

			/*
			 * var msgList=data.data.msgs; var html="";
			 * $.each(msgList,function(i,n){ var
			 * userId=im_sdk.Base.getNick(n.from);
			 * if($(".contact-name").attr("memberid")==userId){ html=' <div
			 * class="discover-others-msg"> ' +'<img
			 * class="discover-others-portrait" src="'+ base_root
			 * +'/res/images/discover/Leo.png" /> ' +'
			 * <p class="discover-others-contens">'+n.msg+'</p> ' +' </div> ';
			 * $(".discover-chat-msg-wrap").append(html);
			 * conscroll.scrollTo(0,-$(".discover-chat-msg-wrap").height(),0);
			 * conscroll.refresh(); }
			 * 
			 * });
			 */
		});
		im_sdk.Chat.startListenMsg({
			touid : toUserId
		});
	}

	// 停止接收与某个用户的聊天消息
	function stopListenAllMsg(toUserId) {
		im_sdk.Event.on('CHAT.MSG_RECEIVED', function(data) {
			console.log(data);
		});
		im_sdk.Chat.startListenMsg();
	}

	// 获取用户在线状态
	// userIdJson格式为:['iwangxinvisitor1','iwangxinvisitor2','iwangxinvisitor3']
	function getUserStatus() {
		/*im_sdk.Chat.getUserStatus({
			uids : userIds,
			hasPrefix : false,
			appkey:userModel.appKey,
			success : function(data) {
				console.log("userIdJson",userIds);
				console.log("getUserStatus",data);
				var status=data.data.status;
				$.each(userIds,function(i,n){
					if(status[i]==0){
						var a = $('.chat-tree-rows p[memberid=' + n + ']');
						a.parents(".chat-list-rows").find(".chat-list-portrait ").addClass("chat-list-portrait-off");
						//console.log("outline",a);
					}
				})
				console.log('getUserStatus' ,data);
			},
			error : function() {
				console.log('getUserStatus fail');
			}
		});*/
	}

	// 发送消息
	function sendMsg(toUserId, toMsg,type) {
		im_sdk.Chat.sendMsg({
			touid : toUserId,
			msg : toMsg,
			msgType:type,
			success : function(data) {
				// layer.msg('send succes');
				console.log('send success', data);
			},
			error : function(error) {
				// layer.msg('send fail');
				if (error.code = '1001') {
					/*
					 * setTimeout(function(){ sendMsg(toUserId,toMsg); },1000);
					 */
				}
				console.log('send fail', error);
			}
		});
	}

	// 设置消息已读
	function setReadState() {
		im_sdk.Chat.setReadState({
			touid : 'touid',
			timestamp : Math.floor((+new Date()) / 1000),
			success : function(data) {
				console.log('set read state success', data);
			},
			error : function(error) {
				if (error.code = '1001') {
					// login(userModel);
					setTimeOut(function() {
						setReadState();
					}, 1000)

				}
				console.log('set read state fail', error);
			}
		});
	}

	// 获得历史消息
	function getHistory(toUserId, nextKeyNum) {
		if (nextKeyNum == "" || nextKeyNum == undefined) {// 如果是第一页
			$('.discover-chat-msg-wrap').empty();
		}
		im_sdk.Chat.getHistory({
			touid : toUserId,
			nextkey : nextKeyNum,
			count : 10,
			success : function(data) {
				console.log('get history msg success', data);

				var dataList = data.data.msgs;
				var next = data.data ? data.data.nextKey : '';
				var html = '';
				var moreHtml = "";

				if (toUserId == "" || toUserId == undefined)
					return;

				$.each(dataList, function(i, n) {
					var id = im_sdk.Base.getNick(n.from);// iwk51z0yebd8827f-5da2-423a-bd63-24e2b45e0666
					var msg=n.msg;
					msg=_emotion.parseEmotion(msg);
					if (id == toUserId) {// 如果是对方发的消息
						//html=getOtherMsg(n);
						html = ' <div class="discover-others-msg"> ' + '<img class="discover-others-portrait" src="' + otherIconUrl + '" /> ' + ' <p class="discover-others-contens">' + msg + '</p> ' + ' </div> ' + html;
					} else {
						html =' <div class="discover-own-msg"> ' + '<img class="discover-own-portrait" src="' + curIconUrl + '" /> ' + '<p class="discover-own-contens">' + msg + '</p> ' + '</div>' + html;
					}
					// console.log(id+" : " +n.msg);
				});
				if (next != "" && next != undefined && !next.indexOf("magic") == 0) {
					moreHtml = '<div class="chat-record-more-wrap" next-key="' + next + '"><img class="chat-record-more-ico" src="' + base_root + '/res/images/discover/chat-record-time.png" /><p class="chat-record-more-title">查看更多消息</p></div>';
					html = moreHtml + html;
				}
				if (nextKeyNum == "" || nextKeyNum == undefined) {// 如果是第一页
					$('.discover-chat-msg-wrap').empty().append(html);
				} else {
					$('.discover-chat-msg-wrap').prepend(html);
				}
				conscroll.refresh();
				setReadTag(toUserId);//设置已读
				

			},
			error : function(error) {

				/*
				 * if(error.code='1001'){ setTimeout(function(){ getHistory();
				 * },1000)
				 *  }
				 */
				console.log('get history msg fail', error);
			}
		});
	}
	
	//设置已读
	function setReadTag(toUserId){
		im_sdk.Chat.setReadState({
			touid : toUserId,
			timestamp : Math.floor((+new Date()) / 1000),
			success : function(data) {
				//console.log('设置已读成功', data);
				getUnreadMsgCount();
				var a = $('.my-buddy-contens img[memberid=' + toUserId + ']');
				if (a != undefined) {
					a.parent().find(".my-ifa-msg").hide();
					a.parent().find(".my-ifa-msg").text("0");
				}
			},
			error : function(error) {
				console.log('设置已读失败', error);
			}
		});
	};
	
	//重整当前用户发送的消息
	function getOwnMsg(msgData){
	    var html="";
	    if(msgData!=undefined){
	    	if(msgData.type=='1'){//如果是图片
	    		var msg= eval("(" + msgData.msg + ")")//JSON.parse(msgData.msg);
	    		console.log(msg);
	    		html='<div class="chat-document-sendcon"><img class="chat-document-sendimg" src='+msgData.url+' /><div class="chat-document-sendtext"><p>aaa.png</p><p>发送成功</p></div><div class="chat-document-senddownload"></div><div class="chat-document-senddocheck"></div></div>';
	    	}else if(msgData.type=='0'){//如果是文本
	    		html = ' <div class="discover-own-msg"> ' + '<img class="discover-own-portrait" src="' + curIconUrl + '" /> ' + '<p class="discover-own-contens">' + msgData.msg + '</p> ' + '</div>' ;
	    	}
	    }
	}
	//重整当前聊天对方用户发送的消息
	function getOtherMsg(msgData){
		var html="";
	    if(msgData!=undefined){
	    	if(msgData.type=='1'){//如果是图片
	    		var msg= eval("(" + msgData.msg + ")")//JSON.parse(msgData.msg);
	    		console.log(msg);
	    		html='<div class="chat-document-sendcon"><img class="chat-document-sendimg" src='+msgData.url+' /><div class="chat-document-sendtext"><p>aaa.png</p><p>发送成功</p></div><div class="chat-document-senddownload"></div><div class="chat-document-senddocheck"></div></div>';
	    	}else if(msgData.type=='0'){//如果是文本
	    		html = ' <div class="discover-others-msg"> ' + '<img class="discover-others-portrait" src="' + otherIconUrl + '" /> ' + ' <p class="discover-others-contens">' + msgData.msg + '</p> ' + ' </div> ' ;
	    	}
	    }
	}
	
	$('body').on('click',function(){
		if($('.chat-expression-con').hasClass('chat-expression-conac')){
			$('.chat-expression-con').removeClass('chat-expression-conac');
		}
	});
	
	/**
	 * ********************************************IM SDK
	 * 方法*********************************************************
	 */
	
	// 获取聊天数
	function Getchat(num) {
		var _num = num;

		if (_num > 0) {
			var _html = '<div class="wmes-chat-number">' + _num + '</div>';
			$(".ls-menu-discover").parents("li").append(_html);
			$(".ls-menu-messages").parentsUntil("li").append(_html);
		}
	}

	return chat;

});
