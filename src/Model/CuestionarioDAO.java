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

    public ArrayList<Cuestionario> getCuestionario(int asignatura, int grupo) {
        ArrayList listaCuestionario = new ArrayList();
        Cuestionario cuestionario;
        try {
            sql = "SELECT * FROM c_cuestionario WHERE id_asignatura = "+asignatura+" AND id_grupo = "+grupo+"";
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

    public Object getPreguntasCuestionario() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
