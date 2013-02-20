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
        $('#uploader').preview();
        WS.Template.initCloseButton('add_announce_section');
        $("#addAnnounce").validate({
            onkeyup: false,
            rules: {
                validator: {
                    required: true,
                    remote: WS.validateUrl+"?id=AddAnnounceValidator"
                }
            },
            messages: {
                validator: "验证码错误"
            },
            // the errorPlacement has to take the layout into account
            errorPlacement: function(error, element) {
                error.insertAfter(element.parent().find('label:first'));
            },
            // set new class to error-labels to indicate valid fields
            success: function(label) {
                // set &nbsp; as text for IE
                if(label[0].htmlFor === 'validator')label.html("&nbsp;").addClass("ok");
            }
        });
    });
    </script>
</head>
<body>
    <header>
        <div class="corners" style="width: 100%; height: 30px; margin: 4px;">
            <h1 style="color: #222;">发布新通告</h1>
        </div>
    </header>
    <div id="page">
        <div id="add_announce_section" class="horizontal_center"
            style="width: 620px; display: true">
            <section class="full width5">
                <div class="notification information png_bg">
                    <a href="#" class="close"><img src="resources/img/platform/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
			        <div> 输入通告信息 </div>
			    </div>
                <form id="addAnnounce" action="./platform/admin/addAnnounce" method="POST" enctype="multipart/form-data">
                    <fieldset>
                    <p>
                        <label class="required" for="publication_title">标题:</label> <input
                            type="text" id="publication_title" class="text-input large-input" value="" name="publication_title" />
                    </p>

                    <p>
                        <label class="required" for="subtitle">副标题:</label> <input
                            type="text" id="subtitle" class="text-input large-input" value="" name="subtitle" />
                    </p>

                    <p>
                        <label class="required" for="content">内容:</label>
                        <textarea id="content" class="text-input large-input" name="content"></textarea>
                    </p>

                    <p>
                        <label class="optional" for="external_url">外部链接:</label> <input
                            type="text" id="external_url" class="text-input large-input" value="" name="external_url" />
                    </p>
                    <p>
                        <label class="required" for="watchdesc">配图:</label>
                        <div id="uploader">
                        </div>
                    </p>
                    <p>
                    <div class="validator">
                        <label class="required" for="validator">验证码:</label> <input
                            type="text" id="validator" value="" name="validator"  class="text-input small-input"/> <img
                            id="AddAnnounceValidator" src="randomCode.do?id=AddAnnounceValidator">
                        <a href="javascript: void(0);" onclick="WS.reloadCode('AddAnnounceValidator');">看不清</a>
                    </div>
                    </p>

                    <div class="horizontal_center" style="width: 150px;">
                        <p>
                            <input type="submit" id="add_watch_submit"
                                class="button" value="提交" />
                        </p>
                    </div>
                    </fieldset>
                    <div class="clear">&nbsp;</div>
                </form>
            </section>
        </div>
    </div>
</body>
</html>