<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.watchshow.platform.dao.*"%>
<%@ page import="com.watchshow.common.util.*"%>
<%@ page import="com.watchshow.platform.domain.*"%>
<%
	/* setup basePath */
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String adminId = request.getParameter("aid");
	System.out.println("adminId = "+adminId);
	Assert.isTrue(adminId!=null, "adminId cannot be null while this id will add watch to store!");
%>
<html>
<head>
	<base href="<%=basePath%>">
	<script type="text/javascript">
	$(document).ready(function(){
		$('#uploader').preview();
		$("#addPublication").validate({
	        onkeyup: false,
	        rules: {
	            validator: {
	                required: true,
	                remote: WS.validateUrl+"?id=AddPublicationValidator"
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
            <h1>发布新通告</h1>
        </div>
    </header>
	<div id="page">
		<div id="register_store_section" class="horizontal_center"
			style="width: 620px; display: true">
			<section class="full width5">
				<div class="box box-info">输入通告信息</div>
				<form id="addPublication" action="store/admin/addAnnouncement" method="POST" enctype="multipart/form-data">
					<p>
						<label class="required" for="publication_title">标题:</label><br /> <input
							type="text" id="publication_title" class="full" value="" name="publication_title" />
					</p>

					<p>
						<label class="required" for="subtitle">副标题:</label><br /> <input
							type="text" id="subtitle" class="full" value="" name="subtitle" />
					</p>

					<p>
						<label class="required" for="content">内容:</label><br />
						<textarea id="content" class="large full" name="content"></textarea>
					</p>

					<p>
						<label class="optional" for="external_url">外部链接:</label><br /> <input
							type="text" id="external_url" class="full" value="" name="external_url" />
					</p>
					<p>
						<label class="required" for="watchdesc">配图:</label>
						<div id="uploader">
						</div>
					</p>
					<p>
					<div class="validator">
						<label class="required" for="validator">验证码:</label><br /> <input
							type="text" id="validator" value="" name="validator" /> <img
							id="AddPublicationValidator" src="randomCode.do?id=AddPublicationValidator">
						<a href="javascript: void(0);" onclick="WS.reloadCode('AddPublicationValidator');">看不清</a>
					</div>
					</p>

					<div class="horizontal_center" style="width: 150px;">
						<p>
							<input type="submit" id="add_watch_submit"
								class="btn btn-green big" value="提交" />
						</p>
					</div>
					<div class="clear">&nbsp;</div>
				</form>
			</section>
		</div>
	</div>
</body>
</html>