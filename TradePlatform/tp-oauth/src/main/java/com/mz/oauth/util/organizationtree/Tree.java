/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年1月13日 下午2:02:00
 */
package com.mz.oauth.util.organizationtree;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年1月13日 下午2:02:00 
 */
public class Tree {
	
	//根节点
	private Node root;
	
	//所有结点
	private List<Node>  nodes; 
	
	//深度
	private Integer depth;
	
	//广度
	private Integer range;
	
	/**
	 * 查询某一深度的结点个数
	 * <p> TODO</p>
	 * @author:         Liu Shilei
	 * @param:    @param depth
	 * @param:    @return
	 * @return: List<Node> 
	 * @Date :          2016年1月13日 下午3:16:31   
	 * @throws:
	 */
	public List<Node>  listByDepth(Integer depth){
		List<Node> list = new ArrayList<Node>();
		for(Node node : this.nodes){
			if(node.getLevel().compareTo(depth)==0){
				list.add(node);
			}
		}
		return list;
	}
	
	/**
	 * <p> TODO</p>
	 * @return:     Node
	 */
	public Node getRoot() {
		return root;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Node
	 */
	public void setRoot(Node root) {
		this.root = root;
	}

	/**
	 * <p> TODO</p>
	 * @return:     List<Node>
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/** 
	 * <p> TODO</p>
	 * @return: List<Node>
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getDepth() {
		return depth;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getRange() {
		return range;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setRange(Integer range) {
		this.range = range;
	}
	
	
	
	
}
