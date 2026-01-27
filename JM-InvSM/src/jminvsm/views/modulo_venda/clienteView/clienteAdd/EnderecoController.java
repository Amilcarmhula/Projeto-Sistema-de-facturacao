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
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jminvsm.SysFact;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.cliente.ServiceCliente;
import jminvsm.service.cliente.contacto.ServiceContacto;
import jminvsm.service.cliente.endereco.ServiceEndereco;
import jminvsm.util.ButtonUtilities;
import jminvsm.util.LoadAndMoveUtilities;

/**
 * FXML Controller class
 *
 * @author JM-Tecnologias
 */
public class EnderecoController implements Initializable {

    private Usuario userData;
    private ServiceEndereco serviceEndereco;

   
    @FXML
    private Button btnAddEndereco;

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
//        Object obj = SysFact.getData();
//        int vectorDados[] = (int[]) obj;
//
//        labHeadTitle.setText("Endereco #"+vectorDados[2]);
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
            serviceEndereco = new ServiceEndereco();
            showEndereco();
            // TODO
        } catch (SQLException ex) {
            Logger.getLogger(AddClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void showEndereco() throws SQLException {
       if ((SysFact.getData() instanceof EnderecoCliente) && SysFact.getData() != null) {
            EnderecoCliente ed = (EnderecoCliente)SysFact.getData();
                txtIDEndereco.setText(ed.getId()+"");
                txtCidade.setText(ed.getCidade_cli());
                txtAvenida.setText(ed.getAvenida_cli());
                txtCodigooPostal.setText(String.valueOf(ed.getCodigoPostal_cli()));
                txtNumeroEndereco.setText(String.valueOf(ed.getNumero_cli()));
                txtRua.setText(ed.getRua_cli());
                txtBairro.setText(ed.getBairro_cli());
                combProvincia.setValue(ed.getProvincia_cli());
                if("Envio".equals(ed.getTipo_cli())){
                    radEnvio.setSelected(true);
                    radFacturacao.setSelected(false);
                }
                if("Facturação".equals(ed.getTipo_cli())){
                    radEnvio.setSelected(false);
                    radFacturacao.setSelected(true);
                }
        }
    }



 

    public void addEndereco(ActionEvent e) throws SQLException {
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        Integer id = Integer.valueOf(txtIDEndereco.getText().equals("") ? "0" : txtIDEndereco.getText());
        serviceEndereco.registar(combTipoEndereco.getValue(), txtAvenida.getText(), txtBairro.getText(),
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(), id, userData);
        if (serviceEndereco.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
        }
    }



    public void updateEndereco(ActionEvent e) throws SQLException {
        Integer postal = Integer.valueOf(txtCodigooPostal.getText().equals("") ? "0" : txtCodigooPostal.getText());
        Integer numero = Integer.valueOf(txtNumeroEndereco.getText().equals("") ? "0" : txtNumeroEndereco.getText());
        serviceEndereco.actualizar(Integer.valueOf(txtIDEndereco.getText()), combTipoEndereco.getValue(), txtAvenida.getText(), txtBairro.getText(),
                txtCidade.getText(), postal, numero, combProvincia.getValue(),
                txtRua.getText(), userData);
        if (serviceEndereco.isOpsSuccess()) {
            showEndereco();
            resetEndereco();
        }
    }



    public void deleteEndereco(MouseEvent event, EnderecoCliente x) throws SQLException {
        if (event.getButton().name().equals("SECONDARY")) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Aviso.");
            dialog.setContentText("Deseja excluir o item?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            ButtonType btnDelete = new ButtonType("Apagar", ButtonType.OK.getButtonData());
            ButtonType btnCancel = new ButtonType("Cancelar", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnDelete, btnCancel);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnDelete) {
                serviceEndereco.excluir(x.getId());
                showEndereco();
                resetEndereco();
            } else if (result.get() == btnCancel) {
                resetEndereco();
            }
        }
    }


    public void resetEndereco() {
        txtIDEndereco.setText("");
        txtAvenida.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtCodigooPostal.setText("");
        txtNumeroEndereco.setText("");
        combProvincia.setPromptText("Provincia");
        txtRua.setText("");
        combTipoEndereco.setPromptText("Tipo de endereco");
        ButtonUtilities.buttonChangeText(btnAddEndereco, txtIDEndereco);
    }

}
