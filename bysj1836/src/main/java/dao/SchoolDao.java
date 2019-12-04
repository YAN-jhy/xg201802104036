package dao;
import domain.School;
import util.JdbcHelper;
import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;
public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools = new TreeSet<School>();
	private SchoolDao(){}
	public static SchoolDao getInstance(){
		return schoolDao;
	}
	public Collection<School> findAll()throws SQLException{
		schools = new TreeSet<School>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from school");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			schools.add(new School(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		//使用JdbcHelper关闭Connection对象
		JdbcHelper.close(stmt,connection);
		//返回degrees
		return schools;
	}
	public School addWithSP(School school)throws SQLException,ClassNotFoundException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备可调用语句对象，sp_addDepartment为存储过程名称，后面为5个参数
		CallableStatement callableStatement=connection.prepareCall("CALL sp_addSchool (?,?,?,?)");
		//得第5个参数设置为输出参数，类型为长整型（数据库的数据类型)
		callableStatement.registerOutParameter(4, Types.BIGINT);
		callableStatement.setString(1,school.getDescription());
		callableStatement.setString(2,school.getNo());
		callableStatement.setString(3,school.getRemarks());
		//执行可调用语句callableStatement
		callableStatement.execute();
		//获得第5个参数的值，数据库为该记录自动生成的id
		int id = callableStatement.getInt(4);
		//为参数department的id字段赋值
		school.setId(id);
		callableStatement.close();
		connection.close();
		return school;
	}
	public School find(Integer id)throws SQLException{
		School school = null;
		Connection connection = JdbcHelper.getConn();
		String findSchool_sql = "SELECT * FROM school WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findSchool_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			school = new School(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return school;
	}
	public boolean update(School school)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String updateSchool_sql = "update school set description=?,no = ?,remarks=? WHERE id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateSchool_sql);
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("更新"+affectedRows+"条记录");
		preparedStatement.close();
		connection.close();
		return  affectedRows>0;
	}
	public boolean add(School school)throws SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备语句对象
		//SQL语句为多行时，注意语句不同部分之间有空格
		PreparedStatement pstmt = connection.prepareStatement("insert into school" + "(no,description,remarks)" + " values (?,?,?)");
		//为预编译参数赋值
		pstmt.setString(1, school.getNo());
		pstmt.setString(2, school.getDescription());
		pstmt.setString(3, school.getRemarks());
		//执行预编译对象的executeUpdate方法，获取添加的记录行数
		//执行预编译语句，用其返回值、影响的行数为赋值affectedRowNum
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加"+affectedRowNum+"条记录");
		//关闭pstmt, connection对象（关闭资源）
		JdbcHelper.close(pstmt, connection);
		//如果影响的行数大于1，则返回true，否则返回false
		return affectedRowNum > 0;
	}
	public boolean delete(Integer id)throws SQLException{
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String deleteSchool_sql = "DELETE FROM school" + " WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteSchool_sql);
		//为预编译语句赋值
		pstmt.setInt(1,id);
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除"+affectedRowNum+"条记录");
		//关闭pstmt, connection对象（关闭资源）
		JdbcHelper.close(pstmt,connection);
		//如果影响的行数大于1，则返回true，否则返回false
		return affectedRowNum > 0;
	}
	public boolean delete(School school)throws SQLException{
		return delete(school.getId());
	}
	public static void main(String[] args) throws SQLException,ClassNotFoundException {
		School school = SchoolDao.getInstance().find(18);
		System.out.println(school);
//		School schoolToAdd = new School("管理工程","05","");
//		School addedSchool = schoolDao.addWithSP(schoolToAdd);
//		System.out.println("添加School成功");
	}
}
