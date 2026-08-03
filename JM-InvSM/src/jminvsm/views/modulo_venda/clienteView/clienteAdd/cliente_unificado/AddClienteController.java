package jminvsm.views.modulo_venda.clienteView.clienteAdd.cliente_unificado;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;
import jminvsm.service.cliente.ServiceClienteCadastro;
import jminvsm.service.cliente.ServiceContactoCliente;
import jminvsm.service.cliente.ServiceEnderecoCliente;
import jminvsm.util.LoadAndMoveUtilities;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 * Controlador Ãºnico para dados gerais, contactos e endereÃ§os do cliente.
 * TambÃ©m pode controlar contactoView.fxml e enderecoView.fxml isoladamente.
 */
public class AddClienteController implements Initializable {

    private static final Logger LOG = Logger.getLogger(AddClienteController.class.getName());

    private Usuario userData;
    private ServiceClienteCadastro serviceCliente;
    private ServiceContactoCliente serviceContacto;
    private ServiceEnderecoCliente serviceEndereco;
    private Cliente cliente;

    private ObservableList<ContactoCliente> listaContactos = FXCollections.observableArrayList();
    private ObservableList<EnderecoCliente> listaEndereco = FXCollections.observableArrayList();

    // Dados gerais â€” nomes originais preservados
    @FXML private Button btnClose;
    @FXML private Button btnAddCliente;
    @FXML private Button btnContactoView;
    @FXML private Button btnEnderecoView;
    @FXML private ComboBox<String> combTipoCliente;
    @FXML private TextField txtIDCliente;
    @FXML private TextField txtNome;
    @FXML private TextField txtNuit;
    @FXML private TextField txtRazao;
    @FXML private VBox anchorMain;

    // Contacto â€” nomes originais preservados
    @FXML private Button btnAddContacto;
    @FXML private Button btnDelete;
    @FXML private Label labHeadTitle;
    @FXML private TextField txtContacto;
    @FXML private TextField txtEmail;
    @FXML private TextField txtIDContacto;
    @FXML private TextField txtResponsavel;
    @FXML private TextField txtSite;
    @FXML private ListView<ContactoCliente> listaContactosView;

    // EndereÃ§o â€” nomes originais preservados
    @FXML private Button btnAddEndereco;
    @FXML private ComboBox<String> combProvincia;
    @FXML private RadioButton radFacturacao;
    @FXML private RadioButton radEnvio;
    @FXML private ComboBox<String> combTipoEndereco;
    @FXML private TextField txtAvenida;
    @FXML private TextField txtBairro;
    @FXML private TextField txtCidade;
    @FXML private TextField txtCodigooPostal;
    @FXML private TextField txtIDEndereco;
    @FXML private TextField txtNumeroEndereco;
    @FXML private TextField txtRua;
    @FXML private ListView<EnderecoCliente> listaEnderecosView;

    // Novos nomes necessÃ¡rios porque btnDelete e labHeadTitle estavam duplicados
    @FXML private Button btnDeleteEndereco;
    @FXML private Label labHeadTitleEndereco;

    private final ToggleGroup finalidadeEndereco = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userData = SessaoAplicacao.getUserData();

