/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.desconto;

import java.math.BigDecimal;
import java.time.LocalDate;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;

/**
 *
 * @author JM-Tecnologias
 */
public class Desconto {
    private int id;
    private String nome;
    private BigDecimal percentagem = BigDecimal.ZERO;
    private String descricao;
    private LocalDate data_criacao;
    
    public Desconto(){super();}

    public Desconto(int id, String nome, double percentagem, String descricao, String data_criacao) {
        super();
        this.id = id;
        this.nome = nome;
        this.percentagem = toBigDecimal(percentagem);
        this.descricao = descricao;
        this.data_criacao = toLocalDate(data_criacao);
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
        return toDouble(percentagem);
    }

    public void setPercentagem(double percentagem) {
        this.percentagem = toBigDecimal(percentagem);
    }

    public BigDecimal getPercentagemDecimal() {
        return percentagem;
    }

    public void setPercentagem(BigDecimal percentagem) {
        this.percentagem = percentagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData_criacao() {
        return toDateString(data_criacao);
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = toLocalDate(data_criacao);
    }

    public LocalDate getDataCriacao() {
        return data_criacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.data_criacao = dataCriacao;
    }   
}
