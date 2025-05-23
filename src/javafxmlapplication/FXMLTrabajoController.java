/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import model.Answer;
import model.NavDAOException;
import model.Navigation;
import model.Problem;
import model.User;
import model.sub.SqliteConnection;
import poiupv.Poi;


public class FXMLTrabajoController implements Initializable {

    // — Datos y Zoom —
    private final HashMap<String,Poi> hm = new HashMap<>();
    private ObservableList<Poi> data;
    private Group zoomGroup;
    private String pendingText = null; 

    private ListView<Poi> map_listview;
    @FXML private ScrollPane  map_scrollpane;
    @FXML private Slider      zoom_slider;
    @FXML private Pane        paneCarta;
    @FXML private MenuButton  mbPerfil;
    @FXML private MenuItem    miEditarPerfil;
    @FXML private MenuItem    menuItemEliminarMarcaTool;
    @FXML private MenuItem    menuItemLimpiarCartaTool;

    // — ColorPickers y controles —
    @FXML private ColorPicker colorPickkerPunto;
    @FXML private ColorPicker colorPickerLinea;
    @FXML private ColorPicker colorPickerArco;
    @FXML private TextField   textFieldRadioArco;
    @FXML private TextField   textFieldTamanoTexto;
    @FXML private ColorPicker colorPickerTexto;
    @FXML private ColorPicker colorPickerEdicion;

    // — Línea menú items —
    @FXML private MenuItem menuItemIniciarLinea;
    
    // Transportador
@FXML private MenuItem menuItemTransportador;

// Preview dinámico de la línea mientras arrastras
private Group   previewLineGroup;
private EventHandler<MouseEvent> lineDragHandler;
    @FXML
    private Menu menuMarcarPunto;
    @FXML
    private MenuItem menuItemIniciarPunto;
    @FXML
    private MenuButton mbMarcarPunto;
    @FXML
    private MenuItem menuItemMarcaCirculo;
    @FXML
    private MenuItem menuItemMarcaCruz;
    @FXML
    private MenuItem menuItemMarcaAsterisco;
    @FXML
    private MenuItem menuItemMarcaEstrella;
    @FXML
    private Menu menuTrazarLinea;
    @FXML
    private MenuItem menuGrosorLinea;
    @FXML
    private Menu menuTrazarArco;
    @FXML
    private MenuItem menuItemIniciarArco;
    @FXML
    private MenuItem menuItemRadioLibre;
    @FXML
    private MenuItem menuItemRadioFijo;
    @FXML
    private Menu menuAnotarTexto;
    @FXML
    private MenuItem menuItemAnotarTexto;
    @FXML
    private Menu menuColor;
    @FXML
    private MenuItem menuItemEditarColor;
    @FXML
    private Menu menuEliminarMarca;
    @FXML
    private Menu menuLimpiarCarta;
    @FXML
    private ImageView ivPerfil;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private TextArea taProblem;
    private Problem currentProblem;
    @FXML
    private Slider sliderGrosorLinea;
    @FXML
    private Slider sliderGrosorArco;
    @FXML
    private MenuItem btnRegla;
    @FXML
    private MenuItem menuItemLyL;
    @FXML
    private MenuItem miCerrarSesion;
    @FXML
    private MenuItem miCerrarAplicacion;
    @FXML
    private Label mousePosition;

    private void CerrarSesionAction(ActionEvent event) throws IOException, Exception {
        ivPerfil.getScene().getWindow().hide();
        poiupv.PoiUPVApp.reiniciarApp();
    }

    private void CerrarAplicacionAction(ActionEvent event) {
        ivPerfil.getScene().getWindow().hide();
    }

