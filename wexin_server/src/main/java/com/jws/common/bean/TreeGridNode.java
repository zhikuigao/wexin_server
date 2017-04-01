package com.jws.common.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 主界面中的树形列表
 */
public class TreeGridNode {

	/**
	 * 节点ID
	 */
	private Long id;

	/**
	 * 节点名称
	 */
	private String name;

	/**
	 * 操作员
	 */
	private String operaterCode;

	/**
	 * 绑定时间
	 */
	private Date createTime;

	/**
	 * cls,iconCls是css元素，指定视图效果
	 */
	private String cls;
	private String iconCls;

	/**
	 * 是否单击展开(默认为叶子节点的属性)
	 */
	private boolean singleClickExpand = false;

	/**
	 * 是否是叶子节点(默认为叶子节点的属性)
	 */
	private boolean leaf = true;

	/**
	 * 叶子节点
	 */
	private List<TreeGridNode> children;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperaterCode() {
		return operaterCode;
	}
	public void setOperaterCode(String operaterCode) {
		this.operaterCode = operaterCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isSingleClickExpand() {
		return singleClickExpand;
	}
	public void setSingleClickExpand(boolean singleClickExpand) {
		this.singleClickExpand = singleClickExpand;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public List<TreeGridNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeGridNode> children) {
		this.children = children;
	}

	public void addToChildren(TreeGridNode node) {
		if (this.children == null) {
			this.children = new ArrayList<TreeGridNode>();
		}
		this.children.add(node);
	}

}
