<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%
String path = request.getContextPath();
%>
<%!String exceptionMsgForInner(Throwable e) {
	String ls_ErrMsg = e.getLocalizedMessage();
	if (null == ls_ErrMsg)
		ls_ErrMsg = "";
	ls_ErrMsg += "\r\n";
	Throwable eCause = e.getCause();
	if (null == eCause) {
		for (int ii = 0; ii < e.getStackTrace().length; ii++) {
			ls_ErrMsg += e.getStackTrace()[ii].toString() + "\r\n";
		}
	} else {
		ls_ErrMsg += exceptionMsgForInner(eCause);
	}
	return ls_ErrMsg.trim();
}%>
<%
	Exception ex;
	try{
		ex = (Exception) request.getAttribute("javax.servlet.error.exception");
	}catch(Exception e){
		ex = e;
	}
	String str = exceptionMsgForInner(ex);
	str = str == null ? "":str;
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>error</title>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<style>
	body{font-family:"微软雅黑";margin: 0;padding: 0; background-color: #c7eeff }
	.mainbg{background:url(<%=path%>/error/main-bg_2.jpg) no-repeat top ; width:1000px; height: 440px;  padding-top: 300px;  margin: auto}	
	.message1{width: 550px; color:#333333;font-size: 35px;font-weight: bold; margin-left:450px; }
	.btn{margin-top: 40px; margin-left: 490px; position: absolute }
	.bt1{ background: url(<%=path%>/error/btn-re.png) no-repeat; width: 162px; height: 60px;color:#FFFFFF;font-weight: bold;font-size:18px;text-decoration: none; text-align: center; line-height: 60px; display: inline-block;float:left;margin: 0 10px;	}
</style>
</head>
<script language="JavaScript">
<!--
	function dokeydown() {
		var obj = document.getElementById("divexception");
		if ("none" == obj.style.display.toLowerCase()) {
			obj.style.display = "block";
		} else {
			obj.style.display = "none";
		}
		return false;
	}
	
	function refreshPage() {
		window.location.reload();
	}
	
	function reback() {
		//location.replace(document.referrer);
		//document.referrer;
		history.go(-1);
	}
	
//-->
</script>
</head>
<body>
	<div class="mainbg">
		<div class="message1">系统在开小差哦~请稍后再试！</div>
			<div class="btn">
				<a href="javascript:void(0);" onclick="refreshPage()" class="bt1">刷新</a>
				<a href="javascript:void(0);" onclick="reback()" class="bt1">返回</a>
			</div>
	</div>
		<div style="display: none;" id=divexception>
			<textarea name="" rows="40" cols="100"><%=str%></textarea>
		<div>
</body>
</html>