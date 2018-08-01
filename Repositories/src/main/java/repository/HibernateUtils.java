package repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.Serializable;

public class HibernateUtils implements Serializable {

     private static SessionFactory sessionFactory;
    public HibernateUtils(){}
    public SessionFactory getSessionFactory() {
        initialize();
        return sessionFactory;
    }
    static void initialize() {
// A SessionFactory is set up once for an application!
// configures settings from hibernate.cfg.xml
        final StandardServiceRegistry registry = new
                StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new
                    MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }
    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }}


