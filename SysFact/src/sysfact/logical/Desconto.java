/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class Desconto {
    private int id;
    private String nome;
    private double percentagem;
    private double valorFixo;
    private String descricao;
    private boolean estadoActive;
    
    public Desconto(){}

    public Desconto(int id, String nome, double percentagem, double valorFixo, String descricao, boolean estadoActive) {
        this.id = id;
        this.nome = nome;
        this.percentagem = percentagem;
        this.valorFixo = valorFixo;
        this.descricao = descricao;
        this.estadoActive = estadoActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPercentagem() {
        return percentagem;
    }

    public void setPercentagem(double percentagem) {
        this.percentagem = percentagem;
    }

    public double getValorFixo() {
        return valorFixo;
    }

    public void setValorFixo(double valorFixo) {
        this.valorFixo = valorFixo;
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
