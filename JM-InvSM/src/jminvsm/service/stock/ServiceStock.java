/*
 * The MIT License
 *
 * Copyright 2024 JM-Tecnologias.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jminvsm.service.stock;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import jminvsm.dao.stock.StockDAO;
import jminvsm.model.stock.Stock;
import jminvsm.service.configuracao.ServiceConfiguracaoSistema;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceStock {
    private final StockDAO stockDao;
    private final ServiceConfiguracaoSistema serviceConfiguracao;
    
    public ServiceStock() throws SQLException{
        this.stockDao = new StockDAO();
        this.serviceConfiguracao = new ServiceConfiguracaoSistema();
    }
    
    // metodo que buscao produtos mara mostrar na tabela de inventarios. CORRIGIR
    public ObservableList<Stock> listaStockProdutos(Integer idArm){
        if (!serviceConfiguracao.isModuloStockActivo()) {
            return FXCollections.observableArrayList();
        }
        return stockDao.listStock(idArm);
    }

    public ObservableList<Stock> listaProdutosParaVenda(Integer idArm) {
        if (serviceConfiguracao.isModuloStockActivo()) {
            return stockDao.listStock(idArm);
        }
        return stockDao.listProdutosParaVendaSemStock(idArm);
    }
    
    public ObservableList<Stock> stockBaixo(){
        if (!serviceConfiguracao.isModuloStockActivo()) {
            return FXCollections.observableArrayList();
        }
        return stockDao.loadLowStock();
    }
    
    public ObservableList<Stock> consultaStock(Integer idCat, Integer idArm){
        if (!serviceConfiguracao.isModuloStockActivo()) {
            return FXCollections.observableArrayList();
        }
        return stockDao.getStock(idCat, idArm);
    }
    
    public Stock consultaDetalhesProduto(Integer id){
        if (!serviceConfiguracao.isModuloStockActivo()) {
            return null;
        }
        return stockDao.getProdutoForStock(id);
    }
    
    public ObservableList<Stock> getResumo(Integer idPro){
        if (!serviceConfiguracao.isModuloStockActivo()) {
            return FXCollections.observableArrayList();
        }
        return stockDao.getResumoStock(idPro);
    }
    
    public int contaSaldoStock(Integer idCat, Integer idArm){
        if (!serviceConfiguracao.isModuloStockActivo()) {
            return 0;
        }
        return stockDao.countStock(idCat, idArm);
    }
    
}
