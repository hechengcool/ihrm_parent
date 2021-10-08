package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/sys")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 分配权限
     */
    @RequestMapping(value = "/role/assignPrem", method = RequestMethod.PUT)
    public Result assignPerms(@RequestBody Map<String, Object> map) {
        //1.获取被分配用户id
        String roleId = (String) map.get("id");
        //获取分配角色列表id
        List<String> permIds = (List<String>) map.get("permIds");
        //调用service完成分配
        roleService.assignPerms(roleId, permIds);
        return new Result(ResultCode.SUCCESS);
    }

    //保存企业
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public Result add(@RequestBody Role role) {
        //业务操作
        role.setCompanyId(companyId);
        roleService.save(role);
        return new Result(ResultCode.SUCCESS);
    }

    //查询分页角色列表
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Result findByPage(Integer page, Integer size, @RequestParam Map map) {
        map.put("companyId", companyId);
        Page<Role> all = roleService.findByPage(map, page, size);
        PageResult<Role> pr = new PageResult<>(all.getTotalElements(), all.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }

    //查询角色列表
    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    public Result findAll() {
        List<Role> all = roleService.findAll(companyId);
        return new Result(ResultCode.SUCCESS, all);
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        Role role = roleService.findById(id);
        return new Result(ResultCode.SUCCESS, new RoleResult(role));
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody Role role) {
        role.setId(id);
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable("id") String id) {
        roleService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
