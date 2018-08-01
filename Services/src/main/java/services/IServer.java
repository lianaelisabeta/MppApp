package services;

import models.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public interface IServer {

    //int login(String uid,String pass);
    void login(User user, IObserver client) throws ValidationExc;
    void logout(User user, IObserver client) throws ValidationExc;
    List<CursaDestinatie> getCurse();
    //Destinatie getDestinatie(String loc, String jud, String tara);

    Cursa getCursa(String loc, String jud, String tar, Date data, Time ora);
    List<Loc> getLocuri(Cursa cursa);
    void adaugaRezervare(Rezervare rez) throws ValidationExc;
    User getUser(String username);






}
