var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var request = require('request');
var redis = require("redis");

var redis_password = process.env.npm_package_config_redis_password;
redis_client = redis.createClient({
    host: process.env.npm_package_config_redis_host,
    port: process.env.npm_package_config_redis_port,
    password: redis_password
});

app.get('/', function (req, res) {
    res.send('<h1>Welcome Realtime Server</h1>');
});

// 在线用户
var onlineUsers = {};
// 当前在线人数
var onlineCount = 0;

var url_cn = process.env.npm_package_config_front_url;

function tochange(mobile, count) {
    console.log(count + "秒后再次提醒" + mobile + "更新")
    io.in("user_room").emit('change', mobile, 200);
}

var otcjson = [];

function uniqeByKeys(array, keys) {
    var arr = [];
    var hash = {};
    for (var i = 0, j = array.length; i < j; i++) {
        var k = obj2key(array[i], keys);
        if (!(k in hash)) {
            hash[k] = true;
            arr.push(array[i]);
        }
    }
    return arr;
}

function _tocahnge(mobile, count) {
    return function () {
        tochange(mobile, count);
    }
}

function obj2key(obj, keys) {
    var n = keys.length,
        key = [];
    while (n--) {
        key.push(obj[keys[n]]);
    }
    return key.join('|')
}

function getRedisData() {
    redis_client.auth(redis_password, function () {
        console.log("redis连接成功");
    });
    //客户端连接redis成功后执行回调
    redis_client.on("ready", function () {
        //订阅消息
        redis_client.subscribe("user_room");
        console.log("订阅成功。。。");
    });

    redis_client.on("error", function (error) {
        console.log("Redis Error " + error);
    });

    //监听订阅成功事件
    redis_client.on("subscribe", function (channel, count) {
        console.log("client subscribed to " + channel + "," + count + "total subscriptions");
    });

    //收到消息后执行回调，message是redis发布的消息
    redis_client.on("message", function (channel, message) {
        console.log("我接收到成交信息了" + message);
        try {
            if (message != null && message != undefined) {
                var mobilearr = message.split(",");
                for (var i = 0; i < mobilearr.length; i++) {
                    console.log("提醒" + mobilearr[i] + "更新")

                    //第一次立即通知
                    io.in("user_room").emit('change', mobilearr[i], 200);
                    //1秒后第二次通知
                    setTimeout(_tocahnge(mobilearr[i], 1), 1000);
                    //5秒后第三次通知
                    setTimeout(_tocahnge(mobilearr[i], 5), 5000);
                }
            }
        } catch (e) {
            console.log(e);
        }
    });

    //监听取消订阅事件
    redis_client.on("unsubscribe", function (channel, count) {
        console.log("client unsubscribed from" + channel + ", " + count + " total subscriptions")
    });
}

getRedisData();

