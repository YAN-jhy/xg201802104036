package basic;

import domain.User;
import service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        String username_str = request.getParameter("username");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseUserByUsername(username_str,response);
            } else {
                int id = Integer.parseInt(id_str);
                responseUser(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }
    }
    //响应一个学院对象
    private void responseUser(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        User user = UserService.getInstance().getUser(id);
        String user_json = JSON.toJSONString(user);
        //响应message到前端
        response.getWriter().println(user_json);
    }
    //响应所有学院对象
    private void responseUserByUsername(String username,HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        User user = UserService.getInstance().getUserByUsername(username);
        String users_json = JSON.toJSONString(user);
        //响应message到前端
        response.getWriter().println(users_json);
    }
}
