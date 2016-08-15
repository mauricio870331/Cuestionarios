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

/**
 *
 * @author Mauricio Herrera
 */
public class RespuetasCuestionarioDAO {

     Connection cn;
    
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public RespuetasCuestionarioDAO()  {
       cn = Conexion.getConexion();
    }

    public ArrayList<RespuestasCuestionario> getRespuestasCuestionario(int idpregunta, int idCuestionario) {
        ArrayList ListRespuestas = new ArrayList();
        try {
            sql = "SELECT r.id_pregunta as id_pregunta,"
                       + "r.id_respuesta as id_respuesta,"
                       + "r.respuesta as respuesta,"
                       + "r.estado as estado"
                + " FROM test_respuestas_cuestionario r INNER JOIN test_preguntas_cuestionario p ON p.id = r.id_fk"
                + " INNER JOIN test_c_cuestionario c ON c.id_cuestionario = p.id_cuestionario AND c.id_cuestionario = "+idCuestionario+""
                + " WHERE r.id_pregunta = " + idpregunta + "";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                RespuestasCuestionario rc = new RespuestasCuestionario();
                rc.setIdPregunta(rs.getInt("id_pregunta"));
                rc.setIdRespuesta(rs.getInt("id_respuesta"));
                rc.setRespuesta(rs.getString("respuesta"));
                rc.setEstado(rs.getBoolean("estado"));
                ListRespuestas.add(rc);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListRespuestas;
    }

    public int getIdRespuesta(int pregunta, String rpta) {
        int idRpta = 0;
        try {
            sql = "SELECT id_respuesta FROM test_respuestas_cuestionario "
                + "WHERE id_pregunta = " + pregunta  + " AND respuesta LIKE '"+rpta+"%'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {               
                idRpta = rs.getInt("id_respuesta");             
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return idRpta;
    }

}
