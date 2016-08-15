package Controllers;

import App.Login;
import App.Principal;
import Model.UsersDAO;
import Model.GrupoDAO;
import Model.Users;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;

public final class LoginController implements ActionListener, KeyListener {

    int pagina = 1;
    Login lg;
    Principal pr;
    UsersDAO admDao;
    GrupoDAO gymdao = new GrupoDAO();
    UsersController administradorController;
    AsignaturaController ac;
    ArrayList<Users> userArray = new ArrayList<>();
    boolean existe = false;
    int idUserLog;
    int rol;
    int idGrupo;
    String Nombre;
    String Apellido;

    public LoginController(Login lg, UsersDAO admDao, Principal pr) {
        this.lg = lg;
        this.admDao = admDao;
        this.administradorController = null;
        this.pr = pr;
        this.lg.btnIngresar.addActionListener(this);
        this.pr.btnLogout.addActionListener(this);
        this.lg.btnSalir.addActionListener(this);
        this.lg.txtUser.addActionListener(this);
        this.lg.txtUser.addKeyListener(this);
        this.lg.txtPass.addKeyListener(this);
        this.ac = null;
    }

    public void ocultarCapas(int rol) {
        if (rol == 1) {
            pr.pnCreateAdmin.setVisible(false);
            pr.pnCuestionario.setVisible(false);
            pr.createUsers.setVisible(false);
            pr.pnEditCuestionary.setVisible(false);
            pr.pnCreateCuestionary.setVisible(true);            
        }
        if (rol == 2) {
            pr.mnuAdministrar.setText("Menú");
            pr.pnCuestionario.setVisible(true);
            pr.pnCreateAdmin.setVisible(false);
            pr.pnCreateCuestionary.setVisible(false);
            pr.mnuAdministrar.setVisible(true);
            pr.createUsers.setVisible(false);
            pr.MnuCuestionarios.setVisible(false);
            pr.asignCuestionaryToGroup.setVisible(false);
            pr.pnPregunta.setVisible(false);
            pr.pnfinishCuestionario.setVisible(false);
        }

        if (rol == 3) {
            pr.pnCreateAdmin.setVisible(true);
            pr.pnCuestionario.setVisible(false);
            pr.pnCreateCuestionary.setVisible(false);
            pr.MnuCuestionarios.setVisible(false);
            pr.asignCuestionaryToGroup.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lg.txtUser) {
            lg.txtUser.transferFocus();
        }
        if (e.getSource() == lg.btnIngresar) {
            String user = lg.txtUser.getText();
            String pass = new String(lg.txtPass.getPassword());
            userArray = admDao.getExistAdmin(user, pass);
            Iterator<Users> u = userArray.iterator();
            while (u.hasNext()) {
                Users us = u.next();
                rol = us.getIdRol();
                idUserLog = us.getIdUser();
                idGrupo = us.getIdGrupo();
                Nombre = us.getNombres();
                Apellido = us.getApellidos();
                existe = true;
            }
            if (existe) {
                lg.dispose();
                lg.txtUser.setText("");
                lg.txtPass.setText("");
                System.out.println("rol " + rol);
                ocultarCapas(rol);
                //administradorController = new UsersController(pr, admDao, rol, idUserLog, idGrupo);
                CuestionarioController cc = new CuestionarioController(pr, idGrupo, idUserLog, rol);
                ac = new AsignaturaController(pr, idUserLog); 
                ac.cargarCboAsignaturas(); 
                enabledBtnPaginator();
                if (rol == 2) {
                    pr.txtAlumnoName.setText(Nombre + " " + Apellido);
                }
                pr.setLocationRelativeTo(null);
                pr.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "Usuario incorrecto");
            }
        }
        if (e.getSource() == lg.btnSalir) {
            System.exit(0);
        }
        if (e.getSource() == pr.btnLogout) {
            int response = JOptionPane.showConfirmDialog(null, "Esta seguro de cerrar la sesion ?", "Aviso..!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                pr.txtAlumnoName.setText("");
                pr.dispose();
                administradorController = null;
                pr = null;
                LoginController lgc = new LoginController(lg = new Login(), admDao = new UsersDAO(), pr = new Principal());
                lg.setVisible(true);
                lg.setLocationRelativeTo(null);
            }
        }
    }

    private void enabledBtnPaginator() {
        if (pagina >= admDao.totalPaginas("")) {
            pr.btnAdelante.setEnabled(false);
            pr.btnUltimo.setEnabled(false);
        }
        if (pagina == 1) {
            pr.btnAtras.setEnabled(false);
            pr.btnPrimero.setEnabled(false);
        }

//        if (pagina >= gymdao.totalPaginas("")) {
////            pr.btnSiguienteGym.setEnabled(false);
////            pr.btnUltimoGym.setEnabled(false);
//        }
//        if (pagina == 1) {
////            pr.btnAtrasGym.setEnabled(false);
////            pr.btnPrimerGym.setEnabled(false);
//        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == lg.txtPass) {
            char tecla = e.getKeyChar();
            if (tecla == KeyEvent.VK_ENTER) {
                if (!lg.txtUser.getText().equals("") && !lg.txtPass.getPassword().equals("")) {
                    lg.btnIngresar.doClick();
                }
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
