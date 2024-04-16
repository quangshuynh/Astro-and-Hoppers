package puzzles.astro.gui;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

public class AstroGUI extends Application implements Observer<HoppersModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image robot = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"robot-blue.png"));
    private BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("resources/space.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    private Background background = new Background(backgroundImage);

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;

    public void init() {
        String filename = getParameters().getRaw().get(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Button button = new Button();
        button.setGraphic(new ImageView(robot));
        button.setBackground(background);
        button.setMinSize(ICON_SIZE, ICON_SIZE);
        button.setMaxSize(ICON_SIZE, ICON_SIZE);
        Scene scene = new Scene(button);
        stage.setScene(scene);
        stage.setTitle("AstroGUI");
        stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java AstroGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
