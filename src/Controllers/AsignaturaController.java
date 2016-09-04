/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.*;
import App.*;
import static Controllers.UsersController.isNumeric;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Mauricio
 */
public final class AsignaturaController extends MouseAdapter implements ActionListener, KeyListener {

    String dato = "";
    Principal pr;
    AsignaturasDAO asignaturadao = new AsignaturasDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    UsersDAO userdao = null;
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    String opc2 = "";
    int profesorUpdate = 0;
    int idArea = 0;
    int user = 0;
    AddAsignatura aa = null;
    AddAsignaturaToTeacher att;
    AddAsignaturaToTeacherAdmin att2;
    int profesor;

    public AsignaturaController(Principal pr, int profesor) {
        aa = new AddAsignatura(null, true);
        this.pr = pr;
        this.profesor = profesor;        
        this.pr.btnAddAsignatura.addActionListener(this);
        aa.btnCreateAsignatura.addActionListener(this);
        aa.mnuUpdateAsignatura.addActionListener(this);
        aa.mnuDeleteAsignatura.addActionListener(this);
        aa.txtFindAsignatura.addKeyListener(this);
        aa.btnCancelaAsignatura.addActionListener(this);
        aa.tbAsignatura.addMouseListener(this);
        aa.asocAasignature.addActionListener(this);
    }

    public AsignaturaController(AddAsignaturaToTeacher att, int profesor, String opc, int update) throws SQLException {
        this.att = att;
        this.opc2 = opc;
        this.profesorUpdate = update;
        this.profesor = profesor;
        this.att.btnCreateAsignatura.addActionListener(this);
        this.att.btnCancelaAsignatura.addActionListener(this);
        this.att.deleteAsignaturaToTeach.addActionListener(this);
        this.att.tbAsignatura.addMouseListener(this);
        this.att.mnuAsocAsignaturaTeach.addActionListener(this);
        this.att.mnuUpdateAsignatura.addActionListener(this);
        this.att.mnuDeleteAsignatura.addActionListener(this);
    }

    public AsignaturaController(AddAsignaturaToTeacherAdmin att) throws SQLException {
        this.att2 = att;
        this.att2.btnCreateAsignatura2.addActionListener(this);
        this.att2.btnCancelaAsignatura2.addActionListener(this);
        this.att2.deleteAsignatura.addActionListener(this);
        this.att2.btnCargarAsig.addActionListener(this);
        this.att2.btnClear.addActionListener(this);
        this.att2.updateAsignatura.addActionListener(this);
        this.att2.asocAsignToteacher.addActionListener(this);
        this.att2.deleteAsignaturaToTeach.addActionListener(this);
        this.att2.txtDocTeacherAsign.addKeyListener(this);

    }

