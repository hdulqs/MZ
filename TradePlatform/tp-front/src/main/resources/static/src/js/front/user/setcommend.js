
define(function(require, exports, module) {
	
	this._table = require("js/base/table");//新增
	this.validate = require("js/base/validate");
	this.md5 = require("js/base/utils/hrymd5");
	require('lib/clipboard.min.js');
	require("js/base/googleauth/utf");
	require("js/base/googleauth/qrcode");
	require("js/base/googleauth/jquery.qrcode");
	
	module.exports = {
		
		//初始化方法
		/*init : function(){
			//清除定时器
			clearPageTimer(); 
			google();
			
			function  google(){
				var username=$(".username").val();
				$.ajax({
					type : "post",
					url : _ctx + "/user/setcommendfind",
					data:{username:username},
					cache : false,
					dataType : "json",
				success : function(data) {
				//$(".coss").html('<tr><td >返佣币种</td><td style="padding-left: 20px;">已返佣金额</td>  <td style="padding-left: 20px;">未返佣金额</td><td>未返佣金额</td><td>未返佣金额</td></tr>');
				var html = '';
				var a = $("#inviteLink1").val();
				jQuery('#qrcodeTable').qrcode({
			         render    : "canvas",                
			         text : a,  
			         width : "150",               //二维码的宽度
	                 height : "150",
			     });
				for(var i=0;i<data.obj.length;i++){
					 html +='<tr>'+
						'<td>'+data.obj[i].name+
						'</td>'+'<td>'+data.obj[i].commendMoney+'</td>'+'<td>'+data.obj[i].fixPriceCoinCode+
					'</td>'+'<tr>';
				}
				$("#aab").html(html);
				},
				eerror:function(e){
					alert(e)
				}
				});

			}
			var clipboard = new Clipboard('.copy-btn');

			clipboard.on('success', function(e) {
				layer.msg('复制成功',{
					icon: 1,
					time: 500 
				})
			});

			clipboard.on('error', function(e) {
				console.log(e);
			});

		}*/
		
   //新增
			//初始化方法
			init : function(){

				var conf = {

					detail : function(e, index, row, $detail) {
						var html = [];
						$.each(row, function(key, value) {
							html.push('<p><b>' + key + ':</b> ' + value + '</p>');
						});
						$detail.html(html.join(''));
					},
					url : _ctx + "/user/setcommendfind",
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
						title : beituijianren,
						field : 'custromerName',
						align : 'center',
						visible : true,
						sortable : false,
						searchable : false,
						formatter:function(value,row,index){
							var pattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
							var domains= ["qq.com","163.com","vip.163.com","263.net","yeah.net","sohu.com","sina.cn","sina.com","eyou.com","gmail.com","hotmail.com","42du.cn"];
	                        var patternt = /^1\d{10}$/;
	                        if(patternt.test(value)){
	                        	var domain1 = value.substring(0,3);
	                        	var domain2 = value.substring(7);
	                            return domain1 + '****' + domain2;
	                        }else if(pattern.test(value)){
	                        	var domain3 = value.substring(0,3);
	                        	var domain4 = value.substring(value.indexOf("@")+1);
	                        	return domain3+'****'+domain4;
	                        }
	                        return '****'
	                    }
					},
					{
						title : huodejianglijine,
						field : 'moneyNum',
						align : 'center',
						visible : true,
						sortable : false,
						searchable : false
					},
					{
						title : bizhongdanwei,
						field : 'fixPriceCoinCode',
						align : 'center',
						visible : true,
						sortable : false,
						searchable : false
					}, {
	                    title : paifazhaungtai,
	                    field : 'transactionNum',
	                    align : 'center',
	                    visible : true,
	                    sortable : false,
	                    searchable : false,
	                    formatter:function(value,row,index){
	                        //1买 2卖
	                        if(value==null){
	                            return daipaifa;
	                        }else if(value!=null){
	                        		return yipaifa;
							}
	                        return jiaoyiquxiao
	                    }
	                }
					],
					
					
					
					
					queryParams : function queryParams(params) {
					    return {
					        limit:params.limit,
					        offset:params.offset,
					        sortOrder: params.order,
					        transactionType:$($("#type").find(".selected")[0]).attr("value")
					    };
					}
				}
				_table.initTable($("#table"), conf);
			
				var a = $("#inviteLink1").val();
				jQuery('#qrcodeTable').qrcode({
			         render    : "canvas",                
			         text : a,  
			         width : "150",               //二维码的宽度
			         
	                 height : "150",
			     });
				
				var clipboard = new Clipboard('.copy-btn');

				clipboard.on('success', function(e) {
					layer.msg('复制成功',{
						icon: 1,
						time: 500 
					})
				});

				clipboard.on('error', function(e) {
					console.log(e);
				});
				
				//充值筛选条件
				$("#type").on("click","a",function(){
					$(this).siblings().removeClass('selected');
					$(this).addClass('selected');
					$("#table").bootstrapTable('refresh',null);
				})
				
			}
			
	
	}
	

});