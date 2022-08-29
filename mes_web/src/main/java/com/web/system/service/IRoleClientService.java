package com.web.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.system.entity.RoleClient;

import java.util.List;

/**
 * <p>
 * 菜单  服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface IRoleClientService extends IService<RoleClient> {

    int saveOrUpdateRole(List<RoleClient> list) throws Exception;
    int deleteById(String id);
}
