package dao;

import domain.User;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	
	private static Collection<User> users;
//	static{
//		TeacherDao teacherDao = TeacherDao.getInstance();
//		users = new TreeSet<User>();
//		User user = new User(1,"st","st",new Date(),teacherDao.find(1));
//		users.add(user);
//		users.add(new User(2,"lx","lx",new Date(),teacherDao.find(2)));
//		users.add(new User(3,"wx","wx",new Date(),teacherDao.find(3)));
//		users.add(new User(4,"lf","lf",new Date(),teacherDao.find(4)));
//	}
	public Collection<User> findAll() throws SQLException{
        Collection users = new TreeSet<User>();
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from user");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            users.add(new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherDao.getInstance().find(resultSet.getInt("teacher_id"))));
        }
        //使用JdbcHelper关闭Connection对象
        JdbcHelper.close(stmt,connection);
        //返回degrees
        return users;
	}
	
	public User find(Integer id) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String findUser_sql = "SELECT * FROM user WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findUser_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherDao.getInstance().find(resultSet.getInt("teacher_id")));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
	}
    public User findByUsername(String username) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String findUser_sql = "SELECT * FROM user WHERE username=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findUser_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherDao.getInstance().find(resultSet.getInt("teacher_id")));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
    public User login(String username,String password) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String findUser_sql = "SELECT * FROM user WHERE username=? and password=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findUser_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherDao.getInstance().find(resultSet.getInt("teacher_id")));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
	public boolean update(User user)throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String updateUser_sql = "update user set password=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateUser_sql);
        preparedStatement.setString(1,user.getPassword());
        preparedStatement.setInt(2,user.getId());
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("更新"+affectedRows+"条记录");
        preparedStatement.close();
        connection.close();
        return affectedRows>0;
	}
    public boolean add(User user){
        return users.add(user);
    }
    public boolean delete(Integer id) throws SQLException{
        User user = this.find(id);
        return this.delete(user);
    }
    public boolean delete(User user){
        return users.remove(user);
    }
	public static void main(String[] args) throws SQLException{
		UserDao dao = new UserDao();
		Collection<User> users = dao.findAll();
		display(users);
	}
	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}
}
