/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年1月13日 下午2:02:24
 */
package com.mz.oauth.util.organizationtree;

import com.mz.oauth.user.model.AppOrganization;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> TODO</p>  
 * @author:         Liu Shilei 
 * @Date :          2016年1月13日 下午2:02:24 
 */
public class Node {
	
	//值
	private AppOrganization value;
	
	//级别   从1开始
	private Integer level;
	
	//父级node
	private Node parent;
	
	//直属子结点
	private List<Node> children = new ArrayList<Node>();

	/**
	 * <p> TODO</p>
	 * @return:     AppOrganization
	 */
	public AppOrganization getValue() {
		return value;
	}

	/** 
	 * <p> TODO</p>
	 * @return: AppOrganization
	 */
	public void setValue(AppOrganization value) {
		this.value = value;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Node
	 */
	public Node getParent() {
		return parent;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Node
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * <p> TODO</p>
	 * @return:     Integer
	 */
	public Integer getLevel() {
		return level;
	}

	/** 
	 * <p> TODO</p>
	 * @return: Integer
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * <p> TODO</p>
	 * @return:     List<Node>
	 */
	public List<Node> getChildren() {
		return children;
	}

	/** 
	 * <p> TODO</p>
	 * @return: List<Node>
	 */
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	
	
	
	
	
}
