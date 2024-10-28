/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.armazem.endereco;

import jminvsm.model.armazem.Armazem;
import jminvsm.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoArmazem{

    private int id;
    private String data_criacao;
    private Armazem armazem;
    private Usuario usuario;
    private String provincia_arm;
    private String cidade_arm;
    private String bairro_arm;
    private String avenida_arm;
    private String rua_arm;
    private int codigoPostal_arm;
    private int numero_arm;

    public EnderecoArmazem() {
    }

    public EnderecoArmazem(Armazem armazem, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero) {
        this.armazem = armazem;
        this.provincia_arm = provincia;
        this.cidade_arm = cidade;
        this.bairro_arm = bairro;
        this.avenida_arm = avenida;
        this.rua_arm = rua;
        this.codigoPostal_arm = codigoPostal;
        this.numero_arm = numero;
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

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getProvincia_arm() {
        return provincia_arm;
    }

    public void setProvincia_arm(String provincia_arm) {
        this.provincia_arm = provincia_arm;
    }

    public String getCidade_arm() {
        return cidade_arm;
    }

    public void setCidade_arm(String cidade_arm) {
        this.cidade_arm = cidade_arm;
    }

    public String getBairro_arm() {
        return bairro_arm;
    }

    public void setBairro_arm(String bairro_arm) {
        this.bairro_arm = bairro_arm;
    }

    public String getAvenida_arm() {
        return avenida_arm;
    }

    public void setAvenida_arm(String avenida_arm) {
        this.avenida_arm = avenida_arm;
    }

    public String getRua_arm() {
        return rua_arm;
    }

    public void setRua_arm(String rua_arm) {
        this.rua_arm = rua_arm;
    }

    public int getCodigoPostal_arm() {
        return codigoPostal_arm;
    }

    public void setCodigoPostal_arm(int codigoPostal_arm) {
        this.codigoPostal_arm = codigoPostal_arm;
    }

    public int getNumero_arm() {
        return numero_arm;
    }

    public void setNumero_arm(int numero_arm) {
        this.numero_arm = numero_arm;
    }

    
}
