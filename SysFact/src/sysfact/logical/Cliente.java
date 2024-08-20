/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class Cliente {

    private int id;
    private String nome;
    private String apelido;
    private int nuit;
    private String data_criacao;
    private Usuario fk_id_usuario;
    private boolean active_or_not;

    public Cliente() {
    }

    public Cliente(int id, String nome, String apelido, int nuit, String data_criacao, Usuario fk_id_usuario, boolean active_or_not) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.nuit = nuit;
        this.data_criacao = data_criacao;
        this.fk_id_usuario = fk_id_usuario;
        this.active_or_not = active_or_not;
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

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
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
