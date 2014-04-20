package com.portal.service.permession.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.portal.po.TResourcePo;
import com.portal.service.impl.BaseServiceImpl;
import com.portal.service.permession.IResourceService;
import com.portal.statics.TreeStatic;
import com.portal.vo.TreeVo;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<TResourcePo> implements
		IResourceService {
	
	
	@Override
	public List<TreeVo> treeAll() {
		String sql = "select * from t_resource where res_pid = '"+TreeStatic.MENU_ROOT+"' order by res_order ";
		List<TResourcePo> list = this.find(sql,null);
		List<TreeVo> children = iterator(list);
		return children;
	}
	
	private List<TreeVo> iterator(List<TResourcePo> children){
		List<TreeVo> retValue = new ArrayList<TreeVo>();
		if(children!=null && children.size()>0){
			for(TResourcePo tp : children){
				retValue.add(convert(tp));
				String treeSql = "select * from t_resource where res_pid = '"+tp.getResId()+"' order by res_order ";
				List<TResourcePo> templist = this.find(treeSql,null);
				if(templist!=null && templist.size()>0){
					retValue.addAll(iterator(templist));
				}
			}
		}
		return retValue;
	}
	
	private TreeVo convert(TResourcePo po){
		TreeVo vo = new TreeVo();
		Map<String,Object> attribute = new HashMap<String,Object>();
		attribute.put("url", po.getResUrl());
		vo.setAttributes(attribute);
		vo.setIconCls(po.getResIcon());
		vo.setId(po.getResId());
		vo.setPid(po.getResPid());
		vo.setState("open");
		vo.setText(po.getResName());
		return vo;
	}

}
