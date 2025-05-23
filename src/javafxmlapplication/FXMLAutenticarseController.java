/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.*;
import model.sub.SqliteConnection;

/**
 * FXML Controller class
 *
 * @author usole
 */

////CLASE CONTROLADORA DE VENTANAAUTENTICACION .FXML

public class FXMLAutenticarseController implements Initializable {

    @FXML
    private Label emailError;
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private TextField usuarioField;
    @FXML
    private Button RegistrarseButton;
    @FXML
    private Button contrseñaButton;
    
    private boolean passwordVisible = false;
    
    
        

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //BooleanBinding validFields = Bindings.and(emailField,passwordField);
        //bAccept.disableProperty().bind(Bindings.not(validFields));
        try {
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("C:data.db");
            System.out.println("Base de datos encontrada");
            try {
                Navigation nav = Navigation.getInstance();
                
            } catch (NavDAOException ex) {
                System.out.println("hola");

            }
            

        } catch (SQLException ex) {
            System.out.println("Base de datos no encontrada");
        }
    }    

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws IOException, NavDAOException {
        String nick = usuarioField.getText();
        String pass = passwordField.getText();
        Navigation nav = Navigation.getInstance();
        int aciertos = 0;
        int fallos = 0;
        
        if(nav.exitsNickName(nick)){
            
                
                User res = nav.authenticate(nick, pass);
                Session ses = new Session(LocalDateTime.now(),aciertos,fallos);
                
                
                
                if(res.chekCredentials(nick, pass)){
                FXMLLoader loader = new  FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root,650,500);
                Stage stage = new Stage();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
                stage.setScene(scene);
                stage.setTitle("Inicio");
                stage.initModality(Modality.APPLICATION_MODAL);
                InicioController controlador1= loader.getController();
                controlador1.initUser(res.getNickName(), res.getEmail(), res.getPassword(), res.getAvatar(), res.getBirthdate());
                
                
                
                
                ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
                stage.show();
        
            }
        }
            
        
        
    }

    @FXML
    private void handleBCanceltOnAction(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).hide();
    }

    @FXML
    private void RegistrarseButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("/vista/VentanaRegistro.fxml"));
        Parent root =loader.load();
        Scene scene = new Scene(root,800,500);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        stage.setScene(scene);
        stage.setTitle("Registrarse");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void contrseñaButtonOnAction(ActionEvent event) {
        if (passwordVisible) {
            // Ocultar contraseña
            passwordField.setStyle("-fx-echo-char: '*';");
            contrseñaButton.setText("Mostrar contraseña");
            passwordVisible = false;
        } else {
            // Mostrar contraseña
            passwordField.setStyle("-fx-echo-char: 0;");
            contrseñaButton.setText("Ocultar contraseña");
            passwordVisible = true;
        }
}

    
}
