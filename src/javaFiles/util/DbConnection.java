package javaFiles.util;

import java.sql.*;

public class DbConnection
{
    private static Connection connection = null;
    public static Connection connection() throws SQLException
    {
        if(connection ==null)
        {
            connection = DriverManager.getConnection("jdbc:sqlite:EmailStorageSystemDatabase.db");
        }
        else
        {
            connection.close();
            connection = DriverManager.getConnection("jdbc:sqlite:EmailStorageSystemDatabase.db");
        }

        return connection;
//        try
//        {
//            Class.forName("org.sqlite.JDBC");
//
//            return DriverManager.getConnection("jdbc:sqlite:EmailStorageSystemDatabase.db");
//        }
//        catch (SQLException | ClassNotFoundException e)
//        {
//            System.out.println(e);
//            return null;
//        }
    }

    public static void updateData(String username, String pass,String salt ) throws SQLException
    {
        try
        {
            Connection connection = connection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO loginData VALUES (?,?,?)");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, pass);
            preparedStatement.setString(3, salt);

            preparedStatement.executeUpdate();

            System.out.println("Data updated");

            connection.close();
            preparedStatement.close();
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

}

