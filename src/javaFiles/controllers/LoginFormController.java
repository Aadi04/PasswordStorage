package javaFiles.controllers;

import javaFiles.controllers.SignUpPageController;
import javaFiles.util.Alerts;
import javaFiles.util.DbConnection;
import javaFiles.util.EncryptionSystem;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    public PasswordField passwordField;

    @FXML
    private TextField usernameField;

    SignUpPageController signUpPageController = new SignUpPageController();
    DashboardController dashboardController = new DashboardController();

    public void signInMouseClicked(MouseEvent mouseEvent) throws SQLException, NoSuchAlgorithmException, IOException
    {
        //showAlerts(usernameField.getText(), passwordField.getText());
        dashboardController.showDashboardWindow();

    }

    public void onEnterKey(KeyEvent keyEvent)
    {

    }

    private boolean doesUserNameExistInDatabase(String username) throws SQLException
    {
        Connection con = DbConnection.connection();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM loginData WHERE Username = ?");
        ps.setString(1,username);

        System.out.println("Excuteing Query");
        ResultSet rs = ps.executeQuery();

        if(rs.next())
        {
            return true;
        }

        return false;
    }

    private byte[] getSaltFromDatabase(String username) throws SQLException
    {
        byte[] salt = new byte[5];

        Connection connection = DbConnection.connection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM  loginData WHERE Username = ?");

        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if(rs.next())
        {
             salt = rs.getBytes("Salt");
        }

        ps.close();
        rs.close();
        connection.close();

        return salt;
    }

    private String getHashFromDatabase(String username, String pass) throws SQLException
    {
       String hash = null;
        Connection connection = DbConnection.connection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM  loginData WHERE Username = ?");

        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if(rs.next())
        {
            hash = rs.getString("Password");
        }

        return hash;
    }

    private boolean isItTheCorrectPassword(String username, String pass) throws SQLException, NoSuchAlgorithmException
    {
        byte[] salt = getSaltFromDatabase(username);
        String hash = getHashFromDatabase(username, pass);

        //Generating a new hash based on the entered password
        String generateNewHash = EncryptionSystem.generateHash(pass,salt);

        if(generateNewHash.equals(hash))
        {
            return true;
        }

        return false;
    }

    private boolean showAlerts (String username, String pass) throws SQLException, NoSuchAlgorithmException
    {
        if(username.isEmpty() || pass.isEmpty())
        {
            Alerts.displayErrorAlert("Error", "Please Fill Everything Out");
            return false;
        }

        else if (!doesUserNameExistInDatabase(username))
        {
            Alerts.displayErrorAlert("Error", "Username Doesn't Exist, Please Create An Account");
            return false;
        }

        else if(!isItTheCorrectPassword(username,pass))
        {
            Alerts.displayErrorAlert("Error","Incorrect Password");
            return false;
        }

        else
        {
            Alerts.displayConfirmationAlert("You are in!", "You are in!");
            return true;
        }
    }

    public void onSignUpClicked(MouseEvent mouseEvent) throws IOException
    {
        signUpPageController.showSignUpWindow();
    }
}
