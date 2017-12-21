<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>统计表</title>
	<reps:theme />
</head>
<body onload="load()">
	<reps:container layout="true">
		<reps:panel id="myleft" dock="left" title="统计列表" border="true" style="width:200px;overflow-y:scroll;overflow-x:auto;">
			<reps:tree id="mytree" items="${treelist}" var="li"
				cssClass="treeFolder" checkbox="false" expand="true">
				<reps:treenode parentKey="${li.tjTableDefineTypeId}" key="${li.id}">
					<a href="javascript:void(0);"
						onclick="showChilds(this,'${li.id}')" id="${li.id}">${li.chineseName}</a>
				</reps:treenode>
			</reps:tree>
		</reps:panel>
		<reps:panel id="myCenter" dock="center" border="false">
			<reps:iframe id="iframe" fit="true"></reps:iframe>
		</reps:panel>
	</reps:container>
</body>
<script>
	function load() {
	    var $iframe = $("#iframe");
	    $iframe.attr("src","list.mvc");
	}

	function showChilds(obj,id) {
		var level = $(obj).attr("level");
		if(level=='0'){
			document.getElementById("iframe").src = "show.mvc?id="+id;
		}else{
			var $iframe = $("#iframe");
		    $iframe.attr("src","list.mvc?isFixed="+id);
		}
	}
	
</script>
</html>

