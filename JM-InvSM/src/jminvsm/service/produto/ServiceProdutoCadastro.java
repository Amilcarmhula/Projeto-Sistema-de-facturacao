package jminvsm.service.produto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.unidade_medida.UnidadeMedida;
import jminvsm.service.ServiceException;
import jminvsm.service.armazem.ServiceArmazem;
import jminvsm.service.categoria.ServicoCategoria;
import jminvsm.service.desconto.ServiceDesconto;
import jminvsm.service.taxaimposto.ServiceTaxaImpostos;
import jminvsm.service.unidadeMedida.ServiceUnidadeMedida;

public class ServiceProdutoCadastro {

    private final ServiceProduto serviceProduto;
    private final ServiceArmazem serviceArmazem;
    private final ServicoCategoria serviceCategoria;
    private final ServiceTaxaImpostos serviceTaxa;
    private final ServiceUnidadeMedida serviceUnidade;
    private final ServiceDesconto serviceDesconto;

    public ServiceProdutoCadastro() throws SQLException {
        this.serviceProduto = new ServiceProduto();
        this.serviceArmazem = new ServiceArmazem();
        this.serviceCategoria = new ServicoCategoria();
        this.serviceTaxa = new ServiceTaxaImpostos();
        this.serviceUnidade = new ServiceUnidadeMedida();
        this.serviceDesconto = new ServiceDesconto();
    }

    public boolean isOpsSuccess() {
        return serviceProduto.isOpsSuccess();
    }

    public void registar(Categoria categoria, UnidadeMedida unidade, Imposto taxa,
            String nome, String codigoBarras, String descricao, String tipo,
            Integer unidadePorTipo, Boolean controleStock, Integer nivelStock) {
        serviceProduto.regista(idCategoria(categoria), idUnidade(unidade), idTaxa(taxa),
                nome, codigoBarras, descricao, tipo, unidadePorTipo, controleStock, nivelStock);
    }

    public void actualizar(int idProduto, Categoria categoria, UnidadeMedida unidade, Imposto taxa,
            String nome, String codigoBarras, String descricao, String tipo,
            Integer unidadePorTipo, Boolean controleStock, Integer nivelStock) {
        serviceProduto.actualiza(idProduto, idCategoria(categoria), idUnidade(unidade), idTaxa(taxa),
                nome, codigoBarras, descricao, tipo, unidadePorTipo, controleStock, nivelStock);
    }

    public ObservableList<jminvsm.model.produto.Produto> listaTodosProdutos() {
        return serviceProduto.listaTodosProdutos();
    }

    public void excluir(int idProduto) {
        serviceProduto.excluir(idProduto);
    }

    public OpcoesProdutoCadastro carregarOpcoesCadastro() {
        ObservableList<Armazem> armazens = serviceArmazem.listaTodosArmazens();
        ObservableList<Desconto> descontos = serviceDesconto.listDescontos();
        ObservableList<Categoria> categorias = serviceCategoria.listaCategorias();
        ObservableList<Imposto> taxas = serviceTaxa.getTaxas();
        ObservableList<UnidadeMedida> unidades = serviceUnidade.getUnidades();

        Map<String, Armazem> mapaArmazens = new HashMap<>();
        List<String> nomesArmazens = new ArrayList<>();
        for (Armazem armazem : armazens) {
            nomesArmazens.add(armazem.getNome_arm());
            mapaArmazens.put(armazem.getNome_arm(), armazem);
        }

        Map<String, Desconto> mapaDescontos = new HashMap<>();
        List<String> nomesDescontos = new ArrayList<>();
        for (Desconto desconto : descontos) {
            nomesDescontos.add(desconto.getNome());
            mapaDescontos.put(desconto.getNome(), desconto);
        }

        Map<String, Categoria> mapaCategorias = new HashMap<>();
        List<String> nomesCategorias = new ArrayList<>();
        nomesCategorias.add("Todas...");
        for (Categoria categoria : categorias) {
            nomesCategorias.add(categoria.getFamilia());
            mapaCategorias.put(categoria.getFamilia(), categoria);
        }

        Map<String, UnidadeMedida> mapaUnidades = new HashMap<>();
        List<String> nomesUnidades = new ArrayList<>();
        nomesUnidades.add("Todas...");
        for (UnidadeMedida unidade : unidades) {
            nomesUnidades.add(unidade.getNome());
            mapaUnidades.put(unidade.getNome(), unidade);
        }

        Map<String, Imposto> mapaTaxas = new HashMap<>();
        List<String> nomesTaxas = new ArrayList<>();
        nomesTaxas.add("Todas...");
        for (Imposto taxa : taxas) {
            nomesTaxas.add(taxa.getNome());
            mapaTaxas.put(taxa.getNome(), taxa);
        }

        return new OpcoesProdutoCadastro(categorias, unidades, taxas, armazens, descontos,
                mapaCategorias, mapaUnidades, mapaTaxas, mapaArmazens, mapaDescontos,
                nomesCategorias, nomesUnidades, nomesTaxas, nomesArmazens, nomesDescontos);
    }

    private Integer idCategoria(Categoria categoria) {
        if (categoria == null || categoria.getId() <= 0) {
            throw new ServiceException("Categoria nao selecionada!");
        }
        return categoria.getId();
    }

    private Integer idUnidade(UnidadeMedida unidade) {
        if (unidade == null || unidade.getId() <= 0) {
            throw new ServiceException("Unidade de medida nao selecionada!");
        }
        return unidade.getId();
    }

    private Integer idTaxa(Imposto taxa) {
        if (taxa == null || taxa.getId() <= 0) {
            throw new ServiceException("Taxa/Imposto nao selecionada!");
        }
        return taxa.getId();
    }
}
