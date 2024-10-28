/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.vendas.factura;

import javafx.collections.ObservableList;
import jminvsm.model.vendas.factura.Factura;

/**
 *
 * @author JM-Tecnologias
 */
public interface FacturaDAOImpl<T> {
    
    boolean addEntity(T x);
    T getFactura(String numero);
    T getFacturaData(String factura);
    ObservableList<T> getFacturaToDashboard();
    ObservableList<T> getFacturaToPagamentos(int id);
    int countFactura();
//    double totalPaidInvoice();
    
}
