 $(function(){
         $(".bullet_p .bull_click").click(function() {
           $(this).addClass('actives').siblings().removeClass('actives');
            var index =   $(".bullet_p .bull_click").index(this);
            $(".tb-box1 form").eq(index).show().siblings(".tb-box1 form").hide();
        });
  })

      $("input[name='phone']").bind('blur', function(){
         $.post("/user/moExist",{mo:$(this).val()},function(d){
             var d = eval('('+d+')');
            if(d.code !== 1){
               $('.phone_note').css('display','block');
                return false;
            }else{
               $('.phone_note').css('display','none');
            }
        });
    })

    function sendPhoneCode(){
        var timecount = 60;
        var phone = $("input[name='phone']").val();
        if(phone <11 || phone == ''){
            $('.phone_note').html('手机号不正确');
            return false;
        }
	$.ajax({
		url:'/Ajax/sendmsg3',
		type:'post',
		data:{type:101,voice:0,mo:phone},
		dataType:'json',
		success:function(res){
		if(res.msg=="发送成功"){
                    countTime(timecount);
                    $('.phone_code').css('background','#ccc').html('<span class="count-time" style="margin-left: 0px;">'+timecount+'</span>秒后重新发送');
                        alert(res.msg);
                    }else{
			alert(res.msg);
                    }
		}

	});
    }

    var ii=0;
    var tc;
    function countTime(time){
        ii = time;
        ii--;
        if(ii==-1){
            $(".phone_code").css('background','#f60').attr('onclick','sendPhoneCode();').html('重新发送');
            window.clearTimeout(tc);
        }else{
            $("span.count-time").html(ii);
            tc=setTimeout(function() {
                countTime(ii)
            },1000);
        }
    }

    $("input[name='pwd']").bind('blur',function(){
        var pwd_len = $(this).val().length;
        if(pwd_len < 6 || pwd_len > 20){
             $('.pwd_note').css('display','block');;
            return false;
        }else{
             $('.pwd_note').css('display','none');;
        }
    })

    $("input[name='repwd']").bind('blur',function(){
        var pwd = $("input[name='pwd']").val();
        var repwd = $(this).val();
        if(pwd != repwd){
            $('.repwd_note').css('display','block');;
            return false;
        }else{
             $('.repwd_note').css('display','none');;
        }
    })

    $("input[name='code']").bind('blur',function(){
        var code = $("input[name='code']").val();
        var code = $(this).val();
        if(code == ''){
            $('.code_note').css('display','block');;
            return false;
        }else{
             $('.code_note').css('display','none');;
        }
    })

    $('#msubmit').click(function(){
        var phone = $("input[name='phone']").val();
        var code = $("input[name='code']").val();
        var pwd = $("input[name='pwd']").val();
        var repwd = $("input[name='repwd']").val();
        if(phone == '' ){
            $('.phone_note').css('display','block');;
            return false;
        }
        if(code == '' ){
           $('.code_note').css('display','block');;
            return false;
        }
        if(pwd == '' || repwd == ''){
            $('.repwd_note').css('display','block');;
            return false;
        }
        $(this).attr('type','submit');
    })
    
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
