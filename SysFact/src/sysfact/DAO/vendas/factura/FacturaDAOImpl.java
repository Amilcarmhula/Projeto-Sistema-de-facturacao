/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.vendas.factura;

/**
 *
 * @author JM-Tecnologias
 */
public interface FacturaDAOImpl<T> {
    
    void addEntity(T x);
    T getFacturaData(String factura);
    
}
