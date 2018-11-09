/**
 * Created by Jason on 2015/3/18.
 */
;(function(){
   var BitCookie = {
	   'Cookie' : function(key, val, options) {
		   // 处理 key
		   var _cookie_pre = COOKIE_PRE || '';
		   key = _cookie_pre + key;
		   return this._Cookie(key, val, options);
	   },
       //设置Cookie
       '_Cookie' : function(key, val, options){
           var _key     = key,
               _val     = val;
           //设置
           if(_val !== undefined){
               var _date    = new Date();
               options && Math.round(_date.setDate(_date.getDate() +  options['expires']));
               return (document.cookie =  [
                   encodeURI(_key), '=', encodeURIComponent(_val),
                   options && options.expires ? '; expires=' + _date.toUTCString() : '',
                   options && options.path    ? '; path=' + options.path : '',
                   options && options.domain  ? '; domain=' + options.domain : '',
                   options && options.secure  ? '; secure' : ''
               ].join(''))
           }
           //读取
	       var _value = document.cookie.match('(?:^|;)\\s*' + _key + '=([^;]*)');
	       return (_value) ? decodeURIComponent(_value[1]) : null;
       },
       //删除Cookie
       'RemoveCookie' : function(key){
           if(this.Cookie(key) !== undefined){
               this.Cookie(key, '', {'expires' : -1});
               return true;
           }
           return false
       }
    };

    "function" == typeof define ? define(function() {
        return BitCookie
    }) : "undefined" != typeof exports ? module.exports = BitCookie : window.BitCookie = BitCookie
})();