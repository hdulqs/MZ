/**
 * 编辑器插件
 * 使用方法
 * <textarea ckeditor name="content"  ng-model="formData.content"></textarea>
 */
define(['app','ckeditor'], function (app) {
	app.directive('ckeditor', function() {
	    return {
	        require : '?ngModel',
	        template:'',
	        link : function(scope, element, attrs, ngModel) {
	            var ckeditor = CKEDITOR.replace(element[0], {
	            	 toolbar : 'Full'
	            });
	           // CKFinder.setupCKEditor( ckeditor, HRY.staticUrl+'/static/lib/ckfinder/' );
	            if (!ngModel) {
	                return;
	            }
	            ckeditor.on('instanceReady', function() {
	                ckeditor.setData(ngModel.$viewValue);
	            });
	            ckeditor.on('pasteState', function() {
	                scope.$apply(function() {
	                    ngModel.$setViewValue(ckeditor.getData());
	                });
	            });
	            ngModel.$render = function(value) {
	                ckeditor.setData(ngModel.$viewValue);
	            };
	        }
	    };
	});

})
