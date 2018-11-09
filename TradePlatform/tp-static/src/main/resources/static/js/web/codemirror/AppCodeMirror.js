/**
 * student.js
 */
define([ 'app' ,'hryTable' ,'htmleditor'], function(app,DT) {

	// 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
	app.controller.$inject = [ '$scope', '$rootScope', '$compile', '$http', '$stateParams', '$state' ,'htmleditor'];
	function controller($scope, $rootScope, $compile, $http, $stateParams, $state, hryCore,htmleditor) {

		// ------------------------查看页面路径---------------------------------------------
		
		
	
		// ------------------------配置应用页面路径---------------------------------------------
			var table1 = $("#table1");
			var table2 = $("#table2");
			$scope.flag1 = true;//标记table是否是第一次渲染
			$scope.flag2 = true;//标记table是否是第一次渲染
			var editor1=null;
			// 加载应用菜单树
			loadTree();
			function loadTree() {
				var zNodes = [];
				$.post(HRY.modules.web + "codemirror/appcodemirror/findByApp", {
					id : "1",
					type : "tree"
				}, function(data) {
					for (var i = 0; i < data.length; i++) {
						var item = {
							id : data[i].id,
							name : data[i].name,
							type : data[i].type,
							mkey : data[i].id,
							pkey : data[i].pid,
							open : true
						//    drop : true
						}
						zNodes.push(item);
					}
					var conf = htmleditor.config();
					// 初始化菜单树
					conf.sort=true;
					conf.fnModify=fnModify;
				/*	conf.fnUp=fnTreeUp;
					conf.fnDown=fnTreeDown;*/
			    	conf.fnAdd=fnTreeAdd;
			    	conf.fnRemove=fnTreeRemove;
			    	htmleditor.init("tree",conf,zNodes);
			    	gng();
					//树上的 添加按钮弹窗---窗体中的添加按钮
					$scope.fnTreeAddBtn = function(){
						var ids = DT.getSelect(table2);
						if (ids.length < 1) {
							growl.addInfoMessage("请选择功能菜单")
							return false;
						}
						
						$http({
							method : 'POST',
							url : HRY.modules.web + 'codemirror/appcodemirror/addnode',
							data : $.param({id:$scope.nodeId,functionIds : hryCore.ArrayToString(ids)}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							loadTree();
						});
						layer.closeAll();
					}
					function fnRemove(){
						
					}
					
					
					
					
					
					//树上的 修改按钮
					 function fnModify(treeNode){
					 $scope.nodeId =  treeNode.id;
					 
						$.post(HRY.modules.web + "codemirror/appcodemirror/findById", {
							id : treeNode.id,
							type : "select"
						},
						function(data) {
							  $.each(data,function(index,value){
								  remark=value.remark;
						if(remark==1){
							layer.msg("文件夹不可编辑", {
								icon : 1,
								time : 2000
							});
						}else{
						 $(".CodeMirror").remove();
						 $("#ye").hide();
						 $("#codemirr").show();
						 $("#tj").show();
						 $("#editcode").text("【"+treeNode.name+"】"+"正在编辑");
						 loaa(treeNode)
						}
					 })
				}, "json");
			}
					 
					
					 
					 function fnTreeAdd(treeNode){
					 $scope.nodeId =  treeNode.id;
						$.post(HRY.modules.web + "codemirror/appcodemirror/findById", {
							id : treeNode.id,
							type : "select"
						},
						function(data) {
							  $.each(data,function(index,value){
								  remark=value.remark;
						if(remark==0){
							layer.msg("文件不可添加", {
								icon : 1,
								time : 2000
							});
						}else{
							 $("#codemirr").hide();
							 $("#ye").show();
							 
							 $("#superiorMenuId").val(treeNode.mkey);
							 $("#superiorMenu").val(treeNode.name);
						}
							  })
				}, "json");
						 
		 }
					
					//修改菜单节点名称
					$scope.fnModifyNode = function(){
						var modifyNodeName =  $("#modifyNodeName").val();
						$http({
							method : 'POST',
							url : HRY.modules.web + 'codemirror/appcodemirror/modify',
							data : $.param({id:$scope.nodeId,name:modifyNodeName}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							loadTree();
						});
						layer.closeAll();
					}
					
					

					// 菜单上的删除按钮
					function fnTreeRemove(treeNode) {
						var id=treeNode.id;
						$http({
							method : 'POST',
							url : HRY.modules.web + 'codemirror/appcodemirror/remove',
							data : $.param({id:id}),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						}).success(function(data) {
							loadTree();
							layer.msg("删除成功", {
								icon : 1,
								time : 2000
							});
						});
					}

				}, "json");
			}

		
			/**
			 * 双击选中
			 */
			function fnDblClick(treeNode){
				$("#superiorMenu").val(treeNode.name);
				if (treeNode.id == -1) {
					$("#superiorMenuId").val(0);
				} else {
					$("#superiorMenuId").val(treeNode.id);
				}
				layer.closeAll();
			
			}

			//选择上级
			$scope.selectAppMenuTree = function() {
				var zNodes = [];
				function loadMenuList() {
					$.post(HRY.modules.web + "codemirror/appcodemirror/findByApp", {
						id : "1",
						type : "select"
					}, function(data) {
						for (var i = 0; i < data.length; i++) {
							var item = {
								id : data[i].id,
								mkey : data[i].id,
								pkey : data[i].pid,
								name : data[i].name,
								open : true
							// drop : false
							}
							zNodes.push(item);
						}
						// 移除隐藏样式
						$("#selectTreeDiv").removeClass("hide")
						
						// 初始化菜单树
					var conf = htmleditor.config();	
			    	conf.fnDblClick=fnDblClick;
					conf.setting.edit={};
					conf.setting.view={};
			    	htmleditor.init("selectTree",conf,zNodes);
						// 弹出选择窗体
						layer.open({
							type : 1,
							title : "双击选择上级菜单",
							closeBtn : 2,
							area : [ '530px', '400px' ],
							shadeClose : true,
							content : $('#selectTreeDiv')
						});

					}, "json");
				}
				loadMenuList();

			}

			
			// 刷新按钮
			function fnList() {
				table.DataTable().draw(); 
			}

		$scope.addye=function(){
				$("#codemirr").hide();
				$("#ye").show();
				gng();
				 
			}
			
			// 添加按钮
			$scope.fnAdd=function(treenode){
			$scope.formData = {};
			var cc=editor1.getValue();
			var orderNo=$("input[name='pay-type']:checked").val();
				// 上级菜单
				var superiorMenuId = $("#superiorMenuId").val();
				var name=$("#name").val();
				var pid = $("#superiorMenuId").val();
				var functionIds = $("#functionIds").val();
				if(name==undefined || name ==""){
					layer.msg("文件名称不能为空", {
						icon : 1,
						time : 2000
					});
					return false;
				}
				if (superiorMenuId == undefined || superiorMenuId == "") {
					layer.msg("上级菜单不能为空", {
						icon : 1,
						time : 2000
					});
					return false;
				}

				// 发送请求
				$scope.formData.superiorMenuId = orderNo;
				//$scope.formDate.remark = orderNo;
				$scope.formData.name =$("#name").val();
				$scope.formData.pid = pid;
				$scope.formData.content = cc;
				$.ajax({
				    type: "post",
				    url: HRY.modules.web + 'codemirror/appcodemirror/add',
				    data: $.param($scope.formData),
				    success: function(data) {
				        layer.msg("配置成功", {
				            icon: 1,
				            time: 2000

				        });
				        $state.go("web.codemirror_appcodemirror", {
				            id: $stateParams.id,
				            page: "list"
				        },
				        {
				            reload: true
				        });
				    },
				    error: function(e) {
				        layer.alert("添加失败")
				    }
				});


			}

			
			
			function gng(){
				
				 editor1 = CodeMirror.fromTextArea(document.getElementById("code2"), {
				 	  //显示行号
				      lineNumbers: true, 
				      //括号匹配
				      matchBrackets: true,
				      indentWithTabs: true,
				      smartIndent: true,
				      autofocus: true ,
				 	  theme: "custom", 
				 	  autoMatchParens: true ,
				 	  //html联想
				 	  mode: 'text/html',
				 	  //mode:"text/x-java",
				 	  autoCloseBrackets: true,
				 	  extraKeys: {"Ctrl": "autocomplete"},
				    }); 
			}
			
			
			 var id=null;
			 var editor =null;
			 var content=null;
			function loaa(treeNode){
				$("#aa").show();
			      	$(".CodeMirror").empty();
						$.post(HRY.modules.web + "codemirror/appcodemirror/findById", {
							id : treeNode.id,
							type : "select"
						},
						function(data) {
						  $.each(data,function(index,value){
					           content=value.content;
						  id=treeNode.id;
						  editor = CodeMirror.fromTextArea(document.getElementById("code"), {
							  //显示行号
				 		      lineNumbers: true, 
				 		      //括号匹配
				 		      matchBrackets: true,
				 	          indentWithTabs: true,
				 	          smartIndent: true,
				 	          autofocus: true ,
				 	     	  theme: "custom", 
				 	     	  //是否自动换行
				 	     	  lineWrapping:false,
				 	     	  autoMatchParens: true ,
				 	     	  //联想
				 	     	  mode: {  
				                    name: "text/x-mysql"  
				                },  
				                theme: "3024-day", //主题    
				 			   autoCloseBrackets: true,
				 			   extraKeys: {"Ctrl": "autocomplete"},
				 		    });  
						  
						       editor.setValue(content);
						  
					  })

				}, "json");
						   

			}
			$scope.update=function(){
				var cc=editor.getValue(); 
				if(id!=null){
				layer.confirm("你确定要修改吗？", {
					btn : [ '确定', '取消' ], // 按钮
				}, function() {
					layer.closeAll();
					$.post(HRY.modules.web + "codemirror/appcodemirror/modify", {
						id :id,content:cc,
						type : "select"
					},
					function(data) {
						layer.msg("修改成功", {
							icon : 1,
							time : 2000
							
						});
						$state.go("web.codemirror_appcodemirror", {
							id : $stateParams.id,
							page : "list"
						}, {
							reload : true
						});
					
				     })
				   })
				}
				else{
					 layer.alert('请在左侧选择');
				 }
			}

			


		hryCore.initPlugins();
		// 上传插件
		hryCore.uploadPicture();

	}
	return {
		controller : controller
	};
});