package com.ihrm.company.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDAO;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private IdWorker idWorker;


    /**
     * 保存企业
     * 1.配置idwork导工程
     * 2.在service中注入idwork
     * 3.通过idwork生成id
     * 4.保存企业
     */
    public void add(Company company) {
        //基本属性设置
        String id = idWorker.nextId() + "";
        company.setId(id);
        //默认状态
        company.setAuditState("1");// 0 未审核 1 已审核
        company.setState(0);//0 表示未激活 1 表示激活
        companyDAO.save(company);
    }

    /**
     * 修改企业
     */
    public void update(Company company) {
        Company temp = companyDAO.findById(company.getId()).get();
        temp.setName(company.getName());
        temp.setCompanyPhone(company.getCompanyPhone());
        companyDAO.save(temp);
    }

    /**
     * 删除企业
     */
    public void deleteById(String id) {
        companyDAO.deleteById(id);
    }

    /**
     * 根据id查询企业
     */
    public Company findById(String id) {
        Company company = companyDAO.findById(id).get();
        return company;
    }

    /**
     * 查询企业列表
     */
    public List<Company> findAll() {
        return companyDAO.findAll();
    }
}
