/**
 * add by Yuanzc 互融云树 使用 htm中加入 <div id="tree" class="ztree"></div>
 * 
 * //引入 htmleditor define(['app','htmleditor'], function (app) { //注入 htmleditor
 * app.controller.$inject = ['htmleditor']; function controller(htmleditor){
 * 
 * function loadDictionaryList(typeid){
 * htmleditor.conf.dataurl=HRY.modules.web+"dictionary/appdicmultilevel/findlist.do?rootKey="+typeid;
 * htmleditor.conf.fnModify=fnModify; htmleditor.conf.fnAdd=fnAdd;
 * htmleditor.conf.fnRemove=fnRemove; htmleditor.conf.check=true; //是否复选框 // "tree"
 * 为html 中 id="tree" 的值 同一个页面可以 支持多个树 只要 如 id="tree1" ,id="tree2" ...
 * htmleditor.init("tree"); }
 */
define([ "app", "layer", "ztree" ], function(app, layer) {

	app.factory('htmleditor', [ '$http', '$q', '$resource', function($http, $q, $resource, $scope) {

		function config() {
			var conf = {
				setting : {

					edit : {
						enable : false,
						showRemoveBtn : showRemoveBtn,
						showRenameBtn : showRenameBtn,
						removeTitle : "删除",
						renameTitle : "修改"
					},
					view : {
						addHoverDom : addHandle,
						removeHoverDom : removeAddHandle,
						dblClickExpand : false,
						selectedMulti : false
					},
					data : {

						simpleData : {
							enable : true,
							id : "id",
							idKey : "mkey",
							pIdKey : "pkey",
							rootPId : ""
						}

					},
					check : {
						autoCheckTrigger : false,
						chkboxType : {
							"Y" : "ps",
							"N" : "ps"
						},
						chkStyle : "checkbox",
						enable : false,
						nocheckInherit : false,
						chkDisabledInherit : false,
						radioType : "level"

					},

					callback : {
						beforeDblClick : beforeDblClick,
						// beforeEditName: beforeEditName,
						// beforeRemove: beforeRemove,
						beforeCheck : beforeCheck,
						onCheck : onCheck,
						//onClick: zTreeOnClick,
					// beforeRename: beforeRename,
					// onRemove: onRemove
					// onRename: onRename
					}
				},
				dataurl : "",
				sort : false,
				check : false,
				fnDblClick : null,// 双击选中
				fnModify : null, // 修改
				fnAdd : null, // 添加
				fnMove : null, // 移动
				fnRemove : null, // 删除
				fnUp : null, // 上排序
				fnDown : null
			// 下排序
			};
			
			
		/*	function zTreeOnClick(event, treeId, treeNode) {
			    //alert(treeNode.tId + ", " + treeNode.name);
			    loaa(treeNode);
			};*/
			
			
			// 添加节点
			function addHandle(treeId, treeNode) {
				var sObj = $("#" + treeNode.tId + "_span");
				if (treeNode.editNameFlag || $("#removeBtn_" + treeNode.tId).length > 0)
					return;
				var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='添加' onfocus='this.blur();'></span>";
				//var upStr = "<span class='button up' id='upBtn_" + treeNode.tId + "' title='上移' onfocus='this.blur();'></span>";
				//var downStr = "<span class='button down' id='downBtn_" + treeNode.tId + "' title='下移' onfocus='this.blur();'></span>";

				var removeStr = "<span class='button remove' id='removeBtn_" + treeNode.tId + "' title='删除' onfocus='this.blur();'></span>";

				var editStr = "<span class='button edit' id='editBtn_" + treeNode.tId + "' title='修改' onfocus='this.blur();'></span>";

				if (conf.sort) {
					sObj.after(addStr+editStr + removeStr );
				} else {
					sObj.after(addStr+editStr + removeStr);
				}
				var btn = $("#addBtn_" + treeNode.tId);
				if (btn)
					btn.bind("click", function() {
						var zTree = $.fn.zTree.getZTreeObj(treeId);
						conf.fnAdd(treeNode);
						return false;
					});

				/*var upbtn = $("#upBtn_" + treeNode.tId);
				if (upbtn)
					upbtn.bind("click", function() {
						var zTree = $.fn.zTree.getZTreeObj(treeId);
						conf.fnUp(treeNode);
						return false;
					});

				var downbtn = $("#downBtn_" + treeNode.tId);
				if (downbtn)
					downbtn.bind("click", function() {
						var zTree = $.fn.zTree.getZTreeObj(treeId);
						conf.fnDown(treeNode);
						return false;
					});
*/
				var removebtn = $("#removeBtn_" + treeNode.tId);
				if (removebtn)
					removebtn.bind("click", function() {
						var zTree = $.fn.zTree.getZTreeObj(treeId);
						return layer.confirm('确定删除吗？', {
							btn : [ '确定', '取消' ]
						// 按钮
						}, function() {
							conf.fnRemove(treeNode);
						});
						return false;
					});

				var editbtn = $("#editBtn_" + treeNode.tId);
				if (editbtn)
					editbtn.bind("click", function() {
						var zTree = $.fn.zTree.getZTreeObj(treeId);
						conf.fnModify(treeNode);
						return false;
					});
			}
			;

			/**
			 * 删除自定义按钮
			 */
			function removeAddHandle(treeId, treeNode) {
				$("#addBtn_" + treeNode.tId).unbind().remove();
				$("#removeBtn_" + treeNode.tId).unbind().remove();
				$("#editBtn_" + treeNode.tId).unbind().remove();
				if (conf.sort) {
					$("#upBtn_" + treeNode.tId).unbind().remove();
					$("#downBtn_" + treeNode.tId).unbind().remove();
				}

			}
			
			/**
			 * 是否展示删除按钮
			 */
			function showRemoveBtn(treeId, treeNode) {
				// return !treeNode.isFirstNode;
				return true;
			}
			/**
			 * 是否展示修改按钮
			 */
			function showRenameBtn(treeId, treeNode) {
				// return !treeNode.isLastNode;
				return true;
			}
			
			/**
			 * 修改
			 */
			function beforeEditName(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj(treeId);
				conf.fnModify(treeNode);
			}
			
			/**
			 * 双击选中
			 */
			function beforeDblClick(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj(treeId);
				conf.fnDblClick(treeNode);
			}
			
			
			return conf;
		}
		// 选中的节点
		var checkNodes = null;
		// 参数
		var setting =
		// 设置树参数
		function initSetting() {
			/*
			 * conf.setting={ data: { simpleData: { enable:false } } }
			 */
			
			var cloneSetting = setting;
			var tempSetting = angular.extend({}, cloneSetting, conf.setting);
			return tempSetting;
		}

		/**
		 * 初始化树
		 */
		function init(treeId, conf,zNodes) {
			$.fn.zTree.init($("#" + treeId), conf.setting, zNodes);
			
		}


		var newCount = 1;


	


		function beforeRename(treeId, treeNode, newName, isCancel) {
			if (newName.length == 0) {
				alert("节点名称不能为空.");
				var zTree = $.fn.zTree.getZTreeObj(treeId);
				setTimeout(function() {
					zTree.editName(treeNode)
				}, 10);
				return false;
			}
			return true;
		}
		function onRename(e, treeId, treeNode, isCancel) {
			showLog((isCancel ? "<span style='color:red'>" : "") + "[  onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>" : ""));
		}

		function beforeRemove(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			zTree.selectNode(treeNode);
			return layer.confirm('确定删除吗？', {
				btn : [ '确定', '取消' ]
			// 按钮
			}, function() {
				conf.fnRemove(treeNode);
			});
			// return confirm("确认删除 节点" + treeNode.name + " 吗？");
		}
		/*
		 * function onRemove(e, treeId, treeNode) { conf.fnRemove(treeNode.id);
		 * showLog("[ onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name); }
		 */

		/**
		 * 全选
		 */
		function beforeCheck(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);

		}
		/**
		 * 全选 完成
		 */
		function onCheck(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			// checkNodes=zTree.getCheckedNodes();
		}

		/**
		 * 获取选中树的节点
		 */
		function getTreeNodes(treeId) {
			var zTree = $.fn.zTree.getZTreeObj(treeId);
			return zTree.getCheckedNodes();
		}

		return {
			config : config,
			init : init,
			getTreeNodes : getTreeNodes
		}

	} ])
})
