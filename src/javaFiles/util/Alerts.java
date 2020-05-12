package javaFiles.util;

import javaFiles.controllers.SignUpPageController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts
{
    private static SignUpPageController signUpPageController = new SignUpPageController();

    public static void displayConfirmationAlert(String title, String message, boolean closeWindow)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);

        //Making it so hitting ok or cancel button will also close the Sign Up window
        if(!closeWindow)
        {
            alert.showAndWait();
        }
        else
        {
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if(button == ButtonType.OK || button == ButtonType.CANCEL)
            {
                try
                {
                    signUpPageController.getSignUpStage().close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void displayConfirmationAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void displayErrorAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void displayWarningAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
