define(function(require) {
	require('layui');
	var $=require('jquery');
	var cookies=require('cookies');
	//var layer = layui.layer;
	
	var host=window.document.location.host;
	
	layui.use('layim', function(layim){
		 var socket = null;
		    var im = {
		        getUid:function () {
		          var uid = sessionMemberId;
		            return uid;
		        },
		        init:function(){
		            if ('WebSocket' in window){
		            	
		                var uid = im.getUid();
		                if(!uid){
		                    //console.log('当前用户未登陆，应该跳到login');
		                }else {
		                    var socketUrl = 'ws://'+host+base_root+'/websocket/'+ uid;
		                    socket = new WebSocket(socketUrl);
		                    im.startListener();
		                   
		                }
		            } else {
		                alert('当前浏览器不支持WebSocket功能，请更换浏览器访问。');
		            }
		        },
		        startListener:function () {
		            if (socket) {
		                // 连接发生错误的回调方法
		                socket.onerror = function () {
//		                    console.log("连接失败!");
		                };
		                // 连接成功建立的回调方法
		                socket.onopen = function (event) {
//		                    console.log("连接成功");
		                    cookies.set('socketRe', '1'); 
		                }
		                // 接收到消息的回调方法
		                socket.onmessage = function (event) {
//		                    console.log("接收到消息");
		                    im.handleMessage(event.data);
		                }
		                // 连接关闭的回调方法
		                socket.onclose = function () {
//		                    console.log("关闭连接！!");
		                }
		            }
		        },
		        handleMessage:function (msg) {
		            var msg = JSON.parse(msg);
		            //console.log(msg);
		            switch (msg.type){
		                case 'TYPE_TEXT_MESSAGE':
		                    layim.getMessage(msg.msg);
		                    break;
		                default:
		                    break;
		            }
		        }
		    };
        	im.init();

		  //基础配置
		  layim.config({
		    //初始化接口
		    init: {
		      url: base_root+'/front/chat/getBaseList.do'
		      ,data: {id: im.getUid()}
		    }   
		    
		    //查看群员接口
		    ,members: {
		      url: base_root+'/front/chat/getMember.do'//'json/getMembers.json'
		      ,data: {}
		    }
		    /*//上传图片接口
		    ,uploadImage: {
		      url: base_root+'/upload?t=img' 
		      ,type: 'post' //默认post
		    } 
		    //上传文件接口
		    ,uploadFile: {
		      url: base_root+'/upload?t=file' 
		      ,type: 'post' //默认post
		    }*/
		    ,initSkin: '5.jpg' //1-5 设置初始背景
		    ,min: true //是否始终最小化主面板（默认false）
		    ,isgroup: false //是否开启群组
		    
		    ,notice: true //是否开启桌面消息提醒，默认false
		   
		    ,msgbox:  base_root+'/front/chat/msgbox.do' //消息盒子页面地址，若不开启，剔除该项即可
		 
		    ,chatLog:   base_root+'/front/chat/chatLog.do' //聊天记录页面地址，若不开启，剔除该项即可
		  });
		  

		  //监听在线状态的切换事件
		  layim.on('online', function(data){
		     alert(data);
		    //console.log(data);
		  });
		  
		  //监听签名修改
		  layim.on('sign', function(value){
			  var hightLightMemo = value;
//				var memberId = getQueryString('id');
				$.ajax({
					type : 'post',
					datatype : 'json',
					url : base_root + "/front/community/space/updateHighlight.do?datestr="+ new Date().getTime(),
					data : {'highlight':hightLightMemo },
					success : function(json) {
						if(json.result){
							layer.msg("修改成功");
						}
					}
				});

		  });

		 
		  
		  //监听layim建立就绪
		  layim.on('ready', function(res){
		    //console.log(res.mine);
		    // layim.msgbox(3); //模拟消息盒子有新消息，实际使用时，一般是动态获得 
		    //添加好友（如果检测到该socket）
		     
		    setTimeout(function(){
		      //接受消息（如果检测到该socket）
		        
		     /* layim.getMessage({
		        username: "贤心"
		        ,avatar: "http://tp1.sinaimg.cn/1571889140/180/40030060651/1"
		        ,id: "100001"
		        ,type: "friend"
		        ,content: "嗨，你好！欢迎体验LayIM。演示标记："+ new Date().getTime()
		      });     */
		    }, 3000);
		  });

		  //监听发送消息
		  layim.on('sendMessage', function(data){
			  //console.log(data);
		        var msg = JSON.stringify(data);
		        socket.send(msg);

		  });
		  
		  //监听查看群员
		  layim.on('members', function(data){

		  }); 
		  //监听聊天窗口的切换
		  layim.on('chatChange', function(res){
		    var type = res.data.type;
		    //console.log(res.data.id)
		    if(type === 'friend'){
		     
		    } else if(type === 'group'){
		      //模拟系统消息
		      layim.getMessage({
		        system: true
		        ,id: res.data.id
		        ,type: "group"
		        ,content: '模拟群员'+(Math.random()*100|0) + '加入群聊'
		      });
		    }
		  });
		  
		  $(document).on("click",".layim-pop-chat",function(){
			  var memberId=$(this).attr("layim-pop-id");
			  popchat(memberId);
		  })
		  
		 // popchat("40280a25559b852e01559bb24f7d0008");
		  function popchat(memberId){
			  // 面板外的操作
			  $.ajax({
		    	     type:"post",
		    	     datatype:"json",
		    	     url:base_root+"/front/chat/getMemberInfo.do",
		    	     data:{id:memberId,lang:lang},
		    	     success:function(json){
		    	    	 //console.log(json);
		    	    	 layim.chat(json);
		    	    	 //layer.msg('也就是说，此人可以不在好友面板里');
		    	     }
		     })
		  }
		  
		  
		  
		});
	
	
	
	
})