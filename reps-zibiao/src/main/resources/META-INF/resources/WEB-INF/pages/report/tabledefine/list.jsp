<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>统计表</title>
	<reps:theme/>
	<link rel="stylesheet" href="${ctx}/library/ztree/css/zTreeStyle.css" type="text/css">
	<script src="${ctx}/library/base/jquery-ui-1.11.0.min.js" type=text/javascript></script>
	<script src="${ctx}/library/ztree/jquery.ztree.core-3.5.min.js" type="text/javascript"></script>
	<style type="text/css">
		li {
			color: #333;
			font-size: 14px;
			line-height: 22px;
			font-family: "Helvetica","Arial","微软雅黑","宋体";
		}
		ul {
			padding-left: 2px;
		}
		#maincontainer {
			width: 840px;
			height: 95%;
			margin: 0 auto;
			border: 2px solid #0069a0;
			padding: 10px;
		}
		#leftpanel {
			float: left;
			width: 140px;
			height: 100%;
		}
		#itemproperty {
			width: 100%;
			height: 100%;
			border: 1px solid green;
			overflow: auto;
			float: left;
			word-break: keep-all;
			white-space: nowrap;
		}
		#selecteditem {
			width: 690px;
			height: 100%;
			overflow: auto;
			float: right;
			border: 1px solid green;
		}
		/*拖动的元素最好设置一个宽带和高度*/
		#draggableDiv {
			z-index: 1000;
			background-color: Green;
			width: 0px;
			height: 0px;
			position: absolute;
			color: #fff;
		}
		#rightlistcontainer {
			width: 100%;
			height: 92%;
			overflow: auto;
			float: right;
			border: 0px;
		}
		.praxis{}
		.pTop{border-bottom:1px #cddfed solid;border-top:1px #cddfed solid;height:20px;padding:6px 5px 5px 5px;background:#E5EDEF;}
		.pTitle{float:left;width:300px;height:18px;font-size:13px;}
		.pTitle .span1{font-weight: bold;color: blue;}
		.pTitle .span2{margin-left: 10px;}
		.pTitle .span2 input,label {vertical-align: middle;}
		.pButtons{float: right;width: 200px;height: 18px;text-align: right}
		.pButtons a{margin-left: 5px;}
		
		.pContent{padding:5px;word-break:break-all;line-height:18px;}
		.pOptions{padding:5px;word-break:break-all;}
		.resizable_f_c{cursor: s-resize;width: 100%;bottom: 0;bottom:20px;left: 0;z-index: 1;}
		
	</style>
</head>
<body onselectstart="return false;">
<div id="dialogadditem" class="dialog" style="display:none;top: 0px; left: 0px; z-index: 44; height: 530px; width: 900px;">
	<div class="dialogHeader" onselectstart="return false;" oncopy="return false;" onpaste="return false;" oncut="return false;">
		<div class="dialogHeader_r">
			<div class="dialogHeader_c">
 			<a class="close" href="javascript:void(0);" onclick="closeDialog()">close</a>
 			<a class="maximize" href="#maximize" style="display: none;">maximize</a>
 			<a class="restore" href="#restore">restore</a>
 			<h1 id="title"></h1>
			</div>
		</div>
	</div>
	<div class="dialogContent layoutBox unitBox" style="height:90%;overflow:auto;">
		<div id="maincontainer">
			<div id='draggableDiv' class="ui-widget-content">
			</div>
			<!--左边面板-->
			<div id="leftpanel">
				<div id="itemproperty" class="threepanels">
					<ul class="ptreelist">
						<li id="itemlist">
							<ul id="treeDemo" class="ztree"></ul>
						</li>
					</ul>
				</div>
			</div>
			<!--右边面板-->
			<div id="selecteditem">
				<div style="float:left;padding-left:5px;padding-top:5px">
					<span style="color:red;">请把指标项拖入下方</span>
					<button id="lsx";style="color:red;" type="button" onClick="temp()">临时项</button>
				</div>
				<div style="float:right;padding-top:5px">
					<!-- 提交按钮 -->
					<reps:button id="btnSave" cssClass="small_btn_save" type="button" onClick="submitcheck()" messageCode="add.button.save" />
					<reps:button cssClass="small_btn_cancel" type="button" onClick="back()" messageCode="add.button.cancel" />
				</div>
				<div id="rightlistcontainer">
					<form method="post" id="itemForm">
						<input id="tjTableDefineId" name="tableId" type="hidden" value="${tableDefine.id}"/>
						<input id="isBasicId" name="isBasic" type="hidden" value="${tableDefine.isBasic}"/>
						
						<div class="all">
							<div class="block" id="block">
								<div class="blockTop">
									<div class="blockInfo"></div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="resizable_f_c" tar="s"></div>
	</div>
	<div class="dialogButtons" style="display:none;"></div>  
	<div class="dialogFooter">
		<div class="dialogFooter_r">
			<div class="dialogFooter_c"></div>
		</div>
	</div>  
	<div class="resizable_c_r" tar="e" style="height:95%;"></div>
</div>
<reps:container layout="true">
	<reps:panel id="top" dock="top" method="post" action="list.mvc" formId="queryForm">
		<reps:formcontent parentLayout="true" style="width:80%;">
			<reps:formfield label="统计报表名称" labelStyle="width:23%;" textStyle="width:30%;">
				<reps:input name="chineseName">${info.chineseName}</reps:input>
			</reps:formfield>
			<reps:formfield label="数据表名" labelStyle="width:20%;" textStyle="width:27%;">
				<reps:input name="name">${info.name}</reps:input>
			</reps:formfield>
		</reps:formcontent>
		<reps:querybuttons>
			<reps:ajaxgrid messageCode="manage.button.query" formId="queryForm" gridId="orglist" cssClass="search-form-a"></reps:ajaxgrid>
		</reps:querybuttons>
		<reps:footbar>
			<reps:button cssClass="add-a" action="toadd.mvc" messageCode="manage.action.add"></reps:button>
		</reps:footbar>
	</reps:panel>
	<reps:panel id="mybody" dock="center">
		<reps:grid id="orglist" items="${list}" form="queryForm" var="table" pagination="${pager}" flagSeq="true">
			<reps:gridrow>
				<reps:gridfield title="统计报表名称" width="22">${table.chineseName}</reps:gridfield>
				<reps:gridfield title="数据表名" width="18">${table.name}</reps:gridfield>
				<reps:gridfield title="是否基础表" width="8" align="center">
					<c:if test="${table.isBasic==1}">是</c:if>
				</reps:gridfield>
				<reps:gridfield title="统计方法" width="7" align="center">
					<c:if test="${table.method=='V'}">简单</c:if>
					<c:if test="${table.method=='H'}">复杂</c:if>
				</reps:gridfield>
				<reps:gridfield title="优先级" width="5" align="center">${table.priority}</reps:gridfield>
				<reps:gridfield title="是否启用" width="7" align="right">
					<c:if test="${table.enabled=='1' || table.enabled==null}">启用</c:if>
					<c:if test="${table.enabled=='0'}">不启用</c:if>
				</reps:gridfield>
				<reps:gridfield title="操作" width="33">
					<reps:ajax cssClass="sample-data-table" value="创建数据表" confirm="您确定要创建?"
						callBack="my" url="createtable.mvc?id=${table.id}">
					</reps:ajax>
					<reps:button cssClass="modify-table" messageCode="manage.action.update" action="toedit.mvc?id=${table.id}"></reps:button>
					<reps:ajax cssClass="delete-table" messageCode="manage.action.delete" confirm="您确定要删除所选行吗?"
						callBack="my" url="delete.mvc?id=${table.id}">
					</reps:ajax>
					<%--
					<reps:button cssClass="add-table" messageCode="manage.action.update" action="additem2.mvc?id=${table.id}" value="添加指标"></reps:button>
					--%>
				 <reps:button cssClass="add-table" messageCode="manage.action.update" onClick="chooseItem('${table.id}','${table.chineseName}','${table.isBasic}')" value="添加指标"></reps:button>
				 
				 </reps:gridfield>
			 </reps:gridrow>
		 </reps:grid>
	  </reps:panel>
</reps:container>
<script type="text/javascript">
	/*
	$.fn.serializeObject = function()
	{
	   var o = {};
	   var a = this.serializeArray();
	   $.each(a, function() {
		   if (o[this.name]) {
			   if (!o[this.name].push) {
				   o[this.name] = [o[this.name]];
			   }
			   o[this.name].push(this.value || '');
		   } else {
			   o[this.name] = this.value || '';
		   }
	   });
	   return o;
	};*/
	
	var  valuesJson = [];
	
	var expressionTypes = {
		'SQL'  : 'SQL',
		'常量' : '常量',
		'变量' : '变量',
		'运算' : '运算'
	};

	var fieldTypes = {
		'varchar'  : '字符型（单字节）',
		'nvarchar' : '字符型（双字节）',
		'byte'	 : '超短整型',
		'short'	: '短整型',
		'int'	  : '整型',
		'long'	 : '长整型',
		'float'	: '小数点',
		'datetime' : '时间型',
	};
	
	//btn表单之前，表单数据进行验证。
	function submitcheck(){
		var block = $("#block div[class=praxis]");
		var isSubmit = 0;//默认提交成功。
		var arr=new Array();
		arr=["dependency","itemName","itemChineseName","fieldLength","expression","orders"];
		
		for(var i=0; i<arr.length; i++){
			isSubmit = checkedField(arr[i]);
			var dependency = $("#dependency1");
			var expression = $("#expression1");
			var itemName = $("#itemName1");
			
			if(isSubmit == 1){//提交失败。
				break;
			}
		}
		
		if(dependency == undefined && expression == undefined){
			
		}else{
			var count = -1;//有多少项 条件依赖。
			$.each($("input[name=dependency]"),function(index,obj){
				count = index;
			});
			var tableId = $("#tjTableDefineId").val();
			count = count + 1;
			
			if(valuesJson.length>0){
				valuesJson.splice(0,valuesJson.length);
			}
			
			for(var i=1;i<=count;i++){
				//验证 条件依赖和表达式。
				debugger;
				var expressionType = $.trim($("#expressionType"+i).val());
				var dependency = $.trim($("#dependency"+i).val());
				var expression = $.trim($("#expression"+i).val());
				
				if (expressionType == "SQL" || expressionType == "常量"){
					if (expression == ""){
						messager.info("表达式不能都为空！");
						isSubmit = 1;
						break;
					}
				}
				
				var addValue = [];
				var values = {};
				
				var itemNameJson = $("#itemName"+i).val();
				var batchKeyJson = $("#batchKey"+i).val();
				var itemChineseNameJson = $("#itemChineseName"+i).val();
				var fieldTypeJson = $("#fieldType"+i).val();
				var fieldLengthJson = $("#fieldLength"+i).val();
				var expressionTypeJson = $("#expressionType"+i).val();
				var dependencyJson = $("#dependency"+i).val();
				var valuedColumnJson = $("#valuedColumn"+i).val();
				var expressionJson = $("#expression"+i).val();
				var ordersJson = $("#orders"+i).val();
				var isTemporaryJson = $("#isTemporary"+i).val();
				
				values.itemName = itemNameJson;
				values.batchKey = batchKeyJson;
				values.itemChineseName = itemChineseNameJson;
				values.fieldType = fieldTypeJson;
				values.fieldLength = fieldLengthJson;
				values.expressionType = expressionTypeJson;
				values.dependency = dependencyJson;
				values.valuedColumn = valuedColumnJson;
				values.expression = expressionJson;
				values.orders = ordersJson;
				values.isTemporary = isTemporaryJson;
				
				addValue.push(values);
				valuesJson.push(addValue);
				
				//循环每一次比较 不能出现指标名称相同。
				for(var j=0;j<i;j++){
					var itemNameI = $.trim($("#itemName"+i).val());
					var itemNameJ = $.trim($("#itemName"+j).val());
					if(itemNameI == itemNameJ){
						messager.info("指标名称不能相同！");
						isSubmit = 1;
						break;
						}
					}
				/*	
				//从数据库中返回所有的指标名称。 与页面中的指标名称进行比较。
				$.ajax({
					url:"getallitemname.mvc",
					type:"post",
					data:{tableId:tableId},
					async:false,
					dataType: "json",
					success:function(result){
						if(result.length>0){
							for(var k=0;k<result.length;k++){
								var itemName = $.trim($("#itemName"+i).val());
								if(itemName == result[k]){
									messager.info("指标名称: "+itemName+" 已存在!");
									isSubmit = 1;
									return;
								}
								
							}
						}
					}
				
					
			   });
				*/
				
				}
		}
		
		
		if(isSubmit == 1){
			return false;
		}
		debugger;
		if(block.length>0){
			//$("#itemForm").submit();//提交表单
			
			//ajax方式提交
			
			var sendJson = JSON.stringify(valuesJson);
			
			$.ajax({
				type: "post",  
				data:{"data":sendJson,"tableId":tableId},  
				url: "saveitem.mvc",  
				dataType: "text",
				cache: false,
				success: function(data){
					if(data == "success"){
						if(valuesJson.length>0){
							valuesJson.splice(0,valuesJson.length);
						}
						window.location.href= "list.mvc";
					}
				}
			});
			
			return true;
		}else{
			messager.info("请添加指标！");
			return false;
		}
		
		return false;
		
	}
	
	function back()
	{
		window.location.href= "list.mvc";
	}
	
	//验证表单数据。
	function checkedField(field){
		var isChecked = 0;
		var count = -1;
		$.each($("input[name="+field+"]"),function(index, obj){
			if(field=="itemName" || field=="itemChineseName" || field=="fieldLength" || field == "orders"){
				if(field == "itemName"){
					var id1 = index + 1;
					$(obj).attr("id","itemName"+id1);
					if($(obj).val().trim() == ""){
						messager.info("指标名称不能为空！");
						isChecked = 1;
						return;
					}
					
				}
				if($(obj).val().trim() == "" && field == "itemChineseName"){
					messager.info("指标中文名称不能为空！");
					isChecked = 1;
					return;
				}
				if($(obj).val().trim() == "" && field == "fieldLength"){
					messager.info("字段长度不能为空！");
					isChecked = 1;
					return;
				}
				if($(obj).val().trim() == "" && field == "orders"){
					messager.info("统计顺序不能为空！");
					isChecked = 1;
					return;
				}
			}
			
			if(field == "itemName" && $(obj).val().trim().length > 30){
				messager.info("指标名称长度过长");
				isChecked = 1;
				return;
			}
			if(field == "itemChineseName" && $(obj).val().trim().length > 30){
				messager.info("指标中文名称长度过长");
				isChecked = 1;
				return;
			}
			if(field == ""){
				
			}
			if(field == "fieldLength"){
				var type = "^[0-9]*[1-9][0-9]*$"; 
				var re = new RegExp(type); 
			 	if($(obj).val().trim().match(re)==null)
				{ 
			 		if(field == "fieldLength"){
			 			messager.info("字段长度请输入整数!");
			 		}else{
			 			messager.info("统计顺序请输入整数!");
			 		}
			 		
			 		isChecked = 1;
					return;
				}
			}
			if(field == "fieldLength" && $(obj).val().trim().length > 5){
				messager.info("字段长度过长");
				isChecked = 1;
				return;
			}
			if(field == "dependency"){
				var id2 = index + 1;
				$(obj).attr("id","dependency"+id2);
				if($(obj).val().trim().length > 200){
					messager.info("条件依赖长度过长");
					isChecked = 1;
					return;
				}
			}
			if(field == "valuedColumn"){
				var id2 = index + 1;
				$(obj).attr("id","valuedColumn"+id2);
				if($(obj).val().trim().length > 80){
					messager.info("指标取值项长度过长");
					isChecked = 1;
					return;
				}
			}
			
		});
		
//		if(field == "expression"){
//			$.each($("textarea[name="+field+"]"),function(exIndex, exObj){
//				var id3 = exIndex + 1;
//				$(exObj).attr("id","expression"+id3);
//				
//				if($(exObj).val().trim().length > 500){
//					messager.info("表达式长度过长");
//					isChecked = 1;
//					return;
//				}
//			});
//		}
		
		return isChecked;
	}
	
	//添加的指标项个数
	var addItemCount = 0;
	var itemCount = 0;
	
	//弹出添加指标项页面，并显示所有的指标。
	function chooseItem(id, name, isBasic){//isBasic 为是否基础表
		$("#dialogadditem").show();
		$("#tjTableDefineId").val(id);
		$("#isBasicId").val(isBasic);
		var title = $("#title");
		title.append("<span>"+name+":添加指标项"+"</span>");
		//查询所有的指标
		var setting = {
			data: {
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pId",
					rootPId: 0
				}
			}
		};
		
		$.ajax({
			url:"getitemlist.mvc",
			type:"post",
			async:false,
			success:function(result){
				/*
				itemCount = result.length;
				$.each(result,function(index, obj){
					var item = $("#itemlist");
					var html = '<ul class="ptreelist">';
					html += '<li id="'+obj.id+'" title="'+obj.chineseName+'" name="'+obj.name+'" style="cursor:pointer;">' + obj.chineseName + '<span class="closebutton"></span></li>';
					html += '</ul>';
					item.append(html);
				});
				*/
				var zNodes = eval(result);
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				
				//修改属性问题：
				
				//获取树对象
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				//拿到所有树节点
				var nodes = treeObj.getNodes();
				//for循环逐个修改树节点属性
				for(var i = 0;i<nodes.length;i++)
				{
					var node = nodes[i];
					var childrens = node.children;
					var titleId = node.tId;//treeDemo_1
					//var childrenId = childrens.tId;//treeDemo_2
					for(var j=0;j<childrens.length;j++){
						itemCount++;
						var childrenId = childrens[j].tId;//treeDemo_2
						var id = childrens[j].id;
						var tableName = childrens[j].tableName;
						var span$ = $("#"+childrenId+"_span");
						span$.attr("id",id);
						span$.attr("name",tableName);
						
					}
					
					//更新节点
					//treeObj.updateNode(nodes[i]);
				}
			}
			
		});
		
		var curPraxis=$("#block");
		
		//显示已添加的指标项
 		$.ajax({
 			url:"getallitembytableid.mvc",
 			type:"post",
 			data:{tableId:id},
 			async:false,
 			success:function(result){
				addItemCount = result.length;
				
 				$.each(result, function(index, obj){
 					var innerHtml = createItem(obj, 1, isBasic);//统计表指标 类型的方式添加
 					var div = '<div class="praxis">';
 					div += innerHtml;
 					div += '</div>';
 					curPraxis.append(div);
 			 		REPS.reloadDialogJs();
				});
 			},
			error:function(){
				alert("failure");
			}
 		});
		
		initDrag();//初始化拖拉控件。
	}
	
	function initDrag(){
		var clickElement = null;
		$("#treeDemo li ul li a").bind("mousedown", function (event) {
			//获取当前mousedown元素的内容
			var itemContent = $(this).html();
			//定义一个div，拖拉时把指标内容放入div中。
			var draggableDiv = $("#draggableDiv");
			$(draggableDiv).css({ "display": "block", "height": 0 });
			//将点击的元素内容复制
			var id = this.id;
			var item = $("#"+id+" span").last();
			clickElement = item.clone();

			var currentdiv = $(this).offset();//获得点击指标的位置。
			//给div添加显示的位置。
			$(draggableDiv).css({ "top": currentdiv.top-differHeight, "left": currentdiv.left-differWidth });
			
			//触发draggableDiv元素的mousedown事件。
			draggableDiv.trigger(event);
			
			//取消默认行为
			return false;
		});
		
		$("#draggableDiv").mouseup(function (event) {
			$(this).css({ "height": "0" });
		});
		
		$("#draggableDiv").draggable({
			containment: "parent",
			drag: function (event, ui) {
				$("#draggableDiv").css({ "width": "auto", "height": "22px" });
				$("#draggableDiv").append(clickElement);

			},
			stop: function () {
				//拖拽结束，将拖拽容器内容清空
				$("#draggableDiv").html("");
				$("#draggableDiv").css({"height": "0" });
			}
		});
		
	}
	
	//关闭添加指标div
	function closeDialog(){
		$("#dialogadditem").hide();
		$("#title").children("span").remove();//将标题内容清空。
		$("#block").children("div[class=praxis]").remove();//将拖入的指标项全部清空。
		$("#treeDemo").children("li").remove();//清除所有的指标项.
		
		//关闭后页面布局都重置。
		var obj = $('div.dialog');
		differHeight = 0;
		differWidth = 0;
		obj.css({'left':0, 'top':0});
		
		//所有拖拽过的指标id集合。itemList
		//清除所有的指标名称集合。
		if(itemList.length>0){
			itemList.splice(0,itemList.length);
		}
		
		//设为没有拖拉过。
		createItemCount=1;
		
	}
	
	var differHeight = 0;//拖动整个div后，与原来的div高度差。
	var differWidth = 0;//拖动整个div后，与原来的div宽度差。
	
	$(function() {
		REPS.reloadDialogJs();
		$("#dialogadditem").hide();
		$( "#dragsource" ).draggable({
			  start: function() {
			  },
			  drag: function() {
			  },
			  stop: function() {
			  }
 		});
		
		//“放”的操作代码
		$("#rightlistcontainer").droppable({
			drop: function (event, ui) {
				var draggableDiv = $("#draggableDiv span");
				var itemId = draggableDiv.attr("id");//得到拖入指标的id
				var itemName = draggableDiv.attr("name");//得到拖入指标的名称
				
				//通过指标id获得指标所有信息。
				getItemById(itemId,itemName);
			}
		});
		
		/*
	  //删除
		$("a[name='sqdel']").live("click", function(evt){
		   var thisobj = $(this);
		   var id = thisobj.attr("id");
		   for(var i=0;i<itemList.length;i++){
				if(id == itemList[i]){
					itemList.splice(i,1);
				}
		   }
		 	createItemCount--;
		 	addItemCount--;
		   deletePraxis(thisobj);
	  });
		*/
		//上移
	  //$("a[name='squp']").live("click", function(evt){
	  //	  var thisobj = $(this);
	  //	  upPraxis(thisobj);
	  //});
		
		
	//下移
	// $("a[name='sqdown']").live("click", function(evt){
	//	  var thisobj = $(this);
	//	  downPraxis(thisobj);
	// });
	
		//拖拉整个添加指标div
		$(".dialogHeader").mousedown(
			function(event){
				var isMove = true;
				var x = event.pageX - $("div.dialog").offset().left;
				var y = event.pageY - $("div.dialog").offset().top;
				$(document).mousemove(function (e) {
					if (isMove) {
						var obj = $('div.dialog'); 
						obj.css({'left':e.pageX - x, 'top':e.pageY - y});
						differHeight = e.pageY - y;
						differWidth = e.pageX - x;
						
					}
				}).mouseup(
					function () {
						isMove = false; 
				});
				
			}
		);
	
		//bindResize(document.getElementById('dialogadditem'));
		var resizeHeight = $(".resizable_f_c");
		bindResize(resizeHeight.get(0),'h');//调高度
		var resizeWidth = $(".resizable_c_r");
		bindResize(resizeWidth.get(0),'w');//调宽度
	});
	
	function bindResize(el, type){
		var els = el.style,
		x = y = 0;
		
		$(el).mousedown(function (e){
			//x = e.clientX - el.offsetWidth,
			//y = e.clientY - el.offsetHeight;
			
			el.setCapture ? (
 				el.setCapture(),
				//设置事件
				el.onmousemove = function (ev)
				{
					mouseMove(ev || event);
				},
				el.onmouseup = mouseUp
			) : (
 				//绑定事件
				$(document).bind("mousemove", mouseMove).bind("mouseup", mouseUp)
			);
			  //防止默认事件发生
			  e.preventDefault();
		});
		//移动事件
		function mouseMove(e){
			 //els.width = e.clientX - x + 'px',
			var elsHeight = document.getElementById('dialogadditem').style;
			 if(type == 'h'){
				 elsHeight.height = e.clientY + 22+ 'px';
			 }
			 if(type == 'w'){
				 elsHeight.width = e.clientX + 'px';
			 }
		}
		
		//停止事件
		function mouseUp(){
			//在支持 releaseCapture 做些东东
			el.releaseCapture ? (
			//释放焦点
				el.releaseCapture(),
			//移除事件
				el.onmousemove = el.onmouseup = null
			) : (
				//卸载事件
				$(document).unbind("mousemove", mouseMove).unbind("mouseup", mouseUp)
			);
		}
	}
	
	//删除
	function deletePraxis(linkObj){
		var curDiv=linkObj.parent().parent().parent();
		var praxisDiv=curDiv.parent();
		curDiv.remove();
		
	}
	
	//下移
	function downPraxis(linkObj,order){
		var curDiv=linkObj.parent().parent().parent();//取得当前整个内容
		var nextDiv=curDiv.next();//下一个
		if(nextDiv.length==0){
			alert("已经是最后一个了");
			return;
		}
		
		//替换
		var pIndex1=curDiv.find("span[name='pIndex']").eq(0);//
		$("#orders"+order).val(order+1);//设置要下移的顺序值1->2
		var index = order+1;//2
		var orders= $("#orders"+index);//下一个内容
		$("#orders"+order).attr("id","orders"+index); //id="orders=2"
		
		var method = "toDown(" + index + ")";//toDown(2)
		linkObj.attr("onclick",method);
		
		var aUp1 = $("a[name=squp"+order+"]");
		var methodUp = "toUp(" + index + ")";
		aUp1.attr("onclick",methodUp);
		var aUp2 = $("a[name=squp"+index+"]");
		aUp1.attr("name","squp"+index);
		
		var adel1 = $("a[name=sqdel"+order+"]");
		var methodDel = "deleteItem(" + index + ")";
		adel1.attr("onclick",methodDel);
		var adel2 = $("a[name=sqdel"+index+"]");
		adel1.attr("name","sqdel"+index);
		
		//该部分的所有字段，id属性都下移。
		var itemNameDown1 = $("#itemName"+order);
		var itemNameDown2 = $("#itemName"+index);
		var batchKeyDown1 = $("#batchKey"+order);
		var batchKeyDown2 = $("#batchKey"+index);
		var itemChineseNameDown1 = $("#itemChineseName"+order);
		var itemChineseNameDown2 = $("#itemChineseName"+index);
		var fieldTypeDown1 = $("#fieldType"+order);
		var fieldTypeDown2 = $("#fieldType"+index);
		var fieldLengthDown1 = $("#fieldLength"+order);
		var fieldLengthDown2 = $("#fieldLength"+index);
		var expressionTypeDown1 = $("#expressionType"+order);
		var expressionTypeDown2 = $("#expressionType"+index);
		var dependencyDown1 = $("#dependency"+order);
		var dependencyDown2 = $("#dependency"+index);
		var valuedColumnDown1 = $("#valuedColumn"+order);
		var valuedColumnDown2 = $("#valuedColumn"+index);
		var expressionDown1 = $("#expression"+order);
		var expressionDown2 = $("#expression"+index);
		var isTemporary1 = $("#isTemporary"+order);
		var isTemporary2 = $("#isTemporary"+index);
		
		itemNameDown1.attr("id","itemName"+index);
		itemNameDown2.attr("id","itemName"+order);
		batchKeyDown1.attr("id","batchKey"+index);
		batchKeyDown2.attr("id","batchKey"+order);
		itemChineseNameDown1.attr("id","itemChineseName"+index);
		itemChineseNameDown2.attr("id","itemChineseName"+order);
		fieldTypeDown1.attr("id","fieldType"+index);
		fieldTypeDown2.attr("id","fieldType"+order);
		fieldLengthDown1.attr("id","fieldLength"+index);
		fieldLengthDown2.attr("id","fieldLength"+order);
		expressionTypeDown1.attr("id","expressionType"+index);
		expressionTypeDown2.attr("id","expressionType"+order);
		dependencyDown1.attr("id","dependency"+index);
		dependencyDown2.attr("id","dependency"+order);
		valuedColumnDown1.attr("id","valuedColumn"+index);
		valuedColumnDown2.attr("id","valuedColumn"+order);
		expressionDown1.attr("id","expression"+index);
		expressionDown2.attr("id","expression"+order);
		isTemporary1.attr("id","isTemporary"+index);
		isTemporary2.attr("id","isTemporary"+order);
		
		
		var aObj = $("a[name=sqdown"+index+"]");//下一个a
		linkObj.attr("name","sqdown"+index);
		var pIndex2=nextDiv.find("span[name='pIndex']").eq(0);//
		
		orders.val(order);
		orders.attr("id","orders"+order);
		
		method = "toDown(" + order + ")";
		aObj.attr("onclick",method);
		aObj.attr("name","sqdown"+order);
		
		methodUp = "toUp(" + order + ")";
		aUp2.attr("onclick",methodUp);
		aUp2.attr("name","squp"+order);
		
		methodDel = "deleteItem(" + order + ")";
		adel2.attr("onclick",methodDel);
		adel2.attr("name","sqdel"+order);
		
		var val1=pIndex1.text();
		var val2=pIndex2.text();
		pIndex1.text(val2);
		pIndex2.text(val1);
		
		nextDiv.after(curDiv);//移动
	}
	
	//上移
	function upPraxis(linkObj,order){
		var curDiv=linkObj.parent().parent().parent();//取得当前整个内容
		var prevDiv=curDiv.prev();//上一个
		if(prevDiv.length==0 || prevDiv.hasClass("blockTop")){
			alert("已经是第一个了");
			return;
		}
		
		//替换
		var pIndex1=curDiv.find("span[name='pIndex']").eq(0);//
		$("#orders"+order).val(order-1);//设置要上移的顺序值
		var index = order-1;
		var orders= $("#orders"+index);//上一个内容
		$("#orders"+order).attr("id","orders"+index);//设置要上移的id减1
		
		var methodTop = "toUp(" + index + ")";
		linkObj.attr("onclick",methodTop);
		//下移
		var aDown1 = $("a[name=sqdown"+order+"]");
		var methodDown = "toDown(" + index + ")";
		aDown1.attr("onclick",methodDown);
		var aDown2 = $("a[name=sqdown"+index+"]");
		aDown1.attr("name","sqdown"+index);
		
		//删除
		var adel1 = $("a[name=sqdel"+order+"]");
		var methodDel = "deleteItem(" + index + ")";
		adel1.attr("onclick",methodDel);
		var adel2 = $("a[name=sqdel"+index+"]");
		adel1.attr("name","sqdel"+index);
		
		
		//该部分的所有字段，id属性都下移。
		var itemNameDown1 = $("#itemName"+order);
		var itemNameDown2 = $("#itemName"+index);
		var batchKeyDown1 = $("#batchKey"+order);
		var batchKeyDown2 = $("#batchKey"+index);
		var itemChineseNameDown1 = $("#itemChineseName"+order);
		var itemChineseNameDown2 = $("#itemChineseName"+index);
		var fieldTypeDown1 = $("#fieldType"+order);
		var fieldTypeDown2 = $("#fieldType"+index);
		var fieldLengthDown1 = $("#fieldLength"+order);
		var fieldLengthDown2 = $("#fieldLength"+index);
		var expressionTypeDown1 = $("#expressionType"+order);
		var expressionTypeDown2 = $("#expressionType"+index);
		var dependencyDown1 = $("#dependency"+order);
		var dependencyDown2 = $("#dependency"+index);
		var valuedColumnDown1 = $("#valuedColumn"+order);
		var valuedColumnDown2 = $("#valuedColumn"+index);
		var expressionDown1 = $("#expression"+order);
		var expressionDown2 = $("#expression"+index);
		var isTemporary1 = $("#isTemporary"+order);
		var isTemporary2 = $("#isTemporary"+index);
		
		itemNameDown1.attr("id","itemName"+index);
		itemNameDown2.attr("id","itemName"+order);
		batchKeyDown1.attr("id","batchKey"+index);
		batchKeyDown2.attr("id","batchKey"+order);
		itemChineseNameDown1.attr("id","itemChineseName"+index);
		itemChineseNameDown2.attr("id","itemChineseName"+order);
		fieldTypeDown1.attr("id","fieldType"+index);
		fieldTypeDown2.attr("id","fieldType"+order);
		fieldLengthDown1.attr("id","fieldLength"+index);
		fieldLengthDown2.attr("id","fieldLength"+order);
		expressionTypeDown1.attr("id","expressionType"+index);
		expressionTypeDown2.attr("id","expressionType"+order);
		dependencyDown1.attr("id","dependency"+index);
		dependencyDown2.attr("id","dependency"+order);
		valuedColumnDown1.attr("id","valuedColumn"+index);
		valuedColumnDown2.attr("id","valuedColumn"+order);
		expressionDown1.attr("id","expression"+index);
		expressionDown2.attr("id","expression"+order);
		isTemporary1.attr("id","isTemporary"+index);
		isTemporary2.attr("id","isTemporary"+order);
		
		
		var aObj = $("a[name=squp"+index+"]");
		linkObj.attr("name","squp"+index);
		var pIndex2=prevDiv.find("span[name='pIndex']").eq(0);//
		
		$("#orders"+index).val(order);
		orders.attr("id","orders"+order);
		
		methodTop = "toUp(" + order + ")";
		aObj.attr("onclick",methodTop);
		aObj.attr("name","squp"+order);
		
		methodDown = "toDown(" + order + ")";
		aDown2.attr("onclick",methodDown);
		aDown2.attr("name","sqdown"+order);
		
		//删除
		methodDel = "deleteItem(" + order + ")";
		adel2.attr("onclick",methodDel);
		adel2.attr("name","sqdel"+order);
		
		var val1=pIndex1.text();
		var val2=pIndex2.text();
		pIndex1.text(val2);
		pIndex2.text(val1);
		
		prevDiv.before(curDiv);//移动
	}
	
	//所有拖拽过的指标名称的集合。
	var itemList = new Array();
	
	function getItemById(id,name){
		
		for(var i=0;i<itemList.length;i++){
			if(name == itemList[i]){
				messager.info("已经添加过了！");
				return false;
			}
		}
		
		itemList.push(name);//将指标名称放入itemList中
		
		var curPraxis=$("#block");
		$.ajax({
			url:"chooseitem.mvc",
			type:"post",
			data:{id:id},
			async:false,
			success:function(result){
				//var result = $.parseJSON(result);
				var isBasic = $("#isBasicId").val();
				var innerHtml = createItem(result,0,isBasic);//指标 类型的方式添加
				var div = '<div class="praxis">';
				
				div += innerHtml;
				
				div += '</div>';
				curPraxis.append(div);
		 		REPS.reloadDialogJs();
			}
	   });
	}
	
	//上移
	function toUp(order){
		var thisobj = $("a[name=squp"+order+"]");
		upPraxis(thisobj,order);
	}
	
	//下移
	function toDown(order){
		var thisobj = $("a[name=sqdown"+order+"]");
		downPraxis(thisobj,order);
	}
	
	//删除
	function deleteItem(order){
		var thisobj = $("a[name=sqdel"+order+"]");
		   var id = thisobj.attr("id");
		   for(var i=0;i<itemList.length;i++){
				if(id == itemList[i]){
					itemList.splice(i,1);
				}
		   }
		 	createItemCount--;
		 	addItemCount--;
		   
		   
		   //删除该指标项时，该指标下的指标 统计顺序整体前移。
		   var curOrder = $("#orders"+order);
		   var curValue = parseInt(curOrder.val())+1;
		   for(var i=curValue;i<=itemCount;i++){
			   var order = $("#orders"+i).val();
			   if(order=="" || order == undefined){
				   break;
			   }else{
				   //统计顺序前移
				   var value = parseInt(order)-1;
				   var orderObj = $("#orders"+i);
				   orderObj.val(value);
				   orderObj.attr("id","orders"+value);//修改统计顺序id
				   
				   //指标名称
				   var itemNameObj = $("#itemName"+i);
				   itemNameObj.attr("id","itemName"+value);//修改指标名称id
				   
				   //临时指标项
				   var itemNameObj = $("#isTemporary"+i);
				   itemNameObj.attr("id","isTemporary"+value);//修改临时指标项id
				   
				   //是否唯一批次约束项
				   var batchKeyObj = $("#batchKey"+i);
				   batchKeyObj.attr("id","batchKey"+value);//修改是否唯一批次约束项id
				   
				   //指标中文名称
				   var itemChineseNameObj = $("#itemChineseName"+i);
				   itemChineseNameObj.attr("id","itemChineseName"+value);//修改指标中文名称id
				   
				   //字段类型
				   var fieldTypeObj = $("#fieldType"+i);
				   fieldTypeObj.attr("id","fieldType"+value);//修改字段类型id
				   
				 	//字段类型
				   var fieldLengthObj = $("#fieldLength"+i);
				   fieldLengthObj.attr("id","fieldLength"+value);//修改字段类型id
				   
					//表达式类型
				   var expressionTypeObj = $("#expressionType"+i);
				   expressionTypeObj.attr("id","expressionType"+value);//修改表达式类型id
				   
				   //条件依赖
				   var dependencyObj = $("#dependency"+i);
				   dependencyObj.attr("id","dependency"+value);//修改条件依赖id
				   
				   //指标取值项
				   var valuedColumnObj = $("#valuedColumn"+i);
				   valuedColumnObj.attr("id","valuedColumn"+value);//修改取值项id
				   
					//表达式
				   var expressionObj = $("#expression"+i);
				   expressionObj.attr("id","expression"+value);//修改表达式id
				   
				   //修改上移按钮
					var aUp1 = $("a[name=squp"+i+"]");
					var methodUp = "toUp(" + value + ")";
					aUp1.attr("onclick",methodUp);
					aUp1.attr("name","squp"+value);
					
					//修改下移按钮
					var aDown1 = $("a[name=sqdown"+i+"]");
					var methodDown = "toDown(" + value + ")";
					aDown1.attr("onclick",methodDown);
					aDown1.attr("name","sqdown"+value);
					
					//修改删除按钮
					var aDelete1 = $("a[name=sqdel"+i+"]");
					var methodDelete = "deleteItem(" + value + ")";
					aDelete1.attr("onclick",methodDelete);
					aDelete1.attr("name","sqdel"+value);
			   }
		   }
		   
		   deletePraxis(thisobj);
		   
	}
	
	//创建临时项的空白表单
	function temp(){
		var htmlTemp=createTemp();
		var blockTemp=$("#block");
			blockTemp.append(htmlTemp);
	}
	
	function createTemp(){
		var htmlItem='';
		htmlItem +='<div class="praxis">';
		htmlItem +='<div class="pTop">';
		htmlItem +='<div class="pTitle">';
		htmlItem += '<span class="span1">临时指标项</span>';
		htmlItem += '</div>';
		
		htmlItem += '<div class="pButtons">';
		htmlItem+='  <a href="javascript:void(0);" onclick="toUp('+createItemCount+')"  title="向上移动" name="squp'+createItemCount+'"><img src="${ctx}/theme/portal/images/up.gif" width="15" height="15" /></a>';
		htmlItem+='  <a href="javascript:void(0);" onclick="toDown('+createItemCount+')" title="向下移动" name="sqdown'+createItemCount+'"><img src="${ctx}/theme/portal/images/down.gif" width="15" height="15" /></a>';
		htmlItem+='  <a href="javascript:void(0);" onclick="deleteItem('+createItemCount+')" title="删除" name="sqdel'+createItemCount+'"><img src="${ctx}/theme/portal/images/delete.gif" width="15" height="15" /></a>';		
		htmlItem+=' </div> ';
		htmlItem+='</div>  ';
		
		
		htmlItem+=' <div class="pOptions"> ';
		htmlItem+='   <div class="pOption">';
		
		htmlItem += '<table class="formContent" style="width:710px;padding:0px 0px 5px 0px;"><tbody>';
		htmlItem += '<tr><td class="fLabel" style="width:12%">指标名称:</td>';
		htmlItem += '<td class="fInput" style="width:30%"><input class="txtInput required" maxlength="30" style="width:176px;background-color: rgb(255, 255, 255);" id="itemName'+createItemCount+'" name="itemName"';
		htmlItem += '</td>';
		
		htmlItem += '<td class="fLabel" style="width:12%">指标中文名称:</td>';
		htmlItem += '<td class="fInput" style="width:30%"><input class="txtInput required" maxlength="30" style="width:176px" name="itemChineseName" id="itemChineseName'+createItemCount+'"';
		htmlItem += '</td></tr>';
		
		htmlItem += '<tr style="display:none"><td class="fLabel">字段类型:</td>';
		htmlItem += '<td class="fInput"><select name="fieldType" id="fieldType'+createItemCount+'" style="width:180px;">';
		
		$.each(fieldTypes, function(value, text){
			htmlItem += '<option value="' + value + '"';
			htmlItem += '>' + text + '</option> ';
		});
		
		htmlItem += '</select> </td>';
	   
		
		htmlItem += '<td class="fLabel">字段长度:</td>';
		htmlItem += '<td class="fInput"><input class="txtInput number required" maxlength="5" style="width:176px" name="fieldLength" id="fieldLength'+createItemCount+'" /></td></tr>';
		
		htmlItem += '<tr><td class="fLabel">表达式类型:</td>';
		htmlItem += '<td><select class="required" style="width:180px;" name="expressionType" id="expressionType'+createItemCount+'">';
		
		$.each(expressionTypes, function(value, text){
			htmlItem += '<option value="' + value + '"';
			htmlItem += '>' + text + '</option> ';
		});
		
		htmlItem += '</select></td>';
		
		htmlItem += '<td class="fLabel">条件依赖:</td>';
		htmlItem += '<td class="fInput"><input class="txtInput valid" maxlength="200" style="width:176px" id="dependency'+createItemCount+'" name="dependency"';
		htmlItem += '></td></tr>';
	
		htmlItem += '<tr><td class="fLabel">指标项取值:</td>';
		htmlItem += '<td colspan="3" class="fInput"><input class="txtInput valid" maxlength="200" style="width:176px" id="valuedColumn'+createItemCount+'" name="valuedColumn"';
		htmlItem += '></td></tr>';

		htmlItem += '<tr><td class="fLabel">表达式:</td>';
		htmlItem += '<td colspan="3" class="fInput"><textarea class="txtInput valid" name="expression" id="expression'+createItemCount+'" style="width:530px;height:50px;resize:none">';
		htmlItem += '</textarea></td></tr>';
		
		htmlItem += '<input type="hidden" class="txtInput  required valid" style="width:176px" name="orders" id="orders'+createItemCount+'"';
		addItemCount++;
		htmlItem += 'value="'+addItemCount+'" >';

		htmlItem += '<input type="hidden" class="txtInput  required valid" style="width:176px" name="isTemporary" id="isTemporary'+createItemCount+'"';
		htmlItem += 'value="1" >';
			   
		htmlItem += '</table>';
		htmlItem+='</div>  ';
		htmlItem+='</div>  ';
		htmlItem+='</div>  ';
		
		createItemCount++;
	   
		return htmlItem;
	}
	
	//创建表单的个数，用于加入name属性中。方便查找
	var createItemCount = 1;
	
	function createItem(result, type, isBasic){//isBasic 是否基础表 1:是基础表（则不显示是唯一批次约束项）0：是业务表（则显示是唯一批次约束项）
		//组装html
		var htmlItem='';
		htmlItem +='<div class="pTop">';
		htmlItem +='<div class="pTitle">';
		htmlItem += '<span class="span1" name="itemName'+createItemCount+'">';
		debugger;
		if(isBasic=='0'){//显示 是唯一批次约束项
			if(type == 1){
				htmlItem += result.itemChineseName+'</span>';
				if(result.isTemporary=='1'){ 
					htmlItem += '(临时指标项)</span>';
				}else{
					
				
				htmlItem += '<span class="span2 fLabel">';
				htmlItem += '<input type="checkbox" onclick="changeCheckboxValue(this)" name="batchKey" id="batchKey'+createItemCount+'"';
				if(result.batchKey=='0' || result.batchKey==undefined || result.batchKey==null){
					htmlItem += 'value="0"><label for="batchKey' + createItemCount + '">&nbsp;是唯一批次约束项</label><span>';
				}
				if(result.batchKey=='1'){
					htmlItem += 'checked="checked" value="1"><label for="batchKey' + createItemCount + '">&nbsp;是唯一批次约束项</label><span>';
				}
				}
			}
			if(type == 0){
				htmlItem += result.chineseName+'</span>';
				htmlItem += '&nbsp;&nbsp;<span>';
				htmlItem += '<input type="checkbox" onclick="changeCheckboxValue(this)" name="batchKey" id="batchKey'+createItemCount+'" value="0">是否唯一批次约束项</span>';
			}
		}
		if(isBasic=='1'){//不显示 是唯一批次约束项
			if(type == '1'){
				htmlItem += result.itemChineseName;
				
				if(result.isTemporary=='1'){ 
					htmlItem += '(临时指标项)</span>';
				}else{
					htmlItem += '</span>';
				}
			}
			if(type == '0'){
				htmlItem += result.chineseName+'</span>';
			}
		}
		
		
		htmlItem += '</div>';
		
		htmlItem += '<div class="pButtons">';
		htmlItem+='  <a href="javascript:void(0);" onclick="toUp('+createItemCount+')"  title="向上移动" name="squp'+createItemCount+'"><img src="${ctx}/theme/portal/images/up.gif" width="15" height="15" /></a>';
		htmlItem+='  <a href="javascript:void(0);" onclick="toDown('+createItemCount+')" title="向下移动" name="sqdown'+createItemCount+'"><img src="${ctx}/theme/portal/images/down.gif" width="15" height="15" /></a>';
		htmlItem+='  <a href="javascript:void(0);" onclick="deleteItem('+createItemCount+')" title="删除" name="sqdel'+createItemCount+'"';
		if(type==1){
			htmlItem += 'id="'+result.itemName+'" ><img src="${ctx}/theme/portal/images/delete.gif" width="15" height="15" /></a>';
		}
		if(type==0){
			htmlItem += 'id="'+result.name+'" ><img src="${ctx}/theme/portal/images/delete.gif" width="15" height="15" /></a>';
		}
		
		htmlItem+=' </div> ';
		htmlItem+='</div>  ';
		
		htmlItem+=' <div class="pOptions"> ';
		htmlItem+='   <div class="pOption">';
		htmlItem += '<table class="formContent" style="width:710px;padding:0px 0px 5px 0px;"> <tbody> <tr> <td class="fLabel" style="width:12%">';
		htmlItem += '指标名称:</td><td class="fInput" style="width:30%" ><input class="txtInput  required valid"readonly="readonly"  maxlength="30" style="width:176px;background-color: rgb(233, 233, 233);" id="itemName'+createItemCount+'"  name="itemName"';
		if(type == 1){
			htmlItem += 'value="'+result.itemName+'" /></td>';
			itemList.push(result.itemName);
		}
		if(type == 0){
			htmlItem += 'value="'+result.name+'" /></td>';
		}
		
		
		htmlItem += '<td class="fLabel" style="width:12%">';
		htmlItem += '指标中文名称:</td><td class="fInput" style="width:30%"><input class="txtInput  required" maxlength="30" style="width:176px" name="itemChineseName" id="itemChineseName'+createItemCount+'"';
		if(type == 1){
			htmlItem += 'value="'+result.itemChineseName+'"/></td></tr>';
		}
		if(type == 0){
			htmlItem += 'value="'+result.chineseName+'"/></td></tr>';
		}
		
		
		htmlItem += '<tr style="display:none"><td class="fLabel">字段类型:</td>';
		htmlItem += '<td class="fInput"><select name="fieldType" id="fieldType'+createItemCount+'" style="width:180px;">';
		
		$.each(fieldTypes, function(value, text){
			htmlItem += '<option value="' + value + '"';
			if (result.fieldType == value){
				htmlItem += ' selected="selected"';
			}
			htmlItem += '>' + text + '</option> ';
		});
		
		htmlItem += '</select> </td>';
		
		htmlItem += '<td class="fLabel">字段长度:</td>';
		htmlItem += '<td class="fInput"><input class="txtInput number required" maxlength="5" style="width:176px" value="'+result.fieldLength+'" name="fieldLength" id="fieldLength'+createItemCount+'" /></td></tr>';
		
		htmlItem += '<tr><td class="fLabel">表达式类型:</td>';
		htmlItem += '<td><select class="required" style="width:180px;" name="expressionType" id="expressionType'+createItemCount+'">';
		
		$.each(expressionTypes, function(value, text){
			htmlItem += '<option value="' + value + '"';
			if (result.expressionType == value){
				htmlItem += ' selected="selected"';
			}
			htmlItem += '>' + text + '</option> ';
		});
		
		htmlItem += '</select></td>';
		
		htmlItem += '<td class="fLabel">条件依赖:</td>';
		htmlItem += '<td class="fInput"><input class="txtInput valid" maxlength="200" style="width:176px" id="dependency'+createItemCount+'" name="dependency"';
		if(type == 1){
			htmlItem += 'value="'+result.dependency+'" ></td></tr>';
		}
		if(type == 0){
			htmlItem += '></td></tr>';
		}
		
		htmlItem += '<tr><td class="fLabel">指标项取值:</td>';
		htmlItem += '<td colspan="3" class="fInput"><input class="txtInput valid" maxlength="200" style="width:176px" id="valuedColumn'+createItemCount+'" name="valuedColumn"';
		if(type == 1){
			htmlItem += 'value="'+result.valuedColumn+'" ></td></tr>';
		}
		if(type == 0){
			htmlItem += '></td></tr>';
		}

		
		
		htmlItem += '<tr><td class="fLabel">';
		
		if(type == 1){
			htmlItem += '<input type="hidden" class="txtInput  required valid" style="width:176px" name="orders" id="orders'+result.statOrder+'"';
			htmlItem += 'value="'+result.statOrder+'" >';
			htmlItem += '<input type="hidden" class="txtInput  required valid" style="width:176px" name="isTemporary" id="isTemporary'+createItemCount+'"';
			htmlItem += 'value="'+result.isTemporary+'" >';
		}
		if(type == 0){
			htmlItem += '<input type="hidden" class="txtInput  required valid" style="width:176px" name="orders" id="orders'+createItemCount+'"';
			addItemCount++;
			htmlItem += 'value="'+addItemCount+'" >';
			
			htmlItem += '<input type="hidden" class="txtInput  required valid" style="width:176px" name="isTemporary" id="isTemporary'+createItemCount+'"';
			htmlItem += 'value="0" >';
		}
		
		if(type == 0){
			htmlItem += '表达式:</td><td colspan="3" class="fInput"><textarea class="txtInput valid" name="expression" id="expression'+createItemCount+'" style="width:530px;height:50px;resize:none">' + result.defaultExpression;
			htmlItem += '</textarea></td></tr>';
		}
		if(type == 1){
			htmlItem += '表达式:</td><td colspan="3" class="fInput"><textarea class="txtInput valid" name="expression" id="expression'+createItemCount+'" style="width:530px;height:50px;resize:none">'+ result.expression;
			htmlItem += '</textarea></td></tr>';
		}
		
		htmlItem += '</tbody></table>';
		
		htmlItem+='</div>  ';
		htmlItem+='</div>  ';
		htmlItem+='';
		
		createItemCount++;
		
		return htmlItem;
		
	}
	
	function changeCheckboxValue(checkbox){
		var defaultValue = $(checkbox).val();
		if(defaultValue=='0'){
			$(checkbox).val('1');
		}else{
			$(checkbox).val('0');
		}
	}
	
	var my=function(data){
		messager.message(data, function(){ window.parent.location.reload(); });
	};
	
	

</script>
</body>
</html>