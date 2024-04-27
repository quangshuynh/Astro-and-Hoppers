package puzzles.astro.gui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import puzzles.astro.model.AstroModel;
import puzzles.common.Coordinates;
import puzzles.common.Direction;
import puzzles.common.Observer;
import puzzles.common.solver.Move;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;


/**
 * AstroGUI
 *
 * @author Quang Huynh
 */
public class AstroGUI extends Application implements Observer<AstroModel, String> {
    private AstroModel model;  // astro model
    private String filename;  // file name of astro
    private Label status;  // game status
    private GridPane game;  // gridpane of game
    private Label selectedLabel; // selected box
    private FileChooser fileChooser; // file chooser
    private Stage stage; // gui stage
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    /** Images */
    private final Image astronaut = getResourceIMG("astro.png");
    private final Image earthGoal = getResourceIMG("earth.png");
    private final Image blueRobot = getResourceIMG("robot-blue.png");
    private final Image greenRobot = getResourceIMG("robot-green.png");
    private final Image lightblueRobot = getResourceIMG("robot-lightblue.png");
    private final Image orangeRobot = getResourceIMG("robot-orange.png");
    private final Image pinkRobot = getResourceIMG("robot-pink.png");
    private final Image purpleRobot = getResourceIMG("robot-purple.png");
    private final Image whiteRobot = getResourceIMG("robot-white.png");
    private final Image yellowRobot = getResourceIMG("robot-yellow.png");
    private final BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("resources/space.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    private final Background background = new Background(backgroundImage);

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;


    /**
     * Initialize AstroModel with getting filename and add observer
     */
    public void init() throws IOException {
        filename = getParameters().getRaw().get(0);
        model = new AstroModel(filename);
        model.addObserver(this);
        fileChooser = new FileChooser();
        this.fileChooser.setTitle("Open Astro File");
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
        this.fileChooser.setInitialDirectory(new File(Paths.get(".").toAbsolutePath().normalize().toString()));
    }

    /**
     * Construct the layout for Astro
     *
     * @param stage container (window) in which to render the GUI
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane main = new BorderPane(); // main borderpane for everything
        status = new Label("");  // initialize status

        /** Game Grid (center) */
        game = new GridPane();  // game grid
        game.setPadding(new Insets(10, 10, 10, 10));  // set padding
        game.setVgap(1);  // vertical gap inbetween tiles
        game.setHgap(1);  // horizontal gap inbetween tiles
        for(int row = 0; row < model.getRow(); row++) {
            for(int col = 0; col < model.getCol(); col++) {
                Label tile = new Label("");
                tile.setMinSize(ICON_SIZE, ICON_SIZE);
                tile.setAlignment(Pos.CENTER);
                tile.setBackground(background);
                int r = row;
                int c = col;
                tile.setOnMouseClicked(e -> select(tile, r, c));
                GridPane.setRowIndex(tile, row);
                GridPane.setColumnIndex(tile, col);
                game.getChildren().add(tile);
            }
        }

        /** Direction Buttons (right) */
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setAlignment(Pos.CENTER);
        Button north = new Button("N");
        Button south = new Button("S");
        Button east = new Button("E");
        Button west = new Button("W");
        north.setStyle("-fx-font-weight: bold; -fx-font-size:16");
        south.setStyle("-fx-font-weight: bold; -fx-font-size:16");
        east.setStyle("-fx-font-weight: bold; -fx-font-size:16");
        west.setStyle("-fx-font-weight: bold; -fx-font-size:16");
        buttonsGrid.add(north, 1, 0);
        buttonsGrid.add(south, 1, 2);
        buttonsGrid.add(east, 2, 1);
        buttonsGrid.add(west, 0, 1);
        VBox directions = new VBox();
        directions.setAlignment(Pos.CENTER);
        directions.getChildren().addAll(buttonsGrid);
        buttonsGrid.setPadding(new Insets(0, 7, 0, 0));

        /** FlowPane Buttons (bottom) */
        FlowPane fp = new FlowPane();
        fp.setPadding(new Insets(2, 0, 5, 0));
        fp.setAlignment(Pos.CENTER);
        Button load = new Button("Load");
        Button reset = new Button("Reset");
        Button hint = new Button("Hint");
        load.setStyle("-fx-font-size:18");
        reset.setStyle("-fx-font-size:18");
        hint.setStyle("-fx-font-size:18");
        fp.getChildren().addAll(load, reset, hint);

        /** Status (top) */
        HBox top = new HBox();
        status.setStyle("-fx-font-size:15");
        top.getChildren().add(status);
        top.setAlignment(Pos.CENTER);

        /** SetOnAction */
        load.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){
                File astroFile = new File(file.getPath());
                String astroFilename = astroFile.getName();
                filename = astroFilename;
                this.model.loadPuzzle(astroFilename);
            }
        });
        hint.setOnAction(e -> model.getHint());
        reset.setOnAction(e -> model.resetPuzzle());
        north.setOnAction(e -> model.makeMove(Direction.NORTH));
        south.setOnAction(e -> model.makeMove(Direction.SOUTH));
        east.setOnAction(e -> model.makeMove(Direction.EAST));
        west.setOnAction(e -> model.makeMove(Direction.WEST));

        /** Main adding */
        main.setCenter(game);
        main.setRight(buttonsGrid);
        main.setBottom(fp);
        main.setTop(top);

        /** Scene */
        Scene scene = new Scene(main);
        this.stage = stage;
        this.stage.setScene(scene);
        this.stage.setTitle("AstroGUI");

        /** Keybinds */
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {  // keybinds
            if(!model.getCurrentConfig().isSolution()) {  // arrows only work if game is not over
                switch(event.getCode()) {
                    case UP, W:  // up arrow
                        model.makeMove(Direction.NORTH);
                        break;
                    case DOWN, S:  // down arrow
                        model.makeMove(Direction.SOUTH);
                        break;
                    case LEFT, A:  // left arrow
                        model.makeMove(Direction.WEST);
                        break;
                    case RIGHT, D:  // right arrow
                        model.makeMove(Direction.EAST);
                        break;
                    case SPACE:  // new game
                        File file = fileChooser.showOpenDialog(stage);
                        if(file != null) {
                            File astroFile = new File(file.getPath());
                            String astroFilename = astroFile.getName();
                            filename = astroFilename;
                            this.model.loadPuzzle(astroFilename);
                        }
                        break;
                    case R: // reset
                        model.resetPuzzle();
                        break;
                    case H:
                        model.getHint();
                        break;
                }
            } else {  // when game is over
                if(event.getCode() == KeyCode.SPACE) {
                    File file = fileChooser.showOpenDialog(stage);
                    if(file != null) {
                        File astroFile = new File(file.getPath());
                        String astroFilename = astroFile.getName();
                        filename = astroFilename;
                        this.model.loadPuzzle(astroFilename);
                    }
                } else if(event.getCode() == KeyCode.R) {
                    model.resetPuzzle();
                }
            }
        });
        this.stage.show();
        update(model, "");
        model.notifyLoad(filename);
        main.requestFocus();
    }

    /**
     * Called by the model, model.AstroModel, whenever there is a state change
     * that needs to be updated by the GUI.
     *
     * @param astroModel the AstroModel
     * @param msg the status message sent by the model
     */
    @Override
    public void update(AstroModel astroModel, String msg) {
        game.getChildren().clear();  // clear all children after updating

        /** Updating game grid */
        for(int row = 0; row < astroModel.getRow(); row++) {
            for(int col = 0; col < astroModel.getCol(); col++) {
                Label tile = new Label("");
                tile.setMinSize(ICON_SIZE, ICON_SIZE);
                tile.setAlignment(Pos.CENTER);
                tile.setBackground(background);
                int r = row;
                int c = col;
                tile.setOnMouseClicked(e -> select(tile, r, c));
                GridPane.setRowIndex(tile, row);
                GridPane.setColumnIndex(tile, col);
                game.getChildren().add(tile);
            }
        }

        /** Updating cell tiles */
        for(Node child : game.getChildren()) {
            if(child instanceof Label label) {
                int row = GridPane.getRowIndex(label);
                int col = GridPane.getColumnIndex(label);
                Coordinates coordinates = new Coordinates(row, col);
                String value = astroModel.getContent(coordinates);
                switch(value) {
                    case "A" -> label.setGraphic(new ImageView(astronaut));
                    case "*" -> label.setGraphic(new ImageView(earthGoal));
                    case "B" -> label.setGraphic(new ImageView(blueRobot));
                    case "C" -> label.setGraphic(new ImageView(greenRobot));
                    case "D" -> label.setGraphic(new ImageView(lightblueRobot));
                    case "E" -> label.setGraphic(new ImageView(orangeRobot));
                    case "F" -> label.setGraphic(new ImageView(pinkRobot));
                    case "G" -> label.setGraphic(new ImageView(purpleRobot));
                    case "H" -> label.setGraphic(new ImageView(whiteRobot));
                    case "I" -> label.setGraphic(new ImageView(yellowRobot));
                }
            }
            status.setText(msg);
            stage.sizeToScene();
        }
    }

    /**
     * Selecting a tile from game
     *
     * @param clicked selected box that is clicked
     */
    private void select(Label clicked, int row, int col) {
        if(selectedLabel != null) {
            selectedLabel.setStyle("-fx-border-width: 0;");
        }
        model.select(row, col);  // notify observer & select
        selectedLabel = clicked;
        clicked.setStyle("-fx-border-color: red; -fx-border-width: 2px;");  // border doesnt work anymore?
    }

    /**
     * Gets resource image
     *
     * @param resource resource file
     * @return resource image
     */
    public Image getResourceIMG(String resource) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + resource)));
    }

    /**
     * Run the main program
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AstroGUI filename");
        } else {
            Application.launch(args);
        }
    }
}