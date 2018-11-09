$(function(){
	/*$('#phone').click(function(){
    $.post('/ajax/changemode',{mode:0},function(){
    location.reload();
    })
    })*/
	$('#window').click(function(){
        $.post('/ajax/changemode',{mode:1},function(){
            window.location.href = 'https://www.hurong.com/coins?notice=1';
        })
    })
	
    
    $(".j-nav").on("tap","a",function(){
		var coinname = $('#coinnames').val();
		$(this).addClass('dangqian').siblings().removeClass("dangqian");
		var i = $(this).index();
		if(i == 0){
			$('.sales').css('display','none');
			$('.mywait').css('display','none');
			$('.wt-box3').css('display','none');	//委托历史
			$('.j-L').css('display','block');		//左侧委托记录
			$('.buys').css('display','block');		//买入
			$('.su_log').css('display','block');	//成交记录
			$('.Hclick').css('display','block');
			$('.msg').css('display','block');	//买入提示

		}else if(i == 1){
			$('.buys').css('display','none');
			$('.mywait').css('display','none');
			$('.wt-box3').css('display','none');
			$('.msg').css('display','block');	//买入提示隐藏
			$('.j-L').css('display','block');
			$('.sales').css('display','block');
			$('.su_log').css('display','block');
			$('.Hclick').css('display','block');

		}else{
			/*if($('#logid').val() == false){
				window.location="../user/login.htm";
				return false;
			}*/
			$('.buys').css('display','none');
			$('.sales').css('display','none');
			$('.j-L').css('display','none');
			$('.msg').css('display','none');	//买入提示隐藏
			$('.su_log').css('display','none');
			$('.Hclick').css('display','none');
			$('.wt-box3').css('display','block');
			$('.mywait').css('display','block');
//			我的委托
			/*mycointrust();
			var page = 0;
			history(coinname,page);*/
		}
	})

	//成交记录
	$(".Hclick").on("click","span", function () {
		$(this).addClass("spanBottom").siblings("span").removeClass("spanBottom")
	})
	$('.history_order').click(function(){
		//隐藏成交记录，显示我的成交
		$('.mysuccess').css('display','none');
		$('.log_history').css('display','block');
		$('.log_history').removeAttr('style');
	})

	//我的成交
	$('.mysuorder').click(function(){
		//隐藏成交记录，显示我的成交
		$('.log_history').css('display','none');
		$('.mysuccess').css('display','block');
		$(this).addClass(".spanBottom")
		var page = 0;
		var coinname = $('#coinnames').val();
		coinordersuccess(coinname,page);
	})

	//我的成交加载更多
	$('#more').click(function(){
		var coinname = $('#coinnames').val();
		var page = $('#pnumber').val();
		coinordersuccess(coinname,page);
	})
	//委托历史加载更多
	$('#more_history').click(function(){
		var coinname = $('#coinnames').val();
		var page = $('#historynum').val();
		history(coinname,page);
	})

	//委托历史下拉选择
	$('#status').change(function(){
		var coinname = $('#coinnames').val();
		var page = 0;
//		$('#more_history').html('↓  显示更多');
		history(coinname,page);
	})

	//K线显示
	var flags = true;	//声明全局变量
	function clicks(){
		if(flags == true){
			//图标隐藏
			$('.img').css('display','none');
			$('.return').css('display','block');

			$('#trade_area').css('display','none');
			$('#kline').css('display','block');
		}else{
			$('.img').css('display','block');
			$('.return').css('display','none');

			$('#trade_area').css('display','block');
			$('#kline').css('display','none');
		}
		flags = !flags;
	}
})