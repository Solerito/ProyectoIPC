/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author usole
 */
public class Ventana_autenticacionController implements Initializable {

    @FXML
    private TextField email;
    @FXML
    private Label emailError;
    @FXML
    private TextField rpassword;
    @FXML
    private Label emailError311;
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) {
    }

    @FXML
    private void handleBCanceltOnAction(ActionEvent event) {
    }
    
}
