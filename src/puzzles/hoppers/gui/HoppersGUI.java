package puzzles.hoppers.gui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.astro.model.AstroModel;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The GUI class representing the View and Controller of MVC model
 *
 * @author Kai Fan
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    private Label status; //the status label
    private GridPane game; //the main game pane
    private Stage stage; //the stage of the gui
    private String filename; //the file used for the current gui display
    private HoppersModel model; //the model of the MVC
    private FileChooser fileChooser; //a file chooser to allow file change
    private Label selectedLabel_1; //the first label selected
    private Coordinates selectedLabel_1_Coordinate; //the first label's coordinate
    private Label selectedLabel_2; //the second label selected
    private Coordinates selectedLabel_2_Coordinate; //the second label's coordinate
    private boolean validFirstSelect; //keep track if first select is valid

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    //below are images for the Hoppers gui
    private final Image redFrog = getResourceIMG("red_frog.png"); //the red frog
    private final Image greenFrog = getResourceIMG("green_frog.png"); //the green frog
    private final Image lilyPad = getResourceIMG("lily_pad.png"); //the lily pad indicating a valid jump space
    private final BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("resources/water.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT); //the background image
    private final Background background = new Background(backgroundImage); //the background of the gui

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;

    /**
     * the optional initialize method, in this case, it is used to initialize internal fields
     *
     * @throws IOException - if file corrupt or not found
     */
    public void init() throws IOException {
        filename = getParameters().getRaw().get(0); //getting the file name
        model = new HoppersModel(filename); //initializing new model
        model.addObserver(this); //register this view as an observer of model
        fileChooser = new FileChooser(); //a file chooser to be initialized
        this.fileChooser.setTitle("Open Hoppers File");
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
        this.fileChooser.setInitialDirectory(new File(Paths.get(".").toAbsolutePath().normalize().toString()));
    }

    /**
     * The start method of the Hoppers Gui, it sets everything GUI related up
     *
     * @param stage - the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception -
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane main = new BorderPane(); // main borderpane for other panes
        status = new Label("");  // initialize the game status
        //the center grid
        game = new GridPane();
        game.setPadding(new Insets(10, 10, 10, 10));
        for(int row = 0; row < model.getTotalRow(); row++) {
            for(int col = 0; col < model.getTotalCol(); col++) {
                int r = row;
                int c = col;
                Label tile = new Label("");
                tile.setMinSize(ICON_SIZE, ICON_SIZE);
                tile.setAlignment(Pos.CENTER);
                tile.setBackground(background);
                tile.setOnMouseClicked(e -> select(tile, r, c));
                GridPane.setRowIndex(tile, row);
                GridPane.setColumnIndex(tile, col);
                game.getChildren().add(tile);
            }
        }

        //all the buttons at the bottom of the GUI
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

        //the controller of MVC
        load.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){
                File hoppersFile = new File(file.getPath());
                String hoppersFilename = hoppersFile.getName();
                filename = hoppersFilename;
                this.model.load(hoppersFilename);
            }
        });
        hint.setOnAction(e -> model.hint());
        reset.setOnAction(e -> model.reset());

        //the status of the game, top of main border pane
        HBox top = new HBox();
        status.setStyle("-fx-font-size:15");
        top.getChildren().add(status);
        top.setAlignment(Pos.CENTER);

        //adding to the main pane
        main.setCenter(game);
        main.setTop(top);
        main.setBottom(fp);


        //setting up the scene
        Scene scene = new Scene(main);
        this.stage = stage;
        this.stage.setScene(scene);
        this.stage.setTitle("Hoppers GUI");
        this.stage.show();
        update(model, "");
        model.notifyLoad(filename);
        main.requestFocus();
    }

    /**
     * The update method used to change the GUI based on user interaction
     *
     * @param hoppersModel - the object that wishes to inform this object
     *                about something that has happened.
     * @param msg - optional data the server.model can send to the observer
     */
    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        game.getChildren().clear();

        //updating the grid
        for(int row = 0; row < hoppersModel.getTotalRow(); row++) {
            for(int col = 0; col < hoppersModel.getTotalCol(); col++) {
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
                char value = hoppersModel.getCellValue(coordinates);
                switch(value) {
                    case '.' -> label.setGraphic(new ImageView(lilyPad));
                    case 'R' -> label.setGraphic(new ImageView(redFrog));
                    case 'G' -> label.setGraphic(new ImageView(greenFrog));
                }
            }
            status.setText(msg);
            stage.sizeToScene();
        }
    }

    /**
     * A select method used to keep track of the cell selected
     *
     * @param clicked - the label that has been interacted with
     * @param row - the row of that label
     * @param col - the col of that label
     */
    private void select(Label clicked, int row, int col) {
        if(selectedLabel_1 != null) {
            selectedLabel_1.setStyle("-fx-border-width: 0;");
        }
        if(selectedLabel_1 == null && selectedLabel_1_Coordinate == null){
            selectedLabel_1 = clicked; //first select
            selectedLabel_1_Coordinate = new Coordinates(row, col);
            validFirstSelect = model.select(selectedLabel_1_Coordinate);
        }
        if(!validFirstSelect){ //reset if first select is invalid
            selectedLabel_1 = null;
            selectedLabel_1_Coordinate = null;
        }

        if(validFirstSelect && !clicked.equals(selectedLabel_1) && !new Coordinates(row, col).equals(selectedLabel_1_Coordinate)){ //second select
            selectedLabel_2 = clicked;
            selectedLabel_2_Coordinate = new Coordinates(row, col);
            moveIt();
        }
    }

    /**
     * A helper function that calls the model's move method and reset the selected states
     */
    private void moveIt(){
        model.move(selectedLabel_1_Coordinate, selectedLabel_2_Coordinate);  // notify observer & select
        //resetting the select
        selectedLabel_1 = null;
        selectedLabel_1_Coordinate = null;
        selectedLabel_2 = null;
        selectedLabel_2_Coordinate = null;
        validFirstSelect = false;
    }

    /**
     * Gets resource images
     *
     * @param resource - resource file
     * @return the image
     */
    public Image getResourceIMG(String resource) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + resource)));
    }

    /**
     * the main method of the GUI, it launches the GUI
     *
     * @param args - commandline input (expect a file input)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}