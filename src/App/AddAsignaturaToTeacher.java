/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

/**
 *
 * @author Mauricio Herrera
 */
public class AddAsignaturaToTeacher extends javax.swing.JDialog {

    /**
     * Creates new form AddAutor
     */
    public AddAsignaturaToTeacher(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupAsignatura = new javax.swing.JPopupMenu();
        mnuUpdateAsignatura = new javax.swing.JMenuItem();
        mnuDeleteAsignatura = new javax.swing.JMenuItem();
        mnuAsocAsignaturaTeach = new javax.swing.JMenuItem();
        popupAsignaturaToTeacher = new javax.swing.JPopupMenu();
        deleteAsignaturaToTeach = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        btnCreateAsignatura = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbAsignaturaDocente = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbAsignatura = new javax.swing.JTable();
        btnCancelaAsignatura = new javax.swing.JButton();
        txtNomAsignatura = new javax.swing.JTextField();

        mnuUpdateAsignatura.setText("Actualizar");
        popupAsignatura.add(mnuUpdateAsignatura);

        mnuDeleteAsignatura.setText("Eliminar");
        popupAsignatura.add(mnuDeleteAsignatura);

        mnuAsocAsignaturaTeach.setText("Asociar Asignatura Docente");
        popupAsignatura.add(mnuAsocAsignaturaTeach);

        deleteAsignaturaToTeach.setText("Quitar Asignatura");
        popupAsignaturaToTeacher.add(deleteAsignaturaToTeach);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Asignaturas Docente");

        jLabel1.setText("Asignatura");

        btnCreateAsignatura.setText("Guardar");

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Asignaturas Asociadas Al Docente", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        tbAsignaturaDocente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbAsignaturaDocente.setComponentPopupMenu(popupAsignaturaToTeacher);
        jScrollPane2.setViewportView(tbAsignaturaDocente);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Asignaturas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        tbAsignatura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbAsignatura.setComponentPopupMenu(popupAsignatura);
        jScrollPane1.setViewportView(tbAsignatura);

        btnCancelaAsignatura.setText("Cancelar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNomAsignatura, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCreateAsignatura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelaAsignatura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnCreateAsignatura)
                    .addComponent(txtNomAsignatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelaAsignatura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddAsignaturaToTeacher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddAsignaturaToTeacher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddAsignaturaToTeacher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddAsignaturaToTeacher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddAsignaturaToTeacher dialog = new AddAsignaturaToTeacher(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancelaAsignatura;
    public javax.swing.JButton btnCreateAsignatura;
    public javax.swing.JMenuItem deleteAsignaturaToTeach;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JMenuItem mnuAsocAsignaturaTeach;
    public javax.swing.JMenuItem mnuDeleteAsignatura;
    public javax.swing.JMenuItem mnuUpdateAsignatura;
    public javax.swing.JPopupMenu popupAsignatura;
    public javax.swing.JPopupMenu popupAsignaturaToTeacher;
    public javax.swing.JTable tbAsignatura;
    public javax.swing.JTable tbAsignaturaDocente;
    public javax.swing.JTextField txtNomAsignatura;
    // End of variables declaration//GEN-END:variables
}
