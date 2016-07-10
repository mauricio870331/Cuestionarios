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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
        this.pr.mnuAddPago.addActionListener(this);
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
                pr.cboGrupo.addItem(elemento.getIdGrupo() + " - " + elemento.getGrupo());
            }
        }

    }

    public void cargarRol() {
        pr.cboIdRol.removeAllItems();
        Iterator<Roles> rol = rolDao.getListRol().iterator();
        pr.cboIdRol.addItem("-- Seleccione --");
        while (rol.hasNext()) {
            Roles elemento = rol.next();
            pr.cboIdRol.addItem(elemento.getIdRol() + " - " + elemento.getRol());
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
            //String Fechacompleta = FchaHoy + " " + hora + ":" + minutos + ":" + segundos;
//            String Horacomp = hora + ":" + minutos + ":" + segundos;
            String tipo_doc = (String) pr.cboTipoDocAdmin.getSelectedItem();
            String idGym = (String) pr.cboGrupo.getSelectedItem();
            int grupo = 0;
            if (!idGym.equals("-- Seleccione --")) {
                String[] idGymSeparated = idGym.split("-");
                grupo = Integer.parseInt(idGymSeparated[0].trim());
            }
            String idRol = (String) pr.cboIdRol.getSelectedItem();
            int rolU = 0;
            if (!idRol.equals("-- Seleccione --")) {
                String[] idRolSeparated = idRol.split("-");
                rolU = Integer.parseInt(idRolSeparated[0].trim());
            }
            String documento = pr.txtDoc.getText();
            String nombres = pr.txtNombres.getText();
            String apellidos = pr.txtApellidos.getText();
            String pass = new String(pr.txtPass.getPassword());
            if (tipo_doc.equals("-- Seleccione --")) {
                JOptionPane.showMessageDialog(null, "debe seleccionar un tipo de documento");
                return;
            }
            String rptaRegistro = admDao.Create(documento, tipo_doc, nombres, apellidos, grupo, rolU, pass, foto, opc, idToUpdate);
            if (rptaRegistro != null) {
                JOptionPane.showMessageDialog(null, rptaRegistro);
                cargarAdmin(pr.tbAdmin, dato, 0);
//                if (opc.equals("C")) {
//                    this.pr.pnCreateAdmin.setVisible(false);
//                }
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
                String tipoDoc = pr.tbAdmin.getValueAt(fila, 0).toString();
                pr.cboTipoDocAdmin.setSelectedItem(tipoDoc);
                String documento = pr.tbAdmin.getValueAt(fila, 1).toString();
                pr.txtDoc.setText(documento);
                if (admDao.getIdToUpdate(documento).size() > 0) {
                    pr.txtPass.setText(admDao.getIdToUpdate(documento).get(0).getPassword());
                }
                idToUpdate = admDao.getIdToUpdate(documento).get(0).getIdUser();
                int idRol = admDao.getIdToUpdate(documento).get(0).getIdRol();
                int idGrupo = admDao.getIdToUpdate(documento).get(0).getIdGrupo();
                pr.cboIdRol.setSelectedItem(Integer.toString(rolDao.getListRolToString(idRol).get(0).getIdRol()) + " - " + rolDao.getListRolToString(idRol).get(0).getRol());
                pr.cboGrupo.setSelectedItem(Integer.toString(gymDao.getListGrupoToString(idGrupo).get(0).getIdGrupo()) + " - " + gymDao.getListGrupoToString(idGrupo).get(0).getGrupo());
                pr.txtNombres.setText(pr.tbAdmin.getValueAt(fila, 2).toString());
                pr.txtApellidos.setText(pr.tbAdmin.getValueAt(fila, 3).toString());

            } else {
                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
            }
        }
        if (e.getSource() == pr.mnuDeleteAdmin) {
//            int fila = pr.tbAdmin.getSelectedRow();
//            if (fila >= 0) {
//                int response = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro ?", "Aviso..!",
//                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                if (response == JOptionPane.YES_OPTION) {
//                    String rptaDelete = admDao.deleteAdmin(admDao.getIdToUpdate(pr.tbAdmin.getValueAt(fila, 1).toString()).get(0).getId());
//                    if (rptaDelete != null) {
//                        JOptionPane.showMessageDialog(null, rptaDelete);
//                        cargarAdmin(pr.tbAdmin, dato, rol);
//                    }
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "No has seleccionado un registro..!");
//            }
        }

        if (e.getSource() == pr.btnAdelante) {
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

        }
        if (e.getSource() == pr.btnAtras) {
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
        }
        if (e.getSource() == pr.btnUltimo) {
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
        }
        if (e.getSource() == pr.btnPrimero) {
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
        if (e.getSource() == pr.txtBuscarAdmin) {
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
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

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

    }

}
