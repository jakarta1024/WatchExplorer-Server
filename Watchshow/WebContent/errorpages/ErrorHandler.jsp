<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误</title>
</head>
<body>
	错误码：<%=request.getAttribute("javax.servlet.error.status_code") %> <br>
	错误：<%=request.getAttribute("javax.servlet.error.message") %> <br>
	异常：<%=request.getAttribute("javax.servlet.error.exception_type") %> <br>
</body>
</html>