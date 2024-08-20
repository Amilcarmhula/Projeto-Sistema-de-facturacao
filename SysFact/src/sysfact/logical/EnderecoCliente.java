/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoCliente extends Endereco{

    private Cliente fk_id_cliente;
    private Usuario fk_id_usuario;
    private boolean active_or_not;
    
    public EnderecoCliente(){}

    public EnderecoCliente(Cliente fk_id_cliente, Usuario fk_id_usuario, boolean active_or_not, int id, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(id, provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
        this.fk_id_cliente = fk_id_cliente;
        this.fk_id_usuario = fk_id_usuario;
        this.active_or_not = active_or_not;
    }

    public Cliente getFk_id_cliente() {
        return fk_id_cliente;
    }

    public void setFk_id_cliente(Cliente fk_id_cliente) {
        this.fk_id_cliente = fk_id_cliente;
    }

    public Usuario getFk_id_usuario() {
        return fk_id_usuario;
    }

    public void setFk_id_usuario(Usuario fk_id_usuario) {
        this.fk_id_usuario = fk_id_usuario;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }
    
    
}
