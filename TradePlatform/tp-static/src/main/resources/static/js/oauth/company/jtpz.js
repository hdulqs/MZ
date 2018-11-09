/**
 * AppUser.js
 */
define(['app'], function (app) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore){
        $rootScope.headTitle = $rootScope.title = "集团配置";
        
        	$http.get(HRY.modules.oauth+"company/subcompany/loadRoot").
                 success(function(data) {debugger
                 	 $scope.model = data;
                 	 $scope.formData = data;
            });
        	
			$scope.processForm = function() {
				//获取图片路径值
				$scope.formData.logoPath = $("#imgSrc").val();
				
				$http({
					method : 'POST',
					url : HRY.modules.oauth+'company/subcompany/saveRoot',
					data : $.param($scope.formData), 
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				})
				.success(function(data) {
							console.log(data);
							if (data.success) {
								growl.addInfoMessage('保存成功')
							} else {
								growl.addInfoMessage(data.msg)
							}
	
				 });
	
			};
        	
			//加载插件
			hryCore.initPlugins();
			//上传插件
			hryCore.uploadPicture();
			
    }
        
    return {controller:controller};
});