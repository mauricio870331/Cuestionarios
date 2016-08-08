/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.*;

/**
 *
 * @author Mauricio Herrera
 */
public class ResultadosDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;
    //Resultados resultado;

    public ResultadosDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
       // resultado = new Resultados(0, 0, 0, 0, 0, "", false, "");
    }
    
     public boolean create(Resultados resultado, String opc) {
        boolean responseCreate = false;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO test_c_resultados (id_cuestionario, id_grupo, id_user, nota,  tiempo, aprobo, fecha_presentacion) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE test_asignaturas SET nombre_asig = ? WHERE id_asignatura = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, resultado.getId_cuestionario());
            pstm.setInt(2, resultado.getId_grupo());
            pstm.setInt(3, resultado.getId_user());
            pstm.setDouble(4, resultado.getNota());
            pstm.setString(5, resultado.getTiempo());
            pstm.setBoolean(6, resultado.isAprobo());
            pstm.setString(7, resultado.getFecha_presentacion());            
            if (opc.equals("U")) {
                //pstm.setInt(2, a.getAsignatura());
            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
              responseCreate = true;
            }
        } catch (Exception e) {
            System.out.println("error: " + e + " " + e.getClass());
        }

        return responseCreate;
    }

}
