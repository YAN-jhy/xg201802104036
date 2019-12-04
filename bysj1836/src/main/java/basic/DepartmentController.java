package basic;

import domain.Department;
import service.DepartmentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 将所有方法组织在一个Controller(Servlet)中
 */
@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
    //    GET, http://localhost:8080/department.ctl?id=1, 查询id=1的系
    //    GET, http://localhost:8080/department.ctl, 查询所有的系
    //    POST, http://localhost:8080/department.ctl, 增加系
    //    PUT, http://localhost:8080/department.ctl, 修改系
    //    DELETE, http://localhost:8080/department.ctl?id=1, 删除id=1的系
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //请使用以下JSON测试增加功能
    //{"description":"id为null新系","no":"0201","remarks":"","school":{"description":"管理工程","id":1,"no":"02","remarks":"最好的学院"}}

    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的系","id":1,"no":"0201","remarks":"","school":{"description":"管理工程","id":1,"no":"02","remarks":"最好的学院"}}
    /**
     * POST,http://localhost:8080/department.ctl
     * 增加一个系对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //前台没有为id赋值，此处模拟自动生成id。Dao能实现数据库操作时，应删除此语句。
        departmentToAdd.setId(4 + (int)(Math.random()*100));
        //响应
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加Department对象
            DepartmentService.getInstance().add(departmentToAdd);
            //加入数据信息
            message.put("message", "增加成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * DELETE, http://localhost:8080/department.ctl?id=1
     * 删除一个系对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //到数据库表中删除对应的系
            DepartmentService.getInstance().delete(id);
            //加入数据信息
            message.put("message", "删除成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }


    /**
     * PUT, http://localhost:8080/department.ctl
     * 修改一个系对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加Department对象
            DepartmentService.getInstance().update(departmentToAdd);
            //加入数据信息
            message.put("message", "更新成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    /**
     * GET, http://localhost:8080/department.ctl?id=1, 查询id=1的系
     * GET, http://localhost:8080/department.ctl, 查询所有的系
     * 响应一个或所有系对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        String school_str = request.getParameter("paraType");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有系对象，否则响应id指定的系对象
            if (id_str == null&&school_str==null) {
                responseDepartments(response);
            }else {
                int id = Integer.parseInt(id_str);
                if(school_str==null) {
                    responseDepartment(id, response);
                }else if(school_str.equals("school")){
                    responseDepSch(id, response);
                }
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    //响应一个系对象
    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找系
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        //响应message到前端
        response.getWriter().println(department_json);
    }
    //响应所有系对象
    private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有系
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        response.getWriter().println(departments_json);
    }
    //响应school_id的所有系对象
    private void responseDepSch(int school_id,HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有系
        Collection<Department> departments = DepartmentService.getInstance().findAllBySchool(school_id);
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        response.getWriter().println(departments_json);
    }
}
