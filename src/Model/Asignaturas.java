/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Mauricio Herrera
 */
public class Asignaturas {
    private int asignatura;
    private String nombreAsignatura;

    public Asignaturas() {
        this.asignatura = 0;
        this.nombreAsignatura = "";
    }

    public int getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(int isAsignatura) {
        this.asignatura = isAsignatura;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }
    
    
}
