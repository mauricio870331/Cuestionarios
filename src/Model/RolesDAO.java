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
public class RolesDAO {
    Conexion conexion;
    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;

    public RolesDAO() {
        conexion = new Conexion();
        cn = conexion.getConexion();
    }

    public ArrayList<Roles> getListRol() {
        ArrayList ListaRoles = new ArrayList();
        Roles roles;
        try {
            sql = "SELECT * FROM roles ORDER BY id_rol";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                roles = new Roles();
                roles.setIdRol(rs.getInt("id_rol"));
                roles.setRol(rs.getString("rol"));
                             
                ListaRoles.add(roles);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListaRoles;

    }
    
    public ArrayList<Roles> getListRolToString(int id_rol) {
        ArrayList ListaRoles = new ArrayList();
        Roles roles;
        try {
            sql = "SELECT * FROM roles WHERE id_rol = "+id_rol+"";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                roles = new Roles();
                roles.setIdRol(rs.getInt("id_rol"));
                roles.setRol(rs.getString("rol"));                             
                ListaRoles.add(roles);
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return ListaRoles;

    }
    
    public int getIdRolByNombre(String rol) {
        int idRol = 0;       
        try {
            sql = "SELECT id_rol FROM roles where rol = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, rol);
            rs = pstm.executeQuery();
            if (rs.next()) {
                idRol = rs.getInt("id_rol");
            }
        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return idRol;
    }
}
