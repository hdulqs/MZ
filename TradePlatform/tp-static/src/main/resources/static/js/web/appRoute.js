
/**
 * 注册web路由服务
 */
define(['app'], function (app) {
	 
	var webAppRouteService = function(){
		this.$get = function(){
			return {
					state:'web',
					url:'/web',
					path:['js/web/web'],
					controller:'webCtrl',
		        	
					routes:[
					{	state:'web.dictionary_dicitionary',
						url : '/app/appdictionary/:page/:id',
						path : ['js/web/app/AppDictionary'],
						controller : 'web_app_appdictionary'
					},
					{	state:'web.dictionary_level',
						url : '/dictionary/appdiconelevel/:page/:id',
						path : ['js/web/dictionary/AppDicOnelevel'],
						controller : 'web_dic_appdiconelevel'
					},
					{	state:'web.dictionary_mul',
						url : '/dictionary/appdicmultilevel/:page/:id',
						path : ['js/web/dictionary/AppDicMultilevel'],
						controller : 'web_dic_appdicmultilevel'
					},
					{	state:'web.app_config',
						url : '/app/appconfig/:page/:id',
						path : ['js/web/app/AppConfig'],
						controller : 'web_app_appconfig'
					},
					{	state:'web.app_holidayconfig',
						url : '/app/appholidayconfig/:page/:id',
						path : ['js/web/app/AppHolidayConfig'],
						controller : 'web_app_holidayconfig'
					},
					{//文件管理
						state:'web.log_file',
						url : '/file/appfile/:page/:id',
						path : ['js/web/file/AppFile'],
						controller : 'web_file_appfile',
						sonroute :[{//用户管理
							name : "filemanage",
							path : ['js/web/file/FileManage'],
							controller : 'web_file_filemanage'
						   }]
					},
					{//操作日志
						state:'web.log_applog',
						url : '/log/applog/:page/:id',
						path : ['js/web/log/AppLog'],
						controller : 'web_log_applog'
					},
					{//登录日志
						state:'web.log_login',
						url : '/log/apploglogin/:page/:id',
						path : ['js/web/log/AppLogLogin'],
						controller : 'web_log_apploglogin'
					},
					{//异常日志
						state:'web.log_exce',
						url : '/log/appexception/:page/:id',
						path : ['js/web/log/AppException'],
						controller : 'web_log_appexception'
					},
					{//druid监控
						state:'web.log_durid',
						url : '/log/druid',
						path : ['js/web/log/druid'],
//						templateUrl :'static/views/web/log/druid.html',
						controller : 'web_log_druid'
					},
					{//monitoring监控
						state:'web.log_monitoring',
						url : '/log/monitoring',
						path : ['js/web/log/monitoring'],
						templateUrl :'static/views/web/log/monitoring.html',
						controller : 'web_log_monitoring'
					},
					{//操作日志
						state:'web.log_student',
						url : '/log/student',
						path : ['js/web/log/Student'],
						controller : 'web_log_student'
					},
					{	state:'web.app_appsetting',
						url : '/app/appsetting/:page/:id',
						path : ['js/web/app/AppSetting'],
						controller : 'web_app_appsetting'
					},
					{	state:'web.article_apparticle',
						url : '/article/apparticle/:page/:id/:state',
						path : ['js/web/article/AppArticle'],
						controller : 'web_article_apparticle'
					},
					{	state:'web.article_gory',
						url : '/article/appcategory/:page/:id',
						path : ['js/web/article/AppCategory'],
						controller : 'web_article_appcategory'
					},
					{	state:'web.app_cache',
						url : '/app/appcache/:page/:id',
						path : ['js/web/app/AppCache'],
						controller : 'web_app_appcache'
					},
					{	state:'web.article_link',
						url : '/article/appfriendlink/:page/:id',
						path : ['js/web/article/AppFriendlink'],
						controller : 'web_article_appfriendlink'
					},
					{	state:'web.menu_appmenu',
						url : '/menu/appmenu/:page/:id',
						path : ['js/web/menu/AppMenu'],
						controller : 'web_menu_appmenu'
					},
					{	state:'web.message_appmess',
						url : '/message/appmessage/:page/:id',
						path : ['js/web/message/AppMessage'],
						controller : 'web_message_appmessage'
					},
					{	state:'web.message_gory',
						url : '/message/appmessagecategory/:page/:id',
						path : ['js/web/message/AppMessageCategory'],
						controller : 'web_message_appmessagecategory'
					},
					{	state:'web.menu_cust',
						url : '/menu/appmenucust/:page/:id',
						path : ['js/web/menu/AppMenuCust'],
						controller : 'web_menu_appmenucust'
					},
					{	state:'web.menu_appmenutree',
						url : '/menu/appmenutree/:page/:id',
						path : ['js/web/menu/AppMenuTree'],
						controller : 'web_menu_appmenutree'
					},
					{	//前台banner
						state:'web.article_banner',
						url : '/article/appbanner/:page/:id',
						path : ['js/web/article/AppBanner'],
						controller : 'web_article_appbanner'

					},
					{	//api接口
						state:'web.app_appapi',
						url : '/app/appapi/:page/:id',
						path : ['js/web/app/AppApi'],
						controller : 'web_app_appapi'
					},
					{	//api接口参数
						state:'web.app_appapiparam',
						url : '/app/appapiparam/:page/:id',
						path : ['js/web/app/AppApiParam'],
						controller : 'web_app_appapiparam'

					},
					{	state:'web.schedule_schedulejob',
						url : '/schedule/schedulejob/:page/:id',
						path : ['js/web/schedule/ScheduleJob'],
						controller : 'web_schedule_schedulejob'

					},
					{	state:'web.user_testuser',
						url : '/user/testuser/:page/:id',
						path : ['js/web/user/TestUser'],
						controller : 'web_user_testuser'
					},
					{	state:'web.coin_coinmanage',
						url : '/coin/coinmanage/:page/:id',
						path : ['js/web/coin/CoinManage'],
						controller : 'web_coin_coinmanage'
					},
					{	state:'web.quartz_appquartzjob',
						url : '/quartz/appquartzjob/:page/:id',
						path : ['js/web/quartz/AppQuartzJob'],
						controller : 'web_quartz_appquartzjob'
					},
					{	state:'web.codemirror_appcodemirror',
						url : '/codemirror/appcodemirror/:page/:id',
						path : ['js/web/codemirror/AppCodeMirror'],
						controller : 'web_codemirror_appcodemirror'
					},
					{	state:'web.open_SysOpenUser',
						url : '/open/sysopenuser/:page/:id',
						path : ['js/web/open/SysOpenUser'],
						controller : 'web_open_SysOpenUser'
					},{	
						state:'web.app_applogthirdinterface',
						url : '/app/applogthirdinterface/:page/:id',
						path : ['js/web/app/AppLogThirdInterface'],
						controller : 'web_app_applogthirdinterface'
					},{
						state:'web.exdmtransfcolddetail_exDmTransfColdDetail',
						url : '/exdmtransfcolddetail/exdmtransfcolddetail/:page/:id',
						path : ['js/web/exdmtransfcolddetail/ExDmTransfColdDetail'],
						controller : 'web_exDmTransfColdDetail_exdmtransfcolddetail'
					},
					{	state:'web.app_appLendConfig',
						url : '/app/applendconfig/:page/:id',
						path : ['js/web/app/AppLendConfig'],
						controller : 'web_app_applendconfig'
					},
                        {	state:'web.system_SysCodeValue',
                            url : '/system/syscodevalue/:page/:code_id',
                            path : ['js/web/system/SysCodeValue'],
                            controller : 'web_system_SysCodeValue'
                        },
                        {	state:'web.system_AppMemberPointConfig',
                            url : '/system/appmemberpointconfig/:page/:id',
                            path : ['js/web/system/AppMemberPointConfig'],
                            controller : 'web_system_AppMemberPointConfig'
                        }
					]
			}
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("webRoute",webAppRouteService);
	return app;
	
})