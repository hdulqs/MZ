/**
 * @author:      zongwei
 * @version:     V1.0
 * @Date:        20180526
 */
define([ 'app', 'hryTable', 'layer' ,'pikadayJq','hryUtil'], function(app, DT, layer) {

    // 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
    function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {

        $rootScope.headTitle = $rootScope.title = "otc交易订单";



        /**
         * ------------------------列表页面路径---------------------------------------------
         */
        if ($stateParams.page == "list") {
            var table = $("#dataTable");
            $scope.serchData = {
                //status_EQ : 1
            };
            // --------------------加载dataTable--------------------------------
            var config = DT.config();

            config.onlyClick = function(b){
                if(b.attr("src")=='1'){
                    payInforfun(b);
                }else if(b.attr("src")=='2'){
                    applyArbitrationfun(b);
                }else if(b.attr("src")=='3'){
                    applyArbitrationfunonlyready(b);
                }
            }

            config.bAutoSerch = true; // 是否开启键入搜索
            config.ajax.url = HRY.modules.customer + 'businessman/otcOrdertransaction/list';
            config.ajax.data = function(d) {

                // 设置select下拉框
                DT.selectData($scope.serchData);

                $.each($scope.serchData, function(name, value) {
                    if ("" != value) {
                        eval("d." + name + "='" + value + "'");
                    }
                });
            }
            config.columns = [
                {
                    "data" : "id"   //id
                },
                {
                    "data" : "transactionNum"   //交易订单号
                },
                {
                    "data" : "created"   //创建时间
                },
                {
                    "data" : "coinCode"   //交易种类
                },
                {
                    "data" : "transactionPrice"   //单价
                },
                {
                    "data" : "transactionCount"   //数据量
                },
                {
                    "data" : "transactionMoney"   //金额
                },
                {
                    "data" : "fee"   //手续费
                },
                {
                    "data" : "paymentTime"   //付款时间
                },
                {
                    "data" : "finishTime"   //成交时间
                },
				{
                    "data" : "buyCustomname"   //买方
                },
                {
                    "data" : "sellCustomname"   //卖方
                },
                {
                    "data" : "statusByDes"   //状态
                },
                {
                    "data" : "status"   //操作
                }

            ]
            config.columnDefs = [ {
                "targets" : 0,
                "orderable" : false,
                "orderable" : false,
                "render" : function(data, type, row) {
                    return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
                },
            } , {
                "targets" : 13,
                "orderable" : false,
                "orderable" : false,
                "render" : function(data, type, row) {
                    var btn = '<input type="button" value="付款信息" src="1"  data-rowid="'+row.id+'"  style="width: 100px"    class="btn btn-spacing btn-info payInforfun"></input>';
                    if(data==6){
                        btn += '<input type="button" value="申诉处理"  src="2" data-rowid="'+row.id+'"  class="btn btn-pric"></input>';
                    }else {
                        if(row.appealFlag == "Y"){
                            btn += '<input type="button" value="申诉查看"  src="3" data-rowid="'+row.id+'"  class="btn btn-pric"></input>';
                        }
                    }
                    return btn;
                },

            } ]
            // --------------------加载dataTable--------------------------------
            DT.draw(table, config);

            /**
             * 刷新按钮
             */
            $scope.fnList = function(){
                table.DataTable().draw();
            }
            //查看付款信息
            function  payInforfun (e){
              var recordid =   e.attr("data-rowid");
                $http.get(HRY.modules.customer + "businessman/otcOrdertransaction/otcPayInfor?id=" + recordid).success(function(data) {
                    $scope.formData = data;
                });
                $("#OtcPayInfor").removeClass("hide");
                layer.open({
                    type : 1,
                    title : "查看付款信息",
                    closeBtn : 2,
                    area : [ '700px', '800px' ],
                    shadeClose : true,
                    content : $('#OtcPayInfor')
                });
            }
            //申诉处理
            function applyArbitrationfun(e){
                var recordid =   e.attr("data-rowid");
                $http.get(HRY.modules.customer + "businessman/otcOrdertransaction/applyArbitrationinfo?id=" + recordid).success(function(data) {
                    $scope.applyArbitrationData = data;
                });
                $("#applyArbitrationinfobt").removeClass("hide");
                $("#applyArbitrationHandle").addClass("hide");
                $("#applyArbitrationinfo").removeClass("hide");
                layer.open({
                    type : 1,
                    title : "申诉处理",
                    closeBtn : 2,
                    area : [ '700px', '800px' ],
                    shadeClose : true,
                    content : $('#applyArbitrationinfo')
                });
            };
            //申诉查看
            function applyArbitrationfunonlyready(e){
                var recordid =   e.attr("data-rowid");
                $http.get(HRY.modules.customer + "businessman/otcOrdertransaction/applyArbitrationinfo?id=" + recordid).success(function(data) {
                    $scope.applyArbitrationData = data;
                });
                $("#applyArbitrationinfobt").addClass("hide");
                $("#applyArbitrationHandle").removeClass("hide");
                $("#applyArbitrationinfo").removeClass("hide");
                layer.open({
                    type : 1,
                    title : "申诉处理",
                    closeBtn : 2,
                    area : [ '700px', '800px' ],
                    shadeClose : true,
                    content : $('#applyArbitrationinfo')
                });
            };

            $scope.close_otc = function() {
                $scope.close_otcData = {
                };
                var id = $("#otctransactionorderid").val();
                $scope.close_otcData.id=id;
                $http({
                    method : 'POST',
                    url : HRY.modules.customer + 'businessman/otcOrdertransaction/otcColse',
                    data : $.param($scope.close_otcData),
                    headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).success(function(data) {
                    if (data.success) {
                        growl.addInfoMessage('关闭成功');
                       // window.location.href = '#/customer/businessman/otcOrder/list/anon';
                        window.location.reload();
                    } else {
                        growl.addInfoMessage(data.msg);
                    }
                });
            };

            $scope.finish_otc = function() {
                $scope.finish_otc = {
                };
                var id = $("#otctransactionorderid").val();
                $scope.finish_otc.id=id;
                $http({
                    method : 'POST',
                    url : HRY.modules.customer + 'businessman/otcOrdertransaction/finishOtcOrder',
                    data : $.param($scope.finish_otc),
                    headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).success(function(data) {
                    if (data.success) {
                        growl.addInfoMessage('操作成功');
                       // window.location.href = '#/customer/businessman/otcOrder/list/anon';
                        window.location.reload();
                    } else {
                        growl.addInfoMessage(data.msg);
                    }
                });
            };

            $scope.refuse_otc = function() {
                $scope.refuse_otc = {
                };
                var id = $("#otctransactionorderid").val();
                $scope.refuse_otc.id=id;
                $http({
                    method : 'POST',
                    url : HRY.modules.customer + 'businessman/otcOrdertransaction/otcrefuse',
                    data : $.param($scope.refuse_otc),
                    headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).success(function(data) {
                    if (data.success) {
                        growl.addInfoMessage('驳回成功');
                      //  window.location.href = '#/customer/businessman/otcOrder/list/anon';
                        window.location.reload();
                    } else {
                        growl.addInfoMessage(data.msg);
                    }
                });
                // var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                // //window.parent.refreshtable;//访问父页面方法
                // parent.layer.close(index);
                // parent.location.reload();
            }

            //导出Excel
            $scope.fnExcel = function() {
                DT.excel(table,$scope.serchData,"OTC交易订单！");
            }



        }



        // 加载插件
        hryCore.initPlugins();
        // 上传插件
        hryCore.uploadPicture();
    }

    // -----------controller.js加载完毕--------------
    return {
        controller : controller
    };
});