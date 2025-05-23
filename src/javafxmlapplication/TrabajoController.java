/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxmlapplication.FXMLTrabajoController;
import model.Navigation;
import model.User;

public class TrabajoController implements Initializable {

    //Datos y Zoom
    private final HashMap<String, Poi> hm = new HashMap<>();
    private ObservableList<Poi> data;
    private Group zoomGroup;
    private String pendingText = null;
    private final Delta dragDelta = new Delta();

    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private Pane paneCarta;
    @FXML
    private MenuButton mbPerfil;
    @FXML
    private MenuItem miEditarPerfil;

//ColorPickers y controles
    @FXML
    private ColorPicker colorPickkerPunto;
    @FXML
    private ColorPicker colorPickerLinea;
    @FXML
    private ColorPicker colorPickerArco;
    @FXML
    private TextField textFieldRadioArco;
    @FXML
    private TextField textFieldTamanoTexto;
    @FXML
    private ColorPicker colorPickerTexto;
    @FXML
    private ColorPicker colorPickerEdicion;

//Preview dinámico de la línea mientras arrastras
    private Group previewLineGroup;
    private EventHandler<MouseEvent> lineDragHandler;

    @FXML
    private ImageView ivPerfil;
    @FXML
    private MenuButton map_pin;
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
    private MenuItem menuItemIniciarLinea;
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
    private Label mousePosition;
    @FXML
    private MenuItem menuItemEditarColor;
    @FXML
    private Menu menuEliminarMarca;
    @FXML
    private MenuItem menuItemEliminarMarcaTool;
    @FXML
    private Menu menuLimpiarCarta;
    @FXML
    private MenuItem menuItemLimpiarCartaTool;
    @FXML
    private MenuItem menuItemTransportador;
    @FXML
    private MenuItem btnRegla;
    @FXML
    private MenuItem menuItemLyL;
    @FXML
    private MenuItem pin_info;
    @FXML
    private Slider sliderGrosorLinea;

    @FXML
    private Slider sliderGrosorArco;

    @FXML
    private void showPosition(MouseEvent event) {
    }

//Estado global
    private enum Tool {
        NONE, POINT, LINE, ARC, TEXT, EDIT_COLOR, DELETE,
        COMPASS, RULER, PROTRACTOR,
        LATLON_MARKER, REGLA_VISUAL // ← NUEVO
    }
    private Tool currentTool = Tool.NONE;
    private boolean markingEnabled = false;

//Punto
    private Color colorSeleccionado = Color.RED;
    private String tipoPunto = "circulo";

//Línea
    private boolean lineStarted = false;
    private double lineStartX, lineStartY;
    private Color colorLinea = Color.RED;
    private double grosorLinea = 4;      // ← grosor “grueso” por defecto

//Arco
    private boolean arcStarted = false;
    private double arcCenterX, arcCenterY;
    private Color colorArco = Color.RED;
    private double grosorArco = 4;
    private double radioFijo = 30;

//Texto
    private double tamanoTexto = 12;
    private Color colorTexto = Color.RED;
    private String textoPendiente;

//Edición Color
    private Color colorEdicion = Color.BLACK;

//Compás, Regla, Transportador
    private Point2D compasP1, compasP2;
    private Point2D rulerP1, rulerP2;
    private Circle protractorNode;
    private ImageView ivOverlay;
    private Pane reglaContainer;
    private Group previewArcGroup;
    private EventHandler<MouseEvent> arcDragHandler;
    private Arc previewArc;

    private enum ModoArco {
        RADIO_FIJO, RADIO_LIBRE
    }
    private ModoArco modoArco = ModoArco.RADIO_FIJO;
    private ImageView ivRegla;
    private ImageView ivHouse;
    private double mouseAnchorX;
    private double mouseAnchorY;

//Clase Delta para el arrastre
    private static class Delta {

        double x, y;
    }
    
