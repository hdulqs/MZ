/**
 * Created by Jason on 2015/3/18.
 * Simple JavaScript Templating
 * John Resig - http://ejohn.org/ - MIT Licensed
 */
;(function(){
    var TempCache  = {};
     function TmpL(str, data){
        var fn = !/\W/.test(str) ?  TempCache[str] = TempCache[str] || TmpL(document.getElementById(str).innerHTML) :
            new Function("obj", "var p=[],print=function(){p.push.apply(p,arguments);},__data = obj;" +
                "with(obj){p.push('" +
                str .replace(/[\r\t\n]/g, " ")
                    .split("<%").join("\t")
                    .replace(/((^|%>)[^\t]*)'/g, "$1\r")
                    .replace(/\t=(.*?)%>/g, "',$1,'")
                    .split("\t").join("');")
                    .split("%>").join("p.push('")
                    .split("\r").join("\\'")
                + "');}return p.join('');");
        return data ? fn( data ) : fn;
    }

    "function" == typeof define ? define(function() {
        return TmpL
    }) : "undefined" != typeof exports ? module.exports = TmpL : window.TmpL = TmpL;

})();