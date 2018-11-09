function showExplain(f) {
    if (f == 1) {
        $('.header').hide();
        $('.footer').hide();
        $('.strip').hide();
        $('#showw').hide();
        $("#showExplain").show();
    } else if (f == 2) {
        $('.header').show();
        $('.footer').show();
        $('.strip').show();
        $('#showw').show();
        $("#showExplain").hide();
    }
}
// 加载更多
$(function() {
    p = 1;
    $('.much').bind('click', function() {
            ajaxPage();
            p++;
        });

 //点击充值记录的，暂时注释掉
    // $("#list").on('click', '.dd_o_news', function() {
    //     $('.header').hide();
    //     $('.footer').hide();
    //     $('.strip').hide();
    //     $("#showw").hide();
    //     $('#recharge_alert_bank').show();
    //     Ebank.showTips('', $(this).attr('title'));
    // })

})