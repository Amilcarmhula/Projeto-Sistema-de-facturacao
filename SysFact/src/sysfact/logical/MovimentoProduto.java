/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class MovimentoProduto {

    private int id;
    private String tipo_movimento;
    private String data_movimento;
    private int quantidade;
    private String motivo_movimento;
    private Produto fk_id_produto;
    private Usuario fk_id_usuario;
    private Armazem fk_id_armazem;
    private boolean active_or_not;
    
    public MovimentoProduto(){}

    public MovimentoProduto(int id, String tipo_movimento, String data_movimento, int quantidade, String motivo_movimento, Produto fk_id_produto, Usuario fk_id_usuario, Armazem fk_id_armazem, boolean active_or_not) {
        this.id = id;
        this.tipo_movimento = tipo_movimento;
        this.data_movimento = data_movimento;
        this.quantidade = quantidade;
        this.motivo_movimento = motivo_movimento;
        this.fk_id_produto = fk_id_produto;
        this.fk_id_usuario = fk_id_usuario;
        this.fk_id_armazem = fk_id_armazem;
        this.active_or_not = active_or_not;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo_movimento() {
        return tipo_movimento;
    }

    public void setTipo_movimento(String tipo_movimento) {
        this.tipo_movimento = tipo_movimento;
    }

    public String getData_movimento() {
        return data_movimento;
    }

    public void setData_movimento(String data_movimento) {
        this.data_movimento = data_movimento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivo_movimento() {
        return motivo_movimento;
    }

    public void setMotivo_movimento(String motivo_movimento) {
        this.motivo_movimento = motivo_movimento;
    }

    public Produto getFk_id_produto() {
        return fk_id_produto;
    }

    public void setFk_id_produto(Produto fk_id_produto) {
        this.fk_id_produto = fk_id_produto;
    }

    public Usuario getFk_id_usuario() {
        return fk_id_usuario;
    }

    public void setFk_id_usuario(Usuario fk_id_usuario) {
        this.fk_id_usuario = fk_id_usuario;
    }

    public Armazem getFk_id_armazem() {
        return fk_id_armazem;
    }

    public void setFk_id_armazem(Armazem fk_id_armazem) {
        this.fk_id_armazem = fk_id_armazem;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }
    
    
}
