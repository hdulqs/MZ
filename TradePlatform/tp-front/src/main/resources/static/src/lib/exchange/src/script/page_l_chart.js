/**
 * Created by andyk on 2015/11/12.
 * 闪电手图表
 */
define(function (require, exports, module) {
    var IO = require('./module_socket_io'),
        CONN = require('./module_connect'),
        API = require('./module_socket_api'),
        key_map = {
            symbolId: "symbolId",
            bids: "bids",
            asks: "asks",
            trades: "trades",
            priceNew: "priceNew",
            level: "level",
            volume: "volume",
            amount: "amount",
            totalAmount: "totalAmount",
            totalVolume: "totalVolume",
            amp: "amp",
            priceOpen: "priceOpen",
            priceHigh: "priceHigh",
            priceLow: "priceLow",
            priceLast: "priceLast",
            price: "price",
            priceBid: "priceBid",
            priceAsk: "priceAsk",
            topBids: "topBids",
            topAsks: "topAsks",
            tradeId: "tradeId",
            time: "time",
            timeMin: "timeMin",
            timeMax: "timeMax",
            bidId: "bidId",
            askId: "askId",
            direction: "direction",
            version: "version",
            versionOld: "versionOld",
            bidName: "bidName",
            bidPrice: "bidPrice",
            bidAmount: "bidAmount",
            bidTotal: "bidTotal",
            askName: "askName",
            askPrice: "askPrice",
            askAmount: "askAmount",
            askTotal: "askTotal",
            period: "period",
            date: "date",
            count: "count",
            symbolName: "symbolName",
            exchangeId: "exchangeId",
            exchangeName: "exchangeName",
            currencyId: "currencyId",
            currencyName: "currencyName",
            cryptoId: "cryptoId",
            cryptoName: "cryptoName",
            cryptoNameZh: "cryptoNameZh",
            total: "total",
            suply: "suply",
            introduction: "introduction",
            msgType: "msgType",
            uniqueId: "_id",
            idCur: "idCur",
            idPrev: "idPrev",
            payload: "payload",
            from: "from",
            to: "to",
            requestIndex: "requestIndex",
            pushType: "pushType",
            symbolList: "symbolList",
            symbolIdList: "symbolIdList",
            retCode: "retCode",
            retMsg: "retMsg",
            bidInsert: "bidInsert",
            bidDelete: "bidDelete",
            bidUpdate: "bidUpdate",
            askInsert: "askInsert",
            askDelete: "askDelete",
            askUpdate: "askUpdate",
            commissionRatio: "commissionRatio",
            poor: "poor",
            updownVolume: "updownVolume",
            updownRatio: "updownRatio",
            priceAverage: "priceAverage",
            volumeRatio: "volumeRatio",
            amountRatio: "amountRatio",
            turnVolume: "turnVolume",
            turnoverRate: "turnoverRate",
            outerDisc: "outerDisc",
            innerDisc: "innerDisc",
            percent: "percent",
            accuAmount: "accuAmount",
            index: "index",
            row: "row",
            insertList: "insertList",
            deleteList: "deleteList",
            updateList: "updateList",
            timeStart: "timeStart",
            timeEnd: "timeEnd",
            timeServer: "timeServer",
            isTemp: "isTemp",
            marketDetail: "marketDetail",
            tradeDetail: "tradeDetail",
            marketDepthTop: "marketDepthTop",
            marketDepthTopShort: "marketDepthTopShort",
            marketDepth: "marketDepth",
            reqTradeDetail: "reqTradeDetail",
            marketDepthTopDiff: "marketDepthTopDiff",
            marketDepthDiff: "marketDepthDiff",
            lastKLine: "lastKLine",
            lastTimeLine: "lastTimeLine",
            marketOverview: "marketOverview",
            marketStatic: "marketStatic",
            reqSymbolList: "reqSymbolList",
            reqSymbolDetail: "reqSymbolDetail",
            reqMsgSubscribe: "reqMsgSubscribe",
            reqMsgUnsubscribe: "reqMsgUnsubscribe",
            reqTimeLine: "reqTimeLine",
            reqKLine: "reqKLine",
            reqMarketDepthTop: "reqMarketDepthTop",
            reqMarketDepth: "reqMarketDepth",
            reqTradeDetailTop: "reqTradeDetailTop",
            reqMarketDetail: "reqMarketDetail",
            request: "request",
            message: "message",
            pushLong: "pushLong",
            pushShort: "pushShort",
            kline1Min: "1min",
            kline5Min: "5min",
            kline15Min: "15min",
            kline30Min: "30min",
            kline60Min: "60min",
            kline1Day: "1day",
            kline1Week: "1week",
            kline1Mon: "1mon",
            kline1Year: "1year",
            klineTimeline: "tl",
            percent10: "10",
            percent20: "20",
            percent50: "50",
            percent80: "80",
            percent100: "100",
            percentTop: "top",
            marketDepthTopShortLength: 20,
            stringSplit: "|",
            klineTradeOpen: 1,
            klineTradeLast: 2,
            klineTradeOne: 4,
            klineTradeNone: 8,
            klineTradeMid: 16,
            protocolVersion: 1,
            maxPackageCount: 500,
            tradePackageCount: 100,
            maxTradeId: -1,
            CODE_OK: 200,
            CODE_PARAM_ERROR: 601,
            CODE_SERVER_ERROR: 701,
            lineLimitSize: 300,
            tradeDetailLimitSize: 300,
            amountDotLength: 4,
            volumeDotLength: 2
        },
        Event = {
            add: document.addEventListener ?
                function (o, t, f) {
                    o.addEventListener(t, f, false)
                } : function (o, t, f) {
                o.attachEvent('on' + t, f)
            },
            remove: document.removeEventListener ?
                function (o, t, f) {
                    o.removeEventListener(t, f, false)
                } : function (o, t, f) {
                o.detachEvent('on' + t, f)
            },
            target: function (e) {
                return e.target ? e.target : window.event.srcElement;
            },
            delta: function (e) {
                var evt = e || window.event,
                    d = evt.wheelDelta / -120 || evt.detail / 3;
                return d;
            },
            stop: function (e) {
                if (e && e.stopPropagation) {
                    e.stopPropagation();
                    e.preventDefault();
                } else {
                    window.event.cancelBubble = true;
                    window.event.returnValue = false;
                }
            },
            mouse: function (e) { //get mouse position
                return {
                    x: e.pageX || (e.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft)),
                    y: e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop))
                }
            }
        };

    /*********/

    var _$ = ["\x72\x65", "\x72\x65", "\x76", "\x76", "\x73\x74\x72\x69\x6e\x67", "\x6c\x61\x73\x74\x4b\x4c\x69\x6e\x65", "\x43\x72\x6f\x73\x73\x4c\x69\x6e\x65", "\x47\x61\x6e\x6e\x46\x61\x6e", "\x6d\x6f\x75\x73\x65\x77\x68\x65\x65\x6c", "\x44\x4f\x4d\x4d\x6f\x75\x73\x65\x53\x63\x72\x6f\x6c\x6c", "\x72\x65\x73\x69\x7a\x65", "\x6d\x6f\x75\x73\x65\x64\x6f\x77\x6e", "\x6d\x6f\x75\x73\x65\x6f\x76\x65\x72", "\x6d\x6f\x75\x73\x65\x6d\x6f\x76\x65", "\x6d\x6f\x75\x73\x65\x75\x70", "\x6d\x6f\x75\x73\x65\x6f\x75\x74", "\x74\x6f\x75\x63\x68\x73\x74\x61\x72\x74", "\x74\x6f\x75\x63\x68\x6d\x6f\x76\x65", "\x74\x6f\x75\x63\x68\x65\x6e\x64", "\x6b\x65\x79\x64\x6f\x77\x6e", "\x6d\x69\x64\x64\x6c\x65", "\x31\x32\x70\x78\x20\x61\x72\x69\x61\x6c", "\x73\x6f\x6c\x69\x64", "\x6f\x62\x6a\x65\x63\x74", "\x31\x32\x70\x78\x20\x61\x72\x69\x61\x6c", "\u4ebf", "\u4e07", "\x31\x32\x70\x78\x20\x61\x72\x69\x61\x6c", "\x68\x3a\x69", "\x6d\u6708\x64\u65e5", "\x79\u5e74\x6d\u6708", "\x23", "\x72\x67\x62\x61\x28", "\x2c", "\x2c", "\x2c", "\x29", "\x31\x70\x78", "\x2d\x39\x39\x39\x70\x78", "\x31\x70\x78", "\x70\x78", "\x70\x78", "\x62\x6f\x74\x74\x6f\x6d", "\x31\x32\x70\x78\x20\x61\x72\x69\x61\x6c", "\x72\x69\x67\x68\x74", "\x6c\x65\x66\x74", "\x4d\x41", "\x2d", "\x6e\x6f\x4b\x6c\x69\x6e\x65", "\x4d\x41", "\x2d", "\x75\x62", "\x6c\x62", "\x42\x4f\x4c\x4c", "\x2d", "\x55\x42", "\x2d", "\x4c\x42", "\x2d", "\u4ebf", "\u4e07", "\x79\x2d\x6d\x2d\x64\x20\x68\x3a\x69\x3a\x73", "\x79\x2d\x6d\x2d\x64", "\x6d\x69\x64\x64\x6c\x65", "\x6d\x69\x64\x64\x6c\x65", "\x31\x32\x70\x78\x20\x61\x72\x69\x61\x6c", "\x64\x69\x66", "\x64\x65\x61", "\x4b", "\x44", "\x4a", "\x52\x53\x49\x31", "\x52\x53\x49\x32", "\x52\x53\x49\x33", "\x57\x52\x31", "\x57\x52\x32", "\x44\x49\x46", "\x2d", "\x44\x45\x41", "\x2d", "\x4d\x41\x43\x44", "\x2d", "\x4b", "\x2d", "\x44", "\x2d", "\x4a", "\x2d", "\x52\x53\x49\x31", "\x2d", "\x52\x53\x49\x32", "\x2d", "\x52\x53\x49\x33", "\x2d", "\x57\x52\x31", "\x2d", "\x57\x52\x32", "\x2d", "\x4d\x41", "\x2d", "\x25\x45\x39\x25\x38\x37\x25\x38\x46", "\x74\x65\x78\x74\x41\x6c\x69\x67\x6e", "\x74\x65\x78\x74\x42\x61\x73\x65\x6c\x69\x6e\x65", "\x66\x6f\x6e\x74", "\x66\x69\x6c\x6c\x53\x74\x79\x6c\x65", "\x73\x74\x72\x6f\x6b\x65\x53\x74\x79\x6c\x65", "\x6f\x62\x6a\x65\x63\x74", "\x6d\x69\x64\x64\x6c\x65", "\x64\x31", "\x64\x32", "\x64\x33", "\x64\x34", "\x79", "\x6d", "\x64", "\x68", "\x69", "\x73", "\x2d", "\x2d", "\x20", "\x3a", "\x3a", "\x30", "", "\x79\x2d\x6d\x2d\x64\x20\x68\x3a\x69\x3a\x73", "\x79\x2d\x6d\x2d\x64", "\x62\x61\x63\x6b\x67\x72\x6f\x75\x6e\x64\x3a", "\x3b\x64\x69\x73\x70\x6c\x61\x79\x3a\x69\x6e\x6c\x69\x6e\x65\x2d\x62\x6c\x6f\x63\x6b\x3b\x70\x61\x64\x64\x69\x6e\x67\x2d\x72\x69\x67\x68\x74\x3a\x35\x70\x78\x3b\x70\x61\x64\x64\x69\x6e\x67\x2d\x74\x6f\x70\x3a\x34\x70\x78\x3b\x63\x6f\x6c\x6f\x72\x3a", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e\u65e5\u671f\x3a', "\x3c\x2f\x73\x70\x61\x6e\x3e", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e\u5f00\x3a', "\x3c\x2f\x73\x70\x61\x6e\x3e", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e\u9ad8\x3a', "\x3c\x2f\x73\x70\x61\x6e\x3e", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e\u4f4e\x3a', "\x3c\x2f\x73\x70\x61\x6e\x3e", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e\u6536\x3a', "\x3c\x2f\x73\x70\x61\x6e\x3e", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e\u91cf\x3a', "\x3c\x2f\x73\x70\x61\x6e\x3e", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e', "\x3a", "\x3c\x2f\x73\x70\x61\x6e\x3e", "", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e', "\x3a", "\x3c\x2f\x73\x70\x61\x6e\x3e", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e', "\x28", "\x4d\x41\x43\x44", "\x2c", "\x3a", "\x2c", "\x29\x3c\x2f\x73\x70\x61\x6e\x3e", '\x3c\x73\x70\x61\x6e\x20\x73\x74\x79\x6c\x65\x3d\x22', '\x22\x3e', "\x3a", "\x3c\x2f\x73\x70\x61\x6e\x3e", "\x31\x6d\x69\x6e", "\x63\x61\x6e\x76\x61\x73", "\x32\x64", "\x3b\x70\x6f\x73\x69\x74\x69\x6f\x6e\x3a\x61\x62\x73\x6f\x6c\x75\x74\x65\x3b\x6c\x65\x66\x74\x3a\x30\x3b\x74\x6f\x70\x3a\x30\x3b", "\x44\x4c", "\x3b\x70\x6f\x73\x69\x74\x69\x6f\x6e\x3a\x61\x62\x73\x6f\x6c\x75\x74\x65\x3b\x6c\x65\x66\x74\x3a\x31\x30\x70\x78\x3b\x74\x6f\x70\x3a\x2d\x39\x39\x39\x70\x78\x3b\x77\x69\x64\x74\x68\x3a\x31\x30\x30\x25\x3b", "\x3c\x64\x74\x3e\x3c\x2f\x64\x74\x3e\x3c\x64\x64\x3e\x3c\x2f\x64\x64\x3e\x3c\x64\x64\x3e\x3c\x2f\x64\x64\x3e", "\x64\x74", "\x64\x64", "\x64\x64", "\x3b\x70\x6f\x73\x69\x74\x69\x6f\x6e\x3a\x61\x62\x73\x6f\x6c\x75\x74\x65\x3b\x6c\x65\x66\x74\x3a\x30\x3b\x74\x6f\x70\x3a\x30\x3b\x77\x69\x64\x74\x68\x3a\x31\x30\x30\x25\x3b\x66\x6f\x6e\x74\x2d\x73\x69\x7a\x65\x3a\x31\x32\x70\x78\x3b", "\x63\x61\x6e\x76\x61\x73", "\x63\x6f\x6e\x73\x74\x72\x75\x63\x74", "\x73\x74\x72\x69\x6e\x67", "\x4d\x41\x43\x44", "\x63\x68\x69\x6e\x65\x73\x65\x48\x61\x62\x69\x74", "\x4d\x41", "\x31\x6d\x69\x6e", "\x68\x74\x74\x70\x3a\x2f\x2f\x68\x71\x2e\x68\x75\x6f\x62\x69\x2e\x63\x6f\x6d", "\x68\x75\x6f\x62\x69\x2e\x63\x6f\x6d", "\x36\x30", "\x33\x30\x30", "\x39\x30\x30", "\x31\x38\x30\x30", "\x33\x36\x30\x30", "\x38\x36\x34\x30\x30", "\x36\x30\x34\x38\x30\x30", "\x32\x36\x32\x39\x37\x34\x33", "\x33\x31\x35\x35\x36\x39\x32\x36", "\x6b\x6c\x69\x6e\x65\x31\x4d\x69\x6e", "\x30\x30\x31", "\x6b\x6c\x69\x6e\x65\x35\x4d\x69\x6e", "\x30\x30\x35", "\x6b\x6c\x69\x6e\x65\x31\x35\x4d\x69\x6e", "\x30\x31\x35", "\x6b\x6c\x69\x6e\x65\x33\x30\x4d\x69\x6e", "\x30\x33\x30", "\x6b\x6c\x69\x6e\x65\x36\x30\x4d\x69\x6e", "\x30\x36\x30", "\x6b\x6c\x69\x6e\x65\x31\x44\x61\x79", "\x31\x30\x30", "\x6b\x6c\x69\x6e\x65\x31\x57\x65\x65\x6b", "\x32\x30\x30", "\x6b\x6c\x69\x6e\x65\x31\x4d\x6f\x6e", "\x33\x30\x30", "\x6b\x6c\x69\x6e\x65\x31\x59\x65\x61\x72", "\x34\x30\x30", "\x23\x63\x63\x31\x34\x31\x34", "\x23\x34\x39\x63\x30\x34\x33", "\x73\x6f\x6c\x69\x64", "\x68\x6f\x6c\x6c\x6f\x77", "\x23\x63\x63\x63", "\x23\x36\x36\x36\x36\x36\x36", "\x23\x66\x66\x66", "\x72\x67\x62\x61\x28\x32\x35\x35\x2c\x32\x35\x35\x2c\x32\x35\x35\x2c\x30\x2e\x32\x29", "\x23\x66\x66\x66\x66\x66\x66", "\x23\x61\x36\x63\x65\x65\x33", "\x23\x66\x64\x62\x66\x36\x66", "\x23\x64\x66\x38\x61\x64\x66", "\x23\x66\x66\x66\x66\x30\x30", "\x23\x30\x30\x66\x66\x66\x66", "\x23\x66\x66\x39\x36\x30\x30", "\x23\x30\x30\x30\x30\x30\x30", "#fc0404", "#00a800", "\x68\x6f\x6c\x6c\x6f\x77", "\x73\x6f\x6c\x69\x64", "\x23\x39\x39\x39", "\x23\x36\x36\x36", "\x23\x63\x34\x63\x64\x64\x39", "\x72\x67\x62\x61\x28\x30\x2c\x30\x2c\x30\x2c\x30\x2e\x31\x29", "\x23\x61\x36\x62\x31\x63\x31", "\x23\x32\x39\x39\x36\x33\x62", "\x23\x66\x66\x39\x36\x30\x30", "\x23\x66\x66\x30\x30\x64\x32", "\x23\x35\x35\x65\x31\x30\x30", "\x23\x38\x62\x38\x62\x38\x62", "\x23\x32\x33\x64\x38\x66\x66", "\x23\x30\x30\x33\x36\x66\x66", "\x23\x66\x66\x66\x66\x66\x66", "\x23\x66\x66\x33\x32\x33\x32", "\x23\x35\x34\x66\x66\x66\x66", "\x68\x6f\x6c\x6c\x6f\x77", "\x73\x6f\x6c\x69\x64", "\x23\x63\x63\x63", "\x23\x36\x36\x36", "#666", "\x72\x67\x62\x61\x28\x32\x35\x35\x2c\x32\x35\x35\x2c\x32\x35\x35\x2c\x30\x2e\x32\x29", "#aaa", "\x23\x66\x66\x66\x66\x66\x66", "\x23\x66\x66\x66\x66\x30\x30", "\x23\x66\x66\x30\x30\x66\x66", "\x23\x30\x30\x66\x66\x30\x30", "\x23\x30\x30\x66\x66\x66\x66", "\x23\x66\x66\x39\x36\x30\x30", "\x23\x30\x30\x30\x30\x30\x30", "\x62\x74\x63\x63\x6e\x79", "\x6c\x74\x63\x63\x6e\x79", "\x30\x2e\x31", "\x76\x65\x72\x20\x31\x2e\x30\x2e\x31", "\x6c\x69\x67\x68\x74", "\x31\x6d\x69\x6e", "\x4d\x41", "\x4b\x44\x4a", "\x6d", "\x67", "\x68", "\x69", "\x6a", "\x6b", "\x6c"];


    function b2() {
        var a = [].slice.apply(arguments);
        if (!(a.length < 2)) return a.shift().apply(a.shift(), a)
    }

    var preSyb;

    function aa(t) {
        var kx = !1;

        function e(t) {
            var e = a.n[a.o.f];
            l = CONN(a.b.a),
                s = a.d[a.o.k],
                s = key_map[a.e[s].socket],
                d = v.reqKLine(e, s),
                m = v.lastKLine(e, s),
            _$[0] === t && l.msg(v.lastKLine(a.n[preSyb], h).unmsg), preSyb = a.o.f,
            !t && l.reg(m.msgType, n),
                l.reg(d.msgType, n),
                l.msg(d.msg),
                l.msg(m.msg), h = s;
        };

        function n(a) {
            var t = key_map.msgType;
            !u && (u = o()), a && u[a[t]] && u[a[t]](a)
        };

        function o() {
            function a(a) {
                for (var t = [], e = a[key_map.payload], n = 0, o = e[key_map.priceLast].length; o > n; n++) t.push([e[key_map.time][n], e[key_map.priceOpen][n], e[key_map.priceHigh][n], e[key_map.priceLow][n], e[key_map.priceLast][n], e[key_map.amount][n]]);
                c(t, a)
            };

            function t(a) {
            	 
                var t = a[key_map.payload],
                    e = [
                        [t[key_map.time], t[key_map.priceOpen], t[key_map.priceHigh], t[key_map.priceLow], t[key_map.priceLast], t[key_map.amount]]
                    ];
                c(e, a)
            };
            return {
                reqKLine: a,
                lastKLine: t
            }
        };

        function i(t, f) {
            if (cig)return false;
            a.o.k = t;
            cig = 1;
            kx = 1;
            p.a2 = !1;
            a.o.d = 0;
            kzx = f;
            e(_$[1]);
            return true;
        };
        function ii(s, f) {
            if (cig)return false;
            preSyb = a.o.f;
            a.o.f = s;
            cig = 1;
            kx = 1;
            p.a2 = !1;
            a.o.d = 0;
            kzx = f;
            e(_$[1]);
            return true;
        };

        function c(t, e) {
        	 
            var n = 1,
                o = window.localStorage.getItem(_$[2]),
                i = key_map.msgType;
            if (o && o == a.o.a || (window.localStorage.setItem(_$[3], a.o.a)), _$[4] != typeof t) {
                if (e && _$[5] == e[i]) {
                    if (n = r(t, e), !n) return
                } else f(t, e);
                p && !p.a2 && p.v8()
            }
        };

        function r(t, e) {
            var n, o, i = window.localStorage.getItem(a.n[a.o.f] + a.o.k),
                c = t[0][0];
            if (kx)return;
            if (s = a.d[a.o.k], e[key_map.payload][key_map.period] == a.o.k) {
                if (!i || !i.length) return !1;
                if (i = JSON.parse(i), n = {
                        first: i[0],
                        last: i[i.length - 1]
                    }, c != n.last[0]) {
                    if (1 != ((c - n.last[0]) / s).toFixed()) return !1;
                    i.push(t[0]), i.length > 500 && i.splice(0, i.length - 500), o = JSON.stringify(i)
                } else i[i.length - 1] = t[0], i.length > 500 && i.splice(0, i.length - 500), o = JSON.stringify(i);
                return window.localStorage.setItem(a.n[a.o.f] + a.o.k, o), !0
            }
        };

        function f(t) {
            var e, n, o, i, c = window.localStorage.getItem(a.n[a.o.f] + a.o.k);
            s = a.d[a.o.k];
            e = {
                first: t[0],
                last: t[t.length - 1]
            }
            kzx && kzx();
            if (t.length < 1 || (t.length > 1 && !((t[1][0] - t[0][0]) / s).toFixed()))return cig = false;
            kx = !1;
            if (c)
                if (c = JSON.parse(c), n = {
                        first: c[0],
                        last: c[c.length - 1]
                    }, e.last[0] != n.last[0])
                    if (e.first[0] > n.last[0]) i = JSON.stringify(t);
                    else {
                        if (!(e.last[0] > n.last[0]))return cig = false;
                        o = ((n.last[0] - e.first[0]) / s).toFixed(), t.splice(0, o), t.length && (c[c.length - 1] = t.shift(), c = t ? c.concat(t) : c), c.length > 500 && c.splice(0, c.length - 500), i = JSON.stringify(c)
                    } else t[t.length - 1] && (c[c.length - 1] = t[t.length - 1]), c.length > 500 && c.splice(0, c.length - 500), i = JSON.stringify(c);
            else i = JSON.stringify(t);
            window.localStorage.setItem(a.n[a.o.f] + a.o.k, i)
            return cig = false;
        };
        var s, u, l, d, m, h, p = t, cig, kzx,
            v = API(),
            s = a.d[a.o.k];
        return e(), {
            changePeriod: i,
            changeSymbolId: ii
        }
    };

    function b0() {
        var a = this;
        a.position = function (t) {
            var e, n = a,
                o = 0,
                i = 0;
            if (a.nodeName) {
                for (; n && (o += n.offsetLeft, i += n.offsetTop, e = n.offsetParent ? n.offsetParent : n, n = n.offsetParent, !(t & t === n)););
                return {
                    x: o,
                    y: i,
                    element: n,
                    forefather: t || e
                }
            }
        }, a.size = function () {
            return {
                w: a.offsetWidth,
                h: a.offsetHeight
            }
        }
    };

    function __a2(t) {
        var mvv;

        function e(a) {
            var e = Event.mouse(a);
            if (!v && w && w.d5 && w.d5.length) {
                v = 1;
                setTimeout(function () {
                    v = 0
                }, 50);
                var n = Math.floor((e.x - w.pst.x) / g[0]),
                    o = w.d5[n];

                o = o ? o : w.d5[w.d5.length - 1],
                w.v4 == _[0] && (w.v7.id(o.e0, e.y),
                    w.v7.iw(o.i, o.e0, e.y),
                    t.e1 = !1,
                    t.v9(o.i),
                    t.e1 = !0);
            }
        };

        function n() {
            clearTimeout(y), y = null, g = a.a[a.o.j] || a.a[7]
        };

        function o(a) {
            var t = a.touch ? a : Event.mouse(a),
                e = (Event.target(a), t.x - w.move.x);
            i(e, t.x)
        };

        function i(e, n) {
            if (!(a.o.v || a.o.e < 1 || a.o.age > (a.o.z || Number.MAX_VALUE)) && 0 != ~~(e / g[0])) {
                if (a.o.d += ~~(e / g[0]), a.o.d < 0) return a.o.d = 0, b || (t.v8(), t.a2 = !1), b = !0, void 0;
                t.a2 = !0, b = !1, t.v8(), n && (w.move.x = n)
            }
        };

        function c(a) {
            e(a), x && o(a)
        };

        function r(a, t, e, n) {
            return Math.pow(Math.abs(a - e), 2) + Math.pow(Math.abs(t - n), 2)
        };

        function f(t) {
            var e = r(t[0].clientX, t[0].clientY, t[1].clientX, t[1].clientY);
            null === k && (k = r(t[0].clientX, t[0].clientY, t[1].clientX, t[1].clientY)), Math.abs((M - e) / k) < .15 || (w.changeSize(-1 * (e / k > 1 ? -1 : 1)), g = a.a[a.o.j] || a.a[7], M = e)
        };

        function s(a) {
            if (!v) {
                Event.stop(a), v = 1;
                var t = a.touches || a.changedTouches;
                2 == t.length ? f(t) : 1 == t.length && x && o({
                    touch: !0,
                    x: t[0].clientX,
                    y: t[0].clientY
                }), setTimeout(function () {
                    v = 0
                }, 20)
            }
        };

        function u() {
            w.v7.ae(), clearTimeout(y), y = null, y = setTimeout(t.v9, 30), x = !1, t.e1 = !1
        };

        function l(t) {
            var e = t.touches || t.changedTouches || null;
            return e && e.length > 1 ? (x = !1, k = null, void 0) : (w.move = e ? {
                x: e[0].clientX,
                y: e[0].clientY
            } : Event.mouse(t), g = a.a[a.o.j] || a.a[7], x = !0, void 0)
        };

        function d() {
            x = !1
        };

        function m() {
            x = !1
        };

        function h(t) {
            Event.stop(t), w.changeSize(-1 * Event.delta(t)), g = a.a[a.o.j] || a.a[7]
        };

        function p(t) {
            var e = t ? t.keyCode : window.event.keyCode;
            g = a.a[a.o.j] || a.a[7], 38 == e && (w.changeSize(1), g = a.a[a.o.j] || a.a[7]), 40 == e && (w.changeSize(-1), g = a.a[a.o.j] || a.a[7]), 37 == e && i(g[0]), 39 == e && i(-1 * g[0])
        };
        var v, g = a.a[a.o.j],
            y, x, b, w = this,
            k = null,
            M = null,
            _ = [_$[6], _$[7], _$[8], _$[9], _$[10], _$[11], _$[12], _$[13], _$[14], _$[15], _$[16], _$[17], _$[18], _$[19]];
        return w.cc ? w : (w.cc = !0, w.winresize = function () {
            var a = w.size(),
                t = w.position(),
                e = w.sz;
            (e.w != a.w || e.h != a.h) && (a.fs = 1, t.fs = 1, w.v8(a, t))
        }, Event.add(w, _[2], h), Event.add(w, _[3], h), Event.add(window, _[4], w.winresize), Event.add(w, _[5], l), Event.add(w, _[6], n), Event.add(w, _[7], c), Event.add(w, _[8], d), Event.add(w, _[9], u), Event.add(w, _[10], l), Event.add(w, _[11], s), Event.add(w, _[12], m)/*, Event.add(document, _[13], p)*/, void 0)
    };

    function __a(t, e) {
        function n() {
            var t = c.m3 - c.m1;
            return !t ? 0 : c.cc.h * a.o.n / t;
        };

        function o(a) {
            return 1 * ((c.m3 - a) * c.kr).toFixed() + 1 * c.e6 + .5
        };

        function i(e) {
            var n = o(e, c.m3),
                i = {};
            n > c.cc.h - 18 || 0 > n || (c.a(), i.x = c.cc.w - c.cc.rw + 5, i.y = ~~n, c[c.j1] = _$[20], c.b(i.x, i.y), c.c(i.x + 5, i.y + 8), c.c(i.x + t.ms.rw - 5, i.y + 8), c.c(i.x + t.ms.rw - 5, i.y - 8), c.c(i.x + 5, i.y - 8), c.c(i.x, i.y), c[c.l] = a.f[a.o.g].ir, c.e(), c[c.l] = a.f[a.o.g].sj, c.f(e, i.x + 5, i.y))
        };
        var c = this,
            e = e[1],
            r = {
                t: null,
                ts: 1,
                nl: 1,
                n: null
            };
        return c.sa ? c : (c.sa = !0, c.d6 = function (t, e, f, s, u) {
            var l, d, m, h, p, v, g = {
                    e9: t[0],
                    e8: t[1],
                    n: t[2],
                    t: t[3],
                    ts: t[4],
                    ta: t[5],
                    e7: e
                },
                y = 1 * g.ts > 1 * g.e8,
                x = y ? a.f[a.o.g].p0 : a.f[a.o.g].p5,
                b = a.a[a.o.j] || a.a[7],
                w = b[0] / 2 != (b[0] / 2).toFixed();
            return c.e6 = ~~(c.cc.h * (.5 - a.o.n / 2)), c.kr || (c.kr = n(), i(a.o.d ? u.pop()[4] : g.ts)), c[c.m] = x, c[c.l] = x, c.a(), d = b[0] * e + b[0] / 2 + (w ? 0 : .5), d > c.cc.w - b[0] && a.o.d ? null : (p = o(g.n, c.m3), v = o(g.t, c.m3), b[1] > 2 ? (l = d - b[1] / 2 + .5, y ? (m = o(g.ts, c.m3), h = o(g.e8, c.m3) - m) : (m = o(g.e8, c.m3), h = o(g.ts, c.m3) - m), !a.o.u && c.s0(l, m, b[1], h, y), c.b(d, p), c.c(d, m), c.b(d, m + h), c.c(d, v)) : (c.b(d, p), c.c(d, v == p ? p + .5 : v)), a.o.u || (c.d(), (c.m3 == g.n || c.m1 == g.t) && (c[c.m] = a.f[a.o.g].p3, c[c.l] = a.f[a.o.g].p3, c.a(), c[c.k] = _$[21]), a.o.x && c.m3 == g.n && !r.n && (c.b(d + 18, p - 13 * r.nl), c.c(d, p - 13 * r.nl), c.c(d, p), c.c(d + 3, p - 5), c.b(d, p), c.c(d - 3, p - 5), c.f(g.n, d + 20, p - 13 * r.nl), c.d(), r.n = 1), a.o.y && c.m1 == g.t && !r.t && (c.b(d + 10, v + 13 * r.ts), c.c(d, v + 13 * r.ts), c.c(d, v), c.c(d + 3, v + 5), c.b(d, v), c.c(d - 3, v + 5), c.f(g.t, d + 10, v + 13 * r.ts), c.d(), r.t = 1)), {
                i: e + a.o.c,
                str: f,
                e0: d,
                e11: x,
                d6X: l,
                e10: b,
                e12: y
            })
        }, c.s0 = function (t, e, n, o, i) {
            var r = i ? a.f[a.o.g].p9 : a.f[a.o.g].p8;
            _$[22] == r && c.g(t, e, n, o), o ? c.i(t, e, n, o) : (c.b(t, e), c.c(t + n, e))
        }, c.f0 = function (a) {
            c.j(0, 0, a.w, a.h), e.j(0, 0, a.w, a.h), c.kr = !1, r = {
                t: null,
                ts: 1,
                nl: 1,
                n: null
            }
        }, void 0)
    };

    function a6(t, e) {
        function n(a, t) {
            return _$[23] == typeof a ? Math.max(a[0] ? a[0] < 1 ? a[0] * t : a[0] : 0, a[1] ? a[1] < 1 ? a[1] * t : a[1] : 0) : a
        };

        function o(t, e, n, o, i, c, r, f) {
            var s = r - t * n,
                e = e * c / 2,
                l = o + t * n * c;
            l = 1 * l.toFixed(), a.o.s && 2 !== f && (u[u.m] = a.o.sc, u.a(), u.n(.5, l + .5), u.p(i + .5, l + .5), u.d()), u[u.l] = a.f[a.o.g].p3, u[u.m] = a.f[a.o.g].p7, u.a(), u.b(i + .5, l + .5), u.c(i + 5.5, l + .5), t && 1 === !f && (u.b(i + .5, l - e + .5), u.c(i + 5.5, l - e + .5)), u[u.k] = _$[24], s = s.toFixed(2), s > 1e8 ? s = (1e4 * (s / 1e4)).toFixed() + a.STR[17] : s > 1e4 && (s = (s / 1e4).toFixed() + a.STR[18]), 2 === f ? l -= 5 : (u.f(s, i + 8.5, l), u.d())

        };

        function i(a, t, e, n, i, c, r) {
            for (var f = t + 1, s = a / t; f--;) o(f / t, s, a, e, n, i, c, r), 2 === r && (r = null)
        };

        function c(e, n) {
            var o, i, c, r = e.e0,
                f = u.cc.vi + u.cc.vh + .5,
                s = n * a.d[a.o.k],
                l = e.data[e.i][0];
            u[u.l] = a.f[a.o.g].p3, u[u.m] = a.f[a.o.g].p7, u.a(), u.b(r, f), u.c(r, f + 5), u[u.k] = _$[27], 18e3 >= s ? (o = t.b3(l, _$[28]), i = 32) : 604800 >= s ? (o = t.b3(l, a.q[0]), i = 52) : (o = t.b3(l, a.q[1]), i = 64), c = r - i / 2, c = 0 > c ? 0 : c, u.f(o, c, f + 15), u.d()
        };

        function r(a, t) {
            var e, n, o;
            return a.indexOf(_$[31]) ? void 0 : (7 === a.length ? (e = parseInt(a.substring(1, 3), 16), n = parseInt(a.substring(3, 5), 16), o = parseInt(a.substring(5, 7), 16)) : (e = parseInt(a.substring(1, 2) + a.substring(1, 2), 16), n = parseInt(a.substring(2, 3) + a.substring(2, 3), 16), o = parseInt(a.substring(3, 4) + a.substring(3, 4), 16)), _$[32] + e + _$[33] + n + _$[34] + o + _$[35] + t + _$[36])
        };

        function f(t, e) {
            var n = a.o.A ? 1 - a.o.A : .5,
                o = t,
                i = e,
                c = a.o.B || .02,
                f = a.f[a.o.g].sj,
                l = a.f[a.o.g].p2,
                d = Math.min(o, i) * (1 - n) / 2,
                m = 1 * (.5 * d).toFixed(),
                h = 1 * (1.09091 * d).toFixed(),
                p = 1 * (.09 * d).toFixed(),
                v = 1 * (.176364 * d).toFixed(),
                g = 1 * (.46 * d).toFixed(),
                y = 1 * (.454546 * d).toFixed(),
                x = {
                    x: o / 2,
                    y: i / 2,
                    left: o / 2 - d,
                    top: i / 2 - d
                },
                b = {
                    x: 1 * (g + x.left).toFixed(),
                    y: 1 * (y + x.top).toFixed()
                };
            //s = s || r(l, c), 0 > d || (u[u.l] = s, u.a(), u.h(x.x, x.y, d, 0, 2 * Math.PI), u.e(), u[u.l] = f, u.a(), u.h(x.x, x.y, d - v, 0, 2 * Math.PI), u.e(), u[u.l] = s, u.g(b.x, b.y, m, h), u.g(b.x + p + m, b.y, m, h), u[u.l] = f, u.g(b.x + m / 3, b.y, 2 * m / 3, 1 * h / 4), u.g(b.x + m / 3, b.y + 3 * h / 4, 2 * m / 3, 1 * h / 4), u.g(b.x, b.y + 5 * h / 12, 2 * m / 3, 1 * h / 6), u.g(b.x + p + m, b.y, 2 * m / 3, 1 * h / 4), u.g(b.x + p + m, b.y + 3 * h / 4, 2 * m / 3, 1 * h / 4), u.g(b.x + p + m + m / 3, b.y + 5 * h / 12, 2 * m / 3, 1 * h / 6))
        };
        var s, u = this;
        return u.bd ? u : (u.bd = !0, u.x = function () {
            var t = u.m3 - u.m1,
                e = u.cc.vol * a.o.n,
                n = ~~(e / a.o.o),
                o = (u.cc.vol - e) / 2,
                c = u.cc.w;
            i(t, n, o, c, u.kr, u.m3)
        }, u.v = function () {
            var t = u.u3,
                e = u.cc.vh * a.o.n,
                n = ~~(e / a.o.o),
                o = u.cc.vol + u.cc.vh - e,
                c = u.cc.w;
            i(t, n, o, c, u.u2, u.u3, 2)
        }, u.u = function () {
            var t, e, n, o, i, c = u[a.o.m];
            c && (t = u.cc.w, e = c.offsetTop, n = c.m3 - c.m1, o = u.cc.vh * a.o.n, i = ~~(o / a.o.o))
        }, u.d3 = function (t) {
            var e = t.e10[0],
                n = Math.ceil(a.o.r / e);
            step = (t.str - t.i - 1) % n, step || c(t, n)
        }, u.d8 = function (o, i) {
            var c = (o.w, a.o.q),
                r = a.o.h,
                s = a.o.i;
            r = ~~n(r, o.w), s = ~~n(s, o.h), u.cc = {
                size: o,
                rw: r,
                vh: s,
                ad: c,
                vol: o.h - 2 * s - c,
                vi: o.h - s - c,
                w: o.w - r,
                h: o.h - 2 * s - c
            }, t.ms = u.cc, i && (t.v5.t.width = o.w, t.v5.t.height = o.h, t.v6.t.width = o.w, t.v6.t.height = u.cc.h, t.v7.t.width = o.w, t.v7.t.height = o.h, e[0].width = o.w, e[0].height = u.cc.h - 1, e[0].style.top = _$[37], t.z[0].style.top = o.h - 2 * s - c < 30 ? _$[38] : _$[39], t.z[1].style.top = o.h - 2 * s - c + 1 + _$[40], t.z[2].style.top = o.h - s - c + 1 + _$[41]), f(o.w - r, o.h - c, i), u[u.m] = a.f[a.o.g].p7, u.a(), a.o.t && (u.b(.5, .5), u.c(o.w, .5), u.b(o.w - .5, 0), u.c(o.w - .5, o.h - c + .5), u.b(.5, 0), u.c(.5, o.h - c + .5)), u.b(o.w - r + .5, 0), u.c(o.w - r + .5, o.h - c), u.b(0, o.h - 2 * s - c + .5), u.c(o.w, o.h - 2 * s - c + .5), u.b(0, o.h - s - c + .5), u.c(o.w, o.h - s - c + .5), u.b(0, o.h - c + .5), u.c(o.w, o.h - c + .5), u.d(), u[u.j1] = _$[42], u[u.k] = _$[43], u[u.l] = a.f[a.o.g].p3, u[u.j2] = _$[44], u.f(a.o.b, o.w - 7, o.h - 7), u[u.j2] = _$[45]
        }, void 0)
    };

    function a5(t, e) {
        var n = this,
            o = [],
            e = e[1];
        if (n.v1 = [], n.dv) return n;
        n.dv = !0, o[5] = 0, o[10] = 1, o[30] = 2, o[60] = 3, o[120] = 4, o[250] = 5;
        var i = function () {
            function t(t, i, c, r, f) {
                for (var s, u = 0, l = f || 0 === f ? f : o[t] ? o[t] : 0, d = 0; t > d; d++) u += 1 * i[r - d][4];
                return s = u, u = n.kr * (n.m3 - u / t) + 1 * n.e6, n.c1.a[t] && (e.a(), e[e.m] = a.f[a.o.g].sj2[l], e.b(n.c1.a[t][0], n.c1.a[t][1]), e.c(c, u), e.d()), n.c1.a[t] = [c, u], s
            };

            function i(a, t, e, n) {
                for (var o = 0, i = 0; a > i; i++) o += Math.pow(1 * e[n - i][4] - t, 2);
                return Math.sqrt(o / a)
            };

            function c(e, i, c) {
                var f, s = a.m || [5, 10, 30, 60, 120, 250],
                    u = 0,
                    l = s.length;
                if (a.o.u) return r(e, i, c);
                for (; l > u; u++) e > s[u] - 2 && (f = t(s[u], i, c, e)), n.v1[e] || (n.v1[e] = []), n.v1[e].push([_$[46] + s[u], f || 0 === f ? (f / s[u]).toFixed(2) : _$[47], o[s[u]]]), f = null
            };

            function r(e, i, c) {
                var r, s = a.o.p || 60;
                f(_$[48], i[e][4], c, 0), n.v1[e] = [], e + 1 >= a.o.p && (r = t(a.o.p, i, c, e, 1)), n.v1[e].push([_$[49] + s, r || 0 === r ? (r / s).toFixed(2) : _$[50], o[10]]), r = null
            };

            function f(t, o, i, c) {
                var r = n.kr * (n.m3 - o) + 1 * n.e6;
                n.c1.a[t] && (e.a(), e[e.m] = a.f[a.o.g].sj2[c], e.b(n.c1.a[t][0], n.c1.a[t][1]), e.c(i, r), e.d()), n.c1.a[t] = [i, r]
            };

            function s(e, o, c) {
                var s, u, l, d = a.g || 20;
                return a.o.u ? r(e, o, c) : (e + 1 >= d && (s = t(d, o, c, e, 0), u = s / d, l = i(d, u, o, e), f(_$[51], u + 2 * l, c, 1), f(_$[52], u - 2 * l, c, 2)), n.v1[e] = [
                    [_$[53], s || 0 === s ? u.toFixed(2) : _$[54], 0],
                    [_$[55], !s && 0 !== s || !l && 0 !== l ? _$[56] : (u + 2 * l).toFixed(2), 1],
                    [_$[57], !s && 0 !== s || !l && 0 !== l ? _$[58] : (u - 2 * l).toFixed(2), 2]
                ], void 0)
            };
            return {
                MA: c,
                BOLL: s
            }
        }();
        n.d1 = function (t) {
            i[a.o.l] && i[a.o.l](t.i, t.data, t.e0)
        }, n.f2 = function () {
            n.c1.a = [], n.v1 = []
        }
    };

    function __a1(t) {
        function e(e) {
            var n, o = t.v5.as;
            return e < t.ms.h ? n = (o.m3 - (e - o.e6 - .5) / o.kr).toFixed(2) : e < t.ms.vi ? (n = ((o.cc.vi - e) / o.u2).toFixed(2), n > 1e8 ? n = (1e4 * (n / 1e4)).toFixed() + _$[59] : n > 1e4 && (n = (n / 1e4).toFixed() + _$[60])) : n = (o.u1 - (e - o.cc.vi - o.cc.vh * (1 - a.o.n) / 2 - .5) / o.g1).toFixed(2), n
        };
        var n = this;


        return n.ks ? n : (n.ks = !0, n.a1 = function (a) {
            n.j(0, 0, a.w, a.h)
        }, n.ir = function (e, o, i, c) {
            n[n.m] = a.f[a.o.g].ir,
                n.a1(i),
                n.a(),
                n.b(e, 0),
                n.c(e, i.h - a.o.q),
            ~~o - .5 - c.y < i.h - a.o.q && (n.b(0, ~~o - .5 - c.y),
                    n.c(i.w - t.ms.rw, ~~o - .5 - c.y)
            ),
                n.d()
        }, n.iw = function (o, i, c, r, f) {
            var s = {};
            a.d[a.o.k] < 86400 ? (s.e9 = t.b3(o[0], _$[61]), s.w = 130) : (s.e9 = t.b3(o[0], _$[62]), s.w = 76), ~~c - .5 - f.y < r.h - a.o.q && (n.a(), s.x = r.w - t.ms.rw, s.y = ~~c - .5 - f.y, n[n.j1] = _$[63], n.b(s.x, s.y), n.c(s.x + 5, s.y + 8), n.c(s.x + t.ms.rw, s.y + 8), n.c(s.x + t.ms.rw, s.y - 8), n.c(s.x + 5, s.y - 8), n.c(s.x, s.y), n[n.l] = a.f[a.o.g].ir, n.e(), n[n.l] = a.f[a.o.g].sj, s.val = e(s.y), n.f(s.val, s.x + 10, s.y)), n.a(), n[n.m] = a.f[a.o.g].ir, n[n.l] = a.f[a.o.g].sj, s.x = i - s.w / 2, s.x = s.x < 1 ? 1 : s.x, s.y = r.h - t.ms.ad + .5, n[n.j1] = _$[64], n.b(s.x, s.y), n.c(s.x + s.w, s.y), n.c(s.x + s.w, s.y + a.o.q - 1.5), n.c(s.x, s.y + a.o.q - 1.5), n.c(s.x, s.y), n.d(), n.e(), n[n.l] = a.f[a.o.g].ir, n.f(s.e9, s.x + 8, s.y + a.o.q / 2)
        }, void 0)
    };

    function a7(t) {
        var e = this;
        return e.sl ? e : (e.sl = !0, e.d7 = function (n, o, i) {
            e.a(), e[e.l] = a.f[a.o.g].ir, e.h(o, i - t.pst.y, 2, 0, 2 * Math.PI, !0), e.e()
        }, e.da = function (n, o, i) {
            e.a(), e[e.l] = a.f[a.o.g].ir, e.h(o, i - t.pst.y, 2, 0, 2 * Math.PI, !0), e.e()
        }, void 0)
    };

    function __a3() {
        function t(a, t, e) {
            for (var n = (t + 1) * t / 2, o = 0, i = 0; t--; i++) o += a(e - i) * (t + 1) / n;
            return o
        };

        function e(e, n) {
            function o(a) {
                return c[a] ? c[a][4] : 0 / 0
            };

            function i(e) {
                return t(o, a.k[0], e) - t(o, a.k[1], e)
            };
            var c = n,
                r = e;
            return e < a.k[1] - 1 ? 0 / 0 : i(r)
        };

        function n(n, o) {
            function i(a) {
                return e(a, o)
            };

            function c(e) {
                return t(i, a.k[2], e)
            };
            var r, f = n;
            return r = c(f)
        };

        function o() {
            return b.g1 = !0, b.u1 = j, b.cc.vh * a.o.n / (j - $)
        };

        function i(t, e, n) {
            t < b.cc.vi || (b[b.m] = a.o.sc, b.a(), n ? (b.b(0, t), b.c(b.cc.w + 5.5, t)) : (b.n(0, t), b.p(b.cc.w + 5.5, t)), b[b.l] = a.f[a.o.g].p3, b[b.k] = _$[65], b.f(e, b.cc.w + 8.5, t), b.d())
        };

        function c(t) {
            var e, n = k[t.i - a.o.c].macd;
            b.g1 || (b.g1 = o(), w = b.cc.vh * (.5 - a.o.n / 2) + b.cc.vi, b.cd = ~~(b.g1 * j + w) + .5, i(b.cd, 0, 1), b.MACD = {
                m3: j,
                m1: $,
                rule: b.g1,
                offsetTop: w,
                cd: b.cd
            }), b[b.m] = 0 > n ? a.f[a.o.g].p5 : a.f[a.o.g].p0, b.a(), b.b(t.e0, b.cd), e = b.g1 * n, b.c(t.e0, b.cd - e), b.d(), u(t, _$[66], k[t.i - a.o.c].dif, a.f[a.o.g].sj2[0]), u(t, _$[67], k[t.i - a.o.c].dea, a.f[a.o.g].sj2[1])
        };

        function r(t) {
            b.g1 || (b.g1 = o(), w = b.cc.vh + b.cc.vi - b.cc.vh * (.5 - a.o.n / 2), i(w - b.g1 * (100 - $), 100), i(w - b.g1 * (50 - $), 50), i(w - b.g1 * (0 - $), 0), b.KDJ = {
                m3: j,
                m1: $,
                rule: b.g1,
                offsetTop: b.cc.vh * (.5 - a.o.n / 2) + b.cc.vi
            }), u(t, _$[68], k[t.i - a.o.c].K, a.f[a.o.g].sj2[0]), u(t, _$[69], k[t.i - a.o.c].D, a.f[a.o.g].sj2[1]), u(t, _$[70], k[t.i - a.o.c].J, a.f[a.o.g].sj2[2])
        };

        function f(t) {
            b.g1 || (b.g1 = o(), w = b.cc.vh + b.cc.vi - b.cc.vh * (.5 - a.o.n / 2), i(w - b.g1 * (80 - $), 80), i(w - b.g1 * (50 - $), 50), i(w - b.g1 * (20 - $), 20), b.RSI = {
                m3: j,
                m1: $,
                rule: b.g1,
                offsetTop: b.cc.vh * (.5 - a.o.n / 2) + b.cc.vi
            }), u(t, _$[71], k[t.i - a.o.c].RSI1, a.f[a.o.g].sj2[0]), u(t, _$[72], k[t.i - a.o.c].RSI2, a.f[a.o.g].sj2[1]), u(t, _$[73], k[t.i - a.o.c].RSI3, a.f[a.o.g].sj2[2])
        };

        function s(t) {
            b.g1 || (b.g1 = o(), w = b.cc.vh + b.cc.vi - b.cc.vh * (.5 - a.o.n / 2), i(w - b.g1 * (80 - $), 80), i(w - b.g1 * (50 - $), 50), i(w - b.g1 * (20 - $), 20), b.WR = {
                m3: j,
                m1: $,
                rule: b.g1,
                offsetTop: b.cc.vh * (.5 - a.o.n / 2) + b.cc.vi
            }), u(t, _$[74], k[t.i - a.o.c].WR1, a.f[a.o.g].sj2[0]), u(t, _$[75], k[t.i - a.o.c].WR2, a.f[a.o.g].sj2[1])
        };

        function u(a, t, e, n) {
            if (e || 0 === e) {
                var o = null === b.cd ? w - b.g1 * (e - $) : b.cd - b.g1 * e;
                b.c1.a7[t] && (b[b.m] = n, b.a(), b.b(b.c1.a7[t][0], b.c1.a7[t][1]), b.c(a.e0, o), b.d()), b.c1.a7[t] = [a.e0, o]
            }
        };

        function l(a, t) {
            return 1 > a ? 0 / 0 : t[a - 1][4]
        };

        function d(a, t, e, n) {
            var o = 0;
            if (n - 1 > t) return 0 / 0;
            for (var i = 0; n > i; i++) o += a(t - i, e);
            return o / n
        };

        function m(a, t) {
            return Math.max(t[a][4] - l(a, t), 0)
        };

        function h(a, t) {
            return Math.abs(t[a][4] - l(a, t))
        };

        function p(t, e) {
            return 100 * ((e[t][4] - y(t, e, a.l.N)) / (g(t, e, a.l.N) - y(t, e, a.l.N)))
        };

        function v(t, e) {
            return d(p, t, e, a.l.M1)
        };

        function g(a, t, e) {
            var n = Number.MIN_VALUE;
            if (e - 1 > a) return 0 / 0;
            for (var o = 0; e > o; o++) n = Math.max(t[a - o][2], n);
            return n
        };

        function y(a, t, e) {
            var n = Number.MAX_VALUE;
            if (e - 1 > a) return 0 / 0;
            for (var o = 0; e > o; o++) n = Math.min(t[a - o][3], n);
            return n
        };

        function x() {
            function t(a, t, o) {
                var i = e(a, o),
                    c = n(a, o),
                    r = 2 * i - 2 * c;
                k.unshift({
                    e7: a,
                    macd: r,
                    dif: i,
                    dea: c
                }), i && (j = Math.max(j, i), $ = Math.min($, i)), c && (j = Math.max(j, c), j = Math.max(j, r), $ = Math.min($, c), $ = Math.min($, r)), b.v3[a] = [
                    [_$[76], i || 0 === i ? i.toFixed(2) : _$[77], 0],
                    [_$[78], c || 0 === c ? c.toFixed(2) : _$[79], 1],
                    [_$[80], r || 0 === r ? r.toFixed(2) : _$[81], 2]
                ]
            };

            function o(t, e, n) {
                var o, i, c;
                t + 1 < a.l.N && t + 1 < a.l.M1 && t + 1 < a.l.M2 ? k.unshift({
                    e7: t,
                    K: null,
                    D: null,
                    J: null
                }) : (o = v(t, n), i = d(v, t, n, a.l.M2), c = 3 * o - 2 * i, k.unshift({
                    e7: t,
                    K: o,
                    D: i,
                    J: c
                }), (o || 0 === o) && (j = Math.max(j, o), $ = Math.min($, o)), (i || 0 === i) && (j = Math.max(j, i), $ = Math.min($, i)), (c || 0 === c) && (j = Math.max(j, c), $ = Math.min($, c))), b.v3[t] = [
                    [_$[82], o || 0 === o ? o.toFixed(2) : _$[83], 0],
                    [_$[84], i || 0 === i ? i.toFixed(2) : _$[85], 1],
                    [_$[86], c || 0 === c ? c.toFixed(2) : _$[87], 2]
                ]
            };

            function i(t, e, n) {
                var o, i, c;
                t < a.j.N1 && t < a.j.N2 && t < a.j.N3 ? k.unshift({
                    e7: t,
                    RSI1: null,
                    RSI2: null,
                    RSI3: null
                }) : (o = 100 * (d(m, t, n, a.j.N1) / d(h, t, n, a.j.N1)), i = 100 * (d(m, t, n, a.j.N2) / d(h, t, n, a.j.N2)), c = 100 * (d(m, t, n, a.j.N3) / d(h, t, n, a.j.N3)), k.unshift({
                    e7: t,
                    RSI1: o,
                    RSI2: i,
                    RSI3: c
                }), (o || 0 === o) && (j = Math.max(j, o), $ = Math.min($, o)), (i || 0 === i) && (j = Math.max(j, i), $ = Math.min($, i)), (c || 0 === c) && (j = Math.max(j, c), $ = Math.min($, c))), b.v3[t] = [
                    [_$[88], o || 0 === o ? o.toFixed(2) : _$[89], 0],
                    [_$[90], i || 0 === i ? i.toFixed(2) : _$[91], 1],
                    [_$[92], c || 0 === c ? c.toFixed(2) : _$[93], 2]
                ]
            };

            function c(t, e, n) {
                var o, i, c, r;
                t + 1 < a.i.N && t + 1 < a.i.N1 ? k.unshift({
                    e7: t,
                    WR1: null,
                    WR2: null
                }) : (c = g(t, n, a.i.N), r = g(t, n, a.i.N1), o = 100 * (c - n[t][4]) / (c - y(t, n, a.i.N)), i = 100 * (r - n[t][4]) / (r - y(t, n, a.i.N1)), k.unshift({
                    e7: t,
                    WR1: o,
                    WR2: i
                }), (o || 0 === o) && (j = Math.max(j, o), $ = Math.min($, o)), (i || 0 === i) && (j = Math.max(j, i), $ = Math.min($, i))), b.v3[t] = [
                    [_$[94], o || 0 === o ? o.toFixed(2) : _$[95], 0],
                    [_$[96], i || 0 === i ? i.toFixed(2) : _$[97], 1]
                ]
            };
            return {
                MACD: t,
                KDJ: o,
                RSI: i,
                WR: c
            }
        };
        var b = this,
            w = 0,
            k = [],
            M = {},
            _ = null,
            j = Number.MIN_VALUE,
            $ = Number.MAX_VALUE;
        return b.ac ? b : (b.ac = !0, b.g1 = !1, b.cd = null, b.v3 = [], b.f1 = function () {
            k.length = 0, b.c1.a7 = [], j = Number.MIN_VALUE, $ = Number.MAX_VALUE, b.g1 = !1, b.MACD = null, b.KDJ = null, b.RSI = null, b.WR = null, b.cd = null, b.v3 = []
        }, M.fn = {
            MACD: c,
            KDJ: r,
            RSI: f,
            WR: s
        }, b.d2 = function (t) {
            M.fn[a.o.m] && M.fn[a.o.m](t)
        }, b.d7 = function (t, e, n) {
            !_ && (_ = x()), _[a.o.m] && _[a.o.m](t, e, n)
        }, void 0)
    };

    function a4() {
        function t() {
            return n.u2 = !0, n.cc.vh * a.o.n / n.u3
        };
        var A = a;

        function e(t, e, i, c) {
            function r(r) {
                var f, s, u = 0;
                o++, s = o++ % e;
                for (var l = 0; r > l; l++) u += 1 * i[t - l][5];
                f = u / r, u = 1 * n.cc.vi - n.u2 * u / r, n.c1.a2[r] && (n[n.m] = a.f[a.o.g].sj2[s], n.a(), n.b(n.c1.a2[r][0], n.c1.a2[r][1]), n.c(c, u), n.d()), n.v2[t].push([_$[98] + r, f || 0 === f ? f.toFixed(2) : _$[99], s]), n.c1.a2[r] = [c, u]
            };
            var e = 0;
            for (var f in a.h) a.h.hasOwnProperty(f) && (e++, t > a.h[f] - 2 && r(a.h[f]))
        };
        var n = this,
            o = 0;
        return n.v2 = [], n.u2 = !1, n.cd ? n : (n.cd = !0, n.d4 = function (a) {
            var o, i, c = a.data[a.i][5],
                r = 0,
                f = n.cc.vol + n.cc.vh;
            n[n.m] = a.e11, n[n.l] = a.e11, n.a(), n.u2 || (n.u2 = t()), o = (n.u2 * c).toFixed(), i = 0 >= o ? f - r + .5 : f - o - r + .5, a.e10[1] > 1 ? n.s0(a.d6X, i, a.e10[1], o, a.e12, a.i) : (n.b(a.e0, i), n.c(a.e0, 0 == o ? 1 * i + .5 : 1 * i + 1 * o), n.d()), n.v2[a.i] || (n.v2[a.i] = {}), n.v2[a.i] = [
                [A.STR[16], 1 * c.toFixed(4), -1]
            ], n.d(), e(a.i, a.str, a.data, a.e0)
        }, n.f3 = function () {
            n.c1.a2 = [], n.v2 = [], n.u2 = !1
        }, void 0)
    };

    function b1() {
        var a = this;
        a.o || (a.n = function (t, e) {
            a.b(t, e), a.o.x = t, a.o.y = e
        }), a.o = {}, a.p = function (t, e, n) {
            for (var o, i, n = void 0 === n ? 3 : n, c = a.o.x, r = a.o.y, f = t - c, s = e - r, u = Math.floor(Math.sqrt(f * f + s * s) / n), l = 0; u > l; ++l) o = c + f / u * l, i = r + s / u * l, 0 === l % 2 ? a.n(o, i) : a.c(o, i);
            a.n(t, e)
        }, a.a = a.beginPath, a.b = a.moveTo, a.c = a.lineTo, a.d = a.stroke, a.e = a.fill, a.f = a.fillText, a.g = a.fillRect, a.h = a.arc, a.i = a.rect, a.j = a.clearRect, a.j2 = _$[101], a.j1 = _$[102], a.k = _$[103], a.l = _$[104], a.m = _$[105]
    };

    function period() {
        function t() {
            function a() {
                var a = [].slice.apply(arguments),
                    t = a.shift();
                c[t] && c[t].apply(c, a)
            };

            function t(a, t, e) {
                return c.d6(a, t, e, I, F)
            };

            function e(a, t) {
                c.d7(a, t, F)
            };

            function n(a) {
                c.d8(w, a)
            };

            function o() {
                c.x(), c.v(), c.u()
            };

            function i() {
                c.f0(w), c.f1(), c.f2(), c.f3()
            };
            var c = j[1],
                r = j[0];
            return c.c1 = {
                a: [],
                a2: [],
                a7: []
            }, {
                t: r,
                as: c,
                clear: i,
                c2: n,
                c3: o,
                tt: a,
                t1: t,
                ct: e
            }
        };

        function e() {
            var a = $[1],
                t = $[0];
            return {
                t: t,
                as: a
            }
        };

        function n() {
            function a(a, t) {
                n.ir(a, t, w, I)
            };

            function t(a, t, e) {
                n.iw(F[a], t, e, w, I)
            };

            function e() {
                n.a1(w)
            };
            var n = N[1],
                o = N[0];
            return {
                t: o,
                as: n,
                id: a,
                iw: t,
                ae: e
            }
        };

        function o(t, e) {
            var n, o, i, c = 0,
                f = 0,
                s = Number.MAX_VALUE,
                u = a.a[a.o.j] || a.a[7];
            if (window['ISPAUSE'])return;
            t && t.fs && (w = t) && e && (I = e, b.pst = e);
            b.d5 = [];
            a.o.c = 0;
            a.o.d = a.o.d || 0;
            F = JSON.parse(window.localStorage.getItem(a.n[a.o.f] + a.o.k));
            if (F && (_$[106] == typeof F) && F.length) {
                if (o = F.length, k.clear(), t && t.fs ? k.c2(!0) : (k.c2(A), A = !1), !k.as.cc.w) return;
                if (n = ~~((k.as.cc.w - 50) / u[0]), 0 > n) return;
                a.o.age = n, b.str = o, b.age = n, a.o.e = o - n, i = n > o ? o : n, a.o.e > 0 && (a.o.c = a.o.e), a.o.d > a.o.e && a.o.e > 0 && (a.o.d = a.o.e), i == o && (a.o.d = 0), a.o.c -= a.o.d;
                for (var l = i, d = o - 1 - a.o.d; l--; d--) c = Math.max(F[d][2], c), f = Math.max(F[d][5], f), s = Math.min(F[d][3], s), k.ct(d, o);
                k.as.m3 = c, k.as.m1 = s, k.as.u3 = f, k.as[k.as.j1] = _$[107];
                for (var m, l = i, d = o - 1 - a.o.d; l--; d--) m = k.t1(F[d], l, o), m && (!m.data && (m.data = F), b.d5.push(m), k.tt(_$[108], m), k.tt(_$[109], m), k.tt(_$[110], m), k.tt(_$[111], m));
                r(), b.d5 = b.d5.reverse(), k.c3(), m && (m.data = null)
            }
        };

        function i(a, t) {
            var e = a < Math.pow(10, 11) ? new Date(1e3 * a) : new Date(a),
                n = e.getFullYear(),
                o = c(e.getMonth() + 1),
                i = c(e.getDate()),
                r = c(e.getHours()),
                f = c(e.getMinutes()),
                s = c(e.getSeconds());
            return t ? t.replace(_$[112], n).replace(_$[113], o).replace(_$[114], i).replace(_$[115], r).replace(_$[116], f).replace(_$[117], s) : n + _$[118] + o + _$[119] + i + _$[120] + r + _$[121] + f + _$[122] + s
        };

        function c(a) {
            return a > 9 ? a : _$[123] + a
        };

        function r(t) {
            if (!b.e1) {
                var e, n = t || 0 === t ? k.as.v1[t] : k.as.v1[k.as.v1.length - 1],
                    o = t || 0 === t ? k.as.v2[t] : k.as.v2[k.as.v2.length - 1],
                    c = t || 0 === t ? k.as.v3[t] : k.as.v3[k.as.v3.length - 1],
                    r = _$[124],
                    f = a.d[a.o.k] < 86400 ? _$[125] : _$[126],
                    s = _$[127] + a.f[a.o.g].sj + _$[128],
                    u = a.f[a.o.g].sj2,
                    l = a[pa[a.o.m]],
                    d = [];
                if (F) {
                    if (e = t || 0 === t ? F[t] : F[F.length - 1 - a.o.d], b.z[0].innerHTML = _$[129] + s + a.f[a.o.g].p3 + a.STR[0] + i(e[0], f) + a.STR[1] + a.STR[2] + s + a.f[a.o.g].p3 + a.STR[3] + e[1] + a.STR[4] + a.STR[5] + s + a.f[a.o.g].p3 + a.STR[6] + e[2] + a.STR[7] + a.STR[8] + s + a.f[a.o.g].p3 + a.STR[9] + e[3] + a.STR[10] + a.STR[11] + s + a.f[a.o.g].p3 + a.STR[12] + e[4] + a.STR[13] + a.STR[14] + s + a.f[a.o.g].p3 + a.STR[15] + 1 * e[5].toFixed(4) + _$[146], n && n.length) {
                        for (var m = 0, h = n.length; h > m; m++) r += _$[147] + s + (~n[m][2] ? u[n[m][2]] : a.f[a.o.g].p3) + _$[148] + n[m][0] + _$[149] + n[m][1] + _$[150];
                        b.z[0].innerHTML += r, r = _$[151]
                    }
                    ;
                    if (o && o.length) {
                        for (var m = 0, h = o.length; h > m; m++) r += _$[152] + s + (~o[m][2] ? u[o[m][2]] : a.f[a.o.g].p3) + _$[153] + o[m][0] + _$[154] + o[m][1] + _$[155];
                        b.z[1].innerHTML = r
                    }
                    ;
                    if (c && c.length) {
                        if (r = _$[156] + s + a.f[a.o.g].p3 + _$[157] + a.o.m + _$[158], _$[159] === a.o.m) r += a.k.join(_$[160]);
                        else {
                            for (var p in l) l.hasOwnProperty(p) && d.push(p + _$[161] + l[p]);
                            r += d.join(_$[162])
                        }
                        ;
                        r += _$[163];
                        for (var m = 0, h = c.length; h > m; m++) r += _$[164] + s + (~c[m][2] ? u[c[m][2]] : a.f[a.o.g].ir) + _$[165] + c[m][0] + _$[166] + c[m][1] + _$[167];
                        b.z[2].innerHTML = r
                    }
                }
            }
        };

        function f(a) {
            b.v4 = a
        };

        function s(t, f, dci) {
            if (a.o.k != t || a.o.u) {
                a.o.u = dci;
                S.changePeriod(t, f);
            } else {
                f && f();
            }
        };

        function u() {
            var t = a.f[a.o.g].sj;
            b.style.background = t, a.o.sc = a.f[a.o.g].p1
        };

        function l() {
            return w
        };

        function d(t) {
            a.o.l != t && (a.o.l = t, o())
        };

        function m(t) {
            a.o.m != t && (a.o.m = t, o())
        };

        function h(t) {
            a.o.g != t && (a.o.g = t, u(), o())
        };

        function p(m, f) {
            if (!a.o.u) {
                a.o.u = !0, s(_$[168], f, 1);
            } else {
                f && f();
            }
        };

        function v(t) {
            var t = -1 * t;
            a.o.w || (a.o.j -= t, (a.o.j < 0 || a.o.j >= a.o.tt) && (a.o.j < 0 && (a.o.j = 0), a.o.j >= a.o.tt && (a.o.j = a.o.tt - 1)), o())
        };

        function g() {
            var a = document.createElement(_$[169]),
                t = a.getContext(_$[170]);
            return b2(b1, t), a.style.cssText += _$[171], b.appendChild(a), [a, t]
        };

        function y() {
            var a, t, e, n = document.createElement(_$[172]),
                o = _$[173];
            return n.innerHTML = _$[174], a = n.getElementsByTagName(_$[175])[0], t = n.getElementsByTagName(_$[176])[0], e = n.getElementsByTagName(_$[177])[1], n.style.cssText += _$[178], a.style.cssText += o, t.style.cssText += o, e.style.cssText += o, b.appendChild(n), [a, t, e]
        };

        function x() {
            return S = aa(b)
        };
        var b, w, k, M, _, j, $, N, I, S, F, T, R, E = document.createElement(_$[179]),
            A = !0;
        return E && E.getContext ? (~function (c, f) {
            _$[180];
            b = c && _$[181] == typeof c ? document.getElementById(c) : c, b && (a.a.sort(function (a, t) {
                return a[0] - t[0]
            }), b2(b0, b), w = b.size(), I = b.position(), j = g(), T = g(), N = g(), $ = g(), R = y(), k = t(), M = e(), _ = n(), b.v5 = k, b.v6 = M, b.v7 = _, b.v8 = o, b.changeSize = v, b.sz = l, b.mi = T, b.pst = I, b.z = R, b.v9 = r, b.b3 = i, b2(a6, k.as, b, T), b2(__a, k.as, b, T), b2(a5, k.as, b, T), b2(a4, k.as, b), b2(__a3, k.as, b), b2(__a1, _.as, b), b2(a7, M.as, b), b2(__a2, b, b), b.v4 = f.currentTools, a.o.a = f.version || a.o.a, a.o.u = f.timeShare || a.o.u, a.o.tt = a.a.length, a.o.j = f.level || a.o.j || 7, a.b.a = f.url || a.b.a, a.n = f.symlist || a.n, a.o.r = f.timeSpace || a.o.r || 50, a.o.m = f.vIndicator || a.o.m || _$[182], a.o.n = f.drawRange || a.o.n || 1, a.o.q = f.timeline || a.o.q || 20, a.o.o = f.m1RuleHeight || a.o.o || 30, a.o.f = f.symbol || a.o.f || 0, a.o.g = f.schemes || a.o.g || _$[183], a.o.h = f.ruleWidth || a.o.h || [60, .005], a.o.i = f.viceHeight || a.o.i || [80, .2], a.o.l = f.indicator || a.o.l || _$[184], a.o.p = f.timeShareMA || a.o.p || 60, a.o.s = 0 === f.dashed ? 0 : f.dashed || 1, a.o.t = 0 === f.border ? 0 : f.border || 1, a.o.v = f.stopDrag || a.o.v || 0, a.o.w = f.stopZoom || a.o.w || 0, a.o.x = f.showHigh || a.o.x || 0, a.o.y = f.showLow || a.o.y || 0, a.o.z = f.moveThreshold || a.o.z || 999, a.o.k = a.o.u ? _$[185] : f.time || a.o.k, x(), u());
            preSyb = a.o.f;

        }.apply(window, arguments), {
            box: b,
            req: S,
            setTools: f,
            setTime: s,
            changeMI: d,
            changeVI: m,
            rd: o,
            changeSchemes: h,
            changeSize: v,
            noKMode: p
        }) : {
            req: x(),
            lower: !0
        }
    }

    var a = FL_CONFIG,
        pa = {
            MA: _$[272],
            BOLL: _$[273],
            VOLUME: _$[274],
            WR: _$[275],
            RSI: _$[276],
            MACD: _$[277],
            KDJ: _$[278]
        };
    module.exports = period;
});