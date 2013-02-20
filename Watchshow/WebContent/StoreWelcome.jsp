<!DOCTYPE>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ page import="com.watchshow.platform.domain.*"%>
<%
	/**
	Set basepath
	 */
	String path = request.getContextPath();
	//TODO: Remove 'WebContent' in basePath
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";/*+"WebContent/";*/
	StoreAdministrator admin = (StoreAdministrator) session.getAttribute("active_store_admin");
	if (admin != null) {
		request.getRequestDispatcher("StoreAdminHome.jsp").forward(request, response);
	} 
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content="名表秀" />
<meta name="keywords" content="Watch, Show" />
<meta name="viewport" content="width=device-width">
<base href="<%=basePath%>">
<title>名表秀 - 首页</title>
<!-- Bootstrap CSS Toolkit styles -->
<!-- Shim to make HTML5 elements usable in older Internet Explorer versions -->
<!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->

<!-- Favicons -->
<!-- <link rel="shortcut icon" type="image/png" HREF="img/favicons/favicon.png"/> -->
<!-- <link rel="icon" type="image/png" HREF="img/favicons/favicon.png"/> -->
<!-- <link rel="apple-touch-icon" HREF="img/favicons/apple.png" /> -->
<!-- Main Stylesheet -->
<link rel="stylesheet" type="text/css" href="./css/style.css"/>
<link rel="stylesheet" type="text/css" href="./css/jwysiwyg.css" />
<link rel="stylesheet" type="text/css" href="./css/imagePreview.css">
<link rel="stylesheet" type="text/css" href="./css/custom.css"/>
<!-- jQuery with plugins -->


<script type="text/javascript" src="./script/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="./script/js/jquery/jquery.wysiwyg.min.js"></script>
<!-- jQuery tooltips -->
<script type="text/javascript" src="./script/js/jquery/jquery.tipTip.min.js"></script>
<!-- Superfish navigation -->
<script type="text/javascript" src="./script/js/jquery/jquery.superfish.min.js"></script>
<script type="text/javascript" src="./script/js/jquery/jquery.supersubs.min.js"></script>
<!-- jQuery form validation -->
<script type="text/javascript" src="./script/js/jquery/jquery.validate.min.js"></script><!-- Third party libs -->
<script type="text/javascript" src="./script/js/libs/geoSelector.js"></script>
<script type="text/javascript" src="./script/js/image.preview.js"></script>
<!-- app -->
<script type="text/javascript" src="./script/js/basic.js"></script>
<script type="text/javascript" src="./script/js/templates.js"></script>
<script type="text/javascript" src="./script/js/index.js"></script>
 
