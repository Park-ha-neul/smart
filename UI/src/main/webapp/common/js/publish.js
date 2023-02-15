$(document).ready(function () {

	$('.id_input').focus(function () {
		$(this).css("color", "#000");
		$(this).next().addClass('active');

	});
	$('.id_input').focusout(function () {
		if ($(this).val() == "") {
			$(this).css("color", "#999");
			setTimeout(function () { $('.clear_btn').removeClass('active'); }, 200);

		}else if ($(this).val() != "") {
			setTimeout(function () { $('.clear_btn').removeClass('active'); }, 200);
		};
	});

	$('.pwd_input').focus(function () {
		$(this).css("color", "#000");
		$(this).next().addClass('active');
	});
	$('.pwd_input').focusout(function () {
		if ($(this).val() == "") {
			$(this).css("color", "#999");
			setTimeout(function () { $('.clear_btn2').removeClass('active'); }, 200);
		}else if ($(this).val() != "") {
			setTimeout(function () { $('.clear_btn2').removeClass('active'); }, 200);
		};
	});

	$('.clear_btn').on('click', function () {
		$(this).prev().val('').focus();
		return false;
	});
	$('.clear_btn2').on('click', function () {
		$(this).prev().val('').focus();
		return false;
	});
});