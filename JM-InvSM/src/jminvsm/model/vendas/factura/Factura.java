/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.vendas.factura;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.empresa.contacto.ContactoEmpresa;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.empresa.endereco.EnderecoEmpresa;
import jminvsm.model.enums.EstadoFactura;
import jminvsm.model.transacao.Transacao;
import jminvsm.model.usuario.Usuario;
import static jminvsm.model.util.ModelConverters.toBigDecimal;
import static jminvsm.model.util.ModelConverters.toDateString;
import static jminvsm.model.util.ModelConverters.toDouble;
import static jminvsm.model.util.ModelConverters.toLocalDate;
import jminvsm.model.vendas.documetos.DocumentosComerciais;
import jminvsm.model.vendas.factura.itens.Item;

/**
 *
 * @author JM-Tecnologias
 */
public class Factura {

    private String numero_fac;
    private LocalDate data_emissao; // Alterar o fild do jasperreport para que o dado possa refletir no relatorio
    private LocalDate data_vencimento;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalPago = BigDecimal.ZERO;
    private BigDecimal saldoPendente = BigDecimal.ZERO;
    private int numeroParcelas;
    private String condicaoPagamento;
    private EstadoFactura estado;
    private DocumentosComerciais docComerciais;
    private List<Item> itens;
    private Item item;
    private Transacao transacoes;
    private Empresa empresa;
    private Cliente cliente;
    private Usuario usuario;

    public Factura() {
    }

    public Factura(String numero_fac, String data_emissao, String data_vencimento, double total, double totalPago, double saldoPendente, int numeroParcelas, String condicaoPagamento, String estado, DocumentosComerciais docComerciais, List<Item> itens, Item item, Transacao transacoes, Empresa empresa, Cliente cliente, Usuario usuario) throws SQLException {
        this.numero_fac = numero_fac;
        this.data_emissao = toLocalDate(data_emissao);
        this.data_vencimento = toLocalDate(data_vencimento);
        this.total = toBigDecimal(total);
        this.totalPago = toBigDecimal(totalPago);
        this.saldoPendente = toBigDecimal(saldoPendente);
        this.numeroParcelas = numeroParcelas;
        this.condicaoPagamento = condicaoPagamento;
        this.estado = EstadoFactura.fromValue(estado);
        this.docComerciais = docComerciais;
        this.itens = itens;
        this.item = item;
        this.transacoes = transacoes;
        this.empresa = empresa;
        this.cliente = cliente;
        this.usuario = usuario;

    }

    public String getNumero_fac() {
        return numero_fac;
    }


    public void setNumero_fac(String numero_fac) {
        this.numero_fac = numero_fac;
    }

    public String getNumeroFactura() {
        return numero_fac;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numero_fac = numeroFactura;
    }

    public String getData_emissao() {
        return toDateString(data_emissao);
    }

    public void setData_emissao(String data_emissao) {
        this.data_emissao = toLocalDate(data_emissao);
    }

    public LocalDate getDataEmissao() {
        return data_emissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.data_emissao = dataEmissao;
    }

    public String getData_vencimento() {
        return toDateString(data_vencimento);
    }

    public void setData_vencimento(String data_vencimento) {
        this.data_vencimento = toLocalDate(data_vencimento);
    }

    public LocalDate getDataVencimento() {
        return data_vencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.data_vencimento = dataVencimento;
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
        return estado != null ? estado.getValor() : null;
    }

    public double getTotalPago() {
        return toDouble(totalPago);
    }

    public void setTotalPago(double totalPago) {
        this.totalPago = toBigDecimal(totalPago);
    }

    public BigDecimal getTotalPagoDecimal() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public double getSaldoPendente() {
        return toDouble(saldoPendente);
    }

    public void setSaldoPendente(double saldoPendente) {
        this.saldoPendente = toBigDecimal(saldoPendente);
    }

    public BigDecimal getSaldoPendenteDecimal() {
        return saldoPendente;
    }

    public void setSaldoPendente(BigDecimal saldoPendente) {
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
        this.estado = EstadoFactura.fromValue(estado);
    }

