package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Service
public class RoleService extends BaseService<Role> {
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private IdWorker idWorker;

    public void assignPerms(String roleId, List<String> permIds) {
        //1.获取被分配角色对象
        Role role = roleDao.findById(roleId).get();
        //2.构造分配权限集合
        Set<Permission> perms = new HashSet<>();
        for (String permId : permIds) {
            Permission permission = permissionDao.findById(permId).get();
            //根据父id查询API权限
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PY_API, permission.getId());
            perms.addAll(apiList);
            perms.add(permission);
        }
        //3.设置角色和权限的关系
        role.setPermissions(perms);
        //4.更新角色
        roleDao.save(role);
    }

    /**
     * 保存角色
     *
     * @param role
     */
    public void save(Role role) {
        //基本属性设置
        String id = idWorker.nextId() + "";
        role.setId(id);
        //默认状态
        roleDao.save(role);
    }

    /**
     * 更新角色
     */
    public void update(Role role) {
        Role temp = roleDao.findById(role.getId()).get();
        temp.setDescription(role.getDescription());
        temp.setName(role.getName());
        temp.setPermissions(role.getPermissions());
        roleDao.save(temp);
    }

    /**
     * 根据id删除角色
     */
    public void deleteById(String id) {
        roleDao.deleteById(id);
    }

    /**
     * 根据id查询角色
     */
    public Role findById(String id) {
        Role role = roleDao.findById(id).get();
        return role;
    }

    /**
     * 查询分页 角色列表
     */
    public Page<Role> findByPage(Map map, int page, int size) {
        Specification<Role> spec = getSpec(map);
        return roleDao.findAll(spec, new PageRequest(page - 1, size));
    }

    /*
        查询全部角色
     */
    public List<Role> findAll(String companyId) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        Specification<Role> spec = getSpec(map);
        return roleDao.findAll(spec);
    }
}
