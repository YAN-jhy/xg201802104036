package dao;
import domain.Degree;
import util.JdbcHelper;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
public final class DegreeDao {
    private static DegreeDao degreeDao =
            new DegreeDao();

    private DegreeDao() {
    }

    public static DegreeDao getInstance() {
        return degreeDao;
    }

    private static Collection<Degree> degrees = new TreeSet<Degree>();
    public Collection<Degree> findAll() throws SQLException {

        degrees = new HashSet<Degree>();
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from degree");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
           degrees.add(new Degree(resultSet.getInt("id"),
                   resultSet.getString("description"),
                   resultSet.getString("no"),
                   resultSet.getString("remarks")));
        }
        //使用JdbcHelper关闭Connection对象
        JdbcHelper.close(stmt,connection);
        //返回degrees
        return DegreeDao.degrees;
    }
    public Degree find(Integer id) throws SQLException{
        Degree degree = null;
        Connection connection = JdbcHelper.getConn();
        String findDegree_sql = "SELECT * FROM degree WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findDegree_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            degree = new Degree(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return degree;
    }

    public boolean update(Degree degree) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateDegree_sql = " UPDATE degree SET description=?,no=?,remarks=? WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,degree.getDescription());
        preparedStatement.setString(2,degree.getNo());
        preparedStatement.setString(3,degree.getRemarks());
        preparedStatement.setInt(4,degree.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("更新"+affectedRows+"条记录");
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return  affectedRows>0;
    }
    //增加学位的方法
    public boolean add(Degree degree) throws SQLException{
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //根据连接对象准备语句对象
        //SQL语句为多行时，注意语句不同部分之间有空格
        PreparedStatement pstmt = connection.prepareStatement("insert into degree" + "(no,description,remarks)" + " values (?,?,?)");
        //为预编译参数赋值
        pstmt.setString(1, degree.getNo());
        pstmt.setString(2, degree.getDescription());
        pstmt.setString(3, degree.getRemarks());
        //执行预编译对象的executeUpdate方法，获取添加的记录行数
        //执行预编译语句，用其返回值、影响的行数为赋值affectedRowNum
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("添加"+affectedRowNum+"条记录");
        //关闭pstmt, connection对象（关闭资源）
        JdbcHelper.close(pstmt, connection);
        //如果影响的行数大于1，则返回true，否则返回false
        return affectedRowNum > 0;
    }

    //学位删除方法
    //Integer类是基本数据类型int的包装器类，是抽象类Number的子类，位于java.lang包中。
    public boolean delete(Integer id) throws SQLException{
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句
        String deleteDegree_sql = "DELETE FROM degree" + " WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
        //为预编译语句赋值
        pstmt.setInt(1,id);
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除"+affectedRowNum+"条记录");
        //关闭pstmt, connection对象（关闭资源）
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    public boolean delete(Degree degree)throws SQLException {
        return delete(degree.getId());
    }
    //方便测试的mian方法
    public static void main(String[] args) throws ClassNotFoundException,SQLException{
        Degree degree1 = DegreeDao.getInstance().find(47);
        System.out.println(degree1);
        degree1.setDescription("研究生");
        DegreeDao.getInstance().update(degree1);
        Degree degree2 = DegreeDao.getInstance().find(47);
        System.out.println(degree2.getDescription());
//        Degree degree = new Degree(5,"博士","05","");
//        System.out.println(DegreeDao.getInstance().add(degree));
    }
}

