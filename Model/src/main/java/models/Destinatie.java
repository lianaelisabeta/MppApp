package models;

import java.io.Serializable;

public class Destinatie implements HasId<Integer>,Serializable{
    private Integer DestinatieId;
    private String Localitate;
    private String Judet;
    private String Tara;

    public Destinatie() {

    }
    public Destinatie(Integer id,String l,String j,String t){
        DestinatieId=id;
        Localitate=l;
        Judet=j;
        Tara=t;
    }

    public Integer getDestinatieid() {
        return DestinatieId;
    }
    public Integer getId() {
        return DestinatieId;
    }


    public String getLocalitate() {
        return Localitate;
    }

    public String getJudet() {
        return Judet;
    }

    public String getTara() {
        return Tara;
    }

    public void setDestinatieId(Integer destinatieId) {
        DestinatieId = destinatieId;
    }
    public void setId(Integer destinatieId) {
        DestinatieId = destinatieId;
    }

    public void setLocalitate(String localitate) {
        Localitate = localitate;
    }

    public void setJudet(String judet) {
        Judet = judet;
    }

    public void setTara(String tara) {
        Tara = tara;
    }
    @Override
    public String toString(){
        return ""+DestinatieId+" "+Localitate+" "+Judet+" "+Tara;
    }
}
