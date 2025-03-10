import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("MIST Osmany Hall");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);  // Set full-screen mode
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
