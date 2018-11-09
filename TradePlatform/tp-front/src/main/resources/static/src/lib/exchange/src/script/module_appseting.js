/**
 * Created by Jason on 2015/6/1.
 */
define(function (require, exports, module) {

    var $ = require('jquery'),
        A = require('./module_ajax');
      
    //加载系统配置信息
    function GetAppConfigData(options){/*
        var op   = options || {},
            Fn;
            Fn =  new A({"url":HRY.host + HRY.modules.exmain + 'config/find/baseConfig',datatype:'json'},function(data){
            	var _code = data.code;
               
                switch (_code){
                   
                    case 'error':
                    	// error
                        break;
                    case 'success':
                    	 
                       if(data!=undefined&&data!=""){
                    	  var  appData=JSON.parse(data.data.obj);
                    	   $("#logo_img").attr("src",HRY.host+"\\"+appData.siteLogo);
                    	   $("#copyRight").html(appData.siteCopyright);
                    	   HRYB.GLOBAL.appConfigData=appData;
                    	}
                        	
                }
            });

        return Fn;
    */}




    exports.GetAppConfigData    = GetAppConfigData;


});


