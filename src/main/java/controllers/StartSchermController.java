
package controllers;


import domain.DomainController;
import domain.Leerling;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class StartSchermController implements Initializable {
    private ArrayList<Leerling> listLeerlingen; 
    private DomainController dc; 
    
    @FXML
    private ListView<String> listViewLeerlingen = new ListView<>();
    
    @FXML
    private  Node startScherm ;
    
    @FXML
    private Label lblMessage; 
    
    @FXML
    private  TextField txtNaam ;
    
    @FXML
    private TextField txtNummer; 
    
    @FXML 
    private Button afsluiten ;
    
    private int alert = 2 ; 
    
    @FXML 
    private void handleButtonAfsluiten(ActionEvent event) {
        if (alert == 3 ){
            Stage currentStage = new Stage();
            currentStage = (Stage) startScherm.getScene().getWindow();
            currentStage.close();
        }
        else {
            alert +=1 ; 
            afsluiten.setStyle("-fx-background-color: Red;");
        }
    }
    
    private BackendController bc = new BackendController(); 
    @FXML
    private void handleButtonZoeken(ActionEvent event) {
        lblMessage.setVisible(true);
        if(listViewLeerlingen.getSelectionModel().getSelectedItem() != null){
            for (Leerling leerling : dc.getLeerlingen()) {
                if(listViewLeerlingen.getSelectionModel().getSelectedItem().equalsIgnoreCase(leerling.getnaam())){
                    bc.addLeerling(leerling);
                }
            }
            
            lblMessage.setText("De leerling is online opgeslagen");
        }
        else {
            lblMessage.setVisible(true);
            lblMessage.setText("gelieve een leerling te selecteren in de rechter kader");
        }
        
    }
    
    @FXML
    private void handleButtonSync(ActionEvent event) {
        lblMessage.setVisible(true);
        bc.updateList();
        dc.setLeerlingen(bc.getLeerlingList());
        refreshList();
        lblMessage.setText("leerlingen zijn gesynchroniseerd");
    }
    
    @FXML
    private void handleButtonOpenEva(ActionEvent event) throws IOException {
        if(listViewLeerlingen.getSelectionModel().getSelectedItem() != null){
            for (Leerling leerling : dc.getLeerlingen()) {
                if(listViewLeerlingen.getSelectionModel().getSelectedItem().equalsIgnoreCase(leerling.getnaam())){
                    dc.setHuidigeLeerling(leerling);
                }
            }
            
            openHoofdScherm();
        }
        else {
            lblMessage.setVisible(true);
            lblMessage.setText("gelieve een leerling te selecteren in de rechter kader");
        }
    }
    
    @FXML
    private void handleButtonNieuw(ActionEvent event) {
        boolean leerlingBestaatAl = true; 
       
        lblMessage.setTextFill(Color.web("#e42d2d"));   
        
        lblMessage.setVisible(true);
        if (txtNaam.getText().isEmpty() == true   && txtNummer.getText().isEmpty() == true ){
            lblMessage.setText("Gelieve een naam en een nummer in te geven!");
        }
        else if(txtNaam.getText().isEmpty() == true){
            lblMessage.setText("gelieve een naam in te geven!");
        }
        else if(txtNummer.getText().isEmpty() == true){
            lblMessage.setText("gelieve een nummer in te geven!");
        }
        
        else {
            for (Leerling l : dc.getLeerlingen()) {
                if( !l.getNummer().equals(txtNummer.getText())){
                    leerlingBestaatAl = false ; 
                } 
                else {
                    leerlingBestaatAl = true ;
                    lblMessage.setText("Leerling bestaat al");
                }
            }
            
            if (leerlingBestaatAl == false){
                dc.getLeerlingen().add(new Leerling(txtNummer.getText(), txtNaam.getText()));
                lblMessage.setTextFill(Color.web("#006400"));
                lblMessage.setText("Leerling aangemaakt");
            
            }
            
           
        }
        
        refreshList();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {  
        startScherm.setStyle("-fx-background-image: url(\"/image/achtergrond.jpg\"); -fx-background-position: center center; ");
        listViewLeerlingen.setStyle("-fx-font: 18pt \"Arial\";");
        if(dc == null){
            dc = new DomainController(); 
            dc.generateData();
        }
        
        refreshList();
       
    }
    
    private void openHoofdScherm() throws IOException{
        
        Stage currentStage = new Stage();
        currentStage = (Stage) startScherm.getScene().getWindow();
        
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/HoofdScherm.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Evaluatie scherm");
        
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root1, 1024, 743);
        
        stage.setScene(scene);
        // DC meegeven aan de volgende controller 
        HoofdSchermController controller = fxmlLoader.<HoofdSchermController>getController();
        controller.initData(dc, false, 0);
        
        stage.show();
        currentStage.close();
        
    }

    public void refreshList(){
        ArrayList<String> listLeerlingenNamen = new ArrayList<>() ; 
        for (Leerling leerling : dc.getLeerlingen()) {
            listLeerlingenNamen.add(leerling.toString());
        }
               
        ObservableList<String> olLeerlingen = FXCollections.observableArrayList(listLeerlingenNamen);
        listViewLeerlingen.setItems(olLeerlingen);
    }
    
    public void initData(DomainController dc) {
        this.dc = dc ;
        refreshList();
    }
}
