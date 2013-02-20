<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<script type="text/javascript"
	src="script/js/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="debug/services.js"></script>
<link rel="stylesheet" type="text/css" href="debug/app-min.css">
<link rel="stylesheet" type="text/css" href="debug/jsoneditor-min.css">
<style type="text/css">
h1 {
	font-size: 2.4em;
	margin: 0;
	color: #FFF;
}
</style>
<script type="text/javascript" src="debug/jsoneditor-min.js"></script>
<script type="text/javascript" src="debug/app-min.js"></script>
<script type="text/javascript">
	$(document).ready(
			
			function() {
				initializeSelector("servicetype", "", "servicename", "");
				var webappContext = "<%=basePath %>";
				$('#post').click(
					function() {
						var url = webappContext + $('#servicetype').val() + $('#servicename').val();
						$('#all_url').html(url);
						$.post(url, $('#request_query').val(), function(data,	textStatus, jqXHR) {
							if (!!data) {
								$('#response_result').html(JSON.stringify(data, null, 4));
							}
						});
					});
				$('#get').click(
					function() {
						var parameters = $('#request_query').val();
						if (parameters !== "") {
							parameters = "?"+$('#request_query').val();
						} 
						var url = webappContext + $('#servicetype').val() + $('#servicename').val() + parameters;
						$('#all_url').html(url);
						$.get(url, function(result) {
							$("#response_result").html(JSON.stringify(result, null, 4));
						});
					});
				$('#validateJSON').click(function(){
	                var a = $('.jsonformatter-textarea')[0],
	                      h = JSONEditor.parse(a.value);
	                a.value = JSON.stringify(h, null, 4);
	            });
				$('#validateJSON2').click(function(){
	                var a = $('.jsonformatter-textarea')[1],
	                      h = JSONEditor.parse(a.value);
	                a.value = JSON.stringify(h, null, 4);
	            });
			});
</script>
<title>Webservice Validator Page</title>
</head>
<body>
	<div id="header">
		<h1>WatchShow Webservice Validator</h1>
	</div>
	<div class="content-container">
		<div class="jsoneditor-menu" id="ssselectors">
			Service URL Pattern: <select id="servicetype" name="servicetype"></select>
			Service Name: <select id="servicename" name="servicename"></select>
			<div style="float: right;">
			    
				<input type='button' id="post" value="POST">
				<input type='button' id="get" value="GET">
			</div>
		</div>
		<div class="jsoneditor-menu">
			<b>Request URL: </b>  <span id="all_url"></span>
		</div>
		<div class="jsonformatter-content" style="width: 100%; height:100%; overflow:auto">
						<!-- ----------------left--------------- -->
			<div class="jsoneditor-frame" style="float: left; width: 50%;">
				<div class="jsoneditor-menu">
					<h3>Client Request Data</h3>
					<input type="button" id="validateJSON" value="Validate JSON" class="jsoneditor-menu jsoneditor-format" style="float: right;">
				</div>
				<div class="jsonformatter-content">
					<textarea id="request_query" class="jsonformatter-textarea"></textarea>
				</div>
			</div>
						<!-- -----------------right-------------- -->
			<div class="jsoneditor-frame" style="float: right; width: 50%;">
				<div class="jsoneditor-menu">
					<h3>Service Response Data</h3>
					<input type="button" id="validateJSON2" value="Format JSON" class="jsoneditor-menu jsoneditor-format" style="float: right;">
				</div>
				<div class="jsonformatter-content">
					<textarea id="response_result" class="jsonformatter-textarea" readonly>
					</textarea>
				</div>
			</div>
		</div>
	</div>
			
</body>
</html>