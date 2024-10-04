/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.empresa.conta;

import sysfact.model.empresa.Empresa;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ContaBancaria {

    private int id;
    private String nome_banco;
    private String numero;
    private String nib;
    private String nuib;
    private Empresa empresa;
    private String data_criacao;

    public ContaBancaria() {
    }

    public ContaBancaria(String nome_banco, String numero, String nib, String nuib, Empresa empresa) {
        this.nome_banco = nome_banco;
        this.numero = numero;
        this.nib = nib;
        this.nuib = nuib;
        this.empresa = empresa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_banco() {
        return nome_banco;
    }

    public void setNome_banco(String nome_banco) {
        this.nome_banco = nome_banco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNib() {
        return nib;
    }

    public void setNib(String nib) {
        this.nib = nib;
    }

    public String getNuib() {
        return nuib;
    }

    public void setNuib(String nuib) {
        this.nuib = nuib;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }
    
    
    
}