    private String nick;
    private String email;
    private String pass;
    private Image avatar;
    private LocalDate birthday;
    private Boolean pulsadoGuardar;
    private User user;
    private Navigation nav;
    
    
    public void initUser(String u, String e,String p, Image a,LocalDate dt ){
            nick = u;
            email = e;
            pass = p;
            avatar = a;
            birthday = dt;        
        }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Tamaño paneCarta, scrollbars y zoom (igual que antes)
        paneCarta.setPrefSize(2500, 1700);
        map_scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        map_scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        setupZoom();
        setupControls();
        setupPaneClickHandler();

        // Inicializar grosor por defecto
        grosorLinea = 4;
        grosorArco = 4;

        // Configurar sliders de grosor (asumiendo que sliderGrosorLinea y sliderGrosorArco están vinculados via @FXML)
        sliderGrosorLinea.setMin(1);
        sliderGrosorLinea.setMax(10);
        sliderGrosorLinea.setBlockIncrement(1);
        sliderGrosorLinea.setMajorTickUnit(1);
        sliderGrosorLinea.setSnapToTicks(true);
        sliderGrosorLinea.setValue(grosorLinea);
        sliderGrosorLinea.valueProperty().addListener((obs, oldVal, newVal) -> {
            grosorLinea = newVal.intValue();
        });

        sliderGrosorArco.setMin(1);
        sliderGrosorArco.setMax(10);
        sliderGrosorArco.setBlockIncrement(1);
        sliderGrosorArco.setMajorTickUnit(1);
        sliderGrosorArco.setSnapToTicks(true);
        sliderGrosorArco.setValue(grosorArco);
        sliderGrosorArco.valueProperty().addListener((obs, oldVal, newVal) -> {
            grosorArco = newVal.intValue();
        });

        // Texto y radio arco iniciales
        textFieldRadioArco.setText(String.format("%.0f", radioFijo));
        textFieldTamanoTexto.setText(String.format("%.0f", tamanoTexto));

        // Transportador: igual que antes
        Image raw = new Image(getClass().getResourceAsStream("/resources/transportador.png"));
        Image overlay = makeOverlayPreserveGray(raw);
        ivOverlay = new ImageView(overlay);
        ivOverlay.setMouseTransparent(false);
        ivOverlay.setPickOnBounds(true);
        ivOverlay.setVisible(false);
        paneCarta.getChildren().add(ivOverlay);

        final Delta dragDeltaOverlay = new Delta();

        ivOverlay.setOnMousePressed(e -> {
            Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            dragDeltaOverlay.x = mouse.getX() - ivOverlay.getLayoutX();
            dragDeltaOverlay.y = mouse.getY() - ivOverlay.getLayoutY();
            e.consume();
        });

        ivOverlay.setOnMouseDragged(e -> {
            Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            ivOverlay.setLayoutX(mouse.getX() - dragDeltaOverlay.x);
            ivOverlay.setLayoutY(mouse.getY() - dragDeltaOverlay.y);
            e.consume();
        });

        ivOverlay.setOnMouseReleased(e -> {
            resetModes();
            markingEnabled = false;
            currentTool = Tool.NONE;
            e.consume();
        });

        // ------------------------- REGLA -------------------------
        // Creamos un Pane contenedor para la regla
        Pane reglaContainer = new Pane();
        reglaContainer.setPickOnBounds(true);  // Muy importante: capta eventos en toda el área
        reglaContainer.setStyle("-fx-background-color: rgba(0,0,0,0);"); // Fondo transparente para captar eventos

        // Cargamos imagen regla y la ponemos en el contenedor
        Image reglaImg = new Image(getClass().getResourceAsStream("/resources/regla123.png"));
        ivRegla = new ImageView(reglaImg);

        ivRegla.setOpacity(0.85);
        ivRegla.setScaleX(0.8);
        ivRegla.setScaleY(0.8);
        ivRegla.setMouseTransparent(true);  // Delegamos eventos al contenedor

        reglaContainer.getChildren().add(ivRegla);

        // Añadimos el contenedor al paneCarta
        paneCarta.getChildren().add(reglaContainer);

        // Inicialmente no visible
        reglaContainer.setVisible(false);

