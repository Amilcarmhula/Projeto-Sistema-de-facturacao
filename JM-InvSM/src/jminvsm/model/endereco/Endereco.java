/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.endereco;


/**
 *
 * @author JM-Tecnologias
 */
public class Endereco {
    private String provincia;
    private String cidade;
    private String bairro;
    private String avenida;
    private String rua;
    private int codigoPostal;
    private int numero;
    private String tipo;
    
    public Endereco(){}

    public Endereco( String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        this.provincia = provincia;
        this.cidade = cidade;
        this.bairro = bairro;
        this.avenida = avenida;
        this.rua = rua;
        this.codigoPostal = codigoPostal;
        this.numero = numero;
        this.tipo = tipo;
    }

    public String getProvincia_emp() {
        return provincia;
    }

    public void setProvincia_emp(String provincia) {
        this.provincia = provincia;
    }

    public String getCidade_emp() {
        return cidade;
    }

    public void setCidade_emp(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro_emp() {
        return bairro;
    }

    public void setBairro_emp(String bairro) {
        this.bairro = bairro;
    }

    public String getAvenida_emp() {
        return avenida;
    }

    public void setAvenida_emp(String avenida) {
        this.avenida = avenida;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
