package test;

import domain.School;
import service.SchoolService;
import com.alibaba.fastjson.JSON;

import java.sql.SQLException;
import java.util.Collection;

public class FastJsonTest {
    public static void main(String[] args) {
        try{
            //获得所有学院
            Collection<School> schools = SchoolService.getInstance().findAll();
            //将集合类对象转换为json字串
            String schools_str = JSON.toJSONString(schools);
            System.out.println(schools_str);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}

//    Collection<Teacher> teachers = TeacherService.getInstance().findAll();
//    String str1= JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
//        System.out.println(str1);
