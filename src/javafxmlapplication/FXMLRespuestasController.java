/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Answer;
import model.NavDAOException;
import model.Navigation;
import model.Problem;
import model.Session;
import model.User;
import model.sub.SqliteConnection;

/**
 * FXML Controller class
 *
 * @author usole
 */
public class FXMLRespuestasController implements Initializable {

    @FXML
    private Label labelP;
    @FXML
    private Button buttonA;
    @FXML
    private Button buttonB;
    @FXML
    private Button buttonC;
    @FXML
    private Button buttonD;
    
    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;
    
    private Navigation nav;
    private User user;
    private Problem problema;
    private int indice;
    private List<Problem> list;
    private List<Answer> listAnswers;
    @FXML
    private Label labelA;
    private int hits;
    private int faults;
    private Stage sTrabajo;
    
    
    public void initUser(String u, String e,String p, Image a,LocalDate dt ){
            nick = u;
            email = e;
            pass = p;
            avatar = a;
            birthday = dt;        
        }
    
    public void initProblema(Problem p, Integer i){
        problema = p;
        indice = i;
        
        String res = problema.getText();
        labelP.setText(res);
        labelP.setWrapText(true);
        listAnswers = problema.getAnswers();
        buttonA.setText(listAnswers.get(0).getText());
        buttonB.setText(listAnswers.get(1).getText());
        buttonC.setText(listAnswers.get(2).getText());
        buttonD.setText(listAnswers.get(3).getText());
    }
    
    public void initSesion(Integer h, Integer f){
        hits = h;
        faults = f;
    }
    
    
    
    
    public void alertaCorrecto() throws IOException{
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Respuesta");
        alerta.setHeaderText("Su respuesta es correcta!");
        alerta.setContentText("Click en aceptar para avanzar");
        

        // Mostrar la ventana y esperar que el usuario la cierre
        
        
        alerta.showAndWait();
        
        //sTrabajo.hide();
        
        if(!alerta.isShowing()){
            buttonA.getScene().getWindow().hide();

            FXMLLoader loader = new  FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,650,500);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
            stage.setScene(scene);
            stage.setTitle("Inicio");
            stage.initModality(Modality.APPLICATION_MODAL);
            InicioController controlador1= loader.getController();
            controlador1.initUser(nick, email, pass, avatar, birthday);
            
            stage.show();
        }
    }
    
    public void alertaIncorrecto() throws IOException{
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Respuesta");
        alerta.setHeaderText("Su respuesta es incorrecta");
        alerta.setContentText("Click en aceptar para avanzar");

        // Mostrar la ventana y esperar que el usuario la cierre
        
        
        alerta.showAndWait();
        //buttonA.getScene().getWindow().hide();
        
        
        
        
        
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("C:data.db");
            System.out.println("Base de datos encontrada");
            
        } catch (SQLException ex) {
            Logger.getLogger(ElegirProblemaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            nav = Navigation.getInstance();
            
        } catch (NavDAOException ex) {
            Logger.getLogger(FXMLRespuestasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        user = nav.authenticate(nick, pass);
        
        //list = nav.getProblems();
        //problema = list.get(indice);
        //String res = problema.getText();
        //labelP.setText(res);
        System.out.println("Inicial aciertos: "+ hits + "Inicial fallos: " + faults);
        
        
        
    }    

    @FXML
    private void buttonAOnAction(ActionEvent event) throws IOException {
        
        Answer res = listAnswers.get(0);
        System.out.println(res.getText());
        if(res.getValidity()){
            alertaCorrecto();
            hits++;
        }else{
            alertaIncorrecto();
            faults++;
        }
        System.out.println("aciertos: "+ hits + "fallos: " + faults);
    }

    @FXML
    private void buttonBOnAction(ActionEvent event) throws IOException {
        
        Answer res = listAnswers.get(1);
        System.out.println(res.getText());
        if(res.getValidity()){
            alertaCorrecto();
            hits++;
        }else{
            alertaIncorrecto();
            faults++;
        }
        System.out.println("aciertos: "+ hits + "fallos: " + faults);
    }

    @FXML
    private void buttonCOnAction(ActionEvent event) throws IOException {
        
        Answer res = listAnswers.get(2);
        System.out.println(res.getText());
        if(res.getValidity()){
            alertaCorrecto();
            hits++;
            
        }else{
            alertaIncorrecto();
            faults++;
        }
        System.out.println("aciertos: "+ hits + "fallos: " + faults);
    }

    @FXML
    private void buttonDOnAction(ActionEvent event) throws IOException {
        
        Answer res = listAnswers.get(3);
        System.out.println(res.getText());
        if(res.getValidity()){
            alertaCorrecto();
            hits++;
        }else{
            alertaIncorrecto();
            faults++;
        }
        System.out.println("aciertos: "+ hits + "fallos: " + faults);
    }
    
}
