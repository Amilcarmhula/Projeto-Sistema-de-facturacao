package jminvsm.dao.auditoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.model.auditoria.AuditoriaRegisto;
import static jminvsm.util.AlertUtilities.showDialog;
import jminvsm.util.Conexao;

public class AuditoriaRegistoDAO {

    public ObservableList<AuditoriaRegisto> pesquisar(String dataInicio, String dataFim,
            String tabela, String operacao, Integer idUsuario, String idRegisto) {
        ObservableList<AuditoriaRegisto> lista = FXCollections.observableArrayList();
        String sql = "SELECT ar.id, ar.fk_id_usuario, u.nome AS usuarioNome, ar.tabela, ar.id_registo, "
                + "ar.operacao, ar.colunas_alteradas, ar.valores_anteriores, ar.valores_novos, "
                + "ar.data_operacao, ar.origem "
                + "FROM auditoria_registo ar "
                + "LEFT JOIN usuario u ON u.id = ar.fk_id_usuario "
                + "WHERE (? IS NULL OR ar.data_operacao >= CONCAT(?, ' 00:00:00')) "
                + "AND (? IS NULL OR ar.data_operacao <= CONCAT(?, ' 23:59:59')) "
                + "AND (? IS NULL OR ar.tabela = ?) "
                + "AND (? IS NULL OR ar.operacao = ?) "
                + "AND (? IS NULL OR ar.fk_id_usuario = ?) "
                + "AND (? IS NULL OR ar.id_registo LIKE ?) "
                + "ORDER BY ar.data_operacao DESC, ar.id DESC "
                + "LIMIT 1000";
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql)) {
            setNullableString(ps, 1, dataInicio);
            setNullableString(ps, 2, dataInicio);
            setNullableString(ps, 3, dataFim);
            setNullableString(ps, 4, dataFim);
            setNullableString(ps, 5, tabela);
            setNullableString(ps, 6, tabela);
            setNullableString(ps, 7, operacao);
            setNullableString(ps, 8, operacao);
            setNullableInt(ps, 9, idUsuario);
            setNullableInt(ps, 10, idUsuario);
            setNullableString(ps, 11, idRegisto);
            setNullableString(ps, 12, idRegisto == null || idRegisto.trim().isEmpty()
                    ? null : "%" + idRegisto.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRegisto(rs));
                }
            }
        } catch (SQLException ex) {
            showDialog("Erro de auditoria", "Erro ao pesquisar auditoria: " + ex);
        }
        return lista;
    }

    public ObservableList<String> listarTabelasAuditadas() {
        ObservableList<String> lista = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT tabela FROM auditoria_registo ORDER BY tabela";
        try (Connection con = Conexao.getConexao();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("tabela"));
            }
        } catch (SQLException ex) {
            showDialog("Erro de auditoria", "Erro ao listar tabelas auditadas: " + ex);
        }
        return lista;
    }

    private AuditoriaRegisto mapRegisto(ResultSet rs) throws SQLException {
        AuditoriaRegisto registo = new AuditoriaRegisto();
        registo.setId(rs.getLong("id"));
        int idUsuario = rs.getInt("fk_id_usuario");
        if (!rs.wasNull()) {
            registo.setIdUsuario(idUsuario);
        }
        registo.setUsuarioNome(rs.getString("usuarioNome"));
        registo.setTabela(rs.getString("tabela"));
        registo.setIdRegisto(rs.getString("id_registo"));
        registo.setOperacao(rs.getString("operacao"));
        registo.setColunasAlteradas(rs.getString("colunas_alteradas"));
        registo.setValoresAnteriores(rs.getString("valores_anteriores"));
        registo.setValoresNovos(rs.getString("valores_novos"));
        registo.setDataOperacao(rs.getString("data_operacao"));
        registo.setOrigem(rs.getString("origem"));
        return registo;
    }

    private void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty() || "Todos".equalsIgnoreCase(value.trim())) {
            ps.setNull(index, java.sql.Types.VARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null || value <= 0) {
            ps.setNull(index, java.sql.Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }
}
