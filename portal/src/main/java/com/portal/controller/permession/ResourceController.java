/**
 * ResourceController.java
 * Created at 2014-04-20
 * Created by yuhaiyang
 * Copyright (C) 2014 SHANGHAI, All rights reserved.
 */

package com.portal.controller.permession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.annotation.Log;
import com.portal.service.permession.IResourceService;

/**
 * <p>ClassName: ResourceController</p>
 * <p>Description: TODO</p>
 * <p>Author: yuhaiyang</p>
 * <p>Date: 2014-4-20</p>
 */
@Controller
public class ResourceController {
    
    /** 注入Resource资源 **/
    @Autowired
    private IResourceService rs;

    /**
     * <p>Description: 得到所有菜单树</p>
     * @param model 参数列表
     * @return  资源树列表地址
     */
    @RequestMapping
    @Log("得到所有的菜单")
    public String treeAll(Model model) {
        model.addAttribute("tree", this.rs.treeAll());
        return "admin/resource/treeList";
    }

    /**
     * <p>Description: TODO</p>
     * @param model 参数列表
     * @return 资源树对象列表
     */
    @RequestMapping
    public String findAll(Model model) {
        model.addAttribute("tree", this.rs.findAll());
        return "admin/permession/resource/resourceList";
    }

    /**
     * <p>Description: TODO</p>
     * @return 返回到资源列表页面
     */
    @RequestMapping
    public String toResourceListView() {
        return "admin/permession/resource/resourceList";
    }

    /**
     * <p>Description: TODO</p>
     * @return 添加资源地址
     */
    @RequestMapping
    public String toAddView() {
        return "admin/permession/resource/resourceAdd";
    }
}
