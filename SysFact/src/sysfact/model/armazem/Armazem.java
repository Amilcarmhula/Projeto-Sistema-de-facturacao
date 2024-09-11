/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.armazem;

import sysfact.model.endereco.Endereco;


/**
 *
 * @author JM-Tecnologias
 */
public class Armazem extends Endereco{
    private int id;
    private String nome;
    private boolean active_or_not;
    private String data_criacao;
    
    public Armazem(){}

    public Armazem(String nome, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
        this.nome = nome;
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

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    
}
