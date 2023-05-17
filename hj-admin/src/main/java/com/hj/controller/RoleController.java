package com.hj.controller;

import com.hj.domain.ResponseResult;
import com.hj.domain.dto.ChangeRoleStatusDto;
import com.hj.domain.entity.Role;
import com.hj.serivce.RoleMenuService;
import com.hj.serivce.RoleService;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.OutputKeys;
import java.util.List;

@RestController
@RequestMapping("system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 角色列表分页查询
     * @param role
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult roleList(Role role,Integer pageNum,Integer pageSize){
        return roleService.roleList(role,pageNum,pageSize);
    }

    /**
     * 更新状态
     * @param roleDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changStatus(@RequestBody ChangeRoleStatusDto roleDto){
        Role role = new Role();
        role.setId(roleDto.getRoleId());
        role.setStatus(roleDto.getStatus());
        return ResponseResult.okResult(roleService.updateById(role));
    }

    /**
     * 新增角色
     * 需要添加role表和role_menu关联表
     * @param role
     * @return
     */
  @PostMapping
    public ResponseResult addRole(@RequestBody Role role){
    roleService.addRole(role);
    return ResponseResult.okResult();
    }


    /**
     * 编辑角色信息回显
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseResult selectRole(@PathVariable Long id){
        return ResponseResult.okResult(roleService.getById(id));

    }

    /**
     * 更新角色信息
     * 权限信息先删除再
     * @param role
     * @return
     */
    @PutMapping
    public ResponseResult updateRole(@RequestBody Role role){
    roleService.updateRole(role);
    return ResponseResult.okResult();

    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseResult delRole(@PathVariable Long id){
        roleService.removeById(id);
        roleMenuService.deleteRoleMenu(id);
        return ResponseResult.okResult();
    }

    /**
     * 新增用户回显
     * @return
     */
    @GetMapping("/listAllRole")
    public ResponseResult selectRoleList(){
       List<Role> roles =  roleService.selectRoleList();
        return ResponseResult.okResult(roles);
    }
}
