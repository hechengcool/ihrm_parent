package com.ihrm.common.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseService<T> {
    protected Specification<T> getSpec(Map map) {
        Specification<T> spect = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(cb.equal(root.get("companyId").as(String.class), (String) map.get("companyId")));
                }

                if (!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(cb.equal(root.get("departmentId").as(String.class), (String) map.get("departmentId")));
                }
                if (!StringUtils.isEmpty(map.get("hasDept"))) {
                    if (!StringUtils.isEmpty(map.get("hasDept")) || "0".equals((String) map.get("hasDept"))) {
                        list.add(cb.isNull(root.get("departmentId")));
                    } else {
                        list.add(cb.isNotNull(root.get("departmentId")));
                    }
                }

                //permission
                if (!StringUtils.isEmpty(map.get("pid"))) {
                    list.add(cb.equal(root.get("pid").as(String.class), (String) map.get("pid")));
                }
                if (!StringUtils.isEmpty(map.get("enVisible"))) {
                    list.add(cb.equal(root.get("enVisible").as(String.class), (String) map.get("enVisible")));
                }
                if (!StringUtils.isEmpty(map.get("type"))) {
                    String ty = (String) map.get("type");
                    CriteriaBuilder.In<Object> in = cb.in(root.get("type"));
                    if ("0".equals(ty)) {
                        in.value(1).value(2);
                    } else {
                        in.value(Integer.parseInt(ty));
                    }
                    list.add(in);
                }
                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return spect;
    }

}
