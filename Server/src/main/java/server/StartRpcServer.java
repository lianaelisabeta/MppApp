package server;



import models.*;
import repository.*;
import services.IServer;
import utils.AbstractServer;
import utils.RpcConcurrentServer;
import utils.ServerException;

import java.io.IOException;

import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
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
        IRepository<String,User> repoU= new RepositoryUser(serverProps);
        IRepository<Integer, Cursa> repoC= new RepositoryCursa(serverProps);
        IRepository<Integer, Destinatie> repoD= new RepositoryDestinatie(serverProps);
        IRepository<Integer, Loc> repoL= new RepositoryLoc(serverProps);
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
        AbstractServer server = new RpcConcurrentServer(serverPort, serverimpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
