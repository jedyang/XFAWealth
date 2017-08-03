<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<html>
<head>
<title>ws示例</title>  
</head>
<body>
<script type="text/javascript" charset="UTF-8" src="<%=path%>/res/js/jquery.min.js"></script>
<script type="text/javascript" charset="UTF-8" src="<%=path%>/res/third/sockjs/sockjs-0.3.4.min.js"></script>
<script type="text/javascript">
	var websocket = null;
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws://localhost/wmes/webSocketServer.do");
	} 
	else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket("ws://localhost/wmes/webSocketServer.do");
	} 
	else {
		websocket = new SockJS("http://localhost/wmes/sockjs/webSocketServer.do");
	}
	websocket.onopen = onOpen;
	websocket.onmessage = onMessage;
	websocket.onerror = onError;
	websocket.onclose = onClose;
	      	
	function onOpen(openEvt) {
		//alert(openEvt.Data);
	}
	
	function onMessage(evt) {
		//alert(evt.data);
	}
	function onError() {}
	function onClose() {}
	
	function doSend() {
		if (websocket.readyState == websocket.OPEN) {  		
            var msg = document.getElementById("inputMsg").value;  
            websocket.send(msg);//调用后台handleTextMessage方法
            //alert("发送成功!");  
        } else {  
        	alert("连接失败!");  
        }  
	}
</script>
请输入：<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
<button onclick="doSend();">发送</button>
</body>
</html>