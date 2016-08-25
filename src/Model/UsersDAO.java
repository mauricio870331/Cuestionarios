/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.mysql.jdbc.SQLError;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Mauricio Herrera
 */
public class UsersDAO {

    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public UsersDAO() {
        cn = Conexion.getConexion("userdao getExistAdmin");
    }

    public String Create(String documento, String tipo_doc, String nombres, String apellidos, int idGrupo, int idRol, String password, String foto, String opc, int idToUpdate) {
        String responseCreate = null;
        FileInputStream fis = null;
        File file = null;
        try {
            String ruta = "src/ImagenPerfilTmp/" + documento + ".png";
            String f = "";
            if (!foto.equals("")) {
                file = new File(foto);
                fis = new FileInputStream(file);
                f = ", foto = ?";
            }
            if (opc.equals("C")) {
                sql = "INSERT INTO test_usuarios (tipo_doc, documento, nombres, apellidos, id_grupo, id_rol, password, foto)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE test_usuarios SET tipo_doc = ?, documento = ?, nombres = ?, apellidos = ?, id_grupo = ?, id_rol = ?,  password = ?" + f + " WHERE id_user = ?";
            }
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, tipo_doc);
            pstm.setString(2, documento);
            pstm.setString(3, nombres);
            pstm.setString(4, apellidos);
            pstm.setInt(5, idGrupo);
            pstm.setInt(6, idRol);
            pstm.setString(7, password);
            if (opc.equals("U")) {
                if (!foto.equals("")) {
                    pstm.setBinaryStream(8, fis, (int) file.length());
                    pstm.setInt(9, idToUpdate);
                } else {
                    pstm.setInt(8, idToUpdate);
                }
            }
            if (opc.equals("C")) {
                if (!foto.equals("")) {
                    pstm.setBinaryStream(8, fis, (int) file.length());
                } else {
                    pstm.setString(8, null);
                }
            }

            int rowAfected = pstm.executeUpdate();
            if (rowAfected > 0) {
                if (!foto.equals("")) {
                    fis.close();
                    try {
                        if (ruta.equals(foto)) {
                            if (file.delete()) {
                                System.out.println("borrado");
                            } else {
                                System.out.println("No borrado");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("error " + e);
                    }
                }
                if (opc.equals("C")) {
                    responseCreate = "Registro creado con exito";
                } else {
                    responseCreate = "Registro actualizado con exito";
                }
            }
        } catch (SQLException | IOException e) {
            String error = e.getMessage();
//            responseCreate = error.substring(0, 15);
            System.out.println(error);
        }
        return responseCreate;
    }

    public ArrayList<Users> getListAdministrador(int pagina, String dato, int rol) {
        String adnWhere = "WHERE id_rol <> 3";
        String and = "WHERE documento LIKE '" + dato + "%' OR nombres LIKE '" + dato + "%' OR apellidos LIKE '" + dato + "%'";
        int regitrosXpagina = 10;
        int inicio = ((pagina - 1) * regitrosXpagina);
        if (inicio < regitrosXpagina) {
            inicio = 0;
        }
        if (rol == 1) {
            adnWhere = "WHERE id_rol = 1";
            and = " AND (documento LIKE '" + dato + "%' OR nombres LIKE '" + dato + "%' OR apellidos LIKE '" + dato + "%')";
        }
        if (rol == 2) {
            adnWhere = "WHERE id_rol = 2";
            and = " AND (documento LIKE '" + dato + "%' OR nombres LIKE '" + dato + "%' OR apellidos LIKE '" + dato + "%')";
        }

        ArrayList listaAdministrador = new ArrayList();
        Users admin;
        try {

            if (dato.equals("")) {
                sql = "SELECT * FROM test_usuarios " + adnWhere + " ORDER BY id_user DESC LIMIT " + inicio + "," + regitrosXpagina + "";
            } else {
                sql = "SELECT * FROM test_usuarios " + adnWhere + and + "  ORDER BY id_user DESC LIMIT " + inicio + "," + regitrosXpagina + "";
            }
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                admin = new Users();
                admin.setTipoDoc(rs.getString("tipo_doc"));
                admin.setDocumento(rs.getString("documento"));
                admin.setNombres(rs.getString("nombres"));
                admin.setApellidos(rs.getString("apellidos"));
                listaAdministrador.add(admin);
            }

        } catch (Exception e) {
            System.out.println("error 1" + e);
        }
        return listaAdministrador;

    }

