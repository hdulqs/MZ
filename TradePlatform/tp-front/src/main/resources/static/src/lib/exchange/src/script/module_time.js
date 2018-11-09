/**
 * Created by andyk on 2015/11/9.
 */

/**
 *
 */
;(function(){
    function unixToTime(unix,format){
        var _unix    = new Date(unix*1000),
            _format = format || 'YYYY-MM-DD';

        Date.prototype.Format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1,
                "D+": this.getDate(),
                "h+": this.getHours(),
                "m+": this.getMinutes(),
                "s+": this.getSeconds(),
                "q+": Math.floor((this.getMonth() + 3) / 3),
                "S": this.getMilliseconds()
            };
            if (/(Y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));

            return fmt;
        };
        return _unix.Format(_format);
    }

    "function" == typeof define ? define(function() {
        return unixToTime
    }) : "undefined" != typeof exports ? module.exports = unixToTime : window.unixToTime = unixToTime;
}());