define(function(require,exports,module){
	var base = require("base");
	module.exports = {
		//静态菜单
		loadstaticmenu : function(){
			/**
			 * 监听菜单点击事件
			 * 2016-12-28
			 */
			$("#sidebar").find("li a").on("click", function(o) {
				var src = $(this).attr("src");
				if (src != undefined) {
					base.loadUrl(src);
				}
			});
			$("#sidebar").find("h3 a").on("click", function(o) {
				var src = $(this).attr("src");
				if (src != undefined) {
					base.loadUrl(src);
				}
			});
			
		}
	}
	
	
	
})