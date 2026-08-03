package jminvsm.service.dashboard;

import javafx.collections.ObservableList;
import jminvsm.model.stock.Stock;
import jminvsm.model.vendas.factura.Factura;

public class DashboardResumo {

    private final ObservableList<String> categorias;
    private final ObservableList<String> armazens;
    private final ObservableList<Factura> facturas;
    private final ObservableList<Stock> stock;
    private final ObservableList<Stock> stockBaixo;
    private final DashboardIndicadores indicadores;

    public DashboardResumo(ObservableList<String> categorias, ObservableList<String> armazens,
            ObservableList<Factura> facturas, ObservableList<Stock> stock,
            ObservableList<Stock> stockBaixo, DashboardIndicadores indicadores) {
        this.categorias = categorias;
        this.armazens = armazens;
        this.facturas = facturas;
        this.stock = stock;
        this.stockBaixo = stockBaixo;
        this.indicadores = indicadores;
    }

    public ObservableList<String> getCategorias() {
        return categorias;
    }

    public ObservableList<String> getArmazens() {
        return armazens;
    }

    public ObservableList<Factura> getFacturas() {
        return facturas;
    }

    public ObservableList<Stock> getStock() {
        return stock;
    }

    public ObservableList<Stock> getStockBaixo() {
        return stockBaixo;
    }

    public DashboardIndicadores getIndicadores() {
        return indicadores;
    }
}
