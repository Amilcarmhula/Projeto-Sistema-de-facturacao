/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.vendas.factura;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sysfact.dao.vendas.factura.FacturaDAO;
import sysfact.dao.vendas.itens.ItemDAO;
import sysfact.model.cliente.Cliente;
import sysfact.model.empresa.Empresa;
import sysfact.model.usuario.Usuario;
import sysfact.model.vendas.documetos.DocumentosComerciais;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.util.report.ReportUtilities;

/**
 *
 * @author JM-Tecnologias
 */
public class Factura {

    private String numero_fac;
    private String data_criacao;
    private String data_vencimento;
    private DocumentosComerciais docComerciais;
    private List<Item> itens;
    private boolean estado;
    private Empresa empresa;
    private Cliente cliente;
    private Usuario usuario;

    public Factura() {
        super();
    }

    public Factura(String numero, String data_vencimento, List<Item> itens,
            boolean estado, Empresa empresa, Cliente cliente, DocumentosComerciais docComerciais) {
        super();
        this.numero_fac = numero;
        this.data_vencimento = data_vencimento;
        this.itens = itens;
        this.estado = estado;
        this.empresa = empresa;
        this.cliente = cliente;
        this.docComerciais = docComerciais;
    }

    public void criaRelatorio(String numeroFactura) throws SQLException {
//        String numeroFactura = "2024-1010/2"; // Número da fatura

        List<Factura> detalhesFactura = new ArrayList<Factura>();
        Factura detalhes = new FacturaDAO().getFacturaData(numeroFactura);
        detalhesFactura.add(detalhes);
        JRBeanCollectionDataSource facturaDetalhesDATASOURCE = new JRBeanCollectionDataSource(detalhesFactura);

        ItemDAO x = new ItemDAO();
        List<Item> detalhesItens = x.getItensData(numeroFactura);

        // Configurar o DataSource para os itens
        JRBeanCollectionDataSource itemDataSource = new JRBeanCollectionDataSource(detalhesItens);

        // Passar parâmetros para o relatório
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("TABLE_DATA_SOURCE", itemDataSource); 
        parametros.put("NOMEFACTURA", numeroFactura);
//        String path = "../../reports/vendas/facturas/PF/RelatorioFacturas.jrxml";
        String path = "C:\\Users\\JM-Tecnologias\\Documents\\NetBeansProjects\\Projeto Sistema de facturacao\\SysFact\\src\\sysfact\\reports\\vendas\\facturas\\PF\\RelatorioFacturas.jrxml";
        
        ReportUtilities.genarateJasperReport(path, parametros, facturaDetalhesDATASOURCE);
    }

    public String getNumero_fac() {
        return numero_fac;
    }

    public void setNumero_fac(String numero_fac) {
        this.numero_fac = numero_fac;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getData_vencimento() {
        return data_vencimento;
    }

    public void setData_vencimento(String data_vencimento) {
        this.data_vencimento = data_vencimento;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
