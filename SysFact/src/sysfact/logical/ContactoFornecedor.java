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
    private int id;
    private String data_criacao;
    private int fk_id_Fornecedor;
    private boolean estadoActive;
    
    public ContactoFornecedor(){}

    public ContactoFornecedor( Fornecedor fk_id_Fornecedor, String contacto, String email) {
        super(contacto, email);
        this.fk_id_Fornecedor = fk_id_Fornecedor.getId();
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

    public int getFk_id_Fornecedor() {
        return fk_id_Fornecedor;
    }

    public void setFk_id_Fornecedor(int fk_id_Fornecedor) {
        this.fk_id_Fornecedor = fk_id_Fornecedor;
    }

    public boolean isEstadoActive() {
        return estadoActive;
    }

    public void setEstadoActive(boolean estadoActive) {
        this.estadoActive = estadoActive;
    }
   
}

