vali = {
    captcha: 0,
    email: 0,
    pwd: 0
};
$('#email').bind('blur', function() {
    var pLen = $(this).val().length;

    if (pLen < 6 || pLen > 30) return validateMsg('email', '请输入邮箱', 0);
    var email = $(this).val();

    return validateMsg('email', '', vali.email = 1);
});
$('#pwd').bind('blur', function() {
    var pLen = $(this).val().length;
    if (pLen < 6 || pLen > 20) return validateMsg('pwd', '密码格式错误', 0);
    return validateMsg('pwd', '', vali.pwd = 1);
    });
    vali.captcha = 1;
    
  //短信倒计时
var ii = 0;
var tc;
var timecount = 60;

function countTime(time) {
    ii = time;
    ii--;
    if (ii == -1) {
        $("label.retry-send").css('background', '#f60').html('重新发送');
        window.clearTimeout(tc);
    } else {
        $("span.count-time").html(ii);
        tc = setTimeout(function() {
            countTime(ii)
        }, 1000);
    }
}