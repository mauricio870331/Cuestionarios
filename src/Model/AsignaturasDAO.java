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
import java.util.Iterator;

/**
 *
 * @author Mauricio Herrera
 */
public class AsignaturasDAO {

    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public AsignaturasDAO() {
        cn = Conexion.getConexion("Asignaturasdao");
    }

    public String create(Asignaturas a, String opc, int Profesor) throws SQLException {
        String responseCreate = null;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO test_asignaturas (nombre_asig) VALUES (?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE test_asignaturas SET nombre_asig = ? WHERE id_asignatura = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, a.getNombreAsignatura());
            if (opc.equals("U")) {
                pstm.setInt(2, a.getAsignatura());
            }
            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
                if (opc.equals("C")) {
                    if (Profesor > 0) {
                        int asi = getLastInsert().get(0).getAsignatura();
                        sql = "INSERT INTO test_asignaturas_profesor (id_user, id_asignatura) VALUES (?, ?)";
                        pstm = cn.prepareStatement(sql);
                        pstm.setInt(1, Profesor);
                        pstm.setInt(2, asi);
                        pstm.executeUpdate();
                    }
                    responseCreate = "Asignatura creada con éxito";
                } else {
                    responseCreate = "Asignatura actualizada con éxito";
                }
            }

        } catch (Exception e) {
            System.out.println("error cerdo: " + e + " " + e.getClass());
        }
        return responseCreate;
    }

    public String create(Asignaturas a, String opc) throws SQLException {
        String responseCreate = null;
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO test_asignaturas (nombre_asig) VALUES (?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE test_asignaturas SET nombre_asig = ? WHERE id_asignatura = ?";
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
            System.out.println("error cerdo: " + e + " " + e.getClass());
        }
        return responseCreate;
    }

    public ArrayList<Asignaturas> getListCboAsignaturas(int profesor) throws SQLException {
        ArrayList ListAsignatura = new ArrayList();
        try {
            sql = "SELECT * FROM test_asignaturas a "
                    + "INNER JOIN test_asignaturas_profesor ap ON a.id_asignatura = ap.id_asignatura "
                    + "WHERE ap.id_user = " + profesor + "";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                Asignaturas as = new Asignaturas();
                as.setAsignatura(rs.getInt("id_asignatura"));
                as.setNombreAsignatura(rs.getString("nombre_asig"));
                ListAsignatura.add(as);
            }

        } catch (Exception e) {
            System.out.println("error m" + e);
        }
        return ListAsignatura;
    }

    public ArrayList<Asignaturas> getListAsignaturas(String dato) throws SQLException {
        ArrayList ListAsignatura = new ArrayList();
        try {
            if (dato.equals("")) {
                sql = "SELECT * FROM test_asignaturas";
            } else {
                sql = "SELECT * FROM test_asignaturas where nombre_asig LIKE '" + dato + "%'";
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
            System.out.println("error gato" + e);
        }
        return ListAsignatura;
    }

    public ArrayList<Asignaturas> getAsignaturasByCuestionario(int grupo) {
        ArrayList ListAsignatura = new ArrayList();
        try {
            sql = "SELECT DISTINCT a.id_asignatura as id_asignatura, a.nombre_asig as nombre_asig "
                    + "FROM test_asignaturas a INNER JOIN test_c_cuestionario c ON a.id_asignatura = c.id_asignatura "
                    + "INNER JOIN test_cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
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
            sql = "SELECT id_asignatura FROM test_asignaturas where nombre_asig = '" + Asignatura + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                id_asignatura = rs.getInt("id_asignatura");
            }

        } catch (Exception e) {
            System.out.println("error pio" + e);
        }
        return id_asignatura;
    }

    public boolean existAsignatura(String asignatura) {
        boolean existe = false;
        try {
            sql = "SELECT * FROM test_asignaturas WHERE nombre_asig = '" + asignatura + "' OR nombre_asig LIKE '%" + asignatura + "%'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                existe = true;
            }

        } catch (Exception e) {
            System.out.println("error mulk" + e);
        }
        return existe;
    }

    public String deleteAsignatura(int idAsignatura) {
        String responseDelete = null;
        try {
            String sqlL = "SELECT * FROM test_c_cuestionario WHERE id_asignatura = " + idAsignatura + "";
            pstm = cn.prepareStatement(sqlL);
            rs = pstm.executeQuery();
            if (rs.next()) {
                responseDelete = "La Asignatura ya tiene cuestionarios asociados\nNo se puede eliminar..";
            } else {
                String sqlA = "DELETE FROM test_asignaturas_profesor WHERE id_asignatura = ?";
                pstm = cn.prepareStatement(sqlA);
                pstm.setInt(1, idAsignatura);
                pstm.executeUpdate();

                sql = "DELETE FROM test_asignaturas WHERE id_asignatura = ?";
                pstm = cn.prepareStatement(sql);
                pstm.setInt(1, idAsignatura);
                int rowDelete = pstm.executeUpdate();
                if (rowDelete > 0) {
                    responseDelete = "registro eliminado con exito..!";
                }

            }

        } catch (SQLException e) {
            System.out.println("error addao " + e);
        }
        return responseDelete;
    }

    public ArrayList<Asignaturas> getLastInsert() {
        ArrayList listaAsignaturas = new ArrayList();
        Asignaturas asignatura;
        try {

            sql = "SELECT * FROM test_asignaturas ORDER BY id_asignatura DESC limit 1";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                asignatura = new Asignaturas();
                asignatura.setAsignatura(rs.getInt("id_asignatura"));
                asignatura.setNombreAsignatura(rs.getString("nombre_asig"));
                listaAsignaturas.add(asignatura);
            }

        } catch (Exception e) {
            System.out.println("error mula" + e);
        }
        return listaAsignaturas;

    }

    public String getAsignaturaById(int idasignatura) {
        String asignaruta = "";
        try {
            sql = "SELECT nombre_asig FROM test_asignaturas WHERE id_asignatura = " + idasignatura + "";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                asignaruta = rs.getString("nombre_asig");
            }
        } catch (Exception e) {
            System.out.println("error peste" + e);
        }
        return asignaruta;
    }

    public String addAsignaturaToTeacher(ArrayList<Asignaturas> asignaturas, int id_user) throws SQLException {
        String responseCreate = "bad";
        try {
            String sqlv = "SELECT * FROM test_asignaturas_profesor WHERE id_user = ? and id_asignatura = ?";
            sql = "INSERT INTO test_asignaturas_profesor (id_user, id_asignatura) VALUES (?, ?)";
//            if (opc.equals("U")) {
//                sql = "UPDATE test_asignaturas SET nombre_asig = ? WHERE id_asignatura = ?";
//            }
            Iterator<Asignaturas> ItrA = asignaturas.iterator();
            while (ItrA.hasNext()) {
                Asignaturas a = ItrA.next();
                pstm = cn.prepareStatement(sqlv);
                pstm.setInt(1, id_user);
                pstm.setInt(2, a.getAsignatura());
                rs = pstm.executeQuery();
                if (!rs.next()) {
                    pstm = cn.prepareStatement(sql);
                    pstm.setInt(1, id_user);
                    pstm.setInt(2, a.getAsignatura());
                    if (pstm.executeUpdate() > 0) {
                        responseCreate = "ok";
                    }
                } else {
                    System.out.println("existe");
                }
            }
        } catch (Exception e) {
            System.out.println("error cerdo: " + e + " " + e.getClass());
        }
        return responseCreate;
    }

    public ArrayList<Asignaturas> getAsignaturasTeacher(int id_uer) {
        ArrayList listaAsignaturas = new ArrayList();
        Asignaturas asignatura;
        try {
            sql = "SELECT a.id_asignatura as id_asignatura, a.nombre_asig as nombre_asig "
                    + "FROM test_asignaturas a INNER JOIN test_asignaturas_profesor ta ON a.id_asignatura = ta.id_asignatura "
                    + "WHERE id_user = " + id_uer + " ORDER BY nombre_asig";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                asignatura = new Asignaturas();
                asignatura.setAsignatura(rs.getInt("id_asignatura"));
                asignatura.setNombreAsignatura(rs.getString("nombre_asig"));
                listaAsignaturas.add(asignatura);
            }

        } catch (Exception e) {
            System.out.println("error mula" + e);
        }
        return listaAsignaturas;

    }

}
