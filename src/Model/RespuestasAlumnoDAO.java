/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Mauricio Herrera
 */
public class RespuestasAlumnoDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public RespuestasAlumnoDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public String Create(RespuestasAlumno ra, String opc) {
        String responseCreate = "";
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO respuestas_alumno (id_pregunta, id_respuesta) VALUES (?, ?)";
            }
            if (opc.equals("U")) {
                sql = "";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, ra.getIdPregunta());
            pstm.setInt(2, ra.getIdRespuesta());
            //            if (opc.equals("U")) {
//
//            }
//            if (opc.equals("C")) {
//
//            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {

                if (opc.equals("C")) {
                    responseCreate = "Registro creado con exito";
                } else {
                    responseCreate = "Registro actualizado con exito";
                }
            }
        } catch (SQLException e) {
            System.out.println("error = " + e);
        }
        return responseCreate;
    }

}
