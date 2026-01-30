/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package jminvsm.views.modulo_venda.clienteView.clienteAdd;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import jminvsm.SysFact;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.cliente.endereco.ServiceEndereco;
import jminvsm.util.ButtonUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class EnderecoController implements Initializable {

    private Usuario userData;
    private ServiceEndereco serviceEndereco;
    private ServiceCliente serviceCliente;
    private Cliente cliente;
    private EnderecoCliente enderecoCliente;

    @FXML
    private Button btnAddEndereco;
    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox<String> combProvincia;
    @FXML
    private RadioButton radFacturacao;
    @FXML
    private RadioButton radEnvio;
    @FXML
    private ComboBox<String> combTipoEndereco;

    @FXML
    private TableView<EnderecoCliente> tabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> tipotabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, Integer> numerotabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> provinciatabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> ruatabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> btntabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> cidadetabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, Integer> codigopostaltabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, Integer> IDtabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> avenidatabelaEndereco;
    @FXML
    private TableColumn<EnderecoCliente, String> bairrotabelaEndereco;
    @FXML
    private TextField txtAvenida;
    @FXML
    private TextField txtBairro;
    @FXML
    private TextField txtCidade;
    @FXML
    private TextField txtCodigooPostal;
    @FXML
    private TextField txtIDEndereco;
    @FXML
    private TextField txtNumeroEndereco;
    @FXML
    private TextField txtRua;
    @FXML
    private Label labHeadTitle;

    public void addORupdateEndereco(ActionEvent e) throws SQLException {
        if ("".equals(txtIDEndereco.getText())) {
            addEndereco(e);
        } else {
            updateEndereco(e);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.userData = SysFact.getUserData();
        if (SysFact.getData() != null) {
            if (SysFact.getData() instanceof Cliente) {
                this.cliente = (Cliente) SysFact.getData();
            }
        }
        combTipoEndereco.setItems(FXCollections.observableArrayList("Facturação", "Envio"));
        combProvincia.setItems(FXCollections.observableArrayList(
                "Maputo(Cidade)",
                "Maputo(Provincia)",
                "Gaza",
                "Inhambane",
                "Sofala",
                "Manica",
                "Tete",
                "Zambezia",
                "Napula",
                "Niassa",
                "Cabo Delgado"
        )
        );

        try {
            serviceCliente = new ServiceCliente();
            serviceEndereco = new ServiceEndereco();
            showEndereco();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(AddClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);

    }

    public void showEndereco() {
        if ((SysFact.getData() instanceof EnderecoCliente) && SysFact.getData() != null) {
            enderecoCliente = (EnderecoCliente) SysFact.getData();
            txtIDEndereco.setText(enderecoCliente.getId() + "");
            txtCidade.setText(enderecoCliente.getCidade_cli());
            txtAvenida.setText(enderecoCliente.getAvenida_cli());
            txtCodigooPostal.setText(String.valueOf(enderecoCliente.getCodigoPostal_cli()));
            txtNumeroEndereco.setText(String.valueOf(enderecoCliente.getNumero_cli()));
            txtRua.setText(enderecoCliente.getRua_cli());
            txtBairro.setText(enderecoCliente.getBairro_cli());
            combProvincia.setValue(enderecoCliente.getProvincia_cli());
            if ("Envio".equals(enderecoCliente.getTipo_cli())) {
                radEnvio.setSelected(true);
                radFacturacao.setSelected(false);
            }
            if ("Facturação".equals(enderecoCliente.getTipo_cli())) {
                radEnvio.setSelected(false);
                radFacturacao.setSelected(true);
            }
            ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);

        }
    }

    public void addEndereco(ActionEvent e) throws SQLException {
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        serviceEndereco.registar(combTipoEndereco.getValue(), txtAvenida.getText(), txtBairro.getText(),
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(), cliente, userData);
        if (serviceEndereco.isOpsSuccess()) {
            serviceEndereco.setOpsSuccess(false);
            for (EnderecoCliente ed : serviceEndereco.consultaEnderecosPorCLiente(cliente.getId())) {
                SysFact.setData(ed);
            }
            showEndereco();
        }
    }

    public void updateEndereco(ActionEvent e) throws SQLException {
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        serviceEndereco.actualizar(Integer.valueOf(txtIDEndereco.getText()), combTipoEndereco.getValue(), txtAvenida.getText(), txtBairro.getText(),
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(), userData);
        if (serviceEndereco.isOpsSuccess()) {
            serviceEndereco.setOpsSuccess(false);
        }
    }

    public void deleteEndereco(ActionEvent event) throws SQLException {
        if ((event.getSource() == btnDelete) && (!"".equals(txtIDEndereco.getText()) && !txtIDEndereco.getText().isEmpty())) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnApagar = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnApagar, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnApagar) {
                serviceEndereco.excluir(Integer.parseInt(txtIDEndereco.getText()));
                if (serviceEndereco.isOpsSuccess()) {
                    serviceEndereco.setOpsSuccess(false);
                    /*
                    O que pretende aqui e encontrar a AnchorPane onde esta o botao que disparou esta accao dentro da VBox e remover essa AnchorPane
                     */
                    // botão que disparou a acção
                    Node source = (Node) event.getSource();
                    // AnchorPane que contém este botão
                    AnchorPane contactoPane1 = (AnchorPane) source.getParent();
                    // AnchorPane que contém essa AnchorPane que contém este botão
                    AnchorPane contactoPane2 = (AnchorPane) contactoPane1.getParent();
                    // VBox que contém essa AnchorPane que contém essa AnchorPane que contém este botão
                    VBox vbox = (VBox) contactoPane2.getParent();
                    // remover a view inteira
                    vbox.getChildren().remove(contactoPane2);

                }
            }
        }
    }
}
