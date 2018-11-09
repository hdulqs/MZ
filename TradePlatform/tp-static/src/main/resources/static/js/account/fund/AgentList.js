/**
 * 给代理商派发佣金
 */
define(['app','hryTable' ,'hryUtil'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	$rootScope.headTitle = $rootScope.title = "代理商佣金记录";
    	//初始化js 插件

    	//------------------------列表页面路径---------------------------------------------
        //if($stateParams.page=="list"){
        	var table =  $("#table2");
        	$scope.serchData="";
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.account+'fund/appaccount/agentList';
        		config.ajax.data = function(d){
					        			//设置select下拉框
										DT.selectData($scope.serchData);
					        			$.each($scope.serchData, function(name,value){
											if(""!=value){
												eval("d."+name+"='"+value+"'");
											}
										});
						           }
        		config.columns = [	{
        								"data" : "id"
        							}, { 
										"data" : "appPersonInfo.trueName"
									}, {
										"data" : "appPersonInfo.mobilePhone"
									},{
										"data" : "rewardMoney"
									},{
										"data" : "hasRewardMoney"
									},{
										"data" : "appPersonInfo.jkAgentType"
									},{
										"data" : "appPersonInfo.jkAgentProvince"
									},{
										"data" : "appPersonInfo.jkAgentCity"
									},{
										"data" : "appPersonInfo.jkAgentCounty"
									}
        		                  ]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
											"render" : function(data, type, row) {
												return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
											}
										},{
											// 0 会员   1 区代（县代）  2 市代   3省代
											"targets" : 5,
											"renderRule" : "VALUE_CONVERT",//单纯值的替换
											"renderValueRule" : "0=普通会员,1=区代（县代）,2=市代,3=省代,else=类型错误",//值替换规则
											"render" : function(data, type, row) {
												//类型     1实名得币  2推荐奖励得币  
												if(data=="0"){
													return "普通会员";
												}else if(data=="1"){
													return "区代（县代）";
												}else if(data=="2"){
													return "市代";
												}else if(data=="3"){
													return "省代";
												}else{
													return "类型错误";
												}
											}
										},{
											"targets" : 6,
											"render" : function(data, type, row) {
												if(data.indexOf("请选择")>0){
													return "--";
												}else{
													return data;
												}
											}
										},{
											"targets" : 7,
											"render" : function(data, type, row) {
												if(data.indexOf("请选择")>0){
													return "--";
												}else{
													return data;
												}
											}
										},{
											"targets" : 8,
											"render" : function(data, type, row) {
												if(data.indexOf("请选择")>0){
													return "--";
												}else{
													return data;
												}
											}
										}
									 ]
        	DT.draw(table,config);
    		//--------------------加载dataTable--------------------------------
        		
            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnSee=fnSee;//see按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnPost=fnPost;//派发佣金
            $scope.fnList=fnList;//刷新方法
            $scope.modifySubmit = modifySubmit;//修改提交按钮方法
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
            //导出excel
			$scope.fnExcel = function() {
				DT.excel(table,this.serchData,"代理商佣金账户管理");
			}
            
			// 派发佣金	
            function fnPost(){
            	var ids = DT.getSelect(table);
            	var data = DT.getRowData(table);
            	if(ids.length==0){
        			layer.msg('请选择数据', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;
            	}else if(ids.length>1){
            		layer.msg('只能选择一条数据', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;	
            	}else if(data[0].rewardMoney<=0){
            		layer.msg('待派发佣金为0,无法派发！', {
		    		    icon: 1,
		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
		    		});
            		return false;	
            	}else{  
            		var selectData = DT.getRowData(table)[0];
					$('#modifyInfo').removeClass("hide");
					$scope.rewardMoney=selectData.rewardMoney;
					$("#rewardMoney").val(selectData.rewardMoney);
					$("#agentName").val(selectData.appPersonInfo.trueName);
					$("#id").val(selectData.id);
					layer.open({
						type : 1,
						title : "派发佣金",
						closeBtn : 2,
						area : [ '450px', '400px' ],
						shadeClose : true,
						content : $('#modifyInfo')
					});
            	}
            }
            
            
          //修改提交按钮
			function modifySubmit(){
				debugger;
				//检验金額
				var id = $("#id").val();//appaccount的id
				var money = $("#money").val();//派发金额
				if(money==undefined ||money == ""){
					layer.msg("派发金额不能为空！", {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
				     return false;		
				}
				
				if (!(/^(-?\d*)\.?\d{1,4}$/.test(money))){
					$("#money").val("")
					layer.msg("只能输入数字(最多四位小数)!", {
					    		    icon: 1,
					    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					    		});
					     return false;
				}
				
				//校验金额大小
				if (money>$scope.rewardMoney){
					$("#money").val("")
					layer.msg("派发金额大于可派发金额！", {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
				     return false;
				}else{
					layer.confirm("确定派发佣金？", {
					btn : [ '确定', '取消' ], // 按钮
						id : id
					}, function() {
						$http.get(HRY.modules.customer+"fund/appaccount/postMoney?id="+id+"&money="+money).
		                 success(function(data) {
		                	layer.msg(data.msg, {
				    		    icon: 1,
				    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				    		});
		                	layer.closeAll();
		                	fnList();
		                 });
					});
				}
			}
            //添加按钮
            function fnAdd(url){}
            
            //查看按钮
            function fnSee(url){}
            
            //修改按钮
            function fnModify(url){}
            
            //删除按钮
            function fnRemove(url){}
        //}
            
        
        hryCore.initPlugins();
        //上传插件
		hryCore.uploadPicture();
        
    }
    return {controller:controller};
});