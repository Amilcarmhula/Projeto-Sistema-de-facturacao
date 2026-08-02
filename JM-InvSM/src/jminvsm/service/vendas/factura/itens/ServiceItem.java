/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.vendas.factura.itens;

import java.sql.SQLException;
import java.util.List;
import jminvsm.dao.vendas.itens.ItemDAO;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.ServiceException;
import jminvsm.service.configuracao.ServiceConfiguracaoSistema;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceItem {
    private ItemDAO itemDao;
    private final ServiceConfiguracaoSistema serviceConfiguracao;

    public ServiceItem() throws SQLException {
        this.itemDao = new ItemDAO();
        this.serviceConfiguracao = new ServiceConfiguracaoSistema();
    }
    public void registaItens(List<Item> itens){
        registaItens(itens, serviceConfiguracao.isModuloStockActivo());
    }

    public void registaItens(List<Item> itens, boolean modoFacturacaoStock){
        if(itens == null || itens.isEmpty()){
            throw new ServiceException("Nao pode ser emitida factura sem itens!");
        }
        
        try {
            itemDao.addEntity(itens);
        } catch (Exception ex) {
            throw new ServiceException("Erro ao emitir factura!", ex);
        }
    }
    
    public void registaCotacao(List<Item> itens){
        if(itens == null || itens.isEmpty()){
            throw new ServiceException("Nao pode ser emitida factura sem itens!");
        }
        
        try {
            itemDao.addEntityQuotation(itens);
        } catch (Exception ex) {
            throw new ServiceException("Erro ao emitir factura!", ex);
        }
    }
    
    public List<Item>  consultaItensFactura(String numero){
        return itemDao.getItensData(numero);
    }

    public List<Item> carregarItensCotacaoEditaveis(String numeroCotacao) {
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            throw new ServiceException("Cotacao nao informada!");
        }
        return itemDao.getItensCotacaoEditaveis(numeroCotacao);
    }

    public boolean guardarItensCotacao(String numeroCotacao, List<Item> itens) {
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            throw new ServiceException("Cotacao nao informada!");
        }
        if (itens == null || itens.isEmpty()) {
            throw new ServiceException("A cotacao deve possuir pelo menos um item!");
        }
        return itemDao.substituirItensCotacao(numeroCotacao, itens);
    }

    public boolean converterCotacaoParaFactura(String numeroCotacao, String numeroFactura) {
        if (numeroCotacao == null || numeroCotacao.trim().isEmpty()) {
            throw new ServiceException("Cotacao nao informada!");
        }
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new ServiceException("Factura destino nao informada!");
        }
        return itemDao.converterCotacaoParaFactura(numeroCotacao, numeroFactura);
    }
    
}
