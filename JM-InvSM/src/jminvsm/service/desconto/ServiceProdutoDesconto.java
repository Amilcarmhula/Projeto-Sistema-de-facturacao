/*
 * The MIT License
 *
 * Copyright 2025 JM-Tecnologias.
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
package jminvsm.service.desconto;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.ObservableList;
import jminvsm.dao.desconto.ProdutoDescontoDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.desconto.ProdutoDesconto;
import jminvsm.model.produto.Produto;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceProdutoDesconto {

    private final ProdutoDescontoDAO dao;
    private boolean opsSuccess;

    public ServiceProdutoDesconto() throws SQLException {
        this.dao = new ProdutoDescontoDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void regista(Integer idProduto, Integer idDesconto, Integer idArmazem,
            String dataInicio, String dataFim) {
        setOpsSuccess(false);

        if (idProduto == null) {
            showErroAlert("Produto nao definido.");
            return;
        }

        if (idDesconto == null) {
            showErroAlert("Desconto nao definido.");
            return;
        }

        if (dataInicio == null || dataInicio.isEmpty() || "null".equalsIgnoreCase(dataInicio)) {
            showErroAlert("Data de descontos nao definidas!");
            dataInicio = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if (dataFim == null || dataFim.isEmpty() || "null".equalsIgnoreCase(dataFim)) {
            showErroAlert("Data de descontos nao definidas!");
            dataFim = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        ProdutoDesconto pd = new ProdutoDesconto();
        Produto p = new Produto();
        p.setId(idProduto);
        pd.setProduto(p);
        Desconto d = new Desconto();
        d.setId(idDesconto);
        pd.setDesconto(d);
        Armazem a = new Armazem();
        a.setId(idArmazem);
        pd.setArmazem(a);
        pd.setDataInicio(dataInicio);
        pd.setDataFim(dataFim);
        if (dao.addEntity(pd)) {
            dao.callAplicaDesconto(pd);
            setOpsSuccess(true);
            showSuccessAlert("Desconto registado com sucesso!");
        }
    }
    
    public ObservableList<ProdutoDesconto> consultaProdutoDesconto(int idProduto) {
        return dao.getEntityByID(idProduto);
    }

}
