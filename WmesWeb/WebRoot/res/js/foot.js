define(function(require) {
	//依赖
	var $ = require('jquery');
	
	var foot={};
	
	/*require.async('chat',function(chat){
		//console.log(chat);
		chat.init();
		
		$(".overall-chat-speack").on('click',function(){
			chat.show();
		})
		
	});*/
	
	$(".overall-chat-msg").on('click',function(){
		window.location.href=base_root+"/front/ifa/info/discover.do?type=Messages";
	})
	
	foot.refreshChatCount=function(count){
		var total=Number(count);
		if(total>0){
			$(".chat-number").css("display","block");
			$(".chat-number").text(total);
		}else{
			$(".chat-number").css("display","none");
		}
		
	}
	foot.refreshMsgCount=function(count){
		getUnreadMsgCount();
	}
	getUnreadMsgCount();
	function getUnreadMsgCount(){
		$.ajax({
			type:"post",
			datatype:"json",
			url:base_root+"/front/ifa/info/getAllUnreadCount.do",
			data:{},
			success:function(json){
				var count=json.count;
				if(count>0){
					$(".msg-number").css("display","block");
					$(".msg-number").text(count);
				}else{
					$(".msg-number").css("display","none");
					
				}
				
			}
		})
	}
	return foot;
});