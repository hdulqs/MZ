define(function(require, exports, module) {
    this._table = require("js/base/table");
    this.validate = require("js/base/validate");
    this.md5 = require("js/base/utils/hrymd5")
    require("js/base/secondvail");
    this.firstvail = require("js/base/firstvail");
    module.exports = {




        init : function() {

            // 清除定时器
            clearPageTimer();
            $(".verifyLayout").hide();
            $(".verifyLayout1").hide();
            $(".verifyLayout2").hide();
            $(".verifyLayout").hide();

            var a =$("#coinType").val();
            var language='';
            if($.cookie('Language')=='' || $.cookie('Language')==null){
                language = 'en'
            }else{
                language = $.cookie('Language');
            }

            if(language=="en" && ($.cookie('Language')!='') && ($.cookie('Language')!=null)){
                $("#coinCode5").hide();
                $("#coinCode6").hide();
            }else{
                $("#coinCode5").show();
                $("#coinCode6").show();
            }

            //首次加载取一下币的下拉框值
            var coinSelect = $("#coinSelect").val();
            if(coinSelect!=null){
                var coinSelectsplit = coinSelect.split(",");
                $("#addWaccount").attr("onclick","loadUrl('"+_ctx+"/user/publickeylist/index.do?coinCode="+coinSelectsplit[4]+"')");
            }


            $.ajax({
                type : "post",
                url : _ctx + "/user/btc/findcurrcy",
                dataType : "JSON",
                async:true,
                data : {
                    coinCode : a,
                },
                success : function(data) {
                    var currecyType = data.obj[1];
                    var html = '';
                    if(currecyType==1){
                        html +='<div class="form-group">'+
                            '<div class="col-md-6 col-md-offset-3">'+
                            '<div class="alert alert-warning fade in m-t-15 col-md-12" id="divPrompt">'+shouxufeie+'<span id="shouxufei">0.0000</span><span id="coincode3">'+a+'</span> '+shijidaobi+'<span id="shijidao">0.0000</span>'+
                            '<span id="coincode4">'+a+'</span>'+
                            '</div>'+
                            '</div>'+
                            '</div>';

                        $("#shouxufeiId").html(html);
                    }else if(currecyType==2){
                        html += '<div class="form-group drop-group" style="position:relative;">'+
                            '<label class="col-sm-3 control-label">'+wangluuoshouxufei+'：</label>'+
                            '<select id="issued_sub_key_c" style="margin-left: 15px;" class="col-sm-3 control-label " name="issued_sub_key_c">'+
                            ' </select></div>';
                        $("#shouxufeiId").html(html);
                    }

                    if(data.obj[0]!=null){
                        var result=data.obj[0].split(",");
                        for (var i = 0; i < result.length; i++) {
                            $("#issued_sub_key_c").append("<option>" + result[i] + "</option>");
                        }
                    }
                }
            })
            /* $.ajax({
                  timeout: 3000,
                  async: false,
                  type: "POST",
                  url: "WareHouse.ashx",
                  dataType: "json",
                  data: {
                      warehouse: $("#issued_sub_key_c").val(),
                },
                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
                        $("#issued_sub_key_c").append("<option>" + data[i].Name + "</option>");
                    }
                 }
             });*/



            $('.dropdown-toggle').on('click',function(){
                $('.dropdown-menu').slideToggle();
            })
            $('#ulFees li').on('click',function(){
                var curtext=$(this).text();
                $('#fees').val(curtext),$('.dropdown-menu').hide();
            })


            /*     $(document).ready(function () {
                  $.ajax({
                      timeout: 3000,
                      async: false,
                      type: "POST",
                      url: "WareHouse.ashx",
                      dataType: "json",
                      data: {
                          warehouse: $("#issued_sub_key_c").val(),
                    },
                    success: function (data) {
                        for (var i = 0; i < data.length; i++) {
                            $("#issued_sub_key_c").append("<option>" + data[i].Name + "</option>");
                        }
                     }
                 });
             });
                */



            $("#coinSelect").on("change", function() {
                $('#inputNumWit').val('')
                var selectValue = $(this).find("option:selected").val();
                var split = selectValue.split("HURONGSPLIT");
                $("#availableCTC").val(split[1]);
                $("#frozenCTC").val(split[2]);
                $("#publicKey").empty().text(split[3]);
                $("#coinType").val(split[4]);
                $("#firstHotspan").empty().text(split[1]);

                $("#coincode1").empty().text(split[4]);
                $("#coincode2").empty().text(split[4]);
                $("#coincode3").empty().text(split[4]);
                $("#coincode4").empty().text(split[4]);
                $("#coincode5").empty().text(split[4]);
                $("#coincode6").empty().text(split[4]);

                $("#coinCode1").empty().text(split[4]);
                $("#coinCode2").empty().text(split[4]);
                $("#coinCode3").empty().text(split[4]);
                $("#coinCode4").empty().text(split[4]);
                $("#coinCode5").empty().text(split[4]);
                $("#coinCode6").empty().text(split[4]);

                $("#paceFeeRate").empty().text(split[5]);
                $("#leastPaceNum").empty().text(split[6]);
                $("#oneDayPaceNum").empty().text(split[7]);
                $("#keepDecimalForCoin").val(split[8]);

                $("#addWaccount").attr("onclick","loadUrl('"+_ctx+"/user/publickeylist/index.do?coinCode="+split[4]+"')");

                $.ajax({
                    type : "post",
                    url : _ctx + "/user/btc/findPublicKey",
                    dataType : "JSON",
                    data : {
                        coinCode : split[4],
                    },
                    success : function(data) {
                        $("#btcKey").empty();

                        for(var i = 0 ; i < data.length; i++){

                            $("#btcKey").append("<option remark=\""+data[i].remark+ "\"value=\""+data[i].publicKey+"\">"+data[i].publicKey+"</option>");

                        }
                    }
                })
                $("#issued_sub_key_c").empty();
                $.ajax({
                    type : "post",
                    url : _ctx + "/user/btc/findcurrcy",
                    dataType : "JSON",
                    async:true,
                    data : {
                        coinCode : split[4],
                    },
                    success : function(data) {
                        var currecyType = data.obj[1];
                        var html = '';
                        if(currecyType==1){
                            html +='<div class="form-group">'+
                                '<div class="col-md-6 col-md-offset-3">'+
                                '<div class="alert alert-warning fade in m-t-15 col-md-12" id="divPrompt">'+shouxufeie+'<span id="shouxufei">0.0000</span><span id="coincode3">'+split[4]+'</span> '+shijidaobi+'<span id="shijidao">0.0000</span>'+
                                '<span id="coincode4">'+split[4]+'</span>'+
                                '</div>'+
                                '</div>'+
                                '</div>';
                            $("#shouxufeiId").html(html);
                        }else if(currecyType==2){
                            html += '<div class="form-group drop-group" style="position:relative;">'+
                                '<label class="col-sm-3 control-label">'+wangluuoshouxufei+'：</label>'+
                                '<select id="issued_sub_key_c" style="margin-left: 15px;" class="col-sm-3 control-label" name="issued_sub_key_c">'+
                                ' </select></div>';
                            $("#shouxufeiId").html(html);
                        }

                        if(data.obj[0]!=null){
                            var result=data.obj[0].split(",");
                            for (var i = 0; i < result.length; i++) {
                                $("#issued_sub_key_c").append("<option>" + result[i] + "</option>");
                            }
                        }
                    }
                })





            })

            // 添加提现账户
            /*
            $("#addWaccount").on("click", function() {
                loadUrl(_ctx + "/user/btc/get");
            })
*/
            $("#inputNumWit").bind("input", function() {
                var firstHotspan = $("#firstHotspan").text();
                var inputNumWit = $("#inputNumWit").val();
                var i, j, strTemp;
                strTemp = "0123456789";
                /*if (inputNumWit < $("#leastPaceNum").html()) {

                    $('#btcNum_message').html(bukexiaoyudanbizuixiaotibuedu);
                    return false;
                }
                if (parseInt(inputNumWit) < parseInt($("#leastPaceNum").html())) {
                    layer.msg(bukexiaoyudanbizuixiaotibuedu, {
                        icon : 2
                    });
                    return false;
                }*/
                if (parseInt(inputNumWit) > parseInt(firstHotspan)) {
                    layer.msg(bukedayutixianshuliang, {
                        icon : 2
                    });
                    return false;
                }
                if(!validate.isNumber(inputNumWit)){
                    $("#btcNum_message").html(tixianshuliangbixuweishuzi);
                    return false;
                }
                else{
                    $("#btcNum_message").html("");
                    //var actualMoney = (parseFloat(inputNumWit) * parseFloat($("#paceFeeRate").html())).toFixed(6);
                    //var actualMoney_b = (parseFloat(inputNumWit) - parseFloat(actualMoney)).toFixed(6);
                    var actualMoney = parseFloat(inputNumWit) * parseFloat($("#paceFeeRate").html());
                    var actualMoney_b = parseFloat(inputNumWit) - parseFloat(actualMoney);
                    //.toFixed(5)保留小数点后5位
                    /*$("#shouxufei").html(actualMoney.toFixed($("#keepDecimalForCoin").val()));
                    $("#shijidao").html(actualMoney_b.toFixed($("#keepDecimalForCoin").val()));*/
                    $("#shouxufei").html(actualMoney.toFixed(8));
                    $("#shijidao").html(actualMoney_b.toFixed(8));
                }
            })

            //交易密码验证
            $("#tradepwd").on("click", function() {
                $.ajax({
                    type : "post",
                    url : _ctx + "/sms/smsPhone",
                    data : {
                    },
                    cache : false,
                    dataType : "json",
                    success : function(data) {
                        
                    },
                    error : function(e) {

                    }
                });
            })
            $("#oktx").on("click", function() {
                var coinType = $("#coinType").val();
                var withdrawCode = $("#withdrawCode").val();
                var currencyType = $("#currencyType").val();
                var btcNum = $("#inputNumWit").val();
                var btcKey = $("#btcKey").val();
                var remark = $("#btcKey").find("option:selected").attr("remark")
                if(btcKey==null){
                    layer.msg(qingtianjiaqianbaodizhi, {
                        icon : 2
                    });
                    return false;
                }
                if(btcNum ==null || btcNum==""){
                    layer.msg(qingshurutibishuliang, {
                        icon : 2
                    });
                    return false;
                }

                $("#oktx").attr("disabled","disabled");

                var load = layer.load(1,{shade: [0.3,'#000']});

                //提币交易
                $.ajax({
                    type : "post",
                    url : _ctx + "/sencodvail",
                    data : {
                        coinType : coinType,
                        withdrawCode : withdrawCode,
                        currencyType : currencyType,
                        btcNum : btcNum,
                        btcKey : btcKey,
                        type:"3",
                        remark:remark
                    },
                    cache : false,
                    dataType : "json",
                    success : function(data) {
                        layer.close(load);
                        if(data){
                            if(data.success){
                                var phone =data.obj.phoneState;
                                var google=data.obj.googleState;

                                if(google==1&&phone==0){
                                    $(".verifyLayout1").show();
                                    $('.dialog-close').on('click',function(){
                                        $("#oktx").removeAttr("disabled");
                                        $(this).parent().parent().hide()
                                    })

                                }else if(google==0&&phone==1){
                                    $(".verifyLayout").show();
                                    $('.dialog-close').on('click',function(){
                                        $("#oktx").removeAttr("disabled");
                                        $(this).parent().parent().hide()
                                    })

                                }else if(google==1&&phone==1){

                                    $('#mobile-form').css('display','none');
                                    $(".verifyLayout2").show();
                                    $('.verify-form1').hide();
                                    $('.dialog-close').on('click',function(){
                                        $("#oktx").removeAttr("disabled");
                                        $(this).parent().parent().hide()
                                    })
                                    $('.btns').find('span').on('click',function(){
                                        var ind=$(this).index();
                                        $(this).addClass('cur').siblings().removeClass('cur');
                                        if(ind==0){
                                            $('.verify-form').show();
                                            $('.verify-form1').hide();
                                        }else{
                                            $('.verify-form1').show();
                                            $('.verify-form').hide();
                                        }
                                    })

                                }else{
                                    firstvail.sendvail("btcget");
                                    //window.location.href = _ctx+"/user/center";
                                    //$("#oktx").removeAttr("disabled");// 按钮可用
                                }



                            }else{
                                layer.msg(data.msg, {icon: 2});
                                $("#oktx").removeAttr("disabled");
                            }
                        }else{
                            layer.msg("tibishibai", {icon: 2})
                            $("#oktx").removeAttr("disabled");
                        }
                    },
                    error : function(e) {

                    }
                });

                //$("#oktx").attr("disabled", true);

            })



            $("#sendBtn,#sendBtn1").on("click",function(){

                $(this).attr("disabled","disabled");
                $(this).html(yifasong);
                $.ajax({
                    type : "post",
                    url : _ctx + "/sms/smsPhone",
                    data : {
                    },
                    cache : false,
                    dataType : "json",
                    success : function(data) {
                        if(data){
                            if(data.success){
                                layer.msg(sms_success, {icon: 1})

                                var time = 120;
                                window.clearInterval(pageTimer["btcget"]);
                                // 开启点击后定时数字显示
                                pageTimer["btcget"] = window.setInterval(function() {
                                    time = time - 1;
                                    if (time == 0) {
                                        $("#sendBtn1").html(dianji);
                                        $("#sendBtn1").removeAttr("disabled");// 按钮可用
                                        $("#sendBtn1").html(chongxinfasong);// 按钮可用
                                        $("#sendBtn").html(dianji);
                                        $("#sendBtn").removeAttr("disabled");// 按钮可用
                                        $("#sendBtn").html(chongxinfasong);// 按钮可用
                                        window.clearInterval(pageTimer["btcget"]);
                                    } else {
                                        $("#sendBtn").html(time+miaochongxinfasong );
                                        $("#sendBtn1").html(time+miaochongxinfasong );
                                    }

                                }, 1000);

                            }else{
                                $("#sendBtn").removeAttr("disabled");// 按钮可用
                                $("#sendBtn1").removeAttr("disabled");// 按钮可用
                                layer.msg(data.msg, {icon: 2})
                            }
                        }else{
                            $("#sendBtn").removeAttr("disabled");// 按钮可用
                            $("#sendBtn").removeAttr("disabled");// 按钮可用

                            layer.msg(fasongshibai, {icon: 2})
                        }
                    },
                    error : function(e) {

                    }
                });

            });

            // 分页bootstrapTable插件
            var conf = {
                url : _ctx + "/user/publickeylist/list",
                columns : [ {
                    field : 'state',
                    checkbox : true,
                    align : 'center',
                    valign : 'middle',
                    value : "id",
                    visible : false,
                    searchable : false
                }, {
                    title : 'id',
                    field : 'id',
                    align : 'center',
                    visible : false,
                    sortable : false,
                    searchable : false
                }, {
                    title : dingdanhao,
                    field : 'transactionNum',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : true,
                    formatter :function(value,row,index){
                        if(row.orderNo!=undefined&&row.orderNo!=""){
                            return value+"<br/>blockid:"+row.orderNo
                        }
                        return value;
                    }
                }, {
                    title : bizhong,
                    field : 'coinCode',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : true
                },{
                    title : tibishulixang,
                    field : 'transactionMoney',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : true
                }, {
                    title : shouxufei,
                    field : 'fee',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : true
                }, {
                    title : daozhangshuliang,
                    field : 'transactionMoney',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : true,
                    formatter :function(value,row,index){
                        return (row.transactionMoney-row.fee).toFixed(8);
                        //return parseFloat(i-a).toFixed(5);
                    }
                }, {
                    title : chongbishijian,
                    field : 'created_long',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : true,
                    formatter : function(value,row,index){
                        return TimestampFormat('Y-m-d H:i:s', value/1000);
                    }
                }, {
                    title : zhaungtai,
                    field : 'status',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : true,
                    formatter : function(value, row, index) {
                        // 1待审核 2已完成 3已否决
                        if (value == 1) {
                            return daishenhe;
                        } else if (value == 2) {
                            return yiwancheng;
                        } else if (value == 3) {
                            return yifoujue;
                        }
                    }
                } ,{
                    title : bohuiyuanyin,
                    field : 'rejetionReason',
                    align : 'center',
                    visible : true,
                    sortable : false,
                    searchable : true,
                    formatter:function(value,row,index){
                        //1待审核 2已完成 3已否决
                        if(row.rejectionReason!=null){
                            return row.rejectionReason;
                        }else{
                            return '-';
                        }
                    }
                }

                ],
                queryParams : function queryParams(params) {
                    return {
                        limit : params.limit,
                        offset : params.offset,
                        sortOrder : params.order,
                        type : $($("#type").find(".selected")[0]).attr("value"),
                        status : $($("#type").find(".selected")[0]).attr("value")
                    };
                }
            }
            _table.initTable($("#table"), conf);
            // 筛选条件
            $("#type").on("click", "a", function() {
                $(this).siblings().removeClass('selected');
                $(this).addClass('selected');
                $("#table").bootstrapTable('refresh', null);
            })
        },



        // 发送短信
        sendsms : function() {
            $("#sendsmsBtn").on("click", function() {
                var username = $("#username").val();
                var inputNumWit = $("#inputNumWit").val();
                //var accountPassWord = $("#accountPassWord").val();
                var withdrawCode = $("#withdrawCode").val();
                $(this).attr("disabled", "disabled");
                $.ajax({
                    type : "post",
                    url : _ctx + "/sms/smsPhone",
                    data : {
                        username : username,
                        //accountPassWord : md5.md5(accountPassWord),
                        inputNumWit : inputNumWit,
                        withdrawCode : withdrawCode
                    },
                    cache : false,
                    dataType : "json",
                    success : function(data) {
                        if (data) {
                            if (data.success) {
                                layer.msg(sms_success, {
                                    icon : 1
                                })

                                var time = 120;
                                window.clearInterval(pageTimer["btcget"]);
                                // 开启点击后定时数字显示
                                pageTimer["btcget"] = window.setInterval(function() {
                                    time = time - 1;
                                    if (time == 0) {
                                        $("#sendsmsBtn").html(fasongyanzhengma);
                                        $("#sendsmsBtn").removeAttr("disabled");// 按钮可用
                                        window.clearInterval(pageTimer["btcget"]);
                                    } else {
                                        $("#sendsmsBtn").html(time + miaochongxinfasong);
                                    }

                                }, 1000);

                            } else {
                                $("#sendsmsBtn").removeAttr("disabled");// 按钮可用
                                layer.msg(data.msg, {
                                    icon : 2
                                })
                            }
                        } else {
                            $("#sendsmsBtn").removeAttr("disabled");// 按钮可用
                            layer.msg(fasongshibai, {
                                icon : 2
                            })
                        }
                    },
                    error : function(e) {

                    }
                });

            });
        }
    }
})