/**
 * Copyright:   北京互融时代软件有限公司
 * ExDmLendTimes.js
 * @author:      Gaomm
 * @version:     V1.0
 * @Date:        2017-11-29 18:36:30
 */
define([ 'app', 'hryTable', 'layer' ], function(app, DT, layer) {

    // 也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = [ '$scope', '$rootScope', '$http', '$stateParams', '$state' ];
    function controller($scope, $rootScope, $http, $stateParams, $state, hryCore) {

        /**
         * ------------------------查看页面路径---------------------------------------------
         */
        if ($stateParams.page == "see") {
            $http.get(HRY.modules.exchange + "lend/exdmlendtimes/see/" + $stateParams.id).success(function(data) {
                $scope.formData = data;
            });
        }

        /**
         * ------------------------添加页面路径---------------------------------------------
         */
        if ($stateParams.page == "add") {

            $scope.formData = {};
            $scope.processForm = function() {
                $http({
                    method : 'POST',
                    url : HRY.modules.exchange + 'lend/exdmlendtimes/add',
                    data : $.param($scope.formData),
                    headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).success(function(data) {
                    if (data.success) {
                        growl.addInfoMessage('添加成功')
                        window.location.href = '#/exchange/lend/exdmlendtimes/list/anon';
                    } else {
                        growl.addInfoMessage(data.msg)
                    }
                });

            };
        }

        /**
         * ------------------------修改页面路径---------------------------------------------
         */
        if ($stateParams.page == "modify") {

            $http.get(HRY.modules.exchange + "lend/exdmlendtimes/see/" + $stateParams.id).success(function(data) {
                $scope.formData = data;
            });

            $scope.processForm = function() {

                $http({
                    method : 'POST',
                    url : HRY.modules.exchange + 'lend/exdmlendtimes/modify',
                    data : $.param($scope.formData),
                    headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                }).success(function(data) {
                    if (data.success) {
                        growl.addInfoMessage('修改成功')
                        window.location.href = '#/exchange/lend/exdmlendtimes/list/anon';
                    } else {
                        growl.addInfoMessage(data.msg)
                    }
                });

            };

        }

        /**
         * ------------------------列表页面路径---------------------------------------------
         */
        if ($stateParams.page == "list") {
            var table =  $("#table2");
            $scope.serchData = {
            };
            // --------------------加载dataTable--------------------------------
            var config = DT.config();
            config.bAutoSerch = true; // 是否开启键入搜索
            config.ajax.url = HRY.modules.account + 'lend/exdmlendtimes/list.do';
            config.ajax.data = function(d) {

                // 设置select下拉框
                DT.selectData($scope.serchData);
                DT.inputData($scope.serchData);
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
                    "data" : "userName"   //userName
                },
                {
                    "data" : "trueName"   //trueName
                },
                {
                    "data" : "lendTimes"   //申请的杠杆倍数
                },
                /*{
                    "data" : "lendTimesStatus"   //申请状态1申请成功，2审批驳回
                },*/
                {
                    "data" : "applyTime"   //申请的时间
                },
                {
                    "data" : "status"   //申请状态1申请中，2审批完
                }
            ]
            config.columnDefs = [ {
                "targets" : 0,
                "orderable" : false,

                "render" : function(data, type, row) {
                    return "<input type=\"checkbox\" id=\"checkbox" + data + "\" /><label for=\"checkbox" + data + "\"></label>"
                }
            },{
                "targets" : 5,

                "render" : function(data, type, row) {
                    if(data==1){
                        return "待审核"
                    }else if(data==2){
                        if(row.lendTimesStatus==1){
                            return "通过"
                        }else{
                            return "被驳回"
                        }
                    }

                }
            } ]
            DT.draw(table, config);
            // --------------------加载dataTable--------------------------------

            $scope.fnList = fnList;// 刷新方法

            //导出excel
            $scope.fnExcel = function() {
                DT.excel(table,this.serchData,"充值申请审核");
            }

            $scope.fnConfirm = fnConfirm;// 确认充值
            $scope.fnInvalid = fnInvalid;
            $scope.fnInvalid1 = fnInvalid1;
            $scope.fnQuery = fnQuery;// 查看订单状态

            // 通过充值方法
            function fnConfirm() {
                var ids = DT.getSelect(table);
                if (ids.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                } else if (ids.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                } else if(DT.getRowData(table)[0].status==2){
                    growl.addInfoMessage('该请求已被审核')
                    return false;
                }else{

                    $scope.confirm = DT.getRowData(table)[0];

                    $('#fnConfirmDiv').removeClass("hide");
                    layer.open({
                        type : 1,
                        title : "通过杠杆变更确认",
                        closeBtn : 2,
                        area : [ '450px', '440px' ],
                        shadeClose : true,
                        content : $('#fnConfirmDiv')
                    });

                    // 充值通过提交
                    $scope.fnConfirmSubmit = function() {
                        var id = $scope.confirm.id;
                        var lengRiskRate=$("#lengRiskRate").val();
                        var lengPing=$("#lengPing").val();
                        $http({
                            method : 'POST',
                            url : HRY.modules.account + 'lend/exdmlendtimes/confirm',
                            params : {
                                'id' : id,
                                'lengPing':lengPing,
                                'lengRiskRate':lengRiskRate
                            },
                            headers : {
                                'Content-Type' : 'application/x-www-form-urlencoded'
                            }
                        }).success(function(data) {
                            if (!data.success) {
                                $scope.errorName = data.msg;
                            } else {
                                growl.addInfoMessage('确认成功')
                                fnList();
                            }
                            layer.closeAll();
                        });

                    }

                }
            }
            // 驳回处理
            function fnInvalid1() {
                var post_ids="";
                var ids = DT.getSelect(table);
                if (ids.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                } else if (ids.length > 1) {
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                } else if(DT.getRowData(table)[0].status==2){
                    growl.addInfoMessage('该请求已被审核')
                    return false;
                } else {
                    $scope.invalid = DT.getRowData(table)[0];

                    $('#fnInvalidDiv').removeClass("hide");
                    layer.open({
                        type : 1,
                        title : "驳回确认",
                        closeBtn : 2,
                        area : [ '400px', '400px' ],
                        shadeClose : true,
                        content : $('#fnInvalidDiv')
                    });
                    // 驳回确认提交
                    $scope.fnInvalidSubmit = function() {
                        var description = $("#description").val();
                        var param = {
                            id : $scope.invalid.id,
                            description:description
                        };
                        $http({
                            method : 'POST',
                            url : HRY.modules.account + 'lend/exdmlendtimes/invalid',
                            data : $.param(param),
                            headers : {
                                'Content-Type' : 'application/x-www-form-urlencoded'
                            }
                        }).success(function(data) {
                            if (!data.success) {
                                $scope.errorName = data.msg;
                            } else {
                                growl.addInfoMessage('处理成功')
                                fnList();
                            }
                            layer.closeAll();
                        });
                    };

                }
            }
            // 无效处理(批量)
            function fnInvalid() {
                var post_ids="";
                var ids = DT.getSelect(table);
                if (ids.length == 0) {
                    growl.addInfoMessage('请选择数据')
                    return false;
                }

                else  {
                    for(var i=0;i<ids.length;i++){
                        post_ids=ids[i]+"_"+post_ids
                    }
                    // 驳回确认提交
                    var param = {
                        id:post_ids
                    }
                    $http({
                        method : 'POST',
                        url : HRY.modules.account + 'lend/exdmlendtimes/invalids',
                        data : $.param(param),
                        headers : {
                            'Content-Type' : 'application/x-www-form-urlencoded'
                        }
                    }).success(function(data) {
                        if (!data.success) {
                            $scope.errorName = data.msg;
                        } else {
                            growl.addInfoMessage('处理成功')
                            fnList();
                        }
                        layer.closeAll();
                    });

                };

            }

            // 查询订单状态
            function fnQuery() {}

            // 刷新按钮
            function fnList() {
                table.DataTable().draw();
            }

            // 刷新按钮
            $scope.fnList =function() {
                table.DataTable().draw();
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