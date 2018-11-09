var data = seajs.data,
doc = document;
seajs.Module.preload = function(a) {
    var e = data.preload,
    s = e.length;
    s ? seajs.Module.use(e,
    function() {
        e.splice(0, s),
        seajs.Module.preload(a)
    },
    data.cwd + "_preload_" + data.cid()) : a()
},
seajs.use = function(a, e) {
    return seajs.Module.preload(function() {
        seajs.Module.use(a, e, data.cwd + "_use_" + data.cid())
    }),
    seajs
},
data.preload = function() {
    var a = [],
    e = location.search.replace(/(seajs-\w+)(&|$)/g, "$1=1$2");
    return e += " " + doc.cookie,
    e.replace(/(seajs-\w+)=1/g,
    function(e, s) {
        a.push(s)
    }),
    a
} ();

seajs.config({
    alias: {
        "jquery": "dist/jquery",
        "es5-safe": "dist/es5-safe",
        "json": "dist/json2",
        "lang": "dist/lang",
        "zepto": "dist/zepto",
        "Z": "dist/zepto"
    },
    paths: {
        "dev": "satic/js/exchange/src/script",
        "dist": "static/js/exchange/dist/script"
    },
    preload: [Function.prototype.bind ? "": "es5-safe", this.JSON ? "": "json", !!window.L ? "": "lang"],
    debug: false,
    base: HTTP_STATIC_DOMAIN,
    map: [[/^(.*\.(?:css|js))(?:.*)$/i, '$1?' + HBVERSION]],
    charset: "utf-8"
});