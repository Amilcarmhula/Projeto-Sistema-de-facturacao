/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class TaxaImposto {

    private int id;
    private String identificador;
    private double percentagem;
    private String descricao;
    private boolean estadoActive;
    
    public TaxaImposto(){}

    public TaxaImposto(int id, String identificador, double percentagem, String descricao, boolean estadoActive) {
        this.id = id;
        this.identificador = identificador;
        this.percentagem = percentagem;
        this.descricao = descricao;
        this.estadoActive = estadoActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public double getPercentagem() {
        return percentagem;
    }

    public void setPercentagem(double percentagem) {
        this.percentagem = percentagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isEstadoActive() {
        return estadoActive;
    }

    public void setEstadoActive(boolean estadoActive) {
        this.estadoActive = estadoActive;
    }
    
}
