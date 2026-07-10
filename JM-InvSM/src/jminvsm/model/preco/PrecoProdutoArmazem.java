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
package jminvsm.model.preco;

import java.math.BigDecimal;
import java.time.LocalDate;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.enums.EstadoPreco;
import jminvsm.model.produto.Produto;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toLocalDate;
import static jminvsm.model.util.ModelConverters.toNullableDouble;

/**
 *
 * @author JM-Tecnologias
 */
public class PrecoProdutoArmazem {
    private int id;
    private Produto produto;
    private Armazem armazem;
    private BigDecimal precoBase;
    private BigDecimal precoVenda;
    private BigDecimal precoFinal;
    private LocalDate dataInicioVigencia;
    private LocalDate dataFimVigencia;
    private EstadoPreco estado;

    public PrecoProdutoArmazem() {
    }

    public PrecoProdutoArmazem(Produto produto, Armazem armazem, Double preco, Double precoVenda, Double precoFinal, String dataInicioVigencia, String dataFimVigencia, String estado) {
        this.produto = produto;
        this.armazem = armazem;
        this.precoBase = toBigDecimal(preco);
        this.precoVenda = toBigDecimal(precoVenda);
        this.precoFinal = toBigDecimal(precoFinal);
        this.dataInicioVigencia = toLocalDate(dataInicioVigencia);
        this.dataFimVigencia = toLocalDate(dataFimVigencia);
        this.estado = EstadoPreco.fromValue(estado);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public Double getPrecoBase() {
        return toNullableDouble(precoBase);
    }

    public void setPrecoBase(Double precoBase) {
        this.precoBase = toBigDecimal(precoBase);
    }

    public BigDecimal getPrecoBaseDecimal() {
        return precoBase;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public Double getPrecoVenda() {
        return toNullableDouble(precoVenda);
    }

    public void setPrecoVenda(Double precoVenda) {
        this.precoVenda = toBigDecimal(precoVenda);
    }

    public BigDecimal getPrecoVendaDecimal() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Double getPrecoFinal() {
        return toNullableDouble(precoFinal);
    }

    public void setPrecoFinal(Double precoFinal) {
        this.precoFinal = toBigDecimal(precoFinal);
    }

    public BigDecimal getPrecoFinalDecimal() {
        return precoFinal;
    }

    public void setPrecoFinal(BigDecimal precoFinal) {
        this.precoFinal = precoFinal;
    }

    public String getDataInicioVigencia() {
        return toDateString(dataInicioVigencia);
    }

    public void setDataInicioVigencia(String dataInicioVigencia) {
        this.dataInicioVigencia = toLocalDate(dataInicioVigencia);
    }

    public LocalDate getDataInicioVigenciaDate() {
        return dataInicioVigencia;
    }

    public void setDataInicioVigencia(LocalDate dataInicioVigencia) {
        this.dataInicioVigencia = dataInicioVigencia;
    }


    public String getDataFimVigencia() {
        return toDateString(dataFimVigencia);
    }

    public void setDataFimVigencia(String dataFimVigencia) {
        this.dataFimVigencia = toLocalDate(dataFimVigencia);
    }

    public LocalDate getDataFimVigenciaDate() {
        return dataFimVigencia;
    }

    public void setDataFimVigencia(LocalDate dataFimVigencia) {
        this.dataFimVigencia = dataFimVigencia;
    }

    public String getEstado() {
        return estado != null ? estado.getValor() : null;
    }

    public void setEstado(String estado) {
        this.estado = EstadoPreco.fromValue(estado);
    }

    public EstadoPreco getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoPreco estado) {
        this.estado = estado;
    }

   
}
