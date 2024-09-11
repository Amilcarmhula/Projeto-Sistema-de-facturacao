/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.agente.endereco;

import sysfact.model.agente.Agente;
import sysfact.model.endereco.Endereco;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoAgente extends Endereco {

    private int id;
    private String data_criacao;
    private Agente agente;
    private Usuario usuario;
    private boolean active_or_not;

    public EnderecoAgente() {
    }

    public EnderecoAgente(Agente agente, Usuario usuario, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
        this.agente = agente;
        this.usuario = usuario;
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

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

    
}
