package service;


import dao.GraduateProjectDao;
import domain.GraduateProject;
import domain.GraduateProjectStatus;
import domain.Teacher;

import java.util.Collection;
import java.util.HashSet;

public final class GraduateProjectService {
	private GraduateProjectDao graduateProjectDao= GraduateProjectDao.getInstance();
	private static GraduateProjectService graduateProjectService = new GraduateProjectService();
	
	//Singleton
	private GraduateProjectService(){}

	public static GraduateProjectService getInstance(){
		return graduateProjectService;
	}

	//获取所有课题
	public Collection<GraduateProject> findAll(){
		return graduateProjectDao.findAll();
	}
	//获取所有课题(参数指定的)
	public Collection<GraduateProject> findAll(Teacher teacher){
		Collection<GraduateProject> projectsForTeacher = new HashSet<GraduateProject>();

		for (GraduateProject graduateProject : graduateProjectDao.findAll()) {
			if (teacher.equals(graduateProject.getTeacher())) {
				projectsForTeacher.add(graduateProject);
			}
		}
		return projectsForTeacher;
	}
	//获取所有课题(除参数外）
	public Collection<GraduateProject> findAllWithout(Teacher teacher) {
		Collection<GraduateProject> projectsForTeacher = new HashSet<GraduateProject>();

		for (GraduateProject graduateProject : graduateProjectDao.findAll()) {
			if (!teacher.equals(graduateProject.getTeacher())) {
				projectsForTeacher.add(graduateProject);
			}
		}
		return projectsForTeacher;
	}
	public Collection<GraduateProject> findAll(GraduateProjectStatus status){
		Collection<GraduateProject> graduateProjects = new HashSet<GraduateProject>();
		for(GraduateProject graduateProject: graduateProjectDao.findAll()){
			if(graduateProject.getGraduateProjectStatus()==status){
				graduateProjects.add(graduateProject);
			}
		}
		return graduateProjects;
	}


	//增加一个课题
	public void add(GraduateProject project){
		graduateProjectDao.addGraduateProject(project);
	}
	//更新一个课题
	public void updateGraduateProject(GraduateProject project){
		graduateProjectDao.update(project);
	}
	//更新一个课题
	public void update(GraduateProject project){
		graduateProjectDao.update(project);
	}

	//获得id对应的课题
	public GraduateProject find(Integer id) {
		return graduateProjectDao.find(id);
	}
	//删除一个课题
	public void delete( int id){
		graduateProjectDao.delete(id);
	}
	
}
