/**
 * 注册oauth路由服务
 */
define(['app'], function (app) {
	 
	var oauthAppRouteService = function(){
		this.$get = function() {
				return {
					state:'oauth',
					url:'/oauth',
					path:['js/oauth/oauth'],
					controller:'oauthCtrl',
					
				 routes:[{
				//角色管理
					state:'oauth.user_approle',
					url: '/user/approle/:page/:id',
					path: ['js/oauth/user/AppRole'],
					controller: 'oauth_user_approle'
				}, {//资源管理
					state:'oauth.user_appresource',
					url: '/user/appresource/:page/:anon',
					path: ['js/oauth/user/AppResource'],
					controller: 'oauth_user_appresource'
				}, {//个人中心
					state:'oauth.user_appuser',
					url: '/user/appuser/:page/:id',
					path: ['js/oauth/user/AppUser'],
					controller: 'oauth_user_appuser'
				}, {//部门管理
					state:'oauth.org',
					url: '/company/org',
					path: ['js/oauth/company/Company'],
					controller: 'oauth_company_company',
					routes: [{//用户管理
						state:'oauth.org.user',
						url: '/user/appuser/:page/:orgid/:pid',
						path: ['js/oauth/user/AppUser'],
						controller: 'oauth_user_appuser'
					},
					{//部门管理
						state:'oauth.org.organization',
						url: '/company/apporganization/:page/:id',
						path: ['js/oauth/company/AppOrganization'],
						controller: 'oauth_company_apporganization'
					}
					]
				}, {//分公司管理
					state:'oauth.company_subcompany',
					url: '/company/subcompany/:page/:id',
					path: ['js/oauth/company/Subcompany'],
					controller: 'oauth_company_subcompany'
				}, {//集团配置
					state:'oauth.company_jtpz',
					url: '/company/jtpz',
					path: ['js/oauth/company/jtpz'],
					//templateUrl: '/views/oauth/company/jtpz.html',
					controller: 'oauth_company_jtpz'
				}, {//人员关系图
					state:'oauth.company_rjgxt',
					url: '/company/rjgxt',
					path: ['js/oauth/company/rjgxt'],
					//templateUrl: '/views/oauth/company/rjgxt.html',
					controller: 'oauth_company_rjgxt'
				}, {//组织机构图
					state:'oauth.company_zzjgt',
					url: '/company/zzjgt',
					path: ['js/oauth/company/zzjgt'],
					//templateUrl: '/views/oauth/company/zzjgt.html',
					controller: 'oauth_company_zzjgt'
				}, {//上下级配置
					state:'oauth.user_appuserlevel',
					url: '/user/appuserlevel/:page/:anon',
					path: ['js/oauth/user/AppUserLevel'],
					controller: 'oauth_user_appuserlevel'
				}]	
			};
		};
	};
	//在模块中登记--使用provider用于config中调用
	app.provider("oauthRoute",oauthAppRouteService);
	return app;
	
})