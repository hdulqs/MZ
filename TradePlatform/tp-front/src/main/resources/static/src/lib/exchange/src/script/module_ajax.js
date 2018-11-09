/**
 * Created by Jason on 2015/6/4.
 * Hurongbi Ajax
 */
;(function(){
    var HbAjax = function(options,callback){
        var _op        = options || {},
            _url       = _op.url ? _op.url : '/account/ajax.do',
            _data      = _op.data || {},
            _init      = _op.init != undefined ? _op.init : true,
            _refresh   = _op.refresh ? _op.refresh*1 : 0,
            _type      = _op.type || 'GET',
            _datatype  = _op.datatype ? _op.datatype : '',
            _jsonp     = _op.jsonp ? _op.jsonp : '',
            _damp      = _op.damp!=undefined ? _op.damp : CONST['AJAX_DAMP'],
            _damp_time = CONST['AJAX_DAMP_TIME'] ? CONST['AJAX_DAMP_TIME'] : 6,
            _damp_refresh= CONST['AJAX_DAMP_REFRESH'] ? CONST['AJAX_DAMP_REFRESH'] : 5000,
            _jsonpc    = _op.jsonpcallback ? _op.jsonpcallback : '',
            _auto      = null,
            _stop      = 0,
            _this      = this,
            _location  = window.location,
            _vUrl      = '/account/account.do?a=validate_all&jump_url='+_location.pathname + _location.search;

        var __damp_time= _damp_time,
            __refresh  = _refresh;


        clearTimeout(_auto);
        _init && _data && Get();

        function Get(){
            Ajax();
            __refresh ? Refresh() : clearTimeout(_auto);
        }

        function Ajax(state){

            var _state  = typeof  state == 'undefined' ? _stop : state;
                _data.r = Math.random().toString(36).substr(2);
            if(_state){
                return
            }

            //console.log(__refresh,__damp_time,_this,new Date().getTime()/1000);
            $.ajax({
                url:_url,
                type: _type,
                data       : _data,
                dataType   : _datatype,
                jsonp      : _jsonp,
                jsonpCallback:_jsonpc,
                beforeSend : ABeforeSend,
                success    : ACallback,
                error      : AError,
                complete   : AComplete
            });
        }

        function Refresh(){
            _auto = setTimeout(Get, __refresh);
        }

        function ABeforeSend(){
            callback && callback({"code":"loading", "msg":"loading"})
        }

        function ACallback(data){
            if(data.flag == 10002){
                window.location = _vUrl;
                //console.warn('ajax 10002 验证校验码');
            }
            if(data.flag == 10003){
               // console.warn('Ajax Stop');
                _this.Stop();
            }

            _damp ? __damp_time -- : __damp_time = _damp_time;
            if(__damp_time <= 0 && __refresh <= (_damp_refresh-1000)){
               __refresh += 1000;
            }

            callback && callback({"code":"success",data:data})
        }

        function AError(){
            callback && callback({"code":"error"  , "msg":Lang.L('wangluoyichang')})
        }

        function AComplete(xhr, ts){
            callback && callback({"code":"complete", "xhr":xhr, "ts":ts, "msg":'complete'})
        }

        function Stop(){
            //console.log('AJAX stop');
            _stop = 1;
        }
        function Play(){
            //console.log('AJAX Play');
            _stop = 0;
            __damp_time = _damp_time;
            __refresh = _refresh;
        }

        function Fire(){
            Ajax(0);
            __damp_time = _damp_time;
            __refresh = _refresh;
        }

        function Update(options){
           var _op   = options || {};
            _url     = _op.url ? _op.url : _url;
            _data    = _op.data || _data;
            _refresh = _op.refresh ? _op.refresh*1 : _refresh;
            _datatype= _op.datatype ? _op.datatype : _datatype;
            _jsonp   = _op.jsonp ? _op.jsonp : _jsonp;
            _jsonpc  = _op.jsonpcallback ? _op.jsonpcallback : _jsonpc;
            __refresh = _refresh;
        }

        _this.Stop = Stop;
        _this.Play = Play;
        _this.Fire = Fire;
        _this.Update = Update;
    };

    "function" == typeof define ? define(function() {
        return HbAjax
    }) : "undefined" != typeof exports ? module.exports = HbAjax : window.HbAjax = HbAjax;

})();
