package jminvsm.dao;

import java.util.List;

public interface BatchItemDAO<T> {

    void addEntity(List<T> entities);

    void addEntityQuotation(List<T> entities);

    List<T> getItensData(String reference);

    boolean converterCotacaoParaFactura(String numeroCotacao, String numeroFactura);

    List<T> getItensCotacaoEditaveis(String numeroCotacao);

    boolean substituirItensCotacao(String numeroCotacao, List<T> itens);
}
