package com.portal.vo;

import java.util.Map;

public class TreeVo extends Vo{
	
	private static final long serialVersionUID = 8520519828106383708L;
	
	private String id;
	private String text;
	private Map<String,Object> attributes;
	private boolean checked;
	private String state;
	private String iconCls;
	private String pid;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Map<String,Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String,Object> attributes) {
		this.attributes = attributes;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
}
