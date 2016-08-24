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
import static Controllers.CuestionarioController.isNumeric;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Mauricio
 */
public final class UsersController implements ActionListener, KeyListener {

    int idToUpdate = 0;
    int id_rol;
    int rol = 0;
    int id_gym;
    int pagina = 1;
    String dato = "";
    Principal pr;
    UsersDAO admDao;
    GrupoDAO gymDao = new GrupoDAO();
    RolesDAO rolDao = new RolesDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    Date m = new Date();//para capturar la fecha actual
    String opc = "C";
    int countAction = 0;
    JFileChooser FileChooser = new JFileChooser();
    String foto = "";
    boolean clientes = true;
    boolean admin = false;
    boolean secretaria = false;
    int idUserLog;
    int idGrupo;

    public UsersController(Principal pr, UsersDAO admDao, int id_rol, int idUserLog, int idGrupo) {
        this.pr = pr;
        this.admDao = admDao;
        this.pr.btnRegistrar.addActionListener(this);
        this.pr.btnCancelar.addActionListener(this);
        this.pr.mnuUpdateAdmin.addActionListener(this);
        this.pr.mnuDeleteAdmin.addActionListener(this);
        this.pr.btnAdelante.addActionListener(this);
        this.pr.btnAtras.addActionListener(this);
        this.pr.btnUltimo.addActionListener(this);
        this.pr.btnPrimero.addActionListener(this);
        this.pr.txtBuscarAdmin.addKeyListener(this);
        this.pr.createUsers.addActionListener(this);
        this.id_rol = id_rol;
        this.idUserLog = idUserLog;
        this.idGrupo = idGrupo;
        this.pr.rdoClientes.addActionListener(this);
        this.pr.rdoAdmin.addActionListener(this);
        this.pr.rdoTodos.addActionListener(this);
        this.pr.rdoTodos.setSelected(true);
        this.pr.txtDoc.addKeyListener(this);
        cargarAdmin(pr.tbAdmin, "", 0);
        cargarCboGrupo();
        cargarRol();
        this.pr.lblVerUsers.setText("Tipo Usuarios");
        //        this.pr.btnFotoAdmin.addActionListener(this);
//        this.pr.btnAdjuntarfoto.addActionListener(this);

    }

    public void cargarAdmin(JTable tbAdmin, String dato, int rol) {
        String Titulos[] = {"Tipo Documento", "Documento", "Nombres", "Apellidos"};
        modelo = new DefaultTableModel(null, Titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {//para evitar que las celdas sean editables
                return false;
            }
        };
        Object[] columna = new Object[6];
        Iterator<Users> nombreIterator = admDao.getListAdministrador(pagina, dato, rol).iterator();
        while (nombreIterator.hasNext()) {
            Users p = nombreIterator.next();
            columna[0] = p.getTipoDoc();
            columna[1] = p.getDocumento();
            columna[2] = p.getNombres();
            columna[3] = p.getApellidos();
            modelo.addRow(columna);
        }
        TableRowSorter<TableModel> ordenar = new TableRowSorter<>(modelo);
        tbAdmin.setRowSorter(ordenar);
        tbAdmin.setModel(modelo);
    }

    public void cargarCboGrupo() {
        pr.cboGrupo.removeAllItems();
        Iterator<Grupo> nombreIterator = null;
        if (id_rol != 4 && id_gym != 0) {
            nombreIterator = gymDao.getListGrupoById(id_gym).iterator();
            while (nombreIterator.hasNext()) {
                Grupo elemento = nombreIterator.next();
                pr.cboGrupo.addItem(elemento.getIdGrupo() + " - " + elemento.getGrupo());
            }
        } else {
            pr.cboGrupo.addItem("-- Seleccione --");
            nombreIterator = gymDao.getListGrupoById(id_gym).iterator();
            while (nombreIterator.hasNext()) {
                Grupo elemento = nombreIterator.next();
                pr.cboGrupo.addItem(elemento.getGrupo());
            }
        }

    }

