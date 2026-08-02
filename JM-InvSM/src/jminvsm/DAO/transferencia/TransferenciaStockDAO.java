package jminvsm.dao.transferencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.produto.Produto;
import jminvsm.model.transferencia.ItemTransferencia;
import jminvsm.model.transferencia.TransferenciaStock;
import jminvsm.util.Conexao;

public class TransferenciaStockDAO {

    public boolean transferirProduto(int idProduto, int idArmazemOrigem, int idArmazemDestino, int quantidade) {
        TransferenciaStock transferencia = new TransferenciaStock();
        transferencia.setArmazemOrigem(refArmazem(idArmazemOrigem, null));
        transferencia.setArmazemDestino(refArmazem(idArmazemDestino, null));
        transferencia.setEstado("Pendente");
        ItemTransferencia item = new ItemTransferencia();
        item.setProduto(refProduto(idProduto, null));
        item.setQuantidade(quantidade);
        transferencia.setItens(FXCollections.observableArrayList(item));
        if (!criarRequisicaoTransferencia(transferencia)) {
            return false;
        }
        int idTransferencia = transferencia.getId();
        return aprovarTransferencia(idTransferencia) && enviarTransferencia(idTransferencia) && receberTransferencia(idTransferencia);
    }

    public boolean criarRequisicaoTransferencia(TransferenciaStock transferencia) {
        String sqlTrans = "INSERT INTO transferencia_stock (idArmazemOrigem,idArmazemDestino,estado) VALUES (?,?,?)";
        String sqlItem = "INSERT INTO item_transferencia (idTransferencia,idProduto,quantidade,quantidadeRecebida) VALUES (?,?,?,?)";
        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sqlTrans, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, transferencia.getArmazemOrigem().getId());
                ps.setInt(2, transferencia.getArmazemDestino().getId());
                ps.setString(3, transferencia.getEstado() == null ? "Pendente" : transferencia.getEstado());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        transferencia.setId(keys.getInt(1));
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                if (transferencia.getItens() != null) {
                    for (ItemTransferencia item : transferencia.getItens()) {
                        ps.setInt(1, transferencia.getId());
                        ps.setInt(2, item.getProduto().getId());
                        ps.setInt(3, item.getQuantidade());
                        ps.setInt(4, item.getQuantidadeRecebida());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            con.commit();
            return true;
        } catch (SQLException ex) {
            rollback(con);
            DaoErrorHandler.log("Erro persistencia", "Erro ao criar requisicao de transferencia: " + ex.getMessage());
            return false;
        } finally {
            close(con);
        }
    }

    public ObservableList<TransferenciaStock> listarTransferencias(String estado) {
        ObservableList<TransferenciaStock> lista = FXCollections.observableArrayList();
        String sql = "SELECT ts.*, ao.nome AS origemNome, ad.nome AS destinoNome FROM transferencia_stock ts "
                + "LEFT JOIN armazem ao ON ao.id=ts.idArmazemOrigem "
                + "LEFT JOIN armazem ad ON ad.id=ts.idArmazemDestino "
                + "WHERE (? IS NULL OR ?='Todos' OR ts.estado=?) ORDER BY ts.id DESC";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setString(2, estado);
            ps.setString(3, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapTransferencia(rs));
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar transferencias: " + ex.getMessage());
        }
        return lista;
    }

    public TransferenciaStock getTransferencia(int idTransferencia) {
        String sql = "SELECT ts.*, ao.nome AS origemNome, ad.nome AS destinoNome FROM transferencia_stock ts "
                + "LEFT JOIN armazem ao ON ao.id=ts.idArmazemOrigem "
                + "LEFT JOIN armazem ad ON ad.id=ts.idArmazemDestino WHERE ts.id=?";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTransferencia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TransferenciaStock transferencia = mapTransferencia(rs);
                    transferencia.setItens(getItensTransferencia(idTransferencia));
                    return transferencia;
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao consultar transferencia: " + ex.getMessage());
        }
        return null;
    }

    public ObservableList<ItemTransferencia> getItensTransferencia(int idTransferencia) {
        ObservableList<ItemTransferencia> lista = FXCollections.observableArrayList();
        String sql = "SELECT it.*, p.nome AS produtoNome FROM item_transferencia it "
                + "LEFT JOIN produto p ON p.id=it.idProduto WHERE it.idTransferencia=? ORDER BY it.id";
        try (Connection con = Conexao.getConexao(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTransferencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ItemTransferencia item = new ItemTransferencia();
                    item.setId(rs.getInt("id"));
                    TransferenciaStock transferencia = new TransferenciaStock();
                    transferencia.setId(rs.getInt("idTransferencia"));
                    item.setTransferencia(transferencia);
                    item.setProduto(refProduto(rs.getInt("idProduto"), rs.getString("produtoNome")));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setQuantidadeRecebida(rs.getInt("quantidadeRecebida"));
                    lista.add(item);
                }
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de consulta", "Erro ao listar itens da transferencia: " + ex.getMessage());
        }
        return lista;
    }

    public boolean aprovarTransferencia(int idTransferencia) {
        return updateEstado(idTransferencia, "Reservado");
    }

    public boolean enviarTransferencia(int idTransferencia) {
        return callProcedure("EnviarTransferencia", idTransferencia);
    }

    public boolean receberTransferencia(int idTransferencia) {
        return callProcedure("ReceberTransferencia", idTransferencia);
    }

    public boolean cancelarTransferencia(int idTransferencia) {
        return updateEstado(idTransferencia, "Cancelado");
    }

    private boolean updateEstado(int idTransferencia, String estado) {
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement("UPDATE transferencia_stock SET estado=? WHERE id=?")) {
            ps.setString(1, estado);
            ps.setInt(2, idTransferencia);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro persistencia", "Erro ao atualizar transferencia: " + ex.getMessage());
            return false;
        }
    }

    private boolean callProcedure(String name, int idTransferencia) {
        try (Connection con = Conexao.getConexao(); CallableStatement cs = con.prepareCall("{CALL " + name + "(?)}")) {
            cs.setInt(1, idTransferencia);
            cs.execute();
            return true;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro processamento", "Erro ao executar " + name + ": " + ex.getMessage());
            return false;
        }
    }

    private TransferenciaStock mapTransferencia(ResultSet rs) throws SQLException {
        TransferenciaStock transferencia = new TransferenciaStock();
        transferencia.setId(rs.getInt("id"));
        transferencia.setArmazemOrigem(refArmazem(rs.getInt("idArmazemOrigem"), rs.getString("origemNome")));
        transferencia.setArmazemDestino(refArmazem(rs.getInt("idArmazemDestino"), rs.getString("destinoNome")));
        transferencia.setEstado(rs.getString("estado"));
        transferencia.setDataCriacao(rs.getString("dataCriacao"));
        return transferencia;
    }

    private Armazem refArmazem(int id, String nome) {
        Armazem armazem = new Armazem();
        armazem.setId(id);
        armazem.setNome_arm(nome);
        return armazem;
    }

    private Produto refProduto(int id, String nome) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        return produto;
    }

    private void rollback(Connection con) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro transacao", "Erro ao reverter transacao: " + ex.getMessage());
        }
    }

    private void close(Connection con) {
        try {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro conexao", "Erro ao fechar conexao: " + ex.getMessage());
        }
    }
}
