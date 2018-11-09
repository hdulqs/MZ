/**
 * Created by andyk on 2015/9/1.
 * 交易面板页面
 * todo 更新杠杆额度 *
 * todo 借贷 ajax 150923 0431
 * 正在买入状态问题
 */

define(function (require, exports) {
    var PC   = require('./page_common'),
        mAcc = require('./module_accurate'),  //精确算法
        mNum = require('./module_number'),    //数字处理
        mV   = require('./module_validator'),    //表单校验
        mR   = require('./module_range'),        //滑块
        mT   = require('./module_table'),        //表格
        mO   = require('./module_order'),        //委托单
        mC   = require('./module_cookie'),       //Cookie
        mA   = require('./module_check_all'),    //选取
        mD   = require('./module_dialog'),
        mI   = require('./module_inform'),
        mM   = require('./module_md5'),
        mHB  = require('./module_hb_extend'),
        mWin = require('./module_window'),
        mX   = require('./module_ajax'),
        mS   = require('./module_switch'),
        M  = require('./module_md5'),
        J    = require('./module_this_jack'),
        MTmp = require('./module_tmpl'),
        mUTT   = require('./module_time'),
        orderType = 'PlaceOrder', //默认交易类型
        coinType  = 'cny_btc',    //默认币种
        symbol,
        csrf,
        DOM  = $(document),
        HBEvert = {},
        limit,
        loanLimit,
        systemLimit,
        buyRatio          = 0,         //买入默认比例
        sellRatio         = 0;         //卖出默认比例
        window.mNum = mNum;
        window.UTT = mUTT;
        //限制交易量
        limit = {
            "buyMaxAmount": 0,         //限价买
            "sellMaxAmount": 0,         //限价卖
            "buyMaxAmountMp": 0,         //市价买
            "sellMaxAmountMp": 0,         //市价卖"

            //限价限制交易量
            "BuyBtc": 1000,      //买入币数CNY-BTC
            "SellBtc": 1000,      //卖出币数CNY-BTC
            "BuyLtc": 20000,     //买入币数CNY-LTC
            "SellLtc": 20000,     //卖出币数CNY-LTC
            "BuyUsdBtc": 1000,      //买入币数USD-BTC
            "SellUsdBtc": 1000,      //卖出币数USD-BTC
            "BuyUsdLtc": 20000,     //买入币数USD-LTC
            "SellUsdLtc": 20000,     //卖出币数USD-LTC

            //市价限制交易量
            "BuyBtcMp": 1000000,   //市价买入金额CNY-BTC
            "SellBtcMp": 1000,      //市价卖出币数CNY-BTC
            "BuyLtcMp": 1000000,   //市价买入金额CNY-LTC
            "SellLtcMp": 20000,     //市价卖出币数CNY-LTC
            "BuyUsdBtcMp": 100000,    //市价买入金额USD-BTC
            "SellUsdBtcMp": 1000,      //市价卖出币数USD-BTC
            "BuyUsdLtcMp": 1000000,   //市价买入金额USD-LTC
            "SellUsdLtcMp": 20000,     //市价卖出币数USD-LTC

            //杠杆借贷额度
            "LoanCny":100,
            "LoanCnyBtc":1,
            "LoanCnyLtc":100,
            "LoanUsd":100,
            "LoanUsdBtc":1
        };

        //借贷额度
        loanLimit = {
            "cny_loan_enable" : 0,
            "cny_btc_loan_enable" : 0,
            "cny_ltc_loan_enable" : 0,
            "usd_usd_loan_enable" : 0,
            "usd_btc_loan_enable" : 0,
            "cny_loan_total" : 0,
            "cny_btc_loan_total" : 0,
            "cny_ltc_loan_total" : 0,
            "usd_loan_total" : 0,
            "usd_btc_loan_total" : 0
        };

        //系统最小借贷额度
        systemLimit = {
            "cny_cny":100,
            "cny_btc":0.1,
            "cny_ltc":1,
            "usd_usd":50,
            "usd_btc":0.1,
            "usd_ltc":1
        };

        window.loanLimit = loanLimit;
        //币种
        coinType = document.getElementsByName('coin_type')[0].value;
        symbol = coinType.split('_');
        //csrf
        csrf = document.getElementsByName('_csrf')[0] && document.getElementsByName('_csrf')[0].value;

        //DOM List
        DOM['#panel_wrap']   = $('#panel_wrap');
        DOM['#order_list']   = $('#order_list');
        DOM['#history_list'] = $('#history_list');
        DOM['#mod_order']   = $('#mod_order');
        DOM['#mod_history'] = $('#mod_history');
        DOM['#depth_top']   = $('#depth_top');
        DOM['.form_trade']  = $('.form_trade');
        DOM['#cny_cny_a_panel'] = $('#cny_cny_a_panel');
        DOM['#cny_btc_a_panel'] = $('#cny_btc_a_panel');
        DOM['#usd_usd_a_panel'] = $('#usd_usd_a_panel');
        DOM['#usd_btc_a_panel'] = $('#usd_btc_a_panel');
        DOM['#cny_ltc_a_panel'] = $('#cny_ltc_a_panel');
        DOM['#cny_cny_l_panel'] = $('#cny_cny_l_panel');
        DOM['#cny_btc_l_panel'] = $('#cny_btc_l_panel');
        DOM['#cny_ltc_l_panel'] = $('#cny_ltc_l_panel');
        DOM['#usd_usd_l_panel'] = $('#usd_usd_l_panel');
        DOM['#usd_btc_l_panel'] = $('#usd_btc_l_panel');
        DOM['#market']          = $('#market');

        DOM['order_type']   = DOM['#panel_wrap'].find('[name="order_type"]');
        DOM['coin_type']    = $('[name="coin_type"]').eq(0);

        //Ajax List
        var HBBalance  = window.HUOBI.AJAX.GetBalance || null,
            GetOrder   = DOM['#panel_wrap'].length > 0 ? mO.GetOrder({temp: 'order_list_tmp', wrap: 'order_list', coin_type: coinType, is_history: 0, damp:true},function(){GetHistory && GetHistory.Fire()}) : null,
            GetHistory = DOM['#panel_wrap'].length > 0 ? mO.GetOrder({temp: 'history_list_tmp', wrap: 'history_list', coin_type: coinType, is_history: 1, delay:0}) : null;

        var GetLoan  = function(loan_type,init){
            if(DOM['#panel_wrap'].length <= 0) return ;
            return new mX({"url":'/loan/index.php',"data":{"a":'get_loan_available_multi',"loan_types":loan_type},"init":init},function(data) {
                var _code = data.code;
                switch (_code) {
                    case 'loading':
                        break;
                    case 'error':
                        break;
                    case 'success':
                        var _data = data['data'],
                            _enable;

                        if(_data.code === 0 && _data['ext']){
                            $.each(_data['ext'],function(i,c){
                                _enable = c['enable'] * 1;
                                switch(i){
                                    case 'cny_cny':
                                        loanLimit["cny_loan_enable"] = _enable;
                                        loanLimit["cny_loan_total"] = mAcc.Add(_enable,HUOBI['BALANCE']['cny_available']||0);
                                        break;
                                    case 'cny_btc':
                                        loanLimit["cny_btc_loan_enable"] = _enable;
                                        loanLimit["cny_btc_loan_total"] = mAcc.Add(_enable,HUOBI['BALANCE']['cny_btc_available']||0);
                                        break;
                                    case 'cny_ltc':
                                        loanLimit["cny_ltc_loan_enable"] = _enable;
                                        loanLimit["cny_ltc_loan_total"] = mAcc.Add(_enable,HUOBI['BALANCE']['cny_ltc_available']||0);
                                        break;
                                    case 'usd_usd':
                                        loanLimit["usd_loan_enable"] = _enable;
                                        loanLimit["usd_loan_total"] = mAcc.Add(_enable,HUOBI['BALANCE']['usd_available']||0);
                                        break;
                                    case 'usd_btc':
                                        loanLimit["usd_btc_loan_enable"] = _enable;
                                        loanLimit["usd_btc_loan_total"] = mAcc.Add(_enable,HUOBI['BALANCE']['usd_btc_available']||0);
                                        break;
                                }
                            });


                            //console.log('更新杠杆额度ajax',loanLimit,loan_type)
                            DOM.trigger('__LoanUpdate');
                        }


                        break;
                }
            });
        };

        var LoanNow = function(option,callback){
            var _op = option || {},
                _amount= _op.amount || 0,
                _loanType = _op.loan_type,
                _fn ;
                if(_amount<=0) return;
                _fn = new mX({"url":'/loan/index.php',"data":{"a":'do_loan',"loan_type":_loanType,"currency":_amount,"_csrf":csrf,"check":'on'},"type":'POST',"random":false},function(data) {
                    callback && callback(data)
                })
            return _fn;
        };

        //共享到HUOBI
        mHB({'name': 'AJAX', 'value': {'GetOrder': GetOrder}});
        mHB({'name': 'AJAX', 'value': {'GetHistory': GetHistory}});
        mHB({'name': 'DOM', 'value': DOM});

    var pageInfo = DOM['coin_type'].attr('data-page');
    //注册借贷Ajax
    var LoanCB = GetLoan('cny_cny,cny_btc',coinType=='cny_btc'),
        LoanCL = GetLoan('cny_cny,cny_ltc',coinType=='cny_ltc'),
        LoanUB = GetLoan('usd_usd,usd_btc',coinType=='usd_btc');

    //买卖盘
    var MR = DOM['#market'].length>0 ? require('./page_trade_market') : '';
        //买卖盘写入面板
        DOM['#market'].on('click', 'li', function(){
        var _this         = $(this),
            _data         = _this.attr('data-price') ? _this.attr('data-price').split(','):[],
            _price        = _data[0],
            _dPrice       = $('.panel_wrap').find('.form_trade').find('[name="price"]');

        if(orderType === 'PlaceOrder'){
            WriteIn()
        }else{
            OrderType(0);
            WriteIn()
        }
        function WriteIn(){
            _dPrice.val(_price).addClass("focus");
            setTimeout(function(){
                _dPrice.removeClass("focus");
            },300)
        }
    });


    //处理模板
    var _tempCache = {};
    function DataView(dom, data) {
        var _view = document.getElementById(dom) || document.getElementById('view_' + dom);
        if(!_tempCache[dom] && !document.getElementById('tmp_' + dom) && !_view.getElementsByTagName('script')[0])return;
        _tempCache[dom] = _tempCache[dom] ? _tempCache[dom] : document.getElementById('tmp_' + dom) ? document.getElementById('tmp_' + dom).innerHTML : _view.getElementsByTagName('script')[0].innerHTML;
        _view.innerHTML = MTmp( _tempCache[dom], data);
    }

    //类型切换
    function OrderType(type){
        orderType = type ? 'PlaceMarketOrder' : 'PlaceOrder';
        DOM.trigger('__OrderType',[type]);
    }

    //面板切换
    function PanelType(o,i){
        if(o==='PlaceOrder'){
            DOM['#panel_wrap'].hasClass('market_price') && DOM['#panel_wrap'].removeClass('market_price');
            DOM['#panel_wrap'].find('.trade_amount').show();
        }else if(o==='PlaceMarketOrder'){
            !DOM['#panel_wrap'].hasClass('market_price') && DOM['#panel_wrap'].addClass('market_price');
            DOM['#panel_wrap'].find('.trade_amount').hide();
        }
        DOM['order_type'].val(orderType);
        panelTab.find('a').eq(i).addClass('cur').siblings().removeClass('cur');
    }
    //开关效果
    mS({
        Switch:'.switch'
    });

    //切换交易类型
    var panelTab = DOM['#panel_wrap'].find('.wrap_tabs');
    panelTab.on('click','a',function(){
        var _t = $(this);
        OrderType(_t.index());
        return false;
    });

    //遍历Form
    function GetForm(form){
        if(form['Type']==='HBForm'){
            return form
        }else{
        var _form = $(form);
        _form.find('[name]').each(function(i,c){
            var _t = $(c),
                _name = _t.attr('name');
            _form[_name] = _t;
            _form[_name]['Value'] = _t.val()
        });
        _form['Type'] = 'HBForm';
        return _form;
        }
    }

    //数字修正
    function NumFix(type, num) {
        var _num;

        if(/\.$/.test(num)){
            return mNum(num)
        }
        type == 'price' ? _num = mNum(num, 3) : _num = mNum(num, 4);
        return _num;
    }

    //交易面板表单
    var BtnSubmit = '';
    mV({
        forms: DOM['.form_trade'],
        tip: '.trade_msg',
        beforeSend: function (_form) {
            DOM.trigger('__TradeFormBeforeSend',[_form]);
        },
        beforeValidation: function (_form) {
            DOM.trigger('__TradeFormValidation',[_form]);
        },
        beforeSubmit: function (form) {
            var _form = GetForm(form),
                _loan = _form['loan_type'],
                _loanType = _loan && _loan.data('loan-type'),
                _loanState= _loan && _loan.attr('data-loan-state') * 1,
                _loanAmount= _form.find('.loan_amount').attr('data-amount')*1;

            if(_loanAmount>0 && _loan.prop('checked')){
                switch (_loanState){
                    case 0:
                        //初始状态
                        DOM.trigger('__GetLoan',[_form, _loanType, _loanAmount]);
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
            DOM.trigger('__TradeFormBeforeSubmit',[_form]);
        },
        postData:function(data){
            var _data = [];
            console.log(data);
            $.each(data, function(i, field){
                //field['name'] != 'password' ? _data.push(field) : _data.push({'name':'password','value':M.hbmd5(field['value'])});
                var val = "";
                if($.trim(field['value']) != ""){
                    val = M.hbmd5($.trim(field['value']));
                }
                field['name'] != 'trade_pwd' ? _data.push(field) : _data.push({'name':'trade_pwd','value':val});
            });
            return _data;
        },
        callback: function (data, _form) {
            DOM.trigger('__TradeFormCallback', [_form,data]);
        },
        ajaxError:function(_form){
            DOM.trigger('__TradeFormError', [_form]);
        }
    });

    //交易面板资金密码
    function show_trade_pwd(_form) {
        var _s = false;
        if (check_use_trade_pwd()) {
            // 当前表单的才阻断提交
            var _form_trade_pwd = _form.find('.trade_pwd_group');
            if (_form_trade_pwd.is(':hidden')) {
                _form_trade_pwd.show();
                _s = true;
            }
            $('.trade_pwd_group').each(function () {
                var _t = $(this);
                if (_t.is(':hidden')) {
                    _t.show();
                }
            });
        }
        return _s;
    }
    // 资金密码
    function check_use_trade_pwd() {
        var trade_pwd_on = mC.Cookie('trade_pwd_on');
        var trade_pwd_time = mC.Cookie('trade_pwd_time');
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

    //买入滑块
 var buyRange = new mR({
        range : '.range_buy',
        step  : 1,
        point : 4,
        slide: function (x,o) {
            Slide(x, o, 'buy')
        }
    }),
    //卖出滑块
     sellRange = new mR({
        range: '.range_sell',
        step  : 1,
        point : 4,
        slide: function (x,o) {
            Slide(x, o, 'sell')
        }
    }),

    //修改买入滑块
     editBuyRange = new mR({
        range: '.range_edit_buy',
        step  : 1,
        point : 4,
        slide: function (x,o) {
            Slide(x, o, 'buy','edit')
        }
    });
    //修改卖出滑块
    var editSellRange = new mR({
        range: '.range_edit_sell',
        step  : 1,
        point : 4,
        slide: function (x,o) {
            Slide(x, o, 'sell','edit')
        }
    });


    /**
     *最大交易量
     * @param option
     * trade_type: buy/sell
     * loan_type: true/false
     * price: 单价
     * un_price: 剩余额度
     * un_amount: 剩余数量
     */
    function LargestVolume(option){

        var _op = option || {},
            _tradeType = _op['trade_type'],
            _loanType  = _op['loan_type'],
            _price     = _op['price'],
            _available = _op['available'],
            _loanTotal = mAcc.Add(_op['loan_enable'],_available),
            _unPrice   = _op['un_price'],
            _unAmount  = _op['un_amount'],
            _A = [];
        switch (coinType) {
            //人民币-BTC
            case 'cny_btc':
                _A = Amount(
                    [(_unPrice || _available), (_unAmount || _available)],
                    [_loanTotal , _loanTotal ],
                    _price,
                    [limit['BuyBtc'],limit['SellBtc']],
                    [limit['BuyBtcMp'],limit['SellBtcMp']]
                );
                _A[0] !== undefined ? limit['buyMaxAmount']   = _A[0] : '';
                _A[1] !== undefined ? limit['buyMaxAmountMp'] = _A[1] : '';
                _A[2] !== undefined ? limit['sellMaxAmount']  = _A[2] : '';
                _A[3] !== undefined ? limit['sellMaxAmountMp']= _A[3] : '';

                break;
            //人民币-LTC
            case 'cny_ltc':
                _A = Amount(
                    [(_unPrice || _available), (_unAmount || _available)],
                    [_loanTotal , _loanTotal ],
                    _price,
                    [limit['BuyLtc'],limit['SellLtc']],
                    [limit['BuyLtcMp'],limit['SellLtcMp']]
                );
                _A[0] !== undefined  ? limit['buyMaxAmount']   = _A[0] : '';
                _A[1] !== undefined  ? limit['buyMaxAmountMp'] = _A[1] : '';
                _A[2] !== undefined  ? limit['sellMaxAmount']  = _A[2] : '';
                _A[3] !== undefined  ? limit['sellMaxAmountMp']= _A[3] : '';

                break;
            //美元-BTC
            case 'usd_btc':
                _A = Amount(
                    [(_unPrice || _available), (_unAmount || _available)],
                    [_loanTotal , _loanTotal],
                    _price,
                    [limit['BuyBtc'],limit['SellBtc']],
                    [limit['BuyBtcMp'],limit['SellBtcMp']]
                );
                _A[0] !== undefined  ? limit['buyMaxAmount']   = _A[0] : '';
                _A[1] !== undefined  ? limit['buyMaxAmountMp'] = _A[1] : '';
                _A[2] !== undefined  ? limit['sellMaxAmount']  = _A[2] : '';
                _A[3] !== undefined  ? limit['sellMaxAmountMp']= _A[3] : '';

                break;
            //美元-LTC
            case 'usd_ltc':
                _A = Amount(
                    [(_unPrice || _available), (_unAmount || _available)],
                    [_loanTotal, _loanTotal],
                    _price,
                    [limit['BuyLtc'],limit['SellLtc']],
                    [limit['BuyLtcMp'],limit['SellLtcMp']]
                );
                _A[0] !== undefined  ? limit['buyMaxAmount']   = _A[0] : '';
                _A[1] !== undefined  ? limit['buyMaxAmountMp'] = _A[1] : '';
                _A[2] !== undefined  ? limit['sellMaxAmount']  = _A[2] : '';
                _A[3] !== undefined  ? limit['sellMaxAmountMp']= _A[3] : '';
                break;
        }

        /**
         * @param a  [可用钱,可用币]
         * @param la 杠杆[可用额,可用币]
         * @param p  交易价
         * @param l  [买限制,卖限制]
         * @param lm [市价买限制，卖限制]
         * @returns {Array} [买限价量,买市价量,卖限价量,卖市价量]
         * @constructor
         */
        function Amount(a,la,p,l,lm){
            var _a = [];
            if (_tradeType === 'buy') {
                //限价
                _a[0] = p ? mAcc.Div(_loanType ? la[0] : a[0], p ) : 0;
                _a[0] = _a[0] < l[0] ? _a[0] : l[0];
                //市价
                _a[1] = _loanType ? la[0] : a[0];
                _a[1] = _a[1] < lm[0] ? _a[1] : lm[0];
            } else {
                //限价
                _a[2] = _loanType ? la[1] : a[1];
                _a[2] = _a[2] < l[1] ? _a[2] : l[1];
                //市价
                _a[3] =_loanType ? (la[1] < lm[1] ? la[1] : lm[1]) : (a[1] < lm[1] ? a[1] : lm[1]);
            }
            //console.log('计算最大交易量',a,la,p,l,lm,_a)
            return _a;
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
        if (orderType === 'PlaceOrder') {
            if (a && p) {
                _t = mAcc.Mul(a * 1, p * 1);
                box && box.html(mNum(_t, 3)).attr('actual',_t)
            } else {
                _t = 0;
                box && box.html('0.00')
            }
        }else{
            _t = p !== undefined ? p : a;
        }
        return _t;
    }

    /**
     *借贷额度
     * @param total     [买入总额,卖出总量]
     * @param available 可用交易额
     * @param type      交易类型(buy/sell)
     * @param box       容器
     * @param loanType  借贷类型
     * @returns {number}
     * @constructor
     */
    function LoanAmount(total,available,type,box,loanType){
        var _loan = 0;

        if(type === 'buy'){
            _loan =  mAcc.Sub(total[0],available) > 0 ? mAcc.Sub(total[0],available) : 0;
        }else{
            _loan =  mAcc.Sub(total[1],available) > 0 ? mAcc.Sub(total[1],available) : 0;
        }

        //系统借贷额度
        _loan =  (_loan > 0 && _loan < systemLimit[loanType]) ? systemLimit[loanType] : _loan;

        if(type === 'buy'){
            box && box.html(Carry(mNum(_loan),'currency')).attr('data-amount',Carry(mNum(_loan),'currency'));
        }else{
            box && box.html(Carry(mNum(_loan),'coin')).attr('data-amount',Carry(mNum(_loan),'coin'));
        }
        return _loan;
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
            _regP = /^(([0-9]{1}\d*)|([0]{1}))(\.(\d){0,2})?$/,
            _regA = /^(([0-9]{1}\d*)|([0]{1}))(\.(\d){0,4})?$/;

        /**
         * @return {number}
         */
        _this.FormatVal = function (type) {
            var _v = _this.val();
            if (_v < 0) {
                _v = 0;
                _this.val(_v)
            }
            if (_v != '' && !(type == 'price' ? _regP : _regA).test(_v)) {
                _v = NumFormat(_v*1, type == 'price' ? 2 : 4);
                _this.val(_v);
            }
            return _v * 1;
        }
    }

    /**
     *面板监听
     * @param form 表单选择器
     */
    function HandlingEvent(form) {
        form.each(function () {
            var _form       = GetForm($(this)),
                _formType   = _form.attr('data-form-type'),
                _type       = _form.attr('data-trade-type'),
                _DP         = _form["price"],
                _DA         = _form["amount"],
                _DTA        = _form.find(".transaction_amount"),
                _DMA        = _form["market_amount"],
                _DMTA       = _form["market_transaction_amount"],
                _DMax       = _form.find('.max_amount'),
                _DMaxWrap   = _DMax.parent(),
                _DMaxB      = _DMax.find('b'),
                _DLS        = _form["loan_type"],
                _DLAW       = _form.find('.loan_amount_wrap'),
                _DTAW       = _form.find('.trade_amount'),
                _DLA        = _form.find('.loan_amount'),
                _DR         = _form.find('[data-ratio]'),
                _DAV        = _form.find('.available'),
                _DUP        = _form.find('.un_price'),
                _DUA        = _form.find('.un_amount'),
                _DLT        = _form.find('.loan_available'),
                _DRange     = _form.find('.range_wrap'),
                _DCredit    = _form.find('.credit_bar'),
                _VLT        = _DLS && _DLS.attr('data-loan-type'),
                _VLS, _VP,_VA,_VTA,_VMA,_VMTA, _VUP, _VUA, _VLE;

                _form['_DMax']  = _DMax;
                _form['_DMaxB'] = _DMaxB;
                _form['_type']  = _type;
                _form['_DLA']   = _DLA;
                _form['_DTA']   = _DTA;
                _form['_VLT']   = _VLT;

            J(InFormat,_DP);
            J(InFormat,_DA);
            J(InFormat,_DMTA);
            J(InFormat,_DMA);
            //价格
            _DP && _DP.on('keyup',function(){
                _VP = _DP.val()*1;
                _VA = _DA.val()*1;
                _VLE=_DLT.attr('data-enable')*1;

                _DP.FormatVal('price');

                if (_type === 'buy') {
                    _VUP = _DUP.html()*1;
                    _VUA = _DUA.html()*1;
                    //最大交易量
                    DOM.trigger('__FormMax',[_form,{
                        "trade_type":_type,
                        "loan_type":_DLS&&_DLS.prop('checked'),
                        "price":_VP*1,
                        "available":_DAV.attr('data-flaunt')*1,
                        "un_price":_VUP,
                        "un_amount":_VUA,
                        "loan_enable":_VLE
                    },'price keyup']);
                }

                _DA.trigger('change');
            }).blur(function(){
                _VP == 0 && _DP.val('');
            });

            //数量
            _DA && _DA.on('keyup', function(){
                _VP = _DP.val()*1;
                _VA = _DA.val()*1;
                _form['_VAV'] = _DAV.attr('data-flaunt')*1;

                _DA.FormatVal('amount');

                if(_type === 'buy'){
                    /*更新滑块比例*/
                    _formType=='edit' ? UpRange(editBuyRange,limit['buyMaxAmount'],_VA) : UpRange(buyRange,limit['buyMaxAmount'],_VA)
                }else{
                    _formType=='edit' ?  UpRange(editSellRange,limit['sellMaxAmount'],_VA) : UpRange(sellRange,limit['sellMaxAmount'],_VA)
                }

                _VP = _DP.val()*1;
                _VA = _DA.val()*1;
                DOM.trigger('__FormChange',[_form, _VP, _VA, 'amount keyup']);
            }).on('focus', function(){
                _VUP = _DUP.html()*1;
                _VUA = _DUA.html()*1;
                _VP = _DP.val();
                _VLE=_DLT.attr('data-enable')*1;
                //最大交易量
                !_form['_AM'] && DOM.trigger('__FormMax', [_form, {
                    "trade_type": _type,
                    "loan_type": _DLS&&_DLS.prop('checked'),
                    "price": _VP * 1,
                    "available": _DAV.attr('data-flaunt')*1,
                    "un_price": _VUP,
                    "un_amount": _VUA,
                    "loan_enable":_VLE
                },'amount focus']);
                _DMaxWrap.addClass('focus');
            }).blur(function(){
                _DMaxWrap.removeClass('focus');
                _VA == 0 && _DA.val('');
            }).change(function(){
                _VP = _DP.val();
                _VA = _DA.val();
                _form['_VAV'] = _DAV.attr('data-flaunt')*1;
                DOM.trigger('__FormChange',[_form, _VP, _VA, 'amount change']);
            });

            //市价买
            _DMTA && _DMTA.on('keyup',function(){
                _VMTA = _DMTA.val();

                _DMTA.FormatVal('price');

                _form['_VAV'] = _DAV.attr('data-flaunt')*1;
                /*更新滑块比例*/
                _formType!=='edit' && UpRange(buyRange,limit['buyMaxAmountMp'],_VMTA);

                DOM.trigger('__FormChange',[_form, _VMTA, _VA, 'market_transaction_amount keyup']);
            }).change(function(){
                _VMTA = _DMTA.val()*1;
                _form['_VAV'] = _DAV.attr('data-flaunt')*1;
                DOM.trigger('__FormChange',[_form, _VMTA, _VA, 'market_transaction_amount change']);
            });

            //市价卖
            _DMA && _DMA.on('keyup', function(){
                _VMA = _DMA.val();

                _DMA.FormatVal('amount');

                _form['_VAV'] = _DAV.attr('data-flaunt')*1;

                /*更新滑块比例*/
                _formType!=='edit' && UpRange(sellRange,limit['sellMaxAmountMp'],_VMA);

                DOM.trigger('__FormChange',[_form, _VP, _VMA, 'market_amount keyup']);
            }).change(function(){
                _VMA = _DMA.val();
                _form['_VAV'] = _DAV.attr('data-flaunt')*1;
                DOM.trigger('__FormChange',[_form, _VP, _VMA, 'market_amount change']);
            });

            //借贷
            _DLS && _DLS.change(function(){
                _VUP = _DUP.html()*1;
                _VUA = _DUA.html()*1;
                _VLS =  _DLS.prop('checked');
                _VLE=_DLT.attr('data-enable')*1;
                //更新杠杆信息
                _VLS && DOM.trigger('__UpLoan',[coinType, 'loan_type change']);
                //最大交易量
                DOM.trigger('__FormMax', [_form, {
                    "trade_type": _type,
                    "loan_type": _VLS,
                    "price": _VP * 1,
                    "available": _DAV.attr('data-flaunt')*1,
                    "un_price": _VUP,
                    "un_amount": _VUA,
                    "loan_enable":_VLE
                },'loan change']);

                _form['_VAV'] = _DAV.attr('data-flaunt')*1;

                if(_type === 'buy'){
                    /*更新滑块比例*/
                    orderType==='PlaceOrder' ?  _formType!=='edit' && UpRange(buyRange,limit['buyMaxAmount'],_VA) :  _formType!=='edit' && UpRange(buyRange,limit['buyMaxAmountMp'],_VA);
                }else{
                    orderType==='PlaceOrder' ?  _formType!=='edit' && UpRange(sellRange,limit['sellMaxAmount'],_VA) :  _formType!=='edit' && UpRange(buyRange,limit['sellMaxAmountMp'],_VA);
                }


                if(_VLS){
                    _DLAW.show();
                    _DTAW.show();
                    _DCredit.removeClass('disabled')
                } else{
                    _DLAW.hide();
                    orderType==='PlaceMarketOrder' && _DTAW.hide();
                    _DCredit.addClass('disabled')
                }

            });

            //滑块
            _DRange.focus(function(){
                _DMaxWrap.addClass('focus');
            }).blur(function(){
                _DMaxWrap.removeClass('focus');
            }).on('focus ',function(){
                //__rangeSlide
                _VUP = _DUP.html()*1;
                _VUA = _DUA.html()*1;
                _VP = _DP.val();
                _VLE=_DLT.attr('data-enable')*1;
                //最大交易量
                DOM.trigger('__FormMax', [_form, {
                    "trade_type": _type,
                    "loan_type": _DLS&&_DLS.prop('checked'),
                    "price": _VP * 1,
                    "available": _DAV.attr('data-flaunt')*1,
                    "un_price": _VUP,
                    "un_amount": _VUA,
                    "loan_enable":_VLE
                },' range slide']);
            });

            //表单
            _form.on('__FormActive',function(e){
                if(!_DP)return;
                _VUP = _DUP.html()*1;
                _VUA = _DUA.html()*1;
                _VP = _DP.val();
                _VLE=_DLT.attr('data-enable')*1;
                //最大交易量
                DOM.trigger('__FormMax', [_form, {
                    "trade_type": _type,
                    "loan_type": _DLS&&_DLS.prop('checked'),
                    "price": _VP * 1,
                    "available": _DAV.attr('data-flaunt')*1,
                    "un_price": _VUP,
                    "un_amount": _VUA,
                    "loan_enable":_VLE
                },'Form Active']);
            })
        });
    }

    //滑块操作
    function Slide(x, o, t, e) {
        var _o = $(o),
            _ratio = _o.find('.range_ratio'),
            _form = GetForm(_o.parents('form')),
            _amount = _form["amount"],
            _mAmount= _form["market_amount"],
            _tAmount = _form["transaction_amount"],
            _mTAmount= _form["market_transaction_amount"],
            tradeRatio = x / 100;

        tradeRatio ? _ratio.show() : _ratio.hide();
        _ratio.css({
            'left': x + '%'
        });

        if (orderType === 'PlaceOrder' || e === 'edit') {
            //限价
            if (t === 'buy') {
                _amount.val(NumFix('amount', mAcc.Mul(limit['buyMaxAmount'], tradeRatio))).change().trigger('__Focus');
            } else {
                _amount.val(NumFix('amount', mAcc.Mul(limit['sellMaxAmount'], tradeRatio))).change().trigger('__Focus');
            }
        } else {
            //市价
            if (t === 'buy') {
                _mTAmount.val(NumFix('price', mAcc.Mul(limit['buyMaxAmountMp'], tradeRatio))).change().trigger('__Focus');
            } else {
                _mAmount.val(NumFix('amount', mAcc.Mul(limit['sellMaxAmountMp'], tradeRatio))).change().trigger('__Focus');
            }
        }
    }

    /**
     * 更新滑块比例
     * @param range  滑块
     * @param amount 总额
     * @param price  单价
     */
    function UpRange(range,amount,price){
        var _v = (price&&amount) ? price / amount*100 : 0;
        range.Update(_v)
    }

    /**
     *更新面板最大交易量
     * @param type
     * @param maxBox
     * @param amount
     * @param mAmount
     */
    function MaxWrite(type,maxBox,amount,mAmount){
        //console.log('更新面板最大交易量')
        if (type === 'buy') {
            maxBox  && maxBox.html(mNum(limit['buyMaxAmount'], 4));
            amount  && amount.attr('data-max', mNum(limit['buyMaxAmount'],4));
            mAmount && mAmount.attr('data-max',mNum(limit['buyMaxAmountMp'],4));
        } else {
            maxBox  && maxBox.html(mNum(limit['sellMaxAmount'], 4));
            amount  && amount.attr('data-max', mNum(limit['sellMaxAmount'],4));
            mAmount && mAmount.attr('data-max', mNum(limit['sellMaxAmountMp'],4));
        }
    }

    //面板重置
    function PanelReset(form, type) {
        var _form     = GetForm(form),
            _dPrice   = _form["price"],
            _dAmount  = _form["amount"],
            _dTAmount = _form.find('.transaction_amount'),
            _mTAmount = _form["market_transaction_amount"],
            _mAmount  = _form["market_amount"],
            _dLAmount = _form.find('.loan_amount'),
            _dTip     = _form.find('.trade_msg'),
            _dPwd     = _form["trade_pwd"],
            _loan     = _form["loan_type"],
            _formType = _form.attr('data-form-type'),
            _formBtn  = _form.find(':submit');
        if (type === 'all') {
            _dPrice.val('');
        }
        _dAmount&&_dAmount.val('').change();
        _dTAmount&&_dTAmount.html('0.00');
        _dLAmount&&_dLAmount.html('0.00');
        _mTAmount&&_mTAmount.val('').change();
        _mAmount&&_mAmount.val('').change();
        _dTip&&_dTip.html('');
        _dPwd&&_dPwd.val('').removeClass('input_text_red');
        _loan&&_loan.prop('checked',false).attr('data-loan-state',0).trigger('__Update');


        //计划委托面板
        _form['trigger_price'] && _form['trigger_price'].val('');
        _form['order_price']&& _form['order_price'].val('');
        _form['order_amount'] && _form['order_amount'].val('');

        //重置滑块
        if(pageInfo !== 'entrust' && _formType!='edit'){
            buyRange.Reset();
            sellRange.Reset();
        }
        if(_formType == 'edit'){
            editBuyRange.Reset();
            editSellRange.Reset();
        }
    }

    //委托单条取消
    DOM['#order_list'].on('click', '.cancel', function () {
        var _t = $(this),
            _ids = _t.attr('data-ids'),
            _tr = _t.parents('tr'),
            _a = $('.check_all'),
            _init= _t.attr('data-init')*1;
        if(_init){
            return false
        }
        _t.attr('data-init',1);
        mO.CancelOrder({"coin_type": coinType, id: _ids}, function (data) {
            if (data['code'] == 'loading') {
                _tr.addClass('cancel');
                GetOrder && GetOrder.Stop();
            } else if(data['code'] == 'error'){
                _tr.hasClass('cancel') && _tr.removeClass('cancel');
                _t.attr('data-init',0);
                mI({"msg": window.Texts && Texts['NetworkError'], "type":"error", "delay":1800});
                GetOrder && GetOrder.Play();
            }else {
                if (data['code'] === 0) {
                    _tr.next('.hidden_info').remove();
                    _tr.remove();
                    _a.prop('checked', false);
                    pageInfo == 'entrust' && mWin.reload(1);
                    DOM.trigger('__OrderCancel')
                } else {
                    mI({"msg": data['msg'],"type":"error"});
                    _t.attr('data-init',0);
                    _tr.hasClass('cancel') && _tr.removeClass('cancel');
                }
                GetOrder && GetOrder.Play();
            }
        });
        return false
    });

    //委托批量选取
    var orderCancel = [],
        orderCheck = $('.check_info'),
        dOrderCancel = $('#orderCancel');
    mA({box: '.order_wrap'}, function (data) {
        var _data = data || [];
        orderCheck.html(_data.length);
        orderCancel = _data;
    });

    //批量取消
    dOrderCancel.click(function () {
        var _t = $(this),
            _a = $('.check_all'),
            _n = _t.attr('data-msg-null'),
            _trs = DOM['#order_list'].find('tr'),
            _tr,
            _init= _t.attr('data-init')*1;

        if(_init){
            return false
        }
        if (orderCancel.length <= 0) {
            mI({"msg": _n, "type": 'error'})
        } else {
            mO.CancelOrder({"coin_type": coinType, id: orderCancel}, function (data) {
                if (data.code == 'loading') {
                    _t.attr('data-init',1);
                    $.each(orderCancel, function (i, c) {
                        _tr = $('[value="' + c + '"]').parents('tr');
                        _tr.addClass('cancel');
                    });
                }else if(data['code'] == 'error'){
                    _t.attr('data-init',0);
                    _trs.hasClass('cancel') && _trs.removeClass('cancel');
                    mI({"msg": window.Texts && Texts['NetworkError'], "type":"error", "delay":1800});
                } else {
                    _t.attr('data-init',0);
                    if (data.code == 0) {
                        _a.prop('checked', false);
                        _tr = $('tr.cancel');
                        _tr.next('.hidden_info').remove();
                        _tr.remove();
                        orderCancel = [];
                        pageInfo == 'entrust' && mWin.reload(1);
                        DOM.trigger('__OrderCancel');
                    } else {
                        mI({"msg": data.msg,"type":"error"});
                        _trs.hasClass('cancel') && _trs.removeClass('cancel');
                    }
                }
            })
        }
        return false
    });

    //委托折叠
    function ModFold(){
        var _List = [];
        $.each(this,function(i,c){
        var _this = $(this);
            _List.push(_this);

        var _bd = _this.find('.mod_bd'),
            _trigger = _this.find('.trigger_fold'),
            _text = _trigger.attr('data-text') ? _trigger.attr('data-text').split(','):'',
            _state= _trigger.attr('data-state');

        _this.Close = function(){
            _bd.hide();
            _trigger.attr('data-state','close').html(_text[1]).removeClass('open');
            _state= 'close';
            mC.Cookie('mod_fold'+i,0, {path:'/', expires:36500, domain:COOKIE_DOMAIN});
        };
        _this.Open  = function(){
            _bd.show();
            _trigger.attr('data-state','open').html(_text[0]).addClass('open');
            _state= 'open';
            mC.Cookie('mod_fold'+i,1, {path:'/', expires:36500, domain:COOKIE_DOMAIN});
        };
        _this.Toggle=function(){
            if(_state === 'open'){
                _this.Close()
            }else{
                _this.Open()
            }
        };
            _trigger.on('click',function(){
                _this.Toggle();
            })

        });
    return _List;
    }

  var _FoldList = J(ModFold , $('.mod_fold'));


    //委托详情
    mT({table: '.multi_table'}, function (wrap, trigger, type) {
        var ids = trigger.attr('data-ids'),
            types = trigger.attr('data-type');
        if (type === 'show') {
            //获取委托详情
            mO.GetDetails({coin_type: coinType, id: ids, type: types, temp: 'details_tmp', wrap: wrap[0]})
        }
    });

    //委托修改弹出层
    var tradeEdit = new mD({dialog: '.edit_dialog'});

    //委托修改
    DOM['#order_list'].on('click', '.edit', function () {
        var _t = $(this),
            _tr = _t.parents('tr'),
            _ids = _t.attr('data-ids'),
            _price = $.trim(_tr.find('.order_price').html())*1,
            _amount = $.trim(_tr.find('.order_amount').html())*1,
            _unsettled = $.trim(_tr.find('.order_unsettled').html()) * 1,
            _type = _t.attr('data-type'),
            _unPrice = mAcc.Mul(_price, _unsettled);

        tradeEdit.Show(
            function (_dialog) {
                var _form,_dId,_dPrice,_dAmount,_dUPrice,_dUAmount,_available,_dTa,
                    _dTip  = _dialog.find('.trade_msg'),
                    _errIn = _dialog.find('.input_text_red'),
                    _forms = _dialog.find('form');

                if(_type === 'buy'){
                    _form =  GetForm(_dialog.find('.form_buy_edit'));
                }else{
                    _form =  GetForm(_dialog.find('.form_sell_edit'))
                }
                _dId        = _form["id"];
                _dPrice     = _form["price"];
                _dAmount    = _form["amount"];
                _dUPrice    = _form.find(".un_price");
                _dUAmount   = _form.find(".un_amount");
                _available  = _form.find('.available').attr('data-flaunt') * 1;
                _dTa        = _form.find('.transaction_amount');
                _form["_DTA"] = _dTa;
                _forms.hide();
                _form.show();

                _dId.val(_ids);
                _dPrice.val(mNum(_price,3));
                _dAmount.val(mNum(_unsettled,4));
                _dUPrice && _dUPrice.html(mAcc.Add(_unPrice, _available)).attr('data-unsettled',_unPrice);
                _dUAmount && _dUAmount.html(mAcc.Add(_unsettled, _available)).attr('data-unsettled',_unsettled);
                _dTip.html('');
                _errIn.removeClass('input_text_red');

                //交易额
                DOM.trigger('__FormChange',[_form,_price,_amount, 'edit'+_type]);
                //最大交易量
                DOM.trigger('__FormMax',[_form,{
                    "trade_type":_type,
                    "available":_available,
                    "price"    :_price,
                    "un_price":_type ==='buy' && mAcc.Add(_unPrice, _available),
                    "un_amount":_type ==='sell' && mAcc.Add(_unsettled, _available)
                },'edit'+_type]);

                //重置滑块
                editBuyRange.Reset();
                editSellRange.Reset();
            }
        );
        return false
    });


    /**
     *计划委托列表
     */
    //require('src/data/order_plan');
    var _plan_template = 'plan_order_list',
        _plan_now = $('.plan_panel').length,
        _hash,
        PlanList = _plan_now && new mX({"url":'/trade/get_trade_plan_list',"datatype":'json', "refresh":3000, "data":{"symbol":symbol[1]+symbol[0],"status":0}},function(data){
        var _code = data.code;
        switch (_code){
            case 'loading':
                break;
            case 'error':
                DataView(_plan_template, {"network":false});
                break;
            case 'success':
                data['data']['network'] = true;
                if(!data['data']['data']||!data['data']['data']['list'] || data['data']['data']['list'].length<1){
                    PlanList.Stop();
                    console.log('PlanList Stop')
                }else{
                    var _h = mM.md5(JSON.stringify(data['data']['data']['list']));
                    if(_h !== _hash){
                        GetOrder && GetOrder.Fire();
                        _hash = _h;
                    }
                }
                DataView(_plan_template, data.data);
                break;
        }
    }),
    /*计划委托撤销*/
    PlanCancel = function(option,callback){
        var _op = option,
            _id = _op.id;
        mX({"url":'/trade/trade_plan_cancel',"data":{"id":_id}},function(data){
            var _code = data.code;
            switch (_code){
                case 'loading':
                    callback && callback(data);
                    break;
                case 'error':
                    callback && callback(data);
                    break;
                case 'success':
                    callback && callback(data['data']);
                    break;
            }
        });
    },
    _plan = $('#plan_dialog'),
    _planDialog = _plan_now && new mD({"dialog":_plan},function(o,t){
        if(t==='close'){
            //重置面板
            PanelReset(o.find('form'));
        }
    });
    //计划委托
    !(function(){
        if(!_plan_now) return;
        var _form = GetForm(_plan.find('.form_trade')),
            _price = _form['order_price']['Value'],
            _amount = _form['order_amount']['Value'],
            _tradeType = _form['trade_type']['Value']*1,
            _trigger  = _form['trigger_price']['Value']*1,
            _btn      = _form.find(':submit'),
            _btnT     = _btn.data('msg-submit').split(','),
            _planList = $('#'+_plan_template);
            _form['_DTA'] =  _form.find('.transaction_amount');

        J(InFormat, _form.order_price);
        J(InFormat, _form.order_amount);
        J(InFormat, _form.trigger_price);

        function Limit(obj){
            var _limit,_l,_t,_r;
            _trigger = _form['trigger_price'].val()*1;
            _tradeType = _form['trade_type'].val()*1;
            _amount  = _form['order_amount'].val()*1;
            _t = obj.attr('data-msg').split(',');

            if(_amount){
                if(_amount < 0.1){
                    _l = _trigger * 0.01;
                    _r = ['101%','99%'];
                }else{
                    _l =_trigger * 0.1;
                    _r = ['110%','90%'];
                }
            }else{
                _l = _trigger * 0.1;
                _r = ['110%','90%'];
            }

            if(_tradeType === 1){
                _limit = mNum(_trigger + _l,3);
                obj.attr({'data-max':_limit,'data-min':0.01,'data-msg-error':_t[0]+','+_t[1]+_t[3]+','+_t[2]+_t[4].replace('**',_r[0])})
            }
            if(_tradeType === 2){
                _limit =  mNum(_trigger - _l);
                obj.attr({'data-min':_limit,'data-max':100000,'data-msg-error':_t[0]+','+_t[1]+_t[4].replace('**',_r[1])+','+_t[2]+_t[3]})
            }
            return _limit;
        }

        $('#plan_panel').click(function(){
            _planDialog.Show()
        });

        _form['trigger_price'].on('keyup',function(){
           _form.trigger_price.FormatVal('price');
        }).on('change',function(){
            Limit(_form['order_price'])
        }).blur(function(){
            _form['trigger_price'].val() == 0 && _form['trigger_price'].val('');
        });

        _form['order_price'].on('keyup',function(){
            _price = _form.order_price.FormatVal('price');
            DOM.trigger('__FormChange',[_form,_price,_amount]);
        }).on('change',function(){
            Limit(_form['order_price'])
        }).blur(function(){
            _form['order_price'].val() == 0 && _form['order_price'].val('');
        });

        _form['order_amount'].on('keyup',function(){
            _amount = _form.order_amount.FormatVal('amount');
            DOM.trigger('__FormChange',[_form,_price,_amount]);
        }).on('change',function(){
            Limit(_form['order_price'])
        }).blur(function(){
            _form['order_amount'].val() == 0 && _form['order_amount'].val('');
        });

        _form['trade_type'].on('change',function(){

            _tradeType =  $(this).val();
            TypeChange();
            Limit(_form['order_price'])
        });
        TypeChange();
        function TypeChange(){
            if(_tradeType == 1){
                _btn.addClass('btn_buy').removeClass('btn_sell').text(_btnT[0])
            }else if(_tradeType == 2){
                _btn.addClass('btn_sell').removeClass('btn_buy').text(_btnT[1])
            }
        }

        //计划委托取消
        _planList.on('click','.cancel',function(){
            var _this = $(this),
                _id = $(this).attr('data-ids'),
                _tr = _this.parents('tr');
            PlanCancel({"id":_id},function(data){
                if (data['code'] == 'loading') {
                    _tr.addClass('cancel');
                } else if(data['code'] == 'error'){
                    _tr.hasClass('cancel') && _tr.removeClass('cancel');
                    mI({"msg": window.Texts && Texts['NetworkError'], "type":"error", "delay":1800});

                }else {
                    if (data['code'] === 0) {
                        _tr.remove();
                        PlanList.Fire()
                    } else {
                        mI({msg: data['msg'], "type":"error"});
                        _tr.hasClass('cancel') && _tr.removeClass('cancel');
                    }
                }
            });

            return false;
        })

    })();


    /****事件处理****/
    //面板监听
    HandlingEvent(DOM['.form_trade'], coinType);
    //DocHidden
    DOM.on('DoDocHidden',function(){
        HUOBI['GLOBAL']['docHidden'] ? GetOrder&&GetOrder.Stop() : GetOrder&&GetOrder.Play();
    })
    //面板可用额度
    .on('__GetBalance',function(e){
        var _c_c_a = mNum(HUOBI['BALANCE']['cny_available'],2).split('.'),
            _c_b_a = mNum(HUOBI['BALANCE']['cny_btc_available'],3).split('.'),
            _c_l_a = mNum(HUOBI['BALANCE']['cny_ltc_available'],4).split('.'),
            _u_c_a = mNum(HUOBI['BALANCE']['usd_available'],2).split('.'),
            _u_b_a = mNum(HUOBI['BALANCE']['usd_btc_available'],4).split('.');
        //CNY
        DOM['#cny_cny_a_panel'].attr('data-flaunt',HUOBI['BALANCE']['cny_available']).html('<b>'+_c_c_a[0]+'</b>.'+_c_c_a[1]);
        DOM['#cny_btc_a_panel'].attr('data-flaunt',HUOBI['BALANCE']['cny_btc_available']).html('<b>'+_c_b_a[0]+'</b>.'+_c_b_a[1]);
        DOM['#cny_ltc_a_panel'].attr('data-flaunt',HUOBI['BALANCE']['cny_ltc_available']).html('<b>'+_c_l_a[0]+'</b>.'+_c_l_a[1]);
        //USD
        DOM['#usd_usd_a_panel'].attr('data-flaunt',HUOBI['BALANCE']['usd_available']).html('<b>'+_u_c_a[0]+'</b>.'+_u_c_a[1]);
        DOM['#usd_btc_a_panel'].attr('data-flaunt',HUOBI['BALANCE']['usd_btc_available']).html('<b>'+_u_b_a[0]+'</b>.'+_u_b_a[1]);
    })

    //面板可用借贷
    .on('__LoanUpdate',function(e){
        DOM['#cny_cny_l_panel'].html(mNum(loanLimit["cny_loan_enable"],2)).attr({'data-enable':loanLimit["cny_loan_enable"],'data-total':loanLimit["cny_loan_total"]});
        DOM['#cny_btc_l_panel'].html(mNum(loanLimit["cny_btc_loan_enable"],4)).attr({'data-enable':loanLimit["cny_btc_loan_enable"],'data-total':loanLimit["cny_btc_loan_total"]});
        DOM['#cny_ltc_l_panel'].html(mNum(loanLimit["cny_ltc_loan_enable"],4)).attr({'data-enable':loanLimit["cny_ltc_loan_enable"],'data-total':loanLimit["cny_ltc_loan_total"]});
        DOM['#usd_usd_l_panel'].html(mNum(loanLimit["usd_loan_enable"],2)).attr({'data-enable':loanLimit["usd_loan_enable"],'data-total':loanLimit["usd_loan_total"]});
        DOM['#usd_btc_l_panel'].html(mNum(loanLimit["usd_btc_loan_enable"],4)).attr({'data-enable':loanLimit["usd_btc_loan_enable"],'data-total':loanLimit["usd_btc_loan_total"]});

        //有可用借贷开启开关
        $.each(loanLimit,function(i,c){
            if(i.indexOf('_loan_enable')>0){
                var _t = i.replace('_loan_enable',""),
                    _s = $('#'+_t +'_loan_switch');
                if(c){
                    _s.prop('disabled',false).trigger('__Update')
                }else{
                    _s.prop('disabled', true).trigger('__Update')
                }
            }
        });
    })
    //交易类型切换
    .on('__OrderType',function(e,i){
        PanelType(orderType,i);
        PanelReset(DOM['.form_trade'].eq(0));
        PanelReset(DOM['.form_trade'].eq(1));
    })
    //交易表单变化
   .on('__FormChange',function(e,f,p,a,t){
        /*成交额*/
        var _TA = WriteTA(p, a, f['_DTA']),
            _l  = f.find('[name="loan_type"]');

        /*借贷额*/
        LoanAmount([_TA,a],f['_VAV'],f['_type'],f['_DLA'],f['_VLT']);

        /*重置借贷状态*/
        _l.attr('data-loan-state',0);
    })
    //最大交易量
    .on('__FormMax',function(e,f,v,t){
        //console.log('__FormMax','最大交易量',f,t);
        /*最大交易量*/
        LargestVolume(v);
        MaxWrite(f['_type'],f['_DMaxB'],f['amount'],f["market_transaction_amount"]||f['market_amount']);
    });

    //表单验证前
    DOM.on('__TradeFormValidation',function(e,f){
        f.trigger('__FormActive');
    })
    //表单验证通过提交前
   .on('__TradeFormBeforeSubmit',function(e,f){
        var _type = f.attr('data-form-type'),
            _btn =  f.find(':submit'),
            _btnT = _btn.data('msg-submit').split(','),
            _btnI = _btn.text(),
            _trade= f.find('[name="trade_type"]').val();

       //计划委托
       if(_type === 'plan'){
           _trade ? _trade == 1 ?  _btn.text(_btnT[0]+_btnT[2]) :  _btn.text(_btnT[1]+_btnT[2]):'';
       }else {
           _btn.text(_btnT[1] + _btnT[_btnT.length - 1]);
       }
        //资金密码
        if (show_trade_pwd(f)) {
            return false;
        }
    })
    //表单提交后回调
   .on('__TradeFormCallback',function(e,f,d){
        var _type = f.attr('data-form-type'),
            _btn  = f.find(':submit'),
            _btnT = _btn.data('msg-submit').split(','),
            _btnI = _btn.text(),
            _trade= f.find('[name="trade_type"]').val();
       if (d.code == 0) {
            // 资金密码
            if (!check_use_trade_pwd()) {
                $('.trade_pwd_group').hide();
            }

            //关闭弹出层
            if (_type === 'edit') {
                tradeEdit.Close();
                pageInfo == 'entrust' && mWin.reload(1);
            }

            //计划委托
            if(_type === 'plan'){
                PlanList.Fire();
                PlanList.Play();
                _planDialog.Close();
                _FoldList[0].Close();
                _FoldList[1].Close();

                _trade  ? _trade == 1 ?  _btn.text(_btnT[0]) :  _btn.text(_btnT[1]):'';
            }else{
                _btn.text(_btnT[0]);
                GetHistory && (orderType === 'PlaceMarketOrder' ? setTimeout(function(){GetHistory.Fire()},1000) :setTimeout(function(){GetHistory.Fire()},500));
            }

           //更新委托
           GetOrder && GetOrder.Play();
           //重置面板
           PanelReset(f);

           //消息条
           mI({msg: d.msg, delay: 3000});
        } else {
           //计划委托
           if(_type === 'plan') {
               _trade  ? _trade == 1 ?  _btn.text(_btnT[0]) :  _btn.text(_btnT[1]):'';
           }else{
               _btn.text(_btnT[0]);
           }
           mI({msg: d.msg, type: 'error'});
        }

        //更新balance
        HBBalance.Fire();
    })
    .on('__TradeFormError',function(e,f){
        var _type = f.attr('data-form-type'),
            _btn  = f.find(':submit'),
            _btnT = _btn.data('msg-submit').split(',');
        mI({msg:  window.Texts && Texts['NetworkError'] , type: 'error'});
        _btn.text(_btnT[0]);
    })
    //杠杆借贷
    .on('__GetLoan',function(e,f,t,a){
        var _form = f,
            _loan = _form['loan_type'],
            _loanType = _loan.data('loan-type'),
            _data,
            _btn =  f.find(':submit'),
            _btnT = _btn.data('msg-submit').split(',');

           // console.log('类型'+_loanType,'数量'+a);

            /*状态值 0初始 1正在 2成功 */
        LoanNow({"loan_type":_loanType,"amount":a},function(data){
           var _code = data.code;
            switch (_code){
                case 'loading':
                    _loan.attr('data-loan-state',1);
                    _btn.text(_btnT[2]+_btnT[3]);
                    break;
                case 'error':
                    _loan.attr('data-loan-state',0);
                    break;
                case 'success':
                    _data = data['data'];
                    if(_data.code === 0){
                        _loan.attr('data-loan-state',2);

                        //更新balance
                        HBBalance.Fire();

                        //更新杠杆信息
                        DOM.trigger('__UpLoan',[coinType,'loan success']);

                        //提交委托
                        f.submit();
                        break;
                    }else{
                        mI({msg: _data.msg, type: 'error'});
                        _loan.attr('data-loan-state',0);
                        _btn.text(_btnT[0]);
                    }

            }

        })
    })
    //更新杠杆信息
    .on('__UpLoan',function(e,t,y){
            switch(t){
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
    })
    //委托取消
    .on('__OrderCancel',function(e){
        //重置面板
        PanelReset(DOM['.form_trade'].eq(0));
        PanelReset(DOM['.form_trade'].eq(1));
        //更新杠杆信息
        setTimeout(function(){
            DOM.trigger('__UpLoan',[coinType ,'Order Cancel']);
        },500);
    });


    /**
     * 小数向上进位
     * @param num   数字
     * @param type  curr货币/coin虚拟币
     * @returns number
     * @constructor
     */
    function Carry(num,type){
        var _num ;
        if(type=='currency'){
            //_num = Math.ceil(num*100)/100;
            _num = Math.floor(num*100+0.9)/100
        }
        if(type=='coin'){
            //_num = Math.ceil(num*10000)/10000;
            _num = Math.floor(num*10000+0.9)/10000
        }
        return _num
    }

});

