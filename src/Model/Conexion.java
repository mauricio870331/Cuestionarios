package Model;

import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion {

    private static Connection con = null;

    public static Connection getConexion(String clase) throws InstantiationException, IllegalAccessException {
        try {          
            
            if (con==null) {
//                String driver = "com.mysql.jdbc.Driver"; //el driver varia segun la DB que usemos
                String url = "jdbc:mysql://190.8.176.243:3306/codigo_proyectos";
                String pwd = "P}3LdHI^+WMV";
                String usr = "codigo_proyectos";
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                //con = DriverManager.getConnection("jdbc:mysql://localhost/codigo_proyectos", "codigo_proyectos", "P}3LdHI^+WMV");
                con = DriverManager.getConnection(url, usr, pwd);
                System.out.println("Conectionesfull de clase: " + clase);
            }

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return con;
    }

    public static Connection closeConexion() {
        con = null;
        return con;
    }
}
