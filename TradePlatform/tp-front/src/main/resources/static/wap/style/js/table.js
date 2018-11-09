define(function(require, exports, module) {
	

	module.exports = {
		/**
		 * 批量删除数据
		 */
		removeRow : function(table,url){
			var ids = $.map(table.bootstrapTable('getSelections'), function(row) {
				return row.id
			});
			if(ids!=undefined&&ids.length>0){
				
				layer.confirm('你确定要删除吗？', {
	    			btn: ['确定','取消'],
	    			ids : ids
				}, function(){
					$.ajax({
						type : "post",
						url : url,
						cache : false,
						dataType : "json",
						data:{
							ids : ids.join(",")
						},
						success : function(data) {
							if(data!=undefined){
								if(data.success){
									layer.msg('删除成功', {icon: 1});
									//table刷新
									table.bootstrapTable('refresh');
								}else{
									layer.msg(data.msg, {icon: 2});
								}
							}else{
								layer.msg("请求无响应", {icon: 2});
							}
						},
						error : function(e) {
							$("#page-wrapper").html("<div class='row'><h1>此路径不存在："+url.substring(url.indexOf("u=")+2)+"</h1></div>");
						}
					});
					
				})
				
				
			}else{
				layer.msg('请选择数据', {icon: 2});
				return false;
			}
		},
		/**
		 * 查询一行数据
		 */
		seeRow : function(table,url){
			var ids = $.map(table.bootstrapTable('getSelections'), function(row) {
				return row.id
			});
			if(ids!=undefined&&ids.length==1){
				loadUrl(url+"/"+ids[0]+".do");
			}else{
				layer.msg('请选择一条数据', {icon: 2});
			}
		},
		/**
		 * 获得表格选中的ID
		 */
		getIdSelects : function(table){
		  return $.map(table.bootstrapTable('getSelections'), function(row) {
				return row.id
			});
		},
		initTable : function(table, conf) {
			var $table = table, selections = [];
			var $remove = $('#remove');

			// sometimes footer render error.
			// 延时渲染
			setTimeout(function() {
				$table.bootstrapTable('resetView');
			}, 200);

			// checkBox单击事件
			$table.on('check.bs.table uncheck.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function() {
				// $remove.prop('disabled',
				// !$table.bootstrapTable('getSelections').length);
				selections = getIdSelections();
			});
			
			//选中变色
//			$table.on('click-row.bs.table', function (e, row, $element) {
//	            $('.info').removeClass('info');
//	            $($element).addClass('info');
//
//	            
//				debugger
//				//设置透明
//				$("#test_div").css("opacity","0.95");
//	            if($("#test_div").attr("show")=="true"){
//	            	$("#test_div").animate({left:"100%"});
//	            	$("#test_div").attr("show","false");
//	            	$('#test_div').css('display','none');
//	            }else{
//	            	//表格第二列的left值
//	            	var left = $($(table).find("th[data-field]")[3]).offset().left;
//	            	$('#test_div').css('display','block')
//	            	$("#test_div").animate({left:left,width:document.documentElement.clientWidth-left});
//	            	$("#test_div").attr("show","true");
//	            }
//	            
//	        });
			

			// 展开事件,渲染方法由页面传入
			if (conf.detail != undefined) {
				$table.on('expand-row.bs.table', conf.detail);
			}

			/**
			 * 所有的事件都会触发该事件，参数包括： name：事件名， args：事件的参数
			 */
			// $table.on('all.bs.table', function(e, name, args) {
			// console.log("hello");
			// console.log(name, args);
			// });
			
			
			// 删除按钮
//			$remove.click(function() {
//				var ids = getIdSelections();
//				//删除完重新请求
//				$table.bootstrapTable('refresh');
//			});

			$(window).resize(function() {
				$table.bootstrapTable('resetView', {
				// height : getHeight()
				});
			});

			window.operateEvents = {
				'click .like' : function(e, value, row, index) {
					alert('You click like action, row: ' + JSON.stringify(row));
				},
				'click .remove' : function(e, value, row, index) {
					$table.bootstrapTable('remove', {
						field : 'id',
						values : [ row.id ]
					});
				}
			};

			/**
			 * 获取选中的ID
			 */
			function getIdSelections() {
				return $.map($table.bootstrapTable('getSelections'), function(row) {
					return row.id
				});
			}

			function getHeight() {
				return $(window).height() - $('h1').outerHeight(true);
			}
			
			/**
			 * bootstrap table  init
			 */
			//查询条件
			function queryParams(params){
				$params=$("input[tablesearch]");
				$params.each(function(i,o){
					if(o.value.length!=0){
						params[o.name]=o.value;
					}else{
						params[o.name]=null;
					}
				})
				return params;
			}
			

			
			if(conf.url){//服务器
				/**
				 * bootstrap table  init
				 */
				$table.bootstrapTable({
					columns : conf.columns,
					url : conf.url,
					queryParams:conf.queryParams
				});
			}else{//静态
				$table.bootstrapTable({
					// height: getHeight(),
					columns : conf.columns,
					data : conf.data
				});
			}
			
			
			//监听输入框变化
			$params=$("input[tablesearch]");
			$params.change(function(){
				$table.bootstrapTable('refresh',{
					columns : conf.columns,
					url : conf.url,
					queryParams:queryParams
				});
			});

		},
		getHiddenColumns : function(table,name){
			table.bootstrapTable('hideColumn', name);
		},
		getShowColumn : function(table,name){
			return table.bootstrapTable('showColumn', name);
		},
		bootstrapRefresh : function(table){
			table.bootstrapTable('refresh');
		}
	}

});