/**
 * exchangeHome.js
 */
define(['app','layer'], function (app,layer) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$state','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,$state, hryCore){
    	
    	
    	
		// 删除方法
		$scope.fnRemove = function(ids) {
			 
			// 提示框
			layer.confirm('你确定要删除吗？', {
				btn : [ '确定', '取消' ], // 按钮
				ids : ids
			}, function() {
				hryCore.CURD({
					url : HRY.modules.oauth + "company/apporganization/remove" + "/" + this.ids
				}).remove(null, function(data) {
					if (data.success) {
						// 提示信息
						layer.msg('删除成功', {
							icon : 1,
							time : 2000
						// 2秒关闭（如果不配置，默认是3秒）
						});
						$state.go("oauth.org.user", {
							page : "list",
							orgid : 0,
							pid : "anon"
						}, {
							reload : true
						});

					} else {
						layer.msg(data.msg, {
							icon : 1,
							time : 2000
						// 2秒关闭（如果不配置，默认是3秒）
						});
						layer.close();
					}
				}, function(data) {
					growl.addInfoMessage("error:" + data.msg);
				});
			});

		}
    	
    }
    return {controller:controller};
});