package Model;

import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion {

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost/codigo_proyectos", "codigo_proyectos", "P}3LdHI^+WMV");
//           con = DriverManager.getConnection("jdbc:mysql://190.8.176.243:3306/codigo_proyectos", "codigo_proyectos", "P}3LdHI^+WMV");
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return con;
    }
}
