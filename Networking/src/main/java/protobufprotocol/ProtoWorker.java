package protobufprotocol;

import dto.CursaDTO;
import javafx.scene.chart.ValueAxis;
import models.*;
import services.IObserver;
import services.IServer;
import services.ValidationExc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ProtoWorker implements Runnable, IObserver {
    private IServer server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public ProtoWorker(IServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
            input=connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                // Object request=input.readObject();
                System.out.println("Waiting requests ...");
                FirmaTrProtobufs.Request request=FirmaTrProtobufs.Request.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                FirmaTrProtobufs.Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }



    private FirmaTrProtobufs.Response handleRequest(FirmaTrProtobufs.Request request){
        FirmaTrProtobufs.Response response=null;
        switch (request.getType()){
            case LOGIN: {
                System.out.println("Login request ...");
                User user = ProtoUtils.getUser(request);
                try {
                    server.login(user, this);
                    return ProtoUtils.createOkResponse();
                } catch (ValidationExc e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case LOGOUT:{
                System.out.println("Logout request");
                User user=ProtoUtils.getUser(request);
                try {
                    server.logout(user, this);
                    connected=false;
                    return ProtoUtils.createOkResponse();

                } catch (ValidationExc e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }}

            case GET_USER:{
                System.out.println("Get user by id request");
                String userid=ProtoUtils.getUsername(request);
                User user=server.getUser(userid);
                if(user.equals(null)){
                    return ProtoUtils.createErrorResponse("Non existent user");
                }
                else{
                    return ProtoUtils.createGetUserResponse(user);
                }
            }
            case GET_CURSA: {
                System.out.println("Get_cursa request...");

                CursaDTO cursaDTO=ProtoUtils.getCursaDTO(request);
                String[] dates=cursaDTO.getDate().toString().split("[-]");
                int y=Integer.parseInt(dates[0]);
                int m= Integer.parseInt(dates[1]);
                int day=Integer.parseInt(dates[2]);
                System.out.println("in getCursaDTO :"+cursaDTO.getLoc()+cursaDTO.getDate().getDate()+" "+cursaDTO.getTime().getHours()+" year: "+cursaDTO.getDate().getYear()+" "+cursaDTO.getDate().getMonth());
               // Date dt= new Date(cursaDTO.getDate().getYear(),cursaDTO.getDate().getMonth(),cursaDTO.getDate().getDate());
                Date dt= new Date(y,m,day);
                Time t=new Time(cursaDTO.getTime().getHours(),cursaDTO.getTime().getMinutes(),0);
                //System.out.println(cursaDTO.toString());
                try{
                    Cursa cursa=server.getCursa(cursaDTO.getLoc(),cursaDTO.getJud(),cursaDTO.getTar(),cursaDTO.getDate(),t);//dt,t);

                    return ProtoUtils.createGetCursaResponse(cursa);
                }catch (ValidationExc e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GET_CURSE:{
                System.out.println("GetCurse Request ...");


                try {
                    List<CursaDestinatie> curse=server.getCurse();
                    // User[] friends=server.getLoggedFriends(user);
                    //UserDTO[] frDTO=DTOUtils.getDTO(friends);
                    return ProtoUtils.createGetCurseResponse(curse);
                } catch (ValidationExc e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case ADD_REZERVARE:{
                System.out.println("Add_Rezervare request...");
                try{
                    server.adaugaRezervare(ProtoUtils.getRezervare(request));
                    List<CursaDestinatie> list= server.getCurse();
                    return ProtoUtils.createAddedRezervareResponse(list);
                }
                catch(ValidationExc e){
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GET_LOCURI:{
                System.out.println("GetLocuri Request ...");
                Cursa cursa=ProtoUtils.getCursa(request);

                try {

                    List<Loc> locuri=server.getLocuri(cursa);
                    return ProtoUtils.createGetLocuriResponse(locuri);
                } catch (ValidationExc e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
        }

        return response;

    }


    public void UpdateCurse(List<CursaDestinatie> curse){
        //curse=server.getCurse();
        // List<CursaDestinatie> curs=server.getCurse();
        System.out.println("in UpdateCurse "+curse.size());


        try {
            sendResponse(ProtoUtils.createUpdatedResponse(curse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(FirmaTrProtobufs.Response response) throws IOException{
        System.out.println("sending response "+response);
        response.writeDelimitedTo(output);
        //output.writeObject(response);
        output.flush();
    }


}
