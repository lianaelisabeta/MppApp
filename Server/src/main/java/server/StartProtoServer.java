package server;

import models.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.*;
import services.IServer;
import services.ValidationExc;
import utils.AbstractServer;
import utils.ProtoConcurrentServer;
import utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartProtoServer {
    private static int defaultPort=55555;
    private static SessionFactory factory;
    public static void main(String[] args) {


        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/firmatrserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find firmatrserver.properties "+e);
            return;
        }

        final StandardServiceRegistry registry = new
                StandardServiceRegistryBuilder().configure().build();
        try {
            factory = new
                    MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }


        IRepository<String,User> repoU= new RepositoryUser(serverProps);
        IRepository<Integer, Cursa> repoC= new RepositoryCursa(serverProps);
        IRepository<Integer, Destinatie> repoD=new RepositoryDestinatieHibernate(factory);// new RepositoryDestinatie(serverProps);
        IRepository<Integer, Loc> repoL= new RepositoryLocHibernate(factory);//new RepositoryLoc(serverProps);
        IRepository<Integer, Rezervare> repoR= new RepositoryRezervare(serverProps);

        IServer serverimpl= new ServerImpl(repoU,repoC,repoD,repoL,repoR);


        int serverPort=defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("FirmaTransport.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new ProtoConcurrentServer(serverPort,serverimpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }



    }
}