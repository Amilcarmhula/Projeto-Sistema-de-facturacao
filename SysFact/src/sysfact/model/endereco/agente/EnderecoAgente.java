/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.endereco.agente;

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
    private int fk_id_agente;
    private int fk_id_usuario;
    private boolean active_or_not;

    public EnderecoAgente() {
    }

    public EnderecoAgente(Agente fk_id_agente, Usuario fk_id_usuario, int id, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
        this.fk_id_agente = fk_id_agente.getId();
        this.fk_id_usuario = fk_id_usuario.getId();
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

    public int getFk_id_agente() {
        return fk_id_agente;
    }

    public void setFk_id_agente(int fk_id_agente) {
        this.fk_id_agente = fk_id_agente;
    }

    public int getFk_id_usuario() {
        return fk_id_usuario;
    }

    public void setFk_id_usuario(int fk_id_usuario) {
        this.fk_id_usuario = fk_id_usuario;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

}
