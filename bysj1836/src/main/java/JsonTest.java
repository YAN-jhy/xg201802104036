import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ajaxTest")
public class JsonTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().println("{\"sites\":" +
                "[{\"name\":\"runoob\",\"url\":\"www.runoob.com\"}," +
                "{\"name\":\"google\",\"url\":\"www.google.com\"}," +
                "{\"name\":\"weibo\",\"url\":\"www.weibo.com\"}]}\n");
        System.out.println("/ajaxTest");
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

}
