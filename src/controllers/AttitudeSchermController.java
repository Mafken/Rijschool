package controllers;

import domain.DomainController;
import domain.Leerling;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class AttitudeSchermController implements Initializable {
    
    private DomainController dc = new DomainController(); 
    private ArrayList<String> listVoorbeeldwaarden;
    private ArrayList<String> listEigenschappen;
    
    @FXML
    private Node attitudeScherm ;
    
    @FXML 
    private Label naam; 
    @FXML
    private Button terugknop;
    
    @FXML
    private ListView listViewVoorbeeldwaarden;
    
    @FXML
    private ListView listViewEigenschappen;
    
    @FXML
    private Button btnVoegVoorbeeldWaardenToe;
    
    @FXML
    private Button btnVerwijderWaarden;
    
    @FXML
    private TextField txtEigenschap;
    
    @FXML
    private Button btnEigenAttribuut;
    
    @FXML
    public void handleButtonTerugknop(ActionEvent event) throws IOException {
        keerTerug();        
    }
    
    @FXML
    public void handleVoegVoorbeeldwaardenToe(ActionEvent event){
        List<String> checkList = new ArrayList<>();
        checkList.addAll(listViewVoorbeeldwaarden.getSelectionModel().getSelectedItems());
        for(String s : checkList){
            if(!listEigenschappen.contains(s)){
                listEigenschappen.add(s);
            }
        }
        refreshLists();
    }
    
    @FXML
    public void handleVerwijderWaarden(ActionEvent event){
        listEigenschappen.removeAll(listViewEigenschappen.getSelectionModel().getSelectedItems());
        refreshLists();
    }
    
    @FXML
    public void handleVoegEigenAttribuutToe(ActionEvent event){
        if(!listEigenschappen.contains(txtEigenschap.getText())){
            listEigenschappen.add(txtEigenschap.getText());
        }
        refreshLists();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        Image imgTerugknop = new Image(getClass().getResourceAsStream("/image/Terugknop.png"));
        terugknop.setGraphic(new ImageView(imgTerugknop));
        
        listVoorbeeldwaarden = new ArrayList<>();  
        listVoorbeeldwaarden.add("Rustig");
        listVoorbeeldwaarden.add("Nonchalant");
        listVoorbeeldwaarden.add("Aggresief");
        listEigenschappen = new ArrayList<>(); 
        listViewVoorbeeldwaarden.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        refreshLists();
    }
 
    public void refreshLists(){
        ObservableList<String> olVoorbeelden = FXCollections.observableArrayList(listVoorbeeldwaarden);
        listViewVoorbeeldwaarden.setItems(olVoorbeelden);
        
        ObservableList<String> olEigenschappen = FXCollections.observableArrayList(listEigenschappen);
        listViewEigenschappen.setItems(olEigenschappen);
    }

    public void keerTerug() throws IOException{
        Stage currentStage = (Stage) attitudeScherm.getScene().getWindow();
        currentStage.close();
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/HoofdScherm.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Hoofdscherm");
        stage.setScene(new Scene(root1));
        
        // DC meegeven aan de volgende controller 
        HoofdSchermController controller = fxmlLoader.<HoofdSchermController>getController();
        controller.initData(dc);
        
        stage.show();
    }

    void initData(DomainController dc) {
        this.dc = dc ; 
        naam.setText(dc.getHuidigeLeerling().getnaam());
    }

    
    
}