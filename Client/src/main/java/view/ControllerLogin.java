package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.*;
import repository.*;
import server.ServerImpl;
import services.IServer;
import services.ValidationExc;


import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class ControllerLogin  {

    private IServer service;
    private CurseController cursectrl;

    @FXML
    private Button btnlogin;

    @FXML
    private TextField txtuser;
    @FXML
    private PasswordField pass;

    @FXML
    private AnchorPane loginanch;
    Parent parent;

    public void setServer(IServer s){
        service=s;
    }
    public void setCursectrl(CurseController cursectrl){this.cursectrl=cursectrl;}
    public void setParent(Parent p){parent=p;}



    @FXML
    void userLogin() {

        String userid=txtuser.getText();
        String password=pass.getText();

        try{
            User user=null;//new User(userid,"",password);
           user=service.getUser(userid);
           if(user.equals(null)){
                Alert alert= new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Eroare login.");
                alert.setContentText("Nu exista utilizatorul.");
            }
            else{
            service.login(user,cursectrl);
                Stage stage= new Stage();
                Scene scene= new Scene(parent);
                stage.setScene(scene);
                stage.show();
                cursectrl.setService(service);
               cursectrl.setUser(user);
                txtuser.setText("");
                pass.setText("");



            }
        }
        catch(ValidationExc ex){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Eroare la login.");
            alert.setContentText("Id sau parola incorecte.");
        }





    }







    private BorderPane initStage(User user){
        try {


            // setProperties();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerLogin.class.getResource("/mainfxml.fxml"));
            AnchorPane centerLayout = (AnchorPane) loader.load();
            //String user=txtuser.getText();
            CurseController viewCtrl=loader.getController();
            viewCtrl.setService(service);

           // service.addObserver(viewCtrl);
            txtuser.setText("");
            pass.cut();
            BorderPane bp= new BorderPane();
            bp.setCenter(centerLayout);
            return bp;
            //loginanch.getChildren().setAll(centerLayout);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }









}
