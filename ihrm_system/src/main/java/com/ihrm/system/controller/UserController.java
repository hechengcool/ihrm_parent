package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.client.DepartmentFeignClient;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    /**
     * 测试feign
     */
    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    public Result test(@PathVariable("id") String id) {
        Result result = departmentFeignClient.findById(id);
        return result;
    }

    /**
     * 通过文件
     */
    @RequestMapping(value = "/user/import", method = RequestMethod.POST)
    public Result importUser(@PathVariable("file") MultipartFile file) throws Exception {
        //1.1.解析file
        Workbook wb = new XSSFWorkbook(file.getInputStream());
        //1.2.获取Sheet
        Sheet sheet = wb.getSheetAt(0);
        //1.3.获取Sheet种的每一行，每一列
        //2.获取用户列表
        List<User> users = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            //获取行
            Row row = sheet.getRow(i);
            Object[] values = new Object[row.getLastCellNum() - 1];
            for (int j = 0; j < row.getLastCellNum(); j++) {
                //获取单元格
                Cell cell = row.getCell(j);
                //获取单元格内容
                Object cellValue = getCellValue(cell);
                values[j] = cellValue;
            }
            User user = new User();
            users.add(user);
        }
        userService.saveAll(users, companyId, companyName);
        return new Result(ResultCode.SUCCESS);
    }

    /*
        获取poi - Cell内容值
     */
    private Object getCellValue(Cell cell) {

        CellType type = cell.getCellType();
        Object value = null;
        switch (type) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            default:
                break;
        }
        return value;
    }


    /**
     * 1.通过service根据mobile查询用户
     * 2.比较password
     * 3.生成jwt信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Map<String, String> map) {
        String mobile = map.get("mobile");
        String password = map.get("password");
        System.out.println(password);
        try {
            //1.构造认证令牌
            //加密密码
            password = new Md5Hash(password, mobile, 3).toString();
            //构造UPToken
            UsernamePasswordToken uptoken = new UsernamePasswordToken(mobile, password);
            //2.获取subject
            Subject subject = SecurityUtils.getSubject();
            //3.调用login方法,进入realm认证
            subject.login(uptoken);
            //4.
            String id = (String) subject.getSession().getId();
            //构造返回结果
            return new Result(ResultCode.SUCCESS, id);
        } catch (Exception e) {
            return new Result(ResultCode.MOBILEORPASSWORD);
        }
    }

    /**
     * 用户成功登录之后，获取用户信息
     * 1.获取用户id
     * 2.根据id查询用户
     * 3.构建返回值对象
     * 4.响应
     *
     * @return
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {
        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        //2.获取安全数据
        ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS, result);
    }

    //分配角色
    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.PUT)
    public Result assignRoles(@RequestBody Map<String, Object> map) {
        //1.获取被分配用户id
        String userId = (String) map.get("id");
        //获取分配角色列表id
        List<String> roleIds = (List<String>) map.get("roleIds");
        //调用service完成分配
        userService.assignRoles(userId, roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    //保存企业
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        //业务操作
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.save(user);
        return new Result(ResultCode.SUCCESS);
    }

    //查询企业列表
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Result findAll(int page, int size, @RequestParam Map map) {
        map.put("companyId", companyId);
        Page<User> all = userService.findAll(map, page, size);
        PageResult<User> pr = new PageResult<>(all.getTotalElements(), all.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS, new UserResult(user));
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequiresPermissions("API-USER-DELETE")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, name = "API-USER-DELETE")
    public Result delete(@PathVariable("id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