</head>
<body>
	<!-- Header -->
	<header id="top">
	<div class="wrapper-login">
		<!-- Title/Logo - can use text instead of image -->
		<div id="title">
			<h3 class="header_title">名表秀 - 表店管理</h3>
		</div>
		<div>
			<a href="./PlatformAdminLogin.jsp">超级管理员入口</a>
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
	<!-- End of Header -->
	<!-- Page content -->
	<div id="page">
		<!-- Wrapper -->
		<div id="login_section" class="wrapper-login">
			<!-- Login form -->
			<section class="full">

			<h3>登录</h3>

			<div class="box box-info">输入表店管理员用户名密码</div>

			<form id="loginform" class="cmxform" action="store/admin/login" method="post">

				<p>
					<label class="required" for="storenumber">表店名:</label><br /> <input
						type="text" id="storenumber" class="full" value=""
						name="storenumber" />
				</p>

				<p>
					<label class="required" for="username">登录名:</label><br /> <input
						type="text" id="username" class="full" value="" name="username" />
				</p>

				<p>
					<label class="required" for="password">密&#160;&#160;码:</label><br />
					<input type="password" id="password" class="full" value=""
						name="password" />
				</p>

				<p>
				<div class="validator">
					<label class="required" for="validator">验证码:</label><br /> <input
						type="text" id="validator" value="" name="validator" /> <img
						id="loginValidator" src="randomCode.do?id=loginValidator"> <a
						href="javascript:WS.reloadCode('loginValidator');">看不清</a>
				</div>
				<p>
					<input type="checkbox" id="remember" class="" value="1"
						name="remember" /> <label class="choice" for="remember">记住我?</label>
				</p>

				<p>
					<input type="submit" class="btn btn-green big" value="提交" />
					&nbsp; <a href="javascript: //;"
						onClick="$('#emailform').slideDown(); return false;">忘记密码?</a> or
					<a href="./StoreAdminHome.jsp">需要帮助?</a>
				</p>
				<div class="clear">&nbsp;</div>

			</form>

			<form id="emailform" style="display: none" method="post" action="#">
				<div class="box">
					<p id="emailinput">
						<label for="email">Email:</label><br /> <input type="text"
							id="email" class="full" value="" name="email" />
					</p>
					<p>
						<input type="submit" class="btn" value="Send" />
					</p>
				</div>
			</form>

			</section>
			<!-- End of login form -->
		</div>
		<form id="register" action="store/register" method="POST" enctype="multipart/form-data">
			<div id="register_user_section" class="wrapper-login" style="display: none">
				<section class="full">
				<h3>注册</h3>

				<div class="box box-info">输入表店管理员信息</div>

				<p>
					<label class="required" for="founder">登录名:</label><br /> <input
						type="text" id="founder" class="full" value="" name="founder" />

				</p>

				<p>
					<label class="required" for="password">密&#160;&#160;码:</label><br />
					<input type="password" id="password" class="full" value=""
						name="password" />
				</p>

				<p>
					<label class="required" for="repassword">重复密码:</label><br />
					<input type="password" id="repassword" class="full" value=""
						name="repassword" />
				</p>

				<p>
					<label class="required" for="email">邮箱地址:</label><br /> <input
						type="email" id="email" class="full" value="" name="email" />
				</p>

				<div class="horizontal_center" style="width: 70px;">
					<p>
						<input id="to_register_page2" type="button"
							class="btn btn-green big" value="下一页" />
					</p>
				</div>
				<div class="clear">&nbsp;</div>
				</section>
			</div>
			<div id="register_store_section" class="horizontal_center" style="width: 600px; display: none">
				<section class="full width5">
				<h3>注册</h3>
				<div class="box box-info">输入表店信息</div>

				<p>
					<label class="required" for="storename">店名:</label><br /> <input
						type="text" id="storename" class="full" value="" name="storename" />
				</p>

				<p>
					<label class="required" for="brand">销售品牌:</label><br />
					<span>中文名: </span><br />
					<input type="text" id="cnbrand" class="full block" value="" name="cnbrand"/>
					<span>英文名: </span><br />
				    <input type="text" id="enbrand" class="full" value="" name="enbrand" />
				</p>

				<p>
					<label class="required" for="telephone">联系电话:</label><br /> <input
						type="text" id="telephone" class="full" value="" name="telephone" />
				</p>

				<p>
					<label class="required" for="address">省市区:</label><br /> <select
						id="province" name="province"></select> <select id="city"
						name="city"></select> <select id="district" name="district"></select>
				</p>

				<p>
					<label class="required" for="address">地址:</label><br /> <input
						type="text" id="address" class="full" value="" name="address" />
				</p>

				<p>
					<label class="required" for="postcode">邮编:</label><br /> <input
						type="text" id="postcode" class="full" value="" name="postcode" />
				</p>

				<p>
					<label class="required" for="fax">传真:</label><br /> <input
						type="text" id="fax" class="full" value="" name="fax" />
				</p>

				<p>
					<label class="required" for="website">网站:</label><br /> <input
						type="text" id="website" class="full" value="" name="website" />
				</p>

				<p>
					<label class="required" for="storedesc">文字描述:</label><br />
					<textarea id="storedesc" class="large full" name="storedesc"></textarea>
				</p>

                <p>
	                <label class="required" for="storedesc">图片介绍:</label>
					<div id="uploader"></div>
				</p>

				<div class="validator">
					<label class="required" for="validator">验证码:</label><br /> <input
						type="text" id="validator" value="" name="validator" /> <img
						id="registerValidator" src="randomCode.do?id=registerValidator"> <a
						href="javascript: void(0);" onclick="WS.reloadCode('registerValidator');">看不清</a>
				</div>

				<div class="clear">&nbsp;</div>				
				<div id="submit_action" class="horizontal_center" style="width: 450px; display: none;">
					<p>
						<input id="to_register_page1" type="button" class="btn btn-green big" value="上一页" />
						<input id="registersubmitbt" type="submit"  class="btn btn-green big" value="提交" />
					</p>
				</div>
				<div class="clear">&nbsp;</div>
				</section>
			</div>
		</form>
	</div>
	<!-- End of Wrapper -->
	<!-- End of Page content -->

	<!-- Page footer -->
	<footer id="bottom"> </footer>
	<!-- End of Page footer -->
</body>
</html>
