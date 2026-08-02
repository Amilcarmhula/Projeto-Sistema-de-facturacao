package jminvsm.views.configuracaoSistemaView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.auditoria.AuditoriaRegisto;
import jminvsm.model.configuracao.ConfiguracaoSistema;
import jminvsm.service.ServiceException;
import jminvsm.service.auditoria.ServiceAuditoriaRegisto;
import jminvsm.service.configuracao.ServiceConfiguracaoSistema;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

public class ConfiguracaoSistemaViewController implements Initializable {

    private ServiceConfiguracaoSistema serviceConfiguracao;
    private ServiceAuditoriaRegisto serviceAuditoria;

    @FXML
    private Label labModoActual;
    @FXML
    private Label labDescricaoModo;
    @FXML
    private ComboBox<String> combModoOperacao;
    @FXML
    private CheckBox checkControlarStock;
    @FXML
    private CheckBox checkPermitirVendaSemStock;
    @FXML
    private TextArea txtObservacaoConfig;

    @FXML
    private DatePicker dataInicioPicker;
    @FXML
    private DatePicker dataFimPicker;
    @FXML
    private ComboBox<String> combTabelaAuditoria;
    @FXML
    private ComboBox<String> combOperacaoAuditoria;
    @FXML
    private TextField txtUsuarioAuditoria;
    @FXML
    private TextField txtRegistoAuditoria;
    @FXML
    private TableView<AuditoriaRegisto> tabelaAuditoria;
    @FXML
    private TableColumn<AuditoriaRegisto, Long> colAudId;
    @FXML
    private TableColumn<AuditoriaRegisto, String> colAudData;
    @FXML
    private TableColumn<AuditoriaRegisto, String> colAudUsuario;
    @FXML
    private TableColumn<AuditoriaRegisto, String> colAudOperacao;
    @FXML
    private TableColumn<AuditoriaRegisto, String> colAudTabela;
    @FXML
    private TableColumn<AuditoriaRegisto, String> colAudRegisto;
    @FXML
    private TableColumn<AuditoriaRegisto, String> colAudColunas;
    @FXML
    private TextArea txtValoresAnteriores;
    @FXML
    private TextArea txtValoresNovos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviceConfiguracao = new ServiceConfiguracaoSistema();
        serviceAuditoria = new ServiceAuditoriaRegisto();
        configurarModoOperacao();
        configurarAuditoria();
        carregarConfiguracao();
        pesquisarAuditoria();
    }

    @FXML
    private void guardarConfiguracao() {
        String modo = combModoOperacao.getSelectionModel().getSelectedItem();
        if (modo == null || modo.trim().isEmpty()) {
            showErroAlert("Seleccione o modo de operacao!");
            return;
        }
        boolean controlarStock = "FACTURACAO_STOCK".equals(modo) && checkControlarStock.isSelected();
        boolean permitirVendaSemStock = checkPermitirVendaSemStock.isSelected();
        if ("FACTURACAO".equals(modo)) {
            controlarStock = false;
            permitirVendaSemStock = true;
        }
        try {
            if (serviceConfiguracao.actualizarModo(modo, controlarStock, permitirVendaSemStock,
                    txtObservacaoConfig.getText(), SessaoAplicacao.getUserData())) {
                showSuccessAlert("Configuracao do sistema actualizada com sucesso!");
                carregarConfiguracao();
                pesquisarAuditoria();
            }
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
        }
    }

    @FXML
    private void pesquisarAuditoria() {
        String dataInicio = dataInicioPicker.getValue() != null ? dataInicioPicker.getValue().toString() : null;
        String dataFim = dataFimPicker.getValue() != null ? dataFimPicker.getValue().toString() : null;
        String tabela = combTabelaAuditoria.getSelectionModel().getSelectedItem();
        String operacao = combOperacaoAuditoria.getSelectionModel().getSelectedItem();
        Integer idUsuario = parseInteger(txtUsuarioAuditoria.getText());
        String idRegisto = txtRegistoAuditoria.getText();
        tabelaAuditoria.setItems(serviceAuditoria.pesquisar(dataInicio, dataFim, tabela, operacao, idUsuario, idRegisto));
        limparDetalheAuditoria();
    }

    @FXML
    private void limparFiltrosAuditoria() {
        dataInicioPicker.setValue(LocalDate.now().minusDays(30));
        dataFimPicker.setValue(LocalDate.now());
        combTabelaAuditoria.getSelectionModel().select("Todos");
        combOperacaoAuditoria.getSelectionModel().select("Todos");
        txtUsuarioAuditoria.clear();
        txtRegistoAuditoria.clear();
        pesquisarAuditoria();
    }

    private void configurarModoOperacao() {
        combModoOperacao.setItems(FXCollections.observableArrayList("FACTURACAO_STOCK", "FACTURACAO"));
        combModoOperacao.valueProperty().addListener((obs, oldValue, newValue) -> aplicarRegraModo(newValue));
    }

    private void configurarAuditoria() {
        dataInicioPicker.setValue(LocalDate.now().minusDays(30));
        dataFimPicker.setValue(LocalDate.now());
        combOperacaoAuditoria.setItems(FXCollections.observableArrayList("Todos", "INSERT", "UPDATE", "DELETE"));
        combOperacaoAuditoria.getSelectionModel().select("Todos");
        combTabelaAuditoria.setItems(serviceAuditoria.listarTabelasAuditadas());
        combTabelaAuditoria.getSelectionModel().select("Todos");

        tabelaAuditoria.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colAudId.setCellValueFactory(new PropertyValueFactory<AuditoriaRegisto, Long>("id"));
        colAudData.setCellValueFactory(new PropertyValueFactory<AuditoriaRegisto, String>("dataOperacao"));
        colAudUsuario.setCellValueFactory(new PropertyValueFactory<AuditoriaRegisto, String>("usuarioNome"));
        colAudOperacao.setCellValueFactory(new PropertyValueFactory<AuditoriaRegisto, String>("operacao"));
        colAudTabela.setCellValueFactory(new PropertyValueFactory<AuditoriaRegisto, String>("tabela"));
        colAudRegisto.setCellValueFactory(new PropertyValueFactory<AuditoriaRegisto, String>("idRegisto"));
        colAudColunas.setCellValueFactory(new PropertyValueFactory<AuditoriaRegisto, String>("colunasAlteradas"));
        tabelaAuditoria.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, registo) -> mostrarDetalheAuditoria(registo));
    }

    private void carregarConfiguracao() {
        ConfiguracaoSistema configuracao = serviceConfiguracao.getConfiguracaoActual();
        combModoOperacao.getSelectionModel().select(configuracao.getModoOperacao());
        checkControlarStock.setSelected(configuracao.isControlarStockGlobal());
        checkPermitirVendaSemStock.setSelected(configuracao.isPermitirVendaSemStock());
        txtObservacaoConfig.setText(configuracao.getObservacao());
        labModoActual.setText(configuracao.getModoOperacao());
        aplicarRegraModo(configuracao.getModoOperacao());
    }

    private void aplicarRegraModo(String modo) {
        boolean modoStock = "FACTURACAO_STOCK".equals(modo);
        checkControlarStock.setDisable(!modoStock);
        checkPermitirVendaSemStock.setDisable(!modoStock);
        if (!modoStock) {
            checkControlarStock.setSelected(false);
            checkPermitirVendaSemStock.setSelected(true);
            labDescricaoModo.setText("Facturacao simples: documentos e financeiro activos, stock fisico ignorado.");
        } else {
            labDescricaoModo.setText("Facturacao com stock: valida reservas, lotes, movimentos, recebimentos e transferencias.");
        }
    }

    private void mostrarDetalheAuditoria(AuditoriaRegisto registo) {
        if (registo == null) {
            limparDetalheAuditoria();
            return;
        }
        txtValoresAnteriores.setText(formatJson(registo.getValoresAnteriores()));
        txtValoresNovos.setText(formatJson(registo.getValoresNovos()));
    }

    private void limparDetalheAuditoria() {
        txtValoresAnteriores.clear();
        txtValoresNovos.clear();
    }

    private Integer parseInteger(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String formatJson(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        return value.replace(",", ",\n").replace("{", "{\n").replace("}", "\n}");
    }
}

