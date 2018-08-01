package protocol;


import dto.CursaDTO;
import models.*;
import services.IObserver;
import services.IServer;
import services.ValidationExc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServerRpcProxy implements IServer {
    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;


    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public ServerRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    public void login(User user, IObserver client) throws ValidationExc {
        initializeConnection();

        Request req=new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new ValidationExc(err);
        }
    }

    public  void adaugaRezervare(Rezervare rez) throws ValidationExc{
        Request req=new Request.Builder().type(RequestType.ADD_REZERVARE).data(rez).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ValidationExc(err);
        }
        client.UpdateCurse((List<CursaDestinatie>)response.data());



    }



    public void logout(User user, IObserver client) throws ValidationExc {

        Request req=new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ValidationExc(err);
        }
    }





    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws ValidationExc {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ValidationExc("Error sending object "+e);
        }

    }

    private Response readResponse() throws ValidationExc {
        Response response=null;
        try{
            /*synchronized (responses){
                responses.wait();
            }
            response = responses.remove(0);    */
            //if(qresponses.size()!=0)
           // {
                response=qresponses.take();//}
           // else{response=null;}



        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws ValidationExc {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response){

        if (response.type()== ResponseType.UPDATED){
            List<CursaDestinatie> list=new ArrayList<>();

                  list=  (List<CursaDestinatie>)response.data();


            try {
                for(CursaDestinatie cd:list){
                System.out.println(cd.getCursa().toString());}

                client.UpdateCurse(list);

            } catch (ValidationExc e) {
                e.printStackTrace();
            }
        }

       /* if(response.type()==ResponseType.UPDATEL){
           Cursa cursa=(Cursa) response.data();
            try{
                client.UpdateLocuri(cursa);
            }catch (ValidationExc e) {
                e.printStackTrace();
            }
        }*/

    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.UPDATED ;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }


    public List<CursaDestinatie> getCurse(){
        List<CursaDestinatie> list=new ArrayList<>();
        Request req=new Request.Builder().type(RequestType.GET_CURSE).data(list).build();
        sendRequest(req);
        Response response=readResponse();
       // closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ValidationExc(err);
        }


        return (List<CursaDestinatie>) response.data();}
    //public Destinatie getDestinatie(String loc, String jud, String tara){return null;}

    public Cursa getCursa(String loc, String jud, String tar, Date data, Time ora){
        CursaDTO cursaDTO= new CursaDTO(loc,jud,tar,data,ora);
       Request req=new Request.Builder().type(RequestType.GET_CURSA).data(cursaDTO).build();
       sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            //closeConnection();
            throw new ValidationExc(err);

        }
        return (Cursa)response.data();

    }
   public  List<Loc> getLocuri(Cursa cursa){

       Request req=new Request.Builder().type(RequestType.GET_LOCURI).data(cursa).build();
       sendRequest(req);
       Response response=readResponse();
       if (response.type()== ResponseType.ERROR){
           String err=response.data().toString();
           //closeConnection();
           throw new ValidationExc(err);

       }
       return (List<Loc>)response.data();


   }
   public  User getUser(String username){
       initializeConnection();

       Request req=new Request.Builder().type(RequestType.GET_USER).data(username).build();
       sendRequest(req);


       Response response=readResponse();
       if (response.type()== ResponseType.ERROR){
           String err=response.data().toString();
           //closeConnection();
           throw new ValidationExc(err);

       }
      // closeConnection();
       return (User) response.data();
   }

}
