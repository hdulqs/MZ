define(function(require,exports,module){this._table=require("js/base/table"),module.exports={init:function(){require("base");var e={detail:function(e,t,i,a){var l=[];$.each(i,function(e,t){l.push("<p><b>"+e+":</b> "+t+"</p>")}),a.html(l.join(""))},url:_ctx+"/user/oamessage/list",columns:[{field:"state",checkbox:!0,align:"center",valign:"middle",value:"id",visible:!1,searchable:!1},{title:"id",field:"id",align:"center",visible:!1,sortable:!1,searchable:!1},{title:"标题",field:"title",align:"center",visible:!0,sortable:!1,searchable:!0,formatter:function(e,t,i){return['<a class="read" sid="'+t.id+'" title="消息标题">',e,"</a>  "].join("")}},{title:"简介",field:"sortTitle",align:"center",visible:!0,sortable:!1,searchable:!0},{title:"发送时间",field:"sendDate",align:"center",visible:!0,sortable:!1,searchable:!0},{title:"状态",field:"state",align:"center",visible:!0,sortable:!1,searchable:!0,formatter:function(e,t,i){return 1==e?"未读":"已读"}}]};_table.initTable($("#table"),e),$("#table").on("click","a[sid]",function(){var e=$(this).attr("sid");loadUrl(_ctx+"/user/oamessage/read/"+e)})},read:function(){$("a[gobank]").click(function(){loadUrl(_ctx+"/v.do?u=front/user/mymessage")})}}});