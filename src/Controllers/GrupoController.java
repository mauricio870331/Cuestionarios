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
public final class GrupoController extends MouseAdapter implements ActionListener, KeyListener {

    String dato = "";
    DefaultTableModel modelo = new DefaultTableModel();
    GrupoDAO grupodao = new GrupoDAO();
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    int idArea = 0;
    Principal pr;
    AddGroup att;
    int idGrupo = 0;
    String oldGrupo = "";

    public GrupoController(Principal pr, AddGroup att) throws SQLException {
        this.pr = pr;
        this.att = att;
        this.att.btnCreateGrupo.addActionListener(this);
        this.att.btnCancelarGrupo.addActionListener(this);
        this.att.tbGrupos.addMouseListener(this);
        this.att.mnuDeleteGrupo.addActionListener(this);
        this.att.mnuUpdateGrupo.addActionListener(this);
        this.att.txtFindGrupo.addKeyListener(this);

    }

    public void cargarGrupos() throws SQLException {
        String Titulos[] = {"", "Grupo", ""};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[3];
        Iterator<Grupo> nombreIterator = grupodao.getListGrupos(dato).iterator();
        while (nombreIterator.hasNext()) {
            Grupo a = nombreIterator.next();
            columna[0] = a.getIdGrupo();
            columna[1] = a.getGrupo();
            columna[2] = a.getCant();
            modelo.addRow(columna);
        }
        att.tbGrupos.setModel(modelo);
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        att.tbGrupos.setRowSorter(ordenar);
        att.tbGrupos.getColumnModel().getColumn(0).setMaxWidth(0);
        att.tbGrupos.getColumnModel().getColumn(0).setMinWidth(0);
        att.tbGrupos.getColumnModel().getColumn(0).setPreferredWidth(0);
        att.tbGrupos.getColumnModel().getColumn(1).setPreferredWidth(100);
        att.tbGrupos.getColumnModel().getColumn(2).setMaxWidth(0);
        att.tbGrupos.getColumnModel().getColumn(2).setMinWidth(0);
        att.tbGrupos.getColumnModel().getColumn(2).setPreferredWidth(0);
        att.tbGrupos.setModel(modelo);
    }