    public ArrayList<Users> getExistAdmin(String usuario, String password) throws SQLException {
        ArrayList listaAdmin = new ArrayList();
        Users admin;
        try {

            sql = "SELECT * FROM test_usuarios where documento = ? AND password = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, usuario);
            pstm.setString(2, password);
            rs = pstm.executeQuery();
            if (rs.next()) {
                admin = new Users();
                admin.setNombres(rs.getString("nombres"));
                admin.setApellidos(rs.getString("apellidos"));
                admin.setIdUser(rs.getInt("id_user"));
                admin.setFoto(rs.getBinaryStream("foto"));
                admin.setIdRol(rs.getInt("id_rol"));
                admin.setIdGrupo(rs.getInt("id_grupo"));
                listaAdmin.add(admin);
                System.out.println("nombre " + rs.getString("nombres"));
            }

        } catch (SQLException e) {
            System.out.println("error Aqui" + e);
        }
        return listaAdmin;
    }

    public int totalPaginas(String dato) throws SQLException {
        int regitrosXpagina = 10;
        int creg = 0;
        int paginas = 0;
        if (dato.equals("")) {
            sql = "SELECT COUNT(*) AS con FROM test_usuarios";
        } else {
            sql = "SELECT COUNT(*) AS con FROM test_usuarios WHERE documento LIKE '" + dato + "%' OR nombres LIKE '" + dato + "%' OR apellidos LIKE '" + dato + "%'";
        }
        try {
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                creg = rs.getInt("con");
            }

        } catch (Exception e) {
            System.out.println("error 2 mao " + e);
        }

        paginas = (creg / regitrosXpagina);
        return paginas;
    }

    public ArrayList<Users> getIdToUpdate(String documento) {
        ArrayList listaAdmin = new ArrayList();
        Users admin;
        try {
            sql = "SELECT * FROM test_usuarios where documento = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, documento);
            rs = pstm.executeQuery();
            if (rs.next()) {
                admin = new Users();
                admin.setPassword(rs.getString("password"));
                admin.setIdUser(rs.getInt("id_user"));
                admin.setIdRol(rs.getInt("id_rol"));
                admin.setIdGrupo(rs.getInt("id_grupo"));
                listaAdmin.add(admin);
            }

        } catch (Exception e) {
            System.out.println("error 3" + e);
        }
        return listaAdmin;
    }

    public String getUser(int id) {
        String profesor = "";
        try {

            sql = "SELECT nombres, apellidos FROM test_usuarios where id_user = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                profesor = rs.getString("nombres") + " " + rs.getString("apellidos");
            }

        } catch (Exception e) {
            System.out.println("error 4" + e);
        }
        return profesor;
    }

    public boolean getDoc(String documento) {
        boolean existe = false;
        try {

            sql = "SELECT documento FROM test_usuarios where documento = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, documento);
            rs = pstm.executeQuery();
            if (rs.next()) {
                existe = true;
            }

        } catch (Exception e) {
            System.out.println("error 5" + e);
        }
        return existe;
    }

    public boolean deleteUser(String documento) {
        boolean R = false;
        try {
            sql = "DELETE FROM test_usuarios where documento = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, documento);
            pstm.executeUpdate();
            R = true;
        } catch (Exception e) {
            System.out.println("error 5" + e);
        }
        return R;
    }

    public int getLastInsert() {
        int R = 0;
        try {
            sql = "SELECT id_user FROM test_usuarios ORDER BY id_user DESC LIMIT 1";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                R = rs.getInt("id_user");
            }
        } catch (Exception e) {
            System.out.println("error 5" + e);
        }
        return R;
    }
}
