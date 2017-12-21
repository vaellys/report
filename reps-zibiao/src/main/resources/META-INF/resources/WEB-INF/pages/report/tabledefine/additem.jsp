<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>添加指标</title>
	<reps:theme />
<style type="text/css">
	#item{width:940px;margin:0px auto;padding-bottom:10px;}
	#top{background:#E5EDEF;height:60px;padding:10px 0;line-height:12px;border-top:1px #cddfed solid;border-left:1px #cddfed solid;border-right:1px #cddfed solid;}
	#top .title{text-align:center;}
	#top .title h1{text-align:center;font-family:"微软雅黑";font-weight:normal;font-size:18px;}
	
	#bottom{border:1px #cddfed solid;}
	.block{border-left:1px #cddfed solid;border-right:1px #cddfed solid;}
	.blockTop{ height:20px;font-weight:bold;background-color:#efefef;padding:6px 5px 5px 5px; border-top:1px #cddfed solid;}
	.blockTop .blockInfo{float:left;width:700px;height:18px;font-size: 14px}
	.blockTop .blockButton{float:right;width:150px;height:18px;text-align: right}
	.blockTop .blockButton a{margin-left: 5px;}
	
	.praxis{}
	.pTop{border-bottom:1px #cddfed solid;border-top:1px #cddfed solid;height:20px;padding:6px 5px 5px 5px;background:#E5EDEF;}
	.pTitle{float:left;width:700px;height:18px;font-size: 13px}
	.pButtons{float:right;width:200px;height:18px;text-align: right}
	.pButtons a{margin-left: 5px;}
	
	.pContent{padding:5px;word-break:break-all;line-height:18px;}
	.pOptions{padding:5px;word-break:break-all;}
	.pOption{line-height:15px;}
</style>
</head>
<body>
	<reps:container>
		<reps:panel id="myform" dock="none">
			<div id="item">
				<form action="saveitem.mvc" method="post" id="itemForm">
					<input id="paperId" name="tjTableDefine.id" type="hidden" value="${tableDefine.id}"/>
					<div id="top">
						<div class="title"><h1>${tableDefine.chineseName}:添加指标项</h1></div>
					
						<div align="right" style="padding:15px 0 0 0;">
		      	    		<reps:dialog type="link" id="chooseItem"  width="800" height="500" iframe="true" cssClass="small_btn_add_a"
			      	        	url="toitemlist.mvc?tId=${tableDefine.id}" closeCall="true" value="添加指标项"  title="添加指标项" mask="true"/>
			      		</div>
		      		</div>
		      		
		      		<div class="all">
			      		<div class="block" id="block">
		      				<div class="blockTop">
		      					<div class="blockInfo">
		      						<span name="bIndex">1、</span>已添加<span id="cntstbpraxis_${tableDefine.id}">${itemCount}</span>个指标项
		      					</div>
		      				</div>
			      		</div>
		      		</div>
		      		<reps:formbar>
			       		<reps:button id="btnSave" cssClass="btn_save" type="button" onClick="submitcheck()" messageCode="add.button.save" />
			            <reps:button cssClass="btn_cancel" type="button" onClick="back()" messageCode="add.button.cancel" />
       				</reps:formbar>
					<br><br>
				</form>
			</div>
		</reps:panel>
	</reps:container>	
	<script type="text/javascript">
	
	$(function(){
		//删除
      $("a[name='sqdel']").live("click", function(evt){
		   var thisobj = $(this);
		   deletePraxis(thisobj);
	  });
		//上移
	  $("a[name='squp']").live("click", function(evt){
		  var thisobj = $(this);
		  upPraxis(thisobj);
	  });
		
	//下移
	 $("a[name='sqdown']").live("click", function(evt){
		  var thisobj = $(this);
		  downPraxis(thisobj);
	 });
	});
	
	//删除
	function deletePraxis(linkObj){
		var curDiv=linkObj.parent().parent().parent();
		var praxisDiv=curDiv.parent();
		curDiv.remove();
		
	}
	
	//下移
	function downPraxis(linkObj){
		var curDiv=linkObj.parent().parent().parent();//取得当前整个内容
		var nextDiv=curDiv.next();//下一个
		if(nextDiv.length==0){
			alert("已经是最后一个了");
			return;
		}
		nextDiv.after(curDiv);//移动
		//替换
		var pIndex1=curDiv.find("span[name='pIndex']").eq(0);//
		var pIndex2=nextDiv.find("span[name='pIndex']").eq(0);//
		var val1=pIndex1.text();
		var val2=pIndex2.text();
		pIndex1.text(val2);
		pIndex2.text(val1);
	}
	
	//上移
	function upPraxis(linkObj){
		debugger;
		var curDiv=linkObj.parent().parent().parent();//取得当前整个内容
		var prevDiv=curDiv.prev();//上一个
		if(prevDiv.length==0 || prevDiv.hasClass("blockTop")){
			alert("已经是第一个了");
			return;
		}
		prevDiv.before(curDiv);//移动
		//替换
		var pIndex1=curDiv.find("span[name='pIndex']").eq(0);//
		var pIndex2=prevDiv.find("span[name='pIndex']").eq(0);//
		var val1=pIndex1.text();
		var val2=pIndex2.text();
		pIndex1.text(val2);
		pIndex2.text(val1);
	}
	
	function back() 
	{
		window.location.href= "list.mvc";
	}
	
	function checkedField(field){
		var isChecked = 0;
		$.each($("input[name="+field+"]"),function(index, obj){
			if($(obj).val().trim() == ""){
				messager.info("不能为空！");
				isChecked = 1;
				return;
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
			if(field == "fieldLength" || field == "orders"){
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
			if(field == "dependency" && $(obj).val().trim().length > 200){
				messager.info("条件依赖长度过长");
				isChecked = 1;
				return;
			}
			if(field == "expression" && $(obj).val().trim().length > 500){
				messager.info("表达式长度过长");
				isChecked = 1;
				return;
			}
		});
		return isChecked;
	}
	
	function submitcheck(){
		var block = $("#block div[class=praxis]");
		var isSubmit = 0;
		var arr=new Array();
		arr=["dependency","itemName","itemChineseName","fieldLength","expression","orders"];
		
		for(var i=0; i<arr.length; i++){
			isSubmit = checkedField(arr[i]);
			if(isSubmit == 1){
				break;
			}
		}
		
		
		if(isSubmit == 1){
			return false;
		}
		if(block.length>0){
			$("#itemForm").submit();
			return true;
		}
		
		return false;
	    
	}
	
	function chooseAddItem(id){
		addItem(id);
	}
	
	/**
	 * 添加指标项
	 */
	function addItem(id){
		var curPraxis=$("#block");
	   $.ajax({
			url:"chooseitem.mvc",
			type:"post",
			data:{id:id},
			async:false,
			success:function(result){
				//var result = $.parseJSON(result);
				var innerHtml = createItem(result);
				var div = '<div class="praxis">';
				div += '<table class="formContent"> <tbody>';
				div += innerHtml;
				div += '</tbody></table>';
				div += '</div>';
				curPraxis.append(div);
		 		$.pdialog.closeDialog();
		 		REPS.reloadDialogJs();
			}
	   });
	}
	
	function createItem(result){
		//组装html
		var htmlItem='';
		htmlItem += '<div class="pTop">';
		htmlItem +='<div class="pTitle">';
		htmlItem += '<span name="itemName">'+result.chineseName+'</span>';
		htmlItem += '</div>';
		
		htmlItem += '<div class="pButtons">';
		htmlItem+='  <a href="javascript:void(0);" title="向上移动" name="squp"><img src="${RepsPath}/images/up.gif" width="15" height="15" /></a>';
		htmlItem+='  <a href="javascript:void(0);"  title="向下移动" name="sqdown"><img src="${RepsPath}/images/down.gif" width="15" height="15" /></a>';
		htmlItem+='  <a href="javascript:void(0);"  title="删除" name="sqdel"><img src="${RepsPath}/images/delete.gif" width="15" height="15" /></a>';
	    htmlItem+=' </div> ';
	    htmlItem+='</div>  ';
	    
	    htmlItem+=' <div class="pOptions"> ';
	    htmlItem+='   <div class="pOption">';
	    htmlItem += '<table class="formContent" style="width:800px;"> <tbody> <tr> <td class="fLabel" style="width:18%">';
	    htmlItem += '指标名称:</td><td class="fInput" style="width:30%" ><input class="txtInput  required valid" maxlength="50" style="width:176px" value="'+result.name+'" name="itemName" /></td>';
	    htmlItem += '<td class="fLabel" style="width:18%">';
	    htmlItem += '指标中文名称:</td><td class="fInput" style="width:30%"><input class="txtInput  required" maxlength="30" style="width:176px" value="'+result.chineseName+'" name="itemChineseName" /></td></tr>';
	    
	    htmlItem += '<tr><td class="fLabel">';
	    htmlItem += '字段类型:</td><td class="fInput"><select name="fieldType" style="width:180px;">';
	    htmlItem += '<option value="varchar" ';
	    if(result.fieldType == "varchar"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += '  >字符型（单字节）</option> ';
	    
	    htmlItem += '<option value="nvarchar" ';
	    if(result.fieldType == "nvarchar"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >字符型（双字节）</option> ';
		    
	    htmlItem += '<option value="byte" ';
	    if(result.fieldType == "byte"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >超短整型</option> ';
	    
	    htmlItem += '<option value="short" ';
	    if(result.fieldType == "short"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >短整型</option> ';
	    
	    htmlItem += '<option value="int" ';
	    if(result.fieldType == "int"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >整型</option> ';
	    
	    htmlItem += '<option value="long" ';
	    if(result.fieldType == "long"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >长整型</option> ';
	    
	    htmlItem += '<option value="numeric" ';
	    if(result.fieldType == "numeric"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >数字型</option> ';
	    
	    htmlItem += '<option value="datetime" ';
	    if(result.fieldType == "datetime"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >时间型</option> ';
	    htmlItem += '</select> </td>';
	    
	    htmlItem += '<td class="fLabel">';
	    htmlItem += '字段长度:</td><td class="fInput"><input class="txtInput number required" maxlength="5" style="width:176px" value="'+result.fieldLength+'" name="fieldLength" /></td></tr>';
	    
	    htmlItem += '<tr><td class="fLabel">';
	    htmlItem += '表达式类型:</td><td><select class="required" style="width:180px;" name="expressionType">';
	    
	    htmlItem += '<option value="SQL" ';
	    if(result.expressionType == "SQL"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >SQL</option> ';
	    
	    htmlItem += '<option value="变量" ';
	    if(result.expressionType == "变量"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >变量</option> ';
	    
	    htmlItem += '<option value="运算" ';
	    if(result.expressionType == "运算"){
	    	htmlItem += 'selected="selected"';
	    }
	    htmlItem += ' >运算</option> ';
	    htmlItem += '</select></td>';
	    
	    htmlItem += '<td class="fLabel">';
	    htmlItem += '条件依赖:</td><td class="fInput"><input class="txtInput  required valid"  maxlength="200" style="width:176px" name="dependency"></td></tr>';
	    
	    htmlItem += '<tr><td class="fLabel">';
	    htmlItem += '统计顺序:</td><td colspan="3" class="fInput"><input class="txtInput  required valid" style="width:176px" name="orders"></td></tr>';
	    
	    htmlItem += '<tr><td class="fLabel">';
	    htmlItem += '表达式:</td><td colspan="3" class="fInput"><textarea class="txtInput valid" name="expression" maxlength="500" style="width:570px;height:50px;resize:none">' + result.defaultExpression;
	    htmlItem += '</textarea></td></tr>';
	    htmlItem += '</tbody></table>';
	    
	    htmlItem+='</div>  ';
	    htmlItem+='</div>  ';
	    htmlItem+='';
	    
	    return htmlItem;
		
	}
	</script>
</body>
</html>