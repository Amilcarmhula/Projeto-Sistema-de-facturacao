/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoEmpresa extends Contacto{

    private Empresa fk_id_empresa;
    private boolean active_or_not;

    public ContactoEmpresa() {
    }

    public ContactoEmpresa(Empresa fk_id_empresa, boolean active_or_not, int id, String contacto, String email) {
        super(id, contacto, email);
        this.fk_id_empresa = fk_id_empresa;
        this.active_or_not = active_or_not;
    }

    public Empresa getFk_id_empresa() {
        return fk_id_empresa;
    }

    public void setFk_id_empresa(Empresa fk_id_empresa) {
        this.fk_id_empresa = fk_id_empresa;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }
    
}
