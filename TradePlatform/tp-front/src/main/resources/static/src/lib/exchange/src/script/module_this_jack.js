/**
 * Created by Jason on 2015/3/18.
 * hijack function
 * Jack(function,obj,args)
 */
;(function(){
    function Jack() {
        var a = [].slice.apply(arguments);
        if (a.length < 2) return;
        return a.shift().apply(a.shift(), a);
    }

    "function" == typeof define ? define(function() {
        return Jack
    }) : "undefined" != typeof exports ? module.exports = Jack : window.Jack = Jack;
})();
