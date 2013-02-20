<%@page import="com.watchshow.common.util.ConstantsProvider"%>
<%@page import="java.text.SimpleDateFormat"%>
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
	String nid = request.getParameter("newsId");
	Long wid = new Long(-1);
	if (nid != null && !nid.isEmpty()) {
		wid = Long.parseLong(nid);
	}
	
	//get watch info
	PublicationDao dao = new PublicationDao();
	Publication news = dao.get(wid);
	String hostPath = request.getServletContext().getRealPath("/");
	System.out.println("hostpath = " + hostPath);
	List<String> imgPathset = dao.getResourcePathWithKind(wid, PublicationDao.SRC_KIND.IMAGE, hostPath);
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
                    <label></label><br />
                    <h1><%=news.getTitle() %></h1>
                </p>
                <p>
                    <label></label><br />
                    <h3><%=news.getSubtitle() %></h3>
                </p>

                <p>
                    <label></label><br />
                    <strong><%=news.getContent() %></strong>
                </p>

                <p>
                    <label></label><br />
                    <strong><%=news.getExternalURL() %></strong>
                </p>
                

                <p>
                    <label></label><br />
                    <%
                    StoreAdminHistoryDao sDAO = new StoreAdminHistoryDao();
                    StoreAdminHistory log = sDAO.getLastEditedHistoryForPublication(news);
                    SimpleDateFormat df = new SimpleDateFormat(ConstantsProvider.SIMPLE_DATE_FORMAT);
                    
                    %>
                    <strong><%=df.format(log.getTimestamp()) %></strong>
                </p>
                
                <div id="images_preview"></div>
               
                <div class="clear">&nbsp;</div>

            </section>
        </div>
    </div>
</body>
</html>