define(function(require,exports,module){
	this._table = require("js/base/table");
	this.validate = require("js/base/validate");
	
	module.exports = {
		init : function(){
			tool.switchTab();
			tool.switchTab({ele:".form_box",tabList:".payment_li",boxList:".payment_form_box"});
			$('#coinSelect').change(function(){
				var p1=$(this).children('option:selected').val();//这就是selected的值
				$.ajax({
					 type: "get",
			         url: _ctx + "/lendcoin/coinlist",
			         dataType: "JSON",
			         data : {"lendCoin":p1},
			         resetForm : true,
			         success: function(data) {
			         if(data.success){
						  $("#coinCodeForRmb1").html(data.obj.coinCodeForRmb);
						   $("#coinCodeForRmb2").html(data.obj.coinCodeForRmb);
						    $("#coinCodeForRmb3").html(data.obj.coinCodeForRmb);
						     $("#coinCodeForRmb4").html(data.obj.coinCodeForRmb);
						       $("#coinCodeForRmb5").html(data.obj.coinCodeForRmb);
						 $("#rMBSum").html(data.obj.rMBSum);
					     $("#coinNetAsset").html(data.obj.coinNetAsset);
					     $("#rMBLendMoneyed").html(data.obj.rMBLendMoneyed);
					     $("#canLendMoney").html(data.obj.canLendMoney);
						 $("#selectCoinCode").text(p1);
			         }else{
			        	 
			         }
			         }
				})
				
				}) 
			
			//金额
			$("#lendmoneycount").on("input",function(){
				
				var money = this.value;
				var canLendMoney = $("#canLendMoney").html();//单笔不能少于
				if(!validate.isNumber(money)){
					$("#lendmoneycount_message").html(shuruwuxiao);
				}else{
					$("#lendmoneycount_message").html("");
					
					if(parseFloat(money)>parseFloat(canLendMoney)){
						$("#lendmoneycount_message").html(bunengdayukejie);
					}else{
					$("#lendmoneycount_message").html("");
					}
				}
			})
			//提交
			$("#lendSubmit").on("click",function(){
				var canLendMoney = $("#canLendMoney").html();
				var lendCoin=$("#coinSelect").children('option:selected').val();//这就是selected的值 
				var money = $("#lendmoneycount").val();
				if(!validate.isNumber(money)){
						layer.msg(shuruwuxiao, {icon: 2});
						return false;
				}else{
					if(parseFloat(money)>parseFloat(canLendMoney)){
						layer.msg(bunengdayukejie, {icon: 2});
						return false;
					}
				}
				if(!$("#checkyuedu").attr('checked')){
                    layer.msg(qingxianyueduxieyi, {icon: 2});
                    return false;
				}
				$("#lendSubmit").attr("disabled",true);
				$("#lendSubmit").empty().text("Loading...");
				$("#lendFrom").ajaxSubmit({
					 type: "post",
			         url: _ctx + "/lendcoin/addCoin",
			         dataType: "JSON",
			         data : {"lendCount":money,"lendCoin":lendCoin},
			         resetForm : true,
			         success: function(data) {
			         if(data.success){
			        	 $("#lendSubmit").attr("disabled",false);
						 $("#lendSubmit").empty().text("申请");
						 
						 $("#rMBSum").html(data.obj.rMBSum);
					     $("#coinNetAsset").html(data.obj.coinNetAsset);
					     $("#rMBLendMoneyed").html(data.obj.rMBLendMoneyed);
					     $("#canLendMoney").html(data.obj.canLendMoney);
			        	 $('#table').bootstrapTable('refresh');
			        	  layer.alert(shengqingchenggong);
			         }else{
			         	 $("#lendSubmit").attr("disabled",false);
						 $("#lendSubmit").empty().text("申请");
			        	 layer.alert(data.msg);
			        	 
			         }
			         }
				})
			})
			
			//分页bootstrapTable插件
			var conf = {

				url : _ctx + "/lendcoin/listpage",
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
					title : jikuanshijian,
					field : 'lendTime',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : leixing,
					field : 'lendCoin',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : yijieyihaun,
					field : 'lendCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						return value+"/"+row.repayLendCount;
					}
				},
				{
					title : rihualilv,
					field : 'lendRate',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						return value+"%";
					}
				},
				{
					title : zonglixi,
					field : 'interestCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true
				},
				{
					title : yihuanjine,
					field : 'repayLendCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						return row.repayLendCount+row.repayInterestCount;
					}
				},{
					title : yinghuanjine,
					field : 'lendCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						return row.lendCount- row.repayLendCount+ row.interestCount- row.repayInterestCount;
			     }
			  },{
					title : caozuo,
					field : 'lendCount',
					align : 'center',
					visible : true,
					sortable : false,
					searchable : true,
					formatter:function(value,row,index){
						if(row.status==1){
							return '<a id="'+row.id+'">'+quhuankuan+'</a>';
						}else if(row.status==2){
								return '<a name="'+row.id+'">'+chakan+'</a>|<a id="'+row.id+'">'+quhuankuan+'</a>';
					
						}else if(row.status==3){
								return '<a name="'+row.id+'">'+chakan+'</a>';
					
						}
				   }
				}
				],
				queryParams : function queryParams(params) {
				    return {
				        limit:params.limit,
				        offset:params.offset,
				        sortOrder: params.order
				    };
				}
			}
			 _table.initTable($("#table"), conf);
			 
			   $('input[type="radio"]').on('click',function(){
						var that=$(this);
				        //console.log(that.parent().text())
				        if(that.attr('htmlType')=='part'){
					 		$('.part-con').show();
					 		$("#repaymentType").val("part");
					 	}else{
					 		$('.part-con').hide();
					 		$("#repaymentType").val("all");
					 	}

					})
			
			 
			 //提交
			$("#repaymentSubmit").on("click",function(){
			// debugger
				var repaymentType = $("#repaymentType").val();
				var repaymentMoney = $("#repaymentMoney").val();
				
			   if(repaymentType=="part"){
				   if(!validate.isNumber(repaymentMoney)){
							layer.msg(shuruwuxiao, {icon: 2});
							return false;
					}else{
					 
					}
			   }
				var repaymentid=$("#repaymentid").val();
				$("#repaymentSubmit").attr("disabled",true);
				$("#repaymentSubmit").empty().text("Loading...");
					$.ajax({
			         url: _ctx + "/lendcoin/repayment",
			         dataType: "JSON",
			         data : {"repaymentMoney":repaymentMoney,"type":repaymentType,"id":repaymentid},
			         resetForm : true,
			         success: function(data) {
			         if(data.success){
			        	 $("#repaymentSubmit").attr("disabled",false);
						 $("#repaymentSubmit").empty().text("申请");
						 
						 $('#repayment').hide();
			        
			        	 
			        	 $('#table').bootstrapTable('refresh');
			        	  layer.alert(huankuanchenggong);
			        	  layer.closeAll('page');
			         }else{
			         	 $("#repaymentSubmit").attr("disabled",false);
						 $("#repaymentSubmit").empty().text("申请");
			        	 layer.alert(data.msg);
			        	 
			         }
			         }
				})
			})
		 $("#table").on("click","a[name]",function(){
			// debugger
			  var sid = $(this).attr("name");
			  	$('.part-con').show();
				$.ajax({
					 type: "get",
			         url: _ctx + "/lendcoin/fildIntentlist",
			         dataType: "JSON",
			         data : {"lendId":sid},
			         resetForm : true,
			         success: function(data) {
			             var json =data; 
			          var html="";
			          
			            for(var i=0;i<json.length;i++){
			               var record=json[i];
			               html=html+ "<tr>"+
			                      
						   "<td>"+(record.intentType)+"</td>"+
							  //   "<td>"+record.lendCoin+"</td>"+
							     "<td>"+record.repayCount+"</td>"+
							     "<td>"+record.factTime+"</td>"+
						   " </tr>";
			            
			            }
			          
			               $("#tbodyrecord").html(html);
			          
			          
				        
							     layer.open({
								  type: 1,
								  title :huankaunmingxi,
								  area: ['650px', '450px'],
								  content: $('#layer-record'),
								  cancel: function(){ 
									  $('#layer-record').hide();
									  
									}    
							  })
			         }
				})
			})
			 
			 $("#table").on("click","a[id]",function(){
			  var sid = $(this).attr("id");
			  $("#repaymentid").val(sid);
			  $("#repaymentType").val("part");
			   $("#repaymentMoney").val("");
			  	$('.part-con').show();
			  	$("input[name='repaid']").get(0).checked=true; 
				$.ajax({
					 type: "get",
			         url: _ctx + "/lendcoin/getRepaymentInfo/"+sid,
			         dataType: "JSON",
			         data : {},
			         resetForm : true,
			         success: function(data) {
				         var json = eval('(' + data + ')');
                         // var json = data;
				          $('#LendCount').html(json.LendCount+"("+json.coinCode+")");
					     $('#notRepayLendCount').html(json.notRepayLendCount+"("+json.coinCode+")");
					     $('#balance').html(json.balance+"("+json.coinCode+")");;
					     $('#notInterest').html(json.notInterest+"("+json.coinCode+")");
							     layer.open({
								  type: 1,
								  title :huankuan,
								  area: ['650px', '450px'],
								  content: $('#repayment'),
								  cancel: function(){ 
									  $('#repayment').hide();
									}    
							  })
			         }
				})
				  
				  
	
			})
			
		}
	}
})
