package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.*;
import server.ServerImpl;
import services.IObserver;
import services.IServer;


import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CurseController implements IObserver {
    IServer service;
    ObservableList<CursaDestinatie> model;
    ObservableList<Loc> locuri;
    private Cursa c;
    @FXML
    private Button btnsearch;
    @FXML
    private Button btnrez;

    @FXML
    private AnchorPane anchor;

    @FXML
    private TableView<Loc> tableSearch;

    @FXML
    private TableColumn<Loc,String> colnrloc;

    @FXML
    private TableColumn<Loc,String> colclient;

    @FXML
    private TableView<CursaDestinatie> tableCurse;

    @FXML
    private TableColumn<CursaDestinatie,String> cloc;

    @FXML
    private TableColumn<CursaDestinatie,String> cjud;
    @FXML
    private TableColumn<CursaDestinatie,String> ctar;
    @FXML
    private TableColumn<CursaDestinatie,String> cdata;
    @FXML
    private TableColumn<CursaDestinatie,String> cora;
    @FXML
    private TableColumn<CursaDestinatie,String> cdisp;

    @FXML
    private TextField txtloc;
    @FXML
    private TextField txtjud;
    @FXML
    private TextField txttar;
    @FXML
    private TextField txtora;
    @FXML
    private TextField txtclient;
    @FXML
    private TextField txtnrloc;
    @FXML
    private DatePicker datepick;
    @FXML
    private Label lbluname;
    @FXML
    private Button btnlogout;
private User user=null;

    public CurseController(){

    }

    public void setService(IServer service) {
        this.service=service;
        //this.user=user;
        //lbluname.setText(user.getName());




        this.model= FXCollections.observableArrayList(service.getCurse());
        tableCurse.setItems(model);


    }
    public void setUser(User user){
        this.user=user;
        lbluname.setText(user.getName());
    }

    public void UpdateCurse(List<CursaDestinatie> list){
        model.setAll(FXCollections.observableArrayList(list));
        tableCurse.setItems(model);


    }

   /* public void UpdateLocuri(Cursa cursa){
        locuri=FXCollections.observableArrayList(service.getLocuri(cursa));
        tableSearch.setItems(locuri);
        colnrloc.setCellValueFactory(new PropertyValueFactory<Loc,String>("nrloc"));
        colclient.setCellValueFactory(new PropertyValueFactory<Loc,String>("numeclient"));

    }*/

  /*  @Override
    public void update(Observable o, Object arg) {
        model.setAll(FXCollections.observableArrayList(service.getCurse()));
        tableCurse.setItems(model);
       /* Cursa cursa=tableCurse.getSelectionModel().getSelectedItem().getCursa();
        if(cursa!=null){
            setTableLocuri(cursa);
        }
    }*/

    @FXML
    private void initialize(){
        tableCurse.setItems(model);
        cloc.setCellValueFactory(new PropertyValueFactory<CursaDestinatie,String>("localitate"));
        cjud.setCellValueFactory(new PropertyValueFactory<CursaDestinatie,String>("judet"));
        ctar.setCellValueFactory(new PropertyValueFactory<CursaDestinatie,String>("tara"));
        cdata.setCellValueFactory(new PropertyValueFactory<CursaDestinatie,String>("data"));
        cora.setCellValueFactory(new PropertyValueFactory<CursaDestinatie,String>("ora"));
        cdisp.setCellValueFactory(new PropertyValueFactory<CursaDestinatie,String>("locuri"));




    }

    @FXML
    private void handleCauta(){
        String loc=txtloc.getText();
        String jud=txtjud.getText();
        String tar=txttar.getText();
        Date date= Date.valueOf(datepick.getValue().toString());
       // Time ora=new Time(1);
        String[] h=txtora.getText().split("[:]");
        int h1=Integer.valueOf(h[0]);
        int m1=Integer.valueOf(h[1]);


        Time ora=new Time(h1,m1,0);
       // Date time=Date.valueOf(cal.getTime().toString());


        //Time ora= Time.valueOf(time.toString());

        Cursa cursa=service.getCursa(loc,jud,tar,date,ora);

        setTableLocuri(cursa);




    }

    private void setTableLocuri(Cursa cursa){
        locuri=FXCollections.observableArrayList(service.getLocuri(cursa));
        tableSearch.setItems(locuri);
        colnrloc.setCellValueFactory(new PropertyValueFactory<Loc,String>("nrloc"));
        colclient.setCellValueFactory(new PropertyValueFactory<Loc,String>("numeclient"));


    }

    @FXML
    public void handleRezerva(){
        Cursa cursa=tableCurse.getSelectionModel().getSelectedItem().getCursa();
        if(cursa!=null){
            this.c=cursa;
            setTableLocuri(cursa);
            try{
            String nume=txtclient.getText();
            Integer nrloc=Integer.parseInt(txtnrloc.getText());

            Rezervare rez=new Rezervare(1,cursa.getCursaid(),nume,nrloc);

            service.adaugaRezervare(rez);
               // model.setAll(FXCollections.observableArrayList(service.getCurse()));
               // tableCurse.setItems(model);

            setTableLocuri(cursa);
           //update(this.service,this);


        }catch(NumberFormatException nex){
                System.out.println("Dati un numar.");
            }


        }
        else
        {
            System.out.println("Selectati o cursa.");
        }

    }

    @FXML
    void userLogout(){
        service.logout(user,this);
        Stage stage=(Stage) anchor.getScene().getWindow();
        stage.close();
    }
}
