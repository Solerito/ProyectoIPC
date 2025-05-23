package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import model.NavDAOException;
import model.Navigation;
import model.Problem;
import model.User;
import model.sub.SqliteConnection;
import poiupv.TrabajoController;



public class ElegirProblemaController implements Initializable {

    @FXML
    private Button botonHacerEjercicio;

    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;
    @FXML
    private Button botonInicio;
    @FXML
    private Label labelProblemas;
    @FXML
    private Button botonModoAleatorio;
    @FXML
    private ListView<String> listaProblemas;
    @FXML
    private MenuButton mbPerfil;
    @FXML
    private MenuItem miEditarPerfil;
    @FXML
    private MenuItem miCerrarSesion;
    @FXML
    private MenuItem miCerrarAplicacion;
    
    private List<Problem> list;
    private ObservableList<Problem> ol;
    private Navigation nav;
    private User user;
    
    private Problem selectedProblem;
    @FXML
    private ImageView ivPerfil;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            nav = Navigation.getInstance();
            user = nav.authenticate(nick, pass);
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("C:data.db");
            System.out.println("Base de datos encontrada");
            
        } catch (NavDAOException ex) {
            Logger.getLogger(ElegirProblemaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ElegirProblemaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        list = nav.getProblems();
        ol = FXCollections.observableList(list);
        
        listaProblemas.getItems().addAll("Problema 1", "Problema 2", "Problema 3", "Problema 4", "Problema 5", "Problema 6", "Problema 7", "Problema 8", "Problema 9","Problema 10", "Problema 11", "Problema 12", "Problema 13", "Problema 14", "Problema 15", "Problema 16", "Problema 17", "Problema 18");
        //ivPerfil.imageProperty().setValue(avatar);
        botonHacerEjercicio.setDisable(true);
        listaProblemas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Si hay un elemento seleccionado, habilitamos el botón, sino lo deshabilitamos
            botonHacerEjercicio.setDisable(newSelection == null);
        });
        
        
        
    }
    
    
    public void initUser(String nick, String email, String pass, Image avatar, LocalDate birthday) {
        this.nick = nick;
        this.email = email;
        this.pass = pass;
        this.avatar = avatar;
        this.birthday = birthday;
    }

    @FXML
    private void realizarEjercicioButtonOnAction(ActionEvent event) throws IOException {
        if (!listaProblemas.getSelectionModel().isEmpty()) {
            int sel = listaProblemas.getSelectionModel().getSelectedIndex();
            selectedProblem = ol.get(sel);
        
        // Carga la siguiente ventana (FXMLTrabajo)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLTrabajo.fxml"));
        Parent root = loader.load();

        if (root == null) {
            System.out.println("Error al cargar el FXML: FXMLTrabajo.fxml");
        }
        
        FXMLTrabajoController controlador = loader.getController();
        if (controlador == null) {
            System.out.println("El controlador de FXMLTrabajo no se ha inicializado correctamente");
        }
        controlador.setProblem(selectedProblem);  // Pasa el problema seleccionado

        // Muestra la nueva ventana
        Scene scene = new Scene(root, 900, 700);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Realizar Problema");
        stage.show();

        // Opcionalmente oculta la ventana actual (si lo deseas)
        ((Stage)((Node) event.getSource()).getScene().getWindow()).hide();
    }
}

    @FXML
    private void onCerrarSesion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaCerrarSesion.fxml"));
        Parent root = loader.load();
        
        CerrarSesionController ctrl = loader.getController();
        // Pasamos el Stage principal (ventana que queremos cerrar)
        Stage mainStage = (Stage) mbPerfil.getScene().getWindow();
        
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Cerrar Sesión");
        stage.initModality(Modality.APPLICATION_MODAL);
        
        stage.show();
        
    }



    @FXML
    private void onInicioButtonAction(ActionEvent event) throws IOException {
        // Carga tu FXML de inicio
       
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/vista/Inicio.fxml")  // ajusta ruta si es distinta
        );
        Parent root = loader.load();

        // Obtienes la ventana actual y cambias la escena
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));  // usa el tamaño que quieras
        stage.setTitle("Pantalla de Inicio");
        stage.show();
    }

    @FXML
    
        private void onEditarPerfil(ActionEvent event) throws IOException {
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
                Image res = user.getAvatar();
                ivPerfil.setImage(res);
                
            }
}

    @FXML
    private void onCerrarAplicacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaCerrarAplicacion.fxml"));
        Parent root = loader.load();
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Cerrar Aplicación");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
private void botonModoAleatorioOnAction(ActionEvent event) {
    if (list != null && !list.isEmpty()) {
        int indiceAleatorio = (int) (Math.random() * list.size());
        // Selecciona el problema aleatorio en el ListView
        listaProblemas.getSelectionModel().select(indiceAleatorio);
        // Opcional: hacer que ese problema seleccionado se enfoque/visible en la lista
        listaProblemas.scrollTo(indiceAleatorio);
        int sel = listaProblemas.getSelectionModel().getSelectedIndex();
        selectedProblem = ol.get(sel);
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING, "No hay problemas disponibles para seleccionar.");
        alert.showAndWait();
    }
}

}




    
    


