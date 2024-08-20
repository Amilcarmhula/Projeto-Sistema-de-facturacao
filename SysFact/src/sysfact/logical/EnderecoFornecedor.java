/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoFornecedor extends Endereco{
    
    private Fornecedor fk_id_Fornecedor;
    private boolean estadoActive;
    
    public EnderecoFornecedor(){}

    public EnderecoFornecedor(Fornecedor fk_id_Fornecedor, boolean estadoActive, int id, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(id, provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
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
