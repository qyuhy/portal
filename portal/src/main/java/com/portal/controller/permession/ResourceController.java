package com.portal.controller.permession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.annotation.Log;
import com.portal.service.permession.IResourceService;

@Controller
public class ResourceController {
	
	@Autowired
	private IResourceService rs;
	
	@RequestMapping
	@Log("得到所有的菜单")
	public String treeAll(Model model){
		model.addAttribute("tree",rs.treeAll());
		return "admin/resource/treeList";
	}
	
	
	@RequestMapping
	public String findAll(Model model){
		model.addAttribute("tree",rs.findAll());
		return "admin/permession/resource/resourceList";
	}
	
	@RequestMapping
	public String toResourceListView(){
		return "admin/permession/resource/resourceList";
	}
	
	@RequestMapping
	public String toAddView(){
		return "admin/permession/resource/resourceAdd";
	}
}
