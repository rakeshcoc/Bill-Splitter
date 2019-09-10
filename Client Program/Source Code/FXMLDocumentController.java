package bsclient;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class FXMLDocumentController implements Initializable {
  
    @FXML
    private TextArea msgarea;
    @FXML
    private Label msgdisplay;
    @FXML
    private TextField purpose;
    @FXML
    private TextField expenditures;
    @FXML
    public TableView<?> exptable;
    @FXML
    private TableColumn<?,?> tpp;
    @FXML
    private TableColumn<?, ?> texp;     
    @FXML
    private TableColumn<?, ?> tpb;
    @FXML
    private Button split;
    @FXML
    private Button Send;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            
        SendRecv.data = FXCollections.observableArrayList();
	exptable.setItems(SendRecv.data);
        tpp.setCellValueFactory(new PropertyValueFactory("purpose"));
        texp.setCellValueFactory(new PropertyValueFactory("exp"));
        tpb.setCellValueFactory(new PropertyValueFactory("pbname"));
                
    }    

    @FXML
    public void onSelectTable() {

    if (exptable.getSelectionModel().getSelectedItem() != null) {
        expense e = (expense) exptable.getSelectionModel().getSelectedItem();
        msgdisplay.setText(e.message);
    }
    }
    
    @FXML
    private void sendmsg(ActionEvent event) {
        expense newexp=new expense();
        newexp.message=msgarea.getText();
        newexp.purpose=purpose.getText();
        String checkint=expenditures.getText();
        
        if(newexp.purpose.isEmpty()||checkint.isEmpty())
        {
            DisplayWarning("Invalid Input","You need to Specify a Purpose and Expenditure");
            return;
        }
        try{
        newexp.exp=Integer.parseInt(checkint);
        if(newexp.exp<=0)
            throw new NumberFormatException();
        }catch(NumberFormatException e){
            DisplayWarning("Invalid Input","Please Enter a Valid Non Zero Expenditure");
            return;
        }
        newexp.paidby=BSclient.clientid;

        newexp.pbname=BSclient.nm;
        msgarea.clear();
        purpose.clear();
        expenditures.clear();
        SendRecv.sendtogroup(newexp);
        //System.out.println(newexp.message);
        msgdisplay.setText("Message Sent");
    }

    @FXML
    private void stopsend(ActionEvent event) { 
       expense newexp=new expense();
       newexp.purpose="stop";
       newexp.paidby=BSclient.clientid;
       newexp.exp=0;
       SendRecv.sendtogroup(newexp);
       Send.setDisable(true);
       msgdisplay.setText("Stopped sending but not receive");
       SendRecv.recieveresults();
    }
    
    public void updateTable(expense store){
        SendRecv.data.add(store);
    }

    public static void Displaymsg(String title,String text) {
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
    
    public static void DisplayWarning(String WType,String Winfo)
    {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText(WType);
        alert.setContentText(Winfo);
        alert.showAndWait();
    }
    
    public static void GetServerIP()
    {
        TextInputDialog dialog = new TextInputDialog("Server IP");
        dialog.setTitle("Server IP");
        dialog.setHeaderText("Server IP Address");
        dialog.setContentText("Please enter the Server IP:");

 
        Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
            try {
                BSclient.serverip=InetAddress.getByName(result.get());
            } catch (UnknownHostException ex) {
                FXMLDocumentController.DisplayWarning("Wrong IP", "IP address not incorrect please enter in\nproper format (xxx.xxx.xxx.xxx)");
                System.exit(0);
            }
            }
    }
    
    public static void LoginDailog()
    {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Logi"
                + "n Dialog");
        dialog.setHeaderText("Enter YOUR NAME and GROUP PASSWORD");

        ImageView I=new ImageView(new BSclient().getClass().getResource("user_login.png").toString());
        dialog.setGraphic(I);

        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");


        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);


        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);


        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();


        if(result.isPresent()){
      //  System.out.println("user : "+result.get().getKey()+" Pass: "+result.get().getValue());
        BSclient.nm=result.get().getKey();
        SendRecv.connectToServer(result.get().getKey(),result.get().getValue());     
        }
        else{
        java.lang.System.exit(0);
        }

    }
    
    
}
