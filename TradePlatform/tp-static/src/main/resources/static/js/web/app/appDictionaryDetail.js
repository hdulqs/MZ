define(['app'], function(app){
   return app.controller('appDictionaryDetail', ['$scope','$rootScope','$http', '$stateParams', function ($scope,$rootScope,$http, $stateParams) {

            $rootScope.headTitle = $rootScope.title = "保理项目详情";
            //获取参数
            $scope.projId = $stateParams.id;
            $scope.projName = $stateParams.name;
            //测试数据
            //$scope.user={name:'yzc',id:'1'}
            seeDeatil(HRY.modules.web+'app/appdctionary/load/'+$stateParams.id);
            
            function seeDeatil(url){
            	 $http.get(url).
                 success(function(data) {
                 	 $scope.appConfig = data;
                 });
            }

    }])

})