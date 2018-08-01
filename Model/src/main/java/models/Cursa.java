package models;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Cursa implements HasId<Integer>,Serializable{
    private Integer CursaId;
    private Integer DestinatieId;
    private Date Data;
    private Time Ora;
    private Integer NrLocuri=18;

    public Cursa(Integer id,Integer did,Date d, Time t,Integer nr){
        CursaId=id;
        DestinatieId=did;
        Data=d;
        Ora=t;
        NrLocuri=nr;
    }

    public Integer getCursaid() {
        return CursaId;
    }
    public Integer getId() {
        return CursaId;
    }
    public void setId(Integer id){
        CursaId=id;
    }

    public Integer getDestinatieid() {
        return DestinatieId;
    }

    public Date getData() {
        return Data;
    }

    public Time getOra() {
        return Ora;
    }

    public Integer getNrlocuri() {
        return NrLocuri;
    }

    public void setCursaId(Integer cursaId) {
        CursaId = cursaId;
    }

    public void setDestinatieId(Integer destinatieId) {
        DestinatieId = destinatieId;
    }

    public void setData(Date data) {
        Data = data;
    }

    public void setOra(Time ora) {
        Ora = ora;
    }

    public void setNrLocuri(Integer nrLocuri) {
        NrLocuri = nrLocuri;
    }
    @Override
    public String toString(){
        return ""+CursaId+" "+DestinatieId+" "+Data.toString()+" "+Ora.toString()+" "+NrLocuri;
    }
}
