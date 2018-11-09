/**
 * 互融app应用 Main
 * oauth:Yzc
 */

var growl;

define(['angular', 'layer', 'ngRoute', 'uiRoute', 'ngSanitize', 'ngResource',
  'anguarGrowl', 'angularAnimate', 'angular-validation',
  'angular-validation-rule', 'ocLazyLoad', 'hryFramework', 'angularMaterialize',
  'js/common/service/OauthService', 'js/common/service/UserService',
  'loginTimeOut'], function (angular, layer) {
  var app = angular.module('HryApp',
      ['ngRoute', 'ui.router', 'ngSanitize', 'ngResource', 'angular-growl',
        'ngAnimate', 'validation', 'validation.rule', 'oc.lazyLoad',
        'hry-framework', 'ui.materialize', 'oauthserviceModule',
        'userserviceModule', 'ng.ueditor'])

  var userInfo;

  //html 过滤
  app.filter('trustHtml', function ($sce) {
    return function (input) {
      return $sce.trustAsHtml(input);
    }
  });

  app.factory('UserInterceptor',
      ["$q", "$rootScope", function ($q, $rootScope) {
        return {
          request: function (config) {
            //config.headers["TOKEN"] = $rootScope.user.id;
            return config;
          },
          response: function (response) {
            if (typeof response.data == "string") {
              if (response.data.indexOf("login_html") > 0) {
                layer.msg('登录超时,2秒后退出系统！', {icon: 2, time: 2000}, function () {
                  window.location.href = HRY.modules.mstatic + "/login.html";
                });
              }
            }
            return response;
          },
          requestError: function (rejectReason) {

          },
          responseError: function (response) {/*
	            var data = response.data;
				// 判断错误码，如果是未登录
	            if(data["errorCode"] == "500999"){
					// 清空用户本地token存储的信息，如果
	                $rootScope.user = {token:""};
					// 全局事件，方便其他view获取该事件，并给以相应的提示或处理
	                $rootScope.$emit("userIntercepted","notLogin",response);
	            }
				// 如果是登录超时
				if(data["errorCode"] == "500998"){
	                $rootScope.$emit("userIntercepted","sessionOut",response);
	            }
	            return $q.reject(response);
	        */
          }
        };
      }]);

  //启动初始化相关数据
  app.run(
      ["$rootScope", "oauthService", "$ocLazyLoad", "$location", "userService",
        'growl', '$injector', "$templateCache", "$timeout",
        function ($rootScope, oauthService, $ocLazyLoad, $location, userService,
            nggrowl, $injector, $templateCache, $timeout) {

          //全局提示框
          growl = nggrowl;
          $rootScope.loading = false;//载入状态控制
          $rootScope.HRY = HRY;
          require(['_hry', '_con']);

          //加载权限指令
          $ocLazyLoad.load('js/common/directives/HryOauth');
          //设置用户信息
          oauthService.setUser(userInfo);

          //监听路由变化加入权限
          $rootScope.$on('$stateChangeStart', function (event, next, current) {

            if (typeof(next) != undefined) {
              $templateCache.remove(next.templateUrl);
            }

            //loading 效果设置监听
            $rootScope.loading = true;
            $("body").addClass("app-content-loading");
            if (oauthService.userIsLogin()) {
              //校验权限
              $window.location.href =  "/admin/login.html";
            }
          });

          //全局表单校验
          var $validationProvider = $injector.get('$validation');
          $rootScope.form = {
            checkValid: $validationProvider.checkValid,
            submit: function (form, success, err) {
              var formName = form.$name;
              var buttonval = "loading....";
              var button = $(
                  "form[name=" + formName + "] button[type='submit']");
              var btnval = button.attr("data-loading-text");
              var btnhtml = button.html();
              if (btnval != undefined && btnval != "") {
                buttonval = btnval;
              }
              button.html(buttonval).attr("disabled", "disabled");
              $validationProvider.validate(form)
              .success(success)
              .error(function () {
              });

              //定时器移除 按钮 不可点击事件
              var timer = $timeout(
                  function () {
                    button.html(btnhtml).removeAttr("disabled");
                  },
                  1000
              );

              //将resolve/reject处理函数绑定到timer promise上以确保我们的cancel方法能正常运行
              timer.then(
                  function () {
                    $timeout.cancel(timer);
                    console.log("Timer resolved!", Date.now());
                  }
              );

            }
          };

          $rootScope.$on('$stateChangeSuccess', function (element) {

            $rootScope.loading = false;
            $("body").removeClass("app-content-loading");

            //定时器控制导航选中
            var timer = $timeout(
                function () {
                  var URI = decodeURIComponent(window.location.hash);
                  var Nav = $("#menuHTML a");
                  Nav.each(function () {
                    if ($(this).attr("href") == URI) {
                      //移除激活
                      $("#menuHTML li").removeClass("active");
                      //激活当前选中
                      $(this).parent().addClass("active");

                      //打开当前折叠
                      $(this).parent().parent().parent().addClass("open");
                    }
                  });
                  console.log("Timeout executed", Date.now());
                },

                500
            );

            //将resolve/reject处理函数绑定到timer promise上以确保我们的cancel方法能正常运行
            timer.then(
                function () {
                  $timeout.cancel(timer);
                  console.log("Timer resolved!", Date.now());
                }
            );

            $rootScope._simpleConfig = {
              //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
              toolbars: [[
                'fullscreen', 'source', '|', 'undo', 'redo', '|',
                'bold', 'italic', 'underline', 'fontborder', 'strikethrough',
                'superscript', 'subscript', '|', 'forecolor', 'backcolor',
                'insertorderedlist', 'insertunorderedlist', 'selectall',
                'cleardoc', '|',
                'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',

                'justifyleft', 'justifycenter', 'justifyright',
                'justifyjustify', '|', 'imagenone', 'imageleft', 'imageright',
                'imagecenter', '|',
                'insertimage', 'attachment', 'link'//'simpleupload',
              ]]
              , UEDITOR_HOME_URL: HRY.modules.web + "/static/ueditor/"
              //,UEDITOR_HOME_URL:HRY.host+"/static/static/lib/ueditor/"
              , serverUrl: HRY.modules.web + "baidu/baidu"

            }

          });

        }]);

  //最高Controller 一些全局操作
  app.controller('appCtrl',
      ['$scope', '$rootScope', '$http', '$state', '$stateParams', '$document',
        "$timeout",
        function ($scope, $rootScope, $http, $state, $stateParams, $document,
            $timeout) {

          $rootScope.$on('UserIntercepted', function (errorType) {
            // 跳转到登录界面，这里我记录了一个from，这样可以在登录后自动跳转到未登录之前的那个界面
            $state.go("login", {from: $state.current.name, w: errorType});
          });

          /** ---------------------------读取系统配置信息---------------------------------------- */
          $rootScope.appConfigData = [];
          $http.get(HRY.modules.web
              + 'app/appconfig/dataByConfigKey?type=baseConfig').success(
              function (data) {
                if (data != undefined) {
                  var ret = data;
                  $rootScope.appConfigData = ret;
                }

              });
          /** ------------------------------------------------------------------------------- */

          /** ---------------------------读取金融参数配置信息---------------------------------------- */
          $rootScope.financeConfig = [];
          $http.get(HRY.modules.web
              + 'app/appconfig/dataByConfigKey?type=financeConfig').success(
              function (data) {
                if (data != undefined) {
                  var ret = data;
                  $rootScope.financeConfig = ret;
                  //默认值
                  //$rootScope.financeConfig.dollarRateWithdraw=1;
                  //充值汇率
                  if ($rootScope.financeConfig.dollarRate != undefined) {
                    $rootScope.financeConfig.dollarRate = Number(
                        $rootScope.financeConfig.dollarRate.slice(0,
                            $rootScope.financeConfig.dollarRate.indexOf(":"))
                        / $rootScope.financeConfig.dollarRate.slice(
                        $rootScope.financeConfig.dollarRate.indexOf(":") + 1,
                        $rootScope.financeConfig.dollarRate.leng));
                  }
                  //提现汇率
                  if ($rootScope.financeConfig.dollarRateWithdraw
                      != undefined) {
                    $rootScope.financeConfig.dollarRateWithdraw = Number(
                        $rootScope.financeConfig.dollarRateWithdraw.slice(0,
                            $rootScope.financeConfig.dollarRateWithdraw.indexOf(
                                ":"))
                        / $rootScope.financeConfig.dollarRateWithdraw.slice(
                        $rootScope.financeConfig.dollarRateWithdraw.indexOf(":")
                        + 1, $rootScope.financeConfig.dollarRateWithdraw.leng));
                  }
                }

              });

          //全局控制 Ctrl
        }]);

  //启动 app
  app.bootstrap = function () {
    //angular.bootstrap(document, ['HryApp']);

    $.get(HRY.modules.oauth + 'getUser', function (data) {
      try {
        console.log("data----" + data);
        if (data != undefined && data != "") {
          userInfo = JSON.parse(data);
          angular.bootstrap(document, ['HryApp']);
        } else {
          window.location.href = HRY.staticUrl + "/login.html";
        }
      } catch (e) {
        window.location.href = HRY.staticUrl + "/login.html";
      }

    });

    window.clearInterval(getUser_Interval);
    // 开启点击后定时数字显示
    var getUser_Interval = window.setInterval(function () {
      $.get(HRY.modules.oauth + 'getUser', function (data) {
        if (data != undefined && data != "") {
          userInfo = JSON.parse(data);
        }
      });
    }, 1000 * 60 * 2);

  };
  return app;
});
