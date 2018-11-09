/**
 * Created by andyk on 2015/11/12.
 * 闪电手行情深度数据
 */
define(function (require) {
    var TMP       = require('./module_tmpl'),
        IO        = require('./module_socket_io'),
        CONN      = require('./module_connect'),
        mNum = require('./module_number'),    //数字处理
        API       = require('./module_socket_api'),
        mAcc      = require('./module_accurate'),
        DOM       = $(document);

    /*****行情*****/
    /**
     * Api切换
     * @param api
     * @param back
     * @param callback
     * @returns api
     * @constructor
     */
    function ApiSelect(api,back,callback){
        var _api = api || '';
        if(!_api) return ;
        connect.msg(_api.msg);
        back && connect.UnMsg(back.msg);
        connect.reg(_api.msgType,function(data){
            callback && callback(data)
        });
        return _api;
    }

    /**
     * 渲染页面数据
     * @param dom ID
     * @param data
     */
    function DataView(dom,data){
        var _view = document.getElementById(dom) || document.getElementById('view_'+dom);
        _view.innerHTML = TMP('tmp_'+dom,data);
    }
    
    /*行情配置*/
    var connect = CONN($_ws_hq_url),
        api_detail = API(1)['marketDetail0'](SYMBOL),
        api_detail_back,
        price_back;


    /*注册深度数据*/
    api_detail_back = ApiSelect(api_detail,'',function(data){
        DOM.trigger('__MarketData');
        DataProcess(data);
    });

    /*深度切换事件*/
    $('#depth_select').on('click','span',function(){
        var _this  = $(this),
            _op    = _this.data('option');
        api_detail = API(1)['marketDetail'+_op](SYMBOL);
        api_detail_back = ApiSelect(api_detail,api_detail_back,function(data){
            DataProcess(data);
        });
        _this.addClass('cur').siblings('span').removeClass('cur');
    });

    /*title行情*/
    var _oldTitle;
    function TitleNow(price){
//         var _now;
//         if(HRYB.GLOBAL.appConfigData!=undefined){
//         	_oldTitle=HRYB.GLOBAL.appConfigData.siteName;
//         }else{
//         	_oldTitle = "hurong"
//         }
//     	_now = CURRENCY_STR == 'cny' ? "￥" + price : '$' + price;
//        $(document).attr("title", _now+' | '+_oldTitle);
    }
    /*数据加工*/
    var _overview = [];/*Open,High,Low,Last,New,totalAmount,gains*/
    function DataProcess(data){
        var _data     = data.payload,
            _asks     = [],    /*price,amount 卖*/
            _bids     = [],    /*price,amount 买*/
            _trades   = [],    /*time,price,amount*/
            _temp     = [0,0]; /*ask,big*/
        var bl = 0,al=0;
        if( _data.asks.price!=undefined&& _data.asks.price!=null){
        	al = _data.asks.price.length;
        }
        if( _data.bids.price!=undefined&& _data.bids.price!=null){
        	bl = _data.bids.price.length;
        }
        try {
	        for ( var i = 0, leg = Math.max(bl, al); i < leg; i++) {
	            _temp[0]  = mAcc.Add(_temp[0],_data.asks.amount==undefined?0:_data.asks.amount[i]);
	            _temp[1]  = mAcc.Add(_temp[1],_data.bids.amount==undefined?0:_data.bids.amount[i]);
	            try {
	            	_data.asks.price[i] && _asks.push([_data.asks.price==undefined?0:_data.asks.price[i],_data.asks.amount==undefined?0:_data.asks.amount[i], _temp[0]]);
	            } catch (e) { 
				}
	            try {
	            	_data.bids.price[i] && _bids.push([_data.bids.price==undefined?0:_data.bids.price[i],_data.bids.amount==undefined?0:_data.bids.amount[i], _temp[1]]);
	            } catch (e) {
				}
	        }
        } catch (e) {
		}
        
        //获取当天的年月日
//        var t_today=new Date();
//        var today_stamp=t_today.toLocaleDateString();
//        //临时时间，循环
//        var time_stamp='';
//        try {
//	        for( var l = 0, legs = _data.trades.time.length; l < legs; l++){
//	        	if(_data.trades.time[l]){
//	        		t_today.setTime(_data.trades.time[l] * 1000);
//	        		time_stamp=t_today.toLocaleDateString();
//	        		today_stamp==time_stamp&&_trades.push([_data.trades.time[l],_data.trades.price[l],_data.trades.amount[l],_data.trades.direction[l]]);
//	        	}
//	        }
//        } catch (e) {
//		}
        
        try {
        	for( var l = 0, legs = _data.trades.time.length; l < legs; l++){
	            _data.trades.time[l] && _trades.push([_data.trades.time[l],_data.trades.price[l],_data.trades.amount[l],_data.trades.direction[l]]);
	        }
		} catch (e) {
		}
	        
     
            _overview[0]=_data.priceOpen;
            _overview[1]=_data.priceHigh;
            _overview[2]=_data.priceLow;
            _overview[3]=_data.priceLast;
            _overview[4]=_data.priceNew;
            _overview[5]=_data.totalAmount;
            //console.log(data.payload.yestdayPriceLast)
            if(_data.priceNew==0||data.payload.yestdayPriceLast==0){
            	_overview[6] = 0;
            }else{
            	_overview[6]=((_data.priceNew - data.payload.yestdayPriceLast)/data.payload.yestdayPriceLast*100).toFixed(3);
            }


        if(price_back){

            if(price_back == _data.priceNew){
                _overview[7] = _overview[7]
            }else if(price_back > _data.priceNew){
                _overview[7] = 'font_down'
            }else{
                _overview[7] = 'font_up'
            }

        }
        _overview[8]=(_data.usdtToRmb*_data.usdtCount).toFixed(2);
        console.log(_overview[8]);
        
        price_back = _data.priceNew;


        DataView('depth_ask',_asks);
        DataView('depth_bid',_bids);
        DataView('trades',_trades);
        DataView('overview',_overview);
        DataView('new_price',_overview);
        TitleNow( _overview[4] )

    }
});