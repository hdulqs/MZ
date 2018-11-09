/**
 * coinService 虚拟币业务数据处理
 */
define(['js/common/service/BaseService'], function () {
angular.module('coinserviceModule',['BaseServiceModule']).service( 'coinService',function($http,$q,baseService) {
          
	       var service = {};
	       /**
	        * 查询用户持有币种list
	        */
           service.findListByUser= function (user) {
        	   return  baseService(HRY.modules.exmain+"account/find",{id:user.id}).query().$promise;
		    }
           
    	   return service;
    	});
})