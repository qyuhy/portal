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
import com.portal.po.Po;

@SuppressWarnings("serial")
public class TRolePo extends Po{

	private Date updateTime;
	private String roleId;
	private String remark;
	private String createBy;
	private Date createTime;
	private String roleName;
	private String updateBy;

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

	public Date getUpdateTime(){
		return this.updateTime;
	}

	public void setRoleId(String roleId){
		this.roleId=roleId;
	}

	public String getRoleId(){
		return this.roleId;
	}

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

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setRoleName(String roleName){
		this.roleName=roleName;
	}

	public String getRoleName(){
		return this.roleName;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

	public String getUpdateBy(){
		return this.updateBy;
	}

}