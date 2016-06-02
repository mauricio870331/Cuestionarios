/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Mauricio Herrera
 */
public class PreguntasCuestionarioDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public PreguntasCuestionarioDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public ArrayList<PreguntasCuestionario> getPreguntasCuestionario(int idCuestionario) {
        ArrayList ListPreguntas = new ArrayList();
        try {
            sql = "SELECT * FROM preguntas_cuestionario WHERE id_cuestionario = " + idCuestionario + "";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                PreguntasCuestionario prc = new PreguntasCuestionario();
                prc.setIdPregunta(rs.getInt("id_pregunta"));
                prc.setPregunta(rs.getString("pregunta"));
                prc.setIdCuestionario(rs.getInt("id_cuestionario"));
                prc.setImagen(rs.getBinaryStream("imagen"));
                ListPreguntas.add(prc);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListPreguntas;
    }
}
