define(function(require,exports,module){
	require('js/setmenu.js')
	module.exports = {
		
		language : function(){
			var language = $("#language").val();
		    var yuyan = $("#language").val();
		    var yy = $("#language").val();
		    var yy1 = $("#language").val();
		    var tt = $("#language").val();
		    var tt1 = $("#language").val();
		    if(language == "" || language == null){
			    language = "简体中文";
		    }else if(language == "en"){
			    language = "English";
		    }else if(language == "zh_CN"){
			    language = "简体中文";
		    }else if(language == "tn"){
		    	language = "ไทย";
		    }
		    if(yy == "zh_CN"){
			    yy = "English";
			    yy1 = "en";
			    tt = "ไทย";
			    tt1 = "tn";
		    }else if(yy == "en"){
			    yy = "简体中文";
			    yy1 = "zh_CN";
			    tt = "ไทย";
			    tt1 = "tn";
		    }else if(yy == "tn"){
			    yy = "简体中文";
			    yy1 = "zh_CN";
			    tt = "English";
			    tt1 = "en";
		    }
		    $("#slide_lang dt span").html('<i class=\'icon_lang icon_lang_'+yuyan.toLowerCase()+'\'></i>');
//		    $("#slide_lang_box a").html('<i class=\'icon_lang icon_lang_'+yy1.toLowerCase()+'\'></i>'+yy);
		
		    $("#slide_lang_box").html('<a id=\'tt1\'>'+'<i class=\'icon_lang icon_lang_'+yy1.toLowerCase()+'\'></i>'+yy+'</a>'+'<a  id=\'tt2\'><i class=\'icon_lang icon_lang_'+tt1.toLowerCase()+'\'></i>'+tt+'</a>');
		    $("#slide_lang dt").on('click',function() {
		    	//获取下当前url地址
		    	var url = '';
		    	if(location.href.split('?').length>1){
		    		url = "?" +location.href.split('?')[1];
		    	}
		    	var split = window.document.location.pathname+url;
		    	
		        $("#slide_lang_box").slideToggle()
		        $(this).toggleClass('cur');
		        if($("#tokenId").val()!=""){
		        	if($("#language").val() == "zh_CN"){
					    $("#slide_lang_box a[id='tt1']").attr("href",_ctx + "/language.do?language=en&split="+split+"&tokenId="+$("#tokenId").val());
					    $("#slide_lang_box a[id='tt2']").attr("href",_ctx + "/language.do?language=tn&split="+split+"&tokenId="+$("#tokenId").val());
				    }else if($("#language").val() == "en"){
					    $("#slide_lang_box a[id='tt1']").attr("href",_ctx + "/language.do?language=zh_CN&split="+split+"&tokenId="+$("#tokenId").val());
					    $("#slide_lang_box a[id='tt2']").attr("href",_ctx + "/language.do?language=tn&split="+split+"&tokenId="+$("#tokenId").val());
				    }else if($("#language").val() == "tn"){
					    $("#slide_lang_box a[id='tt1']").attr("href",_ctx + "/language.do?language=zh_CN&split="+split+"&tokenId="+$("#tokenId").val());
					    $("#slide_lang_box a[id='tt2']").attr("href",_ctx + "/language.do?language=en&split="+split+"&tokenId="+$("#tokenId").val());
				    }
		        }else{
		        	if($("#language").val() == "zh_CN"){
		        		$("#slide_lang_box a[id='tt1']").attr("href",_ctx + "/language.do?language=en&split="+split);
		        		$("#slide_lang_box a[id='tt2']").attr("href",_ctx + "/language.do?language=tn&split="+split);
		        	}else if($("#language").val() == "en"){
		        		$("#slide_lang_box a[id='tt1']").attr("href",_ctx + "/language.do?language=zh_CN&split="+split);
		        		$("#slide_lang_box a[id='tt2']").attr("href",_ctx + "/language.do?language=tn&split="+split);
		        	}else if($("#language").val() == "tn"){
		        		$("#slide_lang_box a[id='tt1']").attr("href",_ctx + "/language.do?language=en&split="+split);
		        		$("#slide_lang_box a[id='tt2']").attr("href",_ctx + "/language.do?language=zh_CN&split="+split);
		        	}
		        }
	        })
	        $("#slide_lang_box a").on('click',function() {
	    	   var slidecon=$("#slide_lang dt span").html();
	           //$("#slide_lang dt span").html('<i class=\'icon_lang icon_lang_'+yy1.toLowerCase()+'\'></i>'+yy);
	           $('#slide_lang_box a').html(slidecon)
	           $("#slide_lang dd").hide()
		    })
		}
	}
})