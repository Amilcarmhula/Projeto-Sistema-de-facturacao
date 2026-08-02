/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jminvsm.dao.vendas.factura;

import jminvsm.dao.AddDAO;
import jminvsm.dao.CountDAO;
import javafx.collections.ObservableList;

/**
 *
 * @author JM-Tecnologias
 */
public interface FacturaDAOImpl<T> extends AddDAO<T>, CountDAO {
    
    T getFactura(String numero);
    T getFacturaData(String factura);
    ObservableList<T> getFacturaToDashboard();
    ObservableList<T> getFacturaToPagamentos(int id);
    boolean converterCotacaoEmFactura(String numeroCotacao, T facturaDestino);
    int countFactura();

    @Override
    default int count() {
        return countFactura();
    }
//    double totalPaidInvoice();
    
}
