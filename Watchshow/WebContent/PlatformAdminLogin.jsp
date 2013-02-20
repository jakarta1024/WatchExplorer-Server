<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" isELIgnored="false"%>

<%
    String path = request.getContextPath();
    //TODO: Remove 'WebContent' in basePath
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";/*+"WebContent/";*/    
%>
<html>
<head>
	<base href="<%=basePath %>"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="description" content="Watch Show" />
	<meta name="keywords" content="Watch, Show" />
	<title>名表秀系统管理员登陆</title>
<link rel="stylesheet" href="./css/reset.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/platform.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/invalid.css" type="text/css" media="screen" />
<!-- jQuery -->
<script type="text/javascript" src="./script/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="./script/js/jquery/jquery.tipTip.min.js"></script>
<script type="text/javascript" src="./script/js/jquery/jquery.validate_pack.js"></script>
<script type="text/javascript" src="./script/js/basic.js"></script>
<script type="text/javascript">
        $(document).ready(function(){    
            // validate signup form on keyup and submit
            var validator = $("#loginform").validate({
                rules: {
                    username: "required",
                    password: "required",
                    validator: {
                    	required: true,
                    	remote: WS.validateUrl+"?id=ploginValidator"
                    }
                },
                messages: {
                    username: "Enter your username",
                    password: "Provide your password"
                },
                // the errorPlacement has to take the layout into account
                errorPlacement: function(error, element) {
                    error.insertAfter(element.parent().find('label:first'));
                },
                // set new class to error-labels to indicate valid fields
                success: function(label) {
                    // set &nbsp; as text for IE
                    label.html("&nbsp;").addClass("ok");
                }
            });
        });
        </script>
</head>
<body id="login">
<div id="login-wrapper">
  <div id="login-top"><h1>名表秀系统管理员登陆</h1></div>
  <div id="login-content">
    <form id="loginform" action="./platform/admin/login" method="POST">
      <div class="notification information">
          <div> 输入用户名密码以登录 </div>
      </div>
      <p>
        <label class="required" for="username">用户名</label><br/>
        <input class="text-input" type="text" id="username" name="username" />
      </p>
      <div class="clear"></div>
      <p>
        <label class="required" for="password">密&#160;&#160;码:</label><br/>
        <input class="text-input" type="password" id="password" name="password" />
      </p>
      <div class="clear"></div>
      <p>
          <label class="required" for="validator">验证码:</label><br/>
          <input type="text" id="validator" class="text-input validator" name="validator"/>
          <img id="platformValidator" src="randomCode.do?id=ploginValidator"><a href="javascript:WS.reloadCode('platformValidator');">看不清</a>
      </p>
      <div class="clear"></div>
      <p>
        <input class="button" type="submit" value="提交" />
      </p>
    </form>
  </div>
  <!-- End #login-content -->
</div>
<!-- End #login-wrapper -->
</body>
</html>

