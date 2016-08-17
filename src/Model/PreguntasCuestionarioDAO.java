/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public int getIdPreguntaByName(String pregunta) {
        int id = 0;
        try {
            sql = "select id from test_preguntas_cuestionario where pregunta = '" + pregunta + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return id;

    }

    public InputStream getImgPreguntaByName(String pregunta) {
        InputStream imagen = null;
        try {
            sql = "select imagen from test_preguntas_cuestionario where pregunta = '" + pregunta + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                imagen = rs.getBinaryStream("imagen");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return imagen;

    }

    public String updateNamePregunta(String Newname, String Oldname) {
        String rsponse = "bad";
        try {
            sql = "select pregunta from test_preguntas_cuestionario where pregunta = '" + Oldname + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.getRow() > 0) {
                rsponse = "existe";
            } else {
                sql = "UPDATE  test_preguntas_cuestionario SET pregunta = '" + Newname + "' where pregunta = '" + Oldname + "'";
                pstm = cn.prepareStatement(sql);
                int rowAfectedR = pstm.executeUpdate();
                if (rowAfectedR > 0) {
                    rsponse = "ok";
                }
            }

        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return rsponse;

    }

    public String updateImagen(String imagenupdate, String pregunta) {
        String imagen = "";
        FileInputStream fis = null;
        File img = null;
        try {
            img = new File(imagenupdate);
            fis = new FileInputStream(img);
            sql = "UPDATE  test_preguntas_cuestionario SET imagen = ? WHERE pregunta = ?";            
            pstm = cn.prepareStatement(sql);
            pstm.setBinaryStream(1, fis, (int) img.length());
            pstm.setString(2, pregunta);
            if (pstm.executeUpdate() > 0) {
                imagen = imagenupdate;
            }
        } catch (FileNotFoundException | SQLException e) {
            System.out.println("error" + e);
        }
        return imagen;
    }

}
