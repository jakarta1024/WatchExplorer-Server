<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
<!--
body {
	margin: 0;
	font-size: .7em;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	background: #EEEEEE;
}

fieldset {
	padding: 0 15px 10px 15px;
}

h1 {
	font-size: 2.4em;
	margin: 0;
	color: #FFF;
}

h2 {
	font-size: 1.7em;
	margin: 0;
	color: #CC0000;
}

h3 {
	font-size: 1.2em;
	margin: 10px 0 0 0;
	color: #000000;
}

#header {
	width: 96%;
	margin: 0 0 0 0;
	padding: 6px 2% 6px 2%;
	font-family: "trebuchet MS", Verdana, sans-serif;
	color: #FFF;
	background-color: #555555;
}

#content {
	margin: 0 0 0 2%;
	position: relative;
}

.content-container {
	background: #FFF;
	width: 96%;
	margin-top: 8px;
	padding: 10px;
	position: relative;
}
-->
</style>
<title>服务器内部错误</title>
</head>
<body>
	<div id="header">
		<h1>服务器错误</h1>
	</div>
	<div id="content">
		<div class="content-container">
			<fieldset>
				<h2>错误：</h2>
				<pre><%=exception%></pre>
				<h3>错误内容：</h3>
				<pre>
					<%
						exception.printStackTrace(response.getWriter());
					%>
				</pre>
			</fieldset>
		</div>
	</div>

</body>
</html>
