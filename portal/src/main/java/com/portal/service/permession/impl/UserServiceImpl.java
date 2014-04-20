package com.portal.service.permession.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.po.TUserPo;
import com.portal.service.impl.BaseServiceImpl;
import com.portal.service.permession.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<TUserPo> implements UserService {

	@Autowired
	private SqlSessionTemplate sst;
	
	public void finalAll() {
		
	}

	public void findById() {
		
	}

	public void insert() {
		
	}

	public void update() {
		
	}

}