        try {
            serviceCliente = new ServiceClienteCadastro();
            serviceContacto = new ServiceContactoCliente();
            serviceEndereco = new ServiceEnderecoCliente();

            prepararCombos();
            prepararListas();
            prepararFinalidadeEndereco();
            carregarContextoInicial();
            actualizarTextosBotoes();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "NÃ£o foi possÃ­vel inicializar o cadastro do cliente.", ex);
            mostrarErro("InicializaÃ§Ã£o", "NÃ£o foi possÃ­vel carregar os serviÃ§os do cadastro.");
        }
    }

    private void prepararCombos() {
        if (combTipoCliente != null) {
            combTipoCliente.setItems(FXCollections.observableArrayList("Pessoa FÃ­sica", "JurÃ­dica"));
        }
        if (combTipoEndereco != null) {
            combTipoEndereco.setItems(FXCollections.observableArrayList("FacturaÃ§Ã£o", "Envio"));
        }
        if (combProvincia != null) {
            combProvincia.setItems(FXCollections.observableArrayList(
                    "Maputo(Cidade)", "Maputo(ProvÃ­ncia)", "Gaza", "Inhambane",
                    "Sofala", "Manica", "Tete", "ZambÃ©zia", "Nampula",
                    "Niassa", "Cabo Delgado"
            ));
        }
    }

    private void prepararFinalidadeEndereco() {
        if (radEnvio != null) {
            radEnvio.setToggleGroup(finalidadeEndereco);
            radEnvio.setOnAction(e -> sincronizarTipoEndereco("Envio"));
        }
        if (radFacturacao != null) {
            radFacturacao.setToggleGroup(finalidadeEndereco);
            radFacturacao.setOnAction(e -> sincronizarTipoEndereco("FacturaÃ§Ã£o"));
        }
        if (combTipoEndereco != null) {
            combTipoEndereco.setOnAction(e -> seleccionarRadioDoTipo(combTipoEndereco.getValue()));
        }
    }

    private void prepararListas() {
        if (listaContactosView != null) {
            listaContactosView.setItems(listaContactos);
            listaContactosView.setCellFactory(view -> new ListCell<ContactoCliente>() {
                @Override
                protected void updateItem(ContactoCliente item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String responsavel = texto(item.getResponsavel(), "Sem responsÃ¡vel");
                        String telefone = texto(item.getContacto_cli(), "Sem telefone");
                        setText(responsavel + "\n" + telefone);
                    }
                }
            });
        }

        if (listaEnderecosView != null) {
            listaEnderecosView.setItems(listaEndereco);
            listaEnderecosView.setCellFactory(view -> new ListCell<EnderecoCliente>() {
                @Override
                protected void updateItem(EnderecoCliente item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String tipo = texto(item.getTipo_cli(), "EndereÃ§o");
                        String cidade = texto(item.getCidade_cli(), "Sem cidade");
                        String bairro = texto(item.getBairro_cli(), "");
                        setText(tipo + "\n" + cidade + (bairro.isEmpty() ? "" : " â€” " + bairro));
                    }
                }
            });
        }
    }

    private void carregarContextoInicial() throws SQLException {
        Object data = SessaoAplicacao.getData();

        if (data instanceof Cliente) {
            cliente = (Cliente) data;
            preencherCliente(cliente);
        } else if (data instanceof ContactoCliente) {
            preencherContacto((ContactoCliente) data);
        } else if (data instanceof EnderecoCliente) {
            preencherEndereco((EnderecoCliente) data);
        }

        if (cliente == null && possuiIdCliente()) {
            cliente = serviceCliente.consultaPorId(valor(txtIDCliente));
        }

        if (cliente != null) {
            carregarContactos();
            carregarEnderecos();
        }
    }

    public void close(ActionEvent event) {
        SessaoAplicacao.setData(null);
        if (LoadAndMoveUtilities.returnToStage()) {
            LoadAndMoveUtilities.setEstadoStage(false);
            LoadAndMoveUtilities.showStage(null, null);
        } else if (LoadAndMoveUtilities.returnToBaseAnchor()) {
            LoadAndMoveUtilities.setEstadoPopUP(false);
            LoadAndMoveUtilities.showAsPopUP(null, null);
        }
        if (event != null && event.getSource() instanceof Node) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    // ---------------------------------------------------------------------
    // CLIENTE
    // ---------------------------------------------------------------------
    public void addORupdateCliente(ActionEvent e) throws SQLException {
        if (!possuiIdCliente()) {
            addCliente(e);
        } else {
            updateCliente(e);
        }
    }

    public void addCliente(ActionEvent e) throws SQLException {
        try {
            cliente = serviceCliente.registar(
                    txtNome.getText().trim(),
                    txtRazao.getText().trim(),
                    combTipoCliente.getValue(),
                    valor(txtNuit),
                    userData
            );
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (serviceCliente.isOpsSuccess()) {
            showSuccessAlert("Registo adicionado com sucesso.");
            if (cliente != null) {
                preencherCliente(cliente);
                carregarContactos();
                carregarEnderecos();
            }
            actualizarTextosBotoes();
        }
    }

    public void updateCliente(ActionEvent e) throws SQLException {
        try {
            cliente = serviceCliente.actualizar(
                    valor(txtIDCliente),
                    txtNome.getText().trim(),
                    txtRazao.getText().trim(),
                    combTipoCliente.getValue(),
                    valor(txtNuit),
                    userData
            );
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (serviceCliente.isOpsSuccess()) {
            showSuccessAlert("Registo actualizado com sucesso.");
            preencherCliente(cliente);
            actualizarTextosBotoes();
        }
    }

    public void showCliente() throws SQLException {
        preencherCliente(cliente);
        carregarContactos();
        carregarEnderecos();
    }

    public void showClienteByID() throws SQLException {
        if (possuiIdCliente()) {
            cliente = serviceCliente.consultaPorId(valor(txtIDCliente));
            preencherCliente(cliente);
        }
    }

    public void resetCliente() {
        cliente = null;
        limpar(txtIDCliente, txtNome, txtRazao, txtNuit);
        if (combTipoCliente != null) {
            combTipoCliente.getSelectionModel().clearSelection();
            combTipoCliente.setPromptText("Tipo de cliente");
        }
        listaContactos.clear();
        listaEndereco.clear();
        resetContacto();
        resetEndereco();
        actualizarTextosBotoes();
    }

    private void preencherCliente(Cliente c) {
        if (c == null) return;
        cliente = c;
        setTexto(txtIDCliente, String.valueOf(c.getId()));
        if (combTipoCliente != null) combTipoCliente.setValue(c.getTipo());
        setTexto(txtNome, c.getNome_cli());
        setTexto(txtRazao, c.getRazao_cli());
        setTexto(txtNuit, String.valueOf(c.getNuit_cli()));
    }

    // Mantido por compatibilidade com chamadas antigas.
    public void lookDetails() throws SQLException {
        carregarContactos();
        carregarEnderecos();
    }

    public void lookContactos() throws SQLException {
        carregarContactos();
    }

    public void lookEnderecos() throws SQLException {
        carregarEnderecos();
    }

    // Agora os botÃµes limpam/preparam os formulÃ¡rios laterais.
    public void addConAddView(ActionEvent e) {
        if (!garantirClienteGuardado()) return;

        if (e != null && e.getSource() == btnEnderecoView) {
            resetEndereco();
            if (txtCidade != null) txtCidade.requestFocus();
        } else {
            resetContacto();
            if (txtResponsavel != null) txtResponsavel.requestFocus();
        }
    }

    // ---------------------------------------------------------------------
    // CONTACTO
    // ---------------------------------------------------------------------
    public void addORupdateContacto(ActionEvent e) throws SQLException {
        if (!garantirClienteGuardado()) return;
        if (txtIDContacto == null || vazio(txtIDContacto.getText())) {
            addContacto(e);
        } else {
            updateContacto(e);
        }
    }

    public void addContacto(ActionEvent e) throws SQLException {
        if (!garantirClienteGuardado()) return;

        try {
            serviceContacto.registar(
                    valor(txtEmail), valor(txtContacto), valor(txtSite),
                    valor(txtResponsavel), cliente, userData
            );
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (serviceContacto.isOpsSuccess()) {
            showSuccessAlert("Registo adicionado com sucesso.");
            serviceContacto.setOpsSuccess(false);
            carregarContactos();
            resetContacto();
        }
    }

    public void updateContacto(ActionEvent e) throws SQLException {
        try {
            serviceContacto.actualizar(
                    valor(txtIDContacto),
                    valor(txtEmail), valor(txtContacto), valor(txtSite),
                    valor(txtResponsavel), userData
            );
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (serviceContacto.isOpsSuccess()) {
            showSuccessAlert("Registo actualizado com sucesso.");
            serviceContacto.setOpsSuccess(false);
            carregarContactos();
            resetContacto();
        }
    }

    public void deleteContacto(ActionEvent event) throws SQLException {
        if (!confirmarExclusao("contacto")) return;

        try {
            serviceContacto.excluir(valor(txtIDContacto));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceContacto.isOpsSuccess()) {
            showSuccessAlert("Registo excluido com sucesso.");
            serviceContacto.setOpsSuccess(false);
            carregarContactos();
            resetContacto();
        }
    }

    @FXML
    public void selecionaContacto(MouseEvent event) {
        if (listaContactosView == null) return;
        ContactoCliente seleccionado = listaContactosView.getSelectionModel().getSelectedItem();
        preencherContacto(seleccionado);
    }

    // Mantido por compatibilidade com o controlador antigo.
    public void showContactos() {
        Object data = SessaoAplicacao.getData();
        if (data instanceof ContactoCliente) preencherContacto((ContactoCliente) data);
    }

    private void preencherContacto(ContactoCliente contacto) {
        if (contacto == null) return;
        setTexto(txtIDContacto, String.valueOf(contacto.getId()));
        setTexto(txtResponsavel, contacto.getResponsavel());
        setTexto(txtContacto, contacto.getContacto_cli());
        setTexto(txtEmail, contacto.getEmail_cli());
        setTexto(txtSite, contacto.getWebsite_cli());
        actualizarTextosBotoes();
    }

    public void resetContacto() {
        limpar(txtIDContacto, txtResponsavel, txtContacto, txtEmail, txtSite);
        if (listaContactosView != null) listaContactosView.getSelectionModel().clearSelection();
        actualizarTextosBotoes();
    }

    private void carregarContactos() throws SQLException {
        listaContactos.clear();
        if (cliente != null) {
            ObservableList<ContactoCliente> encontrados = serviceContacto.consultaPorCliente(cliente);
            if (encontrados != null) listaContactos.addAll(encontrados);
        }
    }

    // ---------------------------------------------------------------------
    // ENDEREÃ‡O
    // ---------------------------------------------------------------------
    public void addORupdateEndereco(ActionEvent e) throws SQLException {
        if (!garantirClienteGuardado()) return;
        if (txtIDEndereco == null || vazio(txtIDEndereco.getText())) {
            addEndereco(e);
        } else {
            updateEndereco(e);
        }
    }

    public void addEndereco(ActionEvent e) throws SQLException {
        if (!garantirClienteGuardado()) return;

        try {
            serviceEndereco.registar(
                    tipoEnderecoSeleccionado(), valor(txtAvenida), valor(txtBairro),
                    valor(txtCidade), valor(txtCodigooPostal),
                    valor(txtNumeroEndereco), valor(combProvincia),
                    valor(txtRua), cliente, userData
            );
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (serviceEndereco.isOpsSuccess()) {
            showSuccessAlert("Registo adicionado com sucesso.");
            serviceEndereco.setOpsSuccess(false);
            carregarEnderecos();
            resetEndereco();
        }
    }

    public void updateEndereco(ActionEvent e) throws SQLException {
        try {
            serviceEndereco.actualizar(
                    valor(txtIDEndereco),
                    tipoEnderecoSeleccionado(), valor(txtAvenida), valor(txtBairro),
                    valor(txtCidade), valor(txtCodigooPostal),
                    valor(txtNumeroEndereco), valor(combProvincia),
                    valor(txtRua), userData
            );
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }

        if (serviceEndereco.isOpsSuccess()) {
            showSuccessAlert("Registo actualizado com sucesso.");
            serviceEndereco.setOpsSuccess(false);
            carregarEnderecos();
            resetEndereco();
        }
    }

    public void deleteEndereco(ActionEvent event) throws SQLException {
        if (txtIDEndereco == null || vazio(txtIDEndereco.getText())) return;
        if (!confirmarExclusao("endereÃ§o")) return;

        try {
            serviceEndereco.excluir(valor(txtIDEndereco));
        } catch (ServiceException ex) {
            showErroAlert(ex.getMessage());
            return;
        }
        if (serviceEndereco.isOpsSuccess()) {
            showSuccessAlert("Registo excluido com sucesso.");
            serviceEndereco.setOpsSuccess(false);
            carregarEnderecos();
            resetEndereco();
        }
    }

    @FXML
    public void selecionaEndereco(MouseEvent event) {
        if (listaEnderecosView == null) return;
        EnderecoCliente seleccionado = listaEnderecosView.getSelectionModel().getSelectedItem();
        preencherEndereco(seleccionado);
    }

    // Mantido por compatibilidade com o controlador antigo.
    public void showEndereco() {
        Object data = SessaoAplicacao.getData();
        if (data instanceof EnderecoCliente) preencherEndereco((EnderecoCliente) data);
    }

    private void preencherEndereco(EnderecoCliente endereco) {
        if (endereco == null) return;
        setTexto(txtIDEndereco, String.valueOf(endereco.getId()));
        setTexto(txtCidade, endereco.getCidade_cli());
        setTexto(txtAvenida, endereco.getAvenida_cli());
        setTexto(txtCodigooPostal, String.valueOf(endereco.getCodigoPostal_cli()));
        setTexto(txtNumeroEndereco, String.valueOf(endereco.getNumero_cli()));
        setTexto(txtRua, endereco.getRua_cli());
        setTexto(txtBairro, endereco.getBairro_cli());
        if (combProvincia != null) combProvincia.setValue(endereco.getProvincia_cli());
        if (combTipoEndereco != null) combTipoEndereco.setValue(endereco.getTipo_cli());
        seleccionarRadioDoTipo(endereco.getTipo_cli());
        actualizarTextosBotoes();
    }

    public void resetEndereco() {
        limpar(txtIDEndereco, txtCidade, txtAvenida, txtCodigooPostal,
                txtNumeroEndereco, txtRua, txtBairro);
        if (combProvincia != null) combProvincia.getSelectionModel().clearSelection();
        if (combTipoEndereco != null) combTipoEndereco.getSelectionModel().clearSelection();
        finalidadeEndereco.selectToggle(null);
        if (listaEnderecosView != null) listaEnderecosView.getSelectionModel().clearSelection();
        actualizarTextosBotoes();
    }

    private void carregarEnderecos() throws SQLException {
        listaEndereco.clear();
        if (cliente != null) {
            ObservableList<EnderecoCliente> encontrados = serviceEndereco.consultaPorCliente(cliente);
            if (encontrados != null) listaEndereco.addAll(encontrados);
        }
    }

    // ---------------------------------------------------------------------
    // UtilitÃ¡rios
    // ---------------------------------------------------------------------
    private boolean garantirClienteGuardado() {
        if (cliente != null) return true;
        if (possuiIdCliente()) {
            try {
                cliente = serviceCliente.consultaPorId(valor(txtIDCliente));
                return cliente != null;
            } catch (NumberFormatException ex) {
                LOG.log(Level.WARNING, "NÃ£o foi possÃ­vel recuperar o cliente.", ex);
            }
        }
        mostrarAviso("Cliente nÃ£o guardado", "Guarde primeiro os dados gerais do cliente.");
        return false;
    }

    private boolean possuiIdCliente() {
        return txtIDCliente != null && !vazio(txtIDCliente.getText());
    }

    private String tipoEnderecoSeleccionado() {
        if (combTipoEndereco != null && combTipoEndereco.getValue() != null) {
            return combTipoEndereco.getValue();
        }
        if (radEnvio != null && radEnvio.isSelected()) return "Envio";
        if (radFacturacao != null && radFacturacao.isSelected()) return "FacturaÃ§Ã£o";
        return null;
    }

    private void sincronizarTipoEndereco(String tipo) {
        if (combTipoEndereco != null) combTipoEndereco.setValue(tipo);
    }

    private void seleccionarRadioDoTipo(String tipo) {
        if (tipo == null) return;
        if ("Envio".equalsIgnoreCase(tipo) && radEnvio != null) radEnvio.setSelected(true);
        if ("FacturaÃ§Ã£o".equalsIgnoreCase(tipo) && radFacturacao != null) radFacturacao.setSelected(true);
    }

    private boolean confirmarExclusao(String item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar remoÃ§Ã£o");
        alert.setHeaderText("Remover " + item);
        alert.setContentText("Deseja realmente remover este " + item + "?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void actualizarTextosBotoes() {
        if (btnAddCliente != null) btnAddCliente.setText(possuiIdCliente() ? "Actualizar cliente" : "Guardar cliente");
        if (btnAddContacto != null) btnAddContacto.setText(txtIDContacto != null && !vazio(txtIDContacto.getText()) ? "Actualizar contacto" : "Guardar contacto");
        if (btnAddEndereco != null) btnAddEndereco.setText(txtIDEndereco != null && !vazio(txtIDEndereco.getText()) ? "Actualizar endereÃ§o" : "Guardar endereÃ§o");
    }

    private void mostrarAviso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private static boolean vazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private static String valor(TextField campo) {
        return campo == null || campo.getText() == null ? "" : campo.getText().trim();
    }

    private static String valor(ComboBox<String> combo) {
        return combo == null || combo.getValue() == null ? null : combo.getValue();
    }

    private static void limpar(TextField... campos) {
        for (TextField campo : campos) {
            if (campo != null) campo.clear();
        }
    }

    private static void setTexto(TextField campo, String texto) {
        if (campo != null) campo.setText(texto == null ? "" : texto);
    }

    private static String texto(String valor, String padrao) {
        return vazio(valor) ? padrao : valor;
    }
}

