<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>聊天室示例</title>
<style type="text/css">
#connect-container {
	float: left;
	width: 400px
}

#connect-container div {
	padding: 5px;
}

#console-container {
	float: left;
	margin-left: 15px;
	width: 400px;
}

#console {
	border: 1px solid #CCCCCC;
	border-right-color: #999999;
	border-bottom-color: #999999;
	height: 170px;
	overflow-y: scroll;
	padding: 5px;
	width: 100%;
}

#console p {
	padding: 0;
	margin: 0;
}
</style>
<script type="text/javascript" charset="UTF-8" src="<%=path%>/res/js/jquery.min.js"></script>
<script type="text/javascript" charset="UTF-8" src="<%=path%>/res/third/sockjs/sockjs-0.3.4.min.js"></script>
<script type="text/javascript">
	var ws = null;
	var url = null;
	var transports = [];

	function setConnected(connected) {
		document.getElementById('connect').disabled = connected;
		document.getElementById('disconnect').disabled = !connected;
		document.getElementById('echo').disabled = !connected;
	}

	function connect() {
		if (!url) {
			alert('Select whether to use W3C WebSocket or SockJS');
			return;
		}

		//ws = (url.indexOf('sockjs') != -1) ?new SockJS(url, undefined, {protocols_whitelist: transports}) : new WebSocket(url);
		if ('WebSocket' in window) {
			ws = new WebSocket("ws://localhost/wmes/webSocketServer.do");
		} else {
			ws = new SockJS("http://localhost/wmes/sockjs/webSocketServer.do");
		}
		//websocket = new SockJS("http://localhost/wmes/SpringWebSocketPush/sockjs/websck");
		ws.onopen = function() {
			alert('open');
			setConnected(true);
			//log('Info: connection opened.');
		};
		ws.onmessage = function(event) {
			alert('Received:' + event.data);
			log('Received: ' + event.data);
		};
		ws.onclose = function(event) {
			setConnected(false);
			log('Info: connection closed.');
			log(event);
		};
	}

	function disconnect() {
		if (ws != null) {
			ws.close();
			ws = null;
		}
		setConnected(false);
	}

	function echo() {
		if (ws != null) {
			var message = document.getElementById('message').value;
			log('Sent: ' + message);
			ws.send(message);
		} else {
			alert('connection not established, please connect.');
		}
	}

	function updateUrl(urlPath) {
		if (urlPath.indexOf('sockjs') != -1) {
			url = urlPath;
			document.getElementById('sockJsTransportSelect').style.visibility = 'visible';
		} else {
			if (window.location.protocol == 'http:') {
				url = 'ws://' + window.location.host + urlPath;
			} else {
				url = 'wss://' + window.location.host + urlPath;
			}
			document.getElementById('sockJsTransportSelect').style.visibility = 'hidden';
		}
	}

	function updateTransport(transport) {
		alert(transport);
		transports = (transport == 'all') ? [] : [ transport ];
	}

	function log(message) {
		var console = document.getElementById('console');
		var p = document.createElement('p');
		p.style.wordWrap = 'break-word';
		p.appendChild(document.createTextNode(message));
		console.appendChild(p);
		while (console.childNodes.length > 25) {
			console.removeChild(console.firstChild);
		}
		console.scrollTop = console.scrollHeight;
	}
	
	function mylogin(){
		$.post('<%=path%>/chat/addUser.do', function(j) {
		},'json');
	}
	
</script>
</head>
<body>
<noscript>
	<h2 style="color: #ff0000">
		Seems your browser doesn't support Javascript! Websockets rely on
		Javascript being enabled. Please enable Javascript and reload this
		page!
	</h2>
</noscript>
<div>
	<div id="connect-container">
		<input type="button" value="登录" onclick="mylogin()"/><br>
		<input id="radio1" type="radio" name="group1" onclick="updateUrl('/wmes/webSocketServer.do');">
		<label for="radio1">W3C WebSocket</label>
		<br>
		<input id="radio2" type="radio" name="group1" onclick="updateUrl('/wmes/sockjs/webSocketServer.do');">
		<label for="radio2">SockJS</label>
		<div id="sockJsTransportSelect" style="visibility: hidden;">
			<span>SockJS transport:</span>
			<select onchange="updateTransport(this.value)">
				<option value="all">all</option>
				<option value="websocket">websocket</option>
				<option value="xhr-polling">xhr-polling</option>
				<option value="jsonp-polling">jsonp-polling</option>
				<option value="xhr-streaming">xhr-streaming</option>
				<option value="iframe-eventsource">iframe-eventsource</option>
				<option value="iframe-htmlfile">iframe-htmlfile</option>
			</select>
		</div>
		<div>
			<button id="connect" onclick="connect();">建议连接</button>
			<button id="disconnect" disabled="disabled" onclick="disconnect();">断开连接</button>
		</div>
		<div>
			<textarea id="message" style="width: 350px">这是一条测试消息!</textarea>
		</div>
		<div>
			<button id="echo" onclick="echo();" disabled="disabled">发送消息 </button>
		</div>
	</div>
	<div id="console-container">
		<div id="console"></div>
	</div>
</div>
</body>
</html>