/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.reports.vendas.facturas;

/**
 *
 * @author JM-Tecnologias
 */
public class ClienteData {

    private String apelido;
    private int nuit;
    private String contacto;
    private String email;
    private String endereco;
    private String numero;
    private String data_criacao;
    private String data_vencimento;
    private String tipo;

    public ClienteData() {
    }

    public ClienteData(String apelido, int nuit, String contacto, String email, String endereco, String numero, String data_criacao, String data_vencimento, String tipo
    ) {
        this.apelido = apelido;
        this.nuit = nuit;
        this.contacto = contacto;
        this.email = email;
        this.endereco = endereco;
        this.numero = numero;
        this.data_criacao = data_criacao;
        this.data_vencimento = data_vencimento;
        this.tipo = tipo;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public int getNuit() {
        return nuit;
    }

    public void setNuit(int nuit) {
        this.nuit = nuit;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getData_vencimento() {
        return data_vencimento;
    }

    public void setData_vencimento(String data_vencimento) {
        this.data_vencimento = data_vencimento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
