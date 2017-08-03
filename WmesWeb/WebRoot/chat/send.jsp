<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>send</title>
<script type="text/javascript" charset="UTF-8" src="<%=path%>/res/js/jquery.min.js"></script>
<script type="text/javascript" charset="UTF-8" src="<%=path%>/res/third/sockjs/sockjs-0.3.4.min.js"></script>
<script type="text/javascript">
var websocket;
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://localhost/wmes/webSocketServer.do");
} else {
    websocket = new SockJS("http://localhost/wmes/sockjs/webSocketServer.do");
}

websocket.onopen = function (evnt) {
	//alert(evnt);
	//console.info(evnt);
};

websocket.onmessage = function (evnt) {
	//alert(evnt);
    $("#msgcount").html($("#msgcount").html()+"<br>(<font color='red'>"+evnt.data+"</font>)")
};

websocket.onerror = function (evnt) {
	
};

websocket.onclose = function (evnt) {
	
}

function sendMsg(){
	var msg = $("#msgtext").val();
	$.post('<%=path%>/chat/sendMsg.do', {msg:msg}, function(j) {
		/* if (j.success) {
			user_datagrid.datagrid('reload');
			dialog.dialog('destroy');
		}
		p.messager.show({
			title : '提示',
			msg : j.msg,
			timeout : 5000,
			showType : 'slide'
		}); */
	},'json');
}
</script>
</head>
<body>
<span id="msgcount"></span><br>
<textarea id="msgtext" style="width: 300px;height: 100px;" ></textarea><input type="button" value="发送" onclick="sendMsg()"/>
</body>
</html>
