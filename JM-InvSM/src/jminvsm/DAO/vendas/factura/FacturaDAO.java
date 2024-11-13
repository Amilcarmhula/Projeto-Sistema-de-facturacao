/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.dao.vendas.factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.contacto.ContactoCliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.empresa.Empresa;
import jminvsm.model.empresa.conta.ContaBancaria;
import jminvsm.model.empresa.contacto.ContactoEmpresa;
import jminvsm.model.empresa.endereco.EnderecoEmpresa;
import jminvsm.model.metodo_pagamento.MetodoPagamento;
import jminvsm.model.transacao.Transacao;
import jminvsm.model.usuario.Usuario;
import jminvsm.model.vendas.documetos.DocumentosComerciais;
import jminvsm.model.vendas.factura.Factura;
import jminvsm.util.AlertUtilities;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

/**
 *
 * @author JM-Tecnologias
 */
public class FacturaDAO implements FacturaDAOImpl<Factura> {

    private Connection con;
    private PreparedStatement ps;
    private String sql;

    public FacturaDAO() throws SQLException {
        this.con = Conexao.getConexao();
    }

    @Override
    public boolean addEntity(Factura t) {
        boolean isSuccess = false;
        try {
            sql = "insert into factura (numero,data_emissao,data_vencimento,total,condicaoPagemento,"
                    + "fk_id_empresa,fk_id_cliente,fk_id_usuario,fk_id_doc,saldoPendente)"
                    + " value (?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getNumero_fac());
            ps.setString(2, t.getData_emissao());
            ps.setString(3, t.getData_vencimento());
            ps.setDouble(4, t.getTotal());
//            ps.setDouble(5, t.getTotalPago());
            ps.setString(5, t.getCondicaoPagamento());
//            ps.setInt(8, t.getNumeroParcelas());
//            ps.setString(9, t.getEstado());
            ps.setInt(6, t.getEmpresa().getId());
            ps.setInt(7, t.getCliente().getId());
            ps.setInt(8, t.getUsuario().getId());
            ps.setInt(9, t.getDocComerciais().getId());
            ps.setDouble(10, t.getSaldoPendente());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                isSuccess = true; // Se uma linha foi afetada, a inserção foi bem-sucedida
            }

        } catch (SQLException ex) {
            showDialog( "Erro persistencia", "Detalhes: " + ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public Factura getFactura(String numero) {
        Factura factura = null;
        try {
            sql = "SELECT f.numero, f.data_emissao, f.data_vencimento, f.total, f.totalPago, f.saldoPendente, f.estado, "
                    + "f.estado, f.fk_id_empresa, f.fk_id_cliente, f.fk_id_usuario, f.fk_id_doc "
                    + "FROM factura f WHERE f.numero = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, numero);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                factura = new Factura();
                factura.setNumero_fac(result.getString("numero"));
                factura.setData_emissao(result.getString("data_emissao"));
                factura.setData_vencimento(result.getString("data_vencimento"));
                factura.setTotal(result.getDouble("total"));
                factura.setTotalPago(result.getDouble("totalPago"));
                factura.setSaldoPendente(result.getDouble("saldoPendente"));
                factura.setEstado(result.getString("estado"));
                DocumentosComerciais d = new DocumentosComerciais();
                d.setId(result.getInt("fk_id_doc"));
                factura.setDocComerciais(d);
                Cliente c = new Cliente();
                c.setId(result.getInt("fk_id_cliente"));
                factura.setCliente(c);
                Empresa e = new Empresa();
                e.setId(result.getInt("fk_id_empresa"));
                factura.setEmpresa(e);
                Usuario u = new Usuario();
                u.setId(result.getInt("fk_id_usuario"));
                factura.setUsuario(u);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Erro: "+e);
            showDialog( "Erro de consulta", "Detalhes: " + e);
        }
        return factura;
    }

    public Factura getFacturaData(String factura) {
        Factura f = null;
        try {
            sql = "SELECT f.numero AS numero_factura, f.data_emissao, f.data_vencimento, "
                    + "f.total, f.totalPago, f.saldoPendente, f.estado, f.condicaoPagemento,"
                    + "sum(t.trocos) as trocosTrans, sum(t.valorRecebido) as valorTrans, "
                    + "GROUP_CONCAT(DISTINCT m.nome SEPARATOR '; ') AS metodoPagamento,"
                    + "doc.sigla, doc.nome as documento,"
                    + "cli.razaosocial, cli.nuit as nuit_cliente,"
                    + "ec.avenida,ec.rua,ec.numero,ec.bairro,ec.cidade,ec.provincia,ec.codigo_postal,"
                    + "cc.contacto AS contatos_cliente,cc.email as email_cliente, "
                    + "e.nome as nome_empresa, e.razaosocial AS razao_empresa, e.nuit as nuit_empresa, "
                    + "en.avenida as avenida_e,en.rua as rua_e,en.numero as numero_e,"
                    + "en.bairro as bairro_e,en.cidade as cidade_e,en.provincia as provincia_e,en.codigo_postal as codigo_postal_e,"
                    + "ce.contacto AS contatos_empresa, "
                    + "ce.email as email_empresa, ce.website,"
                    + "a.nome_banco, a.numero as numeroConta, a.nib, a.nuib "
                    + "FROM factura f "
                    + "LEFT JOIN transacoes t ON t.fk_numero = f.numero " 
                    + "LEFT JOIN metodo_pagamento m ON m.id = t.fk_id_metodo_pagamento "
                    + "LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id "
                    + "LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente "
                    + "LEFT JOIN contacto_cliente cc ON cc.fk_id_cliente = cli.id "
                    + "LEFT JOIN endereco_cliente ec ON ec.fk_id_cliente = cli.id "
                    + "LEFT JOIN empresa e ON f.fk_id_empresa=e.id "
                    + "LEFT JOIN endereco_empresa en ON e.id = en.fk_id_empresa "
                    + "LEFT JOIN contacto_empresa ce ON e.id = ce.fk_id_empresa "
                    + "LEFT JOIN contabancaria a ON e.id=a.fk_id_empresa "
                    + "WHERE f.numero = ? ";
            ps = con.prepareStatement(sql);
            ps.setString(1, factura);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                f = new Factura();
                f.setNumero_fac(result.getString("numero_factura"));
                f.setData_emissao(result.getString("data_emissao"));
                f.setData_vencimento(result.getString("data_vencimento"));
                f.setTotal(result.getDouble("total"));
                f.setTotalPago(result.getDouble("totalPago"));
                f.setSaldoPendente(result.getDouble("saldoPendente"));
                f.setEstado(result.getString("estado"));
                f.setCondicaoPagamento(result.getString("condicaoPagemento"));
                MetodoPagamento mt = new MetodoPagamento();
                mt.setNomePagamento(result.getString("metodoPagamento"));
                Transacao t = new Transacao();
                t.setValorRecebido(result.getDouble("valorTrans"));
                t.setTrocos(result.getDouble("trocosTrans"));
                t.setMetodo(mt);
                f.setTransacoes(t);
                DocumentosComerciais d = new DocumentosComerciais();
                d.setNome_doc(result.getString("documento"));
                d.setSigla_doc(result.getString("sigla"));
                f.setDocComerciais(d);
                Cliente c = new Cliente();
                c.setRazao_cli(result.getString("razaosocial"));
                c.setNuit_cli(result.getInt("nuit_cliente"));
                EnderecoCliente ec = new EnderecoCliente();
                ec.setAvenida_cli(result.getString("avenida"));
                ec.setRua_cli(result.getString("rua"));
                ec.setNumero_cli(result.getInt("numero"));
                ec.setBairro_cli(result.getString("bairro"));
                ec.setCidade_cli(result.getString("cidade"));
                ec.setProvincia_cli(result.getString("provincia"));
                ec.setCodigoPostal_cli(result.getInt("codigo_postal"));
                c.setEnderecoCliente(ec);
                ContactoCliente cc = new ContactoCliente();
                cc.setContacto_cli(result.getString("contatos_cliente"));
                cc.setEmail_cli(result.getString("email_cliente"));
                c.setContactoCliente(cc);
                f.setCliente(c);
                Empresa e = new Empresa();
                e.setNome_emp(result.getString("nome_empresa"));
                e.setRazao_emp(result.getString("razao_empresa"));
                e.setNuit_emp(result.getInt("nuit_empresa"));
                EnderecoEmpresa ee = new EnderecoEmpresa();
                ee.setAvenida_emp(result.getString("avenida_e"));
                ee.setRua_emp(result.getString("rua_e"));
                ee.setNumero_emp(result.getInt("numero_e"));
                ee.setBairro_emp(result.getString("bairro_e"));
                ee.setCidade_emp(result.getString("cidade_e"));
                ee.setProvincia_emp(result.getString("provincia_e"));
                ee.setCodigoPostal_emp(result.getInt("codigo_postal_e"));
                e.setEnderecoEmpresa(ee);
                ContactoEmpresa ce = new ContactoEmpresa();
                ce.setContacto_emp(result.getString("contatos_empresa"));
                ce.setEmail_emp(result.getString("email_empresa"));
                ce.setWebsite_emp(result.getString("website"));
                e.setContactoEmpresa(ce);
                ContaBancaria cb = new ContaBancaria();
                cb.setNome_banco(result.getString("nome_banco"));
                cb.setNumero_conta(result.getString("numeroConta"));
                cb.setNib(result.getString("nib"));
                cb.setNuib(result.getString("nuib"));
                e.setContaBancaria(cb);
                f.setEmpresa(e);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            showDialog( "Erro persistencia", "Detalhes: " + ex);
        }
        return f;
    }

    public ObservableList<Factura> getFacturaToDashboard() {
        ObservableList<Factura> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT f.numero AS numero_factura, f.data_emissao, f.data_vencimento, "
                    + "f.estado, f.total, f.totalPago, f.saldoPendente, "
                    + "concat(doc.sigla,' - ', doc.nome) as Documento, "
                    + "concat(cli.id,' - ', cli.razaosocial,'/ ', cli.nuit) as cliente "
                    + "FROM factura f "
                    + "LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id "
                    + "LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente "
                    + "LEFT JOIN itemfactura i ON i.fk_numero = f.numero "
                    + "GROUP BY f.numero, f.data_emissao, f.data_vencimento, "
                    + "f.estado, f.total, f.totalPago, f.saldoPendente,"
                    + "doc.sigla, doc.nome, cli.id, cli.razaosocial, cli.nuit "
                    + "ORDER BY f.numero";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Factura f = new Factura();
                f.setNumero_fac(result.getString("numero_factura"));
                f.setData_emissao(result.getString("data_emissao"));
                f.setData_vencimento(result.getString("data_vencimento"));
                f.setEstado(result.getString("estado"));
                f.setTotal(result.getDouble("total"));
                f.setTotalPago(result.getDouble("totalPago"));
                f.setSaldoPendente(result.getDouble("saldoPendente"));
                DocumentosComerciais d = new DocumentosComerciais();
                d.setNome_doc(result.getString("Documento"));
                f.setDocComerciais(d);
                Cliente c = new Cliente();
                c.setRazao_cli(result.getString("cliente"));
                f.setCliente(c);
                lista.add(f);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            showDialog( "Erro de consulta", "Detalhes: " + e);
        }
        return lista;
    }

    public ObservableList<Factura> getFacturaToPagamentos(int id) {
        ObservableList<Factura> lista = FXCollections.observableArrayList();
        try {
            sql = "SELECT f.numero AS numero_factura, f.data_emissao, f.data_vencimento, "
                    + "f.estado, f.total, f.totalPago, f.saldoPendente, f.numeroParcelas, f.condicaoPagemento, "
                    + "CONCAT(doc.sigla, ' - ', doc.nome) AS Documento "
                    + "FROM factura f "
                    + "LEFT JOIN documentocomercial doc ON f.fk_id_doc = doc.id "
                    + "LEFT JOIN cliente cli ON cli.id = f.fk_id_cliente "
                    + "WHERE cli.id = ? "
                    + "GROUP BY f.numero, f.data_emissao, f.data_vencimento, f.estado,"
                    + "f.total, f.totalPago, f.saldoPendente, f.numeroParcelas, doc.sigla, doc.nome "
                    + "ORDER BY f.numero";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                Factura f = new Factura();
                f.setNumero_fac(result.getString("numero_factura"));
                f.setData_emissao(result.getString("data_emissao"));
                f.setData_vencimento(result.getString("data_vencimento"));
                f.setEstado(result.getString("estado"));
                f.setTotal(result.getDouble("total"));
                f.setTotalPago(result.getDouble("totalPago"));
                f.setSaldoPendente(result.getDouble("saldoPendente"));
                f.setNumeroParcelas(result.getInt("numeroParcelas"));
                f.setCondicaoPagamento(result.getString("condicaoPagemento"));
                DocumentosComerciais d = new DocumentosComerciais();
                d.setNome_doc(result.getString("Documento"));
                f.setDocComerciais(d);
                lista.add(f);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            AlertUtilities.showDialog( "Erro de consulta", "Detalhes: " + e);
        }
        return lista;
    }

    @Override
    public int countFactura() {
        int total = 0;
        try {
            sql = "select count(*) as total from factura";
            ps = con.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            if (result.next()) { // Verifica se há resultados
                total = result.getInt("total");
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            AlertUtilities.showDialog( "Erro de contagem", "Detalhes: " + ex);
        }

        return total;
    }

//    @Override
//    public double totalPaidInvoice() {
//        double total = 0;
//        try {
//            sql = "SELECT SUM(i.subtotal) AS total_fatura FROM factura f "
//                    + "LEFT JOIN itemfactura i ON i.fk_numero = f.numero "
//                    + "where f.estado=1";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            if (result.next()) { // Verifica se há resultados
//                total = result.getInt("total_fatura");
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            AlertUtilities.showDialog(FontAwesomeIcon.TIMES, "Erro de somatorio de facturas", "Detalhes: " + ex);
//        }
//
//        return total;
//    }
    /*
    Metodo abaixo obsoleto
     */
//    public List<itensFacturaData> getItensFacturaData(String factura) {
//        List<itensFacturaData> lista = new ArrayList<>();
//        itensFacturaData c = null;
//        try {
//            sql = "SELECT "
//                    + "p.nome, "
//                    + "p.descricao, "
//                    + "i.quantidade, "
//                    + "pv.preco, "
//                    + "ip.identificador, "
//                    + "ip.percentagem AS taxa_imposto, "
//                    + "d.nome AS titulo_desconto, "
//                    + "d.percentagem AS percentagem_desconto,"
//                    + "i.subtotal "
//                    + "FROM factura f "
//                    + "LEFT JOIN itemfactura i ON i.fk_numero = f.numero "
//                    + "LEFT JOIN desconto d ON d.id = i.fk_id_desconto "
//                    + "LEFT JOIN produto p ON p.id = i.fk_id_produto "
//                    + "LEFT JOIN precovenda pv ON pv.fk_id_produto = p.id "
//                    + "LEFT JOIN taxa_imposto ip ON ip.id = p.fk_id_taxa_imposto "
//                    + "WHERE f.numero = ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, factura);
//            ResultSet result = ps.executeQuery();
//            double sub = 0;
//            while (result.next()) {
//                c = new itensFacturaData();
//                c.setNomeProduto(result.getString("nome"));
//                c.setDescricaoProduto(result.getString("descricao"));
//                c.setQuantidadeProduto(result.getInt("quantidade"));
//                c.setPrecoProduto(result.getDouble("preco"));
//                c.setTaxaProduto(result.getDouble("taxa_imposto"));
//                c.setDescontoProduto(result.getDouble("percentagem_desconto"));
//                c.setTotaProduto(result.getDouble("subtotal"));
//                sub += c.getQuantidadeProduto() * c.getPrecoProduto();
//                c.setSubtotal(sub);
//                lista.add(c);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Falha ao Bucar cliente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
//
//        }
//        return lista;
//    }
//    public itensFacturaData getTotaisFacturaData(String factura) {
//        itensFacturaData c = null;
//        try {
//            sql = "SELECT "
//                    + "sum(ii.quantidade * pv2.preco) as subtotal,"
//                    + "sum((((ii.quantidade * pv2.preco) * ip2.percentagem)+ii.quantidade * pv2.preco) * dd.percentagem) AS desconto,"
//                    + "sum(ii.quantidade * pv2.preco * ip2.percentagem) AS taxas,"
//                    + "sum(ii.subtotal) as total_geral "
//                    + "FROM factura ff "
//                    + "    LEFT JOIN itemfactura ii ON ii.fk_numero = ff.numero "
//                    + "    LEFT JOIN desconto dd ON dd.id = ii.fk_id_desconto "
//                    + "    LEFT JOIN produto pp ON pp.id = ii.fk_id_produto "
//                    + "    LEFT JOIN precovenda pv2 ON pv2.fk_id_produto = pp.id "
//                    + "    LEFT JOIN taxa_imposto ip2 ON ip2.id = pp.fk_id_taxa_imposto "
//                    + "WHERE ff.numero = ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, factura);
//            ResultSet result = ps.executeQuery();
//            double sub = 0;
//            while (result.next()) {
//                c = new itensFacturaData();
//                c.setSubtotal(result.getDouble("subtotal"));
//                c.setDesconto(result.getDouble("desconto"));
//                c.setTaxas(result.getDouble("taxas"));
//                c.setTotal_geral(result.getDouble("total_geral"));
//
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Falha ao Bucar cliente: " + e, "Informação", JOptionPane.ERROR_MESSAGE);
//
//        }
//        return c;
//    }
//
//    @Override
//    public void updateEntityByID(Factura t, int id) {
//        try {
//            sql = "update factura set tipo=?, fk_id_cliente=?"
//                    + " where id=?";
//            ps = con.prepareStatement(sql);
//
//             ps.setString(1, t.getTipo());
//            ps.setInt(2, t.getFk_id_agente());
//            ps.setInt(3, id);
//
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Falha ao actulizar  Factura: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    @Override
//    public List<Factura> listAllEntities() {
//        List<Factura> lista = new ArrayList<>();
//        try {
//            sql = "select * from factura";
//            ps = con.prepareStatement(sql);
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                Factura u = new Factura();
//                u.setId(result.getInt("id"));
//                u.setTipo(result.getString("tipo"));
//                u.setNumero(result.getLong("numero"));
//                u.setData_criacao(result.getString("data_criacao"));
//                u.setData_vencimento(result.getString("data_vencimento"));
//                u.setEstado(result.getBoolean("estado"));
//                u.setTotal(result.getDouble("total"));
//                u.setFk_id_cliente(result.getInt("fk_id_cliente"));
//                u.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
//                u.setActive_or_not(result.getBoolean("active_or_not"));
//                lista.add(u);
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao Buscar Factura: " + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return lista;
//    }
//
//    @Override
//    public Factura searchEntityByName(String number) {
//        Factura u = null;
//        try {
//            sql = "select * from factura where numero like ?";
//            ps = con.prepareStatement(sql);
//            ps.setString(1, "%" + number + "%");
//            ResultSet result = ps.executeQuery();
//            while (result.next()) {
//                u = new Factura();
//                u.setId(result.getInt("id"));
//                u.setTipo(result.getString("tipo"));
//                u.setNumero(result.getLong("numero"));
//                u.setData_criacao(result.getString("data_criacao"));
//                u.setData_vencimento(result.getString("data_vencimento"));
//                u.setEstado(result.getBoolean("estado"));
//                u.setTotal(result.getDouble("total"));
//                u.setFk_id_cliente(result.getInt("fk_id_cliente"));
//                u.setFk_id_empresa(result.getInt("fk_id_empresa"));
//                u.setFk_id_usuario(result.getInt("fk_id_usuario"));
//                u.setActive_or_not(result.getBoolean("active_or_not"));
//            }
//            result.close();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao pesquisar Factura: " + ex,
//                    "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//        return u;
//
//    }
//
//    @Override
//    public void deleteEntityByROOT(int id) {
//        try {
//            sql = "delete from factura where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Factura: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//    
//    @Override
//    public void deleteEntityByUSER(int id) {
//        try {
//            sql =  "update factura set active_or_not=false where id=?";
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Erro ao deletar Factura: "
//                    + ex, "Informação", JOptionPane.ERROR_MESSAGE);
//        }
//    }
}
