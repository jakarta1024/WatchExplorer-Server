var sk = ["Mobile","Platform", "Store"];
var skpattern = ["mobileuser/services/", "platform/admin/", "store/admin/"];
var services = [
                ["registerMobileUser","login","logout","addComment","getComments", "getComment", "getNewsList","getNewsItem","getWatchList","getWatch"],
                ["getWatchList","getWatch","login","logout", "addBulletin", "getBulletins", "getBulletinItem", "getStores"],
                ["getWatchList","getWatch","XXXX","XXXX"]
               ];

function initializeSelector(type, typeval, name, nameval) {
	//定义默认数据
	var ar = ["Service Pattern","Service Name"];
	var pindex=0;
	//初始化
	$("<option value=''>"+ar[0]+"</option>").appendTo($("#"+type));
	$("<option value=''>"+ar[1]+"</option>").appendTo($("#"+name));
	
	//初始化type
	for (var i=0;i<sk.length;i++){
		if (skpattern[i]==typeval){
			pindex = i;
			$("<option selected>"+skpattern[i]+"</option>").appendTo($("#"+type));
		}else{
			$("<option>"+skpattern[i]+"</option>").appendTo($("#"+type));
		}
	}
	
	
	if (pindex!=0){
		for (var n=1;n<services[pindex].length+1;n++){
			if (services[pindex][n-1]==nameval){
				cindex = n;
				$("<option selected>"+services[pindex][n-1]+"</option>").appendTo($("#"+name));
			}else{						
				$("<option>"+services[pindex][n-1]+"</option>").appendTo($("#"+name));
			}			
		}		
	}
			
	//响应obj_1的change事件	
	$("#"+type).change(function(){
		//获取索引
		pindex = $("#"+type).get(0).selectedIndex;
		//清空c和h
		$("#"+name).empty();
		//重新给c填充内容
		$("<option>"+ar[1]+"</option>").appendTo($("#"+name));
		if (pindex!=0){
			for (var k=0;k<services[pindex-1].length;k++){
				$("<option>"+services[pindex-1][k]+"</option>").appendTo($("#"+name));
			}
		}
	});
}

