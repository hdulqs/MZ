define(function(require,exports,module){
    this._table = require("js/base/table");

    module.exports = {
        init : function(){
            //清除定时器
            clearPageTimer();
            //加载base
            require("base");
            var cardBank="";
            var bankProvince="";
            var bankAddress="";

            $.ajax({
                type : "POST",
                dataType : "JSON",
                url : _ctx + "/user/bankcard/findBankCard",
                cache : false,
                success : function(data) {
                    if (data.obj.length>0){
                        //设置银行卡ID
                        $("#bankId").val(data.obj[0].id);
                        cardBank =  data.obj[0].cardBank;
                        bankProvince =  data.obj[0].bankProvince;
                        bankAddress =  data.obj[0].bankAddress;
                        //设置支行
                        $("#subBank").val(data.obj[0].subBank);
                        //设置卡号
                        $("#cardNumber").val(data.obj[0].cardNumber);
                        //设置显示的支付宝图片
                        var alipayPicture =  data.obj[0].alipayPicture;
                        if (alipayPicture!=null&&alipayPicture!=undefined&&alipayPicture!=""){
                            $("#zhicode").html("")
                            $("#myImg").attr("src", alipayPicture);    //e.target.result就是最后的路径地址
                            $("#myImg").css("width","100%");
                            $("#myImg").css("height","100%");
                            $("#shangchuan").css("padding","0");

                            $("#alipayPicture_1").val(alipayPicture);




                        }

                        //设置显示的支付宝账号
                        $("#alipay").val(data.obj[0].alipay);

                        //设置微信图片
                        var weChatPicture= data.obj[0].weChatPicture;
                        if (weChatPicture){
                            $("#zhicode2").html("")
                            $("#myImg2").attr("src",weChatPicture);    //e.target.result就是最后的路径地址
                            $("#weChatPicture_1").val(weChatPicture);
                            $("#myImg2").css("width","100%");
                            $("#myImg2").css("height","100%");
                            $("#shangchuan2").css("padding","0");

                        }

                        //设置微信号
                        $("#wechat").val(data.obj[0].weChat);
                    }

                    //查询银行
                    $.ajax({
                        type : "POST",
                        dataType : "JSON",
                        url : _ctx + "/user/bankcard/bank",
                        cache : false,
                        success : function(data) {
                            var obj = eval(data.obj);
                            var html = "";

                            for(var i=0;i<obj.length;i++){
                                if (obj[i].itemName==cardBank){

                                    html += "<option selected='true' value="+obj[i].itemName+">"+obj[i].itemName+"</option>"
                                }else {
                                    html += "<option value="+obj[i].itemName+">"+obj[i].itemName+"</option>"
                                }

                            }
                            $("#bankselect").append(html);
                        }
                    })
                    //获取省
                    $.ajax({
                        type : "POST",
                        dataType : "JSON",
                        url : _ctx + "/user/bankcard/area",
                        cache : false,
                        success : function(data) {
                            var obj = eval(data.obj);
                            var html = "";
                            for(var i=0;i<obj.length;i++){
                                if (obj[i].province==bankProvince){
                                    html += "<option selected='true' value="+obj[i].key+">"+obj[i].province+"</option>"
                                }else {
                                    html += "<option value="+obj[i].key+">"+obj[i].province+"</option>"
                                }
                            }
                            $("#province").append(html);
                        }
                    })
                    //查询市
                    $.ajax({
                        type : "POST",
                        dataType : "JSON",
                        url : _ctx + "/user/bankcard/city/"+bankProvince,
                        cache : false,
                        success : function(data) {
                            var obj = eval("["+data.obj+"]");
                            var html = "";
                            for(var i=0;i<obj.length;i++){
                                if (obj[i].city==bankAddress){
                                    html += "<option selected='true' value="+obj[i].city+">"+obj[i].city+"</option>"
                                }else {
                                    html += "<option value="+obj[i].city+">"+obj[i].city+"</option>"
                                }
                            }
                            $("#city").append(html);
                        }
                    })

                }
            })

            //查询省
            $.ajax({
                type : "POST",
                dataType : "JSON",
                url : _ctx + "/user/bankcard/area",
                cache : false,
                success : function(data) {
                    var obj = eval(data.obj);
                    var html = "";
                    for(var i=0;i<obj.length;i++){
                        html += "<option value="+obj[i].key+">"+obj[i].province+"</option>"
                    }
                    $("#province").append(html);
                }
            })
            //获取选中的省
            $("#province").on("change",function(){
                //得到下拉列表的相应的值
                var id = this.value;
                var selectName = $(this).find("option:selected").val();
                //给隐藏文本框赋值
                $("#provinceValue").val(selectName);
                $("#bankProvince").val($(this).find("option:selected").text());
                $("#city option").remove();
                //查询市
                $.ajax({
                    type : "POST",
                    dataType : "JSON",
                    url : _ctx + "/user/bankcard/city/"+$("#provinceValue").val(),
                    cache : false,
                    success : function(data) {
                        var obj = eval("["+data.obj+"]");
                        var html = "";
                        for(var i=0;i<obj.length;i++){
                            html += "<option value="+obj[i].city+">"+obj[i].city+"</option>"
                        }
                        $("#city").append(html);
                    }
                })
            });
            //添加银行卡
            $("#addBankcard").on("click",function(){
                var bankselect = $("#bankselect").val();
                var province = $("#province").val();
                var subBank = $("#subBank").val();
                var subBankNum = $("#subBankNum").val();
                var cardName = $("#cardName").val();
                var surname = $("#surname").val();
                var cardNumber = $("#cardNumber").val();
                var alipay = $("#alipay").val();
                var wechat = $("#wechat").val();
                var verifyCode = $("#verifyCode").val();

                var weChatPicture = $("#weChatPicture").val();
                var alipayPicture = $("#alipayPicture").val();

                if(weChatPicture==""||weChatPicture==null||weChatPicture==undefined){
                    weChatPicture = $("#weChatPicture_1").val();
                }

                if(alipayPicture==""||alipayPicture==null||alipayPicture==undefined){
                    alipayPicture = $("#alipayPicture_1").val();
                }

                var flag1 = $("#zhicode").html();
                var flag2 = $("#zhicode2").html();
                var reg = /^[0-9]*$/;
                if(!reg.test(cardNumber)){
                    layer.msg(yinhangkahaobixuweishuzi,{icon:2});return false;
                }

                if(!bankselect){
                    layer.msg(qingxuanzeyinhang,{icon:2});return false;
                }
                if(!province){
                    layer.msg(qingxuanzesuozaidi,{icon:2});return false;
                }
                if(!subBank){
                    layer.msg(kaihuzhihangbunengweikong,{icon:2});return false;
                }
                if(!cardNumber){
                    layer.msg(yinhangkahaobunengweikong,{icon:2});return false;
                }
                if(!verifyCode){
                    layer.msg("验证码不能为空",{icon:2});return false;
                }

                if(alipayPicture|| flag1 == ""){
                    if(!alipay){
                        layer.msg("请上传支付宝账号",{icon:2});return false;
                    }
                }else{
                    if(alipay){
                        layer.msg("请上传支付宝收款码",{icon:2});return false;
                    }
                }
                if(weChatPicture|| flag2 == ""){
                    if(!wechat){
                        layer.msg("请上传微信账号",{icon:2});return false;
                    }
                }else{
                    if(wechat){
                        layer.msg("请上传微信收款码",{icon:2});return false;
                    }
                }
                $("#addBankcard").attr("disabled",true);

                $("#cardForm").ajaxSubmit({
                    type: "post",
                    url: _ctx + "/user/bankcard/saveBankCard",
                    dataType: "JSON",
                    success: function(data) {
                        $("#addBankcard").attr("disabled",false);
                        if(data!=undefined){
                            if(data.success){
                                layer.msg(tianjiacehngg,{icon:1,time:1500},function(){
                                    loadUrl(_ctx+"/user/bankcard/index");
                                })
                            }else{
                                layer.msg(data.msg, {icon: 2});
                            }
                        }
                    }
                })

            })
            //删除
            $("#div_list").on("click","#remove",function(){
                var id = $(this).find("#bandId").val();
                layer.confirm(isdelete, {
                    btn: [quding,quxiao]
                }, function(){
                    $.ajax({
                        type : "POST",
                        dataType : "JSON",
                        url : _ctx + "/user/bankcard/removeBankCard",
                        cache : false,
                        data : {id:id},
                        success : function(data) {
                            if(data.success){
                                layer.closeAll('dialog');
                                loadUrl(_ctx+"/user/bankcard/index");
                            }else{
                                layer.msg(data.msg, {icon: 2});
                            }
                        }
                    })
                })
            })

            //点击提交图片
            $("#shangchuan").on("click",function(){
                document.getElementById("alipayPicture").click();
            })
            $("#shangchuan2").on("click",function(){
                document.getElementById("weChatPicture").click();
            })
            //支付宝图片回显
            $("#alipayPicture").on("change", function(){
                $("#zhicode").html("")
                var file = this.files[0];
                if (window.FileReader) {
                    var reader = new FileReader();
                    reader.readAsDataURL(file);
                    //监听文件读取结束后事件
                    reader.onloadend = function (e) {
                        $("#myImg").attr("src",e.target.result);    //e.target.result就是最后的路径地址
                        $("#myImg").css("width","100%");
                        $("#myImg").css("height","100%");
                        $("#shangchuan").css("padding","0");
                    }
                }
            })


            //微信图片回显
            $("#weChatPicture").on("change", function(){
                $("#zhicode2").html("")
                var file = this.files[0];
                if (window.FileReader) {
                    var reader = new FileReader();
                    reader.readAsDataURL(file);
                    //监听文件读取结束后事件
                    reader.onloadend = function (e) {
                        $("#myImg2").attr("src",e.target.result);    //e.target.result就是最后的路径地址
                        $("#myImg2").css("width","100%");
                        $("#myImg2").css("height","100%");
                        $("#shangchuan2").css("padding","0");
                    }
                }
            })
            //删除支付宝图片
            $("#deteteAlipay").on("click", function(){
                var alipay = $("#alipay").val();
                var id = $("#bankId").val();
                var  type = "1";
                if (alipay){
                    layer.confirm(isdelete, {
                        btn: [quding,quxiao]
                    }, function(){
                        $.ajax({
                            type : "POST",
                            dataType : "JSON",
                            url : _ctx + "/user/bankcard/detetePicture",
                            cache : false,
                            data : {id:id,type:type},
                            success : function(data) {
                                if(data.success){
                                    layer.closeAll('dialog');
                                    loadUrl(_ctx+"/user/bankcard/index");
                                }else{
                                    layer.msg(data.msg, {icon: 2});
                                }
                            }
                        })
                    })
                }else {
                    layer.msg("操作无效",{icon:2});return false;
                }
            })
            $("#deteteWechat").on("click", function(){
                var wechat = $("#wechat").val();
                var id = $("#bankId").val();
                var  type = "2";
                if (wechat){
                    layer.confirm(isdelete, {
                        btn: [quding,quxiao]
                    }, function(){
                        $.ajax({
                            type : "POST",
                            dataType : "JSON",
                            url : _ctx + "/user/bankcard/detetePicture",
                            cache : false,
                            data : {id:id,type:type},
                            success : function(data) {
                                if(data.success){
                                    layer.closeAll('dialog');
                                    loadUrl(_ctx+"/user/bankcard/index");
                                }else{
                                    layer.msg(data.msg, {icon: 2});
                                }
                            }
                        })
                    })
                }else {
                    layer.msg("操作无效",{icon:2});return false;
                }
            })
            //发送验证短信
            $("#sendBtn").on("click",function(){

                $(this).attr("disabled","disabled");

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
                                layer.msg(fasongchenggong, {icon: 1})
                                $(this).html(yifasong);
                                var time = 120;
                                window.clearInterval(pageTimer["setphone"]);
                                // 开启点击后定时数字显示
                                pageTimer["setphone"] = window.setInterval(function() {
                                    time = time - 1;
                                    if (time == 0) {
                                        $("#sendBtn").removeAttr("disabled");// 按钮可用
                                        $("#sendBtn").html(chongxinfasong);// 按钮可用
                                        clearInterval(pageTimer["setphone"]);
                                    } else {
                                        $("#sendBtn").html(time+miaochongxinfasong );
                                    }

                                }, 1000);

                            }else{
                                $("#sendBtn").removeAttr("disabled");// 按钮可用
                                $("#sendBtn").html(chongxinfasong);// 按钮可用
                                layer.msg(data.msg, {icon: 2})
                            }
                        }else{
                            $("#sendBtn").removeAttr("disabled");// 按钮可用

                            layer.msg(fasongshibai, {icon: 2})
                        }
                    },
                    error : function(e) {

                    }
                });

            });


        }
    }
})
