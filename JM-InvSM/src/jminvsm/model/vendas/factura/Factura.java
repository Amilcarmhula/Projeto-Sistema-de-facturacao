/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.vendas.factura;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.transacao.Transacao;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.documetos.DocumentosComerciais;
import jminvsm.model.vendas.factura.itens.Item;
import jminvsm.service.vendas.factura.ServiceFactura;
import jminvsm.service.vendas.factura.itens.ServiceItem;
import jminvsm.util.AlertUtilities;
import jminvsm.util.report.ReportUtilities;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author JM-Tecnologias
 */
public class Factura {

    private String numero_fac;
    private String data_emissao; // Alterar o fild do jasperreport para que o dado possa refletir no relatorio
    private String data_vencimento;
    private double total;
    private double totalPago;
    private double saldoPendente;
    private int numeroParcelas;
    private String condicaoPagamento;
    private String estado;
    private DocumentosComerciais docComerciais;
    private List<Item> itens;
    private Item item;
    private Transacao transacoes;
    private Empresa empresa;
    private Cliente cliente;
    private Usuario usuario;
    private ServiceFactura serviceFactura;
    private ServiceItem serviceItens;

    public Factura() {
    }

    public Factura(String numero_fac, String data_emissao, String data_vencimento, double total, double totalPago, double saldoPendente, int numeroParcelas, String condicaoPagamento, String estado, DocumentosComerciais docComerciais, List<Item> itens, Item item, Transacao transacoes, Empresa empresa, Cliente cliente, Usuario usuario) throws SQLException {
        this.numero_fac = numero_fac;
        this.data_emissao = data_emissao;
        this.data_vencimento = data_vencimento;
        this.total = total;
        this.totalPago = totalPago;
        this.saldoPendente = saldoPendente;
        this.numeroParcelas = numeroParcelas;
        this.condicaoPagamento = condicaoPagamento;
        this.estado = estado;
        this.docComerciais = docComerciais;
        this.itens = itens;
        this.item = item;
        this.transacoes = transacoes;
        this.empresa = empresa;
        this.cliente = cliente;
        this.usuario = usuario;

    }

    public void criarFactura(String numeroFactura) throws SQLException {
        serviceFactura = new ServiceFactura();
        serviceItens = new ServiceItem();
        List<Factura> detalhesFactura = new ArrayList<Factura>();

        Factura detalhes = serviceFactura.consultaDadosFactura(numeroFactura);
        detalhesFactura.add(detalhes);
        JRBeanCollectionDataSource facturaDetalhesDATASOURCE = new JRBeanCollectionDataSource(detalhesFactura);

        // Configurar o DataSource para os itens
        JRBeanCollectionDataSource itemDATASOURCE = new JRBeanCollectionDataSource(serviceItens.consultaItensFactura(numeroFactura));

        // Passar parâmetros para o relatório
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("TABLE_DATA_SOURCE", itemDATASOURCE);
        parametros.put("NOMEFACTURA", numeroFactura);

        String path = "";
        if (numeroFactura.endsWith("/1") || numeroFactura.endsWith("/2")) {
            path = "C:\\JM-InvSM\\compiled\\Factura.jasper";
        } else if (numeroFactura.endsWith("/3")) {
            path = "C:\\JM-InvSM\\compiled\\FacturaRecibo.jasper";
        } else {
            path = "C:\\JM-InvSM\\compiled\\FacturaSimplificada.jasper";
        }

        try {
            JasperReport jReport = (JasperReport) JRLoader.loadObjectFromFile(path);

            // Preencher o relatório
            JasperPrint printableReport = JasperFillManager.fillReport(jReport, parametros, facturaDetalhesDATASOURCE);
            /*
            Configura escolha decisao de escolha de visualizacao e impressao ou salvar documento
             */
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Opcoes de relatorio.");
            dialog.setContentText("Deseja salvar ou visualizar a Factura?");
            dialog.initModality(Modality.APPLICATION_MODAL);

            // Configurando o botão View/Save
            ButtonType btnVisualizar = new ButtonType("Visualizar", ButtonType.OK.getButtonData());
            ButtonType btnClose = new ButtonType("Close", ButtonType.CANCEL.getButtonData());
            ButtonType btnSaveView = new ButtonType("Salvar", ButtonType.YES.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(btnVisualizar, btnSaveView, btnClose);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == btnVisualizar) {
                // Apenas exibir o relatório
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Relatorio");
                viewer.setVisible(true);
            } else if (result.get() == btnSaveView) {
                //Salva o relatório NOMEFACTURA
                String numero = (String) parametros.get("NOMEFACTURA");
                JasperExportManager.exportReportToPdfFile(printableReport, "C:/JM-InvSM/FACTURA-" + numero.replace("/", "-") + ".pdf");
                // Exibir o relatório
                JasperViewer viewer = new JasperViewer(printableReport, false);
                viewer.setTitle("Factura");
                viewer.setVisible(true);
            } else if (result.get() == btnClose) {
                System.out.println("Aqui nao salva nem visualiza; esta associado ao botao close da tela!");
            }
        } catch (JRException ex) {
            AlertUtilities.showDialog("Erro", "Erro ao gerar relatorio de factura: " + ex);
        }

    }

    public String getNumero_fac() {
        return numero_fac;
    }

    public void setNumero_fac(String numero_fac) {
        this.numero_fac = numero_fac;
    }

    public String getData_emissao() {
        return data_emissao;
    }

