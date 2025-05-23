/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
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
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.Session;
import model.User;
import model.sub.SqliteConnection;

/**
 * FXML Controller class
 *
 * @author usole
 */
public class InicioController implements Initializable {

    @FXML
    private Button estadisticasButton;
    
    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;
    
    private Session session;
    
    public void initUser(String u, String e,String p, Image a,LocalDate dt ){
            nick = u;
            email = e;
            pass = p;
            avatar = a;
            birthday = dt;        
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
            System.out.println("Base de datos no encontrada");
        }
        
    }    

    @FXML
    private void realizarproblemaButtonOnAction(ActionEvent event) throws IOException, NavDAOException {
        Navigation nav = Navigation.getInstance();
        User res = nav.authenticate(nick, pass);
        
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/ElegirProblema.fxml"));
        Parent root = loader.load();
        ElegirProblemaController controlador2= loader.getController();
        controlador2.initUser(nick, email, pass, avatar, birthday);
        Scene scene = new Scene(root,900,500);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        stage.setScene(scene);
        stage.setTitle("Aplicación");
        stage.initModality(Modality.APPLICATION_MODAL);
        ((Stage)((Node)event.getSource()).getScene().getWindow()).hide();
        stage.show();
    }

    @FXML
    private void estadisticasButtonOnAction(ActionEvent event) throws IOException {
      if(session != null){
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("/vista/Estadisticas.fxml"));
        Parent root = loader.load();
        EstadisticasController controlador2= loader.getController();
        controlador2.initUser(nick, email, pass, avatar, birthday);
        //controlador2.initSession(session);
        
        Scene scene = new Scene(root,650,500);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        stage.setScene(scene);
        stage.setTitle("Estadísticas");
        stage.initModality(Modality.APPLICATION_MODAL);
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        stage.showAndWait();
        
        
      }else{
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Advertencia");
        alerta.setHeaderText("¡Error!");
        alerta.setContentText("Debes realizar un problema antes de ver las estadisticas de la Sesión");
        alerta.show();
      }
    }

    @FXML
    private void perfilButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PerfilPanel.fxml"));
            Parent root = loader.load();
            Stage dialog = new Stage();
            dialog.setTitle("Editar perfil");
            
            dialog.initModality(Modality.APPLICATION_MODAL);
            PerfilPanelController controlador2= loader.getController();
            controlador2.initUser(nick, email, pass, avatar, birthday);
            controlador2.mostrarinfo(nick, email, pass, avatar, birthday);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
            
            
            if(controlador2.pulsadoGuardar()){
                User user = controlador2.getUser();
                //Image res = user.getAvatar();
                //ivPerfil.setImage(res);
                
            }
    }
    
}
