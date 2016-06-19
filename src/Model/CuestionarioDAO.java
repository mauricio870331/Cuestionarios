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
public class CuestionarioDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public CuestionarioDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public ArrayList<Cuestionario> getCuestionario(String asignatura, int grupo) {
        ArrayList listaCuestionario = new ArrayList();
        Cuestionario cuestionario;
        try {
            sql = "SELECT * FROM c_cuestionario c INNER JOIN asignaturas a ON a.id_asignatura = c.id_asignatura"
                  + " WHERE a.nombre_asig = '"+asignatura+"' AND id_grupo = "+grupo+"";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                cuestionario = new Cuestionario();
                cuestionario.setIdCuestionario(rs.getInt("id_cuestionario"));
                cuestionario.setIdUser(rs.getInt("id_user"));
                cuestionario.setDescripcion(rs.getString("descripcion"));
                cuestionario.setFecha(rs.getString("fecha"));
                cuestionario.setIdAsignatura(rs.getInt("id_asignatura"));
                cuestionario.setEstado(rs.getBoolean("estado"));
                cuestionario.setIdGrupo(rs.getInt("id_grupo"));
                listaCuestionario.add(cuestionario);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaCuestionario;
    }
    
    
     public ArrayList<Cuestionario> getCuestionarioByGrupo(int grupo) {
        ArrayList listaCuestionario = new ArrayList();
        Cuestionario cuestionario;
        try {
            sql = "SELECT * FROM c_cuestionario WHERE id_grupo = "+grupo+"";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                cuestionario = new Cuestionario();
                cuestionario.setIdCuestionario(rs.getInt("id_cuestionario"));
                cuestionario.setIdUser(rs.getInt("id_user"));
                cuestionario.setDescripcion(rs.getString("descripcion"));
                cuestionario.setFecha(rs.getString("fecha"));
                cuestionario.setIdAsignatura(rs.getInt("id_asignatura"));
                cuestionario.setEstado(rs.getBoolean("estado"));
                cuestionario.setIdGrupo(rs.getInt("id_grupo"));
                listaCuestionario.add(cuestionario);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaCuestionario;
    }
     
    
    

    public int getPreguntasCuestionario(int id_cuestionario) {
        int total = 0;
        try {
            sql = "SELECT count(pregunta) as total FROM c_cuestionario c "
                    + "INNER JOIN preguntas_cuestionario p ON c.id_cuestionario = p.id_cuestionario "
                    + "WHERE c.id_cuestionario = "+id_cuestionario+"";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if  (rs.next()) {
              total = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        } 
        return total;
    }

}
