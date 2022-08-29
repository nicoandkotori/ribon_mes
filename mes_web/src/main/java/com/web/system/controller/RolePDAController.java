package com.web.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.util.CustomStringUtils;
import com.common.util.ResponseResult;
import com.common.util.StringUtils;
import com.common.util.TableResult;
import com.web.system.entity.RoleMenuPda;
import com.web.system.entity.RolePda;
import com.web.system.entity.User;
import com.web.system.service.IRoleMenuPdaService;
import com.web.system.service.IRolePdaService;
import com.web.system.service.IRoleUserPdaService;
import com.web.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 菜单信息
 *
 */
@RestController
@RequestMapping(value = "/sysinfo/rolepda")
public class RolePDAController {

    @Autowired
    private IRolePdaService roleService;
    @Autowired
    private IRoleUserPdaService roleUserService;
    @Autowired
    private IRoleMenuPdaService roleMenuService;
    @Autowired
    private IUserService userService;


    //region  角色管理
    @RequestMapping(value = "/getlist")
    public List<RolePda> getList(HttpServletRequest request,RolePda query){

        ResponseResult result = new ResponseResult();

        LambdaQueryWrapper<RolePda> select=new LambdaQueryWrapper<>();
        select = select.eq(RolePda::getIzDelete, false);
        if(query != null) {
            if (CustomStringUtils.isNotBlank(query.getRoleName())) {
                select = select.like(RolePda::getRoleName, query.getRoleName());
            }

        }
        select.orderByAsc(RolePda::getRoleSort);

        List<RolePda> List = roleService.list(select);
        result.setResult(List);
        return List;
    }

    @RequestMapping(value = "/save")
    public ResponseResult saveOrUpdateRole(String mdatas) throws Exception {
        ResponseResult result = new ResponseResult();
        String errMsg = "保存角色失败，原因：";
        if (CustomStringUtils.isBlank(mdatas)) {
            throw new Exception(errMsg + "参数异常！");
        }
        List<RolePda> list = JSON.parseArray(mdatas, RolePda.class);
        int count = this.roleService.saveOrUpdateRole(list);
        result.setSuccess(true);
        result.setMsg(count + "条数据保存成功！");
        return result;
    }

    @RequestMapping(value = "/deletebyid")
    public ResponseResult deleteById(String id) {
        ResponseResult result = new ResponseResult();
        String errMsg = "删除角色失败，原因：";
        if (CustomStringUtils.isBlank(id)) {
            result.setSuccess(false);
            result.setMsg("参数不为空！");
        }
        int count = this.roleService.deleteById(id);
        result.setSuccess(true);
        result.setMsg(count + "条数据删除成功！");
        return result;
    }
    //endregion

    //region   role user 角色人员

    /**
     * roleid查找用户列表
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/findUserListByRoleid")
    public TableResult<User> findUserListByRoleid(String roleId) {
        TableResult<User> result = new TableResult<User>();
        LambdaQueryWrapper<User> select=new LambdaQueryWrapper();
        select=select.eq(User::getIzDelete,0);
        /*id in (
                select UserID from sys_role_user where RoleID = #{roleid,jdbcType=VARCHAR}
        )*/
        select = select.apply("id in ( select user_id from sys_role_user_pda where role_id ='"+roleId+"')");
        List<User> list = this.userService.list(select);
        result.setRows(list);
        return result;
    }

    @RequestMapping(value = "/deleteroleuser")
    public ResponseResult deleteRoleUser(String userId, String roleId) {
        ResponseResult result = new ResponseResult();
        String errMsg = "删除用户失败，原因：";
        if (CustomStringUtils.isBlank(userId,roleId)) {
            result.setSuccess(false);
            result.setMsg("参数不能为空！");
        }
        int count = this.roleUserService.deleteUser(userId,roleId);
        result.setMsg(count + "条数据删除成功！");
        return result;
    }

    @RequestMapping(value = "/saveRoleUsers")
    @ResponseBody
    public ResponseResult saveUsers(String mdatas, String roleId) {
        ResponseResult result = new ResponseResult();
        if (StringUtils.isEmpty(mdatas)) {
            result.setSuccess(false);
            result.setMsg("参数不准确！");
            return result;
        }
        if (StringUtils.isEmpty(roleId)) {
            result.setSuccess(false);
            result.setMsg("请选择一个角色！");
            return result;
        }
        List<User> list = JSON.parseArray(mdatas, User.class);
        int count = roleUserService.saveUsers(list, roleId);
        result.setMsg(count + "条数据添加成功！");
        return result;
    }

    //endregion



    //region     role menu 菜单权限

    @RequestMapping(value = "/findRoleMenuByRoleid")
    @ResponseBody
    public ResponseResult findRoleMenuByRoleid(String roleId) {
        ResponseResult result = new ResponseResult();
        if (StringUtils.isEmpty(roleId)) {
            result.setSuccess(false);
            result.setMsg("参数不能为空");
            return result;
        }
        List<RoleMenuPda> list = this.roleMenuService.findRoleMenuByRoleId(roleId);
        result.setResult(list);
        return result;
    }

    @RequestMapping(value = "/saveOrDeleteRoleMenus")
    public ResponseResult saveOrDeleteRoleMenus(String mdatas, String roleId, HttpServletRequest request){
        ResponseResult result = new ResponseResult();
        String errMsg = "添加菜单权限失败，原因：";
        if (StringUtils.isEmpty(mdatas)) {
            result.setSuccess(false);
            result.setMsg("参数异常！");
            return  result;
        }
        if (StringUtils.isEmpty(roleId)) {
            result.setSuccess(false);
            result.setMsg("请选择一个角色！");
            return  result;

        }
        List<String> list = JSON.parseArray(mdatas,String.class);

        this.roleMenuService.saveOrDeleteRoleMenus(roleId, list);
        result.setMsg("添加菜单权限成功！");
        return result;
    }

    //endregion

}
