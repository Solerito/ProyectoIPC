/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author usole
 */

////CLASE CONTROLADORA DE VENTANAAUTENTICACION .FXML

public class FXMLRegisterController implements Initializable {

    @FXML
    private Label emailError;
    @FXML
    private Button RegistrarseButton;
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private TextField usuarioField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //BooleanBinding validFields = Bindings.and(emailField,passwordField);
        //bAccept.disableProperty().bind(Bindings.not(validFields));
    }    

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("/vista/FXMLDocument.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root,800,500);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        stage.setScene(scene);
        stage.setTitle("Aplicación");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        
    }

    @FXML
    private void handleBCanceltOnAction(ActionEvent event) {
        usuarioField.getScene().getWindow().hide();
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
    
}
