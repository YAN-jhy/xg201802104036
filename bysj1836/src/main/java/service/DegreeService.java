package service;


import dao.DegreeDao;
import domain.Degree;

import java.sql.SQLException;
import java.util.Collection;

public final class DegreeService {
    private static DegreeDao degreeDao
            = DegreeDao.getInstance();
    private static DegreeService degreeService
            =new DegreeService();
    private DegreeService(){}

    public static DegreeService getInstance(){
        return degreeService;
    }

    public Collection<Degree> findAll() throws SQLException{
        return degreeDao.findAll();
    }

    public Degree find(Integer id)throws SQLException{
        return degreeDao.find(id);
    }

    public boolean update(Degree degree) throws SQLException{
        return degreeDao.update(degree);
    }

    public void add(Degree degree) throws SQLException {
        degreeDao.add(degree);
    }

    public boolean delete(Integer id) throws SQLException{
        Degree degree = this.find(id);
        return degreeDao.delete(degree);
    }

    public boolean delete(Degree degree) throws SQLException{
        return degreeDao.delete(degree);
    }
}

