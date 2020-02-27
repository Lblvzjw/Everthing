import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Creat with IntelliJ IDEA.
 * Description：
 * User:LiuBen
 * Date:2020-01-09
 * Time:11:28
 */


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(
                getClass().getClassLoader()
                        .getResource("app.fxml"));
        primaryStage.setTitle("文件搜索");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
