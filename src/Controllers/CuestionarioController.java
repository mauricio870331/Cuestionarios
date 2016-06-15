/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import App.Principal;
import Model.Asignaturas;
import Model.AsignaturasDAO;
import Model.Cuestionario;
import Model.CuestionarioDAO;
import Model.PreguntasCuestionario;
import Model.PreguntasCuestionarioDAO;
import Model.RespuestasAlumno;
import Model.RespuestasAlumnoDAO;
import Model.RespuestasCuestionario;
import Model.RespuetasCuestionarioDAO;
import Model.UsersDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Mauricio Herrera
 */
public final class CuestionarioController implements ActionListener, MouseListener {

    int idToUpdate = 0;
    int id_rol;
    int rol = 0;
    int idGrupo;
    int pagina = 1;
    String dato = "";
    Principal pr;
    Cuestionario cuestionario;
    CuestionarioDAO cuestionariodao = new CuestionarioDAO();
    PreguntasCuestionarioDAO preguntasdao = new PreguntasCuestionarioDAO();
    RespuetasCuestionarioDAO respuestasdao = new RespuetasCuestionarioDAO();
    RespuestasAlumnoDAO respuestasAlumnodao = new RespuestasAlumnoDAO();
    AsignaturasDAO asdao = new AsignaturasDAO();
    UsersDAO udao = new UsersDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    ArrayList<RespuestasAlumno> objRespuestasAlumno = new ArrayList<>();
    int idCuest = 0;
    int idUserLog;
    public JRadioButton rb[];
    int id_pregunta;

    public CuestionarioController(Principal pr, int idGrupo, int idUserLog) {
        this.pr = pr;
        this.idGrupo = idGrupo;
        this.idUserLog = idUserLog;
//        this.pr.cboAsignatura.addActionListener(this);
        this.pr.btnCancelarC.addActionListener(this);
//        this.pr.tbPreguntasC.addMouseListener(this);
        cargarCuestionarioByGrupo(idGrupo);
        System.out.println("user " + idUserLog);
//        cargarAsignaturas();
        cargarRespuestasCuestionario(0);
        llenarRespuestasAlumno();
    }

//    public void cargarAsignaturas() {
//        pr.cboAsignatura.removeAllItems();
//        Iterator<Asignaturas> nombreIterator = null;
//        pr.cboAsignatura.addItem("-- Seleccione --");
//        nombreIterator = asdao.getListCboAsignaturas().iterator();
//        while (nombreIterator.hasNext()) {
//            Asignaturas elemento = nombreIterator.next();
//            pr.cboAsignatura.addItem(elemento.getAsignatura() + " - " + elemento.getNombreAsignatura());
//        }
//
//    }
    public void llenarRespuestasAlumno() {
        int total = cuestionariodao.getPreguntasCuestionario(1);
        for (int i = 0; i < total; i++) {
            objRespuestasAlumno.add(new RespuestasAlumno());
        }
    }

//    public void cargarCuestionario(JTable tbAdmin, String dato, int rol) {
//        String Titulos[] = {"", "Documento", "Nombres", "Apellidos"};
//        modelo = new DefaultTableModel(null, Titulos) {
//            @Override
//            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
//                return false;
//            }
//        };
//        Object[] columna = new Object[6];
//        Iterator<Cuestionario> nombreIterator = cuestionariodao.getCuestionario().iterator();
//        while (nombreIterator.hasNext()) {
//            Cuestionario c = nombreIterator.next();
//            columna[0] = c.getIdCuestionario();
//            columna[1] = c.getIdUser();
//            columna[2] = c.getDescripcion();
//            columna[3] = c.getFecha();
//            columna[4] = c.isEstado();
//            modelo.addRow(columna);
//        }
//        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
//        tbAdmin.setRowSorter(ordenar);
//        tbAdmin.setModel(modelo);
//    }
    public void cargarCuestionarioByGrupo(int id_grupo) {
        int cantCuestionario = cuestionariodao.getCuestionarioByGrupo(id_grupo).size();
        if (cantCuestionario > 1) {
            pr.cboAsignatura.removeAllItems();
            pr.cboAsignatura.addItem("-- Seleccione --");
            Iterator<Asignaturas> nombreIterator = asdao.getAsignaturasByCuestionario(id_grupo).iterator();
            while (nombreIterator.hasNext()) {
                Asignaturas elemento = nombreIterator.next();
                pr.cboAsignatura.addItem(elemento.getNombreAsignatura());
            }
        } else {
            pr.cboAsignatura.removeAllItems();
            Iterator<Asignaturas> nombreIterator = asdao.getAsignaturasByCuestionario(id_grupo).iterator();
            if (nombreIterator.hasNext()) {
                Asignaturas elemento = nombreIterator.next();
                pr.cboAsignatura.addItem(elemento.getNombreAsignatura());
                pr.cboAsignatura.setEnabled(false);
                Iterator<Cuestionario> itrC = cuestionariodao.getCuestionarioByGrupo(id_grupo).iterator();
                if (itrC.hasNext()) {
                    Cuestionario elementoC = itrC.next();
                    pr.tProfesor.setText(udao.getProfesor(elementoC.getIdUser()));
                }
            }
        }
    }

