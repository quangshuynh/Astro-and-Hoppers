package puzzles.astro.gui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import puzzles.astro.model.AstroModel;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AstroGUI extends Application implements Observer<AstroModel, String> {
    private AstroModel model;  // astro model
    private String filename;  // file name of astro
    private Label status;  // game status
    private GridPane game;  // gridpane of game
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    /** Images */
    private Image astronaut = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"astro.png"));
    private Image earthGoal = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"earth.png"));
    private Image blueRobot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-blue.png"));
    private Image greenRobot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-green.png"));
    private Image lightblueRobot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-lightblue.png"));
    private Image orangeRobot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-orange.png"));
    private Image pinkRobot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-pink.png"));
    private Image purpleRobot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-purple.png"));
    private Image whiteRobot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-white.png"));
    private Image yellowRobot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-yellow.png"));

    private BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("resources/space.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    private Background background = new Background(backgroundImage);

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;


    /**
     * Initialize AstroModel with getting filename and add observer
     */
    public void init() throws IOException {
            filename = getParameters().getRaw().get(0);
            model = new AstroModel(filename);
            model.addObserver(this);
    }

    /**
     * Construct the layout for Astro
     *
     * @param stage container (window) in which to render the GUI
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane main = new BorderPane(); // main borderpane for everything

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
               // tile.setGraphic(new ImageView(earth));
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
        status = new Label("Loaded: " + filename); // filename
        status.setStyle("-fx-font-size:15");
        top.getChildren().add(status);
        top.setAlignment(Pos.CENTER);

        /** SetOnAction */
        load.setOnAction(e -> model.loadPuzzle(filename));
        hint.setOnAction(e -> model.getHint());
        reset.setOnAction(e -> model.resetPuzzle());


        /** Main adding */
        main.setCenter(game);
        main.setRight(buttonsGrid);
        main.setBottom(fp);
        main.setTop(top);

        /** Scene */
        Scene scene = new Scene(main);
        stage.setScene(scene);
        stage.setTitle("AstroGUI");
        stage.show();
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
        for(Node child : game.getChildren()) {
            if(child instanceof Label label) {
                int row = GridPane.getRowIndex(label);
                int col = GridPane.getColumnIndex(label);
                String value = astroModel.getContent(row, col);
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
        }
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
