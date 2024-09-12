/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.cliente.endereco;

import sysfact.model.cliente.Cliente;
import sysfact.model.endereco.Endereco;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoCliente extends Endereco {

    private int id;
    private String data_criacao;
    private Cliente cliente;
    private Usuario usuario;

    public EnderecoCliente() {
    }

    public EnderecoCliente(Cliente cliente, Usuario usuario, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
        this.cliente = cliente;
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
