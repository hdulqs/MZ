define(function(require,exports,module){this._table=require("js/base/table"),module.exports={init:function(){var e={detail:function(e,t,i,l){var a=[];$.each(i,function(e,t){a.push("<p><b>"+e+":</b> "+t+"</p>")}),l.html(a.join(""))},url:_ctx+"/user/trades/list",columns:[{field:"state",checkbox:!0,align:"center",valign:"middle",value:"id",visible:!1,searchable:!1},{title:"id",field:"id",align:"center",visible:!1,sortable:!1,searchable:!1},{title:"交易时间",field:"transactionTime",align:"center",visible:!0,sortable:!1,searchable:!1},{title:"交易类型",field:"type",align:"center",visible:!0,sortable:!1,searchable:!1,formatter:function(e,t,i){return 1==e?"买":"卖"}},{title:"单价(¥)",field:"transactionPrice",align:"center",visible:!0,sortable:!1,searchable:!1},{title:"数量(个)",field:"transactionCount",align:"center",visible:!0,sortable:!1,searchable:!1},{title:"金额(¥)",field:"transactionSum",align:"center",visible:!0,sortable:!1,searchable:!1},{title:"手续费(¥)",field:"transactionFee",align:"center",visible:!0,sortable:!1,searchable:!1}],queryParams:function(e){return{limit:e.limit,offset:e.offset,sortOrder:e.order,type:$($("#type").find(".selected")[0]).attr("value"),transactionType:"chongzhi"}}};_table.initTable($("#table"),e),$("#type").on("click","a",function(){$(this).siblings().removeClass("selected"),$(this).addClass("selected"),$("#table").bootstrapTable("refresh",null)})}}});