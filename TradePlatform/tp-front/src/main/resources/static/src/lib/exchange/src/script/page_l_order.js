/**
 * Created by andyk on 2015/11/12.
 * 闪电手委托
 */
define(function (require, exports, module) {
    /*****委托*****/
    var mO = require('./module_order'),
        LN = require('./page_l_notice'),
        _orderType = 'PlaceOrder', //默认交易类型
        _coinType = SYMBOL,    //默认币种

        GetOrder = UID && mO.GetOrder({
                temp: 'tmp_us_order',
                wrap: 'view_us_order',
                coin_type: _coinType,
                is_history: 10,
                damp: true
            }, function () {
                GetHistory && GetHistory.Fire()
            }),
        GetHistory = UID && mO.GetOrder({
                temp: 'tmp_us_order_history',
                wrap: 'view_us_order_history',
                coin_type: _coinType,
                is_history: 20,
                delay: 0
            }),

        $dom = {};
    $dom['view_us_order'] = $('#view_us_order');
    $dom['cancel_order_all'] = $('#cancel_order_all');

    //委托单条取消
    $dom['view_us_order'].on('click', '.cancel', function () {debugger
        var _t = $(this),
            _ids = _t.attr('data-ids'),
            _tr  = _t.parents('.tr'),
            _init= _t.attr('data-init')*1;
    		_enprice = _tr.find('span').eq(2).text().trim();
    		_type = $(_tr.find('span').eq(1)).attr("type");
        if(_init){
            return false
        }
        _t.attr('data-init',1);
        mO.CancelOrder({"coin_type": _coinType, "id": _ids ,"entrustPrice":_enprice,"type":_type}, function (data) {
        	
            if (data['code'] == 'loading') {
                _tr.addClass('cancel');
                GetOrder.Stop();
            } else if (data['code'] == 'error') {
                _tr.hasClass('cancel') && _tr.removeClass('cancel');
                _t.attr('data-init',0);
                LN({"message": window.Texts && Texts['NetworkError'], "type": "error", "long": 1800});
                GetOrder.Play();
            } else {
                if (data['code'] === 0) {
                    _tr.next('.hidden_info').remove();
                    _tr.slideUp(function () {
                        _tr.remove();
                        GetOrder.Fire();
                    });
                    HRYB['AJAX']['GetBalance'].Fire();
                } else {
                    _t.attr('data-init',0);
                    _tr.hasClass('cancel') && _tr.removeClass('cancel');
                    LN({"message": data.msg, "type": "error", "long": 1800});
                }
                
                setTimeout(function () {
                	GetOrder.Fire();
                }, 1000);
                setTimeout(function(){
  		    		HRY.user_room_waitBalance();
  	    		}, 1100);
                setTimeout(function () {
                	GetHistory.Fire();
                }, 1200);
                
            }
        });
        return false
    });

    //全部撤销
    $dom['cancel_order_all'].on('click', function () {
        var _t = $(this),
            _ids = _t.attr('data-ids') ? _t.attr('data-ids').split('|') : 0;
        _t.prop('disabled', true);
        _ids && mO.CancelAllOrder({"coin_type": _coinType, id: _ids}, function (data) {
            if (data.code == 'loading') {
                GetOrder.Stop();
                $dom['view_us_order'].find('.tr').addClass('cancel')
            } else if (data['code'] == 'error') {
                $dom['view_us_order'].find('.tr').removeClass('cancel');
                _t.prop('disabled', false);
                LN({"message": window.Texts && Texts['NetworkError'], "type": "error", "long": 1800});
            } else {
                if (data.code == 0) {
                    GetOrder.Fire();
                    GetOrder.Play();
                    _t.prop('disabled', false);
                    HRYB['AJAX']['GetBalance'].Fire();
                    LN({"message": data.msg, "long": 1800})
                } else {
                    GetOrder.Play();
                    $dom['view_us_order'].find('.tr').removeClass('cancel');
                    _t.prop('disabled', false);
                    LN({"message": data.msg, "type": "error", "long": 1800});
                }
            }
        });
        return false
    });

    exports.GetOrder = GetOrder;
    exports.GetHistory = GetHistory;
});