/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import App.AsignCuestionaryToGroup;
import App.Principal;
import Model.Asignaturas;
import Model.AsignaturasDAO;
import Model.Cuestionario;
import Model.CuestionarioDAO;
import Model.CuestionariosGrupos;
import Model.CuestionariosGruposDAO;
import Model.Grupo;
import Model.GrupoDAO;
import Model.PreguntasCuestionario;
import Model.PreguntasCuestionarioDAO;
import Model.RespuestasAlumno;
import Model.RespuestasAlumnoDAO;
import Model.RespuestasCuestionario;
import Model.RespuetasCuestionarioDAO;
import Model.UsersDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Mauricio Herrera
 */
public final class CuestionarioController extends WindowAdapter implements ActionListener, ItemListener {

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
    GrupoDAO grupdao = new GrupoDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    CuestionariosGruposDAO cgruposDao = new CuestionariosGruposDAO();
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    ArrayList<RespuestasAlumno> objRespuestasAlumno = new ArrayList<>();
    ArrayList<RespuestasAlumno> TempRespuestasAlumno = new ArrayList<>();
    ArrayList<PreguntasCuestionario> preguntasCList = new ArrayList<>();
    ArrayList<PreguntasCuestionario> ListPreguntas = new ArrayList<>();
    ArrayList<PreguntasCuestionario> ListPreguntasTemp = new ArrayList<>();
    ArrayList<RespuestasCuestionario> ListRespuestas = new ArrayList<>();
    ArrayList<RespuestasCuestionario> ListRespuestasTemp = new ArrayList<>();
    ArrayList<CuestionariosGrupos> ListCuestioariosGroups = new ArrayList<>();
    ArrayList<Integer> sortQuestions = new ArrayList<>();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    int idCuest = 0;
    int idUserLog;
    public JRadioButton rb[];
    public JCheckBox cb[];
    int id_pregunta = 0;
    int pregunt = 0;
    int TotalPreguntas;
    int cantCuestionario;
    boolean estado = false;
    int idPtemp;
    int idtempfk;
    boolean estadoRespuesta;
    int idNextOregunta;
    int contPregunta = 0;
    int deleteSpinner = 0;
    Date date = new Date();//para capturar la fecha actual
    AsignCuestionaryToGroup ac = new AsignCuestionaryToGroup(null, true);

    public CuestionarioController(Principal pr, int idGrupo, int idUserLog) {
        this.pr = pr;
        this.idGrupo = idGrupo;
        this.idUserLog = idUserLog;
        this.pr.cboAsignatura.addActionListener(this);
        this.pr.cboPreguntas.addActionListener(this);
        this.pr.btnCancelarC.addActionListener(this);
        this.pr.btnNextQuestion.addActionListener(this);
        this.pr.btnPreviousQuestion.addActionListener(this);
        this.pr.btnRegistrarCuestionary.addActionListener(this);
        this.pr.chkEstado.addItemListener(this);
        this.pr.btnAddRespuesta.addActionListener(this);
        this.pr.btnAddPregunta.addActionListener(this);
        this.pr.rdoTrue.addItemListener(this);
        this.pr.rdoFalse.addItemListener(this);
        this.pr.btnCancelarCuestionary.addActionListener(this);
        this.pr.asignCuestionaryToGroup.addActionListener(this);
        this.ac.btnSaveCtoGroup.addActionListener(this);
        this.ac.addWindowListener(this);
        this.pr.btnRegistrarCAlumno.addActionListener(this);
        cargarComboBox();
        System.out.println("user " + idUserLog + " grupo " + idGrupo);
        cargarCuestionarioByGrupo(idGrupo);
        if (cantCuestionario == 1) {
            showPreguntasCuestionario(0);
        }
        if (pregunt == 0) {
            pr.btnPreviousQuestion.setEnabled(false);
        }
        enabledAnswer();
        pr.dcFechaCuestionary.setDate(date);
    }

