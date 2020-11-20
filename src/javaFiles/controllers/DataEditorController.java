package javaFiles.controllers;

import javaFiles.util.Alerts;
import javaFiles.util.DbConnection;
import javaFiles.util.EncryptionSystem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataEditorController implements Initializable
{

    DashboardController dashboardController;
   @FXML
    TextField usernameField,websiteField, passwordField;
   @FXML
    TextArea notesField;

    int selectedIndex;
    String selectedWeb;
    String selectedUser;
    String selectedPass;
    String selectedNotes;

    String tableToOpen = LoginFormController.getCurrentUser() + "_database";

    public DataEditorController() throws SQLException
    {
        selectedIndex = DashboardController.getSelectedIndex();
        selectedWeb = DashboardController.getSelectedWebsite();
        selectedUser = DashboardController.getSelectedUsername();
        selectedPass = getPasswordFromDatabase();
        selectedNotes = DashboardController.getSelectedNotes();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        notesField.setWrapText(true);

        websiteField.setText(selectedWeb);
        usernameField.setText(selectedUser);
        passwordField.setText(selectedPass);
        notesField.setText(selectedNotes);
    }

    public void saveButtonPressed(MouseEvent mouseEvent) throws SQLException
    {
      saveData();
      dashboardController.getDataEditorStage().close();
      dashboardController.clearTableView();
      dashboardController.loadData();
    }

    public void deleteButtonPressed(MouseEvent mouseEvent) throws SQLException
    {
        System.out.println("Selected Web: " + selectedWeb);
        deleteData();
        dashboardController.getDataEditorStage().close();
        dashboardController.clearTableView();
        dashboardController.loadData();
    }

    private void deleteData() throws SQLException
    {
        Connection connection = DbConnection.connection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM " + tableToOpen + " WHERE Number = ?");
        ps.setInt(1,selectedIndex);

        Alerts.displayWarningAlert("Deleting Data", "You are about to delete these data, and it can not be reversed.");
        ps.execute();

        connection.close();
        ps.close();
    }

    private void saveData() throws SQLException
    {
        Connection connection = DbConnection.connection();
        PreparedStatement ps = connection.prepareStatement("UPDATE " + tableToOpen + " SET Website = ?, Username = ?, Password =?, Notes = ? " +
                "WHERE Number = ?");

        //For SET clause
        ps.setString(1,EncryptionSystem.basicEncryption(websiteField.getText()));
        ps.setString(2,EncryptionSystem.basicEncryption(usernameField.getText()));
        ps.setString(3,EncryptionSystem.basicEncryption(passwordField.getText()));
        ps.setString(4,EncryptionSystem.basicEncryption(notesField.getText()));
        //For WHERE clause
        ps.setInt(5,selectedIndex);

        ps.execute();

        connection.close();
        ps.close();
    }

    private String getPasswordFromDatabase() throws SQLException
    {
        String password = "";
        Connection connection = DbConnection.connection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableToOpen + " WHERE Number = ?");
        ps.setInt(1,selectedIndex);
        ResultSet rs = ps.executeQuery();

        while(rs.next())
            password = rs.getString("Password");

        connection.close();
        rs.close();

        if(!password.equals(""))
            return EncryptionSystem.basicDecryption(password);

        return "Its empty";
    }

    public void setDashboardController(DashboardController dashboardController)
    {
        this.dashboardController = dashboardController;
    }
}
