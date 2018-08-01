package protobufprotocol;

import dto.CursaDTO;
import models.*;

//import java.sql.Date;
//import java.sql.Time;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static FirmaTrProtobufs.Request createLoginRequest(User user){
        FirmaTrProtobufs.User user2=FirmaTrProtobufs.User.newBuilder().setUserId(user.getId()).setPassword(user.getPassword()).setName(user.getName()).build();
        FirmaTrProtobufs.Request request= FirmaTrProtobufs.Request.newBuilder().setType(FirmaTrProtobufs.Request.Type.LOGIN)
                .setUser(user2).build();
        return request;
    }
    public static FirmaTrProtobufs.Request createLogoutRequest(User user){
        FirmaTrProtobufs.User user2=FirmaTrProtobufs.User.newBuilder().setUserId(user.getId()).build();
        FirmaTrProtobufs.Request request= FirmaTrProtobufs.Request.newBuilder().setType(FirmaTrProtobufs.Request.Type.LOGOUT)
                .setUser(user2).build();
        return request;
    }
    public static FirmaTrProtobufs.Request createGetUserRequest(String username){
        FirmaTrProtobufs.Request request=FirmaTrProtobufs.Request.newBuilder().setType(FirmaTrProtobufs.Request.Type.GET_USER).setUsername(username).build();
        return request;
    }

    public static FirmaTrProtobufs.Response createGetUserResponse(User user){
        FirmaTrProtobufs.User user2=FirmaTrProtobufs.User.newBuilder().setUserId(user.getId())
                .setPassword(user.getPassword())
                .setName(user.getName())
                .build();
        FirmaTrProtobufs.Response response=FirmaTrProtobufs.Response.newBuilder()
                .setType(FirmaTrProtobufs.Response.Type.GET_USER)
                .setUser(user2).build();
        return response;
    }
    public static FirmaTrProtobufs.Request createAddRezervareRequest(Rezervare rez){
          FirmaTrProtobufs.Rezervare rezervare=FirmaTrProtobufs.Rezervare.newBuilder().setRezervareId(rez.getId())
                  .setCursaId(rez.getCursaid())
                  .setNrLocuriRezervate(rez.getNrlocurirezervate())
                  .setNumeClient(rez.getNumeclient())
                  .build();
          FirmaTrProtobufs.Request request=FirmaTrProtobufs.Request.newBuilder().setType(FirmaTrProtobufs.Request.Type.ADD_REZERVARE)
                  .setRez(rezervare).build();

          return request;
    }

    public static FirmaTrProtobufs.Response createAddedRezervareResponse(List<CursaDestinatie> curse){
        FirmaTrProtobufs.Response.Builder response=FirmaTrProtobufs.Response.newBuilder()
                .setType(FirmaTrProtobufs.Response.Type.GET_CURSE);
        for(CursaDestinatie c:curse){
            String[] dates=c.getData().toString().split("[-]");
            int y=Integer.parseInt(dates[0]);
            int m=Integer.parseInt(dates[1]);
            int d=Integer.parseInt(dates[2]);

            FirmaTrProtobufs.Date date=FirmaTrProtobufs.Date.newBuilder()
                    /*.setDay(c.getData().getDay())
                    .setMonth(c.getData().getMonth())
                    .setYear(c.getData().getYear())*/
                    .setDay(d)
                    .setMonth(m)
                    .setYear(y)
                    .build();
            FirmaTrProtobufs.Time time=FirmaTrProtobufs.Time.newBuilder()
                    .setHour(c.getOra().getHours())
                    .setMinute(c.getOra().getMinutes())
                    .build();


            FirmaTrProtobufs.Cursa cursa1=FirmaTrProtobufs.Cursa.newBuilder()
                    .setCursaId(c.getCursa().getCursaid())
                    .setDestinatieId(c.getDestinatie().getDestinatieid())
                    .setNrLocuri(c.getLocuri())
                    .setOra(time)
                    .setData(date)
                    .build();
            FirmaTrProtobufs.Destinatie destinatie=FirmaTrProtobufs.Destinatie.newBuilder()
                    .setDestinatieId(c.getDestinatie().getDestinatieid())
                    .setLocalitate(c.getLocalitate())
                    .setJudet(c.getJudet())
                    .setTara(c.getTara())
                    .build();

            FirmaTrProtobufs.CursaDestinatie cursa=FirmaTrProtobufs.CursaDestinatie.newBuilder()
                    .setCursa(cursa1)
                    .setDestinatie(destinatie)
                    .build();

            response.addCurse(cursa);

        }
        return response.build();
    }

    public static FirmaTrProtobufs.Response createUpdatedResponse(List<CursaDestinatie> curse){
        FirmaTrProtobufs.Response.Builder response=FirmaTrProtobufs.Response.newBuilder()
                .setType(FirmaTrProtobufs.Response.Type.UPDATED);
        for(CursaDestinatie c:curse){
            String[] dates=c.getData().toString().split("[-]");
            int y=Integer.parseInt(dates[0]);
            int m=Integer.parseInt(dates[1]);
            int d=Integer.parseInt(dates[2]);
            FirmaTrProtobufs.Date date=FirmaTrProtobufs.Date.newBuilder()
                    /*.setDay(c.getData().getDay())
                    .setMonth(c.getData().getMonth())
                    .setYear(c.getData().getYear())*/
                    .setDay(d)
                    .setMonth(m)
                    .setYear(y)
                    .build();
            FirmaTrProtobufs.Time time=FirmaTrProtobufs.Time.newBuilder()
                    .setHour(c.getOra().getHours())
                    .setMinute(c.getOra().getMinutes())
                    .build();


            FirmaTrProtobufs.Cursa cursa1=FirmaTrProtobufs.Cursa.newBuilder()
                    .setCursaId(c.getCursa().getCursaid())
                    .setDestinatieId(c.getDestinatie().getDestinatieid())
                    .setNrLocuri(c.getLocuri())
                    .setOra(time)
                    .setData(date)
                    .build();
            FirmaTrProtobufs.Destinatie destinatie=FirmaTrProtobufs.Destinatie.newBuilder()
                    .setDestinatieId(c.getDestinatie().getDestinatieid())
                    .setLocalitate(c.getLocalitate())
                    .setJudet(c.getJudet())
                    .setTara(c.getTara())
                    .build();

            FirmaTrProtobufs.CursaDestinatie cursa=FirmaTrProtobufs.CursaDestinatie.newBuilder()
                    .setCursa(cursa1)
                    .setDestinatie(destinatie)
                    .build();

            response.addCurse(cursa);

        }
        return response.build();
    }

    public static FirmaTrProtobufs.Request createGetCurseRequest(List<CursaDestinatie> curse){
        FirmaTrProtobufs.Request.Builder request=FirmaTrProtobufs.Request.newBuilder()
                .setType(FirmaTrProtobufs.Request.Type.GET_CURSE);
        for(CursaDestinatie c:curse){
            String[] dates=c.getData().toString().split("[-]");
            int y=Integer.parseInt(dates[0]);
            int m=Integer.parseInt(dates[1]);
            int d=Integer.parseInt(dates[2]);
            FirmaTrProtobufs.Date date=FirmaTrProtobufs.Date.newBuilder()
                   /* .setDay(c.getData().getDay())
                    .setMonth(c.getData().getMonth())
                    .setYear(c.getData().getYear())*/
                   .setDay(d)
                    .setMonth(m)
                    .setYear(y)
                    .build();
            FirmaTrProtobufs.Time time=FirmaTrProtobufs.Time.newBuilder()
                    .setHour(c.getOra().getHours())
                    .setMinute(c.getOra().getMinutes())
                    .build();


            FirmaTrProtobufs.Cursa cursa1=FirmaTrProtobufs.Cursa.newBuilder()
                    .setCursaId(c.getCursa().getCursaid())
                    .setDestinatieId(c.getDestinatie().getDestinatieid())
                    .setNrLocuri(c.getLocuri())
                    .setOra(time)
                    .setData(date)
                    .build();
            FirmaTrProtobufs.Destinatie destinatie=FirmaTrProtobufs.Destinatie.newBuilder()
                    .setDestinatieId(c.getDestinatie().getDestinatieid())
                    .setLocalitate(c.getLocalitate())
                    .setJudet(c.getJudet())
                    .setTara(c.getTara())
                    .build();

            FirmaTrProtobufs.CursaDestinatie cursa=FirmaTrProtobufs.CursaDestinatie.newBuilder()
                    .setCursa(cursa1)
                    .setDestinatie(destinatie)
                    .build();

            request.addCurse(cursa);

        }
        return request.build();
    }
    public static FirmaTrProtobufs.Response createGetCurseResponse(List<CursaDestinatie> curse){
        FirmaTrProtobufs.Response.Builder response=FirmaTrProtobufs.Response.newBuilder()
                .setType(FirmaTrProtobufs.Response.Type.GET_CURSE);
        for(CursaDestinatie c:curse){
            String[] dates=c.getData().toString().split("[-]");
            int y=Integer.parseInt(dates[0]);
            int m=Integer.parseInt(dates[1]);
            int d=Integer.parseInt(dates[2]);
            System.out.println(c.getData().toString());

            FirmaTrProtobufs.Date date=FirmaTrProtobufs.Date.newBuilder()
                  //  .setDay(c.getData().getDate())
                  //  .setMonth(c.getData().getMonth())
                  //  .setYear(c.getData().getYear())
                    .setDay(d)
                    .setMonth(m)
                    .setYear(y)
                    .build();
            FirmaTrProtobufs.Time time=FirmaTrProtobufs.Time.newBuilder()
                    .setHour(c.getOra().getHours())
                    .setMinute(c.getOra().getMinutes())
                    .build();


            FirmaTrProtobufs.Cursa cursa1=FirmaTrProtobufs.Cursa.newBuilder()
                    .setCursaId(c.getCursa().getCursaid())
                    .setDestinatieId(c.getDestinatie().getDestinatieid())
                    .setNrLocuri(c.getLocuri())
                    .setOra(time)
                    .setData(date)
                    .build();
            FirmaTrProtobufs.Destinatie destinatie=FirmaTrProtobufs.Destinatie.newBuilder()
                    .setDestinatieId(c.getDestinatie().getDestinatieid())
                    .setLocalitate(c.getLocalitate())
                    .setJudet(c.getJudet())
                    .setTara(c.getTara())
                    .build();

            FirmaTrProtobufs.CursaDestinatie cursa=FirmaTrProtobufs.CursaDestinatie.newBuilder()
                    .setCursa(cursa1)
                    .setDestinatie(destinatie)
                    .build();

            response.addCurse(cursa);

        }
        return response.build();
    }

    public static FirmaTrProtobufs.Request createGetCursaRequest(CursaDTO cursaDTO){
        System.out.println(cursaDTO.getDate().getMonth()+" "+cursaDTO.getDate().getYear());
        String[] dates=cursaDTO.getDate().toString().split("[-]");
        int d=Integer.parseInt(dates[2]);
        int y=Integer.parseInt(dates[0]);
        int m=Integer.parseInt(dates[1]);

        FirmaTrProtobufs.Date date=FirmaTrProtobufs.Date.newBuilder()
                .setDay(d)
                .setMonth(m)
                .setYear(y)
                .build();
        FirmaTrProtobufs.Time time=FirmaTrProtobufs.Time.newBuilder()
                .setHour(cursaDTO.getTime().getHours())
                .setMinute(cursaDTO.getTime().getMinutes())
                .build();
        FirmaTrProtobufs.CursaDTO cursaDTO1=FirmaTrProtobufs.CursaDTO.newBuilder()
                .setDate(date)
                .setLoc(cursaDTO.getLoc())
                .setJud(cursaDTO.getJud())
                .setTar(cursaDTO.getTar())
                .setTime(time)
                .build();
        FirmaTrProtobufs.Request request=FirmaTrProtobufs.Request.newBuilder().setType(FirmaTrProtobufs.Request.Type.GET_CURSA)
                .setCursadto(cursaDTO1)
                .build();
        return request;
    }

    public static FirmaTrProtobufs.Response createGetCursaResponse(Cursa cursa){
        System.out.println(cursa.getData().getMonth()+" "+cursa.getData().getYear());
        String[] dates=cursa.getData().toString().split("[-]");
        int y=Integer.parseInt(dates[0]);
        int m=Integer.parseInt(dates[1]);
        int d=Integer.parseInt(dates[2]);
        FirmaTrProtobufs.Date date=FirmaTrProtobufs.Date.newBuilder()
                .setDay(d)
                .setMonth(m)
                .setYear(y)
                .build();
        FirmaTrProtobufs.Time time=FirmaTrProtobufs.Time.newBuilder()
                .setHour(cursa.getOra().getHours())
                .setMinute(cursa.getOra().getMinutes())
                .build();
        FirmaTrProtobufs.Cursa cursa1=FirmaTrProtobufs.Cursa.newBuilder().setCursaId(cursa.getCursaid())
                .setDestinatieId(cursa.getDestinatieid())
                .setNrLocuri(cursa.getNrlocuri())
                .setData(date)
                .setOra(time)
                .build();
        FirmaTrProtobufs.Response response=FirmaTrProtobufs.Response.newBuilder().setType(FirmaTrProtobufs.Response.Type.GET_CURSA)
                .setCursa(cursa1)
                .build();
        return response;
    }

    public static FirmaTrProtobufs.Request createGetLocuriRequest(Cursa cursa){
        String[] dates=cursa.getData().toString().split("[-]");
        int y=Integer.parseInt(dates[0]);
        int m=Integer.parseInt(dates[1]);
        int d=Integer.parseInt(dates[2]);
        FirmaTrProtobufs.Date date=FirmaTrProtobufs.Date.newBuilder()
                .setDay(d)
                .setMonth(m)
                .setYear(y)
                .build();
        FirmaTrProtobufs.Time time=FirmaTrProtobufs.Time.newBuilder()
                .setHour(cursa.getOra().getHours())
                .setMinute(cursa.getOra().getMinutes())
                .build();
        FirmaTrProtobufs.Cursa cursa1=FirmaTrProtobufs.Cursa.newBuilder().setCursaId(cursa.getCursaid())
                .setDestinatieId(cursa.getDestinatieid())
                .setNrLocuri(cursa.getNrlocuri())
                .setData(date)
                .setOra(time)
                .build();
        FirmaTrProtobufs.Request request=FirmaTrProtobufs.Request.newBuilder().setType(FirmaTrProtobufs.Request.Type.GET_LOCURI)
                .setCursa(cursa1)
                .build();
        return request;
    }

    public static FirmaTrProtobufs.Response createGetLocuriResponse(List<Loc> locuri){
        FirmaTrProtobufs.Response.Builder response=FirmaTrProtobufs.Response.newBuilder()
                .setType(FirmaTrProtobufs.Response.Type.GET_LOCURI);
        for(Loc loc:locuri){
            FirmaTrProtobufs.Loc l= FirmaTrProtobufs.Loc.newBuilder()
                    .setLocId(loc.getLocid())
                    .setCursaId(loc.getCursaid())
                    .setNrLoc(loc.getNrloc())
                    .setNumeClient(loc.getNumeclient())
                    .build();
            response.addLocuri(l);
        }
        return response.build();
    }


    public static FirmaTrProtobufs.Response createOkResponse(){
        FirmaTrProtobufs.Response response=FirmaTrProtobufs.Response.newBuilder()
                .setType(FirmaTrProtobufs.Response.Type.OK).build();
        return response;
    }

    public static FirmaTrProtobufs.Response createErrorResponse(String text){
        FirmaTrProtobufs.Response response=FirmaTrProtobufs.Response.newBuilder()
                .setType(FirmaTrProtobufs.Response.Type.ERROR)
                .setError(text).build();
        return response;
    }

    public static String getError(FirmaTrProtobufs.Response response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static User getUser(FirmaTrProtobufs.Response response){
        User user=new User("","","");
        user.setId(response.getUser().getUserId());
        user.setName(response.getUser().getName());
        user.setPassword(response.getUser().getPassword());
        return user;
    }

    public static User getUser(FirmaTrProtobufs.Request request){
        User user=new User("","","");
        user.setId(request.getUser().getUserId());
        user.setName(request.getUser().getName());
        user.setPassword(request.getUser().getPassword());
        return user;
    }

    public static String getUsername(FirmaTrProtobufs.Request request){
        String username=request.getUsername();
        return username;
    }

    public static Rezervare getRezervare(FirmaTrProtobufs.Request request){
        Rezervare rezervare=new Rezervare(0,0,"",0);
        rezervare.setRezervareId(request.getRez().getRezervareId());
        rezervare.setCursaId(request.getRez().getCursaId());
        rezervare.setNumeClient(request.getRez().getNumeClient());
        rezervare.setNrLocuriRezervate(request.getRez().getNrLocuriRezervate());

        return rezervare;
    }

    public static CursaDTO getCursaDTO(FirmaTrProtobufs.Request request){
       // System.out.println("in getCursaDTO :"+request.getCursadto().getDate().getDay());
        System.out.println(request.getCursadto().getDate().toString());
        String[] dates=request.getCursadto().getDate().toString().split("[\\n]");
        String[] y1=dates[2].split("[: ]");
        for(String s:y1){
            System.out.println("sir:"+s);
        }
        int y=Integer.parseInt(dates[2].split("[: ]")[2]);
        int m=Integer.parseInt(dates[1].split("[: ]")[2]);
        int day=Integer.parseInt(dates[0].split("[: ]")[2]);

      //  Date d=new Date(request.getCursadto().getDate().getYear(),request.getCursadto().getDate().getMonth(),request.getCursadto().getDate().getDay());
        Date d=new Date(y,m,day);
       d.setDate(day);
        d.setMonth(m-1);
        d.setYear(y-1900);
        Time t=new Time(0,0,0);
        t.setHours(request.getCursadto().getTime().getHour());
        t.setMinutes(request.getCursadto().getTime().getMinute());
        CursaDTO cursaDTO=new CursaDTO(request.getCursadto().getLoc(),request.getCursadto().getJud(),request.getCursadto().getTar(),d,t);
      //  System.out.println("in getCursaDTO :"+cursaDTO.getLoc()+cursaDTO.getDate().getDate()+" "+cursaDTO.getTime().getHours());
        return cursaDTO;

    }

    public static Cursa getCursa(FirmaTrProtobufs.Response response){
        java.sql.Date d=new java.sql.Date(0,0,0);
        String[] dates=response.getCursa().getData().toString().split("[-]");
        int y=Integer.parseInt(dates[0]);
        int m=Integer.parseInt(dates[1]);
        int day=Integer.parseInt(dates[2]);
        d.setDate(day);//response.getCursa().getData().getDay());
        d.setMonth(m);//response.getCursa().getData().getMonth());
        d.setYear(y);//response.getCursa().getData().getYear());
        java.sql.Time t=new java.sql.Time(0,0,0);
        t.setHours(response.getCursa().getOra().getHour());
        t.setMinutes(response.getCursa().getOra().getMinute());
        Cursa cursa=new Cursa(response.getCursa().getCursaId(),response.getCursa().getDestinatieId(),d,t,response.getCursa().getNrLocuri());
        return cursa;
    }

    public static Cursa getCursa(FirmaTrProtobufs.Request request){
        String[] dates=request.getCursa().getData().toString().split("[-]");
        int y=Integer.parseInt(dates[0]);
        int m=Integer.parseInt(dates[1]);
        int day=Integer.parseInt(dates[2]);
        java.sql.Date d=new java.sql.Date(0,0,0);
        d.setDate(day);//request.getCursa().getData().getDay());
        d.setMonth(m);//request.getCursa().getData().getMonth());
        d.setYear(y);//request.getCursa().getData().getYear());
        java.sql.Time t=new java.sql.Time(0,0,0);
        t.setHours(request.getCursa().getOra().getHour());
        t.setMinutes(request.getCursa().getOra().getMinute());
        Cursa cursa=new Cursa(request.getCursa().getCursaId(),request.getCursa().getDestinatieId(),d,t,request.getCursa().getNrLocuri());
        return cursa;
    }

    public static List<Loc> getLocuri(FirmaTrProtobufs.Response response){
        List<Loc> locuri=new ArrayList<>();
        int nrloc=response.getLocuriCount();
        for(int i=0;i<nrloc;i++){
            FirmaTrProtobufs.Loc loc=response.getLocuri(i);
            Loc l=new Loc(loc.getLocId(),loc.getNrLoc(),loc.getNumeClient(),loc.getCursaId());
            locuri.add(l);
        }
        return locuri;
    }

    public static List<CursaDestinatie> getCurse(FirmaTrProtobufs.Response response){
        List<CursaDestinatie> curse=new ArrayList<>();
        int nrcurse=response.getCurseCount();
        for(int i=0;i<nrcurse;i++){
            FirmaTrProtobufs.CursaDestinatie cd=response.getCurse(i);

            java.sql.Date d=new java.sql.Date(0,0,0);
            String[] dates=response.getCurse(i).getCursa().getData().toString().split("[-]");
            int y=Integer.parseInt(dates[0]);
            int m=Integer.parseInt(dates[1]);
            int day=Integer.parseInt(dates[2]);
            d.setDate(day);//response.getCurse(i).getCursa().getData().getDay());
            d.setMonth(m);//response.getCurse(i).getCursa().getData().getMonth());
            d.setYear(y);//response.getCurse(i).getCursa().getData().getYear());
            java.sql.Time t=new java.sql.Time(0,0,0);
            t.setHours(response.getCurse(i).getCursa().getOra().getHour());
            t.setMinutes(response.getCurse(i).getCursa().getOra().getMinute());


            Cursa c=new Cursa(response.getCurse(i).getCursa().getCursaId(),response.getCurse(i).getCursa().getDestinatieId(),d,t,response.getCurse(i).getCursa().getNrLocuri());
            Destinatie destinatie=new Destinatie(response.getCurse(i).getDestinatie().getDestinatieId(),response.getCurse(i).getDestinatie().getLocalitate(),response.getCurse(i).getDestinatie().getJudet(),response.getCurse(i).getDestinatie().getTara());

            CursaDestinatie cursa=new CursaDestinatie(c,destinatie);
            curse.add(cursa);
        }
        return curse;
    }


    public static List<CursaDestinatie> getCurse(FirmaTrProtobufs.Request request){
        List<CursaDestinatie> curse=new ArrayList<>();
        int nrcurse=request.getCurseCount();
        for(int i=0;i<nrcurse;i++){
            FirmaTrProtobufs.CursaDestinatie cd=request.getCurse(i);

            java.sql.Date d=new java.sql.Date(0,0,0);

            String[] dates=request.getCurse(i).getCursa().getData().toString().split("[-]");
            int y=Integer.parseInt(dates[0]);
            int m=Integer.parseInt(dates[1]);
            int day=Integer.parseInt(dates[2]);

            d.setDate(day);//request.getCurse(i).getCursa().getData().getDay());
            d.setMonth(m);//request.getCurse(i).getCursa().getData().getMonth());
            d.setYear(y);//request.getCurse(i).getCursa().getData().getYear());
            java.sql.Time t=new java.sql.Time(0,0,0);
            t.setHours(request.getCurse(i).getCursa().getOra().getHour());
            t.setMinutes(request.getCurse(i).getCursa().getOra().getMinute());


            Cursa c=new Cursa(request.getCurse(i).getCursa().getCursaId(),request.getCurse(i).getCursa().getDestinatieId(),d,t,request.getCurse(i).getCursa().getNrLocuri());
            Destinatie destinatie=new Destinatie(request.getCurse(i).getDestinatie().getDestinatieId(),request.getCurse(i).getDestinatie().getLocalitate(),request.getCurse(i).getDestinatie().getJudet(),request.getCurse(i).getDestinatie().getTara());

            CursaDestinatie cursa=new CursaDestinatie(c,destinatie);
            curse.add(cursa);
        }
        return curse;
    }





}
