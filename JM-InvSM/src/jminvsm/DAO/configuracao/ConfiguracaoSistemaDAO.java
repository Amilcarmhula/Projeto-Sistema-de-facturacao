package jminvsm.dao.configuracao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jminvsm.model.configuracao.ConfiguracaoSistema;
import jminvsm.model.usuario.Usuario;
import jminvsm.dao.DaoErrorHandler;
import jminvsm.util.Conexao;

public class ConfiguracaoSistemaDAO {

    public ConfiguracaoSistema getConfiguracaoActual() {
        String sql = "SELECT id, modoOperacao, controlarStockGlobal, permitirVendaSemStock, observacao, "
                + "fk_id_usuario, dataCriacao, dataActualizacao, deleteStatus "
                + "FROM configuracao_sistema "
                + "WHERE id = 1 AND deleteStatus = 0";
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapConfiguracao(rs);
            }
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de configuracao", "Erro ao consultar configuracao do sistema: " + ex);
        }
        return configuracaoPadrao();
    }

    public boolean updateConfiguracao(ConfiguracaoSistema configuracao) {
        String sql = "UPDATE configuracao_sistema SET modoOperacao=?, controlarStockGlobal=?, "
                + "permitirVendaSemStock=?, observacao=?, fk_id_usuario=? "
                + "WHERE id=1 AND deleteStatus=0";
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, configuracao.getModoOperacao());
            ps.setBoolean(2, configuracao.isControlarStockGlobal());
            ps.setBoolean(3, configuracao.isPermitirVendaSemStock());
            ps.setString(4, configuracao.getObservacao());
            if (configuracao.getUsuario() == null || configuracao.getUsuario().getId() <= 0) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, configuracao.getUsuario().getId());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            DaoErrorHandler.log("Erro de configuracao", "Erro ao actualizar configuracao do sistema: " + ex);
        }
        return false;
    }

    private ConfiguracaoSistema mapConfiguracao(ResultSet rs) throws SQLException {
        ConfiguracaoSistema configuracao = new ConfiguracaoSistema();
        configuracao.setId(rs.getInt("id"));
        configuracao.setModoOperacao(rs.getString("modoOperacao"));
        configuracao.setControlarStockGlobal(rs.getBoolean("controlarStockGlobal"));
        configuracao.setPermitirVendaSemStock(rs.getBoolean("permitirVendaSemStock"));
        configuracao.setObservacao(rs.getString("observacao"));
        configuracao.setDataCriacao(rs.getString("dataCriacao"));
        configuracao.setDataActualizacao(rs.getString("dataActualizacao"));
        configuracao.setDeleteStatus(rs.getBoolean("deleteStatus"));
        int idUsuario = rs.getInt("fk_id_usuario");
        if (!rs.wasNull()) {
            Usuario usuario = new Usuario();
            usuario.setId(idUsuario);
            configuracao.setUsuario(usuario);
        }
        return configuracao;
    }

    private ConfiguracaoSistema configuracaoPadrao() {
        ConfiguracaoSistema configuracao = new ConfiguracaoSistema();
        configuracao.setId(1);
        configuracao.setModoOperacao("FACTURACAO_STOCK");
        configuracao.setControlarStockGlobal(true);
        configuracao.setPermitirVendaSemStock(false);
        configuracao.setObservacao("Configuracao padrao em memoria");
        return configuracao;
    }
}
