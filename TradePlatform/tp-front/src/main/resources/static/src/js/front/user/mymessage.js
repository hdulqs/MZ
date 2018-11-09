define(function(require, exports, module) {
	this._table = require("js/base/table");
	
	module.exports = {
		
		//初始化方法
		init : function(){
			//加载base
			require("base");
			
			var conf = {

				detail : function(e, index, row, $detail) {
					var html = [];
					$.each(row, function(key, value) {
						html.push('<p><b>' + key + ':</b> ' + value + '</p>');
					});
					$detail.html(html.join(''));
				},
				url : _ctx + "/user/oamessage/list",
				columns : [ {
					field : 'state',
					checkbox : true,
					align : 'center',
					valign : 'middle',
					value : "id",
					visible : false,
					searchable : false 
				},
				{
					title : 'id',
					field : 'id',
					align : 'center',
					visible : false,
					sortable : false,
					searchable : false
				},
				{
					title : biaoti,
					field : 'title',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
					,
					formatter : function (value, row, index) {
						return [ 
						         '<a class="read" sid="'+row.id+'" title="消息标题">', 
						         value,
						         '</a>  '
						         ].join('');
					}
				},
				{
					title : jianjie,
					field : 'sortTitle',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false
				},{
					title : fasongshijian,
					field : 'sendDate_long',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter : function(value,row,index){
						return TimestampFormat('Y-m-d H:i:s', value/1000);
					}
				},{
					title : zhaungtai,
					field : 'state',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : false,
					formatter:function(value,row,index){  
						//1未读 2已读
						if(value==1){
							return weidu;
						}else {
							return yidu
						}
					}
					
				}
				]
			}
			_table.initTable($("#table"), conf);
		
			$("#table").on("click","a[sid]",function(){
				var sid = $(this).attr("sid");
				loadUrl(_ctx +"/user/oamessage/read/"+sid);
			})
			
			
		},
		read : function(){
			$('a[gobank]').click(function(){
				loadUrl(_ctx +"/v.do?u=front/user/mymessage");
			})
		}

	}
});