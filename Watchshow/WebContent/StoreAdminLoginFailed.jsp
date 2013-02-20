<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	/**
	Set basepath
	 */
	String path = request.getContextPath();
	//TODO: Remove 'WebContent' in basePath
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";/*+"WebContent/";*/
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>登陆失败</title>
</head>
<body>
	<h1>登录失败！</h1>
	<%
		String redirectURL=basePath+"StoreWelcome.jsp";
	%>
	<a href="<%=redirectURL %>">返回登陆页面</a>
</body>
</html>