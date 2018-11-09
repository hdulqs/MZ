define(['angular'], function (angular) {
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams){

            $rootScope.headTitle = $rootScope.title = "保理项目详情";
            //获取参数
            $scope.projId = $stateParams.id;
            $scope.projName = $stateParams.name;
            //测试数据
            //$scope.user={name:'yzc',id:'1'}
            seeDeatil(HRY.modules.factoring+'project/tsysuser/load/'+$stateParams.id);
            
            function seeDeatil(url){
            	 $http.get(url).
                 success(function(data) {
                 	 $scope.tSysUser = data;
                 });
            }

    }
 return {controller:controller};
})