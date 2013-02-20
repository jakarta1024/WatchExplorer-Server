<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.codehaus.jettison.json.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.watchshow.platform.domain.*"%>
<%@ page import="com.watchshow.platform.dao.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	/* setup basePath */
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String swid = request.getParameter("watchId");
	Long wid = new Long(-1);
	if (swid != null && swid != "") {
		wid = Long.parseLong(swid);
	}
	
	//get watch info
	WatchDao watchDAO = new WatchDao();
	Watch watch = watchDAO.get(wid);	
	String hostPath = request.getServletContext().getRealPath("/");
	System.out.println("hostpath = " + hostPath);
	List<String> imgPathset = watchDAO.getResourcePathWithKind(watch.getIdentifier(), WatchDao.SRC_KIND.IMAGE, hostPath);
	//for javascript use
	JSONObject jo = new JSONObject();
	JSONArray array = new JSONArray();
	for (String p : imgPathset) {
		java.net.URLEncoder.encode(p, "UTF-8");
		String url = "./dl?client=browser&path=" + p.replace('\\', '/');
		System.out.println(url);
		array.put(url);
	}
	jo.put("urls", array);
	System.out.println("json = " + jo);
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <script type="text/javascript">
        $(document).ready(function(){
        	var imageUrls = '<%=jo%>';
        	var urls = $.parseJSON(imageUrls.toString().replace(/\\/g, '\\\\')).urls;
        	$('#images_preview').preview({
        		defaults: urls,
        		upload: false
        	});
        });
    </script>
</head>
<body>
    <div id="page">
        <div id="register_store_section" class="horizontal_center"
            style="width: 620px; display: true">
            <section class="full width5">
                <div class="box box-info">详细信息</div>
                <p>
                    <label>表名:</label><br />
                    <strong><%=watch.getName() %></strong>
                </p>

                <p>
                    <%
                    	String brand = watch.getBrand().getEngName()+":"+watch.getBrand().getChnName();
                    %>
                    <strong><%=brand %></strong>
                </p>

                <p>
                    <label>当前折扣（%）:</label><br />
                    <% 
                    	Float d = watch.getDiscount().floatValue() * 100;
                    %>
                    <strong><%=d %></strong>
                </p>

                <p>
                    <label>简单描述:</label><br />
                    <strong><%=watch.getSimpleDescription() %></strong>
                </p>
                <p>
                    <label>详细介绍:</label><br />
                    <strong><%=watch.getDescription() %></strong>
                </p>

                <p>
                    <label>条形码:</label><br />
                    <strong><%=watch.getBarcode() %></strong>
                </p>
                <p>
                    <label>机芯:</label><br />
                    <strong><%=watch.getMovement() %></strong>
                </p>
                <p>
                    <label>材质:</label><br />
                    <strong><%=watch.getMaterial() %></strong>
                </p>
                <p>
                    <label>表盘尺寸:</label><br />
                    <strong><%=watch.getSize() %></strong>
                </p>
                <p>
                    <label>工艺:</label><br />
                    <strong><%=watch.getArchitecture() %></strong>
                </p>
                <p>
                    <label>表带材质:</label><br />
                    <strong><%=watch.getWatchband() %></strong>
                </p>
                <p>
                    <label>风格:</label><br />
                    <strong><%=watch.getStyle() %></strong>
                </p>
                <p>
                    <label>功能:</label><br />
                    <strong><%=watch.getFunction() %></strong>
                </p>
                
                <div id="images_preview"></div>
               
                <div class="clear">&nbsp;</div>
            </section>
        </div>
    </div>
</body>
</html>