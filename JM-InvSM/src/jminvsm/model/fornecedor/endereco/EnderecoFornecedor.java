/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.fornecedor.endereco;

import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoFornecedor{

    private int id; 
    private String data_criacao;
    private Fornecedor fornecedor;
    private Usuario usuario;
    private String provincia_forn;
    private String cidade_forn;
    private String bairro_forn;
    private String avenida_forn;
    private String rua_forn;
    private int codigoPostal_forn;
    private int numero_forn;

    public EnderecoFornecedor() {
    }

    public EnderecoFornecedor(Fornecedor fornecedor, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero) {
        this.fornecedor = fornecedor;
        this.provincia_forn = provincia;
        this.cidade_forn = cidade;
        this.bairro_forn = bairro;
        this.avenida_forn = avenida;
        this.rua_forn = rua;
        this.codigoPostal_forn = codigoPostal;
        this.numero_forn = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getProvincia_forn() {
        return provincia_forn;
    }

    public void setProvincia_forn(String provincia_forn) {
        this.provincia_forn = provincia_forn;
    }

    public String getCidade_forn() {
        return cidade_forn;
    }

    public void setCidade_forn(String cidade_forn) {
        this.cidade_forn = cidade_forn;
    }

    public String getBairro_forn() {
        return bairro_forn;
    }

    public void setBairro_forn(String bairro_forn) {
        this.bairro_forn = bairro_forn;
    }

    public String getAvenida_forn() {
        return avenida_forn;
    }

    public void setAvenida_forn(String avenida_forn) {
        this.avenida_forn = avenida_forn;
    }

    public String getRua_forn() {
        return rua_forn;
    }

    public void setRua_forn(String rua_forn) {
        this.rua_forn = rua_forn;
    }

    public int getCodigoPostal_forn() {
        return codigoPostal_forn;
    }

    public void setCodigoPostal_forn(int codigoPostal_forn) {
        this.codigoPostal_forn = codigoPostal_forn;
    }

    public int getNumero_forn() {
        return numero_forn;
    }

    public void setNumero_forn(int numero_forn) {
        this.numero_forn = numero_forn;
    } 
    
}
