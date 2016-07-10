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
            sql = "SELECT c.id_cuestionario as id_cuestionario, "
                    + "c.id_user as id_user, "
                    + "c.descripcion as descripcion, "
                    + "c.fecha as fecha, "
                    + "c.id_asignatura as id_asignatura, "
                    + "c.id_asignatura as id_asignatura, "
                    + "c.estado as estado, "
                    + "c.objetivo as objetivo FROM c_cuestionario c "
                    + "INNER JOIN asignaturas a ON a.id_asignatura = c.id_asignatura "
                    + "INNER JOIN cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
                    + "AND cg.id_grupo = " + grupo + ""
                    + " WHERE a.nombre_asig = '" + asignatura + "'";
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
                cuestionario.setObjetivo(rs.getString("objetivo"));
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
            sql = "SELECT c.id_cuestionario as id_cuestionario, "
                    + "c.id_user as id_user, "
                    + "c.descripcion as descripcion, "
                    + "c.fecha as fecha, "
                    + "c.id_asignatura as id_asignatura, "
                    + "c.id_asignatura as id_asignatura, "
                    + "c.estado as estado FROM c_cuestionario c "
                    + "INNER JOIN cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
                    + "WHERE cg.id_grupo = " + grupo + "";
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
                listaCuestionario.add(cuestionario);
            }

        } catch (Exception e) {
            System.out.println("error aqui" + e);
        }
        return listaCuestionario;
    }

    public int getPreguntasCuestionario(int id_cuestionario) {
        int total = 0;
        try {
            sql = "SELECT count(pregunta) as total FROM c_cuestionario c "
                    + "INNER JOIN preguntas_cuestionario p ON c.id_cuestionario = p.id_cuestionario "
                    + "WHERE c.id_cuestionario = " + id_cuestionario + "";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("error aqui 2 " + e);
        }
        return total;
    }

    public int nexIdCuestionario() {
        int id = 0;
        try {
            sql = "select max(id_cuestionario) as maxid from c_cuestionario";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                id = rs.getInt("maxid");
            }

        } catch (Exception e) {
            System.out.println("error" + e);
        }
        return id + 1;

    }

    public String createCuestionary(String opc, Cuestionario c, ArrayList<PreguntasCuestionario> ListPreguntas, ArrayList<RespuestasCuestionario> ListRespuestas) {
        String rpta = null;
        int id_cues = nexIdCuestionario();
        try {
            if (opc.equals("C")) {
                sql = "INSERT INTO c_cuestionario (id_user, descripcion, objetivo, fecha, id_asignatura, estado) VALUES (?,?,?,?,?,?)";
                pstm = cn.prepareStatement(sql);
                pstm.setInt(1, c.getIdUser());
                pstm.setString(2, c.getDescripcion());
                pstm.setString(3, c.getObjetivo());
                pstm.setString(4, c.getFecha());
                pstm.setInt(5, c.getIdAsignatura());
                pstm.setBoolean(6, c.isEstado());
                int rowAfected = pstm.executeUpdate();
                if (rowAfected > 0) {
                    Iterator<PreguntasCuestionario> preguntas = ListPreguntas.iterator();
                    while (preguntas.hasNext()) {
                        try {
                            PreguntasCuestionario pc = preguntas.next();
                            sql = "INSERT INTO preguntas_cuestionario (id_pregunta, pregunta, id_cuestionario, imagen) VALUES (?,?,?,?)";
                            pstm = cn.prepareStatement(sql);
                            pstm.setInt(1, pc.getIdPregunta());
                            pstm.setString(2, pc.getPregunta());
                            pstm.setInt(3, id_cues);
                            pstm.setBinaryStream(4, null);
                            int rowAfectedP = pstm.executeUpdate();
                        } catch (Exception e) {
                            System.err.println("CuestionarioDao AddQuest : " + e);
                        }

                    }

                    Iterator<RespuestasCuestionario> respuestas = ListRespuestas.iterator();
                    while (respuestas.hasNext()) {
                        try {
                            RespuestasCuestionario rc = respuestas.next();
                            sql = "INSERT INTO respuestas_cuestionario (id_pregunta, respuesta, estado, id_fk) VALUES (?,?,?,?)";
                            pstm = cn.prepareStatement(sql);
                            pstm.setInt(1, rc.getIdPregunta());
                            pstm.setString(2, rc.getRespuesta());
                            pstm.setBoolean(3, rc.isEstado());
                            pstm.setInt(4, rc.getIdfk());
                            int rowAfectedR = pstm.executeUpdate();
                        } catch (Exception e) {
                            System.err.println("CuestionarioDao AddRs : " + e);
                        }

                    }

                    if (opc.equals("C")) {
                        rpta = "Cuestionario creada con éxito";
                    } else {
                        rpta = "Cuestionario actualizada con éxito";
                    }
                }
            }
            if (opc.equals("U")) {
                sql = "UPDATE areas SET area = ? WHERE id_area = ?";
            }
        } catch (Exception e) {
            System.out.println("error CuestionarioDao : " + e);
        }
        return rpta;
    }

    public ArrayList<Cuestionario> getCuestionariosByProfesor(int user) {
        ArrayList listaCuestionario = new ArrayList();
        Cuestionario cuestionario;
        try {
            sql = "SELECT * FROM c_cuestionario WHERE id_user = " + user + "";
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
                listaCuestionario.add(cuestionario);
            }

        } catch (Exception e) {
            System.out.println("error aqui" + e);
        }
        return listaCuestionario;
    }

    public int getCuestionariosByName(String descuestionario) {
        int idCuestionario = 0;
        try {
            sql = "SELECT * FROM c_cuestionario WHERE descripcion = '" + descuestionario + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                idCuestionario = rs.getInt("id_cuestionario");

            }

        } catch (Exception e) {
            System.out.println("error aqui" + e);
        }
        return idCuestionario;
    }

}
