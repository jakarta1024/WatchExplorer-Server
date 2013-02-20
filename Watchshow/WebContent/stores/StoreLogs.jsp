<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String storeId = request.getParameter("storeId");
%>

<html>
<head>
<script type="text/javascript">
$(document).ready(function(){
    $('#log_list_1').dataTable( {
        "bProcessing": false,
        "sAjaxSource": "./store/admin/getAdminLogList?storeId=" + "<%=storeId%>"
    } );
    
    $('#log_list_2').dataTable( {
        "bProcessing": false,
        "sAjaxSource": "./store/admin/getWatchList",
        "aoColumnDefs": [
            {
                "mRender": function (data, type, row) {
                    return '<input type="checkbox" value="false" /> ';
                },
                "bSortable": false,
                "bSearchable": false,
                "aTargets": [0]
            },{
                "mRender": function (data, type, row) {
                    return '<a href="./WatchPreview.jsp?wid=' + row[0] + '" rel="modal">' + data + '</a>';
                },
                "aTargets": [1]
            }, {
                "mRender": function (data, type, row) {
                    return '<a href="./WatchPreview.jsp" rel="modal">' + data + '</a>';
                },
                "aTargets": [3]
            }, {
                "mRender": function (data, type, row) {
                    return '<a href="store/admin/deleteWatch?watchId=' + row[0] + '" class="ico del">删除</a><a href="store/admin/home?ctrl=updatewatch&wid=' + row[0] + '" class="ico edit">编辑</a>';
                },
                "bSortable": false,
                "bSearchable": false,
                "aTargets": [4]
            }
                         ],
            "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            	$('a[rel*="modal"]', nRow).facebox();
            },
    } );
    
    $('#log_list_3').dataTable( {
        "bProcessing": false,
        "sAjaxSource": "./store/admin/getWatchList",
        "aoColumnDefs": [
            {
                "mRender": function (data, type, row) {
                    return '<input type="checkbox" value="false" /> ';
                },
                "bSortable": false,
                "bSearchable": false,
                "aTargets": [0]
            },{
                "mRender": function (data, type, row) {
                    return '<a href="./WatchPreview.jsp?wid=' + row[0] + '" rel="modal">' + data + '</a>';
                },
                "aTargets": [1]
            }, {
                "mRender": function (data, type, row) {
                    return '<a href="./WatchPreview.jsp" rel="modal">' + data + '</a>';
                },
                "aTargets": [3]
            }, {
                "mRender": function (data, type, row) {
                    return '<a href="store/admin/deleteWatch?watchId=' + row[0] + '" class="ico del">Âà†Èô§</a><a href="store/admin/home?ctrl=updatewatch&wid=' + row[0] + '" class="ico edit">ÁºñËæë</a>';
                },
                "bSortable": false,
                "bSearchable": false,
                "aTargets": [4]
            }
                         ],
            "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            	$('a[rel*="modal"]', nRow).facebox();
            },
    } );
    $(function() {
        $('a[rel*="modal"]').facebox();
    });
    $('#add_publication').attr('href', './stores/AddPublication.jsp?aid=' + WS.StoreAdminId);
});
</script>
</head>
<body>
    <div class="clear">&nbsp;</div>
    <div>
        <h1>管理员操作日志</h1>
    </div>
    <table cellpadding="0" cellspacing="0" border="0" class="display" id="log_list_1">
        <thead>
            <tr>
                <th width="10">管理员</th>
                <th width="73%">日志内容</th>
                <th width="17%">时间</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td colspan="5" class="dataTables_empty">暂无日志...</td>
            </tr>
        </tbody>
        <tfoot>
            <tr>
                <th>管理员</th>
                <th>日志内容</th>
                <th>时间</th>
            </tr>
        </tfoot>
    </table>
    
    <div class="clear">&nbsp;</div>
    <!-- table cellpadding="0" cellspacing="0" border="0" class="display" id="log_list_2">
        <thead>
            <tr>
                <th width="5%"><input type="checkbox" value="false" /></th>
                <th width="30%">ÈÄöÂëäÊ†áÈ¢ò</th>
                <th width="30%">ÂèëÂ∏ÉÊó∂Èó¥</th>
                <th width="20%">ÂèëÂ∏ÉËÄÖ</th>
                <th width="15%">Êìç‰Ωú</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td colspan="5" class="dataTables_empty">Ê≠£Âú®‰ªéÊúçÂä°Âô®Ëé∑ÂèñÊï∞ÊçÆ...</td>
            </tr>
        </tbody>
        <tfoot>
            <tr>
                <th><input type="checkbox" value="false" style="margin: 0px"/></th>
                <th>ÈÄöÂëäÊ†áÈ¢ò</th>
                <th>ÂèëÂ∏ÉÊó∂Èó¥</th>
                <th>ÂèëÂ∏ÉËÄÖ</th>
                <th>Êìç‰Ωú</th>
            </tr>
        </tfoot>
    </table -->
    
    <div class="clear">&nbsp;</div>

    <!-- table cellpadding="0" cellspacing="0" border="0" class="display" id="log_list_3">
        <thead>
            <tr>
                <th width="5%"><input type="checkbox" value="false" /></th>
                <th width="30%">ÈÄöÂëäÊ†áÈ¢ò</th>
                <th width="30%">ÂèëÂ∏ÉÊó∂Èó¥</th>
                <th width="20%">ÂèëÂ∏ÉËÄÖ</th>
                <th width="15%">Êìç‰Ωú</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td colspan="5" class="dataTables_empty">Ê≠£Âú®‰ªéÊúçÂä°Âô®Ëé∑ÂèñÊï∞ÊçÆ...</td>
            </tr>
        </tbody>
        <tfoot>
            <tr>
                <th><input type="checkbox" value="false" style="margin: 0px"/></th>
                <th>ÈÄöÂëäÊ†áÈ¢ò</th>
                <th>ÂèëÂ∏ÉÊó∂Èó¥</th>
                <th>ÂèëÂ∏ÉËÄÖ</th>
                <th>Êìç‰Ωú</th>
            </tr>
        </tfoot>
    </table -->
    <div class="clear">&nbsp;</div>
    <!-- <a href="#" class="btn btn-green" rel="modal" style="float: right; margin: 4px" id="add_publication"><img src="resources/img/add.png"
                        class="icon" alt=""/>Ê∑ªÂä†</a>
    <a href="#" class="btn btn-green" style="float: right; margin: 4px"><img src="resources/img/add.png"
                        class="icon" alt="" />Âà†Èô§</a> -->
</body>
</html>