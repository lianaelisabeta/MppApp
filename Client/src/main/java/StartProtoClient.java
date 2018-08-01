

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import protobufprotocol.ProtoServerProxy;
import protocol.ServerRpcProxy;
import services.IServer;
import view.ControllerLogin;
import view.CurseController;

import java.io.IOException;
import java.util.Properties;

public class StartProtoClient extends Application {
    Stage primarystage;
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws Exception {

        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("firmatrclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("FirmaTransport.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("FirmaTransport.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServer server = new ProtoServerProxy(serverIP, serverPort);

        primaryStage = primaryStage;
        primaryStage.setTitle("Firma Transport");

        //BorderPane borderPane= initRootLayout();

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("loginfxml.fxml"));
        Parent root=loader.load();
        ControllerLogin ctrlog=loader.getController();
        ctrlog.setServer(server);



        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("mainfxml.fxml"));
        Parent croot=cloader.load();
        CurseController ctrlcurse= cloader.getController();
        //ctrlcurse.setService(server);

        ctrlog.setCursectrl(ctrlcurse);
        ctrlog.setParent(croot);



        //Scene scene = new Scene(initRootLayout(server));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
   /* public BorderPane initRootLayout(IServer server) {
        try {
            //Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StartClient.class.getResource("/loginfxml.fxml"));
            AnchorPane center= loader.load();
            ControllerLogin viewCtrl=loader.getController();
            viewCtrl.setServer(server);
            BorderPane rootLayout = new BorderPane();
            rootLayout.setCenter(center);
            //RootLayoutController rootController=loader.getController();
            return rootLayout;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public BorderPane initStage(IServer server,User user){
        try {


            // setProperties();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerLogin.class.getResource("/mainfxml.fxml"));
            AnchorPane centerLayout = (AnchorPane) loader.load();
            //String user=txtuser.getText();
            CurseController viewCtrl=loader.getController();
            viewCtrl.setService(server);

            // service.addObserver(viewCtrl);

            BorderPane bp= new BorderPane();
            bp.setCenter(centerLayout);
            return bp;
            //loginanch.getChildren().setAll(centerLayout);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

*/


}
