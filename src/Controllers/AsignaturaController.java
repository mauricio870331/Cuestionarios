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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    int idArea = 0;
    AddAsignatura aa;

    public AsignaturaController(Principal pr) {
        aa = new AddAsignatura(null, true);
        this.pr = pr;
        this.pr.btnAddAsignatura.addActionListener(this);
        aa.btnCreateAsignatura.addActionListener(this);
        aa.mnuUpdateAsignatura.addActionListener(this);
        aa.mnuDeleteAsignatura.addActionListener(this);
        aa.txtFindAsignatura.addKeyListener(this);
        aa.btnCancelaAsignatura.addActionListener(this);
        aa.tbAsignatura.addMouseListener(this);
    }

    public void cargarAsignaturas(JTable tbArea, String dato) {
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

    public void cargarCboAsignaturas() {
        pr.cboAsignature.removeAllItems();
        pr.cboAsignature.addItem("-- Seleccione --");
        Iterator<Asignaturas> nombreIterator = asignaturadao.getListCboAsignaturas().iterator();
        while (nombreIterator.hasNext()) {
            Asignaturas elemento = nombreIterator.next();
            pr.cboAsignature.addItem(elemento.getNombreAsignatura());
        }       
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pr.btnAddAsignatura) {
            cargarAsignaturas(aa.tbAsignatura, dato);
            aa.setTitle("Asignaturas");
            aa.setLocationRelativeTo(null);
            aa.setVisible(true);
        }
        
        if (e.getSource() == aa.btnCancelaAsignatura) {
            limpiarForm();            
        }

        if (e.getSource() == aa.btnCreateAsignatura) {
            String asignatura = aa.txtNomAsignatura.getText();
            if (asignatura.equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre del Autor..!");
                aa.txtNomAsignatura.requestFocus();
                return;
            }
            if (asignaturadao.existAsignatura(asignatura)) {
                JOptionPane.showMessageDialog(null, "La Asignatura: "+asignatura+" coincide con una existente\nNo es necesario crearla otra vez.\nSi lo desea puede actualizarla..!");
                aa.txtNomAsignatura.setText("");
                aa.txtNomAsignatura.requestFocus();
                return;
            }
            Asignaturas a = new Asignaturas();
            a.setNombreAsignatura(asignatura);
            a.setAsignatura(idArea);
            String rptaRegistro = asignaturadao.create(a, opc);
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
                    cargarAsignaturas(aa.tbAsignatura, dato);
                }
                opc = "C";
                limpiarForm();
            } else if (opc.equals("C")) {
                JOptionPane.showMessageDialog(null, "No se pudo crear el Registro");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el Registro");
            }

        }

        if (e.getSource() == pr.btnCancelar) {

            limpiarForm();
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
                        JOptionPane.showMessageDialog(null, rptaDelete);
                        cargarAsignaturas(aa.tbAsignatura, "");
                        cargarCboAsignaturas();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == aa.txtFindAsignatura) {         
            dato = aa.txtFindAsignatura.getText();
            cargarAsignaturas(aa.tbAsignatura, dato);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    private void limpiarForm() {
        aa.txtNomAsignatura.setText("");
        opc="C";       
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
