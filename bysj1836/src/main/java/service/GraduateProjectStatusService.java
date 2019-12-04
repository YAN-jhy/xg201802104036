package service;


import dao.GraduateProjectStatusDao;
import domain.GraduateProject;
import domain.GraduateProjectStatus;

import java.util.Collection;

public final class GraduateProjectStatusService {
	private static GraduateProjectStatusDao graduateProjectStatusDao= GraduateProjectStatusDao.getInstance();
	private static GraduateProjectStatusService graduateProjectStatusService=new GraduateProjectStatusService();


	public static GraduateProjectStatusService getInstance(){
		return graduateProjectStatusService;
	}

	public Collection<GraduateProjectStatus> findAll(){
		return graduateProjectStatusDao.findAll();
	}

	public GraduateProjectStatus find(Integer id){
		return graduateProjectStatusDao.find(id);
	}

	public boolean update(GraduateProjectStatus graduateProjectStatus){
		return graduateProjectStatusDao.update(graduateProjectStatus);
	}

	public boolean add(GraduateProjectStatus graduateProjectStatus){
		return graduateProjectStatusDao.add(graduateProjectStatus);
	}

	public boolean delete(Integer id){
		GraduateProjectStatus graduateProjectStatus = this.find(id);
		return this.delete(graduateProjectStatus);
	}

	public boolean delete(GraduateProjectStatus graduateProjectStatus){
		//获得所有处于本状态的课题（GraduateProject）
		Collection<GraduateProject> graduateProjectSet = GraduateProjectService.getInstance().findAll(graduateProjectStatus);
		//若没有处于本状态的课题，则能够删除
		if(graduateProjectSet.size()==0){
			return graduateProjectStatusDao.delete(graduateProjectStatus);
		}else {
			return false;
		}
	}
}
