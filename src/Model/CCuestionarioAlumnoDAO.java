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
import java.util.Iterator;

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
        int rpta = 0;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO c_cuestionario_alumno (id_user, id_cuestionario) VALUES (?,?)";
                pstm = cn.prepareStatement(sql);
                pstm.setInt(1, c.getIdAlumno());
                pstm.setInt(2, c.getIdCuestionario());
                pstm.executeUpdate();
                if (pstm.executeUpdate()==1) {
                    
                }
            }
//            if (opc.equals("C")) {
////                rpta = "Grupos asignados a cuestionaro con éxito";
//            } else {
////                rpta = "Grupos actualizada con éxito";
//            }

        } catch (Exception e) {
            System.err.println("CuestionarioGrupoDao AddQuest : " + e);
        }
        return rpta;
    }

}
