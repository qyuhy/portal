/*
* Copyright (c) 2013 Portal, Inc. All  Rights Reserved.
* This software is published under the terms of the Portal Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* CreateDate : 2013-10-19 20:39:38
* CreateBy   : 6910p
* Comment    : generate by com.portal.po.POGen
*/

package com.portal.po;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("serial")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TResourcePo extends Po{

	private String remark;
	private String createBy;
	private String resId;
	private String resPid;
	private String updateBy;
	private Long resOrder;
	private String resName;
	private Date updateTime;
	private String resUrl;
	private Integer resLevel;
	private Date createTime;
	private String resType;
	@JsonProperty("iconCls")
	private String resIcon;

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public String getCreateBy(){
		return this.createBy;
	}

	public void setResId(String resId){
		this.resId=resId;
	}

	public String getResId(){
		return this.resId;
	}

	public void setResPid(String resPid){
		this.resPid=resPid;
	}

	public String getResPid(){
		return this.resPid;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

	public void setResOrder(Long resOrder){
		this.resOrder=resOrder;
	}

	public Long getResOrder(){
		return this.resOrder;
	}

	public void setResName(String resName){
		this.resName=resName;
	}

	public String getResName(){
		return this.resName;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

	public Date getUpdateTime(){
		return this.updateTime;
	}

	public void setResUrl(String resUrl){
		this.resUrl=resUrl;
	}

	public String getResUrl(){
		return this.resUrl;
	}

	public void setResLevel(Integer resLevel){
		this.resLevel=resLevel;
	}

	public Integer getResLevel(){
		return this.resLevel;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setResType(String resType){
		this.resType=resType;
	}

	public String getResType(){
		return this.resType;
	}

	public void setResIcon(String resIcon){
		this.resIcon=resIcon;
	}

	public String getResIcon(){
		return this.resIcon;
	}

}