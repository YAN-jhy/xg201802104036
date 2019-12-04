package dao;


import domain.GraduateProjectStatus;

import java.util.Collection;
import java.util.TreeSet;

public final class GraduateProjectStatusDao {
	private static GraduateProjectStatusDao graduateProjectStatusDao = new GraduateProjectStatusDao();
	private static Collection<GraduateProjectStatus> graduateProjectStatuses;
	static{
		graduateProjectStatuses = new TreeSet<GraduateProjectStatus>();
		GraduateProjectStatus graduateProjectStatus = new GraduateProjectStatus(1,"申请","01","");
		graduateProjectStatuses.add(graduateProjectStatus);
		graduateProjectStatuses.add(new GraduateProjectStatus(2,"通过","02",""));
		graduateProjectStatuses.add(new GraduateProjectStatus(3,"退修","03",""));
	}

	private GraduateProjectStatusDao(){}

	public static GraduateProjectStatusDao getInstance(){
		return graduateProjectStatusDao;
	}

	public Collection<GraduateProjectStatus> findAll(){
		return GraduateProjectStatusDao.graduateProjectStatuses;
	}

	public GraduateProjectStatus find(Integer id){
		GraduateProjectStatus desiredGraduateProjectStatus = null;
		for (GraduateProjectStatus graduateProjectStatus : graduateProjectStatuses) {
			if(id.equals(graduateProjectStatus.getId())){
				desiredGraduateProjectStatus =  graduateProjectStatus;
			}
		}
		return desiredGraduateProjectStatus;
	}

	public boolean update(GraduateProjectStatus graduateProjectStatus){
		graduateProjectStatuses.remove(graduateProjectStatus);
		return graduateProjectStatuses.add(graduateProjectStatus);
	}

	public boolean add(GraduateProjectStatus graduateProjectStatus){
		return graduateProjectStatuses.add(graduateProjectStatus);
	}

	public boolean delete(Integer id){
		GraduateProjectStatus graduateProjectStatus =this.find(id);
		return this.delete(graduateProjectStatus);
	}

	public boolean delete(GraduateProjectStatus graduateProjectStatus){
		return GraduateProjectStatusDao.graduateProjectStatuses.remove(graduateProjectStatus);
	}
}
