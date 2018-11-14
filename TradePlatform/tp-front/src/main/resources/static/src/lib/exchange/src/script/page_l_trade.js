/**
 * Created by andyk on 2015/11/12.
 * 闪电手交易面板
 * todo 最大交易量重新整理
 * todo 获取杠杆额度
 * todo 计算杠杆使用额
 *
 */
define(function (require) {
    var mAcc = require('./module_accurate'),  //精确算法
        mNum = require('./module_number'),    //数字处理
        mVad = require('./module_validator'), //表单校验
        mRange = require('./module_range'),     //滑块
        mAjax = require('./module_ajax'),      //ajax
        mSwitch = require('./module_switch'),    //开关
        mCookie = require('./module_cookie'),
        mDialog = require('./module_dialog'),
        M  = require('./module_md5'),
        J = require('./module_this_jack'),
        LN = require('./page_l_notice'),
        LO = require('./page_l_order'),
        $ = require('jquery'),
        HB = require('./module_hb_extend'),
        DOM = $(document),
    	BALANCE = {};
    	DOC = {},
    	HRYB['BALANCE'] = BALANCE;
    	DOC['doc'] = $(document);
        /*****系统交易限制*****/
        _limits = {
            "trade": {
                //限价限制
                "max_buy_cny_btc": 200000000,      //买入币数CNY-BTC
                "max_buy_cny_ltc": 200000000,     //买入币数CNY-LTC

                "max_sell_cny_btc": 200000000,      //卖出币数CNY-BTC
                "max_sell_cny_ltc": 200000000,     //卖出币数CNY-LTC

                "max_buy_usd_btc": 200000000,      //买入币数USD-BTC
                "max_buy_usd_ltc": 200000000,     //买入币数USD-LTC

                "max_sell_usd_btc": 200000000,      //卖出币数USD-BTC
                "max_sell_usd_ltc": 200000000,     //卖出币数USD-LTC

                //市价限制
                "max_buy_cny_btc_mp": 200000000,   //市价买入金额CNY-BTC
                "max_buy_cny_ltc_mp": 200000000,   //市价买入金额CNY-LTC

                "max_sell_cny_btc_mp": 200000000,      //市价卖出币数CNY-BTC
                "max_sell_cny_ltc_mp": 200000000,     //市价卖出币数CNY-LTC

                "max_buy_usd_btc_mp": 200000000,    //市价买入金额USD-BTC
                "max_buy_usd_ltc_mp": 200000000,   //市价买入金额USD-LTC

                "max_sell_usd_btc_mp": 200000000,      //市价卖出币数USD-BTC
                "max_sell_usd_ltc_mp": 200000000     //市价卖出币数USD-LTC
            },
            "loan": {

                //借贷最小额
                "min_cny_cny": 100,
                "min_cny_btc": 0.1,
                "min_cny_ltc": 1,

                "min_usd_usd": 50,
                "min_usd_btc": 0.1,
                "min_usd_ltc": 1
            }
        },
        /*****数据缓存*****/
        _caches = {
            "trade": {
                "buy_max": 0,         //限价买
                "sell_max": 0,        //限价卖
                "buy_max_mp": 0,      //市价买
                "sell_max_mp": 0      //市价卖"
            },
            "loan": {
                "cny_cny": {
                    "enable": 0,
                    "total": 0
                },
                "cny_btc": {
                    "enable": 0,
                    "total": 0
                },
                "cny_ltc": {
                    "enable": 0,
                    "total": 0
                },
                "usd_usd": {
                    "enable": 0,
                    "total": 0
                },
                "usd_btc": {
                    "enable": 0,
                    "total": 0
                }
            }
        },
        _orderType = 'PlaceOrder', //默认交易类型
        _panelType = 'Limited',      //默认面板
        _coinType = document.getElementsByName('coin_type')[0].value,     //默认币种
        _dom = {
            ".form_trade": $('.form_trade')
        },
    //买入滑块
        buyRange = new mRange({
            range: '#range_buy',
            step: 1,
            point: 4,
            delay: 10,
            slide: function (x, o) {
                Slide(x, o)
            }
        }),
    //卖出滑块
        sellRange = new mRange({
            range: '#range_sell',
            step: 1,
            point: 4,
            delay: 10,
            slide: function (x, o) {
                Slide(x, o)
            }
        }),
    //买入滑块
        buyRangeMp = new mRange({
            range: '#range_buy_mp',
            step: 1,
            point: 4,
            delay: 15,
            slide: function (x, o) {
                Slide(x, o)
            }
        }),
    //卖出滑块
        sellRangeMp = new mRange({
            range: '#range_sell_mp',
            step: 1,
            point: 4,
            delay: 15,
            slide: function (x, o) {
                Slide(x, o)
            }
        }),
        _CSRF = document.getElementsByName('_csrf')[0] && document.getElementsByName('_csrf')[0].value,
        GetLoanInfo = function (loan_type, init) {
            if (!UID)return;
            /*return new mAjax({
                    "url": '/loan/index.do',
                    "data": {"a": 'get_loan_available_multi', "loan_types": loan_type},
                    "init": init
                },
                function (data) {
                    var _code = data.code;
                    if (_code == 'success') {
                        var _data = data['data'],
                            _enable;
                        if (_data.code === 0 && _data['ext']) {
                            $.each(_data['ext'], function (i, c) {
                                _enable = c['enable'] * 1;
                                _caches.loan[i].enable = _enable;
                                _caches.loan[i].total = mAcc.Add(_enable, HRYB['BALANCE'][i + 'available'] || 0);
                            });
                            DOM.trigger('__LoanWrite');
                        }
                    }
                });*/
        },
        GetLoanNow = function (option, callback) {
            var _op = option || {},
                _amount = _op.amount || 0,
                _loanType = _op.loan_type,
                _fn;

            if (_amount <= 0) return;
            /*_fn = new mAjax({
                "url": '/loan/index.do',
                "data": {"a": 'do_loan', "loan_type": _loanType, "currency": _amount, "_csrf": _CSRF, "check": 'on'},
                "type": 'POST',
                "random": false
            }, function (data) {
                callback && callback(data)
            });*/
            return _fn;
        };

    window.buyRange = buyRange;

    /*注册杠杆信息ajax*/

    //注册借贷Ajax
    var LoanCB = GetLoanInfo('cny_cny,cny_btc', _coinType == 'cny_btc'),
        LoanCL = GetLoanInfo('cny_cny,cny_ltc', _coinType == 'cny_ltc'),
        LoanUB = GetLoanInfo('usd_usd,usd_btc', _coinType == 'usd_btc');
    
//    var LoanCB = null;
//    	LoanCL = null;
//    	LoanUB = null;

    /*更新交易类型*/
    function OrderType(panel) {
        _orderType = panel.data('order-type') || 'PlaceOrder';
        _panelType = panel.data('panel-type') || 'Limited';
    }

    /*遍历Form*/
    function GetForm(form) {
        if (form['Type'] === 'HBForm') {
            return form
        } else {
            var _form = $(form);
            _form.find('[name]').each(function (i, c) {
                var _t = $(c),
                    _name = _t.attr('name');
                _form[_name] = _t;
                _form[_name]['Value'] = _t.val()
            });
            _form['Type'] = 'HBForm';
            return _form;
        }
    }

    /*数字格式化*/
    function NumFormat(num, n) {
        if (/\.$/.test(num)) {
            return mNum(num)
        }
        return mNum(num, n);
    }

    /*输入框数值格式化*/
    function InFormat() {
        var _this = this,
         //   _regP = /^(([0-9]{1}\d*)|([0]{1}))(\.(\d){0,2})?$/,
         //   _regA = /^(([0-9]{1}\d*)|([0]{1}))(\.(\d){0,4})?$/;
        
	        _regP = eval("/^(([0-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,"+HRY.keepDecimalForCurrency+"})?$/");
	        _regA = eval("/^(([0-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,"+HRY.keepDecimalForCoin+"})?$/");

        /**
         * @return {number}
         */
        _this.FormatVal = function (type) {
            var _v = _this.val();
            if (_v < 0) {
                _v = 0;
                _this.val(_v);
            }
            if (_v != '' && !(type == 'price' ? _regP : _regA).test(_v)) {
            	var keepDecimalForCoin = HRY.keepDecimalForCoin;
            	var keepDecimalForCurrency = HRY.keepDecimalForCurrency;
                _v = NumFormat(_v*1, type == 'price' ? keepDecimalForCurrency : keepDecimalForCoin );
                _this.val(_v);
            }
          //计算手续费
            var buyRate = $('.buyrate').val();
            var sellRate = $('.sellrate').val();
            
            var buy_price = $('#buy_price').val();
            var buy_amount = $('#buy_amount').val();
            
            var sell_price = $('#sell_price').val();
            var sell_amount = $('#sell_amount').val();
            
            //var buyCharge = (Number(buyRate)/100) * (Number(buy_price)*Number(buy_amount));
            //var sellCharge = (Number(sellRate)/100) * (Number(sell_price)*Number(sell_amount));
            
            /*var buyCharge = (Number(buyRate)/100) * Number(buy_amount);
            var sellCharge = (Number(sellRate)/100) * Number(sell_amount);*/
            
            var rate_type = _this.attr("id");
            if(rate_type != undefined) {
            	/*if(rate_type.substring(0,rate_type.indexOf("_")) == "buy"){
            		$('.buyCharge').html(NumFormat(buyCharge, HRY.keepDecimalForCurrency));
	            }else {
	            	$('.sellCharge').html(NumFormat(sellCharge, HRY.keepDecimalForCurrency));
	            }*/
            }

        	return _v * 1;
        }
    }

    /**
     * 成交额
     * @param p   价格
     * @param a   数量
     * @param box 容器
     * @returns {number}
     */
    function WriteTA(p, a, box) {
        var _t;
        if (_orderType === 'PlaceOrder') {
            if (a && p) {
                _t = mAcc.Mul(a * 1, p * 1);
                box && box.html(mNum(_t, HRY.keepDecimalForCurrency)).attr('actual', _t)
            } else {
                _t = 0;
                box && box.html('0.00')
            }
        } else if (_orderType === 'PlaceMarketOrder') {
            _t = p !== undefined ? p : a;
        }
      
        return _t;
    }
    
    
    
    /**
     * 更新最大交易量
     * @param option
     * trade_type: buy/sell
     * loan_type: true/false
     * price: 单价
     * un_price: 剩余额度
     * un_amount: 剩余数量
     */
    function MaxVolume(option) {
        var _op = option || {},
            _tradeType = _op['trade_type'],
            _loanType = _op['loan_type'],
            _price = _op['price'],
            _available = _op['available'],
            _loanTotal = mAcc.Add(_op['loan_enable'], _available),
            _coin = _op['coin'],
            _A = [];

        _A = Amount(
            _available,
            _loanTotal,
            _price,
            [_limits.trade['max_buy_' + _coinType], _limits.trade['max_sell_' + _coinType]],
            [_limits.trade['max_buy_' + _coinType + '_mp'], _limits.trade['max_sell_' + _coinType + '_mp']]
        );
        _A[0] !== undefined ? _caches.trade.buy_max = _A[0] : '';
        _A[1] !== undefined ? _caches.trade.buy_max_mp = _A[1] : '';
        _A[2] !== undefined ? _caches.trade.sell_max = _A[2] : '';
        _A[3] !== undefined ? _caches.trade.sell_max_mp = _A[3] : '';
        /**
         * @param a  可用钱/可用币
         * @param la 杠杆 可用额/可用币
         * @param p  交易价
         * @param l  [买限制,卖限制]
         * @param lm [市价买限制，卖限制]
         * @returns {Array} [买限价量,买市价量,卖限价量,卖市价量]
         * @constructor
         */
        function Amount(a, la, p, l, lm) {
            var _a = [];
            if (_tradeType === 'buy') {
                //限价
                _a[0] = p ? mAcc.Div(_loanType ? la : a, p) : 0;
                _a[0] = _a[0] < l[0] ? _a[0] : l[0];
                //市价
                _a[1] = _loanType ? la : a;
                _a[1] = _a[1] < lm[0] ? _a[1] : lm[0];
            } else {
                //限价
                _a[2] = _loanType ? la : a;
                _a[2] = _a[2] < l[1] ? _a[2] : l[1];
                //市价
                _a[3] = _loanType ? (la < lm[1] ? la : lm[1]) : (a < lm[1] ? a : lm[1]);
            }
            //console.log('计算最大交易量', a, la, p, l, lm, _a)
            return _a;
        }

    }

    /**
     *更新面板最大交易量
     * @param type
     * @param maxBox
     */
    function MaxWrite(type, maxBox) {
        if (type === 'buy') {
            if (_orderType == 'PlaceOrder') {
                maxBox && maxBox.attr('data-max', mNum(_caches.trade.buy_max, 4)).parent().find('.max_amount_num').html(mNum(_caches.trade.buy_max, 4));
            } else if (_orderType === 'PlaceMarketOrder') {
                maxBox && maxBox.attr('data-max', mNum(_caches.trade.buy_max_mp, 4)).parent().find('.max_amount_num').html(mNum(_caches.trade.buy_max_mp, 4));
            }
        } else {
            if (_orderType == 'PlaceOrder') {
                maxBox && maxBox.attr('data-max', mNum(_caches.trade.sell_max, 4)).parent().find('.max_amount_num').html(mNum(_caches.trade.sell_max, 4));
            } else if (_orderType === 'PlaceMarketOrder') {
                maxBox && maxBox.attr('data-max', mNum(_caches.trade.sell_max_mp, 4)).parent().find('.max_amount_num').html(mNum(_caches.trade.sell_max_mp, 4));
            }
        }
    }

    /**
     *借贷额度
     * @param total     [交易额,交易量]
     * @param available 可用交易额
     * @param type      交易类型(buy/sell)
     * @param box       容器
     * @param coin      币种(cny_cny)
     * @param check
     * @returns {number}
     * @constructor
     */
    function LoanAmount(total, available, type, box, coin, check) {
        var _loan = 0;

        if (type === 'buy') {
            _loan = mAcc.Sub(total[0], available) * 1 > 0 ? mAcc.Sub(total[0], available) * 1 : 0;
        } else {
            _loan = mAcc.Sub(total[1], available) * 1 > 0 ? mAcc.Sub(total[1], available) * 1 : 0;
        }

        //系统借贷额度
        _loan = (_loan > 0 && _loan < _limits.loan['min_' + coin]) ? _limits.loan['min_' + coin] : _loan;
        if (type === 'buy') {
            box && box.html(check ? mNum(Carry(_loan, 'currency'), 4) : '0.00').attr('data-amount', Carry(mNum(_loan), 'currency'));
        } else {
            box && box.html(check ? mNum(Carry(_loan, 'coin'), 4) : '0.0000').attr('data-amount', Carry(mNum(_loan), 'coin'));
        }
        return _loan;
    }

    /**
     * 小数向上进位
     * @param num   数字
     * @param type  curr货币/coin虚拟币
     * @returns number
     * @constructor
     */
    function Carry(num, type) {
        var _num;
        if (type == 'currency') {
            _num = Math.floor(num * 100 + 0.9) / 100
        }
        if (type == 'coin') {
            _num = Math.floor(num * 10000 + 0.9) / 10000
        }
        return _num
    }

    /**
     * 更新滑块比例
     * @param range  滑块
     * @param amount 总额
     * @param price  单价
     */
    function UpRange(range, amount, price) {
        var _v = (price && amount) ? price / amount * 100 : 0;
        range.Update(_v)
    }


    //滑块操作
    function Slide(x, o, t) {
        var _o = $(o),
            _ratio = _o.find('.range_ratio'),
            _form = GetForm(_o.parents('form')),
            _type = _form.data('trade-type'),
            tradeRatio = x / 100;
        tradeRatio ? _ratio.show() : _ratio.hide();
        _ratio.css({
            'left': x + '%'
        });

        if (_orderType === 'PlaceOrder') {
            //限价
            if (_type === 'buy') {
                _form["amount"].val(NumFormat(mAcc.Mul(_caches.trade.buy_max, tradeRatio), 4)).trigger('keyup', ['slide']);
            } else {
                _form["amount"].val(NumFormat(mAcc.Mul(_caches.trade.sell_max, tradeRatio), 4)).trigger('keyup', ['slide']);
            }
        } else if (_orderType === 'PlaceMarketOrder') {
            //市价
            if (_type === 'buy') {
                _form["market_transaction_amount"].val(NumFormat(mAcc.Mul(_caches.trade.buy_max_mp, tradeRatio), 4)).trigger('keyup', ['slide']);
            } else {
                _form["market_amount"].val(NumFormat(mAcc.Mul(_caches.trade.sell_max_mp, tradeRatio), 4)).trigger('keyup', ['slide']);
            }
        }
    }

    function TradeApi(option, callback) {
    	
        var _op = option || {};
        mAjax({
                "url": '/trade/do_' + _op.type + '?st=trade_trade&order_source=8',
                "data": _op.data,
                "type": 'POST',
                "init": _op.init
            },
            function (data) {
                var _code = data.code;
                console.log('trade ajax',_code)
                if (_code == 'success' || _code == 'error') {
                    var _data = data['data'];
                    callback && callback(_data , _code)
                }
            })
    }

    !function () {
        /*****限价/市价交易*****/

        //交易面板资金密码
        var _dDialog_trade = $('#trade_pwd_dialog'),
            _dialog_trade_pwd = new mDialog({'dialog': _dDialog_trade}, function (dialog, type, obj) {
                if (type == 'close' && obj) {
                    _dDialog_trade.attr('data-init', '')
                }
            }),
            _dialog_tip = _dDialog_trade.find('.tip'),
            _dTradePwdSub = $('#trade_pwd_submit'),
            _dTradePwd = $('#trade_pwd'),
            _tradePwd;

        function check_use_trade_pwd() {
            var trade_pwd_on = mCookie.Cookie('trade_pwd_on');
            var trade_pwd_time = mCookie.Cookie('trade_pwd_time');

            if (trade_pwd_on == "0") {
                return false;
            } else if (trade_pwd_on == "1") {
                return true;
            } else {
                if (!trade_pwd_time || trade_pwd_time == "0") {
                    return true;
                }
            }
            return false;
        }

        function TradePwd(callback) {
            _dTradePwd.val('');
            var _nullMsg = _dTradePwd.data('msg-null');
            _dialog_tip.removeClass('v_error').text('');
            if (_dDialog_trade.is(':hidden')) {
                _dialog_trade_pwd.Show();
                _dTradePwd.focus();
                _dDialog_trade.attr('data-init', '1')
            } else {
                return
            }
            _dTradePwd.off('keydown');
            _dTradePwdSub.off('click');

            _dTradePwd.keydown(function (e) {
                if (e.keyCode == 13) {
                    _tradePwd = _dTradePwd.val();
                    SUB()
                }
            });
            _dTradePwdSub.on('click', function () {
                _tradePwd = _dTradePwd.val();
                if (_tradePwd && _tradePwd != '') {
                    SUB();
                } else {
                    _dialog_tip.addClass('v_error').text(_nullMsg);
                }
            });
            function SUB() {
                callback && callback();
                _dialog_trade_pwd.Close();
            }
        }

        //表单校验
        mVad({
            forms: _dom['.form_trade'],
            tip: '.trade_msg',
            beforeValidation: function (_form) {
            	_form.attr("action",HRY.host + "/user/trades/add")
                DOM.trigger('__TradeFormValidation', [_form]);
            },
            beforeSubmit: function (form) {
                var _form = GetForm(form),
                    _tradeType = _form.data('trade-type'),
                    _coinT = _form['coin_type'].val().split('_'),
                    _coin = _tradeType == 'buy' ? _coinT[0] + '_' + _coinT[0] : _coinT[0] + '_' + _coinT[1],
                    _loan = _form['loan_switch'],
                    _loanState = _loan && _loan.attr('data-loan-state') * 1,
                    _loanAmount = _form.find('.loan_amount').attr('data-amount') * 1;
                _form['_coin'] = _coin;

                //资金密码
              /*  if (check_use_trade_pwd() && !_dDialog_trade.attr('data-init')) {
                    TradePwd(function () {
                        _form.submit();
                    });
                    return false
                }*/

                if (_loanAmount > 0 && _loan.prop('checked')) {
                    switch (_loanState) {
                        case 0:
                            //初始状态
                            DOM.trigger('__GetLoanNow', [_form, _coin, _loanAmount]);
                            //console.log('借贷初始');
                            return false;
                            break;
                        case 1:
                            //正在借贷
                            //console.log('正在借贷');
                            return false;
                            break;
                        case 2:
                            //借贷成功
                            //console.log('借贷成功');
                            break;
                        case 3:
                            //借贷失败
                            //console.log('借贷失败');
                            return false;
                            break;
                        default :
                            return false;
                    }
                }

                DOM.trigger('__TradeFormBeforeSubmit', [_form]);

            },
            callback: function (data, _form) {
                _dDialog_trade.attr('data-init', '');
                DOM.trigger('__TradeFormCallback', [_form, data]);
            },
            postData: function (data) {
            	var retData=[{"name":"coinCode","value":CURRENT_SYMBOL}];
            	retData.push({"name":"tokenId","value":$("#tokenId").val()});
            	for(var i=0;i<data.length;i++){
            		if(data[i].name=="price"){
            			retData.push({"name":"entrustPrice","value":data[i].value});
            		}else if(data[i].name=="amount"||data[i].name=="market_amount"){
            			retData.push({"name":"entrustCount","value":data[i].value});
            		}else if(data[i].name=="order_type"||data[i].name=="coin_type"){
            			//retData.push({});
            		}else if(data[i].name=="market_transaction_price"){
            			retData.push({"name":"entrustSum","value":data[i].value});
            		}else{
            			retData.push(data[i]);
            		}
            	}
            	//放入资金密码
                var val = "";
                if($.trim(_tradePwd) != ""){
                    val = M.hbmd5(_tradePwd)
                }
                //data.push({"name": "trade_pwd", "value": val});
                return retData;
            },
            ajaxError:function(_form){
                _dDialog_trade.attr('data-init', '');
                DOM.trigger('__TradeFormError', [_form]);
            }
        });
        /*事件监听*/
        _dom['.form_trade'].each(function () {
            var _form = GetForm($(this)),
                _tradeType = _form.data('trade-type'),
                _coinT = _form['coin_type'].val().split('_'),
                _coin = _tradeType == 'buy' ? _coinT[0] + '_' + _coinT[0] : _coinT[0] + '_' + _coinT[1],
                _loanS = _form['loan_switch'],
                _loanAv = _form.find('.loan_available'),
                _range = _form.find('.range_wrap'),
                _DMax = _form.find('.max_amount'),
                _btn = _form.find(':submit'),
                _DMaxWrap = _DMax.parent(),
                _DMaxB = _DMax.find('b'),
                _DTAWrap = _form.find('.trade_amount'),
                _regP = /^(([0-9]{1}\d*)|([0]{1}))(\.(\d){0,2})?$/,
                _regA = /^(([0-9]{1}\d*)|([0]{1}))(\.(\d){0,4})?$/,
                _cache = {
                    "price": _form.price && _form.price.Value || 0,
                    "amount": _form.amount && _form.amount.Value || 0,
                    "market_t": _form.market_transaction_amount && _form.market_transaction_amount.Value || 0,
                    "market_a": _form.market_amount && _form.market_amount.Value || 0
                };

            J(InFormat, _form.price);
            J(InFormat, _form.amount);
            J(InFormat, _form.market_transaction_amount);
            J(InFormat, _form.market_amount);


            _form['_DMax'] = _DMax;
            _form['_DMaxB'] = _DMaxB;
            _form['_type'] = _tradeType;
            _form['_coin'] = _coin;
            _form['transaction_amount'] = _form.find('.transaction_amount');
            _form['loan_amount'] = _form.find('.loan_amount');
            _form['max_amount'] = _form.find('.max_amount');


            function EP(objs) {
                var _l = objs.length;
                for (var _i = 0; _i < _l; _i++) {
                    objs[_i] && objs[_i].on('focus', function () {
                        _form.trigger('__FormMax');
                        $(this).parent().addClass('focus');
                    }).on('blur', function () {
                        $(this).parent().removeClass('focus');
                        $(this).val()*1 <=0 && $(this).val('');
                    }).on('__Focus', function () {
                        $(this).parent().addClass('focus');
                    })
                }
            }

            EP([_form.price, _form.amount, _form.market_transaction_amount, _form.market_amount]);

            _form.price && _form.price.on('keyup change', function (e, type) {
                _cache.price = _form.price.FormatVal('price');
                _form.trigger('__FormMax');
                DOM.trigger('__TradeFormChange', [_form, _cache, type, 'price change']);
                _btn.prop('disabled', false)
            }).on('keypress', function () {
                _btn.prop('disabled', true)
            }).on('change', function () {
                // console.log('change')
            });

            _form.amount && _form.amount.on('keyup change', function (e, type) {
            	if(e.target.id!='' && e.target.id!=null){
            		if(e.target.id=='sell_amount'){
            			var sell_price = $('#sell_price').val();
            			_cache.price = sell_price;
            		}else if(e.target.id=='buy_amount'){
            			var buy_price = $('#buy_price').val();
            			_cache.price = buy_price;
            		}
            	}
                _cache.amount = _form.amount.FormatVal('amount');
                DOM.trigger('__TradeFormChange', [_form, _cache, type, 'amount change']);
                _btn.prop('disabled', false)
            }).on('keypress', function () {
                _btn.prop('disabled', true)
            });


            _form.market_transaction_amount && _form.market_transaction_amount.on('keyup change', function (e, type) {
                _cache.market_t = _form.market_transaction_amount.FormatVal('price');
                DOM.trigger('__TradeFormChange', [_form, _cache, type, 'market_t change'])
            });

            _form.market_amount && _form.market_amount.on('keyup change', function (e, type) {
                _cache.market_a = _form.market_amount.FormatVal('amount');
                DOM.trigger('__TradeFormChange', [_form, _cache, type, 'market_a change'])
            });

            _loanS.on('change', function () {
                var _t = $(this);
                _form.trigger('__FormMax');
                DOM.trigger('__TradeFormChange', [_form, _cache, '', 'loan change']);

                //更新杠杆信息
                if (_t.prop('checked')) {
                     DOM.trigger('__GetLoanInfo', [_coinType, 'loan success']);
                    _DTAWrap.addClass('show_loan');
                } else {
                    _DTAWrap.removeClass('show_loan');
                }

            });

            _range.on('focus', function () {
                _form.trigger('__FormMax');
                _form.amount && _form.amount.trigger('__Focus');
                _form.transaction_amount && _form.transaction_amount.trigger('__Focus');
                _form.market_transaction_amount && _form.market_transaction_amount.trigger('__Focus');
                _form.market_amount && _form.market_amount.trigger('__Focus');
            }).on('blur', function () {
                $(this).parent().siblings().removeClass('focus');
            });

            //最大交易量
            _form.on('__FormMax', function () {
                if (!HRYB['BALANCE']) {
                    return
                }
                DOM.trigger('__TradeMax', [_form, {
                    "trade_type": _tradeType,
                    "available": HRYB['BALANCE'][_coin + '_available'] || 0,
                    "price": _cache.price,
                    "loan_type": _loanS && _loanS.prop('checked'),
                    "loan_enable": _caches.loan[_coin]['enable'],
                    "coin": _coin
                }, '__FormMax']);
            });

        });

        //快速写入
        J(Quick, $('#market_depth'), $('#quick_bar'));
        //快速写入
        function Quick(bar, callback) {
            var _this = this,
                _timer = null,
                _tri = _this,
                _bar = bar,
                _info = _bar.find('.info'),
                _tr, _type, _class,
                _dom = [$('#buy_price'), $('#buy_amount'), $('#sell_price'), $('#sell_amount')];

            _tri.on('mouseover', '.tr', function () {
                _tr = $(this);
                _this.Show(_tr);
            });
            _tri.on('mouseout', '.tr', function () {
                _this.Hide();
            });

            _bar.hover(function () {
                _this.Clear();
            }, function () {
                _this.Hide();
            }).on('click', 'span', function () {
                var _t = $(this),
                    _i = _t.attr('data-info') && _t.attr('data-info').split('|'),
                    _v = _i && isNaN(_i[0] * 1),
                    _p = _bar.find('.col_2').attr('data-info') * 1,
                    _tr = _bar.attr('data-type');
                if (!_i || !UID) {
                    return
                }

                if (_panelType == 'Lightning') {
                    if (_v) {
                        DOM.trigger('__LightTrade', [_p, _i[0], _tr])
                    }
                } else {
                    if (_panelType !== 'Limited') {
                        DOM.trigger('__OrderType', [1]);
                        DOM.trigger('__TradeTab', [1]);
                    }
                    if (_type == 'sell') {
                        AddFocus(_dom[0]).val(_i[0]).keyup();
                        _i[1] ? AddFocus(_dom[1]).val(_i[1]).keyup() : '';
                    } else {
                        AddFocus(_dom[2]).val(_i[0]).keyup();
                        _i[1] ? AddFocus(_dom[3]).val(_i[1]).keyup() : '';
                    }
                }
            });

            function AddFocus(obj) {
                obj.addClass('focus');
                setTimeout(function () {
                    obj.removeClass('focus')
                }, 300);
                return obj;
            }

            _this.Show = function (obj) {
                _this.Clear();
                var _xy = obj.offset();
                _class = (_panelType == 'Lightning' && UID) ? 'light' : '';
                _type = obj.data('type');
                var aa =_bar.css({
                    'display': 'block',
                    'top': _xy.top,
                    'left': _xy.left + 5
                }).attr('data-type', _type == 'buy' ? 'buy' : 'sell');
                
                if(aa!=undefined&&aa[0]!=undefined){
                	aa[0].className = _type == 'buy' ? 'font_buy ' + _class : 'font_sell ' + _class;
                }

                _info.html(obj.html());
            };
            _this.Hide = function () {
                _timer = setTimeout(function () {
                    _bar.hide();
                }, 50)
            };
            _this.Clear = function () {
                clearTimeout(_timer);
            };
        }

        //面板重置
        function PanelReset(form, type) {
            var _form = GetForm(form),
                _dTAmount = _form.find('.transaction_amount'),
                _dLAmount = _form.find('.loan_amount'),
                _dTip = _form.find('.trade_msg');
            if (type === 'all') {
                _form["price"].val('');
            }
            _form["amount"] && _form["amount"].val('').keyup();
            _form["market_transaction_amount"] && _form["market_transaction_amount"].val('').keyup();
            _form["market_amount"] && _form["market_amount"].val('').keyup();
            _dTip && _dTip.html('');
            _form["loan_switch"] && _form["loan_switch"].prop('checked', false).attr('data-loan-state', 0).change();
        }

        /*****闪电交易*****/
        var _light_state = $('#light_switch'),
            _light_times = $('#light_times'),
            _light_amount = $('#light_amount'),
            _light_price = $('#light_price'),
            _light_total = $('#light_total'),
            _light_cache = {},
            _light_min   = _light_amount.data('min');

        J(InFormat, _light_amount);

        _light_cache = {
            "times": 1,
            "amount": _light_amount.FormatVal('amount'),
            "total": mNum(mAcc.Mul(_light_cache.amount, _light_cache.times), 4),
            "state": _light_state.prop('checked')
        };


        LightTotal();
        function LightTotal() {
            _light_cache.total = mNum(mAcc.Mul(_light_cache.amount, _light_cache.times), 4);

            _light_total.html(_light_cache.total)
        }

        _light_amount.on('keyup', function () {
            var _t = $(this);
            _light_cache.amount = _light_amount.FormatVal('amount');
            LightTotal()
        }).on('change blur',function(){
            var _t = $(this),
                _v = _t.val()*1;
            if(_v<_light_min){
                _t.val(_light_min);
                _light_cache.amount = _light_min
            }
            LightTotal()
        });

        _light_times.on('click', 'span', function () {
            var _t = $(this),
                _v = _t.data('info') * 1;
            _t.addClass('cur').siblings().removeClass('cur');
            _light_cache.times = _v;
            LightTotal();
        });
        _light_state.on('change', function () {
            _light_cache.state = _light_state.prop('checked');
        });

        DOM.on('__LightTrade', function (e, p, t, tr) {
            if (_light_cache.state) {
                var _type = (tr == 'buy') ? (t == 'eat' ? 'sell' : 'buy') : (t == 'eat' ? 'buy' : 'sell');
                _light_price.html(p)[0].className = _type == 'buy' ? 'font_buy' : 'font_sell';
                console.log('__LightTrade')
                function TradeNow() {
                    LN({"message": window.Texts && Texts['ordering']});
                    var val = "";
                    if($.trim(_tradePwd) != ""){
                        val = M.hbmd5(_tradePwd);
                    }
                    TradeApi({
                        "type": _type,
                        "init": true,
                        "data": {
                            "price": p,
                            "amount": _light_cache.total,
                            coin_type: _coinType,
                            order_type: _orderType,
                            trade_pwd: val && val
                        }
                    }, function (data,code) {
                        console.log(code)
                        if(code == 'success'){
                            DOM.trigger('__TradeFormCallback', ['', data]);
                        }
                        if(code == 'error'){
                            DOM.trigger('__TradeFormError');
                        }

                    });
                }

                if (_dDialog_trade.is(':hidden') && check_use_trade_pwd()) {
                    TradePwd(function () {
                        TradeNow();
                    });
                    return false
                } else {
                    TradeNow();
                }
            } else {
                LN({"message": window.Texts && Texts['please_open_loan'], "type": 'error', "long": 5000});
                return false
            }

        });


        /*****Init*****/
        HRYB.NUM = mNum;
        //开关

        mSwitch({
            Switch: '.switch'
        });
        DOM.on('DoDocHidden', function () {
            HRYB['GLOBAL']['docHidden'] ? LO.GetOrder && LO.GetOrder.Stop() : LO.GetOrder && LO.GetOrder.Play();
        });

        //事件
        DOM.on('__TradeFormChange', function (e, f, d, r, t) {
            if (!HRYB['BALANCE']) {
                return
            }
            //成交额
            var _TA = WriteTA(_orderType == 'PlaceOrder' ? d['price'] : d['market_t'], d['amount'], f['transaction_amount']);
            //借贷额
            LoanAmount([_TA, _orderType == 'PlaceOrder' ? d['amount'] : d['market_a']], HRYB['BALANCE'][f['_coin'] + '_available'] || 0, f['_type'], f['loan_amount'], f['_coin'], f['loan_switch'].prop('checked'));
            if (r !== 'slide') {
                //滑块比例
                if (f['_type'] === 'buy') {
                    d.amount !== '' && _orderType == 'PlaceOrder' && UpRange(buyRange, _caches.trade.buy_max, d.amount);
                    d.market_t !== '' && _orderType == 'PlaceMarketOrder' && UpRange(buyRangeMp, _caches.trade.buy_max_mp, d.market_t);
                } else {
                    d.amount !== '' && _orderType == 'PlaceOrder' && UpRange(sellRange, _caches.trade.sell_max, d.amount);
                    d.market_a !== '' && _orderType == 'PlaceMarketOrder' && UpRange(sellRangeMp, _caches.trade.sell_max_mp, d.market_a);
                }
            }

        }).on('__TradeMax', function (e, f, v, t) {
            if (!HRYB['BALANCE']) {
                return
            }
            //最大交易
            MaxVolume(v); 
            MaxWrite(f['_type'], _orderType == 'PlaceOrder' ? f['amount'] : f['_type'] == 'buy' ? f["market_transaction_amount"] : f['market_amount']);
        }).on('__TradeFormBeforeSubmit', function (e, f) {
        	
            var _btn = f.find(':submit'),
                _btnT = _btn.data('msg-submit').split(',');
            //_btn.text(_btnT[1] + _btnT[3]);
            LN({"message": _btnT[1] + _btnT[3]});

        }).on('__TradeFormCallback', function (e, f, d) {
            var _type = f && f.attr('data-form-type'),
                _btn = f && f.find(':submit'),
                _btnT = f && _btn.data('msg-submit').split(',');
            if (d.success == true) {
                //更新委托
                LO.GetOrder.Play();
                _orderType === 'PlaceMarketOrder' ?
                    setTimeout(function () {
                        LO.GetHistory.Fire()
                    }, 1000) :
                    setTimeout(function () {
                        LO.GetOrder.Fire()
                    }, 1000);
                //消息条
                LN({"message": d.msg, "long": 1800});
                
                _btn && _btn.text(_btnT[0]+" "+CURRENT_SYMBOL.split("_")[0]);
            } else {
            	if(d.market=="nomsg"){
            		//d.msg = '超时，请重新登录！';
            		window.location.href = _ctx+"/login";
            		return false;
            	}
            	if(d.msg=='' || d.msg == null){
            		d.msg = '请重新登录.';
            	}
                LN({"message": d.msg, "type": 'error', "long": 5000});
                //_btn && _btn.text(_btnT[0]);
            }
            //重置面板
            PanelReset(f);
            _dDialog_trade.attr('data-init', '');
            //更新balance
            setTimeout(function () {
            	HRYB['AJAX']['GetBalance'].Fire();
            },1000)

        }).on('__TradeFormError',function(e, f){
            var _type = f && f.attr('data-form-type'),
                _btn = f && f.find(':submit'),
                _btnT = f && _btn.data('msg-submit').split(',');
            _dDialog_trade.attr('data-init', '');
            LN({"message": window.Texts && Texts['tradeError'], "type": 'error', "long": 5000});
            _btn && _btn.text(_btnT[0]);
        }).on('__LoanWrite', function () {
            for (var _i in _caches.loan) {
                var _d = _caches.loan[_i];
                $('.loan_' + _i).html(/btc|ltc/.test(_i) ? mNum(_d['enable'], 4) : mNum(_d['enable'], 4)).attr('data-enable', _d['enable']);

                //有可用借贷开启开关
                if (_d['enable']) {
                    $('.loan_switch_' + _i).prop('disabled', false).trigger('__Update')
                }else{
                    $('.loan_switch_' + _i).prop('disabled', true).trigger('__Update')
                }
            }
        }).on('__GetLoanNow', function (e, f, c, a) {
            var _data,
                _btn = f.find(':submit'),
                _btnT = _btn.data('msg-submit').split(',');
            //状态值 0初始 1正在 2成功
            GetLoanNow({"loan_type": f['_coin'], "amount": a}, function (data) {
                var _code = data.code;
                switch (_code) {
                    case 'loading':
                        f['loan_switch'].attr('data-loan-state', 1);
                        _btn.text(_btnT[2] + _btnT[3]);
                        LN({"message": _btnT[2] + _btnT[3]});
                        break;
                    case 'error':
                        f['loan_switch'].attr('data-loan-state', 0);
                        break;
                    case 'success':
                        _data = data['data'];
                        if (_data.code === 0) {
                            f['loan_switch'].attr('data-loan-state', 2);

                            //更新balance
                            HRYB['AJAX']['GetBalance'].Fire();

                            //更新杠杆信息
                            DOM.trigger('__GetLoanInfo', [_coinType, 'loan success']);

                            //提交委托
                            f.submit();
                            break;
                        } else {
                            LN({"message": _data.msg, type: 'error', "long": 5000});
                            f['loan_switch'].attr('data-loan-state', 0);
                            _btn.text(_btnT[0]);
                        }
                }
            })

        }).on('__GetLoanInfo', function (e, t, y) {
            switch (t) {
                case 'cny_btc':
                    LoanCB.Fire();
                    break;
                case 'cny_ltc':
                    LoanCL.Fire();
                    break;
                case 'usd_btc':
                    LoanUB.Fire();
                    break;
            }
        }).on('__OrderType', function (e, i) {
            var _p = $('.panel').eq(i);
            OrderType(_p);
            _p.find('form').each(function(){
                PanelReset($(this));
            })
        });

    }();
});