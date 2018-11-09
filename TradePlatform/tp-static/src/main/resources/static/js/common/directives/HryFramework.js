/**
 * 框架
 * 使用规则 <hry-framework/>
 */
angular.module('hry-framework', []).directive('hryFramework', ["$rootScope","$http",function ($rootScope,$http) {
   
	return {
       replace: false,
       restrict: 'EA', 
       link: function($scope, element, attrs) {
           $scope.framework = 'static/views/framework.html';
           
           
         	 /**-------------------------------------------------------------------------------*/
     	     /**------------------------初始化权限列表 start----------------------------------------*/
  	   	 /**-------------------------------------------------------------------------------*/
  	   	 $rootScope.shiroData = [];
  	 	 $http.get(HRY.modules.oauth+'user/appuser/findMyShiro')
  		 .success(function(data) {
  			 $rootScope.shiroData = data;
  		 });
  	   	 /*
  	   	  * 校验按钮是否显示
  	   	  * 
  	   	  * 调用示例:<div ng-if="requiresPermissions('按钮url')"><a>我是测试按钮</a></div>
  	   	  * 
  	   	  * key  模块名称
  	   	  * shiroUrl url
  	   	  * add by liusl
  	   	  * 
  	   	  */
  	   	 $rootScope.requiresPermissions = function (appkey,shiroUrl) {
  	   		 if((shiroUrl.constructor === Boolean&&shiroUrl== true)||"true"==shiroUrl){
  	   			 return true;
  	   		 }
  	   		 shiroUrl = appkey  + shiroUrl;
  	   		 for(var i = 0 ; i < $rootScope.shiroData.length ; i++){
  	   			 
  	   			 if($rootScope.shiroData[i]==shiroUrl){
  	   				 return true;
  	   			 }
  	   		 }
               return false;
           }
  	   
  	   	 /**------------------------------------------------------------- -----------------*/
     	     /**------------------------初始化权限列表 end-------------------- ---------- ----------*/
  	   	 /**-------------------------------------------------------------------------------*/
  	 	
  		 /**-------------------------------------------------------------------------------*/
     	     /**------------------------加载功能开关设置信息start----------------------------------------*/
  	   	 /**-------------------------------------------------------------------------------*/
  	   	 $rootScope.appSettingData = [];
  	 	 $http.get(HRY.modules.web+'app/appsetting/selectAll')
  		 .success(function(data) {
  			$rootScope.appSettingData = data;
  		});
  	 	/*
  	   	  * 校验按钮是否显示
  	   	  * 
  	   	  * 调用示例:<div ng-if="appSettingPermissions('按钮名称')"><a>我是测试按钮</a></div>
  	   	  * 
  	   	  * key  按钮名称
  	   	  * 
  	   	  * add by zhangxf
  	   	  * 
  	   	  */
  	 	 
  	 	$rootScope.appSettingPermissions = function (key) {
  	   		for(var i = 0 ; i < $rootScope.appSettingData.length ; i++){
  	   			 if($rootScope.appSettingData[i].propertiesName==key){
  	   				 if($rootScope.appSettingData[i].isOpen==0){
  	   					 return true;
  	   				 }else if($rootScope.appSettingData[i].isOpen==1){
  	   					return  false;
  	   				 }
  	   			 }
  	   		}
              return true;
          }
  		 /**-------------------------------------------------------------------------------*/
    	 /**------------------------加载功能开关设置信息end----------------------------------------*/
  	   	 /**-------------------------------------------------------------------------------*/
  	   	 
     	 /**------------------------切换菜单 start-------------------- ---------- ----------*/
  	   	 /**------------------------add by liusl-------------------------------------------*/
  	 	//动态切换调用方法
  	 	$scope.switchApp  = function(target){
  	 		switchAppMenu(target.currentTarget.getAttribute("appname"));
  	 	}
  	 	
  	   	 //刷新浏览器加载此方法
  	   	 //switchAppMenu($.getCookie("home")==null?"index":$.getCookie("home"));
  	 	 if($rootScope.AllApplicactions==undefined){
  			   $http.get(HRY.modules.web+'menu/appmenutree/loadapp?t=1')
  				 .success(function(data) {
  					$rootScope.AllApplicactions = data;
  					
  					if(data!=undefined){
  						var appMenu = "hurong_index";
  						var flag = false;
  						data.forEach(function(obj){
  							if(obj.appName.indexOf("index")!=-1){
  								flag = true;
  							}
  						});
  						if(flag){
  							switchAppMenu("hurong_index");
  						}else{
  							switchAppMenu(data[0].appName);
  						}
  					}
  				});
  		 }
  	   	 //切换菜单方法
  	   	 $scope.switchAppMenu = switchAppMenu;
  	   	 function switchAppMenu(key){
  	   		 if(key=="personalCenter"){//个人中心
  		   		 var menuHTML = 
  			   	  "<li>  "+
  		          "  <a  class=\"waves-effect waves-blue\"><i class=\"ion ion-social-angular\"></i>个人中心<span class=\"badge new\"></span></a>  "+
  		          "</li>  "+
  		          "<li class=\"open\">  "+
  		          "  <a class=\"yay-sub-toggle waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>个人中心<span class=\"yay-collapse-icon mdi-navigation-expand-more\"></span></a>  "+
  		          "  <ul>  "+
  		          "    <li>  "+
  		          "      <a href=\"#/oauth/user/appuser/info/"+$rootScope.user.id+"\" class=\"waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>个人资料</a>  "+
  		          "    </li>  "+
  		          "    <li>  "+
  		          "      <a href=\"#/oauth/user/appuser/picture/"+$rootScope.user.id+"\" class=\"waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>头像设置</a>  "+
  		          "    </li>  "+
  		          "    <li>  "+
  		          "      <a href=\"#/oauth/user/appuser/password/"+$rootScope.user.id+"\" class=\"waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>修改密码</a>  "+
  		          "    </li>  "+
  		          "  </ul>  "+
  		          "</li>   ";
  		   		$("#menuHTML").html(menuHTML);
  		   		//个人中心主页
  				window.location.href='#/oauth/user/appuser/info/'+$rootScope.user.id;
  	   		 }else if(key=="systemSetting"){
  	   			 
  	   			 
  		   		 var menuHTML = 
  				   	  "<li>  "+
  			          "  <a  class=\"waves-effect waves-blue\"><i class=\"ion ion-social-angular\"></i>菜单管理<span class=\"badge new\"></span></a>  "+
  			          "</li>  "+
  			          "<li class=\"open\">  "+
  			          "  <a class=\"yay-sub-toggle waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>菜单管理<span class=\"yay-collapse-icon mdi-navigation-expand-more\"></span></a>  "+
  			          "  <ul>  "+
  			          "    <li>  "+
  			          "      <a href=\"#/web/menu/appmenutree/list/anon\" class=\"waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>应用菜单管理</a>  "+
  			          "    </li>  "+
  			          "  </ul>  "+
  			          "</li>   ";
  		   		 if($rootScope.user.username!="admin"){
	  		   		 menuHTML = 
	  		   		  "<li>  "+
				          "  <a  class=\"waves-effect waves-blue\"><i class=\"ion ion-social-angular\"></i>菜单管理<span class=\"badge new\"></span></a>  "+
				          "</li>  "+
				          "<li class=\"open\">  "+
				          "  <a class=\"yay-sub-toggle waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>菜单管理<span class=\"yay-collapse-icon mdi-navigation-expand-more\"></span></a>  "+
				          "  <ul>  "+
				          "    <li>  "+
				          "      <a href=\"#/web/menu/appmenutree/list/anon\" class=\"waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>应用菜单管理</a>  "+
				          "    </li>  "+
				          "  </ul>  "+
				          "</li>   ";
  		   		 }
  		   		 
  		   		 $("#menuHTML").html(menuHTML);
  	   		 }else {
  	   			if(key=="hurong_index"){//主页
  	   				key = "index"  //左边快捷菜单
  	   			} 
  	   			loadAppMenu(key);
  	   		 }
  	   	 }
  	   	 /**------------------------------------------------------------- -----------------*/
     	     /**------------------------切换菜单 end-------------------- ---------- ----------*/
  	   	 /**-------------------------------------------------------------------------------*/
  	   	 
     	     
  	   	 /**-------------------------------------------------------------------------------*/
     	     /**------------------------初始化菜单 start------------------------------------------*/
  	   	 /**------------------------add by liusl------------------------------------------*/
  	   	 function loadAppMenu(key){
  		   	 //用户信息返回后再load菜单
  			 $http.get(HRY.modules.web+'menu/appmenutree/loadSystemMenu.do?type=loadMeny&key='+key).success(function(data) {
  			 	
  				 //过滤menu属性pkey
  				 function checkPkey(pkey){
  					 
  				 	 for(var i = 0 ; i < $rootScope.AllApplicactions.length ; i++){
  				 		 var app = $rootScope.AllApplicactions[i];
  				 		 if(app.mkey ==pkey){
  				 			 return true;
  				 		 }
  					 }
  				 	 return false;
  					 if(true
//  						pkey=="shortcut" ||
//  						pkey=="oauth"||
//  						pkey=="web"||
//  						pkey=="factoring" ||
//  						pkey=="finance"  ||
//  						pkey=="exchange" ||
//  						pkey=="customer" ||
//  						pkey=="sms" ||
//  						pkey=="account"
  					  ){
  						 return true;
  					 }
  					 return false;
  				 }
  		 		//生成A标签链接
  				 function createAtag(data,dataObject ,strHTML){
  					
  					if(dataObject.isOpen!=null&&dataObject.isOpen=="1"){
  			         	strHTML += "<li class=\"open\">";
  			        }else{
  			          	strHTML += "<li>";
  			        }
  					if(dataObject.url!=null&&dataObject.url!=""){
  						var url = dataObject.url
  						url="#/"+url;
  						
  						if(dataObject.isOutLink!=null&&dataObject.isOutLink=="1"){
  				        	strHTML += "<a href=\""+dataObject.url+"\"  target=\"_blank\" class=\"waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>"+dataObject.name+"<span class=\"yay-collapse-icon mdi-navigation-expand-more\"></span></a>";
  						}else{
  				        	strHTML += "<a href=\""+url+"\" class=\"waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>"+dataObject.name+"<span class=\"yay-collapse-icon mdi-navigation-expand-more\"></span></a>";
  						}
  			        }else{
  			            strHTML += "<a class=\"yay-sub-toggle waves-effect waves-blue\"><i class=\"mdi-action-stars\"></i>"+dataObject.name+"<span class=\"yay-collapse-icon mdi-navigation-expand-more\"></span></a>";
  			        }
  			        
  			        return strHTML
  				 }
  			 		
  			 		
  				var menuTitle = "";
  					
  				var strHTML  = menuTitle;
  			    
  	            for(var i = 0 ; i < data.length ; i++){ //遍历获取的数据   ----- 一级
  		           	if(checkPkey(data[i].pkey)){
  		               	strHTML = createAtag(data,data[i],strHTML);
  		           	    
  		           	    //标记是否加"<ul>"
  		           	    var topUl = true;
  		           		var bottomUl = false;
  		           		for(var x = 0 ; x < data.length ; x++ ){//遍历获取的数据   -----二级
  		           			if(checkPkey(data[x].pkey)){
  		           				continue;
  		           			}
  		           			
  		           			if(data[x].pkey==data[i].mkey){
  		           				if(topUl){
  		           					strHTML += "<ul>";
  		           					topUl = false;   //加了头  就改false
  		           					bottomUl = true; //同时把尾  改为true
  		           				}
  		       				
  		           				strHTML = createAtag(data,data[x],strHTML);
  		           				
  		           				
  		           				var topUl3 = true;
  		           				var bottomUl3 = false;
  		           				for(var y = 0 ; y < data.length ; y++ ){//遍历获取的数据   -----三级
  		           					
  		           					if(checkPkey(data[y].pkey)){
  			                				continue;
  			                			}
  		           					
  		           					if(data[y].pkey==data[x].mkey){
  		           						if(topUl3){
  			                					strHTML += "<ul>";
  			                					topUl3 = false;   //加了头  就改false
  			                					bottomUl3 = true; //同时把尾  改为true
  			                				}
  			                				strHTML = createAtag(data,data[y],strHTML);
  			                				strHTML += "</li>";
  			                				
  		           					}
  		           				}
  		           				//判断是否加结束</ul>
  			                		if(bottomUl3){
  		               				strHTML += "</ul>";
  		               				bottomUl3 =false;
  		              				}  
  		           				strHTML += "</li>";
  		           			}
  		           		}
  		           		//判断是否加结束</ul>
  		           		if(bottomUl){
  		       				strHTML += "</ul>";
  		       				bottomUl =false;
  		      				}
  		               	strHTML += "</li>";
  		               	
  		           	}
  	           }
  	            
  	           $("#menuHTML").html(strHTML);
  	           
  	        });
  	   		 
  	   	 }
  		 
  	   	 
           
       },
       template: '<div ng-include="framework"></div>'
      } 

  }])
  


