package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDAO;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService extends BaseService<User> {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 保存企业
     * 1.配置idwork导工程
     * 2.在service中注入idwork
     * 3.通过idwork生成id
     * 4.保存企业
     */
    public void save(User user) {
        //基本属性设置
        String id = idWorker.nextId() + "";
        String password = new Md5Hash("123456", user.getMobile(), 3).toString();
        user.setLevel("user");
        user.setPassword(password);
        user.setEnableState(1);
        user.setId(id);
        //默认状态
        userDAO.save(user);
    }

    /**
     * 用户批量保存
     */
    @Transactional
    public void saveAll(List<User> users, String companyId, String companyName) {
        for (User user : users) {
            String id = idWorker.nextId() + "";
            String password = new Md5Hash("123456", user.getMobile(), 3).toString();
            user.setLevel("user");
            user.setPassword(password);
            user.setEnableState(1);
            user.setId(id);
            userDAO.save(user);
        }
    }

    /**
     * 修改企业
     */
    public void update(User user) {
        User temp = userDAO.findById(user.getId()).get();
        temp.setUsername(user.getUsername());
        temp.setPassword(user.getPassword());
        temp.setDepartmentId(user.getDepartmentId());
        temp.setDepartmentName(user.getDepartmentName());
        temp.setRoles(user.getRoles());
        userDAO.save(temp);
    }

    /**
     * 删除企业
     */
    public void deleteById(String id) {
        userDAO.deleteById(id);
    }

    /**
     * 根据id查询企业
     */
    public User findById(String id) {
        User user = userDAO.findById(id).get();
        return user;
    }

    /**
     * 查询企业列表
     */
    public Page findAll(Map map, int page, int size) {
        Specification<User> spec = getSpec(map);
        return userDAO.findAll(spec, new PageRequest(page - 1, size));
    }

    /**
     * 分配角色
     */
    public void assignRoles(String userId, List<String> roleIds) {
        //获取用户对象
//        Optional<User> optional = userDAO.findById(userId);
        User user = userDAO.findById(userId).get();
        //设置用户角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        user.setRoles(roles);
        //更新用户
        userDAO.save(user);
    }

    /**
     * 根据mobile查询用户
     *
     * @param mobile
     * @return
     */
    public User findByMobile(String mobile) {
        return userDAO.findByMobile(mobile);
    }
}
