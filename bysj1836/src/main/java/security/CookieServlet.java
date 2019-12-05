package security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/showCookies")
public class CookieServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //得到所偶的cookie信息
        Cookie[] allCookies = request.getCookies();
        if(allCookies==null){
            response.getWriter().println("no cookies available");
        }else{
            for(Cookie cookie : allCookies){
                response.getWriter().println(cookie.getName()+" = "+cookie.getValue());
            }
        }
    }
}
