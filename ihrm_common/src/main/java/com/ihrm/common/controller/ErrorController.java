package com.ihrm.common.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ErrorController {
    @RequestMapping(value = "/autherror")
    public Result autherror(int code) {
        return code == 1 ? new Result(ResultCode.UNAUTHENTICATED) : new Result(ResultCode.UNAUTHORISE);
    }
}