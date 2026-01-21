/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.cliente;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.usuario.Usuario;

/**
 *
 * @author JM-Tecnologias
 */
public class Cliente {

    private int id;
    private String tipo;
    private String nome_cli;
    private String razao_cli;
    private int nuit_cli;
    private String data_criacao;
    private long tempoRelacao;
    private List<ContactoCliente> contactosCliente;
    private List<EnderecoCliente> enderecosCliente;
    private ContactoCliente contactoCliente;
    private EnderecoCliente enderecoCliente;
    private Usuario usuario;

    public Cliente() {
        super();
    }

    public Cliente(String tipo, String nome, String apelido, int nuit, List<ContactoCliente> contactosCliente, List<EnderecoCliente> enderecosCliente, Usuario usuario) {
        super();
        this.tipo = tipo;
        this.nome_cli = nome;
        this.razao_cli = apelido;
        this.nuit_cli = nuit;
        this.contactosCliente = contactosCliente;
        this.enderecosCliente = enderecosCliente;
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_cli() {
        return nome_cli;
    }

    public void setNome_cli(String nome_cli) {
        this.nome_cli = nome_cli;
    }

    public String getRazao_cli() {
        return razao_cli;
    }

    public void setRazao_cli(String razao_cli) {
        this.razao_cli = razao_cli;
    }

    public int getNuit_cli() {
        return nuit_cli;
    }

    public void setNuit_cli(int nuit_cli) {
        this.nuit_cli = nuit_cli;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public long getTempoRelacao() {
        return tempoRelacao;
    }

    public void setTempoRelacao(long tempoRelacao) {
        this.tempoRelacao = tempoRelacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
        Date dataActual = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dataCriacao;
        try {
            dataCriacao = df.parse(data_criacao);
            Date hoje = df.parse(df.format(dataActual));
        long differenceInMiliseconds = Math.abs(hoje.getTime() - dataCriacao.getTime());
        this.tempoRelacao = TimeUnit.DAYS.convert(differenceInMiliseconds, TimeUnit.MILLISECONDS);
        } catch (ParseException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public List<ContactoCliente> getContactosCliente() {
        return contactosCliente;
    }

    public void setContactosCliente(List<ContactoCliente> contactosCliente) {
        this.contactosCliente = contactosCliente;
    }

    public List<EnderecoCliente> getEnderecosCliente() {
        return enderecosCliente;
    }

    public void setEnderecosCliente(List<EnderecoCliente> enderecosCliente) {
        this.enderecosCliente = enderecosCliente;
    }

    public ContactoCliente getContactoCliente() {
        return contactoCliente;
    }

    public void setContactoCliente(ContactoCliente contactoCliente) {
        this.contactoCliente = contactoCliente;
    }

    public EnderecoCliente getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(EnderecoCliente enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    public String printContatos() {
        String contacto = "";
        if (contactosCliente != null) {
            for (ContactoCliente c : contactosCliente) {
                contacto += c.getContacto_cli() + "; ";
            }
        }
        return contacto;
    }

    public String printEnderecos() {
        String endereco = "";
        if (enderecoCliente != null) {
            endereco = "Av. " + enderecoCliente.getAvenida_cli() + ", Rua " + enderecoCliente.getRua_cli()
                    + ", Nr." + enderecoCliente.getNumero_cli() + "; Bairro " + enderecoCliente.getBairro_cli()
                    + ", " + enderecoCliente.getCidade_cli() + ", " + enderecoCliente.getProvincia_cli()
                    + "-" + enderecoCliente.getCodigoPostal_cli();
        }
        return endereco;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
