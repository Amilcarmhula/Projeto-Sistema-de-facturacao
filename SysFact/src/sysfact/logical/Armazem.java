/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class Armazem extends Endereco{
    
    private boolean estadoActive;
    
    public Armazem(){}

    public Armazem(boolean estadoActive, int id, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(id, provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
        this.estadoActive = estadoActive;
    }
   
    public boolean isEstadoActive() {
        return estadoActive;
    }

    public void setEstadoActive(boolean estadoActive) {
        this.estadoActive = estadoActive;
    }
    
    
}
