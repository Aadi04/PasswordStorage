package javaFiles.controllers;

import com.jfoenix.controls.JFXTextField;
import javaFiles.util.DbConnection;
import javaFiles.util.UserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardController implements Initializable
{
    private String currentUser = LoginFormController.getCurrentUser();
    private String tableToOpen = currentUser + "_database";

    private static Stage stage = new Stage();

    private LoginFormController loginFormController;

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

    private ObservableList<UserData> data = FXCollections.observableArrayList();;

    private DbConnection dbConnection;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            notes.setWrapText(true);
            loadData();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    private void loadData() throws SQLException
    {
        Connection connection = DbConnection.connection();
        //PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Tester3_database" );
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM " + tableToOpen);

        while (resultSet.next())
        {
            data.add(new UserData(resultSet.getString("Website"),resultSet.getString("Username"), resultSet.getString("Password"), resultSet.getString("Notes")));
        }

        this.websiteCol.setCellValueFactory(new PropertyValueFactory<>("Website"));
        this.usernameCol.setCellValueFactory(new PropertyValueFactory<>("Username"));
        this.passwordCol.setCellValueFactory(new PropertyValueFactory<>("Password"));
        this.notesCol.setCellValueFactory(new PropertyValueFactory<>("Notes"));

        dataTableview.setItems(data);


        connection.close();
        resultSet.close();
    }

    private void addData() throws SQLException
    {
        Connection conn = DbConnection.connection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO " + tableToOpen + "(Website,Username,Password,Notes) VALUES (?,?,?,?)");

        ps.setString(1,this.website.getText());
        ps.setString(2,this.username.getText());
        ps.setString(3,this.password.getText());
        ps.setString(4,this.notes.getText());

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

    private void clearTableView()
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

}

