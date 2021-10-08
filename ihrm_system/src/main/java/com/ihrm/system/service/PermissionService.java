package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.excetpion.CommonException;
import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionService extends BaseService<Permission> {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionApiDao apiDao;
    @Autowired
    private PermissionMenuDao menuDao;
    @Autowired
    private PermissionPointDao pointDao;


    @Autowired
    private IdWorker idWorker;

    public void save(Map<String, Object> map) throws Exception {
        //基本属性设置
        String id = idWorker.nextId() + "";
        //通过map构造permission对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id);
        //2.根据类型构造不同的资源对象（菜单，按钮，api）
        int type = permission.getType();
        //3.保存权限
        switch (type) {
            case PermissionConstants.PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(id);
                menuDao.save(menu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(id);
                pointDao.save(point);
                break;
            case PermissionConstants.PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(id);
                apiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }

    /**
     * 更新权限
     */
    public void update(Map<String, Object> map) throws Exception {
        Permission perm = BeanMapUtils.mapToBean(map, Permission.class);
        //1.通过传递的权限id查询权限
        Permission permission = permissionDao.findById(perm.getId()).get();
        permission.setName(perm.getName());
        permission.setCode(perm.getCode());
        permission.setDescription(perm.getDescription());
        permission.setEnVisible(perm.getEnVisible());
        //2.根据类型构造不同的资源对象（菜单，按钮，api）
        int type = perm.getType();
        switch (type) {
            case PermissionConstants.PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(perm.getId());
                menuDao.save(menu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(perm.getId());
                pointDao.save(point);
                break;
            case PermissionConstants.PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(perm.getId());
                apiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        //3.保存权限
        permissionDao.save(permission);
    }

    /**
     * 根据id删除权限
     */
    public void deleteById(String id) throws CommonException {
        //1.通过传递的权限id查询权限
        Permission perm = permissionDao.findById(id).get();
        permissionDao.delete(perm);
        //2.根据类型构造不同的资源对象（菜单，按钮，api）
        int type = perm.getType();
        switch (type) {
            case PermissionConstants.PY_MENU:
                menuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                pointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                apiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }

    /**
     * 根据id查询权限
     * 1.根据id查询权限
     * 2.根据权限的类型查询资源
     * 3.构造map集合
     */
    public Map<String, Object> findById(String id) throws CommonException {
        Permission permission = permissionDao.findById(id).get();
        int type = permission.getType();
        Object obj = null;
        if (type == PermissionConstants.PY_MENU) {
            obj = menuDao.findById(id).get();
        } else if (type == PermissionConstants.PY_POINT) {
            obj = pointDao.findById(id).get();
        } else if (type == PermissionConstants.PY_API) {
            obj = apiDao.findById(id).get();
        } else {
            throw new CommonException(ResultCode.FAIL);
        }

        Map<String, Object> map = BeanMapUtils.beanToMap(obj);

        map.put("name", permission.getName());
        map.put("type", permission.getType());
        map.put("code", permission.getCode());
        map.put("description", permission.getDescription());
        map.put("pid", permission.getPid());
        map.put("enVisible", permission.getEnVisible());
        return map;
    }

    /**
     * 查询全部
     * type  ：权限类型 1为菜单 2为功能 3为API
     * enVisible : 0：查询所有saas平台的最高权限，1：查询企业的权限
     * pid ：父id
     */
    public List<Permission> findAll(Map map) {
        Specification<Permission> spec = getSpec(map);
        return permissionDao.findAll(spec);
    }
}
