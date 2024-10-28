/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.cliente.endereco;

import jminvsm.model.cliente.Cliente;
import jminvsm.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoCliente  {

    private int id;
    private String provincia_cli;
    private String cidade_cli;
    private String bairro_cli;
    private String avenida_cli;
    private String rua_cli;
    private int codigoPostal_cli;
    private int numero_cli;
    private String tipo_cli;
    private String data_criacao;
    private Cliente cliente;
    private Usuario usuario;

    public EnderecoCliente() {
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvincia_cli() {
        return provincia_cli;
    }

    public void setProvincia_cli(String provincia_cli) {
        this.provincia_cli = provincia_cli;
    }

    public String getCidade_cli() {
        return cidade_cli;
    }

    public void setCidade_cli(String cidade_cli) {
        this.cidade_cli = cidade_cli;
    }

    public String getBairro_cli() {
        return bairro_cli;
    }

    public void setBairro_cli(String bairro_cli) {
        this.bairro_cli = bairro_cli;
    }

    public String getAvenida_cli() {
        return avenida_cli;
    }

    public void setAvenida_cli(String avenida_cli) {
        this.avenida_cli = avenida_cli;
    }

    public String getRua_cli() {
        return rua_cli;
    }

    public void setRua_cli(String rua_cli) {
        this.rua_cli = rua_cli;
    }

    public int getCodigoPostal_cli() {
        return codigoPostal_cli;
    }

    public void setCodigoPostal_cli(int codigoPostal_cli) {
        this.codigoPostal_cli = codigoPostal_cli;
    }

    public int getNumero_cli() {
        return numero_cli;
    }

    public void setNumero_cli(int numero_cli) {
        this.numero_cli = numero_cli;
    }

    public String getTipo_cli() {
        return tipo_cli;
    }

    public void setTipo_cli(String tipo_cli) {
        this.tipo_cli = tipo_cli;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    
}
