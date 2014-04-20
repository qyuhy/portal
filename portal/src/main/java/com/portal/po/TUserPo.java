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
public class TUserPo extends Po{

	private String upadteBy;
	private String userName;
	private Date updateTime;
	private String remark;
	private String createBy;
	private Date createTime;
	private String userPassword;
	private String userId;
	private String userSex;
	private Date userBirthday;
	private Integer userAge;

	public void setUpadteBy(String upadteBy){
		this.upadteBy=upadteBy;
	}

	public String getUpadteBy(){
		return this.upadteBy;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserName(){
		return this.userName;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

	public Date getUpdateTime(){
		return this.updateTime;
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

	public void setUserPassword(String userPassword){
		this.userPassword=userPassword;
	}

	public String getUserPassword(){
		return this.userPassword;
	}

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setUserSex(String userSex){
		this.userSex=userSex;
	}

	public String getUserSex(){
		return this.userSex;
	}

	public void setUserBirthday(Date userBirthday){
		this.userBirthday=userBirthday;
	}

	public Date getUserBirthday(){
		return this.userBirthday;
	}

	public void setUserAge(Integer userAge){
		this.userAge=userAge;
	}

	public Integer getUserAge(){
		return this.userAge;
	}

}