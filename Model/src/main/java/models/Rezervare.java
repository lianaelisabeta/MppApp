package models;

import java.io.Serializable;

public class Rezervare implements HasId<Integer>,Serializable {
    private Integer RezervareId;
    private Integer CursaId;
    private String NumeClient;
    private Integer NrLocuriRezervate;

    public Rezervare(Integer id,Integer idc,String n,Integer nr){
        RezervareId=id;
        CursaId=idc;
        NumeClient=n;
        NrLocuriRezervate=nr;
    }

    public Integer getRezervareid() {
        return RezervareId;
    }
    public Integer getId() {
        return RezervareId;
    }


    public Integer getCursaid() {
        return CursaId;
    }

    public String getNumeclient() {
        return NumeClient;
    }

    public Integer getNrlocurirezervate() {
        return NrLocuriRezervate;
    }

    public void setRezervareId(Integer rezervareId) {
        RezervareId = rezervareId;
    }
    public void setId(Integer rezervareId) {
        RezervareId = rezervareId;
    }

    public void setNumeClient(String numeClient) {
        NumeClient = numeClient;
    }

    public void setCursaId(Integer cursaId) {
        CursaId = cursaId;
    }

    public void setNrLocuriRezervate(Integer nrLocuriRezervate) {
        NrLocuriRezervate = nrLocuriRezervate;
    }
    @Override
    public String toString(){
        return ""+RezervareId+" "+CursaId+" "+NumeClient+" "+NrLocuriRezervate;
    }
}
