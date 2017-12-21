<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>报表数据添加</title>
	<reps:theme />
</head>
<body>
<reps:container>
	<reps:panel id="top" dock="top" action="add.mvc" formId="xform" validForm="true" method="post">
		<reps:formcontent>
			<c:forEach items="${map}" var="f">
				<reps:formfield label="${f.key}" labelStyle="width:20%" textStyle="width:30%">
				<c:choose>
				<c:when test="${f.value.fieldType eq 'int' || f.value.fieldType eq 'float'}">
					<reps:input name="${f.value.itemName}" dataType="number" maxLength="${f.value.fieldLength}"></reps:input>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${f.value.isDictionary eq 1 and f.value.referDictionary ne '' }"><!-- 字典单独处理 -->
							<c:if test="${f.value.referDictionary eq 'district'}">
								<sys:dictionary src="${f.value.referDictionary}" style="width:158px;float:left;margin-right:5px;background:#fff;" type="input" id="${f.value.itemName}" name="${f.value.itemName}" headerValue="" headerText=""></sys:dictionary>
								<reps:dialog cssClass="btnLook" style="margin-left:-27px;" width="300" height="370" id="reportDistrict" iframe="true" 
								url="${ctx}/reps/sys/district/tree.mvc?callBack=setDistrict&code=" title="选择${f.value.itemChineseName}" value="请选择${f.value.itemChineseName}" />
							</c:if>
							<c:if test="${f.value.referDictionary ne 'district'}">
								<reps:select id="${f.value.itemName}" name="${f.value.itemName}" dictionaryItem="${dictMap[f.value.itemName]}" headerText="" headerValue=""></reps:select>
							</c:if>
						</c:when>
						<c:otherwise>
							<reps:input name="${f.value.itemName}" maxLength="${f.value.fieldLength}"></reps:input>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
				</c:choose>
				</reps:formfield>
			</c:forEach>
		</reps:formcontent>
		<br/><br/><br/>
		<reps:formbar>
			<!-- <reps:ajax  messageCode="add.button.save" formId="xform" callBack="skip" type="button" cssClass="btn_save"></reps:ajax> -->
			<reps:button cssClass="btn_save_a" messageCode="add.button.save" onClick="submitForm(0)" ></reps:button>
			<reps:button cssClass="btn_save_a" value="保存并继续" onClick="submitForm(1)" ></reps:button>
			<reps:button cssClass="btn_cancel_a" messageCode="add.button.cancel" onClick="back(0)"  ></reps:button>
		</reps:formbar>
	</reps:panel>
</reps:container>
</body>
<script type="text/javascript">
	function submitForm(flag){
		var data = $("#xform").serialize();
		data = decodeURIComponent(data, true);
		 $.ajax({
		   	 url: "add.mvc",
		   	 type: "POST",
		   	 dataType: "json",
		   	 data :{"columndata":data,"did":'${define.id}'},
		   	 async: false,
		   	 success: function(data){
	   			 messager.message(data, function(){back(flag);});
		   	 }
		 });
	}
	function convertArray(o) {
		var v = {}; 
		for (var i in o) { 
		if (typeof (v[o[i].name]) == 'undefined') v[o[i].name] = o[i].value; 
		else v[o[i].name] += "," + o[i].value; 
		} 
		return v; 
	} 
	var skip = function(data) {
		messager.message(data, function() {
			parent.location.href = "index.mvc";
		});
	};
	function setDistrict(code, name){
		$("input[name='district']").val(code);
		$("#district").val(name);
		$.pdialog.closeDialog();
		REPS.closeDialog();
	}
	var my = function(data) {
		messager.message(data, function() {
			window.location.href = "list.mvc?did=" + data.data;
		});
	};
	
	function back(flag) {
		if(flag == 1){
			window.location.href = "toadd.mvc?did=${define.id}";
		}else{
			window.location.href = "list.mvc?did=${define.id}";
		}
	}
</script>
</html>