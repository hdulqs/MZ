/**
* <p> Angular DateTable 的封装实现模板+分页的功能</p> 
* @param: @param $ 
* @return: void 
* @throws
 */

function hryAngularDT(vm ,option,DTOptionsBuilder,DTColumnBuilder,$compile, $scope,$http) {
	//全选按钮
	var titleHtml = '<input type="checkbox" id="checkboxAll" ng-model="list.selectAll" ng-click="list.toggleAll(list.selectAll, list.selected)"> <label for="checkboxAll"> </label>';

	vm.$http=$http;
	vm.$scope=$scope;
	vm.selected = {};
    vm.selectAll = false;
    //全选
    vm.toggleAll = this.toggleAll;
    //选择一条
    vm.toggleOne = this.toggleOne;
    
   // vm.dtInstance = {};
    //刷新
    vm.reloadData=this.reloadData;
    
	vm.dtOptions = DTOptionsBuilder.newOptions()
     .withOption('ajax', {
      data : function(d) {
						d.page = d.start/d.length+1;
						d.pageSize = d.length;
					},
      url: option.url,
      type: option.type==undefined?"post":option.type
  })
  .withDataProp('rows')
  .withOption('processing', option.processing==undefined?true:option.processing)
  .withOption('serverSide', option.serverSide==undefined?true:option.serverSide)
  .withOption('autoWidth', false)
  .withOption('lengthChange', option.lengthChange==undefined?true:option.lengthChange)
  .withOption('filter', option.filter==undefined?true:option.filter)
  .withOption('deferRender', option.deferRender==undefined?true:option.deferRender)
  .withOption('displayLength', 10)
   .withOption('createdRow', function(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        })
        .withOption('headerCallback', function(header) {
            if (!$scope.headerCompiled) {
                // Use this headerCompiled field to only compile header once
                $scope.headerCompiled = true;
                $compile(angular.element(header).contents())($scope);
            }
        })
  .withOption('lengthMenu', [
							      [5, 10, 25, 50, -1],
							      [5, 10, 25, 50, "全部"]
							      ])
  
  .withPaginationType('full_numbers') 
  .withLanguage({
      "processing": "处理中...",
      "lengthMenu": "显示 _MENU_ 项结果",
      "zeroRecords": "没有匹配结果",
      "info": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
      "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
      "infoFiltered": "(由 _MAX_ 项结果过滤)",
      "infoPostFix": "",
      "search": "搜索:",
      "url": "",
      "emptyTable": "表中数据为空",
      "loadingRecords": "载入中...",
      "infoThousands": ",",
      "paginate": {
          "first": "首页",
          "previous": "上页",
          "next": "下页",
          "last": "末页"
      },
      "aria": {
          "sortAscending": ": 以升序排列此列",
          "sortDescending": ": 以降序排列此列"
      }
});
	
	vm.dtColumns = [];
	if(option.select==undefined||option.select){
	var select=	 DTColumnBuilder.newColumn(null).withTitle(titleHtml).notSortable()
         .renderWith(function(data, type, full, meta) {
             vm.selected[full.id] = false;
             return '<input type="checkbox" id="checkbox'+data.id+'" ng-model="list.selected[' + data.id + ']" ng-click="list.toggleOne(list.selected)"> <label for="checkbox'+data.id+'"> </label>';
         });
	
	vm.dtColumns.push(select);
	}
	 return vm;
 }
/**
* <p> 全选</p> 
* @param: @param selectAll
* @param: @param selectedItems 
* @return: void 
* @throws
 */
function toggleAll (selectAll, selectedItems) {
    for (var id in selectedItems) {
        if (selectedItems.hasOwnProperty(id)) {
            selectedItems[id] = selectAll;
        }
    }
}
/**
 * 选择一条
 */
function toggleOne (selectedItems) {
    var me = this;
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
/**
 * 
* <p>删除选中</p> 
* @param: @param obj {"1":true,"2":true}
* @param: @param option 请求参数
* @return: void 
* @throws
 */
function remove(obj,url){
	 
	var ids = transform(obj);
	if(ids.length==0){
		alert("请选择数据");
		return false;
	}
	var scope=this.$scope;
	 this.$http({
         method: "POST", 
         url:url,
         params:{'ids':ids}
     }).
     success(function(data, status,headers, config) {
    	// alert(data.msg);
    	 
    	  
    	// this.dtInstance.reloadData(callback, true);
     }).
     error(function(data, status) {
    	 alert("删除失败，code："+status);
   })
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
    return arr;
}
/**
 * 
* <p>reloadData</p> 
* @param:  
* @return: void 
* @throws
 */
function reloadData() {
	 this.newSource = 'data1.json';
    var resetPaging = false;
    this.dtInstance.reloadData(callback, resetPaging);
}
 /**
  * 
 * <p> 回调函数</p> 
 * @param: @param json 
 * @return: void 
 * @throws
  */
function callback(json) {
	//  alert(json)
    console.log(json);
}
