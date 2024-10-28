/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.empresa.endereco;

import jminvsm.model.empresa.Empresa;
import jminvsm.model.endereco.Endereco;
import jminvsm.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoEmpresa {

    private int id;
    private String data_criacao;
    private Empresa empresa;
    private String provincia_emp;
    private String cidade_emp;
    private String bairro_emp;
    private String avenida_emp;
    private String rua_emp;
    private int codigoPostal_emp;
    private int numero_emp;
    private String tipo_emp;

    public EnderecoEmpresa() {
    }

    public EnderecoEmpresa(String data_criacao, Empresa empresa, String provincia_emp, String cidade_emp, String bairro_emp, String avenida_emp, String rua_emp, int codigoPostal_emp, int numero_emp, String tipo_emp) {
        this.data_criacao = data_criacao;
        this.empresa = empresa;
        this.provincia_emp = provincia_emp;
        this.cidade_emp = cidade_emp;
        this.bairro_emp = bairro_emp;
        this.avenida_emp = avenida_emp;
        this.rua_emp = rua_emp;
        this.codigoPostal_emp = codigoPostal_emp;
        this.numero_emp = numero_emp;
        this.tipo_emp = tipo_emp;
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getProvincia_emp() {
        return provincia_emp;
    }

    public void setProvincia_emp(String provincia_emp) {
        this.provincia_emp = provincia_emp;
    }

    public String getCidade_emp() {
        return cidade_emp;
    }

    public void setCidade_emp(String cidade_emp) {
        this.cidade_emp = cidade_emp;
    }

    public String getBairro_emp() {
        return bairro_emp;
    }

    public void setBairro_emp(String bairro_emp) {
        this.bairro_emp = bairro_emp;
    }

    public String getAvenida_emp() {
        return avenida_emp;
    }

    public void setAvenida_emp(String avenida_emp) {
        this.avenida_emp = avenida_emp;
    }

    public String getRua_emp() {
        return rua_emp;
    }

    public void setRua_emp(String rua_emp) {
        this.rua_emp = rua_emp;
    }

    public int getCodigoPostal_emp() {
        return codigoPostal_emp;
    }

    public void setCodigoPostal_emp(int codigoPostal_emp) {
        this.codigoPostal_emp = codigoPostal_emp;
    }

    public int getNumero_emp() {
        return numero_emp;
    }

    public void setNumero_emp(int numero_emp) {
        this.numero_emp = numero_emp;
    }

    public String getTipo_emp() {
        return tipo_emp;
    }

    public void setTipo_emp(String tipo_emp) {
        this.tipo_emp = tipo_emp;
    }

    
}
