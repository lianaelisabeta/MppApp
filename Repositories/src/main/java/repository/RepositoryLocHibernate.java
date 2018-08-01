package repository;

import models.Loc;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RepositoryLocHibernate implements IRepository<Integer, Loc> {
    private SessionFactory factory;
    public RepositoryLocHibernate(SessionFactory f){
        factory=f;
    }

    @Override
    public void save(Loc entity) {

        try(Session session = factory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();

                session.save(entity);
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }


    }

    @Override
    public List<Loc> getAll() {
        List<Loc> locuri= new ArrayList<>();
        try(Session session = factory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                List<Loc> locList =
                        session.createQuery("from Loc").list();
                    locuri=locList;

                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        return locuri;
    }

    @Override
    public Loc findOne(Integer integer) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Loc update(Loc entity) {
        return null;
    }

    @Override
    public Loc delete(Integer integer) {
        return null;
    }
}
