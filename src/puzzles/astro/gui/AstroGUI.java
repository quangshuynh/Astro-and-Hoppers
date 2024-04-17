package puzzles.astro.gui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class AstroGUI extends Application implements Observer<AstroModel, String> {
    private AstroModel model;
    private String filename;
    private Label status;
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image robot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-blue.png"));
    private BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("resources/space.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    private Background background = new Background(backgroundImage);

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;

    /**
     * Initialize AstroModel with getting filename and add observer
     */
    public void init() {
        filename = getParameters().getRaw().get(0);
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
        GridPane game = new GridPane();  // game grid
        game.setPadding(new Insets(10, 10, 10, 10));  // set padding
        game.setVgap(1);  // vertical gap inbetween tiles
        game.setHgap(1);  // horizontal gap inbetween tiles
        for(int row = 0; row < 5; row++) {  // 5 rows (change to getRow later)
            for(int col = 0; col < 5; col++) {  // 5 columns (change to getCol later)
                Label tile = new Label("");
                tile.setMinSize(ICON_SIZE, ICON_SIZE);
                tile.setAlignment(Pos.CENTER);
                tile.setBackground(background);;
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
        String[] buttonLabels = {"Load", "Reset", "Hint"};
        for(String label : buttonLabels) {
            Button button = new Button(label);
            button.setStyle("-fx-font-size:18");
            fp.getChildren().add(button);
        }

        /** Status (top) */
        HBox top = new HBox();
        status = new Label("Loaded: " + filename); // filename
        status.setStyle("-fx-font-size:15");
        top.getChildren().add(status);
        top.setAlignment(Pos.CENTER);

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
        // todo
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
