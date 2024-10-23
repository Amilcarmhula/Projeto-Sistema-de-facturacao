/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sysfact.dao.vendas.factura;

import javafx.collections.ObservableList;
import sysfact.model.vendas.factura.Factura;

/**
 *
 * @author JM-Tecnologias
 */
public interface FacturaDAOImpl<T> {
    
    void addEntity(T x);
    T getFacturaData(String factura);
    ObservableList<T> getFacturaToDashboard();
    ObservableList<T> getFacturaToPagamentos(int id);
    int countFactura();
//    double totalPaidInvoice();
    
}