    public void cargarPreguntasToRespuestas(boolean v) {
        pr.rdoTrue.setEnabled(true);
        if (v) {
            pr.cboPreguntas.removeAllItems();
            pr.cboPreguntas.addItem("-- Seleccione --");
            Iterator<PreguntasCuestionario> preguntas = ListPreguntasTemp.iterator();
            while (preguntas.hasNext()) {
                PreguntasCuestionario pc = preguntas.next();
                pr.cboPreguntas.addItem(pc.getPregunta());
            }
        } else {
            pr.cboPreguntas.removeAllItems();
            pr.cboPreguntas.addItem("-- Seleccione --");
        }

    }

    public void llenarRespuestasAlumno() {
        TotalPreguntas = cuestionariodao.getPreguntasCuestionario(idCuest);
        System.out.println("totalpreg = " + TotalPreguntas);
        for (int i = 0; i < TotalPreguntas; i++) {
            preguntasCList.add(new PreguntasCuestionario());
            sortQuestions.add(i);
        }
        for (int i = 0; i < TotalPreguntas; i++) {
            objRespuestasAlumno.add(new RespuestasAlumno());
        }
        for (Iterator<RespuestasAlumno> iterator = objRespuestasAlumno.iterator(); iterator.hasNext();) {
            RespuestasAlumno next = iterator.next();
            System.out.println(next.getIdPregunta());
        }
    }

