/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.File;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class PerfilPanelController {

    private ImageView ivAvatar;
    @FXML private TextField tfUsuario;
    @FXML private PasswordField pfContrasena;
    @FXML private TextField tfEmail;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML
    private ImageView ivDialogIcon;

    /** Abre el FileChooser para cambiar el avatar */
    @FXML
    void onChooseAvatar() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecciona un avatar");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        Window win = ivAvatar.getScene().getWindow();
        File f = chooser.showOpenDialog(win);
        if (f != null) {
            ivAvatar.setImage(new Image(f.toURI().toString()));
        }
    }

    /** Guarda los cambios (aquí solo cierra el diálogo) */
    @FXML
    void onGuardar() {
        // Validaciones mínimas (si quieres):
        if (pfContrasena.getText().length() < 8) {
            showError("La contraseña debe tener al menos 8 caracteres.");
            return;
        }
        // …tú decides si validas email/fecha aquí…
        // Cerramos la ventana:
        ivAvatar.getScene().getWindow().hide();
        showInfo("Cambios guardados (simulado).");
    }

    /** Cancela y cierra */
    @FXML
    void onCancelar() {
        ivAvatar.getScene().getWindow().hide();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.initOwner(ivAvatar.getScene().getWindow());
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.initOwner(ivAvatar.getScene().getWindow());
        a.showAndWait();
    }
}