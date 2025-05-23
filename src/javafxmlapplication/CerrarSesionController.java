/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 * @author usole
 */
public class CerrarSesionController implements Initializable {

    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    
    private Session ses;
    private int hits;
    private int faults;
    
    private String nick;
    private String pass;
    private String email;
    private Image avatar;
    private LocalDateTime birthday;
    
    private User user;
    private Navigation nav;
    
    public void initUser(String u, String e,String p, Image a,LocalDateTime dt ){
            nick = u;
            email = e;
            pass = p;
            avatar = a;
            birthday = dt;        
        }
    
    public void getSesion(int h, int f){
    
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nav = Navigation.getInstance();
            user = nav.authenticate(nick,pass);
        } catch (NavDAOException ex) {
            Logger.getLogger(CerrarSesionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws Exception {
        //user.addSession(hits, faults,LocalDateTime.now());
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        poiupv.PoiUPVApp.reiniciarApp();
    }

    @FXML
    private void handleBCanceltOnAction(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
    
}
