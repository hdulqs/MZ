/**
 * 权限校验
 */
define(['js/common/service/BaseService'], function () {
angular.module('oauthserviceModule',['BaseServiceModule']).service( 'oauthService',function($rootScope,baseService) {
          
	var service = {};
           //用户权限字符串 eg:articleAdd,articleEdit,....
           var userPermissionList="";
           //用户信息
           var user={};
           /**
            * 权限校验
            */
           service.setPermissions= function(permissions) {
               userPermissionList = permissions;
               $rootScope.$broadcast('permissionsChanged')
             },
             /**
              * 用户信息
              */
             service.setUser= function(userInfo) {
            	 if(userInfo!=undefined) 
            	 {
            		 user = userInfo;
                	 $rootScope.user = user;
            	 }
            	
                 $rootScope.$broadcast('permissionsChanged')
               },
             /**
              * 校验按钮是否显示
              */
             service. hasPermission= function (permission) {
               if(userPermissionList.indexOf(permission.trim()) > -1){
                 return true;
               }else{
                 return false;
               }
             }
             
             /**
              * 校验用户是否登录
              */
             service. userIsLogin= function () {
               if(user.userName!=undefined){
                 return true;
               }else{
                 return false;
               }
             }
    	   return service;
    	});
})