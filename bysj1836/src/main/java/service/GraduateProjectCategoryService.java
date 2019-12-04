package service;


import dao.GraduateProjectCategoryDao;
import domain.GraduateProjectCategory;

import java.util.Collection;

public final class GraduateProjectCategoryService {
	private GraduateProjectCategoryDao graduateProjectCategoryDao = GraduateProjectCategoryDao.getInstance();
	//本类的一个对象引用，保存自身对象
	private static GraduateProjectCategoryService graduateProjectCategoryService =  new GraduateProjectCategoryService();
	//私有的构造方法，防止其它类创建它的对象
	private GraduateProjectCategoryService(){}
	//静态方法，返回本类的惟一对象
	public synchronized static GraduateProjectCategoryService getInstance() {
		return graduateProjectCategoryService;
	}

	public Collection<GraduateProjectCategory> findAll() {
		return graduateProjectCategoryDao.findAll();
	}

	public GraduateProjectCategory find(Integer id) {
		return graduateProjectCategoryDao.find(id);
	}

	public boolean update(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategoryDao.update(graduateProjectCategory);
	}

	public boolean add(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategoryDao.add(graduateProjectCategory);
	}

	public boolean delete(Integer id) {
		GraduateProjectCategory graduateProjectCategory = this.find(id);
		return this.delete(graduateProjectCategory);
	}

	public boolean delete(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategoryDao.delete(graduateProjectCategory);
	}
}