    public void setData_emissao(String data_emissao) {
        this.data_emissao = data_emissao;
    }

    public String getData_vencimento() {
        return data_vencimento;
    }

    public void setData_vencimento(String data_vencimento) {
        this.data_vencimento = data_vencimento;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public String getEstado() {
        return estado;
    }

    public double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(double totalPago) {
        this.totalPago = totalPago;
    }

    public double getSaldoPendente() {
        return saldoPendente;
    }

    public void setSaldoPendente(double saldoPendente) {
        this.saldoPendente = saldoPendente;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public String getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(String condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Transacao getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(Transacao transacoes) {
        this.transacoes = transacoes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public DocumentosComerciais getDocComerciais() {
        return docComerciais;
    }

    public void setDocComerciais(DocumentosComerciais docComerciais) {
        this.docComerciais = docComerciais;
    }

    public String getNomePagamento() {
        if (transacoes != null) {
            return transacoes.getMetodo().getNomePagamento();
        }
        return "";
    }

    public double getTrocos() {
        if (transacoes != null) {
            return transacoes.getTrocos();
        }
        return -1;
    }

    public double getValorRecebido() {
        if (transacoes != null) {
            return transacoes.getValorRecebido();
        }
        return -1;
    }

    public String getNome_doc() {
        if (docComerciais != null) {
            return docComerciais.getNome_doc();
        }
        return null;
    }

    public String getRazao_emp() {
        if (empresa != null) {
            return empresa.getRazao_emp();//getDocComerciais().getNome_doc()
        }
        return null;
    }

    public int getNuit_emp() {
        if (empresa != null) {
            return empresa.getNuit_emp();//
        }
        return -1;
    }

    public String getAvenida_emp() {
        if (empresa != null) {
            return empresa.getEnderecoEmpresa().getAvenida_emp();//
        }
        return null;
    }

    public String getBairro_emp() {
        if (empresa != null) {
            return empresa.getEnderecoEmpresa().getBairro_emp();//
        }
        return null;
    }

    public String getCidade_emp() {
        if (empresa != null) {
            return empresa.getEnderecoEmpresa().getCidade_emp();//
        }
        return null;
    }

    public String getProvincia_emp() {
        if (empresa != null) {
            return empresa.getEnderecoEmpresa().getProvincia_emp();//
        }
        return null;
    }

    public String getRua_emp() {
        if (empresa != null) {
            return empresa.getEnderecoEmpresa().getRua_emp();//
        }
        return null;
    }

    public int getCodigoPostal_emp() {
        if (empresa != null) {
            return empresa.getEnderecoEmpresa().getCodigoPostal_emp();//
        }
        return -1;
    }

    public int getNumero_emp() {
        if (empresa != null) {
            return empresa.getEnderecoEmpresa().getNumero_emp();//
        }
        return -1;
    }

    public String getContacto_emp() {
        if (empresa != null) {
            return empresa.getContactoEmpresa().getContacto_emp();//
        }
        return null;
    }

    public String getEmail_emp() {
        if (empresa != null) {
            return empresa.getContactoEmpresa().getEmail_emp();//
        }
        return null;
    }

    public String getWebsite_emp() {
        if (empresa != null) {
            return empresa.getContactoEmpresa().getWebsite_emp();//
        }
        return null;
    }

    public String getRazao_cli() {
        if (cliente != null) {
            return cliente.getRazao_cli();//
        }
        return null;
    }

    public int getNuit_cli() {
        if (cliente != null) {
            return cliente.getNuit_cli();//
        }
        return -1;
    }

    public String getContacto_cli() {
        if (cliente != null) {
            return cliente.getContactoCliente().getContacto_cli();//
        }
        return null;
    }

    public String getEmail_cli() {
        if (cliente != null) {
            return cliente.getContactoCliente().getEmail_cli();//
        }
        return null;
    }

    public String getAvenida_cli() {
        if (cliente != null) {
            return cliente.getEnderecoCliente().getAvenida_cli();//
        }
        return null;
    }

    public String getBairro_cli() {
        if (cliente != null) {
            return cliente.getEnderecoCliente().getBairro_cli();//
        }
        return null;
    }

    public String getCidade_cli() {
        if (cliente != null) {
            return cliente.getEnderecoCliente().getCidade_cli();//
        }
        return null;
    }

    public String getProvincia_cli() {
        if (cliente != null) {
            return cliente.getEnderecoCliente().getProvincia_cli();//
        }
        return null;
    }

    public String getRua_cli() {
        if (cliente != null) {
            return cliente.getEnderecoCliente().getRua_cli();//
        }
        return null;
    }

    public int getCodigoPostal_cli() {
        if (cliente != null) {
            return cliente.getEnderecoCliente().getCodigoPostal_cli();//
        }
        return -1;
    }

    public int getNumero_cli() {
        if (cliente != null) {
            return cliente.getEnderecoCliente().getNumero_cli();//
        }
        return -1;
    }

    public String getNome_banco() {
        if (empresa != null) {
            return empresa.getContaBancaria().getNome_banco();//
        }
        return null;
    }

    public String getNib() {
        if (empresa != null) {
            return empresa.getContaBancaria().getNib();//
        }
        return null;
    }

    public String getNuib() {
        if (empresa != null) {
            return empresa.getContaBancaria().getNuib();//
        }
        return null;
    }

    public String getNumero_conta() {
        if (empresa != null) {
            return empresa.getContaBancaria().getNumero_conta();//
        }
        return null;
    }

}
