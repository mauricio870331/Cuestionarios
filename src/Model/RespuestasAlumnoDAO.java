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

    public boolean Create(ArrayList<RespuestasAlumno> objRespuestasAlumno, int idca, String opc) {
        boolean responseCreate = false;
        try {
            if (opc.equals("C")) {
                Iterator<RespuestasAlumno> nombreIterator = objRespuestasAlumno.iterator();
                while (nombreIterator.hasNext()) {
                    RespuestasAlumno ra = nombreIterator.next();
                    if (ra.getIdPregunta() >= 0 && ra.getIdRespuesta() >= 0) {
                        sql = "SELECT * FROM respuestas_alumno where id_pregunta = " + ra.getIdPregunta() + " "
                                + "AND id_respuesta = " + ra.getIdRespuesta() + " "
                                + "AND id_c_alumno = " + idca + "";
                        pstm = cn.prepareStatement(sql);
                        rs = pstm.executeQuery();
                        if (rs.next()) {
                            System.out.println("ya esta");
                            responseCreate = true;
                        } else {
                            sql = "INSERT INTO respuestas_alumno (id_pregunta, id_respuesta, id_c_alumno) VALUES (?, ?, ?)";
                            pstm = cn.prepareStatement(sql);
                            pstm.setInt(1, ra.getIdPregunta());
                            pstm.setInt(2, ra.getIdRespuesta());
                            pstm.setInt(3, idca);
                            int rowAfected = pstm.executeUpdate();
                            if (rowAfected > 0) {
                                responseCreate = true;
                            }
                        }
                    } else {
                        responseCreate = true;
                    }

                }
            }
        } catch (SQLException e) {
            System.out.println("RespuestasAlumnoDao = " + e);
        }
        return responseCreate;
    }

}
