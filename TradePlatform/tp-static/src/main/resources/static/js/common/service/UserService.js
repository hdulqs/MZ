/**
 * UserService 虚拟币业务数据处理
 */
define(['js/common/service/BaseService'], function () {
  angular.module('userserviceModule', ['BaseServiceModule']).service(
      'userService', function ($http, $q, baseService) {
        var service = {};

        /**
         * 权限校验
         */
        service.aouth = function () {
          return baseService(
             HRY.modules.oauth + 'getUser').get().$promise;
        }

        /**
         * 用户登录
         */
        service.login = function (params) {
          return baseService(
              HRY.modules.oauth + "loginService.do?"
              + (new Date()).getTime(), params,
              {query: {method: 'POST', isArray: false}}).query().$promise;
        }
        return service;
      });
})