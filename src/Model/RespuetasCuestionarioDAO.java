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

    public RespuetasCuestionarioDAO() {
        cn = Conexion.getConexion("RespuestasDao getRespuestasCuestionario");
    }

    public ArrayList<RespuestasCuestionario> getRespuestasCuestionario(int idpregunta, int idCuestionario) throws SQLException {
        ArrayList ListRespuestas = new ArrayList();
        try {

            sql = "SELECT r.id_pregunta as id_pregunta,"
                    + "r.id_respuesta as id_respuesta,"
                    + "r.respuesta as respuesta,"
                    + "r.estado as estado"
                    + " FROM test_respuestas_cuestionario r INNER JOIN test_preguntas_cuestionario p ON p.id = r.id_fk"
                    + " INNER JOIN test_c_cuestionario c ON c.id_cuestionario = p.id_cuestionario AND c.id_cuestionario = " + idCuestionario + ""
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
            System.out.println("error --" + e);
        } 
        return ListRespuestas;
    }

    public int getIdRespuesta(int pregunta, String rpta) {
        int idRpta = 0;
        try {
            sql = "SELECT id_respuesta FROM test_respuestas_cuestionario "
                    + "WHERE id_pregunta = " + pregunta + " AND respuesta LIKE '" + rpta + "%'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                idRpta = rs.getInt("id_respuesta");
            }
        } catch (Exception e) {
            System.out.println("error nn" + e);
        }
        return idRpta;
    }

    public ArrayList<RespuestasCuestionario> getRespuestasCuestionarioEdit(int idpregunta) throws SQLException {
        ArrayList ListRespuestas = new ArrayList();
        try {
            
            sql = "SELECT r.id_pregunta as id_pregunta,"
                    + "r.id_respuesta as id_respuesta,"
                    + "r.respuesta as respuesta,"
                    + "r.estado as estado"
                    + " FROM test_respuestas_cuestionario r INNER JOIN test_preguntas_cuestionario p ON p.id = r.id_fk"
                    + " WHERE p.id = " + idpregunta + "";
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
            System.out.println("error po" + e);
        } 
        return ListRespuestas;
    }

    public String updateNameRespuesta(String Newname, String Oldname, int id) {
        String rsponse = "bad";
        try {
            sql = "select respuesta from test_respuestas_cuestionario where respuesta = '" + Newname + "'";
            System.out.println(sql);
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                rsponse = "existe";
            } else {
                sql = "UPDATE  test_respuestas_cuestionario SET respuesta = '" + Newname
                        + "' where respuesta = '" + Oldname + "' AND id_respuesta = " + id;
                pstm = cn.prepareStatement(sql);
                int rowAfectedR = pstm.executeUpdate();
                if (rowAfectedR > 0) {
                    rsponse = "ok";
                }
            }
        } catch (Exception e) {
            System.out.println("error uu" + e);
        }
        return rsponse;

    }

    public String updateEstadoRespuesta(String newState, String oldState, int id) {
        String rsponse = "bad";
        int idfk = 0;
        try {
            sql = "select id_fk from test_respuestas_cuestionario where id_respuesta = " + id;
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                idfk = rs.getInt("id_fk");
            }
            if (oldState.toUpperCase().equals("FALSO") && newState.toUpperCase().equals("VERDADERO")) {
                sql = "UPDATE test_respuestas_cuestionario SET estado = 0 where id_fk = " + idfk + " AND estado = 1";
                pstm = cn.prepareStatement(sql);
                int rowA = pstm.executeUpdate();
                if (rowA > 0) {
                    sql = "UPDATE test_respuestas_cuestionario SET estado = 1 where id_respuesta = " + id;
                    pstm = cn.prepareStatement(sql);
                    pstm.executeUpdate();
                    rsponse = "ok";
                }
            } else if (newState.toUpperCase().equals("FALSO")) {
                rsponse = "danger";
            } else {
                rsponse = "no";
            }

        } catch (Exception e) {
            System.out.println("error ff" + e);
        }
        return rsponse;

    }

}
