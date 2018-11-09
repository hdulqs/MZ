/**
 * zzjgt.js
 */
define(['app','d3'], function (app) {
	
    //也可以使用这样的显式注入方式，angular执行controller函数前，会先读取$inject
    app.controller.$inject = ['$scope','$rootScope','$http','$stateParams','$state'];
    function controller($scope,$rootScope,$http,$stateParams,$state,hryCore ){
        $rootScope.headTitle = $rootScope.title = "组织结构图";
		

        function loadD3JS(width,height){

        //边界空白
        var padding = {left: 200, right:50, top: 20, bottom: 20 };
        var svg = d3.select("picture")
                   .append("svg")
        	       .attr("width", width + padding.left + padding.right)
        	       .attr("height", height + padding.top + padding.bottom)
                   .append("g")
             	   .attr("transform","translate("+ padding.left + "," + padding.top + ")");

        //树状图布局
        var tree = d3.layout.tree().size([height, width]);

        //对角线生成器
        var diagonal = d3.svg.diagonal()
            .projection(function(d) { return [d.y, d.x]; });

        var url = HRY.modules.oauth+"company/apporganization/loadTree";

        d3.json(url,function(error,roots){

        //查找根结点
        function findRoot(data){
        	for(var i = 0 ; i <data.length ; i++){
        		if(data[i].type=="root"){
        			return data[i];
        		}
        	}
        }
        //查找子节点
        function findChildren(node,data){
        	var nodes = [];
        	for(var i = 0 ; i <data.length ; i++){
        		if(node.id==data[i].pid){
        			nodes.push(data[i]);
        		}
        	}
        	return nodes;
        }

        //递归封装数据
        function createRoot(startnode,rootNode,childrenNodes,data){
        		for(var i = 0 ; i < childrenNodes.length ; i++){
        			var node = {
        							name:childrenNodes[i].name,
        							number :Math.random(),
        							children :[]
        					   }
        			startnode.children.push(node);		
        			var childrens = findChildren(childrenNodes[i],data);
        			if(childrens.length>0){
        				createRoot(node,childrenNodes[i],childrens,data);
        			}
        		}
        }

        //根节点数据
        var rootNode = findRoot(roots);
        //根节点
        var root = {
    				name : rootNode.name,
    				number :Math.random(),
    				children : []
        		   }
        //根节点的一级子节点
        var childrenNodes = findChildren(rootNode,roots);
        //递归开始
        createRoot(root,rootNode,childrenNodes,roots);

        	
          //给第一个节点添加初始坐标x0和x1
          root.x0 = height / 2;
          root.y0 = 0;

          //以第一个节点为起始节点，重绘
          redraw(root);

          //重绘函数
          function redraw(source){

            /**
           	 *	(1) 计算节点和连线的位置
             */

            //应用布局，计算节点和连线
            var nodes = tree.nodes(root);
            var links = tree.links(nodes);

            //重新计算节点的y坐标
            nodes.forEach(function(d) { d.y = d.depth * 180; });

            /**
             *   (2) 节点的处理
             */

            //获取节点的update部分
            var nodeUpdate = svg.selectAll(".node").data(nodes, function(d){ return d.name+d.number });

            //获取节点的enter部分
            var nodeEnter = nodeUpdate.enter();

            //获取节点的exit部分
            var nodeExit = nodeUpdate.exit();

            //1. 节点的 Enter 部分的处理办法
            var enterNodes = nodeEnter.append("g")
                            .attr("class","node")
                            .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
                            .on("click", function(d) { toggle(d); redraw(d); });

            enterNodes.append("circle")
              .attr("r", 0)
              .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

            enterNodes.append("text")
                .attr("x", function(d) { return d.children || d._children ? -14 : 14; })
                .attr("dy", ".35em")
                .attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
                .text(function(d) { return d.name; })
                .style("fill-opacity", 0);

            //2. 节点的 Update 部分的处理办法
            var updateNodes = nodeUpdate.transition()
                                .duration(1000)   //节点渲染时间  单位毫秒
                                .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; });

            updateNodes.select("circle")
              .attr("r", 10)  //节点的半径  单位像素
              .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });  //节点中心颜色

            updateNodes.select("text")
              .style("fill-opacity", 1);

            //3. 节点的 Exit 部分的处理办法
            var exitNodes = nodeExit.transition()
                              .duration(500)  //节点收缩时间  单位毫秒
                              .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
                              .remove();

            exitNodes.select("circle")
              .attr("r", 0);//收缩效果的半径

            exitNodes.select("text")
              .style("fill-opacity", 0);

            /*
                         （3） 连线的处理
            */

            //获取连线的update部分
            var linkUpdate = svg.selectAll(".link")
                                .data(links, function(d){
                               	 return d.target.name+d.target.number;
                                });

            //获取连线的enter部分
            var linkEnter = linkUpdate.enter();

            //获取连线的exit部分
            var linkExit = linkUpdate.exit();

            //1. 连线的 Enter 部分的处理办法
            linkEnter.insert("path",".node")
                  .attr("class", "link")
                  .attr("d", function(d) {
                      var o = {x: source.x0, y: source.y0};
                      return diagonal({source: o, target: o});
                  })
                  .transition()
                  .duration(500)
                  .attr("d", diagonal);

            //2. 连线的 Update 部分的处理办法
            linkUpdate.transition()
                .duration(500)  //连线的渲染时间
                .attr("d", diagonal);

            //3. 连线的 Exit 部分的处理办法
            linkExit.transition()
                  .duration(500)//连线的收缩时间
                  .attr("d", function(d) {
                    var o = {x: source.x, y: source.y};
                    return diagonal({source: o, target: o});
                  })
                  .remove();


            /*
            (4) 将当前的节点坐标保存在变量x0、y0里，以备更新时使用
            */
            nodes.forEach(function(d) {
              d.x0 = d.x;
              d.y0 = d.y;
            });

          }

          //切换开关，d 为被点击的节点
          function toggle(d){
            if(d.children){ //如果有子节点
              d._children = d.children; //将该子节点保存到 _children
              d.children = null;  //将子节点设置为null
            }else{  //如果没有子节点
              d.children = d._children; //从 _children 取回原来的子节点 
              d._children = null; //将 _children 设置为 null
            }
          }

        });
        }

        	var width = 800;
        	var height = 600;
        	loadD3JS(width,height);
        	
        	/**
        	 * 变大方法
        	 */
        	$("#add").click(function(){
        		//清除picture下的所有元素
        		$("picture").children().remove();
        		
        		width += 200;
        		height += 200;
        		//重新生成
        		loadD3JS(width,height);
        	});
        	
        	/**
        	 * 变小方法
        	 */
        	$("#remove").click(function(){
        		//清除picture下的所有元素
        		$("picture").children().remove();
        		width = width-200;
        		height = height-200;
        		//重新生成
        		loadD3JS(width,height);
        	});
        	
        
    }
        
    return {controller:controller};
});