
function charts(text,subtext,a,b,c,col){
		var result = [];
		for(var i = 0;i<c.length;i++){
			var seObject = {};
			seObject.name = a[i];
			seObject.type = "bar";
			seObject.data = c[i];
//			seObject.barWidth= 10;
			seObject.itemStyle = {};
			seObject.itemStyle.normal  = {color:col[i]};
//			seObject.barGap = '5%';
//			seObject.barCategoryGap = '5%';
			seObject.itemStyle.normal.label  = {show: true, position: 'top'};
//			seObject.itemStyle.emphasis={borderWidth: 5}
			
			seObject.markPoint = {};
			seObject.markPoint.data = [];
			seObject.markPoint.data.push({type:'max',name:'最大值'});
			seObject.markPoint.data.push({type:'min',name:'最小值'});
//			seObject.markLine = {};
//			seObject.markLine.data = [];
//			seObject.markLine.data.push({type:'average',name:'平均值'});
	

			result.push(seObject);
		}
    var    options = {
		    title : {
		        text: text,   //主标题文本
		        subtext: subtext	   //副标题文本超链接
		    },
		    tooltip : {                         //气泡提示框，常用于展现更详细的数据
		        trigger: 'item'                 //trigger气泡类型    可选为item 和 axis
		    },
		    legend: {                            // legend 图例，表述数据和图形的关联
		        data: a
		    },
//		    toolbox: {                         	//toolbox 辅助工具箱，辅助功能
//		    	orient: 'horizontal',             // 布局方式，默认为水平布局，可选为：'horizontal' | 'vertical' 
//		        show : false,
//		        feature : {
//		            mark : {show: true,
//		                lineStyle : {
//		                  //  width : 1,
//		                    color : 'red',
//		                    type : 'dashed'
//		                }
//		            },	     //mark，辅助线标志
//		            dataZoom : {show : true},     //dataZoom，框选区域缩放，自动与存在的dataZoom控件同步
//		            dataView : {show: true, readOnly: false},  //dataView，数据视图
//					magicType : {show: true,type: ['line', 'bar','stack','tiled']},//magicType，动态类型切换，支持直角系下的折线图(line)、柱状图(bar)、堆积(stack)、平铺转换(tiled)
//		            restore : {show: true},  //restore，还原，复位原始图表
//		            saveAsImage : {show: true}  //saveAsImage，保存图片
//		        }
//		    },
//		    calculable : false,    //是否启用拖拽重计算特性，默认关闭
//		    animation  : true,	 //是否开启动画，默认开启
		    xAxis : [
		        {
		            type : 'category',  //类目型 和数值型两种
		            //boundaryGap : false,  //类目起始和结束两端空白策略
		            data : b
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            axisLabel : { //坐标轴文本标签
		            	show: true,
		                formatter: '{value}'//formatter 格式化文本标签  textStyle  控制文本样式
		            },
		        }   
		    ],
		    series : result
			};
	
    return options;
}		


