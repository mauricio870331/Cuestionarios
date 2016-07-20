/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author clopez
 */
public class CCuestionarioAlumno {
    private int idCAlumno;
    private int idAlumno;
    private int idCuestionario;
    private int repetir;
    private String finalizacion;

    public CCuestionarioAlumno() {
        this.idCAlumno = 0;
        this.idAlumno = 0;
        this.idCuestionario = 0;
        this.repetir = 0;
        this.finalizacion = "";
    }

    public int getIdCAlumno() {
        return idCAlumno;
    }

    public void setIdCAlumno(int idCAlumno) {
        this.idCAlumno = idCAlumno;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(int idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public int getRepetir() {
        return repetir;
    }

    public void setRepetir(int repetir) {
        this.repetir = repetir;
    }

    public String getFinalizacion() {
        return finalizacion;
    }

    public void setFinalizacion(String finalizacion) {
        this.finalizacion = finalizacion;
    }
    
    
    
}