    public void cargarRol() {
        pr.cboIdRol.removeAllItems();
        Iterator<Roles> rol = rolDao.getListRol().iterator();
        pr.cboIdRol.addItem("-- Seleccione --");
        while (rol.hasNext()) {
            Roles elemento = rol.next();
            pr.cboIdRol.addItem(elemento.getRol());
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pr.btnRegistrar) {
            SimpleDateFormat Año = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendario = Calendar.getInstance();
            String FchaHoy = Año.format(m);
            int hora, minutos, segundos;
            hora = calendario.get(Calendar.HOUR_OF_DAY);
            minutos = calendario.get(Calendar.MINUTE);
            segundos = calendario.get(Calendar.SECOND);
            String documento = pr.txtDoc.getText();
            String nombres = pr.txtNombres.getText();
            String apellidos = pr.txtApellidos.getText();
            String pass = new String(pr.txtPass.getPassword());
            String tipo_doc = (String) pr.cboTipoDocAdmin.getSelectedItem();
            String Grupo = (String) pr.cboGrupo.getSelectedItem();
            if (tipo_doc.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "debe seleccionar un tipo de documento");
                return;
            }
            if (admDao.getDoc(documento)) {
                JOptionPane.showMessageDialog(null, "El documento " + documento + " ya existe");
                return;
            }
            if (pr.txtDoc.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo documento no debe estar vacio");
                pr.txtDoc.requestFocus();
                return;
            }
            int grupo = 0;
            if (!Grupo.equals("-- Seleccione --")) {
                grupo = gymDao.getIdGrupoByName(Grupo);
            }
            String idRol = (String) pr.cboIdRol.getSelectedItem();
            int rolU = 0;
            if (!idRol.equals("-- Seleccione --")) {
                rolU = rolDao.getIdRolByNombre(idRol);
            }

            String rptaRegistro = admDao.Create(documento, tipo_doc, nombres, apellidos, grupo, rolU, pass, foto, opc, idToUpdate);
            if (rptaRegistro.equals("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "No se pudo crear el registro por que el numero de documento ya existe");
                opc = "C";
                idToUpdate = 0;
                limpiarForm();
                return;
            }
            if (rptaRegistro != null) {
                JOptionPane.showMessageDialog(null, rptaRegistro);
                cargarAdmin(pr.tbAdmin, dato, 0);
                if (opc.equals("U")) {
                    pr.btnRegistrar.setText("Guardar");
                }
                if (rolU == 1) {
                    try {
                        AddAsignaturaToTeacher att = new AddAsignaturaToTeacher(null, true);
                        AsignaturaController ac = new AsignaturaController(att, rolU);
                        ac.cargarCboAsignaturasToteacher();
                        att.setLocationRelativeTo(null);
                        att.setVisible(true);
                    } catch (SQLException ex) {
                        System.out.println("error " + ex);
                    }
                }
                opc = "C";
                idToUpdate = 0;
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
        if (e.getSource() == pr.mnuUpdateAdmin) {
            opc = "U";
            int fila = pr.tbAdmin.getSelectedRow();
            if (fila >= 0) {
                pr.btnRegistrar.setText("Actualizar");
                String tipoDoc = pr.tbAdmin.getValueAt(fila, 0).toString();
                pr.cboTipoDocAdmin.setSelectedItem(tipoDoc);
                String documento = pr.tbAdmin.getValueAt(fila, 1).toString();
                pr.txtDoc.setText(documento);
                int idRolU = 0;
                int idGrupoU = 0;
                ArrayList<Users> User = admDao.getIdToUpdate(documento);
                if (User.size() > 0) {
                    pr.txtPass.setText(User.get(0).getPassword());
                    idToUpdate = User.get(0).getIdUser();
                    idRolU = User.get(0).getIdRol();
                    idGrupoU = User.get(0).getIdGrupo();
                }
                //cambiar todos los combos de crear usuario
                pr.cboIdRol.setSelectedItem(rolDao.getListRol_ToString(idRolU));
                pr.cboGrupo.setSelectedItem(gymDao.getListGrupoToString(idGrupoU));
                pr.txtNombres.setText(pr.tbAdmin.getValueAt(fila, 2).toString());
                pr.txtApellidos.setText(pr.tbAdmin.getValueAt(fila, 3).toString());
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
                pr.btnRegistrar.setText("Guardar");
            }
        }
        if (e.getSource() == pr.mnuDeleteAdmin) {
            int fila = pr.tbAdmin.getSelectedRow();
            if (fila >= 0) {
                int response = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro ?", "Aviso..!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    boolean rptaDelete = admDao.deleteUser(pr.tbAdmin.getValueAt(fila, 1).toString());
                    if (rptaDelete) {
                        JOptionPane.showMessageDialog(null, "Registro eliminado con éxito");
                        cargarAdmin(pr.tbAdmin, dato, rol);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar el registro..");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }

        if (e.getSource() == pr.btnAdelante) {
            try {
                pagina = pagina + 1;
                cargarAdmin(pr.tbAdmin, dato, rol);
                if (pagina >= admDao.totalPaginas(dato)) {
                    pr.btnAdelante.setEnabled(false);
                    pr.btnUltimo.setEnabled(false);
                }
                if (pagina > 1) {
                    pr.btnAtras.setEnabled(true);
                    pr.btnPrimero.setEnabled(true);
                }
//            System.out.println("pagina = " + pagina + "------- total paginas = " + admDao.cantidadRegistros());
            } catch (SQLException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (e.getSource() == pr.btnAtras) {
            try {
                pagina = pagina - 1;
                cargarAdmin(pr.tbAdmin, dato, rol);
                if (pagina <= 1) {
                    pr.btnAtras.setEnabled(false);
                    pr.btnPrimero.setEnabled(false);
                }
                if (pagina < admDao.totalPaginas(dato)) {
                    pr.btnAdelante.setEnabled(true);
                    pr.btnUltimo.setEnabled(true);
                }
//            System.out.println("pagina = " + pagina + "------- total paginas = " + admDao.cantidadRegistros());
            } catch (SQLException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == pr.btnUltimo) {
            try {
                pagina = admDao.totalPaginas(dato);
                cargarAdmin(pr.tbAdmin, dato, rol);
                if (pagina >= admDao.totalPaginas(dato)) {
                    pr.btnAdelante.setEnabled(false);
                    pr.btnUltimo.setEnabled(false);
                }
                if (pagina > 1) {
                    pr.btnAtras.setEnabled(true);
                    pr.btnPrimero.setEnabled(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == pr.btnPrimero) {
            try {
                pagina = 1;
                cargarAdmin(pr.tbAdmin, dato, rol);
                if (pagina <= 1) {
                    pr.btnAtras.setEnabled(false);
                    pr.btnPrimero.setEnabled(false);
                }
                if (pagina < admDao.totalPaginas(dato)) {
                    pr.btnAdelante.setEnabled(true);
                    pr.btnUltimo.setEnabled(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

//        if (e.getSource() == pr.btnFotoAdmin) {
//            if (pr.txtDoc.getText().equals("")) {
//                JOptionPane.showMessageDialog(null, "El campo documento no debe estar vacio..!");
//                pr.txtDoc.requestFocus();
//            } else {
//                foto = "src/ImagenPerfilTmp/" + pr.txtDoc.getText() + ".png";
//                WebcamViewerExample wc = new WebcamViewerExample();
//                wc.setId(pr.txtDoc.getText());
//                wc.run();
//            }
//
//        }
//        if (e.getSource() == pr.btnAdjuntarfoto) {
//            countAction++;
//            File archivo;
//            if (countAction == 1) {
//                addFilter();
//            }
//            FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//            if (FileChooser.showDialog(null, "Seleccionar Archivo") == JFileChooser.APPROVE_OPTION) {
//                archivo = FileChooser.getSelectedFile();
//                if (archivo.getName().endsWith("png") || archivo.getName().endsWith("jpg")) {
//                    foto = String.valueOf(archivo);
//                    String NombreArchivo = FileChooser.getName(archivo);
//                    JOptionPane.showMessageDialog(null, "Archivo Seleccionado: " + String.valueOf(NombreArchivo));
//                } else {
//                    JOptionPane.showMessageDialog(null, "Elija un formato valido");
//                }
//            }
//        }
        if (e.getSource() == pr.createUsers) {
//            pagina = 1;
//            this.pr.pnCreateAdmin.setVisible(true);
//            this.pr.pnListAdmin.setVisible(false);
//            this.pr.pnRutinas.setVisible(false);
//            this.pr.pnCreateRutinas.setVisible(false);
//            this.pr.pnPagos.setVisible(false);
//            cargarAdmin(pr.tbAdmin, dato, rol);
//            cargarCboGym();
//            cargarRol();
        }

        if (e.getSource() == pr.rdoClientes) {
            if (pr.rdoClientes.isSelected()) {

                this.pr.lblVerUsers.setText("Ver Profesores");
                rol = 1;
                cargarAdmin(pr.tbAdmin, dato, rol);
                pagina = 1;
            }
        }

        if (e.getSource() == pr.rdoAdmin) {
            if (pr.rdoAdmin.isSelected()) {

                this.pr.lblVerUsers.setText("Ver Alumnos");
                rol = 2;
                cargarAdmin(pr.tbAdmin, dato, rol);
                pagina = 1;
            }
        }
        if (e.getSource() == pr.rdoTodos) {
            if (pr.rdoTodos.isSelected()) {

                this.pr.lblVerUsers.setText("Ver Todos");
                rol = 0;
                cargarAdmin(pr.tbAdmin, dato, rol);
                pagina = 1;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == pr.txtDoc) {
            if (!isNumeric(pr.txtDoc.getText())) {
                pr.txtDoc.setText("");
            }
        }

        if (e.getSource() == pr.txtBuscarAdmin) {
            try {
                dato = pr.txtBuscarAdmin.getText();
                if (dato.equals("")) {
                    pagina = 1;
                }
                cargarAdmin(pr.tbAdmin, dato, rol);

                if (admDao.totalPaginas(dato) <= 1) {
                    pr.btnAdelante.setEnabled(false);
                    pr.btnUltimo.setEnabled(false);
                    pr.btnAtras.setEnabled(false);
                    pr.btnPrimero.setEnabled(false);
                }
                if (admDao.totalPaginas(dato) > 1) {
                    pr.btnAdelante.setEnabled(true);
                    pr.btnUltimo.setEnabled(true);
                    pagina = 1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == pr.txtDoc) {
            if (pr.txtDoc.getText().length() == 10) {
                e.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    private void addFilter() {
        FileChooser.setFileFilter(new FileNameExtensionFilter("Imagen (*.PNG)", "png"));
        FileChooser.setFileFilter(new FileNameExtensionFilter("Imagen (*.JPG)", "jpg"));
    }

    private void limpiarForm() {
        pr.cboTipoDocAdmin.setSelectedItem("-- Seleccione --");
        pr.cboGrupo.setSelectedItem("-- Seleccione --");
        pr.cboIdRol.setSelectedItem("-- Seleccione --");
        pr.txtDoc.setText("");
        pr.txtNombres.setText("");
        pr.txtApellidos.setText("");
        pr.txtPass.setText("");
        pr.btnRegistrar.setText("Guardar");

    }

    public static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