        // Delta para arrastre
        final Delta dragDeltaRegla = new Delta();

        // Eventos para arrastrar el contenedor (no la imagen) y que capte toda el área
        reglaContainer.setOnMousePressed(e -> {
            Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            dragDeltaRegla.x = mouse.getX() - reglaContainer.getLayoutX();
            dragDeltaRegla.y = mouse.getY() - reglaContainer.getLayoutY();
            e.consume();
        });

        reglaContainer.setOnMouseDragged(e -> {
            Point2D mouse = paneCarta.sceneToLocal(e.getSceneX(), e.getSceneY());
            double newX = mouse.getX() - dragDeltaRegla.x;
            double newY = mouse.getY() - dragDeltaRegla.y;

            // Aquí quitamos límites para que no tenga "paredes invisibles"
            reglaContainer.setLayoutX(newX);
            reglaContainer.setLayoutY(newY);

            e.consume();
        });

        reglaContainer.setOnScroll(e -> {
            reglaContainer.setRotate(reglaContainer.getRotate() + e.getDeltaY() / 10);
            e.consume();
        });

        reglaContainer.setOnMouseReleased(e -> {
            e.consume();  // NO ocultar nada con botón derecho
        });

        this.reglaContainer = reglaContainer;
    }

    private void setupPaneClickHandler() {
        paneCarta.setOnMouseClicked(evt -> {

            if (evt.getButton() == MouseButton.SECONDARY) {
                resetModes();
                return;
            }
            if (!markingEnabled) {
                return;
            }

            double x = evt.getX(), y = evt.getY();
            switch (currentTool) {
                case POINT ->
                    drawPoint(x, y);
                case LINE ->
                    handleLineClick(evt);
                case ARC ->
                    handleArcClick(evt);
                case TEXT ->
                    handleTextClick(x, y);
                case EDIT_COLOR -> {
                    Node n = (Node) evt.getTarget();
                    if (n != paneCarta) {
                        applyColor(n, colorEdicion);
                    }
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
                }
                case COMPASS ->
                    measureCompass(x, y);
                case RULER, REGLA_VISUAL -> {
                    double clickX = evt.getX();
                    double clickY = evt.getY();

                    // Quitar regla antigua si existe
                    if (ivRegla != null) {
                        paneCarta.getChildren().remove(ivRegla);
                        ivRegla = null;
                    }

                    Image reglaImg = new Image(getClass().getResourceAsStream("/resources/regla123.png"));
                    ivRegla = new ImageView(reglaImg);

                    double escala = 1.0 / 6.0;
                    ivRegla.setFitWidth(reglaImg.getWidth() * escala);
                    ivRegla.setPreserveRatio(true);

                    ivRegla.setOpacity(1.0);
                    ivRegla.setMouseTransparent(false);
                    ivRegla.setPickOnBounds(true);

                    paneCarta.getChildren().add(ivRegla);

                    // Posicionar la regla justo donde hiciste click
                    Platform.runLater(() -> {
                        ivRegla.setLayoutX(clickX - ivRegla.getBoundsInLocal().getWidth() / 2);
                        ivRegla.setLayoutY(clickY - ivRegla.getBoundsInLocal().getHeight() / 2);
                    });

                    // Configurar el drag handler para la regla
                    setupReglaDragHandlers(ivRegla, paneCarta);

                    openRotationWindowNormal();
                    resetModes();
                }

                case PROTRACTOR -> {
                    ivOverlay.setLayoutX(x - ivOverlay.getImage().getWidth() / 2);
                    ivOverlay.setLayoutY(y - ivOverlay.getImage().getHeight() / 2);
                    ivOverlay.setVisible(true);
                }
                case LATLON_MARKER -> {
                    marcarLatLon(x, y);
                    resetModes();
                }
                default -> {
                }
            }
        });
    }

    // Funcionalidades dibujo en el mapa
    private void drawCross(double x, double y, Color c) {
        Line h = new Line(x - 5, y, x + 5, y);
        Line v = new Line(x, y - 5, x, y + 5);
        h.setStroke(c);
        v.setStroke(c);
        paneCarta.getChildren().addAll(h, v);
    }

    private void drawPoint(double x, double y) {
        switch (tipoPunto) {
            case "estrella" -> {
                Text t = new Text(x - 6, y + 6, "★");
                t.setFill(colorSeleccionado);
                t.setStyle("-fx-font-size: " + (14 * 1.1) + "px;");
                paneCarta.getChildren().add(t);
            }
            case "asterisco" -> {
                Text t = new Text(x - 4, y + 6, "*");
                t.setFill(colorSeleccionado);
                t.setStyle("-fx-font-size: " + (14 * 2.5) + "px;");
                paneCarta.getChildren().add(t);
            }
            case "cruz" -> {
                Text t = new Text(x - 4, y + 6, "+");
                t.setFill(colorSeleccionado);
                t.setStyle("-fx-font-size: " + (14 * 1.9) + "px;");
                paneCarta.getChildren().add(t);
            }
            default -> {
                Circle c = new Circle(x, y, 5);
                c.setFill(colorSeleccionado);
                paneCarta.getChildren().add(c);
            }
        }
    }

    private void handleLineClick(MouseEvent evt) {
        double x = evt.getX(), y = evt.getY();
        if (!lineStarted) {
            lineStartX = x;
            lineStartY = y;
            lineStarted = true;

            Line line = new Line(lineStartX, lineStartY, x, y);
            line.setStroke(colorLinea);
            line.setStrokeWidth(grosorLinea);

            Line h1 = new Line(lineStartX - 5, lineStartY, lineStartX + 5, lineStartY);
            Line v1 = new Line(lineStartX, lineStartY - 5, lineStartX, lineStartY + 5);
            h1.setStroke(colorLinea);
            v1.setStroke(colorLinea);

            Line h2 = new Line(x - 5, y, x + 5, y);
            Line v2 = new Line(x, y - 5, x, y + 5);
            h2.setStroke(colorLinea);
            v2.setStroke(colorLinea);

            previewLineGroup = new Group(line, h1, v1, h2, v2);
            paneCarta.getChildren().add(previewLineGroup);

            lineDragHandler = moveEvt -> {
                double ex = moveEvt.getX(), ey = moveEvt.getY();
                line.setEndX(ex);
                line.setEndY(ey);
                h2.setStartX(ex - 5);
                h2.setStartY(ey);
                h2.setEndX(ex + 5);
                h2.setEndY(ey);
                v2.setStartX(ex);
                v2.setStartY(ey - 5);
                v2.setEndX(ex);
                v2.setEndY(ey + 5);
            };
            paneCarta.addEventFilter(MouseEvent.MOUSE_MOVED, lineDragHandler);
        } else {
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
            Text t = new Text(
                    (compasP1.getX() + compasP2.getX()) / 2,
                    (compasP1.getY() + compasP2.getY()) / 2,
                    String.format("%.1f", d)
            );
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
            Text t = new Text(
                    rulerP2.getX(),
                    rulerP2.getY(),
                    String.format("Dist: %.1f", d)
            );
            makeDraggable(t);
            paneCarta.getChildren().add(t);
            rulerP1 = rulerP2 = null;
        }
    }

    private void marcarLatLon(double x, double y) {
        double ancho = 200000;    // ancho fijo muy grande
        double alto = 12000;      // alto fijo deseado

        double startXH = x - ancho / 2;
        double endXH = x + ancho / 2;

        double startYV = y - alto / 2;
        double endYV = y + alto / 2;

        Line lineaHorizontal = new Line(startXH, y, endXH, y);
        Line lineaVertical = new Line(x, startYV, x, endYV);

        lineaHorizontal.setStroke(Color.BLUE);
        lineaVertical.setStroke(Color.BLUE);

        lineaHorizontal.setStrokeWidth(2);
        lineaVertical.setStrokeWidth(2);

        lineaHorizontal.getStrokeDashArray().addAll(10.0, 10.0);
        lineaVertical.getStrokeDashArray().addAll(10.0, 10.0);

        lineaHorizontal.setId("latlonLine");
        lineaVertical.setId("latlonLine");

        Group latlonGroup = new Group(lineaHorizontal, lineaVertical);
        latlonGroup.setId("latlonGroup");

        paneCarta.getChildren().add(latlonGroup);
    }

    private void applyColor(Node n, Color c) {
        if (n.getParent() instanceof Group) {
            Group grupo = (Group) n.getParent();
            for (Node hijo : grupo.getChildren()) {
                if (hijo instanceof Arc arc) {
                    arc.setStroke(c);
                    arc.setFill(Color.TRANSPARENT); // ¡Importante! para que no se rellene
                } else if (hijo instanceof Shape s) {
                    s.setStroke(c);
                    s.setFill(null);  // Para líneas o cruces no rellenar
                } else if (hijo instanceof Text t) {
                    t.setFill(c);
                }
            }
        } else {
            if (n instanceof Arc arc) {
                arc.setStroke(c);
                arc.setFill(Color.TRANSPARENT);
            } else if (n instanceof Shape s) {
                s.setStroke(c);
                s.setFill(null);
            } else if (n instanceof Text t) {
                t.setFill(c);
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

            for (Line l : List.of(h1, v1, h2, v2)) {
                l.setStroke(colorArco);
            }

            previewArcGroup = new Group(previewArc, h1, v1, h2, v2);
            paneCarta.getChildren().add(previewArcGroup);

            arcDragHandler = moveEvt -> {
                double ex = moveEvt.getX(), ey = moveEvt.getY();
                double r = Math.hypot(ex - arcCenterX, ey - arcCenterY);

                // Limitar radio para que el arco no salga del pane
                double maxRadiusX = Math.min(arcCenterX, paneCarta.getWidth() - arcCenterX);
                double maxRadiusY = Math.min(arcCenterY, paneCarta.getHeight() - arcCenterY);
                double maxRadius = Math.min(maxRadiusX, maxRadiusY);

                if (r > maxRadius) {
                    r = maxRadius;
                }

                previewArc.setRadiusX(r);
                previewArc.setRadiusY(r);
                double rightX = arcCenterX + r;
                double leftX = arcCenterX - r;
                double y0 = arcCenterY;

                h1.setStartX(rightX - 5);
                h1.setEndX(rightX + 5);
                h1.setStartY(y0);
                h1.setEndY(y0);

                v1.setStartX(rightX);
                v1.setEndX(rightX);
                v1.setStartY(y0 - 5);
                v1.setEndY(y0 + 5);

                h2.setStartX(leftX - 5);
                h2.setEndX(leftX + 5);
                h2.setStartY(y0);
                h2.setEndY(y0);

                v2.setStartX(leftX);
                v2.setEndX(leftX);
                v2.setStartY(y0 - 5);
                v2.setEndY(y0 + 5);
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
        PixelReader pr = src.getPixelReader();
        PixelWriter pw = dst.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = pr.getColor(x, y);
                if (c.getOpacity() > 0.02 && c.getBrightness() < 0.95) {
                    pw.setColor(x, y,
                            new Color(c.getRed(), c.getGreen(), c.getBlue(), 1.0));
                } else {
                    pw.setColor(x, y, Color.TRANSPARENT);
                }
            }
        }
        return dst;
    }

    private void setupZoom() {
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            zoom(newValue.doubleValue());
        });

        Group content = new Group();
        zoomGroup = new Group();
        content.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(content);
    }

    private void setupControls() {
        colorPickkerPunto.setOnAction(e -> colorSeleccionado = colorPickkerPunto.getValue());
        colorPickerLinea.setOnAction(e -> colorLinea = colorPickerLinea.getValue());
        colorPickerArco.setOnAction(e -> colorArco = colorPickerArco.getValue());
        colorPickerTexto.setOnAction(e -> colorTexto = colorPickerTexto.getValue());
        colorPickerEdicion.setOnAction(e -> colorEdicion = colorPickerEdicion.getValue());

        textFieldTamanoTexto.setOnAction(e -> {
            try {
                tamanoTexto = Double.parseDouble(textFieldTamanoTexto.getText());
            } catch (NumberFormatException ex) {
                textFieldTamanoTexto.setText(String.format("%.0f", tamanoTexto));
            }
        });

        textFieldRadioArco.setOnAction(e -> {
            try {
                radioFijo = Double.parseDouble(textFieldRadioArco.getText());
            } catch (NumberFormatException ex) {
                textFieldRadioArco.setText(String.format("%.0f", radioFijo));
            }
        });
    }

    private void handleTextClick(double x, double y) {
        if (textoPendiente != null) {
            Text txt = new Text(x, y, textoPendiente);
            txt.setFill(colorTexto);
            txt.setFont(Font.font(tamanoTexto));  // Aquí aplicamos el tamaño de fuente correctamente
            makeDraggable(txt);
            paneCarta.getChildren().add(txt);
            textoPendiente = null;
        }
        resetModes();
    }

    @FXML
    public void seleccionarHerramientaMarcarPunto(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.POINT;
    }

    @FXML
    public void seleccionarTrazarLinea(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.LINE;
    }

    @FXML
    public void seleccionarTrazarArco(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.ARC;
    }

    @FXML
    public void onMenuColorPicked(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.EDIT_COLOR;
        colorPickerEdicion.show();
    }

    @FXML
    public void seleccionarEliminarElemento(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.DELETE;
    }

    @FXML
    public void seleccionarLimpiarCarta(ActionEvent e) {
        paneCarta.getChildren().removeIf(n
                -> n instanceof Shape
                || n instanceof Text
                || n instanceof Group
        );
        resetModes();
    }

    public void seleccionarCompas(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.COMPASS;
    }

    @FXML
    public void seleccionarTransportador(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.PROTRACTOR;
        ivOverlay.setVisible(true);
    }

    @FXML
    public void onEditarPerfil(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PerfilPanel.fxml"));
            Parent root = loader.load();
            FXMLTrabajoController controlador = loader.getController();
            controlador.initUser(nick, email, pass, avatar, birthday);
            
            Stage dialog = new Stage();
            dialog.setTitle("Editar perfil");
            dialog.initOwner(paneCarta.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
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
            textoPendiente = s;
            resetModes();
            markingEnabled = true;
            currentTool = Tool.TEXT;
        });
    }

    private void listClicked(MouseEvent e) {

        Poi poi = map_listview.getSelectionModel().getSelectedItem();
        if (poi == null) {
            return;
        }

        double contentWidth = zoomGroup.getBoundsInLocal().getWidth() * zoom_slider.getValue();
        double contentHeight = zoomGroup.getBoundsInLocal().getHeight() * zoom_slider.getValue();
        double viewportW = map_scrollpane.getViewportBounds().getWidth();
        double viewportH = map_scrollpane.getViewportBounds().getHeight();

        double h = (poi.getX() * zoom_slider.getValue() - viewportW / 2) / (contentWidth - viewportW);
        double v = (poi.getY() * zoom_slider.getValue() - viewportH / 2) / (contentHeight - viewportH);

        h = Math.max(0, Math.min(1, h));
        v = Math.max(0, Math.min(1, v));

        map_scrollpane.setHvalue(h);
        map_scrollpane.setVvalue(v);
    }

    @FXML
    private void addPoi(MouseEvent e) {
        if (!e.isControlDown()) {
            return;
        }

        Dialog<Poi> dlg = new Dialog<>();
        dlg.setTitle("Nuevo POI");
        dlg.setHeaderText("Introduce un nuevo POI");

        ButtonType ok = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

        TextField name = new TextField();
        name.setPromptText("Nombre del POI");
        TextArea desc = new TextArea();
        desc.setPromptText("Descripción...");
        desc.setWrapText(true);
        desc.setPrefRowCount(5);

        dlg.getDialogPane().setContent(new VBox(10, new Label("Nombre:"), name, new Label("Descripción:"), desc));
        dlg.setResultConverter(btn -> btn == ok
                ? new Poi(name.getText().trim(), desc.getText().trim(), 0, 0)
                : null
        );

        Optional<Poi> res = dlg.showAndWait();
        res.ifPresent(poi -> {
            Point2D lp = new Point2D(e.getX(), e.getY());
            poi.setPosition(lp);
            map_listview.getItems().add(poi);
        });
    }

    @FXML
    public void seleccionarMarcaCirculo(ActionEvent e) {
        tipoPunto = "circulo";
    }

    @FXML
    public void seleccionarMarcaEstrella(ActionEvent e) {
        tipoPunto = "estrella";
    }

    @FXML
    public void seleccionarMarcaAsterisco(ActionEvent e) {
        tipoPunto = "asterisco";
    }

    @FXML
    public void seleccionarMarcaCruz(ActionEvent e) {
        tipoPunto = "cruz";
    }

    @FXML
    public void seleccionarEditarColor(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.EDIT_COLOR;
        colorPickerEdicion.show();
    }

    @FXML
    public void seleccionarLatLonMarker(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.LATLON_MARKER;
    }

    @FXML
    private void about(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("IPC - 2025");
        alert.showAndWait();
    }

    @FXML
    public void zoomIn(ActionEvent e) {
        zoom_slider.setValue(zoom_slider.getValue() + 0.1);
    }

    @FXML
    public void zoomOut(ActionEvent e) {
        zoom_slider.setValue(zoom_slider.getValue() - 0.1);
    }

    private void resetModes() {
        currentTool = Tool.NONE;
        markingEnabled = false;
        lineStarted = arcStarted = false;
        compasP1 = compasP2 = rulerP1 = rulerP2 = null;
    }

    private void zoom(double s) {
        double h = map_scrollpane.getHvalue(), v = map_scrollpane.getVvalue();
        zoomGroup.setScaleX(s);
        zoomGroup.setScaleY(s);
        map_scrollpane.setHvalue(h);
        map_scrollpane.setVvalue(v);
    }

    @FXML
    private void seleccionarArcoRadioFijo(ActionEvent e) {
        modoArco = ModoArco.RADIO_FIJO;
        seleccionarTrazarArco(e);
    }

    @FXML
    private void seleccionarArcoRadioLibre(ActionEvent e) {
        modoArco = ModoArco.RADIO_LIBRE;
        seleccionarTrazarArco(e);
    }

    @FXML
    public void seleccionarRegla(ActionEvent e) {
        resetModes();
        markingEnabled = true;
        currentTool = Tool.RULER;
        // Cuando pinches en el mapa se creará y se activarán estos handlers
    }

    private void openRotationWindowNormal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RotarRegla.fxml"));
            Parent root = loader.load();

            Stage rotationStage = new Stage();
            rotationStage.setTitle("Rotar Regla");
            rotationStage.initModality(Modality.APPLICATION_MODAL); // Ventana modal
            rotationStage.setScene(new Scene(root));

            RotarReglaController rotationController = loader.getController();
            rotationController.setRuleImageView(ivRegla);

            rotationStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupReglaDragHandlers(ImageView ivRegla, Pane paneCarta) {
        ivRegla.setOnMousePressed(event -> {
            Point2D mouse = paneCarta.sceneToLocal(event.getSceneX(), event.getSceneY());
            dragDelta.x = mouse.getX() - ivRegla.getLayoutX();
            dragDelta.y = mouse.getY() - ivRegla.getLayoutY();
            event.consume();
        });

        ivRegla.setOnMouseDragged(event -> {
            Point2D mouse = paneCarta.sceneToLocal(event.getSceneX(), event.getSceneY());
            double newX = mouse.getX() - dragDelta.x;
            double newY = mouse.getY() - dragDelta.y;

            ivRegla.setLayoutX(newX);
            ivRegla.setLayoutY(newY);

            event.consume();
        });

        ivRegla.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                ivRegla.setVisible(false);
                resetModes();
                markingEnabled = false;
                currentTool = Tool.NONE;
                event.consume();
            }
        });
    }

}
