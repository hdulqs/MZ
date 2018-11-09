$(function() {
	$('.page').remove();
	$('.addbanks').bind('click', function() {
		$("#rmbout").hide();
		$('.addbank').show();
		$('.header').hide();
		$('.footer').hide();
		$('.strip').hide();
	})
	$('.back').bind('click', function() {
		$("#rmbout").show();
		$('.addbank').hide();
		$('.header').show();
		$('.footer').show();
		$('.strip').show();
	})
	/*$('#submit').bind('click', function() {
		if (!cnyOut.rmbconfirm()) {
			alert($('#rmbout_showtips').text())
		} else {
			$(this).attr('type', 'submit');
		}
	});*/
	$('.bankId').bind('click', function() {
		var result = $(this).attr('name').split('YBEX');
		$('#bankId').val(result[0]);
		$("#weihao").text($(this).attr('title'));
		$('#bankk').text(result[1]);
		$("#rmbout").show();
		$('.addbank').hide();
	});
	$('.ft_bank_add').bind('click', function() {
		$("#rmbout").hide();
		$('.addbank').hide();
		$('.wf_addbank').show();
	});

	$('.sub').bind('click', function() {
		bank = $('#bank :selected').html();
		if (bank == '请选择银行') {
			alert('请选择银行');
			return false;
		}
		province = $('#p1').val();
		province2 = $("#p1 option:selected").text();
		city = $('#c1').val();
		city2 = $("#c1 option:selected").text();
		if (province <= -1 || city <= -1) {
			alert('请选择银行地址');
			return false;
		}
	});

	$('.fanhui').bind('click', function() {
		$("#rmbout").hide();
		$('.wf_addbank').hide();
		$('.addbank').show();
	});
})