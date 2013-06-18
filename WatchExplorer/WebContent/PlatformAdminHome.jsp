<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="application.dao.*" %>
<%@ page import="application.model.*" %>
<%@ page language="java" pageEncoding="UTF-8"
    contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%
    /* setup basePath */
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    //Get admin inforamtion from cookie

	/*String adminName = (String) request.getSession().getAttribute("active_platform_admin_name");
    PlatformAdministratorDao adminDAO = new PlatformAdministratorDao();
    PlatformAdministrator admin = adminDAO.getAdminByName(adminName); */
	
    PlatformAdministrator admin = (PlatformAdministrator) request.getSession().getAttribute("active_platform_admin");
    String adminName = "untrust administrator";
    if (admin != null) {
    	adminName = admin.getLoginName();
    } else {
    	//raise some error message.
    	throw new Exception();
    }
   	
%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
<title>管理员首页</title>
<!--                       CSS                       -->
<link rel="stylesheet" href="./css/reset.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/platform.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/custom.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/invalid.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/dataTable.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/facebox.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/imagePreview.css" type="text/css" media="screen" />
<link rel="stylesheet" href="./css/jwysiwyg.css" type="text/css" />
<!--                       Javascripts                       -->
<script type="text/javascript" src="./script/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="./script/js/templates.js"></script>
<script type="text/javascript" src="./script/js/libs/facebox.js"></script>
<script type="text/javascript" src="./script/js/jquery/jquery.wysiwyg.min.js"></script>
<script type="text/javascript" src="./script/js/basic.js"></script>
<script type="text/javascript" src="./script/js/platformAdmin.js"></script>
<script type="text/javascript" src="./script/js/jquery/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="./script/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="./script/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="./script/js/libs/facebox.js"></script>
<script type="text/javascript" src="./script/js/image.preview.js"></script>
<script type="text/javascript" src="./script/js/jquery/jquery.validate_pack.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
    	WS.Template.initMenu('sidebar-wrapper');
    	WS.Template.initFaceBox('sidebar-wrapper');
    	$('.foot_wrapper').load('./Footer.html');
    	WS.platformManager = new WS.PlatformAdmin('main-content');
    	WS.platformManager.profile();
    	$('a[operation]').bind('click', function(){
    	    $('#sidebar a.current').removeClass('current');
    		$(this).addClass('current');
    	});
    	$('a[operation="'+WS.PlatformAdmin.STATES.PROFILE+'"]').bind('click', WS.platformManager.profile);
    	$('a[operation="'+WS.PlatformAdmin.STATES.APPROVING_STORES+'"]').bind('click', WS.platformManager.approvingStores);
    	$('a[operation="'+WS.PlatformAdmin.STATES.APPROVED_STORES+'"]').bind('click', WS.platformManager.approvedStores);
    	$('a[operation="'+WS.PlatformAdmin.STATES.PROCESSING_COMPLAINTS+'"]').bind('click', WS.platformManager.processingComplaints);
    	$('a[operation="'+WS.PlatformAdmin.STATES.PROCESSED_COMPLAINTS+'"]').bind('click', WS.platformManager.processedComplaints);
    	$('a[operation="'+WS.PlatformAdmin.STATES.NOTICES+'"]').bind('click', WS.platformManager.notices);
    	$('a[operation="'+WS.PlatformAdmin.STATES.ANNOUNCES+'"]').bind('click', WS.platformManager.announces);
    	$('a[operation="'+WS.PlatformAdmin.STATES.VISIT_STATISTIC+'"]').bind('click', WS.platformManager.visitStatistic);
    	$('a[operation="'+WS.PlatformAdmin.STATES.SYS_USER_AUTHORITY+'"]').bind('click', WS.platformManager.sysAuthority);
    	$('a[operation="'+WS.PlatformAdmin.STATES.COM_USER_AUTHORITY+'"]').bind('click', WS.platformManager.comAuthority);
    	$('a[operation="'+WS.PlatformAdmin.STATES.GENERAL_SETTINGS+'"]').bind('click', WS.platformManager.generalSettings);
    	$('a[operation="'+WS.PlatformAdmin.STATES.SECURITY_SETTINGS+'"]').bind('click', WS.platformManager.securiySettings);
    	$('a[operation="'+WS.PlatformAdmin.STATES.ADD_WATCH+'"]').bind('click', WS.platformManager.addWatch);
    	$('a[operation="'+WS.PlatformAdmin.STATES.LOG+'"]').bind('click', WS.platformManager.log);
    });
