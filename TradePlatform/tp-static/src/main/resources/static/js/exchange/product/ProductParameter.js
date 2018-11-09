/**
 * student.js
 */
define(['app','hryTable'], function (app, DT) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams,hryCore ){
    	//初始化js 插件
    	
    	var table =  $("#table2");
    	
		//------------------------查看页面路径---------------------------------------------
	
		//------------------------增加页面路径---------------------------------------------
        if($stateParams.page=="add"){
        	
        	$scope.formData = {};
    			
    			layer.closeAll();
    			
    			$scope.processForm = function() {
    				
    	        	layer.confirm("添加参数如果状态是开启 将会关闭掉之前开启的同种币的参数？",{
    	    			btn: ['确定','取消'] //按钮
    	    			// ids: ids
    	    		}, function(){
    	    			layer.closeAll();
    	    			
    	    			$scope.formData.name = $('#productSet option:selected').text();
        				$scope.formData.state = $("#pamState").val();
        				$scope.formData.state = $("#schemaModel").val();
        				var ss= formData.leastPaceNum;
        				$http({
        					method : 'POST',
        					url : HRY.modules.exchange+'product/exProductparameter/add',
        					data : $.param($scope.formData), 
        				//	params:{'appResourceStr':ids},
        					headers : {	
        						'Content-Type' : 'application/x-www-form-urlencoded'
        					}
        				})
        				.success(function(data) {
        							if (data.success) {
        								growl.addInfoMessage('添加成功')
        								window.location.href='#/exchange/product/productparameter/list/anon';
        							} else {
        								growl.addInfoMessage('添加失败--- '+data.msg)
        							}
        				});
    	    		});
    			};
        }

      //------------------------修改页面路径---------------------------------------------
        if($stateParams.page=="modify"){

        	$http.get(HRY.modules.exchange+"/product/exproduct/see/"+$stateParams.ing).
                 success(function(data) {debugger;
                 
                 	 $scope.model = data;
                 	 $scope.formData = data;
    				 hryCore.RenderHTML(data);//异步回显控件
    				// $scope.paceFeeRate= $scope.formData.paceFeeRate;
                 	 if(data.priceLimits == 0 ){
                 	 	$scope.formData.priceLimits= 10;
                 	 }
                 		
                 	var a=$scope.formData.paceType;
                 	if(a==2){
                 		 $("#paceType").val("");
                 		 $("#two").show();
            			 $("#one").hide();
                 	}else{
                 		
                 		
                 		 $('#paceCurrecy').val("");
                 		 $("#two").hide();
            			 $("#one").show();
                 	}
                 	 $("#schemaModel").val(data.transactionType)
                 	 conApp.initMaterialPlugins();

            });
        	
        	
        	 var leve ="";
        	 $('#ddlRegType1').on('change',function () {debugger;
			    	//是否开启键入搜索
        		
        		 leve = $("#paceType ").val();
        		 if(leve=="2"){
        				//$("#paceFeeRate").val("");
        			 $("#two").show();
        			 $("#one").hide();
        			 
         		}else{
         			//$('#fixPriceType option:selected').val("");
         			 $("#two").hide();
        			 $("#one").show();
         			
         		}
			    });


      //  	$scope.formData = {};
			$scope.processForm = function() {debugger;
				
				layer.confirm("确定保存修改？",{
	    			btn: ['确定','取消'] //按钮
	    			// ids: ids
	    		}, function(){debugger;
	        		 var leve = $("#paceType").val();

	    			if(leve==2){
                 		$("#paceFeeRate").val("");
    	    			//$scope.formData.paceCurrecy = $("#paceCurrecy").val();
    	 				$scope.formData.paceCurrecy= hryCore.ArrayToString($("#paceCurrecy").val());

                 	}else{
                 		$('#paceCurrecy').val("");
    	    			$scope.formData.paceFeeRate = $("#paceFeeRate").val();

                 	}
	    			layer.closeAll();
	 				$scope.formData.paceCurrecy= hryCore.ArrayToString($("#paceCurrecy").val());

	    			$scope.formData.buyFeeRate = $("#buyFeeRate").val();
	    			$scope.formData.sellFeeRate = $("#sellFeeRate").val();
	    			//$scope.formData.prepaidFeeRate = $("#prepaidFeeRate").val();
	    			//$scope.formData.paceFeeRate = $("#paceFeeRate").val();
	    			$scope.formData.transactionType = $("#schemaModel").val();
	    		      if($("#paceFeeRate").val()==null||$("#paceFeeRate").val()==""){
	    		    	$scope.formData.paceType=2;
	    		    	  //$scope.formData.paceFeeRate=$('#paceCurrecy option:selected').val();
	    		    	 // $scope.formData.paceCurrecy=$('#paceCurrecy').val();
	  	 				$scope.formData.paceCurrecy= hryCore.ArrayToString($("#paceCurrecy").val());

	    		    	  
	    		      }else{
	    		    	  $scope.formData.paceType=1;
	    		    	  $scope.formData.paceCurrecy= $("#paceFeeRate").val();
	    		      }
	    		      var ss= $scope.formData.leastPaceNum;
	    		  /*    if(isEmpty($scope.formData.paceCurrecy)){
	 					 layer.msg("提币手续费不能为空", {
	 	 		    		    icon: 1,
	 	 		    		    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	 	 		    		});
	 	 		          return false;
	 				
	 				 } */
					$http({
						method : 'POST',
						url : HRY.modules.exchange+'product/exproduct/modifytwo',
						data : $.param($scope.formData), 
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded'
						}
					})
					.success(function(data) {
								console.log(data);
								if (!data.success) {
									$scope.errorName = data.msg;
								} else {
									$scope.message = data.msg;
									window.location.href='#/exchange/product/productparameter/list/anon';
							}
					});
	    		});
			};
        }
       // 
       	
        //------------------------列表页面路径---------------------------------------------
        if($stateParams.page=="list"){
        	
        	
        	
        	$scope.serchData={};
        	
        	//--------------------加载dataTable--------------------------------
        	var config = DT.config();
        		config.bAutoSerch = true; //是否开启键入搜索
        		config.ajax.url = HRY.modules.exchange+'product/exproduct/list?page=parameter';
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
										"data" : "name"
									}, {
										"data" : "coinCode"
									},{
										"data" : "paceType"
									},{
										"data" : "paceCurrecy"
									},{
										"data" : "oneDayPaceNum"
									}, {
										"data" : "leastPaceNum"
									}, {
										"data" : "giveCoin"
									}, {
										"data" : "openTibi"
									}]
        		config.columnDefs  = [
										{
											"targets" : 0,"orderable" :false,
											"render" : function(data, type, row) {
											return "<input type=\"checkbox\" id=\"checkbox"+data+"\" /><label for=\"checkbox"+data+"\"></label>"
										}
										},
										 {
											"targets" : 3,
									
											"render" : function(data, type, row) {
												if (data != null && data == "2") {
													return "手动配置"
												}
												return "固定费率"
											}
										},
										{
											"targets" : 8,

											"render" : function(data, type, row) {
												if (data != null && data == "1") {
													return "开启"
												}
												return "关闭"
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
            
            //刷新按钮
            function fnList(){
            	 table.DataTable().draw();
            }
            
            $scope.look = function(){  
            	alert(1)
            }    
            function look(){
            	alert(2)
            }
            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
            	window.location.href='#/exchange/'+url+'/anon';
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
            		window.location.href='#/exchange/'+url+'/'+ids[0];
            	}
            }
            
            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(url){
            	var ids = DT.getSelect(table);
            // 	
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}else{
            		$rootScope.id= ids[0];
            		window.location.href='#/exchange/'+url+'/'+ids[0];
            	}
            }
            
            //删除按钮
            //ng-click="fnRemove(url,selectes)"
            function fnRemove(url){
            	
            	var ids = DT.getSelect(table);
            	//var ids = transform(selectes);
            	if(ids.length==0){
            		growl.addInfoMessage('请选择数据')
            		return false;
            	}else if(ids.length>1){
            		growl.addInfoMessage('只能选择一条数据')
            		return false;	
            	}
            	// else{
            		// window.location.href='#/exchange/'+url+'/'+ids[0];
            	// }
            	
            	var id = ids[0]	;
            	
            	hryCore
				.CURD({
						url:HRY.modules.exchange+url+"/"+id
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