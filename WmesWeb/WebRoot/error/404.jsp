<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="chrome=1,IE=edge" />
	<title>404</title>
	<style>
		html {
			height:100%;
		}
		body {
			background-color:#FFFFFF;
			margin:0;
			height:100%;
		}
	</style>
	<!-- copy these lines to your document head: -->
	<meta name="viewport" content="user-scalable=yes, width=1000" />
	<!-- end copy -->
  </head>
  <body>
	<!-- copy these lines to your document: -->
	<div id="404_hype_container" style="margin:auto;position:relative;width:1000px;height:540px;overflow:hidden;">
		<script type="text/javascript" charset="utf-8" src="<%=path %>/error/404.js"></script>
	</div>
	<!-- end copy -->
	<!-- text content for search engines: -->
	<div style="display:none" aria-hidden=true>
		<div>Page not found.</div>
		<div>页面不存在</div>
	</div>
	<!-- end text content: -->
  </body>
</html>