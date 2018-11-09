/**
 * student.js
 */
define(['app','hryTable' ,'hryUtil' ], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "客户账号管理";
    	//初始化js 插件
    	
    	
    	var table =  $("#table2");
    	
		//------------------------查看页面路径---------------------------------------------
		if ($stateParams.page == "see") {
			 hryCore.CURD({
						url : HRY.modules.customer+"user/appcustomer/see/"+$stateParams.id
					}).get(null,
				function(data) {
             	$scope.model = data;
             	 
		    }, function(data) {
				growl.addInfoMessage("error:" + data.msg);
			});
		}
       	
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	$scope.serchData={};
        	//--------------------加载dataTable--------------------------------
				var config = DT.config();
				config.onlyClick = function(b){
					if(b.attr("src")=='1'){
                        openAlipayWindow()
                        $("#alipayPicture").attr("src",b.attr("name"));

					}else if(b.attr("src")=='2'){
                        openWeChatWindow()
                        $("#weChatPicture").attr("src",b.attr("name"));
					}
				}
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.account+'fund/appbankcard/list.do';
        		config.ajax.data = function(d){
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, {
										"data" : "userName"
									},{
										"data" : "surname"
									},{
										"data" : "trueName"
									},{ 
										"data" : "bankProvince"
									},{ 
										"data" : "bankAddress"
									},{ 
										"data" : "cardBank"
									},{ 
										"data" : "subBank"
									},/*{ 
										"data" : "subBankNum"
									}, */{
										"data" : "cardNumber"
									}/*,{
										"data" : "cardName"
									}*/,{
										"data" : "mobile"
									},{
										"data" : "alipay"
									},{
										"data" : "alipayPicture"
									},{
										"data" : "weChat"
									},{
										"data" : "weChatPicture"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
												
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											"targets" : 11,

											"render" : function(data, type, row) {

												return "<input type='button' src='1' name='"+data+"' id='seeImage' value='查看照片'></input>"
											}
										},{
											"targets" : 13,

											"render" : function(data, type, row) {

												return "<input type='button' src='2' name='"+data+"' id='seeImage' value='查看照片'></input>"
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnSee=fnSee;//see按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnRemove=fnRemove;//remove方法
            $scope.fnList=fnList;//刷新方法
            // 弹出查看支付宝收款码窗口
            function openAlipayWindow(){
                $("#showAlipayPicture").removeClass("hide")
                // 弹出选择窗体
                layer.open({
                    type : 1,
                    title : "查询用户支付宝收款码",
                    closeBtn : 2,
                    area : [ '85%', '85%' ],
                    shadeClose : true,
                    content : $('#showAlipayPicture')
                });
            }
            // 弹出查看微信收款码窗口
            function openWeChatWindow(){
                $("#showWeChatPicture").removeClass("hide")
                // 弹出选择窗体
                layer.open({
                    type : 1,
                    title : "查询用微信收款码",
                    closeBtn : 2,
                    area : [ '85%', '85%' ],
                    shadeClose : true,
                    content : $('#showWeChatPicture')
                });
            }
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
            //导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"银行卡管理");
			}
            
            
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/oauth/'+url+'/anon';
            }
            
            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(url){
            	var ids = DT.getSelect(table);
            	//var ids = transform(selectes);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		window.location.href='#/customer/'+url+'/'+ids[0];
            	}
            }
            
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(url){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		$rootScope.id= ids[0];
            		window.location.href='#/oauth/'+url+'/'+ids[0];
            	}
            }
            
            //删除按钮
            //ng-click="fnRemove(url,selectes)"
            function fnRemove(url){
            	var ids = DT.getSelect(table);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}
            	
            	var id = ""	;
            	for(var i = 0 ; i < ids.length ; i++){
            		id += ids[i];
            		if(i!=ids.length-1){
            			id += ",";
            		}
            	}
            	
            	hryCore
				.CURD({
						url:HRY.modules.oauth+url+"/"+id
				 })
				.remove(null,
						function(data) {
		                	if(data.success){
		                		//提示信息
		                		growl.addInfoMessage('删除成功')
		                		//重新加载list
		                		fnList();
		                	}else{
		                		growl.addInfoMessage(data.msg)
		                	}
		                },
						function(data) {
							growl.addInfoMessage("error:"+ data.msg);
						});
            	
			}
            
        
        }
        
        hryCore.initPlugins();
        
    }
    return {controller:controller};
});