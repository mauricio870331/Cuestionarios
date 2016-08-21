/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.*;
import java.util.ArrayList;

public class GrupoDAO {

    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;
    Grupo grupo;

    public GrupoDAO() {
        cn = Conexion.getConexion("grupodao getListGrupos");
    }

    public ArrayList<Grupo> getListGrupos() throws SQLException {
        ArrayList ListaGrupo = new ArrayList();
        try {

            sql = "SELECT * FROM test_grupo ORDER BY id_grupo";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setGrupo(rs.getString("grupo"));
                grupo.setCant(rs.getString("cant_alumnos"));
                ListaGrupo.add(grupo);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListaGrupo;

    }

    public ArrayList<Grupo> getListGrupoById(int idGrupo) {
        ArrayList ListaGrupo = new ArrayList();
        try {
            if (idGrupo != 0) {
                sql = "SELECT * FROM test_grupo WHERE id_grupo = " + idGrupo + "";
            } else {
                sql = "SELECT * FROM test_grupo";
            }
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                grupo = new Grupo();
                grupo.setIdGrupo(rs.getInt("id_grupo"));
                grupo.setGrupo(rs.getString("grupo"));
                grupo.setCant(rs.getString("cant_alumnos"));
                ListaGrupo.add(grupo);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListaGrupo;
    }

//    public ArrayList<Gym> getListGym(int pagina, String dato) {
//        int regitrosXpagina = 10;
//        int inicio = ((pagina - 1) * regitrosXpagina);
//        if (inicio < regitrosXpagina) {
//            inicio = 0;
//        }
//        ArrayList ListaGym = new ArrayList();
//        try {
//            if (dato.equals("")) {
//                sql = "SELECT * FROM grupo ORDER BY nombre ASC LIMIT " + inicio + "," + regitrosXpagina + "";
//            } else {
//                sql = "SELECT * FROM grupo WHERE nit LIKE '" + dato + "%' OR nombre LIKE '" + dato + "%' ORDER BY nombre  ASC LIMIT " + inicio + "," + regitrosXpagina + "";
//            }
//            pstm = cn.prepareStatement(sql);
//            rs = pstm.executeQuery();
//            while (rs.next()) {
//                gym = new Grupo();
//                gym.setIdGym(rs.getInt("id_gym"));
//                gym.setNit(rs.getString("nit"));
//                gym.setNombre(rs.getString("nombre"));
//                gym.setDireccion(rs.getString("direccion"));
//                gym.setTelefono(rs.getString("telefono"));
//                gym.setEstado(rs.getBoolean("estado"));
//                ListaGym.add(gym);
//            }
//        } catch (Exception e) {
//            System.out.println("error" + e);
//        }
//        return ListaGym;
//
//    }
//
//    public int getIdToUpdate(String nit) {
//        try {
//            sql = "SELECT id_gym FROM gimnasios where nit = ?";
//            pstm = cn.prepareStatement(sql);
//            pstm.setString(1, nit);
//            rs = pstm.executeQuery();
//            if (rs.next()) {
//                gym = new Grupo();
//                gym.setIdGym(rs.getInt("id_gym"));
//            }
//        } catch (Exception e) {
//            System.out.println("error" + e);
//        }
//        return gym.getIdGym();
//    }
    public String getListGrupoToString(int idgrupo) {
        String grupo = null;
        try {
            sql = "SELECT grupo FROM test_grupo WHERE id_grupo = " + idgrupo + "";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                grupo = rs.getString("grupo");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return grupo;

    }

    public int getIdGrupoByName(String grupo) {
        int idGrupo = 0;
        try {
            sql = "SELECT id_grupo FROM test_grupo WHERE grupo = '" + grupo + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                idGrupo = rs.getInt("id_grupo");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return idGrupo;

    }

//
//    public String createGym(Grupo gym, String opc) {
//        String responseCreate = null;
//        try {
//            if (opc.equals("C")) {
//                sql = "INSERT INTO gimnasios (nit, nombre, direccion, telefono) VALUES (?, ?, ?, ?)";
//            }
//            if (opc.equals("U")) {
//                sql = "UPDATE gimnasios SET nit = ?, nombre = ?, direccion = ?, telefono = ? WHERE id_gym = ?";
//            }
//            pstm = cn.prepareStatement(sql);
//            pstm.setString(1, gym.getNit());
//            pstm.setString(2, gym.getNombre());
//            pstm.setString(3, gym.getDireccion());
//            pstm.setString(4, gym.getTelefono());
//            if (opc.equals("U")) {
//                pstm.setInt(5, gym.getIdGym());
//            }
//            int rowAfected = pstm.executeUpdate();
//            if (rowAfected > 0) {
//                if (opc.equals("C")) {
//                    responseCreate = "Gimnasio creado con exito";
//                } else {
//                    responseCreate = "Gimnasio actualizado con exito";
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("error: " + e + " " + e.getClass());
//        }
//        return responseCreate;
//    }
//
//    public String deleteGym(String id) {
//        String responseDelete = null;
//        try {
//            sql = "DELETE FROM gimnasios WHERE nit = ?";
//            pstm = cn.prepareStatement(sql);
//            pstm.setString(1, id);
//            int rowDelete = pstm.executeUpdate();
//            if (rowDelete > 0) {
//                responseDelete = "registro eliminado con exito";
//            }
//        } catch (Exception e) {
//        }
//        return responseDelete;
//    }
//
//    public int totalPaginas(String dato) {
//        int regitrosXpagina = 10;
//        int creg = 0;
//        int paginas = 0;
//        if (dato.equals("")) {
//            sql = "SELECT COUNT(*) AS con FROM gimnasios";
//        } else {
//            sql = "SELECT COUNT(*) AS con FROM gimnasios WHERE nit LIKE '" + dato + "%' OR nombre LIKE '" + dato + "%'";
//        }
//        try {
//            pstm = cn.prepareStatement(sql);
//            rs = pstm.executeQuery();
//            if (rs.next()) {
//                creg = rs.getInt("con");
//            }
//        } catch (Exception e) {
//            System.out.println("error" + e);
//        }
//        paginas = (creg / regitrosXpagina);
//        return paginas;
//    }
}
