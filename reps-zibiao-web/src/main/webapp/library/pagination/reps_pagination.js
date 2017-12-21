
	function pagination(pageObj,All_Record,curPageNumber,pageSize,callback){
			var initOpts = {"currentPage":1,"showPerPage":20,"sumPages":0,"formId":"objectForm","numPages":10};
			var $this = $(pageObj);
			$this.attr("class", "pagination");
			var interval = getInterval(parseInt(curPageNumber.val()),10,parseInt(All_Record),parseInt(pageSize));
			if(interval.start == interval.end){
				//no records
				$this.html("<li class='disabled'><a href='#'>«</a></li><li class='active -num'><a href='#'>1</a></li><li class='disabled'><a href='#'>»</a></li>");
				return false;
			}
			
			// append li
			$this.empty().append("<li class='-prev'><a href='#'>«</a></li>");
			for(var k = interval.start; k < interval.end; k++){
				if(k == curPageNumber.val()){
					$this.append("<li class='active -num'><a href='#'>"+k+"</a></li>");
				}else{
					$this.append("<li class='-num'><a href='#'>"+k+"</a></li>");
				}
			}
			$this.append("<li class='-next'><a href='#'>»</a></li>");
			
			if(hasPrev(curPageNumber.val())){
				$("li.-prev").removeClass("disabled");
				$("li.-prev").find("a").unbind("click").on("click",function(){
					var i = parseInt(curPageNumber.val());
					i = i - getInterval(parseInt(curPageNumber.val()),10,parseInt(All_Record),parseInt(pageSize)).start - 1;
					$("li.-num").eq(i).find("a").trigger("click");
				});
			}else{
				// has not prev
				$("li.-prev").addClass("disabled");
			}
			
			if(hasNext(parseInt(curPageNumber.val()),parseInt(All_Record),parseInt(pageSize))){
				$("li.-next").removeClass("disabled");
				$("li.-next").find("a").unbind("click").on("click",function(){
					var i = parseInt(curPageNumber.val());
					i = i - getInterval(parseInt(curPageNumber.val()),10,parseInt(All_Record),parseInt(pageSize)).start + 1;
					$("li.-num").eq(i).find("a").trigger("click");
				});
			}else{
				// has not next
				$("li.-next").addClass("disabled");
			}
			
			$this.find("li.-num").each(function(i){
				bindEvents($(this),i+getInterval(parseInt(curPageNumber.val()),10,parseInt(All_Record),parseInt(pageSize)).start,initOpts,callback,pageObj,All_Record,pageSize);
			});

	}
	
	function bindEvents($target,cur,initOpts,callback,pageObj,All_Record,pageSize){
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
			
			callback();
			
			var $this = $(pageObj);
			$this.attr("class", "pagination");
			var interval = getInterval(cur,10,parseInt(All_Record),parseInt(pageSize));
			if(interval.start == interval.end){
				//no records
				$this.html("<li class='disabled'><a href='#'>«</a></li><li class='active -num'><a href='#'>1</a></li><li class='disabled'><a href='#'>»</a></li>");
				return false;
			}
			
			// append li
			$this.empty().append("<li class='-prev'><a href='#'>«</a></li>");
			for(var k = interval.start; k < interval.end; k++){
				if(k == cur){
					$this.append("<li class='active -num'><a href='#'>"+k+"</a></li>");
				}else{
					$this.append("<li class='-num'><a href='#'>"+k+"</a></li>");
				}
			}
			$this.append("<li class='-next'><a href='#'>»</a></li>");
			
			if(hasPrev(cur)){
				$("li.-prev").removeClass("disabled");
				$("li.-prev").find("a").unbind("click").on("click",function(){
					var i = parseInt(cur);
					i = i - getInterval(cur,10,parseInt(All_Record),parseInt(pageSize)).start - 1;
					$("li.-num").eq(i).find("a").trigger("click");
				});
			}else{
				// has not prev
				$("li.-prev").addClass("disabled");
			}
			
			if(hasNext(cur,parseInt(All_Record),parseInt(pageSize))){
				$("li.-next").removeClass("disabled");
				$("li.-next").find("a").unbind("click").on("click",function(){
					var i = parseInt(cur);
					i = i - getInterval(cur,10,parseInt(All_Record),parseInt(pageSize)).start + 1;
					$("li.-num").eq(i).find("a").trigger("click");
				});
			}else{
				// has not next
				$("li.-next").addClass("disabled");
			}
			
			$this.find("li.-num").each(function(i){
				bindEvents($(this),i+getInterval(cur,10,parseInt(All_Record),parseInt(pageSize)).start,initOpts,callback,pageObj,All_Record,pageSize);
			});
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
	
	function getInterval(cur,numPages,sum,showPerPage){
		var ne_half = Math.ceil(numPages/2);//numPages -- for page footer ; showPerPage -- for show numbers in page
		var np = Math.ceil(sum/showPerPage);
		var upper_limit = np - numPages;
		var start = cur > ne_half ? Math.max( Math.min(cur - ne_half, upper_limit), 0 ) : 0;
		var end = cur > ne_half ? Math.min(cur+ne_half, np) : Math.min(numPages, np);
		return {start:start+1, end:end+1};
	}