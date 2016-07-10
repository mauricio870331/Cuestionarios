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

    public String create(Asignaturas a, String opc) {
        String responseCreate = null;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO asignaturas (nombre_asig) VALUES (?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE asignaturas SET nombre_asig = ? WHERE id_asignatura = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, a.getNombreAsignatura());
            if (opc.equals("U")) {
                pstm.setInt(2, a.getAsignatura());
            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
                if (opc.equals("C")) {
                    responseCreate = "Asignatura creada con éxito";
                } else {
                    responseCreate = "Asignatura actualizada con éxito";
                }
            }
        } catch (Exception e) {
            System.out.println("error: " + e + " " + e.getClass());
        }

        return responseCreate;
    }

    public ArrayList<Asignaturas> getListCboAsignaturas(int profesor) {
        ArrayList ListAsignatura = new ArrayList();
        try {
            sql = "SELECT * FROM asignaturas a "
                + "INNER JOIN asignaturas_profesor ap ON a.id_asignatura = ap.id_asignatura "
                + "WHERE ap.id_user = "+profesor+"";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                Asignaturas as = new Asignaturas();
                as.setAsignatura(rs.getInt("id_asignatura"));
                as.setNombreAsignatura(rs.getString("nombre_asig"));
                ListAsignatura.add(as);
            }
        } catch (Exception e) {
            System.out.println("error "+ e);
        }
        return ListAsignatura;
    }

    public ArrayList<Asignaturas> getListAsignaturas(String dato) {
        ArrayList ListAsignatura = new ArrayList();
        try {
            if (dato.equals("")) {
                sql = "SELECT * FROM asignaturas";
            } else {
                sql = "SELECT * FROM asignaturas where nombre_asig LIKE '" + dato + "%'";
            }
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

    public ArrayList<Asignaturas> getAsignaturasByCuestionario(int grupo) {
        ArrayList ListAsignatura = new ArrayList();
        try {
            sql = "SELECT a.id_asignatura as id_asignatura, a.nombre_asig as nombre_asig "
                    + "FROM asignaturas a INNER JOIN c_cuestionario c ON a.id_asignatura = c.id_asignatura "
                    + "INNER JOIN cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
                    + "WHERE cg.id_grupo = " + grupo + "";
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
            sql = "SELECT id_asignatura FROM asignaturas where nombre_asig = '" + Asignatura + "'";
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

    public boolean existAsignatura(String asignatura) {
        boolean existe = false;
        try {
            sql = "SELECT * FROM asignaturas WHERE nombre_asig = '" + asignatura + "' OR nombre_asig LIKE '%" + asignatura + "%'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                existe = true;
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return existe;
    }

    public String deleteAsignatura(int idAsignatura) {
        String responseDelete = null;
        try {
            String sqlL = "SELECT * FROM c_cuestionario WHERE id_asignatura = " + idAsignatura + "";
            pstm = cn.prepareStatement(sqlL);
            rs = pstm.executeQuery();
            if (rs.next()) {
                responseDelete = "La Asignatura ya tiene cuestionarios asociados\nNo se puede eliminar..";
            } else {
                String sqlA = "DELETE FROM asignaturas_profesor WHERE id_asignatura = ?";
                pstm = cn.prepareStatement(sqlA);
                pstm.setInt(1, idAsignatura);
                pstm.executeUpdate();

                sql = "DELETE FROM asignaturas WHERE id_asignatura = ?";
                pstm = cn.prepareStatement(sql);
                pstm.setInt(1, idAsignatura);
                int rowDelete = pstm.executeUpdate();
                if (rowDelete > 0) {
                    responseDelete = "registro eliminado con exito..!";
                }

            }
        } catch (Exception e) {
        }
        return responseDelete;
    }

    public ArrayList<Asignaturas> getLastInsert() {
        ArrayList listaAsignaturas = new ArrayList();
        Asignaturas asignatura;
        try {
            sql = "SELECT * FROM asignaturas ORDER BY id_asignatura DESC limit 1";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                asignatura = new Asignaturas();
                asignatura.setAsignatura(rs.getInt("id_asignatura"));
                asignatura.setNombreAsignatura(rs.getString("nombre_asig"));
                listaAsignaturas.add(asignatura);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAsignaturas;

    }

}
