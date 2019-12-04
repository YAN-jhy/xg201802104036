package dao;


import domain.Department;
import domain.Major;

import java.util.Collection;
import java.util.TreeSet;

public final class MajorDao {
	private static MajorDao majorDao = new MajorDao();
	private MajorDao(){}
	public static MajorDao getInstance(){
		return majorDao;
	}
	private static Collection<Major> majors;
	static{
//		DepartmentDao deptDao = DepartmentDao.getInstance();
//		Department pmDepartment = deptDao.find(1);
//		Department misDepartment = deptDao.find(2);
//		Department pcDepartment = deptDao.find(3);
//		Department ieDepartment = deptDao.find(4);
//
//		majors = new TreeSet<Major>();
//		Major major = new Major(1,"信息管理与信息系统","020201","",misDepartment);
//		majors.add(major);
//		majors.add(new Major(2,"工业工程","020401","",ieDepartment));
//		majors.add(new Major(3,"工程管理","020101","",pmDepartment));
//		majors.add(new Major(4,"工程造價","020301","",pcDepartment));
//		majors.add(new Major(5,"信息系统项目管理","020202","",misDepartment));
	}
	public Collection<Major> findAll(){
		return MajorDao.majors;
	}
	
	public Major find(Integer id){
		Major desiredMajor = null;
		for (Major major : majors) {
			if(id.equals(major.getId())){
				desiredMajor =  major; 
			}
		}
		return desiredMajor;
	}
	
	public boolean update(Major major){
		majors.remove(major);
		return majors.add(major);		
	}
	
	public boolean add(Major major){
		return majors.add(major);		
	}

	public boolean delete(Integer id){
		Major major = this.find(id);
		return this.delete(major);
	}
	
	public boolean delete(Major major){
		return majors.remove(major);
	}
	
	
	public static void main(String[] args){
		MajorDao dao = new MajorDao();
		Collection<Major> majors = dao.findAll();
		display(majors);
//		Major major = new Major(2,"副教�?","02","");
//		major.setNo("02x");
//		dao.update(major);
//		display(majors);
//	
//		dao.delete(major);
//		dao.delete(3);
//		display(majors);
//		dao.add(new Major(2,"工程�?","04",""));
//		display(majors);
//		System.out.println("2="+dao.find(2));
	}

	private static void display(Collection<Major> majors) {
		for (Major major : majors) {
			System.out.println(major);
		}
	}	
}
