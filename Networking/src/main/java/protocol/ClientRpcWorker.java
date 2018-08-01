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
import java.util.List;


public class ClientRpcWorker implements Runnable, IObserver {
    private IServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ClientRpcWorker(IServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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





    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
  // private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()==RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());

            User user=(User)request.data();

            try {
                server.login(user, this);
                return okResponse;
            } catch (ValidationExc e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()==RequestType.LOGOUT){
            System.out.println("Logout request");
           // LogoutRequest logReq=(LogoutRequest)request;
            User user=(User)request.data();

            try {
                server.logout(user, this);
                connected=false;
                return okResponse;

            } catch (ValidationExc e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.GET_USER){
            System.out.println("Get user by id request");
            String uid=(String)request.data();

                User user=server.getUser(uid);
                if(user.equals(null)){
                    return new Response.Builder().type(ResponseType.ERROR).data("Non existent user").build();
                }
                else{

                    return new Response.Builder().type(ResponseType.OK).data(user).build();
                }


        }


       if (request.type()==RequestType.GET_CURSE){
            System.out.println("GetCurse Request ...");


            try {
                List<CursaDestinatie> curse=server.getCurse();
               // User[] friends=server.getLoggedFriends(user);
                //UserDTO[] frDTO=DTOUtils.getDTO(friends);
                return new Response.Builder().type(ResponseType.GET_CURSE).data(curse).build();
            } catch (ValidationExc e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.ADD_REZERVARE){
            System.out.println("Add_Rezervare request...");
            try{
                server.adaugaRezervare((Rezervare)request.data());
                List<CursaDestinatie> list= server.getCurse();
                return new Response.Builder().type(ResponseType.ADDED_REZERVARE).data(list).build();
            }
            catch(ValidationExc e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type()==RequestType.GET_CURSA){
            System.out.println("Get_cursa request...");
            CursaDTO cursaDTO=(CursaDTO)request.data();
            System.out.println(cursaDTO.toString());
            try{
                Cursa cursa=server.getCursa(cursaDTO.getLoc(),cursaDTO.getJud(),cursaDTO.getTar(),cursaDTO.getDate(),cursaDTO.getTime());
                return new Response.Builder().type(ResponseType.GET_CURSA).data(cursa).build();
            }catch (ValidationExc e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()==RequestType.GET_LOCURI){
            System.out.println("GetLocuri Request ...");
            Cursa cursa=(Cursa)request.data();

            try {

                List<Loc> locuri=server.getLocuri(cursa);
                return new Response.Builder().type(ResponseType.GET_LOCURI).data(locuri).build();
            } catch (ValidationExc e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

public void UpdateCurse(List<CursaDestinatie> curse){
        //curse=server.getCurse();
       // List<CursaDestinatie> curs=server.getCurse();
    System.out.println("in UpdateCurse "+curse.size());

    Response resp=new Response.Builder().type(ResponseType.UPDATED).data(curse).build();
    try {
        sendResponse(resp);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

/*public void UpdateLocuri(Cursa cursa){
        System.out.println("in Update locuri "+cursa.toString());
        Response resp= new Response.Builder().type(ResponseType.UPDATEL).data(cursa).build();
    try {
        sendResponse(resp);
    } catch (IOException e) {
        e.printStackTrace();
    }

}*/


    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output){
        output.writeObject(response);
        output.flush();}
    }
}
