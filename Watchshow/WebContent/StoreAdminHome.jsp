<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"
    contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ page import="com.watchshow.platform.*" %>
<%@ page import="com.watchshow.common.util.*"%>
<%@ page import="com.watchshow.platform.dao.*"%>
<%@ page import="com.watchshow.platform.domain.*"%>
<%@ page import="java.util.*;"%>
<%
    /* setup basePath */
    System.out.print("recieved get request!");
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    StoreAdministrator admin = (StoreAdministrator) session.getAttribute("active_store_admin");
    if (admin == null) {
    	request.getRequestDispatcher("StoreWelcome.jsp").forward(request, response);
    } 
    String adminName = admin.getLoginName();
    Long admId = admin.getIdentifier();
    Long storeId = admin.getStore().getIdentifier();
    if (adminName == null || adminName == "") {
        adminName = "error_user";
    }    
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath %>"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <title>管理员首页</title>
    <link rel="stylesheet" href="./css/style.css" type="text/css" media="all" />
    <link rel="stylesheet" href="./css/custom.css" type="text/css" />
    <link rel="stylesheet" href="./css/dataTable.css" type="text/css" media="all" />
    <link rel="stylesheet" href="./css/jwysiwyg.css" type="text/css" />
    <link rel="stylesheet" href="./css/facebox.css" type="text/css" />
    <link rel="stylesheet" href="./css/imagePreview.css" type="text/css" />
    <link rel="stylesheet" href="./css/jquery-ui.css" type="text/css" />
    <!-- jQuery with plugins -->
    <script type="text/javascript" src="./script/js/jquery/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="./script/js/jquery/jquery-ui.js"></script>
	<script type="text/javascript" src="./script/js/jquery/jquery.wysiwyg.min.js"></script>
    <script type="text/javascript" src="./script/js/jquery/jquery.tipTip.min.js"></script>
    <script type="text/javascript" src="./script/js/jquery/jquery.superfish.min.js"></script>
    <script type="text/javascript" src="./script/js/jquery/jquery.supersubs.min.js"></script>
    <script type="text/javascript" src="./script/js/jquery/jquery.validate_pack.js"></script>
    <script type="text/javascript" src="./script/js/libs/facebox.js"></script>
    <script type="text/javascript" src="./script/js/jquery/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="./script/js/basic.js"></script>
    <script type="text/javascript" src="./script/js/templates.js"></script>
    <script type="text/javascript" src="./script/js/image.preview.js"></script>
    <script language="JavaScript" type="text/javascript">
    $(document).ready(function(){
    	WS.setup();
        $('#bottom').load("./Footer.html");
        WS.Template.initFaceBox('topnav');
        WS.StoreAdminId = <%=admId%>;
        var url = document.URL;
        switch (url.substring(url.lastIndexOf('#')+1)) {
        case 'news':
        	loadNews();
        break;
        case 'logs':
        	loadLogs();
       	break;
        case 'pubs':
        	loadPubs();
       	break;
        default :
        	loadWatches();
        break;
        }
        
        $('#watches').click(loadWatches);
        $('#publication').click(loadPubs);
        $('#news').click(loadNews);
        $('#logs').click(loadLogs);
        
        function loadWatches () {
        	changeCurrent($('#watches'));
        	$('#container').load('./stores/StoreWatches.html');
        }
        
        function loadNews () {
            changeCurrent($('#news'));
            $('#container').load('./stores/StoreNews.html');
        }
        
        function loadPubs () {
            changeCurrent($('#publication'));
            $('#container').load('./stores/StorePublication.html');
        }
        
        function loadLogs () {
            changeCurrent($('#logs'));
            $('#container').load('./stores/StoreLogs.jsp?storeId='+<%=storeId %>);
        }
        
        function changeCurrent(dom){
            $('#menu li.current').removeClass('current');
            $(dom).parent().addClass('current');
        }
    });
    </script>
</head>
<body>
<!-- Header -->
<header id="top">
    <div class="wrapper">
        <!-- Title/Logo - can use text instead of image -->
        <div class="title_wrapper">
            <h3 class="header_title">名表秀 - 管理页</h3>
        </div>
        <!-- Top navigation -->
        <div id="topnav">
            <a href="#"><img class="avatar" src="./resources/img/user_32.png" alt="" /></a>
                                          欢迎 <b><%=adminName %></b>
            <span>|</span> <a href="./StoreAdminProfile.jsp" rel="modal">设置</a>
            <span>|</span> <a href="./store/admin/logout">注销</a><br />
            <small>你有 <a href="#" class="high"><b>0</b> 新信息!</a></small>
        </div>
        <nav id="menu">
            <ul class="sf-menu">
                <li class="current"><a href="#watches" id="watches">名表一览</a></li>
                <li><a href="#pubs" id="publication">通告</a></li>
                <li><a href="#news" id="news">新闻</a></li>
                <li><a href="#logs" id="logs">日志</a></li> 
            </ul>
        </nav>
        <!-- End of Top navigation -->
    </div>
</header>
<!-- End Header -->
<div id="page"><div id="container" class="wrapper">
</div></div>
<!-- Footer -->
<footer id="bottom"></footer>
<footer id="animated">
    <ul>
        <li><a href="#">名表一览</a></li>
        <li><a href="#">通告</a></li>
        <li><a href="#">新闻</a></li>
        <li><a href="#">日志</a></li> 
    </ul>
</footer>
<!-- End Footer -->
<!-- Scroll to top link -->
<a href="#" id="totop">^ 回到顶部</a>
</body>
</html>