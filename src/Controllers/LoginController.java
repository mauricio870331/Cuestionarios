/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import App.Login;
import App.Principal;
import Model.UsersDAO;
import Model.GrupoDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

public final class LoginController implements ActionListener, KeyListener {

    int pagina = 1;
    Login lg;
    Principal pr;
    UsersDAO admDao;
    GrupoDAO gymdao = new GrupoDAO();
    UsersController administradorController;
    AsignaturaController ac;

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
            pr.pnCreateAdmin.setVisible(true);
            pr.pnCuestionario.setVisible(false);
            pr.pnCreateCuestionary.setVisible(false);
        }
        if (rol == 2) {
            pr.pnCuestionario.setVisible(true);
            pr.pnCreateAdmin.setVisible(false);
            pr.pnCreateCuestionary.setVisible(false);
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
            if (admDao.getExistAdmin(user, pass).size() >= 1) {
                lg.dispose();
                lg.txtUser.setText("");
                lg.txtPass.setText("");
                int rol = admDao.getExistAdmin(user, pass).get(0).getIdRol();
                int idUserLog = admDao.getExistAdmin(user, pass).get(0).getIdUser();
                int idGrupo = admDao.getExistAdmin(user, pass).get(0).getIdGrupo();
                System.out.println("rol " + rol);
                ocultarCapas(rol);
                administradorController = new UsersController(pr, admDao, rol, idUserLog, idGrupo);
                administradorController.cargarAdmin(pr.tbAdmin, "", 0);
                administradorController.cargarCboGrupo();
                administradorController.cargarRol();
                CuestionarioController cc = new CuestionarioController(pr, idGrupo, idUserLog, rol);
                ac = new AsignaturaController(pr, idUserLog);
                ac.cargarCboAsignaturas();
//                    cc.cargarCuestionarioByGrupo(idGrupo);
                enabledBtnPaginator();
                pr.txtAlumnoName.setText(admDao.getExistAdmin(user, pass).get(0).getNombres() + " " + admDao.getExistAdmin(user, pass).get(0).getApellidos());
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
