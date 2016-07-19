/*Custom JQUERY for OPH Expense Tracker Application*/
$(document).ready(function() {

	/*Create program page navigation: Start*/
	/*$(".program-detail-nav-next").on("click",function() {
		$("#program-details").css("left","-100%");
		$("#program-team").css("left", "0");
		$(".progress-meter").css("width", "50%");
	});*/

	$(".program-team-nav-prev").on("click",function() {
		$("#program-details").css("left","0");
		$("#program-team").css("left", "+100%");
		$(".progress-meter").css("width", "25%");
	});
	$(".program-team-nav-next").on("click",function() {
		$("#program-team").css("left","-100%");
		$("#program-activity").css("left", "0");
		$(".progress-meter").css("width", "75%");
	});

	$(".program-activity-nav-prev").on("click",function() {
		$("#program-team").css("left","0");
		$("#program-activity").css("left", "+100%");
		$(".progress-meter").css("width", "50%");
	});
	$(".program-activity-nav-next").on("click",function() {
		$("#program-activity").css("left","-100%");
		$("#program-summary").css("left", "0");
		$(".progress-meter").css("width", "100%");
	});

	$(".program-summary-nav-prev").on("click",function() {
		$("#program-activity").css("left","0");
		$("#program-summary").css("left", "+100%");
		$(".progress-meter").css("width", "75%");
	});
	/*Create program page navigation: End*/

	/*Programs row click transition*/
	$(document).on("click",".row-onclick",function(){
		$("#home-page").hide();
		$("#view-program").removeClass("hide");
	});

	/*Program-tabs table rows editable by double-click*/


});