/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.taxa_imposto;


/**
 *
 * @author JM-Tecnologias
 */
public class TaxaImposto {

    private int id;
    private String identificador;
    private double percentagem;
    private String descricao;
    private String data_criacao;
    private boolean active_or_not;
    
    public TaxaImposto(){}

    public TaxaImposto(int id, String identificador, double percentagem, String descricao, String data_criacao, boolean active_or_not) {
        this.id = id;
        this.identificador = identificador;
        this.percentagem = percentagem;
        this.descricao = descricao;
        this.data_criacao = data_criacao;
        this.active_or_not = active_or_not;
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
