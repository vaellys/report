<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>指标统计</title>
	<reps:theme />
	<style>
	.panel-header {
	    background-color: #ECF2F3;
	    width: 400px;
	}
		
	</style>
	
</head>
<body onload="load()">
	<reps:container layout="true" >
		<reps:panel id="myleft" dock="left" title="指标统计" border="true" style="width:250px;overflow:scroll;background:#fff;">
			<reps:tree id="mytree" items="${treelist}" var="li" cssClass="" checkbox="false" expand="false">
				<reps:treenode parentKey="${li.parentId}" key="${li.id}">
					<a href="javascript:void(0);"
						onclick="showChilds(this, '${li.id}', '${li.type}')"  id="${li.id}" title="${li.name}">${li.name}</a>
				</reps:treenode>
			</reps:tree>
		</reps:panel>
		<reps:panel id="myCenter" dock="center" border="false">
			<reps:iframe id="iframe" fit="true" ></reps:iframe>
		</reps:panel>
	</reps:container>
</body>
<script>
	function load() {
	    var $iframe = $("#iframe");
	    $iframe.attr("src","${ctx}/reps/report/zbztmcdyb/list.mvc");
	}

	function showChilds(obj, id, type) {
		if(type == "1"){//命名空间
			document.getElementById("iframe").src = "${ctx}/reps/report/zbztmcdyb/list.mvc?tjNamespace.namespace="+id;
		}else if(type == "2"){ //主题
			document.getElementById("iframe").src = "${ctx}/reps/report/zbztmcdyb/list.mvc?zbztId="+id;
		}else if(type == "3"){ //指标
			document.getElementById("iframe").src = "${ctx}/reps/report/zdyzbdyb/show.mvc?id="+id;
		}
	}
</script>
</html>

