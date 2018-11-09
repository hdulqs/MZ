<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>二次验证</title>
	
	<link rel="stylesheet" type="text/css" href="/static/src/lib/google/css/global.css">
    <link rel="stylesheet" type="text/css" href="/static/src/lib/google/css/google.css">
    <link rel="stylesheet" type="text/css" href="/static/src/lib/google/css/second.css">
</head>

<body>
    <div class="verifyLayout">
        <div class="main">
            <!--banner -->
            <div class="verify middle">
                <div>
                    <div class="verify-title"><span class="">二次验证</span></div>
                    <div class="btns">
                        <span class="btn cur">谷歌验证</span><span class="btn">手机验证</span>
                    </div>
                    <form class="verify-form" action="" method="post" id="googleVerify-form">

                        <input type="text" id="email" name="email" style="display:none;">
                        <input type="hidden" name="operationType" value="google">
                        <div class="label f-left">谷歌验证码</div>
                        <div class="filed">
                            <input type="text" name="verifyCode" class="ipt" datatype="*" nullmsg="该字段不能为空">
                            <p class="Validform_checktip f-nomargin f-left"></p>
                        </div>
                        <div class="filed">
                            <input type="submit" value="确定" class="btn btn-orange btn-block" id="googleVerify-btn">
                        </div>
                    </form>
                    <form class="verify-form" action="" method="post" id="mobile-form">
                        <div class="label f-left">短信验证码</div>
                        <input type="text" readonly="" name="email" style="display:none;" class="">
                        <input type="hidden" name="operationType" value="mobile">
                        <div class="filed">
                            <input style="width:186px;" type="text" id="password" name="verifyCode" class="ipt">
                            <span id="sendBtn" class="btn btn-grey">发送验证码</span>
                            <p class="Validform_checktip f-nomargin f-left"></p>
                        </div>
                        <div class="filed">
                            <input type="submit" value="确定" class="btn btn-orange btn-block" id="mobile-btn">
                        </div>
                    </form>
                </div>
                <!-- end ngIf: verifyType==0 -->
            </div>
        </div>
    </div>
</body>
<script src="./js/jquery/jquery.min.js"></script>
<script>
  $(function(){
    $('#mobile-form').css('display','none');
    $('.btns').find('span').on('click',function(){debugger;
      var ind=$(this).index();
      $(this).addClass('cur').siblings().removeClass('cur');
      $('.verify-form').hide().eq(ind).show();
    })
  })
</script>
</html>
