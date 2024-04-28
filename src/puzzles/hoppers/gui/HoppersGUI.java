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

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    private Label status;
    private GridPane game;
    private Stage stage;
    private String filename;
    private HoppersModel model;
    private FileChooser fileChooser;
    private Label selectedLabel;
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /** Images */
    private final Image redFrog = getResourceIMG("red_frog.png");
    private final Image greenFrog = getResourceIMG("green_frog.png");
    private final Image lilyPad = getResourceIMG("lily_pad.png");
    private final BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("resources/water.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    private final Background background = new Background(backgroundImage);

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;

    public void init() throws IOException {
        filename = getParameters().getRaw().get(0);
        model = new HoppersModel(filename);
        model.addObserver(this);
        fileChooser = new FileChooser();
        this.fileChooser.setTitle("Open Hoppers File");
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
        this.fileChooser.setInitialDirectory(new File(Paths.get(".").toAbsolutePath().normalize().toString()));
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane main = new BorderPane(); // main borderpane for everything
        status = new Label("");  // initialize status

        /** Game Grid (center) */
        game = new GridPane();  // game grid
        game.setPadding(new Insets(10, 10, 10, 10));  // set padding
        for(int row = 0; row < model.getRow(); row++) {
            for(int col = 0; col < model.getCol(); col++) {
                Label tile = new Label("");
                tile.setMinSize(ICON_SIZE, ICON_SIZE);
                tile.setAlignment(Pos.CENTER);
                tile.setBackground(background);
                int r = row;
                int c = col;
                //tile.setOnMouseClicked(e -> select(tile, r, c));
                GridPane.setRowIndex(tile, row);
                GridPane.setColumnIndex(tile, col);
                game.getChildren().add(tile);
            }
        }

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

        /** SetOnAction */
        load.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){
                File astroFile = new File(file.getPath());
                String astroFilename = astroFile.getName();
                filename = astroFilename;
                this.model.load(astroFilename);
            }
        });
        hint.setOnAction(e -> model.hint());
        reset.setOnAction(e -> model.reset());

        /** Status (top) */
        HBox top = new HBox();
        status.setStyle("-fx-font-size:15");
        top.getChildren().add(status);
        top.setAlignment(Pos.CENTER);

        /** Main adding */
        main.setCenter(game);
        main.setTop(top);
        main.setBottom(fp);


        /** Scene */
        Scene scene = new Scene(main);
        this.stage = stage;
        this.stage.setScene(scene);
        this.stage.setTitle("Hoppers GUI");
        this.stage.show();
        update(model, "");
        model.notifyLoad(filename);
        main.requestFocus();
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        game.getChildren().clear();

        /** Updating game grid */
        for(int row = 0; row < hoppersModel.getRow(); row++) {
            for(int col = 0; col < hoppersModel.getCol(); col++) {
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
                    case '*' -> label.setGraphic(new ImageView(lilyPad));
                    case 'R' -> label.setGraphic(new ImageView(redFrog));
                    case 'G' -> label.setGraphic(new ImageView(greenFrog));
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

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}