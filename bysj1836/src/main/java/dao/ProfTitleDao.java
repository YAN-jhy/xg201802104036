package dao;
import domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	private static Collection<ProfTitle> profTitles = new TreeSet<ProfTitle>();;

	public Collection<ProfTitle> findAll() throws SQLException {

		profTitles = new HashSet<ProfTitle>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from profTitle");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			profTitles.add(new ProfTitle(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		//使用JdbcHelper关闭Connection对象
		JdbcHelper.close(stmt,connection);
		//返回degrees
		return ProfTitleDao.profTitles;
	}

	public ProfTitle find(Integer id)throws SQLException{
		ProfTitle profTitle = null;
		Connection connection = JdbcHelper.getConn();
		String findDegree_sql = "SELECT * FROM proftitle WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findDegree_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			profTitle = new ProfTitle(resultSet.getInt("id"),
					resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return profTitle;
	}

	public boolean update(ProfTitle profTitle)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String updateProfTitle_sql = "UPDATE proftitle set description=?,no=?,remarks=? WHERE id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(updateProfTitle_sql);
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		preparedStatement.setInt(4,profTitle.getId());
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("更新"+affectedRows+"条记录");
		preparedStatement.close();
		connection.close();
		return  affectedRows>0;
	}

	//增加学位的方法
	public boolean add(ProfTitle profTitle) throws SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备语句对象
		//SQL语句为多行时，注意语句不同部分之间有空格
		PreparedStatement pstmt = connection.prepareStatement("insert into profTitle" + "(no,description,remarks)" + " values (?,?,?)");
		//为预编译参数赋值
		pstmt.setString(1, profTitle.getNo());
		pstmt.setString(2, profTitle.getDescription());
		pstmt.setString(3, profTitle.getRemarks());
		//执行预编译对象的executeUpdate方法，获取添加的记录行数
		//执行预编译语句，用其返回值、影响的行数为赋值affectedRowNum
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加"+affectedRowNum+"条记录");
		//关闭pstmt, connection对象（关闭资源）
		JdbcHelper.close(pstmt, connection);
		//如果影响的行数大于1，则返回true，否则返回false
		return affectedRowNum > 0;
	}
//	public boolean delete(Integer id)throws SQLException{
//		ProfTitle profTitle = this.find(id);
//		return this.delete(profTitle);
//	}
	public boolean delete(Integer id) throws SQLException{
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String deleteProfTitle_sql = "DELETE FROM profTitle" + " WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteProfTitle_sql);
		//为预编译语句赋值
		pstmt.setInt(1,id);
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除"+affectedRowNum+"条记录");
		//关闭pstmt, connection对象（关闭资源）
		JdbcHelper.close(pstmt,connection);
		//如果影响的行数大于1，则返回true，否则返回false
		return affectedRowNum > 0;
	}
	public boolean delete(ProfTitle profTitle)throws SQLException{
		return delete(profTitle.getId());
	}
	//方便测试的mian方法
	public static void main(String[] args) throws ClassNotFoundException,SQLException{
		ProfTitle profTitle = ProfTitleDao.getInstance().find(3);
		System.out.println(profTitle);
		profTitle.setDescription("副教授");
		profTitleDao.getInstance().update(profTitle);
		ProfTitle profTitle1 = ProfTitleDao.getInstance().find(3);
		System.out.println(profTitle1);
//		ProfTitle profTitleToAdd = new ProfTitle(1,"副教授","03","");
//		ProfTitleDao.getInstance().add(profTitleToAdd);
//		System.out.println(profTitleToAdd);
		System.out.println("have finished");
	}
}

