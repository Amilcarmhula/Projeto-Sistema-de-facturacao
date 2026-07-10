package jminvsm.model.movimento;

import java.time.LocalDate;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.enums.TipoMovimento;
import jminvsm.model.enums.TipoOrigemMovimento;
import jminvsm.model.lote.Lote;
import jminvsm.model.produto.Produto;
import jminvsm.model.requisicao.ItemRequisicao;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toLocalDate;

public class Movimento {

    private int id;
    private TipoOrigemMovimento tipoOrigem;
    private TipoMovimento tipoMovimento;
    private LocalDate dataMovimento;
    private int quantidade;
    private Armazem armazem;
    private Produto produto;
    private Lote lote;
    private ItemRequisicao itemRequisicao;

    public Movimento() {
    }

    public Movimento(String tipoOrigem, String tipoMovimento, LocalDate dataMovimento,
            int quantidade, Armazem armazem, Produto produto, Lote lote, ItemRequisicao itemRequisicao) {
        this.tipoOrigem = TipoOrigemMovimento.fromValue(tipoOrigem);
        this.tipoMovimento = TipoMovimento.fromValue(tipoMovimento);
        this.dataMovimento = dataMovimento;
        this.quantidade = quantidade;
        this.armazem = armazem;
        this.produto = produto;
        this.lote = lote;
        this.itemRequisicao = itemRequisicao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoOrigem() {
        return tipoOrigem != null ? tipoOrigem.getValor() : null;
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = TipoOrigemMovimento.fromValue(tipoOrigem);
    }

    public TipoOrigemMovimento getTipoOrigemEnum() {
        return tipoOrigem;
    }

    public void setTipoOrigem(TipoOrigemMovimento tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public String getTipoMovimento() {
        return tipoMovimento != null ? tipoMovimento.getValor() : null;
    }

    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = TipoMovimento.fromValue(tipoMovimento);
    }

    public TipoMovimento getTipoMovimentoEnum() {
        return tipoMovimento;
    }

    public void setTipoMovimento(TipoMovimento tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public String getDataMovimento() {
        return toDateString(dataMovimento);
    }

    public void setDataMovimento(String dataMovimento) {
        this.dataMovimento = toLocalDate(dataMovimento);
    }

    public LocalDate getDataMovimentoDate() {
        return dataMovimento;
    }

    public void setDataMovimento(LocalDate dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public ItemRequisicao getItemRequisicao() {
        return itemRequisicao;
    }

    public void setItemRequisicao(ItemRequisicao itemRequisicao) {
        this.itemRequisicao = itemRequisicao;
    }
}
