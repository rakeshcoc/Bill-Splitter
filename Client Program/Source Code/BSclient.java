package bsclient;

import java.net.InetAddress;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class BSclient extends Application {
    
    static int clientid;
    static String nm="me";
    static InetAddress serverip;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        try{
        FXMLDocumentController.GetServerIP();
        FXMLDocumentController.LoginDailog();
        }catch(Exception e){e.printStackTrace();}
        stage.show();
    }

    @Override
    public void stop(){
      SendRecv.mySocket.close();
     // System.out.println("Closing Program");
      java.lang.System.exit(0);
    }
    
    
    public static void main(String[] args) {
        SendRecv.InitMulticast();
        launch(args);
       // System.out.println("system ready");
    }

}