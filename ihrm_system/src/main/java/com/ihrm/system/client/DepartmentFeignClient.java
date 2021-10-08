package com.ihrm.system.client;

import com.ihrm.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 声明接口，通过feign调用其他服务
 */
@FeignClient("ihrm-company")
public interface DepartmentFeignClient {

    @RequestMapping(value = "/company/department/{id}", method = RequestMethod.GET)
    Result findById(@PathVariable("id") String id);

}
