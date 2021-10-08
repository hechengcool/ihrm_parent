package com.ihrm.company.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.excetpion.CommonException;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 需要与前端进行交互
 */
@CrossOrigin//处理跨域
@RestController
@RequestMapping("/company")
public class CompanyController {


    @Autowired
    private CompanyService companyService;

    //保存企业
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result save(@RequestBody Company company) {
        //业务操作
        companyService.add(company);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id修改企业
    @RequestMapping(value = "/{companyId}", method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "companyId") String id, @RequestBody Company company) {
        //业务操作
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id删除企业
    @RequestMapping(value = "/{companyId}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "companyId") String id) {
        //业务操作
        companyService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询企业
    @RequestMapping(value = "/{companyId}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "companyId") String id) throws CommonException {
        //业务操作
        Company company = companyService.findById(id);
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(company);
        return result;
    }

    //查询企业列表
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Result findAll() {
        //业务操作
        List<Company> all = companyService.findAll();
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(all);
        return result;
    }
}
