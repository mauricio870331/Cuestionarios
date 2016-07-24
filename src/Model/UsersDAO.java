/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Mauricio Herrera
 */
public class UsersDAO {

    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public UsersDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
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
                sql = "INSERT INTO usuarios (tipo_doc, documento, nombres, apellidos, id_grupo, id_rol, password, foto)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            }
            if (opc.equals("U")) {
                sql = "UPDATE usuarios SET tipo_doc = ?, documento = ?, nombres = ?, apellidos = ?, id_grupo = ?, id_rol = ?,  password = ?" + f + " WHERE id_user = ?";
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
            System.out.println("error = " + e);
        }
        return responseCreate;
    }

    public ArrayList<Users> getListAdministrador(int pagina, String dato, int rol) {
        String adnWhere = "";
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
                sql = "SELECT * FROM usuarios " + adnWhere + " ORDER BY id_user DESC LIMIT " + inicio + "," + regitrosXpagina + "";
            } else {
                sql = "SELECT * FROM usuarios " + adnWhere + and + "  ORDER BY id_user DESC LIMIT " + inicio + "," + regitrosXpagina + "";
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
            System.out.println("error" + e);
        }
        return listaAdministrador;

    }

    public ArrayList<Users> getExistAdmin(String usuario, String password) {
        ArrayList listaAdmin = new ArrayList();
        Users admin;
        try {
            sql = "SELECT * FROM usuarios where documento = ? AND password = ?";
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
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return listaAdmin;
    }

    public int totalPaginas(String dato) {
        int regitrosXpagina = 10;
        int creg = 0;
        int paginas = 0;
        if (dato.equals("")) {
            sql = "SELECT COUNT(*) AS con FROM usuarios";
        } else {
            sql = "SELECT COUNT(*) AS con FROM usuarios WHERE documento LIKE '" + dato + "%' OR nombres LIKE '" + dato + "%' OR apellidos LIKE '" + dato + "%'";
        }
        try {
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                creg = rs.getInt("con");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }

        paginas = (creg / regitrosXpagina);
        return paginas;
    }

    public ArrayList<Users> getIdToUpdate(String documento) {
        ArrayList listaAdmin = new ArrayList();
        Users admin;
        try {
            sql = "SELECT * FROM usuarios where documento = ?";
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
            System.out.println("error" + e);
        }
        return listaAdmin;
    }

    public String getUser(int id) {
        String profesor = "";       
        try {
            sql = "SELECT nombres, apellidos FROM usuarios where id_user = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                profesor = rs.getString("nombres") + " " + rs.getString("apellidos");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return profesor;
    }
    
    public boolean getDoc(String documento) {
        boolean esiste = false;       
        try {
            sql = "SELECT documento FROM usuarios where documento = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, documento);
            rs = pstm.executeQuery();
            if (rs.getRow()>0) {
                esiste = true;
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return esiste;
    }

}
