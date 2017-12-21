<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/commons/tags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${title}</title>
	<link rel="stylesheet" href="${ctx}/theme/portal/css/bootstrap.min.css" />
	<link rel="stylesheet" href="${ctx}/theme/portal/css/ionicons.min.css" />
	<link rel="stylesheet" href="${ctx}/theme/portal/css/filetypes.css" />
	<link rel="stylesheet" href="${ctx}/theme/portal/css/base.css" />
	<script type="text/javascript" language="javascript" src="${ctx}/library/base/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" language="javascript" src="${ctx}/library/base/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx}/library/echarts/build/echarts-plain.js"></script>
	<script type="text/javascript" src="${ctx}/library/echarts/theme/blue.js"></script>
</head>
<body onload="load()">
	<!--s-top-->
	<div class="s-top">
		<div class="s-topCon clear">
			<h1 class="fl">数据管理控制中心<br><b>Data Center Management</b></h1>
				<c:if test="${fn:trim(userid)==''}">
					<a class="fr" href="${ctx}/login.do" ><span><img src="${ctx}/theme/portal/images/login.png"></span>请登录</a>
				</c:if>
				<c:if test="${fn:trim(userid)!=''}">
					<ul class="s-conWR-top">
						<li class="s-conWR-exit"><a href="${ctx}/logout?refer=<%=basePath%>"><span><img src="${ctx}/theme/portal/images/exit01.png"></span>退出</a></li>
						<li style="color:#fff;"><span><img src="${ctx}/theme/portal/images/touxiang.png"></span>${organizeName}${name}<span class="s-personBg01"><img src="${ctx}/theme/portal/images/jiantou01.png"></span></li>
						<%--<c:if test="${username=='admin'}">
							<li>
								<a href="${ctx}/admin.mvc" target="_blank">
									<span><img src="${ctx}/theme/portal/images/houtai01.png"></span>进入后台管理
								</a>
							</li>
						</c:if>
					--%></ul>
				</c:if>
		</div>
	</div>
	<!--s-nav-->
	<div class="s-nav">
		<div class="s-navCon">
			<ul class="s-navConL fl">
				<li class="s-navCon-cur"><a href="${ctx}/index.jsp"><span class="s-navtu1"></span>首页</a>
					<b class="s-navSan"><img src="${ctx}/theme/portal/images/nav_san.png"></b>
				</li>
				<li><a href="${ctx}/forward/datastandard_forward_list.jsp"><span class="s-navtu2"></span>数据标准</a></li>
				<li><a href="${ctx}/forward/datastandard_forward_objectrelation.jsp"><span class="s-navtu3"></span>对象关系模型</a></li>
			</ul>
			<%--<ul class="s-navConR fr">
				<li class="s-navGreen"><a href="#"><span><img src="${ctx}/theme/portal/images/api.png"></span>API说明</a></li>
				<li class="s-navConSpan"><a href="#"><span><img src="${ctx}/theme/portal/images/help.png"></span>帮助</a></li>
			</ul>
		--%></div>
	</div>
	<!--s-con1-->
	<div class="s-con1 clear">
		<h2><span class="s-con01"></span>基础数据</h2>
		<ul class="s-con1Con">
			<li class="s-con1Bg">
				<h3><span><img src="${ctx}/theme/portal/images/con1tu1.png"></span><b>${school_count}</b></h3>
				<p>学校数量</p>
			</li>
			<li class="s-con01Blue s-con2Bg">
				<h3><span><img src="${ctx}/theme/portal/images/con1tu2.png"></span><b>${classes_count}</b></h3>
				<p>班级数量</p>
			</li>
			<li class="s-con01Red s-con3Bg">
				<h3><span><img src="${ctx}/theme/portal/images/con1tu3.png"></span><b>${teacher_man_count+teacher_woman_count}</b></h3>
				<p>教职工数量</p>
			</li>
			<li class="s-con01Orange s-con4Bg" style="margin-right:0;">
				<h3><span><img src="${ctx}/theme/portal/images/con1tu4.png"></span><b>${student_boy_count+student_girl_count}</b></h3>
				<p>学生数量</p>
			</li>
		</ul>
	</div>
	<!--s-con2-->
	<div class="s-con2 clear" style="height:390px;">
		<iframe id="everyXueDuanAnalyse" width="100%" height="410px" fit="true" border="0" frameborder="0"></iframe>
	</div>
	
	<!-- 近5年中小学学生数量变化情况 -->
	<div class="s-con3 clear" style="height:1240px;">
		<iframe id="fiveyearstudentcount" width="100%" height="1240px" fit="true" border="0" frameborder="0"></iframe>
	</div>
	<!--s-con4-->
	<div class="s-con4 clear" style="margin: -1px auto 0">
		<h2><span class="s-con401"></span>幼儿园学校情况</h2>
		<div class="s-con4Con">
			<div class="s-con4L fl">
				<!--幼儿园办别统计  -->
				<iframe id="iframeYeybb" style="float:left;" width="100%" scrolling="no" height="100%" fit="true" border="0" frameborder="0"></iframe>
			</div>
			<div class="s-con4R fl">
				<!--幼儿园城乡类型统计  -->
				<iframe id="iframeYeycxlx" style="float:left;" width="100%" scrolling="no" height="100%" fit="true" border="0" frameborder="0"></iframe>
			</div>
		</div>
	</div>
	<!--s-con5-->
	<div class="s-con5 clear">
		<h2><span class="s-con501"></span>数据交换流量</h2>
		<div class="s-con5Con">
			<ul>
				<li><span></span>当天记录数<input id="dayNum" type="text" value="0"></li>
				<li class="s-con-input2"><span></span>月记录数<input id="monthNum" type="text" value="0"></li>
				<li class="s-con-input3"><span></span>年记录数<input id="yearNum" type="text" value="0"></li>
				<li class="s-con-input4"><span></span>总记录数<input id="totalNum" type="text" value="0"></li>
			</ul>
			<h3>近30日处理数据统计</h3>
			<p><div id="dscLineChart" style="position:relative;height:300px;width:100%"></div></p>
		</div>
	</div>
	<!--s-con6-->
	<div class="s-con6 clear">
		<h2><span class="s-con601"></span>服务器监控</h2>
		<ul>
			<li style="width:20%">
				<h3 class="s-con6-blue">CPU使用情况</h3>
				<p><div id="cpuPie" style="width:100%;height:100%;"></div></p>
			</li>
			<li style="width:20%">
				<h3 class="s-con6-red">内存使用情况</h3>
				<p><div id="memPie" style="width:100%;height:100%;"></div></p>
			</li>
			<li style="width:20%">
				<h3 class="s-con6-yellow">JVM使用情况</h3>
				<p><div id="jvmPie" style="width:100%;height:100%;"></div></p>
			</li>
		</ul>
	</div>
