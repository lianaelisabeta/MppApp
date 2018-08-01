package repository;

import models.Destinatie;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import sun.security.krb5.internal.crypto.Des;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RepositoryDestinatieHibernate implements IRepository<Integer, Destinatie> {

    private SessionFactory factory;
    private HibernateUtils hb=new HibernateUtils();
    public RepositoryDestinatieHibernate(SessionFactory f){
        factory=f;//hb.getSessionFactory();
    }
    @Override
    public List<Destinatie> getAll ( ){
        List<Destinatie> dests=new ArrayList<>();

        try(Session session = factory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                List<Destinatie> dest =
                        session.createQuery(" FROM Destinatie").list();
                dests=dest;


                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        return dests;
    }
    @Override
    public Destinatie delete(Integer integer) {
        return null;
    }

    @Override
    public Destinatie update(Destinatie entity) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Destinatie findOne(Integer integer) {


        try(Session session = factory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                Destinatie dest =(Destinatie) session.createQuery("FROM Destinatie des where des.Id= ? ")
                        .setInteger(0,integer).setMaxResults(1).list().get(0);
                tx.commit();
                return dest;



            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        return null;
    }



    @Override
    public void save(Destinatie entity) {

    }
}
