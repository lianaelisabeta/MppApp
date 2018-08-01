package models;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

public class CursaDestinatie  implements Serializable{
    private Cursa cursa;
    private Destinatie destinatie;

    public CursaDestinatie(Cursa cursa,Destinatie destinatie){
        this.cursa=cursa;
        this.destinatie=destinatie;
    }

    public Cursa getCursa(){
        return cursa;
    }
    public Destinatie getDestinatie(){return destinatie;}
    public String getLocalitate(){
        return destinatie.getLocalitate();
    }
    public String getJudet(){
        return destinatie.getJudet();
    }
    public String getTara(){
        return destinatie.getTara();
    }
    public Date getData(){
        return cursa.getData();
    }
    public int getAn(){
        return cursa.getData().getYear();
    }
    public int getLuna(){
        return cursa.getData().getMonth();
    }
    public Time getOra(){
        return cursa.getOra();
    }
    public Integer getLocuri(){
        return cursa.getNrlocuri();
    }
}
