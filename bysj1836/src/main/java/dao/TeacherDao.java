package dao;
import domain.*;
import service.DegreeService;
import service.DepartmentService;
import service.ProfTitleService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	private static Collection<Teacher> teachers=new TreeSet<Teacher>();;
	public Collection<Teacher> findAll() throws SQLException {
		teachers = new HashSet<Teacher>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT * FROM teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			teachers.add(new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					ProfTitleService.getInstance().find(resultSet.getInt("title_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id"))
			));
		}
		//使用JdbcHelper关闭Connection对象
		JdbcHelper.close(stmt,connection);
		//返回degrees
		return teachers;
	}
	public Teacher find(Integer id)throws SQLException{
		Teacher teacher = null;
		Connection connection = JdbcHelper.getConn();
		String findTeacher_sql = "SELECT * FROM teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					ProfTitleService.getInstance().find(resultSet.getInt("title_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id")));
		}
        System.out.println("teacher find"+teacher);
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}
	public boolean update(Teacher teacher)throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateTeacher_sql = "UPDATE teacher SET no=?, name=?, title_id=?,degree_id=?,department_id=? WHERE id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateTeacher_sql);
		preparedStatement.setString(1,teacher.getNo());
		preparedStatement.setString(2,teacher.getName());
		preparedStatement.setInt(3,teacher.getTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		preparedStatement.setInt(6,teacher.getId());
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("更新"+affectedRows+"条记录");
		preparedStatement.close();
		connection.close();
		return affectedRows>0;
	}
    public boolean add(Teacher teacher) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = null;
        int affectedRowNum = 0;
        int teacher_id = 0;
        try {
            //关闭连接的自动提交，事务开始
            connection.setAutoCommit(false);
            String addTeacher_sql = "INSERT INTO teacher "+
                    "(no,name,title_id,degree_id,department_id) VALUES"
                    + " (?,?,?,?,?)";
            //在该连接上创建预编译语句对象
            preparedStatement = connection.prepareStatement(addTeacher_sql);
            //为预编译参数赋值
            preparedStatement.setString(1, teacher.getNo());
            preparedStatement.setString(2, teacher.getName());
            preparedStatement.setInt(3, teacher.getTitle().getId());
            preparedStatement.setInt(4, teacher.getDegree().getId());
            preparedStatement.setInt(5, teacher.getDepartment().getId());
            //执行预编译语句，获取添加记录行数并赋值给affectedRowNum
            affectedRowNum = preparedStatement.executeUpdate();
            String selectTeacherByNo_sql = "SELECT * FROM teacher WHERE no=?";
            //在该连接上创建预编译语句对象
            preparedStatement = connection.prepareStatement(selectTeacherByNo_sql);
            //为预编译参数赋值
            preparedStatement.setString(1,teacher.getNo());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                teacher_id = resultSet.getInt("id");
            }
            String addUser_sql = "INSERT INTO User (username,password,teacher_id) VALUES" + " (?,?,?)";
            //在该连接上创建预编译语句对象
            preparedStatement = connection.prepareStatement(addUser_sql);
            //为预编译参数赋值
            preparedStatement.setString(1, teacher.getNo());
            preparedStatement.setString(2, teacher.getNo());
            preparedStatement.setInt(3, teacher_id);
            preparedStatement.executeUpdate();
            //手动提交申请，事务结束
            connection.commit();
        } catch (SQLException e) {
            //若发生异常输出出错信息和错误码
            System.out.println(e.getMessage() + "\nErrorCode:" + e.getErrorCode());
            try {
                //如果连接不为空，则回滚到insert之前的状态
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException d) {
                d.printStackTrace();
            }
        } finally {//最终执行
            try {
                //如果连接不为空，重新开启自动提交
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException f) {
                f.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(preparedStatement, connection);
            return affectedRowNum > 0;
        }
    }
	public boolean delete(Integer id)throws SQLException{
	    //获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		PreparedStatement pstmt = null;
		int affectedRows = 0;
		try{
			connection.setAutoCommit(false);
			String deleteUser_sql = "DELETE FROM user" + " WHERE teacher_id=?";
            //在该连接上创建预编译语句对象
            pstmt = connection.prepareStatement(deleteUser_sql);
            pstmt.setInt(1,id);
            affectedRows = pstmt.executeUpdate();
			//创建sql语句
			String deleteTeacher_sql = "DELETE FROM teacher" + " WHERE id=?";
			//在该连接上创建预编译语句对象
			pstmt = connection.prepareStatement(deleteTeacher_sql);
			//为预编译语句赋值
			pstmt.setInt(1,id);
			affectedRows = pstmt.executeUpdate();
			//创建sql语句

			//提交当前连接所做的操作
			connection.commit();
		}catch (SQLException e) {
			System.out.println(e.getMessage() + "\nerrorCode" + e.getErrorCode());
			try {
				//回滚当前连接所做的操作
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				//恢复自动提交
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(pstmt, connection);
			return affectedRows>0;
		}
	}
	
	public boolean delete(Teacher teacher)throws SQLException{
		return delete(teacher.getId());
	}
	//方便测试的mian方法
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		ProfTitle profTitle = ProfTitleDao.getInstance().find(3);
//		System.out.println(profTitle);
//		Degree phd = DegreeDao.getInstance().find(47);
//		System.out.println(phd);
//		Department misDept = DepartmentService.getInstance().find(2);
//		System.out.println(misDept);
//		Teacher teacher = new Teacher(1,"苏同",profTitle,phd,misDept);
//		TeacherDao.getInstance().add(teacher);
//		System.out.println("have finished");
		Teacher teacher = TeacherDao.getInstance().find(1);
		System.out.println(teacher);
		teacher.setName("赵彤");
		TeacherDao.getInstance().update(teacher);
		Teacher teacher1 = TeacherDao.getInstance().find(1);
		System.out.println(teacher1);
	}
}
