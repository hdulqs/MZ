/**
 * 全局 增删改查 要在模块中注入该 service eg:
 * 
 * define(['app','hryCore','angular','ngRoute'], function (app,hryCore) {
 * 
 * //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject app.controller.$inject =
 * ['$scope','$rootScope','$http','$stateParams','hryCore']; function
 * controller($scope,$rootScope,$http,$stateParams,hryCore){ // to do }) })
 * 
 * 调用方式：hryCore.save(),hryCore.update(),hryCore.load(),hryCore.list(),hryCore.remove();
 */
define([ 'app', 'angular' ], function(app, angular) {

	app.factory('hryCore', [
			'$http',
			'$q',
			'$resource',
			function($http, $q, $resource) {

				/**
				 * CURD 操作 options.method options.url options.parameter 参数 使用方法：
				 * function list(parameter) {
				 * 
				 * hryCore.CURD( { url : HRY.modules.factoring +
				 * 'loanEenterprise/custeloan/list', parameter : parameter
				 * }).query(function(data) { $scope.selected = {}; $scope.list =
				 * data.rows; for ( var id in $scope.list) {
				 * $scope.selected[$scope.list[id].id] = false; } },
				 * function(data) { growl.addInfoMessage("error:" + data.msg,
				 * '1000'); });
				 *  }
				 */
				function CURD(options) {
					return $resource(options.url, {}, {
						query : {
							method : 'POST',
							params : options.parameter,
							isArray : false
						},
						get : {
							method : 'GET'
						},
						remove : {
							method : 'DELETE'
						},
						save : {
							method : 'POST'
						}
					});
				}
				/**
				 * 全局datatable选项
				 */
				function dtOptions(DTOptionsBuilder, option, $compile, $scope) {

					var dtOptions = DTOptionsBuilder.newOptions()/*
																	 * .withOption(
																	 * 'ajax', {
																	 * data :
																	 * function(d) {
																	 * d.page =
																	 * d.start/d.length+1;
																	 * d.pageSize =
																	 * d.length; },
																	 * url
																	 * :option.url,
																	 * type :
																	 * 'post' })
																	 */
					.withOption('sAjaxSource', option.url)
					/*
					 * .withOption('fnServerParams', function(aoData){ //自定义查询条件
					 * objToArr(option.params==undefined?{}:option.params,aoData);
					 * //分页信息 aoData.push( { "name": "page", "value":
					 * aoData[3].value/aoData[4].value+1 }, { "name":
					 * "pageSize", "value": aoData[4].value } );
					 * 
					 *  })
					 */.withFnServerData(

					function serverData(sSource, aoData, fnCallback, oSettings) {
						aoData.push({
							"name" : "page",
							"value" : aoData[3].value / aoData[4].value + 1
						}, {
							"name" : "pageSize",
							"value" : aoData[4].value
						});
						oSettings.jqXHR = $.ajax({
							'dataType' : 'json',
							'type' : 'POST',
							'url' : sSource,
							'data' : aoData,
							'success' : fnCallback
						});
					}

					)

					.withDataProp('rows').withOption('processing', option.processing == undefined ? true : option.processing).withOption('serverSide', option.serverSide == undefined ? true : option.serverSide).withOption('autoWidth', false).withOption('lengthChange', option.lengthChange == undefined ? false : option.lengthChange).withOption('filter', option.filter == undefined ? false : option.filter).withOption('deferRender', true).withOption('displayLength', 2).withOption('lengthMenu',
							[ [ 5, 10, 25, 50, -1 ], [ 5, 10, 25, 50, "全部" ] ])

					.withPaginationType('full_numbers').withOption('aoColumns', []).withOption('createdRow', function(row, data, dataIndex) {
						// Recompiling so we can bind Angular directive to the
						// DT
						$compile(angular.element(row).contents())($scope);
					}).withOption('headerCallback', function(header) {
						if (!$scope.headerCompiled) {
							// Use this headerCompiled field to only compile
							// header once
							$scope.headerCompiled = true;
							$compile(angular.element(header).contents())($scope);
						}
					}).withLanguage({
						"processing" : "处理中...",
						"lengthMenu" : "显示 _MENU_ 项结果",
						"zeroRecords" : "没有匹配结果",
						"info" : "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
						"sInfoEmpty" : "显示第 0 至 0 项结果，共 0 项",
						"infoFiltered" : "(由 _MAX_ 项结果过滤)",
						"infoPostFix" : "",
						"search" : "搜索:",
						"url" : "",
						"emptyTable" : "表中数据为空",
						"loadingRecords" : "载入中...",
						"infoThousands" : ",",
						"paginate" : {
							"first" : "首页",
							"previous" : "上页",
							"next" : "下页",
							"last" : "末页"
						},
						"aria" : {
							"sortAscending" : ": 以升序排列此列",
							"sortDescending" : ": 以降序排列此列"
						}
					});

					return dtOptions;
				}

				/**
				 * datatable 数据列
				 */
				function dtColumns(DTColumnBuilder, option, $scope) {
					$scope.selected = {};// 选择数据
					$scope.selectAll = false;// 是否全选
					$scope.toggleAll = toggleAll; // 全选
					$scope.toggleOne = toggleOne;// 选择一条
					$scope.s_reset = function s_reset() {
						$scope.searchData = {}
					};// 查询页面重置
					var titleHtml = '<input type="checkbox" id="checkboxAll" ng-model="selectAll" ng-click="toggleAll(selectAll, selected)"> <label for="checkboxAll"> </label>';
					dtColumns = [];
					if (option.select == undefined || option.select) {
						var select = DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable().renderWith(function(data, type, full, meta) {
							$scope.selected[full.id] = false;
							return '<input type="checkbox" id="checkbox' + data.id + '" ng-model="selected[' + data.id + ']" ng-click="toggleOne(selected)"> <label for="checkbox' + data.id + '"> </label>';
						});

						dtColumns.push(select);
					}

					$scope.dtColumns = dtColumns;
					return $scope;
				}

				/**
				 * <p>
				 * 全选
				 * </p>
				 * 
				 * @param:
				 * @param selectAll
				 * @param:
				 * @param selectedItems
				 * @return: void
				 * @throws
				 */
				function toggleAll(selectAll, selectedItems) {
					for ( var id in selectedItems) {
						if (selectedItems.hasOwnProperty(id)) {
							selectedItems[id] = selectAll;
						}
					}
				}

				/**
				 * 选择一条
				 */
				function toggleOne(selectedItems) {
					var me = this;
					for ( var id in selectedItems) {
						if (selectedItems.hasOwnProperty(id)) {
							if (!selectedItems[id]) {
								me.selectAll = false;
								return;
							}
						}
					}
					me.selectAll = true;
				}

				/**
				 * <p>
				 * 将对象中 为true 的对象解析为数组，用于删除操作
				 * </p>
				 * 
				 * @param:
				 * @param obj
				 * @param:
				 * @returns {Array}
				 * @return: Array
				 * @throws
				 */
				function transform(obj) {
					var arr = [];
					for ( var item in obj) {
						if (obj[item]) {
							arr.push(item)
						}
						;
					}
					return arr;
				}
				/**
				 * 将{aaa:111,bbb:222,ccc:333} 类型的对象 转换为
				 * [{name:aaa,value:111},{name:bbb,value:222}] 数组
				 * 
				 * @arr 要返回的数组
				 */
				function objToArr(obj, arr) {

					for ( var item in obj) {
						if (obj[item]) {

							arr.push({
								"name" : item,
								"value" : obj[item]
							})
						}
						;
					}

					return arr;
				}

				function alertModal() {
					$.layer({
						type : 2,
						title : '上传文件列表',
						shadeClose : true,
						shade : [ 0.5, '#000', true ],
						move : [ '.xubox_title', true ],
						zIndex : 19891014,
						maxmin : true,
						fix : false,
						area : [ '1024px', 500 ],
						iframe : {
							src : ctx + "/file/appFilestViewWin.do?mark=" + mark
						}
					});
					document.body.style.overflow = "hidden";
				}

				/**
				 * 加载后渲染 js插件
				 */
				function initPlugins() {
					// 加载后 渲染MaterialPlugins();
					// conApp.initSearchBar();
					conApp.initPlugins();
					conApp.initMaterialPlugins();

				}

				/**
				 * 数组转字符串 add by liushilei
				 */
				function uploadPicture() {
					init();
					// 初始化
					function init() {
						// 初始化图片上传
						var btnImg = document.getElementById("btnUploadImg");
						var img = document.getElementById("imgShow");
						var imgSrc = document.getElementById("imgSrc");

						if (btnImg != null && img != null && imgSrc != null) {
							document.getElementById("btnDeleteImg").onclick = function() {
								DelImg(img, imgSrc);
							};
							g_AjxUploadImg(btnImg, img, imgSrc);
						}
						
						
						
						

					}

					// 图片上传
					function g_AjxUploadImg(btn, img, hidPut) {

						var button = btn, interval;
						new AjaxUpload(button, {
							action : HRY.modules.web+"file/upload",
							data : {},
							name : 'myfile',
							onSubmit : function(file, ext) {
								if (!(ext && /^(jpg|JPG|png|PNG|gif|GIF|bpm|BPM)$/.test(ext))) {
									alert("您上传的图片格式不对，请重新选择！");
									return false;
								}
							},
							onComplete : function(file, response) {
								flagValue = response;
								if (flagValue == "1") {
									alert("您上传的图片格式不对，请重新选择！");
								} else if (flagValue == "2") {
									alert("您上传的图片大于200K，请重新选择！");
								} else if (flagValue == "3") {
									alert("图片上传失败！");e
								} else {
									var resp = JSON.parse(response)
									hidPut.value = resp.obj[0].fileWebPath;
									img.src = "/" + resp.obj[0].fileWebPath;
								}
								
								
							}
						});
					}

					// 删除图片
					function DelImg(img, hidPut) {
						hidPut.value = "";
						img.src = "";
					}

				}

				/**
				 * 数组转字符串 add by liushilei 2015/12/21
				 */
				function ArrayToString(arr) {
					if (arr == null || arr == undefined || arr == "") {
						return "";
					}
					var str = "";
					for (var i = 0; i < arr.length; i++) {
						str += arr[i];
						if (i != arr.length - 1) {
							str += ",";
						}
					}
					return str;
				}

				/**
				 * 回显简单下拉框,单个回显 params select : 传这个下拉框的jquery对像 params value :
				 * 传这个下拉框的value值 add by liushilei 2016/01/07
				 */
				function RenderSelect(select, value) {

					select.find("option").each(function(i, option) {
						if ($(option).attr("value") == value) {
							$(option).attr("selected", "selected");
						}
					});
					conApp.initMaterialPlugins();
				}

				/**
				 * 回显简单下拉框,批量回显 params model : 传数据模型对象 add by liushilei
				 * 2016/06/07
				 */
				function RenderAllSelect(model) {
					 
					var selects = $("select[id]");
					for (var i = 0; i < selects.length; i++) {
						var id = $("select[id]")[i].id
						var options = $("#" + id + " option");
						for (var j = 0; j < options.length; j++) {
							var op = $(options[j]);
							if (op.attr("value") == eval("model." + id)) {
								op.attr("selected", "selected");
								break;
							}
						}
					}

					conApp.initMaterialPlugins();
				}

				/**
				 * 重置表单 params model : 传数据模型对象 add by gaomm 2016/06/16
				 */
				function reset(model) {
					$.each(model, function(name, value) {
						eval("model." + name + "=''");
					});
					$(".select2-selection__rendered").each(function(){
		    			var placeholder=undefined;
		    			 placeholder=$(this).parents("hry-select").attr("placeholder");
		    			 if(placeholder===undefined||placeholder==""){
		    				 placeholder=$(this).parent().parent().parent().prev().attr("data-placeholder");
		    			 }
		    			
		    			 $(this).html("<font style='color:#A4A4A4'>"+placeholder+"</font>");
		    		})
					return model;
				}

				/**
				 * 渲染HTML add by liushilei 2015/12/21
				 */
				function RenderHTML(data) {


					var echoData = data;

					$("section").find("hry-select").each(function(i, hrySelect) {

						renderSelect(hrySelect, null);

						// 生成html方法
						function createSelect(select) {
							var html;
							var selectId = select.attr("selectId");
							if (select.attr("multiple") != undefined) {
								if (select.attr("disabled") != undefined) {
									html = "<select disabled class='select2' multiple='' id='" + selectId + "' >";
								} else {
									html = "<select class='select2' multiple='' id='" + selectId + "' >";
								}
							} else {
								if (select.attr("disabled") != undefined) {
									html = "<select disabled id='" + selectId + "' >";
								} else {
									html = "<select  id='" + selectId + "' >";
								}
							}
							return html;
						}

						// 渲染下拉框
						function renderSelect(hrySelect, parentValue) {

							if (hrySelect == null && hrySelect == undefined) {
								return false;
							}
							var select = $(hrySelect);
							select.children().remove();
							var url = select.attr("url"); // 请求url地址
							var selectId = select.attr("selectid"); // selectId
																	// 用于post取值与回显取值
							var parentId = select.attr("parentid"); // 二级连动Id
							var disabled = select.attr("disabled"); // 二级连动Id
							var data = {};
							if (parentValue != null) {
								data[parentId] = parentValue;
							}
							$.ajax({
								type : "POST",
								url : url,
								selectId : selectId,// 异步回调时鉴别下拉框
								data : data,
								success : function(data) {

									var selectId = this.selectId;
									var str = "hry-select[selectId=" + this.selectId + "]"
									var select = $(str);
									select.children().remove();
									var name = select.attr("name"); // 显示字段
									var value = select.attr("value"); // 值字段

                                    if (typeof data === "string" && data.constructor === String) {
                                        data = $.parseJSON(data);
                                    }
									if (data != null && data.length > 0) {

										// 回显key对应的值
										try {
											var echo = eval("echoData." + selectId);
											// 回显数据和下拉数据进行比对
											function hasValue(dataValue) {
												try {
													if (echo != null && echo != undefined && echo.length > 0) {
														for (var i = 0; i < echo.length; i++) {
															if (dataValue == eval("echo[i]." + value)) {
																return true;
															}
														}
													}
													
													if(echo==dataValue){
														return true;
													}
												} catch (e) {
												}
												return false;
											}
											// 字符串
											function hasValueStr(dataValue) {
												if (echo != null && echo != undefined && echo.length != "") {
													var strArr = echo.split(",");
													for (var i = 0; i < strArr.length; i++) {
														if (dataValue == strArr[i]) {
															return true;
														}
													}
												}
												return false;
											}

										} catch (error) {
											var echo = null;
										}
										// 生成html头
										var html = createSelect(select);

										if (echo != null) {
											if (echo.constructor === String) {
												for (var i = 0; i < data.length; i++) {

													if (hasValueStr(eval("data[i]." + value))) {
														html += "<option selected value='" + eval("data[i]." + value) + "' >" + eval("data[i]." + name) + "</option>";
													} else {
														html += "<option value='" + eval("data[i]." + value) + "' >" + eval("data[i]." + name) + "</option>";
													}

												}
											} else {
												for (var i = 0; i < data.length; i++) {

													if (hasValue(eval("data[i]." + value))) {
														html += "<option selected value='" + eval("data[i]." + value) + "' >" + eval("data[i]." + name) + "</option>";
													} else {
														html += "<option value='" + eval("data[i]." + value) + "' >" + eval("data[i]." + name) + "</option>";
													}

												}
											}
										} else {

											for (var i = 0; i < data.length; i++) {
												html += "<option value='" + eval("data[i]." + value) + "' >" + eval("data[i]." + name) + "</option>";

											}

										}

										// 生成html尾
										html += "</select>";
										select.append(html);
									} else {
										var html = createSelect(select);
										html += "<option value=''>无数据</option></select>";
										select.append(html);
									}

									// 渲染下拉框
									conApp.initPlugins();
									conApp.initMaterialPlugins();
									// 开启监听
									startLintener();

									// 查找是否有子下拉框

									var sonSelect = $("hry-select[parentId=" + selectId + "]");
									if (sonSelect != null && sonSelect != undefined) {
										for (var i = 0; i < sonSelect.length; i++) {
											renderSelect(sonSelect[i], $("#" + selectId).val());
										}
									}

								},
								error : function(e) {
									var str = "hry-select[selectId=" + this.selectId + "]"
									var select = $(str);
									var html = createSelect(select);
									html += "<option value=''>加载异常，请检查后台方法</option></select>";
									select.append(html);
									// 渲染下拉框
									conApp.initPlugins();
									conApp.initMaterialPlugins();
								}
							});
						}

						// 渲染完成后开启监听事件
						// ------------------------------监听事件----------------------------------------
						function startLintener() {
							$("div.select-wrapper ul").find('li:not(.optgroup)').each(function(i) {
								$(this).click(function(e) {

									var selectId = $(this).parent().parent().parent().attr("selectId");
									var str = "hry-select[parentId=" + selectId + "]"
									var sonSelect = $(str);
									// 如果没有直接返回
									if (sonSelect == null || sonSelect == undefined || sonSelect == "" || sonSelect.length == 0) {
										return;
									}
									sonSelect.children().remove();// 删除子元素

									var selectValue = $("#" + selectId).val();
									var data = {}
									data[selectId] = selectValue;// 参数默认为parentId

									for (var i = 0; i < sonSelect.length; i++) {

										var select = $(sonSelect[i]);
										var url = select.attr("url"); // 请求url地址
										var name = select.attr("name"); // 显示字段
										var value = select.attr("value"); // 值字段
										var selectId = select.attr("selectid"); // selectId
																				// 用于post取值与回显取值
										var select2 = select.attr("multiple"); // 复选框
																				// 有这个属性表示是复选框
										var parentId = select.attr("parentid"); // 二级连动Id

										// 重新定义此方法，防止冲突
										function isSelect2(select2) {
											var html;
											if (select2 != undefined) {
												html = "<select class='select2' multiple='' id='" + selectId + "' >";
											} else {
												html = "<select  id='" + selectId + "' >";
											}
											return html;
										}

										$.ajax({
											type : "POST",
											url :  url,
											data : data,
											selectId : selectId,// 异步回调时鉴别下拉框
											success : function(data) {
												var selectId = this.selectId;
												var str = "hry-select[selectId=" + this.selectId + "]"
												var select = $(str);
												select.children().remove();
                                                if (typeof data === "string" && data.constructor === String) {
                                                    data = $.parseJSON(data);
                                                }
												if (data != null && data.length > 0) {
													// 回显key对应的值
													try {
														var echo = eval("scope.formData." + selectId);
													} catch (error) {
														var echo = null;
													}

													var html = createSelect(select);

													for (var i = 0; i < data.length; i++) {
														if (eval("data[i]." + value) == echo) {
															html += "<option selected value='" + eval("data[i]." + value) + "' >" + eval("data[i]." + name) + "</option>";
														} else {
															html += "<option value='" + eval("data[i]." + value) + "' >" + eval("data[i]." + name) + "</option>";
														}
													}
													html += "</select>";
													select.append(html);
												} else {
													var html = createSelect(select);
													html += "<option value=''>无数据</option></select>";
													select.append(html);
												}

												// 渲染下拉框
												conApp.initPlugins();
												conApp.initMaterialPlugins();
												// 再次开启监听
												startLintener();

											},
											error : function(e) {
												var str = "hry-select[selectId=" + this.selectId + "]"
												var select = $(str);
												var html = createSelect(select);
												html += "<option value=''>加载异常，请检查后台方法</option></select>";
												select.append(html);
												// 渲染下拉框
												conApp.initPlugins();
												conApp.initMaterialPlugins();
											}

										});

									}

								});

							});
							// ------------------------------监听事件----------------------------------------
						}

					});

				
				
				}

				return {
					dtOptions : dtOptions,
					dtColumns : dtColumns,
					CURD : CURD,
					initPlugins : initPlugins,
					toggleAll : toggleAll,
					toggleOne : toggleOne,
					transform : transform,
					uploadPicture : uploadPicture,// 上传插件
					ArrayToString : ArrayToString, // ["1","2","3"]数组转字符串 1,2,3

					RenderHTML : RenderHTML, // 下动态下拉框方法
					RenderSelect : RenderSelect, // 回显表态单个下拉框
					RenderAllSelect : RenderAllSelect,// 回显静态全部下拉框
					reset : reset
				// 重置表单
				}

			} ])

})
