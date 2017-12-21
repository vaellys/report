function ajaxGrid(gridId,formId,url,confirmMsg,callback) {
	var $form = $("#"+formId);
	$("input[name=curPageNumber]").val(1); 
	$.ajax({
		type:'POST',
		url:$form.attr("action"),
		data:$form.serializeArray(),
		dataType:"html",
		cache: false,
		success: function(data){
			$("table.table-bordered tbody").html($(data).find("table.table-bordered tbody").html());
			var $paginationNav = $(data).find(".pagination");
			var sumpages = parseInt($paginationNav.attr("sumpages"));
            $("input[name=totalRecord]").val(sumpages);
            $(".pagination").attr("sumpages",sumpages);
            $(".pagination").each(function(){
				var $this = $(this);
				$this.pagination({currentPage:$this.attr("currentPage"),showPerPage:$this.attr("showPerPage"),sumPages:$this.attr("sumPages"),formId:$this.attr("formId"),numPages:$this.attr("numPages")});
			});
		}
	});
	return false;
}