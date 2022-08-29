package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.entity.RolePda;

import java.util.List;

/**
 * <p>
 * 菜单  服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IRolePdaService extends IService<RolePda> {

    int saveOrUpdateRole(List<RolePda> list) throws Exception;
    int deleteById(String id);
}
