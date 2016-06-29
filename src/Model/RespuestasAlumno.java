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
public class RespuestasAlumno {
    private int idRespuestaA;
    private int idPregunta;
    private int idRespuesta;
    private int idCuestionarioAlumno;
    private boolean tempEstado;

    public RespuestasAlumno() {
        this.idRespuestaA = -1;
        this.idPregunta = -1;
        this.idRespuesta = -1;
        this.idCuestionarioAlumno = 0;
        this.tempEstado = false;
    }

    public int getIdRespuestaA() {
        return idRespuestaA;
    }

    public void setIdRespuestaA(int idRespuestaA) {
        this.idRespuestaA = idRespuestaA;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public int getIdCuestionarioAlumno() {
        return idCuestionarioAlumno;
    }

    public void setIdCuestionarioAlumno(int idCuestionarioAlumno) {
        this.idCuestionarioAlumno = idCuestionarioAlumno;
    }

    public boolean isTempEstado() {
        return tempEstado;
    }

    public void setTempEstado(boolean tempEstado) {
        this.tempEstado = tempEstado;
    }
    
}
