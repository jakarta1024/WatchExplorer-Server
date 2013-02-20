<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    /* setup basePath */
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript">
        $(document).ready(function(){
        	WS.Template.initCloseButton('add_notice_section');
        })
    </script>
</head>
<body>
    <header>
        <div class="corners" style="width: 100%; height: 30px; margin: 4px;">
            <h1 style="color: #222;">发布新公告</h1>
        </div>
    </header>
    <div id="page">
        <div id="add_notice_section" class="horizontal_center"
            style="width: 620px; display: true">
            <section class="full width5">
                <div class="notification information png_bg">
                    <a href="#" class="close"><img src="resources/img/platform/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                    <div> 输入公告信息 </div>
                </div>
                <form id="addNotice" action="./platform/admin/addNotice" method="POST" enctype="multipart/form-data">
                    <p>
                        <label class="required" for="publication_title">标题:</label> <input
                            type="text" id="publication_title" class="text-input large-input" value="" name="publication_title" />
                    </p>
                    <p>
                        <label class="required" for="content">内容:</label>
                        <textarea id="content" class="text-input large-input" name="content"></textarea>
                    </p>
                    <p>
                    <div class="validator">
                        <label class="required" for="validator">验证码:</label><input
                            type="text" id="validator" value="" name="validator" class="text-input small-input"/> <img
                            id="AddNoticeValidator" src="randomCode.do?id=AddNoticeValidator">
                        <a href="javascript: void(0);" onclick="WS.reloadCode('AddNoticeValidator');">看不清</a>
                    </div>
                    </p>

                    <div class="horizontal_center" style="width: 150px;">
                        <p>
                            <input type="submit" id="add_watch_submit"
                                class="button" value="提交" />
                        </p>
                    </div>
                    <div class="clear">&nbsp;</div>
                </form>
            </section>
        </div>
    </div>
</body>
</html>