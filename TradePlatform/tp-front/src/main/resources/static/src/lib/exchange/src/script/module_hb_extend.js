/**
 * Created by andyk on 2015/8/14.
 */
;(function(){
    if(!window.HRYB){
        window.HRYB = {};
    }
   var HurongbiExtend = function (option){
        var _option = option || {},
            _name   = _option.name,
            _value  = _option.value;
        if(HRYB[_name]){
            $.extend(HRYB[_name],_value)
        }else{
            HRYB[_name]={};
            $.extend(HRYB[_name],_value)
        }
    };

    "function" == typeof define ? define(function() {
        return HurongbiExtend
    }) : "undefined" != typeof exports ? module.exports = HurongbiExtend : window.HurongbiExtend = HurongbiExtend;
})();
