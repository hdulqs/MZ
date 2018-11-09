 /**
 * Copyright:   北京互融时代软件有限公司
 * C2cTransaction.js
 * @author:      liushilei
 * @version:     V1.0 
 * @Date:        2017-12-07 14:06:38 
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
	function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {
		
		
		
		$scope.processForm = function(){
			var c2cTimeOut=$('#c2cTimeOut').val();
			if(!c2cTimeOut){
				layer.msg("请配置交易超时时间！ ")
			}
			
			var c2cTimeOut = $("#c2cTimeOut").val();
			$.ajax({
				type : "post",
				url :  HRY.modules.customer + 'businessman/c2ccoin/settimeout',
				cache : false,
				dataType : "json",
				data : {
					c2cTimeOut :c2cTimeOut,
				},
				success : function(data) {
					if(data){
						if(data.success){
							layer.msg("保存成功");
						}
					}
				},
				error : function(e) {
					$("#content").html("<div class='row'><h1>此路径不存在："+url.substring(url.indexOf("u=")+2)+"</h1></div>");
				}
			});
		}
		
		
		$.ajax({
			type : "post",
			url :  HRY.modules.customer + 'businessman/c2ccoin/gettimeout',
			cache : false,
			dataType : "json",
			success : function(data) {
				debugger
				if(data){
					if(data.success){
						if(data.obj!=undefined&&data.obj!=undefined){
							$("#c2cTimeOut").val(data.obj);
						}
					}
				}
			},
			error : function(e) {
				$("#content").html("<div class='row'><h1>此路径不存在："+url.substring(url.indexOf("u=")+2)+"</h1></div>");
			}
		});
		

		// 加载插件
		hryCore.initPlugins();
		// 上传插件
		hryCore.uploadPicture();
	}
	
	// -----------controller.js加载完毕--------------
	return {
		controller : controller
	};
});