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
			var c2crule=$('input:radio[name="c2crule"]:checked').val();
			if(!c2crule){
				layer.msg("请选择匹配规则！ ")
			}
			
			var businessmanType = $("#businessmanType").val();
			var businessmanType2 = $("#businessmanType2").val();
			$.ajax({
				type : "post",
				url :  HRY.modules.customer + 'businessman/appbusinessman/changerule',
				cache : false,
				dataType : "json",
				data : {
					c2crule :c2crule,
					businessmanType : businessmanType,
					businessmanType2 : businessmanType2
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
			url :  HRY.modules.customer + 'businessman/appbusinessman/loadchangerule',
			cache : false,
			dataType : "json",
			success : function(data) {
				if(data){
					if(data.success){
						if(data.obj!=undefined&&data.obj.c2crule!=undefined){
							$('input:radio[name="c2crule"][value='+ data.obj.c2crule +']').attr("checked",data.obj.c2crule);
						}
						if(data.obj!=undefined&&data.obj.businessmanType!=undefined){
							hryCore.RenderSelect($("#businessmanType"),data.obj.businessmanType);
						}
						if(data.obj!=undefined&&data.obj.businessmanType2!=undefined){
							hryCore.RenderSelect($("#businessmanType2"),data.obj.businessmanType2);
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