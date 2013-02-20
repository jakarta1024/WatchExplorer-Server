<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%
	/* setup basePath */
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String adminName = "Administrator";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
<title>管理员<%=adminName%>首页
</title>
<link rel="stylesheet" href="./css/style.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/elastislide.css" />
<!-- Custom Stylesheet -->
<link rel="stylesheet" href="./css/custom.css" type="text/css" />
<!-- jQuery with plugins -->
<script type="text/javascript"
	src="./script/js/libs/modernizr.custom.17475.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquery.ui.core.min.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquery.ui.widget.min.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquery.ui.tabs.min.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquery.wysiwyg.min.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquery.tagInput.min.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquerypp.custom.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquery.elastislide.js"></script>
<!-- jQuery tooltips -->
<script type="text/javascript"
	src="./script/js/jquery/jquery.tipTip.min.js"></script>
<!-- Superfish navigation -->
<script type="text/javascript"
	src="./script/js/jquery/jquery.superfish.min.js"></script>
<script type="text/javascript"
	src="./script/js/jquery/jquery.supersubs.min.js"></script>
<!-- jQuery form validation -->
<script type="text/javascript"
	src="./script/js/jquery/jquery.validate_pack.js"></script>
<!-- jQuery popup box -->
<script type="text/javascript"
	src="./script/js/jquery/jquery.nyroModal.pack.js"></script>
<!-- Third party libs -->
<script type="text/javascript" src="./script/js/libs/administry.js"></script>
<script type="text/javascript" src="./script/js/libs/geoSelector.js"></script>
<!--swfobject - needed only if you require <video> tag support for older browsers -->
<script type="text/javascript" src="./script/js/libs/swfobject.js"></script>
<!-- app -->
<script type="text/javascript" src="./script/js/basic.js"></script>
<script type="text/javascript" src="./script/js/index.js"></script>
<script type="text/javascript" src="./script/js/imagePreview.js"></script>

</head>
<body>
	<!-- Header -->
	<header id="top">
	<div class="wrapper-login">
		<!-- Title/Logo - can use text instead of image -->
		<div id="title">
			<h3 class="header_title">名表秀 - 表店管理</h3>
		</div>
		<!-- Main navigation -->
		<nav id="menu">
		<ul class="sf-menu">
			<li id="menu_login" class="current"><a href="javascript: //;"
				onclick="login()">登录</a></li>
			<li id="menu_register"><a href="javascript: //;"
				onclick="register()">申请</a></li>
		</ul>
		</nav>
		<!-- End of Main navigation -->
	</div>
	</header>
	<!-- Page footer -->
	<footer id="bottom">
	<div class="wrapper-login">
		<p>
			&copy; 2012 <b><a href="#" title="Watchshow">Watchshow.com</a></b>
		</p>
	</div>
	</footer>
	<!-- End of Page footer -->
	<!-- End Footer -->
</body>
</html>