/**
 * applendconfig.js
 */
define(['app','hryTable'], function (app) {

    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams'];
    function controller($scope,$rootScope,$http,$stateParams){
        $rootScope.headTitle = $rootScope.title = "杠杆基础配置";

        //数据对象
        $scope.formData={};

        $http.get( HRY.modules.web+'app/applendconfig/find').
        success(function(data) {
            $scope.keyList=data;
        });


        /**
         * 添加页面
         */
        if($stateParams.page=="add"){
            $scope.processForm = function() {
                $scope.formData.typekey= $("#appRoleSet").val();
                $http({
                    method : 'POST',
                    url : HRY.modules.web+'app/applendconfig/add.do',
                    data : $.param($scope.formData),
                    headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                })
                    .success(function(data) {
                        if (data.success) {
                            growl.addInfoMessage('添加成功')

                            window.location.href='#/web/app/applendconfig/list/anon';
                        } else {
                            growl.addInfoMessage('添加失败')
                        }

                    });

            };
        }




        /**
         * 列表页面
         */
        if($stateParams.page=="list"){

            fnList();

            $scope.fnToggleAll = fnToggleAll; //全选
            $scope.fnToggleAll = fnToggleAll;//选择一条
            $scope.selected = {};
            $scope.selectAll = false;

            $scope.fnAdd=fnAdd;//add按钮方法
            $scope.fnSee=fnSee;//see按钮方法
            $scope.fnModify=fnModify;//see按钮方法
            $scope.fnRemove=fnRemove;//remove方法
            $scope.fnList=fnList;//list方法

            $scope.tabSwith=tabSwith;//tabSwith方法
            $scope.conType='financeLendConfig';//默认显示基础配置
            $('input[name=typeConfig]').val("financeLendConfig");

            //添加按钮
            //ng-click="fnAdd(url)"
            function fnAdd(url){
                window.location.href='#/web/'+url+'/anon';


            }

            //查看按钮
            //ng-click="fnSee(url,selectes)"
            function fnSee(url,selectes){
                var ids = transform(selectes);
                if(ids.length==0){
                    growl.addInfoMessage('请选择数据')
                    return false;
                }else if(ids.length>1){
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }else{
                    $rootScope.id= ids[0];
                    window.location.href='#/'+url+'/'+$rootScope.id;
                }
            }

            //修改按钮
            //ng-click="fnModify(url,selectes)"
            function fnModify(url,selectes){
                var ids = transform(selectes);
                if(ids.length==0){
                    growl.addInfoMessage('请选择数据')
                    return false;
                }else if(ids.length>1){
                    growl.addInfoMessage('只能选择一条数据')
                    return false;
                }else{
                    $rootScope.id= ids[0];
                    window.location.href='#/'+url+'/'+$rootScope.id;
                }
            }

            //删除按钮
            //ng-click="fnRemove(url,selectes)"
            function fnRemove(url,selectes){
                var ids = transform(selectes);
                if(ids.length==0){
                    growl.addInfoMessage('请选择数据')
                    return false;
                }
                $http({
                    method: "POST",
                    url:"http://localhost/hurong_"+url+".do",
                    params:{'ids':ids}
                }).
                success(function(data, status,headers, config) {
                    if(data.success){
                        //提示信息
                        growl.addInfoMessage('删除成功')
                        //重新加载list
                        fnList();
                    }
                }).
                error(function(data, status) {
                    growl.addInfoMessage('删除失败')
                })
            }

            /**
             * 加载数据
             */
            function fnList(){
                $http.get( HRY.modules.web+'app/applendconfig/list').
                success(function(data) {
                    $scope.list = data;
                });
            }

            function tabSwith(name,event){

                $scope.conType=name;//显示的配置的类型
                $('input[name=typeConfig]').val(name);
                $(event.currentTarget).parent().addClass("active").siblings().removeClass("active");
                $("textarea").remove();

            }

            /**
             * <p> 将对象中 为true 的对象解析为数组，用于删除操作</p>
             * @param: @param obj
             * @param: @returns {Array}
             * @return: Array
             * @throws
             */
            function transform(obj){
                var arr = [];
                for(var item in obj){
                    if(obj[item]){arr.push(item)};
                }
                //重置
                $scope.selected = {};
                return arr;
            }
            /**
             * <p> 全选</p>
             * @param: @param selectAll
             * @param: @param selectedItems
             * @return: void
             * @throws
             */
            function fnToggleAll (selectAll, selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
            }
            /**
             * 选择一条
             */
            function fnToggleOne (selectedItems) {
                var me = $scope;
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        if(!selectedItems[id]) {
                            me.selectAll = false;
                            return;
                        }
                    }
                }
                me.selectAll = true;
            }
            $scope.formData.path="111111";
            $scope.processForm = function() {
                var type=$('input[name=typeConfig]').val();
                var data="configType!!="+type+"!@";
                var appconfigForm = $("#appconfigForm").serializeArray();
                console.log(appconfigForm);
                for(da in	$scope.list){
                    if($scope.list[da].typekey==type){//把所有该类型下的属性与属性值拼接起来


                        if($scope.list[da].datatype==3){
                            data=data+$scope.list[da].configkey+"!!="+$("#"+$scope.list[da].configkey).val()+"!@"


                        }else{
                            data=data+$scope.list[da].configkey+"!!="+$scope.list[da].value+"!@"
                        }

                    }

                }
                data = "postdata={"+data+"}";
                data = data.replace(/&/g,"#");
                data = data.replace(/\+/g,"^");
                $http({
                    method : 'POST',
                    url : HRY.modules.web+'app/applendconfig/modify',
                    data : data,
                    headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded'
                    }
                })
                    .success(function(data) {
                        if (data.success) {
                            growl.addInfoMessage('设置成功')
                            window.location.href='#/web/app/applendconfig/list/anon';
                        } else {
                            growl.addInfoMessage('设置失败')
                        }

                    });

            };

        }

    }
    return {controller:controller};
});