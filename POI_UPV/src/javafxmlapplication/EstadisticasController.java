/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author usole
 */
public class EstadisticasController implements Initializable {

    @FXML
    private ToggleGroup Grupotiempo;
    @FXML
    private ToggleGroup Grupodificultad;
    @FXML
    private ToggleGroup Grupotematica;
    @FXML
    private Label labelProblemas;
    @FXML
    private Label labelPorAciertos;
    @FXML
    private Label labelNumAciertos;
    @FXML
    private Label labelPorFallos;
    @FXML
    private Label labelNumFallos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
