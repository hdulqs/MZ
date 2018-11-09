/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年1月13日 下午2:07:37
 */
package com.mz.oauth.util.organizationtree;

import com.mz.oauth.user.model.AppOrganization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年1月13日 下午2:07:37 
 */
public class TreeFactory {
	
	private static TreeFactory  instance = new TreeFactory();
	
	private TreeFactory(){}
	
	public static TreeFactory getInstance(){
		return instance;
	}
	
	/**
	 * 创建组织结构树
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param list
	 * @param:    @return
	 * @return: Tree 
	 * @Date :          2016年1月13日 下午2:14:26   
	 * @throws:
	 */
	public Tree createTree(List<AppOrganization> list){
	
		Tree tree = new Tree();
		List<Node>  nodes = new ArrayList<Node>();
		
		//设置根结点
		Node root = new Node();
		root.setValue(getRoot(list));
		root.setParent(null);
		root.setLevel(1);
		nodes.add(root);
		tree.setNodes(nodes);
		tree.setRoot(root);
		
		//节点装箱
		nodeBinning(root, nodes, list);
		
		//设置深度值
		tree.setDepth(getDepth(nodes));
		//设置广度值
		tree.setRange(getRange(nodes));
		
		return tree;
	}
	
	/**
	 * <p>获得广度</p>
	 * @author:         Liu Shilei
	 * @param:    @param nodes
	 * @param:    @return
	 * @return: Integer 
	 * @Date :          2016年1月13日 下午2:25:08   
	 * @throws:
	 */
	private Integer getRange(List<Node> nodes) {
		
		int range = 0;
		Map<Integer,Integer>  map = new HashMap<Integer, Integer>();
		for(Node  node : nodes){
			if(map.get(node.getLevel())==null){
				map.put(node.getLevel(), 1);
			}else{
				map.put(node.getLevel(), map.get(node.getLevel())+1);
			}
		}
		
		Set<Entry<Integer, Integer>> entrySet = map.entrySet();
		Iterator<Entry<Integer, Integer>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Integer> entry = iterator.next();
			if(entry.getValue()>range){
				range = entry.getValue();
			}
		}
		
		return range;
	}

	/**
	 * <p>获得深度</p>  
	 * @author:         Liu Shilei
	 * @param:    @param nodes
	 * @param:    @return
	 * @return: Integer 
	 * @Date :          2016年1月13日 下午2:23:00   
	 * @throws:
	 */
	private Integer getDepth(List<Node> nodes) {
		int depth = 1;
		for(Node node : nodes){
			if(node.getLevel().intValue()>depth){
				depth = node.getLevel().intValue();
			}
		}
		return depth;
	}

	/**
	 * 
	 * <p>获得根结点</p>
	 * @author:         Liu Shilei
	 * @param:    @param list
	 * @param:    @return
	 * @return: AppOrganization 
	 * @Date :          2016年1月13日 下午12:21:39   
	 * @throws:
	 */
	private AppOrganization getRoot(List<AppOrganization> list){
		for(AppOrganization appOrganization : list){
			if("root".equals(appOrganization.getType())){
				return appOrganization;
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * <p> 节点装箱</p>  
	 * @author:         Liu Shilei
	 * @param:    @param node
	 * @param:    @param nodes
	 * @param:    @param list
	 * @return: void 
	 * @Date :          2016年1月13日 下午2:20:11   
	 * @throws:
	 */
	private  void nodeBinning(Node node,List<Node> nodes,List<AppOrganization> list){
		for(AppOrganization appOrganization : list){
			if(appOrganization.getPid()!=null){
				if(node.getValue().getId().compareTo(appOrganization.getPid())==0){
					Node childNode = new Node();
					childNode.setValue(appOrganization);
					childNode.setParent(node);
					childNode.setLevel(node.getLevel()+1);
					//装入子结点集合
					node.getChildren().add(childNode);
					nodes.add(childNode);
					//递归
					nodeBinning(childNode, nodes, list);
				}
			}
		}
	}
	
	
	
	
}