<script type="text/javascript">
	function load() {
	    var $everyXueDuanAnalyse = $("#everyXueDuanAnalyse");
	    $everyXueDuanAnalyse.attr("src","${ctx}/forward/everyxueduananalyse_forward.jsp");
	    var $fiveyearstudentcount = $("#fiveyearstudentcount");
	    $fiveyearstudentcount.attr("src","${ctx}/forward/fiveyearstudentcount_forward.jsp");
	    var $iframeYeybb = $("#iframeYeybb");
	    $iframeYeybb.attr("src","${ctx}/forward/yeybb_forward.jsp");
	    var $iframeYeycxlx = $("#iframeYeycxlx");
	    $iframeYeycxlx.attr("src","${ctx}/forward/yeycxlx_forward.jsp");
	}
	$(function(){
		$("#loginId").hide();
		
		$.getJSON("${ctx}/monitor/total.do",function(rs){
			$("#dayNum").val(rs.dayNum);
			$("#monthNum").val(rs.monthNum);
			$("#yearNum").val(rs.yearNum);
			$("#totalNum").val(rs.totalNum);
		});
		
		var linchart = echarts.init(document.getElementById('dscLineChart')); 
		$.getJSON("${ctx}/monitor/dayschart.do",function(rs){
			linchart.setOption(rs);
		});
		
		var cpuPie = echarts.init(document.getElementById('cpuPie')); 
		var memPie = echarts.init(document.getElementById('memPie')); 
		var jvmPie = echarts.init(document.getElementById('jvmPie')); 
		$.getJSON("server.do",function(rs){
			var option1 = getOptions(rs.cpu.useCPU,rs.cpu.nouseCPU,0);
			cpuPie.setOption(option1);
			var useMem = parseInt(rs.mem.useMem);
			var freeMem = parseInt(rs.mem.freeMem);
			var useMemRate = (((useMem)/(useMem+freeMem))*100).toFixed(2);
			var option2 = getOptions(useMemRate,100-useMemRate,1);
			memPie.setOption(option2);
			var useJvm = parseInt(rs.jvm.useJvm);
			var freeJvm = parseInt(rs.jvm.freeJvm);
			var useJvmRate = (((useJvm)/(useJvm+freeJvm))*100).toFixed(2);
			var option3 = getOptions(useJvmRate,100-useJvmRate,2);
			jvmPie.setOption(option3);
		});
	});
	
	function getOptions(a,b,index){
		//定义图表option  
		var first = a+"%";
		var colorArr = ['#d87a80','#f8a20f','#0fc90a'];
		var showColor= new Array();
		showColor.push(colorArr[index]);
		option = {
			color:showColor,
		    title: {
		        text: first,
		        x: 'center',
		        y: 'center',
		        itemGap: 20,
		        textStyle : {
		            color : '#003322',
		            fontFamily : '微软雅黑',
		            fontSize : 20,
		            fontWeight : 'bolder'
		        }
		    },
		    series : [
		        {
		            type:'pie',
		            clockWise:false,
		            radius : [60,64],
		            itemStyle: {
						normal: {
							label: {show:false},
							labelLine: {show:false}
						}
					},
		            data:[
		                {
		                    value:a,
		                    name:'使用百分比:'
		                },
		                {
		                    value:b,
		                    name:'invisible',
		                    itemStyle:{
		                    	normal : {
		                            color: 'rgba(0,0,0,0)',
		                            label: {show:false},
		                            labelLine: {show:false}
		                        },
		                        emphasis : {
		                            color: 'rgba(0,0,0,0)'
		                        }
		                    }
		                }
		            ]
		        },
		    ]
		};
	return option;
	}

</script>
</body>
</html>