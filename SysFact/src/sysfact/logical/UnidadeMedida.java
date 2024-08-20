/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class UnidadeMedida {
    private int id;
    private String tipo;
    private String identificador;
    private String sigla;
    private boolean estadoActive;
    
    public UnidadeMedida(){}

    public UnidadeMedida(int id, String tipo, String identificador, String sigla, boolean estadoActive) {
        this.id = id;
        this.tipo = tipo;
        this.identificador = identificador;
        this.sigla = sigla;
        this.estadoActive = estadoActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public boolean isEstadoActive() {
        return estadoActive;
    }

    public void setEstadoActive(boolean estadoActive) {
        this.estadoActive = estadoActive;
    }
    
    
}
