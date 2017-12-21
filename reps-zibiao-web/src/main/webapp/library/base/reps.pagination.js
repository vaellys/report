$(function(){
	$.fn.extend({
		pagination:function(opts){
			var initOpts = {"currentPage":1,"showPerPage":20,"sumPages":0,"formId":"myForm","numPages":10};
			this.each(function(){
				var $this = $(this);
				$.extend(initOpts,opts);
				initParameterInForm(initOpts);
				var interval = getInterval(parseInt(initOpts.currentPage),parseInt(initOpts.numPages),parseInt(initOpts.sumPages),parseInt(initOpts.showPerPage));	
				if(interval.start == interval.end){
					//no records
					$this.html("<li class='disabled'><a href='#'>«</a></li><li class='active -num'><a href='#'>1</a></li><li class='disabled'><a href='#'>»</a></li>");
					return false;
				}
				
				// append li
				$this.empty().append("<li class='-prev'><a href='#'>«</a></li>");
				for(var k = interval.start; k < interval.end; k++){
					if(k == initOpts.currentPage){
						$this.append("<li class='active -num'><a href='#'>"+k+"</a></li>");
					}else{
						$this.append("<li class='-num'><a href='#'>"+k+"</a></li>");
					}
				}
				$this.append("<li class='-next'><a href='#'>»</a></li>");
				
				//bind events
				
				//prev and next bind events
				if(hasPrev(initOpts.currentPage)){
					$("li.-prev").removeClass("disabled");
					$("li.-prev").find("a").unbind("click").on("click",function(){
						var i = parseInt(initOpts.currentPage);
						i = i - getInterval(parseInt(initOpts.currentPage),parseInt(initOpts.numPages),parseInt(initOpts.sumPages),parseInt(initOpts.showPerPage)).start - 1;
						$("li.-num").eq(i).find("a").trigger("click");
					});
				}else{
					// has not prev
					$("li.-prev").addClass("disabled");
				}
				if(hasNext(parseInt(initOpts.currentPage),parseInt(initOpts.sumPages),parseInt(initOpts.showPerPage))){
					$("li.-next").removeClass("disabled");
					$("li.-next").find("a").unbind("click").on("click",function(){
						var i = parseInt(initOpts.currentPage);
						i = i - getInterval(parseInt(initOpts.currentPage),parseInt(initOpts.numPages),parseInt(initOpts.sumPages),parseInt(initOpts.showPerPage)).start + 1;
						$("li.-num").eq(i).find("a").trigger("click");
					});
				}else{
					// has not next
					$("li.-next").addClass("disabled");
				}
				
				// num bind events 
				$this.find("li.-num").each(function(i){
					bindEvents($(this),i+getInterval(parseInt(initOpts.currentPage),parseInt(initOpts.numPages),parseInt(initOpts.sumPages),parseInt(initOpts.showPerPage)).start,initOpts);
				});
				
				
			});
			
			function getInterval(cur,numPages,sum,showPerPage){
				var ne_half = Math.ceil(numPages/2);//numPages -- for page footer ; showPerPage -- for show numbers in page
				var np = Math.ceil(sum/showPerPage);
				var upper_limit = np - numPages;
				var start = cur > ne_half ? Math.max( Math.min(cur - ne_half, upper_limit), 0 ) : 0;
				var end = cur > ne_half ? Math.min(cur+ne_half, np) : Math.min(numPages, np);
				return {start:start+1, end:end+1};
			}
			
			function bindEvents($target,cur,initOpts){
				$target.on("click",function(event){
					initOpts.currentPage = cur;
					var $form = $("#"+initOpts.formId);
					var curPage = $form.find("input[name='curPageNumber']");
					$(".pagination").attr("currentPage",cur);
					curPage.val(cur);
					
					//selected num
					$target.parent().parent().find(".-num").each(function(){
						$(this).removeClass("active");
					});
					$target.parent().addClass("active");
					var $form=$("#"+initOpts.formId);
					$.ajax({
						type:"post",
						url:$form.attr("action"),
						data:$form.serializeArray(),
						dataType:"html",
						async:false,
						cache:false,
						success:function(data){
							$("#tbody").html($(data).find("#tbody").html());
							$(".pagination").attr("currentPage",cur);
							$(".pagination").each(function(){
								var $this = $(this);
								$this.pagination({currentPage:$this.attr("currentPage"),showPerPage:$this.attr("showPerPage"),sumPages:$this.attr("sumPages"),formId:$this.attr("formId"),numPages:$this.attr("numPages")});
							});
						}
					});
					event.preventDefault();
				});
				
			}
			
			// hasPrev
			function hasPrev(cur){
				return cur > 1;
			}
			
			// hasPrev
			function hasNext(cur,sum,showPerPage){
				return cur <= (sum/showPerPage);
			}
			
			function initParameterInForm(initOpts){
				var $form = $("#"+initOpts.formId);
				var $curPageNumber = $form.find("input[name=curPageNumber]");
				var $pageSize = $form.find("input[name=pageSize]");
				var $totalRecord = $form.find("input[name=totalRecord]");
				if($curPageNumber.size() > 0){
					$curPageNumber.val(initOpts.currentPage);
				}else{
					$form.append('<input type="hidden" name="curPageNumber" value="'+initOpts.currentPage+'" />');
				}
				if($pageSize.size() > 0){
					$pageSize.val(initOpts.showPerPage);
				}else{
					$form.append('<input type="hidden" name="pageSize" value="'+initOpts.showPerPage+'" />');
				}
				if($totalRecord.size() > 0){
					$totalRecord.val(initOpts.sumPages);
				}else{
					$form.append('<input type="hidden" name="totalRecord" value="'+initOpts.sumPages+'" />');
				}
				return $form;
			}
		}
	});
});