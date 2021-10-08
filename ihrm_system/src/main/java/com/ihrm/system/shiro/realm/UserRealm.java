package com.ihrm.system.shiro.realm;

import com.ihrm.common.shiro.realm.IhrmRealm;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRealm extends IhrmRealm {
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    //认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.获取用户手机和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String mobile = upToken.getUsername();
        //2.根据手机号查询
        String password = new String(upToken.getPassword());
        User user = userService.findByMobile(mobile);
        //3.判断密码是否一致
        if (user != null && user.getPassword().equals(password)) {
            //4.构造数据并返回
            ProfileResult result = null;

            if ("user".equals(user.getLevel())) {
                result = new ProfileResult(user);
            } else {
                Map map = new HashMap();
                if ("coAdmin".equals(user.getLevel())) {
                    map.put("enVisible", "1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user, list);
            }
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
            return info;
        }
        //返回null抛出异常
        return null;
    }

}
