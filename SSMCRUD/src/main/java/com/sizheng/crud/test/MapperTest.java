package com.sizheng.crud.test;

import com.sizheng.crud.bean.Department;
import com.sizheng.crud.bean.Employee;
import com.sizheng.crud.dao.DepartmentMapper;
import com.sizheng.crud.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * 测试DAO层
 *
 * 推荐Spring项目就可以使用Spring的单元测试，可以自动注入我们需要的组件
 * 1.导入SpringTest模块
 * 2.@ContextConfiguration指定配置文件的位置
 * 3.直接autowired要使用的组件即可
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    SqlSession sqlSession;

    /**
     * 测试 DepartmentMapper
     */
    @Test
    public void testCrud(){
        /*//1.创建SpringIOC容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2.从容器中获取mapper
        DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);*/
        System.out.println(departmentMapper);

        //1.插入几个部门
        departmentMapper.insertSelective(new Department(null, "经理部"));

        //2.生成员工数据，测试员工插入
        /*employeeMapper.insertSelective(new Employee(null, "Jerry", "M", "Jerry@qq.com", 1));*/

        //3.批量插入
       /* EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for (int i = 0; i < 1000; i++){
            String substring = UUID.randomUUID().toString().substring(0, 5) + i;
            mapper.insertSelective(new Employee(null, substring, "M", substring+"@qq.com", 1));
        }*/
    }
}