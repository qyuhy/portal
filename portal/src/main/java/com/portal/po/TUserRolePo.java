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
public class TUserRolePo extends Po{

	private String userRoleId;
	private String upadteBy;
	private Date updateTime;
	private String roleId;
	private String createBy;
	private Date createTime;
	private String userId;

	public void setUserRoleId(String userRoleId){
		this.userRoleId=userRoleId;
	}

	public String getUserRoleId(){
		return this.userRoleId;
	}

	public void setUpadteBy(String upadteBy){
		this.upadteBy=upadteBy;
	}

	public String getUpadteBy(){
		return this.upadteBy;
	}

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

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

}