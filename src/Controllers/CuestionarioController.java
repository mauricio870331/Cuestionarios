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
import Model.Conexion;
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
import Model.Resultados;
import Model.ResultadosDAO;
import Model.UsersDAO;
import Utils.ExportExcel;
import Utils.ImagensTabla;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Mauricio Herrera
 */
public final class CuestionarioController extends WindowAdapter implements ActionListener, ItemListener, KeyListener, MouseListener {

    int countAction = 0;
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
    ResultadosDAO resultdao = new ResultadosDAO();
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
    ArrayList<Boolean> countfalse = new ArrayList<>();
    ArrayList<String> resultGeneral = new ArrayList<>();
    AsignCuestionaryToGroup ac = new AsignCuestionaryToGroup(null, true);
    ReportResultados rr = new ReportResultados(null, false);
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
    int cantCuestioRepetir;
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
    private final JFileChooser FileChooser = new JFileChooser();
    File archivo;
    String foto = "";
    ImageIcon ii = null;
    ImageIcon iin = null;
    String Ptemp = "";

    public CuestionarioController(Principal pr, int idGrupo, int idUserLog, int rol) {
        this.pr = pr;
        this.idGrupo = idGrupo;
        this.idUserLog = idUserLog;
        cargarAll();

    }

    private void cargarAll() {
        this.pr.cboAsignatura.addActionListener(this);
        this.pr.cboPreguntas.addActionListener(this);
        this.pr.btnNextQuestion.addActionListener(this);
        this.pr.btnPreviousQuestion.addActionListener(this);
        this.pr.btnRegistrarCuestionary.addActionListener(this);
        this.pr.btnRegistrarCuestionary.setText("Continuar");
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
        this.rr.btnExportReport.addActionListener(this);
        this.pr.tCuestionario.addActionListener(this);
        this.ac.addWindowListener(this);
        this.pr.btnIniciarPrueba.addActionListener(this);//        this.pr.btnRegistrarCAlumno.addActionListener(this);
        this.pr.btnPreviousQuestion.setVisible(false);
        this.pr.reporteResultados.addActionListener(this);
        this.rr.cboReportGrupo.addActionListener(this);
        this.rr.cboReportCuestionario.addActionListener(this);
        this.pr.btnAddImagen.addActionListener(this);
        this.pr.tbPreguntasQ.addKeyListener(this);
        this.pr.tbPreguntasQ.addMouseListener(this);
        this.pr.editCustionario.addActionListener(this);
        this.pr.cboCuestionaryEdit.addActionListener(this);
        this.pr.btnChangeName.addActionListener(this);
        this.pr.cboListPreguntasToEdit.addActionListener(this);
        if (rol == 2) {
            activarCuestionarios();
            this.pr.tGrado.setText(grupdao.getListGrupoToString(idGrupo));
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
        if (rol == 1) {
            enabledAnswer(false);
            this.ac.dcFechaCuestionary.setDate(date);
            this.pr.txtDescripPregunta.setEnabled(false);
            this.pr.btnAddPregunta.setEnabled(false);
            this.pr.btnAddImagen.setEnabled(false);
//        this.pr.jButton5.setEnabled(false);
        }
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

    public void activarCuestionarios() {
        cuestionariodao.activeCuestionarios();
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
            pr.cboPreguntas.setEnabled(true);
        } else {
            pr.cboPreguntas.removeAllItems();
            pr.cboPreguntas.addItem("-- Seleccione --");
            JOptionPane.showMessageDialog(null, "¡No hay mas preguntas, ya puede guardar el cuestionario", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            pr.btnRegistrarCuestionary.requestFocus();
        }

    }

    public void cargarCuestionaryToedit() {
        pr.cboCuestionaryEdit.removeAllItems();
        pr.cboCuestionaryEdit.addItem("-- Seleccione --");
        Iterator<Cuestionario> cuest = cuestionariodao.getCuestionarioByGrupoAndProfesor(idUserLog).iterator();
        while (cuest.hasNext()) {
            Cuestionario c = cuest.next();
            pr.cboCuestionaryEdit.addItem(c.getDescripcion());
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
            }
        } else {
            pregunt = 0;
//            pr.btnNextQuestion.setEnabled(false);
        }

    }

    public void cargarPreguntasInTable(JTable tbPreguntas) throws NoSuchFieldException, IOException {
        tbPreguntas.setDefaultRenderer(Object.class, new ImagensTabla());
        String Titulos[] = {"", "Orden", "Descripcion", "Enunciado"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) { //para evitar que las celdas sean editables
                return column == 2 || column == 3;
            }
        };
        Object[] columna = new Object[4];
        Iterator<PreguntasCuestionario> preguntas = ListPreguntas.iterator();
        while (preguntas.hasNext()) {
            PreguntasCuestionario pc = preguntas.next();
            columna[0] = pc.getId();
            columna[1] = pc.getIdPregunta() + 1;
            columna[2] = pc.getPregunta();
            if (!pc.getLargo().equals("")) {
                ImageIcon icon = new ImageIcon(pc.getLargo());
                Image conver = icon.getImage();
                Image tam = conver.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                iin = new ImageIcon(tam);
                columna[3] = new JLabel(iin);
            } else {
                columna[3] = null;
            }
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
        tbPreguntas.getColumnModel().getColumn(2).setPreferredWidth(70);
        tbPreguntas.getColumnModel().getColumn(3).setPreferredWidth(25);
        tbPreguntas.setModel(modelo);
        tbPreguntas.setRowHeight(30);
    }

    public void cargarReporteGeneral(ArrayList resultGeneral) {
        String Titulos[] = {"Documento", "Nombres", "Apellidos", "Cuestionario", "Objetivo", "Grupo", "Nota", "Tiempo", "Presentado", "Calificación"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[10];

        Iterator<String> resultG = resultGeneral.iterator();
        while (resultG.hasNext()) {
            String elem = resultG.next();
            String[] parts = elem.split(",");
            columna[0] = parts[0];
            columna[1] = parts[1];
            columna[2] = parts[2];
            columna[3] = parts[3];
            columna[4] = parts[4];
            columna[5] = parts[5];
            columna[6] = parts[6];
            columna[7] = parts[7];
            columna[8] = parts[8];
            columna[9] = parts[9];
            modelo.addRow(columna);
        }
        rr.tbReportGneral.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        rr.tbReportGneral.setRowSorter(ordenar);
//        rr.tbReportGneral.getColumnModel().getColumn(1).setMaxWidth(55);
//        rr.tbReportGneral.getColumnModel().getColumn(1).setMinWidth(55);
//        rr.tbReportGneral.getColumnModel().getColumn(1).setPreferredWidth(55);
        rr.tbReportGneral.setModel(modelo);
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

    public void cargarRespuestasInTableToEdit(JTable tbRespuestas, ArrayList<RespuestasCuestionario> respo) {
        String Titulos[] = {"id", "Respuesta", "Estado"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<RespuestasCuestionario> respuestas = respo.iterator();
        while (respuestas.hasNext()) {
            RespuestasCuestionario rc = respuestas.next();
            columna[0] = rc.getIdRespuesta();
            columna[1] = rc.getRespuesta();
            columna[2] = (rc.isEstado()) ? "Verdadero" : "Falso";
            modelo.addRow(columna);
        }
        tbRespuestas.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbRespuestas.setRowSorter(ordenar);
        tbRespuestas.getColumnModel().getColumn(0).setMaxWidth(0);
        tbRespuestas.getColumnModel().getColumn(0).setMinWidth(0);
        tbRespuestas.getColumnModel().getColumn(0).setPreferredWidth(0);
        tbRespuestas.getColumnModel().getColumn(2).setMaxWidth(80);
        tbRespuestas.getColumnModel().getColumn(2).setMinWidth(80);
        tbRespuestas.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbRespuestas.setModel(modelo);
    }

    public void cargarCuestionarioByGrupo(int id_grupo) {
        cantCuestionario = cuestionariodao.getCuestionarioByGrupo(id_grupo).size();
//        cantCuestioRepetir = ccuestionarioalumnodao.getCantCuestionariosActive(idUserLog);

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
                    if (!ccuestionarioalumnodao.getCuestionariosActive(idCuest, idUserLog)) {
                        JOptionPane.showMessageDialog(null, "¡Ya presentaste este cuestionario, no puedes repetirlo, la sesión se cerrará..!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                    llenarRespuestasAlumno();
                    pr.tCuestionario.removeAllItems();
                    pr.tCuestionario.addItem(elementoC.getDescripcion());
                    pr.tCuestionario.setSelectedItem(elementoC.getDescripcion());
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
                    tempEstados.set(pregunt, rb[i].getName());
                    // Para probar las respuestas
                    Iterator<RespuestasAlumno> nombreIterator = objRespuestasAlumno.iterator();
                    while (nombreIterator.hasNext()) {
                        RespuestasAlumno p = nombreIterator.next();
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
            pr.pnEditCuestionary.setVisible(false);
            pr.pnCreateCuestionary.setVisible(true);
        }

        if (e.getSource() == pr.editCustionario) {
            cargarCuestionaryToedit();
            pr.pnCreateAdmin.setVisible(false);
            pr.pnCreateCuestionary.setVisible(false);
            pr.pnEditCuestionary.setVisible(true);
        }

        if (e.getSource() == pr.createUsers) {
            pr.pnCreateAdmin.setVisible(true);
            pr.pnCreateCuestionary.setVisible(false);
        }

        if (e.getSource() == pr.btnAddImagen) {
            countAction++;
            if (countAction == 1) {
                addFilterImg();
            }
            FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (FileChooser.showDialog(null, "Seleccionar Archivo") == JFileChooser.APPROVE_OPTION) {
                archivo = FileChooser.getSelectedFile();
                if (archivo.length() > 1000000) {//archivo.length() tamaño en bytes
                    JOptionPane.showMessageDialog(null, "El tamaño máximo para la imagen es de 1 Mega,\nSeleccione otra.");
                    return;
                }
                if (archivo.getName().endsWith("png") || archivo.getName().endsWith("PNG") || archivo.getName().endsWith("jpg")) {
                    foto = String.valueOf(archivo);
                    String NombreArchivo = FileChooser.getName(archivo);
                    pr.lblEnunciado.setText(NombreArchivo);
                    JOptionPane.showMessageDialog(null, "Archivo Seleccionado: " + String.valueOf(NombreArchivo));
                } else {
                    JOptionPane.showMessageDialog(null, "Elija un formato valido");
                }
            }
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
            if ((Integer.parseInt(añoSelc) < Integer.parseInt(añoActual)) || (Integer.parseInt(mesSelect) < Integer.parseInt(mesActual))) {
                JOptionPane.showMessageDialog(null, "La fecha seleccionada no debe ser menor a la actual");
                ac.dcFechaCuestionary.requestFocus();
                ac.dcFechaCuestionary.setDate(date);
                return;
            }
            String rpta = cgruposDao.addGroupToCuestionario(ListCuestioariosGroups, opc, fecha);
            if (rpta != null) {
                JOptionPane.showMessageDialog(null, rpta);
                limpiarformToasingGroup();
                ac.dispose();
                opc = "C";
            } else if (opc.equals("C")) {
                JOptionPane.showMessageDialog(null, "No se pudo asignar el Cuestionario");
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
            if (pr.btnRegistrarCuestionary.getText().equals("Continuar")) {
                int response = JOptionPane.showConfirmDialog(null, "<html>Está seguro de agregar solo " + pr.txtCantPreguntas.getText() + " preguntas al Cuestionario?</html>", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    pr.txtCantPreguntas.setEnabled(false);
                    this.pr.txtDescripPregunta.setEnabled(true);
                    this.pr.btnAddPregunta.setEnabled(true);
                    this.pr.btnAddImagen.setEnabled(true);
                    pr.btnRegistrarCuestionary.setText("Guardar");
                }
            } else {
                if (ListPreguntas.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Aún no ha ingresado preguntas para el cuestionario");
                    return;
                }
                if (ListRespuestas.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Aún no ha ingresado respuestas para las preguntas del cuestionario");
                    return;
                }

                int totalRespuestas = (ListPreguntas.size() * 4);

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
                    try {
                        limpiarForm();
                    } catch (NoSuchFieldException | IOException ex) {
                        System.out.println("error img " + ex);
                    }
                } else if (opc.equals("C")) {
                    JOptionPane.showMessageDialog(null, "No se pudo crear el Cuestionario");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo actualizar el Cuestionario");
                }
            }
        }

        if (e.getSource() == pr.btnNextQuestion) {
            //TotalPreguntas es el 100%
            pResponse++;
            pr.progress.setString("Respuesta " + pResponse + " de " + TotalPreguntas);
            pr.progress.setValue(pResponse);
            pr.btnNextQuestion.setEnabled(false);
            if (pr.btnNextQuestion.getText().equals("Finalizar")) {
                t.stop();
                h = 0;
                m = 0;
                s = 0;
                cs = 0;
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
            } else if (cuestionariodao.getCuestionario(asignatura, idGrupo).size() > 1) {
                pr.txtObjetivoCuestionario.setText("");
                Iterator<Cuestionario> cuestionarios = cuestionariodao.getCuestionario(asignatura, idGrupo).iterator();
                pr.tCuestionario.removeAllItems();
                pr.tCuestionario.addItem("-- Seleccione --");
                while (cuestionarios.hasNext()) {
                    Cuestionario cu = cuestionarios.next();
                    if (ccuestionarioalumnodao.getCuestionariosActive(cu.getIdCuestionario(), idUserLog)) {
                        pr.tCuestionario.addItem(cu.getDescripcion());
                    }
                }
                pr.tCuestionario.setEnabled(true);
            } else {
                Iterator<Cuestionario> nombreIterator = cuestionariodao.getCuestionario(asignatura, idGrupo).iterator();
                if (nombreIterator.hasNext()) {
                    Cuestionario c = nombreIterator.next();
                    idCuest = c.getIdCuestionario();
                    if (!ccuestionarioalumnodao.getCuestionariosActive(idCuest, idUserLog)) {
                        JOptionPane.showMessageDialog(null, "¡Ya presentaste este cuestionario, no puedes repetirlo..!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        pr.cboAsignatura.removeItem((String) pr.cboAsignatura.getSelectedItem());
                        pr.cboAsignatura.setSelectedItem("-- Seleccione --");
                        if (pr.cboAsignatura.getItemCount() == 1) {
                            JOptionPane.showMessageDialog(null, "¡El sistema ha detectado que no tienes cuestionarios habilitados.\nLa sesión se cerrará..!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }

                    } else {
                        pr.btnIniciarPrueba.setEnabled(true);
                        if (cantCuestionario > 1) {
                            llenarRespuestasAlumno();
                        }
                        pr.tCuestionario.removeAllItems();
                        pr.tCuestionario.addItem(c.getDescripcion());
                        pr.tCuestionario.setEnabled(false);
                        //pr.tCuestionario.setSelectedItem(c.getDescripcion());
                        pr.txtObjetivoCuestionario.setText(c.getObjetivo());
                        duracionCuestionario = c.getDuracion();
                        if (cantCuestionario > 1) {
                            if (TotalPreguntas > 0) {
                                cargarPreguntasCuestionario(idCuest);
                                showPreguntasCuestionario(0);
                            } else {
                                clearComponent();
                            }
                        }
                    }

                } else {
                    pr.tCuestionario.setSelectedItem("-- Seleccione --");
                    pr.txtObjetivoCuestionario.setText("");
                }
            }
        }

        if (e.getSource() == pr.tCuestionario) {
            if (pr.tCuestionario.getItemCount() > 1) {
                String descuestionario = (String) pr.tCuestionario.getSelectedItem();
                if (!descuestionario.equals("-- Seleccione --")) {
                    pr.btnIniciarPrueba.setEnabled(true);
                } else {
                    clearComponent();
                    pr.btnIniciarPrueba.setEnabled(false);
                }
                Iterator<Cuestionario> itrC = cuestionariodao.getCuestionariosByNameList(descuestionario).iterator();
                if (itrC.hasNext()) {
                    Cuestionario elementoC = itrC.next();
                    idCuest = elementoC.getIdCuestionario();
                    if (!ccuestionarioalumnodao.getCuestionariosActive(idCuest, idUserLog)) {
                        JOptionPane.showMessageDialog(null, "¡Ya presentaste este cuestionario, no puedes repetirlo..!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        pr.tCuestionario.removeItem((String) pr.tCuestionario.getSelectedItem());
                        pr.tCuestionario.setSelectedItem("-- Seleccione --");
                        if (pr.tCuestionario.getItemCount() == 1) {
                            JOptionPane.showMessageDialog(null, "¡El sistema ha detectado que no tienes cuestionarios habilitados.\nLa sesión se cerrará..!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }
                    } else {
                        duracionCuestionario = elementoC.getDuracion();
                        pr.txtObjetivoCuestionario.setText(elementoC.getObjetivo());
                        llenarRespuestasAlumno();
                        cargarPreguntasCuestionario(idCuest);
                        showPreguntasCuestionario(0);
                    }

                }
            }

        }

        if (e.getSource() == pr.btnAddPregunta) {
            String pregunta = pr.txtDescripPregunta.getText();
            int cantPre = Integer.parseInt(pr.txtCantPreguntas.getText());
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
            pc.setLargo(foto);
            ListPreguntas.add(pc);
            ListPreguntasTemp.add(pc);
            try {
                cargarPreguntasInTable(pr.tbPreguntasQ);
            } catch (NoSuchFieldException | IOException ex) {
                Logger.getLogger(CuestionarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (ListPreguntasTemp.size() == cantPre) {
                cargarPreguntasToRespuestas(true);
            }
            pr.txtDescripPregunta.setText("");
            deleteSpinner++;
            tempCantPreguntas++;
            if (tempCantPreguntas == Integer.parseInt(pr.txtCantPreguntas.getText())) {
                pr.txtDescripPregunta.setEnabled(false);
                pr.btnAddPregunta.setEnabled(false);
                pr.btnAddImagen.setEnabled(false);
                enabledAnswer(true);
            }
            foto = "";
            archivo = null;
            pr.lblEnunciado.setText("Nombre imagen");
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

        if (e.getSource() == pr.btnAddRespuesta) {
            String pregunta = (String) pr.cboPreguntas.getSelectedItem();
            String literal = (String) pr.cboLiteral.getSelectedItem();
            String resp = pr.txtRespuestaQ.getText();
            if (pregunta.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "Debe Seleccionar una pregunta..");
                pr.cboPreguntas.requestFocus();
                pr.cboPreguntas.setEnabled(true);
                return;
            }
            if (pr.cboPreguntas.isEnabled()) {
                pr.cboPreguntas.setEnabled(false);
            }
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
            } else {
                countfalse.add(estadoRespuesta);
            }
            if (countfalse.size() == 3) {
                pr.rdoFalse.setEnabled(false);
                pr.rdoTrue.setSelected(true);
            }
            rc.setRespuesta(literal + ": " + resp);
            ListRespuestas.add(rc);
            ListRespuestasTemp.add(rc);
            cargarRespuestasInTable(pr.tbRespuestasQ);
            pr.txtRespuestaQ.setText("");
            pr.cboLiteral.removeItem(literal);
            if (pr.cboLiteral.getItemCount() == 1) {
                System.out.println("aqui");
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
                cargarCboLiteral();
                pr.cboPreguntas.setEnabled(true);
                pr.rdoFalse.setEnabled(true);
                countfalse.clear();
            }
            if (pr.cboLiteral.getItemCount() > 2) {
                System.out.println("remover " + literal);
                System.out.println("lista " + ListPreguntasTemp.size());
            }
            pr.cboLiteral.setSelectedItem("-- Seleccione --");

        }

        if (e.getSource() == pr.btnCancelarCuestionary) {
            try {
                limpiarForm();
            } catch (NoSuchFieldException | IOException ex) {
                System.out.println("error img " + ex);
            }
        }

        if (e.getSource() == pr.reporteResultados) {
            cargarGrupToRepotrGeneral(rr);
            rr.setLocationRelativeTo(null);
            rr.setVisible(true);
        }

        if (e.getSource() == pr.cboCuestionaryEdit) {
            String cuestion = (String) pr.cboCuestionaryEdit.getSelectedItem();
            if (cuestion.equals("-- Seleccione --")) {
                pr.txtObjetivoEdit.setText("");
                pr.cboAsignatureEdit.setSelectedItem("-- Seleccione --");
                pr.cboListPreguntasToEdit.removeAllItems();
                pr.cboListPreguntasToEdit.addItem("-- Seleccione --");
                pr.txtDuracionEdit.setText("");
                pr.txtCantPreguntasEdit.setText("");
                return;
            }
            Iterator<Cuestionario> cuest = cuestionariodao.getCuestionariosByNameList(cuestion).iterator();
            if (cuest.hasNext()) {
                Cuestionario c = cuest.next();
                pr.txtObjetivoEdit.setText(c.getObjetivo());
                pr.cboAsignatureEdit.setSelectedItem(asdao.getAsignaturaById(c.getIdAsignatura()));
                pr.txtDuracionEdit.setText(Integer.toString(c.getDuracion()));
                pr.txtCantPreguntasEdit.setText(Integer.toString(cuestionariodao.getPreguntasCuestionario(c.getIdCuestionario())));
                ArrayList<PreguntasCuestionario> temp = preguntasdao.getPreguntasCuestionario(c.getIdCuestionario());
                Iterator<PreguntasCuestionario> nombreIterator = temp.iterator();
                pr.cboListPreguntasToEdit.removeAllItems();
                pr.cboListPreguntasToEdit.addItem("-- Seleccione --");
                while (nombreIterator.hasNext()) {
                    PreguntasCuestionario pc = nombreIterator.next();
                    pr.cboListPreguntasToEdit.addItem(pc.getPregunta());
                }
            }

        }

        if (e.getSource() == pr.cboListPreguntasToEdit) {
            String pregunta = (String) pr.cboListPreguntasToEdit.getSelectedItem();
            ArrayList<RespuestasCuestionario> rcuestionarioEdit = respuestasdao.getRespuestasCuestionarioEdit(preguntasdao.getIdPreguntaByName(pregunta));
            cargarRespuestasInTableToEdit(pr.tbRespuestasEdit, rcuestionarioEdit);
        }

        if (e.getSource() == pr.btnChangeName) {
            String cnameTemp = (String) pr.cboCuestionaryEdit.getSelectedItem();
            if (!cnameTemp.equals("-- Seleccione --")) {
                String nombre = JOptionPane.showInputDialog(null,
                        "Digite el nuevo nombre",
                        "Cambiar nombre cuestionario",
                        JOptionPane.INFORMATION_MESSAGE);
                if (nombre != null) {
                    int response = JOptionPane.showConfirmDialog(null, "<html>Está seguro de cambiar el nombre del cuestionario ?</html>", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        if (cuestionariodao.updateName(nombre, cnameTemp)) {
                            pr.cboCuestionaryEdit.removeItem(cnameTemp);
                            pr.cboCuestionaryEdit.addItem(nombre);
                            pr.cboCuestionaryEdit.setSelectedItem(nombre);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo cambiar el nombre", "Aviso..!", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un cuestionario..!");
            }

        }

        if (e.getSource() == rr.cboReportGrupo) {
            String grupo = (String) rr.cboReportGrupo.getSelectedItem();
            if (grupo.equals("-- Seleccione --")) {
                rr.cboReportCuestionario.removeAllItems();
                rr.cboReportCuestionario.addItem("-- Seleccione --");
                rr.cboReportGrupo.requestFocus();
                return;
            }
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

        }

        if (e.getSource() == rr.cboReportCuestionario) {
            String grupo = (String) rr.cboReportGrupo.getSelectedItem();
            String cues = (String) rr.cboReportCuestionario.getSelectedItem();
            resultGeneral = cuestionariodao.reporteGeneralResultados(grupo, cues);
            cargarReporteGeneral(resultGeneral);
        }

        if (e.getSource() == rr.btnExportReport) {
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
            countAction++;
            if (countAction == 1) {
                addFilter();
            }
            if (FileChooser.showDialog(null, "Exportar") == JFileChooser.APPROVE_OPTION) {
                archivo = FileChooser.getSelectedFile();
                ExportExcel ee = new ExportExcel();
                if (archivo.getName().endsWith("xls") || archivo.getName().endsWith("xlsx")) {
                    String response = ee.Export(archivo, rr.tbReportGneral);
                    JOptionPane.showMessageDialog(null, response);
                    rr.cboReportGrupo.setSelectedItem("-- Seleccione --");
                } else {
                    JOptionPane.showMessageDialog(null, "Asegusere de Ingresar un nombre al reporte y una extension valida...!");
                }
            } else {
                return;
            }
        }

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
//        if (e.getSource() == rr.cboReportCuestionario) {
//            System.out.println("hola");
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

    private void limpiarForm() throws NoSuchFieldException, IOException {
        pr.btnRegistrarCuestionary.setText("Continuar");
        tempCantPreguntas = 0;
        contPregunta = 0;
        deleteSpinner = 0;
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
        countfalse.clear();
        cargarPreguntasInTable(pr.tbPreguntasQ);
        cargarRespuestasInTable(pr.tbRespuestasQ);
        pr.cboPreguntas.setSelectedItem("-- Seleccione --");
        foto = "";
        //pr.cboLiteral.addItem("-- Seleccione --");
        pr.txtRespuestaQ.setText("");
        enabledAnswer(false);
        pr.rdoTrue.setSelected(true);
        pr.lblEnunciado.setText("Nombre imagen");
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
        if (e.getSource() == pr) {
            Conexion.closeConexion();
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
        pr.cboAsignatura.setSelectedItem("-- Seleccione --");
        if (pr.tCuestionario.getItemCount() > 2) {
            pr.tCuestionario.removeItem((String) pr.cboAsignatura.getSelectedItem());
            pr.tCuestionario.setSelectedItem("-- Seleccione --");
        }
        pr.pnPregunta.setVisible(false);
        pr.pnfinishCuestionario.setVisible(true);
        pr.tCuestionario.setSelectedItem("-- Seleccione --");
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
                    double nota = cuestionariodao.getCalificacionAlumno(idCuest, idUserLog);
                    boolean aprobo = false;
                    String notaString = "No Aprobó";
                    if (nota >= 3.0) {
                        aprobo = true;
                        notaString = "Aprobó";
                    }
                    Resultados r = new Resultados();
                    r.setId_cuestionario(idCuest);
                    r.setId_grupo(idGrupo);
                    r.setId_user(idUserLog);
                    r.setNota(nota);
                    r.setTiempo(pr.lblTiempo.getText());
                    r.setAprobo(aprobo);
                    r.setFecha_presentacion(df.format(date));
                    if (resultdao.create(r, "C")) {
                        System.out.println("resultado creado");
                    } else {
                        System.out.println("error al crear el resultado");
                    }
                    limpiarformRespuestasAlumno();
                    cuestionariodao.generateReporte(idCuest, idUserLog, idca, TotalPreguntas, nota, notaString);
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
        pr.tCuestionario.removeAllItems();
        pr.tCuestionario.addItem("-- Seleccione --");
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
        // para cambiar las preguntas a l crear el cuestionario
        if (e.getSource() == pr.tbPreguntasQ) {
            int columna = pr.tbPreguntasQ.getSelectedColumn();
            int fila = pr.tbPreguntasQ.getSelectedRow();
            char tecla = e.getKeyChar();
            if (tecla == KeyEvent.VK_ENTER) {
                if (ListRespuestas.size() > 0) {
                    int response = JOptionPane.showConfirmDialog(null, "El sistema ha detectado que hay respuestas asignadas a preguntas.\n Está seguro de editar la pregunta..? Al confirmar deberá volver a Ingresar las respuestas a las preguntas.", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        ListRespuestas.clear();
                        ListRespuestasTemp.clear();
                    } else {
                        pr.tbPreguntasQ.setValueAt(Ptemp, fila, columna);
                        Ptemp = "";
                        return;
                    }
                }
                if (columna == 2) {
                    Iterator<PreguntasCuestionario> lp = ListPreguntas.iterator();
                    while (lp.hasNext()) {
                        PreguntasCuestionario update = lp.next();
                        if (update.getId() == Integer.parseInt(pr.tbPreguntasQ.getValueAt(fila, 0).toString())) {
                            update.setPregunta(pr.tbPreguntasQ.getValueAt(fila, columna).toString());
                        }
                    }
                    Iterator<PreguntasCuestionario> lpt = ListPreguntasTemp.iterator();
                    while (lpt.hasNext()) {
                        PreguntasCuestionario update = lpt.next();
                        if (update.getId() == Integer.parseInt(pr.tbPreguntasQ.getValueAt(fila, 0).toString())) {
                            update.setPregunta(pr.tbPreguntasQ.getValueAt(fila, columna).toString());
                        }
                    }
                    try {
                        cargarPreguntasInTable(pr.tbPreguntasQ);
                        cargarPreguntasToRespuestas(true);
                        cargarRespuestasInTable(pr.tbRespuestasQ);
                        cargarCboLiteral();
                    } catch (NoSuchFieldException | IOException ex) {
                        Logger.getLogger(CuestionarioController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int fila = pr.tbPreguntasQ.rowAtPoint(e.getPoint());
        int columna = pr.tbPreguntasQ.columnAtPoint(e.getPoint());
        if (columna == 2) {
            Ptemp = pr.tbPreguntasQ.getValueAt(fila, columna).toString();
        }
        if (columna == 3) {
            if (ListRespuestas.size() > 0) {
                int response = JOptionPane.showConfirmDialog(null, "El sistema ha detectado que hay respuestas asignadas a preguntas.\n Está seguro de editar el enunciado..? Al confirmar deberá volver a Ingresar las respuestas a las preguntas..", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    ListRespuestas.clear();
                    ListRespuestasTemp.clear();
                    countAction++;
                    if (countAction == 1) {
                        addFilterImg();
                    }
                    FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    if (FileChooser.showDialog(null, "Seleccionar Archivo") == JFileChooser.APPROVE_OPTION) {
                        archivo = FileChooser.getSelectedFile();
                        if (archivo.length() > 1000000) {//archivo.length() tamaño en bytes
                            JOptionPane.showMessageDialog(null, "El tamaño máximo para la imagen es de 1 Mega,\nSeleccione otra.");
                            return;
                        }
                        if (archivo.getName().endsWith("png") || archivo.getName().endsWith("PNG") || archivo.getName().endsWith("jpg")) {
                            foto = String.valueOf(archivo);
                            String NombreArchivo = FileChooser.getName(archivo);
                            JOptionPane.showMessageDialog(null, "Archivo Seleccionado: " + String.valueOf(NombreArchivo));

                            Iterator<PreguntasCuestionario> lp = ListPreguntas.iterator();
                            while (lp.hasNext()) {
                                PreguntasCuestionario update = lp.next();
                                if (update.getId() == Integer.parseInt(pr.tbPreguntasQ.getValueAt(fila, 0).toString())) {
                                    update.setLargo(foto);
                                }
                            }
                            try {
                                cargarPreguntasInTable(pr.tbPreguntasQ);
                                cargarPreguntasToRespuestas(true);
                                cargarRespuestasInTable(pr.tbRespuestasQ);
                                cargarCboLiteral();
                                foto = "";
                                archivo = null;
                            } catch (NoSuchFieldException | IOException ex) {
                                System.out.println("error " + ex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Elija un formato valido");
                        }
                    }
                }
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void addFilter() {
        FileChooser.setFileFilter(new FileNameExtensionFilter("Excel (*.xls)", "xls"));
        FileChooser.setFileFilter(new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx"));
    }

    private void addFilterImg() {
        FileChooser.setFileFilter(new FileNameExtensionFilter("Imagen (*.PNG)", "png"));
        FileChooser.setFileFilter(new FileNameExtensionFilter("Imagen (*.JPG)", "jpg"));
    }

    private void cargarCboLiteral() {
        pr.cboLiteral.removeAllItems();
        pr.cboLiteral.addItem("-- Seleccione --");
        pr.cboLiteral.addItem("A");
        pr.cboLiteral.addItem("B");
        pr.cboLiteral.addItem("C");
        pr.cboLiteral.addItem("D");
    }

}
