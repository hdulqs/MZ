/**
 * 行情
 */
define(function(require, exports){
    var mNum = require('./module_number'),    //数字处理
        TmpL = require('./module_tmpl'),
        connect = require('./module_connect'),
        SOCKET_API = require('./module_socket_api'),
        I = require('./module_socket_io'),
        HuobiAjax = require('./module_ajax');
        //SetTitle = require('./module_set_title'); //title加价格
    var old_title = $(document).attr("title");
    var depth_num = 0;
    var coin_type   = $("input[name='coin_type']").val(),
        money_symbol;
    //console.log(coin_type);
    switch(coin_type)
    {
        case 'cny_btc':
            var coin = "btc", money = "cny" , market_type = "staticmarket";
            money_symbol = "￥";
            break;
        case 'cny_ltc':
            var coin = "ltc", money = "cny" , market_type = "staticmarket";
            money_symbol = "￥";
            break;
        case 'usd_btc':
            var coin = "btc", money = "usd" , market_type = "usdmarket";
            money_symbol = "$";
            break;
        case 'usd_ltc':
            var coin = "ltc", money = "usd" , market_type = "usdmarket";
            money_symbol = "$";
            break;
        default:
            var coin = "btc", money = "cny" , market_type = "staticmarket";
            money_symbol = "￥";

    }

    if(window.RUN_MODE == 140 && (coin_type == 'usd_btc' || coin_type == 'usd_ltc')){
        $_ws_hq_url = $_ws_hq_url_usd;
    }
    var settime = null, rushtime = 2000;
    var SYMBOL = coin + money,
        socket_api = SOCKET_API(),
        socket = connect($_ws_hq_url),
        is_init = 0,
        STEP_MARKET = "";
    var tmpTag = 'https:' == document.location.protocol ? "https" : "http";
    var ajax_url = tmpTag+"://"+AJAX_API_DOMAIN+"/"+market_type+"/detail_"+coin+".js";
    var AJAX_MARKET;
    AJAX_MARKET = new HuobiAjax({"data": {},"url":ajax_url,"refresh":1000,"datatype":"jsonp","jsonp":"callback","jsonpcallback":"view_detail_"+coin},function(data){
        var _code = data.code;
        if (data.code == 'success') {
            var result = data['data'];
            var format_ajax_data = formatMarket_ajax_price(result);
            pushnewPrice(format_ajax_data["data_price"]);
            pushData(format_ajax_data['buy'], format_ajax_data['sel']);
        }
    });
    socket.conn.on('message',function(){
        AJAX_MARKET.Stop();
    });
    socket.conn.on('disconnect',function(){
        AJAX_MARKET.Play();
    });
    function formatMarket_ajax_price(data) {
        var format_data = {},
            format_price = {};
        format_price["priceNew"] = data.p_new;
        format_price["priceOpen"] = data.p_open;
        format_price["priceHigh"] = data.p_high;
        format_price["priceLow"] = data.p_low;
        format_price["priceLast"] = data.p_last;
        format_price["level"] = data.level;
        format_price["totalAmount"] = data.amount;
        format_data["data_price"] = format_price;

        var buy = [],
            sel = [],
            tra = [],
            sel_cun = 0,
            buy_cun = 0,
            i = 0,
            j = 0,
            buy_len = data.buys.length;
        sell_len = data.buys.length;
        for (; i < buy_len; i++) {
            buy_cun += (data.buys[i].amount * 10000000000);
            data.buys[i] && buy.push([mNum(data.buys[i].price, 3), mNum(data.buys[i].amount,4), mNum(buy_cun/10000000000, 4)]);
        }
        for (; j < sell_len; j++) {
            sel_cun += (data.sells[j].amount * 10000000000);
            data.sells[j] && sel.push([mNum(data.sells[j].price, 3), mNum(data.sells[j].amount,4), mNum(sel_cun/10000000000, 4)]);
        }
        for (var i = 0; i < 10; i++) {
            var direction;
            if(data.trades[i].en_type == "bid"){
                direction = 1;
            }else{
                direction = 4;
            }
            data.trades[i].price && tra.push([direction, data.trades[i].time, mNum(data.trades[i].amount, 4), mNum(data.trades[i].price, 3)]);
        }
        //console.log(tra);
        format_data["buy"] = buy;
        format_data["sel"] = sel;
        format_data["tra"] = tra;
        return format_data;
    }

    var _depthSlide = $('#change_depth'),
        _depthCode  = _depthSlide.find('dt b'),
        _depthDD    = _depthSlide.find('dd p');

    _depthSlide.hover(function(){
        _depthSlide.addClass('hover');
    },function(){
        _depthSlide.removeClass('hover');
    });
    _depthDD.on('click',function(){
        var _t = $(this),
            val = _t.text()*1;
        _depthCode.text(val);

        var un_depth = socket_api['marketDetail'+depth_num](SYMBOL);
        socket.msg(un_depth['unmsg']);
        socket.clearmsg();
        depth_num = val;
        market('market',depth_num);
        _depthSlide.removeClass('hover');
    });

    var marketid = document.getElementById('market'),
        con = marketid.getElementsByTagName('div'),
        marketdom = document.getElementById('market_depth'),
        tmpdom = marketid.getElementsByTagName('script'),
        marketTmp = tmpdom[0].innerHTML,
        PUBLIC_DATA = PUBLIC_DATA ? PUBLIC_DATA : {
            acc: {},
            buy5: [],
            sell5: [],
            autoRelease: 1,
            order: [],
            orderList: 0,
            enable_coin: 0,
            limit: [0, 0],
            newprice: 0,
            font_color: 'font_up',
            holdStatus: 0
        };
    function market(id,depth_num) {

        //console.log(coin);
        var

        depth_ = socket_api['marketDetail'+depth_num](SYMBOL);
        //console.log(depth_num);
        socket.msg(depth_.msg);
        socket.reg(depth_.msgType, function (data) {
            var mark;


            if(is_init < 2){
                is_init = is_init+1;
            }
            if (data.payload.symbolId != SYMBOL) return;
            //console.log(data);
            var data = formatMarket_new(data.payload);
            //console.log(data);
            pushData(data['buy'], data['sel']);
            //pushnewPrice(data.payload);
        });

    }
    function formatMarketWithStep(data, p) {
        if (!data.length)return [];
        var tmp = {},
            tmpData = [],
            d = [],
            drst = [],
            n = 1,
            total = 0,
            len = data.length,
            _index = !!p ? ~~data[0][0] : Math.ceil(data[0][0]),
            cmp = !!p ? STEP_MARKET * -1 : STEP_MARKET * 1;

        function setData() {
            var base = tmpData.pop(),
                rindex = !!p ? ~~base[0] : Math.ceil(base[0]),
                len = tmpData.length;
            tmp[rindex] = {
                'amt': base[1],
                'price': rindex
            }
            while (len--) {
                tmp[rindex].amt += tmpData[len][1] * 1;
            }

        }

        for (var i = 0; i < len; i++) {
            _this = data[i][0];

            if (!!p && (data[i][0] - _index < cmp + 1)) {
                setData();
                tmpData = [];
                _index = ~~data[i][0]
            }
            if (!p && (data[i][0] - _index > cmp - 1)) {
                setData();
                tmpData = [];
                _index = Math.ceil(data[i][0])
            }
            tmpData.push(data[i]);
        }
        if (tmpData.length) {
            setData();
        }
        for (var k in tmp) {
            d.push([tmp[k].price, tmp[k].amt]);
        }
        d = d.sort(function (a, b) {
            return !p ? a[0] - b[0] : b[0] - a[0];
        });
        for (var i = 0, len = d.length; i < len; i++) {
            total += d[i][1];
            d[i][2] = total;
        }
        return d;
    }

    function setNewBuySel(b, s) {
        marketdata = {buy: b, sel: s};
    }

    function pushData(buy, sel) {
        var buy1, sel1,
            b = buy,
            s = sel;
        //s = [];
        setNewBuySel(b, s);
        b = (!STEP_MARKET ? b : formatMarketWithStep(b, 1));
        s = (!STEP_MARKET ? s : formatMarketWithStep(s, 0));
        buy1 = 1 * (b[0] ? b[0][0] : 0),
            sel1 = 1 * (s[0] ? s[0][0] : 0);
        //order.hold.pushNewPrice(buy1,sel1);
        PUBLIC_DATA.buy5 = b.slice(0, 5);
        PUBLIC_DATA.sell5 = s.slice(0, 5);
        if(PUBLIC_DATA.newprice == 0){
            PUBLIC_DATA.newprice = buy[0][0]
        }
        marketdom.innerHTML = TmpL(marketTmp, {buy: b.slice(0, 5), sel: s.slice(0, 5), price:PUBLIC_DATA.newprice, font_color:PUBLIC_DATA.font_color, money_symbol:money_symbol});

    }

    function formatMarket_new(data) {
        var buy = [],
            sel = [],
            sel_cun = 0,
            buy_cun = 0,
            i = 0,
            len = data.bids.amount.length;
        for (; i < len; i++) {
            buy_cun += (data.bids.amount[i] * 10000000000);
            data.bids.price[i] && buy.push([mNum(data.bids.price[i], 3), mNum(data.bids.amount[i],4), mNum(buy_cun/10000000000, 4)]);
        }
        i = 0;
        len = data.asks.amount.length;
        for (; i < len; i++) {
            sel_cun += (data.asks.amount[i] * 10000000000);
            data.asks.price[i] && sel.push([mNum(data.asks.price[i], 3), mNum(data.asks.amount[i],4), mNum(sel_cun/10000000000, 4)]);
        }
        pushnewPrice(data);
        return {
            buy: buy,
            sel: sel
        }
    }
    function pushnewPrice(data) {
        //console.log(PUBLIC_DATA.newprice);
        var priceNew = mNum(data.priceNew,3);
        var font_color = PUBLIC_DATA.font_color;
        if(priceNew > PUBLIC_DATA.newprice){
            font_color = "font_up";
        }else if(priceNew < PUBLIC_DATA.newprice){
            font_color = "font_down";
        }
        PUBLIC_DATA.newprice = priceNew;
        PUBLIC_DATA.font_color = font_color;
        var fang = "";
        if(money == 'cny'){
            fang = "￥";
        }else{
            fang = "$";
        }
        var newTitle;
        var title_price = fang +  priceNew;
        newTitle = title_price + " | " + old_title;
        $(document).attr("title", newTitle);

    }
    //console.log(mNum(1.111,2));
    market("market",depth_num);
});