    @FXML
    private void seleccionarLatLonMarker(ActionEvent event) {
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
    private void onCerrarAplicacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaCerrarAplicacion.fxml"));
        Parent root = loader.load();
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Cerrar Aplicación");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    // — Estado global —
    private enum Tool { NONE, POINT, LINE, ARC, TEXT, EDIT_COLOR, DELETE, COMPASS, RULER, PROTRACTOR }
    private Tool currentTool      = Tool.NONE;
    private boolean markingEnabled = false;

    // — Punto —
    private Color  colorSeleccionado = Color.RED;
    private String tipoPunto         = "circulo";

    // — Línea —
     private boolean lineStarted = false;
    private double  lineStartX, lineStartY;
    private Color   colorLinea   = Color.RED;
    private double  grosorLinea  = 4;      // ← grosor “grueso” por defecto

    // — Arco —
    private boolean arcStarted   = false;
    private double  arcCenterX, arcCenterY;
    private Color   colorArco    = Color.RED;
    private double  grosorArco   = 4; 
    private double  radioFijo    = 30;

    // — Texto —
    private double tamanoTexto = 12;
    private Color  colorTexto  = Color.RED;
    private String textoPendiente;   

    // — Edición Color —
    private Color colorEdicion = Color.BLACK;

    // — Compás, Regla, Transportador —
    private Point2D compasP1, compasP2;
    private Point2D rulerP1, rulerP2;
    private Circle   protractorNode;
    private ImageView ivOverlay;
private Group previewArcGroup;
private EventHandler<MouseEvent> arcDragHandler;
private Arc previewArc;
private enum ModoArco { RADIO_FIJO, RADIO_LIBRE }
private ModoArco modoArco = ModoArco.RADIO_FIJO;

    private Navigation nav;

    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;
    
    private User user;
    private List<Problem> listProblem;
    private List<Answer> listAnswer;
    
    public void initUser(String u, String e,String p, Image a,LocalDate dt ){
            nick = u;
            email = e;
            pass = p;
            avatar = a;
            birthday = dt;        
        }


    @Override
public void initialize(URL url, ResourceBundle rb) {
    if (taProblem == null) {
        System.out.println("TextArea 'taProblem' no ha sido inicializado correctamente.");
    } else {
        System.out.println("TextArea 'taProblem' ha sido inicializado correctamente.");
    }
    
    try {
            SqliteConnection sqlite = new SqliteConnection();
            sqlite.connectSqlite("C:data.db");
            System.out.println("Base de datos encontrada");
  
        } catch (SQLException ex) {
            System.out.println("Base de datos no encontrada");
        }
    
    try{
            nav = Navigation.getInstance();
            user = nav.authenticate(nick, pass);
            listProblem = nav.getProblems();
        } catch (NavDAOException ex) {
            System.out.println("Nav exception");
    }
        
        Problem problem = listProblem.get(0);
        String res = problem.getText();
        taProblem.setText(res);
    
    // 1) Tu setup original
    setupZoom();
    setupControls();
    setupPaneClickHandler();
    ivPerfil.imageProperty().setValue(avatar);
    taProblem.setWrapText(true);
    
        
    
    

    // 2) Grosor por defecto para línea y arco
    seleccionarGrosorGrueso(null);
    seleccionarGrosorArcoGrueso(null);

    // 3) Inicializar valores de los sliders y campos de texto
    textFieldRadioArco.setText(String.format("%.0f", radioFijo));
    textFieldTamanoTexto.setText(String.format("%.0f", tamanoTexto));

    // ── Aquí arranca la parte nueva para el transportador ──

    // 4) Carga cruda del PNG desde recursos
    Image raw = new Image(getClass()
        .getResourceAsStream("/resources/transportador.jpg"));

    // 5) Reprocesa para que SOLO las líneas y números queden opacos,
    //    y el fondo totalmente transparente
    Image overlay = makeOverlayPreserveGray(raw);

    // 6) Crea el ImageView con la imagen reprocesada
    ivOverlay = new ImageView(overlay);
    ivOverlay.setMouseTransparent(true); // deja pasar los eventos al pane
    ivOverlay.setVisible(false);         // arranca oculto

    // 7) Añádelo al Pane justo antes de tus trazos (capa intermedia)
    paneCarta.getChildren().add(ivOverlay);

    // ── Ahora habilitamos arrastre igual que con los textos ──

    // Clase interna para guardar desplazamiento
    class Delta { double x, y; }
    final Delta dragDelta = new Delta();

    // 8) Calcular offset al presionar
    ivOverlay.setOnMousePressed(e -> {
        double scale = zoomGroup.getScaleX();
        Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
        dragDelta.x = (mouse.getX() - ivOverlay.getLayoutX()) * scale;
        dragDelta.y = (mouse.getY() - ivOverlay.getLayoutY()) * scale;
        e.consume();
    });

    // 9) Mover mientras arrastras
    ivOverlay.setOnMouseDragged(e -> {
        double scale = zoomGroup.getScaleX();
        Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
        ivOverlay.setLayoutX((mouse.getX() - dragDelta.x) / scale);
        ivOverlay.setLayoutY((mouse.getY() - dragDelta.y) / scale);
        e.consume();
    });

    // 10) Fijar posición al soltar y salir de modo transportador
    ivOverlay.setOnMouseReleased(e -> {
        resetModes();
        markingEnabled = false;
        currentTool    = Tool.NONE;
        e.consume();
    });
    // ────────────────────────────────────────────────────────────
}

    

    private void setupZoom() {
        
        zoom_slider.setMin(0.05);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(0.75);
        zoom_slider.valueProperty().addListener((o,ov,nv)->zoom(nv.doubleValue()));
        Group content = new Group();
        zoomGroup = new Group();
        content.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(content);
    }

    private void setupControls() {
    // Asignación de colores desde los color pickers
    colorPickkerPunto.setOnAction(e -> colorSeleccionado = colorPickkerPunto.getValue());
    colorPickerLinea .setOnAction(e -> colorLinea        = colorPickerLinea.getValue());
    colorPickerArco  .setOnAction(e -> colorArco         = colorPickerArco.getValue());
    colorPickerTexto .setOnAction(e -> colorTexto        = colorPickerTexto.getValue());
    colorPickerEdicion.setOnAction(e -> colorEdicion     = colorPickerEdicion.getValue());

    // TextField para el tamaño del texto
    textFieldTamanoTexto.setOnAction(e -> {
        try {
            tamanoTexto = Double.parseDouble(textFieldTamanoTexto.getText());
        } catch (NumberFormatException ex) {
            textFieldTamanoTexto.setText(String.format("%.0f", tamanoTexto));
        }
    });

    // TextField para el radio fijo del arco
    textFieldRadioArco.setOnAction(e -> {
        try {
            radioFijo = Double.parseDouble(textFieldRadioArco.getText());
        } catch (NumberFormatException ex) {
            textFieldRadioArco.setText(String.format("%.0f", radioFijo));
        }
    });
}


    private void setupPaneClickHandler() {
    paneCarta.setOnMouseClicked(evt -> {
        // Clic derecho cancela la herramienta
        if (evt.getButton() == MouseButton.SECONDARY) {
            resetModes();
            return;
        }
        // Si no estamos en modo marcado, nada que hacer
        if (!markingEnabled) return;

        double x = evt.getX(), y = evt.getY();
        switch (currentTool) {
            case POINT -> drawPoint(x, y);
            case LINE -> handleLineClick(evt);
            case ARC -> handleArcClick(evt);
            case TEXT  -> handleTextClick(x, y);

            case EDIT_COLOR -> {
                Node n = (Node) evt.getTarget();
                if (n != paneCarta) applyColor(n, colorEdicion);
                resetModes();
            }

            case DELETE -> {
                Node n = (Node) evt.getTarget();
                if (n instanceof Shape || n instanceof Text) {
                    if (n.getParent() instanceof Group) {
                        paneCarta.getChildren().remove(n.getParent());
                    } else {
                        paneCarta.getChildren().remove(n);
                    }
                }
                resetModes();
            }

            case COMPASS -> measureCompass(x, y);
            case RULER   -> measureRuler(x, y);

            case PROTRACTOR -> {
                // Reposiciona el overlay y lo hace visible
                ivOverlay.setLayoutX(x - ivOverlay.getImage().getWidth()  / 2);
                ivOverlay.setLayoutY(y - ivOverlay.getImage().getHeight() / 2);
                ivOverlay.setVisible(true);
                // No resetModes() para que puedas reposicionarlo tantas veces como quieras
            }

            default -> { }
        }
    });
}
    // ─── Draw helpers ────────────────────────────────────────────────────────────

    private void drawCross(double x, double y, Color c) {
        Line h = new Line(x-5, y, x+5, y);
        Line v = new Line(x, y-5, x, y+5);
        h.setStroke(c); v.setStroke(c);
        paneCarta.getChildren().addAll(h, v);
    }

    private void drawPoint(double x, double y) {
        switch (tipoPunto) {
            case "estrella"  -> { Text t = new Text(x-6, y+6, "★"); t.setFill(colorSeleccionado); paneCarta.getChildren().add(t); }
            case "asterisco" -> { Text t = new Text(x-4, y+6, "*"); t.setFill(colorSeleccionado); paneCarta.getChildren().add(t); }
            case "cruz"      -> { Text t = new Text(x-4, y+6, "+"); t.setFill(colorSeleccionado); paneCarta.getChildren().add(t); }
            default          -> { Circle c = new Circle(x, y, 5); c.setFill(colorSeleccionado); paneCarta.getChildren().add(c); }
        }
    }
    // Método que recibe el problema y lo pasa al TextArea
    public void setProblem(Problem problem) {
        this.currentProblem = problem;
        mostrarEnunciado();  // Llama a este método para mostrar el enunciado en el TextArea
    }

    // Método para mostrar el enunciado en el TextArea
    private void mostrarEnunciado() {
        if (currentProblem != null && taProblem != null) {
            taProblem.setText(currentProblem.getText());  // Establece el texto del problema en el TextArea
        }
    }

    private void handleLineClick(MouseEvent evt) {
    double x = evt.getX(), y = evt.getY();
    if (!lineStarted) {
        // 1) Primer clic: guardo origen y creo el Group de preview
        lineStartX = x;
        lineStartY = y;
        lineStarted = true;

        // Línea inicial (cero longitud)
        Line line = new Line(lineStartX, lineStartY, x, y);
        line.setStroke(colorLinea);
        line.setStrokeWidth(grosorLinea);

        // Cruces en el punto inicial
        Line h1 = new Line(lineStartX-5, lineStartY, lineStartX+5, lineStartY);
        Line v1 = new Line(lineStartX, lineStartY-5, lineStartX, lineStartY+5);
        h1.setStroke(colorLinea);
        v1.setStroke(colorLinea);

        // Cruces en el extremo (empiezan en el mismo punto)
        Line h2 = new Line(x-5, y, x+5, y);
        Line v2 = new Line(x, y-5, x, y+5);
        h2.setStroke(colorLinea);
        v2.setStroke(colorLinea);

        previewLineGroup = new Group(line, h1, v1, h2, v2);
        paneCarta.getChildren().add(previewLineGroup);

        // 2) Defino el handler de movimiento para actualizar la preview
        lineDragHandler = moveEvt -> {
            double ex = moveEvt.getX(), ey = moveEvt.getY();
            // actualizo línea
            line.setEndX(ex);
            line.setEndY(ey);
            // actualizo cruces final
            h2.setStartX(ex-5); h2.setStartY(ey);
            h2.setEndX(ex+5);   h2.setEndY(ey);
            v2.setStartX(ex);   v2.setStartY(ey-5);
            v2.setEndX(ex);     v2.setEndY(ey+5);
        };
        paneCarta.addEventFilter(MouseEvent.MOUSE_MOVED, lineDragHandler);

    } else {
        // 3) Segundo clic: quito el handler y finalizo el Group
        paneCarta.removeEventFilter(MouseEvent.MOUSE_MOVED, lineDragHandler);
        lineStarted = false;
        resetModes();
    }
}

    private void handleArcClick(MouseEvent evt) {
    double x = evt.getX(), y = evt.getY();

    if (modoArco == ModoArco.RADIO_FIJO) {
        if (!arcStarted) {
            arcCenterX = x;
            arcCenterY = y;
            arcStarted = true;
        } else {
            Arc arc = new Arc(arcCenterX, arcCenterY, radioFijo, radioFijo, 0, 180);
            arc.setType(ArcType.OPEN);
            arc.setStroke(colorArco);
            arc.setStrokeWidth(grosorArco);
            arc.setFill(Color.TRANSPARENT);

            // Cruces en extremos del arco
            Line h1 = new Line(arcCenterX + radioFijo - 5, arcCenterY, arcCenterX + radioFijo + 5, arcCenterY);
            Line v1 = new Line(arcCenterX + radioFijo, arcCenterY - 5, arcCenterX + radioFijo, arcCenterY + 5);
            Line h2 = new Line(arcCenterX - radioFijo - 5, arcCenterY, arcCenterX - radioFijo + 5, arcCenterY);
            Line v2 = new Line(arcCenterX - radioFijo, arcCenterY - 5, arcCenterX - radioFijo, arcCenterY + 5);

            for (Line cruz : List.of(h1, v1, h2, v2)) {
                cruz.setStroke(colorArco);
            }

            Group g = new Group(arc, h1, v1, h2, v2);
            paneCarta.getChildren().add(g);

            arcStarted = false;
            resetModes();
        }

    } else if (modoArco == ModoArco.RADIO_LIBRE) {
        handleArcLibre(evt); // comportamiento dinámico
    }
}
    

    private void measureCompass(double x, double y) {
        if (compasP1 == null) {
            compasP1 = new Point2D(x, y);
        } else {
            compasP2 = new Point2D(x, y);
            double d = compasP1.distance(compasP2);
            Line l = new Line(compasP1.getX(), compasP1.getY(), compasP2.getX(), compasP2.getY());
            paneCarta.getChildren().add(l);
            Text t = new Text((compasP1.getX()+compasP2.getX())/2, (compasP1.getY()+compasP2.getY())/2, String.format("%.1f", d));
            makeDraggable(t);
            paneCarta.getChildren().add(t);
            compasP1 = compasP2 = null;
        }
    }

    private void measureRuler(double x, double y) {
        if (rulerP1 == null) {
            rulerP1 = new Point2D(x, y);
        } else {
            rulerP2 = new Point2D(x, y);
            double d = rulerP1.distance(rulerP2);
            Text t = new Text(rulerP2.getX(), rulerP2.getY(), String.format("Dist: %.1f", d));
            makeDraggable(t);
            paneCarta.getChildren().add(t);
            rulerP1 = rulerP2 = null;
        }
    }

    private void applyColor(Node n, Color c) {
    // Si el nodo pertenece a un Group, recoloreamos TODOS sus hijos
    if (n.getParent() instanceof Group) {
        Group grupo = (Group) n.getParent();
        for (Node hijo : grupo.getChildren()) {
            if (hijo instanceof Shape) {
                ((Shape)hijo).setStroke(c);
            } else if (hijo instanceof Text) {
                ((Text)hijo).setFill(c);
            }
        }
    } else {
        // Caso aislado: un Shape o Text suelto
        if (n instanceof Shape) {
            ((Shape)n).setStroke(c);
        } else if (n instanceof Text) {
            ((Text)n).setFill(c);
        }
    }
}
    
    private void handleArcLibre(MouseEvent evt) {
    double x = evt.getX(), y = evt.getY();

    if (!arcStarted) {
        arcCenterX = x;
        arcCenterY = y;
        arcStarted = true;

        previewArc = new Arc(arcCenterX, arcCenterY, 1, 1, 0, 180);
        previewArc.setType(ArcType.OPEN);
        previewArc.setStroke(colorArco);
        previewArc.setStrokeWidth(grosorArco);
        previewArc.setFill(Color.TRANSPARENT);

        Line h1 = new Line(), v1 = new Line();
        Line h2 = new Line(), v2 = new Line();
        for (Line l : List.of(h1, v1, h2, v2)) l.setStroke(colorArco);

        previewArcGroup = new Group(previewArc, h1, v1, h2, v2);
        paneCarta.getChildren().add(previewArcGroup);

        arcDragHandler = moveEvt -> {
            double ex = moveEvt.getX(), ey = moveEvt.getY();
            double r = Math.hypot(ex - arcCenterX, ey - arcCenterY);
            previewArc.setRadiusX(r);
            previewArc.setRadiusY(r);

            double rightX = arcCenterX + r;
            double leftX  = arcCenterX - r;
            double y0 = arcCenterY;

            h1.setStartX(rightX - 5); h1.setEndX(rightX + 5);
            h1.setStartY(y0);         h1.setEndY(y0);
            v1.setStartX(rightX);     v1.setEndX(rightX);
            v1.setStartY(y0 - 5);     v1.setEndY(y0 + 5);

            h2.setStartX(leftX - 5);  h2.setEndX(leftX + 5);
            h2.setStartY(y0);         h2.setEndY(y0);
            v2.setStartX(leftX);      v2.setEndX(leftX);
            v2.setStartY(y0 - 5);     v2.setEndY(y0 + 5);
        };

        paneCarta.addEventFilter(MouseEvent.MOUSE_MOVED, arcDragHandler);

    } else {
        paneCarta.removeEventFilter(MouseEvent.MOUSE_MOVED, arcDragHandler);
        arcStarted = false;
        previewArc = null;
        previewArcGroup = null;
        resetModes();
    }
}

    private void makeDraggable(Text txt) {
        final double[] d = new double[2];
        txt.setOnMousePressed(e -> {
            Point2D loc = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            d[0] = loc.getX() - txt.getX();
            d[1] = loc.getY() - txt.getY();
            e.consume();
        });
        txt.setOnMouseDragged(e -> {
            Point2D loc = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            txt.setX(loc.getX() - d[0]);
            txt.setY(loc.getY() - d[1]);
            e.consume();
        });
    }
    
    private Image makeOverlayPreserveGray(Image src) {
    int w = (int) src.getWidth();
    int h = (int) src.getHeight();
    WritableImage dst = new WritableImage(w, h);
    PixelReader  pr = src.getPixelReader();
    PixelWriter  pw = dst.getPixelWriter();

    for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {
            Color c = pr.getColor(x, y);
            // si es un píxel muy claro (cercano al blanco), lo tratamos como fondo
            if (c.getOpacity() > 0.02 && c.getBrightness() < 0.95) {
                // línea o número: dejamos su color original, pero forzamos alpha=1
                pw.setColor(x, y,
                    new Color(c.getRed(), c.getGreen(), c.getBlue(), 1.0));
            } else {
                // fondo: transparente
                pw.setColor(x, y, Color.TRANSPARENT);
            }
        }
    }
    return dst;
}
    
