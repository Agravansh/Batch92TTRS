package com.abhi.TrainReservation.model;
import java.sql.*;
public class TrainDao {

    private static final String url = "jdbc:mysql://localhost:3306/train";
    private static final String userName = "root";
    private static final String password = "Vansh@123#";

    private static Connection con = null;

    public static Connection getConnection() throws SQLException,ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url,userName,password);
        return con;

    }

    public Train findTrain(String src, String dest) throws SQLException,ClassNotFoundException {
        getConnection();

        Train train = null;
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from train where source = '" +  src  + "' AND destination = '" + dest +"'");
            while(rs.next()) {
                train = new Train(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),rs.getDouble(5));
            }

        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        con.close();
        return train;

    }

}