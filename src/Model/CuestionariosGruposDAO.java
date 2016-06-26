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
 * @author Mauricio Herrera
 */
public class CuestionariosGruposDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public CuestionariosGruposDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public String addGroupToCuestionario(ArrayList<CuestionariosGrupos> list, String opc) {
        String rpta = null;
        try {
            if (opc.equals("C")) {
                Iterator<CuestionariosGrupos> cg = list.iterator();
                while (cg.hasNext()) {
                    CuestionariosGrupos obj = cg.next();
                    sql = "SELECT  * FROM cuestionarios_grupos WHERE id_cuestionario = " + obj.getIdCuestionario() + " AND id_grupo =" + obj.getIdGrupo();
                    pstm = cn.prepareStatement(sql);
                    rs = pstm.executeQuery();
                    if (rs.next()) {
                        sql = "UPDATE cuestionarios_grupos SET id_cuestionario = ?, id_grupo = ? WHERE id_cuestionario = " + obj.getIdCuestionario() + " AND id_grupo =" + obj.getIdGrupo();
                        pstm = cn.prepareStatement(sql);
                        pstm.setInt(1, obj.getIdCuestionario());
                        pstm.setInt(2, obj.getIdGrupo());
                        pstm.executeUpdate();
                    } else {
                        sql = "INSERT INTO cuestionarios_grupos (id_cuestionario, id_grupo) VALUES (?,?)";
                        pstm = cn.prepareStatement(sql);
                        pstm.setInt(1, obj.getIdCuestionario());
                        pstm.setInt(2, obj.getIdGrupo());
                        pstm.executeUpdate();
                    }
                }
                if (opc.equals("C")) {
                    rpta = "Grupos asignados a cuestionaro con éxito";
                } else {
                    rpta = "Grupos actualizada con éxito";
                }
            }
        } catch (Exception e) {
            System.err.println("CuestionarioGrupoDao AddQuest : " + e);
        }
        return rpta;
    }
}
