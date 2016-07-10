/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author clopez
 */
public class CCuestionarioAlumnoDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public CCuestionarioAlumnoDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public int createCuestionarioAlumno(CCuestionarioAlumno c, String opc) {
        int idcAlumno = 0;
        try {
            if (opc.equals("C")) {
                sql = "SELECT id_c_alumno FROM c_cuestionario_alumno where id_user = " + c.getIdAlumno() + " AND id_cuestionario = " + c.getIdCuestionario() + "";
                pstm = cn.prepareStatement(sql);
                rs = pstm.executeQuery();
                if (rs.next()) {
                    idcAlumno = rs.getInt("id_c_alumno");
                } else {
                    sql = "INSERT INTO c_cuestionario_alumno (id_user, id_cuestionario) VALUES (?,?)";
                    pstm = cn.prepareStatement(sql);
                    pstm.setInt(1, c.getIdAlumno());
                    pstm.setInt(2, c.getIdCuestionario());
                    int rowafected = pstm.executeUpdate();
                    if (rowafected > 0) {
                        sql = "SELECT id_c_alumno FROM c_cuestionario_alumno ORDER BY id_c_alumno DESC LIMIT 1";
                        pstm = cn.prepareStatement(sql);
                        rs = pstm.executeQuery();
                        if (rs.next()) {
                            idcAlumno = rs.getInt("id_c_alumno");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("CCuestionarioAlumnoDao AddQuest : " + e);
        }
        return idcAlumno;
    }

}
