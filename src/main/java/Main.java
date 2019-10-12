import controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader laoder = new FXMLLoader(getClass().getResource("/fxml/mainWindow.fxml"));
        Parent root = laoder.load();
        MainWindowController controller = laoder.getController();
        controller.setHostServices(getHostServices());
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
