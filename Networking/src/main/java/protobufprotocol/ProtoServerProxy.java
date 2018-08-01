package protobufprotocol;

import dto.CursaDTO;
import models.*;
import services.IObserver;
import services.IServer;
import services.ValidationExc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoServerProxy implements IServer {
    private String host;
    private int port;

    private IObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<FirmaTrProtobufs.Response> qresponses;
    private volatile boolean finished;
    public ProtoServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<FirmaTrProtobufs.Response>();
    }


    public void login(User user, IObserver client) throws ValidationExc {
        initializeConnection();
        sendRequest(ProtoUtils.createLoginRequest(user));
        FirmaTrProtobufs.Response response=readResponse();
        if (response.getType()== FirmaTrProtobufs.Response.Type.OK){
            this.client=client;
            return;
        }
        if (response.getType()== FirmaTrProtobufs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ValidationExc(errorText);
        }
    }

    public void logout(User user, IObserver client) throws ValidationExc {
        sendRequest(ProtoUtils.createLogoutRequest(user));
        FirmaTrProtobufs.Response response=readResponse();
        closeConnection();
        if (response.getType()== FirmaTrProtobufs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ValidationExc(errorText);
        }
    }
    public  void adaugaRezervare(Rezervare rez) throws ValidationExc{

        sendRequest(ProtoUtils.createAddRezervareRequest(rez));
        FirmaTrProtobufs.Response response=readResponse();
        if (response.getType()== FirmaTrProtobufs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ValidationExc(errorText);
        }
        client.UpdateCurse(ProtoUtils.getCurse(response));



    }

    public List<CursaDestinatie> getCurse(){
        List<CursaDestinatie> list=new ArrayList<>();

        sendRequest(ProtoUtils.createGetCurseRequest(list));
        FirmaTrProtobufs.Response response=readResponse();
        // closeConnection();
        if (response.getType()== FirmaTrProtobufs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ValidationExc(errorText);
        }


        return ProtoUtils.getCurse(response);}


    public Cursa getCursa(String loc, String jud, String tar, Date data, Time ora){
        CursaDTO cursaDTO= new CursaDTO(loc,jud,tar,data,ora);

        sendRequest(ProtoUtils.createGetCursaRequest(cursaDTO));
        System.out.println(cursaDTO.getDate().getMonth()+" "+cursaDTO.getDate().getYear());
        FirmaTrProtobufs.Response response=readResponse();
        if (response.getType()== FirmaTrProtobufs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ValidationExc(errorText);
        }
        return ProtoUtils.getCursa(response);

    }

    public  List<Loc> getLocuri(Cursa cursa){


        sendRequest(ProtoUtils.createGetLocuriRequest(cursa));
        FirmaTrProtobufs.Response response=readResponse();
        if (response.getType()== FirmaTrProtobufs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ValidationExc(errorText);
        }
        return ProtoUtils.getLocuri(response);


    }

    public  User getUser(String username){
        initializeConnection();


        sendRequest(ProtoUtils.createGetUserRequest(username));


        FirmaTrProtobufs.Response response=readResponse();
        if (response.getType()== FirmaTrProtobufs.Response.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ValidationExc(errorText);
        }
        // closeConnection();
        return ProtoUtils.getUser(response);
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

    private void sendRequest(FirmaTrProtobufs.Request request)throws ValidationExc{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new ValidationExc("Error sending object "+e);
        }

    }

    private FirmaTrProtobufs.Response readResponse() throws ValidationExc{
        FirmaTrProtobufs.Response response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws ValidationExc{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
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

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    FirmaTrProtobufs.Response response=FirmaTrProtobufs.Response.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private boolean isUpdateResponse(FirmaTrProtobufs.Response.Type type){
       return type== FirmaTrProtobufs.Response.Type.UPDATED;
    }

    private void handleUpdate(FirmaTrProtobufs.Response updateResponse) {

        if(updateResponse.getType()== FirmaTrProtobufs.Response.Type.UPDATED){
            List<CursaDestinatie> curse=ProtoUtils.getCurse(updateResponse);
            try {


                client.UpdateCurse(curse);

            } catch (ValidationExc e) {
                e.printStackTrace();
            }
        }

    }





}
