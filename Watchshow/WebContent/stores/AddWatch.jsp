<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="application.dao.*"%>
<%@ page import="commons.util.*"%>
<%@ page import="application.model.*"%>
<%
	/* setup basePath */
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	StoreAdministrator admin = (StoreAdministrator) session.getAttribute("active_store_admin");
	Long adminId = admin.getIdentifier();
	System.out.println("adminId = "+adminId);
	Assert.isTrue(adminId!=null, "adminId cannot be null while this id will add watch to store!");
%>
<html>
<head>
	<base href="<%=basePath%>">
	<script type="text/javascript">
	$(document).ready(function(){
		$('#uploader').preview();
		$("#addWatch").validate({
	        onkeyup: false,
	        rules: {
	            validator: {
	                required: true,
	                remote: WS.validateUrl+"?id=AddWatchValidator"
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
		$('#marketTime').datepicker({
	        showOtherMonths: true,
	        selectOtherMonths: true,
	        dateFormat: 'yy/mm/dd'
	    });
	});
	</script>
</head>
<body>
    <header>
        <div class="corners" style="width: 100%; height: 30px; margin: 4px;">
            <h1>添加新表</h1>
        </div>
    </header>
	<div id="page">
		<div id="register_watch_section" class="horizontal_center"
			style="width: 620px; display: true">
			<section class="full width5">
				<div class="box box-info">输入名表信息</div>
				<form id="addWatch" action="store/admin/addWatch" method="POST" enctype="multipart/form-data">
					<p>
						<label class="required" for="watchname">表名:</label><br /> <input
							type="text" id="watchname" class="full" value="" name="watchname" />
					</p>

					<p>
						<!-- label class="required" for="brand">品牌:</label><br /> <input
							type="text" id="brand" class="full" value="" name="brand" /-->
					</p>
					<p>
						<label class="optional" for="price">价格:</label><br /> <input
							type="text" id="price" class="full" value="" name="price" />
					</p>
					<p>
						<label class="optional" for="discount">当前折扣（%）:</label><br /> <input
							type="text" id="discount" class="full" value="" name="discount" />
					</p>

					<p>
						<label class="optional" for="simpledesc">简单描述:</label><br /> <input
							type="text" id="simpledesc" class="full" value=""
							name="simpledesc" />
					</p>
					<p>
						<label class="required" for="desc">详细介绍:</label><br />
						<textarea id="desc" class="large full" name="desc"></textarea>
					</p>
					<p>
						<label class="optional" for="movement">机芯:</label><br /> <input
							type="text" id="movement" class="full" value="" name="movement" />
					</p>
					<p>
						<label class="optional" for="material">材质:</label><br /> <input
							type="text" id="material" class="full" value="" name="material" />
					</p>
					<p>
						<label class="optional" for="size">表盘尺寸:</label><br /> <input
							type="text" id="size" class="full" value="" name="size" />
					</p>
					<p>
						<label class="optional" for="architecture">工艺:</label><br /> <input
							type="text" id="architecture" class="full" value=""
							name="architecture" />
					</p>
					<p>
						<label class="optional" for="band">表带材质:</label><br /> <input
							type="text" id="band" class="full" value=""
							name="band" />
					</p>
					<p>
						<label class="optional" for="style">风格:</label><br /> <input
							type="text" id="style" class="full" value="" name="style" />
					</p>
					<p>
						<label class="optional" for="function">功能:</label><br /> <input
							type="text" id="function" class="full" value="" name="function" />
					</p>
					<p>
					 	<label class="optional" for="marketTime">上市时间:</label><br /> 
					 	<input type="text" id="marketTime" class="full" value="" name="marketTime" />
					</p>
					<p>
						<label class="required" for="watchdesc">图片:</label>
						<div id="uploader" > </div>
					</p>
					<p>
					<div class="validator">
						<label class="required" for="validator">验证码:</label><br /> <input
							type="text" id="validator" value="" name="validator" /> <img
							id="AddWatchValidator" src="randomCode.do?id=AddWatchValidator">
						<a href="javascript: void(0);" onclick="WS.reloadCode('AddWatchValidator');">看不清</a>
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