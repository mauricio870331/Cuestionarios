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
import java.util.ArrayList;
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
    int idArea = 0;
    AddAsignatura aa = null;
    AddAsignaturaToTeacher att;
    AddAsignaturaToTeacherAdmin att2;
    int profesor;
    ArrayList<Asignaturas> asig = null;
    ArrayList<Asignaturas> asigtmp = null;

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
    }

    public AsignaturaController(AddAsignaturaToTeacher att, int profesor) throws SQLException {
        this.att = att;
        this.profesor = profesor;
        this.att.btnAdsignaturaTeacher.addActionListener(this);
        this.att.btnSaveAsignaturasToTeacher.addActionListener(this);
        this.att.btnCreateAsignatura.addActionListener(this);
        this.att.btnCancelaAsignatura.addActionListener(this);
        this.att.deleteAsignaturaToTeach.addActionListener(this);
        asig = new ArrayList<>();
    }

    public AsignaturaController(AddAsignaturaToTeacherAdmin att) throws SQLException {
        this.att2 = att;
        this.att2.btnAdsignaturaTeacher2.addActionListener(this);
        this.att2.btnSaveAsignaturasToTeacher2.addActionListener(this);
        this.att2.btnCreateAsignatura2.addActionListener(this);
        this.att2.btnCancelaAsignatura2.addActionListener(this);
        this.att2.deleteAsignaturaToTeach2.addActionListener(this);
        this.att2.btnCargarAsig.addActionListener(this);
        this.att2.btnClear.addActionListener(this);
        asig = new ArrayList<>();
        asigtmp = new ArrayList<>();
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

    public void cargarAsignaturasToasign() {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Asignaturas> nombreIterator = asig.iterator();
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

    public void cargarAsignaturasTeacher() {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Asignaturas> nombreIterator = asigtmp.iterator();
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

    public void cargarAsignaturasToasign2() {
        String Titulos[] = {"Id", "Nombre"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Asignaturas> nombreIterator = asig.iterator();
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

    public void cargarCboAsignaturasToteacher() throws SQLException {
        att.txtNomAsignatura.removeAllItems();
        att.txtNomAsignatura.addItem("-- Seleccione --");
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListAsignaturas("").iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas elemento = nombreIterator.next();
            att.txtNomAsignatura.addItem(elemento.getNombreAsignatura());
        }
    }

    public void cargarCboAsignaturasToteacher2() throws SQLException {
        att2.txtNomAsignatura2.removeAllItems();
        att2.txtNomAsignatura2.addItem("-- Seleccione --");
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListAsignaturas("").iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas elemento = nombreIterator.next();
            att2.txtNomAsignatura2.addItem(elemento.getNombreAsignatura());
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
            if (e.getSource() == att.btnAdsignaturaTeacher) {
                String asignatura = (String) att.txtNomAsignatura.getSelectedItem();
                if (asignatura.equals("-- Seleccione --")) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una asignatura");
                    return;
                }
                Asignaturas a = new Asignaturas();
                a.setAsignatura(asignaturadao.getAsignaturaByName(asignatura));
                a.setNombreAsignatura(asignatura);
                asig.add(a);
                att.txtNomAsignatura.removeItem(asignatura);
                cargarAsignaturasToasign();
                System.out.println(asig.size());
            }

            if (e.getSource() == att.deleteAsignaturaToTeach) {
                int fila = att.tbAsignatura.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Esta seguro de quitar la asignatura ?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        String asignatura = att.tbAsignatura.getValueAt(fila, 1).toString();
                        Iterator<Asignaturas> lpt = asig.iterator();
                        while (lpt.hasNext()) {
                            Asignaturas borrar = lpt.next();
                            if (borrar.getNombreAsignatura().equals(asignatura)) {
                                lpt.remove();
                            }
                        }
                        att.txtNomAsignatura.addItem(asignatura);
                        cargarAsignaturasToasign();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura..!");
                }
            }

            if (e.getSource() == att.btnSaveAsignaturasToTeacher) {
                try {
                    if (asig.size() > 0) {
                        userdao = new UsersDAO();
                        System.out.println(userdao.getLastInsert());
                        String r = asignaturadao.addAsignaturaToTeacher(asig, userdao.getLastInsert());
                        if (r.equals("ok")) {
                            JOptionPane.showMessageDialog(null, "Asignaturas agregadas a docente..!");
                            userdao = null;
                            att.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar asignaturas a docente..!");
                            userdao = null;
                            att.dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Aun no ha agregado asignaturas..!");
                        return;
                    }
                } catch (SQLException ex) {
                    System.out.println("error ascontroller " + ex);
                }
            }

            if (e.getSource() == att.btnCancelaAsignatura) {
                asig.clear();
                att.dispose();
            }

            if (e.getSource() == att.btnCreateAsignatura) {
                String nombre = JOptionPane.showInputDialog(null,
                        "Ingrese Asignatura",
                        "Crear Asignaturas",
                        JOptionPane.INFORMATION_MESSAGE);
                if (nombre == null) {
                    return;
                } else if (nombre.equals("")) {
                    JOptionPane.showMessageDialog(null, "El campo asignatura no debe estar vacio..!");
                    return;
                } else if (!asignaturadao.existAsignatura(nombre)) {
                    try {
                        Asignaturas a = new Asignaturas();
                        a.setNombreAsignatura(nombre);
                        String r = asignaturadao.create(a, "C");
                        if (r != null) {
                            JOptionPane.showMessageDialog(null, r);
                            att.txtNomAsignatura.addItem(nombre);
                            att.txtNomAsignatura.requestFocus();
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

        }

        if (att2 != null) {
            if (e.getSource() == att2.btnAdsignaturaTeacher2) {
                String asignatura = (String) att2.txtNomAsignatura2.getSelectedItem();
                if (asignatura.equals("-- Seleccione --")) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una asignatura");
                    return;
                }
                Asignaturas a = new Asignaturas();
                a.setAsignatura(asignaturadao.getAsignaturaByName(asignatura));
                a.setNombreAsignatura(asignatura);
                asig.add(a);
                att2.txtNomAsignatura2.removeItem(asignatura);
                cargarAsignaturasToasign2();
            }

            if (e.getSource() == att2.deleteAsignaturaToTeach2) {
                int fila = att2.tbAsignatura2.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Esta seguro de quitar la asignatura ?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        String asignatura = att2.tbAsignatura2.getValueAt(fila, 1).toString();
                        Iterator<Asignaturas> lpt = asig.iterator();
                        while (lpt.hasNext()) {
                            Asignaturas borrar = lpt.next();
                            if (borrar.getNombreAsignatura().equals(asignatura)) {
                                lpt.remove();
                            }
                        }
                        att2.txtNomAsignatura2.addItem(asignatura);
                        cargarAsignaturasToasign2();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura..!");
                }
            }

            if (e.getSource() == att2.btnSaveAsignaturasToTeacher2) {
                if (att2.txtDocTeacherAsign.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "El campo documento no debe estar vacio..!");
                    return;
                }
                try {
                    if (asig.size() > 0) {
                        userdao = new UsersDAO();
                        String r = asignaturadao.addAsignaturaToTeacher(asig, userdao.getIdUserByDoc(att2.txtDocTeacherAsign.getText()));
                        if (r.equals("ok")) {
                            JOptionPane.showMessageDialog(null, "Asignaturas agregadas a docente..!");
                            userdao = null;
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar asignaturas a docente..!");
                            userdao = null;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Aun no ha agregado asignaturas..!");
                        return;
                    }
                } catch (SQLException ex) {
                    System.out.println("error ascontroller " + ex);
                }
            }

            if (e.getSource() == att2.btnCancelaAsignatura2) {
                asig.clear();
                asigtmp.clear();
                att2.dispose();
            }

            if (e.getSource() == att2.btnCreateAsignatura2) {
                String nombre = JOptionPane.showInputDialog(null,
                        "Ingrese Asignatura",
                        "Crear Asignaturas",
                        JOptionPane.INFORMATION_MESSAGE);
                if (nombre == null) {
                    return;
                } else if (nombre.equals("")) {
                    JOptionPane.showMessageDialog(null, "El campo asignatura no debe estar vacio..!");
                    return;
                } else if (!asignaturadao.existAsignatura(nombre)) {
                    try {
                        Asignaturas a = new Asignaturas();
                        a.setNombreAsignatura(nombre);
                        String r = asignaturadao.create(a, "C");
                        if (r != null) {
                            JOptionPane.showMessageDialog(null, r);
                            att2.txtNomAsignatura2.addItem(nombre);
                            att2.txtNomAsignatura2.requestFocus();
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

            if (e.getSource() == att2.btnCargarAsig) {
                if (!att2.txtDocTeacherAsign.getText().equals("")) {
                    if (!isNumeric(att2.txtDocTeacherAsign.getText())) {
                        att2.txtDocTeacherAsign.setText("");
                        return;
                    }
                    String doc = att2.txtDocTeacherAsign.getText();
                    userdao = new UsersDAO();
                    String profesorToUpdate = userdao.getUserByDoc(doc);
                    System.out.println(profesorToUpdate);
                    if (!profesorToUpdate.equals("")) {
                        att2.txtTeacherAsign.setText(profesorToUpdate);
                        asigtmp = asignaturadao.getAsignaturasTeacher(userdao.getIdUserByDoc(doc));
                        cargarAsignaturasTeacher();
                        userdao = null;
                    } else {
                        att2.txtTeacherAsign.setText("");
                        asigtmp = asignaturadao.getAsignaturasTeacher(userdao.getIdUserByDoc(doc));
                        cargarAsignaturasTeacher();
                        userdao = null;
                        JOptionPane.showMessageDialog(null, "No se encuentra el docente..");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El campo documento no debe estar vacio..");
                }
            }

            if (e.getSource() == att2.btnClear) {
                asigtmp.clear();
                att2.txtDocTeacherAsign.setText("");
                att2.txtTeacherAsign.setText("");
                cargarAsignaturasTeacher();
                userdao = null;
            }

        }

        if (aa != null) {
            if (e.getSource() == aa.btnCancelaAsignatura) {
                limpiarForm();
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
        if (e.getClickCount() == 2) {
            int fila = aa.tbAsignatura.getSelectedRow();
            if (fila >= 0) {
                pr.cboAsignature.setSelectedItem(aa.tbAsignatura.getValueAt(fila, 1).toString());
                aa.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }
    }

}
