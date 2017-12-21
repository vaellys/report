<!--
/*===========================================================================*/
/*   (c)copyright 2000 FreeSky & zibingyu                                        */
/*   Email: zibingyu@gmail.com                                                  */
/*   Web: http://www.ooohome.com/                                             */
/*   QQ: 181719418                                                           */
/*   Msn: zibingyu@live.com                                            */
/*===========================================================================*/

/*===========================================================================
 * 功能：基本功能API；
 * 版本：v1.0；
 *===========================================================================
 * 调用方式:
 *     <script type="text/javascript" src="路径/global.js"></script>
 *===========================================================================*/
//给string对象添加trim方法
String.prototype.trim=function(){
        return this.replace(/(^\s*)|(\s*$)/g, "");
}
//给string对象添加ltrim方法
String.prototype.ltrim=function(){
        return this.replace(/(^\s*)/g,"");
}
//给string对象添加rtrim方法
String.prototype.rtrim=function(){
        return this.replace(/(\s*$)/g,"");
}
//替换所有的字符串
String.prototype.replaceAll = function(s1,s2){
	return this.replace(new RegExp(s1,"gm"),s2);
}

// 功能：检测指定值是否为空；
function isEmpty(value){
	return (value.trim().length == 0);
}
//===========================================================================【底层校验函数】
// 功能：检测指定值是否为数字；
function isNumber(value){
  var reg=new RegExp("[0-9]+$","gi");
  return reg.test(value);
}

// 功能：检测指定值是否为合法 Email 地址；
function isEmail(str){
    var exp =("^([.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$");
    var matchArray = str.match(exp);
    if (matchArray == null) {
       return false;
    }
	return true;
}

// 功能：检测指定值是否为合法 URL 地址；
function isUrl(str){
	var reg=new RegExp("\[http://]?[www\\.]?((\\w)+\\.)+[a-z]{2,3}[/]?$","gi");
	return reg.test(str);
}

// 功能：检测指定值是否包含非法字符( ’，*，%，&，| )；
function isValid(str){
	str = str.trim();
	var badstr = "`~!@#$%^&*()_+-={}[]:\";'<>?,./￥！·（）×＆％＃＠～";
	for(var i=0;i < str.length;i++) {
		var c = str.charAt(i);//字符串str中的字符
		if(badstr.indexOf(c) == -1){
			return true;
		}
	}
	return false;
}
//是否是英文字符，且只是字母，数字，下划线组合
function isEnString(str){
	var exp = /^[a-zA-Z0-9]+[_\da-zA-Z]+$/;
	return exp.test(str);
}

//是否只为中文
function isOnlyCnString(str){
	return str.replace(/[\u4E00-\u9FA5]/g,'')=="";
}
//只能是中文或字母
function isCnOrEn(str){
	return str.replace(/[\u4E00-\u9FA5a-zA-Z]/g,'')=="";
}

//中文判断函数，允许生僻字用英文“*”代替
function containChineseChar(str){
	//alert("containChineseChar");
	var badChar ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	badChar += "abcdefghijklmnopqrstuvwxyz";
	badChar += "0123456789";
	badChar += " "+"　";//半角与全角空格
	badChar += "`~!@#$%^&()-_=+]\\|:;\"\\'<,>?/";//不包含*或.的英文符号
	if(""==str || str==null){
		return false;
	}
	for(var i=0;i < str.length;i++) {
		var c = str.charAt(i);//字符串str中的字符
		if(badChar.indexOf(c) == -1){
			return true;
		}
	}
	return false;
}
// 功能：去除 HTML 标记；
function stripHtml(text){
    return text.replace(/<[^>]*>/g,"");
}

//检查是否为手机号
function isMobile(str){
	if(!str)
		return false;
	if(str.length != 11)
		return false;
    if(!isNumber(str))
    	return false;
    if(str.indexOf("13") == 0 || str.indexOf("14") == 0 || str.indexOf("15") == 0 || str.indexOf("18") == 0)
    	return true;
    return false;
}

function newWindow(mypage, myname, w, h, scroll, toolbar,location){
	var nLeftPos = (screen.width) ? (screen.width-w)/2 : 0;
	var nTopPos = (screen.height) ? (screen.height-h)/2 : 0;
	var settings = 'height='+h+',width='+w+',top='+nTopPos+',left='+nLeftPos+',status=yes,resizable';
	if (scroll == null || scroll == "") {
		scroll = "yes";
	}
	if (location == null || location == "") {
		location = "no";
	}
	settings += ',location=' + location;
	settings += ',scrollbars' + (scroll == 'no' ? '=no' : '');
	if (toolbar) {
		settings += ',toolbar=1';
	}
	var win = window.open(mypage, myname, settings);
	try{
		win.focus();
	}catch(exception){}
}

//检测身份证号码 18位
function isIdcard(idcard)
{ 
  var Errors=new Array( "验证通过!", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!", "身份证号码校验错误!", "身份证地区非法!"); 
  var area = {11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}  
  var idcard,Y,JYM; 
  var S,M; 
  var idcard_array = new Array(); 
  idcard_array = idcard.split(""); 
  //地区检验 
  if(area[parseInt(idcard.substr(0,2))] == null) 
	 return Errors[4];
  //身份号码位数及格式检验 
  switch(idcard.length)
  { 
//    case 15: 
//    if((parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 ))
//    { 
//      ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性 
//    } 
//    else 
//    { 
//      ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性 
//    } 
//    if(ereg.test(idcard)) 
//    {
//      return Errors[0];
//      return true;
//    } 
//    else 
//    {
//      return Errors[2];
//    } 
//    break; 
    case 18: 
    //18位身份号码检测 
    //出生日期的合法性检查  
    //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9])) 
    //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8])) 
    if ( parseInt(idcard.substr(6,4)) % 4 == 0 || (parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 ))
    { 
      ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式 
    } 
    else 
    { 
      ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式 
    } 
    if(ereg.test(idcard))
    {
      //测试出生日期的合法性 
      //计算校验位 
      S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7 
      + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9 
      + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10 
      + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5 
      + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8 
      + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4 
      + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2 
      + parseInt(idcard_array[7]) * 1  
      + parseInt(idcard_array[8]) * 6 
      + parseInt(idcard_array[9]) * 3 ; 
      Y = S % 11; 
      M = "F"; 
      JYM = "10X98765432"; 
      M = JYM.substr(Y,1);//判断校验位 
      if(M == idcard_array[17]) 
      {
        return Errors[0]; //检测ID的校验位
      }
      else
      {
         return Errors[3];
      } 
    } 
    else 
    {
      return Errors[2];
    }
    break; 
    default: return Errors[1]; 
  break; 
  }
}

//小数点位数截取，digit带小数点的数字，length精确到几位
var Digit = {};
Digit.round = function(digit, length) {
	length = length ? parseInt(length) : 0;
	if (length <= 0) return Math.round(digit);
	digit = Math.round(digit * Math.pow(10, length)) / Math.pow(10, length);
	return digit;
};
