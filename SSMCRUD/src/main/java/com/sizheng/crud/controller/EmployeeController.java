package com.sizheng.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sizheng.crud.bean.Employee;
import com.sizheng.crud.bean.Msg;
import com.sizheng.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工CRUD请求
 */
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 查询员工数据（分页查询）
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(
            @RequestParam(value = "pn", defaultValue = "1")Integer pn){
        //引入PageHelper分页插件
        //在查询之前，只需要调用 , 传入页码，以及每页的大小
        PageHelper.startPage(pn, 5);
        //startPage紧跟的这个查询就是一个分页查询
        List<Employee> employeeList = employeeService.getAll();
        //使用PageInfo包装查询后的结果
        System.out.println(employeeList.toString());
        PageInfo page = new PageInfo(employeeList, 5);
        return Msg.success().add("pageInfo", page);
    }

    /**
     * 查询员工数据（分页查询）
     * @return
     */
    @RequestMapping("/empss")
    @ResponseBody
    public List<Employee> getEmps(){
        List<Employee> employeeList = employeeService.getAll();
        return employeeList;
    }

    /**
     * 保存员工信息
     * 1.支持JSP303校验
     * 2.导入Hibernate-Validatoe
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        if (result.hasErrors()){
            //校验失败应该返回失败，在模态框中显示校验失败的错误信息
            Map<String, Object> map = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                System.out.println("错误字段命" + fieldError.getField());
                System.out.println("错误信息" + fieldError.getDefaultMessage());
                map.put(fieldError.getField() , fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        }else {
            System.out.println(employee);
            employeeService.saveEmp(employee);
            return Msg.success();
        }

    }

    /**
     * 检验用户名是否相同
     */
    @RequestMapping(value = "/checkuser", method = RequestMethod.POST)
    @ResponseBody
    public Msg checkuser(@RequestParam("empName") String empName){
        //先判断用户名是否是合法的表达式
        String regx = "(^[\u2E80-\u9FFF]{2,5})"; //pattern: /^[\u4E00-\u9FA5]+$/,
        if (!empName.matches(regx)){
            return Msg.fail().add("va_msg", "用户名必须是2到5位中文");
        }
        //数据库用户名重复校验
        boolean b = employeeService.checkuser(empName);
        if (b){
            return Msg.success();
        }else{
            return Msg.fail().add("va_msg", "用户名不可用");
        }
    }

    /**
     * 按照id查询员工
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp", employee);
    }

    /**
     * 如果直接发送ajax=PUT形式的请求
     * 封装的数据会有null
     * 问题：请求体中有数据但是Employee封装不上
     * 原因：tomcat一看时PUT请求就不会封装请求体
     *
     * 员工更新方法
     * @return
     */
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    @ResponseBody
    public Msg saveEmp(Employee employee){
        System.out.println(employee);
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     *`删除员工(单个，批量二合一)
     * 批量删除：1-2-3；
     * 单个删除：1
     * @return
     */
    @RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmp(@PathVariable("ids")String ids){
        if(ids.contains(("-"))){
            //批量删除
            String[] str_ids = ids.split("-");
            //组装id的集合
            List<Integer> del_ids = new ArrayList<>();
            for(String string : str_ids){
                del_ids.add(Integer.parseInt(string));
            }
            employeeService.deleteBatch(del_ids);
        }else {
            //单个删除
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }

        return Msg.success();
    }
}
