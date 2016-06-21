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
public class RespuestasCuestionario {
 private int idRespuesta;
 private int idPregunta;
 private String respuesta;
 private boolean estado;
 private int idfk;

    public RespuestasCuestionario() {
        this.idRespuesta = 0;
        this.idPregunta = 0;
        this.respuesta = "";
        this.estado = false;
        this.idfk=0;
        
    }

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getIdfk() {
        return idfk;
    }

    public void setIdfk(int idfk) {
        this.idfk = idfk;
    }
    
 
 
}
