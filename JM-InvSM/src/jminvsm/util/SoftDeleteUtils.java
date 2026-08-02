package jminvsm.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

public final class SoftDeleteUtils {

    private static final Pattern IDENTIFICADOR_SEGURO = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    private SoftDeleteUtils() {
    }

    public static boolean softDelete(String tabela, int id) throws SQLException {
        try (Connection con = Conexao.getConexao()) {
            return softDelete(con, tabela, "id", String.valueOf(id));
        }
    }

    public static boolean softDelete(Connection con, String tabela, int id) throws SQLException {
        return softDelete(con, tabela, "id", String.valueOf(id));
    }

    public static boolean softDelete(Connection con, String tabela, String colunaId, Object valorId) throws SQLException {
        validarIdentificador(tabela, "Tabela invalida para delete logico.");
        validarIdentificador(colunaId, "Coluna de identificacao invalida para delete logico.");

        AuditoriaContext.aplicarUsuarioSessao(con);
        String sql = "UPDATE `" + tabela + "` "
                + "SET deleteStatus = 1, deletedAt = NOW(), deletedBy = ? "
                + "WHERE `" + colunaId + "` = ? AND deleteStatus = 0";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            Integer idUsuario = AuditoriaContext.getUsuarioActualId();
            if (idUsuario == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, idUsuario);
            }
            ps.setObject(2, valorId);
            return ps.executeUpdate() > 0;
        }
    }

    private static void validarIdentificador(String valor, String mensagem) throws SQLException {
        if (valor == null || !IDENTIFICADOR_SEGURO.matcher(valor).matches()) {
            throw new SQLException(mensagem);
        }
    }
}
