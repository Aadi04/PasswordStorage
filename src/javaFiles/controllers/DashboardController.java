package javaFiles.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController
{

//    @FXML
//    private JFXTextField websiteTextField;
    
    private static Stage stage = new Stage();

    public void showDashboardWindow() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/dashboard.fxml"));


        Stage stage = getDashboardStage();

        Scene scene = new Scene(root);



        stage.setTitle("Sign-Up Page");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public Stage getDashboardStage()
    {
        return stage;
    }


}
