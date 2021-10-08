package com.ihrm.company.dao;

import com.ihrm.domain.company.Company;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyDAOTest {
    @Autowired
    private CompanyDAO companyDAO;

    @Test
    public void test() {

        Company company = companyDAO.findById("1").get();
        System.out.println(company.getName());
    }

}