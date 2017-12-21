<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>报表数据维护</title>
	<reps:theme />
</head>
<body onload="load()">
	<reps:container layout="true">
		<reps:panel id="myleft" dock="left" title="报表列表" border="true" style="width:200px;overflow-y:scroll;overflow-x:auto;">
			<reps:tree id="mytree" items="${treelist}" var="li"
				cssClass="treeFolder" checkbox="false" expand="true">
				<reps:treenode parentKey="" key="">
					<a href="javascript:void(0);"
						onclick="showChilds('${li.id}')" id="${li.id}">${li.chineseName}</a>
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
	    $iframe.attr("src","right.mvc");
	}

	function showChilds(id) {
		document.getElementById("iframe").src = "list.mvc?did="+id;
	}
	
</script>
</html>

