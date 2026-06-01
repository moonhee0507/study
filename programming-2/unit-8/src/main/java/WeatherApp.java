import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Weather Information App − Entry Point
 * 
 * Launches the JavaFX application and initializes the primary Stage.
 * The dynamic background is set here based on the current time of day,
 * and is refreshed whenever a new search is performed.
 * 
 * @author Hee Moon
 */
public class WeatherApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        WeatherPanel weatherPanel = new WeatherPanel();

        Scene scene = new Scene(weatherPanel.getRoot(), 700, 750);
        scene.getStylesheets().add(getClass().getResource("style.css") != null ? "style.css" : "");

        primaryStage.setTitle("Weather Information App");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
