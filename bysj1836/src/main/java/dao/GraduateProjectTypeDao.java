package dao;


import domain.GraduateProjectType;

import java.util.Collection;
import java.util.TreeSet;

public final class GraduateProjectTypeDao {
	private static GraduateProjectTypeDao graduateProjectTypeDao = new GraduateProjectTypeDao();
	
	private GraduateProjectTypeDao() {}
	public static GraduateProjectTypeDao getInstance(){
		return graduateProjectTypeDao;
	}
	
	private static Collection<GraduateProjectType> graduateProjectTypes;
	static{
		graduateProjectTypes = new TreeSet<GraduateProjectType>();
		GraduateProjectType design = new GraduateProjectType(1,"应用研究","01","");
		graduateProjectTypes.add(design);
		graduateProjectTypes.add(new GraduateProjectType(2,"理论研究","02",""));
		graduateProjectTypes.add(new GraduateProjectType(3,"软件开发","03",""));
		graduateProjectTypes.add(new GraduateProjectType(4,"社会调查","04",""));
		graduateProjectTypes.add(new GraduateProjectType(5,"XXX会调查","04",""));

	}
	public Collection<GraduateProjectType> finaAll(){
		return GraduateProjectTypeDao.graduateProjectTypes;
	}
	
	public GraduateProjectType find(Integer id){
		GraduateProjectType desiredGraduateProjectType = null;
		for (GraduateProjectType graduateProjectType : graduateProjectTypes) {
			if(id.equals(graduateProjectType.getId())){
				desiredGraduateProjectType = graduateProjectType;
			}
		}
		return desiredGraduateProjectType;
	}
	
	public boolean update(GraduateProjectType graduateProjectType){
		graduateProjectTypes.remove(graduateProjectType);
		return graduateProjectTypes.add(graduateProjectType);
	}
	
	public boolean add(GraduateProjectType graduateProjectType){
		return graduateProjectTypes.add(graduateProjectType);
	}

	public boolean delete(Integer id){
		GraduateProjectType graduateProjectType = this.find(id);
		return this.delete(graduateProjectType);
	}
	
	public boolean delete(GraduateProjectType graduateProjectType){
		return graduateProjectTypes.remove(graduateProjectType);
	}
	
	public static void main(String[] args){
		GraduateProjectTypeDao dao = new GraduateProjectTypeDao();
		Collection<GraduateProjectType> graduateProjectTypes = dao.finaAll();
		display(graduateProjectTypes);
	}

	private static void display(Collection<GraduateProjectType> graduateProjectTypes) {
		for (GraduateProjectType graduateProjectType : graduateProjectTypes) {
			System.out.println(graduateProjectType);
		}
	}
}
