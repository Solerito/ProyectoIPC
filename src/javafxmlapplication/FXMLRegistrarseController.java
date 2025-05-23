/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.YEARS;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.util.converter.LocalDateStringConverter;
import jdk.jfr.FlightRecorder;
import static model.Navigation.getInstance;
import model.User;
import model.Navigation;
import model.NavDAOException;
import model.sub.SqliteConnection;

/**
 * FXML Controller class
 *
 * @author usole
 */

////CLASE CONTROLADORA DE VENTANAREGISTRO.FXML

public class FXMLRegistrarseController implements Initializable {

    @FXML
    private Label emailError;
    
    @FXML
    private Button bAccept;
    @FXML
    private Button bCancel;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private DatePicker dateField;
    @FXML
    private Label dateError;

    private BooleanProperty validEmail;
    private BooleanProperty validPassword;
    private BooleanProperty validDate;
    private BooleanProperty validUser;


    
    private ChangeListener<String> listenerEmail;
    private ChangeListener<String> listenerPassword;
    private ChangeListener<LocalDate> listenerDate;
    private ChangeListener<String> listenerUser;
    
    private Image avatar;
    
    @FXML
    private TextField usuarioField;
    @FXML
    private Label usuarioError;
    @FXML
    private Button PredAvatarButton;
    @FXML
    private Button subirAvatarButton;
    
    //private Image setAvatar(){
        
    //}
    
    private void checkUser() throws NavDAOException{
        String user = usuarioField.getText();
        Navigation nav = Navigation.getInstance();
        boolean isValid = nav.exitsNickName(user);
        validUser.set(isValid);
        showError(!isValid, usuarioField, usuarioError);
    }

    private void checkEmail() {
        String email = emailField.getText();
        boolean isValid = email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        validEmail.set(isValid); //actualiza la property asociada
        showError(isValid, emailField, emailError); //muestra o esconde el mensaje de error
    }
    
    private void checkPassword(){
        String password = passwordField.getText();
        boolean isValid = password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,15}$");
        validPassword.set(isValid);
        showError(isValid,passwordField,passwordError);
    }
    
    private void checkDate(){
        LocalDate value = dateField.getValue();
        boolean isValid = value.isBefore(LocalDate.now().minus(16,YEARS));
        validDate.set(isValid);
        showError(isValid,dateField,dateError);
    }
    
    private void showError(boolean isValid, Node field, Node  errorMessage){
        errorMessage.setVisible(!isValid);
        field.setStyle(((isValid) ? "" : "-fx-background-color: #FCE5E0"));
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
        
        
        validUser = new SimpleBooleanProperty();
        validUser.setValue(Boolean.FALSE);
    
        //When the field loses focus, the field is validated. 
        usuarioField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                try{checkUser();
                if (validUser.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerUser != null) {
                        listenerUser = (a, b, c) -> {
                            try {checkUser();} catch (NavDAOException ex){}
                        };
                        usuarioField.textProperty().addListener(listenerUser);
                    }
                }}catch(NavDAOException e){}
            }
        });
        

        validEmail = new SimpleBooleanProperty();
        validEmail.setValue(Boolean.FALSE);

        //When the field loses focus, the field is validated. 
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkEmail();
                if (!validEmail.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerEmail == null) {
                        listenerEmail = (a, b, c) -> checkEmail();
                        emailField.textProperty().addListener(listenerEmail);
                    }
                }
            }
        });
        validPassword = new SimpleBooleanProperty();
        validPassword.setValue(Boolean.FALSE);

        //When the field loses focus, the field is validated. 
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkPassword();
                if (!validPassword.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerPassword == null) {
                        listenerPassword = (a, b, c) -> checkPassword();
                        passwordField.textProperty().addListener(listenerPassword);
                    }
                }
            }
        });
        
        validDate = new SimpleBooleanProperty();
        validDate.setValue(Boolean.FALSE);

        //When the field loses focus, the field is validated. 
        dateField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkDate();
                if (!validDate.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    //dateField.setValue(LocalDate.MIN);
                    if (listenerDate == null) {
                        listenerDate = (a, b, c) -> checkDate();
                        dateField.valueProperty().addListener(listenerDate);
                    }
                }
            }
        });
        
        LocalDateStringConverter localDateStringConverter = new LocalDateStringConverter() {
        
            @Override
            public LocalDate fromString(String value) {
                try {
                    return super.fromString(value);
                } catch (Exception e) {
                    System.out.println("Exception in fromString");
                    return LocalDate.now();
                }
            }
        
            @Override
            public String toString(LocalDate value) {
                return super.toString(value);
            }
        };
        dateField.setConverter(localDateStringConverter);
        
        BooleanBinding validFields = Bindings.and(validEmail, validPassword).and(validDate).and(validUser.not());
        
        bAccept.disableProperty().bind(Bindings.not(validFields));
 
        
        
        
        
    
    }    

    @FXML
    private void handleBAcceptOnAction(ActionEvent event) throws IOException, NavDAOException {
        
        String user = usuarioField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        LocalDate birthdate = dateField.getValue();
        
        
        Navigation nav = Navigation.getInstance();
        User res = nav.registerUser(user, email, password, avatar, birthdate);
        System.out.println("Usuario Registrado");
            
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        
    }

    @FXML
    private void handleBCanceltOnAction(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();

    }

    @FXML
    private void subirAvatar(ActionEvent event) throws IOException{
        
        
        FileChooser fc = new FileChooser();
        fc.setTitle("Abrir Fichero");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("Imágenes","*.png","*.jpg"),
                new ExtensionFilter("Todos","*.*"));
        File fichero = fc.showOpenDialog(
                ((Node)event.getSource()).getScene().getWindow()
        );
        
        //String path = fichero.getCanonicalPath();
        //System.out.println(path);
        //avatar = new Image(getClass().getResource(path).toExternalForm());
        
        
        if(fichero != null){
            String ruta = fichero.toURI().toString();  
            System.out.println("Ruta URI: " + ruta);
            avatar = new Image(ruta);
            System.out.println("avatar modificado");

            
        }
    }

    @FXML
    private void predAvatarAction(ActionEvent event) {
        avatar = new Image(getClass().getResourceAsStream("/resources/default.png"));
    }

    
    

    
    
}
