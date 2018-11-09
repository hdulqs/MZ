/**
 * Created by Jason on 2015/6/1.
 */
define(function (require, exports, module) {
    var $ = require('jquery'),
        A = require('./module_ajax');
    



    //校验用户是否登录系统
    function GetOauth(options){/*
        var op   = options || {},
            Fn;
         
            	
            Fn =  new A({"url":HRY.host + HRY.modules.oauth + 'getUser'},function(data){
            	if(data!=null&&data!=undefined&&data.code=="success"){
            		if(data.data!=null&&data.data!=undefined&&JSON.parse(data.data).id=="0"){
            			window.location.href=HRY.host+HRY.maintain;
                		return;
            		}
            	}
            	var _code = data.code;
               
                switch (_code){
                   
                    case 'error':
                        break;
                    case 'success':
                       if(data.data!=undefined&&data.data!=""){
                    	   HRYB.user=JSON.parse(data.data);
                    	   $("#order").removeClass("hide").addClass("current");
                    	   $("#trade").removeClass("hide").addClass("current");
                    	   $(".head_login").html(''+Lang.L("Hello")+' ， <a href="'+HRY.host+HRY.modules.exstatic+'#/user/base"> '+HRYB.user.userName+' </a>  <a href="'+HRY.host+HRY.modules.oauth+'logout.do?backUrl='+HRY.host+'/market.html" id="logout" class="p_l_10 p_r_10">'+Lang.L("Logout")+'</a>');
                     
                       }
                }
            });

        return Fn;
    */}

    exports.GetOauth    = GetOauth;


});


