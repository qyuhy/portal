package com.portal.controller.permession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.po.PagerPo;
import com.portal.po.TUserPo;
import com.portal.service.permession.UserService;

@Controller
public class UserController{

	/*
	@Autowired
	private UserService uc;
	
	//@RequestMapping("UserController/userList.do")
	@RequestMapping()
	public String userList(){
		return "admin/userList";
	}
	
	//@RequestMapping("UserController/add.do")
	@RequestMapping() 
	public String add(){
		
		TUserPo po = new TUserPo();
		po.setUId(UUID.randomUUID().toString());
		po.setUName("12world");
		po.setUPassword("12tianya");
		List<TUserPo> pos = new ArrayList<TUserPo>();
		pos.add(po);
		uc.add(pos);
		return "admin/userList";
	}
	
	//@RequestMapping("UserController/delete.do")
	@RequestMapping() 
	public String delete(){
		
		TUserPo po = new TUserPo();
		po.setUId("463822d9-c8e1-4736-85f6-c7c5d0113f71");
		uc.delete(po);
		return "admin/userList";
	}
	
	//@RequestMapping("UserController/update.do")
	@RequestMapping() 
	public String update(){
		TUserPo po = new TUserPo();
		po.setUId("feb607f0-bd87-4c2e-ae0a-5f603b77a311");
		po.setUName("helloworld");
		po.setUPassword("world hello");
		TUserPo where = new TUserPo();
		where.setUId(po.getUId());
		uc.update(po, where);
		return "admin/userList";
	}
	
	//@RequestMapping("UserController/list.do")
	@RequestMapping() 
	public String list(){
		List<TUserPo> list = uc.findAll();
		for(TUserPo po : list){
			System.out.println(po);
		}
		return "admin/userList";
	}
	
	
	//@RequestMapping("UserController/list2.do")
	@RequestMapping() 
	public String list2(){
		TUserPo po = new TUserPo();
		po.setUName("1world");
		List<TUserPo> list = uc.find(po);
		for(TUserPo po2 : list){
			System.out.println(po2);
		}
		return "admin/userList";
	}
	
	//@RequestMapping("UserController/select.do")
	@RequestMapping() 
	public String select(){
		List<Map<String,Object>> list = uc.select("com.portal.service.UserService.selectAll", null);
		for(Map<String,Object> map : list){
			Set<String> keys = map.keySet();
			for(String key : keys){
				System.out.print("key-->"+key+"\tvalue-->"+map.get(key)+"\n");
			}
		}
		return "admin/userList";
	}
	
	//@RequestMapping("UserController/page.do")
	@RequestMapping() 
	public String page(){
		PagerPo<TUserPo> page = new PagerPo<TUserPo>();
		page.setStart(0);
		page.setPageSize(2);
		page.getOrder().put("u_name","desc");
		page.getParams().put("uName", "1world");
		page = uc.getScrollData("page", page);
		for(TUserPo po2 : page.getEntities()){
			System.out.println(po2);
		}
		return "admin/userList";
	}
	
	//@RequestMapping("UserController/map.do")
	@RequestMapping() 
	public String map(){
		PagerPo<Map<String,Object>> page = new PagerPo<Map<String,Object>>();
		page.setStart(0);
		page.setPageSize(2);
		page.getOrder().put("u_name","desc");
		page.getParams().put("uName", "1world");
		page = uc.getScrollDataForMap("page", page);
		for(Map<String,Object> po2 : page.getEntities()){
			System.out.println(po2);
		}
		return "admin/userList";
	}
	
	
	//@RequestMapping("UserController/page2.json")
	@RequestMapping() 
	public String page2(Model model){
		PagerPo<TUserPo> page = new PagerPo<TUserPo>();
		page.setStart(0);
		page.setPageSize(2);
		
		TUserPo po = new TUserPo();
		po.setUName("1world");
		
		page = uc.getScrollData(po, page);
		for(TUserPo po2 : page.getEntities()){
			System.out.println(po2);
		}
		model.addAttribute("users", page.getEntities());
		return "admin/userList";
	}
	*/
}