    public EstadoFactura getEstadoEnum() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
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
        return toDouble(total);
    }

    public void setTotal(double total) {
        this.total = toBigDecimal(total);
    }

    public BigDecimal getTotalDecimal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
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
        if (transacoes != null && transacoes.getMetodo() != null) {
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
        EnderecoEmpresa endereco = getEnderecoEmpresaSafe();
        if (endereco != null) {
            return endereco.getAvenida_emp();//
        }
        return null;
    }

    public String getBairro_emp() {
        EnderecoEmpresa endereco = getEnderecoEmpresaSafe();
        if (endereco != null) {
            return endereco.getBairro_emp();//
        }
        return null;
    }

    public String getCidade_emp() {
        EnderecoEmpresa endereco = getEnderecoEmpresaSafe();
        if (endereco != null) {
            return endereco.getCidade_emp();//
        }
        return null;
    }

    public String getProvincia_emp() {
        EnderecoEmpresa endereco = getEnderecoEmpresaSafe();
        if (endereco != null) {
            return endereco.getProvincia_emp();//
        }
        return null;
    }

    public String getRua_emp() {
        EnderecoEmpresa endereco = getEnderecoEmpresaSafe();
        if (endereco != null) {
            return endereco.getRua_emp();//
        }
        return null;
    }

    public int getCodigoPostal_emp() {
        EnderecoEmpresa endereco = getEnderecoEmpresaSafe();
        if (endereco != null) {
            return endereco.getCodigoPostal_emp();//
        }
        return -1;
    }

    public int getNumero_emp() {
        EnderecoEmpresa endereco = getEnderecoEmpresaSafe();
        if (endereco != null) {
            return endereco.getNumero_emp();//
        }
        return -1;
    }

    public String getContacto_emp() {
        ContactoEmpresa contacto = getContactoEmpresaSafe();
        if (contacto != null) {
            return contacto.getContacto_emp();//
        }
        return null;
    }

    public String getEmail_emp() {
        ContactoEmpresa contacto = getContactoEmpresaSafe();
        if (contacto != null) {
            return contacto.getEmail_emp();//
        }
        return null;
    }

    public String getWebsite_emp() {
        ContactoEmpresa contacto = getContactoEmpresaSafe();
        if (contacto != null) {
            return contacto.getWebsite_emp();//
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
        ContactoCliente contacto = getContactoClienteSafe();
        if (contacto != null) {
            return contacto.getContacto_cli();//
        }
        return null;
    }

    public String getEmail_cli() {
        ContactoCliente contacto = getContactoClienteSafe();
        if (contacto != null) {
            return contacto.getEmail_cli();//
        }
        return null;
    }

    public String getAvenida_cli() {
        EnderecoCliente endereco = getEnderecoClienteSafe();
        if (endereco != null) {
            return endereco.getAvenida_cli();//
        }
        return null;
    }

    public String getBairro_cli() {
        EnderecoCliente endereco = getEnderecoClienteSafe();
        if (endereco != null) {
            return endereco.getBairro_cli();//
        }
        return null;
    }

    public String getCidade_cli() {
        EnderecoCliente endereco = getEnderecoClienteSafe();
        if (endereco != null) {
            return endereco.getCidade_cli();//
        }
        return null;
    }

    public String getProvincia_cli() {
        EnderecoCliente endereco = getEnderecoClienteSafe();
        if (endereco != null) {
            return endereco.getProvincia_cli();//
        }
        return null;
    }

    public String getRua_cli() {
        EnderecoCliente endereco = getEnderecoClienteSafe();
        if (endereco != null) {
            return endereco.getRua_cli();//
        }
        return null;
    }

    public int getCodigoPostal_cli() {
        EnderecoCliente endereco = getEnderecoClienteSafe();
        if (endereco != null) {
            return endereco.getCodigoPostal_cli();//
        }
        return -1;
    }

    public int getNumero_cli() {
        EnderecoCliente endereco = getEnderecoClienteSafe();
        if (endereco != null) {
            return endereco.getNumero_cli();//
        }
        return -1;
    }

    public String getNome_banco() {
        ContaBancaria conta = getContaBancariaSafe();
        if (conta != null) {
            return conta.getNome_banco();//
        }
        return null;
    }

    public String getNib() {
        ContaBancaria conta = getContaBancariaSafe();
        if (conta != null) {
            return conta.getNib();//
        }
        return null;
    }

    public String getNuib() {
        ContaBancaria conta = getContaBancariaSafe();
        if (conta != null) {
            return conta.getNuib();//
        }
        return null;
    }

    public String getNumero_conta() {
        ContaBancaria conta = getContaBancariaSafe();
        if (conta != null) {
            return conta.getNumero_conta();//
        }
        return null;
    }

    private EnderecoEmpresa getEnderecoEmpresaSafe() {
        return empresa != null ? empresa.getEnderecoEmpresa() : null;
    }

    private ContactoEmpresa getContactoEmpresaSafe() {
        return empresa != null ? empresa.getContactoEmpresa() : null;
    }

    private ContaBancaria getContaBancariaSafe() {
        return empresa != null ? empresa.getContaBancaria() : null;
    }

    private ContactoCliente getContactoClienteSafe() {
        return cliente != null ? cliente.getContactoCliente() : null;
    }

    private EnderecoCliente getEnderecoClienteSafe() {
        return cliente != null ? cliente.getEnderecoCliente() : null;
    }

}
