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
import Model.CCuestionarioAlumno;
import Model.CCuestionarioAlumnoDAO;
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
import javax.swing.Timer;
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
    CCuestionarioAlumnoDAO ccuestionarioalumnodao = new CCuestionarioAlumnoDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    CuestionariosGruposDAO cgruposDao = new CuestionariosGruposDAO();
    String opc = "C";
    ArrayList<RespuestasAlumno> objRespuestasAlumno = new ArrayList<>();
    ArrayList<RespuestasAlumno> TempRespuestasAlumno = new ArrayList<>();
    ArrayList<PreguntasCuestionario> preguntasCList = new ArrayList<>();
    ArrayList<PreguntasCuestionario> ListPreguntas = new ArrayList<>();
    ArrayList<PreguntasCuestionario> ListPreguntasTemp = new ArrayList<>();
    ArrayList<RespuestasCuestionario> ListRespuestas = new ArrayList<>();
    ArrayList<RespuestasCuestionario> ListRespuestasTemp = new ArrayList<>();
    ArrayList<CuestionariosGrupos> ListCuestioariosGroups = new ArrayList<>();
    ArrayList<String> tempEstados = new ArrayList<>();
    ArrayList<Integer> sortQuestions = new ArrayList<>();
    Date date = new Date();//para capturar la fecha actual
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    int idCuest = 0;
    int idUserLog;
    public JRadioButton rb[];
    public JCheckBox cb[];
    int id_pregunta = 0;
    int pregunt = 0;
    int TotalPreguntas;
    int cantCuestionario;
    boolean estado = true;
    int idPtemp;
    int idtempfk;
    boolean estadoRespuesta;
    int idNextPregunta;
    int contPregunta = 0;
    int deleteSpinner = 0;
    Timer t;
    int h, m, s, cs;
    AsignCuestionaryToGroup ac = new AsignCuestionaryToGroup(null, true);

    public CuestionarioController(Principal pr, int idGrupo, int idUserLog, int rol) {
        this.pr = pr;
        this.idGrupo = idGrupo;
        this.idUserLog = idUserLog;
        this.pr.cboAsignatura.addActionListener(this);
        this.pr.cboPreguntas.addActionListener(this);
//        this.pr.btnCancelarC.addActionListener(this);
        this.pr.btnNextQuestion.addActionListener(this);
        this.pr.btnPreviousQuestion.addActionListener(this);
        this.pr.btnRegistrarCuestionary.addActionListener(this);
//        this.pr.chkEstado.addItemListener(this);
        this.pr.btnAddRespuesta.addActionListener(this);
        this.pr.btnAddPregunta.addActionListener(this);
        this.pr.rdoTrue.addItemListener(this);
        this.pr.rdoFalse.addItemListener(this);
        this.pr.btnCancelarCuestionary.addActionListener(this);
        this.pr.asignCuestionaryToGroup.addActionListener(this);
        this.pr.createUsers.addActionListener(this);
        this.pr.cuestionario.addActionListener(this);
        this.ac.btnSaveCtoGroup.addActionListener(this);
        this.ac.addWindowListener(this);
        this.pr.btnIniciarPrueba.addActionListener(this);//        this.pr.btnRegistrarCAlumno.addActionListener(this);
        this.pr.btnPreviousQuestion.setVisible(false);
        System.out.println("user " + idUserLog + " grupo " + idGrupo);
        if (rol == 2) {
            this.pr.tGrado.setText(grupdao.getListGrupoToString(idGrupo).get(0).getGrupo());
        }
        cargarCuestionarioByGrupo(idGrupo);
        if (cantCuestionario == 1) {
            showPreguntasCuestionario(0);
        }
        if (pregunt == 0) {
            pr.btnPreviousQuestion.setEnabled(false);
        }
        enabledAnswer();
        pr.btnNextQuestion.setEnabled(false);
        pr.btnIniciarPrueba.setEnabled(false);
//        pr.dcFechaCuestionary.setDate(date);
        t = new Timer(10, acciones);
    }

    public ActionListener acciones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ++cs;
            if (cs == 100) {
                cs = 0;
                ++s;
            }
            if (s == 60) {
                s = 0;
                ++m;
            }
            if (m == 60) {
                m = 0;
                ++h;
            }
            actualizarlblTiempo();
        }
    };

    private void actualizarlblTiempo() {
        String tiempo = (h<=9?"0":"")+h+":"+(m<=9?"0":"")+m+":"+(s<=9?"0":"")+s+":"+(cs<=9?"0":"")+cs;
        pr.lblTiempo.setText(tiempo);
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
        preguntasCList.clear();
        sortQuestions.clear();
        tempEstados.clear();
        objRespuestasAlumno.clear();
        if (TotalPreguntas > 0) {
            for (int i = 0; i < TotalPreguntas; i++) {
                preguntasCList.add(new PreguntasCuestionario());
                sortQuestions.add(i);
                tempEstados.add(i, "");
            }
            for (int i = 0; i < TotalPreguntas; i++) {
                objRespuestasAlumno.add(new RespuestasAlumno());
            }
            for (Iterator<RespuestasAlumno> iterator = objRespuestasAlumno.iterator(); iterator.hasNext();) {
                RespuestasAlumno next = iterator.next();
                System.out.println("esta : " + next.getIdPregunta());//limpiar el array de respuestass
            }
        } else {
            pregunt = 0;
//            pr.btnNextQuestion.setEnabled(false);
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

    public void cargarCuestionarioByGrupo(int id_grupo) {
        cantCuestionario = cuestionariodao.getCuestionarioByGrupo(id_grupo).size();
        if (cantCuestionario > 1) {
            System.out.println("cantc");
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
//                    System.out.println("por aqui user: "+elementoC.getIdUser());
//                    pr.txtAlumnoName.setText(udao.getUser(elementoC.getIdUser()));
                    pr.tCuestionario.setText(elementoC.getDescripcion());
                    pr.txtObjetivoCuestionario.setText(elementoC.getObjetivo());
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
        int cantTempEstados = tempEstados.size();
        pr.pnRespuestas.removeAll();
        pr.pnRespuestas.setLayout(new java.awt.GridLayout(cantResp, 1));
        rb = new JRadioButton[cantResp];
        int i = 0;
        Iterator<RespuestasCuestionario> nombreIterator = respuestasdao.getRespuestasCuestionario(id_pregunta, idCuest).iterator();
        while (nombreIterator.hasNext()) {
            RespuestasCuestionario rc = nombreIterator.next();
            rb[i] = new JRadioButton();
            rb[i].setName("rpta" + i);
            rb[i].setText(rc.getRespuesta());
            rb[i].addActionListener(this);
            pr.GrupoRespuestas.add(rb[i]);
            pr.pnRespuestas.add(rb[i]);
            if (cantTempEstados > p) {
                System.out.println("mayor que 0");
                if (tempEstados.get(p).equals(rb[i].getName())) {
                    rb[i].setSelected(true);
                }
            }
            i++;
        }
        pr.pnRespuestas.updateUI();
//        if (TotalPreguntas > 1) {
//            pr.btnNextQuestion.setEnabled(true);
//        } else {
//            pr.btnNextQuestion.setEnabled(false);
//            pr.btnPreviousQuestion.setEnabled(false);
//        }

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
                tempEstados.set(pregunt, rb[i].getName());
                // Para probar las respuestas
                Iterator<RespuestasAlumno> nombreIterator = objRespuestasAlumno.iterator();
                while (nombreIterator.hasNext()) {
                    RespuestasAlumno p = nombreIterator.next();
                    System.out.println("pregunta " + p.getIdPregunta() + " Respuesta " + p.getIdRespuesta());
                }
                pr.btnNextQuestion.setEnabled(true);
            }
        }
        
        if (e.getSource() == pr.btnIniciarPrueba) {
            t.start();
            pr.btnIniciarPrueba.setEnabled(false);
        }

        if (e.getSource() == pr.asignCuestionaryToGroup) {
            cargarGruposToCuestionary();
        }

        if (e.getSource() == pr.cuestionario) {
            pr.pnCreateAdmin.setVisible(false);
            pr.pnCreateCuestionary.setVisible(true);
        }

        if (e.getSource() == pr.createUsers) {
            pr.pnCreateAdmin.setVisible(true);
            pr.pnCreateCuestionary.setVisible(false);
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
            if (pr.txtObjetivo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar el Objetivo del cuestionario..");
                pr.txtObjetivo.requestFocus();
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
            String objetivo = pr.txtObjetivo.getText();
            String fecha = df.format(date);
            String asignatura = (String) pr.cboAsignature.getSelectedItem();
            c.setDescripcion(desccuestionario);
            c.setObjetivo(objetivo);
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
            pr.btnNextQuestion.setEnabled(false);
            if (pr.btnNextQuestion.getText().equals("Finalizar")) {
                System.out.println("fin");
                guardarCuestionarioAlumno();
            } else {
                pregunt++;
                int temp = TotalPreguntas - 1;
                showPreguntasCuestionario(pregunt);
                guardarCuestionarioAlumno();
                if (temp > pregunt) {
                    pr.btnNextQuestion.setEnabled(true);
                } else if (temp == pregunt) {
                    pr.btnNextQuestion.setText("Finalizar");
//                pr.btnNextQuestion.setEnabled(false);
//                pr.btnPreviousQuestion.setEnabled(true);
                }
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
            if (asignatura.equals("-- Seleccione --")) {
                clearComponent();
                return;
            }
            Iterator<Cuestionario> nombreIterator = cuestionariodao.getCuestionario(asignatura, idGrupo).iterator();
            if (nombreIterator.hasNext()) {
                pr.btnIniciarPrueba.setEnabled(true);
                Cuestionario c = nombreIterator.next();
//                String profesor = udao.getProfesor(c.getIdUser());
                idCuest = c.getIdCuestionario();
                if (cantCuestionario > 1) {
                    llenarRespuestasAlumno();
                }
                pr.tCuestionario.setText(c.getDescripcion());
                pr.txtObjetivoCuestionario.setText(c.getObjetivo());
                if (cantCuestionario > 1) {
                    System.out.println("id cuestionario = " + idCuest);
                    if (TotalPreguntas > 0) {
                        cargarPreguntasCuestionario(idCuest);
                        showPreguntasCuestionario(0);
                    } else {
                        clearComponent();
                    }
                }

            } else {
                pr.tCuestionario.setText("");
                pr.txtObjetivoCuestionario.setText("");
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
            idNextPregunta = preguntasdao.nexIdPreguntaCuestionario();
            pc.setId(idNextPregunta + contPregunta);
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

//        if (e.getSource() == pr.btnRegistrarCAlumno) {
//            Iterator<RespuestasAlumno> nombreIterator = objRespuestasAlumno.iterator();
//            int cont = 0;
//            while (nombreIterator.hasNext()) {
//                RespuestasAlumno p = nombreIterator.next();
//                if (p.getIdPregunta() == -1 && p.getIdRespuesta() == -1) {
//                    cont++;
//                }
//            }
//            if (cont > 0) {
//                int response = JOptionPane.showConfirmDialog(null, "<html>Está seguro enviar el Cuestionario?<br>Aún hay"
//                        + " " + cont + " preguntas sin contestar </html>", "Aviso..!",
//                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                if (response == JOptionPane.YES_OPTION) {
//                    guardarCuestionarioAlumno();
//                }
//            } else {
//                guardarCuestionarioAlumno();
//            }
//        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
//        if (e.getSource() == pr.chkEstado) {
//            if (pr.chkEstado.isSelected()) {
//                estado = true;
//            } else {
//                estado = false;
//            }
//        }
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
//        pr.dcFechaCuestionary.setDate(date);
//        pr.chkEstado.setSelected(false);
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

    private void limpiarformRespuestasAlumno() {
        objRespuestasAlumno.clear();
        tempEstados.clear();
        sortQuestions.clear();
        ListPreguntas.clear();
        ListPreguntasTemp.clear();
        ListRespuestas.clear();
        ListRespuestasTemp.clear();
        pr.cboAsignatura.removeItem((String) pr.cboAsignatura.getSelectedItem());
//        pr.btnRegistrarCAlumno.setEnabled(false);
        pr.pnPregunta.setVisible(false);
        pr.pnfinishCuestionario.setVisible(true);
        pr.tCuestionario.setText("");

    }

    private void guardarCuestionarioAlumno() {
        CCuestionarioAlumno cca = new CCuestionarioAlumno();
        cca.setIdAlumno(idUserLog);
        cca.setIdCuestionario(idCuest);
        int idca = ccuestionarioalumnodao.createCuestionarioAlumno(cca, opc);
        if (idca > 0) {
            boolean rpta = respuestasAlumnodao.Create(objRespuestasAlumno, idca, opc);
            if (rpta) {
                if (pr.btnNextQuestion.getText().equals("Finalizar")) {
                    limpiarformRespuestasAlumno();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar el cuestionario");
            }
        }
    }

    private void clearComponent() {
        pr.txtPreguntas.setText("");
        pr.pnRespuestas.removeAll();
        pr.pnRespuestas.updateUI();
        pr.tCuestionario.setText("");
    }

}
