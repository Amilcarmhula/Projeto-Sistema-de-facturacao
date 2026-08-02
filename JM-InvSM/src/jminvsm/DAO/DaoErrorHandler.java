package jminvsm.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Regista erros tecnicos da camada DAO sem acoplar persistencia a JavaFX/Swing.
 */
public final class DaoErrorHandler {

    private static final Logger LOGGER = Logger.getLogger(DaoErrorHandler.class.getName());

    private DaoErrorHandler() {
    }

    public static void log(String title, String message) {
        LOGGER.log(Level.WARNING, "{0}: {1}", new Object[]{title, message});
    }
}
