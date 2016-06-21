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
public class AsignaturasDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public AsignaturasDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public ArrayList<Asignaturas> getListCboAsignaturas() {
        ArrayList ListAsignatura = new ArrayList();
        try {
            sql = "SELECT * FROM asignaturas";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                Asignaturas as = new Asignaturas();
                as.setAsignatura(rs.getInt("id_asignatura"));
                as.setNombreAsignatura(rs.getString("nombre_asig"));
                ListAsignatura.add(as);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListAsignatura;
    }
    
    
    public ArrayList<Asignaturas> getAsignaturasByCuestionario(int grupo){
      ArrayList ListAsignatura = new ArrayList();
        try {
            sql = "SELECT a.id_asignatura as id_asignatura, a.nombre_asig as nombre_asig "
                + "FROM asignaturas a INNER JOIN c_cuestionario c ON a.id_asignatura = c.id_asignatura "
                + "INNER JOIN cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
                + "WHERE cg.id_grupo = "+grupo+"";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                Asignaturas as = new Asignaturas();
                as.setAsignatura(rs.getInt("id_asignatura"));
                as.setNombreAsignatura(rs.getString("nombre_asig"));
                ListAsignatura.add(as);
            }
        } catch (Exception e) {
            System.out.println("error " + e);
        }
        return ListAsignatura;
    } 
    
      public int getAsignaturaByName(String Asignatura) {
        int id_asignatura = 0;
        try {
            sql = "SELECT id_asignatura FROM asignaturas where nombre_asig = '"+Asignatura+"'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
               id_asignatura = rs.getInt("id_asignatura");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return id_asignatura;
    }
    
}
