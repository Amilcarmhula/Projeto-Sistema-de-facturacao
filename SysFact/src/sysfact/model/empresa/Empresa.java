/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.empresa;

import java.util.List;
import sysfact.model.empresa.conta.ContaBancaria;
import sysfact.model.empresa.contacto.ContactoEmpresa;
import sysfact.model.empresa.endereco.EnderecoEmpresa;

/**
 *
 * @author JM-Tecnologias
 */
public class Empresa {

    private int id;
    private String nome;
    private String apelido;
    private int nuit;
    private ContactoEmpresa contactoEmpresa;
    private List<ContactoEmpresa> contactosEmpresa;
    private EnderecoEmpresa enderecoEmpresa;
    private ContaBancaria contaBancaria;
    private String data_criacao;

    public Empresa() {
    }

    public Empresa(String nome, String apelido, int nuit, ContactoEmpresa contactoEmpresa, EnderecoEmpresa enderecoEmpresa, ContaBancaria contaBancaria) {
        this.nome = nome;
        this.apelido = apelido;
        this.nuit = nuit;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
                contacto += c.getContacto() + "; ";
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
            endereco = enderecoEmpresa.getAvenida() + ", " + enderecoEmpresa.getRua()
                    + ", " + enderecoEmpresa.getNumero() + "; " + enderecoEmpresa.getBairro()
                    + ", " + enderecoEmpresa.getCidade() + ", " + enderecoEmpresa.getProvincia()
                    + "-" + enderecoEmpresa.getCodigoPostal();
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
