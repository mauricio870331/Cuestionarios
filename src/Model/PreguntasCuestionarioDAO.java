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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mauricio Herrera
 */
public class PreguntasCuestionarioDAO {

    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public PreguntasCuestionarioDAO() {
       cn = Conexion.getConexion();
    }

    public ArrayList<PreguntasCuestionario> getPreguntasCuestionario(int idCuestionario) {
        ArrayList ListPreguntas = new ArrayList();
        try {
            sql = "SELECT * FROM test_preguntas_cuestionario WHERE id_cuestionario = " + idCuestionario + "";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                PreguntasCuestionario prc = new PreguntasCuestionario();
                prc.setId(rs.getInt("id"));
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

    public int nexIdPreguntaCuestionario() {
        int id = 0;
        try {
            sql = "select max(id) as maxid from test_preguntas_cuestionario";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                id = rs.getInt("maxid");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return id;

    }

}
