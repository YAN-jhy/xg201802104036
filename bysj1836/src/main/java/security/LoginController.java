package security;

import com.alibaba.fastjson.JSONObject;
import domain.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //创建Json对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
            User loggerUser = UserService.getInstance().login(username,password);
            if(loggerUser!=null){
                message.put("message","登陆成功");
                //与当前请求关联的Session对象
                HttpSession session = request.getSession();
                //10分钟没有操作，则使session失效
                session.setMaxInactiveInterval(10*60);
                //将user对象写入Session的属性，名称为currentUser，以便在其它请求中使用
                session.setAttribute("currentUser",loggerUser);
                //响应message到前端
                response.getWriter().println(message);
                //此处应重定向到索引页(菜单页)
                return;
            }else{
                message.put("message","用户名或密码错误");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }
}
