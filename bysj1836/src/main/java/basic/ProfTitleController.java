package basic;

import domain.ProfTitle;
import service.ProfTitleService;
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
@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
    //    GET, http://localhost:8080/profTitle.ctl?id=1, 查询id=1的职称
    //    GET, http://localhost:8080/profTitle.ctl, 查询所有的职称
    //    POST, http://localhost:8080/profTitle.ctl, 增加职称
    //    PUT, http://localhost:8080/profTitle.ctl, 修改职称
    //    DELETE, http://localhost:8080/profTitle.ctl?id=1, 删除id=1的职称
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //请使用以下JSON测试增加功能
    //{"description":"id为null新职称","no":"0201","remarks":""}

    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的职称","id":1,"no":"0201","remarks":""}
    /**
     * POST,http://localhost:8080/profTitle.ctl
     * 增加一个职称对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //前台没有为id赋值，此处模拟自动生成id。Dao能实现数据库操作时，应删除此语句。
        profTitleToAdd.setId(4 + (int)(Math.random()*100));
        //响应
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加ProfTitle对象
            ProfTitleService.getInstance().add(profTitleToAdd);
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
     * DELETE, http://localhost:8080/profTitle.ctl?id=1
     * 删除一个职称对象：根据来自前端请求的id，删除数据库表中id的对应记录
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
            //到数据库表中删除对应的职称
            ProfTitleService.getInstance().delete(id);
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
     * PUT, http://localhost:8080/profTitle.ctl
     * 修改一个职称对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加ProfTitle对象
            ProfTitleService.getInstance().update(profTitleToAdd);
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
     * GET, http://localhost:8080/profTitle.ctl?id=1, 查询id=1的职称
     * GET, http://localhost:8080/profTitle.ctl, 查询所有的职称
     * 响应一个或所有职称对象
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
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseProfTitles(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseProfTitle(id, response);
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
    //响应一个职称对象
    private void responseProfTitle(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找职称
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);
        //响应message到前端
        response.getWriter().println(profTitle_json);
    }
    //响应所有职称对象
    private void responseProfTitles(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有职称
        Collection<ProfTitle> profTitles =ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        response.getWriter().println(profTitles_json);
    }

}