    public void cargarPreguntasCuestionario(JTable tbAdmin, int idCuestionario) {

    }

    public void cargarRespuestasCuestionario(int idpregunta) {
        id_pregunta = idpregunta;
        int cantResp = respuestasdao.getRespuestasCuestionario(idpregunta).size();
        pr.pnRespuestas.removeAll();
        pr.pnRespuestas.setLayout(new java.awt.GridLayout(1, cantResp));
        rb = new JRadioButton[cantResp];
        int i = 0;
        Iterator<RespuestasCuestionario> nombreIterator = respuestasdao.getRespuestasCuestionario(idpregunta).iterator();
        while (nombreIterator.hasNext()) {
            RespuestasCuestionario rc = nombreIterator.next();
            rb[i] = new JRadioButton();
            rb[i].setText(rc.getRespuesta());
            rb[i].addActionListener(this);
            pr.GrupoRespuestas.add(rb[i]);
            pr.pnRespuestas.add(rb[i]);
            i++;
        }
        pr.pnRespuestas.updateUI();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < pr.pnRespuestas.getComponentCount(); i++) {
            if (e.getSource() == rb[i]) {
                RespuestasAlumno ra = new RespuestasAlumno();
                ra.setIdPregunta(id_pregunta);
                ra.setIdRespuesta(respuestasdao.getIdRespuesta(id_pregunta, rb[i].getText().trim()));
                objRespuestasAlumno.set(id_pregunta, ra);
                Iterator<RespuestasAlumno> nombreIterator = objRespuestasAlumno.iterator();
                while (nombreIterator.hasNext()) {
                    RespuestasAlumno p = nombreIterator.next();
                    System.out.println("pregunta " + p.getIdPregunta() + " Respuesta " + p.getIdRespuesta());
                }
            }
        }

//        if (e.getSource() == pr.cboAsignatura) {
//            String as = (String) pr.cboAsignatura.getSelectedItem();
//            int asignatura = 0;
//            if (!as.equals("-- Seleccione --")) {
//                String[] idaseparated = as.split("-");
//                asignatura = Integer.parseInt(idaseparated[0].trim());
//            }
//            Iterator<Cuestionario> nombreIterator = cuestionariodao.getCuestionario(asignatura, idGrupo).iterator();
//            if (nombreIterator.hasNext()) {
//                Cuestionario c = nombreIterator.next();
//                String profesor = udao.getProfesor(c.getIdUser());
//                idCuest = c.getIdCuestionario();
//                pr.tProfesor.setText(profesor);
//                pr.tCuestionario.setText(c.getDescripcion());
//
//            } else {
//                pr.tCuestionario.setText("");
//                pr.tProfesor.setText("");
//            }
//
//        }
        if (e.getSource() == pr.btnCancelarC) {

//            System.out.println(pr.tbPreguntasC.getModel().getValueAt(0, 2).toString());
//            probarvalores();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        int fila = pr.tbPreguntasC.rowAtPoint(e.getPoint());
//        int columna = pr.tbPreguntasC.columnAtPoint(e.getPoint());
//        String valor = pr.tbPreguntasC.getModel().getValueAt(fila, columna).toString();
//        if (!valor.equals("")) {
//            System.out.println(valor);
//        }

    }

    @Override
    public void mousePressed(MouseEvent e
    ) {

    }

    @Override
    public void mouseReleased(MouseEvent e
    ) {
    }

    @Override
    public void mouseEntered(MouseEvent e
    ) {
    }

    @Override
    public void mouseExited(MouseEvent e
    ) {
    }

//    private void probarvalores() {       
//        int filas = pr.tbPreguntasC.getRowCount();
//        int columnas = pr.tbPreguntasC.getColumnCount();
//        RespuestasAlumno ra;        
//        int pregunta = 0;
//        for (int i = 0; i < filas; i++) {
//            ra = new RespuestasAlumno();
//            for (int j = 0; j < columnas; j++) {
//                switch (j) {
//                    case 0:
//                        String idPregunta = (String) pr.tbPreguntasC.getModel().getValueAt(i, j).toString();                        
//                        String[] idPSeparated = idPregunta.split("-");
//                        pregunta = Integer.parseInt(idPSeparated[0].trim());
//                        ra.setIdPregunta(pregunta);
//                        break;
//                    case 5:
//                        String rpta = pr.tbPreguntasC.getModel().getValueAt(i, j).toString();                        
//                        ra.setIdRespuesta(respuestasdao.getIdRespuesta(pregunta, rpta.trim()));
//                        break;
//                }
//            }
//            objAlumno.add(ra);
//        }
//        Iterator<RespuestasAlumno> nombreIterator = objAlumno.iterator();
//        while (nombreIterator.hasNext()) {
//            RespuestasAlumno p = nombreIterator.next();
//            System.out.println("pregunta "+p.getIdPregunta() + " Respuesta "+p.getIdRespuesta());
//        }
//
//    }
}
