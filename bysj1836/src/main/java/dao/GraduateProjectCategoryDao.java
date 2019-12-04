package dao;


import domain.GraduateProjectCategory;

import java.util.Collection;
import java.util.TreeSet;

public final class GraduateProjectCategoryDao {
	//本类的一个对象引用，保存自身对象
	private static GraduateProjectCategoryDao graduateProjectCategoryDao = null;
	//私有的构造方法，防止其它类创建它的对象
	private GraduateProjectCategoryDao(){}
	//静态方法，返回本类的惟一对象
	public synchronized static GraduateProjectCategoryDao getInstance() {
		return graduateProjectCategoryDao == null ? new GraduateProjectCategoryDao()
				: graduateProjectCategoryDao;
	}

	private static Collection<GraduateProjectCategory> graduateProjectCategories;
	static {
		graduateProjectCategories = new TreeSet<GraduateProjectCategory>();
		GraduateProjectCategory design = new GraduateProjectCategory(1, "设计", "01", "");
		graduateProjectCategories.add(design);
		graduateProjectCategories.add(new GraduateProjectCategory(2, "论文", "02", ""));
	}
	
	public Collection<GraduateProjectCategory> findAll() {
		return GraduateProjectCategoryDao.graduateProjectCategories;
	}

	public GraduateProjectCategory find(Integer id) {
		for (GraduateProjectCategory graduateProjectCategory : graduateProjectCategories) {
			if (id.equals(graduateProjectCategory.getId())) {
				return graduateProjectCategory;
			}
		}

		return null;
	}

	public boolean update(GraduateProjectCategory graduateProjectCategory) {
		graduateProjectCategories.remove(graduateProjectCategory);
		return graduateProjectCategories.add(graduateProjectCategory);
	}

	public boolean add(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategories.add(graduateProjectCategory);
	}

	public boolean delete(Integer id) {
		GraduateProjectCategory graduateProjectCategory = this.find(id);
		return this.delete(graduateProjectCategory);
	}

	public boolean delete(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategories.remove(graduateProjectCategory);
	}

	public static void main(String[] args) {
		GraduateProjectCategoryDao dao = new GraduateProjectCategoryDao();
		Collection<GraduateProjectCategory> graduateProjectCategories = dao
				.findAll();
	}
}
