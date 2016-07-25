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
public class Resultados {

    private int id_resultado;
    private int id_cuestionario;
    private int id_grupo;
    private int id_user;
    private double nota;
    private String tiempo;
    private boolean aprobo;
    private String fecha_presentacion;

    public Resultados() {
        this.id_resultado = 0;
        this.id_cuestionario = 0;
        this.id_grupo = 0;
        this.id_user = 0;
        this.nota = 0.0;
        this.tiempo = "";
        this.aprobo = false;
        this.fecha_presentacion = "";
    }

    public String getFecha_presentacion() {
        return fecha_presentacion;
    }

    public void setFecha_presentacion(String fecha_presentacion) {
        this.fecha_presentacion = fecha_presentacion;
    }

    public int getId_resultado() {
        return id_resultado;
    }

    public void setId_resultado(int id_resultado) {
        this.id_resultado = id_resultado;
    }

    public int getId_cuestionario() {
        return id_cuestionario;
    }

    public void setId_cuestionario(int id_cuestionario) {
        this.id_cuestionario = id_cuestionario;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public boolean isAprobo() {
        return aprobo;
    }

    public void setAprobo(boolean aprobo) {
        this.aprobo = aprobo;
    }
}
