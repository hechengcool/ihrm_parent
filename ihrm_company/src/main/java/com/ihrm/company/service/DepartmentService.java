package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDAO;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DepartmentService extends BaseService<Department> {
    @Autowired
    private DepartmentDAO deptDAO;

    @Autowired
    private IdWorker idWorker;

    public void add(Department dept) {
        //基本属性设置
        String id = idWorker.nextId() + "";
        dept.setId(id);
        deptDAO.save(dept);
    }

    public void update(Department dept) {
        Department temp = deptDAO.findById(dept.getId()).get();
        temp.setCode(dept.getCode());
        temp.setIntroduce(dept.getIntroduce());
        temp.setName(dept.getName());
        deptDAO.save(temp);
    }

    /**
     * 删除部门
     */
    public void deleteById(String id) {
        deptDAO.deleteById(id);
    }

    /**
     * 根据id查询部门
     */
    public Department findById(String id) {
        Department dept = deptDAO.findById(id).get();
        return dept;
    }

    /**
     * 查询部门列表
     */
    public List<Department> findAll(Map map) {
        return deptDAO.findAll(getSpec(map));
    }

}