</script>
</head>
<body>
<div id="body-wrapper">
  <!-- Wrapper for the radial gradient background -->
  <div id="sidebar">
    <div id="sidebar-wrapper">
      <!-- Sidebar with logo and menu -->
      <h1 id="sidebar-title"><a href="#">名表秀</a></h1>
      <!-- Sidebar Profile links -->
      <div id="profile-links"> 你好, <a href="#" title="Edit your profile"><%=adminName %></a>, 你有<a href="#messages" title="Messages" rel="modal">3 条新消息</a><br />
        <br />
        <a href="#" title="Settings">设置</a> | <a href="platform/admin/logout" title="Sign Out">退出</a> </div>
      <ul id="main-nav">
        <!-- Accordion Menu -->
        <li> <a href="#" class="nav-top-item no-submenu current" operation="profile"> 概况 </a> </li>
        <li> <a href="#" class="nav-top-item"> 表店管理 </a>
          <ul>
            <li><a href="#"  operation="approving_stores">待审核</a></li>
            <li><a href="#"  operation="approved_stores">已上架</a></li>
          </ul>
        </li>
        <li> <a href="#" class="nav-top-item"> 投诉处理 </a>
          <ul>
            <li><a href="#" operation="processing_complaints"> 待处理 </a></li>
            <li><a href="#" operation="processed_complaints"> 已处理 </a></li>
          </ul>
        </li>
        <li> <a href="#" class="nav-top-item"> 发布 </a>
          <ul>
            <li><a href="javascript: void(0);" operation="notices">公告</a></li>
            <li><a href="javascript: void(0);" operation="announces">通告</a></li>
          </ul>
        </li>
        <li> <a href="#" class="nav-top-item"> 统计 </a>
          <ul>
            <li><a href="#" operation="visit_statistic">流量</a></li>
          </ul>
        </li>
        <li> <a href="#" class="nav-top-item" > 权限管理 </a>
          <ul>
            <li><a href="#" operation="sys_user_authority"> 系统用户权限 </a></li>
            <li><a href="#" operation="com_user_authority"> 普通用户权限 </a></li>
          </ul>
        </li>
        <li> <a href="#" class="nav-top-item"> 设置 </a>
          <ul>
            <li><a href="#" operation="general_settings">常规</a></li>
            <li><a href="#" operation="security_settings">安全</a></li>
          </ul>
        </li>
        <li> <a href="#" class="nav-top-item" operation="log"> 日志 </a> </li>
        <li> <a href="#" class="nav-top-item" operation="add_watch"> 添加新表 </a> </li>
      </ul>
    </div>
  </div>
  <!-- End #sidebar -->
    <div id="main-wrapper">
    <!-- Main Content Section with everything -->
        <noscript>
    <!-- Show a notification if the user has disabled javascript -->
		    <div class="notification error png_bg">
		        <div> 您的浏览器不支持Javascript，请 <a href="http://browsehappy.com/" title="Upgrade to a better browser">更新</a> 您的浏览器，或 <a href="http://www.google.com/support/bin/answer.py?answer=23852" title="Enable Javascript in your browser">允许</a> Javascript 运行
		        </div>
		    </div>
	    </noscript>
        <div id="main-content"></div>
    </div>
      <!-- End .content-box-content -->
    <!-- End Notifications -->
    <footer class='foot_wrapper'></footer>
</div>
</body>
</html>