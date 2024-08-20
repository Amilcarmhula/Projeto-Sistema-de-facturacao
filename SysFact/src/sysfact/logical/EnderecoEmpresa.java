/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoEmpresa extends Endereco{

    private Empresa fk_id_empresa;
    private boolean active_or_not;

    public EnderecoEmpresa() {
    }

    public EnderecoEmpresa(Empresa fk_id_empresa, boolean active_or_not, int id, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(id, provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
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