    public void cargarPreguntasInTable(JTable tbPreguntas) {
        String Titulos[] = {"", "Orden", "Descripcion"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<PreguntasCuestionario> preguntas = ListPreguntas.iterator();
        while (preguntas.hasNext()) {
            PreguntasCuestionario pc = preguntas.next();
            columna[0] = pc.getId();
            columna[1] = pc.getIdPregunta() + 1;
            columna[2] = pc.getPregunta();
            modelo.addRow(columna);
        }
        tbPreguntas.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbPreguntas.setRowSorter(ordenar);
        tbPreguntas.getColumnModel().getColumn(0).setMaxWidth(0);
        tbPreguntas.getColumnModel().getColumn(0).setMinWidth(0);
        tbPreguntas.getColumnModel().getColumn(0).setPreferredWidth(0);
        tbPreguntas.getColumnModel().getColumn(1).setMaxWidth(55);
        tbPreguntas.getColumnModel().getColumn(1).setMinWidth(55);
        tbPreguntas.getColumnModel().getColumn(1).setPreferredWidth(55);
        tbPreguntas.setModel(modelo);
    }

    public void cargarRespuestasInTable(JTable tbRespuestas) {
        String Titulos[] = {"Respuesta", "Estado"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<RespuestasCuestionario> respuestas = ListRespuestasTemp.iterator();
        while (respuestas.hasNext()) {
            RespuestasCuestionario rc = respuestas.next();
            columna[0] = rc.getRespuesta();
            columna[1] = (rc.isEstado()) ? "Verdadero" : "Falso";
            modelo.addRow(columna);
        }
        tbRespuestas.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbRespuestas.setRowSorter(ordenar);
        tbRespuestas.getColumnModel().getColumn(1).setMaxWidth(80);
        tbRespuestas.getColumnModel().getColumn(1).setMinWidth(80);
        tbRespuestas.getColumnModel().getColumn(1).setPreferredWidth(80);
        tbRespuestas.setModel(modelo);
    }

    public void cargarComboBox() {
        pr.cboAsignature.removeAllItems();
        pr.cboAsignature.addItem("-- Seleccione --");
        Iterator<Asignaturas> nombreIterator = asdao.getListCboAsignaturas().iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas elemento = nombreIterator.next();
            pr.cboAsignature.addItem(elemento.getNombreAsignatura());
        }
    }

    public void cargarCuestionarioByGrupo(int id_grupo) {
        cantCuestionario = cuestionariodao.getCuestionarioByGrupo(id_grupo).size();
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
                    idCuest = elementoC.getIdCuestionario();
                    llenarRespuestasAlumno();
                    pr.tProfesor.setText(udao.getProfesor(elementoC.getIdUser()));
                    pr.tCuestionario.setText(elementoC.getDescripcion());
                    pr.tGrado.setText(grupdao.getListGrupoToString(idGrupo).get(0).getGrupo());
                    cargarPreguntasCuestionario(elementoC.getIdCuestionario());
                }

            }
        }
    }

    public void cargarPreguntasCuestionario(int idCuestionario) {
        Random rndm = new Random();
        ArrayList<PreguntasCuestionario> temp = preguntasdao.getPreguntasCuestionario(idCuestionario);
        Collections.shuffle(temp, rndm);
        Iterator<PreguntasCuestionario> nombreIterator = temp.iterator();
        int preg = 0;
        while (nombreIterator.hasNext()) {
            PreguntasCuestionario pc = nombreIterator.next();
            System.out.println("orden =" + preg + " -> " + pc.getIdPregunta() + "  vv " + pc.getPregunta());
            preguntasCList.set(sortQuestions.get(preg), pc);
            preg++;
        }
    }

    public void showPreguntasCuestionario(int p) {
        int pregunta = p + 1;
        pr.txtPreguntas.setText(pregunta + ") " + preguntasCList.get(p).getPregunta());
        cargarRespuestasCuestionario(p);
    }

    public void cargarRespuestasCuestionario(int p) {
        id_pregunta = preguntasCList.get(p).getIdPregunta();
        int cantResp = respuestasdao.getRespuestasCuestionario(id_pregunta, idCuest).size();
        pr.pnRespuestas.removeAll();
        pr.pnRespuestas.setLayout(new java.awt.GridLayout(cantResp, 1));
        rb = new JRadioButton[cantResp];
        int i = 0;
        Iterator<RespuestasCuestionario> nombreIterator = respuestasdao.getRespuestasCuestionario(id_pregunta, idCuest).iterator();
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
        if (TotalPreguntas > 1) {
            pr.btnNextQuestion.setEnabled(true);
        } else {
            pr.btnNextQuestion.setEnabled(false);
            pr.btnPreviousQuestion.setEnabled(false);
        }

    }

    public void cargarGruposToCuestionary() {
        int cantGrupos = grupdao.getListGrupos().size();
        System.out.println("grupos = " + cantGrupos);
        ac.pnGruposAdd.removeAll();
        ac.pnGruposAdd.setLayout(new java.awt.GridLayout(6, cantGrupos));
        cb = new JCheckBox[cantGrupos];
        int i = 0;
        Iterator<Grupo> nombreIterator = grupdao.getListGrupos().iterator();
        while (nombreIterator.hasNext()) {
            Grupo g = nombreIterator.next();
            cb[i] = new JCheckBox();
            cb[i].setText(g.getGrupo());
            cb[i].addActionListener(this);
            cb[i].addItemListener(this);
            ac.pnGruposAdd.add(cb[i]);
            i++;
        }
        cargarCuestionariesToAddGrup(ac);
        ac.pnGruposAdd.updateUI();
        ac.setLocationRelativeTo(null);
        ac.setVisible(true);
    }

    // ACTION PERFORMED
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < pr.pnRespuestas.getComponentCount(); i++) {
            if (e.getSource() == rb[i]) {
                RespuestasAlumno ra = new RespuestasAlumno();
                ra.setIdPregunta(id_pregunta);
                ra.setIdRespuesta(respuestasdao.getIdRespuesta(id_pregunta, rb[i].getText().trim()));
                objRespuestasAlumno.set(id_pregunta, ra);
                rb[i].setSelected(true);
                // Para probar las respuestas
//                Iterator<RespuestasAlumno> nombreIterator = objRespuestasAlumno.iterator();
//                while (nombreIterator.hasNext()) {
//                    RespuestasAlumno p = nombreIterator.next();
//                    System.out.println("pregunta " + p.getIdPregunta() + " Respuesta " + p.getIdRespuesta());
//                }
            }
        }

        if (e.getSource() == pr.asignCuestionaryToGroup) {
            cargarGruposToCuestionary();
        }

        if (e.getSource() == ac.btnSaveCtoGroup) {
            if (ListCuestioariosGroups.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No has seleccionado grupo(s) para asignar al cuestionario");
                ac.cboCuestionaries.requestFocus();
                return;
            }
            String rpta = cgruposDao.addGroupToCuestionario(ListCuestioariosGroups, opc);
            if (rpta != null) {
                JOptionPane.showMessageDialog(null, rpta);
                limpiarformToasingGroup();
                ac.dispose();
                opc = "C";
            } else if (opc.equals("C")) {
                JOptionPane.showMessageDialog(null, "No se pudo crear el Cuestionario");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el Cuestionario");
            }
        }

        if (e.getSource() == pr.btnRegistrarCuestionary) {
            if (pr.txtDescCuestionary.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar el titulo del cuestionario..");
                pr.txtDescCuestionary.requestFocus();
                return;
            }
            if (pr.cboAsignature.getSelectedItem().equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una asignatura.");
                pr.cboAsignature.requestFocus();
                return;
            }
            if (ListPreguntas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Aún no ha ingresado preguntas para el cuestionario");
                return;
            }
            if (ListRespuestas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Aún no ha ingresado respuestas para las preguntas del cuestionario");
                return;
            }
            Cuestionario c = new Cuestionario();
            String desccuestionario = pr.txtDescCuestionary.getText();
            String fecha = df.format(pr.dcFechaCuestionary.getDate());
            String asignatura = (String) pr.cboAsignature.getSelectedItem();
            c.setDescripcion(desccuestionario);
            c.setEstado(estado);
            c.setFecha(fecha);
            c.setIdAsignatura(asdao.getAsignaturaByName(asignatura));
            c.setIdUser(idUserLog);
            String rptaRegistro = cuestionariodao.createCuestionary(opc, c, ListPreguntas, ListRespuestas);
            if (rptaRegistro != null) {
                JOptionPane.showMessageDialog(null, rptaRegistro);
                opc = "C";
                idToUpdate = 0;
                limpiarForm();
            } else if (opc.equals("C")) {
                JOptionPane.showMessageDialog(null, "No se pudo crear el Cuestionario");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el Cuestionario");
            }
        }

        if (e.getSource() == pr.btnNextQuestion) {
            pregunt++;
            int temp = TotalPreguntas - 1;
            showPreguntasCuestionario(pregunt);
            System.out.println("temp = " + temp + " pregunt = " + pregunt);
            if (temp > pregunt) {
                pr.btnNextQuestion.setEnabled(true);
            } else {
                pr.btnNextQuestion.setEnabled(false);
                pr.btnPreviousQuestion.setEnabled(true);
            }
        }

        if (e.getSource() == pr.btnPreviousQuestion) {
            pregunt--;
            int temp = TotalPreguntas - 1;
            showPreguntasCuestionario(pregunt);
            System.out.println("temp = " + temp + " pregunt = " + pregunt);
            if (pregunt > 0) {
                pr.btnPreviousQuestion.setEnabled(true);
            } else {
                pr.btnPreviousQuestion.setEnabled(false);
                pr.btnNextQuestion.setEnabled(true);
            }
        }

        if (e.getSource() == pr.cboAsignatura) {
            String asignatura = (String) pr.cboAsignatura.getSelectedItem();
            Iterator<Cuestionario> nombreIterator = cuestionariodao.getCuestionario(asignatura, idGrupo).iterator();
            if (nombreIterator.hasNext()) {
                Cuestionario c = nombreIterator.next();
                String profesor = udao.getProfesor(c.getIdUser());
                idCuest = c.getIdCuestionario();
                if (cantCuestionario > 1) {
                    llenarRespuestasAlumno();
                }
                pr.tProfesor.setText(profesor);
                pr.tCuestionario.setText(c.getDescripcion());
                pr.tGrado.setText(grupdao.getListGrupoToString(idGrupo).get(0).getGrupo());
                if (cantCuestionario > 1) {
                    cargarPreguntasCuestionario(idCuest);
                    showPreguntasCuestionario(0);
                }
            } else {
                pr.tCuestionario.setText("");
                pr.tProfesor.setText("");
            }

        }

        if (e.getSource() == pr.cboPreguntas) {
            String pregunta = (String) pr.cboPreguntas.getSelectedItem();
            for (PreguntasCuestionario p : ListPreguntasTemp) {
                if (p.getPregunta().equals(pregunta)) {
                    idtempfk = p.getId();
                    idPtemp = p.getIdPregunta();// aumentar el id pregunta al guardarlo en el array ListPreguntas
                }
            }
            ListRespuestasTemp.clear();
            cargarRespuestasInTable(pr.tbRespuestasQ);
            pr.cboPreguntas.setEnabled(false);
        }

        if (e.getSource() == pr.btnAddPregunta) {
            String pregunta = pr.txtDescripPregunta.getText();
            if (pregunta.equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar la pregunta..");
                pr.txtDescripPregunta.requestFocus();
                return;
            }
            contPregunta = contPregunta + 1;
            PreguntasCuestionario pc = new PreguntasCuestionario();
            idNextOregunta = preguntasdao.nexIdPreguntaCuestionario();
            pc.setId(idNextOregunta + contPregunta);
            pc.setIdPregunta(deleteSpinner);
            pc.setPregunta(pregunta);
            ListPreguntas.add(pc);
            ListPreguntasTemp.add(pc);
            cargarPreguntasInTable(pr.tbPreguntasQ);
            cargarPreguntasToRespuestas(true);
            pr.txtDescripPregunta.setText("");
            enabledAnswer();
            deleteSpinner++;
        }

        if (e.getSource() == pr.btnAddRespuesta) {
            String literal = (String) pr.cboLiteral.getSelectedItem();
            String resp = pr.txtRespuestaQ.getText();
            if (literal.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un literal para la respuesta..");
                pr.cboLiteral.requestFocus();
                return;
            }
            if (resp.equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar la respuesta..");
                pr.txtRespuestaQ.requestFocus();
                return;
            }
            RespuestasCuestionario rc = new RespuestasCuestionario();
            rc.setIdPregunta(idPtemp);
            rc.setIdfk(idtempfk);
            rc.setEstado(estadoRespuesta);
            if (estadoRespuesta) {
                pr.rdoTrue.setEnabled(false);
                pr.rdoFalse.setSelected(true);
            }
            rc.setRespuesta(literal + ": " + resp);
            ListRespuestas.add(rc);
            ListRespuestasTemp.add(rc);
            cargarRespuestasInTable(pr.tbRespuestasQ);
            pr.txtRespuestaQ.setText("");
            pr.cboLiteral.removeItem(literal);
            System.out.println(pr.cboLiteral.getItemCount());
            if (pr.cboLiteral.getItemCount() == 1) {
                System.out.println("aqui");
                String pregunta = (String) pr.cboPreguntas.getSelectedItem();
                if (ListPreguntasTemp.size() > 1) {
                    for (PreguntasCuestionario p : ListPreguntasTemp) {
                        if (p.getPregunta().equals(pregunta)) {
                            ListPreguntasTemp.remove(p);
                        }
                    }
                    cargarPreguntasToRespuestas(true);
                } else {
                    cargarPreguntasToRespuestas(false);
                }
                pr.cboLiteral.removeAllItems();
                pr.cboLiteral.addItem("-- Seleccione --");
                pr.cboLiteral.addItem("A");
                pr.cboLiteral.addItem("B");
                pr.cboLiteral.addItem("C");
                pr.cboLiteral.addItem("D");
                pr.cboLiteral.addItem("E");
                pr.cboPreguntas.setEnabled(true);
            }
            if (pr.cboLiteral.getItemCount() > 2) {
                System.out.println("remover " + literal);
                System.out.println("lista " + ListPreguntasTemp.size());
            }
            pr.cboLiteral.setSelectedItem("-- Seleccione --");

        }

        if (e.getSource() == pr.btnCancelarCuestionary) {
            limpiarForm();
        }

        if (e.getSource() == pr.btnRegistrarCAlumno) {
            // Para probar las respuestas
            Iterator<RespuestasAlumno> nombreIterator = objRespuestasAlumno.iterator();
            int cont = 0;
            while (nombreIterator.hasNext()) {
                RespuestasAlumno p = nombreIterator.next();
                if (p.getIdPregunta() == 0 && p.getIdRespuesta() == 0) {
                    cont++;
                }
            }
            if (cont > 0) {
                int response = JOptionPane.showConfirmDialog(null, "<html>Está seguro enviar el Cuestionario?<br>Aún hay"
                        + " "+cont+" preguntas sin contestar </html>", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                   //pendiente guardar respuestas
                }
            }
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == pr.chkEstado) {
            if (pr.chkEstado.isSelected()) {
                estado = true;
            } else {
                estado = false;
            }
        }
        if (e.getSource() == pr.rdoTrue) {
            estadoRespuesta = true;
        }
        if (e.getSource() == pr.rdoFalse) {
            estadoRespuesta = false;
        }

        for (int i = 0; i < ac.pnGruposAdd.getComponentCount(); i++) {
            if (e.getSource() == cb[i]) {
                if (cb[i].isSelected()) {
                    System.out.println(cb[i].getText() + " = " + grupdao.getIdGrupoByName(cb[i].getText().trim()));
                    if (ac.cboCuestionaries.getSelectedItem().equals("-- Seleccione --")) {
                        JOptionPane.showMessageDialog(null, "Debes Seleccionar un cuestionario primero..");
                        ac.cboCuestionaries.requestFocus();
                        cb[i].setSelected(false);
                        System.out.println("tamaño if lista = " + ListCuestioariosGroups.size());
                        return;
                    }
                    CuestionariosGrupos cg = new CuestionariosGrupos();
                    cg.setIdCuestionario(cuestionariodao.getCuestionariosByName((String) ac.cboCuestionaries.getSelectedItem()));
                    cg.setIdGrupo(grupdao.getIdGrupoByName(cb[i].getText().trim()));
                    ListCuestioariosGroups.add(cg);
                    System.out.println("tamaño lista 2if= " + ListCuestioariosGroups.size());
                } else {
                    System.out.println("true " + cb[i].getText().trim());
                    Iterator<CuestionariosGrupos> cg = ListCuestioariosGroups.iterator();
                    while (cg.hasNext()) {
                        CuestionariosGrupos borrar = cg.next();
                        System.out.println("obj =" + borrar.getIdGrupo());
                        if (borrar.getIdGrupo() == grupdao.getIdGrupoByName(cb[i].getText().trim())) {
                            cg.remove();
                        }
                    }
                }

            }
        }

    }

    private void limpiarForm() {
        pr.txtDescCuestionary.setText("");
        pr.dcFechaCuestionary.setDate(date);
        pr.chkEstado.setSelected(false);
        pr.cboAsignature.setSelectedItem("-- Seleccione --");
        pr.txtDescripPregunta.setText("");
        ListPreguntas.clear();
        ListPreguntasTemp.clear();
        ListRespuestas.clear();
        ListRespuestasTemp.clear();
        cargarPreguntasInTable(pr.tbPreguntasQ);
        cargarRespuestasInTable(pr.tbRespuestasQ);
        pr.cboPreguntas.setSelectedItem("-- Seleccione --");
        pr.cboLiteral.addItem("-- Seleccione --");
        pr.txtRespuestaQ.setText("");
        pr.rdoTrue.setSelected(true);
        pr.rdoFalse.setSelected(false);
    }

    public void enabledAnswer() {
        if (ListPreguntasTemp.size() > 0) {
            pr.cboPreguntas.setEnabled(true);
            pr.cboLiteral.setEnabled(true);
            pr.txtRespuestaQ.setEnabled(true);
            pr.rdoTrue.setEnabled(true);
            pr.rdoTrue.setSelected(true);
            pr.rdoFalse.setEnabled(true);
            pr.btnAddRespuesta.setEnabled(true);
        } else {
            pr.cboPreguntas.setEnabled(false);
            pr.cboLiteral.setEnabled(false);
            pr.txtRespuestaQ.setEnabled(false);
            pr.rdoTrue.setEnabled(false);
            pr.rdoFalse.setEnabled(false);
            pr.btnAddRespuesta.setEnabled(false);
        }
    }

    private void cargarCuestionariesToAddGrup(AsignCuestionaryToGroup ac) {
        ac.cboCuestionaries.removeAllItems();
        ac.cboCuestionaries.addItem("-- Seleccione --");
        Iterator<Cuestionario> nombreIterator = cuestionariodao.getCuestionariosByProfesor(idUserLog).iterator();
        while (nombreIterator.hasNext()) {
            Cuestionario elemento = nombreIterator.next();
            ac.cboCuestionaries.addItem(elemento.getDescripcion());
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if (e.getSource() == ac) {
            ListCuestioariosGroups.clear();
        }
    }

    private void limpiarformToasingGroup() {
        ac.cboCuestionaries.removeAllItems();
    }

}
