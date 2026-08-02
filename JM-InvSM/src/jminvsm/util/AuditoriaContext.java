package jminvsm.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jminvsm.util.SessaoAplicacao;
import jminvsm.model.usuario.Usuario;

/**
 * Mantem a sessao do banco alinhada com o usuario autenticado na aplicacao.
 */
public final class AuditoriaContext {

    private AuditoriaContext() {
    }

    public static Integer getUsuarioActualId() {
        Usuario usuario = SessaoAplicacao.getUserData();
        return usuario != null && usuario.getId() > 0 ? usuario.getId() : null;
    }

    public static void aplicarUsuarioSessao(Connection con) throws SQLException {
        Integer idUsuario = getUsuarioActualId();
        try (PreparedStatement ps = con.prepareStatement("SET @app_user_id = ?")) {
            if (idUsuario == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, idUsuario);
            }
            ps.execute();
        }
    }
}