    private void handleTextClick(double x, double y) {
    if (textoPendiente != null) {    // ← aquí también
        Text txt = new Text(x, y, textoPendiente);
        txt.setFill(colorTexto);
        txt.setStyle("-fx-font-size:" + tamanoTexto + "px");
        makeDraggable(txt);
        paneCarta.getChildren().add(txt);
        textoPendiente = null;       // ← limpia para la próxima vez
    }
    resetModes();
}// ─── OnAction handlers ─────────────────────────────────────────────────────

    @FXML public void seleccionarHerramientaMarcarPunto(ActionEvent e) {
        resetModes(); markingEnabled = true; currentTool = Tool.POINT;
    }

    @FXML
public void seleccionarTrazarLinea(ActionEvent e) {
    resetModes();
    markingEnabled = true;
    currentTool    = Tool.LINE;
}
 public void seleccionarGrosorMuyGrueso(ActionEvent e) { grosorLinea = 6; }
public void seleccionarGrosorGrueso    (ActionEvent e) { grosorLinea = 4; }
public void seleccionarGrosorMedio     (ActionEvent e) { grosorLinea = 2; }
public void seleccionarGrosorFino      (ActionEvent e) { grosorLinea = 1; }

    @FXML
public void seleccionarTrazarArco(ActionEvent e) {
    resetModes();
    markingEnabled = true;
    currentTool    = Tool.ARC;
}
    public void seleccionarGrosorArcoMuyGrueso(ActionEvent e) { grosorArco = 6; }
    public void seleccionarGrosorArcoGrueso   (ActionEvent e) { grosorArco = 4; }
    public void seleccionarGrosorArcoMedio    (ActionEvent e) { grosorArco = 2; }
    public void seleccionarGrosorArcoFino     (ActionEvent e) { grosorArco = 1; }

    
    
    
    @FXML
    private void showPosition(MouseEvent e) {
        
    }

