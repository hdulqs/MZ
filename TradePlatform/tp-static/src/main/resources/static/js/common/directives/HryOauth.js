/**
 * 权限校验指令
 * <div has-permission='articleEdit'>articleEdit</div>
 */
angular.module('hry-oauth', ['oauthserviceModule']).directive('hasPermission', function(oauthService) {
	  return {
		    link: function(scope, element, attrs) {
		      if(!angular.isString(attrs.hasPermission))
		        throw "hasPermission value must be a string";
		 
		      var value = attrs.hasPermission.trim();
		      var notPermissionFlag = value[0] === '!';
		      if(notPermissionFlag) {
		        value = value.slice(1).trim();
		      }
		 
		      function toggleVisibilityBasedOnPermission() {
		        var hasPermission = oauthService.hasPermission(value);
		 
		        if(hasPermission && !notPermissionFlag || !hasPermission && notPermissionFlag)
		          element.show();
		        else
		          element.hide();
		      }
		      toggleVisibilityBasedOnPermission();
		      scope.$on('permissionsChanged', toggleVisibilityBasedOnPermission);
		    }
		  };
		})
		
	.directive('userIslogin', function(oauthService) {
	  return {
		    link: function(scope, element, attrs) {
		      function toggleVisibilityBasedOnPermission() {
		        var hasPermission = oauthService.userIsLogin();
		 
		        if(hasPermission )
		          element.show();
		        else
		          element.hide();
		      }
		      toggleVisibilityBasedOnPermission();
		      scope.$on('permissionsChanged', toggleVisibilityBasedOnPermission);
		    }
		  };
		});
  


