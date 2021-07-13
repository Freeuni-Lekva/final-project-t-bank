package com.example.T_Bank;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();


        dataSource.setUrl("jdbc:mysql://localhost:3306/test_db");
        dataSource.setUsername("root");
        dataSource.setPassword("KobaSaba123$");

        Connection connection = dataSource.getConnection();
        Statement st = connection.createStatement();

        String query = "select * from products";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            System.out.println(rs.getString(1));
        }


    }
}
