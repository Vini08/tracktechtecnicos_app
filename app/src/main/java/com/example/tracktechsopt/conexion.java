package com.example.tracktechsopt;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexion {


    public Connection get_connection(){
        Connection c = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://173.249.58.126:3306/techtrack_ar?noAccessToProcedureBodies=true&useSSL=false", "root_tt88", "T4lKaBTr4DFcRwZ");
        } catch (SQLException ex) {
            Log.e("- - - - -  ERROR1", "ERROR con1:  - - - - -- - - -" + ex.getMessage());

        } catch (ClassNotFoundException e) {
            Log.e("- - - - -  ERROR1", "ERROR con2:  - - - - -- - - -" + e.getMessage());
        }
        return c;
    }

}
