define(function(require, exports, module) {
	
	module.exports = {
		init : function(){
			var split = window.location.href.split("?");
			if(split.length>1){
				if(split[1].split('&').length>1){
					var msg = split[1].split('&')[1].split("=")[1];
					if(msg!='' || msg!=null){
						layer.msg("请先进行实名认证", {icon: 2});
					}
				}
			}
		}
	}
})