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
    private String data_criacao;
    private boolean active_or_not;
    
    public UnidadeMedida(){}

    public UnidadeMedida(int id, String tipo, String identificador, String sigla, String data_criacao, boolean active_or_not) {
        this.id = id;
        this.tipo = tipo;
        this.identificador = identificador;
        this.sigla = sigla;
        this.data_criacao = data_criacao;
        this.active_or_not = active_or_not;
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

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

    
}