    public void actionPerformed(ActionEvent e) {
        if (att != null) {
            if (e.getSource() == att.btnCreateGrupo) {
                String grupo = att.txtNomGrupo.getText();
                String cantidad = att.txtCcant.getText();
                if (grupo.equals("")) {
                    JOptionPane.showMessageDialog(null, "El campo Grupo no debe estar vacio");
                    att.txtNomGrupo.requestFocus();
                    return;
                }
                if (cantidad.equals("")) {
                    JOptionPane.showMessageDialog(null, "La cantidad de estudiantes no deebe estar vacia..!");
                    att.txtCcant.requestFocus();
                    return;
                }
                String msn = "Grupo Creado con exito..!";
                Grupo g = new Grupo();
                if (opc.equals("U")) {
                    g.setIdGrupo(idGrupo);
                    msn = "Grupo Actualizado con exito..!";
                }
                g.setGrupo(grupo);
                g.setCant(cantidad);
                String r = grupodao.create(g, opc);
                if (r.equals("ok")) {
                    try {
                        cargarGrupos();
                        JOptionPane.showMessageDialog(null, msn);
                        setGrupo();
                        idGrupo = 0;
                        att.dispose();
                    } catch (SQLException ex) {
                        Logger.getLogger(GrupoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ocurrio un error al ingresar el grupo");
                }

            }

            if (e.getSource() == att.mnuUpdateGrupo) {
                opc = "U";
                int fila = att.tbGrupos.getSelectedRow();
                if (fila >= 0) {
                    idGrupo = Integer.parseInt(att.tbGrupos.getValueAt(fila, 0).toString());
                    oldGrupo = att.tbGrupos.getValueAt(fila, 1).toString();
                    att.txtNomGrupo.setText(att.tbGrupos.getValueAt(fila, 1).toString());
                    att.txtCcant.setText(att.tbGrupos.getValueAt(fila, 2).toString());
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }

            if (e.getSource() == att.mnuDeleteGrupo) {
                int fila = att.tbGrupos.getSelectedRow();
                if (fila >= 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Aviso..!",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        oldGrupo = att.tbGrupos.getValueAt(fila, 1).toString();
                        String rptaDelete = grupodao.deleteGrupo(Integer.parseInt(att.tbGrupos.getValueAt(fila, 0).toString()));
                        if (rptaDelete != null) {
                            try {
                                JOptionPane.showMessageDialog(null, rptaDelete);
                                pr.cboGrupo.removeItem(oldGrupo);
                                oldGrupo = "";
                                cargarGrupos();
                            } catch (SQLException ex) {
                                System.out.println("error " + ex);
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                }
            }

            if (e.getSource() == att.btnCancelarGrupo) {
                limpiarForm();
                att.dispose();
            }

//            if (e.getSource() == att.deleteAsignaturaToTeach) {
//                int fila = att.tbGrupos.getSelectedRow();
//                if (fila >= 0) {
//                    int response = JOptionPane.showConfirmDialog(null, "Esta seguro de quitar la asignatura ?", "Aviso..!",
//                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if (response == JOptionPane.YES_OPTION) {
//                        String asignatura = att.tbGrupos.getValueAt(fila, 1).toString();
//                        Iterator<Asignaturas> lpt = asig.iterator();
//                        while (lpt.hasNext()) {
//                            Asignaturas borrar = lpt.next();
//                            if (borrar.getNombreAsignatura().equals(asignatura)) {
//                                lpt.remove();
//                            }
//                        }
//                        att.txtNomGrupo.addItem(asignatura);
//                        cargarAsignaturasToasign();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "No has seleccionado una asignatura..!");
//                }
//            }
//
//            if (e.getSource() == att.btnSaveAsignaturasToTeacher) {
//                try {
//                    if (asig.size() > 0) {
//                        userdao = new UsersDAO();
//                        System.out.println(userdao.getLastInsert());
//                        String r = asignaturadao.addAsignaturaToTeacher(asig, userdao.getLastInsert());
//                        if (r.equals("ok")) {
//                            JOptionPane.showMessageDialog(null, "Asignaturas agregadas a docente..!");
//                            userdao = null;
//                            att.dispose();
//                        } else {
//                            JOptionPane.showMessageDialog(null, "Ocurrio un error al agregar asignaturas a docente..!");
//                            userdao = null;
//                            att.dispose();
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Aun no ha agregado asignaturas..!");
//                        return;
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("error ascontroller " + ex);
//                }
//            }
//
//            if (e.getSource() == att.btnCancelaAsignatura) {
//                asig.clear();
//                att.dispose();
//            }
//
//            if (e.getSource() == att.btnCreateGrupo) {
//                String nombre = JOptionPane.showInputDialog(null,
//                        "Ingrese Asignatura",
//                        "Crear Asignaturas",
//                        JOptionPane.INFORMATION_MESSAGE);
//                if (nombre == null) {
//                    return;
//                } else if (nombre.equals("")) {
//                    JOptionPane.showMessageDialog(null, "El campo asignatura no debe estar vacio..!");
//                    return;
//                } else if (!asignaturadao.existAsignatura(nombre)) {
//                    try {
//                        Asignaturas a = new Asignaturas();
//                        a.setNombreAsignatura(nombre);
//                        String r = asignaturadao.create(a, "C");
//                        if (r != null) {
//                            JOptionPane.showMessageDialog(null, r);
//                            att.txtNomGrupo.addItem(nombre);
//                            att.txtNomGrupo.requestFocus();
//                        } else {
//                            JOptionPane.showMessageDialog(null, "Ocurrio un error al crear la asignatura");
//                        }
//                    } catch (SQLException ex) {
//                        Logger.getLogger(GrupoController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(null, "La asignatura ya existe..!");
//                }
//            }
//
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == att.txtFindGrupo) {
            try {
                dato = att.txtFindGrupo.getText();
                cargarGrupos();
            } catch (SQLException ex) {
                System.out.println("error " + ex);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        if (att2 != null) {
//            if (e.getSource() == att2.txtDocTeacherAsign) {
//                if (att2.txtDocTeacherAsign.getText().length() == 10) {
//                    e.consume();
//                }
//            }
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    private void limpiarForm() {
        att.txtNomGrupo.setText("");
        att.txtCcant.setText("");
        opc = "C";
    }

    public void setGrupo() {
        pr.cboGrupo.removeItem(oldGrupo);
        pr.cboGrupo.addItem(att.txtNomGrupo.getText());
        pr.cboGrupo.setSelectedItem(att.txtNomGrupo.getText());
        limpiarForm();
        oldGrupo = "";
    }

    public void mouseClicked(MouseEvent e) {

    }

}
