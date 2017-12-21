(function($){
	// jQuery validate
	$.extend($.validator.messages, {
		required: "必填字段",
		remote: "请修正该字段",
		email: "请输入正确格式的电子邮件",
		url: "请输入合法的网址",
		date: "&nbsp;&nbsp;&nbsp;&nbsp;请输入合法的日期",
		dateISO: "请输入合法的日期 (ISO).",
		number: "请输入合法的数字",
		digits: "只能输入整数",
		creditcard: "请输入合法的信用卡号",
		equalTo: "请再次输入相同的值",
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: $.validator.format("长度超长，最多是{0}个字符"),
		minlength: $.validator.format("长度不够，最少是{0}个字符"),
		rangelength: $.validator.format("长度介于 {0} 和 {1} 之间"),
		range: $.validator.format("长度介于 {0} 和 {1} 之间"),
		max: $.validator.format("请输入一个最大为 {0} 的值"),
		min: $.validator.format("请输入一个最小为 {0} 的值"),
		
		alphanumeric: "字母、数字、下划线",
		lettersonly: "必须是字母",
		phone: "请输入正确的号码 ",
		mobile: "请输入正确的手机号",
		chinese:"请输入中文字符",
		password:"6-18位的字符组成",
		identifycode:"请输入有效的身份证",
		integernum:"不小于0的整数",
		chinesealphanumeric:"中文、字母、数字、下划线",
		postcode:"请输入正确的邮编",
		moneyvalidate:"不小于0的数",
		filename:"请输入正确的文件名称",
		contactinformation:"请输入正确的联系方式",
		idcard:"身份证号有误"
	});
	
	//regional
	$.setRegional("datepicker", {
		dayNames: ['日', '一', '二', '三', '四', '五', '六'],
		monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
	});
	$.setRegional("messager", {
		title:{error:"错误", info:"提示", warn:"警告", correct:"成功", confirm:"确认提示"},
		butMsg:{ok:"确定", yes:"是", no:"否", cancel:"取消"},
		ajaxError : "系统连接出错，请稍后访问！",
		overFileSizeFlag : "上传文件大小不能超过100M!",
		containFileType : "上传文件类型不对，请上传以下后缀的文件：",
		validateNoPass:"请填写必填项！"
	});
	
	$.setRegional("pagination", {
		first : "首页",
		previous :"上一页",
		next :"下一页",
		end : "末页",
		confirm :"确定",
		allRecords : "条，共",
		record : "条",
		currentPage:"已经是当前页!",
		overPages:"请输入分页范围内的页码!"
	});
	$.setRegional("compare", {
		novalue : "无记录"
	});
})(jQuery);