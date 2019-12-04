package service;


import dao.GraduateProjectTypeDao;
import domain.GraduateProjectType;

import java.util.Collection;

public final class GraduateProjectTypeService {
	private static GraduateProjectTypeService graduateProjectTypeService = new GraduateProjectTypeService();
	private GraduateProjectTypeDao graduateProjectTypeDao = GraduateProjectTypeDao.getInstance();
	
	private GraduateProjectTypeService(){}
	
	public static GraduateProjectTypeService getInstance(){
		return graduateProjectTypeService;
	}

	public Collection<GraduateProjectType> findAll(){
		return graduateProjectTypeDao.finaAll();
	}

	public GraduateProjectType find(Integer id){
		return graduateProjectTypeDao.find(id);
	}
	
	public boolean update(GraduateProjectType graduateProjectType){
		return graduateProjectTypeDao.update(graduateProjectType);
	}
	
	public boolean add(GraduateProjectType graduateProjectType){
		return graduateProjectTypeDao.add(graduateProjectType);
	}

	public boolean delete(Integer id){
		GraduateProjectType graduateProjectType = this.find(id);
		return this.delete(graduateProjectType);
	}
	
	public boolean delete(GraduateProjectType graduateProjectType){
		return graduateProjectTypeDao.delete(graduateProjectType);
	}
}
