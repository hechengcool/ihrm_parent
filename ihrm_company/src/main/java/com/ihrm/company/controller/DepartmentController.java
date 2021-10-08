package com.ihrm.company.controller;


import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/company")
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService deptService;

    @Autowired
    private CompanyService companyService;

    //保存企业
    @RequestMapping(value = "/department", method = RequestMethod.POST)
    public Result save(@RequestBody Department dept) {
        //业务操作
        dept.setCompanyId(companyId);
        deptService.add(dept);
        return new Result(ResultCode.SUCCESS);
    }

    //查询企业列表
    @RequestMapping(value = "/department", method = RequestMethod.GET)
    public Result findAll() {
        Company company = companyService.findById(companyId);
        Map map = new HashMap();
        map.put("companyId", companyId);
        List<Department> list = deptService.findAll(map);

        DeptListResult deptList = new DeptListResult(company, list);
        Result result = new Result(ResultCode.SUCCESS, deptList);
        return result;
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        Department department = deptService.findById(id);
        return new Result(ResultCode.SUCCESS, department);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody Department department) {
        department.setId(id);
        deptService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable("id") String id) {
        deptService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