    public void cargarAsignaturas(JTable tbArea, String dato) throws SQLException {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListAsignaturas(dato).iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas a = nombreIterator.next();
            columna[0] = a.getAsignatura();
            columna[1] = a.getNombreAsignatura();
            modelo.addRow(columna);
        }
        tbArea.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbArea.setRowSorter(ordenar);
        tbArea.getColumnModel().getColumn(0).setMaxWidth(0);
        tbArea.getColumnModel().getColumn(0).setMinWidth(0);
        tbArea.getColumnModel().getColumn(0).setPreferredWidth(0);
        tbArea.setModel(modelo);
    }

    public void cargarAsignaturasToasign() throws SQLException {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListAsignaturas("").iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas a = nombreIterator.next();
            columna[0] = a.getAsignatura();
            columna[1] = a.getNombreAsignatura();
            modelo.addRow(columna);
        }
        att.tbAsignatura.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        att.tbAsignatura.setRowSorter(ordenar);
        att.tbAsignatura.getColumnModel().getColumn(0).setMaxWidth(0);
        att.tbAsignatura.getColumnModel().getColumn(0).setMinWidth(0);
        att.tbAsignatura.getColumnModel().getColumn(0).setPreferredWidth(0);
        att.tbAsignatura.setModel(modelo);
    }

    public void cargarAsignaturasAsocTeah() throws SQLException {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        if (opc2.equals("C")) {
            user = userdao.getIdUserByDoc(opc);
        } else {
            user = profesorUpdate;
        }
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListAsignaturasAsocTeach(user).iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas a = nombreIterator.next();
            columna[0] = a.getAsignatura();
            columna[1] = a.getNombreAsignatura();
            modelo.addRow(columna);
        }
        att.tbAsignaturaDocente.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        att.tbAsignaturaDocente.setRowSorter(ordenar);
        att.tbAsignaturaDocente.getColumnModel().getColumn(0).setMaxWidth(0);
        att.tbAsignaturaDocente.getColumnModel().getColumn(0).setMinWidth(0);
        att.tbAsignaturaDocente.getColumnModel().getColumn(0).setPreferredWidth(0);
        att.tbAsignaturaDocente.setModel(modelo);
    }

    public void cargarAsignaturasTeacher() throws SQLException {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListAsignaturas("").iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas a = nombreIterator.next();
            columna[0] = a.getAsignatura();
            columna[1] = a.getNombreAsignatura();
            modelo.addRow(columna);
        }
        att2.tbAsignatura3.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        att2.tbAsignatura3.setRowSorter(ordenar);
        att2.tbAsignatura3.getColumnModel().getColumn(0).setMaxWidth(0);
        att2.tbAsignatura3.getColumnModel().getColumn(0).setMinWidth(0);
        att2.tbAsignatura3.getColumnModel().getColumn(0).setPreferredWidth(0);
        att2.tbAsignatura3.setModel(modelo);
    }

    public void cargarAsignaturasToasign2() throws SQLException {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListAsignaturasAsocTeach(user).iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas a = nombreIterator.next();
            columna[0] = a.getAsignatura();
            columna[1] = a.getNombreAsignatura();
            modelo.addRow(columna);
        }
        att2.tbAsignatura2.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        att2.tbAsignatura2.setRowSorter(ordenar);
        att2.tbAsignatura2.getColumnModel().getColumn(0).setMaxWidth(0);
        att2.tbAsignatura2.getColumnModel().getColumn(0).setMinWidth(0);
        att2.tbAsignatura2.getColumnModel().getColumn(0).setPreferredWidth(0);
        att2.tbAsignatura2.setModel(modelo);
    }

    public void cargarCboAsignaturas() throws SQLException {
        pr.cboAsignature.removeAllItems();
        pr.cboAsignature.addItem("-- Seleccione --");
        pr.cboAsignatureEdit.removeAllItems();
        pr.cboAsignatureEdit.addItem("-- Seleccione --");
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListCboAsignaturas(profesor).iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas elemento = nombreIterator.next();
            pr.cboAsignature.addItem(elemento.getNombreAsignatura());
            pr.cboAsignatureEdit.addItem(elemento.getNombreAsignatura());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (pr != null) {
            if (e.getSource() == pr.btnAddAsignatura) {
                try {                    
                    cargarAsignaturas(aa.tbAsignatura, dato);
                    aa.setTitle("Asignaturas");
                    aa.setLocationRelativeTo(null);
                    aa.setVisible(true);
                } catch (SQLException ex) {
                    System.out.println("eror " + ex);
                }
            }
            if (e.getSource() == pr.btnCancelar) {
                limpiarForm();
            }
        }
        if (att != null) {
            if (e.getSource() == att.mnuDeleteAsignatura) {
                int fila = att.tbAsignatura.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        String rptaDelete = asignaturadao.deleteAsignatura(Integer.parseInt(att.tbAsignatura.getValueAt(fila, 0).toString()));
                        if (rptaDelete != null) {
                            try {
                                JOptionPane.showMessageDialog(null, rptaDelete);
                                cargarAsignaturasToasign();
                            } catch (SQLException ex) {
                                Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }

            if (e.getSource() == att.btnCreateAsignatura) {
                String nombre = att.txtNomAsignatura.getText();
                if (nombre.equals("")) {
                    JOptionPane.showMessageDialog(null, "El campo asignatura no debe estar vacio..!");
                    att.txtNomAsignatura.requestFocus();
                    return;
                } else if (!asignaturadao.existAsignatura(nombre)) {
                    try {
                        Asignaturas a = new Asignaturas();
                        a.setNombreAsignatura(nombre);
                        if (opc.equals("U")) {
                            a.setAsignatura(idArea);
                        }
                        String r = asignaturadao.createT(a, opc);
                        if (r != null) {
                            JOptionPane.showMessageDialog(null, r);
                            att.txtNomAsignatura.setText("");
                            att.txtNomAsignatura.requestFocus();
                            att.btnCreateAsignatura.setText("Guardar");
                            opc = "C";
                            idArea = 0;
                            cargarAsignaturasToasign();
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocurrio un error al crear la asignatura");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La asignatura ya existe..!");
                }
            }

            if (e.getSource() == att.mnuUpdateAsignatura) {
                opc = "U";
                att.btnCreateAsignatura.setText("Actualizar");
                int fila = att.tbAsignatura.getSelectedRow();
                if (fila >= 0) {
                    idArea = Integer.parseInt(att.tbAsignatura.getValueAt(fila, 0).toString());
                    att.txtNomAsignatura.setText(att.tbAsignatura.getValueAt(fila, 1).toString());
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }

            if (e.getSource() == att.mnuAsocAsignaturaTeach) {
                int fila = att.tbAsignatura.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Esta seguro de asociar la asignatura ?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        try {
                            String r = asignaturadao.addAsignaturaToTeacher(Integer.parseInt(att.tbAsignatura.getValueAt(fila, 0).toString()), user);
                            if (r.equals("ok")) {
                                JOptionPane.showMessageDialog(null, "Asignatura Asociada a Docente");
                                cargarAsignaturasAsocTeah();
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo asociar la asignatura al docente");
                            }

                        } catch (SQLException ex) {
                            Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura..!");
                }
            }

            if (e.getSource() == att.deleteAsignaturaToTeach) {
                int fila = att.tbAsignaturaDocente.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Esta seguro de quitar la asignatura ?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        try {
                            String r = asignaturadao.deleteAsignaturaToteach(Integer.parseInt(att.tbAsignaturaDocente.getValueAt(fila, 0).toString()), user);
                            JOptionPane.showMessageDialog(null, r);
                            cargarAsignaturasAsocTeah();
                        } catch (SQLException ex) {
                            System.out.println("error " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura..!");
                }
            }

            if (e.getSource() == att.btnCancelaAsignatura) {
                att.dispose();
            }

        }

        if (att2 != null) {

            if (e.getSource() == att2.deleteAsignaturaToTeach) {
                int fila = att2.tbAsignatura2.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Esta seguro de quitar la asignatura ?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        try {
                            String r = asignaturadao.deleteAsignaturaToteach(Integer.parseInt(att2.tbAsignatura2.getValueAt(fila, 0).toString()), user);
                            JOptionPane.showMessageDialog(null, r);
                            cargarAsignaturasToasign2();
                        } catch (SQLException ex) {
                            System.out.println("error " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura..!");
                }
            }

            if (e.getSource() == att2.deleteAsignatura) {
                int fila = att2.tbAsignatura3.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        String rptaDelete = asignaturadao.deleteAsignatura(Integer.parseInt(att2.tbAsignatura3.getValueAt(fila, 0).toString()));
                        if (rptaDelete != null) {
                            try {
                                JOptionPane.showMessageDialog(null, rptaDelete);
                                cargarAsignaturasTeacher();
                            } catch (SQLException ex) {
                                System.out.println("error " + ex);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }

            if (e.getSource() == att2.asocAsignToteacher) {
                int fila = att2.tbAsignatura3.getSelectedRow();
                String doc = att2.txtDocTeacherAsign.getText();
                if (doc.equals("")) {
                    JOptionPane.showMessageDialog(null, "El campo Documento no debe estar vacio ..!");
                    att2.txtDocTeacherAsign.requestFocus();
                    return;
                }
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Esta seguro de asociar la asignatura ?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        try {
                            String r = asignaturadao.addAsignaturaToTeacher(Integer.parseInt(att2.tbAsignatura3.getValueAt(fila, 0).toString()), user);
                            if (r.equals("ok")) {
                                JOptionPane.showMessageDialog(null, "Asignatura Asociada a Docente");
                                cargarAsignaturasToasign2();
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo asociar la asignatura al docente");
                            }

                        } catch (SQLException ex) {
                            System.out.println("error " + ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura..!");
                }
            }

            if (e.getSource() == att2.btnCancelaAsignatura2) {
                att2.dispose();
            }

            if (e.getSource() == att2.btnCreateAsignatura2) {
                String nombre = att2.txtNomAsignatura2.getText();
                if (nombre.equals("")) {
                    JOptionPane.showMessageDialog(null, "El campo asignatura no debe estar vacio..!");
                    att2.txtNomAsignatura2.requestFocus();
                    return;
                } else if (!asignaturadao.existAsignatura(nombre)) {
                    try {
                        Asignaturas a = new Asignaturas();
                        a.setNombreAsignatura(nombre);
                        if (opc.equals("U")) {
                            a.setAsignatura(idArea);
                        }
                        String r = asignaturadao.createT(a, opc);
                        if (r != null) {
                            JOptionPane.showMessageDialog(null, r);
                            att2.txtNomAsignatura2.setText("");
                            att2.txtNomAsignatura2.requestFocus();
                            att2.btnCreateAsignatura2.setText("Guardar");
                            opc = "C";
                            idArea = 0;
                            cargarAsignaturasTeacher();
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocurrio un error al crear la asignatura");
                        }
                    } catch (SQLException ex) {
                        System.out.println("error " + ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La asignatura ya existe..!");
                }
            }

            if (e.getSource() == att2.updateAsignatura) {
                opc = "U";
                att2.btnCreateAsignatura2.setText("Actualizar");
                int fila = att2.tbAsignatura3.getSelectedRow();
                if (fila >= 0) {
                    idArea = Integer.parseInt(att2.tbAsignatura3.getValueAt(fila, 0).toString());
                    att2.txtNomAsignatura2.setText(att2.tbAsignatura3.getValueAt(fila, 1).toString());
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }

            if (e.getSource() == att2.btnCargarAsig) {
                if (!att2.txtDocTeacherAsign.getText().equals("")) {
                    if (!isNumeric(att2.txtDocTeacherAsign.getText())) {
                        att2.txtDocTeacherAsign.setText("");
                        return;
                    }
                    String doc = att2.txtDocTeacherAsign.getText();
                    userdao = new UsersDAO();
                    String profesorToUpdate = userdao.getUserByDoc(doc);
                    user = userdao.getIdUserByDoc(doc);
                    System.out.println(profesorToUpdate);
                    if (!profesorToUpdate.equals("")) {
                        try {
                            att2.txtTeacherAsign.setText(profesorToUpdate);
                            cargarAsignaturasToasign2();
                            userdao = null;
                        } catch (SQLException ex) {
                            Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            att2.txtTeacherAsign.setText("");
                            cargarAsignaturasToasign2();
                            userdao = null;
                            JOptionPane.showMessageDialog(null, "No se encuentra el docente..");
                        } catch (SQLException ex) {
                            Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El campo documento no debe estar vacio..");
                }
            }

            if (e.getSource() == att2.btnClear) {
                try {
                    att2.txtDocTeacherAsign.setText("");
                    att2.txtTeacherAsign.setText("");
                    cargarAsignaturasTeacher();
                    userdao = null;
                } catch (SQLException ex) {
                    Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        if (aa != null) {
            if (e.getSource() == aa.btnCancelaAsignatura) {
                limpiarForm();
            }

            if (e.getSource() == aa.asocAasignature) {
                int fila = aa.tbAsignatura.getSelectedRow();                
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Esta seguro de asociar la asignatura ?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        try {
                            String r = asignaturadao.addAsignaturaToTeacher(Integer.parseInt(aa.tbAsignatura.getValueAt(fila, 0).toString()), profesor);
                            if (r.equals("ok")) {
                                JOptionPane.showMessageDialog(null, "Asignatura Asociada a Docente, ahora puede seleccionarla");
                            } else {
                                JOptionPane.showMessageDialog(null, "No se pudo asociar la asignatura al docente");
                            }

                        } catch (SQLException ex) {
                            Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura..!");
                }
            }

            if (e.getSource() == aa.btnCreateAsignatura) {
                try {
                    String asignatura = aa.txtNomAsignatura.getText();
                    if (asignatura.equals("")) {
                        JOptionPane.showMessageDialog(null, "Ingrese el nombre de la asignatura..!");
                        aa.txtNomAsignatura.requestFocus();
                        return;
                    }
                    if (asignaturadao.existAsignatura(asignatura)) {
                        JOptionPane.showMessageDialog(null, "La Asignatura: " + asignatura + " coincide con una existente\nNo es necesario crearla otra vez.\nSi lo desea puede actualizarla..!");
                        aa.txtNomAsignatura.setText("");
                        aa.txtNomAsignatura.requestFocus();
                        return;
                    }
                    Asignaturas a = new Asignaturas();
                    a.setNombreAsignatura(asignatura);
                    a.setAsignatura(idArea);
                    String rptaRegistro = asignaturadao.create(a, opc, profesor);
                    if (rptaRegistro != null) {
                        JOptionPane.showMessageDialog(null, rptaRegistro);
                        cargarCboAsignaturas();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (opc.equals("C")) {
                            setArea();
                            aa.dispose();
                        } else {
                            try {
                                cargarAsignaturas(aa.tbAsignatura, dato);
                            } catch (SQLException ex) {
                                Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        opc = "C";
                        limpiarForm();
                    } else if (opc.equals("C")) {
                        JOptionPane.showMessageDialog(null, "No se pudo crear el Registro");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el Registro");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if (e.getSource() == aa.mnuUpdateAsignatura) {
                opc = "U";
                int fila = aa.tbAsignatura.getSelectedRow();
                if (fila >= 0) {
                    idArea = Integer.parseInt(aa.tbAsignatura.getValueAt(fila, 0).toString());
                    aa.txtNomAsignatura.setText(aa.tbAsignatura.getValueAt(fila, 1).toString());
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }

            if (e.getSource() == aa.mnuDeleteAsignatura) {
                int fila = aa.tbAsignatura.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        String rptaDelete = asignaturadao.deleteAsignatura(Integer.parseInt(aa.tbAsignatura.getValueAt(fila, 0).toString()));
                        if (rptaDelete != null) {
                            try {
                                JOptionPane.showMessageDialog(null, rptaDelete);
                                cargarAsignaturas(aa.tbAsignatura, "");
                                cargarCboAsignaturas();
                            } catch (SQLException ex) {
                                Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (aa != null) {
            if (e.getSource() == aa.txtFindAsignatura) {
                try {
                    dato = aa.txtFindAsignatura.getText();
                    cargarAsignaturas(aa.tbAsignatura, dato);
                } catch (SQLException ex) {
                    Logger.getLogger(AsignaturaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (att2 != null) {
            if (e.getSource() == att2.txtDocTeacherAsign) {
                if (!isNumeric(att2.txtDocTeacherAsign.getText())) {
                    att2.txtDocTeacherAsign.setText("");
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (att2 != null) {
            if (e.getSource() == att2.txtDocTeacherAsign) {
                if (att2.txtDocTeacherAsign.getText().length() == 10) {
                    e.consume();
                }
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    private void limpiarForm() {
        aa.txtNomAsignatura.setText("");
        opc = "C";
    }

    public void setArea() {
        Iterator<Asignaturas> ar = asignaturadao.getLastInsert().iterator();
        if (ar.hasNext()) {
            Asignaturas elemento = ar.next();
            pr.cboAsignature.setSelectedItem(elemento.getNombreAsignatura());
        }

    }

    public void mouseClicked(MouseEvent e) {
        if (pr != null) {
            if (e.getClickCount() == 2) {
                int fila = aa.tbAsignatura.getSelectedRow();
                if (fila >= 0) {
                    if (asignaturadao.existAsignaturatooTeacher(aa.tbAsignatura.getValueAt(fila, 1).toString(), profesor)) {
                        pr.cboAsignature.addItem(aa.tbAsignatura.getValueAt(fila, 1).toString());
                        pr.cboAsignature.setSelectedItem(aa.tbAsignatura.getValueAt(fila, 1).toString());
                        aa.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "La asignatura no esta asociada al docente, debe asociarla primero..!");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }
        }
    }

}
