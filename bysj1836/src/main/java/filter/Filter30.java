package filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter(filterName = "Filter30",urlPatterns = {"/*"})
public class Filter30 implements Filter {
    private Set<String> toExclude = new HashSet<String>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        toExclude.add("/login");
        toExclude.add("/");
        toExclude.add("/logout");
        toExclude.add("/showCookies");
        toExclude.add("/getSession");
        toExclude.add("/createSession");
    }
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //创建Json对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        HttpServletRequest request = (HttpServletRequest)req;
        //访问权限验证
        HttpSession session = request.getSession(false);
        String url = request.getRequestURI();
        if (!toExclude.contains(url)) {
            if(session==null||session.getAttribute("currentUser")==null){
            message.put("message","请登录或重新登录");
            //响应message到前端
            resp.getWriter().println(message);
            return;
            }
        }
        chain.doFilter(req, resp);
    }


}
