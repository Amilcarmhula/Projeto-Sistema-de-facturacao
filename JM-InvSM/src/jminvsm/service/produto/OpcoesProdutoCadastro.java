package jminvsm.service.produto;

import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.categoria.Categoria;
import jminvsm.model.desconto.Desconto;
import jminvsm.model.imposto.Imposto;
import jminvsm.model.unidade_medida.UnidadeMedida;

public class OpcoesProdutoCadastro {

    private final ObservableList<Categoria> categorias;
    private final ObservableList<UnidadeMedida> unidades;
    private final ObservableList<Imposto> taxas;
    private final ObservableList<Armazem> armazens;
    private final ObservableList<Desconto> descontos;
    private final Map<String, Categoria> mapaCategorias;
    private final Map<String, UnidadeMedida> mapaUnidades;
    private final Map<String, Imposto> mapaTaxas;
    private final Map<String, Armazem> mapaArmazens;
    private final Map<String, Desconto> mapaDescontos;
    private final List<String> nomesCategorias;
    private final List<String> nomesUnidades;
    private final List<String> nomesTaxas;
    private final List<String> nomesArmazens;
    private final List<String> nomesDescontos;

    public OpcoesProdutoCadastro(ObservableList<Categoria> categorias,
            ObservableList<UnidadeMedida> unidades, ObservableList<Imposto> taxas,
            ObservableList<Armazem> armazens, ObservableList<Desconto> descontos,
            Map<String, Categoria> mapaCategorias, Map<String, UnidadeMedida> mapaUnidades,
            Map<String, Imposto> mapaTaxas, Map<String, Armazem> mapaArmazens,
            Map<String, Desconto> mapaDescontos, List<String> nomesCategorias,
            List<String> nomesUnidades, List<String> nomesTaxas, List<String> nomesArmazens,
            List<String> nomesDescontos) {
        this.categorias = categorias;
        this.unidades = unidades;
        this.taxas = taxas;
        this.armazens = armazens;
        this.descontos = descontos;
        this.mapaCategorias = mapaCategorias;
        this.mapaUnidades = mapaUnidades;
        this.mapaTaxas = mapaTaxas;
        this.mapaArmazens = mapaArmazens;
        this.mapaDescontos = mapaDescontos;
        this.nomesCategorias = nomesCategorias;
        this.nomesUnidades = nomesUnidades;
        this.nomesTaxas = nomesTaxas;
        this.nomesArmazens = nomesArmazens;
        this.nomesDescontos = nomesDescontos;
    }

    public ObservableList<Categoria> getCategorias() {
        return categorias;
    }

    public ObservableList<UnidadeMedida> getUnidades() {
        return unidades;
    }

    public ObservableList<Imposto> getTaxas() {
        return taxas;
    }

    public ObservableList<Armazem> getArmazens() {
        return armazens;
    }

    public ObservableList<Desconto> getDescontos() {
        return descontos;
    }

    public Map<String, Categoria> getMapaCategorias() {
        return mapaCategorias;
    }

    public Map<String, UnidadeMedida> getMapaUnidades() {
        return mapaUnidades;
    }

    public Map<String, Imposto> getMapaTaxas() {
        return mapaTaxas;
    }

    public Map<String, Armazem> getMapaArmazens() {
        return mapaArmazens;
    }

    public Map<String, Desconto> getMapaDescontos() {
        return mapaDescontos;
    }

    public List<String> getNomesCategorias() {
        return nomesCategorias;
    }

    public List<String> getNomesUnidades() {
        return nomesUnidades;
    }

    public List<String> getNomesTaxas() {
        return nomesTaxas;
    }

    public List<String> getNomesArmazens() {
        return nomesArmazens;
    }

    public List<String> getNomesDescontos() {
        return nomesDescontos;
    }
}
