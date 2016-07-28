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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

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
    private final String logo = "/Reports/logo.png";
    private final String logo2 = "/Reports/logo2.png";

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
                    + "c.objetivo as objetivo, "
                    + "c.vigencia as vigencia, "
                    + "c.duracion as duracion FROM c_cuestionario c "
                    + "INNER JOIN asignaturas a ON a.id_asignatura = c.id_asignatura "
                    + "INNER JOIN cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
                    + "AND cg.id_grupo = " + grupo + ""
                    + " WHERE a.nombre_asig = '" + asignatura + "' AND c.estado = 1";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                cuestionario = new Cuestionario();
                cuestionario.setIdCuestionario(rs.getInt("id_cuestionario"));
                cuestionario.setIdUser(rs.getInt("id_user"));
                cuestionario.setDescripcion(rs.getString("descripcion"));
                cuestionario.setFecha(rs.getString("fecha"));
                cuestionario.setVigencia(rs.getString("vigencia"));
                cuestionario.setIdAsignatura(rs.getInt("id_asignatura"));
                cuestionario.setEstado(rs.getBoolean("estado"));
                cuestionario.setObjetivo(rs.getString("objetivo"));
                cuestionario.setDuracion(rs.getInt("duracion"));
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
                    + "c.estado as estado, "
                    + "c.objetivo as objetivo, "
                    + "c.vigencia as vigencia, "
                    + "c.duracion as duracion FROM c_cuestionario c "
                    + "INNER JOIN cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
                    + "WHERE cg.id_grupo = " + grupo + " AND c.estado = 1";
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
                cuestionario.setVigencia(rs.getString("vigencia"));
                cuestionario.setDuracion(rs.getInt("duracion"));
                cuestionario.setObjetivo(rs.getString("objetivo"));
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
                sql = "INSERT INTO c_cuestionario (id_user, descripcion, objetivo, fecha, id_asignatura, estado, duracion) VALUES (?,?,?,?,?,?,?)";
                pstm = cn.prepareStatement(sql);
                pstm.setInt(1, c.getIdUser());
                pstm.setString(2, c.getDescripcion());
                pstm.setString(3, c.getObjetivo());
                pstm.setString(4, c.getFecha());
                pstm.setInt(5, c.getIdAsignatura());
                pstm.setBoolean(6, c.isEstado());
                pstm.setInt(7, c.getDuracion());
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
                            sql = "SELECT respuesta, id_fk FROM respuestas_cuestionario WHERE respuesta = '" + rc.getRespuesta() + "' AND id_fk = " + rc.getIdfk() + "";
                            pstm = cn.prepareStatement(sql);
                            rs = pstm.executeQuery();
                            if (rs.getRow() == 0) {
                                sql = "INSERT INTO respuestas_cuestionario (id_pregunta, respuesta, estado, id_fk) VALUES (?,?,?,?)";
                                pstm = cn.prepareStatement(sql);
                                pstm.setInt(1, rc.getIdPregunta());
                                pstm.setString(2, rc.getRespuesta());
                                pstm.setBoolean(3, rc.isEstado());
                                pstm.setInt(4, rc.getIdfk());
                                int rowAfectedR = pstm.executeUpdate();
                            }
                        } catch (Exception e) {
                            System.err.println("CuestionarioDao AddRs : " + e);
                        }

                    }

                    if (opc.equals("C")) {
                        rpta = "Cuestionario creado con éxito";
                    } else {
                        rpta = "Cuestionario actualizado con éxito";
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
                cuestionario.setObjetivo(rs.getString("objetivo"));
                cuestionario.setVigencia(rs.getString("vigencia"));
                cuestionario.setDuracion(rs.getInt("duracion"));
                listaCuestionario.add(cuestionario);
            }

        } catch (Exception e) {
            System.out.println("error aqui" + e);
        }
        return listaCuestionario;
    }

    public ArrayList<Cuestionario> getCuestionariosByGrupo(String grupo) {
        ArrayList listaCuestionario = new ArrayList();
        int idGrupo = 0;
        Cuestionario cuestionario;
        try {
            sql = "SELECT id_grupo FROM grupo WHERE grupo ='" + grupo + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                idGrupo = rs.getInt("id_grupo");
                sql = "SELECT * FROM c_cuestionario c "
                        + "INNER JOIN cuestionarios_grupos cg ON c.id_cuestionario = cg.id_cuestionario AND cg.id_grupo = " + idGrupo;
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
                    cuestionario.setVigencia(rs.getString("vigencia"));
                    cuestionario.setDuracion(rs.getInt("duracion"));
                    listaCuestionario.add(cuestionario);
                }
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

    public ArrayList<Cuestionario> getCuestionariosByNameList(String descuestionario) {
        ArrayList listaCuestionario = new ArrayList();        
        Cuestionario cuestionario;
        try {
            sql = "SELECT * FROM c_cuestionario WHERE descripcion = '" + descuestionario + "'";
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
                cuestionario.setVigencia(rs.getString("vigencia"));
                cuestionario.setDuracion(rs.getInt("duracion"));
                listaCuestionario.add(cuestionario);
            }

        } catch (Exception e) {
            System.out.println("error aqui" + e);
        }
        return listaCuestionario;
    }

    public double getCalificacionAlumno(int idCuestionario, int idUser) {
        double calificacion = 0.0;
        try {
            int cantPreguntas = getPreguntasCuestionario(idCuestionario);
            sql = "SELECT  CASE WHEN (ROUND(sum(calificacion),1) > 5 ) THEN 5 ELSE ROUND(sum(calificacion),1) END as calificacion "
                    + "from (SELECT @prTotalPreguntas := " + cantPreguntas + " totPreguntas) totPreguntas, "
                    + "(SELECT @prId_Alumno := " + idUser + " alumno) alumno, "
                    + "(SELECT @prId_Cuestionario := " + idCuestionario + " cuestionario) cuestionario, nota_alumno";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                calificacion = rs.getDouble("calificacion");
            }
        } catch (Exception e) {
            System.out.println("error aqui" + e);
        }
        return calificacion;
    }

    public void generateReporte(int idCuestionario, int id_alumno, int idca, int total_preguntas, double nota, String notaString) {
        System.out.println(idCuestionario + " " + id_alumno + " " + idca);
        try {
            JasperDesign jd = JRXmlLoader.load("src/Reports/Evaluacion.jrxml");
            //parametros de entrada
            Map parametros = new HashMap();
            //  parametros.clear();
            parametros.put("logo", this.getClass().getResourceAsStream(logo));
            parametros.put("idCuestionario", idCuestionario);
            parametros.put("idCuestionarioAlumno", idca);
            parametros.put("alumno", id_alumno);
            parametros.put("total_preguntas", total_preguntas);
            parametros.put("nota", nota);
            parametros.put("notaString", notaString);
            //fin parametros de entrada
            JasperReport jasperRep = JasperCompileManager.compileReport(jd);
            JasperPrint JasPrint = JasperFillManager.fillReport(jasperRep, parametros, cn);
            JasperViewer jv = new JasperViewer(JasPrint, false);
            jv.setVisible(true);
            jv.setTitle("Evaluación Alumno");
        } catch (JRException ex) {
            System.out.println("Error jasper: " + ex);
        }
    }

    public void reporteGeneralResultados(String grupo, String cuestionario) {
        try {
            JasperDesign jd = JRXmlLoader.load("src/Reports/ReporteEvaluacionGeneral.jrxml");
            //parametros de entrada
            Map parametros = new HashMap();
            //  parametros.clear();
            parametros.put("logo", this.getClass().getResourceAsStream(logo2));
            parametros.put("grupo", grupo);
            parametros.put("cuestionario", cuestionario);
            //fin parametros de entrada
            JasperReport jasperRep = JasperCompileManager.compileReport(jd);
            JasperPrint JasPrint = JasperFillManager.fillReport(jasperRep, parametros, cn);
            JasperViewer jv = new JasperViewer(JasPrint, false);
            jv.setVisible(true);
            jv.setTitle("Evaluación General");
        } catch (JRException ex) {
            System.out.println("Error jasper: " + ex);
        }
    }

}