    @FXML public void onMenuColorPicked(ActionEvent e) {
    // Preparo el modo edición de color
    resetModes();
    markingEnabled = true;
    currentTool    = Tool.EDIT_COLOR;
    // Muestro el popup del ColorPicker que ya está en el menú
    colorPickerEdicion.show();
}

    @FXML public void seleccionarEliminarElemento(ActionEvent e) {
        resetModes(); markingEnabled = true; currentTool = Tool.DELETE;
    }

    @FXML public void seleccionarLimpiarCarta(ActionEvent e) {
    paneCarta.getChildren().removeIf(n ->
        n instanceof Shape ||
        n instanceof Text  ||
        n instanceof Group
    );
    resetModes();
}

    public void seleccionarCompas(ActionEvent e) {
        resetModes(); markingEnabled = true; currentTool = Tool.COMPASS;
    }
    @FXML public void seleccionarRegla(ActionEvent e) {
        resetModes(); markingEnabled = true; currentTool = Tool.RULER;
    }
    @FXML
    public void seleccionarTransportador(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool    = Tool.PROTRACTOR;
        ivOverlay.setVisible(true);
    
    }
    @FXML public void onEditarPerfil(ActionEvent e) throws IOException {
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/PerfilPanel.fxml"));
            Parent root = loader.load();
            Stage dialog = new Stage();
            dialog.setTitle("Editar perfil");
            dialog.initOwner(paneCarta.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            PerfilPanelController controlador2= loader.getController();
            controlador2.initUser(nick, email, pass, avatar, birthday);
            controlador2.mostrarinfo(nick, email, pass, avatar, birthday);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
            
            if(controlador2.pulsadoGuardar()){
                User us = controlador2.getUser();
                Image res = us.getAvatar();
                user = us;
                ivPerfil.setImage(res);
                
            }
            
    }
    
    @FXML
public void seleccionarAnotarTexto(ActionEvent e) {
    TextInputDialog dlg = new TextInputDialog();
    dlg.setTitle("Anotar texto");
    dlg.setHeaderText("Introduce el texto:");
    dlg.getEditor().setPromptText("Tu texto aquí");

    Optional<String> res = dlg.showAndWait();
    res.ifPresent(s -> {
        textoPendiente  = s;          // ← asignamos al nuevo campo
        resetModes();
        markingEnabled  = true;
        currentTool     = Tool.TEXT;
    });
}
    
private void listClicked(MouseEvent e) {
    // Obtenemos el POI seleccionado
    Poi poi = map_listview.getSelectionModel().getSelectedItem();
    if (poi == null) return;

    // Calculamos la posición normalizada para centrar el ScrollPane
    double contentWidth  = zoomGroup.getBoundsInLocal().getWidth() * zoom_slider.getValue();
    double contentHeight = zoomGroup.getBoundsInLocal().getHeight() * zoom_slider.getValue();
    double viewportW     = map_scrollpane.getViewportBounds().getWidth();
    double viewportH     = map_scrollpane.getViewportBounds().getHeight();

    double h = (poi.getX() * zoom_slider.getValue() - viewportW  / 2) / (contentWidth  - viewportW);
    double v = (poi.getY() * zoom_slider.getValue() - viewportH  / 2) / (contentHeight - viewportH);

    // Clampeamos entre 0 y 1
    h = Math.max(0, Math.min(1, h));
    v = Math.max(0, Math.min(1, v));

    map_scrollpane.setHvalue(h);
    map_scrollpane.setVvalue(v);
}
    
    @FXML
private void addPoi(MouseEvent e) {
    // Solo respondemos a Ctrl+clic
    if (!e.isControlDown()) return;

    // Creamos un diálogo para introducir un nuevo POI
    Dialog<Poi> dlg = new Dialog<>();
    dlg.setTitle("Nuevo POI");
    dlg.setHeaderText("Introduce un nuevo POI");

    // (Opcional) poner un icono al diálogo
    Stage ds = (Stage) dlg.getDialogPane().getScene().getWindow();
    ds.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

    // Botones Aceptar / Cancelar
    ButtonType ok = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
    dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

    // Campos de entrada
    TextField name = new TextField();
    name.setPromptText("Nombre del POI");
    TextArea desc = new TextArea();
    desc.setPromptText("Descripción...");
    desc.setWrapText(true);
    desc.setPrefRowCount(5);

    // Montamos el contenido en un VBox
    dlg.getDialogPane().setContent(new VBox(10,
        new Label("Nombre:"), name,
        new Label("Descripción:"), desc
    ));

    // Convertimos el resultado en un objeto Poi (o null si canceló)
    dlg.setResultConverter(btn -> btn == ok
        ? new Poi(name.getText().trim(), desc.getText().trim(), 0, 0)
        : null
    );

    // Mostramos y procesamos el resultado
    Optional<Poi> res = dlg.showAndWait();
    res.ifPresent(poi -> {
        // Calculamos la posición en coordenadas del pane
        Point2D lp = zoomGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
        poi.setPosition(lp);
        map_listview.getItems().add(poi);
    });
}
    
    
@FXML public void seleccionarMarcaCirculo(ActionEvent e) {
    tipoPunto = "circulo";
}

@FXML public void seleccionarMarcaEstrella(ActionEvent e) {
    tipoPunto = "estrella";
}

@FXML public void seleccionarMarcaAsterisco(ActionEvent e) {
    tipoPunto = "asterisco";
}

@FXML public void seleccionarMarcaCruz(ActionEvent e) {
    tipoPunto = "cruz";
}
@FXML public void seleccionarEditarColor(ActionEvent e) {
    resetModes();
    markingEnabled = true;
    currentTool    = Tool.EDIT_COLOR;
    colorPickerEdicion.show();
}
@FXML
private void about(ActionEvent e) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Acerca de");
    alert.setHeaderText("IPC - 2025");
    // opcional: añadir icono
    // Stage st = (Stage) alert.getDialogPane().getScene().getWindow();
    // st.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
    alert.showAndWait();
}

    @FXML public void zoomIn(ActionEvent e)  { zoom_slider.setValue(zoom_slider.getValue()+0.1); }
    @FXML public void zoomOut(ActionEvent e) { zoom_slider.setValue(zoom_slider.getValue()-0.1); }

    private void resetModes() {
        currentTool     = Tool.NONE;
        markingEnabled  = false;
        lineStarted     = arcStarted = false;
        compasP1 = compasP2 = rulerP1 = rulerP2 = null;
    }

    private void zoom(double s) {
        double h = map_scrollpane.getHvalue(), v = map_scrollpane.getVvalue();
        zoomGroup.setScaleX(s); zoomGroup.setScaleY(s);
        map_scrollpane.setHvalue(h); map_scrollpane.setVvalue(v);
    }
    
    @FXML
private void seleccionarArcoRadioFijo(ActionEvent e) {
    modoArco = ModoArco.RADIO_FIJO;
    seleccionarTrazarArco(e); // activa la herramienta
}

@FXML
private void seleccionarArcoRadioLibre(ActionEvent e) {
    modoArco = ModoArco.RADIO_LIBRE;
    seleccionarTrazarArco(e); // activa la herramienta
}
    
   
}