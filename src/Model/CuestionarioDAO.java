/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    Connection cn;
    PreparedStatement pstm;
    String sql;
    ResultSet rs;
    private final String logo = "/Reports/logo.png";
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public CuestionarioDAO() {
        cn = Conexion.getConexion("CuestionarioDao getCuestionario");
    }

    public ArrayList<Cuestionario> getCuestionario(String asignatura, int grupo) throws SQLException {
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
                    + "c.duracion as duracion FROM test_c_cuestionario c "
                    + "INNER JOIN test_asignaturas a ON a.id_asignatura = c.id_asignatura "
                    + "INNER JOIN test_cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
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
            System.out.println("error nose" + e);
        }
        return listaCuestionario;
    }

    public ArrayList<Cuestionario> getCuestionarioByGrupo(int grupo) throws SQLException {
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
                    + "c.duracion as duracion FROM test_c_cuestionario c "
                    + "INNER JOIN test_cuestionarios_grupos cg ON cg.id_cuestionario = c.id_cuestionario "
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

    public ArrayList<Cuestionario> getCuestionarioByGrupoAndProfesor(int profesor) throws SQLException {
        ArrayList listaCuestionario = new ArrayList();
        Cuestionario cuestionario;
        try {
            cn = Conexion.getConexion("CuestionarioDao getCuestionarioByGrupoAndProfesor");
            sql = "SELECT descripcion FROM test_c_cuestionario WHERE id_user = " + profesor;
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                cuestionario = new Cuestionario();
                cuestionario.setDescripcion(rs.getString("descripcion"));
                listaCuestionario.add(cuestionario);
            }

        } catch (Exception e) {
            System.out.println("error aqui" + e);
        }
        return listaCuestionario;
    }

    public int getPreguntasCuestionario(int id_cuestionario) throws SQLException {
        int total = 0;
        try {  
            sql = "SELECT count(pregunta) as total FROM test_c_cuestionario c "
                    + "INNER JOIN test_preguntas_cuestionario p ON c.id_cuestionario = p.id_cuestionario "
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

    public int nexIdCuestionario() throws SQLException {
        int id = 0;
        try {           
            sql = "select max(id_cuestionario) as maxid from test_c_cuestionario";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                id = rs.getInt("maxid");
            }

        } catch (Exception e) {
            System.out.println("error b" + e);
        } 
        return id + 1;

    }

    public boolean updateName(String Newname, String Oldname) throws SQLException {
        boolean rsponse = false;
        try {            
            sql = "UPDATE  test_c_cuestionario SET descripcion = '" + Newname + "' where descripcion = '" + Oldname + "'";
            pstm = cn.prepareStatement(sql);
            int rowAfectedR = pstm.executeUpdate();
            if (rowAfectedR > 0) {
                rsponse = true;
            }

        } catch (Exception e) {
            System.out.println("error fi" + e);
        } 
        return rsponse;
    }
    
     public boolean updateObjetive(String Newname, String Oldname) throws SQLException {
        boolean rsponse = false;
        try {            
            sql = "UPDATE  test_c_cuestionario SET objetivo = '" + Newname + "' where objetivo = '" + Oldname + "'";
            pstm = cn.prepareStatement(sql);
            int rowAfectedR = pstm.executeUpdate();
            if (rowAfectedR > 0) {
                rsponse = true;
            }

        } catch (Exception e) {
            System.out.println("error fi" + e);
        } 
        return rsponse;
    }

    public String createCuestionary(String opc, Cuestionario c, ArrayList<PreguntasCuestionario> ListPreguntas, ArrayList<RespuestasCuestionario> ListRespuestas) throws SQLException {
        String rpta = null;
        FileInputStream fis = null;
        File img = null;
        String strimg = "";
        int id_cues = nexIdCuestionario();
        try {            
            cn.setAutoCommit(false);
            if (opc.equals("C")) {
                sql = "INSERT INTO test_c_cuestionario (id_user, descripcion, objetivo, fecha, id_asignatura, estado, duracion) VALUES (?,?,?,?,?,?,?)";
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
                            sql = "INSERT INTO test_preguntas_cuestionario (id_pregunta, pregunta, id_cuestionario, imagen) VALUES (?,?,?,?)";
                            pstm = cn.prepareStatement(sql);
                            pstm.setInt(1, pc.getIdPregunta());
                            pstm.setString(2, pc.getPregunta());
                            pstm.setInt(3, id_cues);
                            if (!pc.getLargo().equals("")) {
                                img = new File(pc.getLargo());
                                fis = new FileInputStream(img);
                                pstm.setBinaryStream(4, fis, (int) img.length());
                            } else {
                                pstm.setBinaryStream(4, null);
                            }
                            int rowAfectedP = pstm.executeUpdate();
                        } catch (SQLException | FileNotFoundException e) {
                            System.err.println("CuestionarioDao AddQuest : " + e);
                        }

                    }

                    Iterator<RespuestasCuestionario> respuestas = ListRespuestas.iterator();
                    while (respuestas.hasNext()) {
                        try {
                            RespuestasCuestionario rc = respuestas.next();
                            sql = "SELECT respuesta, id_fk FROM test_respuestas_cuestionario WHERE respuesta = '" + rc.getRespuesta() + "' AND id_fk = " + rc.getIdfk() + "";
                            pstm = cn.prepareStatement(sql);
                            rs = pstm.executeQuery();
                            if (rs.getRow() == 0) {
                                sql = "INSERT INTO test_respuestas_cuestionario (id_pregunta, respuesta, estado, id_fk) VALUES (?,?,?,?)";
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
                //sql = "UPDATE test_areas SET area = ? WHERE id_area = ?";
            }
        } catch (Exception e) {
            System.out.println("error CuestionarioDao : " + e);
        } finally {
            cn.commit();
            cn.setAutoCommit(true);            
        }
        return rpta;
    }

    public ArrayList<Cuestionario> getCuestionariosByProfesor(int user) throws SQLException {
        ArrayList listaCuestionario = new ArrayList();
        Cuestionario cuestionario;
        try {           
            sql = "SELECT * FROM test_c_cuestionario WHERE id_user = " + user + "";
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

    public ArrayList<Cuestionario> getCuestionariosByGrupo(String grupo) throws SQLException {
        ArrayList listaCuestionario = new ArrayList();
        int idGrupo = 0;
        Cuestionario cuestionario;
        try {            
            sql = "SELECT id_grupo FROM test_grupo WHERE grupo ='" + grupo + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                idGrupo = rs.getInt("id_grupo");
                sql = "SELECT * FROM test_c_cuestionario c "
                        + "INNER JOIN test_cuestionarios_grupos cg ON c.id_cuestionario = cg.id_cuestionario AND cg.id_grupo = " + idGrupo;
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

    public int getCuestionariosByName(String descuestionario) throws SQLException {
        int idCuestionario = 0;
        try {            
            sql = "SELECT * FROM test_c_cuestionario WHERE descripcion = '" + descuestionario + "'";
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

    public ArrayList<Cuestionario> getCuestionariosByNameList(String descuestionario) throws SQLException {
        ArrayList listaCuestionario = new ArrayList();
        Cuestionario cuestionario;
        try {            
            sql = "SELECT * FROM test_c_cuestionario WHERE descripcion = '" + descuestionario + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
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

    public double getCalificacionAlumno(int idCuestionario, int idUser) throws SQLException {
        double calificacion = 0.0;
        try {            
            int cantPreguntas = getPreguntasCuestionario(idCuestionario);
            sql = "select  SUM(CASE WHEN (r.estado = 1 and r.id_respuesta = ra.id_respuesta) THEN ROUND((5.0)/" + cantPreguntas + ", 1) ELSE 0 END) as calificacion "
                    + "from test_c_cuestionario c "
                    + "INNER JOIN  test_preguntas_cuestionario p on c.id_cuestionario = p.id_cuestionario "
                    + "INNER JOIN test_respuestas_cuestionario r ON r.id_fk = p.id "
                    + "INNER JOIN test_c_cuestionario_alumno ca ON ca.id_cuestionario = c.id_cuestionario AND ca.id_user= " + idUser + " "
                    + "INNER JOIN test_respuestas_alumno ra ON  ra.id_c_alumno = ca.id_c_alumno and ra.id_respuesta = r.id_respuesta "
                    + "WHERE c.id_cuestionario = " + idCuestionario + " limit 1";
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

    public void generateReporte(int idCuestionario, int id_alumno, int idca, int total_preguntas, double nota, String notaString) throws SQLException {
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

    public ArrayList<String> reporteGeneralResultados(String grupo, String cuestionario) throws SQLException {
        ArrayList resultGeneral = new ArrayList();
        try {
     
            sql = "SELECT u.documento, "
                    + "u.nombres, "
                    + "u.apellidos, "
                    + "c.descripcion, "
                    + "c.objetivo, "
                    + "g.grupo, "
                    + "r.nota, "
                    + " r.tiempo, "
                    + "r.fecha_presentacion, "
                    + "CASE WHEN (r.aprobo = 1) THEN 'Aprobo' ELSE 'No Aprobo' END as calificacion "
                    + "FROM test_c_resultados r "
                    + "INNER JOIN test_c_cuestionario c ON r.id_cuestionario = c.id_cuestionario "
                    + "INNER JOIN test_grupo g ON g.id_grupo = r.id_grupo "
                    + "INNER JOIN test_usuarios u ON u.id_user = r.id_user "
                    + "WHERE c.descripcion = '" + cuestionario + "' AND g.grupo = '" + grupo + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                String Obj = rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5) + ","
                        + rs.getString(6) + "," + rs.getDouble(7) + "," + rs.getString(8) + "," + rs.getString(9) + "," + rs.getString(10);
                resultGeneral.add(Obj);
            }

        } catch (Exception e) {
            System.out.println("error aqui" + e);
        }
        return resultGeneral;
    }

    public void activeCuestionarios() throws SQLException {
        try {
            deactiveCuestionarios();         
            sql = "SELECT * FROM test_c_cuestionario WHERE estado = 0 AND vigencia = '" + df.format(date) + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                sql = "UPDATE test_c_cuestionario SET estado = ? WHERE id_cuestionario = ?";
                pstm = cn.prepareStatement(sql);
                pstm.setBoolean(1, true);
                pstm.setInt(2, rs.getInt("id_cuestionario"));
                pstm.executeUpdate();
                System.out.println("cuestionario activado " + rs.getInt("id_cuestionario"));
            }
        } catch (SQLException ex) {
            System.out.println("error activar" + ex);
        }
    }

    public void deactiveCuestionarios() throws SQLException {
        try {           
            sql = "SELECT * FROM test_c_cuestionario WHERE estado = 1 AND vigencia <> '" + df.format(date) + "'";
            pstm = cn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                sql = "UPDATE test_c_cuestionario SET estado = ? WHERE id_cuestionario = ?";
                pstm = cn.prepareStatement(sql);
                pstm.setBoolean(1, false);
                pstm.setInt(2, rs.getInt("id_cuestionario"));
                pstm.executeUpdate();
                System.out.println("cuestionario desactivado " + rs.getInt("id_cuestionario"));
            }
        } catch (SQLException ex) {
            System.out.println("error desactivar" + ex);
        }
    }

}
