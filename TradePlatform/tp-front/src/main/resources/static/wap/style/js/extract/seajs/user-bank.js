define(function(require, exports, module) {
	require("style/css/mobile/css/css.css");
	require("style/js/mobile/js/zepto.js");
	require("style/js/mobile/js/public.js");
	require("style/js/layer/css/layer.css");
	require("style/js/form.js");

	this.md5 = require("style/js/hrymd5")
	this.validate = require("style/js/validate");

	require("style/js/extract/common/user-bank.js");

	module.exports = {
			
		// 初始化方法
		init : function() {
			$("#Return").click(function() {
				
			
			})
		}

	}

})