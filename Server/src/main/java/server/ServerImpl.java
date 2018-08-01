package server;

import models.*;
import repository.IRepository;
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
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl  implements IServer {
    private IRepository<String,User> repoUser;
    private IRepository<Integer, Cursa> repoCursa;
    private IRepository<Integer, Destinatie> repoDest;
    private IRepository<Integer, Loc> repoLoc;
    private IRepository<Integer, Rezervare> repoRez;
    private Map<String, IObserver> loggedClients;

    public ServerImpl(IRepository<String,User> ru,IRepository<Integer,Cursa> rc,IRepository<Integer,Destinatie> rd,IRepository<Integer, Loc> rl,IRepository<Integer, Rezervare> rr){
        repoUser=ru;
        repoCursa=rc;
        repoDest=rd;
        repoLoc=rl;
        repoRez=rr;
        loggedClients=new ConcurrentHashMap<>();
    }

    public synchronized void login(User user, IObserver client) throws ValidationExc {
        User userR=repoUser.findOne(user.getId());
        if (userR!=null){
            if(loggedClients.get(user.getId())!=null)
                throw new ValidationExc("User already logged in.");
            loggedClients.put(user.getId(), client);

        }else
            throw new ValidationExc("Authentication failed.");
    }

    public synchronized void logout(User user, IObserver client) throws ValidationExc {
        IObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new ValidationExc("User "+user.getId()+" is not logged in.");

    }


    /* public synchronized int login(String userid,String password) throws ValidationExc {


        //User user=service.getUser(userid);
        String date=userid+"|"+password;
        try(Socket client=new Socket("localhost",55555)) {

            System.out.println("Connection obtained.  ");


            try(ObjectOutputStream out=new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in=new ObjectInputStream(client.getInputStream())) {
                out.flush();



                out.writeObject(date);
                out.flush();

                System.out.println("Waiting for response...");
                Object response = null;
                try {
                    response = in.readObject();
                    return (Integer)response;
                } catch (ClassNotFoundException e) {
                    System.out.println("Error deserializing " + e);
                }


            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }
*/
    public synchronized  List<CursaDestinatie> getCurse(){
        List<CursaDestinatie> list= new ArrayList<>();
        for(Cursa cursa:repoCursa.getAll()){
            Destinatie destinatie=repoDest.findOne(cursa.getDestinatieid());
            CursaDestinatie cd=new CursaDestinatie(cursa,destinatie);
            list.add(cd);
        }

        return list;
    }

    public Destinatie getDestinatie(String loc,String jud,String tara){
        for(Destinatie d:repoDest.getAll()){
            if(d.getLocalitate()==loc&&d.getJudet()==jud&&d.getTara()==tara){
                return d;
            }
        }
        return null;
    }

    public boolean validatePass(User user){
        User user1=repoUser.findOne(user.getId());
        if(user1.getPassword().equals(user.getPassword())){
            return true;
        }
        return false;
    }

    public synchronized Cursa getCursa(String loc, String jud, String tar, Date data, Time ora){
        int idd=0;
        for(Destinatie destinatie:repoDest.getAll()){
            if(destinatie.getLocalitate().equals(loc)&&destinatie.getJudet().equals(jud)&&destinatie.getTara().equals(tar)){
                idd=destinatie.getDestinatieid();
                break;
            }
        }
        for(Cursa c:repoCursa.getAll()){
            Date d1=c.getData();
            Time t1=c.getOra();
            int id1=c.getDestinatieid();
            if(c.getDestinatieid().equals(idd)&&d1.toString().equals(data.toString())&&t1.toString().equals(ora.toString())){
                return c;
            }
        }
        return null;

    }



    public List<Loc> getLocuri(Cursa cursa){
        List<Loc> list=new ArrayList<>(19);

        for(int i=0;i<18;i++){

            list.add(new Loc(1,i+1,"-",cursa.getCursaid()));

        }
        for(Rezervare rez:repoRez.getAll()){
            if(rez.getCursaid().equals(cursa.getCursaid())){
                for(Loc loc:repoLoc.getAll()){
                    if(loc.getCursaid()==rez.getCursaid()&&loc.getNumeclient().equals(rez.getNumeclient())){
                        list.set(loc.getNrloc()-1,loc);
                        //list.add(loc.getNrloc(),loc);
                    }
                }
            }
        }

        return list;
    }



    public synchronized void adaugaRezervare(Rezervare rez) throws ValidationExc{
        Cursa cursa=repoCursa.findOne(rez.getCursaid());
        if(cursa.getNrlocuri()<rez.getNrlocurirezervate()){
            throw new ValidationExc("Nu mai sunt locuri disponibile.");

        }
        repoRez.save(rez);
        cursa.setNrLocuri(cursa.getNrlocuri()-rez.getNrlocurirezervate());
        repoCursa.update(cursa);

        int nr=rez.getNrlocurirezervate();
        for(Loc loc:getLocuri(cursa)){
            if(nr==0){
                break;
            }
            if(loc.getNumeclient().equals("-")){
                repoLoc.save(new Loc(1,loc.getNrloc(),rez.getNumeclient(),cursa.getCursaid()));
                nr--;
            }
        }

        notifyclients(getCurse());
        //notifyclientsLoc(cursa);




    }

    private final int defaultThreadsNo=5;
    private void notifyclients(List<CursaDestinatie> curse) throws ValidationExc {
        System.out.println("in notifyclients nr clients:"+loggedClients.values().size());
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver obs :loggedClients.values()){

            if (obs!=null)
                executor.execute(() -> {
                    try {

                        obs.UpdateCurse(curse);
                    } catch (ValidationExc e) {
                        System.err.println("Error notifying friend " + e);
                    }
                });
        }

        executor.shutdown();
    }


    /*private void notifyclientsLoc(Cursa cursa) throws ValidationExc {

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(IObserver obs :loggedClients.values()){

            if (obs!=null)
                executor.execute(() -> {
                    try {

                        obs.UpdateLocuri(cursa);
                    } catch (ValidationExc e) {
                        System.err.println("Error notifying friend " + e);
                    }
                });
        }

        executor.shutdown();
    }*/

    public List<User> getUsers(){
        return repoUser.getAll();
    }
    public synchronized User getUser(String username){
        for(User u:repoUser.getAll()){
            if(u.getId().equals(username)){
                return u;
            }
        }
        return null;
    }

}