io.on('connection', function (socket) {
    // 监听新用户加入
    socket.on('login', function (obj) {
        if (obj.room.indexOf("kline") != -1) {
            var i = 0;
            console.log("---------------行情中心退房算法--------------------")
            for (var key in socket.adapter.rooms) {
                console.log("key==" + key)
                i++;
                if (i > 2) {
                    if (key != obj.room) {
                        console.log("退出" + key + "房间");
                        socket.leave(key);
                    }
                }
            }
            console.log("加入专业交易" + obj.room);
            socket.join(obj.room);//加入行情中心

        } else if (obj.room.indexOf("formalTrade") != -1) {
            var i = 0;
            console.log("---------------专业交易退房算法--------------------")
            for (var key in socket.adapter.rooms) {
                console.log("key==" + key)
                i++;
                if (i > 2) {
                    if (key != obj.room) {
                        console.log("退出" + key + "房间");
                        socket.leave(key);
                    }
                }
            }
            console.log("加入专业交易" + obj.room);
            socket.join(obj.room);//加入专业交易
        } else {
            socket.join(obj.room);//加入房间
            console.log("加入----------------------" + obj.room);
        }

        // 将新加入用户的唯一标识当作socket的名称，后面退出的时候会用到
        socket.name = obj.userid;

        // 检查在线列表，如果不在里面就加入
        if (!onlineUsers.hasOwnProperty(obj.userid)) {
            onlineUsers[obj.userid] = obj.username;
            // 在线人数+1
            onlineCount++;
        }

        // 向所有客户端广播用户加入
        io.emit('login', {
            onlineUsers: onlineUsers,
            onlineCount: onlineCount,
            user: obj
        });
        //console.log(obj.username + '加入了聊天室');
    });

    // 监听用户发布聊天内容
    socket.on('message', function (data, isRequest) {
        // 向所有客户端广播发布的消息
        io.emit('message', data, isRequest);

    });

    //初始化数据
    socket.on('request', function (obj) {

        var website = "cn";  //站点      默认cn
        var symbol = "BTC";  //币种      默认BTC
        var period = "1min" //K线分期   默认1min
        var marketDetail = "0"  //深度   默认0

        if (obj.website != undefined && obj.website.indexOf("en") != -1) {
            website = "en";//设置为国际站房间
        }
        if (obj.symbol != undefined) {
            symbol = obj.symbol;
        }

        if (obj.msgType == "reqMsgUnsubscribe") {//退订
            //离开K线房间
            if (obj.symbolList.lastKLine != undefined) {
                period = obj.symbolList.lastKLine[0].period[0];
                var kroom = website + symbol + period;
                console.log("离开" + kroom + "房间");
                socket.leave(kroom);
            }

            //离开交易房间
            for (var i = 0; i <= 5; i++) {
                if (eval("obj.symbolList.marketDetail" + i) != undefined) {
                    marketDetail = i;
                    var droom = website + symbol + marketDetail;
                    console.log("离开" + droom + "房间");
                    socket.leave(droom);
                }
            }
        }
        if (obj.msgType == "reqMsgSubscribe") {//订阅
            try {
                //加载K线房间
                if (obj.symbolList.lastKLine != undefined) {
                    period = obj.symbolList.lastKLine[0].period[0];
                    var kroom = website + symbol + period;
                    console.log("加入" + kroom + "房间");
                    socket.join(kroom);

                    if ("cn" == website) {
                        request(url_cn + '/klinevtwo/con?symbol=' + symbol + '&period=' + period, function (error, response, body) {
                            if (!error && response.statusCode == 200) {
                                var data = JSON.parse(body);
                                console.log("加载K线数据" + website + symbol + data.reqKLine.payload.period);
                                io.in(website + symbol + data.reqKLine.payload.period).emit('request', data.reqMsgSubscribe, 200);
                                io.in(website + symbol + data.reqKLine.payload.period).emit('request', data.reqKLine, 200);
                            }
                        })
                    }
                }
                //加入交易房间
                for (var i = 0; i <= 5; i++) {
                    if (eval("obj.symbolList.marketDetail" + i) != undefined) {
                        marketDetail = i;
                        var droom = website + symbol + marketDetail;
                        console.log("加入" + droom + "房间");
                        socket.join(droom);
                    }
                }
            } catch (e) {
                // TODO: handle exception
            }
        }
        //console.log(obj);
        if (obj.msgType == "user_room") {
            console.log("--------------加入user_room房间")
            socket.join("user_room");
        }


    });

    // 监听用户退出
    socket.on('disconnect', function (type, obj) {
        console.log("退出了" + obj);
    });

    socket.on('reconnect', function (type, obj) {

    });

    socket.on('error', function (type, obj) {

    });
    // otc监听用户进入房间
    socket.on('otclogin', function (obj) {
        var data = {"coinCode": obj.coinCode};
        for (var i = 0; i < otcjson.length; i++) {
            if (otcjson[i] == data) {
                null;
            } else {
                otcjson.push(data);
            }
        }
        if (!otcjson.length) {
            otcjson.push(data);
        }
        otcjson = uniqeByKeys(otcjson, ['coinCode'])
        var user_room = "otc_" + obj.transactionType + "_" + obj.coinCode;

        // 将新加入用户的唯一标识当作socket的名称，后面退出的时候会用到
        socket.name = obj.userid;

        // 检查在线列表，如果不在里面就加入
        if (!onlineUsers.hasOwnProperty(obj.userid)) {
            onlineUsers[obj.userid] = obj.username;
            // 在线人数+1
            onlineCount++;
        }

        // 向所有客户端广播用户加入
        io.emit('login', {
            onlineUsers: onlineUsers,
            onlineCount: onlineCount,
            user: obj
        });
        console.log("进入" + user_room + "房间");
        socket.join(user_room);

    });
    // otc监听用户换房间
    socket.on('otcloginroom', function (obj) {
        console.log("otc换房间");
        console.log("离开" + obj.room + "房间");
        socket.leave(obj.room);
        var user_room = "otc_" + obj.transactionType + "_" + obj.coinCode
        console.log("进入" + user_room + "房间");
        socket.join(user_room);
    });
});

