/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoEmpresa extends Contacto {

    private int id;
    private String data_criacao;
    private int fk_id_empresa;
    private int fk_id_usuario;
    private boolean active_or_not;

    public ContactoEmpresa() {
    }

    public ContactoEmpresa(String contacto, String email, Empresa fk_id_empresa, Usuario fk_id_usuario) {
        super(contacto, email);
        this.fk_id_empresa = fk_id_empresa.getId();
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

    public int getFk_id_empresa() {
        return fk_id_empresa;
    }

    public void setFk_id_empresa(int fk_id_empresa) {
        this.fk_id_empresa = fk_id_empresa;
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
