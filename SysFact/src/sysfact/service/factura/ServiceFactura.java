/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.factura;

import sysfact.dao.vendas.factura.FacturaDAO;
import sysfact.model.vendas.factura.Factura;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceFactura {
    private FacturaDAO facturaDao;

    public ServiceFactura(FacturaDAO facturaDao) {
        this.facturaDao = facturaDao;
    }
    
    public void criaFactura(){
        Factura f = new Factura();
        
        f.setNumero_fac("");
        f.setData_vencimento("");
        f.setCliente(null);
        f.setEmpresa(null);
        f.setUsuario(null);
        facturaDao.addEntity(f);
    }
}
