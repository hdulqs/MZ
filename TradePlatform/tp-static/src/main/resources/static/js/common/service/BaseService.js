/**
 * BaseService.js
 * 使用eg:CoinService.js
 * url：请求url
 * params ：请求参数 {id:1,name:'yzc'...}
 * methods:请求方式 默认为defaults 中的参数 ，可覆盖 eg:{get:{method:'GET',isArray:true}}
 *        isArray:返回值是否只有一条数据
 */
define(['ngResource'], function () {
angular.module('BaseServiceModule',['ngResource']).factory( 'baseService',  ['$resource', function($resource,$q) {
         
	 return function( url, params, methods ) {
		    var defaults = {
			        query: {method:'POST',  isArray:true},
			        get:{method:'GET',isArray:false},
			        remove: {method:'DELETE'},
			        save:   {method:'POST'}
			      };
		    methods = angular.extend( defaults, methods );
		    var resource =  $resource(url, params, methods);
		    return resource;
		  };
    	}]);
})