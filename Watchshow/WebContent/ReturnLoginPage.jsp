<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
    System.out.print("recieved get request!");
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String storeIds = null;
    String username = null;
    String loginpassword = null;
    storeIds = request.getParameter("sid");
    username = request.getParameter("user"); //This would not be required due to database constraints.
    loginpassword = request.getParameter("password");
    boolean savedFiles = request.getParameter("saved").equals("true");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册信息</title>
</head>
<body>
	<h1>您的表店申请信息：</h1>
		<pre>
			<p>表店登录号：<%=storeIds %></p>
			<p>管理员登录名：<%=username %></p>
			<p>管理员密码：<%=loginpassword %></p>
		</pre>
		
	
	<h1>上传图片返回结果：</h1>
		<pre>
		<%
			if (savedFiles == false) {
				%>
				<p>上传图片失败</p>
				<%
			} else {
				%>
				<p>图片上传成功</p>
				<%
			}
		%>
		</pre>
	
	<%
		String redirectURL=basePath+"StoreWelcome.jsp";
	%>
	<a href="<%=redirectURL %>">返回登陆页面</a>
</body>
</html>