package Model;

import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion {

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost/evaluacion_colegios", "root", "");
//            con = DriverManager.getConnection("jdbc:mysql://www.caleb.colombiahosting.com.co:2083/salvav_prueba", "salvav", "nikol0387");
//            con = DriverManager.getConnection("jdbc:mysql://www.db4free.net:3306/cuestionarios", "nikol0387", "m1113626301");
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return con;
    }
}
