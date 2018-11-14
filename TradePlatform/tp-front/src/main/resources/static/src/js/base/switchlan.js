define(function(require,exports,module){
	require('js/setmenu.js')
	module.exports = {
		
		language : function(){
            //获取下当前url地址
            var url = '';
            if(location.href.split('?').length>1){
                url = "?" +location.href.split('?')[1];
            }
            var split = window.document.location.pathname+url;

			$(".slide_lang_box").eq(0).find('a').attr("href",_ctx + "?lang=zh_CN&split="+split);
            $(".slide_lang_box").eq(1).find('a').attr("href",_ctx + "?lang=tn&split="+split);
            $(".slide_lang_box").eq(2).find('a').attr("href",_ctx + "?lang=en&split="+split);

            var language = $("#language").val();
            if(language == 'zh_CN'){
                $(".zh_CN").addClass("active");
            }
            if(language == 'tn'){
                $(".tn").addClass("active");
            }
            if(language == 'en'){
                $(".en").addClass("active");
            }
		}
	}
})