package javaFiles.controllers;

import javaFiles.util.Alerts;
import javaFiles.util.DbConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataEditorController implements Initializable
{

    DashboardController dashboardController;
   @FXML
    TextField usernameField,websiteField, passwordField;
   @FXML
    TextArea notesField;
    private static Stage stage = new Stage();

    String selectedWeb = DashboardController.getSelectedWebsite();
    String selectedUser = DashboardController.getSelectedUsername();
    String selectedPass = DashboardController.getSelectedPassword();
    String selectedNotes = DashboardController.getSelectedNotes();

    String currentUser = LoginFormController.getCurrentUser() + "_database";
    

    public DataEditorController(DashboardController dashboardController)
    {
        this.dashboardController = dashboardController;
    }

    public void showDataEditorWindow() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/dataView.fxml"));

        Stage stage = getDataEditorStage();
        Scene scene = new Scene(root);

        stage.setTitle("Sign-Up Page");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private Stage getDataEditorStage()
    {
        return stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        notesField.setWrapText(true);

        websiteField.setText(DashboardController.getSelectedWebsite());
        usernameField.setText(DashboardController.getSelectedUsername());
        passwordField.setText(DashboardController.getSelectedPassword());
        notesField.setText(DashboardController.getSelectedNotes());
    }

    public void saveButtonPressed(MouseEvent mouseEvent)
    {

    }

    public void deleteButtonPressed(MouseEvent mouseEvent) throws SQLException
    {
        Connection connection = DbConnection.connection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM " + currentUser + " WHERE Website = ? AND Username = ? AND Password = ?");
        ps.setString(1,selectedWeb);
        ps.setString(2,selectedUser);
        ps.setString(3,selectedPass);

        Alerts.displayWarningAlert("Deleting Data", "You are about to delete these data, and it can not be reversed.");
        ps.execute();

        getDataEditorStage().close();


        connection.close();
        ps.close();
    }
}
