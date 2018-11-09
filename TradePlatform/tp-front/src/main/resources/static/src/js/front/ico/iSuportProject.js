define(function(require,exports,module){
	this._table = require("js/base/table");
	this.md5 = require("js/base/utils/hrymd5")
	
	module.exports = {
		init : function(){
			//清除定时器
			clearPageTimer();
			window.seeProject=function(p){
				//展示，就是项目浏览
			}
			window.modifyProject=function(id,step){
				loadUrl(_ctx+"/ico/launch?projectId="+id+"&step="+step);
			}
			window.removeProject=function(id,step){
				layer.open({
					  content: '确定要删除？',
					  btn: ['确定', '取消'],
					  btn1: function(index, layero){
						  $.ajax({
							  type:'post',
							  url:_ctx+"/iLaunchProject/remoteProject",
							  data:{
								  projectId:id
							  },
							  dataType:'json',
							  success:function(data){
								  if(data.success){
									  layer.alert("操作成功");
									  $("#table").bootstrapTable('refresh',null);
								  }
							  }
						  })
					  },cancel: function(){
						  //右上角关闭回调
					  }
					});
			}
			
			//分页bootstrapTable插件
			var conf = {

				url : _ctx + "/iLaunchProject/list",
				columns : [ {
					field : 'state',
					checkbox : true,
					align : 'center',
					valign : 'middle',
					value : false,
					visible : false,
					searchable : false
				},
				{
					title : '订单号',
					field : 'id',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false
				},{
					title : '项目名称',
					field : 'projectName',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},{
					title : '支付金额',
					field : 'sumMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},{
					title : '支付方式',
					field : 'icoDays',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},{
					title : '时间',
					field : 'created',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : '状态',
					field : 'status',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						//'项目状态 0.未提交 1.待审核 2.未通过 3.即将开始 4.进行中 5.已完成 6.失败 7.删除'
						if(value==0){
							return "未提交";
						}else if(value==1){
							return "待审核";
						}else if(value==2){
							return "未通过";
						}else if(value==3){
							return "即将开始";
						}else if(value==4){
							return "进行中";
						}else if(value==5){
							return "已完成";
						}else if(value==6){
							return "失败";
						}
					}
				},{
					title : '代币数量',
					field : 'created',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : '操作',
					field : 'transactionMoney',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						if(row.status==0){
							return "<div>" +
							"<a href='#' onclick=seeProject("+row.id+","+row.step+")>" +
							"预览" +
							"</a>| "+
							"<a href='#' onclick=modifyProject("+row.id+","+row.step+")>" +
							"修改" +
							"</a>|"+
							"<a href='#' onclick=removeProject("+row.id+","+row.step+")>" +
							"删除" +
							"</a>" +
							"</div>";
						}else{
							return "<div>" +
							"<a href='#' onclick=seeProject("+row.id+","+row.step+")>" +
							"预览" +
							"</a>"+
							"</div>";
						}
						
					}
				}
				],
				queryParams : function(params) {
				    return {
				        limit:params.limit,
				        offset:params.offset,
				        sortOrder: params.order,
				        status:$($("#status").find(".selected")[0]).attr("value"),
				        transactionType:"tixian"
				    };
				}
			}
			 _table.initTable($("#table"), conf);
			
			//充值筛选条件
			$("#status").on("click","a",function(){
				$(this).siblings().removeClass('selected');
				$(this).addClass('selected');
				$("#table").bootstrapTable('refresh',null);
			})
			
		}
	}
});