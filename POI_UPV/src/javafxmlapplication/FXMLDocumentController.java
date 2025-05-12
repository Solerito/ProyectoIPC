/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import poiupv.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.NavDAOException;
import model.Navigation;
import model.User;
import poiupv.Poi;
import poiupv.Poi;
import poiupv.Poi;

/**
 *
 * @author jsoler
 */

//CLASE CONTROLADORA DE FXMLDOCUMENT.FXML
public class FXMLDocumentController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    private ObservableList<Poi> data;
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    @FXML
    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private Label mousePosition;
    @FXML
    private Menu menuMarcarPunto;
    @FXML
    private MenuItem menuItemIniciarPunto;
    @FXML
    private MenuItem menuItemMarcaCirculo;
    @FXML
    private MenuItem menuItemMarcaCruz;
    @FXML
    private MenuItem menuItemMarcaAsterisco;
    @FXML
    private MenuItem menuItemMarcaEstrella;
    @FXML
    private ColorPicker colorPickkerPunto;
    @FXML
    private Menu menuTrazarLinea;
    @FXML
    private MenuItem menuItemIniciarLinea;
    @FXML
    private MenuItem menuItemGrosorLineaMuyGrueso;
    @FXML
    private MenuItem menuItemGrosorLineaGrueso;
    @FXML
    private MenuItem menuItemGrosorLineaMedio;
    @FXML
    private MenuItem menuItemGrosorLineaFino;
    @FXML
    private ColorPicker colorPickerLinea;
    @FXML
    private Menu menuTrazarArco;
    @FXML
    private MenuItem menuItemIniciarArco;
    @FXML
    private MenuItem menuItemGrosorArcoMuyGrueso;
    @FXML
    private MenuItem menuItemGrosorArcoGrueso;
    @FXML
    private MenuItem menuItemGrosorArcoMedio;
    @FXML
    private MenuItem menuItemGrosorArcoFino;
    @FXML
    private ColorPicker colorPickerArco;
    @FXML
    private MenuItem menuItemRadioLibre;
    @FXML
    private Slider sliderRadioArco;
    @FXML
    private MenuItem menuItemRadioFijo;
    @FXML
    private TextField textFieldRadioArco;
    @FXML
    private Menu menuAnotarTexto;
    @FXML
    private MenuItem menuItemAnotarTexto;
    @FXML
    private TextField textFieldTamanoTexto;
    @FXML
    private ColorPicker colorPickerTexto;
    @FXML
    private Menu menuColor;
    @FXML
    private CustomMenuItem menuItemEditarColor;
    @FXML
    private ColorPicker colorPickerEdicion;
    @FXML
    private Menu menuEliminarMarca;
    @FXML
    private MenuItem menuItemEliminarMarcaTool;
    @FXML
    private Menu menuLimpiarCarta;
    @FXML
    private MenuItem menuItemLimpiarCartaTool;
    @FXML
    private MenuItem menuItemCompas;
    @FXML
    private MenuItem menuItemTransportador;
    @FXML
    private MenuItem menuItemRegla;
    @FXML
    private Menu menuPerfil;
    @FXML
    private Button btnPerfil;
    @FXML
    private ImageView ivPerfil;
    @FXML
    private Pane paneCarta;

    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;
    
    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
        
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la mousePosistion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la mousePosistion del POI
//        double pinW = map_pin.getBoundsInLocal().getWidth();
//        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
    }

    private void initData() {
        data=map_listview.getItems();
        data.add(new Poi("1F", "Edificion del DSIC", 275, 250));
        data.add( new Poi("Agora", "Agora", 575, 350));
        data.add( new Poi("Pista", "Pista de atletismo y campo de futbol", 950, 350));
        System.out.println("Cambio realizado");
    }
    
    public void initUser(String u, String e,String p, Image a,LocalDate dt ){
            nick = u;
            email = e;
            pass = p;
            avatar = a;
            birthday = dt;        
        }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.05);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(0.5);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        try {
            Navigation nav = Navigation.getInstance();
            User res = nav.authenticate(nick, pass);
            ivPerfil.setImage(res.getAvatar());
        } catch (NavDAOException ex) {}
        
        

    }

    @FXML
    private void showPosition(MouseEvent event) {
        mousePosition.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    private void closeApp(ActionEvent event) {
        ((Stage) zoom_slider.getScene().getWindow()).close();
    }

    @FXML
    private void about(ActionEvent event) {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // Acceder al Stage del Dialog y cambiar el icono
        Stage dialogStage = (Stage) mensaje.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2025");
        mensaje.showAndWait();
    }

    @FXML
    private void addPoi(MouseEvent event) {

        if (event.isControlDown()) {
            Dialog<Poi> poiDialog = new Dialog<>();
            poiDialog.setTitle("Nuevo POI");
            poiDialog.setHeaderText("Introduce un nuevo POI");
            // Acceder al Stage del Dialog y cambiar el icono
            Stage dialogStage = (Stage) poiDialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

            ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            poiDialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

            TextField nameField = new TextField();
            nameField.setPromptText("Nombre del POI");

            TextArea descArea = new TextArea();
            descArea.setPromptText("Descripción...");
            descArea.setWrapText(true);
            descArea.setPrefRowCount(5);

            VBox vbox = new VBox(10, new Label("Nombre:"), nameField, new Label("Descripción:"), descArea);
            poiDialog.getDialogPane().setContent(vbox);

            poiDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return new Poi(nameField.getText().trim(), descArea.getText().trim(), 0, 0);
                }
                return null;
            });
            Optional<Poi> result = poiDialog.showAndWait();

            if(result.isPresent()) {
                Point2D localPoint = zoomGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
                Poi poi=result.get();
                poi.setPosition(localPoint);
                map_listview.getItems().add(poi);
            }
        }
    }

    @FXML
    private void seleccionarHerramientaMarcarPunto(ActionEvent event) {
    }

    @FXML
    private void seleccionarMarcaCirculo(ActionEvent event) {
    }

    @FXML
    private void seleccionarMarcaCruz(ActionEvent event) {
    }

    @FXML
    private void seleccionarMarcaAsterisco(ActionEvent event) {
    }

    @FXML
    private void seleccionarMarcaEstrella(ActionEvent event) {
    }

    @FXML
    private void seleccionarTrazarLinea(ActionEvent event) {
    }

    @FXML
    private void seleccionarGrosorMuyGrueso(ActionEvent event) {
    }

    @FXML
    private void seleccionarGrosorGrueso(ActionEvent event) {
    }

    @FXML
    private void seleccionarGrosorMedio(ActionEvent event) {
    }

    @FXML
    private void seleccionarGrosorFino(ActionEvent event) {
    }

    @FXML
    private void seleccionarTrazarArco(ActionEvent event) {
    }

    @FXML
    private void seleccionarGrosorArcoMuyGrueso(ActionEvent event) {
    }

    @FXML
    private void seleccionarGrosorArcoGrueso(ActionEvent event) {
    }

    @FXML
    private void seleccionarGrosorArcoMedio(ActionEvent event) {
    }

    @FXML
    private void seleccionarGrosorArcoFino(ActionEvent event) {
    }

    @FXML
    private void seleccionarAnotarTexto(ActionEvent event) {
    }

    @FXML
    private void seleccionarEditarColor(ActionEvent event) {
    }

    @FXML
    private void seleccionarEliminarMarca(ActionEvent event) {
    }

    @FXML
    private void seleccionarLimpiarCarta(ActionEvent event) {
    }

    @FXML
    private void seleccionarCompas(ActionEvent event) {
    }

    @FXML
    private void seleccionarTransportador(ActionEvent event) {
    }

    @FXML
    private void seleccionarRegla(ActionEvent event) {
    }

    @FXML
    private void onPerfilAction(ActionEvent event) {
    }


}
