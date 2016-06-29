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
import java.util.ArrayList;
import java.util.Iterator;

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

    public String Create(ArrayList<RespuestasAlumno> objRespuestasAlumno, int idca, String opc) {
        String responseCreate = null;
        try {
            if (opc.equals("C")) {
                Iterator<RespuestasAlumno> nombreIterator = objRespuestasAlumno.iterator();
                while (nombreIterator.hasNext()) {
                    RespuestasAlumno ra = nombreIterator.next();
                    sql = "INSERT INTO respuestas_alumno (id_pregunta, id_respuesta, id_c_alumno) VALUES (?, ?, ?)";
                    pstm = cn.prepareStatement(sql);
                    pstm.setInt(1, ra.getIdPregunta());
                    pstm.setInt(2, ra.getIdRespuesta());
                    pstm.setInt(3, idca);
                    int rowAfected = pstm.executeUpdate();
                    if (rowAfected > 0) {
                        responseCreate = "Cuestionario guardado con exito";
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("RespuestasAlumnoDao = " + e);
        }
        return responseCreate;
    }

}
