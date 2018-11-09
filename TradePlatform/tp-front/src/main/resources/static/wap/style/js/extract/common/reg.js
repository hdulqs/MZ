$(function() {
				$('#tongyi').click(function() {
					var isAgree = $(this).attr('checked');
					if (isAgree) {
						$(this).removeAttr('checked');
						$('#next-btn').css({
							'background': '#e1e1e1'
						});
					} else {
						$(this).attr("checked", 'true');
						$('#next-btn').css({
							'background': '#f60'
						});
					}

				});

				$('#next-btn').click(function() {
					var isAgree = $('#tongyi').attr('checked');
					if (isAgree) {
						$('#reg-prev').hide();
						$('#reg-now').show();
					}
				});
			});