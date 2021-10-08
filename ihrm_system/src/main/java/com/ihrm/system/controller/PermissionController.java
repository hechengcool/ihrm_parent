package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.excetpion.CommonException;
import com.ihrm.domain.system.Permission;
import com.ihrm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/sys")
public class    PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    //保存
    @RequestMapping(value = "/permission", method = RequestMethod.POST)
    public Result save(@RequestBody Map<String, Object> map) throws Exception {
        //业务操作
        permissionService.save(map);
        return new Result(ResultCode.SUCCESS);
    }

    //查询列表
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public Result findAll(@RequestParam Map map) {
        List<Permission> perm = permissionService.findAll(map);
        return new Result(ResultCode.SUCCESS, perm);
    }

    //根据id查询
    @RequestMapping(value = "/permission/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) throws CommonException {
        Map<String, Object> map = permissionService.findById(id);
        return new Result(ResultCode.SUCCESS, map);
    }

    @RequestMapping(value = "/permission/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) throws Exception {
        map.put("id", id);
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/permission/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable("id") String id) throws CommonException {
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

}
