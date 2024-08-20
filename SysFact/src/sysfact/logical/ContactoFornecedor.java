/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoFornecedor extends Contacto{
    
    private Fornecedor fk_id_Fornecedor;
    private boolean estadoActive;
    
    public ContactoFornecedor(){}

    public ContactoFornecedor(Fornecedor fk_id_Fornecedor, boolean estadoActive, int id, String contacto, String email) {
        super(id, contacto, email);
        this.fk_id_Fornecedor = fk_id_Fornecedor;
        this.estadoActive = estadoActive;
    } 

    public Fornecedor getFk_id_Fornecedor() {
        return fk_id_Fornecedor;
    }

    public void setFk_id_Fornecedor(Fornecedor fk_id_Fornecedor) {
        this.fk_id_Fornecedor = fk_id_Fornecedor;
    }

    public boolean isEstadoActive() {
        return estadoActive;
    }

    public void setEstadoActive(boolean estadoActive) {
        this.estadoActive = estadoActive;
    }
   
}

