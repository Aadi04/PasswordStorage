package javaFiles.controllers;

import javaFiles.util.DbConnection;
import javaFiles.util.EncryptionSystem;
import javaFiles.util.UserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable
{
    private String currentUser = LoginFormController.getCurrentUser();

    private String tableToOpen = currentUser + "_database";

    private static Stage dashboardStage = new Stage();
    private static Stage dataEdtiorStage = new Stage();

    DataEditorController dataEditorController;

    static String selectedWebsite = null;
    static String selectedUsername = null;
    static String selectedPassword= null;
    static String selectedNotes = null;

    @FXML
    private TextField website;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextArea notes;
    @FXML
    private Button addBtn;

    //TableView Items
    @FXML
    private TableView<UserData> dataTableview;
    @FXML
    private TableColumn<UserData,String> websiteCol;
    @FXML
    private TableColumn<UserData,String> usernameCol;
    @FXML
    private TableColumn<UserData,String> passwordCol;
    @FXML
    private TableColumn<UserData,String> notesCol;

    private static ObservableList<UserData> data = FXCollections.observableArrayList();;

    private DbConnection dbConnection;

    public void showDashboardWindow() throws IOException
    {
        //Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/dashboard.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = getDashboardStage();
        Scene scene = new Scene(root);

        stage.setTitle("Dashboard");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void showDataEditorWindow() throws IOException
    {
        //Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/dashboard.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/dataView.fxml"));
        Parent root = loader.load();

        DataEditorController dataEditorController = loader.getController();
        dataEditorController.setDashboardController(this);

        Stage stage = getDataEditorStage();
        Scene scene = new Scene(root);

        stage.setTitle("Edit Your Information");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public Stage getDashboardStage()
    {
        return dashboardStage;
    }

    public Stage getDataEditorStage()
    {
        return dataEdtiorStage;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            notes.setWrapText(true);
            doubleClickChecker();
            loadData();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public void loadData() throws SQLException
    {
        Connection connection = DbConnection.connection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableToOpen);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
        {
            data.add(new UserData(EncryptionSystem.basicDecryption(resultSet.getString("Website")),
                    EncryptionSystem.basicDecryption(resultSet.getString("Username")),
                    changePasswordFieldToStars(resultSet.getString("Password")),
                    EncryptionSystem.basicDecryption(resultSet.getString("Notes"))));
        }

        this.websiteCol.setCellValueFactory(new PropertyValueFactory<>("Website"));
        this.usernameCol.setCellValueFactory(new PropertyValueFactory<>("Username"));
        this.passwordCol.setCellValueFactory(new PropertyValueFactory<>("Password"));
        this.notesCol.setCellValueFactory(new PropertyValueFactory<>("Notes"));

        dataTableview.setItems(data);

        connection.close();
        preparedStatement.close();
        resultSet.close();
    }

    private void addData() throws SQLException
    {
        Connection conn = DbConnection.connection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tableToOpen + "(Website,Username,Password,Notes) VALUES (?,?,?,?)");

        ps.setString(1,EncryptionSystem.basicEncryption(this.website.getText()));
        ps.setString(2,EncryptionSystem.basicEncryption(this.username.getText()));
        ps.setString(3,EncryptionSystem.basicEncryption(this.password.getText()));
        ps.setString(4,EncryptionSystem.basicEncryption(this.notes.getText()));

        ps.execute();

        conn.close();
        ps.close();
    }

    private void clearTextField()
    {
        this.username.setText("");
        this.website.setText("");
        this.password.setText("");
        this.notes.setText("");
    }

    public void clearTableView()
    {
        dataTableview.getItems().clear();
    }

    public void addBtnClicked(MouseEvent mouseEvent) throws SQLException
    {
        clearTableView();
        addData();
        loadData();
        clearTextField();
    }

    private void doubleClickChecker()
    {
        dataTableview.setOnMousePressed(event ->
        {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2 && dataTableview.getSelectionModel().getSelectedItem() != null)
            {
                setSelectedWebsite(dataTableview.getSelectionModel().getSelectedItem().getWebsite());
                setSelectedUsername(dataTableview.getSelectionModel().getSelectedItem().getUsername());
                setSelectedPassword(dataTableview.getSelectionModel().getSelectedItem().getPassword());
                setSelectedNotes(dataTableview.getSelectionModel().getSelectedItem().getNotes());

                try
                {
                    //DataEditorController.showDataEditorWindow();
                    showDataEditorWindow();
                    dataTableview.getSelectionModel().clearSelection();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private String changePasswordFieldToStars(String string)
    {
        int length = string.length();
        String changedPass = "";
        char star = '*';

        for(int i = 1; i <= length; i++)
        {
            changedPass+=star;
        }

        return changedPass;
    }

    //Getters and Setters
    public static String getSelectedWebsite()
    {
        return selectedWebsite;
    }

    public void setSelectedWebsite(String selectedWebsite)
    {
        DashboardController.selectedWebsite = selectedWebsite;
    }

    public static String getSelectedUsername()
    {
        return selectedUsername;
    }

    public void setSelectedUsername(String username)
    {
        selectedUsername = username;
    }

    public static String getSelectedPassword()
    {
        return selectedPassword;
    }

    public void setSelectedPassword(String selectedPassword)
    {
        DashboardController.selectedPassword = selectedPassword;
    }

    public static String getSelectedNotes() {
        return selectedNotes;
    }

    public void setSelectedNotes(String selectedNotes)
    {
        DashboardController.selectedNotes = selectedNotes;
    }
}

