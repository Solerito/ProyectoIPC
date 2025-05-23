/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author jose
 */

    
    

    public class PoiUPVApp extends Application {
        private static Stage Pstage;
        private static PoiUPVApp instance;
    
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        Pstage = stage;
        
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("/vista/VentanaAutenticacion.fxml"));
        Parent root = loader.load();
        Pstage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        Scene scene = new Scene(root);
        Pstage.setTitle("Bienvenido");
        Pstage.setScene(scene);
        Pstage.show();
        
        
        
    }
    
    
    
    public static void reiniciarApp() throws Exception {
        // Cierra la ventana actual
        

        // Crea un nuevo Stage y llama a start()
        Stage newStage = new Stage();
        instance.start(newStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
