$(function() {
    $("#layui-hide").click(function() {
        $("#layui-m-layer0").hide();
    });
    
    FastClick.attach(document.body);
    $(".showAndhide").hide();
    
    $('.closeall').click(function() {
        $('#freeze_i').hide();
    })
})

function getInterest(coin) {
    if (coin == 'abc' || coin == 'trmb') {
        alert('请等待系统自动领取利息');
        return false;
    }

}

function showTips(id, msg) {
    var tips = layer.tips(msg, id, {
        style: ['background-color:#fff8db;color:#000; margin-left:-15px; padding:10px;', '#fff8db'],
        guide: 0,
        minWidth: 250
    });
    $(id).on('mouseout', function() {
        layer.close(tips);
    });
}

function showTips(id, msg) {
    var tips = layer.tips(msg, id, {
        style: ['background-color:#fff8db;color:#000; margin-left:-15px; padding:10px;', '#fff8db'],
        guide: 0,
        minWidth: 250
    });
    $(id).on('mouseout', function() {
        layer.close(tips);
    });
}