//分房机制
//交易数据分房机制： 只按站点分房 
//K线数据分房机制：：按站点分房  +  按k线类型分房
//定时请求
setInterval(function () {
    //中国站
    request(url_cn + '/klinevtwo/message', function (error, response, body) {
        if (!error && response.statusCode == 200) {
            try {
                var data = JSON.parse(body);
                for (var i = 0; i < data.productList.length; i++) {
                    var symbol = data.productList[i];
                    var marketDetail = eval("data.marketDetail." + symbol);
                    for (var j = 0; j < marketDetail.length; j++) {
                        var droom = "cn" + symbol + j;
                        //console.log(droom);
                        //推送到交易大厅
                        io.in(droom).emit('message', marketDetail[j], 200);

                        //推送到专业交易的页面
                        // console.log("formalTrad");
                        var formalTradeRoom = "formalTrade_cn_" + symbol + "_" + j;
                        io.in(formalTradeRoom).emit('formalTrade', marketDetail[j]);

                        //推送到行情中心
                        if (j == 0) {
                            var klineRoom = "kline_cn_" + symbol
                            //console.log(klineRoom);
                            io.in(klineRoom).emit('kline', symbol, marketDetail[j]);
                        }

                    }

                    var lastKLine = eval("data.lastKLine." + symbol);
                    for (var j = 0; j < lastKLine.length; j++) {
                        var kroom = "cn" + symbol + lastKLine[j].payload.period;

                        //console.log(kroom);
                        io.in(kroom).emit('message', lastKLine[j], 200);
                    }
                }
            } catch (e) {
                // TODO: handle exception
            }
        }
    });
}, 1000);

//otc定时请求
setInterval(function () {
    request(url_cn + '/otc/getOtcTransactionForSell', function (error, response, body) {
        if (!error && response.statusCode == 200) {
            try {

                var data = JSON.parse(body);
                var otcdata = data.obj;
                for (var j = 0; j < otcjson.length; j++) {
                    var newData = [];
                    for (var i = 0; i < otcdata.length; i++) {

                        if (otcjson[j].coinCode == otcdata[i].coinCode) {
                            newData.push(data.obj[i]);
                        }
                    }
                    var user_room = "otc_buy_" + otcjson[j].coinCode;
                    io.in(user_room).emit("otc_room", newData);
                    //console.log(newData);
                }
            } catch (e) {
                // TODO: handle exception
            }
        }
    });

    request(url_cn + '/otc/getOtcTransactionForBuy', function (error, response, body) {
        if (!error && response.statusCode == 200) {
            try {
                var data = JSON.parse(body);
                var otcdata = data.obj;
                for (var j = 0; j < otcjson.length; j++) {
                    var newData = [];
                    for (var i = 0; i < otcdata.length; i++) {

                        if (otcjson[j].coinCode == otcdata[i].coinCode) {
                            newData.push(data.obj[i]);
                        }
                    }
                    var user_room = "otc_sell_" + otcjson[j].coinCode;
                    io.in(user_room).emit("otc_room", newData);
                    //console.log(newData);
                }
            } catch (e) {
                // TODO: handle exception
            }
        } else {
            //console.log("otc请求错误！");
        }
    });

    request(url_cn + '/otc/getOtcTransactionAll', function (error, response, body) {
        if (!error && response.statusCode == 200) {
            try {
                var data = JSON.parse(body);
                io.in("otc").emit("otc", data);
            } catch (e) {
                // TODO: handle exception
            }
        } else {
            //console.log("otc请求错误！");
        }
    });
}, 1000);

//开启监听
http.listen(process.env.npm_package_config_port, function () {
    console.log('listening on *:' + process.env.npm_package_config_port);
});
