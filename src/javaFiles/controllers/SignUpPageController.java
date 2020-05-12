package javaFiles.controllers;

import javaFiles.util.Alerts;
import javaFiles.util.DbConnection;
import javaFiles.util.EncryptionSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpPageController
{
    @FXML
    public Button button;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField, retypePasswordField;

    private static Stage signUpStage = new Stage();

    private final int PASSWORD_LENGTH  = 6;

    public void showSignUpWindow() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/signUpPage.fxml"));


        Stage stage = getSignUpStage();

        Scene scene = new Scene(root);



        stage.setTitle("Sign-Up Page");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void clickedOnPasswordField(MouseEvent mouseEvent) throws SQLException
    {
        isUsernameAvailable(getUsernameFieldText());
    }

    public void signUpMouseClicked(MouseEvent mouseEvent) throws SQLException, NoSuchAlgorithmException
    {
        if(showAlerts())
        {
            storeDataIntoDatabase(getUsernameFieldText(), getPassFieldText());
        }

    }

    //Checking if user can take use this username
    private boolean isUsernameAvailable(String chosenUsername) throws SQLException
    {
        boolean flag = true;
        Connection con = DbConnection.connection();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM loginData WHERE Username = ?");
        ps.setString(1,chosenUsername);

        ResultSet rs = ps.executeQuery();

        if(rs.next())
            flag = false;


        if(flag)
        {
            if(getUsernameFieldText().contains(" "))
            {
                if (getUsernameFieldText().charAt(getUsernameFieldText().length()-1) == ' ')
                    showCheckMark();
                else
                showCross();
            }
            else
            {
                showCheckMark();
            }

        }
        else
            showCross();

        ps.close();
        rs.close();
        con.close();

        return flag;
    }

    private boolean isPassLongEnough()
    {
        if(getPassFieldText().length() < PASSWORD_LENGTH)
        {
            return false;
        }
        else
            return true;
    }

    private void storeDataIntoDatabase(String username, String password) throws SQLException, NoSuchAlgorithmException
    {
        boolean flag = false;

        Connection con = DbConnection.connection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        byte[] salt = getSalt();
        String hash = encryptThePassword(password, salt);

        String tableName = username + "_database";

        if(!flag)
        {
           ps = con.prepareStatement("INSERT INTO loginData VALUES (?,?,?)");

           ps.setString(1, username);
           ps.setString(2, hash);
           ps.setBytes(3, salt);

           ps.executeUpdate();

           ps.close();
           con.close();

           flag = true;
        }

        if (flag)
        {
            makeANewTable(tableName);
        }
    }

    //Making a new Table for every new user to store their data
    private void makeANewTable(String tableName) throws SQLException
    {
        Connection conn = DbConnection.connection();
        PreparedStatement ps = conn.prepareStatement("CREATE TABLE " + tableName + " (Username TEXT, Password TEXT, Salt TEXT)");

        ps.execute();

        conn.close();
        ps.close();
    }

    //Encrypting the Password Before storing it
    private String encryptThePassword(String password, byte[] salt) throws NoSuchAlgorithmException
    {
       return EncryptionSystem.generateHash(password, salt);
    }

    //Getting the Salt to encrypt the password
    private byte[] getSalt()
    {
        return EncryptionSystem.createSalt();
    }

    //To show alerts if any problem
    private boolean showAlerts() throws SQLException
    {
        //If the user puts space at the ending of the username then it will just remove that space
        if(!getUsernameFieldText().isEmpty())
        {
            if(getUsernameFieldText().charAt(getUsernameFieldText().length()-1) == ' ')
            usernameField.setText(getUsernameFieldText().substring(0, getUsernameFieldText().length()-1));
        }

        //Checking if anything is empty
        if (getUsernameFieldText().isEmpty() || getPassFieldText().isEmpty() || getReTypePassField().isEmpty())
        {
            Alerts.displayErrorAlert("Error", "Please Fill Everything Out");
            return false;
        }

        //Checking if there are any spaces in the username
        else if(getUsernameFieldText().contains(" "))
        {
            Alerts.displayErrorAlert("Error", "Can't Use A Username With Spaces. If you did not used spaces then " +
                    "check that after the entered username there aren't any spaces");
            return false;
        }

        else if (!isPassLongEnough())
        {
            Alerts.displayErrorAlert("Error", "Password Must Be Longer Than " + PASSWORD_LENGTH + " Digits");
            return false;
        }

        //Checking if everything the way it should be
        else if(getPassFieldText().equals(getReTypePassField()) && isUsernameAvailable(getUsernameFieldText()))
        {
            Alerts.displayConfirmationAlert("Account Made!", "Congratulations! Your account is made, please log in now ", true);
            return true;
        }

        //Checking if both password fields match
        else if (!getPassFieldText().equals(getReTypePassField()))
        {
            Alerts.displayErrorAlert("Passwords Do Not Match", "The Given Password In The Two Fields Do Not Match Each Other");
            return false;
        }

        //Checking if the username is actually available
        else if(!isUsernameAvailable(getUsernameFieldText()))
        {
            Alerts.displayErrorAlert("Username Already Taken", "Please Use Another Username, this one is taken");
            return false;
        }

        //If something else that's not accounted for happens
        else
        {
            Alerts.displayErrorAlert("Something Went Wrong", "Something Went Wrong, Please Double Check Everything");
            return false;
        }

    }

    //To indicate if username is available
    private void showCheckMark()
    {
        Image image = new Image("/resources/photos/checkMark.png");
        imageView.setImage(image);
    }

    //To indicate username is not available
    private void showCross()
    {
        Image image = new Image("/resources/photos/cross.png");
        imageView.setImage(image);
    }

    private String getUsernameFieldText()
    {
        return usernameField.getText();
    }

    private String getPassFieldText()
    {
        return passwordField.getText();
    }

    private String getReTypePassField()
    {
        return retypePasswordField.getText();
    }

    public Stage getSignUpStage()
    {
        return signUpStage;
    }





    //If Enter key is pressed then do the same thing as clicking the signUp button
    public void onEnterKey(KeyEvent keyEvent) throws SQLException, NoSuchAlgorithmException
    {

//        if(keyEvent.getCode() == KeyCode.ENTER)
//        {
//            if(showAlerts())
//            {
//                storeDataIntoDatabase(getUsernameFieldText(), getPassFieldText());
//            }
//        }
    }

}
