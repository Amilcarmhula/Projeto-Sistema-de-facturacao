/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.empresa;

import java.util.List;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.empresa.contacto.ContactoEmpresa;
import jminvsm.model.empresa.endereco.EnderecoEmpresa;

/**
 *
 * @author JM-Tecnologias
 */
public class Empresa {

    private int id;
    private String nome_emp;
    private String razao_emp;
    private int nuit_emp;
    private ContactoEmpresa contactoEmpresa;
    private List<ContactoEmpresa> contactosEmpresa;
    private EnderecoEmpresa enderecoEmpresa;
    private ContaBancaria contaBancaria;
    private String data_criacao;

    public Empresa() {
    }

    public Empresa(String nome, String apelido, int nuit, ContactoEmpresa contactoEmpresa, EnderecoEmpresa enderecoEmpresa, ContaBancaria contaBancaria) {
        this.nome_emp = nome;
        this.razao_emp = apelido;
        this.nuit_emp = nuit;
        this.contactoEmpresa = contactoEmpresa;
        this.enderecoEmpresa = enderecoEmpresa;
        this.contaBancaria = contaBancaria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_emp() {
        return nome_emp;
    }

    public void setNome_emp(String nome_emp) {
        this.nome_emp = nome_emp;
    }

    public String getRazao_emp() {
        return razao_emp;
    }

    public void setRazao_emp(String razao_emp) {
        this.razao_emp = razao_emp;
    }

    public int getNuit_emp() {
        return nuit_emp;
    }

    public void setNuit_emp(int nuit_emp) {
        this.nuit_emp = nuit_emp;
    }

    public ContactoEmpresa getContactoEmpresa() {
        return contactoEmpresa;
    }

    public void setContactoEmpresa(ContactoEmpresa contactoEmpresa) {
        this.contactoEmpresa = contactoEmpresa;
    }

    public List<ContactoEmpresa> getContactosEmpresa() {
        return contactosEmpresa;
    }

    public void setContactosEmpresa(List<ContactoEmpresa> contactosEmpresa) {
        this.contactosEmpresa = contactosEmpresa;
    }

    public String printContatos() {
        String contacto = "";
        if (contactoEmpresa != null) {
            for (ContactoEmpresa c : contactosEmpresa) {
                contacto += c.getContacto_emp() + "; ";
            }
        }
        return contacto;
    }

    public EnderecoEmpresa getEnderecoEmpresa() {
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(EnderecoEmpresa enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public String printEnderecos() {
        String endereco = "";
        if (enderecoEmpresa != null) {
            endereco = enderecoEmpresa.getAvenida_emp() + ", " + enderecoEmpresa.getRua_emp()
                    + ", " + enderecoEmpresa.getNumero_emp() + "; " + enderecoEmpresa.getBairro_emp()
                    + ", " + enderecoEmpresa.getCidade_emp() + ", " + enderecoEmpresa.getProvincia_emp()
                    + "-" + enderecoEmpresa.getCodigoPostal_emp();
        }
        return endereco;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

}
