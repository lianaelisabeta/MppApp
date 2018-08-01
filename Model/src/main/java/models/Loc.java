package models;

import java.io.Serializable;

public class Loc implements HasId<Integer>,Serializable{
    private Integer LocId;
    private Integer NrLoc;
    private String NumeClient;
    private Integer CursaId;


    public Loc(Integer id,Integer nr,String client,Integer idc){
        LocId=id;
        NrLoc=nr;
        NumeClient=client;
        CursaId=idc;

    }

    public Integer getLocid() {
        return LocId;
    }
    public Integer getId() {
        return LocId;
    }


    public Integer getNrloc() {
        return NrLoc;
    }

    public String getNumeclient() {
        return NumeClient;
    }

    public Integer getCursaid() {
        return CursaId;
    }



    public void setCursaId(Integer cursaId) {
        CursaId = cursaId;
    }

    public void setLocid(Integer locId) {
        LocId = locId;
    }
    public void setCursaid(Integer cursaId) {
        CursaId = cursaId;
    }

    public void setLocId(Integer locId) {
        LocId = locId;
    }
    public void setId(Integer locId) {
        LocId = locId;
    }

    public void setNrLoc(Integer nrLoc) {
        NrLoc = nrLoc;
    }

    public void setNumeClient(String numeClient) {
        NumeClient = numeClient;
    }


    public void setNrloc(Integer nrLoc) {
        NrLoc = nrLoc;
    }

    public void setNumeclient(String numeClient) {
        NumeClient = numeClient;
    }


    @Override
    public String toString(){
        return ""+LocId+" "+NrLoc+" "+NumeClient+" "+CursaId;
    }
}
