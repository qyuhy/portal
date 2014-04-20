/**
 * IResourceService.java
 * Created at yuhaiyang
 * Created by yuhaiyang
 * Copyright \(C\) \d\d\d\d SHANGHAI VOLKSWAGEN, All rights reserved\.$
 */
package com.portal.service.permession;

import java.util.List;

import com.portal.po.TResourcePo;
import com.portal.service.BaseService;
import com.portal.vo.TreeVo;

/**
 * hello
 * @author 6910p
 */
public interface IResourceService extends BaseService<TResourcePo>{
    
    public List<TreeVo> treeAll();
    
}
