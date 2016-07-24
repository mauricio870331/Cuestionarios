/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import App.AsignCuestionaryToGroup;
import App.Principal;
import App.ReportResultados;
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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public final class CuestionarioController extends WindowAdapter implements ActionListener, ItemListener, KeyListener, FocusListener {

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
    AsignCuestionaryToGroup ac = new AsignCuestionaryToGroup(null, true);
    ReportResultados rr = new ReportResultados(null, true);
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
    boolean estado = false;
    int idPtemp;
    int idtempfk;
    boolean estadoRespuesta;
    int idNextPregunta;
    int contPregunta = 0;
    int deleteSpinner = 0;
    Timer t;
    int h, m, s, cs;
    int tempCantPreguntas = 0;
    double duracionCuestionario;
    double r;
    int p_ent;
    double p_dec;
    float constante = (float) 0.0167;// Equivalente a : 1 Minuto = 0.0167 Hora
    float conversion;
    float result;
    int rconvert;
    boolean iniciar = false;
    int pResponse = 0;
    SimpleDateFormat dfY = new SimpleDateFormat("yyyy");
    SimpleDateFormat dfM = new SimpleDateFormat("MM");
    SimpleDateFormat dfD = new SimpleDateFormat("dd");

    public CuestionarioController(Principal pr, int idGrupo, int idUserLog, int rol) {
        this.pr = pr;
        this.pr.jButton5.setVisible(false);
        this.pr.jButton3.setVisible(false);
        this.idGrupo = idGrupo;
        this.idUserLog = idUserLog;
        this.pr.cboAsignatura.addActionListener(this);
        this.pr.cboPreguntas.addActionListener(this);
//        this.pr.btnCancelarC.addActionListener(this);
        this.pr.btnNextQuestion.addActionListener(this);
        this.pr.btnPreviousQuestion.addActionListener(this);
        this.pr.btnRegistrarCuestionary.addActionListener(this);
        this.pr.btnAddRespuesta.addActionListener(this);
        this.pr.btnAddPregunta.addActionListener(this);
        this.pr.rdoTrue.addItemListener(this);
        this.pr.rdoFalse.addItemListener(this);
        this.pr.btnCancelarCuestionary.addActionListener(this);
        this.pr.asignCuestionaryToGroup.addActionListener(this);
        this.pr.createUsers.addActionListener(this);
        this.pr.cuestionario.addActionListener(this);
        this.ac.btnSaveCtoGroup.addActionListener(this);
        this.pr.txtDuracion.addKeyListener(this);
        this.pr.txtCantPreguntas.addKeyListener(this);
        this.pr.txtCantPreguntas.addFocusListener(this);
        this.rr.btnGenerateReport.addActionListener(this);
//        this.ac.chkEstado.addItemListener(this);
        this.ac.addWindowListener(this);
        this.pr.btnIniciarPrueba.addActionListener(this);//        this.pr.btnRegistrarCAlumno.addActionListener(this);
        this.pr.btnPreviousQuestion.setVisible(false);
        this.pr.reporteResultados.addActionListener(this);
        this.rr.cboReportGrupo.addActionListener(this);
        System.out.println("user " + idUserLog + " grupo " + idGrupo);
        if (rol == 2) {
            this.pr.tGrado.setText(grupdao.getListGrupoToString(idGrupo).get(0).getGrupo());
            cargarCuestionarioByGrupo(idGrupo);
            if (cantCuestionario == 0) {
                JOptionPane.showMessageDialog(null, "¡No hay  cuestionarios activos, la sesión se cerrará..!\nIndique a su profesor para que verifique en el sistema.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            if (cantCuestionario == 1) {
                showPreguntasCuestionario(0);
                this.pr.btnIniciarPrueba.setEnabled(true);
            } else {
                this.pr.btnIniciarPrueba.setEnabled(false);
            }
            if (pregunt == 0) {
                this.pr.btnPreviousQuestion.setEnabled(false);
            }
            this.pr.btnNextQuestion.setEnabled(false);
            r = duracionCuestionario / 60; // se convierten los minutos a horas                 
            p_ent = (int) r; //se obtine la parte entera de la conversion
            p_dec = r - p_ent;   //se obtine la parte decimal de la conversion            
            conversion = (float) p_dec;  //se convierte la part decimal a float         
            result = conversion / constante; // se cnvierte la hora a minutos
            rconvert = (int) Math.ceil(result);// se redondea el valor hacia arriba
            t = new Timer(9, acciones);
        }
        enabledAnswer(false);
        this.ac.dcFechaCuestionary.setDate(date);
        this.pr.txtDescripPregunta.setEnabled(false);
        this.pr.btnAddPregunta.setEnabled(false);
        this.pr.jButton5.setEnabled(false);

    }

    public ActionListener acciones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ++cs;
            if (cs == 95) {
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
//duracionCuestionario

    private void actualizarlblTiempo() {
        String tiempo = (h <= 9 ? "0" : "") + h + ":" + (m <= 9 ? "0" : "") + m + ":" + (s <= 9 ? "0" : "") + s + ":" + (cs <= 9 ? "0" : "") + cs;
        pr.lblTiempo.setText(tiempo);
        if (duracionCuestionario >= 60) {
            if (h == p_ent && m == rconvert) {
                if (t.isRunning()) {
                    t.stop();
                    pr.btnNextQuestion.setText("Finalizar");
                    guardarCuestionarioAlumno(true);
                    h = 0;
                    m = 0;
                    s = 0;
                    cs = 0;
                }
            }
        } else if (m == duracionCuestionario) {
            if (t.isRunning()) {
                t.stop();
                pr.btnNextQuestion.setText("Finalizar");
                guardarCuestionarioAlumno(true);
                h = 0;
                m = 0;
                s = 0;
                cs = 0;
            }
        }
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
            JOptionPane.showMessageDialog(null, "¡No hay mas preguntas, ya puede guardar el cuestionario", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            pr.btnRegistrarCuestionary.requestFocus();
        }

    }

    public void llenarRespuestasAlumno() {
        TotalPreguntas = cuestionariodao.getPreguntasCuestionario(idCuest);
        pr.lbltotalp.setText("Total Preguntas: " + TotalPreguntas);
        pr.progress.setMaximum(TotalPreguntas);
        pr.lbltotalp.setText("Total Preguntas: " + TotalPreguntas);
        pr.progress.setStringPainted(true);
        pr.progress.setString("Respuesta " + pResponse);
        pr.progress.setValue(pResponse);
        System.out.println("totalpreg = " + TotalPreguntas);
        preguntasCList.clear();
        sortQuestions.clear();
        objRespuestasAlumno.clear();
        tempEstados.clear();
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
            pr.cboAsignatura.removeAllItems();
            pr.cboAsignatura.addItem("-- Seleccione --");
            Iterator<Asignaturas> nombreIterator = asdao.getAsignaturasByCuestionario(id_grupo).iterator();
            while (nombreIterator.hasNext()) {
                Asignaturas elemento = nombreIterator.next();
                pr.cboAsignatura.addItem(elemento.getNombreAsignatura());
            }
        } else {
            pr.cboAsignatura.removeAllItems();
            pr.cboAsignatura.addItem("-- Seleccione --");
            Iterator<Asignaturas> nombreIterator = asdao.getAsignaturasByCuestionario(id_grupo).iterator();
            if (nombreIterator.hasNext()) {
                Asignaturas elemento = nombreIterator.next();
                pr.cboAsignatura.addItem(elemento.getNombreAsignatura());
                pr.cboAsignatura.setSelectedItem(elemento.getNombreAsignatura());
                pr.cboAsignatura.setEnabled(false);
                Iterator<Cuestionario> itrC = cuestionariodao.getCuestionarioByGrupo(id_grupo).iterator();
                if (itrC.hasNext()) {
                    Cuestionario elementoC = itrC.next();
                    idCuest = elementoC.getIdCuestionario();
                    if (!ccuestionarioalumnodao.getCuestionariosActive(idCuest)) {
                        JOptionPane.showMessageDialog(null, "¡Ya presentaste este cuestionario, no puedes repetirlo, la sesión se cerrará..!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                    llenarRespuestasAlumno();
//                    System.out.println("por aqui user: "+elementoC.getIdUser());
//                    pr.txtAlumnoName.setText(udao.getUser(elementoC.getIdUser()));
                    pr.tCuestionario.setText(elementoC.getDescripcion());
                    pr.txtObjetivoCuestionario.setText(elementoC.getObjetivo());
                    duracionCuestionario = elementoC.getDuracion();
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
        if (e.getSource() == pr.btnIniciarPrueba) {
            t.start();
            pr.btnIniciarPrueba.setEnabled(false);
            iniciar = true;
            pr.pnPregunta.setVisible(true);
        }

        if (iniciar) {
            for (int i = 0; i < pr.pnRespuestas.getComponentCount(); i++) {
                if (e.getSource() == rb[i]) {
                    RespuestasAlumno ra = new RespuestasAlumno();
                    ra.setIdPregunta(id_pregunta);
                    ra.setIdRespuesta(respuestasdao.getIdRespuesta(id_pregunta, rb[i].getText().trim()));
                    objRespuestasAlumno.set(id_pregunta, ra);
                    System.out.println("tam despues de clear listener = " + tempEstados.size());
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
            String fecha = "";
            String añoActual = dfY.format(date);
            String añoSelc = "";
            String mesActual = dfM.format(date);
            String mesSelect = "";
            String diaActual = dfD.format(date);
            String diaSel = "";
            if (ListCuestioariosGroups.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No has seleccionado grupo(s) para asignar al cuestionario");
                ac.cboCuestionaries.requestFocus();
                return;
            }
            if (ac.dcFechaCuestionary.getDate() != null) {
                fecha = df.format(ac.dcFechaCuestionary.getDate());
                añoSelc = dfY.format(ac.dcFechaCuestionary.getDate());
                mesSelect = dfM.format(ac.dcFechaCuestionary.getDate());
                diaSel = dfD.format(ac.dcFechaCuestionary.getDate());
            }
            if ((Integer.parseInt(añoSelc) < Integer.parseInt(añoActual)) || (Integer.parseInt(mesSelect) < Integer.parseInt(mesActual)) || (Integer.parseInt(diaSel) < Integer.parseInt(diaActual))) {
                JOptionPane.showMessageDialog(null, "La fecha seleccionada no debe ser menor a la actual");
                ac.dcFechaCuestionary.requestFocus();
                ac.dcFechaCuestionary.setDate(date);
                return;
            }
//            if (!estado) {
//                JOptionPane.showMessageDialog(null, "Por favor habilite el cuestionario");
//                ac.cboCuestionaries.requestFocus();
//                return;
//            }            
            String rpta = cgruposDao.addGroupToCuestionario(ListCuestioariosGroups, opc, fecha, true);
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
            if (pr.txtDuracion.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar el tiempo de duración del cuestionario..");
                pr.txtDuracion.requestFocus();
                return;
            }
            if (pr.txtCantPreguntas.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe ingresar la cantidad de preguntas para el cuestionario..");
                pr.txtCantPreguntas.requestFocus();
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

            int totalRespuestas = (ListPreguntas.size() * 5);

            if (totalRespuestas > ListRespuestas.size()) {
                JOptionPane.showMessageDialog(null, "Aún faltan respuestas para agregar a las preguntas");
                pr.txtRespuestaQ.requestFocus();
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
            c.setDuracion(Integer.parseInt(pr.txtDuracion.getText()));
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
            //TotalPreguntas es el 100%
            pResponse++;
            pr.progress.setString("Respuesta " + pResponse + " de " + TotalPreguntas);
            pr.progress.setValue(pResponse);
            pr.btnNextQuestion.setEnabled(false);
            if (pr.btnNextQuestion.getText().equals("Finalizar")) {
                System.out.println("fin");
                t.stop();
                h = 0;
                m = 0;
                s = 0;
                cs = 0;
                System.out.println(pr.lblTiempo.getText());
                guardarCuestionarioAlumno(false);
            } else {
                pregunt++;
                int temp = TotalPreguntas - 1;
                showPreguntasCuestionario(pregunt);
                guardarCuestionarioAlumno(false);
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
            } else {
                Iterator<Cuestionario> nombreIterator = cuestionariodao.getCuestionario(asignatura, idGrupo).iterator();
                if (nombreIterator.hasNext()) {
                    Cuestionario c = nombreIterator.next();
//                String profesor = udao.getProfesor(c.getIdUser());
                    idCuest = c.getIdCuestionario();
                    if (!ccuestionarioalumnodao.getCuestionariosActive(idCuest)) {
                        JOptionPane.showMessageDialog(null, "¡Ya presentaste este cuestionario, no puedes repetirlo..!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        pr.cboAsignatura.removeItem((String) pr.cboAsignatura.getSelectedItem());
                        pr.cboAsignatura.setSelectedItem("-- Seleccione --");
                        if (pr.cboAsignatura.getItemCount() == 1) {
                            JOptionPane.showMessageDialog(null, "¡El sistema ha detectado que no tienes cuestionarios habilitados.\nLa sesión se cerrará..!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }
                    } else {
                        pr.btnIniciarPrueba.setEnabled(true);
                        System.out.println("pase por que estoy activo");
                        if (cantCuestionario > 1) {
                            llenarRespuestasAlumno();
                        }
                        pr.tCuestionario.setText(c.getDescripcion());
                        pr.txtObjetivoCuestionario.setText(c.getObjetivo());
                        duracionCuestionario = c.getDuracion();
                        if (cantCuestionario > 1) {
                            System.out.println("id cuestionario = " + idCuest);
                            if (TotalPreguntas > 0) {
                                cargarPreguntasCuestionario(idCuest);
                                showPreguntasCuestionario(0);
                            } else {
                                clearComponent();
                            }
                        }

                    }

                } else {
                    pr.tCuestionario.setText("");
                    pr.txtObjetivoCuestionario.setText("");
                }
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
            deleteSpinner++;
            tempCantPreguntas++;
            if (tempCantPreguntas == Integer.parseInt(pr.txtCantPreguntas.getText())) {
                pr.txtDescripPregunta.setEnabled(false);
                pr.btnAddPregunta.setEnabled(false);
                enabledAnswer(true);
            }
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
                    Iterator<PreguntasCuestionario> lpt = ListPreguntasTemp.iterator();
                    while (lpt.hasNext()) {
                        PreguntasCuestionario borrar = lpt.next();
                        if (borrar.getPregunta().equals(pregunta)) {
                            lpt.remove();
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

        if (e.getSource() == pr.reporteResultados) {
            cargarGrupToRepotrGeneral(rr);
            rr.setLocationRelativeTo(null);
            rr.setVisible(true);
        }

        if (e.getSource() == rr.cboReportGrupo) {
            String grupo = (String) rr.cboReportGrupo.getSelectedItem();
            if (!grupo.equals("-- Seleccione --")) {
                if (cuestionariodao.getCuestionariosByGrupo(grupo).size() > 0) {
                    rr.cboReportCuestionario.removeAllItems();
                    rr.cboReportCuestionario.addItem("-- Seleccione --");
                    Iterator<Cuestionario> nombreIterator = cuestionariodao.getCuestionariosByGrupo(grupo).iterator();
                    while (nombreIterator.hasNext()) {
                        Cuestionario elemento = nombreIterator.next();
                        rr.cboReportCuestionario.addItem(elemento.getDescripcion());
                    }
                    rr.cboReportCuestionario.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontraron cuestionarios");
                    rr.cboReportCuestionario.removeAllItems();
                    rr.cboReportCuestionario.addItem("-- Seleccione --");
                    rr.cboReportGrupo.requestFocus();
                }

            } else {
                rr.cboReportCuestionario.removeAllItems();
                rr.cboReportCuestionario.addItem("-- Seleccione --");
                rr.cboReportGrupo.requestFocus();
            }
        }

        if (e.getSource() == rr.btnGenerateReport) {
            String grupo = (String) rr.cboReportGrupo.getSelectedItem();
            String cues = (String) rr.cboReportCuestionario.getSelectedItem();
            if (grupo.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Seleccione un grupo");
                return;
            }
            if (cues.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Seleccione un cuestionario");
                return;
            }
            cuestionariodao.reporteGeneralResultados(grupo, cues);
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
//        if (e.getSource() == ac.chkEstado) {
//            if (ac.chkEstado.isSelected()) {
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
        tempCantPreguntas = 0;
        pr.txtDescCuestionary.setText("");
        pr.cboAsignature.setSelectedItem("-- Seleccione --");
        pr.txtDescripPregunta.setText("");
        pr.txtObjetivo.setText("");
        pr.txtDuracion.setText("");
        pr.txtCantPreguntas.setText("");
        pr.txtCantPreguntas.setEnabled(true);
        ListPreguntas.clear();
        ListPreguntasTemp.clear();
        ListRespuestas.clear();
        ListRespuestasTemp.clear();
        cargarPreguntasInTable(pr.tbPreguntasQ);
        cargarRespuestasInTable(pr.tbRespuestasQ);
        pr.cboPreguntas.setSelectedItem("-- Seleccione --");
        pr.cboLiteral.addItem("-- Seleccione --");
        pr.txtRespuestaQ.setText("");
        enabledAnswer(false);
        pr.rdoTrue.setSelected(true);
    }

    public void enabledAnswer(boolean enabled) {
        pr.cboPreguntas.setEnabled(enabled);
        pr.cboLiteral.setEnabled(enabled);
        pr.txtRespuestaQ.setEnabled(enabled);
        pr.rdoTrue.setEnabled(enabled);
        pr.rdoTrue.setSelected(enabled);
        pr.rdoFalse.setEnabled(enabled);
        pr.btnAddRespuesta.setEnabled(enabled);
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

    private void cargarGrupToRepotrGeneral(ReportResultados rr) {
        rr.cboReportGrupo.removeAllItems();
        rr.cboReportGrupo.addItem("-- Seleccione --");
        Iterator<Grupo> nombreIterator = grupdao.getListGrupos().iterator();
        while (nombreIterator.hasNext()) {
            Grupo elemento = nombreIterator.next();
            rr.cboReportGrupo.addItem(elemento.getGrupo());
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
        System.out.println("tam = " + tempEstados.size());
        tempEstados.clear();
        pr.btnNextQuestion.setText("Siguiente");
        System.out.println("tam despues de clear = " + tempEstados.size());
        sortQuestions.clear();
        ListPreguntas.clear();
        ListPreguntasTemp.clear();
        ListRespuestas.clear();
        ListRespuestasTemp.clear();
        pr.cboAsignatura.removeItem((String) pr.cboAsignatura.getSelectedItem());
        pr.cboAsignatura.setSelectedItem("-- Seleccione --");
//        pr.btnRegistrarCAlumno.setEnabled(false);
        pr.pnPregunta.setVisible(false);
        pr.pnfinishCuestionario.setVisible(true);
        pr.tCuestionario.setText("");
        iniciar = false;
        pregunt = 0;
        pResponse = 0;
        pr.progress.setString("Respuesta " + pResponse);
        pr.progress.setValue(pResponse);
    }

    private void guardarCuestionarioAlumno(boolean auto) {
        System.out.println("enviando...");
        CCuestionarioAlumno cca = new CCuestionarioAlumno();
        cca.setIdAlumno(idUserLog);
        cca.setIdCuestionario(idCuest);
        cca.setRepetir(0);
        int idca = ccuestionarioalumnodao.createCuestionarioAlumno(cca, opc);
        if (idca > 0) {
            boolean rpta = respuestasAlumnodao.Create(objRespuestasAlumno, idca, opc, auto, pr.lblTiempo.getText());
            if (rpta) {
                if (pr.btnNextQuestion.getText().equals("Finalizar")) {
                    limpiarformRespuestasAlumno();
                    cuestionariodao.generateReporte(idCuest, idUserLog);
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
        TotalPreguntas = 0;
        pr.txtObjetivoCuestionario.setText("");
        pr.lbltotalp.setText("Total Preguntas: " + TotalPreguntas);
    }

    public static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == pr.txtDuracion) {
            if (!isNumeric(pr.txtDuracion.getText())) {
                pr.txtDuracion.setText("");
            }
        }

        if (e.getSource() == pr.txtCantPreguntas) {
            if (!isNumeric(pr.txtCantPreguntas.getText())) {
                pr.txtCantPreguntas.setText("");
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == pr.txtCantPreguntas) {
            int response = JOptionPane.showConfirmDialog(null, "<html>Está seguro de agregar solo " + pr.txtCantPreguntas.getText() + " preguntas al Cuestionario?</html>", "Aviso..!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                pr.txtCantPreguntas.setEnabled(false);
                this.pr.txtDescripPregunta.setEnabled(true);
                this.pr.btnAddPregunta.setEnabled(true);
                this.pr.jButton5.setEnabled(true);
            }
        }
    }

}
