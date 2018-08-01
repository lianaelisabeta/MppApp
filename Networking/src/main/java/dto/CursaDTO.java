package dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class CursaDTO implements Serializable {
    private String loc;
    private String jud;
    private String tar;
    private Date date;
    private Time time;

    public CursaDTO(String loc,String jud,String tar,Date date,Time time){
        this.loc=loc;
        this.jud=jud;
        this.tar=tar;
        this.date=date;
        this.time=time;
    }

    public String getLoc() {
        return loc;
    }

    public String getJud() {
        return jud;
    }

    public String getTar() {
        return tar;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    @Override
    public String toString() {
        return getLoc()+" "+getJud()+" "+getTar()+" "+getDate()+" "+getTime();
    }
}
