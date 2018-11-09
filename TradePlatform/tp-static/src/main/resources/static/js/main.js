require.config({
	urlArgs : "v=" + (new Date()).getTime(),
	//urlArgs : "v=v_1.0",
    baseUrl : 'static/',
    waitSeconds: 0,//禁用加载超时
    map: {
        '*': {
            'css': 'lib/css'
            
        }
    },
    shim: {

        'underscore': { exports: '_' },
       
        'angular': {
            deps :['jquery'],
            exports: 'angular'
        },
        'angular-md5': {
         	 deps :['angular'],
              exports: 'angular-md5'
          },
        'ocLazyLoad': {
            deps :['angular'],
            exports: 'ocLazyLoad'
        },
        'hryFramework': {
            deps: ['angular'],
            exports: 'hryFramework'
        },
        
        'angular-validation-rule' : {
			 deps : ["angular","angular-validation"],
		 	 exports :'angular-validation-rule'
		 },
		 'angular-validation' : {
			 deps : ["angular"],
		 	 exports :'angular-validation'
		 },
        
        'jqdataTable': {
            deps: ['jquery'],
            exports: 'jqdataTable'
        },
        'tableTools': {
            deps: ['jquery','jqdataTable'],
            exports: 'tableTools'
        },
        'tableScroll': {
            deps: ['jquery','jqdataTable'],
            exports: 'tableScroll'
        },'sortable': {
            deps: ['jquery','jqdataTable'],
            exports: 'sortable'
        },
        'hryTable': {
            deps: ['jqdataTable','tableTools','tableScroll','sortable','layer'],
            exports: 'hryTable'
        },
        
        'ngRoute': {
            deps: ['angular'],
            exports: 'ngRoute'
        },
        'uiRoute': {
            deps: ['angular'],
            exports: 'uiRoute'
        },
        'ngSanitize' : {
            deps : [ "angular" ],
            exports : 'ngSanitize'
        },
        'ngResource' : {
            deps : [ "angular" ],
            exports : 'ngResource'
        },
       
		 
		 
		 'pikadayJq' : {
			 deps :["underscore","jquery","moment","css!lib/pikaday/pikaday.css"],
			 exports : "pikadayJq"
		 },
		 'clockpicker' : {
			 deps :["underscore","jquery","css!lib/jquery-clockpicker/jquery-clockpicker.min.css"],
			 exports : "clockpicker"
		 },
		 
		 'bootstrap-datetimepicker' : {
			 deps :["underscore","jquery","bootstrap","css!lib/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css"],
			 exports : "bootstrap-datetimepicker"
		 },
		 
        'angularAnimate' : {
            deps : [ "angular" ],
            exports : 'angularAnimate'
        },
        
        'angularjs-ueditor' : {
            deps : [ "angular" ],
            exports : 'angularjs-ueditor'
        },
        
       
        'anguarGrowl' : {
            deps : ["angular", "angularAnimate","css!lib/angularjs/plugins/angular-growl.min.css"],
            exports : 'anguarGrowl'
        },
        'wow' : {
        	deps : ["css!lib/animate.css/animate.css"],
        	exports : 'wow'
        },
      
        'jquerymigrate' : {
            deps : ["jquery"],
            exports :'jquerymigrate'
        },
        
       
        '_con' : {
            deps : ["jquery","jquerynano"],
            exports :'_con'
        },
        
        'highcharts' : {
            deps : ["jquery"],
            exports :'highcharts'
        },
        
        'loginTimeOut' : {
            deps : ["jquery"],
            exports :'loginTimeOut'
        },
        
        'layer' : {
            deps :["jquery","css!lib/layer/layer2.1/layer/skin/layer.css","css!lib/layer/layer2.1/layer/skin/layer.ext.css"],
            exports : "layer"
        },

       
       
        'jqdataTable':{
        	deps:["jquery"],
        	exports:'jqdataTable'
        },
       
        'select2':{
        	deps:["jquery"],
        	exports:"select2"
        },
        
        'materialize': {
            deps :['underscore','jquery'],
            exports: 'materialize'
        },
        
        'angularMaterialize': {
            deps :['angular','materialize'],
            exports: 'angularMaterialize'
        },
        
            
        'ztree':{
        	deps:["jquery","css!lib/ztree/css/zTreeStyle/zTreeStyle.css"
        	      ],
        	exports:"ztree"
        }
    
    },
    paths: {
    	
        'underscore':'lib/underscore',
        'codemirror':'lib/codemirror/lib/codemirror',
        'jquery' : 'lib/jquery/jquery-1.9.1.min',
        'jquerymigrate' : 'lib/jquery/jquery-migrate-1.1.0.min',
        'jquerynano':'lib/nanoScroller/jquery.nanoscroller.min',
        'jqueryrequest':'lib/jqueryRAF/jquery.requestAnimationFrame.min',
        'select2':'lib/select2/js/select2.full',
        'jquerySimpleWeather':'lib/simpleWeather/jquery.simpleWeather.min',
        'jquerySparkLine':'lib/sparkline/jquery.sparkline.min',
        
        'angular-validation':'lib/angularjs/plugins/angular-validation',
		'angular-validation-rule':'lib/angularjs/plugins/angular-validation-rule',
		'module_md5':'lib/angularjs-md5/module_md5',
		'angular-md5':'lib/angularjs-md5/angular-md5.min',
        "hryTable":"js/common/dataTable",
        'jqdataTable':'lib/dataTables/js/jquery.dataTables.min',
        'tableTools':'lib/dataTables/extensions/TableTools/js/dataTables.tableTools.min',
        'tableScroll':'lib/dataTables/extensions/Scroller/js/dataTables.scroller.min',
        'sortable':'lib/sortable/Sortable.min',
        
        'materialize':'lib/materialize/js/materialize.min',
        'hammerjs':'lib/materialize/hammer.min',
        'angularMaterialize':'lib/materialize/js/angular-materialize',
        
       
        'pikaday':'lib/pikaday/pikaday',
        'pikadayJq':'lib/pikaday/pikaday.jquery',
        'moment':'lib/pikaday/moment',
        'clockpicker':'lib/jquery-clockpicker/jquery-clockpicker.min',
        
        'bootstrap':'lib/bootstrap/bootstrap.min',
        'bootstrap-datetimepicker':'lib/bootstrap-datetimepicker/bootstrap-datetimepicker',
        
        'dropzone':'lib/dropzone/dropzone.min',
        '_con':'lib/_con/js/_con',
        '_hry':'lib/_con/js/_hry',
        'photoswipe':'lib/PhotoSwipe/photoswipe.min',
        'swipeui':'lib/PhotoSwipe/photoswipe-ui-default.min',
       
        'angular': 'lib/angularjs/angular.min',
        'ngResource':'lib/angularjs/plugins/angular-resource.min',
        'ngRoute': 'lib/angularjs/angular-route',
        'uiRoute': 'lib/angularjs/plugins/angular-ui-router.min',
        "ngSanitize" : "lib/angularjs/angular-sanitize",
       // 'ngDialog':'lib/ngDialog-master/js/ngDialog.min',
        'angularAnimate':'lib/angularjs/plugins/angular-animate.min',
        'anguarGrowl':'lib/angularjs/plugins/angular-growl.min',
        'wow':'lib/wow.js/wow.min',
        'ocLazyLoad':'lib/angularjs/plugins/ocLazyLoad.require.min',
        
        
        'd3':'lib/d3/d3.min',
       
        "app-router":"js/app-router",
        "app":"js/app",
        "hryCore":"js/common/hryCore",
        'hryUtil' : 'js/common/jsUtil',
        'loginTimeOut' : 'js/common/loginTimeOut',
        'hryMessage' : 'js/common/message',
        'layer' : 'lib/layer/layer2.1/layer/layer',
        "highcharts":"lib/highcharts-4.2.5/js/highcharts",
        "hryFramework":"js/common/directives/HryFramework",
        "hryCollection":"js/common/directives/HryCollection",
        'prettify':'lib/google-code-prettify/prettify',
        
        
        "ztree" : "lib/ztree/js/jquery.ztree.all-3.5",
        "hrytree":"js/common/factory/HryTree",
        "htmleditor":"js/common/factory/HtmlEditor",
        
        "ajaxupload":"lib/jquery/ajaxupload.3.6",  //ajax上传
        
        //返回按钮
        "backButton" : "js/common/directives/backButton",
        "hryDicSelect" : "js/common/directives/HryDicSelect",
        "hrySelect" : "js/common/directives/HrySelect",
        "hrySearchPanel" : "js/common/directives/HrySearchPanel",
        "ckfinder":"lib/ckfinder/ckfinder",
        "ckeditor":"lib/ckeditor/ckeditor",
       // "ueditorConf":HRY.modules.web+"static/ueditor/ueditor.config",
        //"ueditor":HRY.modules.web+"static/ueditor/ueditor.all",
        "angularjs-ueditor":"lib/angularjs-ueditor/angular-ueditor",
        "hryEditor" : "js/common/directives/HryEditor",
        "hryCollection" : "js/common/directives/HryCollection",
        "hryUploadone" : "js/common/directives/HryUploadone",
        "hryUploadtwo" : "js/common/directives/HryUploadtwo",
        "message" : "js/common/message"
    }
});

require([
        'jquery',
        'app-router',
        'hryCore',
        'hryCollection',
        'ajaxupload',
        'backButton',
        'hrySelect',
        'hrySearchPanel',
        'hryDicSelect',
        'hryUploadone',
        'select2',
        "hrytree",
        "htmleditor",
        "hryEditor",
        "angularjs-ueditor",
        'hryUploadtwo',
        'message'
    ],function($,angular){
        angular.bootstrap();
    }

);

