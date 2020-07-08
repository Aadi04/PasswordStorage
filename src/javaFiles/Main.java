package javaFiles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application
{

    private static Stage mainStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage = getMainStage();

        Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/loginPage.fxml"));
        primaryStage.setTitle("Login Page");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static Stage getMainStage()
    {
        return mainStage;
    }

    public static void main(String[] args) throws SQLException
    {
        launch(args);
    }
}
