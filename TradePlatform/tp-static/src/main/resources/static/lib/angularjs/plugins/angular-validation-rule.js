(function() {
  angular
    .module('validation.rule', ['validation'])
    .config(['$validationProvider', function($validationProvider) {
      var expression = {
        required: function(value) {
          return !!value;
        },
        url: /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?$/,
        email: /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/,
        number: /^\d+$/,
        password: /^(?!\D+$)(?![^a-zA-Z]+$)\S{6,20}$/,
        chinese: /^[\u0391-\uFFE5]+$/,
        double: /^\d+(?:\.\d{2})?$/,
        float:/^[+|-]?\d*\.?\d*$/,
        nonnegative: /^\d+(\.\d+)?$/,
        natural: /^[0-9]*[1-9][0-9]*$/,
        integer: /^-?\d+$/,
        ip: /(\d+)\.(\d+)\.(\d+)\.(\d+)/g,
        cnen: /^[A-Za-z0-9]+$/,
        eng: /^[A-Za-z]+$/,
        minlength: function(value, scope, element, attrs, param) {
          return value.length >= param;
        },
        maxlength: function(value, scope, element, attrs, param) {
          return value.length <= param;
        },
        min: function(value, scope, element, attrs, param) {
          return parseFloat(value) >=parseFloat(param);
        },
        max: function(value, scope, element, attrs, param) {
            return parseFloat(value) <= parseFloat(param);
          }
        ,
        min1: function(value, scope, element, attrs, param) {
        	var param1=$("."+param).html();
        	param1=param1.replace(/,/g,"");
          return parseFloat(value) >=parseFloat(scope.param1);
        },
        max1: function(value, scope, element, attrs, param) {
        	var param1=$("."+param).html();
        	param1=param1.replace(/,/g,"");
            return parseFloat(value) <= parseFloat(param1);
          }
      };

      var defaultMsg = {
        required: {
          error: 'This should be Required!!',
          success: ''
        },
        url: {
          error: 'This should be Url',
          success: ''
        },
        email: {
          error: 'This should be Email',
          success: ''
        },
        number: {
          error: 'This should be Number',
          success: ''
        },
        password: {
            error: 'This should be password',
            success: ''
          },
        chinese: {
          error: 'This should be password',
          success: ''
          },
        double: {
          error: '请输入金额',
          success: ''
          },
        float: {
              error: '格式错误',
              success: ''
         },
          
        natural: {
              error: '请输入金额',
              success: ''
          },
        integer: {
	          error: '请输入金额',
	          success: ''
	      },
        ip: {
	          error: '请输入ip',
	          success: ''
	      },
	      cnen: {
	          error: '请输入ip',
	          success: ''
	      },
        nonnegative: {
              error: '请输入金额',
              success: ''
          }, 
          eng: {
	          error: '必须为纯英文',
	          success: ''
	      },
        minlength: {
          error: 'This should be longer',
          success: ''
        },
        maxlength: {
          error: 'This should be shorter',
          success: ''
        },
        min: {
            error: 'This should be longer',
            success: ''
          },
        max: {
            error: 'This should be shorter',
            success: ''
          },
          min1: {
              error: 'This should be longer',
              success: ''
            },
          max1: {
              error: 'This should be shorter',
              success: ''
            }
      };
      $validationProvider.setExpression(expression).setDefaultMsg(defaultMsg);
    }]);
}).call(this);
